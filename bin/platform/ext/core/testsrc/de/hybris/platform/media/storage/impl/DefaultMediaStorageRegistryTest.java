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
package de.hybris.platform.media.storage.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.url.MediaURLStrategy;
import de.hybris.platform.media.url.impl.LocalMediaWebURLStrategy;
import de.hybris.platform.testframework.TestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultMediaStorageRegistryTest
{
	private final DefaultMediaStorageRegistry registry = new DefaultMediaStorageRegistry();

	@Mock
	private LocalFileMediaStorageStrategy localFileStrategy;
	@Mock
	private LocalMediaWebURLStrategy localUrlStrategy;
	@Mock
	private MediaFolderConfig folderConfig;
	private Set<String> urlStrategyIds;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		urlStrategyIds = new HashSet<String>();
		urlStrategyIds.add("localMediaWebURLStrategy");

		final Map<String, MediaStorageStrategy> storageStrategies = new HashMap<String, MediaStorageStrategy>();
		storageStrategies.put("localFileMediaStorageStrategy", localFileStrategy);
		registry.setStorageStrategies(storageStrategies);

		final Map<String, MediaURLStrategy> urlStrategies = new HashMap<String, MediaURLStrategy>();
		urlStrategies.put("localMediaWebURLStrategy", localUrlStrategy);
		registry.setURLStrategies(urlStrategies);
	}

	@Test
	public void shouldReturnLocalFileMediaStorageStrategyForFolderNamedFileFolder()
	{
		// given
		given(folderConfig.getStorageStrategyId()).willReturn("localFileMediaStorageStrategy");

		// when
		final MediaStorageStrategy storageStrategy = registry.getStorageStrategyForFolder(folderConfig);

		// then
		assertThat(storageStrategy).isNotNull().isInstanceOf(LocalFileMediaStorageStrategy.class);
	}


	@Test
	public void shouldThrowIllegalStateExceptionWhenNoStorageStrategyHasBeenFoundInRegistry()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		given(folderConfig.getStorageStrategyId()).willReturn(null);

		try
		{
			// when
			registry.getStorageStrategyForFolder(folderConfig);
			fail("Should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then
			assertThat(e).hasMessage("No suitable media storage strategy found.");
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldReturnLocalFileMediaStorageStrategyForFolderWhichDoesNotHaveConfiguration()
	{
		// given
		given(folderConfig.getStorageStrategyId()).willReturn("localFileMediaStorageStrategy");

		// when
		final MediaStorageStrategy storageStrategy = registry.getStorageStrategyForFolder(folderConfig);

		// then
		assertThat(storageStrategy).isNotNull().isInstanceOf(LocalFileMediaStorageStrategy.class);
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenAskingForStorageStrategyWithoutNullFolderConfig()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			registry.getStorageStrategyForFolder(folderConfig);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Folder config is required to perform this operation");
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenNoURLStrategyHasBeenFoundInRegistry()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		given(folderConfig.getURLStrategyIds()).willReturn(Collections.EMPTY_SET);

		try
		{
			// when
			registry.getURLStrategyForFolder(folderConfig, null);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalStateException e)
		{
			// then
			assertThat(e).hasMessage("No suitable media URL strategy found.");
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldReturnLocalUrlStrategyForFolderWhenThereIsNoConfiguredPreferredUrlStrategy()
	{
		// given
		given(folderConfig.getURLStrategyIds()).willReturn(urlStrategyIds);

		// when
		final MediaURLStrategy urlStraegy = registry.getURLStrategyForFolder(folderConfig, null);

		// then
		assertThat(urlStraegy).isNotNull().isInstanceOf(LocalMediaWebURLStrategy.class);
	}

	@Test
	public void shouldReturnLocalUrlStrategyForFolderWhichDoesNotHaveConfiguration()
	{
		// given
		given(folderConfig.getURLStrategyIds()).willReturn(urlStrategyIds);

		// when
		final MediaURLStrategy urlStraegy = registry.getURLStrategyForFolder(folderConfig, null);

		// then
		assertThat(urlStraegy).isNotNull().isInstanceOf(LocalMediaWebURLStrategy.class);
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenAskingForUrlStrategyWithoutFolderConfig()
	{
		// given
		TestUtils.disableFileAnalyzer("error expected");
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			registry.getURLStrategyForFolder(folderConfig, null);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Folder config is required to perform this operation");
		}
		TestUtils.enableFileAnalyzer();
	}
}
