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
package de.hybris.platform.europe1.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type GlobalDiscountRow first defined at extension europe1.
 */
@SuppressWarnings("all")
public class GlobalDiscountRowModel extends AbstractDiscountRowModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "GlobalDiscountRow";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public GlobalDiscountRowModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public GlobalDiscountRowModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _discount initial attribute declared by type <code>AbstractDiscountRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public GlobalDiscountRowModel(final DiscountModel _discount)
	{
		super();
		setDiscount(_discount);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _discount initial attribute declared by type <code>AbstractDiscountRow</code> at extension <code>europe1</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pg initial attribute declared by type <code>GlobalDiscountRow</code> at extension <code>europe1</code>
	 * @param _product initial attribute declared by type <code>GlobalDiscountRow</code> at extension <code>europe1</code>
	 * @param _productId initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public GlobalDiscountRowModel(final DiscountModel _discount, final ItemModel _owner, final ProductDiscountGroup _pg, final ProductModel _product, final String _productId)
	{
		super();
		setDiscount(_discount);
		setOwner(_owner);
		setPg(_pg);
		setProduct(_product);
		setProductId(_productId);
	}
	
	
}
