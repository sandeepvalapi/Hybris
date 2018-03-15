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
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DroolsKIESession first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class DroolsKIESessionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DroolsKIESession";
	
	/**<i>Generated relation code constant for relation <code>DroolsKIEBase2Session</code> defining source attribute <code>kieBase</code> in extension <code>ruleengine</code>.</i>*/
	public static final String _DROOLSKIEBASE2SESSION = "DroolsKIEBase2Session";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIESession.name</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIESession.sessionType</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String SESSIONTYPE = "sessionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIESession.kieBase</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIEBASE = "kieBase";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DroolsKIESessionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DroolsKIESessionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>DroolsKIESession</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public DroolsKIESessionModel(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>DroolsKIESession</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DroolsKIESessionModel(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIESession.kieBase</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the kieBase
	 */
	@Accessor(qualifier = "kieBase", type = Accessor.Type.GETTER)
	public DroolsKIEBaseModel getKieBase()
	{
		return getPersistenceContext().getPropertyValue(KIEBASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIESession.name</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIESession.sessionType</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the sessionType
	 */
	@Accessor(qualifier = "sessionType", type = Accessor.Type.GETTER)
	public DroolsSessionType getSessionType()
	{
		return getPersistenceContext().getPropertyValue(SESSIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIESession.kieBase</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the kieBase
	 */
	@Accessor(qualifier = "kieBase", type = Accessor.Type.SETTER)
	public void setKieBase(final DroolsKIEBaseModel value)
	{
		getPersistenceContext().setPropertyValue(KIEBASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DroolsKIESession.name</code> attribute defined at extension <code>ruleengine</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIESession.sessionType</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the sessionType
	 */
	@Accessor(qualifier = "sessionType", type = Accessor.Type.SETTER)
	public void setSessionType(final DroolsSessionType value)
	{
		getPersistenceContext().setPropertyValue(SESSIONTYPE, value);
	}
	
}
