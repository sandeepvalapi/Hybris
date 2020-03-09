/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

/**
 * Form object for updating email
 */
public class UpdateEmailForm
{

	private String email;
	private String chkEmail;
	private String password;


	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}


	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}


	/**
	 * @return the chkEmail
	 */
	public String getChkEmail()
	{
		return chkEmail;
	}


	/**
	 * @param chkEmail
	 *           the chkEmail to set
	 */
	public void setChkEmail(final String chkEmail)
	{
		this.chkEmail = chkEmail;
	}

}
