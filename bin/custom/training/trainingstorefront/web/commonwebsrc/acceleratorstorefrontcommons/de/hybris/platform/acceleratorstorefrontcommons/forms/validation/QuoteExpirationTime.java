/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Quote expiration time validation constraint
 */
@Retention(RUNTIME)
@Constraint(validatedBy = QuoteExpirationTimeValidator.class)
@Documented
public @interface QuoteExpirationTime
{
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
