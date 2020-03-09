/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for Emails
 */
@Component("emailValidator")
public class EmailValidator implements Validator
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdateEmailForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final UpdateEmailForm updateEmailForm = (UpdateEmailForm) object;
		final String email = updateEmailForm.getEmail();
		final String chkEmail = updateEmailForm.getChkEmail();
		final String password = updateEmailForm.getPassword();

		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}
		else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}

		if (StringUtils.isEmpty(chkEmail))
		{
			errors.rejectValue("chkEmail", "profile.checkEmail.invalid");
		}

		if (StringUtils.isEmpty(password))
		{
			errors.rejectValue("password", "profile.pwd.invalid");
		}
	}

	protected boolean validateEmailAddress(final String email)
	{
		final Matcher matcher = Pattern.compile(configurationService.getConfiguration().getString(WebConstants.EMAIL_REGEX))
				.matcher(email);
		return matcher.matches();
	}
}
