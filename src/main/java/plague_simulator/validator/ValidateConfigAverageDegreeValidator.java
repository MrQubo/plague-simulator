package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import plague_simulator.Config;
import plague_simulator.validator.ValidateConfigAverageDegree;

public class ValidateConfigAverageDegreeValidator implements ConstraintValidator<ValidateConfigAverageDegree, Config> {
  @Override
  public boolean isValid(Config config, ConstraintValidatorContext ctx) {
    if (config == null) { return true; }
    if (config.getAverageDegree() == null) { return true; }
    if (config.getAgentCount() == null) { return true; }

    if (config.getAverageDegree() >= config.getAgentCount()) {
      return false;
    }

    return true;
  }
}
