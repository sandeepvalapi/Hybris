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
/**
 *
 */
package de.hybris.platform.converters.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/platformservices/test/modify-populators-test-spring.xml")
@UnitTest
public class ModifyPopulatorListBeanPostProcessorTest
{
	@Autowired
	private Converter<Data, Data> baseConverter;
	@Autowired
	private Converter<Data, Data> aliasedConverterId;
	@Autowired
	private Converter<Data, Data> withGrantParentConverter;
	@Autowired
	private Converter<Data, Data> mergedConverter;
	@Autowired
	private Converter<Data, Data> simpleConverter;
	@Autowired
	private Converter<Data, Data> listConverter;


	@Autowired
	private Converter<Data, Data> parentAlias1;
	@Autowired
	private Converter<Data, Data> parentAlias3;


	private Data input;

	@Before
	public void setup()
	{
		input = new Data("A", "B", "C", "D");
	}

	@Test
	public void testBaseConverter()
	{
		final Data output = baseConverter.convert(input);

		assertData("A", null, "C", null, output);
	}

	@Test
	public void testAliasedConverter()
	{
		final Data output = aliasedConverterId.convert(input);

		assertData("A", null, "C", null, output);
	}

	@Test
	public void testParentConverter()
	{
		final Data output = withGrantParentConverter.convert(input);

		assertData(null, "B", "C", null, output);
	}

	@Test
	public void testSimpleConverter()
	{
		final Data output = simpleConverter.convert(input);

		assertData("A", "B", "C", null, output);
	}

	@Test
	public void testMergedConverter()
	{
		final Data output = mergedConverter.convert(input);

		assertData(null, "B", "C", null, output);
	}

	@Test
	public void testListConverter()
	{
		final Data output = listConverter.convert(input);

		assertData("A", null, "C", null, output);
	}

	@Test
	public void testParetnWithAliasConverter()
	{
		Data output = parentAlias1.convert(input);
		assertData(null, "B", null, null, output);

		output = parentAlias3.convert(input);
		assertData(null, "B", "C", "D", output);
	}

	private void assertData(final String a, final String b, final String c, final String d, final Data actual)
	{
		Assert.assertNotNull(actual);
		Assert.assertEquals(a, actual.a);
		Assert.assertEquals(b, actual.b);
		Assert.assertEquals(c, actual.c);
		Assert.assertEquals(d, actual.d);
	}


	public static class Data
	{
		public Data()
		{
		}

		public Data(final String a, final String b, final String c, final String d)
		{
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}

		private String a;
		private String b;
		private String c;
		private String d;
	}


	public static class APopulator implements Populator<Data, Data>
	{
		@Override
		public void populate(final Data source, final Data target) throws ConversionException
		{
			target.a = source.a;
		}
	}

	public static class BPopulator implements Populator<Data, Data>
	{
		@Override
		public void populate(final Data source, final Data target) throws ConversionException
		{
			target.b = source.b;
		}
	}

	public static class CPopulator implements Populator<Data, Data>
	{
		@Override
		public void populate(final Data source, final Data target) throws ConversionException
		{
			target.c = source.c;
		}
	}

	public static class DPopulator implements Populator<Data, Data>
	{
		@Override
		public void populate(final Data source, final Data target) throws ConversionException
		{
			target.d = source.d;
		}
	}

}
