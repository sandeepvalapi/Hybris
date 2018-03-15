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
package de.hybris.platform.catalog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.impl.UniqueCatalogItemInterceptor;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.InvalidateModelConverterRegistryEvent;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class CatalogServiceTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(CatalogServiceTest.class.getName());

	@Resource
	private EventService eventService;

	@Resource
	private CatalogService catalogService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ModelService modelService;

	@Resource
	private ProductService productService;

	@Test
	public void testgetAllCatalogsWithNothing()
	{
		assertTrue(catalogService.getAllCatalogs().isEmpty());
	}

	@Test
	public void testgetAllCatalogsWithSomeCatalogs()
	{
		final Collection<CatalogModel> testcatalogs = createTestCatalogs(15);
		final Collection<CatalogModel> searchedCats = catalogService.getAllCatalogs();
		assertEquals(testcatalogs.size(), searchedCats.size());
		assertTrue(testcatalogs.containsAll(searchedCats));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testgetAllCatalogsOfTypeWithNull()
	{
		catalogService.getAllCatalogsOfType(null);
	}

	@Test
	public void testgetAllCatalogsOfType()
	{
		Collection<CatalogModel> testcoll = catalogService.getAllCatalogsOfType(CatalogModel.class);
		assertEquals(Collections.EMPTY_LIST, testcoll);

		final Collection<CatalogModel> testcatalogs = createTestCatalogs(2);

		testcoll = catalogService.getAllCatalogsOfType(CatalogModel.class);
		assertEquals(testcatalogs.size(), testcoll.size());
		assertTrue(testcatalogs.containsAll(testcoll));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCatalogNullId()
	{
		catalogService.getCatalogForId(null);
	}

	@Test
	public void testGetCatalogForId()
	{
		try
		{
			this.catalogService.getCatalogForId("doesnotexist");
			fail("Expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals("No catalog with given id [doesnotexist] was found", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("Expected UnknownIdentifierException, something exle was thrown: " + e);
		}

		createTestCatalogs(1);

		final CatalogModel catalog = this.catalogService.getCatalogForId("test_0");
		assertTrue("Must be catalog 'testCatalog'!", "test_0".equals(catalog.getId()));
	}

	@Test
	public void testUniqueKeyCheck() throws Exception
	{
		final ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);
		final AttributeDescriptor versionAttr = CatalogManager.getInstance().getCatalogVersionAttribute(productType);
		final boolean versionUnique = versionAttr.isUnique();
		final AttributeDescriptor codeAttr = productType.getAttributeDescriptor(ProductModel.CODE);
		final boolean codeUnique = codeAttr.isUnique();
		try
		{
			// pretend that version and unique key attributes are not marked unique !!!
			versionAttr.setUnique(false);
			codeAttr.setUnique(false);
			invalidateProductModelConverterManually();

			final CatalogVersionModel catalogVersion = createTestCatalogVersion("testCatalog", "Online");
			assertNotNull(catalogVersion);
			assertFalse(modelService.isNew(catalogVersion));
			assertTrue(modelService.isUpToDate(catalogVersion));

			final ProductModel pm1 = modelService.create(ProductModel.class);
			pm1.setCatalogVersion(catalogVersion);
			pm1.setCode("uniqueCheck");
			final ProductModel pm2 = modelService.create(ProductModel.class);
			pm2.setCatalogVersion(catalogVersion);
			pm2.setCode("uniqueCheck");
			final ProductModel pm3 = modelService.create(ProductModel.class);
			pm3.setCatalogVersion(catalogVersion);
			pm3.setCode("uniqueCheck2");

			modelService.save(pm1); // ok

			try
			{
				modelService.save(pm2);
				fail("expected " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				final InterceptorException interceptorException = (InterceptorException) e.getCause();
				assertNotNull(interceptorException.getInterceptor());
				assertTrue("Got the " + interceptorException.getInterceptor().getClass().getName()
						+ " instead of UniqueCatalogItemInterceptor",
						interceptorException.getInterceptor() instanceof UniqueCatalogItemInterceptor);
			}

			modelService.save(pm3); // ok again

			// now test duplicates which are in ctx only

			final ProductModel pm4 = modelService.create(ProductModel.class);
			pm4.setCatalogVersion(catalogVersion);
			pm4.setCode("uniqueCheck3");
			final ProductModel pm5 = modelService.create(ProductModel.class);
			pm5.setCatalogVersion(catalogVersion);
			pm5.setCode("uniqueCheck3");
			final ProductModel pm6 = modelService.create(ProductModel.class);
			pm6.setCatalogVersion(catalogVersion);
			pm6.setCode("uniqueCheck4");

			try
			{
				modelService.saveAll(Arrays.asList(pm4, pm5, pm6));
				fail("expected " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				assertNotNull(((InterceptorException) e.getCause()).getInterceptor());
				assertTrue(((InterceptorException) e.getCause()).getInterceptor() instanceof UniqueCatalogItemInterceptor);
			}

			modelService.save(pm6); // must work

			// now test modified ones - database check

			pm1.setName("foo"); // doesn't matter
			pm2.setCode(pm6.getCode()); // this should trigger error

			try
			{
				modelService.saveAll(Arrays.asList(pm1, pm2));
				fail("expected " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				assertNotNull(((InterceptorException) e.getCause()).getInterceptor());
				assertTrue(((InterceptorException) e.getCause()).getInterceptor() instanceof UniqueCatalogItemInterceptor);
			}

			// now test modified ones - ctx check

			pm1.setName("foo"); // doesn't matter
			pm2.setCode(pm1.getCode()); // this should trigger error

			try
			{
				modelService.saveAll(Arrays.asList(pm1, pm2));
				fail("expected " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				assertNotNull(((InterceptorException) e.getCause()).getInterceptor());
				assertTrue(((InterceptorException) e.getCause()).getInterceptor() instanceof UniqueCatalogItemInterceptor);
			}

			pm2.setCode("uniqueCheck5"); // fine again
			modelService.saveAll(Arrays.asList(pm1, pm2));

			// YTODO test if automatically detected models are checked too !!!
		}
		finally
		{
			// restore unique settings of code and version attributes
			versionAttr.setUnique(versionUnique);
			codeAttr.setUnique(codeUnique);
			invalidateProductModelConverterManually();
		}
	}

	//see PLA-7817
	@Test
	public void testCreateModelsOfTypeAndSubtypeSaveAll() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		final CatalogVersionModel catalogVersion = createTestCatalogVersion("testCatalog", "Online");
		assertNotNull(catalogVersion);

		final ProductModel prodm1 = modelService.create(ProductModel.class);
		prodm1.setCatalogVersion(catalogVersion);
		prodm1.setCode("uniqueCode");
		modelService.save(prodm1);

		final TypeManager typeman = TypeManager.getInstance();
		final ComposedType foo_ct = typeman.createComposedType(typeman.getComposedType(Product.class), "FooProduct");
		assertEquals("FooProduct", foo_ct.getCode());

		try
		{
			final ProductModel foo1 = modelService.create(foo_ct.getCode());
			foo1.setCode(prodm1.getCode());
			foo1.setCatalogVersion(prodm1.getCatalogVersion());
			foo1.setApprovalStatus(prodm1.getApprovalStatus());
			modelService.save(foo1);
			fail("InterceptorException expected");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof UniqueAttributesInterceptor);
		}

		try
		{
			productService.getProduct(prodm1.getCode());
		}
		catch (final AmbiguousIdentifierException e)
		{
			fail();
		}
	}

	//see PLA-7817
	@Test
	public void testCreateModelsOfTypeAndSubtypeSaveOnly() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		final CatalogVersionModel catalogVersion = createTestCatalogVersion("testCatalog", "Online");
		assertNotNull(catalogVersion);

		final ProductModel prodm1 = modelService.create(ProductModel.class);
		prodm1.setCatalogVersion(catalogVersion);
		prodm1.setCode("uniqueCode");
		modelService.save(prodm1);

		final TypeManager typeman = TypeManager.getInstance();
		final ComposedType foo_ct = typeman.createComposedType(typeman.getComposedType(Product.class), "FooProduct");
		assertEquals("FooProduct", foo_ct.getCode());

		final ProductModel foo2 = modelService.create(foo_ct.getCode());
		foo2.setCode("differentCode");
		foo2.setCatalogVersion(prodm1.getCatalogVersion());
		foo2.setApprovalStatus(prodm1.getApprovalStatus());
		modelService.save(foo2);

		try
		{
			productService.getProduct(prodm1.getCode());
			productService.getProduct("differentCode");
		}
		catch (final Exception e)
		{
			fail();
		}


		try
		{
			foo2.setCode(prodm1.getCode());
			modelService.save(foo2);
			fail("InterceptorException expected");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof UniqueAttributesInterceptor);
		}

		try
		{
			productService.getProduct(prodm1.getCode());
		}
		catch (final AmbiguousIdentifierException e)
		{
			fail();
		}

	}

	//see PLA-7817
	@Test
	public void testCreateModelsOfTwoSubTypesSaveAll() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		final CatalogVersionModel catalogVersion = createTestCatalogVersion("testCatalog", "Online");
		assertNotNull(catalogVersion);

		final ProductModel prodm1 = modelService.create(ProductModel.class);
		prodm1.setCatalogVersion(catalogVersion);
		prodm1.setCode("uniqueCode");
		modelService.save(prodm1);

		final TypeManager typeman = TypeManager.getInstance();

		final ComposedType foo_ct = typeman.createComposedType(typeman.getComposedType(Product.class), "FooProduct");
		assertEquals("FooProduct", foo_ct.getCode());

		final ComposedType bar_ct = typeman.createComposedType(typeman.getComposedType(Product.class), "BarProduct");
		assertEquals("BarProduct", bar_ct.getCode());

		final ProductModel foo = modelService.create(foo_ct.getCode());
		foo.setCode("differentCode");
		foo.setCatalogVersion(prodm1.getCatalogVersion());
		foo.setApprovalStatus(prodm1.getApprovalStatus());
		modelService.save(foo);

		final ProductModel bar = modelService.create(bar_ct.getCode());
		bar.setCode(foo.getCode());
		bar.setApprovalStatus(foo.getApprovalStatus());
		bar.setCatalogVersion(foo.getCatalogVersion());

		try
		{
			modelService.save(bar);
			fail("InterceptorException expected");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof UniqueAttributesInterceptor);
		}

		try
		{
			productService.getProduct(bar.getCode());
		}
		catch (final AmbiguousIdentifierException e)
		{
			fail();
		}
	}


	//see PLA7931
	@Test
	public void testPLA7931() throws JaloInvalidParameterException, JaloDuplicateCodeException, ConsistencyCheckException
	{

		ClassificationSystemModel csmDef = null;
		ClassificationSystemVersionModel catalogDef = null;
		try
		{
			csmDef = modelService.create(ClassificationSystemModel.class);
			csmDef.setId("model1Def");

			catalogDef = modelService.create(ClassificationSystemVersionModel.class);
			catalogDef.setCatalog(csmDef);
			catalogDef.setVersion("ver def");
			csmDef.setDefaultCatalog(Boolean.TRUE);

			modelService.save(catalogDef);

			final ClassificationSystemModel csm = modelService.create(ClassificationSystemModel.class);
			csm.setId("model1");

			final ClassificationSystemVersionModel cvm = modelService.create(ClassificationSystemVersionModel.class);
			cvm.setCatalog(csm);
			cvm.setVersion("ver1.0");

			final ClassificationClassModel ccm = modelService.create(ClassificationClassModel.class);
			ccm.setCatalogVersion(cvm);
			ccm.setCode("class ver1.0");
			modelService.save(ccm);

			final ClassificationAttributeModel cam = new ClassificationAttributeModel();
			cam.setCode("attrModel");
			cam.setSystemVersion(cvm);
			modelService.save(cam);

			final ClassAttributeAssignmentModel caam = modelService.create(ClassAttributeAssignmentModel.class);
			caam.setClassificationAttribute(cam);
			caam.setClassificationClass(ccm);
			caam.setSystemVersion(null);

			modelService.save(caam);

			Assert.assertEquals(caam.getSystemVersion().getVersion(), cvm.getVersion());

		}

		finally
		{
			if (csmDef != null)
			{
				csmDef.setDefaultCatalog(Boolean.TRUE);

				modelService.save(catalogDef);
			}
		}
	}

	//test getPath PLA-8358, HOR-333
	@Test
	public void testGetPath()
	{

		ClassificationSystemModel csmDef = null;
		ClassificationSystemVersionModel catalogDef = null;

		csmDef = modelService.create(ClassificationSystemModel.class);
		csmDef.setId("someClassModel");

		catalogDef = modelService.create(ClassificationSystemVersionModel.class);
		catalogDef.setCatalog(csmDef);
		catalogDef.setVersion("some version");

		csmDef.setDefaultCatalog(Boolean.TRUE);

		modelService.save(catalogDef);

		final ClassificationSystemModel csm = modelService.create(ClassificationSystemModel.class);
		csm.setId("class model");

		final ClassificationSystemVersionModel cvm = modelService.create(ClassificationSystemVersionModel.class);
		cvm.setCatalog(csm);
		cvm.setVersion("ver1.0");

		final ClassificationClassModel ccm = modelService.create(ClassificationClassModel.class);
		ccm.setCatalogVersion(cvm);
		ccm.setCode("class ver1.0");

		final ClassificationClassModel ccm2 = modelService.create(ClassificationClassModel.class);
		ccm2.setCatalogVersion(cvm);
		ccm2.setCode("base class ver1.0");

		ccm2.setSupercategories(Arrays.asList(new CategoryModel[]
		{ ccm }));

		modelService.save(ccm2);

		checkCategoryPath(ccm2);



	}

	/**
	 * check category path from Slayer in compare to old style Jalo representation
	 */
	private void checkCategoryPath(final CategoryModel ccm2)
	{
		final Category jaloClass = modelService.getSource(ccm2);

		final List<CategoryModel> catList = categoryService.getPath(ccm2);

		final StringBuilder catPathBuilder = new StringBuilder();
		for (int i = catList.size() - 1; i >= 0; i--)
		{
			catPathBuilder.append(catList.get(i).getCode() + "/");
		}

		final List<Category> catJaloList = jaloClass.getPath();

		final StringBuilder catJaloPathBuilder = new StringBuilder();
		for (int i = catJaloList.size() - 1; i >= 0; i--)
		{
			catJaloPathBuilder.append(catJaloList.get(i).getCode() + "/");
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Checking category " + ccm2.getCode() + " " + ccm2.getName() + " " + ccm2.getClass().getName() + " path :: "
					+ catPathBuilder.toString());
		}
		Assert.assertTrue("Category path from jalo " + catJaloPathBuilder.toString() + " differs from " + catPathBuilder.toString()
				+ " for category " + ccm2, catPathBuilder.toString().equals(catJaloPathBuilder.toString()));
	}

	//test getPath PLA-8358, HOR-333
	@Test
	public void testGetPathForTestCatalog() throws JaloInvalidParameterException, JaloItemNotFoundException,
			JaloSecurityException, JaloBusinessException
	{


		//this.catalogService.getCatalogVersion("hwcatalog", "Online");

		for (final CatalogModel cm : this.catalogService.getAllCatalogs())
		{
			for (final CatalogVersionModel cvm : cm.getCatalogVersions())
			{
				for (final CategoryModel rootCm : categoryService.getRootCategories(cvm))
				{
					checkCategoryPath(rootCm);
					for (final CategoryModel subCat : rootCm.getAllSubcategories())
					{
						checkCategoryPath(subCat);
					}
				}

			}
		}
	}

	@Test
	public void testPLA10194_SetCollectionWithNullEntry() throws Exception
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid("xxx");
		user.setAddresses(Collections.singleton((AddressModel) null));
		try
		{
			modelService.save(user);
			fail("expected ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// Fine
		}
		catch (final Exception e)
		{
			fail("wrong exception");
		}

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("xxx");
		final CatalogVersionModel cver = createTestCatalogVersion("testCatalog", "Online");
		assertNotNull(cver);
		product.setCatalogVersion(cver);
		product.setSupercategories(Collections.singleton((CategoryModel) null));
		try
		{
			modelService.save(product);
		}
		catch (final ModelSavingException e)
		{
			// FINE
		}
		catch (final Exception e)
		{
			fail("wrong exception");
		}

		final ProductModel product1 = modelService.create(ProductModel.class);
		product1.setCode("xxx");
		product1.setCatalogVersion(cver);

		final List<KeywordModel> list = new ArrayList<KeywordModel>();
		list.add(null);
		product1.setKeywords(list);

		try
		{
			modelService.save(product1);
		}
		catch (final ModelSavingException e)
		{
			// FINE
		}
		catch (final Exception e)
		{
			fail("wrong exception");
		}
	}

	@Test
	public void testGetDefaultCatalog()
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("testCatalog");
		cat.setDefaultCatalog(Boolean.TRUE);
		modelService.save(cat);

		Assert.assertEquals(cat, catalogService.getDefaultCatalog());

		final CatalogModel newDefaultCatalog = modelService.create(CatalogModel.class);
		newDefaultCatalog.setId("new_Catalog");
		newDefaultCatalog.setName("new catalog");
		newDefaultCatalog.setDefaultCatalog(Boolean.TRUE);
		modelService.save(newDefaultCatalog);

		Assert.assertEquals("Unexpected default catalog", newDefaultCatalog, catalogService.getDefaultCatalog());

		//tear down created model
		newDefaultCatalog.setDefaultCatalog(Boolean.FALSE);
		modelService.save(newDefaultCatalog);
		modelService.remove(newDefaultCatalog);
		Assert.assertNull("Unexpected default catalog", catalogService.getDefaultCatalog());
	}


	private Collection<CatalogModel> createTestCatalogs(final int count)
	{
		//create test data
		final Collection<CatalogModel> testcatalogs = new ArrayList<CatalogModel>(count);
		for (int i = 0; i < count; i++)
		{
			final CatalogModel cat = modelService.create(CatalogModel.class);
			cat.setId("test_" + i);
			modelService.save(cat);
			testcatalogs.add(cat);
		}
		return testcatalogs;
	}

	private CatalogVersionModel createTestCatalogVersion(final String catalogID, final String versionID)
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId(catalogID);
		cat.setDefaultCatalog(Boolean.TRUE);
		final CatalogVersionModel catver = modelService.create(CatalogVersionModel.class);
		catver.setCatalog(cat);
		catver.setActive(Boolean.TRUE);
		catver.setVersion(versionID);
		modelService.saveAll(cat, catver);
		return catver;
	}

	private void invalidateProductModelConverterManually()
	{
		final InvalidateModelConverterRegistryEvent evt = new InvalidateModelConverterRegistryEvent();
		evt.setComposedTypeCode("Product".toLowerCase());
		evt.setComposedClass(Product.class);
		evt.setRefresh(true);
		eventService.publishEvent(evt);
	}
}
