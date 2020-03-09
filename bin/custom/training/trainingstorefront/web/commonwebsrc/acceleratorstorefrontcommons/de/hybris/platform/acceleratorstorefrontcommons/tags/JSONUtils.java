/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.tags;

import com.fasterxml.jackson.core.io.JsonStringEncoder;


/**
 * JSON util class.
 */
public class JSONUtils
{
	private JSONUtils()
	{
		// private constructor to avoid instantiation
	}

	/**
	 * Encode of JSON String values using Jackson JsonStringEncoder.
	 *
	 * @param unencodedString
	 *           the not encoded JSON string
	 * @return encoded JSON string
	 */
	public static String encode(final String unencodedString)
	{
		final JsonStringEncoder encoder = new JsonStringEncoder();
		return String.valueOf(encoder.quoteAsString(unencodedString));
	}
}
