/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commerceservices.consent.dao.ConsentDao;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.enums.RetentionState;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CustomersWithOrdersRetentionCronJobIntegrationTest extends ServicelayerTest
{
	private static final String TEST_BASESITE_UID = "testSite";
	private static final String CUSTOMER_PROCESSES_QUERY = "SELECT {" + StoreFrontCustomerProcessModel.PK + "} FROM {"
			+ StoreFrontCustomerProcessModel._TYPECODE + "} " + "WHERE {" + StoreFrontCustomerProcessModel.CUSTOMER + "} = ?user";

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
	private ConsentDao consentDao;

	@Before
	public void setup() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		importCsv("/trainingcore/import/common/cronjobs.impex", "utf-8");
		importCsv("/trainingcore/test/testCustomersWithOrdersRetentionCronJob.impex", "utf-8");

		i18NService.setCurrentLocale(Locale.ENGLISH);
	}

	@Test
	public void testCustomersWithOrdersRetentionCronJob()
	{
		// customer to be processed but NOT to be removed. user1 has orders, has been deactivated, not been processed and current date past retention period.
		final CustomerModel customerToBeProcessedTemplate = new CustomerModel();
		customerToBeProcessedTemplate.setUid("user1");
		final CustomerModel customerToBeProcessed = flexibleSearchService.getModelByExample(customerToBeProcessedTemplate);
		final Collection<AddressModel> toBeRemovedAddresses = customerToBeProcessed.getAddresses();
		final Collection<PaymentInfoModel> toBeRemovedPaymentInfos = customerToBeProcessed.getPaymentInfos();
		final List<CommentModel> toBeRemovedComments = customerToBeProcessed.getComments();
		final Collection<AbstractContactInfoModel> toBeRemovedContactInfos = customerToBeProcessed.getContactInfos();
		final Collection<CartModel> toBeRemovedCarts = customerToBeProcessed.getCarts();
		final Collection<CustomerReviewModel> toBeRemovedCustomerReviews = customerToBeProcessed.getCustomerReviews();
		List<ConsentModel> toBeRemovedConsents = consentDao.findAllConsentsByCustomer(customerToBeProcessed);
		final List<StoreFrontCustomerProcessModel> toBeRemovedProcesses = getProcessesForCustomer(customerToBeProcessed);

		assertNull("RetentionState", customerToBeProcessed.getRetentionState());
		assertNotNull("Addresses", toBeRemovedAddresses);
		assertEquals("Addresses size", 1, toBeRemovedAddresses.size());
		assertNotNull("PaymentInfos", toBeRemovedPaymentInfos);
		assertEquals("PaymentInfos size", 1, toBeRemovedPaymentInfos.size());
		assertNotNull("Comments", toBeRemovedComments);
		assertEquals("Comments size", 1, toBeRemovedComments.size());
		assertNotNull("ContactInfos", toBeRemovedContactInfos);
		assertEquals("ContactInfos size", 1, toBeRemovedContactInfos.size());
		assertNotNull("Carts", toBeRemovedCarts);
		assertEquals("Carts size", 1, toBeRemovedCarts.size());
		assertNotNull("CustomerReviews", toBeRemovedCustomerReviews);
		assertEquals("CustomerReviews size", 1, toBeRemovedCustomerReviews.size());
		assertNotNull("Consents", toBeRemovedConsents);
		assertEquals("Consents size", 1, toBeRemovedConsents.size());
		assertNotNull("Processes", toBeRemovedProcesses);
		assertEquals("Processes size", 2, toBeRemovedProcesses.size());

		cronJobService.performCronJob(cronJobService.getCronJob("customersWithOrdersRetentionCronJob"), true);

		// customer to be processed but NOT to be removed
		assertFalse("customerNotToBeRemoved", modelService.isRemoved(customerToBeProcessed));
		modelService.refresh(customerToBeProcessed);
		assertEquals("RetentionState", RetentionState.PROCESSED, customerToBeProcessed.getRetentionState());

		// customer attributes to be removed
		assertTrue("toBeRemovedAddress0", modelService.isRemoved(toBeRemovedAddresses.iterator().next()));
		assertTrue("toBeRemovedPaymentInfo", modelService.isRemoved(toBeRemovedPaymentInfos.iterator().next()));
		assertTrue("toBeRemovedComment", modelService.isRemoved(toBeRemovedComments.get(0)));
		assertTrue("toBeRemovedContactInfo", modelService.isRemoved(toBeRemovedContactInfos.iterator().next()));
		assertTrue("toBeRemovedCart", modelService.isRemoved(toBeRemovedCarts.iterator().next()));
		assertTrue("toBeRemovedCustomerReview", modelService.isRemoved(toBeRemovedCustomerReviews.iterator().next()));
		assertTrue("toBeRemovedProcess", modelService.isRemoved(toBeRemovedProcesses.get(0)));
		assertTrue("toBeRemovedProcess", modelService.isRemoved(toBeRemovedProcesses.get(1)));
		toBeRemovedConsents = consentDao.findAllConsentsByCustomer(customerToBeProcessed);
		assertNotNull("Consents", toBeRemovedConsents);
		assertEquals("Consents size", 0, toBeRemovedConsents.size());

		// customer NOT to be processed. user2's retentionState is processed, should not be picked up by cronjob
		final CustomerModel customerNotToBeProcessedTemplate = new CustomerModel();
		customerNotToBeProcessedTemplate.setUid("user2");
		final CustomerModel customerNotToBeProcessed = flexibleSearchService.getModelByExample(customerNotToBeProcessedTemplate);
		final Collection<AddressModel> addresses = customerNotToBeProcessed.getAddresses();
		assertNotNull("Addresses", addresses);
		assertEquals("Addresses size", 1, addresses.size());
		assertFalse("Addresses0", modelService.isRemoved(addresses.iterator().next()));
		final List<PaymentInfoModel> paymentInfos = (List<PaymentInfoModel>) customerNotToBeProcessed.getPaymentInfos();
		assertNotNull("PaymentInfos", paymentInfos);
		assertEquals("PaymentInfos size", 1, paymentInfos.size());
		assertFalse("PaymentInfo", modelService.isRemoved(paymentInfos.get(0)));
		final List<CommentModel> comments = customerNotToBeProcessed.getComments();
		assertNotNull("Comments", comments);
		assertEquals("Comments size", 1, comments.size());
		assertFalse("Comment", modelService.isRemoved(comments.get(0)));
		final Collection<AbstractContactInfoModel> contactInfos = customerNotToBeProcessed.getContactInfos();
		assertNotNull("contactInfos", contactInfos);
		assertEquals("contactInfos size", 1, contactInfos.size());
		assertFalse("contactInfo", modelService.isRemoved(contactInfos.iterator().next()));
		final Collection<CartModel> carts = customerNotToBeProcessed.getCarts();
		assertNotNull("carts", carts);
		assertEquals("carts size", 1, carts.size());
		assertFalse("cart", modelService.isRemoved(carts.iterator().next()));
		final Collection<CustomerReviewModel> customerReviews = customerNotToBeProcessed.getCustomerReviews();
		assertNotNull("customerReviews", customerReviews);
		assertEquals("customerReviews size", 1, carts.size());
		assertFalse("customerReview", modelService.isRemoved(customerReviews.iterator().next()));
		final List<ConsentModel> consents = consentDao.findAllConsentsByCustomer(customerNotToBeProcessed);
		assertNotNull("Consents", consents);
		assertEquals("Consents size", 1, consents.size());
		final List<StoreFrontCustomerProcessModel> processes = getProcessesForCustomer(customerNotToBeProcessed);
		assertNotNull("Processes", processes);
		assertEquals("Processes size", 2, processes.size());
	}

	private List<StoreFrontCustomerProcessModel> getProcessesForCustomer(final CustomerModel user)
	{
		final FlexibleSearchQuery customerProcessesQuery = new FlexibleSearchQuery(CUSTOMER_PROCESSES_QUERY);
		customerProcessesQuery.addQueryParameter("user", user); 
		return new ArrayList(flexibleSearchService.search(customerProcessesQuery).getResult());
	}
}
