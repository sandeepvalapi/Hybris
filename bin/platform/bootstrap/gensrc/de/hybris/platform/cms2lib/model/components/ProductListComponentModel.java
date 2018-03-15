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
import de.hybris.platform.cms2lib.enums.ProductListLayouts;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ProductListComponent first defined at extension cms2lib.
 */
@SuppressWarnings("all")
public class ProductListComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductListComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.headline</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String HEADLINE = "headline";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.categoryCode</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String CATEGORYCODE = "categoryCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.productsFromContext</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTSFROMCONTEXT = "productsFromContext";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.searchQuery</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String SEARCHQUERY = "searchQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.pagination</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PAGINATION = "pagination";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.layout</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String LAYOUT = "layout";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.productCodes</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTCODES = "productCodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.products</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductListComponent.category</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String CATEGORY = "category";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductListComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductListComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _pagination initial attribute declared by type <code>ProductListComponent</code> at extension <code>cms2lib</code>
	 * @param _productsFromContext initial attribute declared by type <code>ProductListComponent</code> at extension <code>cms2lib</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ProductListComponentModel(final CatalogVersionModel _catalogVersion, final boolean _pagination, final boolean _productsFromContext, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setPagination(_pagination);
		setProductsFromContext(_productsFromContext);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pagination initial attribute declared by type <code>ProductListComponent</code> at extension <code>cms2lib</code>
	 * @param _productsFromContext initial attribute declared by type <code>ProductListComponent</code> at extension <code>cms2lib</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ProductListComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final boolean _pagination, final boolean _productsFromContext, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setPagination(_pagination);
		setProductsFromContext(_productsFromContext);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.category</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.GETTER)
	public CategoryModel getCategory()
	{
		return getPersistenceContext().getPropertyValue(CATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.categoryCode</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the categoryCode
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
	public String getCategoryCode()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.headline</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the headline
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
	public String getHeadline()
	{
		return getHeadline(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.headline</code> attribute defined at extension <code>cms2lib</code>. 
	 * @param loc the value localization key 
	 * @return the headline
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
	public String getHeadline(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(HEADLINE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.layout</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the layout
	 */
	@Accessor(qualifier = "layout", type = Accessor.Type.GETTER)
	public ProductListLayouts getLayout()
	{
		return getPersistenceContext().getPropertyValue(LAYOUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.productCodes</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productCodes
	 */
	@Accessor(qualifier = "productCodes", type = Accessor.Type.GETTER)
	public List<String> getProductCodes()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.products</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.searchQuery</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the searchQuery
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
	public String getSearchQuery()
	{
		return getSearchQuery(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.searchQuery</code> attribute defined at extension <code>cms2lib</code>. 
	 * @param loc the value localization key 
	 * @return the searchQuery
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
	public String getSearchQuery(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(SEARCHQUERY, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.pagination</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the pagination
	 */
	@Accessor(qualifier = "pagination", type = Accessor.Type.GETTER)
	public boolean isPagination()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PAGINATION));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductListComponent.productsFromContext</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the productsFromContext
	 */
	@Accessor(qualifier = "productsFromContext", type = Accessor.Type.GETTER)
	public boolean isProductsFromContext()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PRODUCTSFROMCONTEXT));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.category</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.SETTER)
	public void setCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(CATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.headline</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the headline
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
	public void setHeadline(final String value)
	{
		setHeadline(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.headline</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the headline
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
	public void setHeadline(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(HEADLINE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.layout</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the layout
	 */
	@Accessor(qualifier = "layout", type = Accessor.Type.SETTER)
	public void setLayout(final ProductListLayouts value)
	{
		getPersistenceContext().setPropertyValue(LAYOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.pagination</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the pagination
	 */
	@Accessor(qualifier = "pagination", type = Accessor.Type.SETTER)
	public void setPagination(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PAGINATION, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.products</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.productsFromContext</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the productsFromContext
	 */
	@Accessor(qualifier = "productsFromContext", type = Accessor.Type.SETTER)
	public void setProductsFromContext(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTSFROMCONTEXT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.searchQuery</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the searchQuery
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
	public void setSearchQuery(final String value)
	{
		setSearchQuery(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductListComponent.searchQuery</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the searchQuery
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
	public void setSearchQuery(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(SEARCHQUERY, loc, value);
	}
	
}
