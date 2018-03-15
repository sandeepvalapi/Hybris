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
package de.hybris.platform.servicelayer.interceptor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy.InterceptorExecutionContext;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;


/**
 * Testing the order of the registered Interceptors and the dependency of their typecode.
 */
@UnitTest
public class InterceptorRegistryTest
{
	private DefaultInterceptorRegistry registry;

	/**
	 * Using own DefaultInterceptorRegistry. Only allowed types: item, extensibleitem, product, unit
	 */
	@Before
	public void setUp()
	{
		registry = new DefaultInterceptorRegistryWithoutJalo();
		mockInterceptorPolicy();
	}

	private void mockInterceptorPolicy()
	{
		final ApplicationContext mockedAppCtx = mock(ApplicationContext.class);
		when(mockedAppCtx.getBeanNamesForType(any(Class.class))).thenReturn(new String[] {});

		final InterceptorExecutionPolicy mockedInterceptorPolicy = mock(InterceptorExecutionPolicy.class);
		when(mockedInterceptorPolicy.getEnabledInterceptors((InterceptorExecutionContext<? extends Interceptor>) notNull()))
				.thenAnswer(new Answer()
				{
					@Override
					public Object answer(final InvocationOnMock invocation) throws Throwable
					{
						return ((InterceptorExecutionContext) invocation.getArguments()[0]).getAvailableInterceptors();
					}
				});

		registry.setApplicationContext(mockedAppCtx);
		registry.setInterceptorExecutionPolicy(mockedInterceptorPolicy);
	}

	/**
	 * typecode test. Register one TestLoadInterceptor for ExtensibleItem and one for Unit.
	 */
	@Test
	public void testRegister()
	{
		final Interceptor inter = new TestLoadInterceptor();
		registry.registerInterceptor("ExtensibleItem", inter, Collections.EMPTY_LIST);
		final Interceptor inter2 = new TestLoadInterceptor();
		registry.registerInterceptor("Unit", inter2, Collections.EMPTY_LIST);

		Collection<LoadInterceptor> result = registry.getLoadInterceptors("Item");
		assertEquals(0, result.size());

		result = registry.getLoadInterceptors("ExtensibleItem");
		assertEquals(1, result.size());
		assertEquals(inter, result.iterator().next());

		result = registry.getLoadInterceptors("Product");
		assertEquals(1, result.size());
		assertEquals(inter, result.iterator().next());

		assertTrue(registry.getPrepareInterceptors("Product").isEmpty());

		assertEquals(2, registry.getLoadInterceptors("Unit").size());
	}

	/**
	 * GIVEN:<br/>
	 * added in the following order:
	 * <ul>
	 * <li>1: TestLoadInterceptor, order=50, type=product</li>
	 * <li>2: TestLoadInterceptor, order=500, type=item</li>
	 * <li>3: TestLoadInterceptor, order=5, type=product</li>
	 * <li>4: TestLoadInterceptor, defaultorder, type=unit</li>
	 * <li>5: TestLoadInterceptor, order=5, type=unit</li>
	 * <li>6: TestLoadInterceptor, defaultorder, type=item</li>
	 * <li>7: TestLoadInterceptor, order=4, type=unit, replacing 6</li>
	 * <li>8: TestLoadInterceptor, order=30, type=item</li>
	 * </ul>
	 * THEN:<br/>
	 * lowest order number first<br/>
	 * registry.getLoadInterceptors("Product") returns: 3, 8, 1, 2, 6<br/>
	 * registry.getLoadInterceptors("Unit") returns: 7, 5, 8, 2, 4
	 */
	@Test
	public void testRegisterOrdered()
	{
		final InterceptorMapping mapping1 = createTestLoadInterceptorMapping("inter1", "Product", Integer.valueOf(50));
		registry.registerInterceptor(mapping1);

		final InterceptorMapping mapping2 = createTestLoadInterceptorMapping("inter2", "Item", Integer.valueOf(500));
		registry.registerInterceptor(mapping2);

		final InterceptorMapping mapping3 = createTestLoadInterceptorMapping("inter3", "Product", Integer.valueOf(5));
		registry.registerInterceptor(mapping3);

		final InterceptorMapping mapping4 = createTestLoadInterceptorMapping("inter4", "Unit");
		registry.registerInterceptor(mapping4);

		final InterceptorMapping mapping5 = createTestLoadInterceptorMapping("inter5", "Unit", Integer.valueOf(5));
		registry.registerInterceptor(mapping5);

		final InterceptorMapping mapping6 = createTestLoadInterceptorMapping("inter6", "Item");
		registry.registerInterceptor(mapping6);

		final InterceptorMapping mapping7 = createTestLoadInterceptorMapping("inter7", "Unit", Integer.valueOf(4),
				Collections.singletonList(mapping6.getInterceptor()));
		registry.registerInterceptor(mapping7);

		final InterceptorMapping mapping8 = createTestLoadInterceptorMapping("inter8", "Item", Integer.valueOf(30));
		registry.registerInterceptor(mapping8);

		final Collection<LoadInterceptor> prodresult = registry.getLoadInterceptors("Product");
		assertEquals(5, prodresult.size());

		final Iterator<LoadInterceptor> proditer = prodresult.iterator();
		assertEquals(mapping3.getInterceptor(), proditer.next());
		assertEquals(mapping8.getInterceptor(), proditer.next());
		assertEquals(mapping1.getInterceptor(), proditer.next());
		assertEquals(mapping2.getInterceptor(), proditer.next());
		assertEquals(mapping6.getInterceptor(), proditer.next());

		final Collection<LoadInterceptor> unitresult = registry.getLoadInterceptors("Unit");
		assertEquals(5, unitresult.size());

		final Iterator<LoadInterceptor> unititer = unitresult.iterator();
		assertEquals(mapping7.getInterceptor(), unititer.next());
		assertEquals(mapping5.getInterceptor(), unititer.next());
		assertEquals(mapping8.getInterceptor(), unititer.next());
		assertEquals(mapping2.getInterceptor(), unititer.next());
		assertEquals(mapping4.getInterceptor(), unititer.next());
		//6 was replaced by 7
	}

	/**
	 * GIVEN:<br/>
	 * added in the following order:
	 * <ul>
	 * <li>TestLoadInterceptor, default order, type=item</li>
	 * <li>TestLoadInterceptor, default order, type=product, replacing the item TestLoadInterceptor</li>
	 * </ul>
	 * THEN:<br/>
	 * lowest order number first
	 * <ul>
	 * <li>registry.getLoadInterceptors("Item") returns only one TestLoadInterceptor - for item</li>
	 * <li>registry.getLoadInterceptors("Product") returns only one TestLoadInterceptor - for product only, the item
	 * loadinterceptor was replaced</li>
	 * <li>registry.getPrepareInterceptors("Product") returns nothing, no prepare was registered here</li>
	 * </ul>
	 *
	 */
	@Test
	public void testRegisterWithReplacement()
	{
		final Interceptor inter = new TestLoadInterceptor();
		final Interceptor inter2 = new TestLoadInterceptor();

		registry.registerInterceptor("Item", inter, Collections.EMPTY_LIST);
		registry.registerInterceptor("Product", inter2, Collections.singletonList(inter));

		Collection<LoadInterceptor> result = registry.getLoadInterceptors("Item");
		assertEquals(1, result.size());
		assertEquals(inter, result.iterator().next());

		result = registry.getLoadInterceptors("Product");
		assertEquals(1, result.size());
		assertEquals(inter2, result.iterator().next());

		assertTrue(registry.getPrepareInterceptors("Product").isEmpty());
	}

	/**
	 * GIVEN:<br/>
	 * added in the following order:
	 * <ul>
	 * <li>AAA PrepareInterceptor, order=10, type=unit</li>
	 * <li>BBB PrepareInterceptor, default order, type=unit</li>
	 * <li>CCC PrepareInterceptor, order=1, type=unit</li>
	 * </ul>
	 * THEN:<br/>
	 * lowest order number first registry.getPrepareInterceptors() returns in this order: CCC, AAA, BBB
	 */
	@Test
	public void testOrderedUnitPrepareInterceptors()
	{
		final AAAUnitModelPrepareInterceptor inter1 = new AAAUnitModelPrepareInterceptor();
		final BBBUnitModelPrepareInterceptor inter2 = new BBBUnitModelPrepareInterceptor();
		final CCCUnitModelPrepareInterceptor inter3 = new CCCUnitModelPrepareInterceptor();

		final InterceptorMapping mapp1 = new InterceptorMapping();
		mapp1.setInterceptor(inter1);
		mapp1.setTypeCode("Unit");
		mapp1.setOrder(10);
		final InterceptorMapping mapp2 = new InterceptorMapping();
		mapp2.setInterceptor(inter2);
		mapp2.setTypeCode("Unit");
		final InterceptorMapping mapp3 = new InterceptorMapping();
		mapp3.setInterceptor(inter3);
		mapp3.setOrder(1);
		mapp3.setTypeCode("Unit");

		registry.registerInterceptor(mapp1);
		registry.registerInterceptor(mapp2);
		registry.registerInterceptor(mapp3);


		final Collection<PrepareInterceptor> prepcoll = registry.getPrepareInterceptors("Unit");
		assertEquals(3, prepcoll.size());
		final Iterator<PrepareInterceptor> iter = prepcoll.iterator();
		assertEquals(inter3, iter.next());
		assertEquals(inter1, iter.next());
		assertEquals(inter2, iter.next());

	}

	private InterceptorMapping createTestLoadInterceptorMapping(final String name, final String typecode)
	{
		return createTestLoadInterceptorMapping(name, typecode, null, null);
	}

	private InterceptorMapping createTestLoadInterceptorMapping(final String name, final String typecode, final Integer order)
	{
		return createTestLoadInterceptorMapping(name, typecode, order, null);
	}

	/**
	 * Creates an InterceptorMapping for TestLoadInterceptors
	 *
	 * @param name
	 *           name of TestLoadInterceptors
	 * @param typecode
	 *           type for TestLoadInterceptors
	 * @param order
	 *           the order for TestLoadInterceptors
	 * @param replacements
	 *           those Interseptors are replaced
	 * @return an InterceptorMapping
	 */
	private InterceptorMapping createTestLoadInterceptorMapping(final String name, final String typecode, final Integer order,
			final Collection<Interceptor> replacements)
	{
		final TestLoadInterceptor tli = new TestLoadInterceptor(name);
		final InterceptorMapping mapping = new InterceptorMapping();
		mapping.setTypeCode(typecode);
		mapping.setInterceptor(tli);
		if (order != null)
		{
			mapping.setOrder(order.intValue());
		}
		if (replacements != null)
		{
			mapping.setReplacedInterceptors(replacements);
		}
		return mapping;
	}

	/**
	 * dummy loadinterceptor
	 */
	private static class TestLoadInterceptor implements LoadInterceptor
	{
		private String name = null;

		public TestLoadInterceptor()
		{
			super();
		}

		public TestLoadInterceptor(final String name)
		{
			this.name = name;
		}

		@Override
		public void onLoad(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString()
		{
			if (name != null)
			{
				return getClass().getSimpleName() + "<" + name + ">";
			}
			return super.toString();
		}
	}

	/**
	 * own InterceptorRegistry. Only allowed types are: item, extensibleitem, product, unit. The dependancies are:
	 * item-&gt;extensibleitem-&gt;product, item-&gt;extensibleitem-&gt;unit
	 */
	private static class DefaultInterceptorRegistryWithoutJalo extends DefaultInterceptorRegistry
	{
		@Override
		protected boolean isValidTypeCode(final String code)
		{
			if (code.equalsIgnoreCase("Item") || code.equalsIgnoreCase("ExtensibleItem") || code.equalsIgnoreCase("Product")
					|| code.equalsIgnoreCase("Unit"))
			{
				return true;
			}
			fail("unsupported type " + code + " passed");
			return false;
		}

		@Override
		protected List<String> getAssignableTypes(final String type)
		{
			if (type.equalsIgnoreCase("Item"))
			{
				return Arrays.asList("item");
			}
			if (type.equalsIgnoreCase("ExtensibleItem"))
			{
				return Arrays.asList("item", "extensibleitem");
			}
			if (type.equalsIgnoreCase("Product"))
			{
				return Arrays.asList("item", "extensibleitem", "product");
			}
			if (type.equalsIgnoreCase("Unit"))
			{
				return Arrays.asList("item", "extensibleitem", "unit");
			}
			fail("unsupported type " + type + " passed");
			return null;
		}

		@Override
		protected void init()
		{
			// nothing to do
		}
	}

	/**
	 * dummy test PrepareInterceptor class
	 */
	private class AAAUnitModelPrepareInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof UnitModel)
			{
				((UnitModel) model).setCode("AAA");
			}
			else
			{
				throw new InterceptorException("model is not an instance of UnitModel");
			}

		}
	}

	/**
	 * dummy test PrepareInterceptor class
	 */
	private class BBBUnitModelPrepareInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof UnitModel)
			{
				((UnitModel) model).setCode("BBB");
			}
			else
			{
				throw new InterceptorException("model is not an instance of UnitModel");
			}

		}
	}

	/**
	 * dummy test PrepareInterceptor class
	 */
	private class CCCUnitModelPrepareInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof UnitModel)
			{
				((UnitModel) model).setCode("CCC");
			}
			else
			{
				throw new InterceptorException("model is not an instance of UnitModel");
			}

		}

	}
}
