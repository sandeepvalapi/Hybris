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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionOrderEntryConsumed first defined at extension promotions.
 */
@SuppressWarnings("all")
public class PromotionOrderEntryConsumedModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionOrderEntryConsumed";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.code</code> attribute defined at extension <code>promotions</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.promotionResult</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONRESULT = "promotionResult";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.quantity</code> attribute defined at extension <code>promotions</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.adjustedUnitPrice</code> attribute defined at extension <code>promotions</code>. */
	public static final String ADJUSTEDUNITPRICE = "adjustedUnitPrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.orderEntry</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRY = "orderEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRYNUMBER = "orderEntryNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionOrderEntryConsumed.orderEntryNumberWithFallback</code> attribute defined at extension <code>promotions</code>. */
	public static final String ORDERENTRYNUMBERWITHFALLBACK = "orderEntryNumberWithFallback";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionOrderEntryConsumedModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionOrderEntryConsumedModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PromotionOrderEntryConsumedModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.adjustedUnitPrice</code> attribute defined at extension <code>promotions</code>. 
	 * @return the adjustedUnitPrice - The quantity.
	 */
	@Accessor(qualifier = "adjustedUnitPrice", type = Accessor.Type.GETTER)
	public Double getAdjustedUnitPrice()
	{
		return getPersistenceContext().getPropertyValue(ADJUSTEDUNITPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.code</code> attribute defined at extension <code>promotions</code>. 
	 * @return the code - The code for this consumed entry
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.orderEntry</code> attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntry - The order entry.
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
	public AbstractOrderEntryModel getOrderEntry()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntryNumber - The order entry number.
	 */
	@Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.GETTER)
	public Integer getOrderEntryNumber()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRYNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.orderEntryNumberWithFallback</code> dynamic attribute defined at extension <code>promotions</code>. 
	 * @return the orderEntryNumberWithFallback
	 */
	@Accessor(qualifier = "orderEntryNumberWithFallback", type = Accessor.Type.GETTER)
	public Integer getOrderEntryNumberWithFallback()
	{
		return getPersistenceContext().getDynamicValue(this,ORDERENTRYNUMBERWITHFALLBACK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.promotionResult</code> attribute defined at extension <code>promotions</code>. 
	 * @return the promotionResult - The promotion result.
	 */
	@Accessor(qualifier = "promotionResult", type = Accessor.Type.GETTER)
	public PromotionResultModel getPromotionResult()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONRESULT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionOrderEntryConsumed.quantity</code> attribute defined at extension <code>promotions</code>. 
	 * @return the quantity - The quantity.
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.adjustedUnitPrice</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the adjustedUnitPrice - The quantity.
	 */
	@Accessor(qualifier = "adjustedUnitPrice", type = Accessor.Type.SETTER)
	public void setAdjustedUnitPrice(final Double value)
	{
		getPersistenceContext().setPropertyValue(ADJUSTEDUNITPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.code</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the code - The code for this consumed entry
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.orderEntry</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the orderEntry - The order entry.
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
	public void setOrderEntry(final AbstractOrderEntryModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.orderEntryNumber</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the orderEntryNumber - The order entry number.
	 */
	@Accessor(qualifier = "orderEntryNumber", type = Accessor.Type.SETTER)
	public void setOrderEntryNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRYNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.promotionResult</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the promotionResult - The promotion result.
	 */
	@Accessor(qualifier = "promotionResult", type = Accessor.Type.SETTER)
	public void setPromotionResult(final PromotionResultModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONRESULT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionOrderEntryConsumed.quantity</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the quantity - The quantity.
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
}
