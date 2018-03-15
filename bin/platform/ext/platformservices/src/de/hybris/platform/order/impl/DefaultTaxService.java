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
package de.hybris.platform.order.impl;

import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.order.TaxService;
import de.hybris.platform.order.daos.TaxDao;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TaxService}.
 */
public class DefaultTaxService extends AbstractBusinessService implements TaxService
{

	private TaxDao taxDao;

	@Override
	public TaxModel getTaxForCode(final String code)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("code", code);

		final List<TaxModel> taxes = taxDao.findTaxesByCode(code);

		ServicesUtil.validateIfSingleResult(taxes, TaxModel.class, "code", code);
		return taxes.get(0);
	}

	@Override
	public Collection<TaxModel> getTaxesForCode(final String matchedCode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("matchedCode", matchedCode);

		final Collection<TaxModel> taxes = taxDao.findTaxesByCodePattern(matchedCode);
		return taxes;
	}

	@Required
	public void setTaxDao(final TaxDao taxDao)
	{
		this.taxDao = taxDao;
	}

}
