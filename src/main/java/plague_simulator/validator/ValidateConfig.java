package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ValidateConfigPatientZeroCount
@ValidateConfigAverageDegree
@Constraint(validatedBy = {})
public @interface ValidateConfig {
  String message() default "{plague_simulator.validator.ValidateConfig.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
