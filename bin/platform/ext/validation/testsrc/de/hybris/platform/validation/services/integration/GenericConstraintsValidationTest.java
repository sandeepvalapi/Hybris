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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.AssertFalseConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertTrueConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMaxConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMinConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


/**
 * Covers more advanced cases of constraint cases using service layer validation.
 */
@IntegrationTest
public class GenericConstraintsValidationTest extends AbstractConstraintTest
{
	private static final String MESSAGE_SAVE_WITHOUT_NAME = "neither without name";
	private static final String MESSAGE_SAVE_WITHOUT_PASSWORDQUESTION = "hahaha won't save without password question";

	/**
	 * null reference constraint test on different type level attributes
	 * 
	 * <pre>
	 * Principal.NAME
	 * 		|
	 * User.PASSWORDPASSWORDQUESTION
	 * 		|
	 * Customer
	 */
	@Test
	public void testInhereitanceFieldForModel()
	{
		createConstraint(MESSAGE_SAVE_WITHOUT_PASSWORDQUESTION, UserModel.PASSWORDQUESTION, UserModel.class);
		createConstraint(MESSAGE_SAVE_WITHOUT_NAME, PrincipalModel.NAME, PrincipalModel.class);

		validationService.reloadValidationEngine(); //reload

		final CustomerModel custModel = modelService.create(CustomerModel.class);
		custModel.setUid("John Kovalsky");

		try
		{
			modelService.save(custModel);
			Assert.fail("Should not be able to save user without " + UserModel.PASSWORDQUESTION + "," + PrincipalModel.NAME);
		}
		catch (final ModelSavingException e)
		{
			final Map<String, String> expected = new HashMap<String, String>(2);
			expected.put(UserModel.PASSWORDQUESTION, MESSAGE_SAVE_WITHOUT_PASSWORDQUESTION);
			expected.put(PrincipalModel.NAME, MESSAGE_SAVE_WITHOUT_NAME);
			assertModelSavingExceptionWithEvaluatedMessage(e, expected);
		}
		custModel.setName("Damn important value");
		try
		{
			modelService.save(custModel);
			Assert.fail("Should not be able to save user without " + UserModel.PASSWORDQUESTION);
		}
		catch (final ModelSavingException e)
		{
			assertModelSavingExceptionWithEvaluatedMessage(e, MESSAGE_SAVE_WITHOUT_PASSWORDQUESTION, UserModel.PASSWORDQUESTION);
		}
		custModel.setPasswordQuestion("Damn important value too");
		modelService.save(custModel);
	}

	private void createConstraint(final String message, final String qualifier, final Class clazz)
	{
		final NotNullConstraintModel constraint = modelService.create(NotNullConstraintModel.class);
		constraint.setId(message);
		constraint.setMessage(message);
		final AttributeDescriptorModel descrModel1 = typeService.getAttributeDescriptor(typeService.getComposedType(clazz),
				qualifier);
		constraint.setDescriptor(descrModel1);
		constraint.setTarget(clazz);
		modelService.save(constraint);
	}

	/**
	 * simple test - using own model attribute Testing NotNull constraint
	 */
	@Test
	public void testNotNullConstraintWithOwnModelAttributes()
	{
		//create constraint
		final NotNullConstraintModel notNullconstraint = modelService.create(NotNullConstraintModel.class);
		notNullconstraint.setId("notNullcustomerID");
		Assert.assertEquals(javax.validation.constraints.NotNull.class, notNullconstraint.getAnnotation());
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(CustomerModel.class), CustomerModel.CUSTOMERID);
		notNullconstraint.setDescriptor(descrModel);
		modelService.save(notNullconstraint);

		validationService.reloadValidationEngine();

		//now the test: create model
		final CustomerModel customer = modelService.create(CustomerModel.class);
		customer.setUid("test");

		try
		{
			modelService.save(customer); //must fail
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.NOT_NULL.msgKey, "customerID");
		}
		customer.setCustomerID("moo");
		modelService.save(customer); //now it's ok
	}

	/**
	 * also simple test - but using attribute from supermodel Testing Null constraint
	 */
	@Test
	public void testNullConstraintWithSuperModelAttributes()
	{
		//create constraint
		final NullConstraintModel nullconstraint = modelService.create(NullConstraintModel.class);
		nullconstraint.setId("Nullconstraint");
		Assert.assertEquals(javax.validation.constraints.Null.class, nullconstraint.getAnnotation());
		final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
				typeService.getComposedType(CustomerModel.class), CustomerModel.DESCRIPTION);
		nullconstraint.setDescriptor(descrModel);
		modelService.save(nullconstraint);
		validationService.reloadValidationEngine();

		//now the test: create model
		final CustomerModel customer = modelService.create(CustomerModel.class);
		customer.setUid("test");
		customer.setDescription("moo"); //set to null, which is not allowed here

		try
		{
			modelService.save(customer);
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.NULL.msgKey, "description");
		}

		customer.setDescription(null);
		modelService.save(customer);
	}

	/**
	 * testing the AssertTrue setting the constraint to the C2LItemModel.active attribute but for the LanguageModel only
	 */
	@Test
	public void testAssertTrue()
	{
		final AttributeDescriptorModel activeAttrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);

		final AssertTrueConstraintModel assertTrue = modelService.create(AssertTrueConstraintModel.class);
		assertTrue.setId("assertTrue");
		assertTrue.setDescriptor(activeAttrDesc);
		modelService.save(assertTrue);
		validationService.reloadValidationEngine();

		final LanguageModel lang1 = modelService.create(LanguageModel.class);
		lang1.setIsocode("x1");
		lang1.setActive(Boolean.FALSE);

		try
		{
			modelService.save(lang1);
			fail("no validation exception occured! fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.ASSERT_TRUE.msgKey, "active");
		}

		lang1.setActive(Boolean.TRUE);
		modelService.save(lang1);
		assertTrue(lang1.getActive().booleanValue());
	}

	/**
	 * setting the NotNull constraint to C2LItemModel.avtive and the AssertFalse to LanguageModel.active (same attribute
	 * descriptor)
	 */
	@Test
	public void testAssertFalseAndNotNull()
	{
		//constraint for C2LItemModel.active must not be null
		final AttributeDescriptorModel c2litemmodelDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(C2LItemModel.class), C2LItemModel.ACTIVE);
		final NotNullConstraintModel notNull = modelService.create(NotNullConstraintModel.class);
		notNull.setId("notNull");
		notNull.setDescriptor(c2litemmodelDesc);

		//constraint for LanguageModel.active must be false
		final AttributeDescriptorModel activeLanguageModelDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(LanguageModel.class), LanguageModel.ACTIVE);
		final AssertFalseConstraintModel assertFalse = modelService.create(AssertFalseConstraintModel.class);
		assertFalse.setId("assertFalse");
		assertFalse.setDescriptor(activeLanguageModelDesc);
		modelService.saveAll(notNull, assertFalse);
		validationService.reloadValidationEngine();

		//now the test
		final LanguageModel lang1 = modelService.create(LanguageModel.class);
		lang1.setIsocode("x2");

		try
		{
			lang1.setActive(Boolean.TRUE); ///invalid because of the assertFalse constraint
			modelService.save(lang1);
			fail("no validation exception occured! fail!");
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.ASSERT_FALSE.msgKey, "active");
		}

		try
		{
			lang1.setActive(null); //valid - active wasn't set but model nullDecorator changes to default value
			modelService.save(lang1);

		}
		catch (final Exception e)
		{
			fail("no validation exception occured! fail!");
		}

		//this must work
		lang1.setActive(Boolean.FALSE);
		modelService.save(lang1);
	}

	/**
	 * testing the constraints DecimalMin, DecimalMax and Digits
	 */
	@Test
	public void testDecimalMinMaxAndDigitsConstraints()
	{
		//demo object - testing conversion
		final CurrencyModel ownCurr = modelService.create(CurrencyModel.class);
		ownCurr.setIsocode("XxX");
		ownCurr.setSymbol("X");
		ownCurr.setActive(Boolean.TRUE);
		ownCurr.setDigits(Integer.valueOf(5));
		ownCurr.setConversion(Double.valueOf(3.45671));
		modelService.save(ownCurr);

		final AttributeDescriptorModel conversionAttrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);

		final DecimalMinConstraintModel decimalMin = modelService.create(DecimalMinConstraintModel.class);
		decimalMin.setId("decimalMin");
		decimalMin.setValue(BigDecimal.valueOf(0.00001));
		decimalMin.setDescriptor(conversionAttrDesc);

		final DecimalMaxConstraintModel decimalMax = modelService.create(DecimalMaxConstraintModel.class);
		decimalMax.setId("decimalMax");
		decimalMax.setValue(BigDecimal.valueOf(5.23));
		decimalMax.setDescriptor(conversionAttrDesc);

		modelService.saveAll(decimalMin, decimalMax);
		validationService.reloadValidationEngine();

		//now the fun part
		//testobject is conversion from the ownCurr model which must between 0.00001 and 5.23 
		//and the returned value must be X.YYYY

		try
		{
			ownCurr.setConversion(Double.valueOf(0.00000001));
			modelService.save(ownCurr);
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.DECIMAL_MIN.msgKey, "conversion");
		}

		try
		{
			ownCurr.setConversion(Double.valueOf(4.34000001));
			modelService.save(ownCurr);
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, Constraint.DECIMAL_MAX.msgKey, "conversion");
		}

		ownCurr.setConversion(Double.valueOf(3.0));
		modelService.save(ownCurr);
	}


	/**
	 * checking validation after detaching all - should not throw any validation exception
	 */
	@Test
	public void testDecimalMinMaxAndDigitsAfterDetachingConstraints()
	{
		//demo object - testing conversion
		final CurrencyModel ownCurr = modelService.create(CurrencyModel.class);
		ownCurr.setIsocode("XxX");
		ownCurr.setSymbol("X");
		ownCurr.setActive(Boolean.TRUE);
		ownCurr.setDigits(Integer.valueOf(5));
		ownCurr.setConversion(Double.valueOf(3.45671));
		modelService.save(ownCurr);

		final AttributeDescriptorModel conversionAttrDesc = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);

		final DecimalMinConstraintModel decimalMin = modelService.create(DecimalMinConstraintModel.class);
		decimalMin.setId("decimalMin");
		decimalMin.setValue(BigDecimal.valueOf(0.00001));
		decimalMin.setDescriptor(conversionAttrDesc);

		final DecimalMaxConstraintModel decimalMax = modelService.create(DecimalMaxConstraintModel.class);
		decimalMax.setId("decimalMax");
		decimalMax.setValue(BigDecimal.valueOf(5.23));
		decimalMax.setDescriptor(conversionAttrDesc);

		modelService.saveAll(decimalMin, decimalMax);
		validationService.unloadValidationEngine();

		//now the fun part
		//testobject is conversion from the ownCurr model which must between 0.00001 and 5.23 
		//and the returned value must be X.YYYY
		try
		{
			ownCurr.setConversion(Double.valueOf(0.00000001));
			modelService.save(ownCurr);
		}
		catch (final ModelSavingException e)
		{
			fail("Should not be any validation exception after detach .");
		}

		try
		{
			ownCurr.setConversion(Double.valueOf(4.34000001));
			modelService.save(ownCurr);
		}
		catch (final Exception e)
		{
			fail("Should not be any validation exception after detach .");
		}
	}

}
