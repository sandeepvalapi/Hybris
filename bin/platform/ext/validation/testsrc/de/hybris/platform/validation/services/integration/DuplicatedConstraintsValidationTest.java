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
package de.hybris.platform.validation.services.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidationConfigurationException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.interceptors.AbstractConstraintUniqueValidator;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.DynamicConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.AssertFalseConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertTrueConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMaxConstraintModel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests validator {@link AbstractConstraintUniqueValidator}.
 */
@IntegrationTest
public class DuplicatedConstraintsValidationTest extends AbstractConstraintTest
{
	private ConstraintGroupModel sampleGroup = null;
	private ConstraintGroupModel sampleOtherGroup = null;

	@Before
	public void prepareGroup()
	{
		sampleGroup = modelService.create(ConstraintGroupModel.class);
		sampleGroup.setInterfaceName("de.hybris.platform.validation.services.integration.DuplicatedConstraintsValidationTest.SampleGroup");
		sampleGroup.setId("sampleGroup");

		sampleOtherGroup = modelService.create(ConstraintGroupModel.class);
		sampleOtherGroup.setInterfaceName("de.hybris.platform.validation.services.integration.DuplicatedConstraintsValidationTest.List");
		sampleOtherGroup.setId("sampleOtherGroup");

		modelService.saveAll(sampleGroup, sampleOtherGroup);
	}

	/**
	 * can not two active , for default group
	 */
	@Test
	public void testDuplicatedAssertFalseConstraintsForModel()
	{

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertFalseConstraintModel asf1 = modelService.create(AssertFalseConstraintModel.class);
		asf1.setDescriptor(descrModel);
		asf1.setId("oneDuplicated");

		final AssertFalseConstraintModel asf2 = modelService.create(AssertFalseConstraintModel.class);
		asf2.setDescriptor(descrModel);
		asf2.setId("oneDuplicated");

		//where as save all for all constraint won't work 
		modelService.save(asf1);
		try
		{
			modelService.save(asf2);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, UniqueAttributesInterceptor.class);
		}

	}

	/**
	 * can not two active , even for other groups
	 */
	@Test
	public void testDuplicatedAssertFalseConstraintsForModelWithMixedGroups()
	{

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertFalseConstraintModel asf1 = modelService.create(AssertFalseConstraintModel.class);
		asf1.setDescriptor(descrModel);
		asf1.setId("oneDuplicated");
		asf1.setConstraintGroups(new HashSet<>(Arrays.asList(sampleGroup, sampleOtherGroup)));

		final AssertFalseConstraintModel asf2 = modelService.create(AssertFalseConstraintModel.class);
		asf2.setDescriptor(descrModel);
		asf2.setId("twoDuplicated");

		//where as save all for all constraint won't work 
		modelService.save(asf1);

		try
		{
			modelService.save(asf2);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AbstractConstraintUniqueValidator.class);
		}

	}

	/**
	 * can create second the constraint , first one is not active
	 */
	@Test
	public void testDuplicatedAssertFalseConstraintsForModelWithNoActive()
	{

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertFalseConstraintModel asf1 = modelService.create(AssertFalseConstraintModel.class);
		asf1.setDescriptor(descrModel);
		asf1.setId("oneDuplicated");
		asf1.setConstraintGroups(Collections.singleton(sampleGroup));
		asf1.setActive(false);

		final AssertFalseConstraintModel asf2 = modelService.create(AssertFalseConstraintModel.class);
		asf2.setDescriptor(descrModel);
		asf2.setId("twoDuplicated");
		asf2.setConstraintGroups(Collections.singleton(sampleGroup));

		modelService.save(asf1);
		modelService.save(asf2);
	}

	/**
	 * can create second the constraint it is inactive
	 */
	@Test
	public void testDuplicatedAssertFalseConstraintsForModelWithNoActive2()
	{
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertFalseConstraintModel asf1 = modelService.create(AssertFalseConstraintModel.class);
		asf1.setDescriptor(descrModel);
		asf1.setId("oneDuplicated");
		asf1.setConstraintGroups(Collections.singleton(sampleGroup));

		final AssertFalseConstraintModel asf2 = modelService.create(AssertFalseConstraintModel.class);
		asf2.setDescriptor(descrModel);
		asf2.setId("twoDuplicated");
		asf2.setConstraintGroups(Collections.singleton(sampleGroup));
		asf2.setActive(false);

		modelService.save(asf1);
		modelService.save(asf2);
	}

	@Test
	public void testDuplicatedAssertTrueConstraintsForModel()
	{
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertTrueConstraintModel ast1 = modelService.create(AssertTrueConstraintModel.class);
		ast1.setDescriptor(descrModel);
		ast1.setId("oneDuplicated");
		ast1.setActive(true);//default

		final AssertTrueConstraintModel ast2 = modelService.create(AssertTrueConstraintModel.class);
		ast2.setDescriptor(descrModel);
		ast2.setId("twoDuplicated");
		ast1.setActive(true);//default

		modelService.save(ast1);

		try
		{
			modelService.save(ast2);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AbstractConstraintUniqueValidator.class);
		}
	}

	@Test
	public void testDuplicatedAssertTrueConstraintsForModelWithSampleGroup()
	{

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(UserModel.class), UserModel.LOGINDISABLED);

		final AssertTrueConstraintModel ast1 = modelService.create(AssertTrueConstraintModel.class);
		ast1.setDescriptor(descrModel);
		ast1.setId("oneDuplicated");
		ast1.setConstraintGroups(Collections.singleton(sampleGroup));

		final AssertTrueConstraintModel ast2 = modelService.create(AssertTrueConstraintModel.class);
		ast2.setDescriptor(descrModel);
		ast2.setId("twoDuplicated");
		ast2.setActive(false);

		modelService.save(ast1);
		modelService.save(ast2);
	}

	@Test
	public void testDuplicatedDecimalMaxConstraintsForModelWithSampleGroup()
	{

		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);

		final DecimalMaxConstraintModel con1 = modelService.create(DecimalMaxConstraintModel.class);
		con1.setDescriptor(descrModel);
		con1.setId("oneDuplicated");
		con1.setConstraintGroups(Collections.singleton(sampleGroup));
		con1.setValue(BigDecimal.valueOf(1));

		final DecimalMaxConstraintModel con2 = modelService.create(DecimalMaxConstraintModel.class);
		con2.setDescriptor(descrModel);
		con2.setId("twoDuplicated");
		con2.setActive(false);
		con2.setValue(BigDecimal.valueOf(1));

		modelService.save(con1);
		modelService.save(con2);
	}


	/**
	 * can not two active , for default group
	 */
	@Test
	public void testDuplicatedDynamicConstraintsForModel()
	{
		final DynamicConstraintModel asf1 = modelService.create(DynamicConstraintModel.class);
		asf1.setType(typeService.getComposedType(ProductModel.class));
		asf1.setId("oneDuplicated");
		asf1.setExpression("blah , blah ...");
		asf1.setLanguage(ValidatorLanguage.BEANSHELL);

		final DynamicConstraintModel asf2 = modelService.create(DynamicConstraintModel.class);
		asf2.setType(typeService.getComposedType(ProductModel.class));
		asf2.setId("twoDuplicated");
		asf2.setExpression("blah , blah ...");
		asf2.setLanguage(ValidatorLanguage.BEANSHELL);

		//where as save all for all constraint won't work 
		modelService.save(asf1);

		try
		{
			modelService.save(asf2);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AbstractConstraintUniqueValidator.class);
		}
	}

	/**
	 * can not two active , even for other groups
	 */
	@Test
	public void testDuplicatedDynamicConstraintsForModelWithMixedGroups()
	{

		final DynamicConstraintModel asf1 = modelService.create(DynamicConstraintModel.class);
		asf1.setType(typeService.getComposedType(ProductModel.class));
		asf1.setId("oneDuplicated");
		asf1.setExpression("blah , blah ...");
		asf1.setLanguage(ValidatorLanguage.BEANSHELL);
		asf1.setConstraintGroups(new HashSet<>(Arrays.asList(sampleGroup, sampleOtherGroup)));

		final DynamicConstraintModel asf2 = modelService.create(DynamicConstraintModel.class);
		asf2.setType(typeService.getComposedType(ProductModel.class));
		asf2.setId("twoDuplicated");
		asf2.setExpression("blah , blah ...");
		asf2.setLanguage(ValidatorLanguage.BEANSHELL);

		//where as save all for all constraint won't work 
		modelService.save(asf1);
		try
		{
			modelService.save(asf2);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AbstractConstraintUniqueValidator.class);
		}
	}

	/**
	 * can create second the constraint , first one is not active
	 */
	@Test
	public void testDuplicatedDynamicConstraintsForModelWithNoActive()
	{

		final DynamicConstraintModel asf1 = modelService.create(DynamicConstraintModel.class);
		asf1.setType(typeService.getComposedType(ProductModel.class));
		asf1.setId("oneDuplicated");
		asf1.setExpression("blah , blah ...");
		asf1.setLanguage(ValidatorLanguage.BEANSHELL);
		asf1.setConstraintGroups(new HashSet<>(Arrays.asList(sampleGroup, sampleOtherGroup)));
		asf1.setActive(false);

		final DynamicConstraintModel asf2 = modelService.create(DynamicConstraintModel.class);
		asf2.setType(typeService.getComposedType(ProductModel.class));
		asf2.setId("twoDuplicated");
		asf2.setExpression("blah , blah ...");
		asf2.setLanguage(ValidatorLanguage.BEANSHELL);

		//where as save all for all constraint won't work 
		modelService.save(asf1);
		modelService.save(asf2);
	}

	/**
	 * can create second the constraint is inactive
	 */
	@Test
	public void testDuplicatedDynamicConstraintsForModelWithNoActive2()
	{
		final DynamicConstraintModel asf1 = modelService.create(DynamicConstraintModel.class);
		asf1.setType(typeService.getComposedType(ProductModel.class));
		asf1.setId("oneDuplicated");
		asf1.setExpression("blah , blah ...");
		asf1.setLanguage(ValidatorLanguage.BEANSHELL);
		asf1.setConstraintGroups(new HashSet<>(Arrays.asList(sampleGroup, sampleOtherGroup)));
		asf1.setActive(false);

		final DynamicConstraintModel asf2 = modelService.create(DynamicConstraintModel.class);
		asf2.setType(typeService.getComposedType(ProductModel.class));
		asf2.setId("twoDuplicated");
		asf2.setExpression("blah , blah ...");
		asf2.setLanguage(ValidatorLanguage.BEANSHELL);
		asf2.setActive(false);

		//where as save all for all constraint won't work 
		modelService.save(asf1);
		modelService.save(asf2);
	}

	private interface SampleGroup
	{
		//for testing purposes
	}

	private interface List
	{
		//for testing purposes
	}
}
