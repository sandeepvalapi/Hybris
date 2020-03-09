/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrdersRetentionCronJobIntegrationTest extends ServicelayerTest
{
	private static final String TEST_BASESITE_UID = "testSite";
	private static final String ORDER_PROCESSES_QUERY = "SELECT {" + OrderProcessModel.PK + "} FROM {" + OrderProcessModel._TYPECODE + "} "
			+ "WHERE {" + OrderProcessModel.ORDER + "} = ?order";
	private static final String CONSIGNMENT_PROCESSES_QUERY = "SELECT {" + ConsignmentProcessModel.PK + "} FROM {"
			+ ConsignmentProcessModel._TYPECODE + "} " + "WHERE {" + ConsignmentProcessModel.CONSIGNMENT + "} IN (?consignments)";

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private I18NService i18NService;

	@Resource
	private CronJobService cronJobService;

	@Resource
	private ModelService modelService;
	
	@Resource
	private TimeService timeService;

	@Before
	public void setup() throws Exception
	{
		createCoreData();
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		importCsv("/trainingcore/import/common/cronjobs.impex", "utf-8");
		importCsv("/trainingcore/test/testOrderRetentionCronJob.impex", "utf-8");

		i18NService.setCurrentLocale(Locale.ENGLISH);
	}

	@Test
	public void testOrdersRetentionCronJob()
	{
		// order to be removed
		final OrderModel orderToBeRemovedTemplate = new OrderModel();
		orderToBeRemovedTemplate.setCode("orderToBeRemoved");
		final OrderModel orderToBeRemoved = flexibleSearchService.getModelByExample(orderToBeRemovedTemplate);
		final AddressModel toBeRemovedPaymentAddress = orderToBeRemoved.getPaymentAddress();
		final AddressModel toBeRemovedDeliveryAddress = orderToBeRemoved.getDeliveryAddress();
		final PaymentInfoModel toBeRemovedPaymentInfo = orderToBeRemoved.getPaymentInfo();
		final Date now = timeService.getCurrentTime();
		final UserModel guestCustomer = orderToBeRemoved.getUser();
		final List<ConsignmentModel> toBeRemovedConsignments = new ArrayList(orderToBeRemoved.getConsignments());
		final List<ConsignmentEntryModel> toBeRemovedConsignmentEntries = new ArrayList(toBeRemovedConsignments.get(0).getConsignmentEntries());
		final List<OrderProcessModel> toBeRemovedOrderProcesses = getOrderProcesses(orderToBeRemoved);
		final List<ConsignmentProcessModel> toBeRemovedConsignmentProcesses = getConsignmentProcesses(toBeRemovedConsignments);
		
		assertNotNull("PaymentAddress", toBeRemovedPaymentAddress);
		assertNotNull("DeliveryAddress", toBeRemovedDeliveryAddress);
		assertNotNull("PaymentInfo", toBeRemovedPaymentInfo);
		assertNotNull("Comments", orderToBeRemoved.getComments());
		assertEquals("Comments", 1, orderToBeRemoved.getComments().size());
		final CommentModel toBeRemovedComment = orderToBeRemoved.getComments().get(0);
		assertNotNull("Comments", toBeRemovedComment);
		assertNotNull("Consignment entries", toBeRemovedConsignmentEntries);
		assertEquals("Consignment entries count", 1, toBeRemovedConsignmentEntries.size());
		assertNotNull("Consignments", toBeRemovedConsignments);
		assertEquals("Consignments count", 1, toBeRemovedConsignments.size());
		assertNotNull("Consignment processes", toBeRemovedConsignmentProcesses);
		assertEquals("Consignment processes count", 1, toBeRemovedConsignmentProcesses.size());
		assertNotNull("Order processes", toBeRemovedOrderProcesses);
		assertEquals("Order processes count", 2, toBeRemovedOrderProcesses.size());

		cronJobService.performCronJob(cronJobService.getCronJob("ordersRetentionCronJob"), true);

		// order to be removed
		assertTrue("orderToBeRemoved", modelService.isRemoved(orderToBeRemoved));
		assertTrue("toBeRemovedPaymentAddress", modelService.isRemoved(toBeRemovedPaymentAddress));
		assertTrue("toBeRemovedDeliveryAddress", modelService.isRemoved(toBeRemovedDeliveryAddress));
		assertTrue("toBeRemovedPaymentInfo", modelService.isRemoved(toBeRemovedPaymentInfo));
		assertTrue("toBeRemovedComment", modelService.isRemoved(toBeRemovedComment));
		assertTrue("guest customer should be deactivated", now.before(guestCustomer.getDeactivationDate()));
		assertTrue("toBeRemovedConsignmentEntry", modelService.isRemoved(toBeRemovedConsignmentEntries.get(0)));
		assertTrue("toBeRemovedConsignment", modelService.isRemoved(toBeRemovedConsignments.get(0)));
		assertTrue("toBeRemovedConsignmentProcess", modelService.isRemoved(toBeRemovedConsignmentProcesses.get(0)));
		assertTrue("toBeRemovedOrderProcess", modelService.isRemoved(toBeRemovedOrderProcesses.get(0)));
		assertTrue("toBeRemovedOrderProcess", modelService.isRemoved(toBeRemovedOrderProcesses.get(1)));

		// order not to be removed
		final OrderModel orderNotToBeRemovedTemplate = new OrderModel();
		orderNotToBeRemovedTemplate.setCode("orderNotToBeRemoved");
		final OrderModel orderNotToBeRemoved = flexibleSearchService.getModelByExample(orderNotToBeRemovedTemplate);
		final List<ConsignmentModel> consignmentsNotToBeRemoved = new ArrayList(orderNotToBeRemoved.getConsignments());
		final List<ConsignmentEntryModel> consignmentEntriesNotToBeRemoved = new ArrayList(consignmentsNotToBeRemoved.get(0).getConsignmentEntries());
		final List<OrderProcessModel> orderProcessesNotToBeRemoved = getOrderProcesses(orderNotToBeRemoved);
		final List<ConsignmentProcessModel> consignmentProcessesNotToBeRemoved = getConsignmentProcesses(consignmentsNotToBeRemoved);
		assertNotNull("PaymentAddress", orderNotToBeRemoved.getPaymentAddress());
		assertFalse("PaymentAddress", modelService.isRemoved(orderNotToBeRemoved.getPaymentAddress()));
		assertNotNull("DeliveryAddress", orderNotToBeRemoved.getDeliveryAddress());
		assertFalse("DeliveryAddress", modelService.isRemoved(orderNotToBeRemoved.getDeliveryAddress()));
		assertNotNull("PaymentInfo", orderNotToBeRemoved.getPaymentInfo());
		assertFalse("PaymentInfo", modelService.isRemoved(orderNotToBeRemoved.getPaymentInfo()));
		assertNotNull("Comments", orderNotToBeRemoved.getComments());
		assertEquals("Comments", 1, orderNotToBeRemoved.getComments().size());
		assertFalse("Comment", modelService.isRemoved(orderNotToBeRemoved.getComments().get(0)));
		assertNotNull("Consignment entries", consignmentEntriesNotToBeRemoved);
		assertEquals("Consignment entries count", 1, consignmentEntriesNotToBeRemoved.size());
		assertFalse("Consignment entry not to be removed", modelService.isRemoved(consignmentEntriesNotToBeRemoved.get(0)));
		assertNotNull("Consignments", consignmentsNotToBeRemoved);
		assertEquals("Consignments count", 1, consignmentsNotToBeRemoved.size());
		assertFalse("Consignment not to be removed", modelService.isRemoved(consignmentsNotToBeRemoved.get(0)));
		assertNotNull("Consignment processes", consignmentProcessesNotToBeRemoved);
		assertEquals("Consignment processes count", 1, consignmentProcessesNotToBeRemoved.size());
		assertFalse("Consignment processes not to be removed", modelService.isRemoved(consignmentProcessesNotToBeRemoved.get(0)));
		assertNotNull("Order processes", orderProcessesNotToBeRemoved);
		assertEquals("Order processes count", 2, orderProcessesNotToBeRemoved.size());
		assertFalse("Order processes not to be removed", modelService.isRemoved(orderProcessesNotToBeRemoved.get(0)));
		assertFalse("Order processes not to be removed", modelService.isRemoved(orderProcessesNotToBeRemoved.get(1)));
	}
	
	private List<OrderProcessModel> getOrderProcesses(final OrderModel order)
	{
		final FlexibleSearchQuery orderProcessesQuery = new FlexibleSearchQuery(ORDER_PROCESSES_QUERY);
		orderProcessesQuery.addQueryParameter("order", order);
		return new ArrayList(flexibleSearchService.search(orderProcessesQuery).getResult());
	}
	
	private List<ConsignmentProcessModel> getConsignmentProcesses(final List<ConsignmentModel> consignments)
	{
		final FlexibleSearchQuery consignmentProcessesQuery = new FlexibleSearchQuery(CONSIGNMENT_PROCESSES_QUERY);
		consignmentProcessesQuery.addQueryParameter("consignments", consignments);
		return new ArrayList(flexibleSearchService.search(consignmentProcessesQuery).getResult());
	}
}
