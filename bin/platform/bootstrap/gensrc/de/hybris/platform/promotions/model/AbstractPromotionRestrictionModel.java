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
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractPromotionRestriction first defined at extension promotions.
 */
@SuppressWarnings("all")
public class AbstractPromotionRestrictionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractPromotionRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionRestriction.restrictionType</code> attribute defined at extension <code>promotions</code>. */
	public static final String RESTRICTIONTYPE = "restrictionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionRestriction.descriptionPattern</code> attribute defined at extension <code>promotions</code>. */
	public static final String DESCRIPTIONPATTERN = "descriptionPattern";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionRestriction.renderedDescription</code> attribute defined at extension <code>promotions</code>. */
	public static final String RENDEREDDESCRIPTION = "renderedDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionRestriction.promotion</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTION = "promotion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractPromotionRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractPromotionRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractPromotionRestrictionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.descriptionPattern</code> attribute defined at extension <code>promotions</code>. 
	 * @return the descriptionPattern - The description of the restriction (supports message pattern).
	 */
	@Accessor(qualifier = "descriptionPattern", type = Accessor.Type.GETTER)
	public String getDescriptionPattern()
	{
		return getDescriptionPattern(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.descriptionPattern</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the descriptionPattern - The description of the restriction (supports message pattern).
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "descriptionPattern", type = Accessor.Type.GETTER)
	public String getDescriptionPattern(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTIONPATTERN, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.promotion</code> attribute defined at extension <code>promotions</code>. 
	 * @return the promotion - The promotion that this restriction is part of
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
	public AbstractPromotionModel getPromotion()
	{
		return getPersistenceContext().getPropertyValue(PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.renderedDescription</code> attribute defined at extension <code>promotions</code>. 
	 * @return the renderedDescription - The readonly rendered description of this restriction.
	 */
	@Accessor(qualifier = "renderedDescription", type = Accessor.Type.GETTER)
	public String getRenderedDescription()
	{
		return getPersistenceContext().getPropertyValue(RENDEREDDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.restrictionType</code> attribute defined at extension <code>promotions</code>. 
	 * @return the restrictionType - The type of this restriction.
	 */
	@Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
	public String getRestrictionType()
	{
		return getRestrictionType(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionRestriction.restrictionType</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the restrictionType - The type of this restriction.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
	public String getRestrictionType(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(RESTRICTIONTYPE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionRestriction.descriptionPattern</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the descriptionPattern - The description of the restriction (supports message pattern).
	 */
	@Accessor(qualifier = "descriptionPattern", type = Accessor.Type.SETTER)
	public void setDescriptionPattern(final String value)
	{
		setDescriptionPattern(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionRestriction.descriptionPattern</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the descriptionPattern - The description of the restriction (supports message pattern).
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "descriptionPattern", type = Accessor.Type.SETTER)
	public void setDescriptionPattern(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTIONPATTERN, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionRestriction.promotion</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the promotion - The promotion that this restriction is part of
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
	public void setPromotion(final AbstractPromotionModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTION, value);
	}
	
}
