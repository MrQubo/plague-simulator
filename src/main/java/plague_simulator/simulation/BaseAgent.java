package plague_simulator.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import plague_simulator.simulation.IAgent;
import plague_simulator.simulation.IAgent.State;
import plague_simulator.simulation.Infection;
import plague_simulator.simulation.SimulationRunner;

@RequiredArgsConstructor // ~(int id)
public abstract class BaseAgent implements IAgent {
  private final @Getter int id;
  private List<IAgent> adj = new ArrayList<>();

  private @Getter @Setter(AccessLevel.PROTECTED) State state = State.ALIVE;
  private Set<Infection> infections = new HashSet<>();
  private Set<Infection> immunities = new HashSet<>();


  @Override
  public Stream<? extends IAgent> getAdj() {
    return adj.stream();
  }
  @Override
  public Stream<? extends IAgent> getAdjCopy() {
    return List.copyOf(adj).stream();
  }

  @Override
  public void addAdj(IAgent a) {
    adj.add(a);
  }


  @Override
  public Stream<? extends Infection> getInfections() {
    return infections.stream();
  }
  @Override
  public Stream<? extends Infection> getInfectionsCopy() {
    return Set.copyOf(infections).stream();
  }

  @Override
  public Stream<? extends Infection> getImmunities() {
    return immunities.stream();
  }
  @Override
  public Stream<? extends Infection> getImmunitiesCopy() {
    return Set.copyOf(immunities).stream();
  }


  @Override
  public void infect(Infection infection) {
    if (isNotImmune(infection)) {
      infections.add(infection);
    }
  }

  // I don't remove dead agents from graph because this information might be useful in future use.
  @Override
  public void kill(Infection infection) {
    setState(State.DEAD);
  }

  // Might think of a better name.
  @Override
  public void deinfect(Infection infection) {
    if (!isDead()) { // It just makes sense.
      infections.remove(infection);
    }
  }

  @Override
  public void makeImmune(Infection infection) {
    if (!isDead()) {
      deinfect(infection);
      immunities.add(infection);
    }
  }


  // Iterates over infections.
  @Override
  public void runPhase(SimulationRunner sr) {
    getInfectionsCopy()
      .takeWhile(i -> {
        i.tryKill(this);
        if (isDead()) { return false; } // He don't need to be more dead.
        i.tryRecover(this);
        return true;
      }).forEach(i -> { });
  }


  @Override
  public boolean isAlive() {
    return getState() == State.ALIVE;
  }
  @Override
  public boolean isDead() {
    return getState() == State.DEAD;
  }

  @Override
  public boolean isNotHealthy() {
    return !isHealthy();
  }
  @Override
  public boolean isHealthy() {
    return infections.isEmpty();
  }

  @Override
  public boolean isNotInfected(Infection infection) {
    return !isInfected(infection);
  }
  @Override
  public boolean isInfected(Infection infection) {
    return infections.contains(infection);
  }

  @Override
  public boolean isNotImmune(Infection infection) {
    return !isImmune(infection);
  }
  @Override
  public boolean isImmune(Infection infection) {
    return immunities.contains(infection);
  }
}
