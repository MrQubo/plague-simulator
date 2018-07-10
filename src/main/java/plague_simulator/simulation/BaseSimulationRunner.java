package plague_simulator.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;

import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.ISimulationRunner;

import static plague_simulator.graph.GraphUtils.generateRandomSimpleUndirectedGraph;

public class BaseSimulationRunner implements ISimulationRunner {
    private List<IAgent> allAgents;
    private Set<Integer> patientZeroIds;
    private List<PhaseSummary> phaseSummaries;

    @Getter
    @Setter
    private Config config;


    public Stream<? extends IAgent> getAllAgents() {
        return allAgents.stream();
    }
    public Stream<? extends IAgent> getAllAgentsCopy() {
        return List.copyOf(allAgents).stream();
    }

    public Set<Integer> getPatientZeroIdsSet() {
        return Set.copyOf(patientZeroIds);
    }
    public Stream<Integer> getPatientZeroIds() {
        return patientZeroIds.stream();
    }
    public Stream<Integer> getPatientZeroIdsCopy() {
        return Set.copyOf(patientZeroIds).stream();
    }

    public Stream<? extends PhaseSummary> getPhaseSummaries() {
        return phaseSummaries.stream();
    }
    public Stream<? extends PhaseSummary> getPhaseSummariesCopy() {
        return List.copyOf(phaseSummaries).stream();
    }

    public Stream<? extends Infection> getInfections() {
        return getConfig().getInfections().stream();
    }

    @Override
    public int getPhaseNumber() {
        return phaseSummaries.size();
    }

    @Override
    public int getSimulationDuration() {
        return getConfig().getSimulationDuration();
    }


    protected PhaseSummary getLastPhaseSummary() {
        return phaseSummaries.get(phaseSummaries.size() - 1);
    }


    @Data
    @NoArgsConstructor
    static public class Config {
        private int simulationDuration;
        private int agentCount;
        private long edgeCount;

        @NonNull
        // Should return Agent with id equal to the given argument.
        private Function<Integer, ? extends IAgent> generateRandomAgent;

        @NonNull

        private Consumer<List<? extends IAgent>> initializeAgents;
        @NonNull
        private Collection<? extends Infection> infections;


        public int getAverageDegree() {
            return (int)(2L * edgeCount / agentCount);
        }

        public void setAverageDegree(int averageDegree) {
            edgeCount = (long)agentCount * averageDegree / 2L;
        }
    }

    @Value
    public class PhaseSummary {
        private int phaseNumber;
        private int aliveCount;
        private int deadCount;

        @NonNull
        private Map<Infection, Integer> infectedCount;

        @NonNull
        private Map<Infection, Integer> immuneCount;
    }


    public BaseSimulationRunner(Config config) {
        this.config = config;
    }


    public void generate() {
        final int V = getConfig().getAgentCount();
        final long E = getConfig().getEdgeCount();

        allAgents = new ArrayList<>(V);


        for (int i = 0 ; i < V ; i += 1) {
            allAgents.add(getConfig().getGenerateRandomAgent().apply(i+1));
        }

        if (getConfig().getInitializeAgents() != null) {
            getConfig().getInitializeAgents().accept(allAgents);
        }

        patientZeroIds = getAllAgents()
            .filter(IAgent::isNotHealthy)
            .map(IAgent::getId)
            .collect(Collectors.toUnmodifiableSet());


        generateRandomSimpleUndirectedGraph(allAgents, E);
    }


    @Override
    public void run() {
        if (allAgents == null || patientZeroIds == null) {
            generate();
        }
        runAssumeGenerated();
    }

    private void runAssumeGenerated() {
        final int N = getSimulationDuration();

        phaseSummaries = new ArrayList<>(N);

        while (phaseSummaries.size() < N) {
            runNextPhase();
        }
    }

    private void runNextPhase() {
        getAllAgents()
            .filter(IAgent::isAlive)
            .forEach(a -> a.runPhase(this));

        phaseSummaries.add(summarizeCurrentPhase());
    }

    private PhaseSummary summarizeCurrentPhase() {
        final int phaseNumber = getPhaseNumber();


        Map<IAgent.State, Long> stateCount = allAgents.stream()
            .collect(Collectors.groupingBy(IAgent::getState, Collectors.counting()));

        final int aliveCount = (int)(long)stateCount.getOrDefault(IAgent.State.ALIVE, 0L);
        final int deadCount  = (int)(long)stateCount.getOrDefault(IAgent.State.DEAD, 0L);


        Map<Infection, Integer> infectedCount = new HashMap<>();
        Map<Infection, Integer> immuneCount   = new HashMap<>();

        getInfections().forEach(i -> {
            infectedCount.put(i, 0);
            immuneCount.put(i, 0);
        });

        getAllAgents().filter(IAgent::isAlive).forEach(a -> {
            a.getInfections().forEach(i -> infectedCount.compute(i, (k, v) -> v+1));
            a.getImmunities().forEach(i -> immuneCount.compute(i, (k, v) -> v+1));
        });


        return new PhaseSummary(
            phaseNumber,
            aliveCount,
            deadCount,
            Map.copyOf(infectedCount),
            Map.copyOf(immuneCount)
        );
    }
}
