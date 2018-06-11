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
import plague_simulator.simulation.ISimulationRunner;

import static plague_simulator.utils.RandomUtils.nextInt;
import static plague_simulator.utils.RandomUtils.nextIntBinomialTruncated;
import static plague_simulator.utils.RandomUtils.trueWithProbability;

// This Agent plans meetings and meets with other Agents.
public abstract class MeetingAgent extends BaseAgent {
  private @Getter @Setter double meetingProbability;
  private @Getter @Setter int meetingLimit; // Lowering this value won't remove already planned meetings.

  // Key:   Phase number.
  // Value: Agents with whom this Agent will meet.
  private Map<Integer, List<IAgent>> plannedMeetings = new HashMap<>();

  private @Getter int plannedMeetingsCount = 0;


  public MeetingAgent(int id, double meetingProbability, int meetingLimit) {
    super(id);
    this.meetingProbability = meetingProbability;
    this.meetingLimit = meetingLimit;
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
    plannedMeetingsCount += 1;
  }

  protected int removeAllPlannedMeetings(int phaseNumber) {
    List<IAgent> planned = plannedMeetings.remove(phaseNumber);

    if (planned == null) { return 0; }

    plannedMeetingsCount -= planned.size();
    return planned.size();
  }


  @Override
  public void runPhase(ISimulationRunner sr) {
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


  // Plans meetings.
  protected void planAllMeetings(ISimulationRunner sr) {
    while (! cannotPlanMoreMeetings(sr) && trueWithProbability(getMeetingProbability())) {
      planMeeting(sr);
    }
  }

  // Executes all planned mettings for current phase.
  private void arrangeAllPlannedMeetings(ISimulationRunner sr) {
    for (IAgent agent : plannedMeetings.getOrDefault(sr.getPhaseNumber(), List.of())) {
      if (canCancelAllMeetings(sr)) { break; }

      arrangeMeeting(agent, sr);
    }

    removeAllPlannedMeetings(sr.getPhaseNumber());
  }


  // Plans metting with random Friend.
  protected void planMeeting(ISimulationRunner sr) {
    final var wrapper = new Object(){ int N = 0; };

    // Don't need to copy because of `.limit(1)`.
    getFriends(sr)
      .filter(IAgent::isAlive)
      .peek(a -> wrapper.N += 1)
      .skip(wrapper.N > 0 ? nextInt(wrapper.N) : 0)
      .limit(1)
      .forEach(a -> planMeetingWith(a, sr));
  }

  // Plans meetings with given Agent on random future phase.
  protected void planMeetingWith(IAgent friend, ISimulationRunner sr) {
    if (sr.getPhaseNumber() + 1 >= sr.getSimulationDuration()) { return; }

    // Might be slow, but produces a way better results.
    addPlannedMeeting(friend, nextIntBinomialTruncated(sr.getPhaseNumber() + 1, sr.getSimulationDuration()));
  }


  // Execute actual meeting.
  protected void arrangeMeeting(IAgent friend, ISimulationRunner sr) {
    if (friend.isDead()) { return; } // [*]

    // Don't need to copy because it's the other party that gets infected.
    this.getInfections().forEach(i -> i.tryInfect(friend));
    friend.getInfections().forEach(i -> i.tryInfect(this));
  }


  // Value of true stops planning of meetings by planAllMettings.
  // New plans can still be made by planMeeting and planMettingWith.
  protected boolean cannotPlanMoreMeetings(ISimulationRunner sr) {
    return
      (meetingLimit >= 0 && plannedMeetingsCount >= meetingLimit) ||
      sr.getPhaseNumber() + 1 >= sr.getSimulationDuration()       ||
      canCancelAllMeetings(sr);
  }

  // Indicates that Agent can forget all planned mettings.
  // Planned meetings will be removed during runPhase.
  protected boolean canCancelAllMeetings(ISimulationRunner sr) {
    return isDead();
  }


  // Friends: Agents with whom this Agent can meet.
  // Friends are being chosen with equal probability each.
  abstract public Stream<? extends IAgent> getFriends(ISimulationRunner sr);
  abstract public Stream<? extends IAgent> getFriendsCopy(ISimulationRunner sr);
}
