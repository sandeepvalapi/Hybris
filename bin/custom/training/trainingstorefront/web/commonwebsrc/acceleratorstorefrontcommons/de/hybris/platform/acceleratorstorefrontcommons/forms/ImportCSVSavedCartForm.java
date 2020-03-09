/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;

import org.springframework.web.multipart.MultipartFile;


public class ImportCSVSavedCartForm
{
	private MultipartFile csvFile;

	public MultipartFile getCsvFile()
	{
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile)
	{
		this.csvFile = csvFile;
	}
}
