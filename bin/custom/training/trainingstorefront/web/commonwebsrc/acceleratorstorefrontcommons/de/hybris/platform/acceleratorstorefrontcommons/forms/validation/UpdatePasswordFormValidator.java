/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePwdForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("updatePasswordFormValidator")
public class UpdatePasswordFormValidator implements Validator
{
	@Override
	public boolean supports(Class<?> aClass)
	{
		return UpdatePasswordForm.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		final UpdatePwdForm updatePasswordForm = (UpdatePwdForm) object;
        final String newPassword = updatePasswordForm.getPwd();
		final String checkPassword = updatePasswordForm.getCheckPwd();

		if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(checkPassword)
				&& !StringUtils.equals(newPassword,checkPassword))
		{
			errors.rejectValue("checkPwd", "validation.checkPwd.equals");
		}
		else if(StringUtils.isEmpty(checkPassword))
		{
			errors.rejectValue("checkPwd", "updatePwd.checkPwd.invalid");
		}
	}
}
