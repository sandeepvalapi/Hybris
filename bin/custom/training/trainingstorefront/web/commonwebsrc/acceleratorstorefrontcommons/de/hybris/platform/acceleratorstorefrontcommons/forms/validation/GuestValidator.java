/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates entries on Guest user forms.
 */
@Component("guestValidator")
public class GuestValidator implements Validator
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return GuestForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final GuestForm guestForm = (GuestForm) object;
		final String email = guestForm.getEmail();

		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}
		else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}
	}

	protected boolean validateEmailAddress(final String email)
	{
		final Matcher matcher = Pattern.compile(configurationService.getConfiguration().getString(WebConstants.EMAIL_REGEX))
				.matcher(email);
		return matcher.matches();
	}
}
