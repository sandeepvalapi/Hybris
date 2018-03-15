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

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.impex.distributed.batch.ImportDataDumpStrategy;
import de.hybris.platform.impex.distributed.batch.impl.BatchData;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


public class ImportProcessCreationDataTest extends ServicelayerBaseTest
{
	private static final long NO_ITEM_FOUND_WEIGHT = BatchData.ImportData.NO_ITEM_FOUND_WEIGHT;

	@Resource
	private MediaService mediaService;
	@Resource
	private ModelService modelService;
	@Resource
	private ImportDataDumpStrategy importDataDumpStrategy;

	@Before
	public void setUp() throws Exception
	{
		final LanguageModel lang = modelService.create(LanguageModel.class);
		lang.setIsocode("de");
		lang.setName("German");
		lang.setActive(Boolean.TRUE);

		modelService.save(lang);
	}

    @Test
    public void shouldDivideScriptIntoBatchesSkippingFirstInvalidHeaderAndItsLines() throws Exception
    {
        // given
    	final String invalidData = "INSERT NonExistent;foo;bar;baz\n"
                + ";foo1;bar1;baz1;\n"
                + ";foo2;bar2;baz2;\n";
        final String validData = generateImportString("INSERT", 200);
        final String importData = invalidData + validData;
        final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
                importDataDumpStrategy);

        // when
        final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
        final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

        // then
        assertThat(batches).hasSize(2);

        final ImportBatchCreationData batch1 = batches.get(0);
        final ImportBatchCreationData batch2 = batches.get(1);

        assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch1.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch2.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch2.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
    }

    @Test
    public void shouldDivideScriptIntoBatchesSkippingSecondInvalidHeaderAndItsLines() throws Exception
    {
        // given
        final String validData1 = generateImportString("INSERT_UPDATE", 300);
    	final String invalidData = "INSERT NonExistent;foo;bar;baz\n"
                + ";foo1;bar1;baz1;\n"
                + ";foo2;bar2;baz2;\n";
        final String validData2 = generateImportString("INSERT", 200);
        final String importData = validData1 + invalidData + validData2;
        final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
                importDataDumpStrategy);

        // when
        final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
        final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

        // then
        assertThat(batches).hasSize(5);

        final ImportBatchCreationData batch1 = batches.get(0);
        final ImportBatchCreationData batch2 = batches.get(1);
        final ImportBatchCreationData batch3 = batches.get(2);
        final ImportBatchCreationData batch4 = batches.get(3);
        final ImportBatchCreationData batch5 = batches.get(4);

        assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch1.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch2.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch2.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch3.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch3.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch4.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch4.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch5.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch5.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
    }

    @Test
    public void shouldDivideScriptIntoBatchesSkippingLastInvalidHeaderAndItsLines() throws Exception
    {
        // given
        final String validData1 = generateImportString("INSERT_UPDATE", 300);
        final String validData2 = generateImportString("INSERT", 200);
        final String invalidData = "INSERT NonExistent;foo;bar;baz\n"
                + ";foo1;bar1;baz1;\n"
                + ";foo2;bar2;baz2;\n";
        final String importData = validData1 + validData2 + invalidData;
        final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
                importDataDumpStrategy);

        // when
        final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
        final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

        // then
        assertThat(batches).hasSize(5);

        final ImportBatchCreationData batch1 = batches.get(0);
        final ImportBatchCreationData batch2 = batches.get(1);
        final ImportBatchCreationData batch3 = batches.get(2);
        final ImportBatchCreationData batch4 = batches.get(3);
        final ImportBatchCreationData batch5 = batches.get(4);

        assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch1.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch2.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch2.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch3.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch3.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch4.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch4.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
        assertThat(batch5.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
        assertThat(batch5.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
    }

	@Test
	public void shoudDivideBiggerScriptToBatches() throws Exception
	{
		// given
		final String importData = generateImportString("INSERT_UPDATE", 400);
		final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
				importDataDumpStrategy);

		// when
		final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
		final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

		// then
		assertThat(batches).hasSize(4);

		final ImportBatchCreationData batch1 = batches.get(0);
		final ImportBatchCreationData batch2 = batches.get(1);
		final ImportBatchCreationData batch3 = batches.get(2);
		final ImportBatchCreationData batch4 = batches.get(3);

		assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch1.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch2.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch2.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch3.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch3.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch4.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch4.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
	}

	@Test
	public void shouldDivideBiggerScriptToBatchesIfBatchSizeIsExceeded() throws Exception
	{
		// given
		final String importData1 = generateImportString("INSERT_UPDATE", 120);
		final String importData2 = generateImportString("INSERT", 200);
		final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode",
				getAsStream(importData1 + importData2), importDataDumpStrategy);

		// when
		final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
		final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

		// then
		assertThat(batches).hasSize(4);

		final ImportBatchCreationData batch1 = batches.get(0);
		final ImportBatchCreationData batch2 = batches.get(1);
		final ImportBatchCreationData batch3 = batches.get(2);
		final ImportBatchCreationData batch4 = batches.get(3);

		assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch1.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch2.getRemainingWorkLoad()).isEqualTo(20 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch2.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch3.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch3.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch4.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch4.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
	}

	@Test
	public void shouldDivideBiggerScriptToBatchesIfBatchIsNotExceeded() throws Exception
	{
		// given
		final String importData1 = generateImportString("INSERT_UPDATE", 60);
		final String importData2 = generateImportString("INSERT", 85);
		final String importData3 = generateImportString("REMOVE", 25);
		final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode",
				getAsStream(importData1 + importData2 + importData3), importDataDumpStrategy);

		// when
		final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
		final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

		// then
		assertThat(batches).hasSize(3);

		final ImportBatchCreationData batch1 = batches.get(0);
		final ImportBatchCreationData batch2 = batches.get(1);
		final ImportBatchCreationData batch3 = batches.get(2);

		assertThat(batch1.getRemainingWorkLoad()).isEqualTo(60 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch1.getHeader()).isEqualTo("INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch2.getRemainingWorkLoad()).isEqualTo(85 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch2.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch3.getRemainingWorkLoad()).isEqualTo(25 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch3.getHeader()).isEqualTo("REMOVE Title;code[unique=true];name[lang=en];name[lang=de]");
	}

	@Test
	public void shouldDivideBiggerScriptToBatchesWithBeforeEach() throws Exception
	{
		// given
		final String importData = generateImportStringWithBeforeEach("INSERT", 200);
		final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
				importDataDumpStrategy);

		// when
		final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
		final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

		// then
		assertThat(batches).hasSize(2);

		final ImportBatchCreationData batch1 = batches.get(0);
		final ImportBatchCreationData batch2 = batches.get(1);

		assertThat(batch1.getRemainingWorkLoad()).isEqualTo(2000);
		assertThat(batch1.getHeader())
				.isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n#% beforeEach: line.clear();\n");
		assertThat(batch2.getRemainingWorkLoad()).isEqualTo(2000);
		assertThat(batch2.getHeader())
				.isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n#% beforeEach: line.clear();\n");
	}

	@Test
	public void shouldDivideBiggerScriptToBatchesWithExternalDataMedia() throws Exception
	{
		// given
		final ImpExMediaModel externalDataMedia = createExternalDataMedia(200);
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n\"#% impex.includeExternalDataMedia(\"\"testExternalData\"\", \"\"UTF-8\"\", ';', 0, -1);\"";
		final ImportProcessCreationData.ImportProcessContext readerContext = new ImportProcessCreationData.ImportProcessContext();
		readerContext.setImpExMediaModels(Lists.newArrayList(externalDataMedia));
		final ImportProcessCreationData creationData = new ImportProcessCreationData("testCode", getAsStream(importData),
				importDataDumpStrategy, readerContext);

		// when
		final Stream<ImportBatchCreationData> streamOfBatches = creationData.initialBatches();
		final List<ImportBatchCreationData> batches = streamOfBatches.collect(Collectors.toList());

		// then
		assertThat(batches).hasSize(2);

		final ImportBatchCreationData batch1 = batches.get(0);
		final ImportBatchCreationData batch2 = batches.get(1);

		assertThat(batch1.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch1.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");
		assertThat(batch2.getRemainingWorkLoad()).isEqualTo(100 * NO_ITEM_FOUND_WEIGHT);
		assertThat(batch2.getHeader()).isEqualTo("INSERT Title;code[unique=true];name[lang=en];name[lang=de]");

	}

	private InputStream getAsStream(final String input)
	{
		return new ByteArrayInputStream(input.getBytes());
	}

	private String generateImportString(final String operation, final int numOfLines)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(operation).append(" Title").append(";code[unique=true];name[lang=en];name[lang=de]\n");
		IntStream.range(0, numOfLines)
				.forEach(i -> sb.append(";test").append(i).append(";test").append(i).append("EN--UPD;test").append(i).append("DE\n"));
		return sb.toString();
	}

	private String generateImportStringWithBeforeEach(final String operation, final int numOfLines)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(operation).append(" Title").append(";code[unique=true];name[lang=en];name[lang=de]\n");
		sb.append("#% beforeEach: line.clear();").append("\n");
		IntStream.range(0, numOfLines)
				.forEach(i -> sb.append(";test").append(i).append(";test").append(i).append("EN--UPD;test").append(i).append("DE\n"));
		return sb.toString();
	}

	private ImpExMediaModel createExternalDataMedia(final int numOfLines)
	{
		final ImpExMediaModel media = modelService.create(ImpExMediaModel.class);
		media.setCode("testExternalData");
		modelService.save(media);

		final StringBuilder sb = new StringBuilder();
		IntStream.range(0, numOfLines)
				.forEach(i -> sb.append(";test").append(i).append(";test").append(i).append("EN--UPD;test").append(i).append("DE\n"));

		mediaService.setStreamForMedia(media, getAsStream(sb.toString()));

		return media;
	}

}
