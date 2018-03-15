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
package de.hybris.platform.mobileservices.model.text;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.mobileservices.enums.EnginesType;
import de.hybris.platform.mobileservices.enums.MobileActiveStateType;
import de.hybris.platform.mobileservices.model.text.MobileAggregatorParameterModel;
import de.hybris.platform.mobileservices.model.text.MobileShortcodeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MobileAggregator first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileAggregatorModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileAggregator";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.code</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.engine</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ENGINE = "engine";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.maxSendingRetries</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MAXSENDINGRETRIES = "maxSendingRetries";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.sendingRetryInterval</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SENDINGRETRYINTERVAL = "sendingRetryInterval";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.available</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String AVAILABLE = "available";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.state</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.parameters</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PARAMETERS = "parameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregator.shortcodes</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SHORTCODES = "shortcodes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileAggregatorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileAggregatorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileAggregator</code> at extension <code>mobileservices</code>
	 * @param _engine initial attribute declared by type <code>MobileAggregator</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileAggregatorModel(final String _code, final EnginesType _engine)
	{
		super();
		setCode(_code);
		setEngine(_engine);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>MobileAggregator</code> at extension <code>mobileservices</code>
	 * @param _engine initial attribute declared by type <code>MobileAggregator</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileAggregatorModel(final String _code, final EnginesType _engine, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setEngine(_engine);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.available</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the available
	 */
	@Accessor(qualifier = "available", type = Accessor.Type.GETTER)
	public Boolean getAvailable()
	{
		return getPersistenceContext().getPropertyValue(AVAILABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.code</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.engine</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the engine
	 */
	@Accessor(qualifier = "engine", type = Accessor.Type.GETTER)
	public EnginesType getEngine()
	{
		return getPersistenceContext().getPropertyValue(ENGINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.maxSendingRetries</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the maxSendingRetries
	 */
	@Accessor(qualifier = "maxSendingRetries", type = Accessor.Type.GETTER)
	public Integer getMaxSendingRetries()
	{
		return getPersistenceContext().getPropertyValue(MAXSENDINGRETRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.parameters</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.GETTER)
	public Collection<MobileAggregatorParameterModel> getParameters()
	{
		return getPersistenceContext().getPropertyValue(PARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.sendingRetryInterval</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the sendingRetryInterval
	 */
	@Accessor(qualifier = "sendingRetryInterval", type = Accessor.Type.GETTER)
	public Integer getSendingRetryInterval()
	{
		return getPersistenceContext().getPropertyValue(SENDINGRETRYINTERVAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.shortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the shortcodes
	 */
	@Accessor(qualifier = "shortcodes", type = Accessor.Type.GETTER)
	public Collection<MobileShortcodeModel> getShortcodes()
	{
		return getPersistenceContext().getPropertyValue(SHORTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregator.state</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public MobileActiveStateType getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.code</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MobileAggregator.engine</code> attribute defined at extension <code>mobileservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the engine
	 */
	@Accessor(qualifier = "engine", type = Accessor.Type.SETTER)
	public void setEngine(final EnginesType value)
	{
		getPersistenceContext().setPropertyValue(ENGINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.maxSendingRetries</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the maxSendingRetries
	 */
	@Accessor(qualifier = "maxSendingRetries", type = Accessor.Type.SETTER)
	public void setMaxSendingRetries(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXSENDINGRETRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.parameters</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.SETTER)
	public void setParameters(final Collection<MobileAggregatorParameterModel> value)
	{
		getPersistenceContext().setPropertyValue(PARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.sendingRetryInterval</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the sendingRetryInterval
	 */
	@Accessor(qualifier = "sendingRetryInterval", type = Accessor.Type.SETTER)
	public void setSendingRetryInterval(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SENDINGRETRYINTERVAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.shortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the shortcodes
	 */
	@Accessor(qualifier = "shortcodes", type = Accessor.Type.SETTER)
	public void setShortcodes(final Collection<MobileShortcodeModel> value)
	{
		getPersistenceContext().setPropertyValue(SHORTCODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregator.state</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final MobileActiveStateType value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
}
