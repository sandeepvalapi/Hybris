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
package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type VariantProduct first defined at extension catalog.
 */
@SuppressWarnings("all")
public class VariantProductModel extends ProductModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "VariantProduct";
	
	/**<i>Generated relation code constant for relation <code>Product2VariantRelation</code> defining source attribute <code>baseProduct</code> in extension <code>catalog</code>.</i>*/
	public static final String _PRODUCT2VARIANTRELATION = "Product2VariantRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>VariantProduct.baseProduct</code> attribute defined at extension <code>catalog</code>. */
	public static final String BASEPRODUCT = "baseProduct";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VariantProductModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VariantProductModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseProduct initial attribute declared by type <code>VariantProduct</code> at extension <code>catalog</code>
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 */
	@Deprecated
	public VariantProductModel(final ProductModel _baseProduct, final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setBaseProduct(_baseProduct);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseProduct initial attribute declared by type <code>VariantProduct</code> at extension <code>catalog</code>
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public VariantProductModel(final ProductModel _baseProduct, final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setBaseProduct(_baseProduct);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VariantProduct.baseProduct</code> attribute defined at extension <code>catalog</code>. 
	 * @return the baseProduct
	 */
	@Accessor(qualifier = "baseProduct", type = Accessor.Type.GETTER)
	public ProductModel getBaseProduct()
	{
		return getPersistenceContext().getPropertyValue(BASEPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VariantProduct.baseProduct</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the baseProduct
	 */
	@Accessor(qualifier = "baseProduct", type = Accessor.Type.SETTER)
	public void setBaseProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(BASEPRODUCT, value);
	}
	
}
