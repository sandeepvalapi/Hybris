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
package de.hybris.platform.media.services.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultMimeServiceIntegrationTest extends ServicelayerBaseTest
{
	private static final String MASTER_TENANT_CSS_MIME_PROPERTY_KEY = "media.customextension.text.css";
	private static final String TEST_MIME_FILE_EXT = "hbr";
	private static final String TEST_MIME_PROPERTY_KEY = "media.customextension.application.hybris";

	// media.customextension.application.hybris maps to application/hybris
	private static final String TEST_MIME_TYPE = "application/hybris";

	@Resource
	private MimeService mimeService;

	@Resource
	private ConfigurationService configurationService;

	private final PropertyConfigSwitcher testMimePropertySwitcher = new PropertyConfigSwitcher(TEST_MIME_PROPERTY_KEY);

	@After
	public void tearDown() {
		testMimePropertySwitcher.switchBackToDefault();

		// restore master tenant properties
		Registry.getMasterTenant().getConfig().setParameter(TEST_MIME_PROPERTY_KEY, null);
		Registry.getMasterTenant().getConfig().setParameter(MASTER_TENANT_CSS_MIME_PROPERTY_KEY, "css");
	}

	@Test
	public void cacheShouldReturnRepeatableResults()
	{
		// given
		final Set<String> availableMimeTypesAtFirstInvocation = mimeService.getSupportedMimeTypes();

		// when
		final Set<String> availableMimeTypesAtSecondInvocation = mimeService.getSupportedMimeTypes();

		// then
		assertThat(availableMimeTypesAtSecondInvocation).isSameAs(availableMimeTypesAtFirstInvocation);
	}


	@Test
	public void addingMimeTypeInvalidatesCache()
	{
		// given
		final Set<String> availableMimeTypes = mimeService.getSupportedMimeTypes();

		// when
		testMimePropertySwitcher.switchToValue(TEST_MIME_FILE_EXT);
		final Set<String> availableMimeTypesAfterAddition = mimeService.getSupportedMimeTypes();

		// then
		final Set<String> expectedMimeTypes = new ImmutableSet.Builder<String>().addAll(availableMimeTypes).add(TEST_MIME_TYPE)
				.build();
		assertThat(availableMimeTypesAfterAddition).isEqualTo(expectedMimeTypes);
	}

	@Test
	public void removingMimeTypeInvalidatesCache()
	{
		// given
		testMimePropertySwitcher.switchToValue(TEST_MIME_FILE_EXT);
		final Set<String> availableMimeTypesBeforeRemoval = mimeService.getSupportedMimeTypes();

		// when
		testMimePropertySwitcher.switchToValue(null);

		// then
		final Set<String> availableMimeTypesAfterRemoval = mimeService.getSupportedMimeTypes();
		final Set<String> expectedMimeTypes = Sets.difference(availableMimeTypesBeforeRemoval, ImmutableSet.of(TEST_MIME_TYPE));
		assertThat(availableMimeTypesAfterRemoval).isEqualTo(expectedMimeTypes);
	}

	@Test
	public void addingMimeTypeViaConfigurationServiceInvalidatesCache()
	{
		// given
		testMimePropertySwitcher.switchToValue(null);
		final Set<String> availableMimeTypes = mimeService.getSupportedMimeTypes();

		// when
		configurationService.getConfiguration().addProperty(TEST_MIME_PROPERTY_KEY, TEST_MIME_FILE_EXT);
		final Set<String> availableMimeTypesAfterAddition = mimeService.getSupportedMimeTypes();

		// then
		final Set<String> expectedMimeTypes = new ImmutableSet.Builder<String>().addAll(availableMimeTypes).add(TEST_MIME_TYPE)
				.build();
		assertThat(availableMimeTypesAfterAddition).isEqualTo(expectedMimeTypes);
	}

	@Test
	public void removingMimeTypeViaConfigurationServiceInvalidatesCache()
	{
		// given
		testMimePropertySwitcher.switchToValue(TEST_MIME_FILE_EXT);
		final Set<String> availableMimeTypes = mimeService.getSupportedMimeTypes();

		// when
		configurationService.getConfiguration().clearProperty(TEST_MIME_PROPERTY_KEY);

		// then
		final Set<String> availableMimeTypesAfterRemoval = mimeService.getSupportedMimeTypes();
		final Set<String> expectedMimeTypes = Sets.difference(availableMimeTypes, ImmutableSet.of(TEST_MIME_TYPE));
		assertThat(availableMimeTypesAfterRemoval).isEqualTo(expectedMimeTypes);
	}


	@Test
	public void addingPropertyToMasterTenantInvalidatesCache()
	{
		// given
		final Set<String> availableMimeTypesBeforeAddition = mimeService.getSupportedMimeTypes();

		// when
		Registry.getMasterTenant().getConfig().setParameter(TEST_MIME_PROPERTY_KEY, TEST_MIME_FILE_EXT);
		final Set<String> availableMimeTypesAfterAddition = mimeService.getSupportedMimeTypes();

		// then
		final Set<String> expectedMimeTypes = new ImmutableSet.Builder<String>().addAll(availableMimeTypesBeforeAddition)
				.add(TEST_MIME_TYPE).build();
		assertThat(availableMimeTypesAfterAddition).isEqualTo(expectedMimeTypes);
	}

	@Test
	public void removingPropertyFromMasterTenantInvalidatesCache()
	{
		// given
		final Set<String> availableMimeTypesBeforeRemoval = mimeService.getSupportedMimeTypes();

		// when
		Registry.getMasterTenant().getConfig().removeParameter(MASTER_TENANT_CSS_MIME_PROPERTY_KEY);
		final Set<String> availableMimeTypesAfterRemoval = mimeService.getSupportedMimeTypes();

		// then
		final Set<String> expectedMimeTypes = Sets.difference(availableMimeTypesBeforeRemoval, ImmutableSet.of("text/css"));
		assertThat(availableMimeTypesAfterRemoval).isEqualTo(expectedMimeTypes);
	}
}
