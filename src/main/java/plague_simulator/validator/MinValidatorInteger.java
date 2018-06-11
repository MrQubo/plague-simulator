package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import plague_simulator.validator.Min;

public class MinValidatorInteger implements ConstraintValidator<Min, Integer> {
  double bound;
  boolean inclusive;

  @Override
  public void initialize(Min annotation) {
    bound = annotation.value();
    inclusive = annotation.inclusive();
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext ctx) {
    if (value == null) { return true; }

    int cmp = Integer.compare(value, (int)Math.round(bound));
    return inclusive ? cmp >= 0 : cmp > 0;
  }
}
