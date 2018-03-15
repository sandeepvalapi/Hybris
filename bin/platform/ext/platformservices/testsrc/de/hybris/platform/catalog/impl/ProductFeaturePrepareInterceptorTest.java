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
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Tests {@link ProductFeaturePrepareInterceptor} interceptor.
 */
@UnitTest
public class ProductFeaturePrepareInterceptorTest
{

	private ProductFeaturePrepareInterceptor preparer;
	private ProductFeatureModel productFeature;

	@Mock
	private InterceptorContext mockInterceptorContext;

	private static final String CLASSIFICATION_CLASS_CODE = "myTestClass";
	private static final String CLASSIFICATION_ATTRIBUTE_CODE = "MyTestAttribute";
	private static final String TEST_CATALOG_ID = "MyCatalog";
	private static final String TEST_VERSION = "myVersion";


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		preparer = new ProductFeaturePrepareInterceptor();
		productFeature = new ProductFeatureModel();
		final ClassAttributeAssignmentModel caa = new ClassAttributeAssignmentModel();
		final ClassificationClassModel clClass = new ClassificationClassModel();
		final ClassificationAttributeModel clAttribute = new ClassificationAttributeModel();
		final ProductModel product = new ProductModel();
		product.setModifiedtime(new Date());
		productFeature.setProduct(product);

		clClass.setCode(CLASSIFICATION_CLASS_CODE);
		clAttribute.setCode(CLASSIFICATION_ATTRIBUTE_CODE);

		caa.setClassificationAttribute(clAttribute);
		caa.setClassificationClass(clClass);

		productFeature.setClassificationAttributeAssignment(caa);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.impl.ProductFeaturePrepareInterceptor#onPrepare(java.lang.Object, de.hybris.platform.servicelayer.interceptor.InterceptorContext)}
	 * .
	 * 
	 * @throws InterceptorException
	 */
	@Test
	public void testOnPrepareForNullCatalogVersion() throws InterceptorException
	{
		//test preparation of product feature qualifier if system catalog version of the assignment is null:
		final String expected = CLASSIFICATION_CLASS_CODE + "." + CLASSIFICATION_ATTRIBUTE_CODE.toLowerCase().intern();
		Mockito.when(mockInterceptorContext.isModified(productFeature)).thenReturn(true);

		preparer.onPrepare(productFeature, mockInterceptorContext);

		assertThat(productFeature.getQualifier()).isEqualTo(expected);
	}

	@Test
	public void testOnPrepare() throws Exception
	{
		Mockito.when(mockInterceptorContext.isModified(productFeature)).thenReturn(true);
		final Date oldProductModifiedTime = productFeature.getProduct().getModifiedtime();

		final ClassificationSystemModel testCatalog = new ClassificationSystemModel();
		testCatalog.setId(TEST_CATALOG_ID);

		final ClassificationSystemVersionModel testVersion = new ClassificationSystemVersionModel();
		testVersion.setCatalog(testCatalog);
		testVersion.setVersion(TEST_VERSION);

		productFeature.getClassificationAttributeAssignment().setSystemVersion(testVersion);
		//test preparation of product feature qualifier if system catalog version of the assignment is not null:

		final String expected = TEST_CATALOG_ID + '/' + TEST_VERSION + '/' + CLASSIFICATION_CLASS_CODE + '.'
				+ CLASSIFICATION_ATTRIBUTE_CODE.toLowerCase().intern();
		Thread.currentThread().sleep(1000L);

		preparer.onPrepare(productFeature, mockInterceptorContext);

		assertThat(productFeature.getQualifier()).isEqualTo(expected);
		assertThat(productFeature.getProduct().getModifiedtime().after(oldProductModifiedTime)).isTrue();
	}
}
