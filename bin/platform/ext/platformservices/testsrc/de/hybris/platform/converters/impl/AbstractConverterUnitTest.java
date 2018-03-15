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
package de.hybris.platform.converters.impl;

import junit.framework.Assert;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;


/**
 * For testing {@link AbstractConverter}, especially it's optimizations for <lookup-method> injected beans and
 * collection conversion.
 */
@UnitTest
public class AbstractConverterUnitTest
{

	private static final String OBJECT_A_NAME = "OBJECT A";
	private static final String OBJECT_B_NAME = "OBJECT B";

	
	@Mock
	DummyConverterForMockito converter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockitoVsConverterSanityCheck()
	{
		AtomicInteger src = new AtomicInteger(1);
		
		DummyConverterForMockito conv = new DummyConverterForMockito();
		
		List<AtomicInteger> tgtList = conv.convertAll(Collections.singletonList(src));
		
		assertNotNull(tgtList);
		assertEquals(1, tgtList.size());
		assertEquals(-1, tgtList.get(0).get());
	}

	
	@Test
	public void testMockitoVsConverters()
	{
		AtomicInteger src = new AtomicInteger(1);
		
		Mockito.when(converter.convert(src)).thenReturn(new AtomicInteger(11));  // default you be -1 * src.get() !!!
		
		List<AtomicInteger> tgtList = Converters.convertAll(Collections.singletonList(src), converter);
		
		assertNotNull(tgtList);
		assertEquals(1, tgtList.size());
		assertEquals(11, tgtList.get(0).get());
	}

	@Test
	@Ignore("Broken since Mockito simply cannot deal with default methods!")
	public void testMockitoVsConverter()
	{
		AtomicInteger src = new AtomicInteger(1);
		
		Mockito.when(converter.convert(src)).thenReturn(new AtomicInteger(11)); // default you be -1 * src.get() !!!
		
		List<AtomicInteger> tgtList = converter.convertAll(Collections.singletonList(src));
		
		assertNotNull(tgtList);
		assertEquals(1, tgtList.size());
		assertEquals(11, tgtList.get(0).get());
	}

	
	@Test
	public void testUsingBean() throws Exception
	{
		final AbstractConverter<Object, Object> conv = new TestConverter()
		{
			@Override
			protected Object createTarget()
			{
				return "ThisIsANewBeanInstance-" + System.nanoTime();
			}
		};

		final Object source = "SomeSource";
		final Object target = conv.convert(source);

		Assert.assertNotNull(target);
		Assert.assertEquals(String.class, target.getClass());
		Assert.assertTrue(((String) target).startsWith("ThisIsANewBeanInstance-"));
		Assert.assertSame(source, ((TestConverter) conv).populateCalledWithSource);
		Assert.assertSame(target, ((TestConverter) conv).populateCalledWithTarget);

		final Object source2 = "SomeSource2";
		final Object target2 = conv.convert(source2);
		Assert.assertNotNull(target2);
		Assert.assertNotSame(target, target2);
		Assert.assertEquals(String.class, target2.getClass());
		Assert.assertTrue(((String) target2).startsWith("ThisIsANewBeanInstance-"));
		Assert.assertSame(source2, ((TestConverter) conv).populateCalledWithSource);
		Assert.assertSame(target2, ((TestConverter) conv).populateCalledWithTarget);
	}


	@Test
	public void testUsingInjectedClass() throws Exception
	{
		final AbstractConverter<Object, Object> conv = new TestConverter()
		{
			@Override
			protected Object createTarget()
			{
				Assert.fail();
				return null;
			}
		};
		conv.setTargetClass((Class) String.class);

		final Object source = "SomeSource";
		final Object target = conv.convert(source);

		Assert.assertNotNull(target);
		Assert.assertEquals(new String(), target);
		Assert.assertSame(source, ((TestConverter) conv).populateCalledWithSource);
		Assert.assertSame(target, ((TestConverter) conv).populateCalledWithTarget);

		final Object source2 = "SomeSource2";
		final Object target2 = conv.convert(source2);

		Assert.assertNotNull(target);
		Assert.assertEquals(new String(), target2);
		Assert.assertNotSame(target, target2);
		Assert.assertSame(source2, ((TestConverter) conv).populateCalledWithSource);
		Assert.assertSame(target2, ((TestConverter) conv).populateCalledWithTarget);
	}

	@Test
	public void testBeanSetupErrors() throws Exception
	{
		try
		{
			final AbstractConverter illegalConverter = new AbstractConverter()
			{
				@Override
				public void populate(final Object source, final Object target)
				{
					// nope
				}
			};
			illegalConverter.setBeanName("illegalConverter");
			illegalConverter.afterPropertiesSet();
			Assert.fail("Missing targetClass and missing createTarget() should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// expected
		}

		final AbstractConverter legalConverterOldWay = new AbstractConverter()
		{
			@Override
			public void populate(final Object source, final Object target)
			{
				// nope
			}

			@Override
			protected Object createTarget()
			{
				return "newBean" + System.nanoTime();
			}
		};
		legalConverterOldWay.setBeanName("legalConverterOldWay");
		legalConverterOldWay.afterPropertiesSet();

		final AbstractConverter legalConverterNewWay = new AbstractConverter()
		{
			@Override
			public void populate(final Object source, final Object target)
			{
				// nope
			}
		};
		legalConverterNewWay.setTargetClass(String.class);
		legalConverterNewWay.setBeanName("legalConverterNewWay");
		legalConverterNewWay.afterPropertiesSet();
	}

	static class TestConverter extends AbstractConverter<Object, Object>
	{
		Object populateCalledWithSource;
		Object populateCalledWithTarget;

		@Override
		public void populate(final Object source, final Object target)
		{
			this.populateCalledWithSource = source;
			this.populateCalledWithTarget = target;
		}
	}

	static class ToConvert2ConverterConverter extends AbstractConverter<TestClassForConversion, TestClassForConversion>
	{
		public ToConvert2ConverterConverter()
		{
			setTargetClass(TestClassForConversion.class);
			setBeanName("ToConvert2ConverterConverter");
		}

		@Override
		public void populate(final TestClassForConversion source, final TestClassForConversion target)
		{
			target.setName(source.getName());
		}
	}

	static class ToConvert2ConverterConverterThrowingExceptions extends ToConvert2ConverterConverter
	{

		public ToConvert2ConverterConverterThrowingExceptions()
		{
			super();
			setBeanName("ToConvert2ConverterConverterThrowingExceptions");
		}

		@Override
		public TestClassForConversion convert(final TestClassForConversion source) throws ConversionException
		{
			if (source.getName().equals(OBJECT_A_NAME))
			{
				throw new ConversionException("CONVERSION EXCEPTION");
			}
			return super.convert(source);
		}
	}

	@Test
	public void emptyCollectionConvertAll()
	{
		final ToConvert2ConverterConverter converter = new ToConvert2ConverterConverter();
		final List<TestClassForConversion> converterObjects = converter.convertAll(null);

		assertNotNull("Collection conversion result should never be null", converterObjects);
		assertTrue("Empty collection conversion should return empty collection", converterObjects.isEmpty());
	}

	@Test
	public void testConvertAll()
	{
		final List<TestClassForConversion> objects = new ArrayList();
		TestClassForConversion objectA = new TestClassForConversion();
		objectA.setName(OBJECT_A_NAME);
		TestClassForConversion objectB = new TestClassForConversion();
		objectB.setName(OBJECT_B_NAME);
		objects.add(objectA);
		objects.add(objectB);

		final ToConvert2ConverterConverter converter = new ToConvert2ConverterConverter();
		final List<TestClassForConversion> converterObjects = converter.convertAll(objects);

		assertNotNull("Collection conversion result should never be null", converterObjects);
		assertEquals("Collection conversion result size should match input collection size", objects.size(),
				converterObjects.size());

		final TestClassForConversion convertedObjectA = converterObjects.get(0);

		assertNotNull(convertedObjectA);
		assertEquals("Object conversion should have populated name properly", OBJECT_A_NAME, convertedObjectA.getName());

		final TestClassForConversion convertedObjectB = converterObjects.get(1);

		assertNotNull(convertedObjectB);
		assertEquals("Object conversion should have populated name properly", OBJECT_B_NAME, convertedObjectB.getName());
	}

	@Test
	public void testConvertAllIgnoreExceptions()
	{
		final List<TestClassForConversion> objects = new ArrayList();
		TestClassForConversion objectA = new TestClassForConversion();
		objectA.setName(OBJECT_A_NAME);
		TestClassForConversion objectB = new TestClassForConversion();
		objectB.setName(OBJECT_B_NAME);
		objects.add(objectA);
		objects.add(objectB);

		final ToConvert2ConverterConverter converter = new ToConvert2ConverterConverter();
		final List<TestClassForConversion> converterObjects = converter.convertAllIgnoreExceptions(objects);

		assertNotNull("Collection conversion result should never be null", converterObjects);
		assertEquals("Collection conversion result size should match input collection size", objects.size(),
				converterObjects.size());

		final TestClassForConversion convertedObjectA = converterObjects.get(0);

		assertNotNull(convertedObjectA);
		assertEquals("Object conversion should have populated name properly", OBJECT_A_NAME, convertedObjectA.getName());

		final TestClassForConversion convertedObjectB = converterObjects.get(1);

		assertNotNull(convertedObjectB);
		assertEquals("Object conversion should have populated name properly", OBJECT_B_NAME, convertedObjectB.getName());
	}

	@Test
	public void testConvertAllThrowingException()
	{
		final List<TestClassForConversion> objects = new ArrayList();
		TestClassForConversion objectA = new TestClassForConversion();
		objectA.setName(OBJECT_A_NAME);
		TestClassForConversion objectB = new TestClassForConversion();
		objectB.setName(OBJECT_B_NAME);
		objects.add(objectA);
		objects.add(objectB);


		final ToConvert2ConverterConverterThrowingExceptions converter = new ToConvert2ConverterConverterThrowingExceptions();
		ConversionException exception = null;
		try
		{
			converter.convertAll(objects);
		}
		catch (ConversionException ex)
		{
			exception = ex;
		}

		assertNotNull("Conversion should throw exception", exception);
	}

	@Test
	public void testConvertAllIgnoringThrownExceptions()
	{
		final List<TestClassForConversion> objects = new ArrayList();
		TestClassForConversion objectA = new TestClassForConversion();
		objectA.setName(OBJECT_A_NAME);
		TestClassForConversion objectB = new TestClassForConversion();
		objectB.setName(OBJECT_B_NAME);
		objects.add(objectA);
		objects.add(objectB);


		final ToConvert2ConverterConverterThrowingExceptions converter = new ToConvert2ConverterConverterThrowingExceptions();
		List<TestClassForConversion> converterObjects = null;
		ConversionException exception = null;
		try
		{
			converterObjects = converter.convertAllIgnoreExceptions(objects);
		}
		catch (ConversionException ex)
		{
			exception = ex;
		}

		assertNull("Conversion should ignore exceptions", exception);
		assertNotNull("Collection conversion result should never be null", converterObjects);
		assertEquals("Collection conversion should contain properly converted OBJECT B", 1, converterObjects.size());

		final TestClassForConversion convertedObjectB = converterObjects.get(0);

		assertNotNull(convertedObjectB);
		assertEquals("Proper object B conversion", convertedObjectB.getName(), OBJECT_B_NAME);
	}
}
