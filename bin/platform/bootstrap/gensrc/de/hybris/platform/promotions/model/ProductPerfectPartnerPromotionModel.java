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
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type ProductPerfectPartnerPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class ProductPerfectPartnerPromotionModel extends ProductPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductPerfectPartnerPromotion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerPromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. */
	public static final String PARTNERPRODUCTS = "partnerProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerPromotion.partnerPrices</code> attribute defined at extension <code>promotions</code>. */
	public static final String PARTNERPRICES = "partnerPrices";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductPerfectPartnerPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductPerfectPartnerPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public ProductPerfectPartnerPromotionModel(final String _code)
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
	public ProductPerfectPartnerPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired()
	{
		return getMessageCouldHaveFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.partnerPrices</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the partnerPrices - Fixed price for a partner product in specific currencies.
	 */
	@Accessor(qualifier = "partnerPrices", type = Accessor.Type.GETTER)
	public Collection<PromotionPriceRowModel> getPartnerPrices()
	{
		return getPersistenceContext().getPropertyValue(PARTNERPRICES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerPromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the partnerProducts - The collections of products to discount if one of them is in the cart.
	 */
	@Accessor(qualifier = "partnerProducts", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getPartnerProducts()
	{
		return getPersistenceContext().getPropertyValue(PARTNERPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value)
	{
		setMessageCouldHaveFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.partnerPrices</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the partnerPrices - Fixed price for a partner product in specific currencies.
	 */
	@Accessor(qualifier = "partnerPrices", type = Accessor.Type.SETTER)
	public void setPartnerPrices(final Collection<PromotionPriceRowModel> value)
	{
		getPersistenceContext().setPropertyValue(PARTNERPRICES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerPromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the partnerProducts - The collections of products to discount if one of them is in the cart.
	 */
	@Accessor(qualifier = "partnerProducts", type = Accessor.Type.SETTER)
	public void setPartnerProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PARTNERPRODUCTS, value);
	}
	
}
