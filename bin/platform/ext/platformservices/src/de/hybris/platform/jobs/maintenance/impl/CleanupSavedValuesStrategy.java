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
package de.hybris.platform.jobs.maintenance.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.hmc.jalo.ConfigConstants;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This implementation if the {@link MaintenanceCleanupStrategy} removes all <b>old</b> {@link SavedValuesModel}s for
 * any ItemModel which are <b>*over*</b> the given threshold.
 * <p/>
 * If a model has 30 saved values and the threshold is set to 20 - the twentieth oldest entries are removed.
 * 
 */
public class CleanupSavedValuesStrategy implements MaintenanceCleanupStrategy<ItemModel, CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(CleanupSavedValuesStrategy.class.getName());

	//static bean properties
	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	//dynamic job properties
	private Integer maxAllowedValues = Integer.valueOf(ConfigConstants.getInstance().STORING_MODIFIEDVALUES_SIZE);



	@Override
	public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
	{
		if (cjm.getJob() instanceof MaintenanceCleanupJobModel
				&& ((MaintenanceCleanupJobModel) cjm.getJob()).getThreshold() != null)
		{
			//job instance is always before bean definition
			setThreshold(((MaintenanceCleanupJobModel) cjm.getJob()).getThreshold());
		}

		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {" + SavedValuesModel.MODIFIEDITEM + "} FROM {" + SavedValuesModel._TYPECODE + "}");
		builder.append(" group by {" + SavedValuesModel.MODIFIEDITEM + "} having count(*) > ?threshold");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), // 
				Collections.singletonMap("threshold", this.maxAllowedValues));
		query.setResultClassList(Arrays.asList(ItemModel.class));

		return query;
	}

	@Override
	public void process(final List<ItemModel> elements)
	{
		LOG.info("Found " + elements.size() + " items with to many SavedValues");

		for (final Iterator<ItemModel> iter = elements.iterator(); iter.hasNext();)
		{
			final ItemModel model = iter.next();

			final String query = "select {pk} from {" + SavedValuesModel._TYPECODE + "} " + //
					"where {" + SavedValuesModel.MODIFIEDITEM + "} = ?item " + //
					"order by {" + SavedValuesModel.CREATIONTIME + "} desc"; //oldest to the end!

			final SearchResult<SavedValuesModel> searchresult = flexibleSearchService.search(query,
					Collections.singletonMap("item", model));

			final List<SavedValuesModel> items = searchresult.getResult();
			modelService.removeAll(items.subList(this.maxAllowedValues.intValue(), items.size()));
		}
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	private void setThreshold(final Integer maxAllowedValues)
	{
		if (maxAllowedValues.intValue() < 0)
		{
			throw new IllegalArgumentException("maxAllowedValues cannot be negative.");
		}
		this.maxAllowedValues = maxAllowedValues;
	}
}
