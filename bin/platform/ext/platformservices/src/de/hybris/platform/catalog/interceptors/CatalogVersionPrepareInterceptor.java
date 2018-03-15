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
package de.hybris.platform.catalog.interceptors;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor sets this catalog version as {@link CatalogModel#ACTIVECATALOGVERSION} if the
 * {@link CatalogVersionModel#ACTIVE} flag was set to TRUE.
 */
public class CatalogVersionPrepareInterceptor implements PrepareInterceptor
{
	private UserService userService;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogVersionModel)
		{
			final CatalogVersionModel catalogVersion = (CatalogVersionModel) model;
			//if ACTIVE flag was changed to TRUE
			if (ctx.isModified(catalogVersion, CatalogVersionModel.ACTIVE))
			{
				final CatalogModel catalog = catalogVersion.getCatalog();
				if (catalog == null)
				{
					//expect InterceptorException from validator.
					return;
				}
				final CatalogVersionModel activeCatalogVersion = catalog.getActiveCatalogVersion();

				//if current active version is different than this one and ACTIVE flag is set to true
				if (Boolean.TRUE.equals(catalogVersion.getActive()) && !catalogVersion.equals(activeCatalogVersion))
				{
					catalog.setActiveCatalogVersion(catalogVersion);
					ctx.registerElement(catalog);

					resetOtherCatalogVersionsFlags(catalog, catalogVersion, ctx);
				}
				//if current active version is this one and ACTIVE flag is set to false
				else if (Boolean.FALSE.equals(catalogVersion.getActive()) && catalogVersion.equals(activeCatalogVersion))
				{
					catalog.setActiveCatalogVersion(null);
					ctx.registerElement(catalog);
				}
			}

			assignCurrentUserReadWritePermissions(catalogVersion, ctx);
			assureWritePrincipalsHaveReadRights(catalogVersion, ctx);

			removeDuplicatesInPrincipals(catalogVersion);
			removeDuplicateLanguages(catalogVersion);
		}
	}

	private void assignCurrentUserReadWritePermissions(final CatalogVersionModel catalogVersion, final InterceptorContext ctx)
	{

		List<PrincipalModel> writePrincipals = catalogVersion.getWritePrincipals();
		if (writePrincipals == null)
		{
			final List newWritePrincipals = new ArrayList();
			newWritePrincipals.add(userService.getCurrentUser());
			catalogVersion.setWritePrincipals(newWritePrincipals);
		}
		else if (!writePrincipals.contains(userService.getCurrentUser()))
		{
			writePrincipals = new ArrayList<>(writePrincipals);
			writePrincipals.add(userService.getCurrentUser());
			catalogVersion.setWritePrincipals(writePrincipals);
		}

		List<PrincipalModel> readPrincipals = catalogVersion.getReadPrincipals();
		if (readPrincipals == null)
		{
			final List newReadPrincipals = new ArrayList();
			newReadPrincipals.add(userService.getCurrentUser());
			catalogVersion.setWritePrincipals(newReadPrincipals);
		}
		else if (!readPrincipals.contains(userService.getCurrentUser()))
		{
			readPrincipals = new ArrayList<>(readPrincipals);
			readPrincipals.add(userService.getCurrentUser());
			catalogVersion.setReadPrincipals(readPrincipals);
		}

	}

	private void removeDuplicateLanguages(final CatalogVersionModel catalogVersion)
	{
		if (CollectionUtils.isNotEmpty(catalogVersion.getLanguages()))
		{
			final Collection<LanguageModel> languages = catalogVersion.getLanguages();
			final Set<String> differentIsocodes = languages.stream().map(i -> i.getIsocode()).collect(Collectors.toSet());

			if (languages.size() > differentIsocodes.size())
			{
				catalogVersion.setLanguages(new ArrayList<>(new HashSet<>(languages)));
			}
		}
	}

	private void removeDuplicatesInPrincipals(final CatalogVersionModel catalogVersion)
	{
		if (CollectionUtils.isNotEmpty(catalogVersion.getReadPrincipals()))
		{
			final Set<String> collect = catalogVersion.getReadPrincipals().stream().map(i -> i.getUid()).collect(Collectors.toSet());

			if (collect.size() < catalogVersion.getReadPrincipals().size())
			{
				final List<PrincipalModel> readPrincipals = catalogVersion.getReadPrincipals();
				catalogVersion.setReadPrincipals(new ArrayList<>(new HashSet<>(readPrincipals)));
			}
		}

		if (CollectionUtils.isNotEmpty(catalogVersion.getWritePrincipals()))
		{
			final Set<String> collect = catalogVersion.getWritePrincipals().stream().map(i -> i.getUid())
					.collect(Collectors.toSet());

			if (collect.size() < catalogVersion.getWritePrincipals().size())
			{
				final List<PrincipalModel> readPrincipals = catalogVersion.getWritePrincipals();
				catalogVersion.setWritePrincipals(new ArrayList<>(new HashSet<>(readPrincipals)));
			}
		}
	}

	private void assureWritePrincipalsHaveReadRights(final CatalogVersionModel catalogVersion, final InterceptorContext ctx)
	{
		final Set<String> writeNoReadPrincipalUIDs = getWritePrincipalUIDsWithoutReadRight(catalogVersion);

		if (CollectionUtils.isNotEmpty(writeNoReadPrincipalUIDs))
		{
			final Set<PrincipalModel> writeNoReadPrincipals = uidsToPrincipals(catalogVersion, writeNoReadPrincipalUIDs);

			final List<PrincipalModel> readPrincipals = catalogVersion.getReadPrincipals();
			final List<PrincipalModel> newReadPrincipals = readPrincipals == null ? new ArrayList<>()
					: new ArrayList<>(readPrincipals);
			newReadPrincipals.addAll(writeNoReadPrincipals);

			catalogVersion.setReadPrincipals(newReadPrincipals);
		}
	}

	private Set<PrincipalModel> uidsToPrincipals(final CatalogVersionModel catalogVersion,
			final Set<String> writeNoReadPrincipalUIDs)
	{
		return catalogVersion.getWritePrincipals().stream().filter(e -> writeNoReadPrincipalUIDs.contains(e.getUid()))
				.collect(Collectors.toSet());
	}

	private Set<String> getWritePrincipalUIDsWithoutReadRight(final CatalogVersionModel catalogVersion)
	{
		Set<String> result = Collections.emptySet();

		if (CollectionUtils.isNotEmpty(catalogVersion.getWritePrincipals()))
		{
			result = catalogVersion.getWritePrincipals().stream().map(i -> i.getUid()).collect(Collectors.toSet());

			if (CollectionUtils.isNotEmpty(catalogVersion.getReadPrincipals()))
			{
				final Set<String> readPrincipalsUIDs = catalogVersion.getReadPrincipals().stream().map(i -> i.getUid())
						.collect(Collectors.toSet());

				result.removeAll(readPrincipalsUIDs);
			}
		}

		return result;
	}

	private void resetOtherCatalogVersionsFlags(final CatalogModel catalog, final CatalogVersionModel catalogVersion,
			final InterceptorContext ctx)
	{
		if (catalog.getCatalogVersions() != null)
		{
			for (final CatalogVersionModel currentCatalogVersion : catalog.getCatalogVersions())
			{
				if (!currentCatalogVersion.equals(catalogVersion))
				{
					currentCatalogVersion.setActive(Boolean.FALSE);
					ctx.registerElement(currentCatalogVersion);
				}
			}
		}
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
