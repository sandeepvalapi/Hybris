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
package de.hybris.platform.cms2lib.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum RotatingImagesComponentEffect declared at extension cms2lib.
 */
@SuppressWarnings("PMD")
public enum RotatingImagesComponentEffect implements HybrisEnumValue
{
	/**
	 * Generated enum value for RotatingImagesComponentEffect.zoom declared at extension cms2lib.
	 */
	ZOOM("zoom"),
	/**
	 * Generated enum value for RotatingImagesComponentEffect.fade declared at extension cms2lib.
	 */
	FADE("fade"),
	/**
	 * Generated enum value for RotatingImagesComponentEffect.turnDown declared at extension cms2lib.
	 */
	TURNDOWN("turnDown"),
	/**
	 * Generated enum value for RotatingImagesComponentEffect.curtainX declared at extension cms2lib.
	 */
	CURTAINX("curtainX");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RotatingImagesComponentEffect";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "RotatingImagesComponentEffect";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private RotatingImagesComponentEffect(final String code)
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
