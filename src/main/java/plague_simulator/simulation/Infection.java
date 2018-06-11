package plague_simulator.simulation;

import lombok.AllArgsConstructor;
import lombok.Data;

import plague_simulator.simulation.IAgent;

import static plague_simulator.utils.RandomUtils.trueWithProbability;

@Data
@AllArgsConstructor
public class Infection {
  private String id;
  private double infectivity;
  private double recoveryProbability;
  private double immunityProbability;
  private double lethality;

  public void tryInfect(IAgent agent) {
    if (agent.isImmune(this)) { return; }
    if (trueWithProbability(infectivity)) {
      agent.infect(this);
    }
  }

  public void tryRecover(IAgent agent) {
    if (agent.isNotInfected(this)) { return; }
    if (trueWithProbability(recoveryProbability)) {
      if (trueWithProbability(immunityProbability)) {
        agent.makeImmune(this);
      } else {
        agent.deinfect(this);
      }
    }
  }

  public void tryKill(IAgent agent) {
    if (agent.isNotInfected(this)) { return; }
    if (trueWithProbability(lethality)) {
      agent.kill(this);
    }
  }
}
