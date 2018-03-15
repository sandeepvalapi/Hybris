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
package de.hybris.platform.core.cors.converter;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.cors.constants.CorsConstants;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import org.junit.Test;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Test for the {@link ConfigPropertiesToCorsConfigurationConverter} class.
 */
@IntegrationTest
public class ConfigPropertiesToCorsConfigurationConverterTest extends ServicelayerBaseTest
{
	private static final String HTTP_HYBRIS_COM = "http://hybris.com";
	private static final String HTTP_HYBRIS_DE = "http://hybris.de";
	private static final String HTTP_LOCALHOST = "http://localhost:9002";
	private static final String CORSFILTER = "corsfilter";
	private static final String WEBAPP = "webapp";
	private static final String SPACE = " ";
	private static final String DOT = ".";

	@Resource
	private ConfigPropertiesToCorsConfigurationConverter configPropertiesToCorsConfigurationConverter;

	@Test
	public void testConverter()
	{
		// given
		final Map<String, String> corsConfiguration = new HashMap<>();
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowCredentials, Boolean.TRUE
				.toString());
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedOrigins,
				HTTP_HYBRIS_COM + SPACE + HTTP_HYBRIS_DE + SPACE + HTTP_LOCALHOST);
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedHeaders, "Content-Language " +
				"Last-Event-ID");
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.allowedMethods, "PUT GET");
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.exposedHeaders, null);
		corsConfiguration.put(CORSFILTER + DOT + WEBAPP + DOT + CorsConstants.Key.maxAge, "12345");

		// when
		final CorsConfiguration configuration = configPropertiesToCorsConfigurationConverter.createCorsConfiguration
				(WEBAPP,
				corsConfiguration);

		// then
		assertThat(configuration.getAllowCredentials()).isTrue();
		assertThat(configuration.getAllowedOrigins()).isNotNull().containsOnly(HTTP_HYBRIS_COM, HTTP_HYBRIS_DE,
				HTTP_LOCALHOST);
		assertThat(configuration.getAllowedHeaders()).isNotNull().containsOnly("Content-Language", "Last-Event-ID");
		assertThat(configuration.getAllowedMethods()).isNotNull().containsOnly("PUT", "GET");
		assertThat(configuration.getExposedHeaders()).isNull();
		assertThat(configuration.getMaxAge()).isEqualTo(12345);
	}

}
