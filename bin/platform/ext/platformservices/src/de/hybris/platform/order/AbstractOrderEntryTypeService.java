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
package de.hybris.platform.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;


/**
 * <p>
 * Convenience service that resolves {@link ComposedTypeModel} of an entry for a target {@link AbstractOrderModel}.
 * </br>The default implementation bases on spring configurable <b>ORDER TYPE - to - ORDER ENTRY TYPE</b> mapping. Here
 * you can decide on the default behavior of such type resolving. If no mapping is defined in the spring configuration,
 * or it does not contain mapping of the given order type, the resolver will check the type of
 * {@link AbstractOrderModel#ENTRIES} attribute.
 * </p>
 */
public interface AbstractOrderEntryTypeService
{

	/**
	 * Returns {@link ComposedTypeModel} of order entry that best match the target order.
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * @return {@link ComposedTypeModel} of given order's entries.
	 */
	public ComposedTypeModel getAbstractOrderEntryType(final AbstractOrderModel order);

	/**
	 * Convenience method for resolving {@link Class} of the given order entry {@link ComposedTypeModel}.
	 * 
	 * @param entryType
	 *           - {@link ComposedTypeModel} you want to check class for.
	 * @return {@link Class} that corresponds to the type.
	 */
	public Class getAbstractOrderEntryClassForType(ComposedTypeModel entryType);

}
