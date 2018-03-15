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
package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type ProductPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class ProductPromotionModel extends AbstractPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductPromotion";
	
	/**<i>Generated relation code constant for relation <code>ProductPromotionRelation</code> defining source attribute <code>products</code> in extension <code>promotions</code>.</i>*/
	public static final String _PRODUCTPROMOTIONRELATION = "ProductPromotionRelation";
	
	/**<i>Generated relation code constant for relation <code>CategoryPromotionRelation</code> defining source attribute <code>categories</code> in extension <code>promotions</code>.</i>*/
	public static final String _CATEGORYPROMOTIONRELATION = "CategoryPromotionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPromotion.productBanner</code> attribute defined at extension <code>promotions</code>. */
	public static final String PRODUCTBANNER = "productBanner";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPromotion.products</code> attribute defined at extension <code>promotions</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPromotion.categories</code> attribute defined at extension <code>promotions</code>. */
	public static final String CATEGORIES = "categories";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public ProductPromotionModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ProductPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPromotion.categories</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getCategories()
	{
		return getPersistenceContext().getPropertyValue(CATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPromotion.productBanner</code> attribute defined at extension <code>promotions</code>. 
	 * @return the productBanner - Media to display on the product page when this promotion is available.
	 */
	@Accessor(qualifier = "productBanner", type = Accessor.Type.GETTER)
	public MediaModel getProductBanner()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTBANNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPromotion.products</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPromotion.categories</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final Collection<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPromotion.productBanner</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the productBanner - Media to display on the product page when this promotion is available.
	 */
	@Accessor(qualifier = "productBanner", type = Accessor.Type.SETTER)
	public void setProductBanner(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTBANNER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPromotion.products</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
}
