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
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type OrderThresholdFreeVoucherPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class OrderThresholdFreeVoucherPromotionModel extends OrderPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderThresholdFreeVoucherPromotion";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdFreeVoucherPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. */
	public static final String THRESHOLDTOTALS = "thresholdTotals";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdFreeVoucherPromotion.freeVoucher</code> attribute defined at extension <code>promotions</code>. */
	public static final String FREEVOUCHER = "freeVoucher";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdFreeVoucherPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdFreeVoucherPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderThresholdFreeVoucherPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderThresholdFreeVoucherPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public OrderThresholdFreeVoucherPromotionModel(final String _code)
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
	public OrderThresholdFreeVoucherPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.freeVoucher</code> attribute defined at extension <code>promotions</code>. 
	 * @return the freeVoucher - The voucher.
	 */
	@Accessor(qualifier = "freeVoucher", type = Accessor.Type.GETTER)
	public VoucherModel getFreeVoucher()
	{
		return getPersistenceContext().getPropertyValue(FREEVOUCHER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired()
	{
		return getMessageCouldHaveFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGECOULDHAVEFIRED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the messageFired - The message to show when the promotion has fired.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGEFIRED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdFreeVoucherPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the thresholdTotals - The cart total value threshold in specific currencies.
	 */
	@Accessor(qualifier = "thresholdTotals", type = Accessor.Type.GETTER)
	public Collection<PromotionPriceRowModel> getThresholdTotals()
	{
		return getPersistenceContext().getPropertyValue(THRESHOLDTOTALS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.freeVoucher</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the freeVoucher - The voucher.
	 */
	@Accessor(qualifier = "freeVoucher", type = Accessor.Type.SETTER)
	public void setFreeVoucher(final VoucherModel value)
	{
		getPersistenceContext().setPropertyValue(FREEVOUCHER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value)
	{
		setMessageCouldHaveFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGECOULDHAVEFIRED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGEFIRED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdFreeVoucherPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the thresholdTotals - The cart total value threshold in specific currencies.
	 */
	@Accessor(qualifier = "thresholdTotals", type = Accessor.Type.SETTER)
	public void setThresholdTotals(final Collection<PromotionPriceRowModel> value)
	{
		getPersistenceContext().setPropertyValue(THRESHOLDTOTALS, value);
	}
	
}
