package plague_simulator;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.extern.slf4j.Slf4j;

import plague_simulator.Simulation;
import plague_simulator.config.Config;
import plague_simulator.config.ConfigLoader;
import plague_simulator.config.ConfigValidator;
import plague_simulator.config.ConfigurationReader;
import plague_simulator.global.Global;
import plague_simulator.global.MessageComposer;
import plague_simulator.message.ELMessageInterpolator;
import plague_simulator.message.ResourceBundleMessageResolver;

@Slf4j
public class Main {
    static private final String MESSAGES_BUNDLE_NAME                = "Messages";
    static private final String APP_DEFAULTS_FILE_NAME              = "app-defaults.properties";
    static private final String USER_CONFIG_FILE_NAME               = "default.properties";
    static private final String DEFAULT_SIMULATION_CONFIG_FILE_NAME = "simulation-conf.xml";


    // Shouldn't be instantiated.
    private Main() { }


    static public void main(String args[]) {
        LOG.debug("Starting program.");

        // init
        initGlobal();

        // set locale
        setDefaultLocale();

        // init config field names
        initGlobalConfigFieldNames();

        // config defaults
        loadAppDefaults();

        // user config
        loadUserConfig();
        validateUserConfig();

        // cli
        CommandLine cli = parseCli(args);

        // simulation config
        loadSimulationConfig(cli.getOptionValue("config", DEFAULT_SIMULATION_CONFIG_FILE_NAME));
        validateSimulationConfig();

        // simulation
        startSimulation();
    }


    // Initializes Global fields.
    static private void initGlobal() {
        initGlobalConfigInstance();
        initGlobalMessageComposerInstance();
    }

    // Initializes Global Config object with null fields.
    static private void initGlobalConfigInstance() {
        Global.setConfigInstance(Config.createEmptyConfig());
    }

    // Initializes Global MessageComposer object with default locale.
    static private void initGlobalMessageComposerInstance() {
        Global.setMessageComposerInstance(new MessageComposer(
            new ResourceBundleMessageResolver(MESSAGES_BUNDLE_NAME),
            new ELMessageInterpolator()
        ));
    }


    static private void setDefaultLocale() {
        setLocale(new Locale("pl", "PL"));
    }


    static private void initGlobalConfigFieldNames() {
        Global.getConfigInstance().initFieldNames(Global.getMessageComposerInstance());
    }


    // Loads app defaults config into Global Config object.
    static private void loadAppDefaults() {
        final ConfigLoader configLoader = new ConfigLoader();
        configLoader.getConfigurationReader().setLocationStrategy(ConfigurationReader.CLASSPATH_LOCATION_STRATEGY);
        loadGlobalConfig(APP_DEFAULTS_FILE_NAME, configLoader, false, false, true);
    }


    // Loads app defaults config into Global Config object.
    static private void loadUserConfig() {
        final ConfigLoader configLoader = new ConfigLoader();
        loadGlobalConfig(USER_CONFIG_FILE_NAME, configLoader, false, false, true);
    }

    // Validates app defaults config.
    static private void validateUserConfig() {
        validateGlobalConfig(true);
    }


    // Parses CLI arguments.
    static private CommandLine parseCli(String args[]) {
        final Options options = new Options();

        options.addOption(
            Option.builder("c")
                .longOpt("config")
                .desc(String.format("config file [%s]", DEFAULT_SIMULATION_CONFIG_FILE_NAME))
                .hasArg()
                .argName("FILE")
                .build()
        );

        final DefaultParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            // Print help and usage message to stderr.
            System.err.println(e.toString());
            System.err.println();

            final HelpFormatter formatter = new HelpFormatter();
            final PrintWriter printWriter = new PrintWriter(System.err);

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
    } // parseCli


    // Loads app defaults config into Global Config object.
    static private void loadSimulationConfig(String simulationConfigFileName) {
        final ConfigLoader configLoader = new ConfigLoader();
        loadGlobalConfig(simulationConfigFileName, configLoader, true, false, true);
    }

    // Validates app defaults config.
    static private void validateSimulationConfig() {
        validateGlobalConfig(true);
    }


    static private void startSimulation() {
        Simulation simulation = new Simulation();
        simulation.start();
    }


    // Loads config file with specified ConfigLoader into existing Global Config.
    static private void loadGlobalConfig(
        String configFileName, ConfigLoader configLoader, boolean isXML, boolean warnOnMissing, boolean errorOnMissing
    ) {
        Config newConfig;
        if (isXML) {
            newConfig = configLoader.loadXMLConfig(configFileName, Global.getConfigInstance());
        } else {
            newConfig = configLoader.loadPropertiesConfig(configFileName, Global.getConfigInstance());
        }

        if (newConfig != null) {
            Global.setConfigInstance(newConfig);
        } else if (errorOnMissing) {
            LOG.error(Global.getMessageComposerInstance().getMissingConfigFile(configFileName));
            endProgramWithError();
        } else if (warnOnMissing) {
            LOG.warn(Global.getMessageComposerInstance().getMissingConfigFile(configFileName));
        } else {
            LOG.debug(Global.getMessageComposerInstance().getMissingConfigFile(configFileName));
        }
    }

    static private void validateGlobalConfig(boolean isError) {
        final ConfigValidator configValidator = ConfigValidator.createConfigValidator();

        List<String> violations = configValidator.validateConfig(Global.getConfigInstance());

        for (String violation : violations) {
            if (isError) {
                LOG.error(violation);
            } else {
                LOG.warn(violation);
            }
        }

        if (isError && ! violations.isEmpty()) {
            endProgramWithError();
        }
    }


    // Sets program-wide locale.
    static private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Global.getMessageComposerInstance().setMessageResolver(new ResourceBundleMessageResolver(MESSAGES_BUNDLE_NAME, locale));
    }


    // Exits with status code 1.
    static private void endProgramWithError() {
        System.out.flush();
        System.err.flush();
        LOG.info("Forcefully exiting with status code 1.");
        System.exit(1);
    }
} // Main
