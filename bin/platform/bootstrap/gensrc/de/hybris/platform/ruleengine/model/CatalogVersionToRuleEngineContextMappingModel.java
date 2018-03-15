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
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogVersionToRuleEngineContextMapping first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class CatalogVersionToRuleEngineContextMappingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionToRuleEngineContextMapping";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionToRuleEngineContextMapping.catalogVersion</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionToRuleEngineContextMapping.context</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String CONTEXT = "context";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionToRuleEngineContextMappingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionToRuleEngineContextMappingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CatalogVersionToRuleEngineContextMapping</code> at extension <code>ruleengine</code>
	 * @param _context initial attribute declared by type <code>CatalogVersionToRuleEngineContextMapping</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public CatalogVersionToRuleEngineContextMappingModel(final CatalogVersionModel _catalogVersion, final AbstractRuleEngineContextModel _context)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setContext(_context);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CatalogVersionToRuleEngineContextMapping</code> at extension <code>ruleengine</code>
	 * @param _context initial attribute declared by type <code>CatalogVersionToRuleEngineContextMapping</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogVersionToRuleEngineContextMappingModel(final CatalogVersionModel _catalogVersion, final AbstractRuleEngineContextModel _context, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setContext(_context);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionToRuleEngineContextMapping.catalogVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionToRuleEngineContextMapping.context</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.GETTER)
	public AbstractRuleEngineContextModel getContext()
	{
		return getPersistenceContext().getPropertyValue(CONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionToRuleEngineContextMapping.catalogVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionToRuleEngineContextMapping.context</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.SETTER)
	public void setContext(final AbstractRuleEngineContextModel value)
	{
		getPersistenceContext().setPropertyValue(CONTEXT, value);
	}
	
}
