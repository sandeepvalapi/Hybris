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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Testcase for the RegExp classes.
 * 
 * 
 * 
 */
@UnitTest
public class RegExpTest
{
	static final Logger LOG = Logger.getLogger(RegExpTest.class.getName());

	Pattern pattern;

	@Test
	public void testCreate() throws Exception
	{
		pattern = Pattern.compile("#[a-zA-Z]*");
		assertTrue(pattern.matcher("#test").matches());
		assertFalse(pattern.matcher("test").matches());
		pattern = Pattern.compile("[a-zA-Z]*");
		assertTrue(pattern.matcher("test").matches());
	}

	@Test
	public void testMatch()
	{
		pattern = Pattern.compile("#[a-zA-Z]*");
		final String testString = "#test|#test|#test";
		final Matcher matcher = pattern.matcher(testString);
		assertTrue(matcher.find());

		//reset search posi
		matcher.reset();

		while (matcher.find())
		{
			assertEquals("#test", matcher.group());
		}
	}
	/*
	 * public void testEnumeration() { regexp = new RegExp("<A href=\\\"component://[a-zA-Z]*\\\">[a-zA-Z]*</A>",
	 * RegExp.MATCH_MULTILINE|RegExp.MATCH_NEWLINE|RegExp.MATCH_CASEINDEPENDENT); String source =
	 * "Ein toller <a href=\"component://component\">Link</a> und hier noch ein <a href=\"component://component\">Link</a>"
	 * ; for (Enumeration matches = regexp.getMatchEnumeration(source); matches.hasMoreElements(); ) { RegExpMatch match
	 * = (RegExpMatch)matches.nextElement(); assertEquals("<a href=\"component://component\">Link</a>",
	 * match.toString()); } }
	 * 
	 * 
	 * public void testSubExpression() { pattern = Pattern.compile("<A href=\\\"component://(.*?)\\\">(.*?)</A>",
	 * Pattern.MULTILINE|Pattern.DOTALL|Pattern.CASE_INSENSITIVE); String source =
	 * "Ein toller <a href=\"component://PK\">Link</a> und hier noch ein <a href=\"component://PK\">Link</a>";
	 * 
	 * for (Enumeration matches = regexp.getMatchEnumeration(source); matches.hasMoreElements(); ) { RegExpMatch match =
	 * (RegExpMatch)matches.nextElement(); String linkPK = source.substring(match.getStartIndex(1),
	 * match.getEndIndex(1)); assertEquals("PK",linkPK); String linkText = source.substring(match.getStartIndex(2),
	 * match.getEndIndex(2)); assertEquals("Link",linkText); } }
	 */

}
