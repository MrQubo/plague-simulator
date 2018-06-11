package plague_simulator.simulation;

public interface ISimulationRunner {
  public int getPhaseNumber(); // Current phase number.
  public int getSimulationDuration(); // Number of phases == number of last phase + 1.

  // Run simulation.
  public void run();
}
