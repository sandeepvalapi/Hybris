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
package de.hybris.platform.catalog.job.strategy;

import de.hybris.platform.servicelayer.cronjob.PerformResult;


/**
 * Strategy abstraction for removing a given instance <code>T</code> related data.
 * 
 * @since 4.3
 */
public interface RemoveStrategy<T>
{
	PerformResult remove(T type);
}
