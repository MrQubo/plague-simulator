package plague_simulator.simulation;

import java.util.stream.Stream;

import plague_simulator.graph.IGraphNode;
import plague_simulator.simulation.Infection;
import plague_simulator.simulation.SimulationRunner;

// Return values of getters without Copy suffix are expected to be not modified.
public interface IAgent extends IGraphNode<IAgent> {
  // adj: adjacent IAgent nodes
  public Stream<? extends IAgent> getAdj();
  public Stream<? extends IAgent> getAdjCopy();

  static public enum State { ALIVE, DEAD }

  // Should be unique for each instance of Agent in one graph.
  public int getId();

  // Type name of an agent.
  public String getTypeString();

  public Stream<? extends Infection> getInfections();
  public Stream<? extends Infection> getInfectionsCopy();
  public Stream<? extends Infection> getImmunities();
  public Stream<? extends Infection> getImmunitiesCopy();

  public void infect(Infection infection);
  public void kill(Infection infection);
  public void deinfect(Infection infection);
  public void makeImmune(Infection infection);

  // Simulate one phase of a simulation.
  public void runPhase(SimulationRunner simulationRunner);

  public State getState();

  public boolean isAlive();
  public boolean isDead();

  public boolean isNotHealthy();
  public boolean isHealthy();

  public boolean isNotInfected(Infection infection);
  public boolean isInfected(Infection infection);

  public boolean isNotImmune(Infection infection);
  public boolean isImmune(Infection infection);
}
