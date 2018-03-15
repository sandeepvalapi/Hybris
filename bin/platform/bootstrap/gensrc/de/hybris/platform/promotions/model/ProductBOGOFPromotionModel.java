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
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type ProductBOGOFPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class ProductBOGOFPromotionModel extends ProductPromotionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductBOGOFPromotion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductBOGOFPromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. */
	public static final String QUALIFYINGCOUNT = "qualifyingCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductBOGOFPromotion.freeCount</code> attribute defined at extension <code>promotions</code>. */
	public static final String FREECOUNT = "freeCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductBOGOFPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGEFIRED = "messageFired";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductBOGOFPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. */
	public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductBOGOFPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductBOGOFPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public ProductBOGOFPromotionModel(final String _code)
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
	public ProductBOGOFPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.freeCount</code> attribute defined at extension <code>promotions</code>. 
	 * @return the freeCount - The number of products within the cart to give away free. (For standard BOGOF this is 1).
	 */
	@Accessor(qualifier = "freeCount", type = Accessor.Type.GETTER)
	public Integer getFreeCount()
	{
		return getPersistenceContext().getPropertyValue(FREECOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.GETTER)
	public String getMessageCouldHaveFired()
	{
		return getMessageCouldHaveFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 * @return the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
	public String getMessageFired()
	{
		return getMessageFired(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ProductBOGOFPromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. 
	 * @return the qualifyingCount - The number of products required in the cart to activate the promotion. (For standard BOGOF this is 2).
	 */
	@Accessor(qualifier = "qualifyingCount", type = Accessor.Type.GETTER)
	public Integer getQualifyingCount()
	{
		return getPersistenceContext().getPropertyValue(QUALIFYINGCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.freeCount</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the freeCount - The number of products within the cart to give away free. (For standard BOGOF this is 1).
	 */
	@Accessor(qualifier = "freeCount", type = Accessor.Type.SETTER)
	public void setFreeCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FREECOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageCouldHaveFired - The message to show when the promotion could have potentially fire.
	 */
	@Accessor(qualifier = "messageCouldHaveFired", type = Accessor.Type.SETTER)
	public void setMessageCouldHaveFired(final String value)
	{
		setMessageCouldHaveFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.messageCouldHaveFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the messageFired - The message to show when the promotion has fired.
	 */
	@Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
	public void setMessageFired(final String value)
	{
		setMessageFired(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.messageFired</code> attribute defined at extension <code>promotions</code>. 
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
	 * <i>Generated method</i> - Setter of <code>ProductBOGOFPromotion.qualifyingCount</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the qualifyingCount - The number of products required in the cart to activate the promotion. (For standard BOGOF this is 2).
	 */
	@Accessor(qualifier = "qualifyingCount", type = Accessor.Type.SETTER)
	public void setQualifyingCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(QUALIFYINGCOUNT, value);
	}
	
}
