/*
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
package de.hybris.platform.retention;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.processing.model.AfterRetentionCleanupRuleModel;
import de.hybris.platform.retention.impl.AfterRetentionCleanupItemsProvider;
import de.hybris.platform.retention.impl.BasicRemoveCleanupAction;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.RetentionJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AfterRetentionCleanupItemsProviderTest extends ServicelayerBaseTest

{
	@Resource
	protected ModelService modelService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	private CronJobService cronJobService;

	private final TypeService typeService = Registry.getApplicationContext().getBean(TypeService.class);

	private AfterRetentionCleanupRuleModel wrongRule;
	private AfterRetentionCleanupRuleModel secondsRule;
	private static ComposedTypeModel composedType;
	private static AttributeDescriptorModel dateAttributeDescriptor;

	private static boolean initialized = false;

	@Before
	public void setUp() throws Exception
	{
		if (!initialized)
		{
			composedType = typeService.getComposedTypeForCode("Order");
			dateAttributeDescriptor = typeService.getAttributeDescriptor(composedType, "date");
			initialized = true;
		}
		wrongRule = new AfterRetentionCleanupRuleModel();
		wrongRule.setCode("wrongRule");
		wrongRule.setRetirementItemType(composedType);
		wrongRule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		modelService.save(wrongRule);

		secondsRule = new AfterRetentionCleanupRuleModel();
		secondsRule.setCode("secondsRule");
		secondsRule.setItemFilterExpression("{name} = 'secondsRule'");
		secondsRule.setRetirementItemType(composedType);
		secondsRule.setRetirementDateAttribute(dateAttributeDescriptor);
		secondsRule.setRetentionTimeSeconds(Long.valueOf(64800l)); // 18h
		secondsRule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		modelService.save(secondsRule);
	}

	@Test
	public void testRetentionTimeMultiple()
	{
		final Instant localDateTimeBefore = Instant.now().minusSeconds(3600);
		final Instant localDateTimeAfter = Instant.now().plusSeconds(3600);
		createOrders("secondsRule", localDateTimeBefore, 6, localDateTimeAfter, 3);

		final AfterRetentionCleanupItemsProvider afterRetentionCleanupItemsProvider = new AfterRetentionCleanupItemsProvider(
				secondsRule);
		afterRetentionCleanupItemsProvider.setBatchSize(3);
		afterRetentionCleanupItemsProvider.setFlexibleSearchService(flexibleSearchService);

		List<ItemToCleanup> collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(3);

		collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(2);

		collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(0);
	}

	@Test
	public void testRetentionTimeMultiple2()
	{
		final Instant localDateTimeBefore = Instant.now().minusSeconds(3600);
		final Instant localDateTimeAfter = Instant.now().plusSeconds(3600);
		createOrders("secondsRule", localDateTimeBefore, 7, localDateTimeAfter, 3);

		final AfterRetentionCleanupItemsProvider afterRetentionCleanupItemsProvider = new AfterRetentionCleanupItemsProvider(
				secondsRule);
		afterRetentionCleanupItemsProvider.setBatchSize(3);
		afterRetentionCleanupItemsProvider.setFlexibleSearchService(flexibleSearchService);

		List<ItemToCleanup> collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(3);

		collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(3);

		collectionList = afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		assertThat(collectionList).hasSize(0);
	}

	@Test
	public void testRetentionTimeSingleWithCleanup() throws Exception
	{
		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(10);
		job.setRetentionRule(secondsRule);
		modelService.save(job);

		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		final String query = "SELECT {pk} FROM {Order}  WHERE {name} = 'secondsRule' ";

		final Instant localDateTimeBefore = Instant.now().minusSeconds(3600);
		final Instant localDateTimeAfter = Instant.now().plusSeconds(3600);
		createOrders("secondsRule", localDateTimeBefore, 6, localDateTimeAfter, 3);

		SearchResult<List<Object>> result = flexibleSearchService.search(query);
		assertThat(result.getResult()).hasSize(9);

		final AfterRetentionCleanupItemsProvider afterRetentionCleanupItemsProvider = new AfterRetentionCleanupItemsProvider(
				secondsRule);
		afterRetentionCleanupItemsProvider.setBatchSize(10);
		afterRetentionCleanupItemsProvider.setFlexibleSearchService(flexibleSearchService);

		final List<ItemToCleanup> itemToRetentions = afterRetentionCleanupItemsProvider.nextItemsForCleanup();

		assertTrue(itemToRetentions != null);
		assertThat(itemToRetentions).hasSize(5);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isTrue();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();
		result = flexibleSearchService.search(query);
		assertThat(result.getResult()).hasSize(4);
	}

	private CronJobModel prepareCronJob(final String code, final JobModel job)
	{
		final CronJobModel cronjob = modelService.create(CronJobModel.class);
		cronjob.setCode(code);
		cronjob.setSingleExecutable(Boolean.TRUE);
		cronjob.setJob(job);
		modelService.save(cronjob);
		return cronjob;
	}

	@Test
	public void testWrongRule()
	{
		IllegalStateException illegalStateException = null;
		final AfterRetentionCleanupItemsProvider afterRetentionCleanupItemsProvider = new AfterRetentionCleanupItemsProvider(
				wrongRule);
		afterRetentionCleanupItemsProvider.setFlexibleSearchService(flexibleSearchService);

		try
		{
			afterRetentionCleanupItemsProvider.nextItemsForCleanup();
		}
		catch (final IllegalStateException e)
		{
			illegalStateException = e;
		}
		assertTrue("Expected IllegalStateException ", illegalStateException != null);
	}

	private void createOrders(final String orderName, final Instant orderDateBefore, final int countBefore,
			final Instant orderDateAfter, final int countAfter)
	{

		final UserModel userModel = modelService.create(UserModel.class);
		userModel.setUid("userWithOrders");
		modelService.save(userModel);

		final CurrencyModel polishCurrency = modelService.create(CurrencyModel.class);
		polishCurrency.setIsocode("ZL");
		polishCurrency.setSymbol("ZL");

		if (orderDateBefore != null)
		{
			LocalDateTime localDateTimeBefore = orderDateBefore.atZone(ZoneId.systemDefault()).toLocalDateTime();
			for (int i = 0; i < countBefore; i++)
			{
				final OrderModel order = modelService.create(OrderModel.class);
				order.setCode(UUID.randomUUID().toString());
				order.setName(orderName);
				order.setUser(userModel);
				order.setDate(Date.from(localDateTimeBefore.atZone(ZoneId.systemDefault()).toInstant()));
				order.setCurrency(polishCurrency);
				modelService.save(order);
				localDateTimeBefore = localDateTimeBefore.minusDays(1);
			}
		}
		if (orderDateAfter != null)
		{
			LocalDateTime localDateTimeAfter = orderDateAfter.atZone(ZoneId.systemDefault()).toLocalDateTime();
			for (int i = 0; i < countAfter; i++)
			{
				final OrderModel order = modelService.create(OrderModel.class);
				order.setCode(UUID.randomUUID().toString());
				order.setName(orderName);
				order.setUser(userModel);
				order.setDate(Date.from(localDateTimeAfter.atZone(ZoneId.systemDefault()).toInstant()));
				order.setCurrency(polishCurrency);
				modelService.save(order);
				localDateTimeAfter = localDateTimeAfter.plusDays(1);
			}
		}
	}
}
