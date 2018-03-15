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

import de.hybris.platform.core.model.order.price.TaxModel;

import java.util.Collection;


/**
 * Service around the {@link TaxModel}.
 * 
 * @spring.bean taxService
 */
public interface TaxService
{

	/**
	 * Gets the {@link TaxModel} with the specified unique {@link TaxModel#CODE}.
	 * 
	 * @param code
	 *           the tax code
	 * @return the found {@link TaxModel} with the specified code
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            when no tax found.
	 */
	TaxModel getTaxForCode(String code);

	/**
	 * Gets all {@link TaxModel}s which match the specified code.
	 * 
	 * @param matchedCode
	 *           an SQL-Like statement as String, such as <b>%taxCode%</b>
	 * @return the <code>Collection</code> of all {@link TaxModel}s which match the specified code
	 */
	Collection<TaxModel> getTaxesForCode(String matchedCode);

}
