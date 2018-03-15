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
package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrgUnit first defined at extension commerceservices.
 */
@SuppressWarnings("all")
public class OrgUnitModel extends CompanyModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrgUnit";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrgUnit.active</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrgUnit.path</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String PATH = "path";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrgUnitModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrgUnitModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public OrgUnitModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public OrgUnitModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.active</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrgUnit.path</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	@Accessor(qualifier = "path", type = Accessor.Type.GETTER)
	public String getPath()
	{
		return getPersistenceContext().getPropertyValue(PATH);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrgUnit.active</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrgUnit.path</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the path - Flat representation of the path of traversal to reach the OrgUnit from the root of its organization.
	 */
	@Accessor(qualifier = "path", type = Accessor.Type.SETTER)
	public void setPath(final String value)
	{
		getPersistenceContext().setPropertyValue(PATH, value);
	}
	
}
