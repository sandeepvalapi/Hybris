/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.test;

import static org.assertj.core.api.Assertions.fail;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@UnitTest
public class ReservedTypecodesInternalTest
{
	private static final Logger LOG = LoggerFactory.getLogger(ReservedTypecodesInternalTest.class);
	private final Set<Integer> releasedTypecodes = new HashSet<>();

	@Before
	public void setUp()
	{
		releasedTypecodes.add(Integer.valueOf(10001));
		releasedTypecodes.add(Integer.valueOf(10002));
		releasedTypecodes.add(Integer.valueOf(10004));
		releasedTypecodes.add(Integer.valueOf(10005));
		releasedTypecodes.add(Integer.valueOf(10007));
		releasedTypecodes.add(Integer.valueOf(10008));
		releasedTypecodes.add(Integer.valueOf(10009));
		releasedTypecodes.add(Integer.valueOf(10010));
		releasedTypecodes.add(Integer.valueOf(10012));
		releasedTypecodes.add(Integer.valueOf(10013));
		releasedTypecodes.add(Integer.valueOf(10017));
		releasedTypecodes.add(Integer.valueOf(10018));
		releasedTypecodes.add(Integer.valueOf(10021));
		releasedTypecodes.add(Integer.valueOf(10022));
		releasedTypecodes.add(Integer.valueOf(10023));
		releasedTypecodes.add(Integer.valueOf(10025));
		releasedTypecodes.add(Integer.valueOf(10027));
		releasedTypecodes.add(Integer.valueOf(10028));
		releasedTypecodes.add(Integer.valueOf(10030));
		releasedTypecodes.add(Integer.valueOf(10031));
		releasedTypecodes.add(Integer.valueOf(10032));
		releasedTypecodes.add(Integer.valueOf(10033));
		releasedTypecodes.add(Integer.valueOf(10034));
		releasedTypecodes.add(Integer.valueOf(10035));
		releasedTypecodes.add(Integer.valueOf(10036));
		releasedTypecodes.add(Integer.valueOf(10037));
		releasedTypecodes.add(Integer.valueOf(10038));
		releasedTypecodes.add(Integer.valueOf(10039));
		releasedTypecodes.add(Integer.valueOf(10040));
		releasedTypecodes.add(Integer.valueOf(10041));
		releasedTypecodes.add(Integer.valueOf(10042));
		releasedTypecodes.add(Integer.valueOf(10055));
		releasedTypecodes.add(Integer.valueOf(10205));
		releasedTypecodes.add(Integer.valueOf(11100));
		releasedTypecodes.add(Integer.valueOf(11101));
		releasedTypecodes.add(Integer.valueOf(11102));
		releasedTypecodes.add(Integer.valueOf(11103));
		releasedTypecodes.add(Integer.valueOf(13101));
		releasedTypecodes.add(Integer.valueOf(13102));
		releasedTypecodes.add(Integer.valueOf(13113));
		releasedTypecodes.add(Integer.valueOf(13211));
		releasedTypecodes.add(Integer.valueOf(13212));
		releasedTypecodes.add(Integer.valueOf(13213));
		releasedTypecodes.add(Integer.valueOf(13214));
		releasedTypecodes.add(Integer.valueOf(13215));
		releasedTypecodes.add(Integer.valueOf(17001));
		releasedTypecodes.add(Integer.valueOf(17002));
		releasedTypecodes.add(Integer.valueOf(20340));
		releasedTypecodes.add(Integer.valueOf(23401));
		releasedTypecodes.add(Integer.valueOf(23402));
		releasedTypecodes.add(Integer.valueOf(23403));
		releasedTypecodes.add(Integer.valueOf(23404));
		releasedTypecodes.add(Integer.valueOf(23405));
		releasedTypecodes.add(Integer.valueOf(23406));
		releasedTypecodes.add(Integer.valueOf(23407));
		releasedTypecodes.add(Integer.valueOf(23409));
		releasedTypecodes.add(Integer.valueOf(23410));
		releasedTypecodes.add(Integer.valueOf(23411));
		releasedTypecodes.add(Integer.valueOf(23412));
		releasedTypecodes.add(Integer.valueOf(23413));
		releasedTypecodes.add(Integer.valueOf(23414));
		releasedTypecodes.add(Integer.valueOf(23420));
		releasedTypecodes.add(Integer.valueOf(23421));
		releasedTypecodes.add(Integer.valueOf(23423));
		releasedTypecodes.add(Integer.valueOf(23425));
		releasedTypecodes.add(Integer.valueOf(23426));
		releasedTypecodes.add(Integer.valueOf(23427));
		releasedTypecodes.add(Integer.valueOf(23428));
		releasedTypecodes.add(Integer.valueOf(23429));
		releasedTypecodes.add(Integer.valueOf(23430));
		releasedTypecodes.add(Integer.valueOf(23431));
		releasedTypecodes.add(Integer.valueOf(23432));
		releasedTypecodes.add(Integer.valueOf(23451));
		releasedTypecodes.add(Integer.valueOf(23452));
		releasedTypecodes.add(Integer.valueOf(23460));
		releasedTypecodes.add(Integer.valueOf(23461));
		releasedTypecodes.add(Integer.valueOf(23462));
		releasedTypecodes.add(Integer.valueOf(23463));
		releasedTypecodes.add(Integer.valueOf(23464));
		releasedTypecodes.add(Integer.valueOf(23465));
		releasedTypecodes.add(Integer.valueOf(23466));
		releasedTypecodes.add(Integer.valueOf(23467));
		releasedTypecodes.add(Integer.valueOf(23904));
		releasedTypecodes.add(Integer.valueOf(24242));
		releasedTypecodes.add(Integer.valueOf(24401));
		releasedTypecodes.add(Integer.valueOf(24402));
		releasedTypecodes.add(Integer.valueOf(24403));
		releasedTypecodes.add(Integer.valueOf(24404));
		releasedTypecodes.add(Integer.valueOf(24441));
		releasedTypecodes.add(Integer.valueOf(24500));
		releasedTypecodes.add(Integer.valueOf(24600));
		releasedTypecodes.add(Integer.valueOf(24601));
		releasedTypecodes.add(Integer.valueOf(24602));
		releasedTypecodes.add(Integer.valueOf(24603));
		releasedTypecodes.add(Integer.valueOf(24604));
		releasedTypecodes.add(Integer.valueOf(24605));
		releasedTypecodes.add(Integer.valueOf(24606));
		releasedTypecodes.add(Integer.valueOf(24607));
		releasedTypecodes.add(Integer.valueOf(24608));
		releasedTypecodes.add(Integer.valueOf(24609));
		releasedTypecodes.add(Integer.valueOf(24610));
		releasedTypecodes.add(Integer.valueOf(24611));
		releasedTypecodes.add(Integer.valueOf(24612));
		releasedTypecodes.add(Integer.valueOf(24613));
		releasedTypecodes.add(Integer.valueOf(26000));
		releasedTypecodes.add(Integer.valueOf(26001));
		releasedTypecodes.add(Integer.valueOf(26002));
		releasedTypecodes.add(Integer.valueOf(26003));
		releasedTypecodes.add(Integer.valueOf(26004));
		releasedTypecodes.add(Integer.valueOf(26005));
		releasedTypecodes.add(Integer.valueOf(26006));
		releasedTypecodes.add(Integer.valueOf(27000));
		releasedTypecodes.add(Integer.valueOf(30001));
		releasedTypecodes.add(Integer.valueOf(30002));
		releasedTypecodes.add(Integer.valueOf(30003));
		releasedTypecodes.add(Integer.valueOf(30004));
		releasedTypecodes.add(Integer.valueOf(30005));
		releasedTypecodes.add(Integer.valueOf(30006));
		releasedTypecodes.add(Integer.valueOf(30007));
		releasedTypecodes.add(Integer.valueOf(30008));
		releasedTypecodes.add(Integer.valueOf(30009));
		releasedTypecodes.add(Integer.valueOf(30010));
		releasedTypecodes.add(Integer.valueOf(30011));
		releasedTypecodes.add(Integer.valueOf(30012));
		releasedTypecodes.add(Integer.valueOf(30013));
		releasedTypecodes.add(Integer.valueOf(30014));
		releasedTypecodes.add(Integer.valueOf(30015));
		releasedTypecodes.add(Integer.valueOf(30016));
		releasedTypecodes.add(Integer.valueOf(30017));
		releasedTypecodes.add(Integer.valueOf(30018));
		releasedTypecodes.add(Integer.valueOf(30019));
		releasedTypecodes.add(Integer.valueOf(30020));
		releasedTypecodes.add(Integer.valueOf(30021));
		releasedTypecodes.add(Integer.valueOf(30022));
		releasedTypecodes.add(Integer.valueOf(30023));
		releasedTypecodes.add(Integer.valueOf(30024));
		releasedTypecodes.add(Integer.valueOf(30025));
		releasedTypecodes.add(Integer.valueOf(30026));
		releasedTypecodes.add(Integer.valueOf(30027));
		releasedTypecodes.add(Integer.valueOf(30028));
		releasedTypecodes.add(Integer.valueOf(30029));
		releasedTypecodes.add(Integer.valueOf(30030));
		releasedTypecodes.add(Integer.valueOf(30031));
		releasedTypecodes.add(Integer.valueOf(30032));
		releasedTypecodes.add(Integer.valueOf(30033));
		releasedTypecodes.add(Integer.valueOf(30034));
		releasedTypecodes.add(Integer.valueOf(30035));
		releasedTypecodes.add(Integer.valueOf(30036));
		releasedTypecodes.add(Integer.valueOf(30037));
		releasedTypecodes.add(Integer.valueOf(30038));
		releasedTypecodes.add(Integer.valueOf(30039));
		releasedTypecodes.add(Integer.valueOf(30040));
		releasedTypecodes.add(Integer.valueOf(30041));
		releasedTypecodes.add(Integer.valueOf(30042));
		releasedTypecodes.add(Integer.valueOf(30043));
		releasedTypecodes.add(Integer.valueOf(30044));
		releasedTypecodes.add(Integer.valueOf(30045));
		releasedTypecodes.add(Integer.valueOf(32000));
		releasedTypecodes.add(Integer.valueOf(32001));
		releasedTypecodes.add(Integer.valueOf(32003));
		releasedTypecodes.add(Integer.valueOf(32004));
		releasedTypecodes.add(Integer.valueOf(32005));
		releasedTypecodes.add(Integer.valueOf(32006));
		releasedTypecodes.add(Integer.valueOf(32007));
		releasedTypecodes.add(Integer.valueOf(32008));
		releasedTypecodes.add(Integer.valueOf(32009));
		releasedTypecodes.add(Integer.valueOf(32010));
		releasedTypecodes.add(Integer.valueOf(32011));
		releasedTypecodes.add(Integer.valueOf(32012));
		releasedTypecodes.add(Integer.valueOf(32013));
		releasedTypecodes.add(Integer.valueOf(32015));
		releasedTypecodes.add(Integer.valueOf(32016));
		releasedTypecodes.add(Integer.valueOf(32017));
		releasedTypecodes.add(Integer.valueOf(32018));
		releasedTypecodes.add(Integer.valueOf(32019));
		releasedTypecodes.add(Integer.valueOf(32020));
		releasedTypecodes.add(Integer.valueOf(32021));
		releasedTypecodes.add(Integer.valueOf(32022));
		releasedTypecodes.add(Integer.valueOf(32023));
		releasedTypecodes.add(Integer.valueOf(32024));
		releasedTypecodes.add(Integer.valueOf(32025));
		releasedTypecodes.add(Integer.valueOf(32026));
		releasedTypecodes.add(Integer.valueOf(32027));
		releasedTypecodes.add(Integer.valueOf(32028));
		releasedTypecodes.add(Integer.valueOf(32236));
		releasedTypecodes.add(Integer.valueOf(32237));
		releasedTypecodes.add(Integer.valueOf(32764));
		releasedTypecodes.add(Integer.valueOf(32766));
		releasedTypecodes.add(Integer.valueOf(32767));
	}

	@Test
	public void shouldNotContainNotExcludedTypecodesGreaterThan10000() throws IOException
	{
		if (!Boolean.getBoolean("running.on.hybris.ci"))
		{
			LOG.info("ReservedTypecodesInternalTest is disabled");
			//yes we want to break the log analyzer on our CI, we will remove it when it's adopted by the teams
			LOG.error("This error is here to get your attention. Please add '-Drunning.on.hybris.ci=true' to the command line.");
			return;
		}
		getReservedTypecodes().forEach(this::assureThatTheTypeCodeIsNotGreaterThan1000OrHasBeenAlreadyRelesed);
	}

	void assureThatTheTypeCodeIsNotGreaterThan1000OrHasBeenAlreadyRelesed(final Object typeCode, final Object typeName)
	{
		if (!(typeCode instanceof String && typeName instanceof String && NumberUtils.isCreatable((String) typeCode)))
		{
			return;
		}

		final int typeCodeNumber = NumberUtils.createInteger((String) typeCode).intValue();

		if (typeCodeNumber <= 10000)
		{
			return;
		}
		if (!releasedTypecodes.contains(Integer.valueOf(typeCodeNumber)))
		{
			fail(typeCodeNumber + " (" + typeName + ") violates the rule of internal typecodes being less than 10000.");
		}
	}

	private Properties getReservedTypecodes() throws IOException
	{
		try (InputStream is = ReservedTypecodesInternalTest.class.getResourceAsStream("/core/unittest/reservedTypecodes.txt"))
		{
			final Properties result = new Properties();
			result.load(is);

			return result;
		}
	}

}
