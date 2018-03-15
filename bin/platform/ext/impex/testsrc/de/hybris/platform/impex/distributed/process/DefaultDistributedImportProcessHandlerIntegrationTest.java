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
package de.hybris.platform.impex.distributed.process;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultDistributedImportProcessHandlerIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private UserService userService;
	@Resource
	private DistributedProcessService distributedProcessService;
	@Resource
	private ImportDataDumpStrategy importDataDumpStrategy;

	@Test
	public void shouldImportMultipleBatches() throws InterruptedException
	{
		final String input = "INSERT_UPDATE Title;code[unique=true]\n" //
				+ ";ALABAMA1\n" //
				+ ";ALABAMA2\n" //
				+ "INSERT_UPDATE Title;code[unique=true];name[lang=en]\n" //
				+ ";KENTUCKY1;Kentucky1\n" //
				+ ";KENTUCKY2;Kentucky2\n";

		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_PROCESS", getAsStream(input),
				importDataDumpStrategy);

		final DistributedProcessModel process = distributedProcessService.create(data);

		distributedProcessService.start(process.getCode());
		distributedProcessService.wait(process.getCode(), 10L);

		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);

		final TitleModel a1 = userService.getTitleForCode("ALABAMA1");
		assertThat(a1).isNotNull();
		assertThat(a1.getName()).isNull();

		final TitleModel a2 = userService.getTitleForCode("ALABAMA2");
		assertThat(a2).isNotNull();
		assertThat(a2.getName()).isNull();

		final TitleModel k1 = userService.getTitleForCode("KENTUCKY1");
		assertThat(k1).isNotNull();
		assertThat(k1.getName()).isEqualTo("Kentucky1");

		final TitleModel k2 = userService.getTitleForCode("KENTUCKY2");
		assertThat(k2).isNotNull();
		assertThat(k2.getName()).isEqualTo("Kentucky2");
	}

	@Test
	public void shouldImportPossibleBatchesWithImportRelaxedMode() throws InterruptedException
	{
		TestUtils.disableFileAnalyzer("Expected errors from invaid impex", 1000);

		final String input = "INSERT_UPDATE Title;code[unique=true]\n" //
				+ ";ALABAMA1\n;ALABAMA2\n" //
				+ "REMOVE NonExistent;code[unique=true]\n" //
				+ ";TEXAS1\n;TEXAS2\n" //
				+ "INSERT_UPDATE Title;code[unique=true];name[lang=en]\n" //
				+ ";KENTUCKY1;Kentucky1\n" //
				+ ";KENTUCKY2;Kentucky2\n";
		final ImportProcessCreationData.ImportProcessContext ctx = new ImportProcessCreationData.ImportProcessContext();
		ctx.setValidationMode(ImpExConstants.Enumerations.ImpExValidationModeEnum.IMPORT_RELAXED);
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_PROCESS", getAsStream(input),
				importDataDumpStrategy, ctx);

		final DistributedProcessModel process = distributedProcessService.create(data);

		distributedProcessService.start(process.getCode());
		distributedProcessService.wait(process.getCode(), 5);

		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);

		final TitleModel a1 = userService.getTitleForCode("ALABAMA1");
		assertThat(a1).isNotNull();
		assertThat(a1.getName()).isNull();

		final TitleModel a2 = userService.getTitleForCode("ALABAMA2");
		assertThat(a2).isNotNull();
		assertThat(a2.getName()).isNull();

		final TitleModel k1 = userService.getTitleForCode("KENTUCKY1");
		assertThat(k1).isNotNull();
		assertThat(k1.getName()).isEqualTo("Kentucky1");

		final TitleModel k2 = userService.getTitleForCode("KENTUCKY2");
		assertThat(k2).isNotNull();
		assertThat(k2.getName()).isEqualTo("Kentucky2");
	}

	private InputStream getAsStream(final String input)
	{
		return new ByteArrayInputStream(input.getBytes());
	}
}
