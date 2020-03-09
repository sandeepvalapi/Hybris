/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;


/**
 *
 * Utility class for sanitizing up content that will appear in HTML meta tags.
 *
 */
public final class MetaSanitizerUtil
{

	private MetaSanitizerUtil()
	{
		//Utility classes, which are a collection of static members, are not meant to be instantiated
	}

	/**
	 * Takes a Collection of keyword Strings and returns a comma separated list of keywords as String.
	 *
	 * @param keywords
	 *           Collection of keyword Strings
	 * @return String of comma separated keywords
	 */
	public static String sanitizeKeywords(final Collection<String> keywords)
	{
		if (keywords != null && !keywords.isEmpty())
		{
			// Remove duplicates
			final Set<String> keywordSet = new HashSet<String>(keywords);

			// Format keywords, join with comma
			final StringBuilder stringBuilder = new StringBuilder();
			for (final String keyword : keywordSet)
			{
				stringBuilder.append(keyword).append(',');
			}
			if (stringBuilder.length() > 0)
			{
				// Remove last comma
				return stringBuilder.substring(0, stringBuilder.length() - 1);
			}
		}
		return "";
	}

	/**
	 * Takes a string of words, removes duplicates and returns a comma separated list of keywords as a String
	 *
	 * @param keywords
	 *           Keywords to be sanitized
	 * @return String of comma separated keywords
	 */
	public static String sanitizeKeywords(final String keywords)
	{
		final String clean = StringUtils.isNotEmpty(keywords) ? Jsoup.parse(keywords).text() : ""; // Clean html
		final String[] cleaned = StringUtils.split(clean.replace("\"", "")); // Clean quotes

		// Remove duplicates
		String noDupes = "";
		for (final String word : cleaned)
		{
			if (!noDupes.contains(word))
			{
				noDupes += word + ",";
			}
		}
		if (!noDupes.isEmpty())
		{
			noDupes = noDupes.substring(0, noDupes.length() - 1);
		}
		return noDupes;
	}

	/**
	 * Removes all HTML tags and double quotes and returns a String
	 *
	 * @param description
	 *           Description to be sanitized
	 * @return String object
	 */
	public static String sanitizeDescription(final String description)
	{
		if (StringUtils.isNotEmpty(description))
		{
			final String clean = Jsoup.parse(description).text();
			return clean.replace("\"", "");
		}
		else
		{
			return "";
		}
	}
}
