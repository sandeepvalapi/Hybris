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
package de.hybris.platform.europe1;


import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.model.ModelService;


import java.util.UUID;

public class PDTRowTestDataBuilder
{
	private final ModelService modelService;

	public PDTRowTestDataBuilder(final ModelService modelService) {
		this.modelService = modelService;
	}

	public EnumerationValueModel createProductPriceGroup(final String priceGroupCode)
	{
		final EnumerationValueModel productPriceGroup = modelService.create(ProductPriceGroup._TYPECODE);
		productPriceGroup.setCode(priceGroupCode);
		productPriceGroup.setName("ProductPriceGroup");
		modelService.save(productPriceGroup);

		return productPriceGroup;
	}

	public EnumerationValueModel createProductDiscountGroup(final String discountGroupCode)
	{
		final EnumerationValueModel productDiscountGroup = modelService.create(ProductDiscountGroup._TYPECODE);
		productDiscountGroup.setCode(discountGroupCode);
		productDiscountGroup.setName("ProductPriceGroup");
		modelService.save(productDiscountGroup);

		return productDiscountGroup;
	}

	public EnumerationValueModel createUserPriceGroup(final String priceGroupCode)
	{
		final EnumerationValueModel userPriceGroup = modelService.create(UserPriceGroup._TYPECODE);
		userPriceGroup.setCode(priceGroupCode);
		userPriceGroup.setName("userPriceGroup");
		modelService.save(userPriceGroup);

		return userPriceGroup;
	}

	public CurrencyModel createCurrency(final String isocode, final String symbol)
	{
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode(isocode);
		currency.setSymbol(symbol);

		modelService.save(currency);
		return currency;
	}

	public UserModel createUser(final String uid)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(uid);
		modelService.save(user);

		return user;
	}

	public UnitModel createUnit(final String code, final String unitType)
	{
		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode(code);
		unit.setUnitType(unitType);
		modelService.save(unit);
		return unit;
	}

	public ProductModel createProduct(final String code)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setActive(Boolean.TRUE);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		modelService.save(catalog);
		modelService.save(catalogVersion);

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setCatalogVersion(catalogVersion);
		modelService.save(product);

		return product;
	}

}
