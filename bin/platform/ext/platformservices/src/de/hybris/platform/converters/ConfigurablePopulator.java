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

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;


/**
 * Interface for a configurable populator. A populator sets values in a target instance based on values in the source
 * instance. Populators are similar to converters except that unlike converters the target instance must already exist.
 * The collection of options is used to control what data is populated.
 * 
 * @param <SOURCE>
 *           the type of the source object
 * @param <TARGET>
 *           the type of the destination object
 * @param <OPTION>
 *           the type of the options list
 */
public interface ConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	/**
	 * Populate the target instance from the source instance. The collection of options is used to control what data is
	 * populated.
	 * 
	 * @param source
	 *           the source object
	 * @param target
	 *           the target to fill
	 * @param options
	 *           options used to control what data is populated
	 * @throws de.hybris.platform.servicelayer.dto.converter.ConversionException
	 *            if an error occurs
	 */
	void populate(SOURCE source, TARGET target, Collection<OPTION> options) throws ConversionException;
}
