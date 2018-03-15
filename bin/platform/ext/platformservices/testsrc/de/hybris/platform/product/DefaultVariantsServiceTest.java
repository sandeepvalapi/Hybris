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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.daos.VariantTypeDao;
import de.hybris.platform.product.impl.DefaultVariantsService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * test-class for DefaultVariantsService.
 * 
 */
@UnitTest
public class DefaultVariantsServiceTest
{

	@InjectMocks
	private final DefaultVariantsService variantsService = new DefaultVariantsService();

	@Mock
	private VariantTypeDao mockVariantTypeDao;

	@Mock
	private ModelService mockModelService;

	@Mock
	private TypeService mockTypeService;

	@Mock
	private ProductModel mockProductModel1;

	@Mock
	private VariantProductModel mockVariantProductModel1;
	@Mock
	private VariantProductModel mockVariantProductModel2;
	@Mock
	private VariantProductModel mockVariantProductModel3;

	@Mock
	private VariantTypeModel mockVariantTypeModel;

	@Mock
	private VariantTypeModel mockVariantTypeModel1;

	@Mock
	private VariantTypeModel mockVariantTypeModel2;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(mockProductModel1.getVariants()).thenReturn(
				Arrays.asList(mockVariantProductModel1, mockVariantProductModel2, mockVariantProductModel3));
		when(mockProductModel1.getVariantType()).thenReturn(mockVariantTypeModel);

	}

	/**
	 * Test method for {@link de.hybris.platform.product.impl.DefaultVariantsService#getAllVariantTypes()}.
	 */
	@Test
	public void testGetAllVariantTypes()
	{
		//given
		final List<VariantTypeModel> givenVariantTypes = Arrays.asList(mockVariantTypeModel1, mockVariantTypeModel2);
		when(mockVariantTypeDao.findAllVariantTypes()).thenReturn(givenVariantTypes);

		//when
		final Collection<VariantTypeModel> resultList = variantsService.getAllVariantTypes();

		//then
		assertNotNull(resultList);
		assertEquals(givenVariantTypes.size(), resultList.size());

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getAssignedVariantAttributes(de.hybris.platform.core.model.product.ProductModel)}
	 * .
	 */
	@Test
	public void testGetAssignedVariantAttributes()
	{
		//given		
		final VariantAttributeDescriptorModel sizeDescriptor = new VariantAttributeDescriptorModel();
		sizeDescriptor.setQualifier("Size");

		final VariantAttributeDescriptorModel colorDescriptor = new VariantAttributeDescriptorModel();
		colorDescriptor.setQualifier("Color");

		final Set<AttributeDescriptorModel> descriptors = new HashSet<AttributeDescriptorModel>();
		descriptors.add(sizeDescriptor);
		descriptors.add(colorDescriptor);
		when(mockTypeService.getAttributeDescriptors(mockVariantTypeModel)).thenReturn(descriptors);

		//mock variant1 - size:30, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel1, sizeDescriptor.getQualifier())).thenReturn("30");
		when(mockModelService.getAttributeValue(mockVariantProductModel1, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant2 - size:32, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel2, sizeDescriptor.getQualifier())).thenReturn("32");
		when(mockModelService.getAttributeValue(mockVariantProductModel2, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant3 - size:32, color:red
		when(mockModelService.getAttributeValue(mockVariantProductModel3, sizeDescriptor.getQualifier())).thenReturn("32");
		when(mockModelService.getAttributeValue(mockVariantProductModel3, colorDescriptor.getQualifier())).thenReturn("red");

		//when
		final Map<String, Collection<Object>> result = variantsService.getAssignedVariantAttributes(mockProductModel1);

		//then
		assertNotNull(result);
		assertTrue(result.containsKey(sizeDescriptor.getQualifier()));
		assertTrue(result.containsKey(colorDescriptor.getQualifier()));

		final Collection sizeValues = result.get(sizeDescriptor.getQualifier());
		final Collection colorValues = result.get(colorDescriptor.getQualifier());
		assertTrue(sizeValues.size() == 2); // 30 and 32
		assertTrue(colorValues.size() == 2); // blue and red

		assertTrue(sizeValues.containsAll(Arrays.asList("30", "32")));
		assertTrue(colorValues.containsAll(Arrays.asList("blue", "red")));

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getAssignedVariantAttributes(de.hybris.platform.core.model.product.ProductModel)}
	 * .
	 */
	@Test
	public void testGetAssignedVariantAttributesBaseProductNull()
	{
		try
		{
			variantsService.getAssignedVariantAttributes(null);
			fail("An illegal argument Exception was expected but not thrown");
		}
		catch (final IllegalArgumentException ile)
		{
			//ok here 
		}

	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getVariantProductForAttributeValues(de.hybris.platform.core.model.product.ProductModel, java.util.Map)}
	 * .
	 */
	@Test
	public void testGetVariantProductForAttributeValuesFilterWithOneAttribute()
	{
		//given		
		final VariantAttributeDescriptorModel sizeDescriptor = new VariantAttributeDescriptorModel();
		sizeDescriptor.setQualifier("Size");
		final VariantAttributeDescriptorModel colorDescriptor = new VariantAttributeDescriptorModel();
		colorDescriptor.setQualifier("Color");

		//		final List<VariantAttributeDescriptorModel> descriptors = Arrays.asList(sizeDescriptor, colorDescriptor);
		//		when(mockVariantTypeModel.getVariantAttributes()).thenReturn(descriptors);
		final Set<AttributeDescriptorModel> descriptors = new HashSet<AttributeDescriptorModel>();
		descriptors.add(sizeDescriptor);
		descriptors.add(colorDescriptor);
		when(mockTypeService.getAttributeDescriptors(mockVariantTypeModel)).thenReturn(descriptors);

		//mock variant1 - size:30, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel1, sizeDescriptor.getQualifier())).thenReturn("M");
		when(mockModelService.getAttributeValue(mockVariantProductModel1, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant2 - size:32, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel2, sizeDescriptor.getQualifier())).thenReturn("L");
		when(mockModelService.getAttributeValue(mockVariantProductModel2, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant3 - size:32, color:red
		when(mockModelService.getAttributeValue(mockVariantProductModel3, sizeDescriptor.getQualifier())).thenReturn("L");
		when(mockModelService.getAttributeValue(mockVariantProductModel3, colorDescriptor.getQualifier())).thenReturn("red");

		final Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("Size", "L");

		//when
		final Collection<VariantProductModel> result = variantsService.getVariantProductForAttributeValues(mockProductModel1,
				filterMap);

		//then
		assertNotNull(result);
		assertTrue(result.size() == 2);
		assertTrue(result.contains(mockVariantProductModel2));
		assertTrue(result.contains(mockVariantProductModel3));

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getVariantProductForAttributeValues(de.hybris.platform.core.model.product.ProductModel, java.util.Map)}
	 * .
	 */
	@Test
	public void testGetVariantProductForAttributeValuesFilterWithAllAttributes()
	{
		//given		
		final VariantAttributeDescriptorModel sizeDescriptor = new VariantAttributeDescriptorModel();
		sizeDescriptor.setQualifier("Size");
		final VariantAttributeDescriptorModel colorDescriptor = new VariantAttributeDescriptorModel();
		colorDescriptor.setQualifier("Color");

		final Set<AttributeDescriptorModel> descriptors = new HashSet<AttributeDescriptorModel>();
		descriptors.add(sizeDescriptor);
		descriptors.add(colorDescriptor);
		when(mockTypeService.getAttributeDescriptors(mockVariantTypeModel)).thenReturn(descriptors);

		//mock variant1 - size:30, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel1, sizeDescriptor.getQualifier())).thenReturn("M");
		when(mockModelService.getAttributeValue(mockVariantProductModel1, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant2 - size:32, color:blue
		when(mockModelService.getAttributeValue(mockVariantProductModel2, sizeDescriptor.getQualifier())).thenReturn("L");
		when(mockModelService.getAttributeValue(mockVariantProductModel2, colorDescriptor.getQualifier())).thenReturn("blue");
		//mock variant3 - size:32, color:red
		when(mockModelService.getAttributeValue(mockVariantProductModel3, sizeDescriptor.getQualifier())).thenReturn("L");
		when(mockModelService.getAttributeValue(mockVariantProductModel3, colorDescriptor.getQualifier())).thenReturn("red");

		final Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("Size", "L");
		filterMap.put("Color", "blue");


		//when
		final Collection<VariantProductModel> result = variantsService.getVariantProductForAttributeValues(mockProductModel1,
				filterMap);

		//then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(mockVariantProductModel2);
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getVariantProductForAttributeValues(de.hybris.platform.core.model.product.ProductModel, java.util.Map)}
	 * .
	 */
	@Test
	public void testGetVariantProductForAttributeValuesNoFilter()
	{
		//when
		final Collection<VariantProductModel> result = variantsService.getVariantProductForAttributeValues(mockProductModel1, null);

		//then
		assertNotNull(result);
		assertThat(result).isEmpty();

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getVariantProductForAttributeValues(de.hybris.platform.core.model.product.ProductModel, java.util.Map)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetVariantProductForAttributeValuesBaseProductNull()
	{
		variantsService.getVariantProductForAttributeValues(null, new HashMap<String, Object>());
		fail("An illegal argument Exception was expected but not thrown");
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.product.impl.DefaultVariantsService#getVariantAttributesForVariantType(VariantTypeModel)}
	 * .
	 */
	@Test
	public void testGetVariantAttributesForVariantType()
	{
		//given		
		final VariantAttributeDescriptorModel sizeDescriptor = new VariantAttributeDescriptorModel();
		sizeDescriptor.setQualifier("Size");
		sizeDescriptor.setPosition(Integer.valueOf(5));
		final VariantAttributeDescriptorModel colorDescriptor = new VariantAttributeDescriptorModel();
		colorDescriptor.setQualifier("Color");
		colorDescriptor.setPosition(Integer.valueOf(2));
		final VariantAttributeDescriptorModel statusDescriptor = new VariantAttributeDescriptorModel();
		statusDescriptor.setQualifier("Status");
		statusDescriptor.setPosition(Integer.valueOf(3));

		final Set<AttributeDescriptorModel> descriptors = new HashSet<AttributeDescriptorModel>();
		descriptors.add(sizeDescriptor);
		descriptors.add(colorDescriptor);
		descriptors.add(statusDescriptor);
		when(mockTypeService.getAttributeDescriptors(mockVariantTypeModel)).thenReturn(descriptors);

		//when
		final List<VariantAttributeDescriptorModel> result = variantsService
				.getVariantAttributesForVariantType(mockVariantTypeModel);

		//then
		assertNotNull(result);
		assertTrue(result.containsAll(descriptors));
		//check if correctly sorted
		assertTrue(result.get(0).equals(colorDescriptor));
		assertTrue(result.get(1).equals(statusDescriptor));
		assertTrue(result.get(2).equals(sizeDescriptor));

	}


	@Test
	public void testGetVariantAttributesForNullCode()
	{
		try
		{
			variantsService.getVariantAttributes(null);
			fail("Null argument should throw an exception ");
		}
		catch (final IllegalArgumentException ile)
		{
			// fine here 
		}
	}

	@Test
	public void testGetVariantAttributesForTypeServiceThrowingAnException()
	{

		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenThrow(
				new de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException("sorry this is too dummy type"));
		try
		{
			variantsService.getVariantAttributes("someDummyType");
			fail("Null argument should throw an exception ");
		}
		catch (final AmbiguousIdentifierException ile)
		{
			assertEquals("sorry this is too dummy type", ile.getMessage());
			// fine here 
		}
	}

	@Test
	public void testGetVariantAttributesForTypeServiceReturningComposedType()
	{

		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(new ComposedTypeModel());
		try
		{
			variantsService.getVariantAttributes("someDummyType");
			fail("Null argument should throw an exception ");
		}
		catch (final IllegalArgumentException ile)
		{
			// fine here 
		}
	}

	@Test
	public void testGetVariantAttributesWithNullVariantAttributtes()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);

		final VariantTypeModel variant = new VariantTypeModel();

		doReturn(null).when(mockVariantService).getVariantAttributesForVariantType(variant);
		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(variant);

		assertTrue(mockVariantService.getVariantAttributes("someDummyType").size() == 0);
	}

	@Test
	public void testGetVariantAttributesWithEmptyVariantAttributtes()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);
		final VariantTypeModel variant = new VariantTypeModel();

		doReturn(Collections.EMPTY_LIST).when(mockVariantService).getVariantAttributesForVariantType(variant);
		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(variant);

		assertTrue(mockVariantService.getVariantAttributes("someDummyType").size() == 0);
	}

	@Test
	public void testGetVariantAttributesWithNonUniqueVariantAttributtes()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);
		final VariantTypeModel variant = new VariantTypeModel();

		final VariantAttributeDescriptorModel vadModel1 = new VariantAttributeDescriptorModel();
		vadModel1.setQualifier("qualifierOne");

		final VariantAttributeDescriptorModel vadModel2 = new VariantAttributeDescriptorModel();
		vadModel2.setQualifier("qualifierTwo");

		final VariantAttributeDescriptorModel vadModel3 = new VariantAttributeDescriptorModel();
		vadModel3.setQualifier("qualifierOne");

		doReturn(Arrays.asList(vadModel1, vadModel2, vadModel3)).when(mockVariantService).getVariantAttributesForVariantType(
				variant);
		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(variant);

		final Set<String> result = mockVariantService.getVariantAttributes("someDummyType");

		assertTrue(result.size() == 2);
		assertTrue(result.contains("qualifierOne"));
		assertTrue(result.contains("qualifierTwo"));
	}

	@Test
	public void testGetVariantAttributesWithUniqueVariantAttributtes()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);
		final VariantTypeModel variant = new VariantTypeModel();

		final VariantAttributeDescriptorModel vadModel1 = new VariantAttributeDescriptorModel();
		vadModel1.setQualifier("qualifierOne");

		final VariantAttributeDescriptorModel vadModel2 = new VariantAttributeDescriptorModel();
		vadModel2.setQualifier("qualifierTwo");

		final VariantAttributeDescriptorModel vadModel3 = new VariantAttributeDescriptorModel();
		vadModel3.setQualifier("qualifierThree");

		doReturn(Arrays.asList(vadModel1, vadModel2, vadModel3)).when(mockVariantService).getVariantAttributesForVariantType(
				variant);
		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(variant);

		final Set<String> result = mockVariantService.getVariantAttributes("someDummyType");
		assertTrue(result.size() == 3);
		assertTrue(result.contains("qualifierOne"));
		assertTrue(result.contains("qualifierTwo"));
		assertTrue(result.contains("qualifierThree"));
	}


	@Test
	public void testGetVariantTypeModeWithOneResult()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);

		final VariantTypeModel variant = new VariantTypeModel();

		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(variant);

		final VariantTypeModel result = mockVariantService.getVariantTypeForCode("someDummyType");
		assertSame(result, variant);

	}

	@Test
	public void testGetVariantTypeModeWithNoneResult()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);


		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(null);

		try
		{
			mockVariantService.getVariantTypeForCode("someDummyType");
			Assert.fail("This should not succeed for null result ");
		}
		catch (final UnknownIdentifierException uke)
		{
			// ok here 
		}

	}

	@Test
	public void testGetVariantTypeModeWithNoCompatibleREsult()
	{
		final VariantsService mockVariantService = Mockito.spy(variantsService);

		final ComposedTypeModel composedType = new ComposedTypeModel();

		when(mockTypeService.getComposedTypeForCode("someDummyType")).thenReturn(composedType);

		try
		{
			mockVariantService.getVariantTypeForCode("someDummyType");
			Assert.fail("This should not succeed for composedType ");
		}
		catch (final UnknownIdentifierException uke)
		{
			// ok here 
		}


	}

}
