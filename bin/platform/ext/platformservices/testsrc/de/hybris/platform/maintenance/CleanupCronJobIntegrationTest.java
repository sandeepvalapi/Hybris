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
package de.hybris.platform.maintenance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.test.TestItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jobs.GenericMaintenanceJobPerformable;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.jobs.maintenance.impl.CleanupCronJobStrategy;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.CSVReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.CharUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class CleanupCronJobIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private SessionService sessionService;

	private GenericMaintenanceJobPerformable gmjp;
	private CleanupCronJobStrategy cuCJmi;

	@Before
	public void setup()
	{
		final ItemObjectResolver modelResolver = Registry.getApplicationContext()
				.getBean("modelResolver", ItemObjectResolver.class);

		//the performable with all the needed services
		gmjp = new GenericMaintenanceJobPerformable();
		gmjp.setModelService(modelService);
		gmjp.setFlexibleSearchService(flexibleSearchService);
		gmjp.setSessionService(sessionService);
		gmjp.setModelResolver(modelResolver);

		//the testsubject with the default values
		cuCJmi = new CleanupCronJobStrategy();
		cuCJmi.setModelService(modelService);
		cuCJmi.setTypeService(typeService);
		cuCJmi.setResult(Collections.singleton(CronJobResult.SUCCESS));
		cuCJmi.setStatus(Collections.singleton(CronJobStatus.FINISHED));
		cuCJmi.setExcludedCronJobCodes(Collections.EMPTY_SET);

		gmjp.setMaintenanceCleanupStrategy(cuCJmi);
		assertEquals("MaintenanceCleanupJob", gmjp.getType());

		final MaintenanceCleanupJobModel slayerJob = modelService.create(MaintenanceCleanupJobModel.class);
		slayerJob.setCode("cleanupCronJobsPerformable");
		slayerJob.setSpringId("cleanupCronJobsPerformable");


		modelService.save(slayerJob);
	}

	@Test
	public void testCleanupCronjob_Timeframe() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		createCronJobs("too_young", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 1, 1);
		createCronJobs("young", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 7, 13);
		createCronJobs("very_old", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 113, 100);
		assertEquals(currentExistingCJs + 121, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));


		final MaintenanceCleanupJobModel job = new MaintenanceCleanupJobModel();

		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(job);

		PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 8, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		job.setThreshold(Integer.valueOf(3));

		result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 1, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));
	}

	@Test
	public void testCleanupCronjob_StatusAndResult() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(null, null);

		gmjp.setPageSize(2); //just using a smaller page size for fun

		createCronJobs("success_finished", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 1, 50);
		createCronJobs("success_aborted", CronJobResult.SUCCESS, CronJobStatus.ABORTED, 2, 50);
		createCronJobs("error_finished", CronJobResult.ERROR, CronJobStatus.FINISHED, 4, 50);
		createCronJobs("failure_aborted", CronJobResult.FAILURE, CronJobStatus.ABORTED, 8, 50);
		createCronJobs("failure_unknow", CronJobResult.FAILURE, CronJobStatus.UNKNOWN, 16, 50);

		assertEquals(currentExistingCJs + 31, countExistingCronJobs(null, null));
		assertEquals(currentExistingCJs + 3, countExistingCronJobs(CronJobResult.SUCCESS, null));
		assertEquals(currentExistingCJs + 10, countExistingCronJobs(null, CronJobStatus.ABORTED));
		assertEquals(currentExistingCJs + 16, countExistingCronJobs(CronJobResult.FAILURE, CronJobStatus.UNKNOWN));

		final Set<CronJobResult> results = new HashSet<CronJobResult>();
		results.add(CronJobResult.FAILURE);
		results.add(CronJobResult.ERROR);
		cuCJmi.setResult(results);

		final Set<CronJobStatus> statuses = new HashSet<CronJobStatus>();
		statuses.add(CronJobStatus.FINISHED);
		statuses.add(CronJobStatus.ABORTED);
		statuses.add(CronJobStatus.UNKNOWN);
		cuCJmi.setStatus(statuses);


		final PerformResult result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 3, countExistingCronJobs(null, null));
	}

	@Test
	public void testCleanupCronjob_ExclusionList() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(null, null);

		createCronJobs("do_not_delete", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 3, 50);
		createCronJobs("to_delete", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 8, 50);

		assertEquals(currentExistingCJs + 11, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		final Set<String> excluded = new HashSet<String>();
		excluded.add("do_not_delete_0");
		excluded.add("do_not_delete_2");
		cuCJmi.setExcludedCronJobCodes(excluded);

		final PerformResult result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 2, countExistingCronJobs(null, null));

	}

	@Test
	public void testAbortOnErrorWithDummyList()
	{
		assertEquals("expect NO testItems in the db!", Integer.valueOf(0), countTestItems());
		assertFalse(checkForTestItemWithField("dummyitem_1"));
		for (int i = 0; i < 8; i++)
		{
			final TestItemModel testitem = modelService.create(TestItemModel.class);
			testitem.setTestDumpProperty("dummyitem_" + i);
		}
		modelService.saveAll();
		assertTrue(checkForTestItemWithField("dummyitem_1"));
		assertEquals(Integer.valueOf(8), countTestItems());

		final RemoveFirstSevenTestItemStrategy throwStrategy = new RemoveFirstSevenTestItemStrategy();
		gmjp.setMaintenanceCleanupStrategy(throwStrategy);
		gmjp.setAbortOnError(true);
		gmjp.setPageSize(3);

		try
		{
			TestUtils.disableFileAnalyzer("Expecting the DummyException here", 100);

			final PerformResult result = gmjp.perform(new CronJobModel());
			assertEquals(CronJobResult.ERROR, result.getResult());
			assertEquals(CronJobStatus.ABORTED, result.getStatus());


			assertEquals(Integer.valueOf(7), countTestItems());

			assertFalse(checkForTestItemWithField("dummyitem_0"));
			assertTrue(checkForTestItemWithField("dummyitem_1"));
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testNotAbortOnErrorWithDummyList()
	{
		assertEquals("expect NO testItems in the db!", Integer.valueOf(0), countTestItems());
		assertFalse(checkForTestItemWithField("dummyitem_1"));
		for (int i = 0; i < 8; i++)
		{
			final TestItemModel testitem = modelService.create(TestItemModel.class);
			testitem.setTestDumpProperty("dummyitem_" + i);
		}
		modelService.saveAll();
		assertTrue(checkForTestItemWithField("dummyitem_1"));
		assertEquals(Integer.valueOf(8), countTestItems());

		final RemoveFirstSevenTestItemStrategy throwStrategy = new RemoveFirstSevenTestItemStrategy();
		gmjp.setMaintenanceCleanupStrategy(throwStrategy);
		gmjp.setAbortOnError(false);
		gmjp.setPageSize(3);

		try
		{
			TestUtils.disableFileAnalyzer("Expecting the DummyException here", 100);
			final PerformResult result = gmjp.perform(new CronJobModel());
			assertEquals(CronJobResult.FAILURE, result.getResult());
			assertEquals(CronJobStatus.FINISHED, result.getStatus());

			assertEquals(Integer.valueOf(5), countTestItems());

			assertFalse(checkForTestItemWithField("dummyitem_0"));
			assertTrue(checkForTestItemWithField("dummyitem_1"));
			assertFalse(checkForTestItemWithField("dummyitem_3"));
			assertTrue(checkForTestItemWithField("dummyitem_4"));
			assertFalse(checkForTestItemWithField("dummyitem_6"));
			assertTrue(checkForTestItemWithField("dummyitem_7"));
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testSetIllegalValues()
	{
		try
		{
			cuCJmi.setResult(null);
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("The CronJob result set must contains at least one value!", e.getMessage());
		}

		try
		{
			cuCJmi.setResult(new HashSet<CronJobResult>());
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("The CronJob result set must contains at least one value!", e.getMessage());
		}

		try
		{
			cuCJmi.setStatus(null);
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("The CronJob status set must contains at least one value!", e.getMessage());
		}

		try
		{
			cuCJmi.setStatus(new HashSet<CronJobStatus>());
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("The CronJob status set must contains at least one value!", e.getMessage());
		}

		try
		{
			gmjp.setPageSize(-10);
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("pagesize cannot be negative", e.getMessage());
		}

	}

	@Test
	public void testOverrideBeanValuesByJobThreeshold() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		createCronJobs("too_young", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 1, 1);
		createCronJobs("old", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 7, 13);
		createCronJobs("very_old", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 113, 100);
		assertEquals(currentExistingCJs + 121, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setThreshold(Integer.valueOf(4));
		cjm.setJob(the_job);

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 1, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));
	}

	@Test
	public void testOverrideBeanValuesByJobThreesholdWithNullValue() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		createCronJobs("young", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 13);
		createCronJobs("old", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 15);
		assertEquals(currentExistingCJs + 10, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setThreshold(null); //bean has value set to 14, see setup() method
		cjm.setJob(the_job);

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 5, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));
	}

	@Test
	public void testOverrideBeanValuesByJobThreesholdWithNegativeValue() throws ImpExException
	{
		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setThreshold(Integer.valueOf(-10)); //the performable will complain
		//the beanvalue is correct, but after platform is started up the job can overwrite it
		//TODO: set constraints for the job?!?

		cjm.setJob(the_job);
		gmjp.setAbortOnError(true);

		try
		{
			gmjp.perform(cjm);
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Cannot set negative value for daysold", e.getMessage());
		}
	}

	@Test
	public void testOverrideBeanValuesByJobCronjobType() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		createCronJobs("cleanupcronjobs", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 15, "CleanupCronJob");
		createCronJobs("normaljobs", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 15);
		assertEquals(currentExistingCJs + 10, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		//10 jobs older than 14 days, now removing only CleanupCronJob

		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setSearchType(typeService.getComposedTypeForCode("CleanupCronJob"));
		cjm.setJob(the_job);

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs + 5, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

	}

	@Test
	public void testOverrideBeanValuesByJobCronjobTypeNullValue() throws ImpExException
	{
		final int currentExistingCJs = countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		createCronJobs("cleanupcronjobs", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 15, "CleanupCronJob");
		createCronJobs("normaljobs", CronJobResult.SUCCESS, CronJobStatus.FINISHED, 5, 15);
		assertEquals(currentExistingCJs + 10, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));

		//10 jobs older than 14 days, now removing only CleanupCronJob

		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setSearchType(null); //the default value which is the normal CronJobModel will kick in
		cjm.setJob(the_job);

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(currentExistingCJs, countExistingCronJobs(CronJobResult.SUCCESS, CronJobStatus.FINISHED));
	}

	@Test
	public void testOverrideBeanValuesByJobCronjobTypeWithNotAssignableComposedType() throws ImpExException
	{
		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel the_job = new MaintenanceCleanupJobModel();

		the_job.setSearchType(typeService.getComposedTypeForCode(ProductModel._TYPECODE));
		cjm.setJob(the_job);

		try
		{
			gmjp.perform(cjm);
			fail("should fail with IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals(ProductModel._TYPECODE + " must be a subtype of " + CronJobModel._TYPECODE, e.getMessage());
		}
	}

	private void createCronJobs(final String codeprefix, final CronJobResult cjr, final CronJobStatus cjs, final int count,
			final int daysold) throws ImpExException
	{
		createCronJobs(codeprefix, cjr, cjs, count, daysold, "CronJob");
	}

	private void createCronJobs(final String codeprefix, final CronJobResult cjr, final CronJobStatus cjs, final int count,
			final int daysold, final String cronjobtype) throws ImpExException
	{
		final StringBuffer cronjobbuffer = new StringBuffer(1000);
		cronjobbuffer.append("insert_update " + cronjobtype + ";code[unique=true];job(code);status(code)[forceWrite=true];");
		cronjobbuffer.append("result(code)[forceWrite=true];startTime[forceWrite=true,dateformat=dd-MM-yyyy];");
		cronjobbuffer.append("endTime[forceWrite=true,dateformat=dd-MM-yyyy] ").append(CharUtils.LF);

		for (int index = 0; index < count; index++)
		{
			final DateTime start = new DateTime().minusDays(daysold).minusHours(1);
			final DateTime end = new DateTime().minusDays(daysold);
			cronjobbuffer.append(";" + codeprefix + "_" + index + ";cleanupCronJobsPerformable;" + cjs.getCode() + ";");
			cronjobbuffer.append(cjr.getCode() + ";" + start.getDayOfMonth() + "-" + start.getMonthOfYear() + "-" + start.getYear());
			cronjobbuffer.append(";" + end.getDayOfMonth() + "-" + end.getMonthOfYear() + "-" + end.getYear()).append(CharUtils.LF);
		}
		ImpExManager.getInstance().importDataLight(new CSVReader(cronjobbuffer.toString()), true);
	}

	private int countExistingCronJobs(final CronJobResult cjr, final CronJobStatus cjs)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("result", cjr);
		params.put("status", cjs);
		final StringBuffer query = new StringBuffer("select {pk} from {cronjob} ");
		query.append(cjr != null || cjs != null ? "where " : "");
		query.append(cjs == null ? " " : "{status}=?status ");
		query.append(cjr != null && cjs != null ? "and " : "");
		query.append(cjr == null ? " " : "{result}=?result ");

		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(query.toString(), params);
		return flexibleSearchService.search(fsq).getTotalCount();
	}

	private Integer countTestItems()
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("select count({pk}) from {testitem}");
		fsq.setResultClassList(Arrays.asList(Integer.class));
		return (Integer) flexibleSearchService.search(fsq).getResult().get(0);
	}

	private boolean checkForTestItemWithField(final String value)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {pk} from {testitem} where {testDumpProperty} = ?value",
				Collections.singletonMap("value", value));
		fsq.setResultClassList(Arrays.asList(TestItem.class));
		return flexibleSearchService.search(fsq).getTotalCount() > 0;
	}

	private class RemoveFirstSevenTestItemStrategy implements MaintenanceCleanupStrategy<TestItemModel, CronJobModel>
	{

		@Override
		public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
		{
			return new FlexibleSearchQuery("select {pk} from {testitem} order by {testDumpProperty} asc");
		}

		@Override
		public void process(final List<TestItemModel> elements)
		{
			for (int i = 0; i < elements.size(); i++)
			{
				if (i < 1)
				{
					modelService.remove(elements.get(i));
				}
				else
				{
					throw new DummyException("got " + elements.get(i).getTestDumpProperty()
							+ " - throwing now the expected DummyException");
				}
			}
		}

	}

	private class DummyException extends RuntimeException
	{
		public DummyException(final String reason)
		{
			super(reason);
		}
	}
}
