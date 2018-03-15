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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class CSVWriterTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(CSVWriterTest.class.getName());

	@Test
	public void testDefaultValues() throws Exception
	{
		final Writer writer = new PipedWriter();
		final CSVWriter csvw1 = new CSVWriter(writer);
		assertEquals(csvw1.getCommentchar(), '#');
		assertEquals(csvw1.getFieldseparator(), ';');
		assertEquals(csvw1.getTextseparator(), '"');
		csvw1.setCommentchar('a');
		csvw1.setFieldseparator('b');
		csvw1.setTextseparator('c');
		assertEquals(csvw1.getCommentchar(), 'a');
		assertEquals(csvw1.getFieldseparator(), 'b');
		assertEquals(csvw1.getTextseparator(), 'c');
	}

	@Test
	public void testEncodingNormal() throws Exception
	{
		final List original = createDataMap();
		final List should_be = removeEmptyMapFields(original);
		encoding_normal("UTF-8", original, should_be);
		encoding_normal("UTF-16", original, should_be);
		encoding_normal("UTF-16BE", original, should_be);
		encoding_normal("UTF-16LE", original, should_be);

		should_be.remove(5);
		original.remove(6);
		encoding_normal("ISO-8859-1", original, should_be);

		should_be.remove(3);
		original.remove(3);
		encoding_normal("US-ASCII", original, should_be);
	}

	@Test
	public void testEncodingWithComments() throws Exception
	{
		final List original = createDataMap();
		final List should_be = removeEmptyMapFields(original);
		encoding_withComment("UTF-8", original, should_be);
		encoding_withComment("UTF-16", original, should_be);
		encoding_withComment("UTF-16BE", original, should_be);
		encoding_withComment("UTF-16LE", original, should_be);

		should_be.remove(5);
		original.remove(6);
		encoding_withComment("ISO-8859-1", original, should_be);

		should_be.remove(3);
		original.remove(3);
		encoding_withComment("US-ASCII", original, should_be);
	}

	@Test
	public void testEncodingWithSpecialChars() throws Exception
	{
		final List original = createDataMap();
		final List should_be = removeEmptyMapFields(original);
		encoding_withStrangeCSVchars("UTF-8", original, should_be, '§', 'e', 'q');
		encoding_withStrangeCSVchars("UTF-16", original, should_be, '§', ',', 'q');
		encoding_withStrangeCSVchars("UTF-16BE", original, should_be, '§', ',', 'q');
		encoding_withStrangeCSVchars("UTF-16LE", original, should_be, '§', ',', 'q');

		should_be.remove(5);
		original.remove(6);
		encoding_withStrangeCSVchars("ISO-8859-1", original, should_be, '!', ',', 'q');

		should_be.remove(3);
		original.remove(3);
		encoding_withStrangeCSVchars("US-ASCII", original, should_be, '!', ',', 'q');
	}

	public void encoding_withStrangeCSVchars(final String encoding, final List original, final List should_be, final char comment,
			final char fieldseparator, final char textseparator) throws Exception
	{
		File tempfile = null;
		CSVWriter csvwriter = null;
		CSVReader csvreader = null;
		try
		{
			tempfile = File.createTempFile("unittest-csvwriter-withspecialchar-" + encoding, ".csv");
			csvwriter = new CSVWriter(tempfile, encoding);
			csvwriter.setCommentchar(comment);
			csvwriter.setFieldseparator(fieldseparator);
			csvwriter.setTextseparator(textseparator);

			for (int i = 0; i < original.size(); i++)
			{
				final Map line = (Map) original.get(i);
				csvwriter.writeComment(line.toString());
				csvwriter.write(line);
			}

			csvwriter.close();

			csvreader = new CSVReader(tempfile, encoding);
			csvreader.setCommentOut(new char[]
			{ comment });
			csvreader.setFieldSeparator(new char[]
			{ fieldseparator });
			csvreader.setTextSeparator(textseparator);
			final List readresult = new ArrayList();
			while (csvreader.readNextLine())
			{
				readresult.add(csvreader.getLine());
			}
			assertCollection(should_be, readresult);
			csvreader.close();

		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (csvwriter != null)
				{
					csvwriter.close();
				}
				if (csvreader != null)
				{
					csvreader.close();
				}
			}
			catch (final IOException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		if (tempfile != null)
		{
			tempfile.delete();
		}
	}

	public void encoding_withComment(final String encoding, final List original, final List should_be) throws Exception
	{
		File tempfile = null;
		CSVWriter csvwriter = null;
		CSVReader csvreader = null;
		try
		{
			tempfile = File.createTempFile("unittest-csvwriter-withComment-" + encoding + "__", ".csv");
			csvwriter = new CSVWriter(tempfile, encoding);

			for (int i = 0; i < original.size(); i++)
			{
				final Map line = (Map) original.get(i);
				csvwriter.writeComment(CSVUtils.escapeString(line.toString(), new char[]
				{ csvwriter.getTextseparator() }, true));
				csvwriter.write(line);
			}

			csvwriter.close();

			csvreader = new CSVReader(tempfile, encoding);
			final List readresult = new ArrayList();
			while (csvreader.readNextLine())
			{
				readresult.add(csvreader.getLine());
			}
			assertCollection(should_be, readresult);
			csvreader.close();

		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (csvwriter != null)
				{
					csvwriter.close();
				}
				if (csvreader != null)
				{
					csvreader.close();
				}
			}
			catch (final IOException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		if (tempfile != null)
		{
			tempfile.delete();
		}
	}

	public void encoding_normal(final String encoding, final List original, final List should_be) throws Exception
	{
		File tempfile = null;
		CSVWriter csvwriter = null;
		CSVReader csvreader = null;
		try
		{
			tempfile = File.createTempFile("unittest-csvwriter-" + encoding, ".csv");
			csvwriter = new CSVWriter(tempfile, encoding);
			csvwriter.write(original);
			csvwriter.close();

			csvreader = new CSVReader(tempfile, encoding);
			final List readresult = new ArrayList();
			while (csvreader.readNextLine())
			{
				readresult.add(csvreader.getLine());
			}
			assertCollection(should_be, readresult);
			csvreader.close();

		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (csvwriter != null)
				{
					csvwriter.close();
				}
				if (csvreader != null)
				{
					csvreader.close();
				}
			}
			catch (final IOException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
			}
		}
		if (tempfile != null)
		{
			tempfile.delete();
		}
	}

	private List removeEmptyMapFields(final List org)
	{
		final List ret = new ArrayList();
		for (int i = 0, s = org.size(); i < s; i++) // NOPMD
		{
			final Map line = ((Map) org.get(i));
			if (!line.equals(Collections.EMPTY_MAP))
			{
				ret.add(line);
			}
		}
		return ret;
	}

	private List createDataMap()
	{
		final List list = new ArrayList();

		final Map emptydaten = Collections.EMPTY_MAP;

		final Map datenzeile1 = new HashMap();
		datenzeile1.put(Integer.valueOf(0), "das");
		datenzeile1.put(Integer.valueOf(1), "ist");
		datenzeile1.put(Integer.valueOf(2), "ein");
		datenzeile1.put(Integer.valueOf(3), "header");


		final Map datenzeile2 = new HashMap();
		datenzeile2.put(Integer.valueOf(0), "eine");
		datenzeile2.put(Integer.valueOf(1), "Daten");
		datenzeile2.put(Integer.valueOf(2), "zeile");

		final Map datenzeile3 = new HashMap();
		datenzeile3.put(Integer.valueOf(0), "noch");
		datenzeile3.put(Integer.valueOf(1), "eine");
		datenzeile3.put(Integer.valueOf(2), "Daten");
		datenzeile3.put(Integer.valueOf(3), "zeile");

		final Map datenzeile4 = new HashMap();
		datenzeile4.put(Integer.valueOf(0), "ein paar sonderzeichen:");
		datenzeile4.put(Integer.valueOf(1), "\u00e4\u00f6\u00fc\u00df");
		datenzeile4.put(Integer.valueOf(2), "\"");
		datenzeile4.put(Integer.valueOf(3), "\\\\");

		final Map datenzeile5 = new HashMap();
		datenzeile5.put(Integer.valueOf(0), "eine");
		datenzeile5.put(Integer.valueOf(1), "Daten");
		datenzeile5.put(Integer.valueOf(2), "zeile");
		datenzeile5.put(Integer.valueOf(3), "nach");
		datenzeile5.put(Integer.valueOf(4), "einer");
		datenzeile5.put(Integer.valueOf(5), "Leer zeile");

		final Map datenzeile6 = new HashMap();
		datenzeile6.put(Integer.valueOf(0), "Eurosymbol:");
		datenzeile6.put(Integer.valueOf(1), "\u20ac");

		final Map datenzeile7 = new HashMap();
		datenzeile7.put(Integer.valueOf(0), "eine");
		datenzeile7.put(Integer.valueOf(1), "Daten");
		datenzeile7.put(Integer.valueOf(2), "zeile");
		datenzeile7.put(Integer.valueOf(3), "nach viel platz");

		list.add(datenzeile1);
		list.add(datenzeile2);
		list.add(datenzeile3);
		list.add(datenzeile4);
		list.add(emptydaten);
		list.add(datenzeile5);
		list.add(datenzeile6);
		list.add(emptydaten);
		list.add(emptydaten);
		list.add(emptydaten);
		list.add(emptydaten);
		list.add(emptydaten);
		list.add(datenzeile7);
		list.add(emptydaten);
		list.add(emptydaten);

		return list;
	}

	/**
	 * Tests writing of lines with containing escape characters.
	 */
	@Test
	public void testEscaping()
	{
		try
		{
			final StringWriter buffer = new StringWriter();
			final CSVWriter writer = new CSVWriter(buffer);
			final Map<Integer, String> line = new HashMap();
			line.put(Integer.valueOf(0), "bla\"bla");
			line.put(Integer.valueOf(1), "\"bla\"");
			line.put(Integer.valueOf(2), "bla\"\"bla");
			line.put(Integer.valueOf(3), "bla" + CSVConstants.DEFAULT_LINE_SEPARATOR + "bla");
			line.put(Integer.valueOf(4), CSVConstants.DEFAULT_LINE_SEPARATOR + "bla" + CSVConstants.DEFAULT_LINE_SEPARATOR + "bla"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR);
			line.put(Integer.valueOf(5), "bla\nbla");
			line.put(Integer.valueOf(6), "bla" + CSVConstants.DEFAULT_FIELD_SEPARATOR + "bla");
			writer.write(line);
			writer.close();
			buffer.close();
			final String expected =
			/* 0 */"\"bla\"\"bla\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
			/* 1 */+ "\"\"\"bla\"\"\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
			/* 2 */+ "\"bla\"\"\"\"bla\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
			/* 3 */+ "\"bla" + CSVConstants.DEFAULT_LINE_SEPARATOR + "bla\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
			/* 4 */+ "\"" + CSVConstants.DEFAULT_LINE_SEPARATOR + "bla" + CSVConstants.DEFAULT_LINE_SEPARATOR + "bla"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR + "\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
					/* 5 */+ "\"bla\nbla\"" + CSVConstants.DEFAULT_FIELD_SEPARATOR
					/* 6 */+ "\"bla" + CSVConstants.DEFAULT_FIELD_SEPARATOR + "bla\"" + CSVConstants.DEFAULT_LINE_SEPARATOR;
			assertEquals(expected, buffer.toString());
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
	}
}
