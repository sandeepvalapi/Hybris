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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static java.lang.String.format;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link ProductService}.
 */
@SuppressWarnings("deprecation")
public class DefaultProductService extends AbstractBusinessService implements ProductService
{
	/**
	 * @deprecated since ages - as of release, will be no usable when deprecated methods are removed
	 */
	@Deprecated
	private PriceService priceService;
	/**
	 * @deprecated since ages - as of release, will be no usable when deprecated methods are removed
	 */
	@Deprecated
	private UnitDao unitDao;

	private ProductDao productDao;

	@Override
	public ProductModel getProductForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<ProductModel> products = productDao.findProductsByCode(code);

		validateIfSingleResult(products, format("Product with code '%s' not found!", code),
				format("Product code '%s' is not unique, %d products found!", code, Integer.valueOf(products.size())));

		return products.get(0);
	}

	/**
	 * @deprecated since ages
	 */
	@Override
	@Deprecated
	public ProductModel getProduct(final String code)
	{
		return getProductForCode(code);
	}

	/**
	 * @deprecated since ages
	 */
	@Override
	@Deprecated
	public UnitModel getOrderableUnit(final ProductModel product)
	{
		validateParameterNotNull(product, "Product must not be null!");
		final List<PriceInformation> priceInfos = this.priceService.getPriceInformationsForProduct(product);

		UnitModel ret = null;
		for (final PriceInformation priceInfo : priceInfos)
		{
			final PriceRow priceRow = (PriceRow) priceInfo.getQualifierValue(PriceRow.PRICEROW);
			final Unit unit = priceRow != null ? priceRow.getUnit() : null;
			if (unit != null)
			{
				ret = getModelService().get(unit);
				break;
			}
		}
		// fall back to product unit
		if (ret == null)
		{
			ret = product.getUnit();
		}
		if (ret == null)
		{
			throw new ModelNotFoundException("No orderable unit found for this product!");
		}
		return ret;
	}

	@Override
	public Integer getAllProductsCountForCategory(final CategoryModel category)
	{
		validateParameterNotNullStandardMessage("category", category);
		return productDao.findAllProductsCountByCategory(category);
	}

	@Override
	public Integer getProductsCountForCategory(final CategoryModel category) {
		validateParameterNotNullStandardMessage("category", category);
		return productDao.findProductsCountByCategory(category);
	}

	@Override
	public boolean containsProductsForCategory(final CategoryModel category)
	{
		return (getAllProductsCountForCategory(category).intValue() > 0);
	}

	/**
	 * @deprecated since ages
	 */
	@Override
	@Deprecated
	public UnitModel getUnit(final String code)
	{
		final Set<UnitModel> units = unitDao.findUnitsByCode(code);

		validateIfSingleResult(units, format("Unit with code '%s' not found!", code),
				format("Code '%s' is not unique, %d units found!", code, Integer.valueOf(units.size())));

		return units.iterator().next();
	}

	/**
	 * @deprecated since ages
	 */
	@Override
	@Deprecated
	public ProductModel getProduct(final CatalogVersionModel catalogVersion, final String code)
	{
		final JaloSession session = JaloSession.getCurrentSession();
		session.createLocalSessionContext();
		try
		{
			final Collection<CatalogVersion> cvs = (Collection<CatalogVersion>) session
					.getAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS);

			if (cvs == null || cvs.isEmpty() || (cvs.size() == 1 && cvs.contains(CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY)))
			{
				final List<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();
				catalogVersions.add(catalogVersion);
				session.setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, catalogVersions);
			}
			final List<ProductModel> products = productDao.findProductsByCode(catalogVersion, code);

			validateIfSingleResult(
					products,
					format("Product with code '%s' and CatalogVersion '%s.%s' not found!", code, catalogVersion.getCatalog().getId(),
							catalogVersion.getVersion()),
					format("Code '%s' and CatalogVersion '%s.%s' are not unique. %d products found!", code, catalogVersion
							.getCatalog().getId(), catalogVersion.getVersion(), Integer.valueOf(products.size())));

			return products.get(0);
		}
		finally
		{
			session.removeLocalSessionContext();
		}
	}

	@Override
	public ProductModel getProductForCode(final CatalogVersionModel catalogVersion, final String code)
	{
		final List<ProductModel> products = productDao.findProductsByCode(catalogVersion, code);

		validateIfSingleResult(
                products,
                format("Product with code '%s' and CatalogVersion '%s.%s' not found!", code, catalogVersion.getCatalog().getId(),
                        catalogVersion.getVersion()),
                format("Code '%s' and CatalogVersion '%s.%s' are not unique. %d products found!", code, catalogVersion.getCatalog()
                        .getId(), catalogVersion.getVersion(), Integer.valueOf(products.size())));

		return products.get(0);
	}

   /**
 * @deprecated since ages
 */
@Override
	@Deprecated
	public List<ProductModel> getProducts(final CategoryModel category)
	{
		return getProductsForCategory(category);
	}

	@Override
	public List<ProductModel> getProductsForCategory(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter category was null");
		return getProductsForCategory(category, 0, -1);
	}

	/**
	 * @deprecated since ages
	 */
	@Override
	@Deprecated
	public SearchResult<ProductModel> getProducts(final CategoryModel category, final int start, final int count)
	{
		validateParameterNotNull(category, "Parameter category was null");
		return productDao.findProductsByCategory(category, start, count);
	}

	@Override
	public List<ProductModel> getProductsForCategory(final CategoryModel category, final int start, final int count)
	{
		validateParameterNotNull(category, "Parameter category was null");
		return productDao.findProductsByCategory(category, start, count).getResult();
	}

	@Override
	public List<ProductModel> getOnlineProductsForCategory(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter category was null");
		return productDao.findOnlineProductsByCategory(category);
	}

	@Override
	public List<ProductModel> getOfflineProductsForCategory(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter category was null");
		return productDao.findOfflineProductsByCategory(category);
	}

	@Override
	public List<ProductModel> getAllProductsForCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		validateParameterNotNull(catalogVersion, "Parameter catalogVersion was null");
		return productDao.findProductsByCatalogVersion(catalogVersion);
	}

	@Required
	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}

	/**
	 * @return the productDao
	 */
	protected ProductDao getProductDao()
	{
		return productDao;
	}

	/**
	 * @deprecated since ages
	 */
	@Required
	@Deprecated
	public void setUnitDao(final UnitDao unitDao)
	{
		this.unitDao = unitDao;
	}

	/**
	 * @deprecated since ages
	 */
	@Required
	@Deprecated
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

}
