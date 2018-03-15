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
package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.campaigns.model.CampaignModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type SourceRule first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class SourceRuleModel extends AbstractRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SourceRule";
	
	/**<i>Generated relation code constant for relation <code>Campaign2SourceRuleRelation</code> defining source attribute <code>campaigns</code> in extension <code>ruleengineservices</code>.</i>*/
	public static final String _CAMPAIGN2SOURCERULERELATION = "Campaign2SourceRuleRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SourceRule.conditions</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CONDITIONS = "conditions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SourceRule.actions</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ACTIONS = "actions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SourceRule.campaigns</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CAMPAIGNS = "campaigns";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SourceRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SourceRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public SourceRuleModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uuid initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public SourceRuleModel(final String _code, final ItemModel _owner, final String _uuid)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setUuid(_uuid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SourceRule.actions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the actions - Rule consequences stored in a formatted String
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public String getActions()
	{
		return getPersistenceContext().getPropertyValue(ACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SourceRule.campaigns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the campaigns
	 */
	@Accessor(qualifier = "campaigns", type = Accessor.Type.GETTER)
	public Set<CampaignModel> getCampaigns()
	{
		return getPersistenceContext().getPropertyValue(CAMPAIGNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SourceRule.conditions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the conditions - Rule conditions stored in a formatted String
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public String getConditions()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SourceRule.actions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the actions - Rule consequences stored in a formatted String
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final String value)
	{
		getPersistenceContext().setPropertyValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SourceRule.campaigns</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the campaigns
	 */
	@Accessor(qualifier = "campaigns", type = Accessor.Type.SETTER)
	public void setCampaigns(final Set<CampaignModel> value)
	{
		getPersistenceContext().setPropertyValue(CAMPAIGNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SourceRule.conditions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the conditions - Rule conditions stored in a formatted String
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final String value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONS, value);
	}
	
}
