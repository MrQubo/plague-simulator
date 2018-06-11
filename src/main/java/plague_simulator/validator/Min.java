package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import plague_simulator.validator.MinValidatorDouble;
import plague_simulator.validator.MinValidatorFloat;
import plague_simulator.validator.MinValidatorInteger;
import plague_simulator.validator.MinValidatorLong;

// Default implementation doesn't handle double and float values, and doesn't have inclusive option.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {MinValidatorInteger.class, MinValidatorDouble.class, MinValidatorLong.class, MinValidatorFloat.class})
public @interface Min {
  String message() default "{plague_simulator.validator.Min.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  // The bound
  public double value();
  // Should bound value be considered valid
  public boolean inclusive() default true;
}
