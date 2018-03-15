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
package de.hybris.platform.impex.distributed;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.imp.DefaultImportProcessor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.StringWriter;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Tests for PersistableDocumentIDRegistry
 */
@IntegrationTest
public class PersistableDocumentIDRegistryTest extends ServicelayerBaseTest
{
	@Resource
	private UserService userService;

	private static final String CUSTOMER_JAN = "jan";
	private static final String CUSTOMER_PIOTR = "piotr";

	@Test
	public void testSimulateImportWithDocumentIdUsingImpexReader() throws Exception
	{
		final String importData1 = "INSERT_UPDATE Customer; uid[unique=true]; defaultPaymentAddress( &payAddress );\n;jan;payAddress0\n;piotr;payAddress1";
		final String importData2 = "INSERT Address; &payAddress; owner( Customer.uid );department;\n;payAddress0;jan;a1\n;payAddress1;piotr;a2";

		final String script = importData1 + "\n" + importData2;

		final StringWriter dump = new StringWriter();
		final PersistableDocumentIDRegistry docIDregistry = new PersistableDocumentIDRegistry("TEST_PROCESS_CODE");

		ImpExImportReader reader = new ImpExImportReader(new CSVReader(script), new CSVWriter(dump), docIDregistry,
				new DefaultImportProcessor(), ImpExManager.getImportStrictMode());

		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isFalse();
		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isFalse();

		try
		{
			reader.readLine(); //createCustomer Jan
			assertThat(reader.getDumpedLineCount()).isEqualTo(1);
			checkPersistedDocumentIDs(docIDregistry, 0);

			reader.readLine(); //createCustomer Piotr
			assertThat(reader.getDumpedLineCount()).isEqualTo(2);
			checkPersistedDocumentIDs(docIDregistry, 0);

			reader.readLine(); //createAddress payAddress0
			assertThat(reader.getDumpedLineCount()).isEqualTo(2);
			checkPersistedDocumentIDs(docIDregistry, 1);

			reader.readLine(); //createAddress payAddress1
			assertThat(reader.getDumpedLineCount()).isEqualTo(2);
			checkPersistedDocumentIDs(docIDregistry, 2);
		}
		finally
		{
			reader.close();
			docIDregistry.closeStreams();
		}
		assertThat(userService.isUserExisting(CUSTOMER_JAN)).isTrue();
		assertThat(userService.isUserExisting(CUSTOMER_PIOTR)).isTrue();

		//process dumpedLines
		assertThat(dump.getBuffer().length()).isGreaterThan(0);

		final StringWriter dump2 = new StringWriter();
		final PersistableDocumentIDRegistry docIDregistrySecondCycle = new PersistableDocumentIDRegistry("TEST_PROCESS_CODE");

		reader = new ImpExImportReader(new CSVReader(dump.getBuffer().toString()), new CSVWriter(dump2), docIDregistrySecondCycle,
				new DefaultImportProcessor(), ImpExManager.getImportStrictMode());
		try
		{
			while (reader.readLine() != null)
			{
				assertThat(reader.getDumpedLineCount()).isEqualTo(0);
				assertThat(docIDregistrySecondCycle.getQualifiersMap(DocumentIDRegistry.MODE.RESOLVED)).isEmpty();
			}
		}
		finally
		{
			reader.close();
			docIDregistrySecondCycle.closeStreams();
			checkPersistedDocumentIDs(docIDregistrySecondCycle, 2);
			docIDregistrySecondCycle.clearAllDocumentIds();
			checkPersistedDocumentIDs(docIDregistrySecondCycle, 0);
		}
		final CustomerModel storedJan = userService.getUserForUID(CUSTOMER_JAN, CustomerModel.class);
		assertThat(storedJan.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedJan.getDefaultPaymentAddress().getDepartment()).isEqualTo("a1");

		final CustomerModel storedPiotr = userService.getUserForUID(CUSTOMER_PIOTR, CustomerModel.class);
		assertThat(storedPiotr.getDefaultPaymentAddress()).isNotNull();
		assertThat(storedPiotr.getDefaultPaymentAddress().getDepartment()).isEqualTo("a2");

	}

	private void checkPersistedDocumentIDs(final PersistableDocumentIDRegistry docIDregistry, final int expectedSize)
	{
		//the internal map should be always empty in the persistable registry
		assertThat(docIDregistry.getQualifiersMap(DocumentIDRegistry.MODE.RESOLVED)).isEmpty();
		assertThat(docIDregistry.getAllImpexDocumentIDs()).hasSize(expectedSize);
	}

}
