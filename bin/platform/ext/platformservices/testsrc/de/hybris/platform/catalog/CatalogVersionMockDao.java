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

import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class CatalogVersionMockDao implements CatalogVersionDao
{
	public static final String CATALOG_ID = "testCatalog";
	public static final String CATALOGVERSION_ONE = "one";
	public static final String CATALOGVERSION_TWO = "two";
	public static final String CATALOGVERSION_DOESNOTEXIST = "doesnotexist";


	@Override
	public Collection<CatalogVersionModel> findCatalogVersions(final String catalogId, final String version)
	{
		if (catalogId.equals(CATALOG_ID) && version.equals(CATALOGVERSION_ONE))
		{
			final List<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();

			final CatalogModel catalog = new CatalogModel();
			catalog.setId(CATALOG_ID);
			final CatalogVersionModel catalogVersion = new CatalogVersionModel();
			catalogVersion.setCatalog(catalog);
			catalogVersion.setVersion(CATALOGVERSION_ONE);
			catalogVersions.add(catalogVersion);

			return catalogVersions;
		}
		else if (catalogId.equals(CATALOG_ID) && version.equals(CATALOGVERSION_TWO))
		{
			final List<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();

			final CatalogModel catalog = new CatalogModel();
			catalog.setId(CATALOG_ID);
			final CatalogVersionModel catalogVersion1 = new CatalogVersionModel();
			catalogVersion1.setCatalog(catalog);
			catalogVersion1.setVersion(CATALOGVERSION_TWO);

			final CatalogVersionModel catalogVersion2 = new CatalogVersionModel();
			catalogVersion2.setCatalog(catalog);
			catalogVersion2.setVersion(CATALOGVERSION_TWO);

			catalogVersions.add(catalogVersion1);
			catalogVersions.add(catalogVersion2);
			return catalogVersions;
		}
		return Collections.EMPTY_LIST;
	}


	@Override
	public Collection<CatalogVersionModel> findReadableCatalogVersions(final PrincipalModel principal)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Collection<CatalogVersionModel> findWritableCatalogVersions(final PrincipalModel principal)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Collection<CatalogVersionModel> findAllCatalogVersions()
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Integer findAllCategoriesCount(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Integer findAllProductsCount(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Integer findAllMediasCount(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}


	@Override
	public Integer findAllKeywordsCount(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}

	@Override
	public Collection<DuplicatedItemIdentifier> findDuplicatedIds(final CatalogVersionModel catalogVersion)
	{
		throw new UnsupportedOperationException("Mock unsupported operation");
	}

}
