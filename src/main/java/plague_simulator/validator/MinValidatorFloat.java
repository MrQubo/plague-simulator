package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinValidatorFloat implements ConstraintValidator<Min, Float> {
  double bound;
  boolean inclusive;

  @Override
  public void initialize(Min annotation) {
    bound = annotation.value();
    inclusive = annotation.inclusive();
  }

  @Override
  public boolean isValid(Float value, ConstraintValidatorContext ctx) {
    if (value == null) { return true; }

    int cmp = Float.compare(value, (int)Math.round(bound));
    return inclusive ? cmp >= 0 : cmp > 0;
  }
}
