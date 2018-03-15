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
package de.hybris.platform.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.util.Utilities;

import java.util.Properties;

import org.junit.Test;


@UnitTest
public class ConfigUtilTest
{

	private static final String SOME_CONTENT = "/some/content";
	private static final String SOME_MORE_CONTENT = "/some/more/content";
	private static final String UNRESOLVED_PROPERTY = "unresolved.property";
	private static final String TEST_PROPERTY = "test.property";
	private static final String TEST_PROPERTY_1 = "test.property.1";
	private static final String TEST_PROPERTY_2 = "test.property.2";
	private static final String TEST_PROPERTY_3 = "test.property.3";
	private static final String TEST_PROPERTY_4 = "test.property.4";

	/**
	 * Tests if recursive way of expanding properties works. See PLA-10609 for details.<br>
	 * Adds four test properties and checks if they were expanded properly:
	 * <ul>
	 * <li>test.property.1=test.property</li>
	 * <li>test.property.2=${test.property.1}/some/content</li>
	 * <li>test.property.3=${test.property.2}/some/more/content</li>
	 * <li>test.property.4=${test.property.3} ${test.property.1} ${unresolved.property}</li>
	 * </ul>
	 */
	@Test
	public void testExpandProperties()
	{
		final PlatformConfig config = Utilities.getPlatformConfig();
		final Properties tempProps = new Properties();
		tempProps.put(TEST_PROPERTY_1, TEST_PROPERTY);
		tempProps.put(TEST_PROPERTY_2, "${" + TEST_PROPERTY_1 + "}" + SOME_CONTENT);
		tempProps.put(TEST_PROPERTY_3, "${" + TEST_PROPERTY_2 + "}" + SOME_MORE_CONTENT);
		tempProps.put(TEST_PROPERTY_4, "${" + TEST_PROPERTY_3 + "}" + " ${" + TEST_PROPERTY_1 + "}" + " ${" + UNRESOLVED_PROPERTY
				+ "}");
		ConfigUtil.loadRuntimeProperties(tempProps, config);
		assertNotNull(TEST_PROPERTY_2 + " is not set", tempProps.get(TEST_PROPERTY_2));
		assertTrue(TEST_PROPERTY_2 + " was " + tempProps.getProperty(TEST_PROPERTY_2) + " when expected value was "
				+ (TEST_PROPERTY + SOME_CONTENT), (TEST_PROPERTY + SOME_CONTENT).equals(tempProps.getProperty(TEST_PROPERTY_2)));
		assertNotNull(TEST_PROPERTY_3 + " is not set", tempProps.get(TEST_PROPERTY_3));
		assertTrue(TEST_PROPERTY_3 + " was " + tempProps.getProperty(TEST_PROPERTY_3) + " when expected value was "
				+ (TEST_PROPERTY + SOME_CONTENT + SOME_MORE_CONTENT),
				(TEST_PROPERTY + SOME_CONTENT + SOME_MORE_CONTENT).equals(tempProps.getProperty(TEST_PROPERTY_3)));
		assertNotNull(TEST_PROPERTY_4 + " is not set", tempProps.get(TEST_PROPERTY_4));
		assertTrue(TEST_PROPERTY_4
				+ " was "
				+ tempProps.getProperty(TEST_PROPERTY_4)
				+ " when expected value was "
				+ (TEST_PROPERTY + SOME_CONTENT + SOME_MORE_CONTENT + " " + tempProps.getProperty(TEST_PROPERTY_1) + " ${"
						+ UNRESOLVED_PROPERTY + "}"),
				(TEST_PROPERTY + SOME_CONTENT + SOME_MORE_CONTENT + " " + tempProps.getProperty(TEST_PROPERTY_1) + " ${"
						+ UNRESOLVED_PROPERTY + "}").equals(tempProps.getProperty(TEST_PROPERTY_4)));
	}
}
