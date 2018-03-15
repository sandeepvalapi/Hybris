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
package de.hybris.platform.hac.facade.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.hac.data.dto.ExportDataResult;
import de.hybris.platform.hac.data.dto.ImportDataResult;
import de.hybris.platform.hac.facade.ImpexFacade;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ExportConfig;
import de.hybris.platform.servicelayer.impex.ExportResult;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.media.MediaService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
public class DefaultImpexFacadeTest
{
	private static final String MEDIA_NAME = "FooBar";
	private static final String MEDIA_PATH = "foo/bar/baz.zip";
	@InjectMocks
	private final ImpexFacade facade = new DefaultImpexFacade();
	@Mock
	private ImportConfig importConfig;
	@Mock
	private ImportService importService;
	@Mock
	private ImportResult importResult;
	@Mock
	private MediaService mediaService;
	@Mock
	private ImpExMediaModel impexMedia;
	@Mock
	private ExportResult exportResult;
	@Mock
	private ExportConfig exportConfig;
	@Mock
	private ExportService exportService;

	private final byte[] bytes = new byte[]
	{ (byte) 100 >>> 24, (byte) 100 >>> 16, (byte) 100 >>> 8, };


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultImpexFacade#importData(de.hybris.platform.servicelayer.impex.ImportConfig)}
	 * .
	 */
	@Test
	public void shouldCreateImportDataResultWithSuccessWhenImportWasSuccessfullyFinished()
	{
		// given
		given(importService.importData(importConfig)).willReturn(importResult);
		given(Boolean.valueOf(importResult.isSuccessful())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(importResult.hasUnresolvedLines())).willReturn(Boolean.FALSE);

		// when
		final ImportDataResult importDataResult = facade.importData(importConfig);

		// then
		assertThat(importDataResult).isNotNull();
		assertThat(importDataResult.isSuccesss()).isTrue();
		assertThat(importDataResult.isUnresolvedLines()).isFalse();
		assertThat(importDataResult.getUnresolvedData()).isEmpty();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultImpexFacade#importData(de.hybris.platform.servicelayer.impex.ImportConfig)}
	 * .
	 */
	@Test
	public void shouldCreateImportDataResultWithUnSuccessWhenImportWasNotSuccessfullyFinished()
	{
		// given
		given(importService.importData(importConfig)).willReturn(importResult);
		given(Boolean.valueOf(importResult.isSuccessful())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(importResult.hasUnresolvedLines())).willReturn(Boolean.TRUE);
		given(importResult.getUnresolvedLines()).willReturn(impexMedia);
		given(mediaService.getDataFromMedia(impexMedia)).willReturn(bytes);

		// when
		final ImportDataResult importDataResult = facade.importData(importConfig);

		// then
		assertThat(importDataResult).isNotNull();
		assertThat(importDataResult.isSuccesss()).isFalse();
		assertThat(importDataResult.isUnresolvedLines()).isTrue();
		assertThat(importDataResult.getUnresolvedData()).isNotNull();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultImpexFacade#exportData(de.hybris.platform.servicelayer.impex.ExportConfig)}
	 * .
	 */
	@Test
	public void shouldCreateExportDataResultWithSuccessWhenExportWasSuccessfullyFinished()
	{
		// given
		given(exportService.exportData(exportConfig)).willReturn(exportResult);
		given(Boolean.valueOf(exportResult.isSuccessful())).willReturn(Boolean.TRUE);
		given(exportResult.getExportedData()).willReturn(impexMedia);
		given(impexMedia.getRealFileName()).willReturn(MEDIA_NAME);
		given(impexMedia.getDownloadURL()).willReturn(MEDIA_PATH);

		// when
		final ExportDataResult exportDataResult = facade.exportData(exportConfig);

		// then
		assertThat(exportDataResult).isNotNull();
		assertThat(exportDataResult.isSuccess()).isTrue();
		assertThat(exportDataResult.getExportDataName()).isEqualTo(MEDIA_NAME);
		assertThat(exportDataResult.getDownloadUrl()).isEqualTo(MEDIA_PATH);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.hac.facade.impl.DefaultImpexFacade#exportData(de.hybris.platform.servicelayer.impex.ExportConfig)}
	 * .
	 */
	@Test
	public void shouldCreateExportDataResultWithUnSuccessWhenExportWasNotSuccessfullyFinished()
	{
		// given
		given(exportService.exportData(exportConfig)).willReturn(exportResult);
		given(Boolean.valueOf(exportResult.isSuccessful())).willReturn(Boolean.FALSE);
		given(exportResult.getExportedData()).willReturn(null);

		// when
		final ExportDataResult exportDataResult = facade.exportData(exportConfig);

		// then
		assertThat(exportDataResult).isNotNull();
		assertThat(exportDataResult.isSuccess()).isFalse();
		assertThat(exportDataResult.getExportDataName()).isEqualTo("");
		assertThat(exportDataResult.getDownloadUrl()).isEqualTo("");
	}
}
