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
package de.hybris.platform.licence.sap;

import de.hybris.bootstrap.config.ConfigUtil;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * Property based persistence for testing purposes.
 */
public class PropertyBasedTestPersistence extends DefaultPersistence
{
	private static final String FILE_NAME = ConfigUtil.getPlatformConfig(DefaultPersistence.class).getSystemConfig().getTempDir()
			+ "/testPersistence.properties";

	@Override
	String getPropsFileName()
	{
		return FILE_NAME;
	}

	public void removePersistenceFile()
	{
		final File file = new File(FILE_NAME);
		FileUtils.deleteQuietly(file);
	}
}
