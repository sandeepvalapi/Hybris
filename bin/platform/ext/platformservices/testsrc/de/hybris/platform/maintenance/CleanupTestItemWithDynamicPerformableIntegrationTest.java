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
import de.hybris.platform.core.model.test.TestItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.jobs.DynamicMaintenanceJobPerformable;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.internal.model.DynamicMaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CleanupTestItemWithDynamicPerformableIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	ModelService modelService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	DynamicMaintenanceJobPerformable dmjobPerformable;
	DynamicMaintenanceCleanupJobModel the_default_job;

	@Before
	public void setup()
	{
		final ItemObjectResolver modelResolver = Registry.getApplicationContext()
				.getBean("modelResolver", ItemObjectResolver.class);

		dmjobPerformable = new DynamicMaintenanceJobPerformable();
		dmjobPerformable.setAbortOnError(true);
		dmjobPerformable.setFlexibleSearchService(flexibleSearchService);
		dmjobPerformable.setModelResolver(modelResolver);
		dmjobPerformable.setModelService(modelService);
		dmjobPerformable.setApplicationContext(Registry.getApplicationContext());

		the_default_job = new DynamicMaintenanceCleanupJobModel();
		the_default_job.setSpringId("cleanupDynamicallyPerformable");
		the_default_job.setCode("cleanupDynamicallyPerformable");
	}

	@Test
	public void testChangeATestItem()
	{
		createTestItems(1);
		assertTrue(checkForTestItemWithField("dummyitem_00"));

		the_default_job.setProcessScript(getCleanupCode());
		the_default_job.setSearchScript(getFetchQueryCode());

		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		dmjobPerformable.perform(cjm);
		assertTrue(checkForTestItemWithField("changed_00"));
	}

	@Test
	public void testDefaultCleanupCode()
	{
		createTestItems(1);
		assertTrue(checkForTestItemWithField("dummyitem_00"));

		the_default_job.setSearchScript(getFetchQueryCode());

		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		dmjobPerformable.perform(cjm);
		assertFalse(checkForTestItemWithField("dummyitem_00"));
	}

	@Test
	public void testNoFetchBeanshellcodeCode()
	{
		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		try
		{
			dmjobPerformable.perform(cjm);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("no beanshell search code to execute. This is mandatory", e.getMessage());
		}
	}

	@Test
	public void testInvalidFetchBeanshellcodeCode()
	{
		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		the_default_job.setSearchScript("blub");
		try
		{
			dmjobPerformable.perform(cjm);
			fail("IllegalStateException expected");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("The beanshell search code did not return a FlexibleSearchQuery object", e.getMessage());
		}
	}

	@Test
	public void testEmptyFetchBeanshellcodeCode()
	{
		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		the_default_job.setSearchScript("");
		try
		{
			dmjobPerformable.perform(cjm);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("no beanshell search code to execute. This is mandatory", e.getMessage());
		}
	}

	@Test
	public void testEvalErrorInFetchBeanshellcodeCode()
	{
		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		the_default_job.setSearchScript(";-;");
		try
		{
			dmjobPerformable.perform(cjm);
			fail("IllegalStateException expected");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Could not eval beanshell search code. Does it return a FlexibleSearchQuery object?", e.getMessage());
		}
	}

	@Test
	public void testInvalidCleanupBeanshellcodeCode()
	{
		createTestItems(1);

		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(the_default_job);

		the_default_job.setSearchScript(getFetchQueryCode());
		the_default_job.setProcessScript("blubbblubb");

		dmjobPerformable.perform(cjm); //nothing will happen here
		assertTrue(checkForTestItemWithField("dummyitem_00"));
	}

	@Test
	public void testDynamicPerformableWithFalseJob()
	{
		final ServicelayerJobModel wrongJob = modelService.create(ServicelayerJobModel.class);
		wrongJob.setCode("wrongJob");
		wrongJob.setSpringId("cleanupDynamicallyPerformable");

		final CronJobModel cjm = new CronJobModel();
		cjm.setJob(wrongJob);

		try
		{
			dmjobPerformable.perform(cjm);
			fail("IllegalStateException expected");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("The job must be a DynamicMaintenanceCleanupJobModel. Cannot execute the beanshell search", e.getMessage());
		}
	}


	private void createTestItems(final int count)
	{
		for (int i = 0; i < count; i++)
		{
			final TestItemModel testitem = modelService.create(TestItemModel.class);
			testitem.setTestDumpProperty("dummyitem_" + (i < 10 ? "0" : "") + i);
		}
		modelService.saveAll();
	}

	private String getFetchQueryCode()
	{
		final StringBuffer buffer = new StringBuffer(100);
		buffer.append("return new FlexibleSearchQuery(\"select {pk} from {testitem} order by {testDumpProperty} asc\");");

		return buffer.toString();
	}

	private String getCleanupCode()
	{
		final StringBuffer buffer = new StringBuffer(200);
		buffer.append("for (int i=0; i < elements.size(); i++) {");
		buffer.append(" ((de.hybris.platform.core.model.test.TestItemModel)elements.get(i)).setTestDumpProperty(\"changed_\"+ (i < 10 ? \"0\" : \"\")+i);}");
		buffer.append(" modelService.saveAll();");
		return buffer.toString();
	}

	private boolean checkForTestItemWithField(final String value)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {pk} from {testitem} where {testDumpProperty} = ?value",
				Collections.singletonMap("value", value));
		fsq.setResultClassList(Arrays.asList(TestItem.class));
		return flexibleSearchService.search(fsq).getTotalCount() > 0;
	}

}
