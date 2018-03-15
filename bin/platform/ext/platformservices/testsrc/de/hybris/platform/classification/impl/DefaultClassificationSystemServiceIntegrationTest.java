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
package de.hybris.platform.classification.impl;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link DefaultClassificationSystemService}
 */

@IntegrationTest
@DemoTest
public class DefaultClassificationSystemServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private ClassificationSystemService classificationSystemService;

	private ClassificationSystemModel sys = null;
	private ClassificationSystemVersionModel sysVer = null;

	@Before
	public void setUp()
	{
		sys = modelService.create(ClassificationSystemModel.class);
		sys.setId("TestClassificationSystem");
		sysVer = modelService.create(ClassificationSystemVersionModel.class);
		sysVer.setVersion("1.0");
		sysVer.setCatalog(sys);

		modelService.saveAll();
	}

	/**
	 * Check that the ClassificationSystem and ClassificationSystemVersion exist
	 */
	@Test
	public void testSetup()
	{
		assertNotNull(sysVer);
		assertEquals(sys, sysVer.getCatalog());
	}

	/**
	 * Checks that an {@link IllegalArgumentException} is thrown whenever a null is used as an ID
	 */
	@Test
	public void testGetClassificationSystemWithIllegalArgumentException()
	{
		try
		{
			classificationSystemService.getSystemForId(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}

	}

	/**
	 * Checks that an {@link UnknownIdentifierException} is thrown whenever a non existing ClassificationSystem ID is
	 * used
	 */
	@Test
	public void testGetClassificationSystemForIdWithUnknownIdentifierException()
	{
		try
		{
			classificationSystemService.getSystemForId("NonExistingClassificationSystem");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}
	}

	/**
	 * Checks that {@link DefaultClassificationSystemService} retrieves the correct ClassificationSystem for an existing
	 * ClassificationSystem ID
	 */
	@Test
	public void testGetClassificationSystemForIdWithExistingId()
	{
		assertEquals(sys, classificationSystemService.getSystemForId("TestClassificationSystem"));
	}

	/**
	 * Checks that an {@link IllegalArgumentException} is thrown whenever a null is used as an ID or as a system version
	 */
	@Test
	public void testGetClassificationSystemVersionWithIlegalArgumentException()
	{
		try
		{
			classificationSystemService.getSystemVersion("TestClassificationSystem", null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}
		try
		{
			classificationSystemService.getSystemVersion(null, "1.0");
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//ok, IllegalArgumentException expected
		}

	}

	/**
	 * Checks that an {@link UnknownIdentifierException} is thrown whenever a non existing ClassificationSystem ID is
	 * used or a non existing system version
	 */
	@Test
	public void testGetClassificationSystemVersionWithUnknownIdentifierException()
	{

		try
		{
			classificationSystemService.getSystemVersion("TestClassificationSystem", "1.1");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}

		try
		{
			classificationSystemService.getSystemVersion("NonExistingClassificationSystem", "1.0");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//ok, UnknownIdentifierException expected
		}
	}

	/**
	 * Checks that {@link DefaultClassificationSystemService} retrieves the correct ClassificationSystemVersion for an
	 * existing ClassificationSystem ID and a corresponding version
	 */
	@Test
	public void testGetClassificationSystemVersionWithExistingClassificationsSystemVersion()
	{
		assertEquals(sysVer, classificationSystemService.getSystemVersion("TestClassificationSystem", "1.0"));
	}


}
