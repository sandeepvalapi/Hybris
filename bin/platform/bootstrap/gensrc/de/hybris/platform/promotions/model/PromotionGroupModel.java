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
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type PromotionGroup first defined at extension promotions.
 */
@SuppressWarnings("all")
public class PromotionGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionGroup.Identifier</code> attribute defined at extension <code>promotions</code>. */
	public static final String IDENTIFIER = "Identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionGroup.Promotions</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONS = "Promotions";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionGroup.promotionSourceRules</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PROMOTIONSOURCERULES = "promotionSourceRules";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _Identifier initial attribute declared by type <code>PromotionGroup</code> at extension <code>promotions</code>
	 */
	@Deprecated
	public PromotionGroupModel(final String _Identifier)
	{
		super();
		setIdentifier(_Identifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _Identifier initial attribute declared by type <code>PromotionGroup</code> at extension <code>promotions</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PromotionGroupModel(final String _Identifier, final ItemModel _owner)
	{
		super();
		setIdentifier(_Identifier);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionGroup.Identifier</code> attribute defined at extension <code>promotions</code>. 
	 * @return the Identifier
	 */
	@Accessor(qualifier = "Identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionGroup.Promotions</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the Promotions
	 */
	@Accessor(qualifier = "Promotions", type = Accessor.Type.GETTER)
	public Collection<AbstractPromotionModel> getPromotions()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionGroup.promotionSourceRules</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the promotionSourceRules
	 */
	@Accessor(qualifier = "promotionSourceRules", type = Accessor.Type.GETTER)
	public Collection<PromotionSourceRuleModel> getPromotionSourceRules()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONSOURCERULES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionGroup.Identifier</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the Identifier
	 */
	@Accessor(qualifier = "Identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionGroup.Promotions</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the Promotions
	 */
	@Accessor(qualifier = "Promotions", type = Accessor.Type.SETTER)
	public void setPromotions(final Collection<AbstractPromotionModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionGroup.promotionSourceRules</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the promotionSourceRules
	 */
	@Accessor(qualifier = "promotionSourceRules", type = Accessor.Type.SETTER)
	public void setPromotionSourceRules(final Collection<PromotionSourceRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONSOURCERULES, value);
	}
	
}
