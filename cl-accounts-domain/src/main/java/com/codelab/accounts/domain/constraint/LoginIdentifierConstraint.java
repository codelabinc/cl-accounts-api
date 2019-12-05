package com.codelab.accounts.domain.constraint;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author lordUhuru 04/12/2019
 */
@Constraint(validatedBy = LoginIdentifierConstraint.Validator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginIdentifierConstraint {
    String message() default "User with identifier not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    interface Validator extends ConstraintValidator<LoginIdentifierConstraint, String> {
    }
}
