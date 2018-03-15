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
package de.hybris.platform.catalog.jalo.classification;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ClassAttributeAssignmentTest extends HybrisJUnit4Test
{
	private CatalogManager catalogManager;
	private ProductManager productManager;

	private Language german;
	private ClassificationSystem sys;
	private ClassificationSystemVersion sysVer;
	private ClassificationAttribute color, size;
	private ClassificationClass appleproducts, macbookpro;
	private Product product1;
	private ClassAttributeAssignment caa;

	@Before
	public void setUp() throws Exception
	{
		// set de as language to get german locale !!!
		jaloSession.getSessionContext().setLanguage(german = getOrCreateLanguage("de"));

		catalogManager = CatalogManager.getInstance();
		productManager = ProductManager.getInstance();

		assertNotNull(sys = catalogManager.createClassificationSystem("TestSys"));

		assertNotNull(sysVer = catalogManager.createClassificationSystemVersion(sys, "2.0", german));

		assertNotNull(color = sysVer.createClassificationAttribute("color"));
		assertNotNull(size = sysVer.createClassificationAttribute("size"));

		final ClassificationAttributeValue green = sysVer.createClassificationAttributeValue("green");
		final ClassificationAttributeValue black = sysVer.createClassificationAttributeValue("black");

		color.setDefaultAttributeValues(Arrays.asList(green, black));

		assertNotNull(appleproducts = sysVer.createClass("appleproducts"));
		assertNotNull(macbookpro = sysVer.createClass(appleproducts, "macbookpro"));

		//assign size to macbookpro
		macbookpro.assignAttribute(size);
		//assign color to macbookpro
		caa = macbookpro.assignAttribute(color);
		macbookpro.setAttributeType(
				color,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING));
		assertNotNull(product1 = productManager.createProduct("p1"));
		CategoryManager.getInstance().setSupercategories(product1, Arrays.asList(new Category[]
		{ macbookpro }));

	}

	@Test(expected = JaloInvalidParameterException.class)
	public void testDuplicates()
	{
		//reassign color to macbookpro to check for duplicates
		caa.setClassificationClass(macbookpro);
	}

	@Test
	public void testFeatureValues()
	{
		//set color value to "red"
		final FeatureContainer cont1 = FeatureContainer.loadTyped(product1, caa);
		final TypedFeature<String> featureSet = cont1.getFeature(caa);
		featureSet.setValue("red");
		try
		{
			cont1.store();
		}
		catch (final ConsistencyCheckException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		//assign color to appleproducts
		caa.setClassificationClass(appleproducts);
		final FeatureContainer cont2 = FeatureContainer.loadTyped(product1, caa);
		//get color value
		final TypedFeature<String> featureGet = cont2.getFeature(caa);
		//compare values
		assertEquals(featureSet.getValue(0), featureGet.getValue(0));
	}
}
