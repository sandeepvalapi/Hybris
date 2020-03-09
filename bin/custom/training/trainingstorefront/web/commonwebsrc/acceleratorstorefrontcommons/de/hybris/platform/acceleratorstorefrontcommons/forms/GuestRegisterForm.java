/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

/**
 * Form object for registering a guest user after checkout. The only additional information required is a password. The
 * remaining user data is available from the checkout process.
 */
public class GuestRegisterForm
{
	private String pwd;
	private String checkPwd;
	private String orderCode;
	private String uid;
	private ConsentForm consentForm;
	private boolean termsCheck;

	/**
	 * @return the pwd
	 */
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
	 *
	 * @return orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 *
	 * @param orderCode
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 *
	 * @return uid
	 */
	public String getUid()
	{
		return uid;
	}

	/**
	 *
	 * @param uid
	 */
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public ConsentForm getConsentForm()
	{
		return consentForm;
	}

	public void setConsentForm(final ConsentForm consentForm)
	{
		this.consentForm = consentForm;
	}

	/**
	 *
	 * @return termsCheck
	 */
	public boolean isTermsCheck()
	{
		return termsCheck;
	}

	/**
	 *
	 * @param termsCheck
	 */
	public void setTermsCheck(final boolean termsCheck)
	{
		this.termsCheck = termsCheck;
	}


}
