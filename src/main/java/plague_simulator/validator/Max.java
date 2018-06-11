package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import plague_simulator.validator.MaxValidatorDouble;
import plague_simulator.validator.MaxValidatorFloat;
import plague_simulator.validator.MaxValidatorInteger;
import plague_simulator.validator.MaxValidatorLong;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {MaxValidatorInteger.class, MaxValidatorDouble.class, MaxValidatorLong.class, MaxValidatorFloat.class})
public @interface Max {
  String message() default "{plague_simulator.validator.Max.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  // The bound
  public double value();
  // Should bound value be considered valid
  public boolean inclusive() default true;
}
