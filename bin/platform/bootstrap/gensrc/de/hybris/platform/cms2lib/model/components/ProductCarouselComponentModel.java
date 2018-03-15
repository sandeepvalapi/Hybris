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
package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2lib.enums.CarouselScroll;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ProductCarouselComponent first defined at extension cms2lib.
 */
@SuppressWarnings("all")
public class ProductCarouselComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductCarouselComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.scroll</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String SCROLL = "scroll";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.productCodes</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTCODES = "productCodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.categoryCodes</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String CATEGORYCODES = "categoryCodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.products</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.categories</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String CATEGORIES = "categories";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.searchQuery</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String SEARCHQUERY = "searchQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.categoryCode</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String CATEGORYCODE = "categoryCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCarouselComponent.popup</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String POPUP = "popup";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductCarouselComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductCarouselComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ProductCarouselComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ProductCarouselComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.categories</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getCategories()
	{
		return getPersistenceContext().getPropertyValue(CATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.categoryCode</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the categoryCode - A code for a category for a solr search
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
	public String getCategoryCode()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.categoryCodes</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categoryCodes
	 */
	@Accessor(qualifier = "categoryCodes", type = Accessor.Type.GETTER)
	public List<String> getCategoryCodes()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.productCodes</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productCodes
	 */
	@Accessor(qualifier = "productCodes", type = Accessor.Type.GETTER)
	public List<String> getProductCodes()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.products</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.scroll</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the scroll
	 */
	@Accessor(qualifier = "scroll", type = Accessor.Type.GETTER)
	public CarouselScroll getScroll()
	{
		return getPersistenceContext().getPropertyValue(SCROLL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.searchQuery</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the searchQuery - A solr query string
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
	public String getSearchQuery()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the title - Localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the title - Localized title of the component.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCarouselComponent.popup</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the popup - If true shows a popup else redirects to products page
	 */
	@Accessor(qualifier = "popup", type = Accessor.Type.GETTER)
	public boolean isPopup()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(POPUP));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.categories</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final List<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.categoryCode</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the categoryCode - A code for a category for a solr search
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.SETTER)
	public void setCategoryCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CATEGORYCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.popup</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the popup - If true shows a popup else redirects to products page
	 */
	@Accessor(qualifier = "popup", type = Accessor.Type.SETTER)
	public void setPopup(final boolean value)
	{
		getPersistenceContext().setPropertyValue(POPUP, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.products</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.scroll</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the scroll
	 */
	@Accessor(qualifier = "scroll", type = Accessor.Type.SETTER)
	public void setScroll(final CarouselScroll value)
	{
		getPersistenceContext().setPropertyValue(SCROLL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.searchQuery</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the searchQuery - A solr query string
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
	public void setSearchQuery(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - Localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductCarouselComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - Localized title of the component.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
}
