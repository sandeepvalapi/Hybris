/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.catalog.model;


import de.hybris.platform.catalog.jalo.PreviewTicket;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import com.google.common.base.Preconditions;


public class PreviewTicketTicketCodeHandler implements DynamicAttributeHandler<String, PreviewTicketModel>
{

	@Override
	public String get(final PreviewTicketModel model)
	{
		Preconditions.checkNotNull(model.getPk(), "PreviewTicket must be saved before getting ticketCode");
		return PreviewTicket.PREVIEW_TICKET_PREFIX + model.getPk() + PreviewTicket.PREVIEW_TICKET_POSTFIX;
	}

	@Override
	public void set(final PreviewTicketModel model, final String s)
	{
		// read-only
	}
}
