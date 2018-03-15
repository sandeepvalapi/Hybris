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
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type OrderThresholdPerfectPartnerPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class OrderThresholdPerfectPartnerPromotionModel extends OrderPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderThresholdPerfectPartnerPromotion";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. */
	public static final String THRESHOLDTOTALS = "thresholdTotals";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.discountProduct</code> attribute defined at extension <code>promotions</code>. */
	public static final String DISCOUNTPRODUCT = "discountProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.productPrices</code> attribute defined at extension <code>promotions</code>. */
	public static final String PRODUCTPRICES = "productPrices";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.includeDiscountedPriceInThreshold</code> attribute defined at extension <code>promotions</code>. */
	public static final String INCLUDEDISCOUNTEDPRICEINTHRESHOLD = "includeDiscountedPriceInThreshold";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.messageProductNoThreshold</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEPRODUCTNOTHRESHOLD = "messageProductNoThreshold";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderThresholdPerfectPartnerPromotion.messageThresholdNoProduct</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGETHRESHOLDNOPRODUCT = "messageThresholdNoProduct";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderThresholdPerfectPartnerPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderThresholdPerfectPartnerPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public OrderThresholdPerfectPartnerPromotionModel(final String _code)
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
	public OrderThresholdPerfectPartnerPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.discountProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @return the discountProduct - The product to discount if it is in the cart.
	 */
	@Accessor(qualifier = "discountProduct", type = Accessor.Type.GETTER)
	public ProductModel getDiscountProduct()
	{
		return getPersistenceContext().getPropertyValue(DISCOUNTPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.includeDiscountedPriceInThreshold</code> attribute defined at extension <code>promotions</code>. 
	 * @return the includeDiscountedPriceInThreshold - Flag to indicate if the discounted price of the product is allowed to count towards the threshold, otherwise it does not count towards the threshold.
	 */
	@Accessor(qualifier = "includeDiscountedPriceInThreshold", type = Accessor.Type.GETTER)
	public Boolean getIncludeDiscountedPriceInThreshold()
	{
		return getPersistenceContext().getPropertyValue(INCLUDEDISCOUNTEDPRICEINTHRESHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired()
	{
		return getMessageCouldHaveFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageProductNoThreshold</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageProductNoThreshold - The message to show when there is a partner product but the threshold has not been reached.
	 */
	@Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.GETTER)
	public String getMessageProductNoThreshold()
	{
		return getMessageProductNoThreshold(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageProductNoThreshold</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the messageProductNoThreshold - The message to show when there is a partner product but the threshold has not been reached.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.GETTER)
	public String getMessageProductNoThreshold(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGEPRODUCTNOTHRESHOLD, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageThresholdNoProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageThresholdNoProduct - The message to show when the threshold is reached, but there is no partner product.
	 */
	@Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.GETTER)
	public String getMessageThresholdNoProduct()
	{
		return getMessageThresholdNoProduct(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.messageThresholdNoProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the messageThresholdNoProduct - The message to show when the threshold is reached, but there is no partner product.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.GETTER)
	public String getMessageThresholdNoProduct(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGETHRESHOLDNOPRODUCT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.productPrices</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productPrices - Fixed price for discounted product in specific currencies.
	 */
	@Accessor(qualifier = "productPrices", type = Accessor.Type.GETTER)
	public Collection<PromotionPriceRowModel> getProductPrices()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTPRICES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderThresholdPerfectPartnerPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the thresholdTotals - The cart total value threshold in specific currencies.
	 */
	@Accessor(qualifier = "thresholdTotals", type = Accessor.Type.GETTER)
	public Collection<PromotionPriceRowModel> getThresholdTotals()
	{
		return getPersistenceContext().getPropertyValue(THRESHOLDTOTALS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.discountProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the discountProduct - The product to discount if it is in the cart.
	 */
	@Accessor(qualifier = "discountProduct", type = Accessor.Type.SETTER)
	public void setDiscountProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(DISCOUNTPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.includeDiscountedPriceInThreshold</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the includeDiscountedPriceInThreshold - Flag to indicate if the discounted price of the product is allowed to count towards the threshold, otherwise it does not count towards the threshold.
	 */
	@Accessor(qualifier = "includeDiscountedPriceInThreshold", type = Accessor.Type.SETTER)
	public void setIncludeDiscountedPriceInThreshold(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLUDEDISCOUNTEDPRICEINTHRESHOLD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value)
	{
		setMessageCouldHaveFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageProductNoThreshold</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageProductNoThreshold - The message to show when there is a partner product but the threshold has not been reached.
	 */
	@Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.SETTER)
	public void setMessageProductNoThreshold(final String value)
	{
		setMessageProductNoThreshold(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageProductNoThreshold</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageProductNoThreshold - The message to show when there is a partner product but the threshold has not been reached.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageProductNoThreshold", type = Accessor.Type.SETTER)
	public void setMessageProductNoThreshold(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGEPRODUCTNOTHRESHOLD, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageThresholdNoProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageThresholdNoProduct - The message to show when the threshold is reached, but there is no partner product.
	 */
	@Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.SETTER)
	public void setMessageThresholdNoProduct(final String value)
	{
		setMessageThresholdNoProduct(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.messageThresholdNoProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageThresholdNoProduct - The message to show when the threshold is reached, but there is no partner product.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "messageThresholdNoProduct", type = Accessor.Type.SETTER)
	public void setMessageThresholdNoProduct(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGETHRESHOLDNOPRODUCT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.productPrices</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the productPrices - Fixed price for discounted product in specific currencies.
	 */
	@Accessor(qualifier = "productPrices", type = Accessor.Type.SETTER)
	public void setProductPrices(final Collection<PromotionPriceRowModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTPRICES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderThresholdPerfectPartnerPromotion.thresholdTotals</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the thresholdTotals - The cart total value threshold in specific currencies.
	 */
	@Accessor(qualifier = "thresholdTotals", type = Accessor.Type.SETTER)
	public void setThresholdTotals(final Collection<PromotionPriceRowModel> value)
	{
		getPersistenceContext().setPropertyValue(THRESHOLDTOTALS, value);
	}
	
}
