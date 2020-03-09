/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.backoffice.actions;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Iterator;

import javax.annotation.Resource;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


public class ConfirmPickupAction implements CockpitAction<ConsignmentModel, Object>
{
	private static final String CONFIRMATION_MESSAGE = "hmc.action.confirmpickup.confirmation.message";
	private static final String CONFIRM_PICKUP_EVENT = "trainingbackoffice.confirmpickup.event";

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "businessProcessService")
	private BusinessProcessService businessProcessService;

	@Resource(name = "notificationService")
	private NotificationService notificationService;

	@Override
	public boolean canPerform(final ActionContext<ConsignmentModel> ctx)
	{
		return ctx != null && ctx.getData() instanceof ConsignmentModel
				&& ConsignmentStatus.READY_FOR_PICKUP.equals(ctx.getData().getStatus());
	}

	@Override
	public String getConfirmationMessage(final ActionContext<ConsignmentModel> ctx)
	{
		return ctx.getLabel(CONFIRMATION_MESSAGE);
	}

	@Override
	public boolean needsConfirmation(final ActionContext<ConsignmentModel> arg0)
	{
		return true;
	}

	@Override
	public ActionResult<Object> perform(final ActionContext<ConsignmentModel> ctx)
	{
		final Object data = ctx.getData();

		if ((data != null) && (data instanceof ConsignmentModel))
		{
			final ConsignmentModel consignmentModel = (ConsignmentModel) data;
			consignmentModel.setStatus(ConsignmentStatus.PICKUP_COMPLETE);
			modelService.save(consignmentModel);

			for (final Iterator iterator = consignmentModel.getConsignmentProcesses().iterator(); iterator.hasNext();)
			{
				final ConsignmentProcessModel process = (ConsignmentProcessModel) iterator.next();
				businessProcessService.triggerEvent(String.format("%s_%s", process.getCode(), "ConsignmentPickup"));

			}

			notificationService.notifyUser(notificationService.getWidgetNotificationSource(ctx), CONFIRM_PICKUP_EVENT,
					NotificationEvent.Level.SUCCESS);

			return new ActionResult<Object>(ActionResult.SUCCESS, consignmentModel);
		}
		else
		{
			return new ActionResult(ActionResult.ERROR);
		}

	}

}
