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
package de.hybris.platform.configurablebundleservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.configurablebundleservices.enums.BundleRuleTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AbstractBundleRule first defined at extension configurablebundleservices.
 */
@SuppressWarnings("all")
public class AbstractBundleRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractBundleRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.id</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.name</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.ruleType</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String RULETYPE = "ruleType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.conditionalProducts</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CONDITIONALPRODUCTS = "conditionalProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBundleRule.targetProducts</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String TARGETPRODUCTS = "targetProducts";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractBundleRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractBundleRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractBundleRule</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public AbstractBundleRuleModel(final CatalogVersionModel _catalogVersion)
	{
		super();
		setCatalogVersion(_catalogVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractBundleRule</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>AbstractBundleRule</code> at extension <code>configurablebundleservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractBundleRuleModel(final CatalogVersionModel _catalogVersion, final String _id, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.conditionalProducts</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the conditionalProducts
	 */
	@Accessor(qualifier = "conditionalProducts", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getConditionalProducts()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONALPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.id</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the name - Name of the bundle rule
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.ruleType</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
	public BundleRuleTypeEnum getRuleType()
	{
		return getPersistenceContext().getPropertyValue(RULETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBundleRule.targetProducts</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the targetProducts
	 */
	@Accessor(qualifier = "targetProducts", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getTargetProducts()
	{
		return getPersistenceContext().getPropertyValue(TARGETPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractBundleRule.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBundleRule.conditionalProducts</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the conditionalProducts
	 */
	@Accessor(qualifier = "conditionalProducts", type = Accessor.Type.SETTER)
	public void setConditionalProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONALPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractBundleRule.id</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBundleRule.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the name - Name of the bundle rule
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBundleRule.ruleType</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the ruleType
	 */
	@Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
	public void setRuleType(final BundleRuleTypeEnum value)
	{
		getPersistenceContext().setPropertyValue(RULETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBundleRule.targetProducts</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the targetProducts
	 */
	@Accessor(qualifier = "targetProducts", type = Accessor.Type.SETTER)
	public void setTargetProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(TARGETPRODUCTS, value);
	}
	
}
