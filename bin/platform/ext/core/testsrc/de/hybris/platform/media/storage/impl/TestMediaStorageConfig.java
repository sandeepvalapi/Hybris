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

import de.hybris.platform.media.storage.MediaStorageConfigService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Fixed, tenant unaware test implementation for MediaStorageConfig interface.
 */
public class TestMediaStorageConfig implements MediaStorageConfigService
{
	public static final String GLOBAL_S3_KEY = "s3.globalSettings";
	private Map<String, String> configuration = new HashMap<String, String>();

	public TestMediaStorageConfig()
	{
		configuration = new HashMap<String, String>();
		configuration.put("folder.foo.storage.strategy", "localFileMediaStorageStrategy");
		configuration.put("folder.foo.url.strategy", "localMediaWebURLStrategy");
		configuration.put("folder.foo.hashing.depth", "2");
		configuration.put("folder.s3.storage.strategy", "S3MediaStorageStrategy");
		configuration.put("folder.s3.url.strategy", "S3MediaUrlStrategy");
		configuration.put("folder.s3.accessKeyID", "AKIAJUQJWUAV7A45I7XA");
		configuration.put("folder.s3.secretAccessKey", "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");
		configuration.put(GLOBAL_S3_KEY + "." + "accessKeyID", "AKIAJUQJWUAV7A45I7XA");
	}


	@Override
	public boolean isStorageStrategyConfigured(final String storageId)
	{
		return false;
	}

	@Override
	public Collection<String> getSecuredFolders()
	{
		return Collections.emptySet();
	}

	@Override
	public MediaFolderConfig getConfigForFolder(final String folderQualifier)
	{
		return null;
	}

	@Override
	public String getDefaultStrategyId()
	{
		return null;
	}

	@Override
	public Set<MediaFolderConfig> getFolderConfigsForStrategy(final String strategyId)
	{
		return null;
	}


	@Override
	public GlobalMediaStorageConfig getGlobalSettingsForStrategy(final String strategyId)
	{
		return null;
	}


	@Override
	public String getDefaultCacheFolderName()
	{
		return null;
	}
}
