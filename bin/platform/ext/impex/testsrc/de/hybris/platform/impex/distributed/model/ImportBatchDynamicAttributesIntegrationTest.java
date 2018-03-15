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
package de.hybris.platform.impex.distributed.model;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class ImportBatchDynamicAttributesIntegrationTest extends ServicelayerBaseTest
{
    private static final String TEST_CONTENT = "INSERT Title;code[unique=true]\n;foo\n;bar";

    @Resource
	private ModelService modelService;

	@Test
	public void shouldFindCorrespondingImportBatchContentForImportBatch() throws Exception
	{
		// given
        final ImportBatchContentModel content = createBatchContent("testContentCode", TEST_CONTENT);
        final ImportBatchModel importBatch = createImportBatch();
        importBatch.setImportContentCode(content.getCode());
        modelService.saveAll();

		// when
		final ImportBatchContentModel foundContentModel = importBatch.getImportBatchContent();

		// then
		assertThat(foundContentModel).isNotNull();
		assertThat(foundContentModel.getCode()).isEqualTo(content.getCode());
        assertThat(foundContentModel.getContent()).isEqualTo(TEST_CONTENT);
	}

	private ImportBatchModel createImportBatch()
	{
		final DistributedProcessModel process = modelService.create(DistributedProcessModel.class);
		process.setCode(UUID.randomUUID().toString());
		process.setState(DistributedProcessState.WAITING_FOR_EXECUTION);
		process.setCurrentExecutionId("testExecutionId");

		final ImportBatchModel importBatch = modelService.create(ImportBatchModel.class);
		importBatch.setId("testId");
		importBatch.setExecutionId("testExecutionId");
		importBatch.setProcess(process);
		importBatch.setType(BatchType.INITIAL);

		return importBatch;
	}

	private ImportBatchContentModel createBatchContent(final String code, final String content)
	{
		final ImportBatchContentModel model = modelService.create(ImportBatchContentModel.class);
		model.setCode(code);
		model.setContent(content);

        return model;
	}

}
