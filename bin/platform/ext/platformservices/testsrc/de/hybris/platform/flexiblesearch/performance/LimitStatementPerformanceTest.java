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
package de.hybris.platform.flexiblesearch.performance;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilderFactory;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;
import com.google.common.base.Stopwatch;


/**
 * Intention of this test is to run queries (with disabled cache) with LIMIT against 100, 1000 and 10000 items through
 * default LIMIT implementation and through DB specific LIMIT implementation and compare them. Test will run on current
 * DB. All results are printed to the console.
 */
@PerformanceTest
public class LimitStatementPerformanceTest extends ServicelayerTransactionalTest
{
	@Resource
	private ImportService importService;
	private Stopwatch stopWatch;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		stopWatch = Stopwatch.createUnstarted();
	}

	@Test
	public void comparisionTestFor10000Items()
	{
		createTestObjects(10000);
		disableCache();
		try
		{
			final String[][] data = new String[3][];
			data[0] = execPerfTestForRange("Start: 0, Count: 100", 0, 100);
			data[1] = execPerfTestForRange("Start: 5000, Count: 100", 5000, 100);
			data[2] = execPerfTestForRange("Start: 9900, Count: 100", 9900, 100);

			writeResultTable(data);
		}
		finally
		{
			enableCache();
		}
	}

	@Test
	public void comparisionTestFor1000Items()
	{
		createTestObjects(1000);
		disableCache();
		try
		{
			final String[][] data = new String[3][];
			data[0] = execPerfTestForRange("Start: 0, Count: 100", 0, 100);
			data[1] = execPerfTestForRange("Start: 500, Count: 100", 500, 100);
			data[2] = execPerfTestForRange("Start: 900, Count: 100", 900, 100);

			writeResultTable(data);
		}
		finally
		{
			enableCache();
		}
	}

	@Test
	public void comparisionTestFor100Items()
	{
		createTestObjects(100);
		disableCache();
		try
		{
			final String[][] data = new String[3][];
			data[0] = execPerfTestForRange("Start: 0, Count: 10", 0, 10);
			data[1] = execPerfTestForRange("Start: 50, Count: 10", 50, 10);
			data[2] = execPerfTestForRange("Start: 90, Count: 10", 90, 10);

			writeResultTable(data);
		}
		finally
		{
			enableCache();
		}
	}

	private void writeResultTable(final String[][] data)
	{
		final String[] header =
		{ "", "With " + Config.getDatabase() + " Limit support", "With fallback Limit support" };
		ASCIITable.getInstance().printTable(header, data);
	}

	private String[] execPerfTestForRange(final String label, final int start, final int count)
	{
		final String result1 = execDbSupportPerfQueryForRange(start, count);
		final String result2 = execFallbackPerfQueryForRange(start, count);

		final String[] result =
		{ label, result1, result2 };

		return result;
	}

	private String execDbSupportPerfQueryForRange(final int start, final int count)
	{
		stopWatch.start();
		executeQueryForRange(start, count);
		stopWatch.stop();
		final String result = stopWatch.toString();
		stopWatch.reset();

		return result;
	}

	private String execFallbackPerfQueryForRange(final int start, final int count)
	{
		disableDbLimitSupport();
		stopWatch.start();
		executeQueryForRange(start, count);
		stopWatch.stop();
		final String result = stopWatch.toString();
		stopWatch.reset();
		enableDbLimitSupport();

		return result;
	}

	private void disableDbLimitSupport()
	{
		Config.setParameter(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, Boolean.TRUE.toString());
	}

	@SuppressWarnings("deprecation")
	private void executeQueryForRange(final int start, final int count)
	{
		final FlexibleSearch flexSearch = JaloSession.getCurrentSession().getFlexibleSearch();
		final List<Title> res = flexSearch.search("SELECT {PK} FROM {Title} ORDER BY {PK}", Collections.EMPTY_MAP,
				Collections.singletonList(Title.class), false, true, start, count).getResult();
		for (final Title title : res)
		{
			title.getCode();
		}
	}

	private void createTestObjects(final int count)
	{
		final StringBuilder builder = new StringBuilder("INSERT Title;code;name\n");

		for (int i = 0; i <= count; i++)
		{
			builder.append(";").append(RandomStringUtils.randomAlphabetic(3) + i);
			builder.append(";\n");
		}

		final ImpExResource mediaRes = new StreamBasedImpExResource(new ByteArrayInputStream(builder.toString().getBytes()),
				CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = new ImportConfig();
		config.setScript(mediaRes);
		importService.importData(config);
	}

	private void enableDbLimitSupport()
	{
		Config.setParameter(LimitStatementBuilderFactory.DISABLE_SPECIFIC_DB_LIMIT_SUPPORT, Boolean.FALSE.toString());
	}


	private void disableCache()
	{
		Registry.getCurrentTenant().getCache().setEnabled(false);
	}

	private void enableCache()
	{
		Registry.getCurrentTenant().getCache().setEnabled(true);
	}
}
