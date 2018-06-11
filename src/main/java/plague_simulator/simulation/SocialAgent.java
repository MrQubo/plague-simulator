package plague_simulator.simulation;

import java.util.function.Supplier;
import java.util.stream.Stream;

import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.MeetingAgent;
import plague_simulator.simulation.SimulationRunner;

public class SocialAgent extends MeetingAgent {
  public SocialAgent(int id, double meetingProbability, int meetingLimit) { super(id, meetingProbability, meetingLimit); }

  @Override
  public String getTypeString() {
    return "social";
  }

  @Override
  public Stream<? extends IAgent> getFriends(SimulationRunner sr) {
    return getFriendsFromAdj(() -> getAdj());
  }
  @Override
  public Stream<? extends IAgent> getFriendsCopy(SimulationRunner sr) {
    return getFriendsFromAdj(() -> getAdjCopy());
  }


  private Stream<? extends IAgent> getFriendsFromAdj(Supplier<? extends Stream<? extends IAgent>> adj) {
    Supplier<? extends Stream<? extends IAgent>> aliveAdj = () -> adj.get().filter(IAgent::isAlive);

    if (isHealthy()) {
      // I filter early for better performance.
      return Stream.concat(aliveAdj.get(), aliveAdj.get().flatMap(IAgent::getAdj).filter(IAgent::isAlive)).distinct();
    } else {
      return aliveAdj.get();
    }
  }
}
