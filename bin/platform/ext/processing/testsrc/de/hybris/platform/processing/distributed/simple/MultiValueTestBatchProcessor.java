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
package de.hybris.platform.processing.distributed.simple;

import de.hybris.platform.core.PK;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class MultiValueTestBatchProcessor implements SimpleBatchProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(MultiValueTestBatchProcessor.class);
	private ModelService modelService;

	@Override
	public void process(final SimpleBatchModel inputBatch)
	{
		final List<List<?>> ctx = asList(inputBatch.getContext());

		ctx.stream().forEach(row -> {
			final PK pkToRemove = (PK) row.get(0);
			final String code = (String) row.get(1);

            modelService.remove(pkToRemove);
            LOG.info("Removed item with PK: {} and code: {}", pkToRemove, code);
		});
	}

	protected List<List<?>> asList(final Object ctx)
	{
		Preconditions.checkState(ctx instanceof List, "ctx must be instance of List");

		return (List<List<?>>) ctx;
	}

    @Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
