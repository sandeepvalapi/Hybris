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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.FixedLengthCSVReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;


@IntegrationTest
public class CSVReaderTest extends HybrisJUnit4TransactionalTest
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Test
	public void testMultiLines()
	{
		CSVReader r = new CSVReader("1a;1b;1c\n" + "   2a;2b;  2c_part1\\  \n" + "   _2c_part2 \\\n"
				+ "_2c_part3  ;  2d;  2e_part1_  \\ \n" + "\t    \t 2e_end   ; 2f \n");
		r.setMultiLineMode(true);
		assertTrue(r.readNextLine());
		assertEquals(1, r.getCurrentLineNumber());
		Map line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("1a", line.get(Integer.valueOf(0)));
		assertEquals("1b", line.get(Integer.valueOf(1)));
		assertEquals("1c", line.get(Integer.valueOf(2)));
		assertTrue(r.readNextLine());
		assertEquals(2, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(6, line.size());
		assertEquals("2a", line.get(Integer.valueOf(0)));
		assertEquals("2b", line.get(Integer.valueOf(1)));
		assertEquals("2c_part1   _2c_part2 _2c_part3", line.get(Integer.valueOf(2)));
		assertEquals("2d", line.get(Integer.valueOf(3)));
		assertEquals("2e_part1_  \t    \t 2e_end", line.get(Integer.valueOf(4)));
		assertEquals("2f", line.get(Integer.valueOf(5)));
		assertFalse(r.readNextLine());

		r = new CSVReader("1a;1b;1c\n" + "2a;2b;2c_part1\\  \n" + "_2c_part2 \\\n" + "_2c_part3;2d;2e_part1_  \\ \n"
				+ "\t    \t 2e_end; 2f \n");
		r.setMultiLineMode(false);
		assertTrue(r.readNextLine());
		assertEquals(1, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("1a", line.get(Integer.valueOf(0)));
		assertEquals("1b", line.get(Integer.valueOf(1)));
		assertEquals("1c", line.get(Integer.valueOf(2)));
		assertTrue(r.readNextLine());
		assertEquals(2, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("2a", line.get(Integer.valueOf(0)));
		assertEquals("2b", line.get(Integer.valueOf(1)));
		assertEquals("2c_part1\\", line.get(Integer.valueOf(2)));
		assertTrue(r.readNextLine());
		assertEquals(3, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(1, line.size());
		assertEquals("_2c_part2 \\", line.get(Integer.valueOf(0)));
		assertTrue(r.readNextLine());
		assertEquals(4, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("_2c_part3", line.get(Integer.valueOf(0)));
		assertEquals("2d", line.get(Integer.valueOf(1)));
		assertEquals("2e_part1_  \\", line.get(Integer.valueOf(2)));
		assertTrue(r.readNextLine());
		assertEquals(5, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(2, line.size());
		assertEquals("2e_end", line.get(Integer.valueOf(0)));
		assertEquals("2f", line.get(Integer.valueOf(1)));
		assertFalse(r.readNextLine());
	}

	@Test
	public void testUnquoted()
	{
		final CSVReader csvreader = new CSVReader(new StringReader(
				"cell 1 ; cell 2 with \" included ; \" quoted \"\" cell \" ; cell 4 with trailing \"" + LINE_SEPARATOR
						+ "aaa;bbb;ccc;ddd;;;"));
		// 1
		assertTrue(csvreader.readNextLine());
		final Map line = new HashMap();
		line.put(Integer.valueOf(0), "cell 1");
		line.put(Integer.valueOf(1), "cell 2 with \" included");
		line.put(Integer.valueOf(2), "quoted \" cell");
		line.put(Integer.valueOf(3), "cell 4 with trailing \"");
		compareLines(line, csvreader.getLine());
		// 2
		assertTrue(csvreader.readNextLine());
		line.put(Integer.valueOf(0), "aaa");
		line.put(Integer.valueOf(1), "bbb");
		line.put(Integer.valueOf(2), "ccc");
		line.put(Integer.valueOf(3), "ddd");
		line.put(Integer.valueOf(4), "");
		line.put(Integer.valueOf(5), "");
		line.put(Integer.valueOf(6), "");
		compareLines(line, csvreader.getLine());
		assertFalse(csvreader.readNextLine());
	}

	/**
	 * Tests the usage of separators in several extreme cases like consecutively or at start or end of line. As separatot
	 * the tab is used.
	 */
	@Test
	public void testMultipleTabsAsSeparator()
	{
		final CSVReader csvreader = new CSVReader(new StringReader("abc\tdef\t\txyz\n\t\tbla\t"));
		csvreader.setFieldSeparator(new char[]
		{ '\t' });
		// 1
		assertTrue(csvreader.readNextLine());
		final Map line = new HashMap();
		line.put(Integer.valueOf(0), "abc");
		line.put(Integer.valueOf(1), "def");
		line.put(Integer.valueOf(2), "");
		line.put(Integer.valueOf(3), "xyz");
		compareLines(line, csvreader.getLine());
		// 2
		assertTrue(csvreader.readNextLine());
		line.put(Integer.valueOf(0), "");
		line.put(Integer.valueOf(1), "");
		line.put(Integer.valueOf(2), "bla");
		line.put(Integer.valueOf(3), "");
		compareLines(line, csvreader.getLine());
		assertFalse(csvreader.readNextLine());
	}

	@Test
	public void testEmptyFields()
	{
		final CSVReader csvreader = new CSVReader(new StringReader(";;"));
		assertTrue(csvreader.readNextLine());
		final Map line = new HashMap();
		line.put(Integer.valueOf(0), "");
		line.put(Integer.valueOf(1), "");
		line.put(Integer.valueOf(2), "");
		assertEquals(line, csvreader.getLine());
	}

	@Test
	public void testWithFile_exceltest1() throws Exception
	{
		final String data = "A1;B1;C1" + LINE_SEPARATOR + "\"A2" + LINE_SEPARATOR + "und text\";\"B2" + LINE_SEPARATOR
				+ LINE_SEPARATOR + "und mehr Text\";\"C2" + LINE_SEPARATOR + "bissl text\"" + LINE_SEPARATOR + "A3;B3;C3"
				+ LINE_SEPARATOR + "\"A4" + LINE_SEPARATOR + LINE_SEPARATOR + "moo\";B4;C4" + LINE_SEPARATOR + "A5;B5;\"C5"
				+ LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + "huhu" + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + "\"";
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "A1");
		line1.put(Integer.valueOf(1), "B1");
		line1.put(Integer.valueOf(2), "C1");
		final Map line2 = new HashMap();
		line2.put(Integer.valueOf(0), "A2" + LINE_SEPARATOR + "und text");
		line2.put(Integer.valueOf(1), "B2" + LINE_SEPARATOR + LINE_SEPARATOR + "und mehr Text");
		line2.put(Integer.valueOf(2), "C2" + LINE_SEPARATOR + "bissl text");
		final Map line3 = new HashMap();
		line3.put(Integer.valueOf(0), "A3");
		line3.put(Integer.valueOf(1), "B3");
		line3.put(Integer.valueOf(2), "C3");
		final Map line4 = new HashMap();
		line4.put(Integer.valueOf(0), "A4" + LINE_SEPARATOR + LINE_SEPARATOR + "moo");
		line4.put(Integer.valueOf(1), "B4");
		line4.put(Integer.valueOf(2), "C4");
		final Map line5 = new HashMap();
		line5.put(Integer.valueOf(0), "A5");
		line5.put(Integer.valueOf(1), "B5");
		line5.put(Integer.valueOf(2), "C5" + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + "huhu");
		final CSVReader csvreader = new CSVReader(new StringReader(data));
		// 1
		assertTrue(csvreader.readNextLine());
		compareLines(line1, csvreader.getLine());
		// 2
		assertTrue(csvreader.readNextLine());
		compareLines(line2, csvreader.getLine());
		// 3
		assertTrue(csvreader.readNextLine());
		compareLines(line3, csvreader.getLine());
		// 4
		assertTrue(csvreader.readNextLine());
		compareLines(line4, csvreader.getLine());
		// 5
		assertTrue(csvreader.readNextLine());
		compareLines(line5, csvreader.getLine());
		assertFalse(csvreader.readNextLine());
	}

	private void compareLines(final Map line1, final Map line2)
	{
		assertEquals(line1.size(), line2.size());
		for (final Iterator iter = line1.entrySet().iterator(); iter.hasNext();)
		{
			final Map.Entry e = (Map.Entry) iter.next();
			final Integer key = (Integer) e.getKey();
			final String value1 = (String) e.getValue();
			final String value2 = (String) line2.get(key);
			assertEquals("'" + value1.replace(' ', '.') + "' != '" + value2.replace(' ', '.') + "' at cell " + key + " for lines "
					+ line1 + " and " + line2, value1, value2);
		}
	}

	@Test
	public void testWithFile_exceltest2() throws Exception
	{
		final String lines = "A1;" + "\"B1;\";" + "C1" + LINE_SEPARATOR + "\"A2\"\";" + LINE_SEPARATOR + "und text\";" + "\"B2"
				+ LINE_SEPARATOR + LINE_SEPARATOR + "und mehr Text\";" + "\"C2" + LINE_SEPARATOR + "bissl text\"" + LINE_SEPARATOR
				+ "A3;" + "B3;" + "\"C3\"\"\"" + LINE_SEPARATOR + "\"A4" + LINE_SEPARATOR + LINE_SEPARATOR + "moo\";" + "B4;" + "C4"
				+ LINE_SEPARATOR + "A5;" + "B5;" + "\"C5" + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + "huhu"
				+ LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + ";;;\"";
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "A1");
		line1.put(Integer.valueOf(1), "B1;");
		line1.put(Integer.valueOf(2), "C1");
		final Map line2 = new HashMap();
		line2.put(Integer.valueOf(0), "A2\";" + LINE_SEPARATOR + "und text");
		line2.put(Integer.valueOf(1), "B2" + LINE_SEPARATOR + LINE_SEPARATOR + "und mehr Text");
		line2.put(Integer.valueOf(2), "C2" + LINE_SEPARATOR + "bissl text");
		final Map line3 = new HashMap();
		line3.put(Integer.valueOf(0), "A3");
		line3.put(Integer.valueOf(1), "B3");
		line3.put(Integer.valueOf(2), "C3\"");
		final Map line4 = new HashMap();
		line4.put(Integer.valueOf(0), "A4" + LINE_SEPARATOR + LINE_SEPARATOR + "moo");
		line4.put(Integer.valueOf(1), "B4");
		line4.put(Integer.valueOf(2), "C4");
		final Map line5 = new HashMap();
		line5.put(Integer.valueOf(0), "A5");
		line5.put(Integer.valueOf(1), "B5");
		line5.put(Integer.valueOf(2), "C5" + LINE_SEPARATOR + LINE_SEPARATOR + LINE_SEPARATOR + "huhu" + LINE_SEPARATOR
				+ LINE_SEPARATOR + LINE_SEPARATOR + ";;;");
		final List soll = new ArrayList();
		soll.add(line1);
		soll.add(line2);
		soll.add(line3);
		soll.add(line4);
		soll.add(line5);
		final CSVReader csvreader = new CSVReader(lines);
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		assertCollection(soll, ist);
	}

	@Test
	public void testWithFile_ms_exceltest1() throws Exception
	{
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "m\u00F6\u00F6");
		line1.put(Integer.valueOf(1), "m\u00E4\u00E4");
		line1.put(Integer.valueOf(2), "m\u00FC\u00FC");
		final Map line2 = new HashMap();
		line2.put(Integer.valueOf(0), "asd" + LINE_SEPARATOR + "asd" + LINE_SEPARATOR + "asd");
		line2.put(Integer.valueOf(1), "qwe" + LINE_SEPARATOR + "qwe");
		line2.put(Integer.valueOf(2), "yxc");
		final Map line3 = new HashMap();
		line3.put(Integer.valueOf(0), "m\u00F6\u00F6");
		line3.put(Integer.valueOf(1), "m\u00E4\u00E4");
		line3.put(Integer.valueOf(2), "m\u00FC\u00FC");
		final Map line4 = new HashMap();
		line4.put(Integer.valueOf(0), "bbb");
		line4.put(Integer.valueOf(1), "ccc");
		line4.put(Integer.valueOf(2), "a" + LINE_SEPARATOR + "a" + LINE_SEPARATOR + "a");
		final List soll = new ArrayList();
		soll.add(line1);
		soll.add(line2);
		soll.add(line3);
		soll.add(line4);
		final String filename = "/core/unittest/ms-exceltest1_cp1252.csv";
		final InputStream is = CSVWriter.class.getResourceAsStream(filename);
		final CSVReader csvreader = new CSVReader(is, "Cp1252");
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		assertCollection(soll, ist);
	}

	@Test
	public void test_excel_with_showcommands()
	{
		final String thefile = "test1;test1;test1" + LINE_SEPARATOR + "\"#test11" + LINE_SEPARATOR + "test11\";test11;\"test11"
				+ LINE_SEPARATOR + "test11\"";
		final CSVReader csvreader = new CSVReader(thefile);
		csvreader.setShowComments(true);
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "test1");
		line1.put(Integer.valueOf(1), "test1");
		line1.put(Integer.valueOf(2), "test1");
		final Map line2 = new HashMap();
		line2.put(Integer.valueOf(0), "#test11" + LINE_SEPARATOR + "test11");
		line2.put(Integer.valueOf(1), "test11");
		line2.put(Integer.valueOf(2), "test11" + LINE_SEPARATOR + "test11");
		final List soll = new ArrayList();
		soll.add(line1);
		soll.add(line2);
		assertCollection(soll, ist);
	}

	@Test
	public void test_excel_without_showcommands()
	{
		final String thefile = "test1;test1;test1" + LINE_SEPARATOR + "\"#test11" + LINE_SEPARATOR + "test11\";test11;\"test11"
				+ LINE_SEPARATOR + "test11\"";
		final CSVReader csvreader = new CSVReader(thefile);
		csvreader.setShowComments(false);
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "test1");
		line1.put(Integer.valueOf(1), "test1");
		line1.put(Integer.valueOf(2), "test1");
		final List soll = new ArrayList();
		soll.add(line1);
		assertCollection(soll, ist);
	}

	@Test
	public void test_oo_with_showcommands()
	{
		final String thefile = "\"test2\";\"test2\";\"test2\"" + LINE_SEPARATOR + "\"#test22" + LINE_SEPARATOR
				+ "test22\";\"test22\";\"test22" + LINE_SEPARATOR + "test22\"";
		final CSVReader csvreader = new CSVReader(thefile);
		csvreader.setShowComments(true);
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "test2");
		line1.put(Integer.valueOf(1), "test2");
		line1.put(Integer.valueOf(2), "test2");
		final Map line2 = new HashMap();
		line2.put(Integer.valueOf(0), "#test22" + LINE_SEPARATOR + "test22");
		line2.put(Integer.valueOf(1), "test22");
		line2.put(Integer.valueOf(2), "test22" + LINE_SEPARATOR + "test22");
		final List soll = new ArrayList();
		soll.add(line1);
		soll.add(line2);
		assertCollection(soll, ist);
	}

	@Test
	public void test_oo_without_showcommands()
	{
		final String thefile = "\"test2\";\"test2\";\"test2\"" + LINE_SEPARATOR + "\"#test22" + LINE_SEPARATOR
				+ "test22\";\"test22\";\"test22" + LINE_SEPARATOR + "test22\"";
		final CSVReader csvreader = new CSVReader(thefile);
		csvreader.setShowComments(false);
		final List ist = new ArrayList();
		while (csvreader.readNextLine())
		{
			ist.add(csvreader.getLine());
		}
		final Map line1 = new HashMap();
		line1.put(Integer.valueOf(0), "test2");
		line1.put(Integer.valueOf(1), "test2");
		line1.put(Integer.valueOf(2), "test2");
		final List soll = new ArrayList();
		soll.add(line1);
		assertCollection(soll, ist);
	}

	@Test
	public void testUTF8BOM_CORE_4070()
	{
		final String expected = "\u00E4\u00FC\u00F6\u00DF";
		final byte[] utf8_BOM_data = new byte[]
		{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, // BOM
				(byte) 0xC3, (byte) 0xA4, // ae
				(byte) 0xC3, (byte) 0xBC, // ue
				(byte) 0xC3, (byte) 0xB6, // oe
				(byte) 0xC3, (byte) 0x9F // sz
		};
		try
		{
			final CSVReader r = new CSVReader(new ByteArrayInputStream(utf8_BOM_data), "utf-8");
			assertTrue(r.readNextLine());
			final Map line = r.getLine();
			assertNotNull(line);
			assertEquals(expected, line.get(Integer.valueOf(0)));
			assertFalse(r.readNextLine());
		}
		catch (final UnsupportedEncodingException e)
		{
			fail(e.getMessage());
		}
	}

	/**
	 * Tests the FixedLengthCVSReader.
	 */
	@Test
	public void testFixedLength()
	{
		final String testString = "wert1wert2foowert3\n" + "wert4wert5foowert6";
		final FixedLengthCSVReader r = new FixedLengthCSVReader(testString);
		r.addField(0, 4, 0);
		r.addField(5, 9, 1);
		r.addField(13, 17, 3);
		try
		{
			r.addField(24, 36, 0);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			// OK
		}
		try
		{
			r.addField(10, 13, 4);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			// OK
		}
		try
		{
			r.addField(6, 12, 4);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			// OK
		}
		try
		{
			r.addField(6, 8, 4);
			fail();
		}
		catch (final IllegalArgumentException e)
		{
			// OK
		}
		assertTrue(r.readNextLine());
		Map line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("wert1", line.get(Integer.valueOf(0)));
		assertEquals("wert2", line.get(Integer.valueOf(1)));
		assertEquals(null, line.get(Integer.valueOf(2)));
		assertEquals("wert3", line.get(Integer.valueOf(3)));
		assertTrue(r.readNextLine());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("wert4", line.get(Integer.valueOf(0)));
		assertEquals("wert5", line.get(Integer.valueOf(1)));
		assertEquals("wert6", line.get(Integer.valueOf(3)));
		assertFalse(r.readNextLine());
	}

	@Test
	public void testSkippingOfLines()
	{
		CSVReader r = new CSVReader("\"a\";" + "\"b\";" + "\"c\"\n" + "\"d\";" + "\"e\";" + "\"f\"\n");
		r.setLinesToSkip(1);
		assertTrue(r.readNextLine());
		assertEquals(2, r.getCurrentLineNumber());
		Map line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("d", line.get(Integer.valueOf(0)));
		assertEquals("e", line.get(Integer.valueOf(1)));
		assertEquals("f", line.get(Integer.valueOf(2)));
		assertFalse(r.readNextLine());

		r = new CSVReader("\"a\n\";" + "\"b\";" + "\"c\"\n" + "\"d\";" + "\"e\";" + "\"f\"\n");
		r.setLinesToSkip(1);
		assertTrue(r.readNextLine());
		assertEquals(2, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("d", line.get(Integer.valueOf(0)));
		assertEquals("e", line.get(Integer.valueOf(1)));
		assertEquals("f", line.get(Integer.valueOf(2)));
		assertFalse(r.readNextLine());

		r = new CSVReader("\"a\";" + "\"b\";\\\n" + "\"c\"\n" + "\"d\";" + "\"e\";" + "\"f\"\n");
		r.setMultiLineMode(true);
		r.setLinesToSkip(1);
		assertTrue(r.readNextLine());
		assertEquals(2, r.getCurrentLineNumber());
		line = r.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("d", line.get(Integer.valueOf(0)));
		assertEquals("e", line.get(Integer.valueOf(1)));
		assertEquals("f", line.get(Integer.valueOf(2)));
		assertFalse(r.readNextLine());

		r = new CSVReader("\n" + "a\n" + "\n" + "b\n");
		r.setLinesToSkip(2);
		assertTrue(r.readNextLine());
		assertEquals(4, r.getCurrentLineNumber());
		line = r.getLine();
		assertEquals("b", line.get(Integer.valueOf(0)));
		assertFalse(r.readNextLine());

		r = new CSVReader("\n");
		r.setLinesToSkip(2);
		assertFalse(r.readNextLine());
	}
}
