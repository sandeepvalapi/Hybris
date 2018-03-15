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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.validation.annotations.Dynamic;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.interceptors.ValidationInterceptor;
import de.hybris.platform.validation.messages.CustomMessageValidationTest;
import de.hybris.platform.validation.model.constraints.DynamicConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.pojos.PojoTwo;

import java.util.Date;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Test covering common cases of beanshell based validator.
 */
@IntegrationTest
public class BeanShellConstraintValidationTest extends AbstractConstraintTest
{
	private static final Logger LOG = Logger.getLogger(CustomMessageValidationTest.class.getName());
	private static String exampleHelloScript = "print(\"hello!\");";

	@Test
	public void testSimplePrintln()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setExpression(exampleHelloScript);

		modelService.save(constraint);
		validationService.reloadValidationEngine();

		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatable");
		modelService.save(model);
	}

	/**
	 * tests bindable jalosSession object
	 * @deprecated since ages
	 */
	@Deprecated
	@Test
	public void testSimpleJaloFetch()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setExpression("print(jaloSession);\nreturn true;");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatableWithCtx");
		modelService.save(model);
	}

	/**
	 * tests bindable application ctx object
	 */
	@Test
	public void testSimpleSlayerFetch()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel languageModel = typeService.getComposedType(LanguageModel.class);
		constraint.setType(languageModel);
		constraint
				.setExpression("\njava.util.Locale loc= ctx.getBean(\"i18nService\",de.hybris.platform.servicelayer.i18n.I18NService.class).getCurrentLocale();\n"
						+ //
						"print(loc.getLanguage());\n" + //
						"return !loc.getLanguage().equals(getIsocode());\n");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);
		final LanguageModel model = modelService.create(LanguageModel.class);
		model.setIsocode("not_en");
		model.setName("some not english language");
		modelService.save(model);

	}

	/**
	 * this case in future should be replaced with checking script validity after saving such constraint , so misspelled
	 * BSH scripts throwing {@link bsh.EvalError} won't be savable
	 */
	@Test
	public void testSimplePrintlnInvalidScript()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setExpression("won't compile as script ,.,.,,.");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);

		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatable");
		try
		{
			modelService.save(model);
		}
		catch (final ModelSavingException e)
		{
			Assert.assertEquals("Should be thrown RT exception during validation ", ValidationViolationException.class, e.getCause()
					.getClass());
		}
	}

	/**
	 * validates customer instance if it suits such script
	 * 
	 * <pre>
	 * return (this.name != this.customerID);
	 */
	@Test
	public void testSimpleCustomerNameCheck()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setExpression("print(getName()==getCustomerID()); \n return (getName() != getCustomerID());");
		LOG.info("Validating using expression \n [" + constraint.getExpression() + "]");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);

		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatable");
		model.setName("sameCustomerIDAsUserName");
		model.setCustomerID(model.getName());

		Set<HybrisConstraintViolation> violations = validationService.validate(model);
		Assert.assertEquals("Should violate one constraint ", 1, violations.size());
		Assert.assertEquals("Should violate specific dynamic constraint", "{" + Constraint.BEANSHELL.msgKey + "}", violations
				.iterator().next().getMessageTemplate());
		try
		{
			modelService.save(model);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, ValidationViolationException.class, ValidationInterceptor.class);
			assertTrue(e.getCause().getMessage().contains("Type \"CustomerModel\" and script"));
		}

		model.setCustomerID("something else");
		violations = validationService.validate(model);
		Assert.assertEquals("Should not violate one constraint ", 0, violations.size());
		modelService.save(model);
	}

	/**
	 * validates customer instance if it suits such script
	 * 
	 * <pre>
	 * return (this.creationDate.after(new Date());
	 * 
	 */
	@Test
	public void testSimpleCustomerDateCheck()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		final ComposedTypeModel userModel = typeService.getComposedType(UserModel.class);
		constraint.setType(userModel);
		constraint.setMessage("created in the past");
		constraint.setExpression("print(getCreationtime().after(new Date()));\n" //
				+ "return (getCreationtime().after(new Date()));");
		LOG.info("Validating using expression \n [" + constraint.getExpression() + "]");

		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);

		final CustomerModel model = modelService.create(CustomerModel.class);
		model.setUid("BeanShellValidatable");
		model.setName("sameCustomerIDAsUserName");
		model.setCustomerID(model.getName());
		model.setCreationtime(new Date(System.currentTimeMillis()));

		Set<HybrisConstraintViolation> violations = validationService.validate(model);
		Assert.assertEquals("Should violate one constraint ", 1, violations.size());
		Assert.assertEquals("Should violate specific dynamic constraint", "{" + Constraint.BEANSHELL.msgKey + "}", violations
				.iterator().next().getMessageTemplate());
		try
		{
			modelService.save(model);
		}
		catch (final ModelSavingException mse)
		{
			if (mse.getCause() instanceof ValidationViolationException)
			{
				final ValidationViolationException vve = (ValidationViolationException) mse.getCause();

				final Set<HybrisConstraintViolation> violationsDuringSave = vve.getHybrisConstraintViolations();

				Assert.assertEquals("Should violate one constraint ", 1, violationsDuringSave.size());
				Assert.assertEquals("Should violate specific dynamic constraint", "created in the past", violationsDuringSave
						.iterator().next().getLocalizedMessage());
				//checkSimpleCustomerViolations(vve.getConstraintViolations());
			}
		}

		//now a little time travel
		model.setCreationtime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
		violations = validationService.validate(model);
		Assert.assertEquals("Should not violate any constraint ", 0, violations.size());
		modelService.save(model);
	}

	@Test
	public void testSimplePojoFieldCheck()
	{
		final DynamicConstraintModel constraint = modelService.create(DynamicConstraintModel.class);
		constraint.setId("dynaOne");
		constraint.setLanguage(ValidatorLanguage.BEANSHELL);
		constraint.setMessage("date one should after date two");
		constraint.setTarget(PojoTwo.class);
		constraint.setExpression("return (getPojoOnePrivate().after(getPojoTwoPrivate()));");

		LOG.info("Validating using expression \n [" + constraint.getExpression() + "]");
		modelService.save(constraint);
		validationService.reloadValidationEngine();
		Assert.assertEquals(constraint.getAnnotation(), Dynamic.class);

		final PojoTwo pojo = new PojoTwo();
		pojo.setPojoOnePrivate(new Date(System.currentTimeMillis() - 1000));
		pojo.setPojoTwoPrivate(new Date(System.currentTimeMillis() + 1000));

		final Set<HybrisConstraintViolation> violations = validationService.validate(pojo);
		Assert.assertEquals("Should violate one constraint ", 1, violations.size());
		Assert.assertEquals("Should violate specific dynamic constraint", "{" + Constraint.BEANSHELL.msgKey + "}", violations
				.iterator().next().getMessageTemplate());
		pojo.setPojoTwoPrivate(new Date(System.currentTimeMillis() - 1000 * 60));
		final Set<HybrisConstraintViolation> violations2 = validationService.validate(pojo);
		Assert.assertEquals("Should violate none constraint ", 0, violations2.size());
	}
}
