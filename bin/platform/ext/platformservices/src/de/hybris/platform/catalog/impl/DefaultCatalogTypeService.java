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

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.data.CatalogVersionOverview;
import de.hybris.platform.catalog.exceptions.CatalogAwareObjectResolvingException;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CatalogTypeDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CatalogTypeService}.
 */
public class DefaultCatalogTypeService extends AbstractBusinessService implements CatalogTypeService

{
	private CatalogTypeDao catalogTypeDao;
	private TypeService typeService;
	private FlexibleSearchService flexibleSearchService;
	private SearchRestrictionService searchRestrictionService;

	private static final String UNION = "UNION ALL\n";
	private static final Logger LOG = Logger.getLogger(DefaultCatalogTypeService.class);

	@Override
	public String getCatalogVersionContainerAttribute(final String typeCode)
	{
		final ComposedTypeModel composedType = typeService.getComposedTypeForCode(typeCode);
		final AttributeDescriptorModel attDescriptor = composedType.getCatalogVersionAttribute();
		if (attDescriptor != null)
		{
			return attDescriptor.getQualifier();
		}
		else
		{
			throw new IllegalArgumentException("type " + typeCode + " has no catalog version container attribute");
		}
	}

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public <T extends ItemModel> T getCatalogVersionItem(final CatalogVersionModel version, final String typeCode,
			final Map<String, Object> uniqueKeyValues)
	{
		try
		{
			return (T) getCatalogVersionAwareModel(version, typeCode, uniqueKeyValues);
		}
		catch (final CatalogAwareObjectResolvingException e)
		{
			LOG.warn("Could not find matching catalog item due to " + e.getMessage());
			//keep API compatibility
			return null;
		}
	}

	@Override
	public ItemModel getCatalogVersionAwareModel(final CatalogVersionModel version, final String typeCode,
			final Map<String, Object> uniqueKeyValues) throws CatalogAwareObjectResolvingException
	{
		ServicesUtil.validateParameterNotNull(version, "version must not be null");
		ServicesUtil.validateParameterNotNull(uniqueKeyValues, "uniqueKeyValues must not be null");
		if (getModelService().isNew(version))
		{
			//PLA-7900
			throw new CatalogAwareObjectResolvingException("CatalogVersion is not persisted", null, version);
		}
		//should throw unknown identifier for unknown type
		ComposedTypeModel composeType = typeService.getComposedTypeForCode(typeCode);
		if (!isCatalogVersionAwareType(composeType))
		{
			throw new IllegalArgumentException("type " + typeCode + " is not catalog item type");
		}

		final AttributeDescriptorModel versionAD = composeType.getCatalogVersionAttribute();
		final Collection<AttributeDescriptorModel> keyAttributes = composeType.getUniqueKeyAttributes();
		if (keyAttributes == null || keyAttributes.isEmpty())
		{
			throw new IllegalStateException("no key attribute(s) defined for catalog item type " + typeCode);
		}
		// find root type for query -> otherwise we'd miss sibling items with the same key !!!
		composeType = getCatalogAwareRootType(composeType);

		final StringBuilder strbuil = new StringBuilder();
		strbuil.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(composeType.getCode()).append("} ");
		strbuil.append("WHERE {").append(versionAD.getQualifier()).append("}=?tgtVer ");


		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("tgtVer", version);

		for (final AttributeDescriptorModel keyAD : keyAttributes)
		{
			final String qualifier = keyAD.getQualifier();
			if (!keyAD.getSearch().booleanValue())
			{
				throw new IllegalStateException("key attribute " + qualifier + " out of " + keyAttributes + " of catalog item type "
						+ composeType.getCode() + " is not searchable");
			}
			if (!uniqueKeyValues.containsKey(qualifier))
			{
				throw new IllegalArgumentException("missing unqiue key value for '" + qualifier + "' - got " + uniqueKeyValues);
			}
			final Object value = uniqueKeyValues.get(qualifier);
			// if the unique attribute value do not exist in persistence layer yet - it means that there is no such CatalogItem for the given attributes and the method can return null.
			try
			{
				//PLA-7900
				getModelService().toPersistenceLayer(value);
			}
			catch (final IllegalStateException e)
			{
				throw new CatalogAwareObjectResolvingException("Unpersisted model for unique attribute [" + qualifier + "]", e, value);
			}
			strbuil.append(" AND {").append(qualifier).append("}");
			// allow NULL key values as well !!!
			if (value == null)
			{
				strbuil.append(" IS NULL ");
				// got one value here
			}
			else
			{
				final String token = qualifier + "KeyValue";
				strbuil.append("=?").append(token).append(" ");
				params.put(token, value);
			}
		}
		strbuil.append("ORDER BY {").append(ItemModel.CREATIONTIME).append("} DESC");

		final List<ItemModel> items = (List<ItemModel>) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				searchRestrictionService.disableSearchRestrictions();
				final SearchResult<ItemModel> result = flexibleSearchService.search(strbuil.toString(), params);
				return result.getResult();
			}
		});

		if (items.isEmpty())
		{
			return null;
		}
		else if (items.size() > 1)
		{
			throw new AmbiguousIdentifierException("multiple matches for type '" + typeCode + "' and '" + uniqueKeyValues
					+ "' - found " + items.size() + " items");
		}
		else
		{
			return items.get(0);
		}
	}

	private ComposedTypeModel getCatalogAwareRootType(ComposedTypeModel composedType)
	{
		final Collection<AttributeDescriptorModel> keyAttributes = composedType.getUniqueKeyAttributes();
		for (ComposedTypeModel superType = composedType.getSuperType(); superType != null; superType = superType.getSuperType())
		{
			if (keyAttributes.equals(superType.getUniqueKeyAttributes()))
			{
				composedType = superType;
			}
			else
			{
				break;
			}
		}
		return composedType;
	}


	@Override
	public Set<String> getCatalogVersionUniqueKeyAttribute(final String typeCode)
	{
		ServicesUtil.validateParameterNotNull(typeCode, "Type code must not be null");
		final ComposedTypeModel composedType = typeService.getComposedTypeForCode(typeCode);
		Set<String> ret = null;
		if (composedType.getUniqueKeyAttributes() != null)
		{
			for (final AttributeDescriptorModel attDescriptor : composedType.getUniqueKeyAttributes())
			{
				if (ret == null)
				{
					ret = new LinkedHashSet<String>(5);
				}
				ret.add(attDescriptor.getQualifier());
			}
		}
		return ret != null ? ret : Collections.EMPTY_SET;
	}

	@Override
	public boolean isCatalogVersionAwareType(final String typeCode)
	{
		final ComposedTypeModel composedType = typeService.getComposedTypeForCode(typeCode);
		return isCatalogVersionAwareType(composedType);
	}

	@Override
	public Collection<ComposedTypeModel> getAllCatalogVersionAwareTypes(final boolean superTypesOnly)
	{
		final List<ComposedTypeModel> catalogVersionAwareComposedTypes = catalogTypeDao.findAllCatalogVersionAwareTypes();
		final Collection<ComposedTypeModel> allTypes = catalogVersionAwareComposedTypes == null ? Collections
				.<ComposedTypeModel> emptyList() : catalogVersionAwareComposedTypes;

		if (!superTypesOnly)
		{
			return allTypes;
		}
		final Set<ComposedTypeModel> returnSet = new HashSet<ComposedTypeModel>();
		for (final ComposedTypeModel comptype : allTypes)
		{
			final ComposedTypeModel superComposedType = comptype.getSuperType();
			if (!isCatalogVersionAwareType(superComposedType))
			{
				returnSet.add(comptype);
			}
			else
			{
				final Set<String> compColl = getCatalogVersionUniqueKeyAttribute(comptype.getCode());
				final Set<String> superCompColl = getCatalogVersionUniqueKeyAttribute(superComposedType.getCode());
				if (compColl.size() != superCompColl.size() || !compColl.containsAll(superCompColl))
				{
					returnSet.add(comptype);
				}
			}
		}
		return Collections.unmodifiableCollection(returnSet);

	}

	@Override
	public boolean isCatalogVersionAwareType(final ComposedTypeModel type)
	{
		ServicesUtil.validateParameterNotNull(type, "Type must not be null");
		final Class typeClass = typeService.getModelClass(type);
		return (CatalogModel.class.isAssignableFrom(typeClass) || Boolean.TRUE.equals(type.getCatalogItemType()))
				&& type.getCatalogVersionAttribute() != null
				&& (type.getUniqueKeyAttributes() != null && !type.getUniqueKeyAttributes().isEmpty());
	}

	@Override
	public boolean isCatalogVersionAwareModel(final ItemModel model)
	{
		ServicesUtil.validateParameterNotNull(model, "Model must not be null");
		return isCatalogVersionAwareType(typeService.getComposedTypeForClass(model.getClass()));
	}

	@Override
	public CatalogVersionModel getCatalogVersionForCatalogVersionAwareModel(final ItemModel model)
	{
		ServicesUtil.validateParameterNotNull(model, "model must not be null");
		//the following assures that the model's type has an attribute descriptor for catalog version
		final ComposedTypeModel itemType = typeService.getComposedTypeForClass(model.getClass());
		if (isCatalogVersionAwareType(itemType))
		{
			final AttributeDescriptorModel cvDescriptor = itemType.getCatalogVersionAttribute();
			final Object catalogVersionObject = getModelService().getAttributeValue(model, cvDescriptor.getQualifier());
			if (catalogVersionObject != null && CatalogVersionModel.class.isAssignableFrom(catalogVersionObject.getClass()))
			{
				return (CatalogVersionModel) catalogVersionObject;
			}
			else
			{
				throw new IllegalStateException("Item [" + model.getPk() + "] should have catalog version defined as "
						+ cvDescriptor.getQualifier() + ", but was "
						+ (catalogVersionObject == null ? "null" : catalogVersionObject.getClass()));
			}
		}
		throw new IllegalStateException("Item [" + model.getPk() + "] is of type [" + itemType.getCode()
				+ "] which is not catalog item type");
	}

	@Override
	public CatalogVersionOverview getCatalogVersionOverview(final CatalogVersionModel catalogVersion)
	{
		final Map<ComposedTypeModel, Long> typeStatistics = new HashMap<>();
		final CatalogVersionOverview result = new CatalogVersionOverview();
		result.setCatalogVersion(catalogVersion);

		final FlexibleSearchQuery fq = new FlexibleSearchQuery(generateCatalogVersionOverviewQuery());
		fq.addQueryParameter("catver", catalogVersion);
		fq.setResultClassList(Arrays.asList(ComposedTypeModel.class, Long.class));
		final SearchResult<List<Object>> queryResult = flexibleSearchService.search(fq);

		for (final List<Object> row : queryResult.getResult())
		{
			if (row != null)
			{
				final ComposedTypeModel type = ((ComposedTypeModel) row.get(0));
				final Long amount = (Long) row.get(1);
				if (amount > 0)
				{
					typeStatistics.put(type, amount);
				}
			}
		}
		result.setTypeAmounts(typeStatistics);
		return result;
	}

	private String generateCatalogVersionOverviewQuery()
	{
		final StringBuilder query = new StringBuilder();
		boolean firstSubQuery = true;
		query.append("SELECT tbl.comptype, tbl.amount FROM (\n");

		for (final ComposedTypeModel comptype : getAllCatalogVersionAwareTypes(false))
		{
			if (!comptype.getAbstract())
			{
				final String cvqualifier = getCatalogVersionContainerAttribute(comptype.getCode());
				if (firstSubQuery)
				{
					firstSubQuery = false;
				}
				else
				{
					query.append(UNION);
				}
				query.append("{{\n");
				query.append("SELECT " + comptype.getPk().getLongValue() + " AS comptype, COUNT(*) AS amount \n");
				query.append("FROM {" + comptype.getCode() + "! AS ct} \n");
				query.append("WHERE {ct:" + cvqualifier + "}= ?catver \n");
				query.append("}}\n");
			}
		}
		query.append(") tbl ORDER BY tbl.comptype ASC\n");
		return query.toString();
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Required
	public void setCatalogTypeDao(final CatalogTypeDao catalogTypeDao)
	{
		this.catalogTypeDao = catalogTypeDao;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setSearchRestrictionService(final SearchRestrictionService searchRestrictionService)
	{
		this.searchRestrictionService = searchRestrictionService;
	}

	//Depreciations
	@Override
	public boolean isCatalogItemType(final String typeCode)
	{
		return isCatalogVersionAwareType(typeCode);
	}

}
