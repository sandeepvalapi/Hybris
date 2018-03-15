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
package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RuleBasedOrderAddProductAction first defined at extension promotionengineservices.
 */
@SuppressWarnings("all")
public class RuleBasedOrderAddProductActionModel extends AbstractRuleBasedPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleBasedOrderAddProductAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderAddProductAction.product</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderAddProductAction.quantity</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String QUANTITY = "quantity";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleBasedOrderAddProductActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleBasedOrderAddProductActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>RuleBasedOrderAddProductAction</code> at extension <code>promotionengineservices</code>
	 * @param _quantity initial attribute declared by type <code>RuleBasedOrderAddProductAction</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public RuleBasedOrderAddProductActionModel(final ProductModel _product, final Long _quantity)
	{
		super();
		setProduct(_product);
		setQuantity(_quantity);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>RuleBasedOrderAddProductAction</code> at extension <code>promotionengineservices</code>
	 * @param _quantity initial attribute declared by type <code>RuleBasedOrderAddProductAction</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public RuleBasedOrderAddProductActionModel(final ItemModel _owner, final ProductModel _product, final Long _quantity)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setQuantity(_quantity);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderAddProductAction.product</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the product - The product to be added
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderAddProductAction.quantity</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the quantity - The quantity to be added
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderAddProductAction.product</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the product - The product to be added
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderAddProductAction.quantity</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the quantity - The quantity to be added
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
}
