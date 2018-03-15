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
package de.hybris.platform.servicelayer.model.collector;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitorRegistry;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@IntegrationTest
public class DefaultRelatedItemsCollectorTest extends ServicelayerBaseTest
{

	@Resource
	private DefaultRelatedItemsCollector relatedItemsCollector;

	@Resource
	private ItemVisitorRegistry itemVisitorRegistry;

	@Resource
	private TypeService typeService;

	private CatalogModel catalog;
	private CatalogVersionModel sourceCatalogVersion;


	@Resource
	private ModelService modelService;


	private ProductModel product;
	private CategoryModel categoryModel;
	private UnitModel unitModel;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);


		catalog = createCatalog(String.format("%s%s", "test_catalog", RandomStringUtils.randomAlphanumeric(3)));
		sourceCatalogVersion = createCatalogVersion(
				String.format("%s%s", "test_source_version", RandomStringUtils.randomAlphanumeric(3)));
		product = createProductInCatalogVersion(String.format("%s%s", "test_product_", RandomStringUtils.randomAlphanumeric(3)),
				sourceCatalogVersion);

		categoryModel = modelService.create(CategoryModel.class);
		categoryModel.setCode("givenCategory");
		categoryModel.setCatalogVersion(sourceCatalogVersion);

		unitModel = modelService.create(UnitModel.class);
		unitModel.setUnitType("sampleUnit");
		unitModel.setCode(String.format("%s%s", "test_unit", RandomStringUtils.randomAlphanumeric(3)));
		unitModel.setName("unit");

		product.setSupercategories(Lists.newArrayList(categoryModel));
		product.setUnit(unitModel);

		itemVisitorRegistry.setVisitors(Maps.newHashMap());

		modelService.saveAll();

	}


	@Test
	public void testCollectRelatedItemsUsingDefaultCrawlerStrategy()
	{
		final Map<String, Object> context = new HashMap();


		// when
		final List<ItemModel> ret = relatedItemsCollector.collect(product, context);

		// then
		Assertions.assertThat(ret).isNotEmpty().hasSize(1);
		Assertions.assertThat(ret).containsSequence(product);
	}


	@Test
	public void testCollectRelatedItemsUsingGivenStrategy()
	{

		// when
		final Map<String, Object> firstCtx = new HashMap();
		final List<ItemModel> firstRet = relatedItemsCollector.collect(product,
				(theSource, parents, ctx) -> Lists.newArrayList(theSource.getSupercategories()), firstCtx);

		final Map<String, Object> secondCtx = new HashMap();
		final List<ItemModel> secondRet = relatedItemsCollector.collect(product, (theSource, parents, ctx) -> {
			final List<ItemModel> items = Lists.newArrayList(theSource.getSupercategories());
			items.add(theSource.getUnit());
			return items;
		}, secondCtx);

		// then
		Assertions.assertThat(firstRet).isNotEmpty().hasSize(2);
		Assertions.assertThat(firstRet).containsSequence(product, categoryModel);
		Assertions.assertThat(secondRet).isNotEmpty().hasSize(3);
		Assertions.assertThat(secondRet).containsSequence(product, categoryModel, unitModel);

	}

	@Test
	public void testCollectItemsUsingConfiguredStrategies()
	{

		// given
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("MyVariant");
		variantTypeModel.setSingleton(false);
		variantTypeModel.setGenerate(true);
		variantTypeModel.setCatalogItemType(false);

		product.setVariantType(variantTypeModel);
		modelService.save(product);

		final VariantProductModel productVariant = modelService.create(VariantProductModel.class);
		productVariant.setCatalogVersion(sourceCatalogVersion);
		productVariant.setBaseProduct(product);
		productVariant.setCode("variant");


		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode("sampleMedia");
		media.setCatalogVersion(sourceCatalogVersion);

		productVariant.setThumbnail(media);


		final MediaModel anotherMedia = modelService.create(MediaModel.class);
		anotherMedia.setCode("antoherMedia");
		anotherMedia.setCatalogVersion(sourceCatalogVersion);

		categoryModel.setThumbnail(anotherMedia);

		modelService.saveAll();

		final Map<String, Object> context = new HashMap();


		final Map<String, ItemVisitor<? extends ItemModel>> givenVisitors = Maps.newHashMap();
		givenVisitors.put(ProductModel._TYPECODE, createProductVisitor());
		givenVisitors.put(CategoryModel._TYPECODE, createCategoryVisitor());

		itemVisitorRegistry.setVisitors(givenVisitors);


		// when
		final List<ItemModel> collectedItems = relatedItemsCollector.collect(product, context);

		// then
		Assertions.assertThat(collectedItems).isNotEmpty().hasSize(6);
		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel, anotherMedia, productVariant, media,
				unitModel);


	}

	@Test
	public void testCollectItemsUsingConfiguredStrategiesFallbackToSubtypesNotAllowed()
	{

		// given
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel.class);
		variantTypeModel.setCode("MyVariant");
		variantTypeModel.setSingleton(false);
		variantTypeModel.setGenerate(true);
		variantTypeModel.setCatalogItemType(true);

		product.setVariantType(variantTypeModel);
		modelService.save(product);

		final VariantProductModel productVariant = modelService.create(VariantProductModel.class);
		productVariant.setCatalogVersion(sourceCatalogVersion);
		productVariant.setBaseProduct(product);
		productVariant.setCode("variant");

		final MediaModel firstThumbnail = modelService.create(MediaModel.class);
		firstThumbnail.setCode("firstThumbnail");
		firstThumbnail.setCatalogVersion(sourceCatalogVersion);

		final MediaModel secondThumbnail = modelService.create(MediaModel.class);
		secondThumbnail.setCode("secondThumbnail");
		secondThumbnail.setCatalogVersion(sourceCatalogVersion);


		final MediaModel thumbnail = modelService.create(MediaModel.class);
		thumbnail.setCode("sampleMedia");
		thumbnail.setCatalogVersion(sourceCatalogVersion);

		productVariant.setThumbnail(thumbnail);
		productVariant.setThumbnails(Lists.newArrayList(firstThumbnail, secondThumbnail));

		final MediaModel anotherModel = modelService.create(MediaModel.class);
		anotherModel.setCode("antoherMedia");
		anotherModel.setCatalogVersion(sourceCatalogVersion);

		categoryModel.setThumbnail(anotherModel);

		modelService.saveAll();
		final Map<String, Object> context = new HashMap();


		final Map<String, ItemVisitor<? extends ItemModel>> givenVisitors = Maps.newHashMap();
		givenVisitors.put(String.format("%s%s", ProductModel._TYPECODE, "!"), createProductVisitor());
		givenVisitors.put(CategoryModel._TYPECODE, createCategoryVisitor());

		itemVisitorRegistry.setVisitors(givenVisitors);

		// when
		List<ItemModel> collectedItems = relatedItemsCollector.collect(product, context);


		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel, anotherModel, productVariant, unitModel);


		// given
		givenVisitors.put("MyVariant",
				(ItemVisitor<VariantProductModel>) (theVariant, parents, ctx) -> Lists.newArrayList(theVariant.getThumbnails()));

		itemVisitorRegistry.setVisitors(givenVisitors);

		// when
		collectedItems = relatedItemsCollector.collect(product, context);


		// then
		Assertions.assertThat(collectedItems).isNotEmpty().hasSize(7);
		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel, anotherModel, productVariant, firstThumbnail,
				secondThumbnail, unitModel);

	}

	@Test
	public void testCatalogItemAwareCollectorLevel()
	{
		// given
		final MediaModel mediaModel = modelService.create(MediaModel.class);
		mediaModel.setCode("sampleMedia");
		mediaModel.setCatalogVersion(sourceCatalogVersion);

		final CategoryModel anotherCategory = modelService.create(CategoryModel.class);
		anotherCategory.setCode("anotherCategory");
		anotherCategory.setCatalogVersion(sourceCatalogVersion);
		mediaModel.setSupercategories(Lists.newArrayList(anotherCategory));

		categoryModel.setThumbnail(mediaModel);

		modelService.saveAll();

		final Map<String, Object> context = new HashMap();
		context.put(RelatedItemsCollector.MAX_RECURSION_DEPTH, 5);

		final Map<String, ItemVisitor<? extends ItemModel>> givenVisitors = Maps.newHashMap();
		givenVisitors.put(ItemModel._TYPECODE, createGenericCrawlerStrategy(Lists.newArrayList("superCategories", "thumbnail")));

		itemVisitorRegistry.setVisitors(givenVisitors);


		// when
		List<ItemModel> collectedItems = relatedItemsCollector.collect(product, context);

		// then
		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel, mediaModel, anotherCategory);

		// when
		context.put(RelatedItemsCollector.MAX_RECURSION_DEPTH, 2);
		collectedItems = relatedItemsCollector.collect(product, context);


		// then
		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel);


		// when
		context.put(RelatedItemsCollector.MAX_RECURSION_DEPTH, 1);
		collectedItems = relatedItemsCollector.collect(product, context);


		// then
		Assertions.assertThat(collectedItems).containsSequence(product);
	}

	@Test
	public void testCollectItemsUsingConfiguredStrategiesWhenCycleExist()
	{

		// given
		final MediaModel thumbnail = modelService.create(MediaModel.class);
		thumbnail.setCode("sampleMedia");
		thumbnail.setCatalogVersion(sourceCatalogVersion);
		thumbnail.setSupercategories(Collections.singletonList(categoryModel));

		categoryModel.setThumbnail(thumbnail);

		modelService.saveAll();

		final Map<String, Object> context = new HashMap();

		final Map<String, ItemVisitor<? extends ItemModel>> givenVisitors = Maps.newHashMap();
		givenVisitors.put(ItemModel._TYPECODE, createGenericCrawlerStrategy(Lists.newArrayList("superCategories", "thumbnail")));

		itemVisitorRegistry.setVisitors(givenVisitors);

		// when
		final List<ItemModel> collectedItems = relatedItemsCollector.collect(product, context);

		// then
		Assertions.assertThat(collectedItems).isNotEmpty().hasSize(3);
		Assertions.assertThat(collectedItems).containsSequence(product, categoryModel, thumbnail);
	}


	private ItemVisitor<ItemModel> createGenericCrawlerStrategy(final List<String> qualifiers)
	{
		return new GenericItemVisitorForTest(typeService, qualifiers);
	}


	private ItemVisitor<ProductModel> createProductVisitor()
	{

		return (theProduct, parents, ctx) -> {
			final List<ItemModel> ret = Lists.newArrayList(theProduct.getSupercategories());
			ret.addAll(theProduct.getVariants());
			ret.add(theProduct.getThumbnail());
			ret.add(theProduct.getUnit());
			return ret;
		};
	}

	private ItemVisitor<CategoryModel> createCategoryVisitor()
	{
		return (theCategory, parents, ctx) -> Lists.newArrayList(Collections.singletonList(theCategory.getThumbnail()));
	}

	private CatalogModel createCatalog(final String id)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);
		return catalog;
	}

	private CatalogVersionModel createCatalogVersion(final String version)
	{
		final CatalogVersionModel catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setCatalog(catalog);
		catalogVersionModel.setVersion(version);
		return catalogVersionModel;
	}


	private ProductModel createProductInCatalogVersion(final String theCode, final CatalogVersionModel catalogVersion)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(theCode);
		product.setCatalogVersion(catalogVersion);
		return product;
	}
}
