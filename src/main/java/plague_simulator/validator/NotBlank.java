package plague_simulator.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import plague_simulator.validator.NotBlankValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotBlankValidator.class)
public @interface NotBlank {
  String message() default "{plague_simulator.validator.NotBlank.message}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
