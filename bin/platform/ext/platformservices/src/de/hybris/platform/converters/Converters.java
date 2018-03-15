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
package de.hybris.platform.converters;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;


/**
 * Helper class for converters
 */

public final class Converters
{
	private Converters()
	{
		// Private constructor to prevent instance creation
	}

	/**
	 * Convert all the items in the source list using a converter.
	 *
	 * @param sourceList
	 *           The source list of objects to convert.
	 * @param converter
	 *           The converter to use to convert the source objects.
	 * @param <SOURCE>
	 *           The type of the source objects.
	 * @param <TARGET>
	 *           The type of the converted objects.
	 * @return The list of converted objects.
	 *
	 * @see Converter#convertAll(Collection)
	 * @see Converter#convertAllIgnoreExceptions(Collection)
	 */
	public static <SOURCE, TARGET> List<TARGET> convertAll(final Collection<? extends SOURCE> sourceList,
			final Converter<SOURCE, TARGET> converter)
	{
		Assert.notNull(converter);

		if (sourceList == null || sourceList.isEmpty())
		{
			return Collections.emptyList();
		}

		final List<TARGET> result = new ArrayList<TARGET>(sourceList.size());

		for (final SOURCE source : sourceList)
		{
			result.add(converter.convert(source));
		}

		return result;
	}
}
