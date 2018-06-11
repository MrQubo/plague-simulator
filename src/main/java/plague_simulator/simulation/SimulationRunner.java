package plague_simulator.simulation;

import java.io.PrintStream;
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
import lombok.Value;

import plague_simulator.simulation.IAgent;

import static plague_simulator.graph.GraphUtils.generateRandomSimpleUndirectedGraph;

public class SimulationRunner {
  private List<IAgent> allAgents;
  private Set<Integer> patientZeroIds;
  private List<PhaseSummary> phaseSummaries;

  private @Getter Config config;


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

  public int getPhaseNumber() {
    return phaseSummaries.size();
  }

  public void setConfig(Config newConfig) {
    if (this.config == null) {
      this.config = newConfig;
      return;
    }

    if (newConfig.simulationDuration != null) {
      this.config.simulationDuration = newConfig.simulationDuration;
    }
    if (newConfig.agentCount != null) {
      this.config.agentCount = newConfig.agentCount;
    }
    if (newConfig.edgeCount != null) {
      this.config.edgeCount = newConfig.edgeCount;
    }
    if (newConfig.averageDegree != null) {
      this.config.averageDegree = newConfig.averageDegree;
    }
    if (newConfig.generateRandomAgent != null) {
      this.config.generateRandomAgent = newConfig.generateRandomAgent;
    }
    if (newConfig.infectRandomAgents != null) {
      this.config.infectRandomAgents = newConfig.infectRandomAgents;
    }
    if (newConfig.infections != null) {
      this.config.infections = newConfig.infections;
    }
  }


  protected PhaseSummary getLastPhaseSummary() {
    return phaseSummaries.get(phaseSummaries.size() - 1);
  }


  @Data
  @NoArgsConstructor
  static public class Config {
    private Integer simulationDuration;
    private Integer agentCount;
    private Long edgeCount;
    private Integer averageDegree;
    private Function<Integer, ? extends IAgent> generateRandomAgent;
    private Consumer<List<? extends IAgent>> infectRandomAgents;
    private Collection<? extends Infection> infections;

    public long getEdgeCount() {
      if (edgeCount != null) { return edgeCount; }
      return (long)getAgentCount() * getAverageDegree() / 2;
    }
  }

  @Value
  public class PhaseSummary {
    private int phaseNumber;
    private int aliveCount;
    private int deadCount;
    private @NonNull Map<Infection, Integer> infectedCount;
    private @NonNull Map<Infection, Integer> immuneCount;
  }


  public SimulationRunner(Config config) {
    this.config = config;
  }


  public void generate() {
    final int V = getConfig().getAgentCount();
    final long E = getConfig().getEdgeCount();

    allAgents = new ArrayList<>(V);


    for (int i = 0 ; i < V ; i += 1) {
      allAgents.add(getConfig().getGenerateRandomAgent().apply(i+1));
    }

    if (getConfig().getInfectRandomAgents() != null) {
      getConfig().getInfectRandomAgents().accept(allAgents);
    }

    patientZeroIds = getAllAgents()
      .filter(IAgent::isNotHealthy)
      .map(IAgent::getId)
      .collect(Collectors.toUnmodifiableSet());


    generateRandomSimpleUndirectedGraph(allAgents, E);
  }


  public void run() {
    if (allAgents == null || patientZeroIds == null) {
      generate();
    }
    runAssumeGenerated();
  }

  private void runAssumeGenerated() {
    final int N = getConfig().getSimulationDuration();

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


    var stateCount = allAgents.stream()
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
