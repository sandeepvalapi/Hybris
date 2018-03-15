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
import de.hybris.platform.mobileservices.model.text.MobileAggregatorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MobileAggregatorParameter first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileAggregatorParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileAggregatorParameter";
	
	/**<i>Generated relation code constant for relation <code>AggregatorParameterRelation</code> defining source attribute <code>aggregator</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _AGGREGATORPARAMETERRELATION = "AggregatorParameterRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregatorParameter.name</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregatorParameter.value</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileAggregatorParameter.aggregator</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String AGGREGATOR = "aggregator";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileAggregatorParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileAggregatorParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _aggregator initial attribute declared by type <code>MobileAggregatorParameter</code> at extension <code>mobileservices</code>
	 * @param _name initial attribute declared by type <code>MobileAggregatorParameter</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileAggregatorParameterModel(final MobileAggregatorModel _aggregator, final String _name)
	{
		super();
		setAggregator(_aggregator);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _aggregator initial attribute declared by type <code>MobileAggregatorParameter</code> at extension <code>mobileservices</code>
	 * @param _name initial attribute declared by type <code>MobileAggregatorParameter</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileAggregatorParameterModel(final MobileAggregatorModel _aggregator, final String _name, final ItemModel _owner)
	{
		super();
		setAggregator(_aggregator);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregatorParameter.aggregator</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the aggregator
	 */
	@Accessor(qualifier = "aggregator", type = Accessor.Type.GETTER)
	public MobileAggregatorModel getAggregator()
	{
		return getPersistenceContext().getPropertyValue(AGGREGATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregatorParameter.name</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileAggregatorParameter.value</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregatorParameter.aggregator</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the aggregator
	 */
	@Accessor(qualifier = "aggregator", type = Accessor.Type.SETTER)
	public void setAggregator(final MobileAggregatorModel value)
	{
		getPersistenceContext().setPropertyValue(AGGREGATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregatorParameter.name</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileAggregatorParameter.value</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
