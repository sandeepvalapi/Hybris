/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.notificationservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum NotificationType declared at extension notificationservices.
 * <p/>
 * This is only place holder that needs to be extended by specific Notification Types.
 */
@SuppressWarnings("PMD")
public enum NotificationType implements HybrisEnumValue
{
	/**
	 * Generated enum value for NotificationType.BACK_IN_STOCK declared at extension stocknotificationservices.
	 */
	BACK_IN_STOCK("BACK_IN_STOCK"),
	/**
	 * Generated enum value for NotificationType.NOTIFICATION declared at extension notificationservices.
	 */
	NOTIFICATION("NOTIFICATION");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "NotificationType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "NotificationType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private NotificationType(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
