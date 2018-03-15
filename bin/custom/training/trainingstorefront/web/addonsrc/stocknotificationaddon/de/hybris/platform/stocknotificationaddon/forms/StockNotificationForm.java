/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
/*
* [y] hybris Platform
*
* Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/
package de.hybris.platform.stocknotificationaddon.forms;

/**
 *
 */

public class StockNotificationForm
{
	private String emailAddress;
	private String mobileNumber;
	private boolean emailNotificationEnabled;
	private boolean smsNotificationEnabled;
	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public boolean isEmailNotificationEnabled()
	{
		return emailNotificationEnabled;
	}

	public void setEmailNotificationEnabled(boolean emailNotificationEnabled)
	{
		this.emailNotificationEnabled = emailNotificationEnabled;
	}

	public boolean isSmsNotificationEnabled()
	{
		return smsNotificationEnabled;
	}

	public void setSmsNotificationEnabled(boolean smsNotificationEnabled)
	{
		this.smsNotificationEnabled = smsNotificationEnabled;
	}








}
