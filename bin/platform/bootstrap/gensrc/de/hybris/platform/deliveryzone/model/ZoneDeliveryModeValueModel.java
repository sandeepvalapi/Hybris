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
package de.hybris.platform.deliveryzone.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ZoneDeliveryModeValue first defined at extension deliveryzone.
 */
@SuppressWarnings("all")
public class ZoneDeliveryModeValueModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ZoneDeliveryModeValue";
	
	/**<i>Generated relation code constant for relation <code>ModeValuesRelation</code> defining source attribute <code>deliveryMode</code> in extension <code>deliveryzone</code>.</i>*/
	public static final String _MODEVALUESRELATION = "ModeValuesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryModeValue.currency</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryModeValue.minimum</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String MINIMUM = "minimum";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryModeValue.value</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryModeValue.zone</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String ZONE = "zone";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryModeValue.deliveryMode</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String DELIVERYMODE = "deliveryMode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ZoneDeliveryModeValueModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ZoneDeliveryModeValueModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _deliveryMode initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _minimum initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _value initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _zone initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 */
	@Deprecated
	public ZoneDeliveryModeValueModel(final CurrencyModel _currency, final ZoneDeliveryModeModel _deliveryMode, final Double _minimum, final Double _value, final ZoneModel _zone)
	{
		super();
		setCurrency(_currency);
		setDeliveryMode(_deliveryMode);
		setMinimum(_minimum);
		setValue(_value);
		setZone(_zone);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _deliveryMode initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _minimum initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _value initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 * @param _zone initial attribute declared by type <code>ZoneDeliveryModeValue</code> at extension <code>deliveryzone</code>
	 */
	@Deprecated
	public ZoneDeliveryModeValueModel(final CurrencyModel _currency, final ZoneDeliveryModeModel _deliveryMode, final Double _minimum, final ItemModel _owner, final Double _value, final ZoneModel _zone)
	{
		super();
		setCurrency(_currency);
		setDeliveryMode(_deliveryMode);
		setMinimum(_minimum);
		setOwner(_owner);
		setValue(_value);
		setZone(_zone);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.currency</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.deliveryMode</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
	public ZoneDeliveryModeModel getDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.minimum</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the minimum
	 */
	@Accessor(qualifier = "minimum", type = Accessor.Type.GETTER)
	public Double getMinimum()
	{
		return getPersistenceContext().getPropertyValue(MINIMUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.value</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Double getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.zone</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the zone
	 */
	@Accessor(qualifier = "zone", type = Accessor.Type.GETTER)
	public ZoneModel getZone()
	{
		return getPersistenceContext().getPropertyValue(ZONE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryModeValue.currency</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ZoneDeliveryModeValue.deliveryMode</code> attribute defined at extension <code>deliveryzone</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
	public void setDeliveryMode(final ZoneDeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryModeValue.minimum</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the minimum
	 */
	@Accessor(qualifier = "minimum", type = Accessor.Type.SETTER)
	public void setMinimum(final Double value)
	{
		getPersistenceContext().setPropertyValue(MINIMUM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryModeValue.value</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Double value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryModeValue.zone</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the zone
	 */
	@Accessor(qualifier = "zone", type = Accessor.Type.SETTER)
	public void setZone(final ZoneModel value)
	{
		getPersistenceContext().setPropertyValue(ZONE, value);
	}
	
}
