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

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * No operations populator - can be injected into the list of populators for a converter that requires no populators at the time of definition,
 * i.e. when the list of populators will be empty by design and will be extended with real populators in the upstream, overridden converter definition.
 * The NoOpPopulator is required, in order to avoid runtime warnings of the type “Empty populators list found for converter”, that is issued by the
 * AbstractPopulatingConverter validation.
 *
 * @param <SOURCE> Populator Source type
 * @param <TARGET> Populator Target type
 */
public class NoOpPopulator<SOURCE, TARGET> implements Populator<SOURCE, TARGET>
{
    @Override
    public void populate(final SOURCE source, final TARGET target) throws ConversionException
    {
        // NOOP
    }
}
