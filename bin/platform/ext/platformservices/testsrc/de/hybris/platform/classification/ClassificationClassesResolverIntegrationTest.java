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
package de.hybris.platform.classification;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ClassificationClassesResolverIntegrationTest extends ServicelayerTest
{
	@Resource
	ModelService modelService;

	@Resource
	CatalogVersionService catalogVersionService;

	@Resource
	ClassificationClassesResolverStrategy classificationClassesResolverStrategy;

	@Resource
	public ClassificationService classificationService;

	@Resource
	ProductService productService;

	ClassificationSystemModel sys = null;
	ClassificationSystemVersionModel sysVer = null;
	CatalogVersionModel catalogVersion;
	CatalogModel catalog;

	CategoryModel categoryModel1, categoryModel11, categoryModel111, categoryModel2;
	ProductModel product;

	String resolvingModeBefore;

	@Before
	public void setUp()
	{
		resolvingModeBefore = Config.getParameter("classification.resolve.classes.mode");

		// Set a basic classification
		catalog = createCatalog("TestCatalog");
		catalogVersion = createCatalogVersion(catalog, "online");

		categoryModel1 = createCategory(catalogVersion, "c1");
		categoryModel11 = createCategory(catalogVersion, "c11");
		categoryModel111 = createCategory(catalogVersion, "c111");
		categoryModel2 = createCategory(catalogVersion, "c2");

		product = createProduct(catalogVersion, "p1", categoryModel111, categoryModel2);

		sys = createClassificationSystem("TestClassificationSystem");
		sysVer = createClassificationSystemVersion(sys, "1.0");

		modelService.saveAll();

	}

	protected void enableIncludeAllClassesMode()
	{
		Config.setParameter("classification.resolve.classes.mode", "all");
	}

	protected void enableClosestClassesMode()
	{
		Config.setParameter("classification.resolve.classes.mode", "closest");
	}

	@After
	public void restoreClassesResolvingMode()
	{
		Config.setParameter("classification.resolve.classes.mode", resolvingModeBefore);
	}

	private CatalogModel createCatalog(final String id)
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(id);

		return catalog;
	}

	private ClassificationSystemModel createClassificationSystem(final String id)
	{

		final ClassificationSystemModel sys = modelService.create(ClassificationSystemModel.class);
		sys.setId(id);

		return sys;
	}

	private ClassificationSystemVersionModel createClassificationSystemVersion(final ClassificationSystemModel sys,
			final String version)
	{
		final ClassificationSystemVersionModel sysVer = modelService.create(ClassificationSystemVersionModel.class);
		sysVer.setVersion(version);
		sysVer.setCatalog(sys);

		return sysVer;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog, final String version)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(version);
		catalogVersion.setCatalog(catalog);
		catalog.setCatalogVersions(Collections.singleton(catalogVersion));

		return catalogVersion;
	}

	private ClassificationClassModel createClass(final CatalogVersionModel catalogVersionModel, final String code)
	{
		final ClassificationClassModel clclm = modelService.create(ClassificationClassModel.class);
		clclm.setCode(code);
		clclm.setCatalogVersion(catalogVersionModel);
		return clclm;
	}

	private CategoryModel createCategory(final CatalogVersionModel catalogVersionModel, final String code)
	{
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode(code);
		cat.setCatalogVersion(catalogVersionModel);
		return cat;
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

	@Test
	public void testSetup()
	{
		assertNotNull(sys);
		assertNotNull(sysVer);
		assertEquals(sys, sysVer.getCatalog());
	}

	@Test
	public void testEmptyClassificationsSystem()
	{
		assertTrue(classificationClassesResolverStrategy.resolve(new ProductModel(), Collections.EMPTY_SET).isEmpty());
		assertTrue(classificationClassesResolverStrategy.resolve(new CategoryModel(), Collections.EMPTY_SET).isEmpty());
	}

	@Test
	public void testSingleClassificationClass()
	{
		final ClassificationClassModel single = createClass(sysVer, "shoes");
		assertTrue(classificationClassesResolverStrategy.resolve(single).contains(single));
		assertEquals(classificationClassesResolverStrategy.resolve(single).size(), 1);
	}

	@Test
	public void testSingleClassificationClassAtProduct()
	{
		//Create one class and assign it to a product
		final ClassificationClassModel clclm = createClass(sysVer, "shoes");
		product.setSupercategories(Collections.<CategoryModel> singleton(clclm));

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(product);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm));

		final Set<ClassificationClassModel> result2 = classificationClassesResolverStrategy.resolve(product, sysVer);
		assertEquals(1, result2.size());
		assertTrue(result2.contains(clclm));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(product,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());
	}

	@Test
	public void testSomeClassificationClassesAtProduct()
	{
		final ClassificationSystemVersionModel clsysver_B = createClassificationSystemVersion(sys, "1.6");

		final ClassificationClassModel clclm1 = createClass(sysVer, "shoes");
		final ClassificationClassModel clclm2 = createClass(sysVer, "sox");
		final ClassificationClassModel clclm3 = createClass(clsysver_B, "boots");

		final Collection<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(clclm1);
		categories.add(clclm2);
		categories.add(clclm3);

		final ProductModel prod = createProduct(catalogVersion, "p2");
		prod.setSupercategories(categories);
		catalogVersionService.addSessionCatalogVersion(clsysver_B);
		modelService.saveAll();

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod);
		assertEquals(3, result1.size());
		assertTrue(result1.contains(clclm1));
		assertTrue(result1.contains(clclm2));
		assertTrue(result1.contains(clclm3));

		final Set<ClassificationClassModel> result2a = classificationClassesResolverStrategy.resolve(prod, sysVer);
		assertEquals(2, result2a.size());
		assertTrue(result2a.contains(clclm1));
		assertTrue(result2a.contains(clclm2));

		final Set<ClassificationClassModel> result2b = classificationClassesResolverStrategy.resolve(prod, clsysver_B);
		assertEquals(1, result2b.size());
		assertTrue(result2b.contains(clclm3));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(prod,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());
	}

	@Test
	public void testSingleClassificationClassWithCategoryAtProducts()
	{
		final ClassificationClassModel clclm = createClass(sysVer, "bags");

		categoryModel111.setSupercategories(Arrays.<CategoryModel> asList(clclm));

		modelService.saveAll();

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(product);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm));

		final Set<ClassificationClassModel> result2 = classificationClassesResolverStrategy.resolve(product, sysVer);
		assertEquals(1, result2.size());
		assertTrue(result2.contains(clclm));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(product,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());
	}


	@Test
	public void testSeveralClassificationClassesAtProductWithCategory()
	{
		final CatalogModel catlg = createCatalog("TestCatalog2");
		final CatalogVersionModel catlgVer = createCatalogVersion(catlg, "online");

		final ClassificationSystemModel system = createClassificationSystem("TestClassificationSystem2");
		final ClassificationSystemVersionModel clsysver_A = createClassificationSystemVersion(system, "1.1");
		final ClassificationSystemVersionModel clsysver_B = createClassificationSystemVersion(system, "1.2");

		final ClassificationClassModel clclm1 = createClass(clsysver_A, "shoes");
		final ClassificationClassModel clclm2 = createClass(clsysver_A, "sox");
		final ClassificationClassModel clclm3 = createClass(clsysver_B, "boots");

		final ProductModel prod = createProduct(catlgVer, "product1");
		final CategoryModel cat = createCategory(catlgVer, "category1");

		final List<CategoryModel> categories1 = new ArrayList<CategoryModel>();
		categories1.add(clclm1);
		categories1.add(clclm2);
		cat.setSupercategories(categories1);

		modelService.saveAll();

		final Collection<CategoryModel> categories2 = new ArrayList<CategoryModel>();
		categories2.add(clclm3);
		categories2.add(cat);
		prod.setSupercategories(categories2);


		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod);
		assertEquals(3, result1.size());
		assertTrue(result1.contains(clclm1));
		assertTrue(result1.contains(clclm2));
		assertTrue(result1.contains(clclm3));

		final Set<ClassificationClassModel> result2a = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(2, result2a.size());
		assertTrue(result2a.contains(clclm1));
		assertTrue(result2a.contains(clclm2));

		final Set<ClassificationClassModel> result2b = classificationClassesResolverStrategy.resolve(prod, clsysver_B);
		assertEquals(1, result2b.size());
		assertTrue(result2b.contains(clclm3));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(prod,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());

		final Set<ClassificationClassModel> result4 = classificationClassesResolverStrategy.resolve(cat);
		assertEquals(2, result4.size());
		assertTrue(result4.contains(clclm1));
		assertTrue(result4.contains(clclm2));

		final Set<ClassificationClassModel> result5 = classificationClassesResolverStrategy.resolve(cat, clsysver_A);
		assertEquals(2, result5.size());
		assertTrue(result5.contains(clclm1));
		assertTrue(result5.contains(clclm2));

		final Set<ClassificationClassModel> result5b = classificationClassesResolverStrategy.resolve(cat, clsysver_B);
		assertTrue(result5b.isEmpty());

		final Set<ClassificationClassModel> result6 = classificationClassesResolverStrategy.resolve(cat,
				new ClassificationSystemVersionModel());
		assertTrue(result6.isEmpty());
	}


	@Test
	public void testMorePresiceClassificationClassIsGivenBeforeGlobalClClass()
	{
		final CatalogModel catlg = createCatalog("TestCatalog3");
		final CatalogVersionModel catlgVer = createCatalogVersion(catlg, "online");

		final ClassificationSystemModel system = createClassificationSystem("TestClassificationSystem3");
		final ClassificationSystemVersionModel clsysver_A = createClassificationSystemVersion(system, "1.1");
		final ClassificationSystemVersionModel clsysver_B = createClassificationSystemVersion(system, "1.2");

		final ClassificationClassModel clcl1 = createClass(clsysver_A, "shoes");
		final ClassificationClassModel clcl2 = createClass(clsysver_A, "sox");

		final ProductModel prod = createProduct(catlgVer, "product1");

		final CategoryModel cat = createCategory(catlgVer, "category1");

		cat.setSupercategories(Arrays.<CategoryModel> asList(clcl2));

		final List<CategoryModel> categories1 = new ArrayList<CategoryModel>();
		categories1.add(clcl1);
		categories1.add(cat);

		prod.setSupercategories(categories1);

		modelService.saveAll();

		final Set<ClassificationClassModel> result1a = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(2, result1a.size());
		assertTrue(result1a.contains(clcl1));
		assertTrue(result1a.contains(clcl2));

		enableClosestClassesMode();
		try
		{
			final Set<ClassificationClassModel> result1b = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
			assertEquals(1, result1b.size());
			assertTrue(result1b.contains(clcl1));
		}
		finally
		{
			restoreClassesResolvingMode();
		}

		//now, different system
		clcl2.setCatalogVersion(clsysver_B);

		final Set<ClassificationClassModel> result2 = classificationClassesResolverStrategy.resolve(prod);
		assertEquals(2, result2.size());
		assertTrue(result2.contains(clcl1));
		assertTrue(result2.contains(clcl2));

	}

	@Test
	public void testCategoryCycle()
	{
		final CatalogModel catlg = createCatalog("TestCatalog4");
		final CatalogVersionModel catlgVer = createCatalogVersion(catlg, "online");

		final ClassificationSystemModel system = createClassificationSystem("TestClassificationSystem4");
		final ClassificationSystemVersionModel clsysver_A = createClassificationSystemVersion(system, "1.1");

		final ClassificationClassModel clclm1 = createClass(clsysver_A, "shoes");

		final ProductModel prod = createProduct(catlgVer, "product1");

		final CategoryModel cat1 = createCategory(catlgVer, "category1");
		final CategoryModel cat2 = createCategory(catlgVer, "category2");
		final CategoryModel cat3 = createCategory(catlgVer, "category3");

		cat3.setSupercategories(Arrays.<CategoryModel> asList(clclm1));
		cat2.setSupercategories(Arrays.<CategoryModel> asList(clclm1));
		cat1.setSupercategories(Arrays.<CategoryModel> asList(cat3));
		prod.setSupercategories(Arrays.asList(cat1, cat2));
		modelService.saveAll();

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm1));
	}

	@Test
	public void testDuplicatedCategories()
	{
		final CatalogModel catlg = createCatalog("TestCatalog5");
		final CatalogVersionModel catlgVer = createCatalogVersion(catlg, "online");

		final ClassificationSystemModel system = createClassificationSystem("TestClassificationSystem5");
		final ClassificationSystemVersionModel clsysver_A = createClassificationSystemVersion(system, "1.1");

		final ClassificationClassModel clclm1 = createClass(clsysver_A, "shoes");

		final ProductModel prod = createProduct(catlgVer, "product1");

		clclm1.setCatalogVersion(clsysver_A);
		final CategoryModel cat1 = createCategory(catlgVer, "category1");
		final CategoryModel cat2 = createCategory(catlgVer, "category2");

		cat2.setSupercategories(Arrays.<CategoryModel> asList(clclm1));
		cat1.setSupercategories(Arrays.<CategoryModel> asList(cat2));

		prod.setSupercategories(Arrays.asList(cat1, cat2));

		modelService.saveAll();

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm1));
	}

	@Test
	public void testGetClassAttributeAssignments()
	{
		assertTrue(classificationClassesResolverStrategy.getClassAttributeAssignments(null).isEmpty());
		assertTrue(classificationClassesResolverStrategy.getClassAttributeAssignments(Collections.EMPTY_SET).isEmpty());

		final ClassificationSystemModel system = createClassificationSystem("TestClassificationSystem6");
		final ClassificationSystemVersionModel clsysver_A = createClassificationSystemVersion(system, "1.1");

		final ClassificationClassModel levelA = createClass(clsysver_A, "shoes");
		final ClassAttributeAssignmentModel camA1 = modelService.create(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel camA2 = modelService.create(ClassAttributeAssignmentModel.class);
		levelA.setDeclaredClassificationAttributeAssignments(Arrays.asList(camA1, camA2));
		final ClassificationClassModel levelB = createClass(clsysver_A, "pants");
		final ClassAttributeAssignmentModel camB1 = modelService.create(ClassAttributeAssignmentModel.class);
		levelB.setDeclaredClassificationAttributeAssignments(Arrays.asList(camB1));

		final HashSet<ClassificationClassModel> list = new LinkedHashSet<ClassificationClassModel>();
		list.add(levelA);
		list.add(levelB);

		final List<ClassAttributeAssignmentModel> resList = classificationClassesResolverStrategy
				.getClassAttributeAssignments(list);
		assertEquals(3, resList.size());
		assertEquals(resList.get(0), camA1);
		assertEquals(resList.get(1), camA2);
		assertEquals(resList.get(2), camB1);

		assertTrue(classificationClassesResolverStrategy.getClassAttributeAssignments(
				Collections.singleton(new ClassificationClassModel())).isEmpty());
	}

	@Test
	public void testClassificationPropagation() throws ImpExException, JaloDuplicateQualifierException, JaloItemNotFoundException
	{
		importCsv("/platformservices/test/classificationTestData.csv", "utf-8");

		final ProductModel product = getProduct("prod_correct");

		final ProductModel variant = getProduct("variant_correct");

		final FeatureList featlist = classificationService.getFeatures(product);
		final FeatureList featlist2 = classificationService.getFeatures(variant);
		assertNotNull(featlist);
		assertNotNull(featlist2);
		final Set<ClassAttributeAssignmentModel> clatts = featlist.getClassAttributeAssignments();
		final Set<ClassAttributeAssignmentModel> clatts2 = featlist2.getClassAttributeAssignments();
		assertNotNull(clatts);
		assertNotNull(clatts2);

		assertTrue(clatts.equals(clatts2));
	}

	private ProductModel getProduct(final String code)
	{
		final ProductModel result = productService.getProduct(code);
		assertNotNull(result);
		return result;
	}

	@Test
	public void testVariantsClassInheritance()
	{
		final VariantTypeModel type = modelService.create(VariantTypeModel.class);
		type.setCode("TestVariant");
		type.setCatalogItemType(Boolean.FALSE);
		type.setGenerate(Boolean.FALSE);
		type.setSingleton(Boolean.FALSE);
		modelService.save(type);

		final ClassificationClassModel cl1 = modelService.create(ClassificationClassModel.class);
		cl1.setCode("cl1");
		cl1.setCatalogVersion(sysVer);
		final ClassificationClassModel cl2 = modelService.create(ClassificationClassModel.class);
		cl2.setCode("cl2");
		cl2.setCatalogVersion(sysVer);

		categoryModel2.setSupercategories((List) Collections.singletonList(cl1));

		final ProductModel base = modelService.create(ProductModel.class);
		base.setCode("base");
		base.setCatalogVersion(catalogVersion);
		base.setVariantType(type);
		modelService.saveAll(cl1, cl2, categoryModel2, base);

		final VariantProductModel variant1 = modelService.create("TestVariant"); // use explicit type
		variant1.setCode("variant1");
		variant1.setCatalogVersion(catalogVersion);
		variant1.setVariantType(type);
		variant1.setBaseProduct(base);
		final VariantProductModel variant2 = modelService.create(VariantProductModel.class); // !!! rely on type via base product, since VariantProduct is abstract !!!
		variant2.setCode("variant2");
		variant2.setCatalogVersion(catalogVersion);
		variant2.setVariantType(type);
		variant2.setBaseProduct(variant1);
		final VariantProductModel variant3 = new VariantProductModel(); // !!! rely on type via base product, since VariantProduct is abstract !!!
		variant3.setCode("variant3");
		variant3.setCatalogVersion(catalogVersion);
		variant3.setBaseProduct(variant2);

		modelService.saveAll(variant1, variant2, variant3);

		// sanity: no classification yet
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant3));

		// direct assignments clClass1 -> variant3 & clClass2 -> variant2 => classes(variant3)==[clClass1,clClass2] & classes(variant2)==[clClass2]
		variant3.setSupercategories((Collection) Collections.singletonList(cl1));
		variant2.setSupercategories((Collection) Collections.singletonList(cl2));
		modelService.saveAll(variant3, variant2);

		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant3));

		variant3.setSupercategories(Collections.EMPTY_SET);
		variant2.setSupercategories(Collections.EMPTY_SET);
		modelService.saveAll(variant3, variant2);

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3
		base.setSupercategories(Collections.singleton(categoryModel2));
		modelService.save(base);

		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant3));

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3 
		// AND direct assignment clClass2 -> variant2 = classes(variant2)=clClass2 & classes(variant3)=clClass2
		variant2.setSupercategories((Collection) Collections.singletonList(cl2));
		modelService.save(variant2);

		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant3));

		enableClosestClassesMode();
		try
		{
			assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
			assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
			assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant2));
			assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant3));
		}
		finally
		{
			restoreClassesResolvingMode();
		}
	}

}
