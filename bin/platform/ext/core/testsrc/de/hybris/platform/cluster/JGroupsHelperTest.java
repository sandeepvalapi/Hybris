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
package de.hybris.platform.cluster;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cluster.jgroups.JGroupsHelper;

import org.junit.Test;

@UnitTest
public class JGroupsHelperTest
{
	@Test
	public void testDefaultUdpConfig()
	{
		final boolean isJGroupsConfiguredForTCP = JGroupsHelper.isTCPJGroupsConfig("jgroups/jgroups-udp.xml");

		assertThat(isJGroupsConfiguredForTCP).isFalse();
	}

	@Test
	public void testDefaultTCPConfig()
	{
		final boolean isJGroupsConfiguredForTCP = JGroupsHelper.isTCPJGroupsConfig("jgroups/jgroups-tcp.xml");

		assertThat(isJGroupsConfiguredForTCP).isTrue();
	}

	@Test
	public void testTcpMpingConfig()
	{
		final boolean isJGroupsConfiguredForTCP = JGroupsHelper.isTCPJGroupsConfig("jgroups/jgroups-tcp-mping.xml");

		assertThat(isJGroupsConfiguredForTCP).isTrue();
	}
}
