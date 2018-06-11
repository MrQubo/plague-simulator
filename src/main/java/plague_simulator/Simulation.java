package plague_simulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import plague_simulator.Config;
import plague_simulator.Global;
import plague_simulator.simulation.BaseSimulationRunner;
import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.Infection;
import plague_simulator.simulation.NormalAgent;
import plague_simulator.simulation.SocialAgent;

import static plague_simulator.utils.RandomUtils.chooseWithProbability;
import static plague_simulator.utils.RandomUtils.nextInt;

public class Simulation {
  static private final String MY_DEFAULTS_FILE_NAME    = "my-defaults.properties";
  static private final String DEFAULTS_FILE_NAME       = "default.properties";
  static private final String DEFAULT_CONFIG_FILE_NAME = "simulation-conf.xml";

  static private final @Getter(lazy = true) Validator validator    = createValidator();
  static private final @Getter(lazy = true) JavaPropsMapper mapper = new JavaPropsMapper();


  static public void main(String args[]) {
    // Hibernate displays unwanted info messages on start.
    setLoggingLevel(Level.WARNING);
    setLocale(new Locale("pl", "PL"));

    CommandLine cmd = parseCLI(args);

    Path myDefaultsFilePath = loadPath(MY_DEFAULTS_FILE_NAME);
    Path defaultsFilePath   = loadPath(DEFAULTS_FILE_NAME);
    Path configFilePath     = loadPath(cmd.getOptionValue("config", DEFAULT_CONFIG_FILE_NAME));

    loadGlobalConfig(myDefaultsFilePath, defaultsFilePath, configFilePath);

    var infection = new Infection(
      "The Anime", // Very dreadful infection.
      Global.getConfigInstance().getInfectivity(),
      Global.getConfigInstance().getRecoveryProbability(),
      Global.getConfigInstance().getImmunityProbability(),
      Global.getConfigInstance().getLethality()
    );

    final var simulationRunner = simulationRunnerFactory(infection);
    simulationRunner.run();

    outputReport(simulationRunner);
  }


  // Parses CLI arguments.
  static private CommandLine parseCLI(String args[]) {
    final var options = new Options();

    options.addOption(
      Option.builder("c")
        .longOpt("config")
        .desc(String.format("config file [%s]", DEFAULT_CONFIG_FILE_NAME))
        .hasArg()
        .argName("FILE")
        .build()
    );

    final var parser = new DefaultParser();

    try {
      return parser.parse(options, args);
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      System.err.println();

      final var formatter = new HelpFormatter();
      final var printWriter = new PrintWriter(System.err);

      formatter.printHelp(
        printWriter,
        HelpFormatter.DEFAULT_WIDTH,
        "plague-simulator",
        String.format("%n"), // Currently doesn't work: https://issues.apache.org/jira/browse/CLI-287.
        options,
        HelpFormatter.DEFAULT_LEFT_PAD,
        HelpFormatter.DEFAULT_DESC_PAD,
        String.format("%nAuthor: Jakub Nowak"),
        true
      );
      printWriter.flush();

      endProgramWithError();
    }

    return null;
  }


  // Reads, validates and sets Global Config instance.
  static private void loadGlobalConfig(Path myDefaultsFilePath, Path defaultsFilePath, Path configFilePath) {
    Global.getInstance().setConfig(readAndValidateConfig(myDefaultsFilePath, defaultsFilePath, configFilePath));
  }

  // Validates Config from defaults file and from both defaults and config file and returns Config read from bothof sources.
  static private Config readAndValidateConfig(Path myDefaultsFilePath, Path defaultsFilePath, Path configFilePath) {
    var properties = new Properties();

    readAndValidateConfigProperties(properties, myDefaultsFilePath);
    readAndValidateConfigProperties(properties, defaultsFilePath);

    validateConfig(tryParsingProperties(properties));

    readAndValidateConfigXML(properties, configFilePath);

    final var config = tryParsingProperties(properties);
    validateConfig(config, Config.NotNullChecks.class);
    validateConfig(config);
    return config;
  }

  static private void readAndValidateConfigProperties(Properties properties, Path propertiesFilePath) {
    try (Reader reader = createReader(propertiesFilePath, StandardCharsets.UTF_8)) {
      properties.load(reader);
    } catch (NoSuchFileException e) {
      System.err.println(Global.getLocalizedInstance().getMessage("NoSuchFileException.message", propertiesFilePath));
      endProgramWithError();
    } catch (IOException e) {
      endProgramWithError(e);
    } catch (IllegalArgumentException e) {
      System.err.println(Global.getLocalizedInstance().getMessage("malformedUnicodeEscape.message", propertiesFilePath));
      endProgramWithError();
    }
  }

  static private void readAndValidateConfigXML(Properties properties, Path xmlFilePath) {
    try (InputStream in = createInputStream(xmlFilePath)) {
      properties.loadFromXML(in);
    } catch (NoSuchFileException e) {
      System.err.println(Global.getLocalizedInstance().getMessage("NoSuchFileException.message", xmlFilePath));
      endProgramWithError();
    } catch (InvalidPropertiesFormatException e) {
      System.err.println(Global.getLocalizedInstance().getMessage("InvalidPropertiesFormatException.message", xmlFilePath));
      endProgramWithError();
    } catch (UnsupportedEncodingException e) {
      System.err.println(Global.getLocalizedInstance().getMessage("UnsupportedEncodingException.message", xmlFilePath));
      endProgramWithError();
    } catch (IOException e) {
      endProgramWithError(e);
    }
  }


  // Tries parsing Properties to Config, exits on failure
  static private Config tryParsingProperties(Properties properties) {
    try {
      return getMapper().readPropertiesAs(properties, Config.class);
    } catch (InvalidFormatException e) {

      String invalidValue = e.getValue().toString();

      List<JsonMappingException.Reference> errorTrack = e.getPath();
      String propertyName = errorTrack.get(errorTrack.size() - 1).getFieldName();

      System.err.println(Global.getLocalizedInstance().getMessage("InvalidFormatException.message", invalidValue, propertyName));
      endProgramWithError();
    } catch (IOException e) {
      endProgramWithError(e);
    }

    return null;
  }


  // Executes Bean Validation rules.
  static private void validateConfig(final Config config) {
     handleConstraintViolationSet(getValidator().validate(config));
  }
  static private void validateConfig(final Config config, Class<?> constraintGroup) {
     handleConstraintViolationSet(getValidator().validate(config, constraintGroup));
  }

  // Handles ConstraintViolations from Bean Validation.
  static private void handleConstraintViolationSet(Set<? extends ConstraintViolation<? extends Config>> violations) {
    for (var violation : violations) {
      System.err.println(violation.getMessage());
    }

    if (! violations.isEmpty()) {
      endProgramWithError();
    }
  }


  // Outputs report to globally configured report file.
  static private void outputReport(BaseSimulationRunner simulationRunner) {
    try (final PrintStream out = openReportFile()) {
      out.println(Global.getLocalizedInstance().getMessage("report.config"));
      printConfig(out);
      out.println();
      out.println(Global.getLocalizedInstance().getMessage("report.agents"));
      printAgents(simulationRunner, out);
      out.println();
      out.println(Global.getLocalizedInstance().getMessage("report.graph"));
      printGraph(simulationRunner, out);
      out.println();
      out.println(Global.getLocalizedInstance().getMessage("report.phaseSummaries"));
      printPhaseSummaries(simulationRunner, out);
    } catch (IOException e) {
      endProgramWithError(e);
    }
  }

  // Prints config variables.
  @SneakyThrows(NoSuchFieldException.class) // Won't be thrown, if property names are spelled correctly.
  static private void printConfig(PrintStream out) {
    out.printf("%s=%s%n",   Config.getPropertyName("seed"),                   Global.getConfigInstance().getSeed());
    out.printf("%s=%d%n",   Config.getPropertyName("agentCount"),             Global.getConfigInstance().getAgentCount());
    out.printf("%s=%.3e%n", Config.getPropertyName("socialAgentProbability"), Global.getConfigInstance().getSocialAgentProbability());
    out.printf("%s=%.3e%n", Config.getPropertyName("meetingProbability"),     Global.getConfigInstance().getMeetingProbability());
    out.printf("%s=%.3e%n", Config.getPropertyName("infectivity"),            Global.getConfigInstance().getInfectivity());
    out.printf("%s=%.3e%n", Config.getPropertyName("recoveryProbability"),    Global.getConfigInstance().getRecoveryProbability());
    out.printf("%s=%.3e%n", Config.getPropertyName("lethality"),              Global.getConfigInstance().getLethality());
    out.printf("%s=%d%n",   Config.getPropertyName("simulationDuration"),     Global.getConfigInstance().getSimulationDuration());
    out.printf("%s=%d%n",   Config.getPropertyName("averageDegree"),          Global.getConfigInstance().getAverageDegree());
  }

  // Prints agents and their types.
  static private void printAgents(BaseSimulationRunner simulationRunner, PrintStream out) {
    final Set<Integer> patientZeroIds = simulationRunner.getPatientZeroIdsSet();

    simulationRunner.getAllAgents()
      .map(a ->
        a.getId()                                                                              +
        (patientZeroIds.contains(a.getId()) ? "*" : "")                                        +
        " "                                                                                    +
        Global.getLocalizedInstance().getMessage(String.format("agent.%s", a.getTypeString()))
      )
      .forEach(out::println);
  }

  // Prints graph structure.
  static private void printGraph(BaseSimulationRunner simulationRunner, PrintStream out) {
    simulationRunner.getAllAgents()
      // Map agent to stream of itself and neighbours.
      .map(a -> Stream.concat(Stream.of(a), a.getAdj()))
      .map(s -> s.map(IAgent::getId))
      .map(s -> s.map(id -> id.toString()))
      // Join all strings in underlying streams.
      .map(s -> s.collect(Collectors.joining(" ")))
      .forEach(out::println);
  }

  // Prints each phase summary.
  static private void printPhaseSummaries(BaseSimulationRunner simulationRunner, PrintStream out) {
    simulationRunner.getPhaseSummaries()
      // Map phase to stream of integer tuples (of length 3).
      .map(p ->
        simulationRunner.getInfections().map(i -> {
          var infectedCount = p.getInfectedCount().get(i);
          var immuneCount = p.getImmuneCount().get(i);
          return Stream.of(
            p.getAliveCount() - infectedCount - immuneCount,
            infectedCount,
            immuneCount
          );
        })
      )
      // Map stream of integer tuples to stream of string tuples.
      .map(line -> line.map(t -> t.map(i -> i.toString())))
      // Map stream of string tuples to stream of strings.
      .map(line -> line.map(t -> t.collect(Collectors.joining(" "))))
      // Map stream of strings to string.
      .map(line -> line.collect(Collectors.joining(" ; ")))
      // Print all lines.
      .forEach(out::println);
  }


  // For BaseSimulationRunner.Config.
  // Infects random Agents.
  static private Consumer<List<? extends IAgent>> defaultInitializeAgentsFactory(List<? extends Infection> infections) {
    return agents -> {
      final int infectionsCount = infections.size();
      final int N = agents.size();
      final int N_HALF = N / 2;
      final int P = Global.getConfigInstance().getPatientZeroCount();

      Set<Integer> patientZeroIndexes = new HashSet<>();


      // This way it's always O(P).
      if (P <= N_HALF) {
        while (patientZeroIndexes.size() < P) {
          patientZeroIndexes.add(nextInt(N));
        }
      } else {
        for (int i = 0 ; i < N ; i += 1) {
          patientZeroIndexes.add(i);
        }

        while (patientZeroIndexes.size() > P) {
          patientZeroIndexes.remove(nextInt(N));
        }
      }


      for (int idx : patientZeroIndexes) {
        agents.get(idx).infect(infections.get(nextInt(infectionsCount)));
      }
    };
  }

  // For BaseSimulationRunner.Config.
  static private Function<Integer, ? extends IAgent> defaultGenerateRandomAgentFactory() {
    double sp = Global.getConfigInstance().getSocialAgentProbability();
    double mp = Global.getConfigInstance().getMeetingProbability();
    int ml = Global.getConfigInstance().getMeetingLimit();

    return id -> chooseWithProbability(sp, () -> new SocialAgent(id, mp, ml), () -> new NormalAgent(id, mp, ml));
  }

  static private BaseSimulationRunner simulationRunnerFactory(Infection infection) {
    return simulationRunnerFactory(List.of(infection));
  }

  // Creates BaseSimulationRunner with Global configuration variables.
  static private BaseSimulationRunner simulationRunnerFactory(List<? extends Infection> infections) {
    var simulationConfig = new BaseSimulationRunner.Config();

    simulationConfig.setSimulationDuration(Global.getConfigInstance().getSimulationDuration());
    simulationConfig.setAgentCount(Global.getConfigInstance().getAgentCount());
    simulationConfig.setAverageDegree(Global.getConfigInstance().getAverageDegree());
    simulationConfig.setGenerateRandomAgent(defaultGenerateRandomAgentFactory());
    simulationConfig.setInitializeAgents(defaultInitializeAgentsFactory(infections));
    simulationConfig.setInfections(infections);

    return new BaseSimulationRunner(simulationConfig);
  }


  // Prints error message and exits with status code 1.
  static private void endProgramWithError(Throwable e) {
    System.err.println(e.getMessage());
    e.printStackTrace();
    endProgramWithError();
  }

  // Exits with status code 1.
  static private void endProgramWithError() {
    System.out.flush();
    System.err.flush();
    System.exit(1);
  }


  // Creates Validator instance.
  static private Validator createValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }

  // Tries to load given name as resource and if it fails returns standard filesystem path.
  static private Path loadPath(String name) {
    URL res = Thread.currentThread().getContextClassLoader().getResource(name);

    if (res != null) {
      try {
        return Paths.get(res.toURI());
      } catch (URISyntaxException e) { }
    }

    return Paths.get(name);
  }

  // Opens globally configured report file and returns its PrintStream.
  static private PrintStream openReportFile() throws IOException {
    return new PrintStream(Files.newOutputStream(
      Global.getConfigInstance().getReportFilePath(),
      StandardOpenOption.CREATE,
      Global.getConfigInstance().getReportFileOverwrite() ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.APPEND
    ));
  }

  // Sets logging level of java.util.logging.
  static private void setLoggingLevel(Level targetLevel) {
      Logger root = Logger.getLogger("");
      root.setLevel(targetLevel);
      for (Handler handler : root.getHandlers()) {
          handler.setLevel(targetLevel);
      }
  }

  // Sets program-wide locale.
  static private void setLocale(Locale locale) {
    Locale.setDefault(locale);
    Global.getInstance().setLocale(locale);
  }


  // Returns Reader instance.
  static private Reader createReader(Path path, Charset charset) throws IOException {
    return Files.newBufferedReader(path, charset);
  }

  // Returns InputStream instance.
  static private InputStream createInputStream(Path path) throws IOException {
    return Files.newInputStream(path);
  }
}