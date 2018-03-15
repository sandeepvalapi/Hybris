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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DeploymentTest extends HybrisJUnit4Test
{
	private static String[] excludedExtensions;
	private static String[] excludedTypes;

	@Before
	public void setUp()
	{
		// say what to exclude (should be empty)
		excludedExtensions = new String[] {};

		// historical types which needs to be migrated in future
		excludedTypes = new String[]
		{
				//ComposedTypes
				"Export", "PreviewTicket", "WherePart",
				"AbstractAdvancedSavedQuerySearchParameter",
				"SavedQuery",
				"ProductMediaLink",
				"ParameterValue",
				"CMSRelation",
				"PathPrefix",
				"PublicationComponent",
				"CMSItem",
				"WorkflowActionComment",
				"DeeplinkUrl",
				"PageFormat",
				"DeeplinkUrlRule",
				"CockpitFavoriteCategory",//

				//Relations
				"Principal2ReadablePublicationRelation", "ProductPromotionRelation", "CategoriesForProductCarouselComponent",
				"CatalogsForMobileCatalogListComponent", "ProductsForProductCarouselComponent", "ProductsForProductListComponent",
				"CatalogsForCatalogListComponent", "LocalizedAssetToProductMNRelation", "CategoryPromotionRelation",
				"Principal2WriteablePublicationComponentRelation", "LinksForListComponent", "Publication2Media",
				"Category2ContentBlockRelation", "AssetFromProductMNRelation", "AssetToProductMNRelation",
				"PreviewDataToCatalogVersion", "Principal2ReadablePublicationComponentRelation",
				"Principal2WriteablePublicationRelation", "BannersForRotatingComponent" };
	}

	@Test
	public void testGenericItemDeployments()
	{
		final StringBuilder result = new StringBuilder();
		int count = 0;
		final ComposedType genericType = TypeManager.getInstance().getComposedType(GenericItem.class);
		final ItemDeployment genericDepl = Registry.getPersistenceManager().getItemDeployment(
				Registry.getPersistenceManager().getJNDIName(genericType.getCode()));

		for (final ComposedType type : getTypes())
		{
			final ItemDeployment depl = Registry.getPersistenceManager().getItemDeployment(
					Registry.getPersistenceManager().getJNDIName(type.getCode()));

			//YTODO: handling for abstract types, just excluded at the moment
			if (!type.isAbstract() && genericType.equals(type.getSuperType()) && (genericDepl.equals(depl) || depl == null))
			{
				result.append("Type: ").append(type.getCode()).append(", Extension: ").append(type.getExtensionName()).append("\n");
				count++;
			}
		}
		assertEquals(
				"For improving the quality of the hybris platform this test checks if all types extending GenericItem have an own deployment specified. The following types are evil:\n"
						+ result.toString(), 0, count);
	}

	@Test
	public void testRelationDeployments()
	{
		final StringBuilder result = new StringBuilder();
		int count = 0;
		final ComposedType linkType = TypeManager.getInstance().getComposedType(Link.class);
		final ItemDeployment linkDepl = Registry.getPersistenceManager().getItemDeployment(
				Registry.getPersistenceManager().getJNDIName(linkType.getCode()));

		for (final ComposedType type : getTypes())
		{
			final ItemDeployment depl = Registry.getPersistenceManager().getItemDeployment(
					Registry.getPersistenceManager().getJNDIName(type.getCode()));
			if (linkType.equals(type.getSuperType()) && (linkDepl.equals(depl) || depl == null))
			{
				result.append("Type: ").append(type.getCode()).append(", Extension: ").append(type.getExtensionName()).append("\n");
				count++;
			}
		}
		assertEquals(
				"For improving the quality of the hybris platform this test checks if all many to many relation have an own deployment specified. The following types are evil:\n"
						+ result.toString(), 0, count);
	}

	private Collection<ComposedType> getTypes()
	{
		final Set<ComposedType> result = new LinkedHashSet<ComposedType>();
		outer: for (final ComposedType type : TypeManager.getInstance().getAllComposedTypes())
		{
			for (final String excludeType : excludedTypes)
			{
				try
				{
					if (TypeManager.getInstance().getComposedType(excludeType).isAssignableFrom(type))
					{
						continue outer;
					}
				}
				catch (final JaloItemNotFoundException e)
				{
					// just continue, type not existent and does not need to be excluded
				}

			}

			for (final String excludeExtension : excludedExtensions)
			{
				if (type.getExtensionName() != null && type.getExtensionName().equals(excludeExtension))
				{
					continue outer;
				}
			}
			result.add(type);
		}
		return result;
	}
}
