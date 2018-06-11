package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import plague_simulator.validator.ValidateConfigAverageDegreeValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ValidateConfigAverageDegreeValidator.class)
public @interface ValidateConfigAverageDegree {
  String message() default "{plague_simulator.validator.ValidateConfigAverageDegree.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
