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

import java.util.HashMap;


public class Randy
{


	static final String cString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	//public static String cString="abcdefghijklmnopqrstuvwxyz\u00e4\u00f6\u00fcABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c4\u00d6\u00dc\u00df1234567890!\"$%&/()=?+*'-_.:,;~@^\u00b0<>|\\\\n\\t[]{}";
	static final char[] cChars = cString.toCharArray();
	private final HashMap stringHist;

	public Randy()
	{
		stringHist = new HashMap(50);
	}

	/**
	 * returns string with max maxlength and at least one character
	 */
	public static String randomString(final int maxlength)
	{
		if (maxlength == 0)
		{
			throw new RuntimeException("you've used randomString(0).");
		}
		final int length = maxlength == 1 ? 1 : randomInteger(maxlength - 1) + 1;
		final char[] result = new char[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = cChars[randomInteger(cChars.length - 1)];
		}
		return new String(result);
	}

	public String distinctRandomString(final int maxlength)
	{
		String s = randomString(maxlength);
		while (containsString(stringHist, s))
		{
			s = randomString(maxlength);
		}

		stringHist.put(s, null);
		return s;
	}

	public void clearStringHist()
	{
		stringHist.clear();
	}

	public static byte[] randomByteArray(final int maxlength)
	{
		final int length = randomInteger(maxlength);
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++)
		{
			result[i] = randomByte(255);
		}
		return result;
	}


	public static int randomInteger(final int max)
	{
		return ((int) Math.round(max * Math.random()));
	}


	public static byte randomByte(final int max)
	{
		return ((byte) Math.round(max * Math.random()));
	}


	public static boolean compareByteArray(@SuppressWarnings("unused") final byte[] b1, @SuppressWarnings("unused") final byte[] b2)
	{
		/*
		 * if ( b1.length!= b2.length ) return false; for ( int i=0 ; i<b1.length ; i++ ) { if ( b1[i]!=b2[i] ) return
		 * false; }
		 */
		return true;
	}

	public boolean containsString(final HashMap t, final String s)
	{
		if (t == null || s == null)
		{
			return false;
		}
		return t.containsKey(s);
	}

}
