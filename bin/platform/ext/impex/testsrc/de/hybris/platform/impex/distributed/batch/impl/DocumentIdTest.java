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
package de.hybris.platform.impex.distributed.batch.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.distributed.process.ImportProcessCreationData;
import de.hybris.platform.impex.distributed.task.ProcessImpexTask;
import de.hybris.platform.impex.model.ImpexDocumentIdModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;


/**
 * Tests for impex using documentID feature
 */
@IntegrationTest
public class DocumentIdTest extends ServicelayerBaseTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ImportService importService;
	@Resource
	private UserService userService;
	@Resource
	private ProcessImpexTask processImpexTask;
	@Resource
	private DistributedProcessService distributedProcessService;
	@Resource
	private AddressService addressService;
	@Resource
	private ImportDataDumpStrategy importDataDumpStrategy;

	private static final String CUSTOMER_JAN = "jan";
	private static final String CUSTOMER_PIOTR = "piotr";

	@Test
	public void testCorrectDocumentIdUsageWithImportService() throws Exception
	{
		//given
		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isFalse();
		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isFalse();

		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/testDocumentID.csv", "UTF-8");
		config.setScript(impExResource);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();

		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isTrue();
		final CustomerModel storedJan = userService.getUserForUID(CUSTOMER_JAN, CustomerModel.class);
		assertThat(storedJan.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedJan.getDefaultPaymentAddress().getDepartment()).isEqualTo("a1");

		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID(CUSTOMER_PIOTR, CustomerModel.class);
		assertThat(storedPiotr.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedPiotr.getDefaultPaymentAddress().getDepartment()).isEqualTo("a2");

	}

	@Test
	public void testCorrectDocumentIdUsageWithDistributedProcessService() throws Exception
	{
		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isFalse();
		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isFalse();

		final String input = "INSERT_UPDATE Customer; uid[unique=true]; defaultPaymentAddress( &payAddress );\n;jan;payAddress0\n;piotr;payAddress1\n"
				+ "INSERT Address; &payAddress; owner( Customer.uid );department;\n;payAddress0;jan;a1\n;payAddress1;piotr;a2\n";
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_PROCESS", getAsStream(input),
				importDataDumpStrategy);

		final DistributedProcessModel process = distributedProcessService.create(data);

		distributedProcessService.start(process.getCode());
		distributedProcessService.wait(process.getCode(), 10);

		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);

		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isTrue();
		final CustomerModel storedJan = userService.getUserForUID(CUSTOMER_JAN, CustomerModel.class);
		assertThat(addressService.getAddressesForOwner(storedJan)).hasSize(1);
		assertThat(storedJan.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedJan.getDefaultPaymentAddress().getDepartment()).isEqualTo("a1");

		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID(CUSTOMER_PIOTR, CustomerModel.class);
		assertThat(addressService.getAddressesForOwner(storedPiotr)).hasSize(1);
		assertThat(storedPiotr.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedPiotr.getDefaultPaymentAddress().getDepartment()).isEqualTo("a2");
		
		final long waitUntil = System.currentTimeMillis() + 30 * 1000;
		while( getImpexDocumentIDsCount(process.getCode()) > 0  && System.currentTimeMillis() < waitUntil )
		{
			Thread.sleep(500);
		}
		assertThat(getImpexDocumentIDsCount(process.getCode())).isEqualTo(0);
	}

	@Test
	public void testImportWithDocumentIdAndProcessChangesTasksDirectly() throws Exception
	{
		// given
		//import data = header + value lines

		final String importData1 = "INSERT_UPDATE Customer; uid[unique=true]; defaultPaymentAddress( &payAddress );\n;jan;payAddress0\n;piotr;payAddress1";
		final String importData2 = "INSERT Address; &payAddress; owner( Customer.uid );department;\n;payAddress0;jan;a1\n;payAddress1;piotr;a2";

		final TestImportBatchHandler handler1 = new TestImportBatchHandler(importData1);
		final TestImportBatchHandler handler2 = new TestImportBatchHandler(importData2);

		// when
		processImpexTask.execute(handler1);
		assertThat(getImpexDocumentIDsCount(null)).isEqualTo(0);
		processImpexTask.execute(handler2);
		assertThat(getImpexDocumentIDsCount(null)).isEqualTo(2);
		final TestImportBatchHandler handler3 = new TestImportBatchHandler(handler1.getOutputData());
		processImpexTask.execute(handler3);
		assertThat(getImpexDocumentIDsCount(null)).isEqualTo(2); //the documentIds are not cleaned in the test handler

		// then
		assertThat(handler1.getRemainingWorkLoad()).isEqualTo(20);
		assertThat(handler1.getOutputData()).isNotNull();

		assertThat(handler2.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler2.getOutputData()).isNull();

		assertThat(handler3.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler3.getOutputData()).isNull();

		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isTrue();
		final CustomerModel storedJan = userService.getUserForUID(CUSTOMER_JAN, CustomerModel.class);
		assertThat(storedJan.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedJan.getDefaultPaymentAddress().getDepartment()).isEqualTo("a1");

		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID(CUSTOMER_PIOTR, CustomerModel.class);
		assertThat(storedPiotr.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedPiotr.getDefaultPaymentAddress().getDepartment()).isEqualTo("a2");
	}

	private InputStream getAsStream(final String input)
	{
		return new ByteArrayInputStream(input.getBytes());
	}

	private int getImpexDocumentIDsCount(final String processCode)
	{
		final String baseQuery = "SELECT count({PK}) FROM {" + ImpexDocumentIdModel._TYPECODE + "}";
		final FlexibleSearchQuery fQuery = processCode == null
				? new FlexibleSearchQuery(baseQuery + " WHERE {" + ImpexDocumentIdModel.PROCESSCODE + "} IS NULL")
				: new FlexibleSearchQuery(baseQuery + " WHERE {" + ImpexDocumentIdModel.PROCESSCODE + "}=?processcode",
						ImmutableMap.of("processcode", processCode));
		fQuery.setDisableCaching(true);
		fQuery.setResultClassList(Arrays.asList(Integer.class));
				
		return flexibleSearchService.<Integer> search(fQuery).getResult().get(0).intValue();
	}

}
