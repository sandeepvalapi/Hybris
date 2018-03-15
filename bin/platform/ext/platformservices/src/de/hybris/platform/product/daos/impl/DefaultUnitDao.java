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
package de.hybris.platform.product.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Default implementation of the {@link UnitDao}.
 */
public class DefaultUnitDao extends DefaultGenericDao<UnitModel> implements UnitDao
{
	public DefaultUnitDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public Set<UnitModel> findUnitsByCode(final String code)
	{
		validateParameterNotNull(code, "Code must not be null!");

		final List<UnitModel> units = find(Collections.singletonMap(UnitModel.CODE, (Object) code));

		return Collections.unmodifiableSet(new HashSet<UnitModel>(units));
	}

	@Override
	public Set<String> findAllUnitTypes()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery( //
				"SELECT DISTINCT {" + UnitModel.UNITTYPE + "} " + //
						"FROM {" + UnitModel._TYPECODE + "} ");
		query.setResultClassList(Arrays.asList(String.class));
		final SearchResult<String> result = getFlexibleSearchService().search(query);
		return Collections.unmodifiableSet(new HashSet<String>(result.getResult()));
	}

	@Override
	public Set<UnitModel> findAllUnits()
	{
		final List<UnitModel> result = find();
		return Collections.unmodifiableSet(new HashSet<UnitModel>(result));
	}

	@Override
	public Set<UnitModel> findUnitsByUnitType(final String unitType)
	{
		final List<UnitModel> result = find(Collections.singletonMap(UnitModel.UNITTYPE, (Object) unitType));
		return Collections.unmodifiableSet(new HashSet<UnitModel>(result));
	}

}
