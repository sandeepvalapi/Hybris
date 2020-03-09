/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingaddon.controllers.pages.misc;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.consignmenttrackingaddon.controllers.ControllerConstants;
import de.hybris.platform.consignmenttrackingfacades.ConsignmentTrackingFacade;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Controller for ConsignmentTracking
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/consignment")
public class ConsignmentTrackingController
{

	private static final String CONSIGNMENT_CODE_PATH_VARIABLE_PATTERN = "/{consignmentCode:.*}";
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "/{orderCode:.*}";

	@Resource(name = "consignmentTrackingFacade")
	private ConsignmentTrackingFacade consignmentTrackingFacade;

	@RequireHardLogIn
	@RequestMapping(value = ORDER_CODE_PATH_VARIABLE_PATTERN + CONSIGNMENT_CODE_PATH_VARIABLE_PATTERN + "/tracking", method = RequestMethod.GET)
	public String retrieveTrackingEvents(@PathVariable("orderCode") final String orderCode,
			@PathVariable("consignmentCode") final String consignmentCode, final Model model)
	{
		final Optional<ConsignmentData> optional = consignmentTrackingFacade.getConsignmentByCode(orderCode, consignmentCode);
		optional.ifPresent(consignment -> {
			model.addAttribute("consignment", consignment);
			model.addAttribute("statusCode", Integer.valueOf(getConsignmentStatusCode(consignment)));
			model.addAttribute("trackingUrl",
			consignmentTrackingFacade.getTrackingUrlForConsignmentCode(orderCode, consignmentCode));
		});

		return ControllerConstants.Views.Pages.Consignment.TrackPackagePage;
	}

	/**
	 * get consignment shipment state
	 *
	 * @param consignment
	 * @return status code: 0 = ordered, 50 = shipped, 100 = arrived
	 */
	protected int getConsignmentStatusCode(final ConsignmentData consignment)
	{
		// Finished statuses
		final ConsignmentStatus[] endStatuses =
		{ ConsignmentStatus.CANCELLED, ConsignmentStatus.DELIVERY_COMPLETED, ConsignmentStatus.DELIVERY_REJECTED };
		if (ArrayUtils.contains(endStatuses, consignment.getStatus()))
		{
			return 100;
		}

		// statuses during shipped to finished
		final ConsignmentStatus[] deliveringStatuses =
		{ ConsignmentStatus.SHIPPED, ConsignmentStatus.IN_TRANSIT, ConsignmentStatus.DELIVERING };
		if (ArrayUtils.contains(deliveringStatuses, consignment.getStatus()))
		{
			return 50;
		}

		// between ordered and shipped
		return 0;
	}
}
