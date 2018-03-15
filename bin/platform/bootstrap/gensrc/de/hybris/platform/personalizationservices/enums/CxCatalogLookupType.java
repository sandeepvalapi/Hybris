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
package de.hybris.platform.personalizationservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CxCatalogLookupType declared at extension personalizationservices.
 */
@SuppressWarnings("PMD")
public enum CxCatalogLookupType implements HybrisEnumValue
{
	/**
	 * Generated enum value for CxCatalogLookupType.ALL_CATALOGS declared at extension personalizationservices.
	 * <p/>
	 * ALl catalogs in the session.
	 */
	ALL_CATALOGS("ALL_CATALOGS"),
	/**
	 * Generated enum value for CxCatalogLookupType.LEAF_CATALOGS declared at extension personalizationcms.
	 * <p/>
	 * Current catalog lookup only.
	 */
	LEAF_CATALOGS("LEAF_CATALOGS"),
	/**
	 * Generated enum value for CxCatalogLookupType.LEAF_CLOSEST_ANCESTOR_CATALOGS declared at extension personalizationcms.
	 * <p/>
	 * Current catalog or parents if empty.
	 */
	LEAF_CLOSEST_ANCESTOR_CATALOGS("LEAF_CLOSEST_ANCESTOR_CATALOGS");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CxCatalogLookupType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CxCatalogLookupType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CxCatalogLookupType(final String code)
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
