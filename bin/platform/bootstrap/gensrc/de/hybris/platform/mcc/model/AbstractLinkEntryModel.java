/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
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
package de.hybris.platform.mcc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.mcc.enums.CockpitLinkArea;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type AbstractLinkEntry first defined at extension mcc.
 */
@SuppressWarnings("all")
public class AbstractLinkEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractLinkEntry";
	
	/**<i>Generated relation code constant for relation <code>Principal2ReadableAbstractLinkEntryRelation</code> defining source attribute <code>readPrincipals</code> in extension <code>mcc</code>.</i>*/
	public static final String _PRINCIPAL2READABLEABSTRACTLINKENTRYRELATION = "Principal2ReadableAbstractLinkEntryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractLinkEntry.code</code> attribute defined at extension <code>mcc</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractLinkEntry.area</code> attribute defined at extension <code>mcc</code>. */
	public static final String AREA = "area";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractLinkEntry.sortorder</code> attribute defined at extension <code>mcc</code>. */
	public static final String SORTORDER = "sortorder";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractLinkEntry.readPrincipals</code> attribute defined at extension <code>mcc</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractLinkEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractLinkEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 */
	@Deprecated
	public AbstractLinkEntryModel(final CockpitLinkArea _area, final String _code)
	{
		super();
		setArea(_area);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractLinkEntryModel(final CockpitLinkArea _area, final String _code, final ItemModel _owner)
	{
		super();
		setArea(_area);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractLinkEntry.area</code> attribute defined at extension <code>mcc</code>. 
	 * @return the area
	 */
	@Accessor(qualifier = "area", type = Accessor.Type.GETTER)
	public CockpitLinkArea getArea()
	{
		return getPersistenceContext().getPropertyValue(AREA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractLinkEntry.code</code> attribute defined at extension <code>mcc</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractLinkEntry.readPrincipals</code> attribute defined at extension <code>mcc</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getReadPrincipals()
	{
		return getPersistenceContext().getPropertyValue(READPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractLinkEntry.sortorder</code> attribute defined at extension <code>mcc</code>. 
	 * @return the sortorder
	 */
	@Accessor(qualifier = "sortorder", type = Accessor.Type.GETTER)
	public Integer getSortorder()
	{
		return getPersistenceContext().getPropertyValue(SORTORDER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractLinkEntry.area</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the area
	 */
	@Accessor(qualifier = "area", type = Accessor.Type.SETTER)
	public void setArea(final CockpitLinkArea value)
	{
		getPersistenceContext().setPropertyValue(AREA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractLinkEntry.code</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractLinkEntry.readPrincipals</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final List<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractLinkEntry.sortorder</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the sortorder
	 */
	@Accessor(qualifier = "sortorder", type = Accessor.Type.SETTER)
	public void setSortorder(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SORTORDER, value);
	}
	
}
