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
package de.hybris.platform.product;

import de.hybris.platform.core.model.product.UnitModel;

import java.util.Set;


/**
 * Service to read and update {@link UnitModel UnitModel}s.
 * 
 * @spring.bean unitService
 */
public interface UnitService
{
	/**
	 * @param code
	 *           the {@link UnitModel#CODE}
	 * @return for the given <code>code</code> the {@link UnitModel}
	 * @throws IllegalArgumentException
	 *            if <code>code</code> is <code>null</code>.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no unit was found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one unit was found by this code
	 */
	UnitModel getUnitForCode(final String code);

	/**
	 * Returns all Units.
	 * 
	 * @return a set containing all units.
	 */
	Set<UnitModel> getAllUnits();

	/**
	 * Returns all unit types (e.g. 'weight', 'size' ).
	 * 
	 * @return a set of type strings
	 */
	Set<String> getAllUnitTypes();

	/**
	 * Returns all units for specific unit type
	 * 
	 * @param unitType
	 *           search only units for this unit type
	 * 
	 * @return found units that match unit type
	 */
	Set<UnitModel> getUnitsForUnitType(final String unitType);
}
