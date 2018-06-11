package plague_simulator.simulation;

import java.util.function.Supplier;
import java.util.stream.Stream;

import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.MeetingAgent;
import plague_simulator.simulation.ISimulationRunner;

public class NormalAgent extends MeetingAgent {
  public NormalAgent(int id, double meetingProbability, int meetingLimit) { super(id, meetingProbability, meetingLimit); }

  @Override
  public String getTypeString() {
    return "normal";
  }

  @Override
  public double getMeetingProbability() {
    if (isHealthy()) {
      return super.getMeetingProbability();
    } else {
      return super.getMeetingProbability() / 2.0;
    }
  }

  @Override
  public Stream<? extends IAgent> getFriends(ISimulationRunner sr) {
    return getFriendsFromAdj(() -> getAdj());
  }
  @Override
  public Stream<? extends IAgent> getFriendsCopy(ISimulationRunner sr) {
    return getFriendsFromAdj(() -> getAdjCopy());
  }


  private Stream<? extends IAgent> getFriendsFromAdj(Supplier<? extends Stream<? extends IAgent>> adj) {
    return adj.get().filter(IAgent::isAlive);
  }
}
