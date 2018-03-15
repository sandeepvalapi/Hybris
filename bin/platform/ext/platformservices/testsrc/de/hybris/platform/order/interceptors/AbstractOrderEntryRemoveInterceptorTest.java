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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AbstractOrderEntryRemoveInterceptorTest
{
	private DefaultAbstractOrderEntryRemoveInterceptor interceptor;
	private OrderModel order;
	private AbstractOrderEntryModel entry;

	@Mock
	private ModelService mockModelService;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		interceptor = new DefaultAbstractOrderEntryRemoveInterceptor();

		order = new OrderModel();
		order.setCalculated(Boolean.TRUE);

		entry = new AbstractOrderEntryModel();
		entry.setCalculated(Boolean.TRUE);


		order.setEntries(Collections.singletonList(entry));

		when(interceptorContext.getModelService()).thenReturn(mockModelService);

	}

	@Test
	public void testOnRemove() throws Exception
	{
		assertTrue("Order should be calcualted", order.getCalculated().booleanValue());
		boolean success = false;
		try
		{
			interceptor.onRemove(entry, interceptorContext);
			fail("");
		}
		catch (final InterceptorException e)
		{
			success = true;
		}
		assertTrue("InterceptorException expected", success);

		entry.setOrder(order);
		interceptor.onRemove(entry, interceptorContext);

		assertFalse("Order should not be calcualted", order.getCalculated().booleanValue());

	}

}
