package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinValidatorLong implements ConstraintValidator<Min, Long> {
  double bound;
  boolean inclusive;

  @Override
  public void initialize(Min annotation) {
    bound = annotation.value();
    inclusive = annotation.inclusive();
  }

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext ctx) {
    if (value == null) { return true; }

    int cmp = Long.compare(value, (int)Math.round(bound));
    return inclusive ? cmp >= 0 : cmp > 0;
  }
}
