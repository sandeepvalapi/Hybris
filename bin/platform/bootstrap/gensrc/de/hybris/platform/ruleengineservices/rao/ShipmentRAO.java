/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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
 */
package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;

public  class ShipmentRAO extends AbstractRuleActionRAO 
{

 

	/** <i>Generated property</i> for <code>ShipmentRAO.mode</code> property defined at extension <code>ruleengineservices</code>. */
		
	private DeliveryModeRAO mode;
	
	public ShipmentRAO()
	{
		// default constructor
	}
	
		
	
	public void setMode(final DeliveryModeRAO mode)
	{
		this.mode = mode;
	}

		
	
	public DeliveryModeRAO getMode() 
	{
		return mode;
	}
	


}
