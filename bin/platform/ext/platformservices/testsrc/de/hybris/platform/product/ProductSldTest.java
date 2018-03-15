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
package de.hybris.platform.product;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.directpersistence.impl.DefaultJaloAccessorsService;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.persistence.PersistenceUtils;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Direct Persistence tests for product
 */
@IntegrationTest
public class ProductSldTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	private ClassificationSystemVersionModel cvm;
	private CatalogVersionModel testCatalogVersion;
	private UnitModel testUnit;
	private ClassAttributeAssignmentModel caam;
	private VariantTypeModel variantType;


	private final PropertyConfigSwitcher allSafeConfig = new PropertyConfigSwitcher(
			DefaultJaloAccessorsService.CFG_ALL_SAFE_4_WRITING);

	@Before
	public void setUp() throws Exception
	{
		final CatalogModel catalog1 = modelService.create(CatalogModel.class);
		catalog1.setId("catalog1");
		modelService.save(catalog1);

		testCatalogVersion = modelService.create(CatalogVersionModel.class);
		testCatalogVersion.setCatalog(catalog1);
		testCatalogVersion.setVersion("v1.0");

		testUnit = modelService.create(UnitModel.class);
		testUnit.setCode("kg");
		testUnit.setUnitType("test");
		modelService.saveAll();

		final ClassificationSystemModel csm = modelService.create(ClassificationSystemModel.class);
		csm.setId("modelSystemFoo_a");

		cvm = modelService.create(ClassificationSystemVersionModel.class);
		cvm.setCatalog(csm);
		cvm.setVersion("ver1.0");

		final ClassificationClassModel ccm = modelService.create(ClassificationClassModel.class);
		ccm.setCatalogVersion(cvm);
		ccm.setCode("ver1.0");

		final ClassificationAttributeModel cam = modelService.create(ClassificationAttributeModel.class);
		cam.setCode("attrModelFoo_a");
		cam.setSystemVersion(cvm);

		caam = modelService.create(ClassAttributeAssignmentModel.class);
		caam.setClassificationAttribute(cam);
		caam.setClassificationClass(ccm);

		variantType = createVariantType();

		modelService.saveAll();

		allSafeConfig.switchToValue("true");
	}

	@After
	public void TearDown() throws Exception
	{
		allSafeConfig.switchBackToDefault();
	}


	@Test
	public void shouldSaveProductForPriceRows()
	{
		final PriceRowModel priceRow1 = modelService.create(PriceRowModel.class);
		priceRow1.setUnit(testUnit);
		priceRow1.setCurrency(commonI18NService.getBaseCurrency());
		priceRow1.setPrice(Double.valueOf(2.3));
		final PriceRowModel priceRow2 = modelService.create(PriceRowModel.class);
		priceRow2.setUnit(testUnit);
		priceRow2.setCurrency(commonI18NService.getBaseCurrency());
		priceRow2.setPrice(Double.valueOf(20.5));
		modelService.saveAll(priceRow1, priceRow2);

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductModel product = createProduct("testProduct123", testCatalogVersion);
			assertThat(product.getOwnEurope1Prices()).isNullOrEmpty();
			assertThat(product.getEurope1Prices()).containsOnly(priceRow1, priceRow2);
			final PriceRowModel ownPriceRow = modelService.create(PriceRowModel.class);
			ownPriceRow.setUnit(testUnit);
			ownPriceRow.setCurrency(commonI18NService.getBaseCurrency());
			ownPriceRow.setPrice(Double.valueOf(20.5));
			ownPriceRow.setProduct(product);

			product.setEurope1Prices(Arrays.asList(priceRow1, priceRow2, ownPriceRow));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getOwnEurope1Prices()).containsOnly(ownPriceRow);
			assertThat(product.getEurope1Prices()).containsOnly(priceRow1, priceRow2, ownPriceRow);

			product.setEurope1Prices(Collections.singletonList(priceRow1));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getOwnEurope1Prices()).isNullOrEmpty();
			assertThat(product.getEurope1Prices()).containsOnly(priceRow1, priceRow2);

			return null;
		});
	}

	@Test
	public void shouldSaveProductForDiscountRows()
	{
		final DiscountModel testDiscount1 = modelService.create(DiscountModel.class);
		testDiscount1.setCode("DSC1");
		final DiscountModel testDiscount2 = modelService.create(DiscountModel.class);
		testDiscount2.setCode("DSC2");
		modelService.saveAll(testDiscount1, testDiscount2);
		final DiscountRowModel discountRow1 = modelService.create(DiscountRowModel.class);
		discountRow1.setCatalogVersion(testCatalogVersion);
		discountRow1.setCurrency(commonI18NService.getBaseCurrency());
		discountRow1.setAsTargetPrice(true);
		discountRow1.setDiscount(testDiscount1);
		final DiscountRowModel discountRow2 = modelService.create(DiscountRowModel.class);
		discountRow2.setCatalogVersion(testCatalogVersion);
		discountRow2.setCurrency(commonI18NService.getBaseCurrency());
		discountRow2.setDiscount(testDiscount2);

		modelService.saveAll(discountRow1, discountRow2);

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductModel product = createProduct("testProduct123", testCatalogVersion);
			assertThat(product.getOwnEurope1Discounts()).isNullOrEmpty();
			assertThat(product.getEurope1Discounts()).containsOnly(discountRow1, discountRow2);
			final DiscountRowModel ownDiscountRow = modelService.create(DiscountRowModel.class);
			ownDiscountRow.setCatalogVersion(testCatalogVersion);
			ownDiscountRow.setCurrency(commonI18NService.getBaseCurrency());
			ownDiscountRow.setDiscount(testDiscount2);
			ownDiscountRow.setProduct(product);
			product.setEurope1Discounts(Arrays.asList(discountRow1, discountRow2, ownDiscountRow));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getOwnEurope1Discounts()).containsOnly(ownDiscountRow);
			assertThat(product.getEurope1Discounts()).containsOnly(discountRow1, discountRow2, ownDiscountRow);

			product.setEurope1Discounts(Collections.singletonList(discountRow1));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getOwnEurope1Discounts()).isNullOrEmpty();
			assertThat(product.getEurope1Discounts()).containsOnly(discountRow1, discountRow2);

			return null;
		});
	}

	@Test
	public void shouldSaveProductForTaxRows()
	{
		final TaxModel tax1 = modelService.create(TaxModel.class);
		tax1.setCode("testTax1");
		final TaxModel tax2 = modelService.create(TaxModel.class);
		tax2.setCode("testTax2");
		modelService.saveAll(tax1, tax2);

		final TaxRowModel taxRow1 = modelService.create(TaxRowModel.class);
		taxRow1.setCatalogVersion(testCatalogVersion);
		taxRow1.setCurrency(commonI18NService.getBaseCurrency());
		taxRow1.setValue(Double.valueOf(22.2));
		taxRow1.setTax(tax1);
		final TaxRowModel taxRow2 = modelService.create(TaxRowModel.class);
		taxRow2.setCatalogVersion(testCatalogVersion);
		taxRow2.setCurrency(commonI18NService.getBaseCurrency());
		taxRow2.setValue(Double.valueOf(55.5));
		taxRow2.setTax(tax2);

		modelService.saveAll(taxRow1, taxRow2);

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductModel product = createProduct("testProduct123", testCatalogVersion);
			assertThat(product.getOwnEurope1Taxes()).isNullOrEmpty();
			assertThat(product.getEurope1Taxes()).containsOnly(taxRow1, taxRow2);
			final TaxRowModel ownTaxRow = modelService.create(TaxRowModel.class);
			ownTaxRow.setCatalogVersion(testCatalogVersion);
			ownTaxRow.setCurrency(commonI18NService.getBaseCurrency());
			ownTaxRow.setValue(Double.valueOf(22.2));
			ownTaxRow.setTax(tax1);
			ownTaxRow.setProduct(product);

			product.setEurope1Taxes(Arrays.asList(taxRow1, taxRow2, ownTaxRow));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getEurope1Taxes()).containsOnly(taxRow1, taxRow2, ownTaxRow);

			product.setEurope1Taxes(Collections.singletonList(taxRow1));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getOwnEurope1Taxes()).isNullOrEmpty();
			assertThat(product.getEurope1Taxes()).containsOnly(taxRow1, taxRow2);

			return null;
		});
	}

	@Test
	public void shouldSaveAndRemoveTypedAndUntypedFeaturesWithProduct()
	{
		final ProductModel product = createProduct("testProduct123", testCatalogVersion);
		final ProductFeatureModel untypedProductFeature1 = createProductFeature("testUntypedProdFeature1", "21", product);
		final ProductFeatureModel untypedProductFeature2 = createProductFeature("testUntypedProdFeature2", "22", product);

		final ProductFeatureModel typedProductFeature1 = createProductFeature("testTypedProdFeature1", "31", product);
		final ProductFeatureModel typedProductFeature2 = createProductFeature("testTypedProdFeature2", "32", product);
		typedProductFeature1.setClassificationAttributeAssignment(caam);
		typedProductFeature2.setClassificationAttributeAssignment(caam);

		final List<ProductFeatureModel> untypedFeatures = Arrays.asList(untypedProductFeature1, untypedProductFeature2);
		final List<ProductFeatureModel> typedFeatures = Arrays.asList(typedProductFeature1, typedProductFeature2);

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			assertThat(product.getFeatures()).isNullOrEmpty();
			assertThat(product.getUntypedFeatures()).isNullOrEmpty();
			product.setFeatures(typedFeatures);
			product.setUntypedFeatures(untypedFeatures);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getFeatures()).containsOnly(typedProductFeature1, typedProductFeature2, untypedProductFeature1,
					untypedProductFeature2);
			assertThat(product.getUntypedFeatures()).containsOnly(untypedProductFeature1, untypedProductFeature2);

			product.setUntypedFeatures(Collections.singletonList(untypedProductFeature1));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getFeatures()).containsOnly(typedProductFeature1, typedProductFeature2, untypedProductFeature1);
			assertThat(product.getUntypedFeatures()).containsOnly(untypedProductFeature1);

			return null;
		});
	}

	@Test
	public void shouldSaveProductWithClassificationClasses()
	{
		final ClassificationClassModel testClClass1 = createClassificationClass(cvm, "testClClass1");
		final ClassificationClassModel testClClass2 = createClassificationClass(cvm, "testClClass2");

		modelService.saveAll(testClClass1, testClClass2);

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductModel product = createProduct("testProduct123", testCatalogVersion);
			assertThat(product.getClassificationClasses()).isNullOrEmpty();
			product.setSupercategories(Arrays.asList(testClClass1, testClClass2));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, product);

			modelService.refresh(product);
			assertThat(product).isNotNull();
			assertThat(product.getClassificationClasses()).containsOnly(testClClass1, testClClass2);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(product);

			return null;
		});
	}

	@Test
	public void shouldSaveBaseProductAndVariantProducts()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {

			final ProductModel baseProduct = createProduct("testProduct123", testCatalogVersion);
			baseProduct.setVariantType(variantType);
			final VariantProductModel variantProduct1 = createVariantProduct("testVariant1", testCatalogVersion, baseProduct);
			final VariantProductModel variantProduct2 = createVariantProduct("testVariant2", testCatalogVersion, baseProduct);
			final VariantProductModel variantProduct3 = createVariantProduct("testVariant3", testCatalogVersion, baseProduct);

			assertThat(baseProduct.getVariants()).isNullOrEmpty();
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, baseProduct);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, variantProduct1);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, variantProduct2);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, variantProduct3);

			modelService.refresh(baseProduct);
			assertThat(baseProduct).isNotNull();
			assertThat(baseProduct.getVariants()).containsOnly(variantProduct1, variantProduct2, variantProduct3);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(baseProduct);

			return null;
		});
	}

	private ProductModel createProduct(final String code, final CatalogVersionModel catalogVersion)
	{
		final ProductModel model = modelService.create(ProductModel.class);
		model.setCode(code);
		model.setCatalogVersion(catalogVersion);
		return model;
	}

	private ProductFeatureModel createProductFeature(final String qualifier, final Object value,
			final ProductModel productForFeature)
	{
		final ProductFeatureModel model = modelService.create(ProductFeatureModel.class);
		model.setProduct(productForFeature);
		model.setValue(value);
		model.setQualifier(qualifier);
		return model;
	}

	private ClassificationClassModel createClassificationClass(final CatalogVersionModel catalogVersionModel, final String code)
	{
		final ClassificationClassModel clclm = modelService.create(ClassificationClassModel.class);
		clclm.setCode(code);
		clclm.setCatalogVersion(catalogVersionModel);
		return clclm;
	}

	private VariantTypeModel createVariantType()
	{
		final VariantTypeModel variantType = modelService.create(VariantTypeModel.class);
		variantType.setCode("vt");
		variantType.setSingleton(Boolean.FALSE);
		variantType.setGenerate(Boolean.TRUE);
		variantType.setCatalogItemType(Boolean.FALSE);
		return variantType;
	}

	private VariantProductModel createVariantProduct(final String code, final CatalogVersionModel catalogVersion,
			final ProductModel baseProduct)
	{
		final VariantProductModel variant = modelService.create(VariantProductModel.class);
		variant.setCode(code);
		variant.setCatalogVersion(catalogVersion);
		variant.setBaseProduct(baseProduct);
		return variant;
	}

}
