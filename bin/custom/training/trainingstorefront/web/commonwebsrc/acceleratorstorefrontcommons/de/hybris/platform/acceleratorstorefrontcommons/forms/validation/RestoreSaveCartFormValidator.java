/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RestoreSaveCartForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for restore save cart.
 */
@Component("restoreSaveCartFormValidator")
public class RestoreSaveCartFormValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return RestoreSaveCartForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final RestoreSaveCartForm restoreSaveCartForm = (RestoreSaveCartForm) object;
		if (restoreSaveCartForm.getCartName() != null && !restoreSaveCartForm.isPreventSaveActiveCart())
		{
			final String cartName = restoreSaveCartForm.getCartName();

			if (StringUtils.isBlank(cartName))
			{
				errors.rejectValue("cartName", "basket.save.cart.validation.name.notBlank");
				return;
			}

			if (!StringUtils.isAlphanumericSpace(cartName))
			{
				errors.rejectValue("cartName", "basket.save.cart.validation.name.charset");
				return;
			}

			if (StringUtils.length(cartName) > 255)
			{
				errors.rejectValue("cartName", "basket.save.cart.validation.name.size");
				return;
			}
		}
	}
}
