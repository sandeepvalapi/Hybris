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
package de.hybris.platform.cronjob.jalo;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.CompositeJobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PLA_12481_Test extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	private CompositeCronJobModel compCronJob;

	private Boolean legacyMode = null;

	@Before
	public void prepareComposite()
	{
		final CompositeJobModel job = modelService.create(CompositeJobModel.class);
		job.setCode("CompJob");
		compCronJob = createCompositeCronJob(job, "CompCJ");

		legacyMode = StringUtils.isEmpty(Config.getParameter("relation.handle.legacy")) ? null : Boolean.valueOf(Config
				.getParameter("relation.handle.legacy"));
		Config.setParameter("relation.handle.legacy", Boolean.FALSE.toString());
	}

	@After
	public void unprepareComposite()
	{
		if (legacyMode != null)
		{
			Config.setParameter("relation.handle.legacy", legacyMode.toString());
		}
	}

	@Test
	public void testSetForeignKeyMultipleTimes()
	{
		final CompositeCronJob cj = modelService.getSource(compCronJob);
		final CompositeEntryModel entryModel = createEntry(compCronJob, "foo");
		final CompositeEntry entry = modelService.getSource(entryModel);

		Assert.assertEquals(cj, entry.getCompositeCronJob());
		Assert.assertEquals(Collections.singletonList(entry), cj.getCompositeEntries());
		Assert.assertEquals(Integer.valueOf(0), entry.getProperty(CompositeEntry.COMPOSITECRONJOBPOS));

		for (int i = 0; i < 10; i++)
		{
			entry.setCompositeCronJob(cj);
			Assert.assertEquals(cj, entry.getCompositeCronJob());
			Assert.assertEquals(Integer.valueOf(0), entry.getProperty(CompositeEntry.COMPOSITECRONJOBPOS));
		}
	}

	private CompositeCronJobModel createCompositeCronJob(final CompositeJobModel job, final String code)
	{
		final CompositeCronJobModel localCompositeCronJob = modelService.create(CompositeCronJobModel.class);
		localCompositeCronJob.setCode(code);
		localCompositeCronJob.setJob(job);

		modelService.save(localCompositeCronJob);

		return localCompositeCronJob;
	}

	private CompositeEntryModel createEntry(final CompositeCronJobModel cronJob, final String code)
	{
		final CompositeEntryModel entry = modelService.create(CompositeEntryModel.class);
		entry.setCode(code);
		entry.setCompositeCronJob(cronJob);

		modelService.save(entry);

		return entry;
	}
}
