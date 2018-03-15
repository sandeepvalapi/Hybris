/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.impex.jalo.imp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class MultiThreadedImpExImportReaderIntegrationTest extends ServicelayerBaseTest
{
	private final List<ThreadPool> poolsToClose = new LinkedList<>();
	@Resource
	private ModelService modelService;
	@Resource
	private CronJobService cronJobService;
	@Resource
	private TypeService typeService;

	private final PropertyConfigSwitcher retriesSwitcher = new PropertyConfigSwitcher(
			"impex.number.of.retries.to.acquire.initial.threads");

	@Before
	public void setUp()
	{
		retriesSwitcher.switchToValue("0");
	}

	@After
	public void tearDown()
	{
		retriesSwitcher.switchBackToDefault();
		poolsToClose.forEach(ThreadPool::close);
		poolsToClose.clear();
	}

	@Test
	public void shouldNotUseAnyThreadAfterConstruction() throws ImpExException
	{
		final ThreadPool pool = givenThreadPool(1);
		final MultiThreadedImpExImportReader reader = givenMultiThreadedImpExImportReader(pool, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");

		assertThat(reader.tryToBorrowThread()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNull();
	}

	@Test
	public void shouldFailWhenThereIsNotEnoughThreadsInThePool() throws ImpExException
	{
		final ThreadPool pool = givenThreadPool(2);
		final MultiThreadedImpExImportReader reader = givenMultiThreadedImpExImportReader(pool, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");
		reader.setMaxThreads(10);

		assertThatExceptionOfType(SystemException.class).isThrownBy(reader::readLine).withNoCause()
				.withMessage("Couldn't allocate required threads to perform an import.");

		assertThat(reader.tryToBorrowThread()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNull();
	}

	@Test
	public void shouldUseThreeThreadsForMultithreadedImportWithMaxThreadsSetToOne() throws ImpExException
	{
		final ThreadPool pool = givenThreadPool(3);
		final TestableMultiThreadedImpExImportReader reader = givenMultiThreadedImpExImportReader(pool, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");
		reader.setReaderDelayMs(5000);
		reader.setMaxThreads(1);

		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNull();
		assertThat(reader.readLine()).isNull();
	}

	@Test
	public void shouldStartImportWithThreeThreadsForMultithreadedImport() throws ImpExException
	{
		final ThreadPool pool = givenThreadPool(3);
		final TestableMultiThreadedImpExImportReader reader = givenMultiThreadedImpExImportReader(pool, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");
		reader.setReaderDelayMs(5000);
		reader.setMaxThreads(10);

		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNull();
		assertThat(reader.readLine()).isNull();
	}

	@Test
	public void shouldUseAvailableThreadsThreadsForMultithreadedImport() throws ImpExException
	{
		final ThreadPool pool = givenThreadPool(13);
		final TestableMultiThreadedImpExImportReader reader = givenMultiThreadedImpExImportReader(pool, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");
		reader.setReaderDelayMs(5000);
		reader.setMaxThreads(10);

		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.readLine()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNotNull();
		assertThat(reader.tryToBorrowThread()).isNull();
		assertThat(reader.readLine()).isNull();
	}

	@Test
	public void shouldFailCronJobWhenThereIsLessThreadsThanRequired()
			throws IOException, JaloGenericCreationException, JaloAbstractTypeException
	{
		final ThreadPool threadPool = givenThreadPool(2);
		final ImpExImportCronJobModel cronJob = givenImpExImportCronJobModel(threadPool, 100, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");

		cronJobService.performCronJob(cronJob, true);

		modelService.refresh(cronJob);
		assertThat(cronJob.getStatus()).isEqualTo(CronJobStatus.FINISHED);
		assertThat(cronJob.getResult()).isEqualTo(CronJobResult.FAILURE);
	}

	@Test
	public void shouldNotFailCronJobWhenThereAreAtLeastThreeThreads()
			throws IOException, JaloGenericCreationException, JaloAbstractTypeException
	{
		final ThreadPool threadPool = givenThreadPool(3);
		final ImpExImportCronJobModel cronJob = givenImpExImportCronJobModel(threadPool, 100, //
				"INSERT_UPDATE Language;isocode[unique=true];active[default=false];", //
				";iso1;", //
				";iso2;");

		cronJobService.performCronJob(cronJob, true);

		modelService.refresh(cronJob);
		assertThat(cronJob.getStatus()).isEqualTo(CronJobStatus.FINISHED);
		assertThat(cronJob.getResult()).isEqualTo(CronJobResult.SUCCESS);
	}

	private ImpExImportCronJobModel givenImpExImportCronJobModel(final ThreadPool threadPool, final int numberOfThreads,
			final String... lines)
	{
		final ImpExImportJobModel job = createConfiguredJob(threadPool);
		final String impexScript = Stream.of(lines).collect(Collectors.joining("\n"));
		try (ByteArrayInputStream stream = new ByteArrayInputStream(impexScript.getBytes()))
		{
			final StreamBasedImpExResource resource = new StreamBasedImpExResource(stream, "UTF-8");
			final ImpExImportCronJobModel cronJob = modelService.create(ImpExImportCronJobModel.class);
			cronJob.setJobMedia(resource.getMedia());
			cronJob.setJob(job);
			cronJob.setMaxThreads(numberOfThreads);
			modelService.save(cronJob);
			return cronJob;
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	private ImpExImportJobModel createConfiguredJob(final ThreadPool threadPool)
	{
		final ComposedTypeModel impExImportJobType = typeService.getComposedTypeForClass(ImpExImportJobModel.class);
		final ComposedTypeModel testImpExImportJobType = modelService.create(ComposedTypeModel.class);

		testImpExImportJobType.setSuperType(impExImportJobType);
		testImpExImportJobType.setCode(TestImpExImportJob.class.getName());
		testImpExImportJobType.setCatalogItemType(Boolean.FALSE);
		testImpExImportJobType.setGenerate(Boolean.FALSE);
		testImpExImportJobType.setSingleton(Boolean.FALSE);
		testImpExImportJobType.setJaloclass(TestImpExImportJob.class);

		modelService.save(testImpExImportJobType);

		TestImpExImportJob jaloJob;
		try
		{
			jaloJob = (TestImpExImportJob) JaloSession.getCurrentSession().getTypeManager().getComposedType(TestImpExImportJob.class)
					.newInstance(ImmutableMap.of(ImpExImportJob.CODE, "TEST_" + UUID.randomUUID()));
		}
		catch (JaloGenericCreationException | JaloAbstractTypeException | JaloItemNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		jaloJob.setThreadPool(threadPool);

		return modelService.get(jaloJob);
	}

	private ThreadPool givenThreadPool(final int numberOfThreads)
	{
		final ThreadPool pool = new ThreadPool(null, numberOfThreads);

		final GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = numberOfThreads;
		config.maxIdle = 0;
		config.maxWait = -1;
		config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		config.minEvictableIdleTimeMillis = 0;
		config.timeBetweenEvictionRunsMillis = 0; // keep idle threads for at most 30 sec
		pool.setConfig(config);

		poolsToClose.add(pool);

		return pool;
	}

	private TestableMultiThreadedImpExImportReader givenMultiThreadedImpExImportReader(final ThreadPool threadPool,
			final String... lines)
	{
		final String impexScript = Stream.of(lines).collect(Collectors.joining("\n"));
		return new TestableMultiThreadedImpExImportReader(threadPool, impexScript);
	}

	public static class TestImpExImportJob extends ImpExImportJob
	{
		private ThreadPool threadPool;

		public void setThreadPool(final ThreadPool threadPool)
		{
			this.threadPool = threadPool;
		}

		@Override
		protected TestableMultiThreadedImpExImportReader createImportReader(final ImpExImportCronJob cronJob,
				final CSVReader csvReader, final ImportProcessor processor)
		{
			final String mediaPrefix = cronJob.getCode() + "-";
			final int threads = getMaxThreads4Run(cronJob);
			final boolean legacyMode = LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.IMPEX,
					cronJob.isLegacyMode());

			assertThat(threads).isGreaterThan(1);

			final TestableMultiThreadedImpExImportReader reader;
			if (processor == null)
			{
				reader = new TestableMultiThreadedImpExImportReader(threadPool, csvReader, legacyMode, mediaPrefix);
			}
			else
			{
				reader = new TestableMultiThreadedImpExImportReader(threadPool, csvReader, null,
						(MultiThreadedImportProcessor) processor, legacyMode, mediaPrefix);
			}
			return reader;
		}
	}

	static class TestableMultiThreadedImpExImportReader extends MultiThreadedImpExImportReader
	{
		private final ThreadPool threadPool;
		private long readerDelayMs = 0;
		private final String prefixForMediaLookup;

		public TestableMultiThreadedImpExImportReader(final ThreadPool threadPool, final String lines)
		{
			super(lines);
			this.threadPool = threadPool;
			this.prefixForMediaLookup = null;
		}

		public TestableMultiThreadedImpExImportReader(final ThreadPool threadPool, final CSVReader reader, final boolean legacyMode,
				final String prefixForMediaLookup)
		{
			super(reader, legacyMode);
			this.threadPool = threadPool;
			this.prefixForMediaLookup = prefixForMediaLookup;
		}

		public TestableMultiThreadedImpExImportReader(final ThreadPool threadPool, final CSVReader reader,
				final CSVWriter dumpWriter, final MultiThreadedImportProcessor processor, final boolean legacyMode,
				final String prefixForMediaLookup)
		{
			super(reader, dumpWriter, processor, legacyMode);
			this.threadPool = threadPool;
			this.prefixForMediaLookup = prefixForMediaLookup;
		}

		@Override
		public ImpExMedia findExternalDataMedia(final String code) throws JaloBusinessException
		{
			ImpExMedia ret = super.findExternalDataMedia(code);
			if (ret == null)
			{
				ret = super.findExternalDataMedia(prefixForMediaLookup + code);
			}

			return ret;
		}

		public void setReaderDelayMs(final long readerDelayMs)
		{
			this.readerDelayMs = readerDelayMs;
		}

		@Override
		protected boolean readLineFromWorker() throws ImpExException
		{
			try
			{
				Thread.sleep(readerDelayMs);
			}
			catch (final InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			return super.readLineFromWorker();
		}

		@Override
		protected PoolableThread tryToBorrowThread(final ThreadPool threadPool)
		{
			return super.tryToBorrowThread(this.threadPool);
		}
	}
}
