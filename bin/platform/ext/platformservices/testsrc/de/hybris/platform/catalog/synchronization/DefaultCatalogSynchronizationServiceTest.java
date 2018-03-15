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
package de.hybris.platform.catalog.synchronization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.synchronization.SyncSchedule;
import de.hybris.platform.catalog.jalo.synchronization.SyncScheduleReader;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncScheduleMediaModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.TestUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


@IntegrationTest
public class DefaultCatalogSynchronizationServiceTest extends BaseSynchronizationStatusServiceTest
{
	public static final int NUM_OF_PRODUCTS = 500;
	public static final int NUM_OF_REFS = 20;
	public static final int NUMBER_OF_THREADS = 10;
	public static final int ASYNC_CRONJOB_TIMEOUT_SECONDS = 120;

	@Resource
	private ModelService modelService;
	@Resource(name = "catalogSynchronizationService")
	private CatalogSynchronizationService syncService;
	@Resource
	private MediaService mediaService;
	@Resource
	private TypeService typeService;
	@Resource
	private ProductService productService;

	private CatalogVersionModel source, target;
	private CatalogModel catalog;

	@Before
	public void setUp() throws Exception
	{
		catalog = createCatalog("TestCatalog");
		source = createCatalogVersion(catalog, "staged");
		target = createCatalogVersion(catalog, "online");
		modelService.saveAll();
	}

	@Test
	public void testSyncWithSavedValues()
	{
		// given
		final ProductModel product = createProduct("foo", "bar", source);
		final MediaModel media = createTestMedia();
		product.setThumbnail(media);
		modelService.save(product);
		
		final SyncItemJobModel syncItemJob = createSyncJob("testsync", source, target);
		modelService.save(syncItemJob);

		final SyncConfig syncConfig = createSyncConfig(true);
		
		// when
		final SyncResult result = catalogSynchronizationService.performSynchronization(Lists.newArrayList(product), syncItemJob, syncConfig); 
		
		// then
		assertTrue( result.isFinished());
		assertEquals( CronJobResult.SUCCESS, result.getCronJob().getResult() );
		assertEquals( CronJobStatus.FINISHED, result.getCronJob().getStatus() );

		// when (again)
		final SyncResult result2 = catalogSynchronizationService.performSynchronization(Lists.newArrayList(product), syncItemJob, syncConfig); 
		
		// then
		assertTrue( result2.isFinished());
		assertEquals( CronJobResult.SUCCESS, result2.getCronJob().getResult() );
		assertEquals( CronJobStatus.FINISHED, result2.getCronJob().getStatus() );
}

	@Test
	public void shouldSynchronizeBothPartialSyncJobsIfItemsDoNotOverlap() throws Exception
	{
		//given
		final List<ProductModel> sourceProductsPart1 = createProducts(NUM_OF_PRODUCTS, 0, source);
		final List<ProductModel> sourceProductsPart2 = createProducts(NUM_OF_PRODUCTS, NUM_OF_PRODUCTS + 1, source);


		final SyncItemJobModel syncItemJob = createSyncJob("testsync", source, target);
		modelService.save(syncItemJob);

		final SyncConfig asyncConfig = createSyncConfig(false);
		final SyncConfig syncConfig = createSyncConfig(true);
		syncConfig.setAbortWhenCollidingSyncIsRunning(true);

		//when
		final SyncResult asyncResult = catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProductsPart1),
				syncItemJob, asyncConfig);

		waitForAsyncToStartOrFailAfterTimeout(asyncResult, 1);


		final SyncResult syncResult = catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProductsPart2),
				syncItemJob, syncConfig);


		waitForAsyncToFinishOrFailAfterTimeout(asyncResult, ASYNC_CRONJOB_TIMEOUT_SECONDS);


		assertThat(asyncResult).isNotNull();
		assertTrue(asyncResult.isFinished());
		assertEquals(CronJobResult.SUCCESS, asyncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.FINISHED, asyncResult.getCronJob().getStatus());

		assertThat(syncResult).isNotNull();
		assertEquals(CronJobResult.SUCCESS, syncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.FINISHED, syncResult.getCronJob().getStatus());
		assertThat(syncResult.getCronJob().getActiveCronJobHistory()).isNull();

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS * 2);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

	}

	@Test
	public void shouldNotSynchronizeTheSecondPartialSyncIfItemsOverlap() throws Exception
	{
		//given
		final List<ProductModel> sourceProductsPart1 = createProducts(NUM_OF_PRODUCTS * 2, 0, source);
		final List<ProductModel> sourceProductsPart2 = Collections.singletonList(sourceProductsPart1.get(NUM_OF_PRODUCTS));


		final SyncItemJobModel syncItemJob = createSyncJob("testsync", source, target);
		modelService.save(syncItemJob);

		final SyncConfig asyncConfig = createSyncConfig(false);
		final SyncConfig syncConfig = createSyncConfig(true);
		syncConfig.setAbortWhenCollidingSyncIsRunning(true);

		//when
		final SyncResult asyncResult = catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProductsPart1),
				syncItemJob, asyncConfig);

		waitForAsyncToStartOrFailAfterTimeout(asyncResult, 1);

		TestUtils.disableFileAnalyzer("Cron Job tries to run when another full sync is running");

		final SyncResult syncResult = catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProductsPart2),
				syncItemJob, syncConfig);

		TestUtils.enableFileAnalyzer();

		waitForAsyncToFinishOrFailAfterTimeout(asyncResult, ASYNC_CRONJOB_TIMEOUT_SECONDS);

		// then
		assertThat(asyncResult).isNotNull();
		assertTrue(asyncResult.isFinished());
		assertEquals(CronJobResult.SUCCESS, asyncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.FINISHED, asyncResult.getCronJob().getStatus());

		assertThat(syncResult).isNotNull();
		assertEquals(CronJobResult.UNKNOWN, syncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.ABORTED, syncResult.getCronJob().getStatus());
		assertThat(syncResult.getCronJob().getActiveCronJobHistory()).isNull();
		final List<CronJobHistoryModel> entries = syncResult.getCronJob().getCronJobHistoryEntries();
		assertThat(entries).isNotNull();
		assertThat(entries).hasSize(1);
		final CronJobHistoryModel historyEntry = entries.get(0);
		assertThat(historyEntry).isNotNull();
		assertThat(historyEntry.getStatus()).isSameAs(CronJobStatus.ABORTED);
		assertThat(historyEntry.getResult()).isSameAs(CronJobResult.UNKNOWN);
		assertThat(historyEntry.getStartTime()).isNotNull();

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS * 2);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

	}

	@Test
	public void shouldNotSynchronizeTheSecondPartialSyncIfOtherFullSyncIsRunning() throws Exception
	{
		//given
		final List<ProductModel> sourceProducts = createProducts(NUM_OF_PRODUCTS, 0, source);


		final SyncItemJobModel syncItemJob = createSyncJob("testsync", source, target);
		modelService.save(syncItemJob);

		final SyncConfig asyncConfig = createSyncConfig(false);
		final SyncConfig syncConfig = createSyncConfig(true);


		//when
		final SyncResult fullAsyncResult = catalogSynchronizationService.synchronize(syncItemJob, asyncConfig);

		waitForAsyncToStartOrFailAfterTimeout(fullAsyncResult, 1);

		TestUtils.disableFileAnalyzer("Cron Job tries to run when another full sync is running");

		final SyncResult partialSyncResult = catalogSynchronizationService
				.performSynchronization(Collections.singletonList(sourceProducts.get(NUM_OF_PRODUCTS / 2)), syncItemJob, syncConfig);

		TestUtils.enableFileAnalyzer();

		waitForAsyncToFinishOrFailAfterTimeout(fullAsyncResult, ASYNC_CRONJOB_TIMEOUT_SECONDS);

		// then
		assertThat(fullAsyncResult).isNotNull();
		assertTrue(fullAsyncResult.isFinished());
		assertEquals(CronJobResult.SUCCESS, fullAsyncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.FINISHED, fullAsyncResult.getCronJob().getStatus());

		assertThat(partialSyncResult).isNotNull();
		assertEquals(CronJobResult.UNKNOWN, partialSyncResult.getCronJob().getResult());
		assertEquals(CronJobStatus.UNKNOWN, partialSyncResult.getCronJob().getStatus());

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

	}


	@Test
	public void shouldNotSynchronizeTheSecondFullSyncIfOtherFullSyncIsRunning() throws Exception
	{
		//given
		createProducts(NUM_OF_PRODUCTS, 0, source);


		final SyncItemJobModel syncItemJob = createSyncJob("testsync", source, target);
		modelService.save(syncItemJob);

		final SyncConfig asyncConfig = createSyncConfig(false);


		//when
		final SyncResult asyncResult1 = catalogSynchronizationService.synchronize(syncItemJob, asyncConfig);

		waitForAsyncToStartOrFailAfterTimeout(asyncResult1, 1);

		TestUtils.disableFileAnalyzer("Cron Job tries to run when another full sync is running");

		final SyncResult asyncResult2 = catalogSynchronizationService.synchronize(syncItemJob, asyncConfig);

		waitForAsyncToFinishOrFailAfterTimeout(asyncResult1, ASYNC_CRONJOB_TIMEOUT_SECONDS);

		TestUtils.enableFileAnalyzer();

		// then
		assertThat(asyncResult1).isNotNull();
		assertTrue(asyncResult1.isFinished());
		assertEquals(CronJobResult.SUCCESS, asyncResult1.getCronJob().getResult());
		assertEquals(CronJobStatus.FINISHED, asyncResult1.getCronJob().getStatus());

		assertThat(asyncResult2).isNotNull();
		assertEquals(CronJobResult.UNKNOWN, asyncResult2.getCronJob().getResult());
		assertEquals(CronJobStatus.UNKNOWN, asyncResult2.getCronJob().getStatus());

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

	}

	private void waitForAsyncToStartOrFailAfterTimeout(final SyncResult asyncResult, int timeoutSeconds) throws Exception
	{
		if (timeoutSeconds < 1)
		{
			timeoutSeconds = 1;
		}
		final int max = 1000 * timeoutSeconds / 100;

		int counter = 0;
		while (!asyncResult.isRunning() && !asyncResult.isFinished() && counter < max)
		{
			TimeUnit.MILLISECONDS.sleep(100);
			counter++;
		}
		if (counter == max)
		{
			fail("Timeout exceeded");
		}
	}

	private void waitForAsyncToFinishOrFailAfterTimeout(final SyncResult asyncResult, int timeoutSeconds) throws Exception
	{
		if (timeoutSeconds < 1)
		{
			timeoutSeconds = 1;
		}
		final int max = 1000 * timeoutSeconds / 100;

		int counter = 0;
		while (!asyncResult.isFinished() && counter < max)
		{
			TimeUnit.MILLISECONDS.sleep(100);
			counter++;
		}
		if (counter == max)
		{
			fail("Timeout exceeded");
		}
	}


	@Test
	public void testMediaFilesProperlyRemovedUponUpdateSync()
	{
		final MediaModel src = createTestMedia();
		final Media srcJalo = modelService.getSource(src);
		final File srcFile1 = srcJalo.getFile();
		assertNotNull(srcFile1);
		assertTrue(srcFile1.exists());

		// first sync --> should re-use the same file
		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);
		modelService.refresh(src);

		final MediaModel tgt = mediaService.getMedia(target, src.getCode());
		final Media tgtJalo = modelService.getSource(tgt);
		final File tgtFile1 = tgtJalo.getFile();

		assertNotNull(tgt);
		assertNotNull(tgtFile1);
		assertTrue(tgtFile1.exists());
		assertEquals(new String(mediaService.getDataFromMedia(src)),new String(mediaService.getDataFromMedia(tgt)));
		assertEquals(tgt.getFolder(),src.getFolder());
		assertEquals(tgt.getDataPK(),src.getDataPK());
		assertEquals(tgt.getLocation(),src.getLocation());
		assertEquals(srcFile1, tgtFile1);

		// now change source media content
		mediaService.setDataForMedia(src, "Second Version of Data".getBytes());
		final File srcFile2 = srcJalo.getFile();
		assertNotNull(srcFile2);
		assertTrue(srcFile2.exists());
		assertNotEquals(new String(mediaService.getDataFromMedia(src)),new String(mediaService.getDataFromMedia(tgt)));
		assertNotEquals(tgt.getDataPK(),src.getDataPK());
		assertNotEquals(tgt.getLocation(),src.getLocation());
		assertNotEquals(srcFile2, tgtFile1);

		// target file must still exists since tgt media is still using it
		assertTrue(tgtFile1.exists());

		// second sync --> should re-use the second file *and* remove first one !
		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);
		modelService.refresh(src);
		modelService.refresh(tgt);

		final File tgtFile2 = tgtJalo.getFile();

		assertNotNull(tgtFile2);
		assertTrue(tgtFile2.exists());
		assertEquals(new String(mediaService.getDataFromMedia(src)),new String(mediaService.getDataFromMedia(tgt)));
		assertEquals(tgt.getFolder(),src.getFolder());
		assertEquals(tgt.getDataPK(),src.getDataPK());
		assertEquals(tgt.getLocation(),src.getLocation());
		assertEquals(srcFile2, tgtFile2);

		// now the first file must be deleted
		assertFalse("old media file still not removed after update + sync",tgtFile1.exists());
	}

	MediaModel createTestMedia()
	{
		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode("SyncTestMedia");
		media.setCatalogVersion(source);
		modelService.save(media);

		mediaService.setDataForMedia( media, "First Version of Data".getBytes());

		return media;
	}

	@Test
	public void shouldSynchronizeTwoCatalogsFullyMultithreaded() throws Exception
	{
		final List<ProductModel> sourceProducts = createCatalogWithNProducts(NUM_OF_PRODUCTS,NUM_OF_REFS);

		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

		modifyProductsInSource(sourceProducts);
		final ImmutableMap<String, String> expectedPropertiesAfterMod = ImmutableMap.<String, String> builder()
				.put("code", "MyCode").put("ean", "NewEan").build();
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(source).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);

		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);
	}

	@Test
	public void shouldSynchronizeWithSyncJobAndConfigSynchronous() throws Exception
	{
		final List<ProductModel> sourceProducts = createCatalogWithNProducts(NUM_OF_PRODUCTS,NUM_OF_REFS);
		final SyncConfig testSyncConfig = prepareDefaultSyncConfig();

		syncService.synchronizeFully(source, target, NUMBER_OF_THREADS);

		final ImmutableMap<String, String> expectedProperties = ImmutableMap.<String, String> builder().put("code", "MyCode")
				.put("ean", "MyEan").build();
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedProperties);

		modifyProductsInSource(sourceProducts);

		final ImmutableMap<String, String> expectedPropertiesAfterMod = ImmutableMap.<String, String> builder()
				.put("code", "MyCode").put("ean", "NewEan").build();
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(source).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);

		testSyncConfig.setSynchronous(Boolean.TRUE);
		final CatalogVersionSyncJobModel syncJob = (CatalogVersionSyncJobModel) createSyncJob(source, target);

		final SyncResult syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(NUM_OF_PRODUCTS);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(expectedPropertiesAfterMod);
	}

	@Test
	public void shouldSynchronizePartiallyWithSyncJobAndConfigSynchronous()
	{
		final List<ProductModel> sourceProducts = createCatalogWithNProducts(6,0);
		SyncConfig testSyncConfig = prepareDefaultSyncConfig();
		testSyncConfig.setSynchronous(Boolean.TRUE);
		final CatalogVersionSyncJobModel syncJob = (CatalogVersionSyncJobModel) createSyncJob(source, target);

		final Map<String, String> unchangedProps = ImmutableMap.<String, String> builder().put("code", "MyCode").put("ean", "MyEan").build();
		final Map<String, String> changedProps = ImmutableMap.<String, String> builder().put("code", "MyCode").put("ean", "NewEan").build();

		// 1. sync: 3 out of 10
		testSyncConfig.addItemToSync(sourceProducts.get(0).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(2).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(4).getPk());

		SyncResult syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(3);
		CatalogVersionAssert.assertThat(target).hasCounterPartProduct(sourceProducts.get(0));
		CatalogVersionAssert.assertThat(target).hasCounterPartProduct(sourceProducts.get(2));
		CatalogVersionAssert.assertThat(target).hasCounterPartProduct(sourceProducts.get(4));
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(unchangedProps);

		// 2. full sync - no schedule
		testSyncConfig = prepareDefaultSyncConfig();
		testSyncConfig.setSynchronous(Boolean.TRUE);

		syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(unchangedProps);

		// 3. modify + partial sync 3 out of 10
		modifyProductsInSource(sourceProducts);

		testSyncConfig = prepareDefaultSyncConfig();
		testSyncConfig.setSynchronous(Boolean.TRUE);
		testSyncConfig.addItemToSync(sourceProducts.get(1).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(3).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(5).getPk());

		syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(6);
		// those must be sync'ed and therefore have new properties
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(1), changedProps);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(3), changedProps);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(5), changedProps);
		// those must *not* be sync'ed and therefore have old properties
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(0), unchangedProps);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(2), unchangedProps);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(4), unchangedProps);

		// 4. full sync via schedule list
		testSyncConfig = prepareDefaultSyncConfig();
		testSyncConfig.setSynchronous(Boolean.TRUE);
		testSyncConfig.addItemToSync(sourceProducts.get(0).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(2).getPk());
		testSyncConfig.addItemToSync(sourceProducts.get(4).getPk());

		syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(6);
		CatalogVersionAssert.assertThat(target).hasAllProductsWithPropertiesAs(changedProps);

		// 5. partial sync with removal
		final ProductModel srcBeingRemoved = sourceProducts.get(5);
		final ProductModel tgtToRemove = modelService.get(CatalogManager.getInstance()
				.getCounterpartItem(modelService.getSource(srcBeingRemoved), modelService.getSource(target)));
		modelService.remove(srcBeingRemoved);
		assertThat(srcBeingRemoved.getItemModelContext().isRemoved()).isTrue();
		final ProductModel srcBeingChanged = sourceProducts.get(4);
		srcBeingChanged.setEan("xxx");
		final Map<String, String> changedAgainProps = ImmutableMap.<String, String> builder().put("code", "MyCode").put("ean", "xxx").build();
		modelService.save(srcBeingChanged);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(5);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(6);

		testSyncConfig = prepareDefaultSyncConfig();
		testSyncConfig.setSynchronous(Boolean.TRUE);
		testSyncConfig.addItemToDelete(tgtToRemove.getPk());
		testSyncConfig.addItemToSync(srcBeingChanged.getPk());

		syncResult = syncService.synchronize(syncJob, testSyncConfig);

		assertThat(syncResult).isNotNull();
		assertThat(syncResult.isFinished()).isTrue();
		assertThat(syncResult.isSuccessful()).isTrue();
		assertThat(syncResult.getCronJob()).isInstanceOf(CatalogVersionSyncCronJobModel.class);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(5);
		CatalogVersionAssert.assertThat(target).hasNumOfProducts(5);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(3), changedProps);
		CatalogVersionAssert.assertThat(target).hasCounterPartProductWithPropertiesAs(sourceProducts.get(4), changedAgainProps);
		assertThat(tgtToRemove.getItemModelContext().isRemoved()).isTrue();
	}

	@Override
	protected SyncItemJobModel createSyncJob(final CatalogVersionModel source, final CatalogVersionModel target)
	{
		final CatalogVersionSyncJobModel job = modelService.create(CatalogVersionSyncJobModel.class);
		job.setCode("testSyncJob");
		job.setSourceVersion(source);
		job.setTargetVersion(target);
		job.setRemoveMissingItems(true);
		job.setCreateNewItems(true);
		job.setMaxThreads(NUMBER_OF_THREADS);
		modelService.save(job);
		return job;
	}

	@Test
	public void shouldSynchronizeRemovedItems()
	{
		final List<ProductModel> sourceProducts = createCatalogWithNProducts(NUM_OF_PRODUCTS, NUM_OF_REFS);

		final CatalogVersionSyncJobModel cronJobModel = (CatalogVersionSyncJobModel) createSyncJob(source, target);

		final SyncConfig defaultSyncConfig = defaultSyncConfigForFullSynchronization();

		final SyncResult syncResultCopy = syncService.synchronize(cronJobModel, defaultSyncConfig);

		assertThat(syncResultCopy.isFinished()).isTrue();
		assertThat(syncResultCopy.isSuccessful()).isTrue();

		final List<SyncSchedule> syncSchedulesAfterCopy = originalSyncMediaFromFirstSchedule(syncResultCopy);

		final List<PK> productPKsFromSchedule = syncSchedulesAfterCopy.stream().map(SyncSchedule::getSrcPK)
				.collect(Collectors.toList());
		final List<PK> productsPKsFromSourceCatalogVersion = sourceProducts.stream().map(ProductModel::getPk)
				.collect(Collectors.toList());

		//we are not asserting the size so we can get print of list of PKs that are not spouse to be in sync schedule
		assertThat(productPKsFromSchedule).containsOnlyElementsOf(productsPKsFromSourceCatalogVersion);

		//is source catalog ok
		assertNumberOfProductAndProductReferencesInCatalogVersion(source, NUM_OF_PRODUCTS, NUM_OF_PRODUCTS * NUM_OF_REFS);

		//is target catalog ok
		assertNumberOfProductAndProductReferencesInCatalogVersion(target, NUM_OF_PRODUCTS, NUM_OF_PRODUCTS * NUM_OF_REFS);


		final List<ProductModel> removed = removeSomeProductsInSource(sourceProducts);
		assertThat(removed).isNotEmpty();

		//is source catalog ok
		assertNumberOfProductAndProductReferencesInCatalogVersion(source, NUM_OF_PRODUCTS - removed.size(),
				(NUM_OF_PRODUCTS - removed.size()) * NUM_OF_REFS - getAdditionalReferencesBeingDeleted(NUM_OF_REFS));

		final SyncResult syncResultRemove = syncService.synchronize(cronJobModel, defaultSyncConfig);

		assertThat(syncResultRemove.isFinished()).isTrue();
		assertThat(syncResultRemove.isSuccessful()).isTrue();

		final List<SyncSchedule> syncSchedulesAfterRemove = originalSyncMediaFromFirstSchedule(syncResultRemove);


		final List<PK> targetPKsFromScheduleToRemove = syncSchedulesAfterRemove.stream()
				.filter(syncSchedule -> syncSchedule.getSrcPK() == null).map(SyncSchedule::getTgtPK).collect(Collectors.toList());

		final List<PK> targetPKsFromCatalog = productService.getAllProductsForCatalogVersion(target).stream()
				.map(ProductModel::getPk).collect(Collectors.toList());

		assertThat(targetPKsFromCatalog).doesNotContainAnyElementsOf(targetPKsFromScheduleToRemove);

		assertThat(syncSchedulesAfterRemove).hasSize(removed.size() + NUM_OF_REFS);

		//is target catalog ok
		assertNumberOfProductAndProductReferencesInCatalogVersion(target, NUM_OF_PRODUCTS - removed.size(),
				(NUM_OF_PRODUCTS - removed.size()) * NUM_OF_REFS - getAdditionalReferencesBeingDeleted(NUM_OF_REFS));

	}

	@Test
	public void testSynchronizationForOneProduct()
	{

		// given
		ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)),
				source);
		modelService.save(product);
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);

		// when
		performSynchronization(syncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		product = modelService.get(product.getPk());

		final ProductModel productCounterpart = catalogSynchronizationService.getSynchronizationTargetFor(syncItemJob, product);
		SyncItemInfo syncInfo = synchronizationStatusService.getSyncInfo(product, syncItemJob);


		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), product.getPk());

		// when
		product.setEan("has been changed!!!");
		modelService.save(product);


		catalogSynchronizationService.performSynchronization(Lists.newArrayList(product), syncItemJob, prepareSyncConfig());
		product = modelService.get(product.getPk());

		syncInfo = synchronizationStatusService.getSyncInfo(product, syncItemJob);
		SyncItemInfo pullSyncInfo = synchronizationStatusService.getSyncInfo(productCounterpart, syncItemJob);

		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(pullSyncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), productCounterpart.getPk());

		// create the item on the source end
		ProductModel sourceProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_source_product", RandomStringUtils.randomAlphanumeric(3)), source);
		modelService.save(sourceProduct);

		// when
		syncInfo = synchronizationStatusService.getSyncInfo(sourceProduct, syncItemJob);

		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.COUNTERPART_MISSING, syncItemJob.getPk(), sourceProduct.getPk());

		// when

		catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProduct), syncItemJob, prepareSyncConfig());
		sourceProduct = modelService.get(sourceProduct.getPk());

		final ProductModel sourceProductCounterpart = resolveCounterpart(target, sourceProduct, "code");

		syncInfo = synchronizationStatusService.getSyncInfo(sourceProduct, syncItemJob);
		pullSyncInfo = synchronizationStatusService.getSyncInfo(sourceProductCounterpart, syncItemJob);


		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), sourceProduct.getPk());
		assertSyncInfoEquals(pullSyncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), sourceProductCounterpart.getPk());
	}


	@Test
	public void testPullSynchronizationForOneProduct()
	{

		// given
		ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)),
				source);
		modelService.save(product);
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);

		// when
		performSynchronization(syncItemJob, Lists.newArrayList(product), prepareSyncConfig());
		product = modelService.get(product.getPk());

		final ProductModel productCounterpart = catalogSynchronizationService.getSynchronizationTargetFor(syncItemJob, product);
		SyncItemInfo syncInfo = synchronizationStatusService.getSyncInfo(product, syncItemJob);


		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), product.getPk());

		// when
		product.setEan("has been changed!!!");
		modelService.save(product);


		catalogSynchronizationService.performSynchronization(Lists.newArrayList(productCounterpart), syncItemJob,
				prepareSyncConfig());
		product = modelService.get(product.getPk());



		syncInfo = synchronizationStatusService.getSyncInfo(product, syncItemJob);
		SyncItemInfo pullSyncInfo = synchronizationStatusService.getSyncInfo(productCounterpart, syncItemJob);

		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), product.getPk());
		assertSyncInfoEquals(pullSyncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), productCounterpart.getPk());

		// create the item on the target end
		final ProductModel targetProduct = createProductInCatalogVersion(
				String.format("%s%s", "additional_target_product", RandomStringUtils.randomAlphanumeric(3)), target);
		modelService.save(targetProduct);

		// when
		pullSyncInfo = synchronizationStatusService.getSyncInfo(targetProduct, syncItemJob);

		// then
		assertSyncInfoEquals(pullSyncInfo, SyncItemStatus.COUNTERPART_MISSING, syncItemJob.getPk(), targetProduct.getPk());

		// when
		catalogSynchronizationService.performSynchronization(Lists.newArrayList(targetProduct), syncItemJob,
				prepareSyncConfig());
		pullSyncInfo = synchronizationStatusService.getSyncInfo(targetProduct, syncItemJob);


		// then
		assertSyncInfoEquals(pullSyncInfo, SyncItemStatus.ITEM_MISSING, syncItemJob.getPk(), targetProduct.getPk());

	}

	@Test
	public void testGetApplicableItemsForDifferentSyncItemJobs()
	{

		// given
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);
		syncItemJob.setRootTypes(Lists.newArrayList(typeService.getComposedTypeForCode("Product")));

		final ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)), source);


		final CatalogVersionModel additionalCatalogVersion = createCatalogVersion(catalog,
				String.format("%s%s", "additional_source_version", RandomStringUtils.randomAlphanumeric(3)));


		final SyncItemJobModel additionalSyncJob = createSyncJob(RandomStringUtils.randomAlphanumeric(5), additionalCatalogVersion,
				target);
		additionalSyncJob.setRootTypes(Lists.newArrayList(typeService.getComposedTypeForCode("Media")));

		final MediaModel thumbnail = modelService.create(MediaModel.class);
		thumbnail.setCode("thumbnail");
		thumbnail.setCatalogVersion(additionalCatalogVersion);

		modelService.saveAll();


		// when
		final List<ItemModel> applicableForBaseSyncJob = catalogSynchronizationService
				.getApplicableItems(Lists.newArrayList(product, thumbnail), syncItemJob);
		final List<ItemModel> applicableForAdditionalSyncJob = catalogSynchronizationService
				.getApplicableItems(Lists.newArrayList(product, thumbnail), additionalSyncJob);


		// then
		assertThat(applicableForBaseSyncJob).isNotEmpty().containsExactly(product);
		assertThat(applicableForAdditionalSyncJob).isNotEmpty().containsExactly(thumbnail);

	}

	@Test
	public void testGetSyncCounterpartItems()
	{

		// given
		final ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)),
				source);
		modelService.save(product);
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);


		performSynchronization(syncItemJob, Lists.newArrayList(product), prepareSyncConfig());

		final ProductModel productCounterpart = resolveCounterpart(target, product, "code");

		// when
		SyncItemInfo syncInfo = synchronizationStatusService.getSyncInfo(product, syncItemJob);
		ProductModel targetItem = catalogSynchronizationService.getSynchronizationTargetFor(syncItemJob, product);
		ProductModel sourceItem = catalogSynchronizationService.getSynchronizationSourceFor(syncItemJob, product);

		// then
		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), product.getPk());
		assertThat(targetItem).isNotNull();
		assertThat(targetItem.getCode()).isEqualTo(product.getCode());
		assertThat(targetItem.getCatalogVersion()).isEqualTo(target);

		assertThat(sourceItem).isNull();


		// when

		syncInfo = synchronizationStatusService.getSyncInfo(productCounterpart, syncItemJob);
		targetItem = catalogSynchronizationService.getSynchronizationTargetFor(syncItemJob, productCounterpart);
		sourceItem = catalogSynchronizationService.getSynchronizationSourceFor(syncItemJob, productCounterpart);

		assertSyncInfoEquals(syncInfo, SyncItemStatus.IN_SYNC, syncItemJob.getPk(), productCounterpart.getPk());

		assertThat(sourceItem).isNotNull();
		assertThat(sourceItem.getCode()).isEqualTo(product.getCode());
		assertThat(sourceItem.getCatalogVersion()).isEqualTo(source);

		assertThat(targetItem).isNull();

	}



	private int getAdditionalReferencesBeingDeleted(final int refPerProduct)
	{
		return (refPerProduct + 1) * refPerProduct / 2;
	}

	private List<ProductModel> removeSomeProductsInSource(final List<ProductModel> sourceProducts)
	{
		final List<ProductModel> toRemove = sourceProducts.subList(sourceProducts.size() / 2, sourceProducts.size());
		modelService.removeAll(toRemove);
		return toRemove;
	}

	private void modifyProductsInSource(final List<ProductModel> sourceProducts)
	{
		int i = 0;
		for (final ProductModel product : sourceProducts)
		{
			product.setEan("NewEan" + i);
			modelService.save(product);
			i++;
		}
	}

	private List<ProductModel> createCatalogWithNProducts(final int NUM_OF_PRODUCTS, final int NUM_OF_REFS)
	{
		final List<ProductModel> sourceProducts = createProducts(NUM_OF_PRODUCTS, 0, source);
		createProductReferences(sourceProducts, NUM_OF_REFS);
		CatalogVersionAssert.assertThat(source).hasNumOfProducts(NUM_OF_PRODUCTS);

		return sourceProducts;
	}

	private List<ProductModel> createProducts(final int numOfProducts, final int offset, final CatalogVersionModel version)
	{
		final List<ProductModel> products = new ArrayList<>(numOfProducts);
		for (int i = offset; i < numOfProducts + offset; i++)
		{
			products.add(createProduct("MyCode-" + i, "MyEan-" + i, version));
		}

		return products;
	}

	private ProductModel createProduct(final String code, final String ean, final CatalogVersionModel version)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setEan(ean);
		product.setCatalogVersion(version);
		modelService.save(product);

		return product;
	}

	private List<ProductReferenceModel> createProductReferences(final List<ProductModel> products, final int numOfRefs)
	{
		final List<ProductReferenceModel> refs = new ArrayList<>(numOfRefs);
		for (int i = 0; i < products.size(); i++)
		{
			final ProductModel sourceProduct = products.get(i);

			for (int j = 0; j < numOfRefs; j++)
			{
				final ProductModel targetProduct = products.get((i + j + 1) % products.size());

				refs.add(createProductReference("Ref" + i, sourceProduct, targetProduct));
			}
		}

		return refs;
	}

	private ProductReferenceModel createProductReference(final String qualifier, final ProductModel source,
			final ProductModel target)
	{
		final ProductReferenceModel ref = modelService.create(ProductReferenceModel.class);
		ref.setQualifier(qualifier);
		ref.setSource(source);
		ref.setTarget(target);
		ref.setQuantity(Integer.valueOf(1));
		ref.setActive(Boolean.FALSE);
		ref.setPreselected(Boolean.FALSE);

		modelService.save(ref);

		return ref;
	}

	private SyncConfig prepareDefaultSyncConfig()
	{
		final SyncConfig config = new SyncConfig();
		config.setCreateSavedValues(Boolean.FALSE);
		config.setLogToDatabase(Boolean.FALSE);
		config.setLogToFile(Boolean.TRUE);
		config.setLogLevelDatabase(JobLogLevel.WARNING);
		config.setLogLevelFile(JobLogLevel.INFO);
		config.setForceUpdate(Boolean.FALSE);
		config.setErrorMode(ErrorMode.IGNORE);

		config.setSynchronous(Boolean.FALSE);
		config.setFullSync(Boolean.TRUE);

		return config;
	}

	public void awaitThenSynchronizeAndCountDown(final List<ProductModel> sourceProductsPart, final SyncItemJobModel syncItemJob,
			final SyncConfig syncConfig, final AtomicReference<SyncResult> result, final CountDownLatch startSignal,
			final CountDownLatch testFinish, final long millisecondsOffSet)
	{
		try
		{
			startSignal.await();
			TimeUnit.MILLISECONDS.sleep(millisecondsOffSet);
		}
		catch (final InterruptedException ignored)
		{
		}
		result.set(catalogSynchronizationService.performSynchronization(Lists.newArrayList(sourceProductsPart), syncItemJob,
				syncConfig));
		testFinish.countDown();
	}

	protected SyncConfig createSyncConfig(final boolean synchronous)
	{
		final SyncConfig syncConfig = new SyncConfig();
		syncConfig.setCreateSavedValues(Boolean.TRUE);
		syncConfig.setForceUpdate(Boolean.TRUE);
		syncConfig.setLogLevelDatabase(JobLogLevel.WARNING);
		syncConfig.setLogLevelFile(JobLogLevel.WARNING);
		syncConfig.setLogToFile(Boolean.TRUE);
		syncConfig.setLogToDatabase(Boolean.FALSE);
		syncConfig.setSynchronous(synchronous);
		return syncConfig;
	}

	@Test
	public void testCreateAndRetrieveSnycJobs()
	{

		// given
		final ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)), source);
		modelService.save(product);
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);

		//when
		final SyncItemJobModel retrievetSyncItemJob = syncService.getSyncJob(source, target, null);

		//then
		assertThat(syncItemJob).isEqualTo(retrievetSyncItemJob);
	}

	@Test
	public void testCreateAndRetrieveSnycJobsWithCode()
	{

		// given
		final ProductModel product = createProductInCatalogVersion(
				String.format("%s%s", "test_product", RandomStringUtils.randomAlphanumeric(3)), source);
		modelService.save(product);
		final SyncItemJobModel syncItemJob = createSyncJob(source, target);

		//when
		final SyncItemJobModel retrievetSyncItemJob = syncService.getSyncJob(source, target, syncItemJob.getCode());

		//then
		assertThat(syncItemJob).isEqualTo(retrievetSyncItemJob);
	}

	private void assertNumberOfProductAndProductReferencesInCatalogVersion(final CatalogVersionModel catalogVersion,
			final int numberOfProducts, final int numberOfProductReferences)
	{
		final List<ProductModel> products = productService.getAllProductsForCatalogVersion(catalogVersion);

		final List<ProductReferenceModel> referenceModelList = products.stream()
				.flatMap(productModel -> productModel.getProductReferences().stream()).collect(Collectors.toList());

		assertThat(products).hasSize(numberOfProducts);
		assertThat(referenceModelList).hasSize(numberOfProductReferences);
	}

	private SyncConfig defaultSyncConfigForFullSynchronization()
	{
		final SyncConfig defaultSyncConfig = new SyncConfig();
		defaultSyncConfig.setForceUpdate(Boolean.FALSE);
		defaultSyncConfig.setCreateSavedValues(Boolean.FALSE);
		defaultSyncConfig.setLogToDatabase(Boolean.FALSE);
		defaultSyncConfig.setLogToFile(Boolean.TRUE);
		defaultSyncConfig.setLogLevelDatabase(JobLogLevel.WARNING);
		defaultSyncConfig.setLogLevelFile(JobLogLevel.INFO);
		defaultSyncConfig.setErrorMode(ErrorMode.IGNORE);
		defaultSyncConfig.setAbortWhenCollidingSyncIsRunning(false);
		return defaultSyncConfig;
	}

	private List<SyncSchedule> originalSyncMediaFromFirstSchedule(final SyncResult syncResult)
	{
		final CatalogVersionSyncScheduleMediaModel firstScheduleMedia = ((CatalogVersionSyncCronJobModel) syncResult.getCronJob())
				.getScheduleMedias().get(0);
		final InputStream mediaAsStream = MediaManager.getInstance().getMediaAsStream(firstScheduleMedia.getFolder().getQualifier(),
				firstScheduleMedia.getLocation());

		return syncSchedulesFromMedia(mediaAsStream);
	}

	private List<SyncSchedule> syncSchedulesFromMedia(final InputStream mediaAsStream)
	{
		final List<SyncSchedule> syncSchedules = Lists.newArrayList();
		SyncScheduleReader syncScheduleReader = null;
		try
		{
			syncScheduleReader = new SyncScheduleReader(new InputStreamReader(mediaAsStream, "UTF-8"), 0);
			while (syncScheduleReader.readNextLine())
			{
				syncSchedules.add(syncScheduleReader.getScheduleFromLine());
			}
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new IllegalStateException("cant handle UTF-8 encoding !? ", e);
		}
		finally
		{
			if (syncScheduleReader != null)
			{
				syncScheduleReader.closeQuietly();
			}
		}

		return syncSchedules;
	}

}
