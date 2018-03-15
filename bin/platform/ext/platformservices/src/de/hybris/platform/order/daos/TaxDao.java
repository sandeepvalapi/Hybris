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
package de.hybris.platform.order.daos;

import de.hybris.platform.core.model.order.price.TaxModel;

import java.util.List;


public interface TaxDao
{

	/**
	 * Finds the {@link TaxModel}s with the specified code.
	 * 
	 * @param code
	 *           the tax code
	 * @return the List of found {@link TaxModel}s ordered by creation time.
	 */
	List<TaxModel> findTaxesByCode(String code);

	/**
	 * Finds all {@link TaxModel}s which match the specified code.
	 * 
	 * @param code
	 *           an SQL-Like statement as String, such as <b>%taxCode%</b>
	 * @return the <code>List</code> of all {@link TaxModel}s which match the specified code
	 */
	List<TaxModel> findTaxesByCodePattern(String code);

}
