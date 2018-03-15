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
package de.hybris.platform.validation.model.constraints.jsr303;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.validation.constants.ValidationConstants;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import de.hybris.platform.validation.jalo.constraints.ConstraintGroup;
import de.hybris.platform.validation.messages.ResourceBundleProvider;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;


/**
 * Basis class for the single constraint (JSR 303) test. The needed services are provided and (IMPORTANT!!!) the
 * validation engine is NOT! reloaded before each test.
 */
@Ignore
public abstract class AbstractConstraintTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(AbstractConstraintTest.class);

	@Resource
	protected ModelService modelService;

	@Resource
	protected ValidationService validationService;

	@Resource
	protected ConstraintDao constraintDao;

	@Resource
	protected TypeService typeService;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	@Resource
	protected I18NService i18nService;

	@Resource
	private ResourceBundleProvider resourceBundleProvider;

	protected enum Constraint
	{
		ASSERT_FALSE("javax.validation.constraints.AssertFalse.message"), //
		ASSERT_TRUE("javax.validation.constraints.AssertTrue.message"), //
		NOT_NULL("javax.validation.constraints.NotNull.message"), //
		NULL("javax.validation.constraints.Null.message"), //
		DECIMAL_MIN("javax.validation.constraints.DecimalMin.message"), //
		DECIMAL_MAX("javax.validation.constraints.DecimalMax.message"), //
		HYBRIS_DECIMAL_MIN("de.hybris.platform.validation.annotations.HybrisDecimalMin.message"), //
		HYBRIS_DECIMAL_MAX("de.hybris.platform.validation.annotations.HybrisDecimalMax.message"), //
		DIGITS("javax.validation.constraints.Digits.message"), //
		FUTURE("javax.validation.constraints.Future.message"), //
		PAST("javax.validation.constraints.Past.message"), //
		PATTERN("javax.validation.constraints.Pattern.message"), //
		MAX("javax.validation.constraints.Max.message"), //
		MIN("javax.validation.constraints.Min.message"), //
		SIZE("javax.validation.constraints.Size.message"), //
		BEANSHELL("de.hybris.platform.validation.annotations.Dynamic.message"), //
		XOR_NOT_NULL_REFERENCE("de.hybris.platform.validation.annotations.XorNotNull.message");

		public final String msgKey;

		private Constraint(final String key)
		{
			this.msgKey = key;
		}
	}

	@Before
	public void resetConstraints()
	{
		//same as ValidationDataSetup.createEssencialDataForValidation() but without reloading the validationService!
		try
		{
			//see PLA-9783 - AD targetclass existed till 4.2.0.x and was removed in 4.2.1.0
			final AttributeDescriptorModel attrdesc = typeService.getAttributeDescriptor(ConstraintGroupModel._TYPECODE,
					"targetclass");
			LOG.info("Found AttributeDescriptor 'targetClass' for type ConstraintGroup. Removing it.");
			modelService.remove(attrdesc);
			//refresh the constraintgroup ComposedTypeModel, maybe it was already loaded and still contains the deleted AD
			modelService.refresh(typeService.getComposedTypeForClass(ConstraintGroup.class)); //see BAM-380
		}
		catch (final UnknownIdentifierException e)
		{
			//AttributeDescriptor does not exist, which is ok 
		}

		try
		{
			validationService.getDefaultConstraintGroup();
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Default constraint group was found.");
			}
		}
		catch (final ModelNotFoundException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Creating default constraint group.");
			}
			final ConstraintGroupModel defaultgroup = modelService.create(ConstraintGroupModel.class);
			defaultgroup.setId(ValidationConstants.DEFAULT_CONSTRAINTGROUP_ID);
			defaultgroup.setInterfaceName(ValidationConstants.DEFAULT_CONSTRAINTGROUP_INTERFACENAME);
			defaultgroup.setConstraints(Collections.EMPTY_SET);
			modelService.save(defaultgroup);
		}
	}

    private static final double MINUS_ONE = -1;

    protected double calculateFractionPrecision(final double adjust)
    {
        return (Config.getDouble("jdbcmappings.big_decimal_scale", 5.0D) + adjust) * MINUS_ONE;
    }


	protected void checkException(final Throwable exception, final Class mainException, final Class interceptor)
	{
		assertThat(mainException).isEqualTo(exception.getClass());
		assertThat(exception.getCause().getClass()).isEqualTo(interceptor);
	}

	protected void checkException(final Throwable exception, final Class mainException, final Class innerException,
								  final Class interceptor)
	{
		assertEquals(mainException, exception.getClass());
		assertEquals(innerException, exception.getCause().getClass());
		if (InterceptorException.class.equals(innerException))
		{
			final InterceptorException interceptorException = (InterceptorException) exception.getCause();
			assertEquals(interceptor, interceptorException.getInterceptor().getClass());
		}

	}

	protected String getDefaultMessage(final String key)
	{
		try
		{
			return resourceBundleProvider.getResourceBundle(jaloSession.getSessionContext().getLocale()).getString(key);
		}
		catch (final MissingResourceException mre)
		{
			LOG.info("could not load bundle resource " + key + ", details[" + mre.getMessage() + "]");
			final ResourceBundle bundle = resourceBundleProvider.getResourceBundle(jaloSession.getSessionContext().getLocale());
			for (final Enumeration<String> keyEnum = bundle.getKeys(); keyEnum.hasMoreElements();)
			{
				LOG.warn(String.format("existing key[%s] : %s ", jaloSession.getSessionContext().getLocale(), keyEnum.nextElement()));
			}
			throw mre;
		}
	}

	protected void assertModelSavingExceptionWithMessageKey(final Exception msEx,
			final Map<String, String> expectedConstraintViolations)
	{
		Assert.assertTrue(msEx.getClass() + " is not an instance of " + ModelSavingException.class,
				msEx instanceof ModelSavingException);

		final Throwable cause = msEx.getCause();

		Assert.assertTrue(
				cause.getClass().getSimpleName() + " is not of type " + ValidationViolationException.class.getSimpleName(),
				cause instanceof ValidationViolationException);

		final ValidationViolationException validationViolationException = (ValidationViolationException) cause;

		final Set<HybrisConstraintViolation> violations = validationViolationException.getHybrisConstraintViolations();

		Assert.assertEquals("expected constraint violation size (" + expectedConstraintViolations.size()
				+ ") is differend from the actual size (" + violations.size() + ")", expectedConstraintViolations.size(),
				violations.size());

		for (final HybrisConstraintViolation violation : violations)
		{
			final String gotQualifier = violation.getProperty();
			final String gotType = violation.getTypeName();
			String expectedMessage = null;
			if (expectedConstraintViolations.containsKey(gotQualifier))
			{
				expectedMessage = expectedConstraintViolations.get(gotQualifier);

				Assert.assertEquals("Expected localization key for error message is: " + expectedMessage + " but actually got "
						+ violation.getMessageTemplate(), "{" + expectedMessage + "}", violation.getMessageTemplate());
			}
			else if (expectedConstraintViolations.containsKey(gotType))
			{
				expectedMessage = expectedConstraintViolations.get(gotType);

				Assert.assertEquals("Expected localization key for error message is: " + expectedMessage + " but actually got "
						+ violation.getMessageTemplate(), "{" + expectedMessage + "}", violation.getMessageTemplate());
			}
			else
			{
				Assert.fail("Got a ConstraintViolation with property '" + violation.getProperty() + "' or type '"
						+ violation.getTypeName() + "' which was not expected");
			}
		}
	}

	protected void assertModelSavingExceptionWithEvaluatedMessage(final Exception msEx,
			final Map<String, String> expectedConstraintViolations)
	{
		Assert.assertTrue(msEx.getClass() + " is not an instance of " + ModelSavingException.class,
				msEx instanceof ModelSavingException);

		final Throwable cause = msEx.getCause();

		Assert.assertTrue(
				cause.getClass().getSimpleName() + " is not of type " + ValidationViolationException.class.getSimpleName(),
				cause instanceof ValidationViolationException);

		final ValidationViolationException validationViolationException = (ValidationViolationException) cause;

		final Set<HybrisConstraintViolation> violations = validationViolationException.getHybrisConstraintViolations();

		Assert.assertEquals("expected constraint violation size (" + expectedConstraintViolations.size()
				+ ") is differend from the actual size (" + violations.size() + ")", expectedConstraintViolations.size(),
				violations.size());

		for (final HybrisConstraintViolation violation : violations)
		{
			final String gotQualifier = violation.getProperty();
			final String gotType = violation.getTypeName();
			String expectedMessage = null;
			if (expectedConstraintViolations.containsKey(gotQualifier))
			{
				expectedMessage = expectedConstraintViolations.get(gotQualifier);

				Assert.assertEquals(
						"Expected message for error message is: " + expectedMessage + " but actually got "
								+ violation.getMessageTemplate(), expectedMessage, violation.getLocalizedMessage());
			}
			else if (expectedConstraintViolations.containsKey(gotType))
			{
				expectedMessage = expectedConstraintViolations.get(gotType);

				Assert.assertEquals(
						"Expected message for error message is: " + expectedMessage + " but actually got "
								+ violation.getMessageTemplate(), expectedMessage, violation.getLocalizedMessage());
			}
			else
			{
				Assert.fail("Got a ConstraintViolation with property '" + violation.getProperty() + "' or type '"
						+ violation.getTypeName() + "' which was not expected");
			}
		}
	}

	/**
	 * Assert for an expected exception, expected evaluated for current locale message , expected Property/Type
	 * 
	 */
	protected void assertModelSavingExceptionWithEvaluatedMessage(final Exception msEx, final String expectedMessageKey,
			final String expectedPropertyType)
	{
		final Map<String, String> params = new HashMap<String, String>(1);
		params.put(expectedPropertyType, expectedMessageKey);
		assertModelSavingExceptionWithEvaluatedMessage(msEx, params);
	}

	/**
	 * Assert for an expected exception, expected message key , expected Property/Type
	 * 
	 */
	protected void assertModelSavingExceptionWithMessageKey(final Exception msEx, final String expectedMessageKey,
			final String expectedPropertyType)
	{
		final Map<String, String> params = new HashMap<String, String>(1);
		params.put(expectedPropertyType, expectedMessageKey);
		assertModelSavingExceptionWithMessageKey(msEx, params);
	}

	protected void assertTrimmedException(final Exception msEx, final String key)
	{
		final String messageTrimmed = getDefaultMessage(key).replaceAll("'.*?'", "").trim();
		final String exceptionTrimmed = msEx.getCause().getMessage().trim().replaceAll("'.*?'", "").trim();
		if (messageTrimmed.equals(exceptionTrimmed))
		{
			LOG.info("message expected [" + getDefaultMessage(key) + "]");
			LOG.info("message occurred [" + msEx.getCause().getMessage() + "]");
		}
		Assert.assertEquals(
				"Not equal [" + msEx.getMessage() + "] with expected [" + exceptionTrimmed + "] for a key[" + key + "]",
				messageTrimmed, exceptionTrimmed);
	}

	@After
	public void unloadEngine()
	{
		validationService.unloadValidationEngine();
	}
}
