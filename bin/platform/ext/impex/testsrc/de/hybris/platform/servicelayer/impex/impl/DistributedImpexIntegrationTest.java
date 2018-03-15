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
package de.hybris.platform.servicelayer.impex.impl;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import de.hybris.platform.impex.distributed.process.ImportMetadata;
import de.hybris.platform.impex.model.DistributedImportProcessModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DistributedImpexIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ImportService importService;

	@Resource
	private DistributedProcessService distributedProcessService;

	private final PropertyConfigSwitcher legacyScripting = new PropertyConfigSwitcher("impex.legacy.scripting");

	private final PropertyConfigSwitcher persistenceLegacy = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void setUp()
	{
		TestCounter.getInstance().reset();
	}

	@After
	public void tearDown() throws Exception
	{
		legacyScripting.switchBackToDefault();
		persistenceLegacy.switchBackToDefault();
		TestCounter.getInstance().reset();
	}

	@Test
	public void shouldExecuteBeforeEachLine() throws Exception
	{
		// given
		legacyScripting.switchToValue("false");
		final String input = "INSERT_UPDATE Title;code[unique=true]\n" //
				+ "#%groovy% beforeEach: de.hybris.platform.servicelayer.impex.impl.DistributedImpexIntegrationTest.TestCounter.getInstance().bump();\n"
				+ ";foo1;\n" //
				+ ";foo2;\n" //
				+ ";foo3;\n" //
				+ ";foo4;\n" //
				+ ";foo5;\n";
		final ImportConfig config = new ImportConfig();
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setDistributedImpexEnabled(true);
		config.setScript(input);

		// when
		importService.importData(config);

		// then
		assertThat(TestCounter.getInstance().getCounter()).isEqualTo(5);
	}

	@Test
	public void shouldExecuteAfterEachLine() throws Exception
	{
		// given
		legacyScripting.switchToValue("false");
		final String input = "INSERT_UPDATE Title;code[unique=true]\n" //
				+ "#%groovy% afterEach: de.hybris.platform.servicelayer.impex.impl.DistributedImpexIntegrationTest.TestCounter.getInstance().bump();\n"
				+ ";foo1;\n" //
				+ ";foo2;\n" //
				+ ";foo3;\n" //
				+ ";foo4;\n" //
				+ ";foo5;\n";
		final ImportConfig config = new ImportConfig();
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setDistributedImpexEnabled(true);
		config.setScript(input);

		// when
		importService.importData(config);

		// then
		assertThat(TestCounter.getInstance().getCounter()).isEqualTo(5);
	}

	@Test
	public void shouldExecuteAfterEachLineOnlyForSuccessfullyImportedItems() throws Exception
	{
		// given
		legacyScripting.switchToValue("false");
		final String input = "INSERT Title;code[unique=true]\n" //
				+ "#%groovy% afterEach: de.hybris.platform.servicelayer.impex.impl.DistributedImpexIntegrationTest.TestCounter.getInstance().bump();\n"
				+ ";foo1;\n" //
				+ ";foo2;\n" //
				+ ";foo2;\n" //
				+ ";foo4;\n" //
				+ ";foo5;\n";
		final ImportConfig config = new ImportConfig();
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setDistributedImpexEnabled(true);
		config.setScript(input);

		// when
		importService.importData(config);

		// then
		assertThat(TestCounter.getInstance().getCounter()).isEqualTo(4);
	}


	@Test
	public void shouldKeep_SLD_ENABLED_Flag_FALSE_WhenLegacyPersistenceIsSetTo_TRUE_Globally() throws Exception
	{
		// given
		persistenceLegacy.switchToValue("true");
		final String input = "INSERT Title;code[unique=true]\n;foo;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 1));

		// then
		assertThat(process).isNotNull();
		ImportMetadataAssert.assertThat(process.getMetadata()).hasSize(3);
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.PROCESS_CODE).withValue("TEST_PROCESS");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.SLD_ENABLED).withValue("false");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.CODE_EXECUTION_FLAG).withValue("false");
	}

	@Test
	public void shouldKeep_SLD_ENABLED_Flag_TRUE_WhenLegacyPersistenceIsSetTo_FALSE_Globally() throws Exception
	{
		// given
		persistenceLegacy.switchToValue("false");
		final String input = "INSERT Title;code[unique=true]\n;foo;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 1));

		// then
		assertThat(process).isNotNull();
		ImportMetadataAssert.assertThat(process.getMetadata()).hasSize(3);
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.PROCESS_CODE).withValue("TEST_PROCESS");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.SLD_ENABLED).withValue("true");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.CODE_EXECUTION_FLAG).withValue("false");
	}

	@Test
	public void shouldKeep_SLD_ENABLED_Flag_TRUE_WhenLegacyPersistenceIsSetTo_FALSE_GloballyButConfigOverrides() throws Exception
	{
		// given
		persistenceLegacy.switchToValue("true");
		final String input = "INSERT Title;code[unique=true]\n;foo;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setSldForData(Boolean.TRUE);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 1));

		// then
		assertThat(process).isNotNull();
		ImportMetadataAssert.assertThat(process.getMetadata()).hasSize(3);
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.PROCESS_CODE).withValue("TEST_PROCESS");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.SLD_ENABLED).withValue("true");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.CODE_EXECUTION_FLAG).withValue("false");
	}

	@Test
	public void shouldKeep_SLD_ENABLED_Flag_FALSE_WhenLegacyPersistenceIsSetTo_TRUE_GloballyButConfigOverrides() throws Exception
	{
		// given
		persistenceLegacy.switchToValue("false");
		final String input = "INSERT Title;code[unique=true]\n;foo;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setSldForData(Boolean.FALSE);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 1));

		// then
		assertThat(process).isNotNull();
		ImportMetadataAssert.assertThat(process.getMetadata()).hasSize(3);
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.PROCESS_CODE).withValue("TEST_PROCESS");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.SLD_ENABLED).withValue("false");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.CODE_EXECUTION_FLAG).withValue("false");
	}

	@Test
	public void shouldKeep_CODE_EXECUTION_Flag_TRUE_WhenConfigSetsItToTrue() throws Exception
	{
		// given
		persistenceLegacy.switchToValue("true");
		final String input = "INSERT Title;code[unique=true]\n;foo;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 1));

		// then
		assertThat(process).isNotNull();
		ImportMetadataAssert.assertThat(process.getMetadata()).hasSize(3);
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.PROCESS_CODE).withValue("TEST_PROCESS");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.SLD_ENABLED).withValue("false");
        ImportMetadataAssert.assertThat(process.getMetadata()).containsMetadataKey(ImportBatchHandler.CODE_EXECUTION_FLAG).withValue("true");
	}

	@Test
	public void shouldSwitchAllBatchesInto_IMPORT_BY_LINE_ModeWhenExceptionDuringBatchProcessingOccurs() throws Exception
	{
		// given
		final String input = "INSERT Title;code[unique=true]\n" //
				+ ";foo1;\n" //
				+ ";foo2;\n" //
				+ ";foo2;\n" //
				+ ";foo4;\n" //
				+ ";foo5;\n";
		final ImportConfig config = new ImportConfig();
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setScript(input);

		// when
		importService.importData(config);
		final DistributedImportProcessModel process = asDistributedImportProcess(distributedProcessService.wait("TEST_PROCESS", 5));

		// then
		assertThat(process).isNotNull();
		final List<ImportBatchModel> inputBatches = getInputBatches(process);
		assertThat(inputBatches).hasSize(6);
		ImportMetadataAssert.assertThat(inputBatches.get(0).getMetadata()).isEmpty();
		inputBatches.subList(1, 5).stream().map(ImportBatchModel::getMetadata).forEach(m -> ImportMetadataAssert.assertThat(m)
				.containsOnlyMetadataKey(ImportBatchHandler.IMPORT_BY_LINE_FLAG).withValue("true"));
		final List<ImportBatchModel> resultBatches = getResultBatches(process);
		assertThat(resultBatches).hasSize(6);
		resultBatches.stream().map(ImportBatchModel::getMetadata).forEach(m -> ImportMetadataAssert.assertThat(m)
				.containsOnlyMetadataKey(ImportBatchHandler.IMPORT_BY_LINE_FLAG).withValue("true"));
	}

	private List<ImportBatchModel> getInputBatches(final DistributedImportProcessModel process)
	{
		return process.getBatches().stream().filter(b -> b.getType() == BatchType.INPUT).map(this::asImportBatchModel)
				.collect(toList());
	}

	private List<ImportBatchModel> getResultBatches(final DistributedImportProcessModel process)
	{
		return process.getBatches().stream().filter(b -> b.getType() == BatchType.RESULT).map(this::asImportBatchModel)
				.collect(toList());
	}

	private DistributedImportProcessModel asDistributedImportProcess(final DistributedProcessModel process)
	{
		assertThat(process).isInstanceOf(DistributedImportProcessModel.class);
		return (DistributedImportProcessModel) process;
	}

	private ImportBatchModel asImportBatchModel(final BatchModel batch)
	{
		assertThat(batch).isInstanceOf(ImportBatchModel.class);
		return (ImportBatchModel) batch;
	}

	public static final class TestCounter
	{
		private final AtomicInteger counter = new AtomicInteger(0);
		private final static TestCounter instance = new TestCounter();

		public static TestCounter getInstance()
		{
			return instance;
		}

		public void bump()
		{
			counter.incrementAndGet();
		}

		public void reset()
		{
			counter.set(0);
		}

		public int getCounter()
		{
			return counter.get();
		}
	}

	private static class ImportMetadataAssert extends GenericAssert<ImportMetadataAssert, String>
	{
		private final ImportMetadata metadata;

		protected ImportMetadataAssert(final String metadataString)
		{
			super(ImportMetadataAssert.class, metadataString);
			metadata = ImportMetadata.fromMetadata(metadataString);
		}

		public static ImportMetadataAssert assertThat(final String metadataString)
		{
			return new ImportMetadataAssert(metadataString);
		}

		public ImportMetadataAssert hasSize(final int size)
		{
			Assertions.assertThat(metadata.size()).isEqualTo(size);

			return this;
		}

		public ImportMetadataAssert isEmpty()
		{
			return hasSize(0);
		}

		public ImportMetadataValueAssert containsMetadataKey(final String key)
		{
			final String value = metadata.get(key);
			Assertions.assertThat(value).isNotNull();

			return new ImportMetadataValueAssert(value);
		}

		public ImportMetadataValueAssert containsOnlyMetadataKey(final String key)
		{
			final String value = metadata.get(key);
			hasSize(1);
			Assertions.assertThat(value).isNotNull();

			return new ImportMetadataValueAssert(value);
		}


	}

	private static class ImportMetadataValueAssert extends GenericAssert<ImportMetadataValueAssert, String>
	{

		protected ImportMetadataValueAssert(final String actual)
		{
			super(ImportMetadataValueAssert.class, actual);
		}

		public ImportMetadataValueAssert withValue(final String value)
		{
			Assertions.assertThat(actual).isNotEmpty().isEqualTo(value);

			return this;
		}
	}
}
