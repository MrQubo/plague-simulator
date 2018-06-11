package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import plague_simulator.Config;
import plague_simulator.validator.ValidateConfigPatientZeroCount;

public class ValidateConfigPatientZeroCountValidator implements ConstraintValidator<ValidateConfigPatientZeroCount, Config> {
  @Override
  public boolean isValid(Config config, ConstraintValidatorContext ctx) {
    if (config == null) { return true; }
    if (config.getAgentCount() == null) { return true; }

    if (config.getPatientZeroCount() > config.getAgentCount()) {
      return false;
    }

    return true;
  }
}
