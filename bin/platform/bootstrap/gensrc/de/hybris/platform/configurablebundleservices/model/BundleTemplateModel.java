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
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateStatusModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type BundleTemplate first defined at extension configurablebundleservices.
 */
@SuppressWarnings("all")
public class BundleTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BundleTemplate";
	
	/**<i>Generated relation code constant for relation <code>BundleTemplateRelation</code> defining source attribute <code>parentTemplate</code> in extension <code>configurablebundleservices</code>.</i>*/
	public static final String _BUNDLETEMPLATERELATION = "BundleTemplateRelation";
	
	/**<i>Generated relation code constant for relation <code>ProductsBundleTemplatesRelation</code> defining source attribute <code>products</code> in extension <code>configurablebundleservices</code>.</i>*/
	public static final String _PRODUCTSBUNDLETEMPLATESRELATION = "ProductsBundleTemplatesRelation";
	
	/**<i>Generated relation code constant for relation <code>RequiredBundleTemplatesDependentBundleTemplatesRelation</code> defining source attribute <code>requiredBundleTemplates</code> in extension <code>configurablebundleservices</code>.</i>*/
	public static final String _REQUIREDBUNDLETEMPLATESDEPENDENTBUNDLETEMPLATESRELATION = "RequiredBundleTemplatesDependentBundleTemplatesRelation";
	
	/**<i>Generated relation code constant for relation <code>BundleTemplateStatusRelation</code> defining source attribute <code>status</code> in extension <code>configurablebundleservices</code>.</i>*/
	public static final String _BUNDLETEMPLATESTATUSRELATION = "BundleTemplateStatusRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.id</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.version</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.name</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.description</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.bundleSelectionCriteria</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String BUNDLESELECTIONCRITERIA = "bundleSelectionCriteria";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.parentTemplatePOS</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String PARENTTEMPLATEPOS = "parentTemplatePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.parentTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String PARENTTEMPLATE = "parentTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.childTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CHILDTEMPLATES = "childTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.products</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.changeProductPriceBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String CHANGEPRODUCTPRICEBUNDLERULES = "changeProductPriceBundleRules";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.disableProductBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String DISABLEPRODUCTBUNDLERULES = "disableProductBundleRules";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.requiredBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String REQUIREDBUNDLETEMPLATES = "requiredBundleTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.dependentBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String DEPENDENTBUNDLETEMPLATES = "dependentBundleTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>BundleTemplate.status</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String STATUS = "status";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BundleTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BundleTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 * @param _version initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public BundleTemplateModel(final CatalogVersionModel _catalogVersion, final String _id, final String _version)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 * @param _id initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _parentTemplate initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 * @param _version initial attribute declared by type <code>BundleTemplate</code> at extension <code>configurablebundleservices</code>
	 */
	@Deprecated
	public BundleTemplateModel(final CatalogVersionModel _catalogVersion, final String _id, final ItemModel _owner, final BundleTemplateModel _parentTemplate, final String _version)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setId(_id);
		setOwner(_owner);
		setParentTemplate(_parentTemplate);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.bundleSelectionCriteria</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	@Accessor(qualifier = "bundleSelectionCriteria", type = Accessor.Type.GETTER)
	public BundleSelectionCriteriaModel getBundleSelectionCriteria()
	{
		return getPersistenceContext().getPropertyValue(BUNDLESELECTIONCRITERIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.changeProductPriceBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the changeProductPriceBundleRules
	 */
	@Accessor(qualifier = "changeProductPriceBundleRules", type = Accessor.Type.GETTER)
	public Collection<ChangeProductPriceBundleRuleModel> getChangeProductPriceBundleRules()
	{
		return getPersistenceContext().getPropertyValue(CHANGEPRODUCTPRICEBUNDLERULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.childTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the childTemplates
	 */
	@Accessor(qualifier = "childTemplates", type = Accessor.Type.GETTER)
	public List<BundleTemplateModel> getChildTemplates()
	{
		return getPersistenceContext().getPropertyValue(CHILDTEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.dependentBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dependentBundleTemplates
	 */
	@Accessor(qualifier = "dependentBundleTemplates", type = Accessor.Type.GETTER)
	public Collection<BundleTemplateModel> getDependentBundleTemplates()
	{
		return getPersistenceContext().getPropertyValue(DEPENDENTBUNDLETEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the description - Description of the bundle template
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.description</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @param loc the value localization key 
	 * @return the description - Description of the bundle template
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.disableProductBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the disableProductBundleRules
	 */
	@Accessor(qualifier = "disableProductBundleRules", type = Accessor.Type.GETTER)
	public Collection<DisableProductBundleRuleModel> getDisableProductBundleRules()
	{
		return getPersistenceContext().getPropertyValue(DISABLEPRODUCTBUNDLERULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.id</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the name - Name of the bundle template
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name of the bundle template
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.parentTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the parentTemplate
	 */
	@Accessor(qualifier = "parentTemplate", type = Accessor.Type.GETTER)
	public BundleTemplateModel getParentTemplate()
	{
		return getPersistenceContext().getPropertyValue(PARENTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.products</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.requiredBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the requiredBundleTemplates
	 */
	@Accessor(qualifier = "requiredBundleTemplates", type = Accessor.Type.GETTER)
	public Collection<BundleTemplateModel> getRequiredBundleTemplates()
	{
		return getPersistenceContext().getPropertyValue(REQUIREDBUNDLETEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.status</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public BundleTemplateStatusModel getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BundleTemplate.version</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.bundleSelectionCriteria</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the bundleSelectionCriteria - Criteria how many of the bundle's assigned products must/can be selected'
	 */
	@Accessor(qualifier = "bundleSelectionCriteria", type = Accessor.Type.SETTER)
	public void setBundleSelectionCriteria(final BundleSelectionCriteriaModel value)
	{
		getPersistenceContext().setPropertyValue(BUNDLESELECTIONCRITERIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.catalogVersion</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the catalogVersion - Catalog Version
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.changeProductPriceBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the changeProductPriceBundleRules
	 */
	@Accessor(qualifier = "changeProductPriceBundleRules", type = Accessor.Type.SETTER)
	public void setChangeProductPriceBundleRules(final Collection<ChangeProductPriceBundleRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(CHANGEPRODUCTPRICEBUNDLERULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.childTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the childTemplates
	 */
	@Accessor(qualifier = "childTemplates", type = Accessor.Type.SETTER)
	public void setChildTemplates(final List<BundleTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(CHILDTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.dependentBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the dependentBundleTemplates
	 */
	@Accessor(qualifier = "dependentBundleTemplates", type = Accessor.Type.SETTER)
	public void setDependentBundleTemplates(final Collection<BundleTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(DEPENDENTBUNDLETEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.description</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the description - Description of the bundle template
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.description</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the description - Description of the bundle template
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.disableProductBundleRules</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the disableProductBundleRules
	 */
	@Accessor(qualifier = "disableProductBundleRules", type = Accessor.Type.SETTER)
	public void setDisableProductBundleRules(final Collection<DisableProductBundleRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(DISABLEPRODUCTBUNDLERULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleTemplate.id</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id - Identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the name - Name of the bundle template
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.name</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the name - Name of the bundle template
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BundleTemplate.parentTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the parentTemplate
	 */
	@Accessor(qualifier = "parentTemplate", type = Accessor.Type.SETTER)
	public void setParentTemplate(final BundleTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(PARENTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.products</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.requiredBundleTemplates</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the requiredBundleTemplates
	 */
	@Accessor(qualifier = "requiredBundleTemplates", type = Accessor.Type.SETTER)
	public void setRequiredBundleTemplates(final Collection<BundleTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(REQUIREDBUNDLETEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.status</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final BundleTemplateStatusModel value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BundleTemplate.version</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the version - The version of the bundle template. Each clone of a BundleTemplate needs to have a different version.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
