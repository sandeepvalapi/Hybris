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
import de.hybris.platform.ruleengine.enums.DroolsEqualityBehavior;
import de.hybris.platform.ruleengine.enums.DroolsEventProcessingMode;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type DroolsKIEBase first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class DroolsKIEBaseModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DroolsKIEBase";
	
	/**<i>Generated relation code constant for relation <code>DroolsKIEModule2Base</code> defining source attribute <code>kieModule</code> in extension <code>ruleengine</code>.</i>*/
	public static final String _DROOLSKIEMODULE2BASE = "DroolsKIEModule2Base";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.name</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.equalityBehavior</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String EQUALITYBEHAVIOR = "equalityBehavior";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.eventProcessingMode</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String EVENTPROCESSINGMODE = "eventProcessingMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.defaultKIESession</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String DEFAULTKIESESSION = "defaultKIESession";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.kieModule</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIEMODULE = "kieModule";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.kieSessions</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIESESSIONS = "kieSessions";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEBase.rules</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String RULES = "rules";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DroolsKIEBaseModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DroolsKIEBaseModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _kieModule initial attribute declared by type <code>DroolsKIEBase</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>DroolsKIEBase</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public DroolsKIEBaseModel(final DroolsKIEModuleModel _kieModule, final String _name)
	{
		super();
		setKieModule(_kieModule);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _kieModule initial attribute declared by type <code>DroolsKIEBase</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>DroolsKIEBase</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DroolsKIEBaseModel(final DroolsKIEModuleModel _kieModule, final String _name, final ItemModel _owner)
	{
		super();
		setKieModule(_kieModule);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.defaultKIESession</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the defaultKIESession - Default DroolsKIESession for DroolsKIEBase.
	 */
	@Accessor(qualifier = "defaultKIESession", type = Accessor.Type.GETTER)
	public DroolsKIESessionModel getDefaultKIESession()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTKIESESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.equalityBehavior</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the equalityBehavior
	 */
	@Accessor(qualifier = "equalityBehavior", type = Accessor.Type.GETTER)
	public DroolsEqualityBehavior getEqualityBehavior()
	{
		return getPersistenceContext().getPropertyValue(EQUALITYBEHAVIOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.eventProcessingMode</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the eventProcessingMode
	 */
	@Accessor(qualifier = "eventProcessingMode", type = Accessor.Type.GETTER)
	public DroolsEventProcessingMode getEventProcessingMode()
	{
		return getPersistenceContext().getPropertyValue(EVENTPROCESSINGMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.kieModule</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the kieModule
	 */
	@Accessor(qualifier = "kieModule", type = Accessor.Type.GETTER)
	public DroolsKIEModuleModel getKieModule()
	{
		return getPersistenceContext().getPropertyValue(KIEMODULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.kieSessions</code> attribute defined at extension <code>ruleengine</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the kieSessions
	 */
	@Accessor(qualifier = "kieSessions", type = Accessor.Type.GETTER)
	public Collection<DroolsKIESessionModel> getKieSessions()
	{
		return getPersistenceContext().getPropertyValue(KIESESSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.name</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEBase.rules</code> attribute defined at extension <code>ruleengine</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rules
	 */
	@Accessor(qualifier = "rules", type = Accessor.Type.GETTER)
	public Set<DroolsRuleModel> getRules()
	{
		return getPersistenceContext().getPropertyValue(RULES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEBase.defaultKIESession</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the defaultKIESession - Default DroolsKIESession for DroolsKIEBase.
	 */
	@Accessor(qualifier = "defaultKIESession", type = Accessor.Type.SETTER)
	public void setDefaultKIESession(final DroolsKIESessionModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTKIESESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEBase.equalityBehavior</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the equalityBehavior
	 */
	@Accessor(qualifier = "equalityBehavior", type = Accessor.Type.SETTER)
	public void setEqualityBehavior(final DroolsEqualityBehavior value)
	{
		getPersistenceContext().setPropertyValue(EQUALITYBEHAVIOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEBase.eventProcessingMode</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the eventProcessingMode
	 */
	@Accessor(qualifier = "eventProcessingMode", type = Accessor.Type.SETTER)
	public void setEventProcessingMode(final DroolsEventProcessingMode value)
	{
		getPersistenceContext().setPropertyValue(EVENTPROCESSINGMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DroolsKIEBase.kieModule</code> attribute defined at extension <code>ruleengine</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the kieModule
	 */
	@Accessor(qualifier = "kieModule", type = Accessor.Type.SETTER)
	public void setKieModule(final DroolsKIEModuleModel value)
	{
		getPersistenceContext().setPropertyValue(KIEMODULE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEBase.kieSessions</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the kieSessions
	 */
	@Accessor(qualifier = "kieSessions", type = Accessor.Type.SETTER)
	public void setKieSessions(final Collection<DroolsKIESessionModel> value)
	{
		getPersistenceContext().setPropertyValue(KIESESSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DroolsKIEBase.name</code> attribute defined at extension <code>ruleengine</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEBase.rules</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the rules
	 */
	@Accessor(qualifier = "rules", type = Accessor.Type.SETTER)
	public void setRules(final Set<DroolsRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(RULES, value);
	}
	
}
