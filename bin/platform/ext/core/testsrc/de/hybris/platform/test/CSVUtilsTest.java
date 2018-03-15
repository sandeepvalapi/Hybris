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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.CSVUtils;

import org.apache.log4j.Logger;
import org.junit.Test;


@UnitTest
public class CSVUtilsTest
{
	private static final Logger LOG = Logger.getLogger(CSVUtilsTest.class.getName()); //NOPMD

	private final char[] value1 = new char[]
	{ '"' };
	private final char[] value2 = new char[]
	{ '"', ';' };
	private final char[] value3 = new char[]
	{ '\\' };
	private final char[] value4 = new char[]
	{ '"', ';', '\n' };

	private final String testString1 = "abcdef"; // abcdef
	private final String testString2 = ""; //
	private final String testString3 = null; // null
	private final String testString4 = "\"abc\""; // "abc"
	private final String testString5 = "a\"b\"c"; // a"b"c
	private final String testString6 = "ab\"\"c"; // ab""c
	private final String testString7 = ";\";"; // ;";
	private final String testString8 = ";;;;\"\""; // ;;;;""
	private final String testString9 = "\\;\\\""; // \;\"
	private final String testString10 = "abc\\def\";ab\";;\"\"gh\\\\\"\"\\"; // abc\def";ab";;""gh\\""\
	private final String testString11 = ";abc;"; //;abc;

	@Test
	public void testCSVUtils() throws Exception
	{
		StringBuilder buf = null;

		//	 abcdef
		assertFalse(CSVUtils.escapeString(buf = new StringBuilder(testString1), value2, value4, true));
		assertEquals(testString1, buf.toString());
		assertFalse(CSVUtils.escapeString(buf = new StringBuilder(testString1), value2, value4, false));
		assertEquals(testString1, buf.toString());
		assertTrue(testString1.equals(CSVUtils.escapeString(testString1, value2, true)));
		assertTrue(testString1.equals(CSVUtils.escapeString(testString1, value2, false)));
		assertTrue(testString1.equals(CSVUtils.unescapeString(testString1, value2, true)));
		assertTrue(testString1.equals(CSVUtils.unescapeString(testString1, value2, false)));

		//
		assertFalse(CSVUtils.escapeString(buf = new StringBuilder(testString2), value2, value4, true));
		assertEquals(testString2, buf.toString());
		assertFalse(CSVUtils.escapeString(buf = new StringBuilder(testString2), value2, value4, false));
		assertEquals(testString2, buf.toString());
		assertTrue(testString2.equals(CSVUtils.escapeString(testString2, value2, true)));
		assertTrue(testString2.equals(CSVUtils.escapeString(testString2, value2, false)));
		assertTrue(testString2.equals(CSVUtils.unescapeString(testString2, value2, true)));
		assertTrue(testString2.equals(CSVUtils.unescapeString(testString2, value2, false)));

		// null
		assertNull(CSVUtils.escapeString(testString3, value2, true));
		assertNull(CSVUtils.escapeString(testString3, value2, false));
		assertNull(CSVUtils.unescapeString(testString3, value2, true));
		assertNull(CSVUtils.unescapeString(testString3, value2, false));

		// "abc"
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString4), value1, value4, true));
		assertEquals("\"\"abc\"\"", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString4), value1, value4, false));
		assertEquals("\\\"abc\\\"", buf.toString());
		assertTrue(CSVUtils.escapeString(testString4, value1, true).equals("\"\"abc\"\""));
		assertTrue(CSVUtils.unescapeString(testString4, value1, true).equals(testString4));
		assertTrue(CSVUtils.escapeString(testString4, value2, true).equals("\"\"abc\"\""));
		assertTrue(CSVUtils.unescapeString(testString4, value2, true).equals(testString4));
		assertTrue(CSVUtils.escapeString(testString4, value3, true).equals(testString4));
		assertTrue(CSVUtils.unescapeString(testString4, value3, true).equals(testString4));
		assertTrue(CSVUtils.escapeString(testString4, value1, false).equals("\\\"abc\\\""));
		assertTrue(CSVUtils.unescapeString(testString4, value1, false).equals(testString4));
		assertTrue(CSVUtils.escapeString(testString4, value2, false).equals("\\\"abc\\\""));
		assertTrue(CSVUtils.unescapeString(testString4, value2, false).equals(testString4));
		assertTrue(CSVUtils.escapeString(testString4, value3, false).equals(testString4));
		assertTrue(CSVUtils.unescapeString(testString4, value3, false).equals(testString4));

		//	 a"b"c
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString5), value1, value4, true));
		assertEquals("a\"\"b\"\"c", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString5), value1, value4, false));
		assertEquals("a\\\"b\\\"c", buf.toString());
		assertTrue(CSVUtils.escapeString(testString5, value1, true).equals("a\"\"b\"\"c"));
		assertTrue(CSVUtils.unescapeString(testString5, value1, true).equals(testString5));
		assertTrue(CSVUtils.escapeString(testString5, value2, true).equals("a\"\"b\"\"c"));
		assertTrue(CSVUtils.unescapeString(testString5, value2, true).equals(testString5));
		assertTrue(CSVUtils.escapeString(testString5, value3, true).equals(testString5));
		assertTrue(CSVUtils.unescapeString(testString5, value3, true).equals(testString5));
		assertTrue(CSVUtils.escapeString(testString5, value1, false).equals("a\\\"b\\\"c"));
		assertTrue(CSVUtils.unescapeString(testString5, value1, false).equals(testString5));
		assertTrue(CSVUtils.escapeString(testString5, value2, false).equals("a\\\"b\\\"c"));
		assertTrue(CSVUtils.unescapeString(testString5, value2, false).equals(testString5));
		assertTrue(CSVUtils.escapeString(testString5, value3, false).equals(testString5));
		assertTrue(CSVUtils.unescapeString(testString5, value3, false).equals(testString5));

		// ab""c
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString6), value1, value4, true));
		assertEquals("ab\"\"\"\"c", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString6), value1, value4, false));
		assertEquals("ab\\\"\\\"c", buf.toString());
		assertTrue(CSVUtils.escapeString(testString6, value1, true).equals("ab\"\"\"\"c"));
		assertTrue(CSVUtils.unescapeString(testString6, value1, true).equals("ab\"c"));
		assertTrue(CSVUtils.escapeString(testString6, value2, true).equals("ab\"\"\"\"c"));
		assertTrue(CSVUtils.unescapeString(testString6, value2, true).equals("ab\"c"));
		assertTrue(CSVUtils.escapeString(testString6, value3, true).equals(testString6));
		assertTrue(CSVUtils.unescapeString(testString6, value3, true).equals(testString6));
		assertTrue(CSVUtils.escapeString(testString6, value1, false).equals("ab\\\"\\\"c"));
		assertTrue(CSVUtils.unescapeString(testString6, value1, false).equals(testString6));
		assertTrue(CSVUtils.escapeString(testString6, value2, false).equals("ab\\\"\\\"c"));
		assertTrue(CSVUtils.unescapeString(testString6, value2, false).equals(testString6));
		assertTrue(CSVUtils.escapeString(testString6, value3, false).equals(testString6));
		assertTrue(CSVUtils.unescapeString(testString6, value3, false).equals(testString6));

		// ;";
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString7), value1, value4, true));
		assertEquals(";\"\";", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString7), value1, value4, false));
		assertEquals(";\\\";", buf.toString());
		assertTrue(CSVUtils.escapeString(testString7, value1, true).equals(";\"\";"));
		assertTrue(CSVUtils.unescapeString(testString7, value1, true).equals(testString7));
		assertTrue(CSVUtils.escapeString(testString7, value2, true).equals(";;\"\";;"));
		assertTrue(CSVUtils.unescapeString(testString7, value2, true).equals(testString7));
		assertTrue(CSVUtils.escapeString(testString7, value3, true).equals(testString7));
		assertTrue(CSVUtils.unescapeString(testString7, value3, true).equals(testString7));
		assertTrue(CSVUtils.escapeString(testString7, value1, false).equals(";\\\";"));
		assertTrue(CSVUtils.unescapeString(testString7, value1, false).equals(testString7));
		assertTrue(CSVUtils.escapeString(testString7, value2, false).equals("\\;\\\"\\;"));
		assertTrue(CSVUtils.unescapeString(testString7, value2, false).equals(testString7));
		assertTrue(CSVUtils.escapeString(testString7, value3, false).equals(testString7));
		assertTrue(CSVUtils.unescapeString(testString7, value3, false).equals(testString7));

		// ;;;;""
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString8), value1, value4, true));
		assertEquals(";;;;\"\"\"\"", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString8), value1, value4, false));
		assertEquals(";;;;\\\"\\\"", buf.toString());
		assertTrue(CSVUtils.escapeString(testString8, value1, true).equals(";;;;\"\"\"\""));
		assertTrue(CSVUtils.unescapeString(testString8, value1, true).equals(";;;;\""));
		assertTrue(CSVUtils.escapeString(testString8, value2, true).equals(";;;;;;;;\"\"\"\""));
		assertTrue(CSVUtils.unescapeString(testString8, value2, true).equals(";;\""));
		assertTrue(CSVUtils.escapeString(testString8, value3, true).equals(testString8));
		assertTrue(CSVUtils.unescapeString(testString8, value3, true).equals(testString8));
		assertTrue(CSVUtils.escapeString(testString8, value1, false).equals(";;;;\\\"\\\""));
		assertTrue(CSVUtils.unescapeString(testString8, value1, false).equals(testString8));
		assertTrue(CSVUtils.escapeString(testString8, value2, false).equals("\\;\\;\\;\\;\\\"\\\""));
		assertTrue(CSVUtils.unescapeString(testString8, value2, false).equals(testString8));
		assertTrue(CSVUtils.escapeString(testString8, value3, false).equals(testString8));
		assertTrue(CSVUtils.unescapeString(testString8, value3, false).equals(testString8));

		// \;\"
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString9), value1, value4, true));
		assertEquals("\\;\\\"\"", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString9), value1, value4, false));
		assertEquals("\\\\;\\\\\\\"", buf.toString());
		assertTrue(CSVUtils.escapeString(testString9, value1, true).equals("\\;\\\"\""));
		assertTrue(CSVUtils.unescapeString(testString9, value1, true).equals(testString9));
		assertTrue(CSVUtils.escapeString(testString9, value2, true).equals("\\;;\\\"\""));
		assertTrue(CSVUtils.unescapeString(testString9, value2, true).equals(testString9));
		assertTrue(CSVUtils.escapeString(testString9, value3, true).equals("\\\\;\\\\\""));
		assertTrue(CSVUtils.unescapeString(testString9, value3, true).equals(testString9));
		assertTrue(CSVUtils.escapeString(testString9, value1, false).equals("\\\\;\\\\\\\""));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString9, value1, false), value1, false).equals(testString9));
		assertTrue(CSVUtils.escapeString(testString9, value2, false).equals("\\\\\\;\\\\\\\""));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString9, value2, false), value2, false).equals(testString9));
		assertTrue(CSVUtils.escapeString(testString9, value3, false).equals("\\\\;\\\\\""));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString9, value3, false), value3, false).equals(testString9));

		// abc\def";ab";;""gh\\""\
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString10), value1, value4, true));
		assertEquals("abc\\def\"\";ab\"\";;\"\"\"\"gh\\\\\"\"\"\"\\", buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString10), value1, value4, false));
		assertEquals("abc\\\\def\\\";ab\\\";;\\\"\\\"gh\\\\\\\\\\\"\\\"\\\\", buf.toString());
		assertTrue(CSVUtils.escapeString(testString10, value1, true).equals("abc\\def\"\";ab\"\";;\"\"\"\"gh\\\\\"\"\"\"\\"));
		assertTrue(CSVUtils.unescapeString(testString10, value1, true).equals("abc\\def\";ab\";;\"gh\\\\\"\\"));
		assertTrue(CSVUtils.escapeString(testString10, value2, true).equals("abc\\def\"\";;ab\"\";;;;\"\"\"\"gh\\\\\"\"\"\"\\"));
		assertTrue(CSVUtils.unescapeString(testString10, value2, true).equals("abc\\def\";ab\";\"gh\\\\\"\\"));
		assertTrue(CSVUtils.escapeString(testString10, value3, true).equals("abc\\\\def\";ab\";;\"\"gh\\\\\\\\\"\"\\\\"));
		assertTrue(CSVUtils.unescapeString(testString10, value3, true).equals("abc\\def\";ab\";;\"\"gh\\\"\"\\"));

		assertTrue(CSVUtils.escapeString(testString10, value1, false).equals(
				"abc\\\\def\\\";ab\\\";;\\\"\\\"gh\\\\\\\\\\\"\\\"\\\\"));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString10, value1, false), value1, false).equals(testString10));

		assertTrue(CSVUtils.escapeString(testString10, value2, false).equals(
				"abc\\\\def\\\"\\;ab\\\"\\;\\;\\\"\\\"gh\\\\\\\\\\\"\\\"\\\\"));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString10, value2, false), value2, false).equals(testString10));

		assertTrue(CSVUtils.escapeString(testString10, value3, false).equals("abc\\\\def\";ab\";;\"\"gh\\\\\\\\\"\"\\\\"));
		assertTrue(CSVUtils.unescapeString(CSVUtils.escapeString(testString10, value3, false), value3, false).equals(testString10));

		//;abc;
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString11), value1, value4, true));
		assertEquals(testString11, buf.toString());
		assertTrue(CSVUtils.escapeString(buf = new StringBuilder(testString11), value1, value4, false));
		assertEquals(testString11, buf.toString());
		assertTrue(CSVUtils.escapeString(testString11, value1, true).equals(testString11));
		assertTrue(CSVUtils.unescapeString(testString11, value1, true).equals(testString11));
		assertTrue(CSVUtils.escapeString(testString11, value2, true).equals(";;abc;;"));
		assertTrue(CSVUtils.unescapeString(testString11, value2, true).equals(testString11));
		assertTrue(CSVUtils.escapeString(testString11, value3, true).equals(testString11));
		assertTrue(CSVUtils.unescapeString(testString11, value3, true).equals(testString11));
		assertTrue(CSVUtils.escapeString(testString11, value1, false).equals(testString11));
		assertTrue(CSVUtils.unescapeString(testString11, value1, false).equals(testString11));
		assertTrue(CSVUtils.escapeString(testString11, value2, false).equals("\\;abc\\;"));
		assertTrue(CSVUtils.unescapeString(testString11, value2, false).equals(testString11));
		assertTrue(CSVUtils.escapeString(testString11, value3, false).equals(testString11));
		assertTrue(CSVUtils.unescapeString(testString11, value3, false).equals(testString11));
	}

	public void testLineStartsWith()
	{
		assertFalse(CSVUtils.lineStartsWith(null, null, null));
		assertFalse(CSVUtils.lineStartsWith("test", null, null));
		assertFalse(CSVUtils.lineStartsWith("test", new char[]
		{ 'x' }, null));
		assertTrue(CSVUtils.lineStartsWith("test", new char[]
		{ 't' }, null));
		assertTrue(CSVUtils.lineStartsWith("test", new char[]
		{ 't' }, "est"));
		assertTrue(CSVUtils.lineStartsWith("test", new char[]
		{ 's', 't' }, "est"));
		assertFalse(CSVUtils.lineStartsWith("test", new char[]
		{ 's', 't' }, "st"));
	}
}
