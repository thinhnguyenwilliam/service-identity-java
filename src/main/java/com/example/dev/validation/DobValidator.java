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
            return false; // You could return true if null is allowed
        }

        int age = Period.between(dob, LocalDate.now()).getYears();
        return age >= minAge;
    }
}
