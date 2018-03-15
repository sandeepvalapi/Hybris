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
package de.hybris.platform.validation.messages;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.model.constraints.DynamicConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.DigitsConstraintModel;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for message propagation logic from different source model's message explicit localized value or message key from
 * resource bundle.
 */
@IntegrationTest
public class MessageLocalizationTest extends AbstractConstraintTest
{
	@Before
	public void setUp()
	{
		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode("de");
		modelService.save(language);
	}

	/**
	 * Test localized validation messages for type based validation.
	 */
	@Test
	public void testTypeConstraintLocalization()
	{
		// create a DynamicConstraint which always evaluates a Beanshellscript to 'false' 
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("typeConstraint");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setExpression("return false;");
		constraint.setMessage("{type} fails validation", Locale.UK);
		constraint.setMessage("{type} ist nicht valide", Locale.GERMANY);

		modelService.save(constraint);
		validationService.reloadValidationEngine();

		// create a CustomerModel which gets validated
		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatable");


		// assert english and german localized messages
		// both are taken from 'message' attribute
		// placeholder {type} is processed
		assertLocalization(Locale.ENGLISH, "CustomerModel fails validation", model);
		assertLocalization(Locale.GERMAN, "CustomerModel ist nicht valide", model);

		// nullify localized message for UK
		// this enabled ResourceBundle fallback
		constraint.setMessage(null, Locale.UK);

		// same as above but english localization is now taken from resource bundle
		assertLocalization(Locale.ENGLISH, "Type \"CustomerModel\" and script \"return false;\" reports an error.", model);
		assertLocalization(Locale.GERMAN, "CustomerModel ist nicht valide", model);
	}

	/**
	 * Test localized validation messages for attribute based validation.
	 */
	@Test
	public void testAttributeConstraintLocalization()
	{
		// create a DigitConstraint which defines format: integer=1, fraction=2
		final DigitsConstraintModel constraint = modelService.create(DigitsConstraintModel.class);
		final AttributeDescriptorModel attribute = typeService.getAttributeDescriptor(
				typeService.getComposedType(CurrencyModel.class), CurrencyModel.CONVERSION);

		//constraint.setQualifier("conversion");
		constraint.setTarget(CurrencyModel.class);
		constraint.setDescriptor(attribute);
		constraint.setId("digitConstraint");
		constraint.setInteger(Integer.valueOf(1));
		constraint.setFraction(Integer.valueOf(2));
		constraint.setMessage(
				"Type {type} fails validation at property {field}. Valid format: integer={integer}, fractions={fraction}", Locale.UK);
		constraint.setMessage("Typ {type} ist nicht valide bei {field}. Valides format: {integer}.{fraction}", Locale.GERMANY);

		modelService.save(constraint);
		validationService.reloadValidationEngine();

		// create a CurrencyModel which gets validated
		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setConversion(Double.valueOf(11.11));
		currency.setIsocode("ERN");

		// assert english and german localized messages
		// both are taken from 'message' attribute
		// placeholder {type} is processed
		assertLocalization(Locale.GERMAN, "Typ CurrencyModel ist nicht valide bei conversion. Valides format: 1.2", currency);
		assertLocalization(Locale.ENGLISH,
				"Type CurrencyModel fails validation at property conversion. Valid format: integer=1, fractions=2", currency);

		// nullify localized message for UK
		// this enabled ResourceBundle fallback
		constraint.setMessage(null, Locale.UK);

		// same as above but english localization is now taken from resource bundle
		assertLocalization(Locale.GERMAN, "Typ CurrencyModel ist nicht valide bei conversion. Valides format: 1.2", currency);
		assertLocalization(Locale.ENGLISH,
				"The attribute \"conversion\" has an invalid numeric syntax (<1 digits>.<2 digits> expected).", currency);
	}

	private void assertLocalization(final Locale sessionLocale, final String expectedMessage, final ItemModel model)
	{
		i18nService.setCurrentLocale(sessionLocale);

		try
		{
			modelService.save(model);
			Assert.fail("Missing expected " + ModelSavingException.class.getSimpleName());
		}
		catch (final ModelSavingException e)
		{
			Assert.assertTrue(e.getCause() instanceof ValidationViolationException);
			final String gotMessage = e.getMessage().trim().toLowerCase();
			Assert.assertTrue("expected: " + expectedMessage.toLowerCase() + " got: " + gotMessage, //
					gotMessage.contains(expectedMessage.toLowerCase()));
		}
	}
}
