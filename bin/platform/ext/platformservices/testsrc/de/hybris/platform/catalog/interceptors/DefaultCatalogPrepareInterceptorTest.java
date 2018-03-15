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
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCatalogPrepareInterceptorTest
{
	private CatalogPrepareInterceptor interceptor;

	@Mock
	private InterceptorContext mockInterceptorContext;
	@Mock
	private ModelService mockModelService;
	@Mock
	private CatalogService mockCatalogService;

	private CatalogModel catalog;
	private CatalogVersionModel catalogVersionActive;
	private CatalogVersionModel catalogVersion;

	private Object catalogSource;

	private Object catalogVersionSource;

	private Object catalogVersionActiveSource;


	@Before
	public void setUp() throws Exception
	{
		interceptor = new CatalogPrepareInterceptor();
		MockitoAnnotations.initMocks(this);

		interceptor.setCatalogService(mockCatalogService);

		catalogVersion = new CatalogVersionModel();
		catalogVersionActive = new CatalogVersionModel();
		catalog = new CatalogModel();

		catalogSource = new Object();
		catalogVersionSource = new Object();
		catalogVersionActiveSource = new Object();

		when(mockInterceptorContext.getModelService()).thenReturn(mockModelService);
		when(mockModelService.getSource(catalog)).thenReturn(catalogSource);
		when(mockModelService.getSource(catalogVersion)).thenReturn(catalogVersionSource);
		when(mockModelService.getSource(catalogVersionActive)).thenReturn(catalogVersionActiveSource);
	}


	@Test(expected = InterceptorException.class)
	public void testOnPrepareWrongCatalog() throws InterceptorException
	{
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.ACTIVECATALOGVERSION))).thenReturn(
				Boolean.TRUE);

		catalogVersion.setCatalog(new CatalogModel());
		catalog.setActiveCatalogVersion(catalogVersionActive);
		interceptor.onPrepare(catalog, mockInterceptorContext);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.interceptors.CatalogPrepareInterceptor#onPrepare(java.lang.Object, de.hybris.platform.servicelayer.interceptor.InterceptorContext)}
	 * .
	 * 
	 * @throws InterceptorException
	 */
	@Test
	public void testOnPrepareActiveCatalogVersion() throws InterceptorException
	{
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.ACTIVECATALOGVERSION))).thenReturn(
				Boolean.TRUE);
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.DEFAULTCATALOG))).thenReturn(Boolean.FALSE);
		catalogVersionActive.setCatalog(catalog);
		catalogVersionActive.setActive(Boolean.TRUE);

		catalogVersion.setCatalog(catalog);
		catalogVersion.setActive(Boolean.FALSE);

		final Set<CatalogVersionModel> cvs = new HashSet();
		cvs.add(catalogVersion);
		cvs.add(catalogVersionActive);
		catalog.setCatalogVersions(cvs);

		//setting version to active
		catalog.setActiveCatalogVersion(catalogVersion);
		//calling prepare interceptor
		interceptor.onPrepare(catalog, mockInterceptorContext);


		Assert.assertTrue(catalogVersion.getActive().booleanValue());
		Assert.assertFalse(catalogVersionActive.getActive().booleanValue());

	}

	@Test
	public void testOnPrepareDefaultCatalogFlag() throws InterceptorException
	{
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.ACTIVECATALOGVERSION))).thenReturn(
				Boolean.FALSE);
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.DEFAULTCATALOG))).thenReturn(Boolean.TRUE);
		final CatalogModel defaultCatalogModel = new CatalogModel();
		final Object defaultCatalogSource = new Object();
		defaultCatalogModel.setDefaultCatalog(Boolean.TRUE);

		when(mockCatalogService.getDefaultCatalog()).thenReturn(defaultCatalogModel);
		when(mockModelService.getSource(defaultCatalogModel)).thenReturn(defaultCatalogSource);

		//setting new default
		catalog.setDefaultCatalog(Boolean.TRUE);
		interceptor.onPrepare(catalog, mockInterceptorContext);

		Assert.assertTrue("Catalog should be default", catalog.getDefaultCatalog().booleanValue());
		Assert.assertFalse("Catalog should not be default", defaultCatalogModel.getDefaultCatalog().booleanValue());
	}

	@Test
	public void testOnPrepareNullCatalogVersions() throws InterceptorException
	{
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.ACTIVECATALOGVERSION))).thenReturn(
				Boolean.TRUE);
		when(Boolean.valueOf(mockInterceptorContext.isModified(catalog, CatalogModel.DEFAULTCATALOG))).thenReturn(Boolean.TRUE);
		final CatalogModel catalogModel = new CatalogModel();
		try
		{
			interceptor.onPrepare(catalogModel, mockInterceptorContext);
		}
		catch (final Exception e)
		{
			Assert.fail("Noe exception expected , but cought :" + e.getMessage());
		}

	}

}
