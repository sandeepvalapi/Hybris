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
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type CxVariation first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxVariationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxVariation";
	
	/**<i>Generated relation code constant for relation <code>CxCustomizationToVariation</code> defining source attribute <code>customization</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXCUSTOMIZATIONTOVARIATION = "CxCustomizationToVariation";
	
	/**<i>Generated relation code constant for relation <code>PreviewDataToCxVariation</code> defining source attribute <code>previews</code> in extension <code>personalizationcms</code>.</i>*/
	public static final String _PREVIEWDATATOCXVARIATION = "PreviewDataToCxVariation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.code</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.name</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.active</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.enabled</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ENABLED = "enabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.status</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.rank</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String RANK = "rank";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.customizationPOS</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CUSTOMIZATIONPOS = "customizationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.customization</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CUSTOMIZATION = "customization";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.actions</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ACTIONS = "actions";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.triggers</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String TRIGGERS = "triggers";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxVariation.previews</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String PREVIEWS = "previews";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxVariationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxVariationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _customization initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _name initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxVariationModel(final CatalogVersionModel _catalogVersion, final String _code, final CxCustomizationModel _customization, final String _name)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCustomization(_customization);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _customization initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _name initial attribute declared by type <code>CxVariation</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CxVariationModel(final CatalogVersionModel _catalogVersion, final String _code, final CxCustomizationModel _customization, final String _name, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCustomization(_customization);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.actions</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actions
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public List<CxAbstractActionModel> getActions()
	{
		return getPersistenceContext().getPropertyValue(ACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.code</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.customization</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the customization
	 */
	@Accessor(qualifier = "customization", type = Accessor.Type.GETTER)
	public CxCustomizationModel getCustomization()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMIZATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.name</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the name - Name of variation
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.previews</code> attribute defined at extension <code>personalizationcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the previews
	 */
	@Accessor(qualifier = "previews", type = Accessor.Type.GETTER)
	public Collection<PreviewDataModel> getPreviews()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.rank</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 * @return the rank - Defines the rank (priority) of variation
	 */
	@Accessor(qualifier = "rank", type = Accessor.Type.GETTER)
	public Integer getRank()
	{
		return getPersistenceContext().getDynamicValue(this,RANK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.status</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the status - Contains current status of item
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public CxItemStatus getStatus()
	{
		final CxItemStatus value = getPersistenceContext().getPropertyValue(STATUS);
		return value != null ? value : CxItemStatus.ENABLED;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.triggers</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.GETTER)
	public Collection<CxAbstractTriggerModel> getTriggers()
	{
		return getPersistenceContext().getPropertyValue(TRIGGERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.active</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 * @return the active - Defines if the variation is active or not
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public boolean isActive()
	{
		return toPrimitive( (Boolean) getPersistenceContext().getDynamicValue(this,ACTIVE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxVariation.enabled</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the enabled - Defines if the variation is enabled or not
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
	public boolean isEnabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ENABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.actions</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the actions
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final List<CxAbstractActionModel> value)
	{
		getPersistenceContext().setPropertyValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CxVariation.code</code> attribute defined at extension <code>personalizationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.customization</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the customization
	 */
	@Accessor(qualifier = "customization", type = Accessor.Type.SETTER)
	public void setCustomization(final CxCustomizationModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMIZATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.enabled</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the enabled - Defines if the variation is enabled or not
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
	public void setEnabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.name</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the name - Name of variation
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.previews</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the previews
	 */
	@Accessor(qualifier = "previews", type = Accessor.Type.SETTER)
	public void setPreviews(final Collection<PreviewDataModel> value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.rank</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the rank - Defines the rank (priority) of variation
	 */
	@Accessor(qualifier = "rank", type = Accessor.Type.SETTER)
	public void setRank(final Integer value)
	{
		getPersistenceContext().setDynamicValue(this,RANK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.status</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the status - Contains current status of item
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final CxItemStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxVariation.triggers</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the triggers
	 */
	@Accessor(qualifier = "triggers", type = Accessor.Type.SETTER)
	public void setTriggers(final Collection<CxAbstractTriggerModel> value)
	{
		getPersistenceContext().setPropertyValue(TRIGGERS, value);
	}
	
}
