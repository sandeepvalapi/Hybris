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
package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type AbstractRulesModule first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class AbstractRulesModuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractRulesModule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.name</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.ruleType</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULETYPE = "ruleType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.active</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.version</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.catalogVersions</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRulesModule.allowedTargets</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String ALLOWEDTARGETS = "allowedTargets";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractRulesModuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractRulesModuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 * @param _version initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public AbstractRulesModuleModel(final String _name, final Long _version)
	{
		super();
		setName(_name);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public AbstractRulesModuleModel(final String _name, final ItemModel _owner, final Long _version)
	{
		super();
		setName(_name);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.active</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the active - Is module active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.allowedTargets</code> attribute defined at extension <code>ruleengine</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allowedTargets - List of the allowed module targets to perform synchronisation to.
	 */
	@Accessor(qualifier = "allowedTargets", type = Accessor.Type.GETTER)
	public List<AbstractRulesModuleModel> getAllowedTargets()
	{
		return getPersistenceContext().getPropertyValue(ALLOWEDTARGETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.catalogVersions</code> dynamic attribute defined at extension <code>ruleengine</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions - Catalog version if mapped.
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public Collection<CatalogVersionModel> getCatalogVersions()
	{
		return getPersistenceContext().getDynamicValue(this,CATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.name</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.ruleType</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
	public RuleType getRuleType()
	{
		return getPersistenceContext().getPropertyValue(RULETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRulesModule.version</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the version - unique module version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Long getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRulesModule.active</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the active - Is module active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRulesModule.allowedTargets</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the allowedTargets - List of the allowed module targets to perform synchronisation to.
	 */
	@Accessor(qualifier = "allowedTargets", type = Accessor.Type.SETTER)
	public void setAllowedTargets(final List<AbstractRulesModuleModel> value)
	{
		getPersistenceContext().setPropertyValue(ALLOWEDTARGETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractRulesModule.name</code> attribute defined at extension <code>ruleengine</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRulesModule.ruleType</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
	public void setRuleType(final RuleType value)
	{
		getPersistenceContext().setPropertyValue(RULETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRulesModule.version</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the version - unique module version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Long value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
