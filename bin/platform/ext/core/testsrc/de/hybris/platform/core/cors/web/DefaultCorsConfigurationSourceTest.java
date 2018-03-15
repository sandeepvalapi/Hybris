/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.core.cors.web;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.cors.constants.CorsConstants;
import de.hybris.platform.core.cors.exception.MissingDefaultCorsConfigurationException;
import de.hybris.platform.core.cors.loader.CorsPropertiesLoader;
import de.hybris.platform.core.model.cors.CorsConfigurationPropertyModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.Resource;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class DefaultCorsConfigurationSourceTest extends ServicelayerBaseTest
{
	private static final String HTTP_HYBRIS_COM = "http://hybris.com";
	private static final String HTTP_HYBRIS_DE = "http://hybris.de";
	private static final String HTTP_LOCALHOST = "http://localhost";
	private static final String CORSFILTER = "corsfilter";
	private static final String DEFAULT = "default";
	private static final String WEBAPP = "webapp";
	private static final String DOT = ".";

	@Resource(name = "defaultCorsConfigurationSource")
	private DefaultCorsConfigurationSource defaultCorsConfigurationSource;
	@Resource(name = "guavaCachedCorsPropertiesLoader")
	private CorsPropertiesLoader guavaCachedCorsPropertiesLoader;
	@Resource(name = "hashMapCachedCorsPropertiesLoader")
	private CorsPropertiesLoader hashMapCachedCorsPropertiesLoader;
	@Resource(name = "registryBasedCorsPropertiesLoader")
	private CorsPropertiesLoader defaultCorsPropertiesLoader;
	@Resource
	private ModelService modelService;

	@Resource
	private ConfigurationService configurationService;

	@Test
	public void testAllowAllOrigins()
	{
		// given
		setParameter(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedOrigins, "*");

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				(WEBAPP));

		// then
		assertThat(corsConfiguration.checkOrigin("*")).isNotNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_LOCALHOST)).isNotNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_DE)).isNotNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_COM)).isNotNull();
	}

	@Test
	public void testAllowSpecificOrigin()
	{
		// given
		setParameter(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedOrigins, HTTP_HYBRIS_COM);

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				(WEBAPP));

		// then
		assertThat(corsConfiguration.checkOrigin("*")).isNull();
		assertThat(corsConfiguration.checkOrigin(null)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_LOCALHOST)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_COM)).isNotNull().isEqualTo(HTTP_HYBRIS_COM);
	}

	@Test
	public void testAllowSpecificOriginWithDBItemPrecedence()
	{
		// given
		setParameter(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedOrigins, HTTP_LOCALHOST + " " +
				HTTP_HYBRIS_DE);
		final CorsConfigurationPropertyModel corsConfigurationProperty = modelService.create
				(CorsConfigurationPropertyModel.class);
		corsConfigurationProperty.setContext(WEBAPP);
		corsConfigurationProperty.setKey(CorsConstants.Key.allowedOrigins.name());
		corsConfigurationProperty.setValue(HTTP_HYBRIS_COM);
		modelService.save(corsConfigurationProperty);

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				(WEBAPP));

		// then
		assertThat(corsConfiguration.checkOrigin(null)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_LOCALHOST)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_DE)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_COM)).isNotNull().isEqualTo(HTTP_HYBRIS_COM);
	}

	@Test
	public void testAllowedMethods()
	{
		// given
		setParameter(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedMethods, "POST PUT DELETE");

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				(WEBAPP));

		// then
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.POST)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.PUT)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.DELETE)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.GET)).isNull();
	}

	@Test
	public void testAllowedMethodsWithDBItemPrecedence()
	{
		// given
		setParameter(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedMethods, "POST PUT DELETE");
		final CorsConfigurationPropertyModel corsConfigurationProperty = modelService.create
				(CorsConfigurationPropertyModel.class);
		corsConfigurationProperty.setContext(WEBAPP);
		corsConfigurationProperty.setKey(CorsConstants.Key.allowedMethods.name());
		corsConfigurationProperty.setValue(HttpMethod.GET.toString());
		modelService.save(corsConfigurationProperty);

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				(WEBAPP));

		// then
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.POST)).isNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.PUT)).isNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.DELETE)).isNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.GET)).isNotEmpty();
	}

	@Test
	public void testEmptyConfiguration()
	{
		// given
		// no configuration at all
		final MockHttpServletRequest request = new MockHttpServletRequest();

		// when
		try
		{
			defaultCorsConfigurationSource.getCorsConfiguration(request);
		}
		// then
		catch (final MissingDefaultCorsConfigurationException exception)
		{
			assertThat(exception.getMessage())
					.isEqualTo(Localization.getLocalizedString(CorsConstants
							.EXCEPTION_CORS_MISSING_DEFAULT_CONFIGURATION, new String[]
							{WEBAPP}));
		}
	}

	@Test
	public void testDefaultConfiguration()
	{
		// given
		setParameter(CORSFILTER + DOT + DEFAULT + DOT + CorsConstants.Key.allowedOrigins, HTTP_LOCALHOST + " " +
				HTTP_HYBRIS_DE);
		setParameter(CORSFILTER + DOT + DEFAULT + DOT + CorsConstants.Key.allowedMethods, "POST PUT DELETE");

		// when
		final CorsConfiguration corsConfiguration = defaultCorsConfigurationSource.getCorsConfiguration(createRequest
				());

		// then
		assertThat(corsConfiguration.checkOrigin(null)).isNull();
		assertThat(corsConfiguration.checkOrigin(HTTP_LOCALHOST)).isNotNull().isEqualTo(HTTP_LOCALHOST);
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_DE)).isNotNull().isEqualTo(HTTP_HYBRIS_DE);
		assertThat(corsConfiguration.checkOrigin(HTTP_HYBRIS_COM)).isNull();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.POST)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.PUT)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.DELETE)).isNotEmpty();
		assertThat(corsConfiguration.checkHttpMethod(HttpMethod.GET)).isNull();
	}

	@Test
	public void testCachePerformance()
	{
		CorsPropertiesLoader propertiesLoader = defaultCorsConfigurationSource.getPropertiesLoader();
		defaultCorsConfigurationSource.setPropertiesLoader(defaultCorsPropertiesLoader);

		final DateTime noCacheStart = DateTime.now();
		for (int i = 0; i < 99999; i++)
		{
			defaultCorsConfigurationSource.loadPropertiesFromHybrisConfig(DEFAULT);
		}
		final DateTime noCacheEnd = DateTime.now();
		final Duration noCacheDuration = new Duration(noCacheStart, noCacheEnd);
		final float durationWithoutCache = noCacheDuration.getMillis();

		defaultCorsConfigurationSource.setPropertiesLoader(hashMapCachedCorsPropertiesLoader);
		final DateTime hashMapCacheStart = DateTime.now();
		for (int i = 0; i < 99999; i++)
		{
			defaultCorsConfigurationSource.loadPropertiesFromHybrisConfig(DEFAULT);
		}
		final DateTime hashMapCacheEnd = DateTime.now();
		final Duration hashMapCacheDuration = new Duration(hashMapCacheStart, hashMapCacheEnd);
		final float durationWithHashMapCache = hashMapCacheDuration.getMillis();

		defaultCorsConfigurationSource.setPropertiesLoader(guavaCachedCorsPropertiesLoader);
		final DateTime guavaCacheStart = DateTime.now();
		for (int i = 0; i < 99999; i++)
		{
			defaultCorsConfigurationSource.loadPropertiesFromHybrisConfig(DEFAULT);
		}
		final DateTime guavaCacheEnd = DateTime.now();
		final Duration guavaCacheDuration = new Duration(guavaCacheStart, guavaCacheEnd);
		final float durationWithGuavaCache = guavaCacheDuration.getMillis();

		defaultCorsConfigurationSource.setPropertiesLoader(propertiesLoader);

		System.out.printf("loading properties 99999 times\n");
		System.out.printf("          noCache: %5.0f ms\n", durationWithoutCache);
		System.out.printf("       guavaCache: %5.0f ms (%4.0f times faster)\n", durationWithGuavaCache,
				durationWithoutCache /
						durationWithGuavaCache);
		System.out.printf("     hashMapCache: %5.0f ms (%4.0f times faster)\n", durationWithHashMapCache,
				durationWithoutCache /
						durationWithHashMapCache);
		assertThat(durationWithoutCache / durationWithGuavaCache).isGreaterThan(10);
	}

	private void setParameter(final String key, final String value)
	{
		configurationService.getConfiguration().setProperty(key, value);
	}

	private MockHttpServletRequest createRequest(final String contextName)
	{
		if (contextName == null)
		{
			return new MockHttpServletRequest();
		}
		final MockServletContext context = new MockServletContext();
		context.setServletContextName(contextName);
		return new MockHttpServletRequest(context);
	}

	private MockHttpServletRequest createRequest()
	{
		return createRequest(null);
	}
}
