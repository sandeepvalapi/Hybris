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
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type ZoneDeliveryMode first defined at extension deliveryzone.
 */
@SuppressWarnings("all")
public class ZoneDeliveryModeModel extends DeliveryModeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ZoneDeliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryMode.propertyName</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String PROPERTYNAME = "propertyName";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryMode.net</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>ZoneDeliveryMode.values</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String VALUES = "values";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ZoneDeliveryModeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ZoneDeliveryModeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeliveryMode</code> at extension <code>core</code>
	 * @param _net initial attribute declared by type <code>ZoneDeliveryMode</code> at extension <code>deliveryzone</code>
	 */
	@Deprecated
	public ZoneDeliveryModeModel(final String _code, final Boolean _net)
	{
		super();
		setCode(_code);
		setNet(_net);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeliveryMode</code> at extension <code>core</code>
	 * @param _net initial attribute declared by type <code>ZoneDeliveryMode</code> at extension <code>deliveryzone</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ZoneDeliveryModeModel(final String _code, final Boolean _net, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setNet(_net);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.net</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.propertyName</code> attribute defined at extension <code>deliveryzone</code>. 
	 * @return the propertyName
	 */
	@Accessor(qualifier = "propertyName", type = Accessor.Type.GETTER)
	public String getPropertyName()
	{
		return getPersistenceContext().getPropertyValue(PROPERTYNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.values</code> attribute defined at extension <code>deliveryzone</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the values
	 */
	@Accessor(qualifier = "values", type = Accessor.Type.GETTER)
	public Set<ZoneDeliveryModeValueModel> getValues()
	{
		return getPersistenceContext().getPropertyValue(VALUES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryMode.net</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryMode.propertyName</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the propertyName
	 */
	@Accessor(qualifier = "propertyName", type = Accessor.Type.SETTER)
	public void setPropertyName(final String value)
	{
		getPersistenceContext().setPropertyValue(PROPERTYNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ZoneDeliveryMode.values</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the values
	 */
	@Accessor(qualifier = "values", type = Accessor.Type.SETTER)
	public void setValues(final Set<ZoneDeliveryModeValueModel> value)
	{
		getPersistenceContext().setPropertyValue(VALUES, value);
	}
	
}
