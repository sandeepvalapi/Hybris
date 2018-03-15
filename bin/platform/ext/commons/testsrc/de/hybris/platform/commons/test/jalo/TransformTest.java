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
package de.hybris.platform.commons.test.jalo;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


@IntegrationTest
public class TransformTest extends HybrisJUnit4TransactionalTest
{
	private InputStream getStream(final String fileName) throws Exception
	{
		return TransformTest.class.getResourceAsStream(fileName);
	}

	private String getStringFromStream(final String fileName) throws Exception
	{
		return getStringFromStream(getStream(fileName));
	}

	private String getStringFromStream(final InputStream inputStream) throws Exception
	{
		final InputStreamReader isr = new InputStreamReader(inputStream);
		final BufferedReader buffr = new BufferedReader(isr);
		final StringBuffer output = new StringBuffer();

		for (String line = buffr.readLine(); line != null; line = buffr.readLine())
		{
			output.append(line + "\n");
		}
		return output.toString();
	}

	@Test
	public void testSmallTest() throws Exception
	{
		final String html = getStringFromStream("/commons/unittest/simpleContent.html");
		final InputStream htmlStream = getStream("/commons/unittest/simpleContent.html");
		final InputStream xslStream = getStream("/commons/unittest/simpleTransformation.xsl");
		final InputStream rereadXslStream = getStream("/commons/unittest/simpleTransformation.xsl");
		try
		{
			final String stringStringResult = CommonsManager.getInstance().transform(html, xslStream, true);
			final String streamStringResult = CommonsManager.getInstance().transform(htmlStream, rereadXslStream, true);
			assertEquals(streamStringResult, stringStringResult);
		}
		finally
		{
			IOUtils.closeQuietly(htmlStream);
			IOUtils.closeQuietly(xslStream);
			IOUtils.closeQuietly(rereadXslStream);
		}
	}
}
