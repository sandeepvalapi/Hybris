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
package de.hybris.platform.healthcheck.check;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.healthcheck.HealthCheck;
import de.hybris.platform.healthcheck.HealthCheckStatus;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


@IntegrationTest
public class DatabaseHealthCheckTest extends ServicelayerBaseTest
{
	@Resource
	private HealthCheck databaseHealthCheck;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Test
	public void shouldExecuteCrudSuccessfully() throws Exception
	{
		// when
		final Map<MultiKey, Object> result = databaseHealthCheck.perform();

		// then
		assertThat(result).isNotNull();
		assertThat(result.get(new MultiKey("database check", "status"))).isEqualTo(HealthCheckStatus.OK);
	}

	@Test
	public void shouldFailOnExecutingCreateTableOperation() throws Exception
	{
		createHealthCheckTable();

		final Map<MultiKey, Object> result = databaseHealthCheck.perform();
		assertThat(result).isNotNull();
		assertThat(result.get(new MultiKey("database check", "status"))).isEqualTo(HealthCheckStatus.ERROR);

	}

	private void createHealthCheckTable()
	{
		try
		{
			jdbcTemplate.execute("CREATE TABLE healthchecktest (ID INT, NAME VARCHAR(5))");
		}
		catch (final DataAccessException exc)
		{
			fail("Table 'healthchecktest' for building the precondition for this test could not be created. " + exc.getMessage());
		}
	}
}
