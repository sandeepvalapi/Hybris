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
package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter.ModelAttributeInfo;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter.TypeAttributeInfo;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;


@UnitTest
public class ItemModelConverterUnitTest
{

	private static final Logger LOG = Logger.getLogger(ItemModelConverterUnitTest.class.getName());

	private static final int MAX_CALLS = 1000000;

	@Mock
	private ModelService modelService;

	@Mock
	private I18NService i18nService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private SerializationStrategy serializationStrategy;

	@Mock
	private AttributePrefetchMode attributePrefetchMode;

	@Mock
	private SourceTransformer sourceTransformer;

	@Mock
	private SelfHealingService selfHealingService;

	private TypeAttributeInfo typeAttributeInfo;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		typeAttributeInfo = new TypeAttributeInfo("foo", "bar")
		{
			@Override
			boolean isDynamic()
			{
				return false;
			}
		};

	}

	@Test
	public void testFunctional() throws InterruptedException
	{

		final String type = "foo";
		final Class<? extends AbstractItemModel> clazz = Foo.class;
		final ItemModelConverter converter = new ItemModelConverter(modelService, i18nService, commonI18NService, type, clazz,
				serializationStrategy, sourceTransformer, selfHealingService)
		{
			@Override
			protected AttributePrefetchMode readPrefetchSettings()
			{
				return attributePrefetchMode;
			}
		};


		final Foo modelFoo = new Foo("foo");

		final Foo modelBoo = new Boo("boo", "foo");

		final Foo modelBar = new Bar("bar", "boo", "foo");


		assertValid(converter, modelFoo, "foo", "foo");
		assertValid(converter, modelBoo, "foo", "foo");
		assertValid(converter, modelBar, "foo", "foo");


		assertNotValid(converter, modelFoo, "boo", "boo");
		assertValid(converter, modelBoo, "boo", "boo");
		assertValid(converter, modelBar, "boo", "boo");


		assertNotValid(converter, modelFoo, "bar", "bar");
		assertNotValid(converter, modelBoo, "bar", "bar");
		assertValid(converter, modelBar, "bar", "bar");


	}

	private void assertValid(final ItemModelConverter converter, final Foo model, final String attribute, final Object value)
	{
		final ModelAttributeInfo attributeFoo = createAttributeInfo(attribute);

		Assert.assertEquals(value, converter.getFieldValue(model, attributeFoo));
	}


	private void assertNotValid(final ItemModelConverter converter, final Foo model, final String attribute, final Object value)
	{
		final ModelAttributeInfo attributeFoo = createAttributeInfo(attribute);
		Assert.assertNull(converter.getFieldValue(model, attributeFoo));
	}

	@Test
	public void testPerformance() throws InterruptedException
	{
		for (int i = 0; i < 10; i++)
		{
			testPerformanceImpl();
		}
	}

	private void testPerformanceImpl() throws InterruptedException
	{

		final String type = "foo";
		final Class<? extends AbstractItemModel> clazz = Foo.class;
		final ItemModelConverter converter = new ItemModelConverter(modelService, i18nService, commonI18NService, type, clazz,
				serializationStrategy, sourceTransformer, selfHealingService)
		{
			@Override
			protected AttributePrefetchMode readPrefetchSettings()
			{
				return attributePrefetchMode;
			}
		};

		final ModelAttributeInfo attributeFoo = createAttributeInfo("foo");
		final ModelAttributeInfo attributeBoo = createAttributeInfo("boo");
		final ModelAttributeInfo attributeBar = createAttributeInfo("bar");

		final Foo modelFoo = new Foo("foo");

		final Foo modelBoo = new Boo("boo", "foo");

		final Foo modelBar = new Bar("bar", "boo", "foo");

		final long start = System.currentTimeMillis();

		callGets(converter, attributeFoo, modelFoo);
		callGets(converter, attributeFoo, modelBoo);
		callGets(converter, attributeFoo, modelBar);

		callGets(converter, attributeBoo, modelBoo);
		callGets(converter, attributeBoo, modelBar);

		callGets(converter, attributeBar, modelBar);

		final long time = System.currentTimeMillis() - start;

		LOG.info(" get field times  " + MAX_CALLS + " took " + time);

	}

	private ModelAttributeInfo createAttributeInfo(final String attribute)
	{
		return new TestModelAttributeInfo(attribute);
	}

	private void callGets(final ItemModelConverter converter, final ModelAttributeInfo attribute, final AbstractItemModel model)
	{
		for (int i = 0; i < MAX_CALLS; i++)
		{
			converter.getFieldValue(model, attribute);
		}

	}

	class TestModelAttributeInfo extends ModelAttributeInfo
	{

		public TestModelAttributeInfo(final String qualifier)
		{
			super(qualifier);

		}

		@Override
		public TypeAttributeInfo getAttributeInfo()
		{

			return typeAttributeInfo;
		}
	}


	class Foo extends AbstractItemModel
	{
		public final static String FOO = "foo";

		Foo(final String foo)
		{
			super();
			setFoo(foo);
		}

		public String getFoo()
		{
			return getPersistenceContext().getPropertyValue(FOO);
		}

		public void setFoo(final String value)
		{
			getPersistenceContext().setPropertyValue(FOO, value);
		}
	}

	class Boo extends Foo
	{
		public final static String BOO = "boo";

		Boo(final String boo, final String foo)
		{
			super(foo);
			setBoo(boo);
		}

		public String getBoo()
		{
			return getPersistenceContext().getPropertyValue(BOO);
		}

		public void setBoo(final String value)
		{
			getPersistenceContext().setPropertyValue(BOO, value);
		}
	}

	class Bar extends Boo
	{
		public final static String BAR = "bar";

		Bar(final String bar, final String boo, final String foo)
		{
			super(boo, foo);
			setBar(bar);
		}

		public String getBar()
		{
			return getPersistenceContext().getPropertyValue(BAR);
		}

		public void setBar(final String value)
		{
			getPersistenceContext().setPropertyValue(BAR, value);
		}

	}
}
