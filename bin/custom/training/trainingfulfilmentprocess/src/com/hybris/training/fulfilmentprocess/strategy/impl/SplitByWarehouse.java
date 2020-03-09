/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.strategy.impl;

import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.stock.StockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Required;


public class SplitByWarehouse implements SplittingStrategy
{
	private static final String WAREHOUSE_LIST_NAME = "WAREHOUSE_LIST";
	private StockService stockService;
	private CommerceAvailabilityCalculationStrategy commerceAvailabilityCalculationStrategy;

	protected List<OrderEntryGroup> splitForWarehouses(final OrderEntryGroup orderEntryList)
	{
		final List<OrderEntryGroup> result = new ArrayList<OrderEntryGroup>();

		//list of orderEntry - todoList
		final OrderEntryGroup todoEntryList = orderEntryList.getEmpty();

		//List of working elements
		OrderEntryGroup workingOrderEntryList = sortOrderEntryBeforeWarehouseSplitting(orderEntryList);

		// list of entries that can't be performed by any warehouse
		final OrderEntryGroup emptyOrderEntryList = orderEntryList.getEmpty();

		do
		{
			//clear need here before normal proceeding 
			todoEntryList.clear();

			//list of warehouse after retailAll of prev. orderEntries
			List<WarehouseModel> tmpWarehouseResult = null;
			//list of orderEntries that can be realized by tmpWarehouseResult
			final OrderEntryGroup tmpOrderEntryResult = orderEntryList.getEmpty();


			tmpWarehouseResult = prepareWarehouses(todoEntryList, workingOrderEntryList, emptyOrderEntryList,
					tmpWarehouseResult, tmpOrderEntryResult);

			if (!tmpOrderEntryResult.isEmpty())
			{
				//add chosen one to result
				tmpOrderEntryResult.setParameter(WAREHOUSE_LIST_NAME, tmpWarehouseResult);
				result.add(tmpOrderEntryResult);
			}
			//starting process with new (not split yet) orderEntry List
			//remember to make clean at begin of new loop - if will not done unfinished loop will appear
			workingOrderEntryList = todoEntryList.getEmpty();
			workingOrderEntryList.addAll(todoEntryList);
		}
		//still something to do
		while (!todoEntryList.isEmpty());

		//entries for which warehouse can't be chosen 
		if (!emptyOrderEntryList.isEmpty())
		{
			result.add(emptyOrderEntryList);
		}

		return result;
	}

	protected List<WarehouseModel> prepareWarehouses(final OrderEntryGroup todoEntryList,
			OrderEntryGroup workingOrderEntryList, final OrderEntryGroup emptyOrderEntryList,
			List<WarehouseModel> tmpWarehouseResult, final OrderEntryGroup tmpOrderEntryResult) {
		
		List<WarehouseModel> results = tmpWarehouseResult;
		for (final AbstractOrderEntryModel orderEntry : workingOrderEntryList)
		{
			final List<WarehouseModel> currentPossibleWarehouses = getPossibleWarehouses(orderEntry);

			// no warehouse can solve order entry
			if (currentPossibleWarehouses.isEmpty())
			{
				emptyOrderEntryList.add(orderEntry);
			}
			else
			{
				//first time we wish to store all warehouses
				if (results != null)
				{
					//if not first time we take retainAll 
					currentPossibleWarehouses.retainAll(results);
				}

				// if this orderEntry can't be realized whit previous set
				if (currentPossibleWarehouses.isEmpty())
				{
					// add entry to todoList
					todoEntryList.add(orderEntry);
				}
				else
				{
					//we store list after retainAll and add orderEntry to tmpResult
					results = currentPossibleWarehouses;
					tmpOrderEntryResult.add(orderEntry);
				}

			}
		}
		return results;
	}

	protected List<WarehouseModel> getPossibleWarehouses(final AbstractOrderEntryModel orderEntry)
	{
		final List<WarehouseModel> possibleWarehouses = new ArrayList<WarehouseModel>();

		if (orderEntry.getOrder().getStore() != null)
		{
			final List<WarehouseModel> candidateWarehouses = new ArrayList<WarehouseModel>();

			candidateWarehouses.addAll(orderEntry.getDeliveryPointOfService() == null
					? orderEntry.getOrder().getStore().getWarehouses() : orderEntry.getDeliveryPointOfService().getWarehouses());

			for (final WarehouseModel candidateWarehouseModel : candidateWarehouses)
			{
				final Long availableCount = commerceAvailabilityCalculationStrategy.calculateAvailability(
						getStockService().getStockLevels(orderEntry.getProduct(), Collections.singletonList(candidateWarehouseModel)));

				if (availableCount == null || availableCount.longValue() > 0)
				{
					possibleWarehouses.add(candidateWarehouseModel);
				}
			}
		}

		return possibleWarehouses;
	}

	/**
	 * Choose best warehouse this function is called by getWarehouseList after we have set of possible warehouses.
	 *
	 * @param orderEntries
	 *           the order entries
	 *
	 * @return the warehouse model
	 */
	@SuppressWarnings("unchecked")
	protected WarehouseModel chooseBestWarehouse(final OrderEntryGroup orderEntries)
	{
		final List<WarehouseModel> warehouses = (List<WarehouseModel>) orderEntries.getParameter(WAREHOUSE_LIST_NAME);
		if (warehouses == null || warehouses.isEmpty())
		{
			return null;
		}
		final Random rnd = new Random(new Date().getTime());

		//basic solution is to random
		return warehouses.get(rnd.nextInt(warehouses.size()));
	}

	/**
	 * Sort order entry before warehouse splitting.
	 *
	 * @param listOrderEntry
	 *           the list order entry
	 *
	 * @return the list< order entry model>
	 */
	protected OrderEntryGroup sortOrderEntryBeforeWarehouseSplitting(final OrderEntryGroup listOrderEntry)
	{
		// basic - not sort
		return listOrderEntry;
	}

	@Override
	public List<OrderEntryGroup> perform(final List<OrderEntryGroup> orderEntryGroup)
	{
		final List<OrderEntryGroup> result = new ArrayList<OrderEntryGroup>();

		for (final OrderEntryGroup orderEntry : orderEntryGroup)
		{
			for (final OrderEntryGroup tmpOrderEntryGroup : splitForWarehouses(orderEntry))
			{
				result.add(tmpOrderEntryGroup);
			}
		}

		return result;
	}

	@Override
	public void afterSplitting(final OrderEntryGroup group, final ConsignmentModel createdOne)
	{
		createdOne.setWarehouse(chooseBestWarehouse(group));
	}

	/**
	 * @return the commerceAvailabilityCalculationStrategy
	 */
	protected CommerceAvailabilityCalculationStrategy getCommerceAvailabilityCalculationStrategy()
	{
		return commerceAvailabilityCalculationStrategy;
	}

	/**
	 * @param commerceAvailabilityCalculationStrategy
	 *           the commerceAvailabilityCalculationStrategy to set
	 */
	@Required
	public void setCommerceAvailabilityCalculationStrategy(
			final CommerceAvailabilityCalculationStrategy commerceAvailabilityCalculationStrategy)
	{
		this.commerceAvailabilityCalculationStrategy = commerceAvailabilityCalculationStrategy;
	}


	/**
	 * @return the stockService
	 */
	protected StockService getStockService()
	{
		return stockService;
	}

	/**
	 * @param stockService
	 *           the stockService to set
	 */
	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}
}
