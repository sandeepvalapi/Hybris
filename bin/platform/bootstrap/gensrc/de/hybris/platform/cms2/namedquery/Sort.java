/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cms2.namedquery;

import de.hybris.platform.cms2.enums.SortDirection;

public class Sort {

	/** <i>Generated property</i> for <code>Sort.parameter</code> property defined at extension <code>cms2</code>. */
	private String parameter;
	/** <i>Generated property</i> for <code>Sort.direction</code> property defined at extension <code>cms2</code>. */
	private SortDirection direction;


		public void setParameter(final String parameter)
	{
		this.parameter = parameter;
	}

	public Sort withParameter(final String parameter)
	{
		this.parameter = parameter;
		return this;
	}

			
	public String getParameter() 
	{
		return parameter;
	}

	
		public void setDirection(final SortDirection direction)
	{
		this.direction = direction;
	}

	public Sort withDirection(final SortDirection direction)
	{
		this.direction = direction;
		return this;
	}

			
	public SortDirection getDirection() 
	{
		return direction;
	}

	}
