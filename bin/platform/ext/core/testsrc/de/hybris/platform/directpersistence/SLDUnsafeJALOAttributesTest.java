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
package de.hybris.platform.directpersistence;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;


@IntegrationTest
public class SLDUnsafeJALOAttributesTest extends AbstractSLDUnsafeJALOAttributesTest
{
	@Override
	protected List<String> getExtensions()
	{
		return Arrays.asList("advancedsavedquery", "catalog", "comments", "commons", "core", "deliveryzone", "europe1", "hac",
				"impex", "maintenanceweb", "mediaweb", "paymentstandard", "platformservices", "processing", "scripting", "testweb",
				"validation", "workflow");
	}

	@Test
	public void shouldReportMediaContainerAsSafe()
	{
		boolean ignoreMarked = false;
		
		assertTrue("MediaContainer should be SLD safe for read",
				unsafeTypesProvider.isSLDSafe(MediaContainerModel._TYPECODE, true, ignoreMarked));
		assertTrue("MediaContainer should be SLD safe for write",
				unsafeTypesProvider.isSLDSafe(MediaContainerModel._TYPECODE, false, ignoreMarked));
		assertTrue("MediaContainer should be SLD safe for read&write",
				unsafeTypesProvider.isSLDSafe(MediaContainerModel._TYPECODE, ignoreMarked));
	}

}
