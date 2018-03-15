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
import de.hybris.platform.converters.PopulatorList;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;


/**
 * Populator that uses a list of configured populators to populate the target.
 */
public class DefaultPopulatorList<SOURCE, TARGET> implements Populator<SOURCE, TARGET>, PopulatorList<SOURCE, TARGET>,
		BeanNameAware
{

	private final static Logger LOG = Logger.getLogger(AbstractPopulatingConverter.class);
	private List<Populator<SOURCE, TARGET>> populators;
	private String myBeanName;

	@Override
	public List<Populator<SOURCE, TARGET>> getPopulators()
	{
		return populators;
	}

	@Override
	public void setPopulators(final List<Populator<SOURCE, TARGET>> populators)
	{
		this.populators = populators;
	}

	/**
	 * Populate the target instance from the source instance. Calls a list of injected populators to populate the
	 * instance.
	 *
	 * @param source
	 *           the source item
	 * @param target
	 *           the target item to populate
	 */
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		final List<Populator<SOURCE, TARGET>> list = getPopulators();
		if (list == null)
		{
			return;
		}

		for (final Populator<SOURCE, TARGET> populator : list)
		{
			if (populator != null)
			{
				populator.populate(source, target);
			}
		}
	}

	// execute when BEAN name is known
	@PostConstruct
	public void removePopulatorsDuplicates()
	{
		// CHECK for populators duplicates
		if (CollectionUtils.isNotEmpty(populators))
		{
			final LinkedHashSet<Populator<SOURCE, TARGET>> distinctPopulators = new LinkedHashSet<>();

			for (final Populator<SOURCE, TARGET> populator : populators)
			{
				if (!distinctPopulators.add(populator))
				{
					LOG.warn("Duplicate populator entry [" + populator.getClass().getName() + "] found for converter "
							+ getMyBeanName() + "! The duplication has been removed.");
				}
			}
			this.populators = new ArrayList<>(distinctPopulators);
		}
		else
		{
			LOG.warn("Empty populators list found for converter " + getMyBeanName() + "!");
		}
	}

	@Override
	public void setBeanName(final String name)
	{
		this.myBeanName = name;
	}

	public String getMyBeanName()
	{
		return myBeanName;
	}
}
