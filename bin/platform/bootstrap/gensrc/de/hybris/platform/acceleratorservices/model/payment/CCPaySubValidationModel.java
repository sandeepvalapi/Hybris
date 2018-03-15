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
package de.hybris.platform.acceleratorservices.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CCPaySubValidation first defined at extension acceleratorservices.
 * <p>
 * Used for keeping track of valid subscriptions.
 */
@SuppressWarnings("all")
public class CCPaySubValidationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CCPaySubValidation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CCPaySubValidation.subscriptionId</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SUBSCRIPTIONID = "subscriptionId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CCPaySubValidationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CCPaySubValidationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CCPaySubValidationModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CCPaySubValidation.subscriptionId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the subscriptionId
	 */
	@Accessor(qualifier = "subscriptionId", type = Accessor.Type.GETTER)
	public String getSubscriptionId()
	{
		return getPersistenceContext().getPropertyValue(SUBSCRIPTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CCPaySubValidation.subscriptionId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the subscriptionId
	 */
	@Accessor(qualifier = "subscriptionId", type = Accessor.Type.SETTER)
	public void setSubscriptionId(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBSCRIPTIONID, value);
	}
	
}
