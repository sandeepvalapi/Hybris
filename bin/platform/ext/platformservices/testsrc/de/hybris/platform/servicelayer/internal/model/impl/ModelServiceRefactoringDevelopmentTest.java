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
package de.hybris.platform.servicelayer.internal.model.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.impl.DefaultChangeSet;
import de.hybris.platform.directpersistence.impl.DefaultWritePersistenceGateway;
import de.hybris.platform.directpersistence.impl.DefaultPersistResult;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.ExtendedServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Ignore
@IntegrationTest
public class ModelServiceRefactoringDevelopmentTest extends ExtendedServicelayerBaseTest
{

	/*
	 * NOT A TEST
	 * 
	 * 
	 * THIS IS NOT REAL TEST - IT IS JUST DEVELOPMENT TIME HELPER TO TRIGGER SOME USECASES
	 * 
	 * 
	 * NOT A TEST
	 */

	// TODO All tests must have @Transactional until HOR-1866 will be resolved


	private static final String PRODUCT_TYPE = "Product";

	private static final String ADDRESS3_STREETNAME = "3333";
	private static final String ADDRESS2_STREETNAME = "2222";
	private static final String ADDRESS1_STREETNAME = "1111";
	private static final String CUSTOMER_DESCRIPTION = "Customer for cascader test";
	private static final String CUSTOMER_NAME = "Jan";

	private static final Logger LOG = Logger.getLogger(ModelServiceRefactoringDevelopmentTest.class);

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private WritePersistenceGateway writePersistenceGateway;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private CacheInvalidator defaultCacheInvalidator;

	InterceptorMapping addUnitToNewProduct, updateUnitInProduct;


	@Before
	public void setUp() throws Exception
	{
		assertThat(modelService).isNotNull();
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	@Transactional
	public void createProducts()
	{
		// Creates 3 products
		final Collection<ProductModel> models = new ArrayList<ProductModel>();
		for (int index = 1; index <= 3; index++)
		{
			models.add(prepareProductModel("TestLSs_" + index));
		}
		modelService.saveAll(models);
	}

	@Test
	@Transactional
	public void createProduct()
	{
		// Create single product
		modelService.save(prepareProductModel("TestLS"));

		final ProductModel product = prepareProductModel("TestLS2");
		product.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
		modelService.save(product);
	}

	@Test
	@Transactional
	public void createCustomerWithAddresses()
	{
		final CustomerModel customer = modelService.create(CustomerModel.class);
		final AddressModel ad1 = modelService.create(AddressModel.class);
		ad1.setOwner(customer);
		ad1.setStreetname(ADDRESS1_STREETNAME);
		ad1.setTown("Gliwice");

		final AddressModel ad2 = modelService.create(AddressModel.class);
		ad2.setOwner(customer);
		ad2.setStreetname(ADDRESS2_STREETNAME);

		final AddressModel ad3 = modelService.create(AddressModel.class);
		ad3.setOwner(customer);
		ad3.setStreetname(ADDRESS3_STREETNAME);

		customer.setUid(CUSTOMER_NAME);
		customer.setDescription(CUSTOMER_DESCRIPTION);
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		customer.setDefaultPaymentAddress(ad1);
		customer.setDefaultShipmentAddress(ad1);
		customer.setName("Szymik");
		customer.setDescription("Description");

		modelService.saveAll();
	}


	@Test
	@Transactional
	public void createAddress()
	{
		final CustomerModel customer = modelService.create(CustomerModel.class);
		customer.setUid(CUSTOMER_NAME);
		customer.setDescription(CUSTOMER_DESCRIPTION);
		modelService.save(customer);

		final AddressModel ad1 = modelService.create(AddressModel.class);
		ad1.setOwner(customer);
		ad1.setStreetname(ADDRESS1_STREETNAME);
		ad1.setTown("Gliwice");
		ad1.setOwner(customer);
		modelService.save(ad1);
	}

	@Test
	@Transactional
	public void createCategoryWithKeywords2Langs()
	{
		// prepare Unsaved models with localized references
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode("cat");

		final CategoryModel cat0 = modelService.create(CategoryModel.class);
		cat0.setCode("cat0");

		final CategoryModel cat2 = modelService.create(CategoryModel.class);
		cat2.setCode("cat2");

		final LanguageModel english = commonI18NService.getLanguage(Locale.ENGLISH.getLanguage());

		final KeywordModel k1 = modelService.create(KeywordModel.class);
		k1.setKeyword("k1");
		k1.setLanguage(english);

		final KeywordModel k2 = modelService.create(KeywordModel.class);
		k2.setKeyword("k2");
		k2.setLanguage(english);

		final KeywordModel k3 = modelService.create(KeywordModel.class);
		k3.setKeyword("k3");
		k3.setLanguage(english);

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.ENGLISH);

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.GERMAN);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.GERMAN);

		modelService.saveAll(Arrays.asList(cat, cat0, cat2, k1, k2, k3));
	}

	@Test
	public void testMixJaloTxTemplate() throws Exception
	{
		printTransactionRunning();
		Transaction.current().execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				printTransactionRunning();
				final PK pk = generatePkForCode("Unit");
				final String startingCode = "TextTx1";
				persistUnitWithGateway(pk, startingCode);
				final String codeJdbcBefore = getCodeWithJDBC(pk);
				assertThat(codeJdbcBefore).isEqualTo(startingCode);
				final String codeJaloBefore = getCodeWithJALO(pk); // LINE-X- If commented out then next call to getCodeWithJalo will be successful
				assertThat(codeJaloBefore).isEqualTo(startingCode);
				final String afterCode = startingCode + "modified";
				updateUnitWithGateway(pk, afterCode);
				final String codeJdbcAfter = getCodeWithJDBC(pk);
				assertThat(codeJdbcAfter).isEqualTo(afterCode);
				final String codeJaloAfter = getCodeWithJALO(pk); // Will work if LINE-X will be commented out
				assertThat(codeJaloAfter).isEqualTo(afterCode);
				return null;
			}

			protected String getCodeWithJALO(final PK pk)
			{
				final Unit unit = (Unit) modelService.getSource(modelService.get(pk));
				LOG.debug("Unit from JALO with code: " + unit.getCode());
				return unit.getCode();
			}

			protected String getCodeWithJDBC(final PK pk)
			{
				final String code = jdbcTemplate.queryForObject("select code from junit_units where pk = ?", new Object[]
				{ new Long(pk.getLongValue()) }, String.class);
				LOG.debug("Found unit with code: " + code);
				return code;
			}

			protected void persistUnitWithGateway(final PK pk, final String code)
			{
				LOG.debug("Inserting unit with code: " + code + " and Pk: " + pk);
				final InsertRecord insertRecord = new InsertRecord(pk, "Unit", Sets.newHashSet(new PropertyHolder("Code", code),
						new PropertyHolder("UnitType", "testUnitType")));
				final MutableChangeSet changeSet = new DefaultChangeSet();
				changeSet.add(insertRecord);
				writePersistenceGateway.persist(changeSet);
				final PersistResult result = new DefaultPersistResult(CrudEnum.CREATE, pk, Long.valueOf(0));
				defaultCacheInvalidator.invalidate(Arrays.asList(result));
			}

			protected void updateUnitWithGateway(final PK pk, final String code)
			{
				LOG.debug("Updating unit with code: " + code);
				final UpdateRecord updateRecord = new UpdateRecord(pk, "Unit", 0, Sets.newHashSet(new PropertyHolder("Code", code),
						new PropertyHolder("UnitType", "testUnitType")));
				final MutableChangeSet changeSet = new DefaultChangeSet();
				changeSet.add(updateRecord);
				writePersistenceGateway.persist(changeSet);
				final PersistResult result = new DefaultPersistResult(CrudEnum.UPDATE, pk, Long.valueOf(1));
				defaultCacheInvalidator.invalidate(Arrays.asList(result));
			}

		});
		printTransactionRunning();
	}

	private PK generatePkForCode(final String typeCode)
	{
		final TypeInfoMap persistenceInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
		return PK.createCounterPK(persistenceInfo.getItemTypeCode());
	}

	private void printTransactionRunning()
	{
		LOG.debug("Transaction running? " + Transaction.current().isRunning());
	}


	@Transactional
	@Test
	public void createAndRemoveSimpleCategory()
	{
		final KeywordModel k1 = modelService.create(KeywordModel.class);
		k1.setKeyword("k1");
		k1.setLanguage(commonI18NService.getLanguage(Locale.ENGLISH.getLanguage()));

		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode("cat");
		cat.setKeywords(Lists.newArrayList(k1));

		modelService.saveAll();

		k1.setCategories(Lists.newArrayList(cat));

		modelService.saveAll();

		modelService.remove(cat);
	}

	@Test
	@Transactional
	public void createCategoryWithKeywords()
	{
		// prepare Unsaved models with localized references
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode("cat");

		final CategoryModel cat0 = modelService.create(CategoryModel.class);
		cat0.setCode("cat0");

		final CategoryModel cat2 = modelService.create(CategoryModel.class);
		cat2.setCode("cat2");

		final LanguageModel english = commonI18NService.getLanguage(Locale.ENGLISH.getLanguage());

		final KeywordModel k1 = modelService.create(KeywordModel.class);
		k1.setKeyword("k1");
		k1.setLanguage(english);

		final KeywordModel k2 = modelService.create(KeywordModel.class);
		k2.setKeyword("k2");
		k2.setLanguage(english);

		final KeywordModel k3 = modelService.create(KeywordModel.class);
		k3.setKeyword("k3");
		k3.setLanguage(english);

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.ENGLISH);

		modelService.saveAll();

		List<KeywordModel> keywords = cat.getKeywords(Locale.ENGLISH);

		assertThat(keywords).hasSize(3);
		assertThat(keywords.get(0)).isEqualTo(k1);
		assertThat(keywords.get(1)).isEqualTo(k2);
		assertThat(keywords.get(2)).isEqualTo(k3);

		Collection<CategoryModel> categories = k2.getCategories(Locale.ENGLISH);

		assertThat(categories).hasSize(3);

		cat.setCode("cat_NEW");
		cat.setKeywords(Arrays.asList(k2, k1, k3), Locale.ENGLISH);

		modelService.saveAll();

		keywords = cat.getKeywords(Locale.ENGLISH);
		assertThat(keywords).hasSize(3);
		assertThat(keywords.get(0)).isEqualTo(k2);
		assertThat(keywords.get(1)).isEqualTo(k1);
		assertThat(keywords.get(2)).isEqualTo(k3);

		k2.setCategories(Arrays.asList(cat0), Locale.ENGLISH);

		modelService.saveAll();

		categories = k2.getCategories(Locale.ENGLISH);

		assertThat(categories).hasSize(1);

		cat.setKeywords(Collections.EMPTY_LIST, Locale.ENGLISH);

		modelService.saveAll();

		assertThat(cat.getKeywords(Locale.ENGLISH)).isEmpty();

		modelService.remove(cat);
	}

	@Test
	@Transactional
	public void removeKeywordsFromCategory()
	{
		//prepare Unsaved models with localized references
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode("cat");
		final LanguageModel english = commonI18NService.getLanguage(Locale.ENGLISH.getLanguage());
		final KeywordModel k1 = modelService.create(KeywordModel.class);
		k1.setKeyword("k1");
		k1.setLanguage(english);
		final KeywordModel k2 = modelService.create(KeywordModel.class);
		k2.setKeyword("k2");
		k2.setLanguage(english);
		final KeywordModel k3 = modelService.create(KeywordModel.class);
		k3.setKeyword("k3");
		k3.setLanguage(english);
		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		modelService.saveAll(cat, k1, k2, k3);
		cat.setKeywords(Collections.<KeywordModel> emptyList(), Locale.ENGLISH);
		modelService.saveAll(cat);
	}

	@Test
	@Transactional
	public void removeCategoryFromKeyword()
	{
		//prepare Unsaved models with localized references
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode("cat");
		final LanguageModel english = commonI18NService.getLanguage(Locale.ENGLISH.getLanguage());
		final KeywordModel k1 = modelService.create(KeywordModel.class);
		k1.setKeyword("k1");
		k1.setLanguage(english);
		cat.setKeywords(Arrays.asList(k1), Locale.ENGLISH);
		modelService.saveAll(cat, k1);
		k1.setCategories(Collections.<CategoryModel> emptyList(), Locale.ENGLISH);
		modelService.saveAll(k1);
	}


	private ProductModel prepareProductModel(final String code)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setName("Pilka", Locale.ENGLISH);
		product.setName("Pilka", Locale.GERMAN);
		return product;
	}

	private static InterceptorMapping createInterceptorMapping(final Interceptor interceptor, final String typeCode)
	{
		final InterceptorMapping mapping = new InterceptorMapping();
		mapping.setTypeCode(typeCode);
		mapping.setInterceptor(interceptor);
		return mapping;
	}

	@Before
	public void registerInterceptor()
	{
		final DefaultInterceptorRegistry registry = (DefaultInterceptorRegistry) ((DefaultModelService) modelService)
				.getInterceptorRegistry();

		registry.registerInterceptor(addUnitToNewProduct = createInterceptorMapping(new AddUnitToNewProductInterceptor(),
				PRODUCT_TYPE));
		registry.registerInterceptor(updateUnitInProduct = createInterceptorMapping(new UpdateUnitInProductInterceptor(),
				PRODUCT_TYPE));
	}

	@After
	public void unregisterInterceptor()
	{
		final DefaultInterceptorRegistry registry = (DefaultInterceptorRegistry) ((DefaultModelService) modelService)
				.getInterceptorRegistry();

		if (addUnitToNewProduct != null)
		{
			registry.unregisterInterceptor(addUnitToNewProduct);
		}
		if (updateUnitInProduct != null)
		{
			registry.unregisterInterceptor(updateUnitInProduct);
		}
	}

	/**
	 * Prepare interceptor which will add new Unit to the Product
	 */
	private class AddUnitToNewProductInterceptor implements PrepareInterceptor
	{

		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof ProductModel)
			{
				LOG.info("*** " + AddUnitToNewProductInterceptor.class.getSimpleName() + " ***");
				final ProductModel product = (ProductModel) model;
				final boolean isNew = product.getItemModelContext().isNew();
				LOG.info("Interceptor is called on new product?: " + isNew);

				// Create a new unit
				final UnitModel unit = modelService.create(UnitModel.class);
				final String code = "pint-" + product.getCode();
				unit.setCode(code);
				unit.setUnitType("measurement");
				// modelService.save(unit);
				ctx.registerElement(unit, null);
				product.setUnit(unit);

				LOG.info("...so unit with code (" + code + ") has been added.");
			}
			// skip 
		}
	}

	/**
	 * Prepare interceptor which will add new Unit to the Product
	 */
	private class UpdateUnitInProductInterceptor implements PrepareInterceptor
	{
		@Override
		public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof ProductModel)
			{
				LOG.info("*** " + UpdateUnitInProductInterceptor.class.getSimpleName() + " ***");
			}
			// skip 
		}
	}

}
