package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import plague_simulator.validator.NotBlank;

public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
  @Override
  public boolean isValid(String str, ConstraintValidatorContext ctx) {
    if (str == null) { return true; }

    return ! str.trim().isEmpty();
  }
}
