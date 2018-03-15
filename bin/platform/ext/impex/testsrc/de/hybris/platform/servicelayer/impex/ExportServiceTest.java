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
package de.hybris.platform.servicelayer.impex;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.exp.converter.DefaultExportResultHandler;
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVConstants;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;

import javax.annotation.Resource;

import de.hybris.platform.util.Utilities;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ExportServiceTest extends ServicelayerTransactionalTest
{
	@Resource
	private ExportService exportService;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void testCreate() throws InterceptorException
	{
		final ImpExExportCronJobModel cronJob = modelService.create(ImpExExportCronJobModel._TYPECODE);
		modelService.initDefaults(cronJob);
		assertNotNull("no job model set to created cronjob model", cronJob.getJob());

		assertNotNull("no code set to created job model", cronJob.getJob().getCode());
		modelService.save(cronJob.getJob());
		modelService.save(cronJob);

		assertNotNull("no code set to created cronjob model", cronJob.getCode());
	}

	@Test
	public void testInit() throws InterceptorException
	{
		final ImpExExportCronJobModel cronJob = new ImpExExportCronJobModel();
		modelService.initDefaults(cronJob);

		assertNotNull("no job model set to created cronjob model", cronJob.getJob());

		modelService.save(cronJob.getJob());
		modelService.save(cronJob);

		assertNotNull("no code set to created cronjob model", cronJob.getCode());
	}

	@Test
	public void testExportByResource() throws IOException, JaloBusinessException
	{
		final ImpExResource res = new StreamBasedImpExResource(getClass().getResourceAsStream(
				"/impex/testfiles/productexportscript2.impex"), CSVConstants.HYBRIS_ENCODING);
		final ExportResult result = exportService.exportData(res);

		assertNotNull("", result);
		assertTrue("", result.isSuccessful());
		assertFalse("", result.isError());
		assertTrue("", result.isFinished());

		assertNotNull("", result.getExport());
		assertNotNull("", result.getExportedData());

		try (final DefaultExportResultHandler handler = new DefaultExportResultHandler())
		{
			handler.setExport(modelService.getSource(result.getExport()));

			final List<ZipEntry> entries = handler
					.getZipEntries(modelService.getSource(result.getExportedData()));
			StringBuilder resultMesg = new StringBuilder();

			for (final ZipEntry entry : entries)
			{
				if (entry.getName().equals("Product.csv"))
				{
					resultMesg = handler.getDataContent(entry);
				}
			}

			assertNotSame("testProduct0 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMesg.indexOf("testProduct0")));
			assertNotSame("testProduct1 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMesg.indexOf("testProduct1")));
			assertNotSame("testProduct2 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMesg.indexOf("testProduct2")));
			assertNotSame("testProduct3 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMesg.indexOf("testProduct3")));
			assertNotSame("testProduct4 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMesg.indexOf("testProduct4")));
		}
	}

	@Test
	public void testExportByConfig() throws IOException, JaloBusinessException
	{
		final int tempFilesBefore = countProcessLogsTempFiles();
		final ImpExResource res = new StreamBasedImpExResource(getClass().getResourceAsStream(
				"/impex/testfiles/productexportscript.impex"), CSVConstants.HYBRIS_ENCODING);
		res.getMedia().setFieldSeparator(Character.valueOf('|'));
		modelService.save(res.getMedia());
		final ExportConfig config = new ExportConfig();
		config.setScript(res);
		config.setFieldSeparator('|');
		final ExportResult result = exportService.exportData(config);

		assertNotNull("", result);
		assertTrue("", result.isSuccessful());
		assertFalse("", result.isError());
		assertTrue("", result.isFinished());

		try (final DefaultExportResultHandler handler = new DefaultExportResultHandler())
		{
			handler.setExport((Export) modelService.getSource(result.getExport()));
			final List<ZipEntry> entries = handler
					.getZipEntries((ImpExExportMedia) modelService.getSource(result.getExportedData()));
			StringBuilder resultMsg = new StringBuilder();

			for (final ZipEntry entry : entries)
			{
				if (entry.getName().equals("Product.csv"))
				{
					resultMsg = handler.getDataContent(entry);
				}
			}

			assertNotSame("testProduct0 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMsg.indexOf("testProduct0")));
			assertNotSame("testProduct1 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMsg.indexOf("testProduct1")));
			assertNotSame("testProduct2 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMsg.indexOf("testProduct2")));
			assertNotSame("testProduct3 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMsg.indexOf("testProduct3")));
			assertNotSame("testProduct4 was not exported", Integer.valueOf(-1), Integer.valueOf(resultMsg.indexOf("testProduct4")));

			final int tempFilesAfter = countProcessLogsTempFiles();
			assertThat(tempFilesAfter).isEqualTo(tempFilesBefore);
		}
	}

	private int countProcessLogsTempFiles()
	{
		final File platformTempDir = Utilities.getPlatformTempDir();
		return platformTempDir.listFiles((dir, name) -> name.startsWith("ImpExZip") && name.endsWith("zip")).length;
	}


	@Test
	public void invalidExportScriptShouldValidateNotOk()
	{
		final String invalidExportScript = "insert_update Catalog;id[unique=true];activeCata22222logVersion(catalog(id),version)\n"
				+ "\"#% impex.exportItems(\"\"SELECT {C:pk} FROM {Catalog as C} WHERE {C:id}='$catalog'\"\", Collections.EMPTY_MAP, Collections.singletonList( Item.class ), true, true, -1, -1 );\"\n";
		final ImpExValidationResult result = exportService.validateExportScript(invalidExportScript,
				ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT);

		assertThat(result.isSuccessful()).isFalse();
		assertThat(result.getFailureCause()).isNotEmpty();
	}

	@Test
	public void validExportScriptShouldValidateOk()
	{
		final String validExportScript = "insert_update Catalog;id[unique=true];activeCatalogVersion(catalog(id),version)\n"
				+ "\"#% impex.exportItems(\"\"SELECT {C:pk} FROM {Catalog as C} WHERE {C:id}='$catalog'\"\", Collections.EMPTY_MAP, Collections.singletonList( Item.class ), true, true, -1, -1 );\"\n";

		final ImpExValidationResult result = exportService.validateExportScript(validExportScript,
				ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT);

		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.getFailureCause()).isNull();
	}


}
