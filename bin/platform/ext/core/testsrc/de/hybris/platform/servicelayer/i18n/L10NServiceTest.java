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
package de.hybris.platform.servicelayer.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.impl.CompositeResourceBundle;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class L10NServiceTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(L10NServiceTest.class);

	@Resource
	private I18NService i18nService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private L10NService l10nService;

	@Resource
	private ModelService modelService;

	@Before
	public void prepare()
	{
		try
		{
			commonI18NService.getLanguage("de");
		}
		catch (final UnknownIdentifierException e)
		{
			((LanguageModel) modelService.create(LanguageModel.class)).setIsocode("de");
		}

		try
		{
			commonI18NService.getLanguage("ch");
		}
		catch (final UnknownIdentifierException e)
		{
			((LanguageModel) modelService.create(LanguageModel.class)).setIsocode("ch");
		}

		try
		{
			commonI18NService.getLanguage("en");
		}
		catch (final UnknownIdentifierException e)
		{
			((LanguageModel) modelService.create(LanguageModel.class)).setIsocode("en");
		}

		modelService.saveAll();
	}

	@Test
	public void testLozalizationGetLocalizedString()
	{
		//constants
		final Float floatValue = new Float(1234.56);
		final Object[] arguments = new Float[]
		{ floatValue };
		final String text = "value = {0}";

		//save previous locale
		final Locale prevLocale = i18nService.getCurrentLocale();

		//English session locale
		final String englishExpectedValue = "value = 1,234.56";
		i18nService.setCurrentLocale(new Locale("en"));
		String actualValue = l10nService.getLocalizedString(text, arguments);
		assertEquals("Wrong english localized value: " + actualValue + " . Should be: " + englishExpectedValue,
				englishExpectedValue, actualValue);

		//German session locale
		final String germanExpectedValue = "value = 1.234,56";
		i18nService.setCurrentLocale(new Locale("de"));
		actualValue = l10nService.getLocalizedString(text, arguments);
		assertEquals("Wrong german localized value: " + actualValue + " . Should be: " + germanExpectedValue, germanExpectedValue,
				actualValue);

		//Swiss session locale
		final String swissExpectedValue = "value = 1'234.56";
		i18nService.setCurrentLocale(new Locale("de", "ch"));
		actualValue = l10nService.getLocalizedString(text, arguments);
		assertEquals("Wrong swiss localized value: " + actualValue + " . Should be: " + swissExpectedValue, swissExpectedValue,
				actualValue);

		//German or Polish VM, English session locale
		i18nService.setCurrentLocale(new Locale("en"));
		actualValue = l10nService.getLocalizedString(text, arguments);
		assertFalse("English localized value: " + actualValue + " should NOT be the same as german localized value: "
				+ germanExpectedValue, germanExpectedValue.equals(actualValue));

		//roll back to previous locale
		i18nService.setCurrentLocale(prevLocale);
	}

	/**
	 * Helper class.<br/>
	 * A single testcase which tests fallback behavior of {@link CompositeResourceBundle}. Provides configuration for
	 * actual (input) and expected (output) values for junit assertions.
	 */
	class AssertFallbackBundle
	{
		private int mode = CompositeResourceBundle.KEY_VALUE_FALLBACK;
		private final List<ResourceBundle> sourceBundles = new ArrayList<ResourceBundle>();
		private ResourceBundle expectedBundle = null;
		private Class<Exception> expectedException = null;

		/**
		 * Add a source/fallback {@link ResourceBundle}.
		 */
		private void addSourceBundle(final String... values)
		{
			this.addSourceBundle(createBundle(values));
		}

		/**
		 * Add a source/fallback {@link ResourceBundle}.
		 */
		private void addSourceBundle(final ResourceBundle bundle)
		{
			this.sourceBundles.add(bundle);
		}

		/**
		 * Sets a {@link ResourceBundle} which equals the expected one.
		 */
		private void setExpectedBundle(final String... values)
		{
			this.expectedBundle = createBundle(values);
		}

		/**
		 * Sets an expected {@link Exception}
		 */
		private void setExpectedException(final Class expectedExceptionClass)
		{
			this.expectedException = expectedExceptionClass;
		}

		/**
		 * Sets the {@link CompositeResourceBundle} mode. e.g {@link CompositeResourceBundle#KEY_VALUE_FALLBACK}
		 */
		private void setMode(final int mode)
		{
			this.mode = mode;
		}


		/**
		 * Executes this test.
		 */
		public void run()
		{
			//create the bundle which gets tested...
			final ResourceBundle actualBundle = new CompositeResourceBundle(this.sourceBundles, mode);

			//...and use the Enumeration of keys from actual and expected bundle
			final Enumeration expectedEnum = expectedBundle.getKeys();
			final Enumeration actualEnum = actualBundle.getKeys();

			try
			{
				//go through each available key...
				while (expectedEnum.hasMoreElements() && actualEnum.hasMoreElements())
				{
					//...fetch expected key; increase counter of actual enum
					final Object expectedKey = expectedEnum.nextElement();
					actualEnum.nextElement();

					//...compare values
					final String expectedValue = (String) expectedBundle.getObject((String) expectedKey);
					final String actualValue = (String) actualBundle.getObject((String) expectedKey);

					LOG.debug("Expecting " + expectedKey + "=" + expectedValue + "; got:" + actualValue);
					Assert.assertEquals(expectedValue, actualValue);
				}

				//fail when size of actual/expected enumeration differs
				if (expectedEnum.hasMoreElements() || actualEnum.hasMoreElements())
				{
					Assert.fail("Enums are not equal: expectedEnumHasMoreElements:" + expectedEnum.hasMoreElements());
				}
			}
			catch (final Exception e)
			{
				LOG.debug("Caught exception " + e.getClass() + "(" + e.getMessage() + ")");
				Assert.assertEquals(e.getClass(), this.expectedException);
			}

		}
	}

	/**
	 * Creates a valid ResourceBundle based on an String array. Each array element is formated in "key=value" and
	 * specifies exactly one bundle entry.
	 */
	public static ResourceBundle createBundle(final String[] bundle)
	{
		//print entries into a Stringwriter
		final StringWriter writer = new StringWriter();
		final PrintWriter printer = new PrintWriter(writer);

		for (final String value : bundle)
		{
			printer.println(value);
		}
		printer.println();


		//create bundle based on an InputStream of previously used StringWriter
		PropertyResourceBundle result = null;
		try
		{
			final InputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes());
			result = new PropertyResourceBundle(inputStream);
		}
		catch (final IOException e)
		{
			Assert.fail(e.getMessage());
		}
		return result;
	}

	@Test
	public void testCompositeResourceBundle()
	{
		AssertFallbackBundle test;
		final List<AssertFallbackBundle> tests = new ArrayList<AssertFallbackBundle>();

		//KEY_VALUE_FALLBACK; test key-fallback
		//each bundle brings own keys
		test = new AssertFallbackBundle();
		test.setMode(CompositeResourceBundle.KEY_VALUE_FALLBACK);
		test.addSourceBundle("bundle1.key1=value1-1", "bundle1.key2=value1-2");
		test.addSourceBundle("bundle2.key1=value2-1", "bundle2.key2=value2-2");
		test.setExpectedBundle("bundle1.key1=value1-1", "bundle1.key2=value1-2", "bundle2.key1=value2-1", "bundle2.key2=value2-2");
		tests.add(test);

		//KEY_VALUE_FALLBACK; test key-fallback
		//key-collision: bundle1.key1; key of bundle1 most overrule
		test = new AssertFallbackBundle();
		test.setMode(CompositeResourceBundle.KEY_VALUE_FALLBACK);
		test.addSourceBundle("bundle1.key1=value1-1", "bundle1.key2=value1-2");
		test.addSourceBundle("bundle1.key1=value2-1", "bundle2.key2=value2-2");
		test.setExpectedBundle("bundle1.key1=value1-1", "bundle1.key2=value1-2", "bundle2.key2=value2-2");
		tests.add(test);

		//KEY_VALUE_FALLBACK; test key-fallback
		//provoke exception
		test = new AssertFallbackBundle();
		test.setMode(CompositeResourceBundle.KEY_VALUE_FALLBACK);
		test.addSourceBundle("bundle1.key1=value1-1", "bundle1.key2=value1-2");
		test.addSourceBundle("bundle1.key1=value2-1", "bundle2.key2=value2-2");
		test.setExpectedException(MissingResourceException.class);
		test.setExpectedBundle("bundle2.key1=value1-1");
		tests.add(test);


		//KEY_VALUE_FALLBACK; test value-fallback
		test = new AssertFallbackBundle();
		test.setMode(CompositeResourceBundle.KEY_VALUE_FALLBACK);
		test.addSourceBundle("bundle1.key1= ", "bundle1.key2=value1-2");
		test.addSourceBundle("bundle1.key1=value2-1", "bundle2.key2=value2-2");
		test.setExpectedBundle("bundle1.key1=value2-1", "bundle1.key2=value1-2", "bundle2.key2=value2-2");
		tests.add(test);

		//KEY_VALUE_FALLBACK; test value-fallback
		test = new AssertFallbackBundle();
		test.setMode(CompositeResourceBundle.KEY_VALUE_FALLBACK);
		test.addSourceBundle("bundle1.key1= ", "bundle1.key2=value1-2");
		test.addSourceBundle("bundle1.key1=", "bundle2.key2=value2-2");
		test.addSourceBundle("bundle1.key1=value3-1");
		test.setExpectedBundle("bundle1.key1=value3-1", "bundle1.key2=value1-2", "bundle2.key2=value2-2");
		tests.add(test);



		for (int i = 0; i < tests.size(); i++)
		{
			final AssertFallbackBundle singleTest = tests.get(i);
			LOG.debug(i + "# mode=" + singleTest.mode);
			singleTest.run();
		}
	}
}
