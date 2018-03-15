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
package de.hybris.platform.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link UnitService}
 */
public class DefaultUnitService extends AbstractBusinessService implements UnitService
{
	private UnitDao unitDao;

	@Override
	public UnitModel getUnitForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code was null");
		final Set<UnitModel> units = unitDao.findUnitsByCode(code);

		validateIfSingleResult(units, format("Unit with code '%s' not found!", code),
				format("Code '%s' is not unique, %d units found!", code, Integer.valueOf(units.size())));

		return units.iterator().next();
	}

	@Override
	public Set<String> getAllUnitTypes()
	{
		return unitDao.findAllUnitTypes();
	}

	@Override
	public Set<UnitModel> getAllUnits()
	{
		return unitDao.findAllUnits();
	}

	@Required
	public void setUnitDao(final UnitDao unitDao)
	{
		this.unitDao = unitDao;
	}

	@Override
	public Set<UnitModel> getUnitsForUnitType(final String unitType)
	{
		validateParameterNotNull(unitType, "Parameter unitType was null");
		return unitDao.findUnitsByUnitType(unitType);
	}

}
