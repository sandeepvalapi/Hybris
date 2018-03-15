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

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.fest.util.Collections;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests demonstrating usage of the variants service.
 */
@DemoTest
public class VariantsServiceDemoTest extends ServicelayerTransactionalTest
{

	@Resource
	private VariantsService variantsService;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	private VariantTypeModel testVariantTypeModel;
	private VariantAttributeDescriptorModel sizeDescriptor;
	private VariantAttributeDescriptorModel colorDescriptor;
	private ProductModel baseProduct;

	/**
	 * prepares some testdata, before execution of every test:<br/>
	 * - the test variantTypeModel will be created and saved<br/>
	 * - the base product (productModel) will be created with assigned variantTypeModel<br/>
	 * - two VariantAttributeDescriptors will be prepared (not saved) for usage in the tests<br/>
	 */
	@Before
	public void setUp() throws Exception
	{
		//create a test variant type model (no attributes assigned
		testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);

		//prepare some attribute descriptors
		sizeDescriptor = createVariantDescriptor();
		sizeDescriptor.setQualifier("size");

		colorDescriptor = createVariantDescriptor();
		colorDescriptor.setQualifier("color");

		//create catalog and catalog version - the required parameters for productModel, which will be created after that
		final CatalogModel testCatalog = modelService.create(CatalogModel.class);
		testCatalog.setId("id");
		final CatalogVersionModel testCatalogVersion = modelService.create(CatalogVersionModel.class);
		testCatalogVersion.setActive(Boolean.TRUE);
		testCatalogVersion.setCatalog(testCatalog);
		testCatalogVersion.setVersion("xxx");
		modelService.saveAll(testCatalog, testCatalogVersion);

		//create the base product with assigned variantType
		baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseprod");
		baseProduct.setCatalogVersion(testCatalogVersion);
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

	}

	/**
	 * creates a VariantAttributeDescriptorModel with a bunch of default values
	 */
	private VariantAttributeDescriptorModel createVariantDescriptor()
	{
		final VariantAttributeDescriptorModel localDescriptor = modelService.create(VariantAttributeDescriptorModel.class);
		localDescriptor.setEnclosingType(testVariantTypeModel);
		localDescriptor.setGenerate(Boolean.TRUE);
		localDescriptor.setPartOf(Boolean.FALSE);
		localDescriptor.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));
		return localDescriptor;
	}

	/**
	 * Demonstrates how to use the variantsService to get and set the VariantAttributes for the VariantType. It shows
	 * also how to get all existing variant types.
	 * 
	 * Test scenario:<br/>
	 * - check the existing variantTypes (the returned list should contain at least the one created in the setUp).<br/>
	 * - check the already assigned variantAttributes for the variantType (no items expected).<br/>
	 * - set the new attributes (prepared in setUp) for the variantType, using the appropriate service method. <br/>
	 * - check the assigned attributes for the variant type (now the just set attributes are expected).<br/>
	 * - create a new AttributeDescriptor List, use one of the old (already assigned) attributes and add two new
	 * attributes.<br/>
	 * - set the new attributes list using the appropriate service method <br/>
	 * - check which attributes are assigned (the last passed attributes are expected)<br/>
	 * 
	 */
	@Test
	public void testSettingVariantAttributesCorrectly()
	{
		//check if the prepared variantType can be found on the "all variant types list".
		assertFalse(Collections.isEmpty(variantsService.getAllVariantTypes()));
		assertTrue("There should available '" + testVariantTypeModel + "' in result for all variant types ", variantsService
				.getAllVariantTypes().contains(testVariantTypeModel));

		//check if any attributes are assigned for the variantType
		final List<VariantAttributeDescriptorModel> currentAttributes = variantsService
				.getVariantAttributesForVariantType(testVariantTypeModel);
		assertTrue("There should not available any VariantAttributeDescriptorModel for a '" + testVariantTypeModel
				+ "'since they haven't been assigned yet", currentAttributes.isEmpty());

		//prepare the attribute - list to set
		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(sizeDescriptor);
		newAttributes.add(colorDescriptor);

		//set the attributes for the variant type
		variantsService.setVariantAttributesForVariantType(testVariantTypeModel, newAttributes);
		//check if correctly assigned
		compareAttributes(testVariantTypeModel, newAttributes);

		//next attempt - prepare new attribute list, with 3 elements - but one of them already exists
		final VariantAttributeDescriptorModel newDescriptor3 = createVariantDescriptor();//modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor3.setQualifier("newDescriptor3");

		final VariantAttributeDescriptorModel newDescriptor4 = createVariantDescriptor();//modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor4.setQualifier("newDescriptor4");

		newAttributes.clear();
		newAttributes.add(newDescriptor3);
		newAttributes.add(sizeDescriptor);
		newAttributes.add(newDescriptor4);

		//set new the attributes for variant type 
		variantsService.setVariantAttributesForVariantType(testVariantTypeModel, newAttributes);
		//check is correctly assigned
		compareAttributes(testVariantTypeModel, newAttributes);

	}

	/**
	 * Demonstrates how to use the variantsService to get the existing attributes for base product. It shows also how to
	 * set the values for variant attributes.
	 * 
	 * Test scenario:<br/>
	 * - check the existing variantTypes (the returned list should contain at least the one created in the setUp).<br/>
	 * - create a new variantProductModel with assigned base product (the base product should have proper variantType
	 * assigned) and save it.<br/>
	 * - create two variantProductModels with assigned base product (the base product should have proper variantType
	 * assigned). <br/>
	 * - set new attributes (prepared in setUp) for the variantType, using the appropriate service method. <br/>
	 * - get the assigned variantAtrributes (for base product). The result should contain the attributes prepared before.
	 * No Attribute Values are assigned for them at this moment. <br/>
	 * - assign some values for the attribute descriptors, using appropriate variantsService method.<br/>
	 * - get the assigned variantAtrributes (for base product). - the result should contain both Attribute descriptors
	 * with the appropriate values which has been set. <br/>
	 * 
	 */
	@Test
	public void testGettingAssignedVariantAttributesForBaseProduct()
	{
		//check if the prepared variantType can be found on the "all variant types list".
		assertFalse(Collections.isEmpty(variantsService.getAllVariantTypes()));
		assertTrue(variantsService.getAllVariantTypes().contains(testVariantTypeModel));

		//create two variantproductModels and save it

		final VariantProductModel testVariantProduct1 = modelService.create(testVariantTypeModel.getCode());
		testVariantProduct1.setCode("aaa");
		testVariantProduct1.setCatalogVersion(baseProduct.getCatalogVersion());
		testVariantProduct1.setBaseProduct(baseProduct);
		modelService.save(testVariantProduct1);
		assertNotNull(testVariantProduct1);

		//create two variantproductModels and save it
		final VariantProductModel testVariantProduct2 = modelService.create(VariantProductModel.class);
		testVariantProduct2.setCode("bbb");
		testVariantProduct2.setCatalogVersion(baseProduct.getCatalogVersion());
		testVariantProduct2.setBaseProduct(baseProduct);
		modelService.save(testVariantProduct2);
		assertNotNull(testVariantProduct2);

		//prepare the attribute - list to set and assign it to the variantType
		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(sizeDescriptor);
		newAttributes.add(colorDescriptor);
		//set the attributes for the variant type
		variantsService.setVariantAttributesForVariantType(testVariantTypeModel, newAttributes);

		//get the assigned variantAtrributes
		Map<String, Collection<Object>> result = variantsService.getAssignedVariantAttributes(baseProduct);
		assertNotNull(result);
		//The result should contain both Attribute descriptors, however, no values are assigned for them at this moment.
		assertTrue(result.containsKey(sizeDescriptor.getQualifier()));
		assertTrue(result.containsKey(colorDescriptor.getQualifier()));

		assertEquals(java.util.Collections.singleton(null), result.get(sizeDescriptor.getQualifier()));
		assertEquals(java.util.Collections.singleton(null), result.get(colorDescriptor.getQualifier()));

		//set the preapred values
		variantsService.setVariantAttributeValue(testVariantProduct1, sizeDescriptor.getQualifier(), "30");
		variantsService.setVariantAttributeValue(testVariantProduct1, colorDescriptor.getQualifier(), "blue");
		variantsService.setVariantAttributeValue(testVariantProduct2, sizeDescriptor.getQualifier(), "32");
		variantsService.setVariantAttributeValue(testVariantProduct2, colorDescriptor.getQualifier(), "green");

		final List<String> expectedSizeValues = Arrays.asList("30", "32");
		final List<String> expectedColorValues = Arrays.asList("blue", "green");

		//get assigned attributes again
		result = variantsService.getAssignedVariantAttributes(baseProduct);
		assertNotNull(result);
		//The result should contain both Attribute descriptors with the values, which just have been assigned.
		assertTrue(result.containsKey(sizeDescriptor.getQualifier()));
		assertTrue(result.containsKey(colorDescriptor.getQualifier()));
		final Set<String> sizeValues = (Set) result.get(sizeDescriptor.getQualifier());
		final Set<String> colorValues = (Set) result.get(colorDescriptor.getQualifier());
		assertTrue(sizeValues.size() == 2);
		assertTrue(colorValues.size() == 2);
		assertTrue(sizeValues.containsAll(expectedSizeValues));
		assertTrue(colorValues.containsAll(expectedColorValues));

	}

	/**
	 * Demonstrates how to use the variantsService to get variant products which matches the given filter with attribute
	 * values.
	 * 
	 * Test scenario:<br/>
	 * - check the existing variantTypes (the returned list should contain at least the one created in the setUp).<br/>
	 * - create a new variantProductModel with assigned base product (the base product should have proper variantType
	 * assigned) and save it.<br/>
	 * - create three variantProductModels with assigned base product (the base product should have proper variantType
	 * assigned). <br/>
	 * - set new attributes (prepared in setUp) for the variantType, using the appropriate service method. <br/>
	 * - assign some values for the attribute descriptors, using appropriate variantsService method.<br/>
	 * - prepare the filter map and use it for searching the variantProducts - check the results, change the filter and
	 * try again, 2 times
	 */
	@Test
	public void testGettingVariantProductsForAttributeValues()
	{
		//check if the prepared variantType can be found on the "all variant types list".
		assertFalse(Collections.isEmpty(variantsService.getAllVariantTypes()));
		assertTrue(variantsService.getAllVariantTypes().contains(testVariantTypeModel));

		//create three variantproductModels and save it
		final VariantProductModel variant1 = modelService.create(testVariantTypeModel.getCode());
		variant1.setCode("aaa");
		variant1.setCatalogVersion(baseProduct.getCatalogVersion());
		variant1.setBaseProduct(baseProduct);
		modelService.save(variant1);
		assertNotNull(variant1);

		final VariantProductModel variant2 = modelService.create(VariantProductModel.class);
		variant2.setCode("bbb");
		variant2.setCatalogVersion(baseProduct.getCatalogVersion());
		variant2.setBaseProduct(baseProduct);
		modelService.save(variant2);
		assertNotNull(variant2);

		final VariantProductModel variant3 = modelService.create(VariantProductModel.class);
		variant3.setCode("ccc");
		variant3.setCatalogVersion(baseProduct.getCatalogVersion());
		variant3.setBaseProduct(baseProduct);
		modelService.save(variant3);
		assertNotNull(variant3);

		//prepare the attribute - list to set and assign it to the variantType
		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(sizeDescriptor);
		newAttributes.add(colorDescriptor);
		//set the attributes for the variant type
		variantsService.setVariantAttributesForVariantType(testVariantTypeModel, newAttributes);

		//set the attribute values
		variantsService.setVariantAttributeValue(variant1, sizeDescriptor.getQualifier(), "30");
		variantsService.setVariantAttributeValue(variant1, colorDescriptor.getQualifier(), "blue");
		variantsService.setVariantAttributeValue(variant2, sizeDescriptor.getQualifier(), "32");
		variantsService.setVariantAttributeValue(variant2, colorDescriptor.getQualifier(), "green");
		variantsService.setVariantAttributeValue(variant3, sizeDescriptor.getQualifier(), "32");
		variantsService.setVariantAttributeValue(variant3, colorDescriptor.getQualifier(), "blue");

		//prepare the filter to be used for search
		final Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("size", "32");
		filterMap.put("color", "blue");

		//find variant products which matches the given filter - variant3 expected as result 
		Collection<VariantProductModel> result = variantsService.getVariantProductForAttributeValues(baseProduct, filterMap);
		assertTrue(result != null && result.size() == 1);
		assertTrue(result.contains(variant3));

		//change the filter
		filterMap.clear();
		filterMap.put("size", "32");

		//search again - variant2 and variant3 expected as result
		result = variantsService.getVariantProductForAttributeValues(baseProduct, filterMap);
		assertTrue(result != null && result.size() == 2);
		assertTrue(result.contains(variant2));
		assertTrue(result.contains(variant3));

		//change the filter
		filterMap.clear();
		filterMap.put("size", "32");
		filterMap.put("color", "red");

		//search again - no matching result.
		result = variantsService.getVariantProductForAttributeValues(baseProduct, filterMap);
		assertTrue(result == null || result.isEmpty());

	}

	/**
	 * compares two Lists of attribute descriptors, the given one with the determined List for given Variant Type. The
	 * position and the enclosing type will be checked as well.
	 * 
	 * @param variantType
	 *           the variantType to find the current attributes to compare.
	 * @param expectedAttributes
	 *           the attribute to list to compare.
	 * 
	 */
	private void compareAttributes(final VariantTypeModel variantType,
			final List<VariantAttributeDescriptorModel> expectedAttributes)
	{
		final List<VariantAttributeDescriptorModel> currentAttributes = variantsService
				.getVariantAttributesForVariantType(variantType);

		assertNotNull(currentAttributes);
		assertTrue(currentAttributes.size() == expectedAttributes.size());
		assertTrue(currentAttributes.containsAll(expectedAttributes));
		for (int i = 0; i < currentAttributes.size(); i++)
		{
			assertTrue(currentAttributes.get(i).getPosition() == Integer.valueOf(i));
			assertTrue(currentAttributes.get(i).getEnclosingType().equals(variantType));
			assertTrue(currentAttributes.get(i).getQualifier().equals(expectedAttributes.get(i).getQualifier()));
			assertSame(currentAttributes.get(i), expectedAttributes.get(i));
		}
	}

}
