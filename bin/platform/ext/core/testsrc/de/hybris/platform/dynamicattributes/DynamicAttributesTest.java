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
package de.hybris.platform.dynamicattributes;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.core.model.test.TestItemType3Model;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DynamicAttributesTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	private TestItemType2Model model;
	private TestItemType3Model model2;

	@Before
	public void setUp() throws Exception
	{
		model = modelService.create(TestItemType2Model.class);
		model2 = modelService.create(TestItemType3Model.class);
		getOrCreateLanguage("en");
		getOrCreateLanguage("de");
	}

	@Test
	public void shouldHaveDynamicAttributesStringSampleBeanAsAttributeHandler()
	{
		//given
		final ComposedTypeModel type = typeService.getComposedTypeForClass(TestItemType2Model.class);

		//when
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(type, TestItemType2Model.FOOBAR);

		//then
		assertThat(attributeDescriptor).isNotNull();
		assertThat(attributeDescriptor.getAttributeHandler()).isEqualTo("dynamicAttributesStringSampleBean");
	}

	@Test
	public void shouldReturnDynamicAttributeInListOfAllAttrubutesForTypeUsingTypeService()
	{
		// given
		final ComposedTypeModel type = typeService.getComposedTypeForClass(TestItemType2Model.class);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(type, TestItemType2Model.FOOBAR);

		// when
		final Set<AttributeDescriptorModel> descriptors = typeService.getAttributeDescriptorsForType(type);

		// then
		assertThat(descriptors).contains(attributeDescriptor);
	}

	@Test
	public void shouldReturnDynamicAttributeInListOfAllAttrubutesForTypeUsingItem() throws JaloInvalidParameterException,
			JaloSecurityException
	{
		// given
		final ComposedTypeModel type = typeService.getComposedTypeForClass(TestItemType2Model.class);
		final ComposedType typeItem = modelService.toPersistenceLayer(type);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(type, TestItemType2Model.FOOBAR);
		final AttributeDescriptor attributeDescriptorItem = modelService.toPersistenceLayer(attributeDescriptor);

		// when
		final Set<AttributeDescriptor> descriptors = typeItem.getAttributeDescriptors();

		// then
		assertThat(descriptors).contains(attributeDescriptorItem);
	}

	@Test
	public void shouldHaveDynamicAttributesEnumSampleBeanAsAttributeHandler()
	{
		//given
		final ComposedTypeModel type = typeService.getComposedTypeForClass(TestItemType2Model.class);

		//when
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(type, TestItemType2Model.GENDER);

		//then
		assertThat(attributeDescriptor.getAttributeHandler()).isEqualTo("dynamicAttributesEnumSampleBean");
	}

	@Test
	public void shouldHaveDynamicAttributesIntSampleBeanAsAttributeHandler()
	{
		//given
		final ComposedTypeModel type = typeService.getComposedTypeForClass(TestItemType2Model.class);

		//when
		final AttributeDescriptorModel attributeDescriptorString = typeService.getAttributeDescriptor(type,
				TestItemType2Model.INTBAR);

		//then
		assertThat(attributeDescriptorString.getAttributeHandler()).isEqualTo("dynamicAttributesIntSampleBean");
	}

	@Test
	public void shouldReturnConcatenatedValuesFromFooAndBarOnGetFooBar()
	{
		//given
		model.setFoo("Foo");
		model.setBar("Bar");

		//when
		final String dynamicValue = model.getFooBar();

		//then
		assertThat(dynamicValue).isEqualTo("Foo,Bar");
	}


	@Test
	public void shouldReturnConcatenatedValuesFromFooAndBarUsingGetAttributeValueMethod()
	{
		//given
		model.setFoo("Foo");
		model.setBar("Bar");

		//when
		final String dynamicValue = modelService.getAttributeValue(model, TestItemType2Model.FOOBAR);

		//then
		assertThat(dynamicValue).isEqualTo("Foo,Bar");
	}

	@Test
	public void shouldReturnSomeDummyTextForFooBarOnSubtype()
	{
		//when
		final String dynamicValue = model2.getFooBar();

		//then
		assertThat(dynamicValue).isEqualTo("Some dummy output");
	}

	@Test
	public void shouldReturnSumOfIntegerValueAnd2()
	{
		//given
		model.setInteger(Integer.valueOf(6));

		//when
		final int dynamicValue = model.getIntBar();

		//then
		assertThat(dynamicValue).isEqualTo(8);
	}

	@Test
	public void shouldReturnSumOfIntegerValueAnd2ForSubtype()
	{
		//given
		model2.setInteger(Integer.valueOf(6));

		//when
		final int dynamicValue = model2.getIntBar();

		//then
		assertThat(dynamicValue).isEqualTo(8);
	}

	@Test
	public void shouldSetIntegerValueOfIntegerMinus2()
	{
		//given
		final int value = 10;

		//when
		model.setIntBar(value);

		//then
		assertThat(model.getInteger()).isEqualTo(8);
	}

	@Test
	public void shouldReturnMaleGender()
	{
		//when
		final Gender dynamicValue = model.getGender();

		//then
		assertThat(dynamicValue).isEqualTo(Gender.MALE);
	}

	@Test
	public void shouldReturnMaleGenderFromSupertype()
	{
		//when
		final Gender dynamicValue = model2.getGender();

		//then
		assertThat(dynamicValue).isEqualTo(Gender.MALE);
	}

	@Test
	public void shouldReturnMaleGenderUsingGetAttributeValueMethod()
	{
		//when
		final Gender dynamicValue = modelService.getAttributeValue(model, TestItemType2Model.GENDER);

		//then
		assertThat(dynamicValue).isEqualTo(Gender.MALE);
	}

	@Test
	public void shouldSplitStringValueByCommaAndSetFirstElementToFooAndSecondToBar()
	{
		// given
		final String value = "Baz,Boom";

		// when
		model.setFooBar(value);

		// then
		assertThat(model.getFoo()).isEqualTo("Baz");
		assertThat(model.getBar()).isEqualTo("Boom");
	}

	@Test
	public void shouldChangeTheLocalizedTestPropertyInGermanAndEnglish()
	{
		// given
		final String valueGerman = "ja,toll";
		final String valueEnglish = "yes,great";

		// when
		model.setLocalizedFooBar(valueEnglish, Locale.ENGLISH);
		model.setLocalizedFooBar(valueGerman, Locale.GERMAN);

		// then
		assertThat(model.getTestProperty2(Locale.GERMAN)).isEqualTo(valueGerman);
		assertThat(model.getTestProperty2(Locale.ENGLISH)).isEqualTo(valueEnglish);
		assertThat(model.getLocalizedFooBar(Locale.GERMAN)).isEqualTo(valueGerman);
		assertThat(model.getLocalizedFooBar(Locale.ENGLISH)).isEqualTo(valueEnglish);
	}

	@Test
	public void shouldChangeTheLocalizedTestPropertyWithDefaultLocale()
	{
		// given
		final String value = "default locale value";

		// when
		model.setLocalizedFooBar(value);

		// then
		assertThat(model.getTestProperty2()).isEqualTo(value);
		assertThat(model.getLocalizedFooBar()).isEqualTo(value);
	}

}
