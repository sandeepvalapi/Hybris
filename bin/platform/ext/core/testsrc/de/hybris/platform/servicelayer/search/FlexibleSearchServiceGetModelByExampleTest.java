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
package de.hybris.platform.servicelayer.search;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelTypeNotSupportedException;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;

import java.util.Collections;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * The {@link FlexibleSearchService#getModelByExample(Object)} method will get a model (representing a persisted item)
 * by passing an example model with partly filled values. For gathering the correct model a flexiblesearch has to be
 * generated from filled model values to gather the persisted model.
 */
@IntegrationTest
public class FlexibleSearchServiceGetModelByExampleTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ItemObjectResolver modelResolver;

	private ProductModel testProduct0;
	private ProductModel testProduct1;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("test");
		modelService.save(catalog);

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("test");
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		final ProductModel product1 = modelService.create(ProductModel.class);
		product1.setCode("testProduct0");
		product1.setCatalogVersion(catalogVersion);
		product1.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		modelService.save(product1);

		final ProductModel product2 = modelService.create(ProductModel.class);
		product2.setCode("testProduct1");
		product2.setCatalogVersion(catalogVersion);
		modelService.save(product2);

		testProduct0 = modelService.get(ProductManager.getInstance().getProductsByCode("testProduct0").iterator().next());
		testProduct1 = modelService.get(ProductManager.getInstance().getProductsByCode("testProduct1").iterator().next());
	}

	/**
	 * A null value is not permitted and will throw a {@link IllegalArgumentException}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithNullFail() //NOPMD
	{
		flexibleSearchService.getModelByExample(null);
	}

	/**
	 * An example without any values set is not permitted and will throw a {@link ModelLoadingException}.
	 */
	@Test(expected = ModelLoadingException.class)
	public void testWithNoValuesFail() //NOPMD
	{
		final ProductModel exampleModel = new ProductModel();
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search by unique identifiers should succeed if there is exactly one entry fulfilling unique attribute values.
	 * The returned model is not same the passed one. If the entry was accessed within request already, the cached
	 * instance should be returned (here the model of {@link #setUp()} method.
	 */
	@Test
	public void testWithUniqueValuesSuccess()
	{
		final ProductModel exampleModel = new ProductModel();
		exampleModel.setCode("testProduct0");
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	/**
	 * A search with models attached to session can be used. Default values are also used as search params.
	 */
	@Test(expected = ModelNotFoundException.class)
	public void testWithUniqueValuesAttachedToSessionFail() //NOPMD
	{
		final ProductModel exampleModel = modelService.create(ProductModel.class);
		assertEquals(ArticleApprovalStatus.CHECK, exampleModel.getApprovalStatus());
		assertNull(exampleModel.getCatalogVersion());
		exampleModel.setCode("testProduct0");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with models attached to session can be used. Default values are also used as search params.
	 */
	@Test
	public void testWithUniqueValuesAttachedToSessionSuccess()
	{
		testProduct0.setApprovalStatus(ArticleApprovalStatus.CHECK);
		modelService.save(testProduct0);

		final ProductModel exampleModel = modelService.create(ProductModel.class);
		exampleModel.setCode("testProduct0");
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	/**
	 * If no entry with unique attribute set can be found a {@link ModelNotFoundException} will be thrown.
	 */
	@Test(expected = ModelNotFoundException.class)
	public void testWithUniqueValuesFail() //NOPMD
	{
		final ProductModel exampleModel = new ProductModel();
		exampleModel.setCode("test");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with unique and optional values set should succeed in general where the optional values will be used too
	 * at query. Restrictions to kind of optional values will be defined in following test cases. The resulting model
	 * should be not same as passed one (because of model cache).
	 */
	@Test
	public void testWithUniqueAndOptionalValuesSuccess()
	{
		testProduct0.setEan("testEAN");
		modelService.save(testProduct0);

		final ProductModel exampleModel = new ProductModel();
		exampleModel.setCode("testProduct0");
		exampleModel.setEan("testEAN");
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	/**
	 * A search with not matching optional values will throw a {@link ModelNotFoundException}.
	 */
	@Test(expected = ModelNotFoundException.class)
	public void testWithUniqueAndOptionalValuesFail() //NOPMD
	{
		final ProductModel exampleModel = new ProductModel();
		exampleModel.setCode("testProduct0");
		exampleModel.setEan("bla");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with only optional values set will succeed in general (like for unique values), restrictions on kind of
	 * optional attributes will be covered in later cases.
	 */
	@Test
	public void testWithOptionalValuesSuccess()
	{
		testProduct0.setEan("testEAN");
		modelService.save(testProduct0);

		final ProductModel exampleModel = new ProductModel();
		exampleModel.setEan("testEAN");
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	/**
	 * Search with localized values is possible.
	 */
	@Test
	public void testWithOptionalValuesLocalizedSuccess()
	{
		testProduct0.setName("testName");
		modelService.save(testProduct0);

		final ProductModel exampleModel = new ProductModel();
		modelService.attach(exampleModel);
		exampleModel.setName("testName");
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	/**
	 * Search with localized values is possible.
	 */
	@Test(expected = AmbiguousIdentifierException.class)
	public void testWithOptionalValuesLocalizedFail() //NOPMD
	{
		testProduct0.setName("testName");
		modelService.save(testProduct0);
		testProduct1.setName("testName");
		modelService.save(testProduct1);

		final ProductModel exampleModel = new ProductModel();
		modelService.attach(exampleModel);
		exampleModel.setName("testName");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with a wrong set of optional values set (no related entry in system) will fail with
	 * {@link ModelNotFoundException}.
	 */
	@Test(expected = ModelNotFoundException.class)
	public void testWithOptionalValuesFail() //NOPMD
	{
		final ProductModel exampleModel = new ProductModel();
		exampleModel.setEan("bla");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with an ambiguous set of optional values will will with {@link AmbiguousIdentifierException}.
	 */
	@Test(expected = AmbiguousIdentifierException.class)
	public void testWithOptionalValuesAmbigiuous() //NOPMD
	{
		testProduct0.setEan("testEAN");
		modelService.save(testProduct0);
		testProduct1.setEan("testEAN");
		modelService.save(testProduct1);

		final ProductModel exampleModel = new ProductModel();
		exampleModel.setEan("testEAN");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search with an already persisted model will fail with a {@link ModelLoadingException}.
	 */
	@Test(expected = ModelLoadingException.class)
	public void testWithSavedExample() //NOPMD
	{
		flexibleSearchService.getModelByExample(testProduct0);
	}

	/**
	 * A search with a wrong set of optional values set (no related entry in system) will fail with
	 * {@link ModelNotFoundException}.
	 */
	@Test(expected = ModelLoadingException.class)
	public void testWithNotSearchableValue() //NOPMD
	{
		final ProductModel exampleModel = new ProductModel();
		exampleModel.setFeatures(Collections.EMPTY_LIST);
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * A search using a super model will succeed and return correct sub model.
	 */
	@Test
	public void testWithSuperTypeSuccess() //NOPMD
	{
		final PrincipalModel exampleModel = new PrincipalModel();
		exampleModel.setUid("admin");
		final PrincipalModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertTrue("Loaded model has to be instance of employee", loadedModel instanceof EmployeeModel);
		assertEquals("Loaded model has different uid then example model", loadedModel.getUid(), exampleModel.getUid());
	}

	/**
	 * At search the composedtype of passed model has to be respected, means only models with that type or subtypes will
	 * be returned.
	 */
	@Test(expected = ModelNotFoundException.class)
	public void testWithWrongType() //NOPMD
	{
		final CustomerModel exampleModel = new CustomerModel();
		exampleModel.setUid("admin");
		flexibleSearchService.getModelByExample(exampleModel);
	}

	/**
	 * Just in case: an exception should be thrown if the example isn't a model. In this case the exception is thrown by
	 * the model converter registry.
	 */
	@Test(expected = ModelTypeNotSupportedException.class)
	public void testWithNoModel() //NOPMD
	{
		final Product prod0 = modelService.getSource(testProduct0);
		flexibleSearchService.getModelByExample(prod0);
	}

	@Test
	public void testWithSomeOptionalValuesLocalizedSuccess()
	{
		testProduct0.setName("testName_de1", Locale.GERMAN);
		testProduct0.setName("testName_en1", Locale.ENGLISH);
		modelService.save(testProduct0);
		testProduct1.setName("testName_de1", Locale.GERMAN);
		modelService.save(testProduct1);

		final ProductModel exampleModel = new ProductModel();
		modelService.attach(exampleModel);
		exampleModel.setName("testName_de1", Locale.GERMAN);
		exampleModel.setName("testName_en1", Locale.ENGLISH);
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testWithSomeOptionalValuesLocalizedFail()
	{
		testProduct0.setName("testName_de1", Locale.GERMAN);
		testProduct0.setName("testName_en1", Locale.ENGLISH);
		modelService.save(testProduct0);

		final ProductModel exampleModel = new ProductModel();
		modelService.attach(exampleModel);
		exampleModel.setName("testName_de1", Locale.GERMAN);
		exampleModel.setName("testName_de2", Locale.ENGLISH);
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(exampleModel);
		assertNotSame("Loaded model should be not same to example model", exampleModel, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);
	}

	@Test
	public void testBigChangedAttributeTestSuccessfull()
	{
		testProduct0.setEan("X1");
		testProduct0.setDeliveryTime(Double.valueOf("13.8"));
		testProduct0.setName("name_de1", Locale.GERMAN);
		testProduct0.setName("bla", Locale.ENGLISH);
		testProduct0.setDescription("desc_de1", Locale.GERMAN);
		modelService.save(testProduct0);

		testProduct1.setEan("X1");
		testProduct1.setDeliveryTime(Double.valueOf("13.8"));
		testProduct1.setName("name_de1", Locale.GERMAN);
		testProduct1.setName("blubb", Locale.ENGLISH);
		testProduct1.setDescription("desc_de1", Locale.GERMAN);
		modelService.save(testProduct1);

		//succsessful search:
		final ProductModel example1 = new ProductModel();
		modelService.attach(example1);
		example1.setEan("X1");
		example1.setDeliveryTime(Double.valueOf("13.8"));
		example1.setName("name_de1", Locale.GERMAN);
		example1.setName("bla", Locale.ENGLISH);
		// example1.setDescription("desc_de1", Locale.GERMAN); can not search for clob on oracle

		final ProductModel loadedModel1 = flexibleSearchService.getModelByExample(example1);
		assertNotSame("Loaded model should be not same to example model", example1, loadedModel1);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel1);


	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void testBigChangedAttributeTestFail() //NOPMD
	{
		testProduct0.setEan("X1");
		testProduct0.setDeliveryTime(Double.valueOf("13.8"));
		testProduct0.setName("name_de1", Locale.GERMAN);
		testProduct0.setName("bla", Locale.ENGLISH);
		testProduct0.setDescription("desc_de1", Locale.GERMAN);
		modelService.save(testProduct0);

		testProduct1.setEan("X1");
		testProduct1.setDeliveryTime(Double.valueOf("13.8"));
		testProduct1.setName("name_de1", Locale.GERMAN);
		testProduct1.setName("blubb", Locale.ENGLISH);
		testProduct1.setDescription("desc_de1", Locale.GERMAN);
		modelService.save(testProduct1);

		//AmbiguousIdentifierException search - search without name[en] - only value where example1 and example2 are different
		final ProductModel example2 = new ProductModel();
		modelService.attach(example2);
		example2.setEan("X1");
		example2.setDeliveryTime(Double.valueOf("13.8"));
		example2.setName("name_de1", Locale.GERMAN);
		// example2.setDescription("desc_de1", Locale.GERMAN); can not search for clob on oracle

		flexibleSearchService.getModelByExample(example2);
	}

	@Test
	public void testSearchWithComposedTypeParameter()
	{
		final UnitModel unit = new UnitModel();
		modelService.attach(unit);
		unit.setCode("yyy");
		unit.setConversion(Double.valueOf("1.0"));
		unit.setName("yyy");
		unit.setUnitType("aaa");
		modelService.save(unit);

		testProduct0.setCode("xxx");
		testProduct0.setContentUnit(unit);
		modelService.save(testProduct0);


		final ProductModel example = new ProductModel();
		example.setCode("xxx");
		example.setContentUnit(unit);
		final ProductModel loadedModel = flexibleSearchService.getModelByExample(example);
		assertNotSame("Loaded model should be not same to example model", example, loadedModel);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel);

		final ProductModel example2 = new ProductModel();
		example2.setContentUnit(unit);
		final ProductModel loadedModel2 = flexibleSearchService.getModelByExample(example2);
		assertNotSame("Loaded model should be not same to example model", example2, loadedModel2);
		assertSame("Loaded model has to be same object than persisted one", testProduct0, loadedModel2);
	}

	@Test
	public void testModelNotFoundOnRemovedItem() throws ConsistencyCheckException
	{
		final CountryModel myCountry = new CountryModel();
		myCountry.setIsocode("thalerland");

		final FlexibleSearchService flexService = createMock(FlexibleSearchService.class);
		final DefaultItemModelSearchStrategy strategy = (DefaultItemModelSearchStrategy) Registry.getApplicationContext().getBean(
				"modelSearchStrategy");
		try
		{
			strategy.setFlexibleSearchService(flexService);

			final LazyLoadItemList<Country> itemList = new LazyLoadItemList<Country>(null, Collections.singletonList(PK
					.createUUIDPK(34)), 2);
			final LazyLoadModelList list = new LazyLoadModelList(itemList, 2, Collections.EMPTY_LIST, modelResolver);

			expect(
					flexService.search("SELECT {pk} from {Country} WHERE {isocode}=?isocode ",
							Collections.singletonMap("isocode", myCountry.getIsocode()))).andReturn(new SearchResultImpl(list, 1, 1, 0));
			replay(flexService);

			try
			{
				flexibleSearchService.getModelByExample(myCountry);
				fail("ModelNotFoundException expected because no entity with this code is existent");
			}
			catch (final ModelNotFoundException e)
			{
				verify(flexService);
			}
		}
		finally
		{
			strategy.setFlexibleSearchService((FlexibleSearchService) Registry.getApplicationContext().getBean(
					"flexibleSearchService"));
		}
	}
}
