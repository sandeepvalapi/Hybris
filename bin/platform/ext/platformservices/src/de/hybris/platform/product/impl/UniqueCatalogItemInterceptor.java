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

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.exceptions.CatalogAwareObjectResolvingException;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelValidationException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Checks ({@link PrepareInterceptor} and {@link ValidateInterceptor}) if a product code is unique within its catalog
 * version. Additionally it also assigns a default catalog version to any model lacking it before creation.
 */
public class UniqueCatalogItemInterceptor implements ValidateInterceptor, PrepareInterceptor
{
	private static final Logger LOG = Logger.getLogger(UniqueCatalogItemInterceptor.class.getName());

	private static final String CHECKED_ATTR = "UniqueCatalogItemInterceptor.checked";

	private CatalogTypeService catalogTypeService;

	private TypeService typeService;

	private InterceptorRegistry interceptorRegistry;

	@Required
	public void setInterceptorRegistry(final InterceptorRegistry interceptorRegistry)
	{
		this.interceptorRegistry = interceptorRegistry;
	}

	@Required
	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!Boolean.TRUE.equals(ctx.getAttribute(CHECKED_ATTR)))
		{
			ctx.setAttribute(CHECKED_ATTR, Boolean.TRUE);

			final Set<ModifiedCatalogItem> uniqueOnes = new HashSet<ModifiedCatalogItem>();
			// 1. Check for duplicates in ctx
			for (final ModifiedCatalogItem catItem : getModifiedCatalogItemModels(ctx))
			{
				if (!uniqueOnes.add(catItem))
				{
					throw new InterceptorException("unique keys " + catItem.uniqueKeys + " of " + catItem.model
							+ " are ambiguous in version " + catItem.version.getCatalog().getId() + "/" + catItem.version.getVersion(),
							this);
				}
			}
			// 2. Check for duplicates in database
			for (final ModifiedCatalogItem catItem : uniqueOnes)
			{
				try
				{
					ItemModel duplicate = null;
					if (catItem.version != null)
					{
						try
						{
							duplicate = catalogTypeService.getCatalogVersionAwareModel(catItem.version, catItem.type,
									catItem.uniqueKeys);
						}
						catch (final CatalogAwareObjectResolvingException e)
						{
							LOG.warn("Could not find duplicates due to " + e.getMessage());
						}
					}
					if (duplicate != null && (catItem.model.getPk() == null || !catItem.model.getPk().equals(duplicate.getPk())))
					{
						throw new InterceptorException(
								"unique keys " + catItem.uniqueKeys + " of " + model + " are ambiguous in version "
										+ catItem.version.getCatalog().getId() + "/" + catItem.version.getVersion(),
								this);
					}
				}
				catch (final AmbiguousIdentifierException e)
				{
					// bad 
					throw new InterceptorException("unique keys " + catItem.uniqueKeys + " of " + model + " are ambiguous in version "
							+ catItem.version.getCatalog().getId() + "/" + catItem.version.getVersion(), e, this);
				}
			}
		}
	}

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		final String type = ctx.getModelService().getModelType(model);
		if (catalogTypeService.isCatalogVersionAwareType(type) && ctx.isNew(model))
		{
			final String versionAttribute = catalogTypeService.getCatalogVersionContainerAttribute(type);
			CatalogVersionModel catalogVersionModel = null;
			try
			{
				catalogVersionModel = ctx.getModelService().getAttributeValue(model, versionAttribute);
			}
			catch (final Exception e)
			{
				LOG.error("cannot read catalog version due to " + e.getMessage(), e);
				return;
			}
			if (catalogVersionModel == null)
			{
				catalogVersionModel = getDefaultCatalogVersion(ctx, model);
				if (catalogVersionModel != null)
				{
					try
					{
						((AbstractItemModel) model).setProperty(versionAttribute, catalogVersionModel);
						setCatalogForCatalogVersionAwareType((AbstractItemModel) model, catalogVersionModel);
					}
					catch (final Exception e)
					{
						LOG.error("Exception occured when setting [" + versionAttribute + "] for model " + model + " with value "
								+ catalogVersionModel + " : " + e.getMessage(), e);
					}
				}
			}
			else
			{
				setCatalogForCatalogVersionAwareType((AbstractItemModel) model, catalogVersionModel);
			}
		}
	}

	private void setCatalogForCatalogVersionAwareType(final AbstractItemModel model, final CatalogVersionModel catalogVersionModel)
	{
		if (model instanceof ProductModel)
		{
			model.setProperty(ProductModel.CATALOG, catalogVersionModel.getCatalog());
		}
		else if (model instanceof CategoryModel)
		{
			model.setProperty(CategoryModel.CATALOG, catalogVersionModel.getCatalog());
		}
		else if (model instanceof MediaModel)
		{
			model.setProperty(MediaModel.CATALOG, catalogVersionModel.getCatalog());
		}
		else if (model instanceof KeywordModel)
		{
			model.setProperty(KeywordModel.CATALOG, catalogVersionModel.getCatalog());
		}
	}


	protected static class ModifiedCatalogItem
	{
		private final ItemModel model;
		private final String type;
		private final CatalogVersionModel version;
		private final Map<String, Object> uniqueKeys;

		ModifiedCatalogItem(final ItemModel model, final String type, final CatalogVersionModel version,
				final Map<String, Object> keys)
		{
			this.model = model;
			this.type = type;
			this.version = version;
			this.uniqueKeys = keys;
		}

		@Override
		public int hashCode()
		{
			return version == null ? super.hashCode() : version.hashCode() ^ uniqueKeys.hashCode();
		}

		@Override
		public boolean equals(final Object obj)
		{
			return version == null ? super.equals(obj)
					: super.equals(obj) || (version.equals(((ModifiedCatalogItem) obj).version)
							&& uniqueKeys.equals(((ModifiedCatalogItem) obj).uniqueKeys));
		}

		@Override
		public String toString()
		{
			return model.toString() + "[" + version + "," + uniqueKeys + "]";
		}

	}

	protected Collection<ModifiedCatalogItem> getModifiedCatalogItemModels(final InterceptorContext ctx)
			throws InterceptorException
	{
		Collection<ModifiedCatalogItem> ret = null;

		// caching type information per type - no sense in getting them for each item !!!
		Map<String, TypeInfo> typeInfoCache = null; // lazy

		for (final Object model : ctx.getAllRegisteredElements())
		{
			final String type = ctx.getModelService().getModelType(model);

			if (typeInfoCache == null)
			{
				typeInfoCache = new HashMap<String, TypeInfo>();
			}
			TypeInfo info = typeInfoCache.get(type);
			if (info == null)
			{
				if (isInterceptorEnabledForType(type) && catalogTypeService.isCatalogVersionAwareType(type))
				{
					info = new TypeInfo(catalogTypeService.getCatalogVersionContainerAttribute(type),
							catalogTypeService.getCatalogVersionUniqueKeyAttribute(type));
					final Set<String> uniqueAttributes = typeService.getUniqueAttributes(type);
					// special case if all catalog attributes are also marked unique - we don't need checking since other interceptor will do it
					info.allUnique = uniqueAttributes.containsAll(info.uniqueKeys) && uniqueAttributes.contains(info.versionAttribute);

					typeInfoCache.put(type, info);
				}
				else
				{
					info = new TypeInfo(null, null);
					typeInfoCache.put(type, info);
				}
			}

			if (info.isCatalogItemType() && !info.allUnique)
			{
				boolean modified = ctx.isModified(model, info.versionAttribute);
				if (!modified)
				{
					for (final String k : info.uniqueKeys)
					{
						if (ctx.isModified(model, k))
						{
							modified = true;
							break;
						}
					}
				}
				if (modified)
				{
					final ItemModel item = (ItemModel) model;
					CatalogVersionModel catalogVersionModel = null;
					try
					{
						catalogVersionModel = (CatalogVersionModel) ctx.getModelService().getAttributeValue(model,
								info.versionAttribute);
					}
					catch (final Exception e)
					{
						throw new ModelValidationException("cannot read catalog version due to " + e.getMessage(), e);
					}
					if (catalogVersionModel == null && isMandatory(item, info.versionAttribute))
					{
						throw new InterceptorException("model " + model + " got no catalog version", this);
					}
					final Map<String, Object> uniqueKeyValues = new HashMap<String, Object>(info.uniqueKeys.size());
					for (final String k : info.uniqueKeys)
					{
						try
						{
							final Object val = ctx.getModelService().getAttributeValue(model, k);
							uniqueKeyValues.put(k, val);
						}
						catch (final Exception e)
						{
							throw new ModelValidationException("cannot read unique key attribute " + k + " due to " + e.getMessage());
						}
					}

					if (ret == null)
					{
						ret = new ArrayList<ModifiedCatalogItem>();
					}
					ret.add(new ModifiedCatalogItem(item, type, catalogVersionModel, uniqueKeyValues));
				}
			}
		}
		return ret != null ? ret : Collections.EMPTY_SET;
	}

	private boolean isInterceptorEnabledForType(final String type)
	{
		final Collection<ValidateInterceptor> validateInterceptors = interceptorRegistry.getValidateInterceptors(type);
		return validateInterceptors.stream().filter(i -> i != null).anyMatch(i -> i.getClass().equals(getClass()));
	}

	/**
	 * @return true if given attribute is mandatory (this check is essential since attributes might have been redeclared)
	 */
	private boolean isMandatory(final ItemModel model, final String atribute)
	{
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(model.getItemtype(), atribute);
		return !attributeDescriptor.getOptional().booleanValue();
	}

	protected CatalogVersionModel getDefaultCatalogVersion(final InterceptorContext ctx,
			@SuppressWarnings("unused") final Object model)
	{
		CatalogVersionModel ret = null;
		final Catalog def = CatalogManager.getInstance().getDefaultCatalog();
		if (def != null)
		{
			CatalogVersion defVer = def.getActiveCatalogVersion();
			if (defVer == null)
			{
				final Collection<CatalogVersion> all = def.getCatalogVersions();
				defVer = !all.isEmpty() ? all.iterator().next() : null;
			}
			if (defVer != null)
			{
				ret = ctx.getModelService().get(defVer);
			}
		}
		return ret;
	}

	private static class TypeInfo
	{
		private final String versionAttribute;
		private final Set<String> uniqueKeys;
		private boolean allUnique;

		TypeInfo(final String version, final Set<String> keys)
		{
			this.versionAttribute = version;
			this.uniqueKeys = keys;
		}

		boolean isCatalogItemType()
		{
			return versionAttribute != null;
		}
	}
}
