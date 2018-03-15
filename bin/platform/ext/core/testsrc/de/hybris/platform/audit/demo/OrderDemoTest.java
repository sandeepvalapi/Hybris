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

package de.hybris.platform.audit.demo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OrderDemoTest extends ServicelayerBaseTest implements AuditableTest
{
	private static final String ORDER = "Order";
	private static final String ENTRIES = "entries";
	private static final String QUANTITY = "quantity";
	private static final String UNIT = "unit";
	private static final String DATE = "date";
	private static final String PRODUCT = "product";

	private static final String PRODUCT_NAME_EN_1 = "prod_en_1";
	private static final String PRODUCT_NAME_DE_1 = "prod_de_1";

	@Resource
	private ModelService modelService;
	@Resource
	private AuditConfigService auditConfigService;
	@Resource
	private AuditViewService auditViewService;

	private AuditTestConfigManager auditTestConfigManager;

	private CatalogModel catalog;
	private CatalogVersionModel catalogVersion;

	private ProductModel product;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;


	@Before
	public void setUp() throws Exception
	{
		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);
		auditTestConfigManager.enableAuditingForTypes("Product", "Order", "OrderEntry", "Unit", "Currency");
		assumeAuditEnabled();

		getOrCreateLanguage("en");
		getOrCreateLanguage("de");

		createCatalogAndCatalogVersion();
	}

	@After
	public void cleanup()
	{
		auditTestConfigManager.resetAuditConfiguration();
	}

	@Test
	public void shouldAuditOrdersAndEntries_showEntries()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		modelService.save(user);


		final CurrencyModel euroCurrency = modelService.create(CurrencyModel.class);
		euroCurrency.setIsocode("EUR");
		euroCurrency.setSymbol("E");

		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode(UUID.randomUUID().toString());
		unit.setUnitType("pieces");

		modelService.saveAll(euroCurrency, unit);

		final OrderModel order = modelService.create(OrderModel.class);
		order.setCode(UUID.randomUUID().toString());
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

		modelService.save(order);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/order-audit.xml");
		final List<ReportView> u1ReportView = auditViewService
				.getViewOn(
						TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(order.getPk()).withFullReport().build())
				.collect(toList());


		final Map<String, List<Map<String, String>>> orderReport0 = (Map<String, List<Map<String, String>>>) u1ReportView.get(0).getPayload().get(ORDER);
		assertThat(orderReport0.get(ENTRIES).size()).isEqualTo(0);

		final Map<String, List<Map<String, Object>>> orderReport1 = (Map<String, List<Map<String, Object>>>) u1ReportView.get(1).getPayload().get(ORDER);
		assertThat(orderReport1.get(ENTRIES).size()).isEqualTo(1);
		assertThat(((Long) orderReport1.get(ENTRIES).get(0).get(QUANTITY))).isEqualTo(1);
		assertThat(Integer.valueOf( ((HashMap) orderReport1.get(ENTRIES).get(0).get(PRODUCT)).size())).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport1.get(ENTRIES).get(0).get(UNIT)).size())).isEqualTo(2);

		final Map<String, List<Map<String, Object>>> orderReport2 = (Map<String, List<Map<String, Object>>>) u1ReportView.get(2).getPayload().get(ORDER);
		assertThat(orderReport2.get(ENTRIES).size()).isEqualTo(1);

		assertThat(((Long) orderReport2.get(ENTRIES).get(0).get(QUANTITY))).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport2.get(ENTRIES).get(0).get(PRODUCT)).size())).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport2.get(ENTRIES).get(0).get(UNIT)).size())).isEqualTo(2);



		final Map<String, Date> orderReport2_date = (Map<String, Date>) u1ReportView.get(2).getPayload().get(ORDER);

		orderReport2_date.get(DATE);

		assertThat(orderReport2_date.get(DATE)).isEqualTo(firstDate);

		final Map<String, Date> orderReport3_date = (Map<String, Date>) u1ReportView.get(3).getPayload().get(ORDER);
		assertThat(orderReport3_date.get(DATE)).isEqualTo(secondDate);
	}


	@Test
	public void shouldAuditOrdersAndEntries()
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
		order.setCode(UUID.randomUUID().toString());
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

		modelService.save(order);

		final AuditReportConfig testConfig = loadConfigFromFile("audit.test/order-audit.xml");
		final List<ReportView> u1ReportView = auditViewService.getViewOn(TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(order.getPk()).withFullReport().build()).collect(toList());

		final Map<String, List<Map<String, String>>> orderReport0 = (Map<String, List<Map<String, String>>>) u1ReportView.get(0).getPayload().get(ORDER);
		assertThat(orderReport0.get(ENTRIES).size()).isEqualTo(0);

		final Map<String, List<Map<String, Object>>> orderReport1 = (Map<String, List<Map<String, Object>>>) u1ReportView.get(1).getPayload().get(ORDER);
		assertThat(orderReport1.get(ENTRIES).size()).isEqualTo(1);
		assertThat(((Long) orderReport1.get(ENTRIES).get(0).get(QUANTITY))).isEqualTo(1);
		assertThat(Integer.valueOf( ((HashMap) orderReport1.get(ENTRIES).get(0).get(PRODUCT)).size())).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport1.get(ENTRIES).get(0).get(UNIT)).size())).isEqualTo(2);

		final Map<String, List<Map<String, Object>>> orderReport2 = (Map<String, List<Map<String, Object>>>) u1ReportView.get(2).getPayload().get(ORDER);
		assertThat(orderReport2.get(ENTRIES).size()).isEqualTo(1);

		assertThat(((Long) orderReport2.get(ENTRIES).get(0).get(QUANTITY))).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport2.get(ENTRIES).get(0).get(PRODUCT)).size())).isEqualTo(2);
		assertThat(Integer.valueOf( ((HashMap) orderReport2.get(ENTRIES).get(0).get(UNIT)).size())).isEqualTo(2);

		final Map<String, Date> orderReport2_date = (Map<String, Date>) u1ReportView.get(2).getPayload().get(ORDER);
		assertThat(orderReport2_date.get(DATE)).isEqualTo(firstDate);

		final Map<String, Date> orderReport3_date = (Map<String, Date>) u1ReportView.get(3).getPayload().get(ORDER);
		assertThat(orderReport3_date.get(DATE)).isEqualTo(secondDate);
	}


	private Date getDate(final int year, final int month, final int dayOfMonth)
	{
		final LocalDate firstOrderDate = LocalDate.of(year, month, dayOfMonth);
		final Date firstDate = Date.from(firstOrderDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		return firstDate;
	}


	private AuditReportConfig loadConfigFromFile(final String file)
	{
		try (InputStream resourceAsStream = UserAuditDemoTest.class.getClassLoader().getResourceAsStream(file))
		{
			final String xml = IOUtils.toString(resourceAsStream, UTF_8);
			auditConfigService.storeConfiguration("testConfig", xml);
			return auditConfigService.getConfigForName("testConfig");
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
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

}
