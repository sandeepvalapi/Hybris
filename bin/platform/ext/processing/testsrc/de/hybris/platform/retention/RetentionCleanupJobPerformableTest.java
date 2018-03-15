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

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;
import de.hybris.platform.retention.impl.BasicRemoveCleanupAction;
import de.hybris.platform.retention.impl.FlexibleSearchRetentionItemsProvider;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.internal.model.RetentionJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class RetentionCleanupJobPerformableTest extends ServicelayerBaseTest
{

	@Resource
	protected ModelService modelService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	private CronJobService cronJobService;

	@Resource
	private ReadAuditGateway readAuditGateway;

	private static final String PRODUCT_NAME_EN_1 = "prod_en_1";
	private static final String PRODUCT_NAME_DE_1 = "prod_de_1";
	private CatalogModel catalog;
	private CatalogVersionModel catalogVersion;
	private ProductModel product;


	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("en");
		getOrCreateLanguage("de");
		createCatalogAndCatalogVersion();
	}

	@Test
	public void testSimpleRemoveJobStrategyWithBatchSizeOneHundred2() throws Exception
	{
		//given
		final List<PK> createdPKList = createOrders("TEST_REMOVE_ORDERS", getDate(2017, 9, 1), 5, getDate(2017, 1, 1), 10,
				OrderStatus.CREATED);

		for (final PK pk : createdPKList)
		{
			Assertions.assertThat(getAuditRecordsForType(OrderModel._TYPECODE, pk)).isNotEmpty();
		}

		final String query = "SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} }  "
				+ " WHERE {os.code} =?statusCode  AND {or.name} = ?orderName ";

		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		final Map<String, String> params = new HashMap<>();
		params.put("statusCode", OrderStatus.CREATED.getCode());
		params.put("orderName", "TEST_REMOVE_ORDERS");
		rule.setQueryParameters(params);
		rule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		rule.setCode("rule");
		modelService.save(rule);

		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(100);
		job.setRetentionRule(rule);
		modelService.save(job);

		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		SearchResult<List<Object>> result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(15);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isTrue();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();

		result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(0);

		for (final PK pk : createdPKList)
		{
			Assertions.assertThat(getAuditRecordsForType(OrderModel._TYPECODE, pk)).isEmpty();
		}
	}



	private List<AuditRecord> getAuditRecordsForType(final String type, final PK pk)
	{
		return readAuditGateway.search(AuditSearchQuery.forType(type).withPkSearchRules(pk).build()).collect(toList());
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
	public void testProperGetOfNextItemsForFlexibleSearchRetentionRuleModel()
	{

		//given
		createOrders("TV", getDate(2017, 1, 1), 5, null, 0, OrderStatus.CREATED);

		final String query = "SELECT {pk},{itemType} FROM {Order} WHERE {name} = ?orderName ";
		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		final Map<String, String> queryParams = new HashMap<>();
		queryParams.put("orderName", "TV");
		rule.setQueryParameters(queryParams);
		final FlexibleSearchRetentionItemsProvider flexibleSearchRetentionItemsProvider = new FlexibleSearchRetentionItemsProvider(
				rule);
		flexibleSearchRetentionItemsProvider.setBatchSize(2);
		flexibleSearchRetentionItemsProvider.setFlexibleSearchService(flexibleSearchService);

		//when
		List<ItemToCleanup> collectionList = flexibleSearchRetentionItemsProvider.nextItemsForCleanup();
		//then
		Assertions.assertThat(collectionList).hasSize(2);

		collectionList = flexibleSearchRetentionItemsProvider.nextItemsForCleanup();
		Assertions.assertThat(collectionList).hasSize(2);

		collectionList = flexibleSearchRetentionItemsProvider.nextItemsForCleanup();
		Assertions.assertThat(collectionList).hasSize(1);

		collectionList = flexibleSearchRetentionItemsProvider.nextItemsForCleanup();
		Assertions.assertThat(collectionList).hasSize(0);
	}



	private List<PK> createOrders(final String orderName, final Date orderDateBefore, final int countBefore,
			final Date orderDateAfter, final int countAfter, final OrderStatus orderStatus)
	{

		final List<PK> createdPKList = new ArrayList<>();
		final UserModel userModel = modelService.create(UserModel.class);
		userModel.setUid("userWithOrders");
		modelService.save(userModel);

		final CurrencyModel polishCurrency = modelService.create(CurrencyModel.class);
		polishCurrency.setIsocode("ZL");
		polishCurrency.setSymbol("ZL");

		if (orderDateBefore != null)
		{
			LocalDateTime localDateTimeBefore = orderDateBefore.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			for (int i = 0; i < countBefore; i++)
			{
				final OrderModel order = modelService.create(OrderModel.class);
				order.setCode(UUID.randomUUID().toString());
				order.setName(orderName);
				order.setUser(userModel);
				order.setDate(Date.from(localDateTimeBefore.atZone(ZoneId.systemDefault()).toInstant()));
				order.setCurrency(polishCurrency);
				order.setStatus(orderStatus);
				modelService.save(order);
				createdPKList.add(order.getPk());
				localDateTimeBefore = localDateTimeBefore.plusDays(1);
			}
		}
		if (orderDateAfter != null)
		{
			LocalDateTime localDateTimeAfter = orderDateAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			for (int i = 0; i < countAfter; i++)
			{
				final OrderModel order = modelService.create(OrderModel.class);
				order.setCode(UUID.randomUUID().toString());
				order.setName(orderName);
				order.setUser(userModel);
				order.setDate(Date.from(localDateTimeAfter.atZone(ZoneId.systemDefault()).toInstant()));
				order.setCurrency(polishCurrency);
				order.setStatus(orderStatus);
				modelService.save(order);
				createdPKList.add(order.getPk());
				localDateTimeAfter = localDateTimeAfter.minusDays(1);
			}
		}
		return createdPKList;
	}

	@Test
	public void testEveryBatchIsProcessedInSeparateTransaction() throws Exception
	{
		//given
		final CurrencyModel polishCurrency = modelService.create(CurrencyModel.class);
		polishCurrency.setIsocode("ZL");
		polishCurrency.setSymbol("ZL");

		createUserRemovableByBasicStrategy();

		createUserNotRemovableByBasicStrategy(polishCurrency);

		IntStream.rangeClosed(1, 3).forEach(i -> {
			createUserRemovableByBasicStrategy();
		});

		final String query = "SELECT {u.pk},{u.itemType} FROM {User as u} WHERE {u.name} =?uName ORDER BY {u.pk} ASC";

		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		final Map<String, String> params = new HashMap<>();
		params.put("uName", "TEST_RETENTION_FRAMEWORK");
		rule.setQueryParameters(params);
		rule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		rule.setCode("rule");
		modelService.save(rule);

		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(2);
		job.setRetentionRule(rule);
		modelService.save(job);

		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isFalse();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();

		final SearchResult<List<Object>> result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(2);

	}

	private void createUserRemovableByBasicStrategy()
	{
		final UserModel userWithoutOrder1 = modelService.create(UserModel.class);
		userWithoutOrder1.setUid(UUID.randomUUID().toString());
		userWithoutOrder1.setName("TEST_RETENTION_FRAMEWORK");
		modelService.save(userWithoutOrder1);
	}

	private void createUserNotRemovableByBasicStrategy(final CurrencyModel polishCurrency)
	{
		final UserModel userWithOrder = modelService.create(UserModel.class);
		userWithOrder.setUid(UUID.randomUUID().toString());
		userWithOrder.setName("TEST_RETENTION_FRAMEWORK");

		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode(UUID.randomUUID().toString());
		order.setUser(userWithOrder);
		order.setCurrency(polishCurrency);
		order.setDate(new Date());
		modelService.save(order);
		userWithOrder.setOrders(Collections.singletonList(order));
		modelService.save(userWithOrder);
	}

	@Test
	public void testSimpleRemoveJobStrategyWithBatchSizeOneHundred() throws Exception
	{

		//given
		createOrders("TEST_REMOVE_ORDERS", getDate(2017, 9, 1), 5, getDate(2017, 1, 1), 10, OrderStatus.CREATED);

		final String query = "SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} }  "
				+ " WHERE {os.code} =?statusCode  AND {or.name} = ?orderName ";

		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		final Map<String, String> params = new HashMap<>();
		params.put("statusCode", OrderStatus.CREATED.getCode());
		params.put("orderName", "TEST_REMOVE_ORDERS");
		rule.setQueryParameters(params);
		rule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		rule.setCode("rule");
		modelService.save(rule);

		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(100);
		job.setRetentionRule(rule);
		modelService.save(job);

		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		SearchResult<List<Object>> result = flexibleSearchService.search(query, params);


		Assertions.assertThat(result.getResult()).hasSize(15);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isTrue();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();

		result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(0);
	}

	@Test
	public void testSimpleRemoveJobStrategyWithBatchSizeOne() throws Exception
	{
		//given
		createOrders("TEST_REMOVE_ORDERS2", getDate(2017, 1, 1), 5, null, 0, OrderStatus.CREATED);

		final String query = "SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} }  "
				+ " WHERE {os.code} =?statusCode  AND {or.name} = ?orderName ";

		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		final Map<String, String> params = new HashMap<>();
		params.put("statusCode", OrderStatus.CREATED.toString());
		params.put("orderName", "TEST_REMOVE_ORDERS2");
		rule.setQueryParameters(params);
		rule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		rule.setCode("rule");
		modelService.save(rule);

		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(Integer.valueOf(1));
		job.setRetentionRule(rule);
		modelService.save(job);

		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		SearchResult<List<Object>> result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(5);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isTrue();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();

		result = flexibleSearchService.search(query, params);
		Assertions.assertThat(result.getResult()).hasSize(0);
	}

	@Test
	public void testCalculatedParamsInFlexibleSearchRetentionRule()
	{
		//given
		createOrders("TEST_CALUCALTE_PARAMS", getDate(2017, 1, 1), 5, null, 0, OrderStatus.CREATED);

		final String query = "SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} }  "
				+ " WHERE {os.code} =?statusCode  AND {or.name} = ?orderName and {or.date} < ?CALC_RETIREMENT_TIME";


		final Map<String, String> params = new HashMap<>();
		params.put("statusCode", OrderStatus.CREATED.toString());
		params.put("orderName", "TEST_CALUCALTE_PARAMS");

		final FlexibleSearchRetentionRuleModel rule = new FlexibleSearchRetentionRuleModel();
		rule.setSearchQuery(query);
		rule.setQueryParameters(params);
		rule.setActionReference(BasicRemoveCleanupAction.SPRING_ID);
		rule.setCode("rule");
		rule.setRetentionTimeSeconds(Long.valueOf(60 * 60 * 24));
		modelService.save(rule);

		final RetentionJobModel job = modelService.create(RetentionJobModel.class);
		job.setCode("code");
		job.setBatchSize(Integer.valueOf(1));
		job.setRetentionRule(rule);
		modelService.save(job);

		final String query2 = "SELECT {or.pk},{or.itemType} FROM {Order as or LEFT JOIN OrderStatus as os ON {or.status} = {os.PK} }  "
				+ " WHERE {os.code} =?statusCode  AND {or.name} = ?orderName ";
		final CronJobModel cronjob = prepareCronJob("myCronjob", job);

		SearchResult<List<Object>> result = flexibleSearchService.search(query2, params);
		Assertions.assertThat(result.getResult()).hasSize(5);

		//when
		cronJobService.performCronJob(cronjob, true);

		//then
		assertThat(cronJobService.isSuccessful(cronjob)).isTrue();
		assertThat(cronJobService.isFinished(cronjob)).isTrue();
		result = flexibleSearchService.search(query2, params);
		Assertions.assertThat(result.getResult()).hasSize(0);

	}

	public void prepareData()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		modelService.save(user);

		final CurrencyModel euroCurrency = modelService.create(CurrencyModel.class);
		euroCurrency.setIsocode("EUR");
		euroCurrency.setSymbol("E");

		final CurrencyModel plnCurrency = modelService.create(CurrencyModel.class);
		plnCurrency.setIsocode("PLN");
		plnCurrency.setSymbol("zl");

		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode(UUID.randomUUID().toString());
		unit.setUnitType("pieces");

		modelService.saveAll(euroCurrency, plnCurrency, unit);

		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode("Order1");
		order.setUser(user);
		final Date firstDate = getDate(2012, 10, 1);
		order.setDate(firstDate);
		order.setCurrency(euroCurrency);

		modelService.save(order);


		final OrderEntryModel orderEntry = modelService.create(OrderEntryModel.class);
		orderEntry.setOrder(order);
		orderEntry.setProduct(product);
		orderEntry.setUnit(unit);
		orderEntry.setQuantity(Long.valueOf(1L));

		modelService.save(orderEntry);

		orderEntry.setQuantity(Long.valueOf(2L));
		modelService.save(orderEntry);

		final Date secondDate = getDate(2012, 10, 2);
		order.setDate(secondDate);
		order.setStatus(OrderStatus.CREATED);

		modelService.save(order);
	}


	private void createCatalogAndCatalogVersion()
	{
		catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		catalogVersion.setCatalog(catalog);

		product = modelService.create(ProductModel.class);
		product.setCode(UUID.randomUUID().toString());

		product.setName(PRODUCT_NAME_EN_1, Locale.ENGLISH);
		product.setName(PRODUCT_NAME_DE_1, Locale.GERMAN);

		product.setCatalogVersion(catalogVersion);


		modelService.saveAll();
	}

	private Date getDate(final int year, final int month, final int dayOfMonth)
	{
		final LocalDate firstOrderDate = LocalDate.of(year, month, dayOfMonth);
		return Date.from(firstOrderDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}




}
