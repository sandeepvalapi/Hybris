/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationaddon.forms;

import org.apache.commons.lang.StringUtils;

public class NotificationPreferenceForm
{
	private String emailAddress = StringUtils.EMPTY;
	private String mobileNumber = StringUtils.EMPTY;

	private boolean emailEnabled;
	private boolean smsEnabled;

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}


	public boolean isEmailEnabled()
	{
		return emailEnabled;
	}

	public void setEmailEnabled(final boolean emailEnabled)
	{
		this.emailEnabled = emailEnabled;
	}

	public boolean isSmsEnabled()
	{
		return smsEnabled;
	}

	public void setSmsEnabled(final boolean smsEnabled)
	{
		this.smsEnabled = smsEnabled;
	}
}