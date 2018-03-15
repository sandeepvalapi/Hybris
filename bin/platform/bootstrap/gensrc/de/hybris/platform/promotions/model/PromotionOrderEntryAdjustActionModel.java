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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionOrderEntryAdjustAction first defined at extension promotions.
 */
@SuppressWarnings("all")
public class PromotionOrderEntryAdjustActionModel extends AbstractPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionOrderEntryAdjustAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryAdjustAction.amount</code> attribute defined at extension <code>promotions</code>. */
	public static final String AMOUNT = "amount";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryAdjustAction.orderEntryProduct</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRYPRODUCT = "orderEntryProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryAdjustAction.orderEntryQuantity</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRYQUANTITY = "orderEntryQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryAdjustAction.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRYNUMBER = "orderEntryNumber";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionOrderEntryAdjustActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionOrderEntryAdjustActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PromotionOrderEntryAdjustActionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryAdjustAction.amount</code> attribute defined at extension <code>promotions</code>. 
	 * @return the amount - The amount to adjust the order entry by.
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
	public Double getAmount()
	{
		return getPersistenceContext().getPropertyValue(AMOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryAdjustAction.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntryNumber - The entry number in the order
	 */
	@Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.GETTER)
	public Integer getOrderEntryNumber()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRYNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryAdjustAction.orderEntryProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntryProduct - The product of the order entry to adjust
	 */
	@Accessor(qualifier = "orderEntryProduct", type = Accessor.Type.GETTER)
	public ProductModel getOrderEntryProduct()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRYPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryAdjustAction.orderEntryQuantity</code> attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntryQuantity - The quantity of the order entry to adjust
	 */
	@Accessor(qualifier = "orderEntryQuantity", type = Accessor.Type.GETTER)
	public Long getOrderEntryQuantity()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRYQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryAdjustAction.amount</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the amount - The amount to adjust the order entry by.
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
	public void setAmount(final Double value)
	{
		getPersistenceContext().setPropertyValue(AMOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryAdjustAction.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the orderEntryNumber - The entry number in the order
	 */
	@Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.SETTER)
	public void setOrderEntryNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRYNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryAdjustAction.orderEntryProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the orderEntryProduct - The product of the order entry to adjust
	 */
	@Accessor(qualifier = "orderEntryProduct", type = Accessor.Type.SETTER)
	public void setOrderEntryProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRYPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryAdjustAction.orderEntryQuantity</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the orderEntryQuantity - The quantity of the order entry to adjust
	 */
	@Accessor(qualifier = "orderEntryQuantity", type = Accessor.Type.SETTER)
	public void setOrderEntryQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRYQUANTITY, value);
	}
	
}
