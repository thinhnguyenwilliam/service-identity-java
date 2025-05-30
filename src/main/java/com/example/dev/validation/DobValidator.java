package com.example.dev.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

  private int minAge;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    this.minAge = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
    if (dob == null) {
      return false;
    }

    int age = Period.between(dob, LocalDate.now()).getYears();
    if (age < minAge) {
      context.disableDefaultConstraintViolation();
      // Keep the default message key but pass min dynamically in the message later
      context
          .buildConstraintViolationWithTemplate("INVALID_DOB:{min=" + minAge + "}")
          .addConstraintViolation();

      // Now the message becomes something like: INVALID_DOB:{min=18}
      return false;
    }

    return true;
  }
}
