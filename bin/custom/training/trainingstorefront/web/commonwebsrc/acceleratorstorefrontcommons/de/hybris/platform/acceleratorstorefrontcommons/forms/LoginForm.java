/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import javax.validation.constraints.NotNull;


public class LoginForm
{
	@NotNull(message = "{general.required}")
	private String j_username; // NOSONAR
	@NotNull(message = "{general.required}")
	private String j_password; // NOSONAR

	/**
	 * @return the j_username
	 */
	public String getJ_username() // NOSONAR
	{
		return j_username;
	}

	/**
	 * @param j_username
	 *           the j_username to set
	 */
	public void setJ_username(final String j_username) // NOSONAR
	{
		this.j_username = j_username;
	}

	/**
	 * @return the j_password
	 */
	public String getJ_password() // NOSONAR
	{
		return j_password;
	}

	/**
	 * @param j_password
	 *           the j_password to set
	 */
	public void setJ_password(final String j_password) // NOSONAR
	{
		this.j_password = j_password;
	}
}
