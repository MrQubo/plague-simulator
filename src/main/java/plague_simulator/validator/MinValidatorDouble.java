package plague_simulator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import plague_simulator.validator.Min;

public class MinValidatorDouble implements ConstraintValidator<Min, Double> {
  double bound;
  boolean inclusive;

  @Override
  public void initialize(Min annotation) {
    bound = annotation.value();
    inclusive = annotation.inclusive();
  }

  @Override
  public boolean isValid(Double value, ConstraintValidatorContext ctx) {
    if (value == null) { return true; }

    int cmp = Double.compare(value, (int)Math.round(bound));
    return inclusive ? cmp >= 0 : cmp > 0;
  }
}
