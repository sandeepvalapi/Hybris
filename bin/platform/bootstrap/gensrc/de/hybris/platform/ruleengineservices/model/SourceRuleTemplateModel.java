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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SourceRuleTemplate first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class SourceRuleTemplateModel extends AbstractRuleTemplateModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SourceRuleTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>SourceRuleTemplate.conditions</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String CONDITIONS = "conditions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SourceRuleTemplate.actions</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ACTIONS = "actions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SourceRuleTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SourceRuleTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleTemplate</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public SourceRuleTemplateModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRuleTemplate</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SourceRuleTemplateModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SourceRuleTemplate.actions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the actions - Rule consequences stored in a formatted String
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public String getActions()
	{
		return getPersistenceContext().getPropertyValue(ACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SourceRuleTemplate.conditions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the conditions - Rule conditions stored in a formatted String
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public String getConditions()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SourceRuleTemplate.actions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the actions - Rule consequences stored in a formatted String
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final String value)
	{
		getPersistenceContext().setPropertyValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SourceRuleTemplate.conditions</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the conditions - Rule conditions stored in a formatted String
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final String value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONS, value);
	}
	
}
