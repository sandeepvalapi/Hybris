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
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AbstractRuleBasedPromotionAction first defined at extension promotionengineservices.
 */
@SuppressWarnings("all")
public class AbstractRuleBasedPromotionActionModel extends AbstractPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractRuleBasedPromotionAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleBasedPromotionAction.rule</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String RULE = "rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleBasedPromotionAction.strategyId</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String STRATEGYID = "strategyId";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleBasedPromotionAction.metadataHandlers</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String METADATAHANDLERS = "metadataHandlers";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractRuleBasedPromotionAction.usedCouponCodes</code> attribute defined at extension <code>couponservices</code>. */
	public static final String USEDCOUPONCODES = "usedCouponCodes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractRuleBasedPromotionActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractRuleBasedPromotionActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractRuleBasedPromotionActionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleBasedPromotionAction.metadataHandlers</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the metadataHandlers
	 */
	@Accessor(qualifier = "metadataHandlers", type = Accessor.Type.GETTER)
	public Collection<String> getMetadataHandlers()
	{
		return getPersistenceContext().getPropertyValue(METADATAHANDLERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleBasedPromotionAction.rule</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the rule - The rule that created this promotion action.
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public AbstractRuleEngineRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleBasedPromotionAction.strategyId</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the strategyId - the strategyId (i.e. spring bean id) this action was created by.
	 */
	@Accessor(qualifier = "strategyId", type = Accessor.Type.GETTER)
	public String getStrategyId()
	{
		return getPersistenceContext().getPropertyValue(STRATEGYID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractRuleBasedPromotionAction.usedCouponCodes</code> attribute defined at extension <code>couponservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the usedCouponCodes
	 */
	@Accessor(qualifier = "usedCouponCodes", type = Accessor.Type.GETTER)
	public Collection<String> getUsedCouponCodes()
	{
		return getPersistenceContext().getPropertyValue(USEDCOUPONCODES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleBasedPromotionAction.metadataHandlers</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the metadataHandlers
	 */
	@Accessor(qualifier = "metadataHandlers", type = Accessor.Type.SETTER)
	public void setMetadataHandlers(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(METADATAHANDLERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleBasedPromotionAction.rule</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the rule - The rule that created this promotion action.
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final AbstractRuleEngineRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleBasedPromotionAction.strategyId</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the strategyId - the strategyId (i.e. spring bean id) this action was created by.
	 */
	@Accessor(qualifier = "strategyId", type = Accessor.Type.SETTER)
	public void setStrategyId(final String value)
	{
		getPersistenceContext().setPropertyValue(STRATEGYID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractRuleBasedPromotionAction.usedCouponCodes</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the usedCouponCodes
	 */
	@Accessor(qualifier = "usedCouponCodes", type = Accessor.Type.SETTER)
	public void setUsedCouponCodes(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(USEDCOUPONCODES, value);
	}
	
}
