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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.distributed.process.ImportProcessCreationData;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


/**
 * Tests for special column handling in impex
 */
@IntegrationTest
public class SpecialColumnTest extends ServicelayerBaseTest
{
	@Resource
	private ImportService importService;
	@Resource
	private DistributedProcessService distributedProcessService;
	@Resource
	private MediaService mediaService;
	@Resource
	private ImportDataDumpStrategy importDataDumpStrategy;


	@Test
	public void testSpecialColumnsWithStandardImpexViaImportService() throws Exception
	{
		//given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/servicelayer/test/testMedias.csv", "UTF-8");
		config.setScript(impExResource);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		checkImportedMediaData(mediaService.getMedia("media1"));
		checkImportedMediaData(mediaService.getMedia("media2"));
		checkImportedMediaData(mediaService.getMedia("media3"));
	}

	@Test
	public void testSpecialColumnsWithDistributedImpexViaImportService() throws Exception
	{
		//given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/servicelayer/test/testMedias.csv", "UTF-8");
		config.setScript(impExResource);
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");

		// when
		final ImportResult result = importService.importData(config);
		final DistributedProcessModel process = distributedProcessService.wait("TEST_PROCESS", 40);

		// then
		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		checkImportedMediaData(mediaService.getMedia("media1"));
		checkImportedMediaData(mediaService.getMedia("media2"));
		checkImportedMediaData(mediaService.getMedia("media3"));
	}

	@Test
	public void testSpecialColumnsWithDistributedProcessService() throws Exception
	{
		final String input = IOUtils.toString(getClass().getResourceAsStream("/servicelayer/test/testMedias.csv"), "UTF-8");
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_PROCESS", getAsStream(input),
				importDataDumpStrategy);

		distributedProcessService.create(data);

		distributedProcessService.start("TEST_PROCESS");
		final DistributedProcessModel process = distributedProcessService.wait("TEST_PROCESS", 40);

		assertThat(process.getState()).isEqualTo(DistributedProcessState.SUCCEEDED);

		checkImportedMediaData(mediaService.getMedia("media1"));
		checkImportedMediaData(mediaService.getMedia("media2"));
		checkImportedMediaData(mediaService.getMedia("media3"));
	}

	private void checkImportedMediaData(final MediaModel media)
	{
		assertThat(media).isNotNull();
		assertThat(mediaService.hasData(media)).isTrue();
	}

	private InputStream getAsStream(final String input)
	{
		return new ByteArrayInputStream(input.getBytes());
	}

}
