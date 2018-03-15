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
package de.hybris.platform.enumeration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests demonstrating usage of the enumeration service.
 */
@IntegrationTest
@DemoTest
public class EnumerationServiceDemoTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private EnumerationService enumerationService;

	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18nService;

	private EnumerationValueModel testEVModel;

	/**
	 * prepares some testdata, before execution of every test:<br/>
	 * - the test EnumerationValueModel will be prepared (not persisted)
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");
		//set the current locale to english
		i18nService.setCurrentLocale(Locale.ENGLISH);
		testEVModel = modelService.create(OrderStatus.CREATED.getType());
		assertNotNull(testEVModel);
		testEVModel.setCode("TestValueCode");
	}

	/**
	 * Demonstrates how to use the enumerationService to get Enum-Values by code. It shows also how to add a new
	 * Enum-Value.
	 * 
	 * Test scenario:<br/>
	 * - Save the new EnumValue (model prepared in setup) and check if correctly saved.<br/>
	 * - Get the Enumeration Values for given code of type (e.g. OrderStatus).<br/>
	 * - Check the result list - the type of every item should match the given one.<br/>
	 * - Check the result - the list should contain the item with the code prepared in setUp<br/>
	 */
	@Test
	public void testGettingEnumerationValuesByCode()
	{
		//save the preapred EnumValue
		modelService.save(testEVModel);
		modelService.refresh(testEVModel);

		//check if correctly saved
		final HybrisEnumValue persistedModel = modelService.<HybrisEnumValue> get(testEVModel.getPk());
		assertNotNull(persistedModel);
		assertEquals("TestValueCode", persistedModel.getCode());
		assertEquals(OrderStatus.CREATED.getType(), persistedModel.getType());

		//get the enumeration values for the given code of type		
		final List<HybrisEnumValue> matchingEnumerations = enumerationService.getEnumerationValues(OrderStatus.CREATED.getType());
		assertNotNull(matchingEnumerations);
		assertFalse(matchingEnumerations.isEmpty());

		//	check the results - the same type code for every item is expected
		boolean expectedValueCodeFound = false;
		for (final HybrisEnumValue enumItem : matchingEnumerations)
		{
			assertEquals(OrderStatus.CREATED.getType(), enumItem.getType());
			if (persistedModel.getCode().equals(enumItem.getCode()))
			{
				expectedValueCodeFound = true;
			}

		}
		// check the prepared value code - it should be found in the list
		assertTrue("expected to find " + persistedModel.getCode() + " in " + matchingEnumerations, expectedValueCodeFound);


	}

	/**
	 * Demonstrates how to use the enumerationService to get Enum-Values by class. It shows also how to add a new
	 * Enum-Value.
	 * 
	 * Test scenario:<br/>
	 * - Save the new EnumValue (model prepared in setup) and check if correctly saved.<br/>
	 * - Get the Enumeration Values for given enum class (e.g. OrderStatus).<br/>
	 * - Check the result list - the type of every item should match the given one.<br/>
	 * - Check the result - the list should contain the item with the code prepared in setUp<br/>
	 */
	@Test
	public void testGettingEnumerationValuesByClass()
	{
		//save the preapred EnumValue
		modelService.save(testEVModel);
		modelService.refresh(testEVModel);

		//check if correctly saved
		final HybrisEnumValue persistedModel = modelService.<HybrisEnumValue> get(testEVModel.getPk());
		assertNotNull(persistedModel);
		assertEquals("TestValueCode", persistedModel.getCode());
		assertEquals(OrderStatus.CREATED.getType(), persistedModel.getType());

		//get the enumeration values for the given code of type
		final List<OrderStatus> matchingEnumerations = enumerationService.getEnumerationValues(OrderStatus.class);
		assertNotNull(matchingEnumerations);
		assertFalse(matchingEnumerations.isEmpty());

		//	check the results - the same type code for every item is expected
		boolean expectedValueCodeFound = false;
		for (final HybrisEnumValue enumItem : matchingEnumerations)
		{
			assertEquals(OrderStatus.CREATED.getType(), enumItem.getType());
			if (persistedModel.getCode().equals(enumItem.getCode()))
			{
				expectedValueCodeFound = true;
			}

		}
		// check the prepared value code - it should be found in the list
		assertTrue("expected to find " + persistedModel.getCode() + " in " + matchingEnumerations, expectedValueCodeFound);

	}

	/**
	 * Demonstrates how to use the enumerationService to get Enum-Values by code and value. It shows also how to add a
	 * new Enum-Value.
	 * 
	 * Test scenario:<br/>
	 * - get the Enumeration Values for given code of type and value (e.g. OrderStatus, code from prepared model).<br/>
	 * - check the result - unknown identifier Exception should be catched.<br/>
	 * - Save the new EnumValue (model prepared in setup) and repeat the previous steps again.<br/>
	 * - Check the result - this time the result should be the item with the code prepared in setUP<br/>
	 */
	@Test
	public void testGettingEnumerationValueByCodeAndValue()
	{
		HybrisEnumValue matchingEnumeration = null;
		//get the enumeration values for the given type and value code - exception is expected because of not matching code. 
		try
		{
			matchingEnumeration = enumerationService.getEnumerationValue(OrderStatus.CREATED.getType(), testEVModel.getCode());
			fail("UnknownIdentifierException expected , but not thrown");
		}
		catch (final UnknownIdentifierException e)
		{
			assertNull(matchingEnumeration);
		}

		//save the preapred EnumValue
		modelService.save(testEVModel);
		assertEquals("TestValueCode", testEVModel.getCode());

		//get the enumeration values again - this time a matching enumeration should be found.
		matchingEnumeration = enumerationService.getEnumerationValue(OrderStatus.CREATED.getType(), testEVModel.getCode());
		assertNotNull(matchingEnumeration);
		assertEquals(matchingEnumeration.getType(), OrderStatus.CREATED.getType());
		assertEquals(matchingEnumeration.getCode(), testEVModel.getCode());

	}

	/**
	 * Demonstrates how to use the enumerationService to get Enum-Values by class and value. It shows also how to add a
	 * new Enum-Value.
	 * 
	 * Test scenario:<br/>
	 * - get the Enumeration Values for given class and value (e.g. OrderStatus, code from prepared model).<br/>
	 * - check the result - unknown identifier Exception should be catched.<br/>
	 * - Save the new EnumValue (model prepared in setup) and repeat the previous steps again.<br/>
	 * - Check the result - this time the result should be the item with the code prepared in setUP<br/>
	 */
	@Test
	public void testGettingEnumerationValueByClassAndValue()
	{
		OrderStatus matchingEnumeration = null;
		//get the enumeration values for the given type and value code - exception is expected because of not matching code.
		try
		{
			matchingEnumeration = enumerationService.getEnumerationValue(OrderStatus.class, testEVModel.getCode());
			fail("UnknownIdentifierException expected , but not thrown");
		}
		catch (final UnknownIdentifierException e)
		{
			assertNull(matchingEnumeration);
		}

		//save the prepared EnumValue
		modelService.save(testEVModel);
		assertEquals("TestValueCode", testEVModel.getCode());

		//get the enumeration values again - this time a matching enumeration should be found.
		matchingEnumeration = enumerationService.getEnumerationValue(OrderStatus.class, testEVModel.getCode());
		assertNotNull(matchingEnumeration);
		assertEquals(matchingEnumeration.getType(), OrderStatus.CREATED.getType());
		assertEquals(matchingEnumeration.getCode(), testEVModel.getCode());

	}

	/**
	 * Demonstrates how to use the enumerationService to get and set the Enumeration Name. It shows also how to add a new
	 * Enum-Value.
	 * 
	 * Test scenario:<br/>
	 * - Save the new EnumValue (model prepared in setup) and repeat the previous steps again.<br/>
	 * - fetch the saved enumValue using enumerationService method.<br/>
	 * - set enumerationName for the enum value.<br/>
	 * - get the enumerationName and check if correclty set.<br/>
	 * - repeat 2 previous steps with changed enumeration name.<br/>
	 * - try it again with locale parameter.
	 */
	@Test
	public void testChangingEnumerationName()
	{
		//save the prepared EnumValue
		modelService.save(testEVModel);
		assertEquals("TestValueCode", testEVModel.getCode());

		//find the enumeration for given type and code
		final HybrisEnumValue matchingEnumeration = enumerationService.getEnumerationValue(OrderStatus.CREATED.getType(),
				testEVModel.getCode());

		//setEnumerationName for the given Enum
		enumerationService.setEnumerationName(matchingEnumeration, "testName");

		//get the name and check if correct
		String enumName = enumerationService.getEnumerationName(matchingEnumeration);
		assertEquals("testName", enumName);

		//setEnumerationName for the given Enum again
		enumerationService.setEnumerationName(matchingEnumeration, "testNameChanged");

		//get the name and check if correct
		enumName = enumerationService.getEnumerationName(matchingEnumeration);
		assertEquals("testNameChanged", enumName);

		//try it with locale - parameter		
		//set the current locale to german
		i18nService.setCurrentLocale(Locale.GERMAN);

		//using the English language the previously saved value will be returned (english was the current locale).
		enumName = enumerationService.getEnumerationName(matchingEnumeration, Locale.ENGLISH);
		assertEquals("testNameChanged", enumName);

		//setEnumerationName for the given Enum again with german language and check it
		enumerationService.setEnumerationName(matchingEnumeration, "neuer_name", Locale.GERMAN);

		enumName = enumerationService.getEnumerationName(matchingEnumeration, Locale.GERMAN);
		assertEquals("neuer_name", enumName);

	}

}
