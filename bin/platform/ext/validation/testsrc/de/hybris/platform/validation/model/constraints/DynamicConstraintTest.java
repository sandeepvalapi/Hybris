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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import de.hybris.platform.validation.annotations.Dynamic;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.interceptors.TypeConstraintValidator;
import de.hybris.platform.validation.interceptors.ValidationInterceptor;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Test for presenting set of required attributes for storing {@link DynamicConstraintModel}.
 */
@IntegrationTest
public class DynamicConstraintTest extends AbstractConstraintTest
{
	/**
	 * simple example of valid bsh script assigned for a pojo class
	 * 
	 * <pre>
	 * out.print("some data");
	 */
	@Test
	public void testCreateDynamicPojoConstraint()
	{

		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		constraint.setTarget(PojoOne.class);
		constraint.setExpression("out.print(\"some data\");");

		modelService.save(constraint);
		Assert.assertEquals(getDefaultMessage(Constraint.BEANSHELL.msgKey), constraint.getDefaultMessage());
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
	}

	/**
	 * simple example of valid bsh script assigned for a {@link ProductModel} class
	 * 
	 * <pre>
	 * out.print("some data");
	 */
	@Test
	public void testCreateDynamicModelConstraint()
	{

		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel productModel = typeService.getComposedType(ProductModel.class);
		constraint.setType(productModel);
		constraint.setExpression("out.print(\"some data\");");

		modelService.save(constraint);
		Assert.assertEquals(getDefaultMessage(Constraint.BEANSHELL.msgKey), constraint.getDefaultMessage());
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
	}


	/**
	 * not allowed creation of the constraint for a model via {@link AbstractConstraintModel#TARGET}
	 */
	@Test
	public void testCreateDynamicWithTargetConstraint()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		constraint.setTarget(ProductModel.class);
		constraint.setExpression("out.print(\"some data\");");
		try
		{
			modelService.save(constraint);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}

	/**
	 * not allowed creation of the constraint for a model without {@link AbstractConstraintModel#TARGET} or
	 * {@link AbstractConstraintModel#TYPE}
	 */
	@Test
	public void testCreateDynamicNoTargetConstraint()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		constraint.setExpression("out.print(\"some data\");");

		try
		{
			modelService.save(constraint);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, TypeConstraintValidator.class);
		}
	}


	/**
	 * not allowed creation of the dynamic constraint without {@link DynamicConstraintModel#EXPRESSION}
	 */
	@Test
	public void testCreateDynamicNoExpressionConstraint()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		try
		{
			modelService.save(constraint);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, MandatoryAttributesValidator.class);
		}
	}

	/**
	 * inappropriate BSH script syntax
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCheckInvalidBSH() throws Exception
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel titleModel = typeService.getComposedType(TitleModel.class);
		constraint.setType(titleModel);
		constraint.setExpression("return getCode().equals11111(getName());");

		modelService.save(constraint);

		validationService.reloadValidationEngine();

		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);

		final TitleModel title = modelService.create(TitleModel.class);

		title.setCode("someCode");
		try
		{
			modelService.save(title);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, ValidationViolationException.class, ValidationInterceptor.class);
		}
	}

	/**
	 * runtime exception are not affecting validation flag
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCheckBSHRuntimeException() throws Exception
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel titleModel = typeService.getComposedType(TitleModel.class);
		constraint.setType(titleModel);
		constraint.setExpression("return getCode().equals(getName().length());");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("someCode");
		modelService.save(title);
	}

	/**
	 * Sample POJO class.
	 */
	@SuppressWarnings("unused")
	private class PojoOne
	{
		private String oneAttribute;

		public void setOneAttribute(final String oneAttribute)
		{
			this.oneAttribute = oneAttribute;
		}

		public String getOneAttribute()
		{
			return oneAttribute;
		}
	}
}
