package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ValidateConfigPatientZeroCountValidator.class)
public @interface ValidateConfigPatientZeroCount {
  String message() default "{plague_simulator.validator.ValidateConfigPatientZeroCount.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
