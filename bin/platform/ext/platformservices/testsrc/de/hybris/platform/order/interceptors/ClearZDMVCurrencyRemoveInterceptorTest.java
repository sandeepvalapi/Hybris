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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.order.daos.ZoneDeliveryModeValueDao;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



@UnitTest
public class ClearZDMVCurrencyRemoveInterceptorTest
{

	private ClearZDMVCurrencyRemoveInterceptor interceptor;

	@Mock
	private ZoneDeliveryModeValueDao mockZoneDeliveryModeValueDao;

	@Mock
	private InterceptorContext mockContext;

	@Mock
	private ModelService mockModelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		interceptor = new ClearZDMVCurrencyRemoveInterceptor();
		interceptor.setZoneDeliveryModeValueDao(mockZoneDeliveryModeValueDao);
		when(mockContext.getModelService()).thenReturn(mockModelService);
	}

	@Test
	public void testZDMValueFound()
	{
		final CurrencyModel removeThisCurrency = new CurrencyModel();
		final ZoneDeliveryModeValueModel dependantZDMV1 = new ZoneDeliveryModeValueModel();
		dependantZDMV1.setCurrency(removeThisCurrency);
		final ZoneDeliveryModeValueModel dependantZDMV2 = new ZoneDeliveryModeValueModel();
		dependantZDMV2.setCurrency(removeThisCurrency);

		final List<ZoneDeliveryModeValueModel> mockResult = new ArrayList<ZoneDeliveryModeValueModel>(2);
		mockResult.add(dependantZDMV1);
		mockResult.add(dependantZDMV2);

		when(mockZoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(removeThisCurrency)).thenReturn(mockResult);

		final ArgumentMatcher<Collection<Object>> matcher = new ArgumentMatcher<Collection<Object>>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof Collection<?>))
				{
					return false;
				}
				final Collection<Object> objectsToRemove = (Collection<Object>) argument;

				if (objectsToRemove.size() == 2)
				{
					final Iterator iterator = objectsToRemove.iterator();
					final Object obj1 = iterator.next();
					final Object obj2 = iterator.next();
					if (obj1 instanceof ZoneDeliveryModeValueModel && obj2 instanceof ZoneDeliveryModeValueModel)
					{
						return ((ZoneDeliveryModeValueModel) obj1).equals(dependantZDMV1)
								&& ((ZoneDeliveryModeValueModel) obj2).equals(dependantZDMV2);
					}
				}

				return false;
			}
		};

		try
		{
			interceptor.onRemove(removeThisCurrency, mockContext);

			verify(mockModelService, times(1)).removeAll(argThat(matcher));
		}
		catch (final InterceptorException e)
		{
			Assert.fail("unexpected InterceptorException : " + e);
		}

	}

	@Test
	public void testZDMValueNotFound()
	{
		final CurrencyModel removeThisCurrency = new CurrencyModel();

		when(mockZoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(removeThisCurrency)).thenReturn(
				Collections.<ZoneDeliveryModeValueModel> emptyList());

		try
		{
			interceptor.onRemove(removeThisCurrency, mockContext);
			verify(mockModelService, never()).removeAll(any(Collection.class));
		}
		catch (final InterceptorException e)
		{
			Assert.fail("unexpected InterceptorException : " + e);
		}

	}

	@Test
	public void testZDMValueNotFoundNull()
	{
		final CurrencyModel removeThisCurrency = new CurrencyModel();

		when(mockZoneDeliveryModeValueDao.findZoneDeliveryModeValuesByCurrency(removeThisCurrency)).thenReturn(null);

		try
		{
			interceptor.onRemove(removeThisCurrency, mockContext);
			verify(mockModelService, never()).removeAll(any(Collection.class));
		}
		catch (final InterceptorException e)
		{
			Assert.fail("unexpected InterceptorException : " + e);
		}

	}

}
