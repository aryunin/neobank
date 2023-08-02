package com.aryunin.conveyor.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateInPastValidator implements ConstraintValidator<DateInPast, LocalDate> {
    @Override
    public void initialize(DateInPast constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isBefore(LocalDate.now());
    }
}
