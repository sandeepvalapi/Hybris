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
package de.hybris.platform.order.interceptors;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AbstractOrderEntryPreparerTest
{
	private DefaultAbstractOrderEntryPreparer interceptor;

	@Mock
	private TypeService mockTypeService;

	@Mock
	private ConfigurationService mockConfigurationService;

	@Mock
	private Configuration mockConfiguration;

	@Mock
	private ModelService mockModelService;

	private OrderModel order;
	private AbstractOrderEntryModel entry;
	private CatalogVersionModel catalogVersion;
	private ProductModel product;

	private ComposedTypeModel productComposedType;
	private ComposedTypeModel variantComposedType;

	private CatalogModel catalog;

	@Mock
	private InterceptorContext mockInterceptorContext;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		interceptor = new DefaultAbstractOrderEntryPreparer();
		interceptor.setConfigurationService(mockConfigurationService);
		interceptor.setTypeService(mockTypeService);

		when(mockInterceptorContext.getModelService()).thenReturn(mockModelService);

		product = new ProductModel();
		product.setCode("test_product");
		//product.setName("Test Product");

		catalog = new CatalogModel();
		catalog.setId("testCatalog");

		catalogVersion = new CatalogVersionModel();
		catalogVersion.setCatalog(catalog);

		order = new OrderModel();
		order.setCalculated(Boolean.TRUE);
		entry = new AbstractOrderEntryModel();
		entry.setCalculated(Boolean.TRUE);

		entry.setOrder(order);
		order.setEntries(Collections.singletonList(entry));

		productComposedType = new ComposedTypeModel();
		productComposedType.setCode("Product");

		variantComposedType = new ComposedTypeModel();
		variantComposedType.setCode("VariantProduct");
		variantComposedType.setSuperType(productComposedType);

		variantComposedType.setCode("CatalogVersion");
	}

	@Test
	public void testOnPrepareProductChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);

		recordMockInterceptorContext(entry, AbstractOrderEntryModel.PRODUCT);
		interceptor.onPrepare(entry, mockInterceptorContext);

		assertEquals("Incorrect info filed for null product", "n/a", entry.getInfo());
		assertFalse("Order entry should not be calculated", entry.getCalculated().booleanValue());
		assertFalse("Order should not be calculated", order.getCalculated().booleanValue());

		//setting product
		entry.setProduct(product);

		recordMockInterceptorContext(entry, AbstractOrderEntryModel.PRODUCT);
		when(mockTypeService.getComposedTypeForClass(product.getClass())).thenReturn(productComposedType);
		when(mockTypeService.getComposedTypeForClass(variantComposedType.getClass())).thenReturn(variantComposedType);
		when(mockConfigurationService.getConfiguration()).thenReturn(mockConfiguration);
		when(mockConfiguration.getString("orderentry.infofield.product", null)).thenReturn(
				  "product \"${code}\" with name \"${name}\" from cv: \"${catalogVersion.catalog.id}\"");
		when(mockConfiguration.getString("orderentry.infofield.variantproduct", null)).thenReturn(null);
		when(mockModelService.getAttributeValue(product, "name")).thenReturn("Test Product");
		when(mockModelService.getAttributeValue(product, "code")).thenReturn("test_product");
		when(mockModelService.getAttributeValue(product, "catalogVersion")).thenReturn(catalogVersion);
		when(mockModelService.getAttributeValue(catalogVersion, "catalog")).thenReturn(catalog);
		when(mockModelService.getAttributeValue(catalog, "id")).thenReturn(catalog.getId());

		interceptor.onPrepare(entry, mockInterceptorContext);

		assertOrderEntryCalculatedStatus(entry, false);

		assertEquals("Incorrect info filed for null product",
				  "product \"test_product\" with name \"Test Product\" from cv: \"testCatalog\"", entry.getInfo());

	}

	@Test
	public void testOnPrepareQuantityChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.QUANTITY);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareUnitChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.UNIT);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareBasePriceChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.BASEPRICE);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareTaxValuesChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.TAXVALUES);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareDiscountValuesChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.DISCOUNTVALUES);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareGiweawayChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.GIVEAWAY);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareRejectedChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.REJECTED);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, false);
	}

	@Test
	public void testOnPrepareCommentsChanged() throws InterceptorException
	{
		assertOrderEntryCalculatedStatus(entry, true);
		recordMockInterceptorContext(entry, AbstractOrderEntryModel.COMMENTS);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertOrderEntryCalculatedStatus(entry, true);
	}

	@Test
	public void testOnPrepareUpdateEntryNumbers() throws InterceptorException
	{
		assertNull(entry.getEntryNumber());
		when(Boolean.valueOf(mockInterceptorContext.isModified(entry, AbstractOrderEntryModel.ENTRYNUMBER))).thenReturn(Boolean
				  .TRUE);
		interceptor.onPrepare(entry, mockInterceptorContext);
		assertEquals(0, entry.getEntryNumber().intValue());

		for (int i = 0; i < 3; i++)
		{
			final AbstractOrderEntryModel testEntry = new AbstractOrderEntryModel();
			testEntry.setCalculated(Boolean.TRUE);
			testEntry.setEntryNumber(interceptor.APPEND_AS_LAST); //
			testEntry.setOrder(order);
			when(Boolean.valueOf(mockInterceptorContext.isModified(testEntry, AbstractOrderEntryModel.ENTRYNUMBER))).thenReturn
					  (Boolean.TRUE);
			interceptor.onPrepare(testEntry, mockInterceptorContext);
			assertEquals(i + 1, testEntry.getEntryNumber().intValue());
		}
	}

	private void recordMockInterceptorContext(final AbstractOrderEntryModel orderEntry, final String attributeChanged)
	{
		final Collection<String> parameters = Arrays.asList(AbstractOrderEntryModel.PRODUCT, AbstractOrderEntryModel.QUANTITY,
				  AbstractOrderEntryModel.UNIT, AbstractOrderEntryModel.BASEPRICE, AbstractOrderEntryModel.TAXVALUES,
				  AbstractOrderEntryModel.DISCOUNTVALUES, AbstractOrderEntryModel.GIVEAWAY, AbstractOrderEntryModel.REJECTED);

		for (final String parameter : parameters)
		{
			if (StringUtils.equals(attributeChanged, parameter))
			{
				when(Boolean.valueOf(mockInterceptorContext.isModified(orderEntry, parameter))).thenReturn(Boolean.TRUE);
			}
			else
			{
				when(Boolean.valueOf(mockInterceptorContext.isModified(order, parameter))).thenReturn(Boolean.FALSE);
			}
		}

		when(Boolean.valueOf(mockInterceptorContext.isRemoved(orderEntry))).thenReturn(Boolean.FALSE);
	}

	private void assertOrderEntryCalculatedStatus(final AbstractOrderEntryModel orderEntry, final boolean expectedFlag)
	{
		assertTrue("OrderEntry should " + (expectedFlag ? "" : " not ") + " be calculated", orderEntry.getCalculated()
				  .booleanValue() == expectedFlag);
		final AbstractOrderModel order = orderEntry.getOrder();
		if (order != null)
		{
			assertTrue("Order should " + (expectedFlag ? "" : " not ") + " be calculated",
					  order.getCalculated().booleanValue() == expectedFlag);

		}
	}

}
