/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.util;


/**
 * Filters given string to prevent cross-site scripting
 */
public final class XSSFilterUtil
{

	private XSSFilterUtil()
	{
		//Utility classes, which are a collection of static members, are not meant to be instantiated
	}

	/**
	 *
	 * @param value
	 *           to be sanitized
	 * @return sanitized content
	 */
	public static String filter(final String value)
	{
		if (value == null)
		{
			return null;
		}
		String sanitized = value;
		// Simple characters
		sanitized = sanitized.replace("<", "&lt;").replace(">", "&gt;");
		sanitized = sanitized.replace("(", "&#40;").replace(")", "&#41;");
		sanitized = sanitized.replace("'", "&#39;");
		// RegEx pattern
		sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
		sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		return sanitized;
	}
}
