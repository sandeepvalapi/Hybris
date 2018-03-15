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
package de.hybris.platform.catalog.interceptors;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCatalogURLPatternsValidatorTest
{

	private CatalogURLPatternsValidator interceptor;
	@Mock
	private InterceptorContext mockInterceptorContext;

	private CatalogModel catalog = null;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		interceptor = new CatalogURLPatternsValidator();

		MockitoAnnotations.initMocks(this);

		catalog = new CatalogModel();

		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.URLPATTERNS))).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testSetValidURLPatterns()
	{
		final Collection<String> patternsToSet = Arrays.asList("^[hc]a", "[:digit:]", "^http://testhost$");
		catalog.setUrlPatterns(patternsToSet);
		try
		{
			interceptor.onValidate(catalog, mockInterceptorContext);
		}
		catch (final Exception e)
		{
			Assert.fail("Unexpected exception " + e.getMessage());
		}
	}

	@Test
	public void testSetInValidURLPatterns()
	{
		try
		{
			final Collection<String> patternsToSet = Arrays.asList("^[hca", ":digit:]");
			catalog.setUrlPatterns(patternsToSet);
			interceptor.onValidate(catalog, mockInterceptorContext);
			Assert.fail("expected InterceptorException");
		}
		catch (final InterceptorException e)
		{
			Assert.assertTrue(e.getInterceptor() instanceof CatalogURLPatternsValidator);
		}
		catch (final Exception e)
		{
			Assert.fail("Unexpected exception " + e.getMessage());
		}

	}


}
