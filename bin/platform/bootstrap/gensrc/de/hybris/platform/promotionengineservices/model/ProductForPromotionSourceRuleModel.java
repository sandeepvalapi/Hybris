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
 * Generated model class for type ProductForPromotionSourceRule first defined at extension promotionengineservices.
 * <p>
 * Contains a relation between product code and PromotionSourceRule in which that product code is used.
 */
@SuppressWarnings("all")
public class ProductForPromotionSourceRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductForPromotionSourceRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductForPromotionSourceRule.productCode</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PRODUCTCODE = "productCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String RULE = "rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String PROMOTION = "promotion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductForPromotionSourceRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductForPromotionSourceRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _productCode initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _promotion initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _rule initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public ProductForPromotionSourceRuleModel(final String _productCode, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setProductCode(_productCode);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _productCode initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _promotion initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 * @param _rule initial attribute declared by type <code>ProductForPromotionSourceRule</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public ProductForPromotionSourceRuleModel(final ItemModel _owner, final String _productCode, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setOwner(_owner);
		setProductCode(_productCode);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductForPromotionSourceRule.productCode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the productCode - product code used in the given PromotionSourceRule
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
	public String getProductCode()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the promotion - RuleBasedPromotion related to the rule
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
	public RuleBasedPromotionModel getPromotion()
	{
		return getPersistenceContext().getPropertyValue(PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the rule - PromotionSourceRule containing the product code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public PromotionSourceRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductForPromotionSourceRule.productCode</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the productCode - product code used in the given PromotionSourceRule
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
	public void setProductCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductForPromotionSourceRule.promotion</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the promotion - RuleBasedPromotion related to the rule
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
	public void setPromotion(final RuleBasedPromotionModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductForPromotionSourceRule.rule</code> attribute defined at extension <code>promotionengineservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the rule - PromotionSourceRule containing the product code
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final PromotionSourceRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
}
