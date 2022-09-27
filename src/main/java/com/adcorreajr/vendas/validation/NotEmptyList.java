package com.adcorreajr.vendas.validation;

import com.adcorreajr.vendas.validation.constraintvalidation.NotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmptyList {

    String message() default  "A lista não pode ser vazia.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
