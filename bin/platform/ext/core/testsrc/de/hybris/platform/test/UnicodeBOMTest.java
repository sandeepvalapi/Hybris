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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.Parser;
import de.hybris.bootstrap.xml.UnicodeInputStream;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;


/**
 * Testing {@link UnicodeInputStream} and {@link de.hybris.bootstrap.xml.UnicodeReader}.
 * 
 * 
 */
@IntegrationTest
public class UnicodeBOMTest extends HybrisJUnit4TransactionalTest
{
	// Unicode Test
	// äöüß
	private static final String line1 = "Unicode Test";
	private static final String line2 = "\u00e4\u00f6\u00fc\u00df";

	// with BOM
	private final byte[] utf8BOMData =
	{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x55, 0x6E, 0x69, 0x63, 0x6F, 0x64, 0x65, 0x20, 0x54, 0x65, 0x73, 0x74, 0x0D, 0x0A,
			(byte) 0xC3, (byte) 0xA4, (byte) 0xC3, (byte) 0xB6, (byte) 0xC3, (byte) 0xBC, (byte) 0xC3, (byte) 0x9F };
	// without BOM
	private final byte[] utf8Data =
	{ 0x55, 0x6E, 0x69, 0x63, 0x6F, 0x64, 0x65, 0x20, 0x54, 0x65, 0x73, 0x74, 0x0D, 0x0A, (byte) 0xC3, (byte) 0xA4, (byte) 0xC3,
			(byte) 0xB6, (byte) 0xC3, (byte) 0xBC, (byte) 0xC3, (byte) 0x9F };

	// iso-8895-1
	private final byte[] isoLatinData =
	{ 0x55, 0x6E, 0x69, 0x63, 0x6F, 0x64, 0x65, 0x20, 0x54, 0x65, 0x73, 0x74, 0x0D, 0x0A, (byte) 0xE4, (byte) 0xF6, (byte) 0xFC,
			(byte) 0xDF };

	@Test
	public void testUnicodeStream() throws IOException
	{
		UnicodeInputStream is = new UnicodeInputStream(new ByteArrayInputStream(utf8BOMData), "utf-8");
		BufferedReader r = new BufferedReader(new InputStreamReader(is, is.getEncoding()));

		assertEquals(line1, r.readLine());
		assertEquals(line2, r.readLine());
		assertNull(r.readLine());

		is = new UnicodeInputStream(new ByteArrayInputStream(utf8Data), "utf-8");
		r = new BufferedReader(new InputStreamReader(is, is.getEncoding()));

		assertEquals(line1, r.readLine());
		assertEquals(line2, r.readLine());
		assertNull(r.readLine());

		is = new UnicodeInputStream(new ByteArrayInputStream(isoLatinData), "iso-8859-1");
		r = new BufferedReader(new InputStreamReader(is, is.getEncoding()));

		assertEquals(line1, r.readLine());
		assertEquals(line2, r.readLine());
		assertNull(r.readLine());

		r.close();
	}

	/*
	 * <?xml version="1.0" encoding="UTF-8"?> <test> <hallo> juhu ���� </hallo> </test>
	 */

	private final byte[] xmlUtf8BOMData =
	{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D,
			0x22, 0x31, 0x2e, 0x30, 0x22, 0x20, 0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67, 0x3d, 0x22, 0x55, 0x54, 0x46, 0x2d,
			0x38, 0x22, 0x3f, 0x3e, 0x0d, 0x0a, 0x3c, 0x74, 0x65, 0x73, 0x74, 0x3e, 0x0d, 0x0a, 0x3c, 0x68, 0x61, 0x6c, 0x6c, 0x6f,
			0x3e, 0x0d, 0x0a, 0x6a, 0x75, 0x68, 0x75, 0x20, (byte) 0xc3, (byte) 0xa4, (byte) 0xc3, (byte) 0xb6, (byte) 0xc3,
			(byte) 0xbc, (byte) 0xc3, (byte) 0x9f, 0x0d, 0x0a, 0x3c, 0x2f, 0x68, 0x61, 0x6c, 0x6c, 0x6f, 0x3e, 0x0d, 0x0a, 0x3c,
			0x2f, 0x74, 0x65, 0x73, 0x74, 0x3e };
	private final byte[] xmlUtf8Data =
	{ 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2e, 0x30, 0x22, 0x20,
			0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67, 0x3d, 0x22, 0x55, 0x54, 0x46, 0x2d, 0x38, 0x22, 0x3f, 0x3e, 0x0d, 0x0a,
			0x3c, 0x74, 0x65, 0x73, 0x74, 0x3e, 0x0d, 0x0a, 0x3c, 0x68, 0x61, 0x6c, 0x6c, 0x6f, 0x3e, 0x0d, 0x0a, 0x6a, 0x75, 0x68,
			0x75, 0x20, (byte) 0xc3, (byte) 0xa4, (byte) 0xc3, (byte) 0xb6, (byte) 0xc3, (byte) 0xbc, (byte) 0xc3, (byte) 0x9f,
			0x0d, 0x0a, 0x3c, 0x2f, 0x68, 0x61, 0x6c, 0x6c, 0x6f, 0x3e, 0x0d, 0x0a, 0x3c, 0x2f, 0x74, 0x65, 0x73, 0x74, 0x3e };

	/*
	 * <?xml version="1.0" encoding="ISO-8859-1"?> <test> <hallo> juhu ���� </hallo> </test>
	 */
	private final byte[] xmlLatinData =
	{ 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6F, 0x6E, 0x3D, 0x22, 0x31, 0x2e, 0x30, 0x22, 0x20,
			0x65, 0x6e, 0x63, 0x6f, 0x64, 0x69, 0x6e, 0x67, 0x3d, 0x22, 0x49, 0x53, 0x4F, 0x2D, 0x38, 0x38, 0x35, 0x39, 0x2d, 0x31,
			0x22, 0x3f, 0x3e, 0x0d, 0x0a, 0x3c, 0x74, 0x65, 0x73, 0x74, 0x3e, 0x0d, 0x0a, 0x3c, 0x68, 0x61, 0x6c, 0x6c, 0x6f, 0x3e,
			0x0d, 0x0a, 0x6a, 0x75, 0x68, 0x75, 0x20, (byte) 0xe4, (byte) 0xf6, (byte) 0xfc, (byte) 0xdf, 0x0d, 0x0a, 0x3c, 0x2f,
			0x68, 0x61, 0x6c, 0x6c, 0x6f, 0x3e, 0x0d, 0x0a, 0x3c, 0x2f, 0x74, 0x65, 0x73, 0x74, 0x3e };

	@Test
	public void testUnicodeXMLParsing() throws ParseAbortException
	{
		Parser p = new Parser(null, null);
		p.parse(new ByteArrayInputStream(xmlUtf8BOMData), "utf-8", new DefaultTagListener()
		{
			@Override
			protected Collection createSubTagListeners()
			{
				return Collections.singleton(new DefaultTagListener()
				{
					@Override
					protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
					{
						return getCharacters();
					}

					@Override
					public String getTagName()
					{
						return "hallo";
					}
				});
			}

			@Override
			protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
			{
				final String msg = (String) getSubTagValue("hallo");
				assertEquals("juhu " + line2, msg);
				return null;
			}

			@Override
			public String getTagName()
			{
				return "test";
			}
		});

		p = new Parser(null, null);
		p.parse(new ByteArrayInputStream(xmlUtf8Data), "utf-8", new DefaultTagListener()
		{
			@Override
			protected Collection createSubTagListeners()
			{
				return Collections.singleton(new DefaultTagListener()
				{
					@Override
					protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
					{
						return getCharacters();
					}

					@Override
					public String getTagName()
					{
						return "hallo";
					}
				});
			}

			@Override
			protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
			{
				final String msg = (String) getSubTagValue("hallo");
				assertEquals("juhu " + line2, msg);
				return null;
			}

			@Override
			public String getTagName()
			{
				return "test";
			}
		});

		p = new Parser(null, null);
		p.parse(new ByteArrayInputStream(xmlLatinData), "iso-8859-1", new DefaultTagListener()
		{
			@Override
			protected Collection createSubTagListeners()
			{
				return Collections.singleton(new DefaultTagListener()
				{
					@Override
					protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
					{
						return getCharacters();
					}

					@Override
					public String getTagName()
					{
						return "hallo";
					}
				});
			}

			@Override
			protected Object processEndElement(final ObjectProcessor processor) throws ParseAbortException
			{
				final String msg = (String) getSubTagValue("hallo");
				assertEquals("juhu " + line2, msg);
				return null;
			}

			@Override
			public String getTagName()
			{
				return "test";
			}
		});
	}

}
