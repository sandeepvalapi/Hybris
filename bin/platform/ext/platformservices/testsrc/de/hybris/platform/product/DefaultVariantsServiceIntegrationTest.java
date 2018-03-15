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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * test-class for DefaultVariantsService.
 * 
 */
@IntegrationTest
public class DefaultVariantsServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private VariantsService variantsService;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	private VariantTypeModel testVariantType;

	@Before
	public void setUp() throws Exception
	{
		//creating test data
		createCoreData();
		createDefaultCatalog();

		//create a test variant type model (no attributes assigned
		testVariantType = modelService.create(VariantTypeModel.class);
		testVariantType.setCode("vt");
		testVariantType.setSingleton(Boolean.FALSE);
		testVariantType.setGenerate(Boolean.TRUE);
		testVariantType.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantType);
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#setVariantAttributesForVariantType(VariantTypeModel, List)}
	 * .
	 */
	@Test
	public void testSetVariantAttributesForVariantTypeValidAttributeList()
	{
		//given
		final VariantAttributeDescriptorModel newDescriptor1 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor1.setQualifier("newDescriptor1");
		newDescriptor1.setEnclosingType(testVariantType);
		newDescriptor1.setGenerate(Boolean.TRUE);
		newDescriptor1.setPartOf(Boolean.FALSE);
		newDescriptor1.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final VariantAttributeDescriptorModel newDescriptor2 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor2.setQualifier("newDescriptor2");
		newDescriptor2.setEnclosingType(testVariantType);
		newDescriptor2.setGenerate(Boolean.TRUE);
		newDescriptor2.setPartOf(Boolean.FALSE);
		newDescriptor2.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(newDescriptor1);
		newAttributes.add(newDescriptor2);

		//when
		variantsService.setVariantAttributesForVariantType(testVariantType, newAttributes);
		//then
		compareAttributes(testVariantType, newAttributes);

		//next attempt - new attributtes with 3 elements - but one of them already exists
		final VariantAttributeDescriptorModel newDescriptor3 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor3.setQualifier("newDescriptor3");
		newDescriptor3.setEnclosingType(testVariantType);
		newDescriptor3.setGenerate(Boolean.TRUE);
		newDescriptor3.setPartOf(Boolean.FALSE);
		newDescriptor3.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final VariantAttributeDescriptorModel newDescriptor4 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor4.setQualifier("newDescriptor4");
		newDescriptor4.setEnclosingType(testVariantType);
		newDescriptor4.setGenerate(Boolean.TRUE);
		newDescriptor4.setPartOf(Boolean.FALSE);
		newDescriptor4.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		newAttributes.clear();
		newAttributes.add(newDescriptor3);
		newAttributes.add(newDescriptor1);
		newAttributes.add(newDescriptor4);

		//when
		variantsService.setVariantAttributesForVariantType(testVariantType, newAttributes);
		//then
		compareAttributes(testVariantType, newAttributes);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#setVariantAttributesForVariantType(VariantTypeModel, List)}
	 * .
	 */
	@Test
	public void testSetVariantAttributesForVariantTypeAttributeListNull()
	{
		//given
		final List<VariantAttributeDescriptorModel> newAttributes = null;

		//when
		variantsService.setVariantAttributesForVariantType(testVariantType, newAttributes);
		//then
		final List<VariantAttributeDescriptorModel> currentAttributes = variantsService
				.getVariantAttributesForVariantType(testVariantType);

		assertTrue(currentAttributes == null || currentAttributes.isEmpty());

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#setVariantAttributesForVariantType(VariantTypeModel, List)}
	 * .
	 */
	@Test
	public void testSetVariantAttributesForVariantTypeVariantTypeIsNotSaved()
	{

		final VariantTypeModel testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("variantTypeCode");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);

		final VariantAttributeDescriptorModel colorDescriptor = modelService.create(VariantAttributeDescriptorModel.class);
		colorDescriptor.setQualifier("color");
		colorDescriptor.setEnclosingType(testVariantTypeModel);
		colorDescriptor.setGenerate(Boolean.TRUE);
		colorDescriptor.setPartOf(Boolean.FALSE);
		colorDescriptor.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(colorDescriptor);

		//when
		try
		{
			variantsService.setVariantAttributesForVariantType(testVariantTypeModel, newAttributes);
			Assert.fail("Settting variant attributtes for not saved " + testVariantTypeModel + " should fail");
		}
		catch (final IllegalArgumentException ile)
		{
			//ok here
		}


	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#setVariantAttributesForVariantType(VariantTypeModel, List)}
	 * .
	 */
	@Test(expected = SystemException.class)
	public void testSetVariantAttributesForVariantTypeWrongEnclosingType()
	{
		//given		
		final VariantAttributeDescriptorModel newDescriptor1 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor1.setQualifier("newDescriptor1");
		//		enclosing type not set - it should lead to an exception
		newDescriptor1.setGenerate(Boolean.TRUE);
		newDescriptor1.setPartOf(Boolean.FALSE);
		newDescriptor1.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final VariantAttributeDescriptorModel newDescriptor2 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor2.setQualifier("newDescriptor2");
		newDescriptor2.setEnclosingType(testVariantType);
		newDescriptor2.setGenerate(Boolean.TRUE);
		newDescriptor2.setPartOf(Boolean.FALSE);
		newDescriptor2.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final List<VariantAttributeDescriptorModel> newAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		newAttributes.add(newDescriptor1);
		newAttributes.add(newDescriptor2);

		//when
		variantsService.setVariantAttributesForVariantType(testVariantType, newAttributes);
		//then
		//Exception should be throw as declared

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#setVariantAttributesForVariantType(VariantTypeModel, List)}
	 * .
	 */
	@Test
	//(expected = SystemException.class)
	public void testSetVariantAttributesForVariantTypeWithInheritedAttributes()
	{
		//given - prepare and save variantAttributes for variantType
		final VariantAttributeDescriptorModel newDescriptor1 = modelService.create(VariantAttributeDescriptorModel.class);
		newDescriptor1.setQualifier("newDescriptor1");
		newDescriptor1.setEnclosingType(testVariantType);
		newDescriptor1.setGenerate(Boolean.TRUE);
		newDescriptor1.setPartOf(Boolean.FALSE);
		newDescriptor1.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final List<VariantAttributeDescriptorModel> superAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		superAttributes.add(newDescriptor1);

		//when
		variantsService.setVariantAttributesForVariantType(testVariantType, superAttributes);
		//then
		compareAttributes(testVariantType, superAttributes); //should be correctly saved

		//next attempt - but with a sub - variantType
		final VariantTypeModel subTestVariantType = modelService.create(VariantTypeModel.class);
		subTestVariantType.setCode("subVt");
		subTestVariantType.setSingleton(Boolean.FALSE);
		subTestVariantType.setGenerate(Boolean.TRUE);
		subTestVariantType.setCatalogItemType(Boolean.FALSE);
		subTestVariantType.setSuperType(testVariantType);
		modelService.save(subTestVariantType);

		final VariantAttributeDescriptorModel subDescriptor1 = modelService.create(VariantAttributeDescriptorModel.class);
		subDescriptor1.setQualifier("subDescriptor1");
		subDescriptor1.setEnclosingType(subTestVariantType);
		subDescriptor1.setGenerate(Boolean.TRUE);
		subDescriptor1.setPartOf(Boolean.FALSE);
		subDescriptor1.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));

		final List<VariantAttributeDescriptorModel> subAttributes = new ArrayList<VariantAttributeDescriptorModel>();
		subAttributes.add(subDescriptor1);

		//try to save the attributes for the subVariantType, without the inherited attributes - should fail.
		try
		{
			variantsService.setVariantAttributesForVariantType(subTestVariantType, subAttributes);
			Assert.fail("Exception was expected but not thrown ");
		}
		catch (final SystemException e)
		{
			//	 correct - the variant attributes list doesn't inlude the inherited attributes  
		}

		//now try to save it correctly - with declared + inherited variant atributes
		final List<VariantAttributeDescriptorModel> fullAttributeList = new ArrayList<VariantAttributeDescriptorModel>();
		fullAttributeList.addAll(variantsService.getVariantAttributesForVariantType(subTestVariantType));
		fullAttributeList.addAll(subAttributes);

		variantsService.setVariantAttributesForVariantType(subTestVariantType, fullAttributeList);
		compareAttributes(subTestVariantType, fullAttributeList); //should be correctly saved

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
		}
	}
}
