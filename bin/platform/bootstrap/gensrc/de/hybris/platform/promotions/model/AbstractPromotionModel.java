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
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * Generated model class for type AbstractPromotion first defined at extension promotions.
 */
@SuppressWarnings("all")
public class AbstractPromotionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractPromotion";
	
	/**<i>Generated relation code constant for relation <code>PromotionGroupPromotionsRelation</code> defining source attribute <code>PromotionGroup</code> in extension <code>promotions</code>.</i>*/
	public static final String _PROMOTIONGROUPPROMOTIONSRELATION = "PromotionGroupPromotionsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.promotionType</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONTYPE = "promotionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.code</code> attribute defined at extension <code>promotions</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.title</code> attribute defined at extension <code>promotions</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.description</code> attribute defined at extension <code>promotions</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.startDate</code> attribute defined at extension <code>promotions</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.endDate</code> attribute defined at extension <code>promotions</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.detailsURL</code> attribute defined at extension <code>promotions</code>. */
	public static final String DETAILSURL = "detailsURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.restrictions</code> attribute defined at extension <code>promotions</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.enabled</code> attribute defined at extension <code>promotions</code>. */
	public static final String ENABLED = "enabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.priority</code> attribute defined at extension <code>promotions</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.immutableKeyHash</code> attribute defined at extension <code>promotions</code>. */
	public static final String IMMUTABLEKEYHASH = "immutableKeyHash";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.immutableKey</code> attribute defined at extension <code>promotions</code>. */
	public static final String IMMUTABLEKEY = "immutableKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.PromotionGroup</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONGROUP = "PromotionGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotion.name</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String NAME = "name";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractPromotionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractPromotionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractPromotion</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public AbstractPromotionModel(final String _code)
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
	public AbstractPromotionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.code</code> attribute defined at extension <code>promotions</code>. 
	 * @return the code - Identifier for this promotion
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.description</code> attribute defined at extension <code>promotions</code>. 
	 * @return the description - Description of this promotion
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.detailsURL</code> attribute defined at extension <code>promotions</code>. 
	 * @return the detailsURL - URL to a content page with further details of this promotion
	 */
	@Accessor(qualifier = "detailsURL", type = Accessor.Type.GETTER)
	public String getDetailsURL()
	{
		return getPersistenceContext().getPropertyValue(DETAILSURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.enabled</code> attribute defined at extension <code>promotions</code>. 
	 * @return the enabled - Flag to indicate if this promotion is enabled.
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
	public Boolean getEnabled()
	{
		return getPersistenceContext().getPropertyValue(ENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.endDate</code> attribute defined at extension <code>promotions</code>. 
	 * @return the endDate - Date on which this promotion stops being available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.immutableKey</code> attribute defined at extension <code>promotions</code>. 
	 * @return the immutableKey - The full immutable identifier for this promotion
	 */
	@Accessor(qualifier = "immutableKey", type = Accessor.Type.GETTER)
	public String getImmutableKey()
	{
		return getPersistenceContext().getPropertyValue(IMMUTABLEKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.immutableKeyHash</code> attribute defined at extension <code>promotions</code>. 
	 * @return the immutableKeyHash - The HASH of the immutableKey
	 */
	@Accessor(qualifier = "immutableKeyHash", type = Accessor.Type.GETTER)
	public String getImmutableKeyHash()
	{
		return getPersistenceContext().getPropertyValue(IMMUTABLEKEYHASH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.name</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the name - Displayable name for this promotion
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.name</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Displayable name for this promotion
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.priority</code> attribute defined at extension <code>promotions</code>. 
	 * @return the priority - Value to indicate relative priority of promotions. The higher the value the higher the priority.
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.PromotionGroup</code> attribute defined at extension <code>promotions</code>. 
	 * @return the PromotionGroup
	 */
	@Accessor(qualifier = "PromotionGroup", type = Accessor.Type.GETTER)
	public PromotionGroupModel getPromotionGroup()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.promotionType</code> attribute defined at extension <code>promotions</code>. 
	 * @return the promotionType - The type of this promotion.
	 */
	@Accessor(qualifier = "promotionType", type = Accessor.Type.GETTER)
	public String getPromotionType()
	{
		return getPromotionType(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.promotionType</code> attribute defined at extension <code>promotions</code>. 
	 * @param loc the value localization key 
	 * @return the promotionType - The type of this promotion.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "promotionType", type = Accessor.Type.GETTER)
	public String getPromotionType(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(PROMOTIONTYPE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.restrictions</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions - Collection of restrictions that are applied to this promotion
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public Collection<AbstractPromotionRestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.startDate</code> attribute defined at extension <code>promotions</code>. 
	 * @return the startDate - Date on which this promotion becomes available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.title</code> attribute defined at extension <code>promotions</code>. 
	 * @return the title - Title for this promotion
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getPersistenceContext().getPropertyValue(TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.code</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the code - Identifier for this promotion
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.description</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the description - Description of this promotion
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.detailsURL</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the detailsURL - URL to a content page with further details of this promotion
	 */
	@Accessor(qualifier = "detailsURL", type = Accessor.Type.SETTER)
	public void setDetailsURL(final String value)
	{
		getPersistenceContext().setPropertyValue(DETAILSURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.enabled</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the enabled - Flag to indicate if this promotion is enabled.
	 */
	@Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
	public void setEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.endDate</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the endDate - Date on which this promotion stops being available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.immutableKey</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the immutableKey - The full immutable identifier for this promotion
	 */
	@Accessor(qualifier = "immutableKey", type = Accessor.Type.SETTER)
	public void setImmutableKey(final String value)
	{
		getPersistenceContext().setPropertyValue(IMMUTABLEKEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.immutableKeyHash</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the immutableKeyHash - The HASH of the immutableKey
	 */
	@Accessor(qualifier = "immutableKeyHash", type = Accessor.Type.SETTER)
	public void setImmutableKeyHash(final String value)
	{
		getPersistenceContext().setPropertyValue(IMMUTABLEKEYHASH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.name</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the name - Displayable name for this promotion
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.name</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the name - Displayable name for this promotion
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.priority</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the priority - Value to indicate relative priority of promotions. The higher the value the higher the priority.
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.PromotionGroup</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the PromotionGroup
	 */
	@Accessor(qualifier = "PromotionGroup", type = Accessor.Type.SETTER)
	public void setPromotionGroup(final PromotionGroupModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.restrictions</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the restrictions - Collection of restrictions that are applied to this promotion
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final Collection<AbstractPromotionRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.startDate</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the startDate - Date on which this promotion becomes available, if not set the promotion will not be available.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotion.title</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the title - Title for this promotion
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		getPersistenceContext().setPropertyValue(TITLE, value);
	}
	
}
