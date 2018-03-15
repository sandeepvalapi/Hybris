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
package de.hybris.platform.category.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Category first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CategoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Category";
	
	/**<i>Generated relation code constant for relation <code>CategoryCategoryRelation</code> defining source attribute <code>supercategories</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATEGORYCATEGORYRELATION = "CategoryCategoryRelation";
	
	/**<i>Generated relation code constant for relation <code>CategoriesForRestriction</code> defining source attribute <code>restrictions</code> in extension <code>cms2</code>.</i>*/
	public static final String _CATEGORIESFORRESTRICTION = "CategoriesForRestriction";
	
	/**<i>Generated relation code constant for relation <code>ProductListComponentsForCategory</code> defining source attribute <code>productListComponents</code> in extension <code>cms2lib</code>.</i>*/
	public static final String _PRODUCTLISTCOMPONENTSFORCATEGORY = "ProductListComponentsForCategory";
	
	/**<i>Generated relation code constant for relation <code>CategoriesForProductCarouselComponent</code> defining source attribute <code>productCarouselComponents</code> in extension <code>cms2lib</code>.</i>*/
	public static final String _CATEGORIESFORPRODUCTCAROUSELCOMPONENT = "CategoriesForProductCarouselComponent";
	
	/**<i>Generated relation code constant for relation <code>CategoryFeatureComponents2CategoryRel</code> defining source attribute <code>categoryFeatureComponents</code> in extension <code>acceleratorcms</code>.</i>*/
	public static final String _CATEGORYFEATURECOMPONENTS2CATEGORYREL = "CategoryFeatureComponents2CategoryRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.order</code> attribute defined at extension <code>catalog</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.normal</code> attribute defined at extension <code>catalog</code>. */
	public static final String NORMAL = "normal";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. */
	public static final String THUMBNAILS = "thumbnails";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.detail</code> attribute defined at extension <code>catalog</code>. */
	public static final String DETAIL = "detail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.logo</code> attribute defined at extension <code>catalog</code>. */
	public static final String LOGO = "logo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. */
	public static final String DATA_SHEET = "data_sheet";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.others</code> attribute defined at extension <code>catalog</code>. */
	public static final String OTHERS = "others";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. */
	public static final String THUMBNAIL = "thumbnail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.picture</code> attribute defined at extension <code>catalog</code>. */
	public static final String PICTURE = "picture";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allSubcategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLSUBCATEGORIES = "allSubcategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allSupercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLSUPERCATEGORIES = "allSupercategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. */
	public static final String KEYWORDS = "keywords";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLOWEDPRINCIPALS = "allowedPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.products</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPERCATEGORIES = "supercategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.categories</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORIES = "categories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.medias</code> attribute defined at extension <code>catalog</code>. */
	public static final String MEDIAS = "medias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.promotions</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONS = "promotions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.restrictions</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.linkComponents</code> attribute defined at extension <code>cms2</code>. */
	public static final String LINKCOMPONENTS = "linkComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.allYFormDefinitions</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String ALLYFORMDEFINITIONS = "allYFormDefinitions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.yFormDefinitions</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String YFORMDEFINITIONS = "yFormDefinitions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.productListComponents</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTLISTCOMPONENTS = "productListComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.productCarouselComponents</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTCAROUSELCOMPONENTS = "productCarouselComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Category.categoryFeatureComponents</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String CATEGORYFEATURECOMPONENTS = "categoryFeatureComponents";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CategoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CategoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Category</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CategoryModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allowedPrincipals - Principals which are allowed to access this catalog category
	 */
	@Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getAllowedPrincipals()
	{
		return getPersistenceContext().getPropertyValue(ALLOWEDPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allSubcategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSubcategories
	 */
	@Accessor(qualifier = "allSubcategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAllSubcategories()
	{
		return getPersistenceContext().getDynamicValue(this,ALLSUBCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allSupercategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSupercategories
	 */
	@Accessor(qualifier = "allSupercategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAllSupercategories()
	{
		return getPersistenceContext().getDynamicValue(this,ALLSUPERCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.allYFormDefinitions</code> dynamic attribute defined at extension <code>xyformsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allYFormDefinitions
	 */
	@Accessor(qualifier = "allYFormDefinitions", type = Accessor.Type.GETTER)
	public List<YFormDefinitionModel> getAllYFormDefinitions()
	{
		return getPersistenceContext().getDynamicValue(this,ALLYFORMDEFINITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.categories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories - Sub Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getCategories()
	{
		return getPersistenceContext().getPropertyValue(CATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.categoryFeatureComponents</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categoryFeatureComponents
	 */
	@Accessor(qualifier = "categoryFeatureComponents", type = Accessor.Type.GETTER)
	public List<CategoryFeatureComponentModel> getCategoryFeatureComponents()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYFEATURECOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the data_sheet
	 */
	@Accessor(qualifier = "data_sheet", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getData_sheet()
	{
		return getPersistenceContext().getPropertyValue(DATA_SHEET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description - Catalog Category Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the description - Catalog Category Description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.detail</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the detail
	 */
	@Accessor(qualifier = "detail", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getDetail()
	{
		return getPersistenceContext().getPropertyValue(DETAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @return the keywords - Keyword
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords()
	{
		return getKeywords(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the keywords - Keyword
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
	public List<KeywordModel> getKeywords(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(KEYWORDS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.linkComponents</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the linkComponents
	 */
	@Accessor(qualifier = "linkComponents", type = Accessor.Type.GETTER)
	public List<CMSLinkComponentModel> getLinkComponents()
	{
		return getPersistenceContext().getPropertyValue(LINKCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.logo</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the logo
	 */
	@Accessor(qualifier = "logo", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getLogo()
	{
		return getPersistenceContext().getPropertyValue(LOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.medias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the medias - Category medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
	public List<MediaModel> getMedias()
	{
		return getPersistenceContext().getPropertyValue(MEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.normal</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the normal
	 */
	@Accessor(qualifier = "normal", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getNormal()
	{
		return getPersistenceContext().getPropertyValue(NORMAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.order</code> attribute defined at extension <code>catalog</code>. 
	 * @return the order - Order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public Integer getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.others</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the others
	 */
	@Accessor(qualifier = "others", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getOthers()
	{
		return getPersistenceContext().getPropertyValue(OTHERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.picture</code> attribute defined at extension <code>catalog</code>. 
	 * @return the picture - Image
	 */
	@Accessor(qualifier = "picture", type = Accessor.Type.GETTER)
	public MediaModel getPicture()
	{
		return getPersistenceContext().getPropertyValue(PICTURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.productCarouselComponents</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productCarouselComponents
	 */
	@Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.GETTER)
	public Collection<ProductCarouselComponentModel> getProductCarouselComponents()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCAROUSELCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.productListComponents</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productListComponents
	 */
	@Accessor(qualifier = "productListComponents", type = Accessor.Type.GETTER)
	public List<ProductListComponentModel> getProductListComponents()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTLISTCOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.products</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.promotions</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the promotions - Promotions
	 */
	@Accessor(qualifier = "promotions", type = Accessor.Type.GETTER)
	public Collection<ProductPromotionModel> getPromotions()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public Collection<CMSCategoryRestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getSupercategories()
	{
		return getPersistenceContext().getPropertyValue(SUPERCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. 
	 * @return the thumbnail - Small Image
	 */
	@Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
	public MediaModel getThumbnail()
	{
		return getPersistenceContext().getPropertyValue(THUMBNAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the thumbnails
	 */
	@Accessor(qualifier = "thumbnails", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getThumbnails()
	{
		return getPersistenceContext().getPropertyValue(THUMBNAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.yFormDefinitions</code> attribute defined at extension <code>xyformsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yFormDefinitions - Form Definitions
	 */
	@Accessor(qualifier = "yFormDefinitions", type = Accessor.Type.GETTER)
	public Set<YFormDefinitionModel> getYFormDefinitions()
	{
		return getPersistenceContext().getPropertyValue(YFORMDEFINITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.allowedPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the allowedPrincipals - Principals which are allowed to access this catalog category
	 */
	@Accessor(qualifier = "allowedPrincipals", type = Accessor.Type.SETTER)
	public void setAllowedPrincipals(final List<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(ALLOWEDPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.categories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categories - Sub Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final List<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.categoryFeatureComponents</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the categoryFeatureComponents
	 */
	@Accessor(qualifier = "categoryFeatureComponents", type = Accessor.Type.SETTER)
	public void setCategoryFeatureComponents(final List<CategoryFeatureComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORYFEATURECOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.code</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.data_sheet</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the data_sheet
	 */
	@Accessor(qualifier = "data_sheet", type = Accessor.Type.SETTER)
	public void setData_sheet(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(DATA_SHEET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - Catalog Category Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - Catalog Category Description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.detail</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the detail
	 */
	@Accessor(qualifier = "detail", type = Accessor.Type.SETTER)
	public void setDetail(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(DETAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keyword
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value)
	{
		setKeywords(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.keywords</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keywords - Keyword
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
	public void setKeywords(final List<KeywordModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(KEYWORDS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.linkComponents</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the linkComponents
	 */
	@Accessor(qualifier = "linkComponents", type = Accessor.Type.SETTER)
	public void setLinkComponents(final List<CMSLinkComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(LINKCOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.logo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the logo
	 */
	@Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
	public void setLogo(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(LOGO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.medias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the medias - Category medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
	public void setMedias(final List<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(MEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Category.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.normal</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the normal
	 */
	@Accessor(qualifier = "normal", type = Accessor.Type.SETTER)
	public void setNormal(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(NORMAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.order</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the order - Order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.others</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the others
	 */
	@Accessor(qualifier = "others", type = Accessor.Type.SETTER)
	public void setOthers(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(OTHERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.picture</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the picture - Image
	 */
	@Accessor(qualifier = "picture", type = Accessor.Type.SETTER)
	public void setPicture(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(PICTURE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.productCarouselComponents</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the productCarouselComponents
	 */
	@Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.SETTER)
	public void setProductCarouselComponents(final Collection<ProductCarouselComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTCAROUSELCOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.productListComponents</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the productListComponents
	 */
	@Accessor(qualifier = "productListComponents", type = Accessor.Type.SETTER)
	public void setProductListComponents(final List<ProductListComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTLISTCOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.products</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.promotions</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the promotions - Promotions
	 */
	@Accessor(qualifier = "promotions", type = Accessor.Type.SETTER)
	public void setPromotions(final Collection<ProductPromotionModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final Collection<CMSCategoryRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
	public void setSupercategories(final List<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(SUPERCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.thumbnail</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the thumbnail - Small Image
	 */
	@Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
	public void setThumbnail(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(THUMBNAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.thumbnails</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the thumbnails
	 */
	@Accessor(qualifier = "thumbnails", type = Accessor.Type.SETTER)
	public void setThumbnails(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(THUMBNAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Category.yFormDefinitions</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the yFormDefinitions - Form Definitions
	 */
	@Accessor(qualifier = "yFormDefinitions", type = Accessor.Type.SETTER)
	public void setYFormDefinitions(final Set<YFormDefinitionModel> value)
	{
		getPersistenceContext().setPropertyValue(YFORMDEFINITIONS, value);
	}
	
}
