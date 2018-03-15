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
package de.hybris.platform.converters.impl;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class DummyConverterForMockito implements Converter<AtomicInteger, AtomicInteger>
{

	@Override
	public AtomicInteger convert(AtomicInteger source) throws ConversionException
	{
		return new AtomicInteger(source.get() * -1 );
	}

	@Override
	public AtomicInteger convert(AtomicInteger source, AtomicInteger prototype) throws ConversionException
	{
		prototype.set(source.get() * -1);
		return prototype;
	}

}
