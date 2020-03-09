/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.retention.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.impl.AbstractExtensibleRemoveCleanupAction;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;
import de.hybris.platform.ticket.constants.TicketsystemConstants;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Action that is responsible for resolving tickets. Configurations for rules when action should run are presented in
 * cronJobs.impex in ticketsFSStagnationRule
 */
public class DefaultCSTicketStagnationAction extends AbstractExtensibleRemoveCleanupAction
{
	private TicketBusinessService ticketBusinessService;

	private static final Logger LOG = Logger.getLogger(DefaultCSTicketStagnationAction.class.getName());

	@Override
	public void cleanup(final AfterRetentionCleanupJobPerformable afterRetentionCleanupJobPerformable,
			final AbstractRetentionRuleModel abstractRetentionRuleModel, final ItemToCleanup itemToCleanup)
	{
		validateParameterNotNullStandardMessage("item to cleanup", itemToCleanup);
		final Object itemModel = getModelService().get(itemToCleanup.getPk());
		if (!(itemModel instanceof CsTicketModel))
		{
			throw new IllegalStateException("Not instance of CsTicketModel:" + itemToCleanup.getPk());
		}

		final CsTicketModel csTicketModel = (CsTicketModel) itemModel;
		final String localizedEmail = fetchLocalizedClosedTicketMessage();
		try
		{
			getTicketBusinessService().resolveTicket(csTicketModel, CsInterventionType.TICKETMESSAGE, CsResolutionType.CLOSED,
					localizedEmail);
			LOG.info("Resolving ticket [ " + csTicketModel.getTicketID() + "] and sending closure email");
		}
		catch (final TicketException exp)
		{
			LOG.error("Error while trying to fetch and close ticket: ", exp);
		}
	}

	protected String fetchLocalizedClosedTicketMessage()
	{
		String localizedClosedTicketMeesage = Localization.getLocalizedString(TicketsystemConstants.SUPPORT_TICKET_STAGNATION_KEY);

		if (TicketsystemConstants.SUPPORT_TICKET_STAGNATION_KEY.equalsIgnoreCase(localizedClosedTicketMeesage))
		{
			localizedClosedTicketMeesage = TicketsystemConstants.SUPPORT_TICKET_STAGNATION_DEFAULT_CLOSING_MESSAGE;
		}

		return localizedClosedTicketMeesage;
	}

	protected TicketBusinessService getTicketBusinessService()
	{
		return ticketBusinessService;
	}

	@Required
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}
}
