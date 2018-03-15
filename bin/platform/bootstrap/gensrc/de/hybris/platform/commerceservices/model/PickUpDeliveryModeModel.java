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
package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PickUpDeliveryMode first defined at extension commerceservices.
 * <p>
 * A Delivery Mode that represents a collection from a PointOfService.
 */
@SuppressWarnings("all")
public class PickUpDeliveryModeModel extends DeliveryModeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PickUpDeliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>PickUpDeliveryMode.supportedMode</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String SUPPORTEDMODE = "supportedMode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PickUpDeliveryModeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PickUpDeliveryModeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeliveryMode</code> at extension <code>core</code>
	 */
	@Deprecated
	public PickUpDeliveryModeModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeliveryMode</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PickUpDeliveryModeModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickUpDeliveryMode.supportedMode</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the supportedMode - The mode supported by the pickup delivery mode
	 */
	@Accessor(qualifier = "supportedMode", type = Accessor.Type.GETTER)
	public PickupInStoreMode getSupportedMode()
	{
		return getPersistenceContext().getPropertyValue(SUPPORTEDMODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PickUpDeliveryMode.supportedMode</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the supportedMode - The mode supported by the pickup delivery mode
	 */
	@Accessor(qualifier = "supportedMode", type = Accessor.Type.SETTER)
	public void setSupportedMode(final PickupInStoreMode value)
	{
		getPersistenceContext().setPropertyValue(SUPPORTEDMODE, value);
	}
	
}
