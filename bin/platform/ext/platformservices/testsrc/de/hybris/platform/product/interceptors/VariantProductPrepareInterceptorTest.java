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
package de.hybris.platform.product.interceptors;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class VariantProductPrepareInterceptorTest extends ServicelayerTransactionalTest
{
	@Resource
	ModelService modelService;

	@Resource
	ProductService productService;

	private ProductModel baseProduct1;
	private ProductModel baseProduct2;
	private VariantProductModel existingVaraint;

	@Before
	public void setUp()
	{
		final VariantTypeModel varaintType = modelService.create(VariantTypeModel.class);
		varaintType.setCode("TestVariant");
		varaintType.setCatalogItemType(Boolean.FALSE);
		varaintType.setGenerate(Boolean.FALSE);
		varaintType.setSingleton(Boolean.FALSE);
		modelService.save(varaintType);

		final CatalogModel catalog = createCatalog("TestCatalog");
		final CatalogVersionModel catalogVersion = createCatalogVersion(catalog, "online");
		final CategoryModel categoryModel1 = createCategory(catalogVersion, "categoryModel1");
		final CategoryModel categoryModel2 = createCategory(catalogVersion, "categoryModel2");

		baseProduct1 = createProduct(catalogVersion, "baseProduct1", categoryModel1, categoryModel2);
		baseProduct1.setName("Base Product 1");
		baseProduct1.setVariantType(varaintType);
		modelService.save(baseProduct1);

		baseProduct2 = createProduct(catalogVersion, "baseProduct2", categoryModel1, categoryModel2);
		baseProduct2.setName("Base Product 2");
		baseProduct2.setVariantType(varaintType);
		modelService.save(baseProduct2);

		existingVaraint = createNewProductVaraint("existingVaraint", baseProduct1);
		modelService.save(existingVaraint);

        shiftModifiedTimeToEnsureBaseProductCreatedBeforeVariant(baseProduct1, 5);
        shiftModifiedTimeToEnsureBaseProductCreatedBeforeVariant(baseProduct2, 5);
	}

	private void shiftModifiedTimeToEnsureBaseProductCreatedBeforeVariant(final ItemModel item, final int seconds)
    {
        final DateTime shifted = new DateTime(item.getModifiedtime()).minusSeconds(seconds);
        item.setModifiedtime(shifted.toDate());
        modelService.save(item);
    }

	@Test
	public void shouldUpdateModifiedTimeOfProductWhenNewProductVariantIsAttached()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		final VariantProductModel newVaraint = createNewProductVaraint("newVariant", baseProduct1);
		modelService.save(newVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.after(original)).overridingErrorMessage("Base product modified time should be updated.").isTrue();
	}

	@Test
	public void shouldUpdateModifiedTimeOfProductWhenAttachedProductVariantIsRemoved()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		modelService.remove(existingVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.after(original)).overridingErrorMessage("Base product modified time should be updated.").isTrue();
	}

	@Test
	public void shouldUpdateModifiedTimeOfProductWhenAttachedProductVariantIsDetached()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		existingVaraint.setBaseProduct(baseProduct2);
		modelService.save(existingVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.after(original)).overridingErrorMessage("Base product modified time should be updated.").isTrue();
	}

	@Test
	public void shouldUpdateModifiedTimeOfProductWhenExistingProductVariantIsAttached()
	{
		//given
		final Date original = baseProduct2.getModifiedtime();

		//when
		existingVaraint.setBaseProduct(baseProduct2);
		modelService.save(existingVaraint);

		//then
		refresh(baseProduct2);
		final Date changed = productService.getProductForCode(baseProduct2.getCode()).getModifiedtime();
		assertThat(changed.after(original)).overridingErrorMessage("Base product modified time should be updated.").isTrue();
	}

	@Test
	public void shouldNotUpdateModifiedTimeOfProductWhenAtacchedProductVariantIsChanging()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		existingVaraint.setName("Changed name");
		modelService.save(existingVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.equals(original)).overridingErrorMessage("Base product modified time should not be updated.").isTrue();
	}

	@Test
	public void shouldUpdateModifiedTimeOfProductWhenAtacchedProductVariantHasBeenDetachedAndFinallyRemoved()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		existingVaraint.setBaseProduct(null);
		modelService.remove(existingVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.after(original)).overridingErrorMessage("Base product modified time should not be updated.").isTrue();
	}

	private void refresh(final ItemModel item)
	{
		final AbstractItemModel cat2;
		modelService.detach(item);
		Assert.assertFalse(modelService.isAttached(item));
		cat2 = modelService.get(item.getPk());
		Assert.assertNotSame(cat2, item);
	}

	@Test
	public void shouldNotUpdateModifiedTimeOfProductWhenAtacchedProductVariantHasNotChanged()
	{
		//given
		final Date original = baseProduct1.getModifiedtime();

		//when
		modelService.save(existingVaraint);

		//then
		refresh(baseProduct1);
		final Date changed = productService.getProductForCode(baseProduct1.getCode()).getModifiedtime();
		assertThat(changed.equals(original)).overridingErrorMessage("Base product modified time should not be updated.").isTrue();
	}

	private VariantProductModel createNewProductVaraint(final String code, final ProductModel baseProduct)
	{
		final VariantProductModel variant = modelService.create(VariantProductModel.class);
		variant.setCode(code);
		variant.setCatalogVersion(baseProduct.getCatalogVersion());
		variant.setVariantType(baseProduct.getVariantType());
		variant.setBaseProduct(baseProduct);
		return variant;
	}

	private CatalogModel createCatalog(final String id)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);

		return catalog;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog, final String version)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(version);
		catalogVersion.setCatalog(catalog);
		catalog.setCatalogVersions(Collections.singleton(catalogVersion));

		return catalogVersion;
	}

	private ProductModel createProduct(final CatalogVersionModel catalogVersionModel, final String code,
			final CategoryModel... categories)
	{
		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCode(code);
		productModel.setCatalogVersion(catalogVersionModel);
		if (categories != null && categories.length > 0)
		{
			productModel.setSupercategories(Arrays.asList(categories));
		}
		return productModel;
	}

	private CategoryModel createCategory(final CatalogVersionModel catalogVersionModel, final String code)
	{
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode(code);
		cat.setCatalogVersion(catalogVersionModel);
		return cat;
	}

}
