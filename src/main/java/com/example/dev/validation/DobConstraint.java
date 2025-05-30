package com.example.dev.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DobValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DobConstraint {
  String message() default "Invalid date of birth";

  int min();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
