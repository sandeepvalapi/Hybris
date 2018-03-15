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
package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatForPromotionSourceRule first defined at extension promotionengineservices.
 * <p>
 * Contains a relation between category code and PromotionSourceRule in which that category code is used.
 */
@SuppressWarnings("all")
public class CatForPromotionSourceRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatForPromotionSourceRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatForPromotionSourceRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String CATEGORYCODE = "categoryCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String RULE = "rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PROMOTION = "promotion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatForPromotionSourceRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatForPromotionSourceRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _categoryCode initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _promotion initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _rule initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public CatForPromotionSourceRuleModel(final String _categoryCode, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setCategoryCode(_categoryCode);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _categoryCode initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _promotion initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _rule initial attribute declared by type <code>CatForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public CatForPromotionSourceRuleModel(final String _categoryCode, final ItemModel _owner, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setCategoryCode(_categoryCode);
		setOwner(_owner);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatForPromotionSourceRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the categoryCode - category code used in the given PromotionSourceRule
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
	public String getCategoryCode()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the promotion - RuleBasedPromotion related to the rule
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
	public RuleBasedPromotionModel getPromotion()
	{
		return getPersistenceContext().getPropertyValue(PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the rule - PromotionSourceRule containing the category code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public PromotionSourceRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatForPromotionSourceRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the categoryCode - category code used in the given PromotionSourceRule
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.SETTER)
	public void setCategoryCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CATEGORYCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the promotion - RuleBasedPromotion related to the rule
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
	public void setPromotion(final RuleBasedPromotionModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the rule - PromotionSourceRule containing the category code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final PromotionSourceRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
}
