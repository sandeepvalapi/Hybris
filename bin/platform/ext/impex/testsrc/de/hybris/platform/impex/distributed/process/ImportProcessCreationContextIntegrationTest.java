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
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.impex.model.ImportBatchModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHandler.ModelWithDependencies;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


@IntegrationTest
public class ImportProcessCreationContextIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private ImportDataDumpStrategy importDataDumpStrategy;

	@Test
	public void shouldAssignProperCodeAndHandlerIdForNewProcess()
	{
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_CODE", getAsStream(StringUtils.EMPTY),
				importDataDumpStrategy);
		final ImportProcessCreationContext ctx = givenCreationContext(data);

		final ModelWithDependencies<DistributedProcessModel> models = ctx.createProcessModel();

		assertThat(models).isNotNull();
		assertThat(models.getAllModels()).isNotNull().hasSize(2);
		assertThat(models.getModel()).isNotNull();
		assertThat(models.getModel().getCode()).isEqualTo("TEST_CODE");
		assertThat(models.getModel().getHandlerBeanId()).isEqualTo(data.getHandlerBeanId());
	}

	@Test
	public void shouldCreateInitailBatchBasedOnBatchData()
	{
		final String input = "INSERT_UPDATE Title;code[unique=true]\n;ALABAMA1\n;ALABAMA2";
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_CODE", getAsStream(input),
				importDataDumpStrategy);
		final ImportProcessCreationContext ctx = givenCreationContext(data);

		final List<ModelWithDependencies<BatchModel>> batches = ctx.initialBatches().collect(Collectors.toList());

		assertThat(batches).isNotNull().hasSize(1);
		assertThat(batches.get(0).getAllModels()).hasSize(2);
		assertThat(batches.get(0).getModel()).isNotNull().isInstanceOf(ImportBatchModel.class);
		assertThat(ImmutableList.copyOf(batches.get(0).getAllModels()).get(1)).isNotNull()
				.isInstanceOf(ImportBatchContentModel.class);

		final ImportBatchModel batch = (ImportBatchModel) batches.get(0).getModel();
		final ImportBatchContentModel content = (ImportBatchContentModel) ImmutableList.copyOf(batches.get(0).getAllModels())
				.get(1);

		assertThat(batch.getType()).isEqualTo(BatchType.INITIAL);
		assertThat(batch.getGroup()).isEqualTo(0);
		assertThat(batch.getRemainingWorkLoad()).isEqualTo(40);
		assertThat(batch.getImportContentCode()).isEqualTo(content.getCode());

		assertThat(content.getContent()).startsWith("INSERT_UPDATE Title;code[unique=true]").contains(";ALABAMA1")
				.contains(";ALABAMA2");
	}

	@Test
	public void shouldAssignGroupsAccordingToTheHeader()
	{
		final String input = "INSERT Title;code[unique=true]\n;ALABAMA1\n;ALABAMA2\n"
				+ "INSERT_UPDATE Title;code[unique=true]\nALABAMA1\n;ALABAMA2\n"
				+ "INSERT Title;code[unique=true]\nALABAMA1\n;ALABAMA2\n"
				+ "INSERT Title;code[unique=true]\nALABAMA1\n;ALABAMA2\n";
		final ImportProcessCreationData data = new ImportProcessCreationData("TEST_CODE", getAsStream(input),
				importDataDumpStrategy);

		final ImportProcessCreationContext ctx = givenCreationContext(data);

		final Stream<ModelWithDependencies<BatchModel>> batches = ctx.initialBatches();
		final int[] groups = batches.map(b -> (ImportBatchModel) b.getModel()).mapToInt(b -> b.getGroup()).toArray();

		assertThat(groups).isEqualTo(new int[]
		{ 0, 1, 2, 2 });
	}

	private ImportProcessCreationContext givenCreationContext(final ImportProcessCreationData processData)
	{
		return new ImportProcessCreationContext(modelService, processData);
	}

	private InputStream getAsStream(final String input)
	{
		return new ByteArrayInputStream(input.getBytes());
	}
}
