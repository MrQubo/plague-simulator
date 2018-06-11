package plague_simulator.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

import plague_simulator.simulation.BaseAgent;
import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.SimulationRunner;

import static plague_simulator.utils.RandomUtils.nextInt;
import static plague_simulator.utils.RandomUtils.nextIntBinomialTruncated;
import static plague_simulator.utils.RandomUtils.trueWithProbability;

// This IAgent plans meetings and meets with other Agents.
public abstract class MeetingAgent extends BaseAgent {
  private @Getter @Setter double meetingProbability;

  // Key:   Phase number.
  // Value: Agents with whom this IAgent will meet.
  private Map<Integer, List<IAgent>> plannedMeetings = new HashMap<>();


  public MeetingAgent(int id, double meetingProbability) {
    super(id);
    this.meetingProbability = meetingProbability;
  }


  public Stream<? extends Map.Entry<Integer, ? extends List<? extends IAgent>>> getPlannedMeetings() {
    return plannedMeetings.entrySet().stream();
  }
  public Stream<? extends Map.Entry<Integer, ? extends List<? extends IAgent>>> getPlannedMeetingsCopy() {
    return Map.copyOf(plannedMeetings).entrySet().stream();
  }


  protected void addPlannedMeeting(IAgent friend, int onPhaseNumber) {
    if (!plannedMeetings.containsKey(onPhaseNumber)) {
      plannedMeetings.put(onPhaseNumber, new ArrayList<>());
    }
    plannedMeetings.get(onPhaseNumber).add(friend);
  }


  @Override
  public void runPhase(SimulationRunner sr) {
    super.runPhase(sr);

    // The check is made 3 times to clear plannedMeetings early and don't waste resources.
    if (canCancelAllMeetings(sr)) {
      plannedMeetings.clear();
      return;
    }

    planAllMeetings(sr);

    if (canCancelAllMeetings(sr)) {
      plannedMeetings.clear();
      return;
    }

    arrangeAllPlannedMeetings(sr);

    if (canCancelAllMeetings(sr)) {
      plannedMeetings.clear();
      return;
    }
  }


  protected void planAllMeetings(SimulationRunner sr) {
    while (trueWithProbability(getMeetingProbability())) {
      if (cannotPlanMoreMeetings(sr) || canCancelAllMeetings(sr)) { return; }
      planMeeting(sr);
    }
  }

  private void arrangeAllPlannedMeetings(SimulationRunner sr) {
    for (IAgent agent : plannedMeetings.getOrDefault(sr.getPhaseNumber(), List.of())) {
      if (canCancelAllMeetings(sr)) { return; }
      arrangeMeeting(agent, sr);
    }
  }


  protected void planMeeting(SimulationRunner sr) {
    final var wrapper = new Object(){ int N = 0; };

    // Don't need to copy because of `.limit(1)`.
    getFriends(sr)
      .filter(IAgent::isAlive)
      .peek(a -> wrapper.N += 1)
      .skip(wrapper.N > 0 ? nextInt(wrapper.N) : 0)
      .limit(1)
      .forEach(a -> planMeetingWith(a, sr));
  }

  protected void planMeetingWith(IAgent friend, SimulationRunner sr) {
    if (sr.getPhaseNumber() + 1 >= sr.getConfig().getSimulationDuration()) { return; }

    // Might be slow, but produces a way better results.
    addPlannedMeeting(friend, nextIntBinomialTruncated(sr.getPhaseNumber() + 1, sr.getConfig().getSimulationDuration()));
  }


  // Execute actual meeting.
  protected void arrangeMeeting(IAgent friend, SimulationRunner sr) {
    if (friend.isDead()) { return; } // [*]

    // Don't need to copy because it's the other party that gets infected.
    this.getInfections().forEach(i -> i.tryInfect(friend));
    friend.getInfections().forEach(i -> i.tryInfect(this));
  }


  protected boolean cannotPlanMoreMeetings(SimulationRunner sr) {
    return sr.getPhaseNumber() + 1 >= sr.getConfig().getSimulationDuration();
  }

  protected boolean canCancelAllMeetings(SimulationRunner sr) {
    return isDead();
  }


  abstract public Stream<? extends IAgent> getFriends(SimulationRunner sr);
  abstract public Stream<? extends IAgent> getFriendsCopy(SimulationRunner sr);
}
