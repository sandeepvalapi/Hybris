/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.storefront.web.mvc;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;

import org.apache.commons.lang3.StringUtils;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.WebUtils;


@UnitTest
public class AcceleratorUrlPathHelperTest
{
	private static final String TEST_VALUE = "testValue";

	private MockHttpServletRequest request;

	@Before
	public void setUp()
	{
		request = new MockHttpServletRequest();
	}

	@Test
	public void testGetContextPath()
	{
		final AcceleratorUrlPathHelper pathHelper = new AcceleratorUrlPathHelper();
		request.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, TEST_VALUE);
		request.setAttribute(WebUtils.INCLUDE_CONTEXT_PATH_ATTRIBUTE, TEST_VALUE);
		final String result = pathHelper.getContextPath(request);
		Assertions.assertThat(result.equals(StringUtils.EMPTY)).isTrue();
	}

	@Test
	public void testGetPathWithinServletMapping()
	{
		final AcceleratorUrlPathHelper pathHelper = new AcceleratorUrlPathHelper();
		request.setAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE, StringUtils.EMPTY);
		final String result = pathHelper.getPathWithinServletMapping(request);
		Assertions.assertThat(result.equals("/")).isTrue();
	}
}
