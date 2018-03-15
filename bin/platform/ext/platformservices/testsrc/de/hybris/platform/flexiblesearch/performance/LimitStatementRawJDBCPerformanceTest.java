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

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Stopwatch;


@ManualTest
//"Should be tested better with truncate, see also PLA-12569
public class LimitStatementRawJDBCPerformanceTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(LimitStatementRawJDBCPerformanceTest.class);
	private static final String ORACLE_SPECIFIC_QUERY = "SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (SELECT code FROM junit_titles ORDER BY pk) row_ WHERE rownum <= ?) WHERE rownum_ > ?";
	private static final String MSSQL_SPECIFIC_QUERY = "WITH query AS (select ROW_NUMBER() OVER (order by  item_t0.pk) as __hybris_limit_query__,  item_t0.pk  from junit_titles item_t0) SELECT pk FROM query WHERE __hybris_limit_query__ > ? AND __hybris_limit_query__ <= ?";
	private static final String MYSQL_SPECIFIC_QUERY = "SELECT * FROM junit_titles ORDER BY pk LIMIT ?,?";
	private static final String POSTGRESQL_SPECIFIC_QUERY = "SELECT * FROM junit_titles ORDER BY pk OFFSET ? LIMIT ?";

	@Resource
	private ImportService importService;
	private JdbcTemplate jdbcTemplate;
	private Stopwatch stopWatch;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		stopWatch = Stopwatch.createUnstarted();
		jdbcTemplate = new JdbcTemplate(Registry.getCurrentTenantNoFallback().getDataSource());
		createTestObjects(10000);
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

	@Test
	public void limitQueryTestForMSSqlOracleAndMySqlAndPostgreSql()
	{
		final String query = getQuery();

		if (query != null)
		{
			stopWatch.start();
			jdbcTemplate.queryForList(query, Integer.valueOf(9900), Integer.valueOf(100));
			stopWatch.stop();
			writeResultToLogger(stopWatch.toString(), 9900, 100);
		}
	}

	private String getQuery()
	{
		String query = null;
		if (Config.isOracleUsed())
		{
			query = ORACLE_SPECIFIC_QUERY;
		}
		else if (Config.isSQLServerUsed())
		{
			query = MSSQL_SPECIFIC_QUERY;
		}
		else if (Config.isMySQLUsed())
		{
			query = MYSQL_SPECIFIC_QUERY;
		}
		else if (Config.isPostgreSQLUsed())
		{
			query = POSTGRESQL_SPECIFIC_QUERY;
		}
		return query;
	}

	private void writeResultToLogger(final String formattedTime, final int start, final int count)
	{
		final String line = "#######################";
		final String endLine = "\n";
		final StringBuilder stringBuilder = new StringBuilder(endLine);
		stringBuilder.append(line).append(Config.getDatabase()).append(line).append(endLine);
		stringBuilder.append("Resulting time for - ").append(start).append(", ");
		stringBuilder.append(count).append(" :").append(formattedTime).append(endLine);

		LOG.info(stringBuilder);
	}
}
