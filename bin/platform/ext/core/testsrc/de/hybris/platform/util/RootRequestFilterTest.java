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
package de.hybris.platform.util;

import static org.easymock.classextension.EasyMock.createMock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.RootRequestFilter.HybrisGZIPResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.junit.Test;


@UnitTest
public class RootRequestFilterTest
{

	private final RootRequestFilter filter = new RootRequestFilter();

	@Test
	public void testPLA8553() throws IOException, ServletException
	{
		final HttpServletResponse responseMock = createMock(HttpServletResponse.class);

		final HybrisGZIPResponseWrapper wrapper = filter.new HybrisGZIPResponseWrapper(responseMock, true, true)
		{

			@Override
			public ServletOutputStream createOutputStream() throws IOException
			{
				return new MyServletOutputStream();
			}
		};

		final ServletOutputStream stream1 = wrapper.getOutputStream();
		stream1.println("sample_one");
		final PrintWriter writer1 = wrapper.getWriter();//writer overwrites the stream


		final ServletOutputStream stream2 = wrapper.getOutputStream();
		stream2.println("sample_one");
		Assert.assertTrue("Initially created stream should be MyServletOutputStream but was " + stream1.getClass(),
				stream1 instanceof MyServletOutputStream);
		Assert.assertTrue("Initially created stream should be MyServletOutputStream but was " + stream2.getClass(),
				stream2 instanceof MyServletOutputStream);
		Assert.assertTrue("Output streams should have the same content ", ((MyServletOutputStream) stream1).getPrintlnMethod()
				.equals(((MyServletOutputStream) stream2).getPrintlnMethod()));
		Assert.assertTrue("Output streams should be the same ", stream1.equals(stream2));

		final PrintWriter writer2 = wrapper.getWriter();//writer overwrites the stream again
		Assert.assertTrue("Printwriter should be the same ", writer1.equals(writer2));
	}



	private class MyServletOutputStream extends ServletOutputStream
	{
		@SuppressWarnings("unused")
		private int writeMethod;

		private String printlnMethod;

		@Override
		public void write(final int idx) throws IOException
		{
			this.writeMethod = idx;
		}

		@Override
		public void println(final String str) throws IOException
		{
			this.printlnMethod = str;
		}


		public String getPrintlnMethod()
		{
			return printlnMethod;
		}


	}

}
