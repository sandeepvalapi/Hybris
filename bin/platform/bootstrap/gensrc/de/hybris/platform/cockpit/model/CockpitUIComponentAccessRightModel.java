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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CockpitUIComponentAccessRight first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitUIComponentAccessRightModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CockpitUIComponentAccessRight";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITEPRINCIPALS = "writePrincipals";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitUIComponentAccessRightModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitUIComponentAccessRightModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentAccessRight</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitUIComponentAccessRightModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentAccessRight</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIComponentAccessRightModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code - Code of component
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getReadPrincipals()
	{
		return getPersistenceContext().getPropertyValue(READPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWritePrincipals()
	{
		return getPersistenceContext().getPropertyValue(WRITEPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code - Code of component
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
	public void setWritePrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITEPRINCIPALS, value);
	}
	
}
