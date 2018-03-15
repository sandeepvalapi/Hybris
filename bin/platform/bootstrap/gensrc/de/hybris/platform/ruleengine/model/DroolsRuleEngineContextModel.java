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
package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DroolsRuleEngineContext first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class DroolsRuleEngineContextModel extends AbstractRuleEngineContextModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DroolsRuleEngineContext";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsRuleEngineContext.kieSession</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIESESSION = "kieSession";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsRuleEngineContext.ruleFiringLimit</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULEFIRINGLIMIT = "ruleFiringLimit";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DroolsRuleEngineContextModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DroolsRuleEngineContextModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _kieSession initial attribute declared by type <code>DroolsRuleEngineContext</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>AbstractRuleEngineContext</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public DroolsRuleEngineContextModel(final DroolsKIESessionModel _kieSession, final String _name)
	{
		super();
		setKieSession(_kieSession);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _kieSession initial attribute declared by type <code>DroolsRuleEngineContext</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>AbstractRuleEngineContext</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DroolsRuleEngineContextModel(final DroolsKIESessionModel _kieSession, final String _name, final ItemModel _owner)
	{
		super();
		setKieSession(_kieSession);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsRuleEngineContext.kieSession</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the kieSession
	 */
	@Accessor(qualifier = "kieSession", type = Accessor.Type.GETTER)
	public DroolsKIESessionModel getKieSession()
	{
		return getPersistenceContext().getPropertyValue(KIESESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsRuleEngineContext.ruleFiringLimit</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the ruleFiringLimit
	 */
	@Accessor(qualifier = "ruleFiringLimit", type = Accessor.Type.GETTER)
	public Long getRuleFiringLimit()
	{
		return getPersistenceContext().getPropertyValue(RULEFIRINGLIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsRuleEngineContext.kieSession</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the kieSession
	 */
	@Accessor(qualifier = "kieSession", type = Accessor.Type.SETTER)
	public void setKieSession(final DroolsKIESessionModel value)
	{
		getPersistenceContext().setPropertyValue(KIESESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsRuleEngineContext.ruleFiringLimit</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the ruleFiringLimit
	 */
	@Accessor(qualifier = "ruleFiringLimit", type = Accessor.Type.SETTER)
	public void setRuleFiringLimit(final Long value)
	{
		getPersistenceContext().setPropertyValue(RULEFIRINGLIMIT, value);
	}
	
}
