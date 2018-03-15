/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.payment.dto;

import de.hybris.platform.core.enums.CreditCardType;

/**
 * Type of payment card
 */
public  class CardType  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CardType.id</code> property defined at extension <code>payment</code>. */
		
	private final String id;

	/** <i>Generated property</i> for <code>CardType.code</code> property defined at extension <code>payment</code>. */
		
	private final CreditCardType code;

	/** <i>Generated property</i> for <code>CardType.description</code> property defined at extension <code>payment</code>. */
		
	private final String description;
	
	public CardType(								final String id  , 								
							  								final CreditCardType code  , 								
							  								final String description   								
							  							  )
	{
		super();
										this.id=id;
															this.code=code;
															this.description=description;
								}
	
		
	
	public String getId() 
	{
		return id;
	}
	
		
	
	public CreditCardType getCode() 
	{
		return code;
	}
	
		
	
	public String getDescription() 
	{
		return description;
	}
	


}