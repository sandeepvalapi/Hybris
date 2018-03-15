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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.impl.DefaultClassificationClassesResolverStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@UnitTest
public class ClassificationClassesResolverServiceTest
{
	private ClassificationClassesResolverStrategy classificationClassesResolverStrategy = null;

	private CatalogVersionService clSysVerSMock;

	// if 'true' only classes reached first are found, if 'false' all classes reachable via super categories
	// are included ( default = false )
	private boolean onlyClosestClasses = false;

	@Before
	public void setUp()
	{
		classificationClassesResolverStrategy = new DefaultClassificationClassesResolverStrategy()
		{
			@Override
			protected boolean isIncludingOnlyClosestClasses()
			{
				return onlyClosestClasses;
			}
		};
		clSysVerSMock = Mockito.mock(CatalogVersionService.class);
		((DefaultClassificationClassesResolverStrategy) classificationClassesResolverStrategy)
				.setCatalogVersionService(clSysVerSMock);
	}

	@Test
	public void testSingleClassificationClass()
	{
		final ClassificationClassModel single = new ClassificationClassModel();
		assertTrue(classificationClassesResolverStrategy.resolve(single).contains(single));
		assertEquals(classificationClassesResolverStrategy.resolve(single).size(), 1);
	}

	@Test
	public void testEmptyClassificationsSystem()
	{
		assertTrue(classificationClassesResolverStrategy.resolve(new ProductModel(), Collections.EMPTY_SET).isEmpty());
		assertTrue(classificationClassesResolverStrategy.resolve(new CategoryModel(), Collections.EMPTY_SET).isEmpty());
	}

	@Test
	public void testSingleClassificationClassAtProduct()
	{
		final ClassificationClassModel clclm = new ClassificationClassModel();
		final ProductModel prod = new ProductModel();
		final ClassificationSystemVersionModel clsysver = new ClassificationSystemVersionModel();

		prod.setSupercategories(Collections.<CategoryModel> singleton(clclm));
		clclm.setCatalogVersion(clsysver);

		setupClassificationSystemsMocking(clsysver);

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm));

		final Set<ClassificationClassModel> result2 = classificationClassesResolverStrategy.resolve(prod, clsysver);
		assertEquals(1, result2.size());
		assertTrue(result2.contains(clclm));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(prod,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());
	}

	private void setupClassificationSystemsMocking(final ClassificationSystemVersionModel... versions)
	{
		Mockito.when(clSysVerSMock.getAllCatalogVersionsOfType(ClassificationSystemVersionModel.class)).thenReturn(
				Arrays.asList(versions));
	}

	@Test
	public void testSomeClassificationClassesAtProduct()
	{
		final ClassificationSystemVersionModel clsysver_A = new ClassificationSystemVersionModel();
		final ClassificationSystemVersionModel clsysver_B = new ClassificationSystemVersionModel();

		final ClassificationClassModel clclm1 = new ClassificationClassModel();
		clclm1.setCatalogVersion(clsysver_A);
		final ClassificationClassModel clclm2 = new ClassificationClassModel();
		clclm2.setCatalogVersion(clsysver_A);
		final ClassificationClassModel clclm3 = new ClassificationClassModel();
		clclm3.setCatalogVersion(clsysver_B);
		final ProductModel prod = new ProductModel();
		final Collection<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(clclm1);
		categories.add(clclm2);
		categories.add(clclm3);
		prod.setSupercategories(categories);

		setupClassificationSystemsMocking(clsysver_A, clsysver_B);


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
	}

	@Test
	public void testSingleClassificationClassWithCategoryAtProduct()
	{
		final ClassificationClassModel clclm = new ClassificationClassModel();
		final CategoryModel cat = new CategoryModel();
		final ProductModel prod = new ProductModel();
		final ClassificationSystemVersionModel clsysver = new ClassificationSystemVersionModel();

		cat.setSupercategories(Arrays.<CategoryModel> asList(clclm));
		prod.setSupercategories(Collections.<CategoryModel> singleton(cat));

		clclm.setCatalogVersion(clsysver);

		setupClassificationSystemsMocking(clsysver);

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clclm));

		final Set<ClassificationClassModel> result2 = classificationClassesResolverStrategy.resolve(prod, clsysver);
		assertEquals(1, result2.size());
		assertTrue(result2.contains(clclm));

		final Set<ClassificationClassModel> result3 = classificationClassesResolverStrategy.resolve(prod,
				new ClassificationSystemVersionModel());
		assertTrue(result3.isEmpty());

		final Set<ClassificationClassModel> result4 = classificationClassesResolverStrategy.resolve(cat);
		assertEquals(1, result4.size());
		assertTrue(result4.contains(clclm));

		final Set<ClassificationClassModel> result5 = classificationClassesResolverStrategy.resolve(cat, clsysver);
		assertEquals(1, result5.size());
		assertTrue(result5.contains(clclm));

		final Set<ClassificationClassModel> result6 = classificationClassesResolverStrategy.resolve(cat,
				new ClassificationSystemVersionModel());
		assertTrue(result6.isEmpty());
	}

	@Test
	public void testSeveralClassificationClassesAtProductWithCategory()
	{
		final ClassificationSystemVersionModel clsysver_A = new ClassificationSystemVersionModel();
		final ClassificationSystemVersionModel clsysver_B = new ClassificationSystemVersionModel();

		final ClassificationClassModel clclm1 = new ClassificationClassModel();
		clclm1.setCatalogVersion(clsysver_A);
		final ClassificationClassModel clclm2 = new ClassificationClassModel();
		clclm2.setCatalogVersion(clsysver_A);
		final ClassificationClassModel clclm3 = new ClassificationClassModel();
		clclm3.setCatalogVersion(clsysver_B);
		final ProductModel prod = new ProductModel();
		final CategoryModel cat = new CategoryModel();

		final List<CategoryModel> categories1 = new ArrayList<CategoryModel>();
		categories1.add(clclm1);
		categories1.add(clclm2);
		cat.setSupercategories(categories1);

		final Collection<CategoryModel> categories2 = new ArrayList<CategoryModel>();
		categories2.add(clclm3);
		categories2.add(cat);
		prod.setSupercategories(categories2);

		setupClassificationSystemsMocking(clsysver_A, clsysver_B);

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
		final ProductModel prod = new ProductModel();
		final ClassificationClassModel clcl1 = new ClassificationClassModel();
		final ClassificationClassModel clcl2 = new ClassificationClassModel();
		final CategoryModel cat = new CategoryModel();
		final ClassificationSystemVersionModel clsysver_A = new ClassificationSystemVersionModel();
		final ClassificationSystemVersionModel clsysver_B = new ClassificationSystemVersionModel();
		clcl1.setCatalogVersion(clsysver_A);
		clcl2.setCatalogVersion(clsysver_A);

		cat.setSupercategories(Arrays.<CategoryModel> asList(clcl2));

		final List<CategoryModel> categories1 = new ArrayList<CategoryModel>();
		categories1.add(clcl1);
		categories1.add(cat);

		prod.setSupercategories(categories1);

		setupClassificationSystemsMocking(clsysver_A, clsysver_B);

		final Set<ClassificationClassModel> result1a = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(2, result1a.size());
		assertTrue(result1a.contains(clcl1));
		assertTrue(result1a.contains(clcl2));

		onlyClosestClasses = true;
		try
		{
			final Set<ClassificationClassModel> result1b = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
			assertEquals(1, result1b.size());
			assertTrue(result1b.contains(clcl1));
		}
		finally
		{
			onlyClosestClasses = false;
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
		final ProductModel prod = new ProductModel();
		final ClassificationClassModel clcl1 = new ClassificationClassModel();
		final ClassificationSystemVersionModel clsysver_A = new ClassificationSystemVersionModel();
		clcl1.setCatalogVersion(clsysver_A);
		final CategoryModel cat1 = new CategoryModel();
		final CategoryModel cat2 = new CategoryModel();
		final CategoryModel cat3 = new CategoryModel();

		cat3.setSupercategories(Arrays.<CategoryModel> asList(clcl1));
		cat2.setSupercategories(Arrays.<CategoryModel> asList(clcl1));
		cat1.setSupercategories(Arrays.<CategoryModel> asList(cat3));

		prod.setSupercategories(Arrays.asList(cat1, cat2));

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clcl1));
	}

	@Test
	public void testDuplicatedCategories()
	{
		final ProductModel prod = new ProductModel();
		final ClassificationClassModel clcl1 = new ClassificationClassModel();
		final ClassificationSystemVersionModel clsysver_A = new ClassificationSystemVersionModel();
		clcl1.setCatalogVersion(clsysver_A);
		final CategoryModel cat1 = new CategoryModel();
		final CategoryModel cat2 = new CategoryModel();

		cat2.setSupercategories(Arrays.<CategoryModel> asList(clcl1));
		cat1.setSupercategories(Arrays.<CategoryModel> asList(cat2));

		prod.setSupercategories(Arrays.asList(cat1, cat2));

		final Set<ClassificationClassModel> result1 = classificationClassesResolverStrategy.resolve(prod, clsysver_A);
		assertEquals(1, result1.size());
		assertTrue(result1.contains(clcl1));
	}

	@Test
	public void testGetClassAttributeAssignments()
	{
		assertTrue(classificationClassesResolverStrategy.getClassAttributeAssignments(null).isEmpty());
		assertTrue(classificationClassesResolverStrategy.getClassAttributeAssignments(Collections.EMPTY_SET).isEmpty());

		final ClassificationClassModel levelA = new ClassificationClassModel();
		final ClassAttributeAssignmentModel camA1 = new ClassAttributeAssignmentModel();
		final ClassAttributeAssignmentModel camA2 = new ClassAttributeAssignmentModel();
		levelA.setDeclaredClassificationAttributeAssignments(Arrays.asList(camA1, camA2));
		final ClassificationClassModel levelB = new ClassificationClassModel();
		final ClassAttributeAssignmentModel camB1 = new ClassAttributeAssignmentModel();
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
	public void testVariantsClassInheritance()
	{

		final ClassificationSystemVersionModel system = new ClassificationSystemVersionModel();
		final ClassificationClassModel cl1 = new ClassificationClassModel();
		cl1.setCatalogVersion(system);
		final ClassificationClassModel cl2 = new ClassificationClassModel();
		cl2.setCatalogVersion(system);
		setupClassificationSystemsMocking(system);

		final CategoryModel category = new CategoryModel();
		category.setSupercategories((List) Collections.singletonList(cl1));

		final ProductModel base = new ProductModel();
		base.setSupercategories(Collections.EMPTY_LIST);
		final VariantProductModel variant1 = new VariantProductModel();
		variant1.setBaseProduct(base);
		variant1.setSupercategories(Collections.EMPTY_LIST);
		final VariantProductModel variant2 = new VariantProductModel();
		variant2.setBaseProduct(variant1);
		variant2.setSupercategories(Collections.EMPTY_LIST);
		final VariantProductModel variant3 = new VariantProductModel();
		variant3.setBaseProduct(variant2);
		variant3.setSupercategories(Collections.EMPTY_LIST);

		// sanity: no classification yet
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant3));

		// direct assignments clClass1 -> variant3 & clClass2 -> variant2 => classes(variant3)==[clClass1,clClass2] & classes(variant2)==[clClass2]
		variant3.setSupercategories((Collection) Collections.singletonList(cl1));
		variant2.setSupercategories((Collection) Collections.singletonList(cl2));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.EMPTY_SET, classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant3));

		variant3.setSupercategories(Collections.EMPTY_SET);
		variant2.setSupercategories(Collections.EMPTY_SET);

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3
		base.setSupercategories(Collections.singletonList(category));

		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant3));

		// top down: clClass1 -> category -> base -> variant1 -> variant2 -> variant3 
		// AND direct assignment clClass2 -> variant2 = classes(variant2)=clClass2 & classes(variant3)=clClass2
		variant2.setSupercategories((Collection) Collections.singletonList(cl2));

		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
		assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant2));
		assertEquals(new HashSet<>(Arrays.asList(cl1, cl2)), classificationClassesResolverStrategy.resolve(variant3));

		onlyClosestClasses = true;
		try
		{
			assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(base));
			assertEquals(Collections.singleton(cl1), classificationClassesResolverStrategy.resolve(variant1));
			assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant2));
			assertEquals(Collections.singleton(cl2), classificationClassesResolverStrategy.resolve(variant3));
		}
		finally
		{
			onlyClosestClasses = false;
		}


	}
}
