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
package de.hybris.platform.catalog.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Service provides basic catalog version oriented functionality. Allows fetching catalog version information, managing
 * session catalog versions and determining whether user is eligible for read/write operations within the given catalog
 * version.
 */
public class DefaultCatalogVersionService extends AbstractBusinessService implements CatalogVersionService
{
	//dao
	private CatalogVersionDao catalogVersionDao;

	//services
	private UserService userService;
	private SearchRestrictionService searchRestrictionService;

	@Override
	public void setSessionCatalogVersion(final String catalogId, final String catalogVersionName)
	{
		ServicesUtil.validateParameterNotNull(catalogId, "Parameter 'catalogId' must not be null!");
		ServicesUtil.validateParameterNotNull(catalogVersionName, "Parameter 'catalogVersionName' must not be null!");

		setSessionCatalogVersions(Collections.singleton(getCatalogVersion(catalogId, catalogVersionName)));
	}

	@Override
	public void setSessionCatalogVersions(final Collection<CatalogVersionModel> catalogVersions)
	{
		ServicesUtil.validateParameterNotNull(catalogVersions, "Parameter 'catalogVersions' must not be null!");
		if (catalogVersions.isEmpty())
		{
			getSessionService().setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS,
					Collections.singleton(CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY));
		}
		else
		{
			getSessionService().setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS,
					Collections.unmodifiableCollection(catalogVersions));
		}
	}


	@Override
	public Collection<CatalogVersionModel> getSessionCatalogVersions()
	{
		final Collection cvs = getSessionService().getAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS);
		if (cvs == null || isEmpty(cvs))
		{
			return Collections.EMPTY_LIST;
		}
		return Collections.<CatalogVersionModel> unmodifiableCollection(cvs);
	}

	private boolean isEmpty(final Collection cvs)
	{
		return cvs.size() == 1 && CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY.equals(cvs.iterator().next());
	}

	@Override
	public void addSessionCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		ServicesUtil.validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' must not be null!");
		final Set<CatalogVersionModel> newSessionCVs = new HashSet<CatalogVersionModel>(getSessionCatalogVersions());
		newSessionCVs.add(catalogVersion);
		setSessionCatalogVersions(newSessionCVs);
	}

	@Override
	public CatalogVersionModel getCatalogVersion(final String catalogId, final String catalogVersionName)
	{
		ServicesUtil.validateParameterNotNull(catalogId, "Parameter 'catalogId' must not be null!");
		ServicesUtil.validateParameterNotNull(catalogVersionName, "Parameter 'catalogVersionName' must not be null!");

		final Collection<CatalogVersionModel> catalogVersions = catalogVersionDao
				.findCatalogVersions(catalogId, catalogVersionName);

		ServicesUtil.validateIfSingleResult(catalogVersions, "CatalogVersion with catalogId '" + catalogId + "' and version '"
				+ catalogVersionName + "' not found!", "Specified parameters catalogId '" + catalogId + "' and version '"
				+ catalogVersionName + "' are not unique. " + catalogVersions.size() + " CatalogVersions found!");

		return catalogVersions.iterator().next();
	}

	@Override
	public CatalogVersionModel getSessionCatalogVersionForCatalog(final String catalogId)
	{
		final Collection<CatalogVersionModel> allCatalogVersionsFromCatalog = getSessionCatalogVersionsForCatalog(catalogId);
		if (allCatalogVersionsFromCatalog.isEmpty())
		{
			return null;
		}
		if (allCatalogVersionsFromCatalog.size() > 1)
		{
			throw new AmbiguousIdentifierException("There are more than one session catalog versions from the catalog " + catalogId);
		}
		return allCatalogVersionsFromCatalog.iterator().next();
	}

	@Override
	public Collection<CatalogVersionModel> getSessionCatalogVersionsForCatalog(final String catalogId)
	{
		ServicesUtil.validateParameterNotNull(catalogId, "Parameter 'catalogId' must not be null!");
		Collection<CatalogVersionModel> result = null;
		for (final CatalogVersionModel cVersion : getSessionCatalogVersions())
		{
			if (catalogId.equals(cVersion.getCatalog().getId()))
			{
				if (result == null)
				{
					result = new HashSet<CatalogVersionModel>();
				}
				result.add(cVersion);
			}
		}
		return result == null ? Collections.EMPTY_LIST : Collections.unmodifiableCollection(result);
	}

	@Override
	public boolean canRead(final CatalogVersionModel catalogVersion, final UserModel user)
	{
		ServicesUtil.validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' must not be null");
		if (userService.isAdmin(user))
		{
			return true;
		}
		final Collection<CatalogVersionModel> userReadableCatalogVersion = getAllReadableCatalogVersions(user);
		return userReadableCatalogVersion.contains(catalogVersion);
	}

	@Override
	public boolean canWrite(final CatalogVersionModel catalogVersion, final UserModel user)
	{
		ServicesUtil.validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' must not be null");
		if (userService.isAdmin(user))
		{
			return true;
		}
		final Collection<CatalogVersionModel> userWritableCatalogVersion = getAllWritableCatalogVersions(user);
		return userWritableCatalogVersion.contains(catalogVersion);
	}

	@Override
	public Collection<CatalogVersionModel> getAllReadableCatalogVersions(final PrincipalModel principal)
	{
		ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' must not be null");
		final Collection<CatalogVersionModel> readableCatalogVersions = (Collection<CatalogVersionModel>) getSessionService()
				.executeInLocalView(new SessionExecutionBody()
				{

					@Override
					public Object execute()
					{
						try
						{
							searchRestrictionService.disableSearchRestrictions();
							return catalogVersionDao.findReadableCatalogVersions(principal);
						}
						finally
						{
							searchRestrictionService.enableSearchRestrictions();
						}
					}

				});
		return readableCatalogVersions;
	}

	@Override
	public Collection<CatalogVersionModel> getAllWritableCatalogVersions(final PrincipalModel principal)
	{
		ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' must not be null");
		final Collection<CatalogVersionModel> readableCatalogVersions = (Collection<CatalogVersionModel>) getSessionService()
				.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public Object execute()
					{
						try
						{
							searchRestrictionService.disableSearchRestrictions();
							return catalogVersionDao.findWritableCatalogVersions(principal);
						}
						finally
						{
							searchRestrictionService.enableSearchRestrictions();
						}
					}

				});
		return readableCatalogVersions;
	}

	@Override
	public Collection<CatalogVersionModel> getAllCatalogVersions()
	{
		return catalogVersionDao.findAllCatalogVersions();
	}

	@Required
	public void setCatalogVersionDao(final CatalogVersionDao catalogVersionDao)
	{
		this.catalogVersionDao = catalogVersionDao;
	}

	@Override
	public <T extends CatalogVersionModel> Collection<T> getAllCatalogVersionsOfType(final Class<T> versionType)
	{
		validateParameterNotNull(versionType, "Parameter 'catalogType' must not be null!");

		final Collection<CatalogVersionModel> versions = getAllCatalogVersions();

		if (versions.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}

		final Collection<T> ret = new ArrayList(versions.size());
		for (final CatalogVersionModel c : versions)
		{
			if (versionType.isInstance(c))
			{
				ret.add((T) c);
			}
		}
		return ret;
	}

	@Override
	public Collection<DuplicatedItemIdentifier> findDuplicatedIds(final CatalogVersionModel catalogVersionModel)
	{
		return catalogVersionDao.findDuplicatedIds(catalogVersionModel);
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

}
