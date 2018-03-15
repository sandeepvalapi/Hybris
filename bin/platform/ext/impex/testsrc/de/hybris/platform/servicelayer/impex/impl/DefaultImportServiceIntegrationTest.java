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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExError;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImpExValueLineError;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultImportServiceIntegrationTest extends ServicelayerTest
{

	private static final Logger LOG = Logger.getLogger(DefaultImportServiceIntegrationTest.class);

	@Resource
	private ImportService importService;
	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private UserService userService;

	private final PropertyConfigSwitcher distributedImpexFlag = new PropertyConfigSwitcher(
			ImportService.DISTRIBUTED_IMPEX_GLOBAL_FLAG);

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
	}

	@After
	public void setLegacyMode()
	{
		distributedImpexFlag.switchBackToDefault();
	}

	@Test
	public void testFileBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";
		final File testFile = File.createTempFile("test", "test");
		final PrintWriter writer = new PrintWriter(testFile);
		writer.write(data);
		writer.close();

		final ImpExResource res = new FileBasedImpExResource(testFile, "windows-1252");
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
		assertEquals("windows-1252", media.getEncoding().getCode().toLowerCase());
	}

	@Test
	public void testStreamBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";

		final ImpExResource res = new StreamBasedImpExResource(new ByteArrayInputStream(data.getBytes()),
				CSVConstants.HYBRIS_ENCODING);
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
	}

	@Test
	public void testMediaBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";
		final ImpExMediaModel newMedia = modelService.create(ImpExMediaModel._TYPECODE);
		try
		{
			modelService.initDefaults(newMedia);
		}
		catch (final ModelInitializationException e)
		{
			throw new SystemException(e);
		}
		modelService.save(newMedia);
		mediaService.setDataForMedia(newMedia, data.getBytes());

		final ImpExResource res = new MediaBasedImpExResource(newMedia);
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
	}

	@Test
	public void testImportByResource()
	{
		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
				"INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportResult result = importService.importData(mediaRes);
		assertNotNull(result);
		assertNotNull("Language 'test' was not imported", getOrCreateLanguage("test"));
	}

	@Test
	public void testImportByConfig()
	{
		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
				"INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = new ImportConfig();
		config.setScript(mediaRes);

		final ImportResult result = importService.importData(config);
		assertNotNull(result);
		assertTrue(result.isSuccessful());
		assertFalse(result.isError());
		assertFalse(result.hasUnresolvedLines());
		assertTrue(result.isFinished());
		assertNotNull("Language 'test' was not imported", getOrCreateLanguage("test"));
	}

	@Test
	public void testImportByConfigWithError()
	{
		internalImportByConfigWithError(false);
	}

	@Test
	public void testImportByConfigWithErrorFailSafe()
	{
		internalImportByConfigWithError(true);
	}

	private void internalImportByConfigWithError(final boolean failOnError)
	{
		try
		{
			TestUtils.disableFileAnalyzer();
			final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
					"UPDATE Language;isocode[unique=true];active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);

			final ImportConfig config = new ImportConfig();
			config.setScript(mediaRes);
			config.setFailOnError(failOnError);

			final ImportResult result = importService.importData(config);
			assertNotNull(result);
			assertFalse(result.isSuccessful());
			assertTrue(result.isError());
			assertTrue(result.hasUnresolvedLines());
			assertNotNull(result.getUnresolvedLines());
			assertTrue(result.isFinished());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testCreateExportImportJobs()
	{
		//innerTestType("ZoneDeliveryMode");
		try
		{
			final ItemModel modelItem = modelService.create("ImpExImportCronJob");

			modelService.save(modelItem);
			//System.out.println(modelService.getSource(modelItem));
			Assert.assertEquals(ImpExManager.getInstance().createDefaultImpExImportCronJob().getComposedType(),
					((Item) modelService.getSource(modelItem)).getComposedType());

		}
		finally
		{
			//nothing here
		}

		try
		{
			final ItemModel modelItem = modelService.create("ImpExExportCronJob");

			modelService.save(modelItem);
			//System.out.println(modelService.getSource(modelItem));
			Assert.assertEquals(ImpExManager.getInstance().createDefaultExportCronJob().getComposedType(),
					((Item) modelService.getSource(modelItem)).getComposedType());

		}
		finally
		{
			//nothing here
		}
	}


	@Test
	public void shouldImportScriptContainingInsertUniquelessItemUsingDistributedImpex() throws Exception
	{
		// given
		final String inputScript = "INSERT Address;firstName;lastName;shippingAddress;billingAddress;contactAddress;owner(User.uid)\n" //
				+ ";foo1;bar1;false;false;true;admin;\n"
				+ ";foo2;bar2;false;false;true;admin;\n"
				+ ";foo3;bar3;false;false;true;admin;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);

		// when
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		final List<? extends ImpExError> errors = collectErrors(importResult);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(errors).isEmpty();
		assertThat(findAddressByFirstNameAndLastName("foo1", "bar1")).isNotNull();
		assertThat(findAddressByFirstNameAndLastName("foo2", "bar2")).isNotNull();
		assertThat(findAddressByFirstNameAndLastName("foo3", "bar3")).isNotNull();
	}

	public AddressModel findAddressByFirstNameAndLastName(final String firstName, final String lastName)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"SELECT {PK} FROM {Address} WHERE {firstName}=?firstName AND {lastName}=?lastName");
		fQuery.addQueryParameter("firstName", firstName);
		fQuery.addQueryParameter("lastName", lastName);

		return flexibleSearchService.searchUnique(fQuery);
	}

	@Test
	public void shouldImportScriptWithLegacyModeOnWhenGlobalSwitchIsOffUsingImportConfig()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");
		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
				"INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setLegacyMode(Boolean.TRUE);
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isFalse();
		assertThat(importResult.getCronJob().getLegacyMode()).isTrue();
	}

	@Test
	public void shouldImportScriptWithLegacyModeOffWhenGlobalSwitchIsOnUsingImportConfig()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
				"INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setLegacyMode(Boolean.FALSE);
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isTrue();
		assertThat(importResult.getCronJob().getLegacyMode()).isFalse();
	}

	@Test
	public void shouldImportScriptWithGlobalModeWhenImportConfigHasLegacyModeNull()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(
				"INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isTrue();
		assertThat(importResult.getCronJob().getLegacyMode()).isNull();
	}

	@Test
	public void shouldSeeFinishedStateForAsyncImport() throws InterruptedException
	{
		// given
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"INSERT Title;code[unique=true]\n"//
						+ ";foo;\n" //
						+ "\"#% java.lang.Thread.sleep(2000);\";\n" //
						+ ";bar;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);

		// when
		final long now = System.currentTimeMillis();
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(System.currentTimeMillis() - now).isGreaterThanOrEqualTo(2000);
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
	}

	@Test
	public void shouldDumpLinesWithInvalidOrNoHeader()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/invalid-headers-test-1.impex",
				"UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();

		// we expect 5 lines to be correct
		// 1st line is without header
		// 4th line has wrong column in header
		assertThat(result.getCronJob().getValueCount()).isEqualTo(5);
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(config.getMaxThreads()).isGreaterThan(1);

		final String importResults = new String(mediaService.getDataFromMedia(result.getUnresolvedLines()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Should dump lines with invalid or no header results:");
			LOG.debug(importResults);
		}
	}

	@Test
	public void shouldNotImportLinesWithPreviousHeaderIfInvalidCurrentOne()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/invalid-headers-test-2.impex",
				"UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();
		assertThat(result.getCronJob().getValueCount()).isEqualTo(1);
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(config.getMaxThreads()).isGreaterThan(1);

		final String importResults = new String(mediaService.getDataFromMedia(result.getUnresolvedLines()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Should not import lines with previous header if invalid current one results:");
			LOG.debug(importResults);
		}
	}

	@Test
	public void shouldRunImportUsingDistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT_UPDATE Title;code[unique=true]\n;foo\n;bar\n;baz";
		final ImportConfig config = new ImportConfig();
		config.setScript(inputScript);
		config.setDistributedImpexEnabled(true);

		// when
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);

		// then
		assertThat(importResult).isNotNull();
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isRunning()).isFalse();

		final TitleModel fooTitle = findTitleForCode("foo");
		final TitleModel barTitle = findTitleForCode("bar");
		final TitleModel bazTitle = findTitleForCode("baz");
		assertThat(fooTitle).isNotNull();
		assertThat(barTitle).isNotNull();
		assertThat(bazTitle).isNotNull();
	}

	@Test
	public void shouldDumpUnresolvedLinesIntoResultingCronJobUsingDistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true]\n;foo\n;foo\n;foo";
		final ImportConfig config = new ImportConfig();
		config.setScript(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult).isNotNull();
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isRunning()).isFalse();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);


		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
	}

	@Test
	public void importConfigShouldOverrideGlobalFlagForDistributedImpexSetToFalse() throws Exception
	{
		// given
		distributedImpexFlag.switchToValue("false");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setDistributedImpexEnabled(true);

		// when
		final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

		// then
		assertThat(distributedImpexEnabled).isTrue();
	}

	@Test
	public void importConfigShouldOverrideGlobalFlagForDistributedImpexSetToTrue() throws Exception
	{
		// given
		distributedImpexFlag.switchToValue("true");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setDistributedImpexEnabled(false);

		// when
		final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

		// then
		assertThat(distributedImpexEnabled).isFalse();
	}

	@Test
	public void defaultSettingForDistributedImpexShouldBeDisabled() throws Exception
	{
		// given
		final ImportConfig importConfig = new ImportConfig();

		// when
		final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

		// then
		assertThat(distributedImpexEnabled).isFalse();
	}

	@Test
	public void shouldEnableGloballyDistributedImpex() throws Exception
	{
		// given
		distributedImpexFlag.switchToValue("true");
		final ImportConfig importConfig = new ImportConfig();

		// when
		final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

		// then
		assertThat(distributedImpexEnabled).isTrue();
	}

	@Test
	public void shouldCollectImportErrors_ConflictingItemInAnInputScript() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];name\n" //
				+ ";title1;name1;\n" //
				+ ";title1;name1;\n" //
				+ ";title3;name3;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
	}

	@Test
	public void shouldCollectImportErrors_ConflictingItemInAnInputScript_DistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];name\n" //
				+ ";title1;name1;\n" //
				+ ";title1;name1;\n" //
				+ ";title3;name3;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
	}

	private TitleModel findExistingTitle(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);

		return flexibleSearchService.searchUnique(fQuery);
	}

	@Test
	public void shouldCollectImportErrors_ConflictingItemInADB() throws Exception
	{
		// given
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("foo");
		modelService.save(title);
		final String inputScript = "INSERT Title;code[unique=true];\n" //
				+ ";foo;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
	}

	@Test
	public void shouldCollectImportErrors_ConflictingItemInADB_DistributedImpEx() throws Exception
	{
		// given
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("foo");
		modelService.save(title);
		final String inputScript = "INSERT Title;code[unique=true];\n" //
				+ ";foo;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
	}

	@Test
	public void shouldCollectImportErrors_MissingMandatoryFieldINSERTHeader() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];name[lang=en]\n" //
				+ ";;foo;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_WITH_ERROR);
	}

	@Test
	public void shouldCollectImportErrors_MissingMandatoryFieldINSERTHeader_DistributedImpex() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];name[lang=en]\n" //
				+ ";;foo;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_WITH_ERROR);
	}

	@Test
	public void shouldCollectImportErrors_InvalidINSERTHeader() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];wwww;\n" //
				+ ";foo1;dfsdsdf;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(HeaderError.class);
		assertThat(((HeaderError) listOfErrors.get(0)).getException()).isInstanceOf(HeaderValidationException.class);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT Title'");
		assertThat(listOfErrors.get(1).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT Title'");
	}

	@Test
	public void shouldCollectImportErrors_InvalidINSERTHeader_DistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT Title;code[unique=true];wwww;\n" //
				+ ";foo1;dfsdsdf;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(HeaderError.class);
		assertThat(((HeaderError) listOfErrors.get(0)).getException()).isInstanceOf(HeaderValidationException.class);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT Title'");
		assertThat(listOfErrors.get(1).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT Title'");
	}

	@Test
	public void shouldCollectImportErrors_InvalidTypeInHeader() throws Exception
	{
		// given
		final String inputScript = "INSERT NonExistent;code[unique=true];\n" //
				+ ";foo1;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(HeaderError.class);
		assertThat(((HeaderError) listOfErrors.get(0)).getException()).isInstanceOf(HeaderValidationException.class);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);

	}

	@Test
	public void shouldCollectImportErrors_InvalidTypeInHeader_DistributedImpex() throws Exception
	{
		// given
		final String inputScript = "INSERT NonExistent;code[unique=true];\n" //
				+ ";foo1;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(HeaderError.class);
		assertThat(((HeaderError) listOfErrors.get(0)).getException()).isInstanceOf(HeaderValidationException.class);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);

	}

	@Test
	public void shouldCollectImportErrors_InvalidINSERT_UPDATEHeader() throws Exception
	{
		// given
		final String inputScript = "INSERT_UPDATE Title;code[unique=true];wwww;\n" //
				+ ";foo1;dfsdsdf;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(2);
		assertThat(listOfErrors.get(0)).isInstanceOf(HeaderError.class);
		assertThat(((HeaderError) listOfErrors.get(0)).getException()).isInstanceOf(HeaderValidationException.class);
		assertThat(((ValueLineError) listOfErrors.get(1)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
		assertThat(listOfErrors.get(1)).isInstanceOf(ValueLineError.class);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT_UPDATE Title'");
		assertThat(listOfErrors.get(1).getErrorMessage()).isEqualTo("unknown attribute 'wwww' in header 'INSERT_UPDATE Title'");
	}

	@Test
	public void shouldCollectImportErrors_InvalidDataInLineINSERTHeader() throws Exception
	{
		// given
		final String inputScript = "INSERT CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;\n" //
				+ ";nonExistentCatalog;Test;true;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldCollectImportErrors_InvalidDataInLineINSERTHeader_DistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;\n" //
				+ ";nonExistentCatalog;Test;true;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldCollectImportErrors_InsufficientDataInLineUPDATEHeader() throws Exception
	{
		// given
		final String inputScript = "UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;\n" //
				+ ";nonExistentCatalog;Test;true;\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldCollectImportErrors_InvalidDataInLineUPDATEHeader_DistributedImpex() throws Exception
	{
		// given
		final String inputScript = "UPDATE CatalogVersion; catalog(id)[unique=true]; version[unique=true];active;\n" //
				+ ";nonExistentCatalog;Test;true;\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldCollectImportErrors_NonExistentNotMandatoryReferenceInDataINSERT_UPDATEHeader() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];unit(code)\n"
				+ ";foo;testCatalog:Stage;nonExistent;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"column 3: cannot resolve value 'nonExistent' for attribute 'unit'");
	}

	@Test
	public void shouldCollectImportErrors_NonExistentNotMandatoryReferenceInDataINSERT_UPDATEHeader_DistributedImpex()
			throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];unit(code)\n"
				+ ";foo;testCatalog:Stage;nonExistent;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"column 3: cannot resolve value 'nonExistent' for attribute 'unit'");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValue_DistributedImpex() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValueINSERT_UPDATEHeader() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValueUPDATEeader() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValueUPDATEHeader_DistributedImpex() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValueINSERTHeader() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_UntranslatableValueINSERTHeader_DistributedImpex() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];numberContentUnits\n"
				+ ";foo;testCatalog:Stage;one;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"cannot parse number 'one' with format specified pattern '#,##0.###' due to Unparseable number: \"one\"");
	}

	@Test
	public void shouldCollectImportErrors_NonExistentNotMandatoryReferenceInDataUPDATEHeader() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];unit(code)\n"
				+ ";foo;testCatalog:Stage;nonExistent;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"column 3: cannot resolve value 'nonExistent' for attribute 'unit'");
	}

	@Test
	public void shouldCollectImportErrors_NonExistentNotMandatoryReferenceInDataUPDATEHeader_DistributedImpex() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];unit(code)\n"
				+ ";foo;testCatalog:Stage;nonExistent;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
		assertThat(listOfErrors.get(0).getErrorMessage()).isEqualTo(
				"column 3: cannot resolve value 'nonExistent' for attribute 'unit'");
	}

	@Test
	public void shouldCollectImportErrors_RemovingMandatoryReference() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "REMOVE CatalogVersion;catalog(id)[unique=true];version[unique=true]\n" + ";testCatalog;Stage;";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.CANNOT_REMOVE_ITEM);
	}

	@Test
	public void shouldCollectImportErrors_RemovingMandatoryReference_DistributedImpEx() throws Exception
	{
		// given
		final CatalogVersionModel ctgVer = prepareTestCatalog("testCatalog", "Stage");
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("foo");
		product.setCatalogVersion(ctgVer);
		modelService.save(product);
		final String inputScript = "REMOVE CatalogVersion;catalog(id)[unique=true];version[unique=true]\n" + ";testCatalog;Stage;";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.CANNOT_REMOVE_ITEM);
	}

	@Test
	public void shouldCollectErrors_UpdatingSuperCategoriesWithNonExistentCategory_DistributedImpex() throws Exception
	{
		prepareTestCatalog("testCatalog", "Staged");
		final String inputScript = "$catalogVersion=catalogversion(catalog(id[default=testCatalog]),version[default='Staged'])[unique=true,default=testCatalog:Staged]\n"
				+ "INSERT_UPDATE Category;$catalogVersion;supercategories(code,$catalogVersion);code[unique=true]\n" //
				+ ";;DHC2;DHC1\n";
		final ImportConfig config = createImportConfig(inputScript);
		config.setDistributedImpexEnabled(true);
		final ImportResult importResult = importService.importData(config);
		waitForDistributedImpEx(importResult, 10);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldCollectErrors_UpdatingSuperCategoriesWithNonExistentCategory() throws Exception
	{
		prepareTestCatalog("testCatalog", "Staged");
		final String inputScript = "$catalogVersion=catalogversion(catalog(id[default=testCatalog]),version[default='Staged'])[unique=true,default=testCatalog:Staged]\n"
				+ "INSERT_UPDATE Category;$catalogVersion;supercategories(code,$catalogVersion);code[unique=true]\n"
				+ ";;DHC2;DHC1\n";
		final ImportConfig config = createImportConfig(inputScript);
		final ImportResult importResult = importService.importData(config);
		assertThat(importResult.isFinished()).isTrue();

		// when
		final List<? extends ImpExError> listOfErrors = collectErrors(importResult);

		// then
		assertThat(listOfErrors).hasSize(1);
		assertThat(listOfErrors.get(0)).isInstanceOf(ValueLineError.class);
		assertThat(((ValueLineError) listOfErrors.get(0)).getErrorType()).isEqualTo(
				ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
	}

	@Test
	public void shouldUpdateExistingAttributeAndMakeItEmptyByImportingEmptyCellValueInDistributeImpex() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];ean\n"
				+ ";foo;testCatalog:Stage;foo bar baz;";
		final ImportConfig importConfig = createImportConfig(inputScript);
		importConfig.setDistributedImpexEnabled(true);
		final ImportResult importResult1 = importService.importData(importConfig);
		waitForDistributedImpEx(importResult1, 10);
		final ProductModel productBefore = findProductByCode("foo");
		assertThat(productBefore).isNotNull();
		assertThat(productBefore.getEan()).isEqualTo("foo bar baz");

		// when
		final String inputScript2 = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];ean\n"
				+ ";foo;testCatalog:Stage;;";
		final ImportConfig importConfig2 = createImportConfig(inputScript2);
		importConfig2.setDistributedImpexEnabled(true);
		final ImportResult importResult2 = importService.importData(importConfig2);
		waitForDistributedImpEx(importResult2, 10);

		// then
		final List<? extends ImpExError> errors = collectErrors(importResult2);
		assertThat(errors).isEmpty();
		final ProductModel productAfter = findProductByCode("foo");
		assertThat(productAfter).isNotNull();
		assertThat(productAfter.getEan()).isNull();
	}

	@Test
	public void shouldUpdateExistingAttributeAndMakeItEmptyByImportingEmptyCellValue() throws Exception
	{
		// given
		prepareTestCatalog("testCatalog", "Stage");
		final String inputScript = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];ean\n"
				+ ";foo;testCatalog:Stage;foo bar baz;";
		final ImportConfig importConfig = createImportConfig(inputScript);
		importService.importData(importConfig);
		final ProductModel productBefore = findProductByCode("foo");
		assertThat(productBefore).isNotNull();
		assertThat(productBefore.getEan()).isEqualTo("foo bar baz");

		// when
		final String inputScript2 = "INSERT_UPDATE Product;code[unique=true];catalogversion(catalog(id),version)[unique=true];ean\n"
				+ ";foo;testCatalog:Stage;;";
		final ImportConfig importConfig2 = createImportConfig(inputScript2);
		final ImportResult importResult = importService.importData(importConfig2);

		// then
		final List<? extends ImpExError> errors = collectErrors(importResult);
		assertThat(errors).isEmpty();
		final ProductModel productAfter = findProductByCode("foo");
		assertThat(productAfter).isNotNull();
		assertThat(productAfter.getEan()).isNull();
	}

	private ProductModel findProductByCode(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Product} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);
		fQuery.setDisableCaching(true);

		final ProductModel product = flexibleSearchService.searchUnique(fQuery);
		modelService.refresh(product);

		return product;
	}


	private ImportConfig createImportConfig(final String inputScript)
	{
		final ImportConfig config = new ImportConfig();
		config.setScript(inputScript);
		config.setSynchronous(true);

		return config;
	}

	private List<? extends ImpExError> collectErrors(final ImportResult importResult)
	{
		final Stream<? extends ImpExError> errors = importService.collectImportErrors(importResult);
		return errors.collect(Collectors.toList());
	}

	private CatalogVersionModel prepareTestCatalog(final String catalogId, final String versionCode)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(catalogId);
		catalog.setName("Test Catalog");
		catalog.setDefaultCatalog(Boolean.TRUE);
		final CatalogVersionModel ctgVer = modelService.create(CatalogVersionModel.class);
		ctgVer.setCatalog(catalog);
		ctgVer.setVersion(versionCode);
		ctgVer.setActive(Boolean.TRUE);

		modelService.saveAll(catalog, ctgVer);
		return ctgVer;
	}

	private void waitForDistributedImpEx(final ImportResult importResult, final long seconds) throws InterruptedException
	{
		final long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
		double sleepMs = 10;

		while (System.nanoTime() < end)
		{
			Thread.sleep((long) sleepMs);
			sleepMs = Math.min(1000.0, sleepMs * 1.1);

			if (importResult.isFinished())
			{
				return;
			}
		}

		fail("Distributed impex process didn't finished in " + seconds + " seconds");
	}

	private TitleModel findTitleForCode(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);

		final SearchResult<TitleModel> searchResult = flexibleSearchService.search(fQuery);

		if (searchResult.getCount() == 0)
		{
			return null;
		}

		if (searchResult.getCount() > 1)
		{
			fail("Found more than one TitleModel with code: " + code);
		}

		return searchResult.getResult().get(0);
	}

	@Test
	public void invalidCellDecoratorShouldFailMultithreadedImport()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource(
				"/impex/testfiles/invalid-cell-decorator-test.impex", "UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();
		assertThat(result.getCronJob().getValueCount()).isEqualTo(4);
		assertThat(result.hasUnresolvedLines()).isTrue();
	}


	@Test
	public void testDumpAfterErrorInCreationSingleThreaded() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(1, false);
	}

	@Test
	public void testDumpAfterErrorInCreationMultiThreaded() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(10, false);
	}

	@Test
	public void testDumpAfterErrorInCreationBatchMode() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(1, true);
	}


	@Test
	public void testStreamNotReadBeyondError()
	{
		final String payload = "This stream throws an error!";
		final int errorPos = 10;
		final AtomicInteger counter = new AtomicInteger();

		final InputStream errorStream = new InputStream()
		{
			@Override
			public int read() throws IOException
			{
				try
				{
					final int pos = counter.getAndIncrement();
					if (pos < errorPos)
					{
						return payload.charAt(pos);
					}
					else
					{
						throw new IOException("Booom!");
					}
				}
				// HACK: this is actually necessary since InputStream.read(byte[],int,int) is *swallowing* any IOException from read()
				// except the very first one !!!
				catch (final IOException e)
				{
					throw new IOExceptionWrapper(e);
				}
			}

			// HACK: this is actually necessary since InputStream.read(byte[],int,int) is *swallowing* any IOException from read()
			// except the very first one !!!
			@Override
			public int read(final byte[] b, final int off, final int len) throws IOException
			{
				try
				{
					return super.read(b, off, len);
				}
				catch (final IOExceptionWrapper wrapper)
				{
					throw (IOException) wrapper.getCause();
				}
			}
		};

		try
		{
			new StreamBasedImpExResource(errorStream, "utf-8");
			fail("expected error due to stream error");
		}
		catch (final ModelSavingException e)
		{
			final IOException root = (IOException) Utilities.getRootCauseOfType(e, IOException.class);
			assertNotNull("missing root cause IOException", root);
			assertEquals("wrong exception", "Booom!", root.getMessage());
			assertEquals("stream should be at error pos and not beyond", errorPos + 1, counter.get());
		}
	}

	@Test
	public void shouldImportMediaInZipFileUsingDistributedImpex() throws IOException
	{
		assertThat(findTitleForCode("title1")).isNull();
		assertThat(findTitleForCode("title2")).isNull();
		assertThat(findTitleForCode("title3")).isNull();

		final ImpExResource res = new StreamBasedImpExResource(
				DefaultImportServiceIntegrationTest.class.getResourceAsStream("/test/impexTestZip.zip"), "UTF-8");
		final ImportConfig config = new ImportConfig();
		config.setSynchronous(true);
		config.setDistributedImpexEnabled(true);
		config.setScript(res);

		final ImportResult result = importService.importData(config);

		assertNotNull(result);
		assertTrue(result.isSuccessful());
		assertFalse(result.isError());
		assertFalse(result.hasUnresolvedLines());
		assertTrue(result.isFinished());

		assertThat(findTitleForCode("title1")).isNotNull();
		assertThat(findTitleForCode("title2")).isNotNull();
		assertThat(findTitleForCode("title3")).isNotNull();
	}

	@Test
	public void shouldImportMediaInZipFileWithCustomMainScriptUsingDistributedImpex() throws IOException
	{
		assertThat(findTitleForCode("title1")).isNull();
		assertThat(findTitleForCode("title2")).isNull();
		assertThat(findTitleForCode("title3")).isNull();

		final ImpExResource res = new StreamBasedImpExResource(
				DefaultImportServiceIntegrationTest.class.getResourceAsStream("/test/impexCustomTestZip.zip"), "UTF-8");
		final ImportConfig config = new ImportConfig();
		config.setMainScriptWithinArchive("custom.csv");
		config.setSynchronous(true);
		config.setDistributedImpexEnabled(true);
		config.setScript(res);

		final ImportResult result = importService.importData(config);

		assertNotNull(result);
		assertTrue(result.isSuccessful());
		assertFalse(result.isError());
		assertFalse(result.hasUnresolvedLines());
		assertTrue(result.isFinished());

		assertThat(findTitleForCode("title1")).isNotNull();
		assertThat(findTitleForCode("title2")).isNotNull();
		assertThat(findTitleForCode("title3")).isNotNull();
	}

	private static class IOExceptionWrapper extends RuntimeException
	{
		public IOExceptionWrapper(final IOException e)
		{
			super(e);
		}
	}


	void testDumpAfterErrorInCreationInternal(final int threads, final boolean batch) throws UnsupportedEncodingException
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");

		final ImportConfig config = new ImportConfig();
		config.setScript("INSERT_UPDATE Title; code[unique=true]; name[lang=de]; name[lang=en]\n" + ";TTT1;TTT1-de;TTT1-en;\n"
				+ ";TTT2;TTT2-de;TTT2-en;\n" + ";TTT3;TTT3-de;TTT3-en;\n");

		final Set<String> processed = new ConcurrentHashSet<>();

		final Interceptor throwErrorOnFirstCreation = new ValidateInterceptor<TitleModel>()
		{
			@Override
			public void onValidate(final TitleModel model, final InterceptorContext ctx) throws InterceptorException
			{
				if (ctx.isNew(model) && !processed.contains(model.getCode()))
				{
					processed.add(model.getCode());
					switch (model.getCode())
					{
						case "TTT1":
							throw new InterceptorException("Test exception - regular one for TTT1");
						case "TTT2":
							throw new IllegalStateException("Test exception - illegal state exception one for TTT2");
						case "TTT3":
							throw new RuntimeException("Test exception - runtime one for TTT3");
					}
				}
			}
		};
		final InterceptorMapping iMapping = new InterceptorMapping(TitleModel._TYPECODE, throwErrorOnFirstCreation,
				Collections.emptyList());

		config.setDumpingEnabled(true);
		config.setFailOnError(false);
		config.setSynchronous(true);
		config.setLegacyMode(Boolean.FALSE);
		config.setMaxThreads(threads);
		config.setDistributedImpexEnabled(batch);

		final DefaultInterceptorRegistry intReg = (DefaultInterceptorRegistry) Registry.getApplicationContext().getBean(
				"interceptorRegistry", InterceptorRegistry.class);

		intReg.registerInterceptor(iMapping);
		try
		{
			final ImportResult res = importService.importData(config);

			assertTrue("interceptor didn't hit all expected titles", processed.containsAll(Arrays.asList("TTT1", "TTT2", "TTT3")));

			assertFalse("import still running", res.isRunning());
			assertTrue("import not finished", res.isFinished());
			assertFalse("import got error", res.isError());
			assertTrue("import wasn't successful", res.isSuccessful());

			assertTitleImported("TTT1", "TTT1-de", "TTT1-en");
			assertTitleImported("TTT2", "TTT2-de", "TTT2-en");
			assertTitleImported("TTT3", "TTT3-de", "TTT3-en");
		}
		finally
		{
			intReg.unregisterInterceptor(iMapping);
		}
	}

	void assertTitleImported(final String code, final String nameDE, final String nameEN)
	{
		final TitleModel title = userService.getTitleForCode(code);
		assertNotNull(title);
		assertFalse("imported title was new", modelService.isNew(title));
		assertTrue("imported title was not up to date", modelService.isUpToDate(title));
		assertEquals("imported title has wrong code", code, title.getCode());
		assertEquals("imported title has wrong name (de)", nameDE, title.getName(Locale.GERMAN));
		assertEquals("imported title has wrong name (en)", nameEN, title.getName(Locale.ENGLISH));
	}

	@Test
	public void testReduceMaxThreads() throws InterruptedException
	{
		// given
		final int poolActive = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"\"#% impex.setMaxThreads(1);\";\n"//
						+ "INSERT Title;code[unique=true]\n"//
						+ ";foo1;\n" //
						+ ";foo2;\n" //
						+ ";foo3;\n" //
						+ ";foo4;\n" //
						+ ";foo5;\n" //
						+ ";foo6;\n" //
						+ ";foo7;\n" //
						+ ";foo8;\n" //
						+ ";foo9;\n" //
						+ ";foo10;\n" //
						+ ";foo11;\n" //
						+ ";foo12;\n" //
						+ ";foo13;\n" //
						+ ";foo14;\n" //
						+ ";foo15;\n" //
						+ ";foo16;\n" //
						+ ";foo17;\n" //
						+ ";foo18;\n" //
						+ ";foo19;\n" //
						+ ";foo20;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setMaxThreads(4);

		// when
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();

		final int poolActiveAfter = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		assertThat(poolActiveAfter)
				.overridingErrorMessage("Work threads not returned afetr multi-threaded ImpEx with setMaxThreads(1) !!!")
				.isLessThanOrEqualTo(poolActive);
	}

	@Test
	public void testGrowMaxThreads() throws InterruptedException
	{
		// given
		final int poolActive = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"\"#% impex.setMaxThreads(10);\";\n"//
						+ "INSERT Title;code[unique=true]\n"//
						+ ";foo1;\n" //
						+ ";foo2;\n" //
						+ ";foo3;\n" //
						+ ";foo4;\n" //
						+ ";foo5;\n" //
						+ ";foo6;\n" //
						+ ";foo7;\n" //
						+ ";foo8;\n" //
						+ ";foo9;\n" //
						+ ";foo10;\n" //
						+ ";foo11;\n" //
						+ ";foo12;\n" //
						+ ";foo13;\n" //
						+ ";foo14;\n" //
						+ ";foo15;\n" //
						+ ";foo16;\n" //
						+ ";foo17;\n" //
						+ ";foo18;\n" //
						+ ";foo19;\n" //
						+ ";foo20;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setMaxThreads(4);

		// when
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();

		final int poolActiveAfter = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		assertThat(poolActiveAfter)
				.overridingErrorMessage("Work threads not returned afetr multi-threaded ImpEx with setMaxThreads(1) !!!")
				.isLessThanOrEqualTo(poolActive);
	}


	@Test
	public void testSetMaxThreadsInTheMiddleOfTheScript() throws InterruptedException
	{
		// given
		final int poolActive = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"INSERT Title;code[unique=true]\n"//
						+ ";foo1;\n" //
						+ ";foo2;\n" //
						+ ";foo3;\n" //
						+ ";foo4;\n" //
						+ "\"#% impex.setMaxThreads(2);\";\n"//
						+ ";foo5;\n" //
						+ ";foo6;\n" //
						+ ";foo7;\n" //
						+ ";foo8;\n" //
						+ ";foo9;\n" //
						+ "\"#% impex.setMaxThreads(1);\";\n"//
						+ ";foo10;\n" //
						+ ";foo11;\n" //
						+ ";foo12;\n" //
						+ ";foo13;\n" //
						+ ";foo14;\n" //
						+ ";foo15;\n" //
						+ "\"#% impex.setMaxThreads(4);\";\n"//
						+ ";foo16;\n" //
						+ ";foo17;\n" //
						+ "\"#% impex.setMaxThreads(10);\";\n"// illegal - should be ignored
						+ ";foo18;\n" //
						+ ";foo19;\n" //
						+ ";foo20;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setMaxThreads(4);

		// when
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();

		final int poolActiveAfter = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		assertThat(poolActiveAfter)
				.overridingErrorMessage("Work threads not returned afetr multi-threaded ImpEx with setMaxThreads(1) !!!")
				.isLessThanOrEqualTo(poolActive);
	}

	@Test
	public void testParallelModeOffWithMultiThreadedImport() throws InterruptedException
	{
		// given
		final int poolActive = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"INSERT Title[parallel=false];code[unique=true]\n"//
						+ ";foo1;\n" //
						+ ";foo2;\n" //
						+ ";foo3;\n" //
						+ ";foo4;\n" //
						+ ";foo5;\n" //
						+ ";foo6;\n" //
						+ ";foo7;\n" //
						+ ";foo8;\n" //
						+ ";foo9;\n" //
						+ ";foo10;\n" //
						+ ";foo11;\n" //
						+ ";foo12;\n" //
						+ ";foo13;\n" //
						+ ";foo14;\n" //
						+ ";foo15;\n" //
						+ ";foo16;\n" //
						+ ";foo17;\n" //
						+ ";foo18;\n" //
						+ ";foo19;\n" //
						+ ";foo20;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setMaxThreads(4);

		// when
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();

		final int poolActiveAfter = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		assertThat(poolActiveAfter)
				.overridingErrorMessage("Work threads not returned afetr multi-threaded ImpEx with setMaxThreads(1) !!!")
				.isLessThanOrEqualTo(poolActive);
	}

	@Test
	public void testParallelModeMixedWithMultiThreadedImport() throws InterruptedException
	{
		// given
		final int poolActive = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"INSERT Title;code[unique=true]\n"//
						+ ";foo1;\n" //
						+ ";foo2;\n" //
						+ ";foo3;\n" //
						+ "INSERT Title[parallel=false];code[unique=true]\n"//
						+ ";foo4;\n" //
						+ ";foo5;\n" //
						+ ";foo6;\n" //
						+ ";foo7;\n" //
						+ ";foo8;\n" //
						+ "INSERT Title[parallel=true];code[unique=true]\n"//
						+ ";foo9;\n" //
						+ ";foo10;\n" //
						+ ";foo11;\n" //
						+ ";foo12;\n" //
						+ ";foo13;\n" //
						+ "INSERT Title[parallel=false];code[unique=true]\n"//
						+ ";foo14;\n" //
						+ ";foo15;\n" //
						+ ";foo16;\n" //
						+ ";foo17;\n" //
						+ ";foo18;\n" //
						+ ";foo19;\n" //
						+ ";foo20;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);
		config.setMaxThreads(4);

		// when
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();

		final int poolActiveAfter = Registry.getCurrentTenantNoFallback().getWorkersThreadPool().getNumActive();
		assertThat(poolActiveAfter)
				.overridingErrorMessage("Work threads not returned afetr multi-threaded ImpEx with setMaxThreads(1) !!!")
				.isLessThanOrEqualTo(poolActive);
	}


}
