/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.SaveCartForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("saveCartFormValidator")
public class SaveCartFormValidator implements Validator
{
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return SaveCartForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final SaveCartForm saveCartForm = (SaveCartForm) object;
		final String name = saveCartForm.getName();
		final String description = saveCartForm.getDescription();

		if (StringUtils.isBlank(name))
		{
			errors.rejectValue("name", "basket.save.cart.validation.name.notBlank");
			return;
		}

		if (!StringUtils.isAlphanumericSpace(name))
		{
			errors.rejectValue("name", "basket.save.cart.validation.name.charset");
			return;
		}

		if (StringUtils.length(name) > 255)
		{
			errors.rejectValue("name", "basket.save.cart.validation.name.size");
			return;
		}

		if (StringUtils.length(description) > 255)
		{
			errors.rejectValue("description", "basket.save.cart.validation.description.size");
			return;
		}
	}
}
