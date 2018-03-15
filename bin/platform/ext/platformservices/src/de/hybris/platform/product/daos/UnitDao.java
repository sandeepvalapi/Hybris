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
package de.hybris.platform.product.daos;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Set;


/**
 * The {@link UnitModel} DAO.
 * 
 * @spring.bean unitDao
 */
public interface UnitDao extends Dao
{
	/**
	 * @param code
	 *           the {@link UnitModel#CODE}
	 * @return for the given <code>code</code> the {@link UnitModel}
	 * @throws IllegalArgumentException
	 *            if <code>code</code> is <code>null</code>.
	 */
	Set<UnitModel> findUnitsByCode(final String code);

	/**
	 * Returns all Units.
	 * 
	 * @return a set containing all units.
	 */
	Set<UnitModel> findAllUnits();

	/**
	 * Finds all unit types (e.g. 'weight', 'size' ).
	 * 
	 * @return a set of type strings
	 */
	Set<String> findAllUnitTypes();

	/**
	 * Finds all units that match specific unit type
	 * 
	 * @param unitType
	 *           unit type
	 * 
	 * @return set of units that match unit type
	 */
	Set<UnitModel> findUnitsByUnitType(String unitType);
}
