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
package de.hybris.platform.catalog;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class CatalogMockDao implements CatalogDao
{

	public static final String DOESNOTEXIST = "doesnotexist";
	public static final String ONECATALOG = "onecatalog";
	public static final String TWOCATALOGS = "twocatalogs";

	@Override
	public Collection<CatalogModel> findCatalogs(final String id)
	{
		if (id == null)
		{
			return findAllCatalogs();
		}
		if (id.equals(DOESNOTEXIST))
		{
			return Collections.EMPTY_LIST;
		}
		else if (id.equals(ONECATALOG))
		{
			final List<CatalogModel> twoCatalogs = new ArrayList<CatalogModel>();

			final CatalogModel test0 = new CatalogModel();
			test0.setId(ONECATALOG);
			twoCatalogs.add(test0);

			return twoCatalogs;
		}
		else if (id.equals(TWOCATALOGS))
		{
			final List<CatalogModel> twoCatalogs = new ArrayList<CatalogModel>();

			final CatalogModel test0 = new CatalogModel();
			test0.setId(TWOCATALOGS);
			twoCatalogs.add(test0);
			twoCatalogs.add(test0);

			return twoCatalogs;
		}
		throw new UnsupportedOperationException("");
	}

	@Override
	public Collection<CatalogModel> findAllCatalogs()
	{
		final List<CatalogModel> allCatalogs = new ArrayList<CatalogModel>();

		final CatalogModel test0 = new CatalogModel();
		test0.setId("one");
		allCatalogs.add(test0);
		final CatalogModel test1 = new CatalogModel();
		test1.setId("two");
		allCatalogs.add(test1);

		return allCatalogs;
	}

	@Override
	public CatalogModel findCatalogById(final String id)
	{
		validateParameterNotNullStandardMessage("id", id);
		if (id.equals(DOESNOTEXIST))
		{
			throw new UnknownIdentifierException("");
		}
		else if (id.equals(ONECATALOG))
		{
			final CatalogModel test0 = new CatalogModel();
			test0.setId(ONECATALOG);

			return test0;
		}
		else if (id.equals(TWOCATALOGS))
		{
			final CatalogModel test0 = new CatalogModel();
			test0.setId(TWOCATALOGS);
			return test0;
		}
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<CatalogModel> find()
	{
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<CatalogModel> find(final Map<String, ? extends Object> params)
	{
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<CatalogModel> find(final SortParameters sortParameters)
	{
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<CatalogModel> find(final Map<String, ? extends Object> params, final SortParameters sortParameters)
	{
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<CatalogModel> find(final Map<String, ? extends Object> params, final SortParameters sortParameters, final int count)
	{
		throw new UnsupportedOperationException("");
	}

	@Override
	public Collection<CatalogModel> findDefaultCatalogs()
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}



}
