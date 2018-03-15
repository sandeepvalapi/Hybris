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
package com.hybris.training.fulfilmentprocess.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;


public class ReserveOrderAmountAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		setOrderStatus(process.getOrder(), OrderStatus.PAYMENT_AMOUNT_RESERVED);
		return Transition.OK;
	}
}
