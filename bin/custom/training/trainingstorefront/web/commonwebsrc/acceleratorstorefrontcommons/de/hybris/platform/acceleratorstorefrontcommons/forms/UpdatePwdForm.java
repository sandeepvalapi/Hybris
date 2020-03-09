/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Form object for updating the password.
 */
public class UpdatePwdForm
{
	private String pwd;
	private String checkPwd;
	private String token;


	/**
	 * @return the pwd
	 */
	@NotNull(message = "{updatePwd.pwd.invalid}")
	@Size(min = 6, max = 255, message = "{updatePwd.pwd.invalid}")
	public String getPwd()
	{
		return pwd;
	}

	/**
	 * @param pwd
	 *           the pwd to set
	 */
	public void setPwd(final String pwd)
	{
		this.pwd = pwd;
	}

	/**
	 * @return the checkPwd
	 */
	@Size(max = 255, message = "{updatePwd.pwd.invalid}")
	public String getCheckPwd()
	{
		return checkPwd;
	}

	/**
	 * @param checkPwd
	 *           the checkPwd to set
	 */
	public void setCheckPwd(final String checkPwd)
	{
		this.checkPwd = checkPwd;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token
	 *           the token to set
	 */
	public void setToken(final String token)
	{
		this.token = token;
	}
}
