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
package de.hybris.platform.order.strategies.ordercloning;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryTypeService;

import java.util.Collection;


/**
 * Order cloning strategy defined on an generic {@link AbstractOrderModel} level. It is extended by Cart and Order
 * dedicated strategy that define the contract on {@link CartModel} and {@link OrderModel} levels. If you need to define
 * a strategy for your own order type, please extend this interface for it.
 */
public interface CloneAbstractOrderStrategy
{

	/**
	 * Make a clone of an abstract order (eventually change the type).
	 * 
	 * @param orderType
	 *           type of newly created order
	 * @param entryType
	 *           type of order entry of newly created order
	 * @param original
	 *           original order
	 * @param code
	 *           code of created order
	 */
	<T extends AbstractOrderModel> T clone(ComposedTypeModel orderType, ComposedTypeModel entryType, AbstractOrderModel original,
			String code, final Class abstractOrderClassResult, final Class abstractOrderEntryClassResult);


	/**
	 * Make a clone of entries (eventually change their type). If entriesType is null, the type will be calculated
	 * according to {@link AbstractOrderEntryTypeService#getAbstractOrderEntryType(AbstractOrderModel)} method.
	 * 
	 * @param entriesType
	 *           type of cloned entries
	 * @param original
	 *           original abstractOrder
	 * @return cloned order entries (same order as original)
	 * @throws IllegalArgumentException
	 *            if original is null
	 */
	<T extends AbstractOrderEntryModel> Collection<T> cloneEntries(ComposedTypeModel entriesType, AbstractOrderModel original);
}
