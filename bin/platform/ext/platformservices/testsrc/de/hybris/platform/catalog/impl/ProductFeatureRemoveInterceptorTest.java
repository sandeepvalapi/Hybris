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
package de.hybris.platform.catalog.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Tests {@link ProductFeatureRemoveInterceptor}
 */
@UnitTest
public class ProductFeatureRemoveInterceptorTest
{
	private ProductFeatureRemoveInterceptor removeInterceptor;
	private ProductFeatureModel productFeature;

	@Mock
	private InterceptorContext mockInterceptorContext;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		removeInterceptor = new ProductFeatureRemoveInterceptor();
		productFeature = new ProductFeatureModel();
		final ProductModel product = new ProductModel();
		product.setModifiedtime(new Date());
		productFeature.setProduct(product);
	}

	@Test
	public void testOnRemoveTouchProduct() throws Exception
	{
		final Date oldProductModifiedTime = productFeature.getProduct().getModifiedtime();
		Thread.currentThread().sleep(1000L);

		removeInterceptor.onRemove(productFeature, mockInterceptorContext);

		assertThat(productFeature.getProduct().getModifiedtime().after(oldProductModifiedTime)).isTrue();
	}
}
