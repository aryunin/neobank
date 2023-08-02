package com.aryunin.conveyor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=DateInPastValidator.class)
public @interface DateInPast {
    String message() default "{message.id}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
