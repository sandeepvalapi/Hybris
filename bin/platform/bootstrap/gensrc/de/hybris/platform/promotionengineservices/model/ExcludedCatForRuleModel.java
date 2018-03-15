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
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ExcludedCatForRule first defined at extension promotionengineservices.
 * <p>
 * Contains a relation between excluded category code and PromotionSourceRule in which that category code is used.
 */
@SuppressWarnings("all")
public class ExcludedCatForRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExcludedCatForRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExcludedCatForRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String CATEGORYCODE = "categoryCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExcludedCatForRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String RULE = "rule";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExcludedCatForRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExcludedCatForRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _categoryCode initial attribute declared by type <code>ExcludedCatForRule</code> at extension <code>promotionengineservices</code>
	 * @param _rule initial attribute declared by type <code>ExcludedCatForRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public ExcludedCatForRuleModel(final String _categoryCode, final PromotionSourceRuleModel _rule)
	{
		super();
		setCategoryCode(_categoryCode);
		setRule(_rule);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _categoryCode initial attribute declared by type <code>ExcludedCatForRule</code> at extension <code>promotionengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _rule initial attribute declared by type <code>ExcludedCatForRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public ExcludedCatForRuleModel(final String _categoryCode, final ItemModel _owner, final PromotionSourceRuleModel _rule)
	{
		super();
		setCategoryCode(_categoryCode);
		setOwner(_owner);
		setRule(_rule);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExcludedCatForRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the categoryCode
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
	public String getCategoryCode()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExcludedCatForRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the rule - PromotionSourceRule containing the excluded category code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public PromotionSourceRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ExcludedCatForRule.categoryCode</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the categoryCode
	 */
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.SETTER)
	public void setCategoryCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CATEGORYCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ExcludedCatForRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the rule - PromotionSourceRule containing the excluded category code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final PromotionSourceRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
}
