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
package de.hybris.platform.servicelayer.converter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.dto.converter.GenericConverter;

import org.junit.Test;


@UnitTest
public class GenericConverterTest
{

	public class Image
	{
		private String url;

		public Image(final String url)
		{
			super();
			this.url = url;
		}

		public String getUrl()
		{
			return url;
		}

		public void setUrl(final String url)
		{
			this.url = url;
		}


	}

	public class Test1
	{

		private String name;
		private int count;
		private Image image;

		public String getName()
		{
			return name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}

		public int getCount()
		{
			return count;
		}

		public void setCount(final int count)
		{
			this.count = count;
		}

		public Image getImage()
		{
			return image;
		}

		public void setImage(final Image image)
		{
			this.image = image;
		}

	}

	public class Test2
	{

		private String name;
		private int count;
		private String image;

		public String getName()
		{
			return name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}

		public int getCount()
		{
			return count;
		}

		public void setCount(final int count)
		{
			this.count = count;
		}

		public String getImage()
		{
			return image;
		}

		public void setImage(final String image)
		{
			this.image = image;
		}

	}

	@Test
	public void testConvert()
	{
		final Test1 test1 = new Test1();
		test1.setCount(10);
		test1.setImage(new Image("hallo,jpg"));
		test1.setName("name");
		final Converter<Test1, Test2> converter = new GenericConverter<Test1, Test2>();
		final Test2 test2 = converter.convert(test1, new Test2());
		assertNotNull("Conversion result", test2);
		assertEquals("Count", test1.getCount(), test2.getCount());
		assertEquals("Name", test1.getName(), test2.getName());
		assertNull("Image", test2.getImage());
	}
}
