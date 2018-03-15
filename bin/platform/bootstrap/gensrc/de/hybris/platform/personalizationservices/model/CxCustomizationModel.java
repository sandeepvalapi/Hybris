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
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type CxCustomization first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxCustomizationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxCustomization";
	
	/**<i>Generated relation code constant for relation <code>CxGroupToCustomization</code> defining source attribute <code>group</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXGROUPTOCUSTOMIZATION = "CxGroupToCustomization";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.code</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.name</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.rank</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String RANK = "rank";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.description</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.active</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.status</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.enabledStartDate</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ENABLEDSTARTDATE = "enabledStartDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.enabledEndDate</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ENABLEDENDDATE = "enabledEndDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.variations</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String VARIATIONS = "variations";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.groupPOS</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String GROUPPOS = "groupPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCustomization.group</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String GROUP = "group";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxCustomizationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxCustomizationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _group initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _name initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxCustomizationModel(final CatalogVersionModel _catalogVersion, final String _code, final CxCustomizationsGroupModel _group, final String _name)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setGroup(_group);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _group initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _name initial attribute declared by type <code>CxCustomization</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CxCustomizationModel(final CatalogVersionModel _catalogVersion, final String _code, final CxCustomizationsGroupModel _group, final String _name, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setGroup(_group);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.code</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.description</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the description - Description of customization
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.enabledEndDate</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the enabledEndDate - Date and time till which customization will be active
	 */
	@Accessor(qualifier = "enabledEndDate", type = Accessor.Type.GETTER)
	public Date getEnabledEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENABLEDENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.enabledStartDate</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the enabledStartDate - Date and time from which customization will be active
	 */
	@Accessor(qualifier = "enabledStartDate", type = Accessor.Type.GETTER)
	public Date getEnabledStartDate()
	{
		return getPersistenceContext().getPropertyValue(ENABLEDSTARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.group</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.GETTER)
	public CxCustomizationsGroupModel getGroup()
	{
		return getPersistenceContext().getPropertyValue(GROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.name</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the name - Name of customization
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.rank</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 * @return the rank - Defines the rank (priority) of the group
	 */
	@Accessor(qualifier = "rank", type = Accessor.Type.GETTER)
	public Integer getRank()
	{
		return getPersistenceContext().getDynamicValue(this,RANK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.status</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the status - Contains current status of item
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public CxItemStatus getStatus()
	{
		final CxItemStatus value = getPersistenceContext().getPropertyValue(STATUS);
		return value != null ? value : CxItemStatus.ENABLED;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.variations</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the variations
	 */
	@Accessor(qualifier = "variations", type = Accessor.Type.GETTER)
	public List<CxVariationModel> getVariations()
	{
		return getPersistenceContext().getPropertyValue(VARIATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCustomization.active</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 * @return the active - Defines if the variation is active or not
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public boolean isActive()
	{
		return toPrimitive( (Boolean) getPersistenceContext().getDynamicValue(this,ACTIVE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CxCustomization.code</code> attribute defined at extension <code>personalizationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.description</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the description - Description of customization
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.enabledEndDate</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the enabledEndDate - Date and time till which customization will be active
	 */
	@Accessor(qualifier = "enabledEndDate", type = Accessor.Type.SETTER)
	public void setEnabledEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENABLEDENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.enabledStartDate</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the enabledStartDate - Date and time from which customization will be active
	 */
	@Accessor(qualifier = "enabledStartDate", type = Accessor.Type.SETTER)
	public void setEnabledStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENABLEDSTARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.group</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.SETTER)
	public void setGroup(final CxCustomizationsGroupModel value)
	{
		getPersistenceContext().setPropertyValue(GROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.name</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the name - Name of customization
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.rank</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the rank - Defines the rank (priority) of the group
	 */
	@Accessor(qualifier = "rank", type = Accessor.Type.SETTER)
	public void setRank(final Integer value)
	{
		getPersistenceContext().setDynamicValue(this,RANK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.status</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the status - Contains current status of item
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final CxItemStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCustomization.variations</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the variations
	 */
	@Accessor(qualifier = "variations", type = Accessor.Type.SETTER)
	public void setVariations(final List<CxVariationModel> value)
	{
		getPersistenceContext().setPropertyValue(VARIATIONS, value);
	}
	
}
