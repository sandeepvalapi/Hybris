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
 * Generated model class for type ProductPerfectPartnerBundlePromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class ProductPerfectPartnerBundlePromotionModel extends ProductPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductPerfectPartnerBundlePromotion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.baseProduct</code> attribute defined at extension <code>promotions</code>. */
	public static final String BASEPRODUCT = "baseProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. */
	public static final String PARTNERPRODUCTS = "partnerProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. */
	public static final String QUALIFYINGCOUNT = "qualifyingCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.bundlePrices</code> attribute defined at extension <code>promotions</code>. */
	public static final String BUNDLEPRICES = "bundlePrices";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.messageFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductPerfectPartnerBundlePromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductPerfectPartnerBundlePromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductPerfectPartnerBundlePromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public ProductPerfectPartnerBundlePromotionModel(final String _code)
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
	public ProductPerfectPartnerBundlePromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.baseProduct</code> attribute defined at extension <code>promotions</code>. 
	 * @return the baseProduct - The base product.
	 */
	@Accessor(qualifier = "baseProduct", type = Accessor.Type.GETTER)
	public ProductModel getBaseProduct()
	{
		return getPersistenceContext().getPropertyValue(BASEPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.bundlePrices</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the bundlePrices - Fixed bundle price for the base product and selected partner products in specific currencies.
	 */
	@Accessor(qualifier = "bundlePrices", type = Accessor.Type.GETTER)
	public Collection<PromotionPriceRowModel> getBundlePrices()
	{
		return getPersistenceContext().getPropertyValue(BUNDLEPRICES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired()
	{
		return getMessageCouldHaveFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the partnerProducts - The collections of potential partner products.
	 */
	@Accessor(qualifier = "partnerProducts", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getPartnerProducts()
	{
		return getPersistenceContext().getPropertyValue(PARTNERPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductPerfectPartnerBundlePromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. 
	 * @return the qualifyingCount - The number of partner products required to qualify for the promotion.
	 */
	@Accessor(qualifier = "qualifyingCount", type = Accessor.Type.GETTER)
	public Integer getQualifyingCount()
	{
		return getPersistenceContext().getPropertyValue(QUALIFYINGCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.baseProduct</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the baseProduct - The base product.
	 */
	@Accessor(qualifier = "baseProduct", type = Accessor.Type.SETTER)
	public void setBaseProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(BASEPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.bundlePrices</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the bundlePrices - Fixed bundle price for the base product and selected partner products in specific currencies.
	 */
	@Accessor(qualifier = "bundlePrices", type = Accessor.Type.SETTER)
	public void setBundlePrices(final Collection<PromotionPriceRowModel> value)
	{
		getPersistenceContext().setPropertyValue(BUNDLEPRICES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value)
	{
		setMessageCouldHaveFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.partnerProducts</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the partnerProducts - The collections of potential partner products.
	 */
	@Accessor(qualifier = "partnerProducts", type = Accessor.Type.SETTER)
	public void setPartnerProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PARTNERPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductPerfectPartnerBundlePromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the qualifyingCount - The number of partner products required to qualify for the promotion.
	 */
	@Accessor(qualifier = "qualifyingCount", type = Accessor.Type.SETTER)
	public void setQualifyingCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(QUALIFYINGCOUNT, value);
	}
	
}
