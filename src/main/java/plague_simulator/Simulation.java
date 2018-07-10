package plague_simulator;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import plague_simulator.config.Config;
import plague_simulator.global.Global;
import plague_simulator.global.MessageComposer;
import plague_simulator.simulation.BaseSimulationRunner;
import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.Infection;
import plague_simulator.simulation.NormalAgent;
import plague_simulator.simulation.SocialAgent;

import static plague_simulator.utils.RandomUtils.chooseWithProbability;
import static plague_simulator.utils.RandomUtils.nextInt;

@Slf4j
public class Simulation {
    public void start() {
        LOG.info("Starting simulation.");

        Infection infection = new Infection(
            "The Anime", // Very dreadful infection.
            Global.getConfigInstance().getInfectivity(),
            Global.getConfigInstance().getRecoveryProbability(),
            Global.getConfigInstance().getImmunityProbability(),
            Global.getConfigInstance().getLethality()
        );

        final BaseSimulationRunner simulationRunner = createSimulationRunner(infection);
        simulationRunner.run();

        LOG.info("Printing report...");
        outputReport(simulationRunner);
        LOG.info("Printed report.");
    }


    private BaseSimulationRunner createSimulationRunner(Infection infection) {
        return createSimulationRunner(List.of(infection));
    }

    // Creates BaseSimulationRunner with Global configuration variables.
    private BaseSimulationRunner createSimulationRunner(List<? extends Infection> infections) {
        BaseSimulationRunner.Config simulationConfig = new BaseSimulationRunner.Config();

        simulationConfig.setSimulationDuration(Global.getConfigInstance().getSimulationDuration());
        simulationConfig.setAgentCount(Global.getConfigInstance().getAgentCount());
        simulationConfig.setAverageDegree(Global.getConfigInstance().getAverageDegree());
        simulationConfig.setGenerateRandomAgent(defaultGenerateRandomAgentFactory());
        simulationConfig.setInitializeAgents(defaultInitializeAgentsFactory(infections));
        simulationConfig.setInfections(infections);

        return new BaseSimulationRunner(simulationConfig);
    }

    // For BaseSimulationRunner.Config.
    // Infects random Agents.
    private Consumer<List<? extends IAgent>> defaultInitializeAgentsFactory(List<? extends Infection> infections) {
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
    } // defaultInitializeAgentsFactory

    // For BaseSimulationRunner.Config.
    private Function<Integer, ? extends IAgent> defaultGenerateRandomAgentFactory() {
        double sp = Global.getConfigInstance().getSocialAgentProbability();
        double mp = Global.getConfigInstance().getMeetingProbability();
        int ml = Global.getConfigInstance().getMeetingLimit();

        return id -> chooseWithProbability(sp, () -> new SocialAgent(id, mp, ml), () -> new NormalAgent(id, mp, ml));
    }


    // Outputs report to globally configured report file.
    private void outputReport(BaseSimulationRunner simulationRunner) {
        final MessageComposer m = Global.getMessageComposerInstance();

        try (final PrintStream out = openReportFile()) {
            out.println(m.getReportConfigHeader());
            printConfig(out);
            out.println();
            out.println(m.getReportAgentsHeader());
            printAgents(simulationRunner, out);
            out.println();
            out.println(m.getReportGraphHeader());
            printGraph(simulationRunner, out);
            out.println();
            out.println(m.getReportPhaseSummariesHeader());
            printPhaseSummaries(simulationRunner, out);
        } catch (IOException e) {
            LOG.error(m.getCannotAccessReportFile(Global.getConfigInstance().getReportFilePath()), e);
        }
    }

    // Prints config variables.
    private void printConfig(PrintStream out) {
        Config c = Global.getConfigInstance();

        out.printf("%s=%s%n",   c.SEED(),                     c.getSeed());
        out.printf("%s=%d%n",   c.AGENT_COUNT(),              c.getAgentCount());
        out.printf("%s=%.3e%n", c.SOCIAL_AGENT_PROBABILITY(), c.getSocialAgentProbability());
        out.printf("%s=%.3e%n", c.MEETING_PROBABILITY(),      c.getMeetingProbability());
        out.printf("%s=%.3e%n", c.INFECTIVITY(),              c.getInfectivity());
        out.printf("%s=%.3e%n", c.RECOVERY_PROBABILITY(),     c.getRecoveryProbability());
        out.printf("%s=%.3e%n", c.LETHALITY(),                c.getLethality());
        out.printf("%s=%d%n",   c.SIMULATION_DURATION(),      c.getSimulationDuration());
        out.printf("%s=%d%n",   c.AVERAGE_DEGREE(),           c.getAverageDegree());
    }

    // Prints agents and their types.
    private void printAgents(BaseSimulationRunner simulationRunner, PrintStream out) {
        final Set<Integer> patientZeroIds = simulationRunner.getPatientZeroIdsSet();

        simulationRunner.getAllAgents()
            .map(a ->
                a.getId()                                                                                                                                                            +
                (patientZeroIds.contains(a.getId()) ? "*" : "")                                                                                +
                " "                                                                                                                                                                        +
                Global.getMessageComposerInstance().getAgentType(a.getTypeString())
            )
            .forEach(out::println);
    }

    // Prints graph structure.
    private void printGraph(BaseSimulationRunner simulationRunner, PrintStream out) {
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
    private void printPhaseSummaries(BaseSimulationRunner simulationRunner, PrintStream out) {
        simulationRunner.getPhaseSummaries()
            // Map phase to stream of integer tuples (of length 3).
            .map(p ->
                simulationRunner.getInfections().map(i -> {
                    int infectedCount = p.getInfectedCount().get(i);
                    int immuneCount = p.getImmuneCount().get(i);
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


    // Opens globally configured report file and returns its PrintStream.
    private PrintStream openReportFile() throws IOException {
        return new PrintStream(Files.newOutputStream(
            Paths.get(Global.getConfigInstance().getReportFilePath()),
            StandardOpenOption.CREATE,
            Global.getConfigInstance().getReportFileOverwrite() ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.APPEND
        ));
    }
} // Simulation
