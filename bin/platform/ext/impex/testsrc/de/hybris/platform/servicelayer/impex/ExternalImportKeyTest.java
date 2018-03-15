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
package de.hybris.platform.servicelayer.impex;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.model.ExternalImportKeyModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.CSVConstants;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * ExternalImportKey test
 */
public class ExternalImportKeyTest extends ServicelayerTest
{
	private static final String SYSTEM_ID = "12345";
    private static final String TEST_CODE_0 = "testCode0";
    private static final String TEST_CODE_1 = "testCode1";
	private static final String TEST_VALUE_0 = "testValue";
	private static final String TEST_VALUE_1 = "someValue";
	private static final String TEST_VALUE_2 = "someOtherValue";
	private static final String INSERT_UPDATE_TITLE = "INSERT_UPDATE Title;code[unique=true];name\n;";
	private static final String INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR = "INSERT_UPDATE Title;PK[cellDecorator=de.hybris.platform.impex.jalo.header.ExternalImportKeyCellDecorator,sourceSystemId="
			+ SYSTEM_ID + "];code[unique=true];name\n;";
	private static final String INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR_FAIL_1ST_PASS = "INSERT_UPDATE Title[processor=de.hybris.platform.servicelayer.impex.TestImportProcessor];PK[cellDecorator=de.hybris.platform.impex.jalo.header.ExternalImportKeyCellDecorator,sourceSystemId="
			+ SYSTEM_ID + "];code[unique=true];name\n;";

	@Resource
	public FlexibleSearchService flexibleSearchService;

	@Resource
	public ImportService importService;

	@Test
	public void testExternalImportKey()
	{
		testExternalImportKey(INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR, false);
	}

	@Test
	public void testExternalImportKeyFail1stPass()
	{
		testExternalImportKey(INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR_FAIL_1ST_PASS, false);
	}

	@Test
	public void testExternalImportKeyInMultithreadedMode()
	{
		testExternalImportKey(INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR, true);
	}

	public void testExternalImportKey(final String header, final boolean multithreaded)
	{
		// insert title model
		String data = INSERT_UPDATE_TITLE + TEST_CODE_0 + ";" + TEST_VALUE_0;
		ImportResult importResult = getImportResult(data, multithreaded);
		assertTrue("import was not successful", importResult.isSuccessful());

		// verify title model
		SearchResult<TitleModel> titleModelSearchResult = getSearchResult("select {PK} from {Title}", TitleModel.NAME, TEST_VALUE_0);
		TitleModel titleModel = verifySearchResult(titleModelSearchResult, TEST_VALUE_0);

		// store pk to use as a remote sync key
		final String remoteSyncKey = titleModel.getPk().getLongValueAsString();
		final PK remoteSyncPK = titleModel.getPk();

		// now impex should search ExternalImportKeys for remoteSyncKey to real PK mapping
		// if not found impex should create new Item instead of updating existing one
		data = INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR + remoteSyncKey + ";" + TEST_CODE_1 + ";" + TEST_VALUE_1 +"\n";
		if (multithreaded)
		{
			for (int i = 0; i < 1000; i++)
			{
				data += INSERT_UPDATE_TITLE_WITH_CELL_DECORATOR + remoteSyncKey + ";" + TEST_CODE_1 + ";" + TEST_VALUE_1 + "\n";
			}
		}
		importResult = getImportResult(data, multithreaded);
		assertTrue("import was not successful", importResult.isSuccessful());

		// verify that original title model was not changed
		titleModelSearchResult = getSearchResult("select {PK} from {Title}", TitleModel.PK, remoteSyncPK);
		verifySearchResult(titleModelSearchResult, TEST_VALUE_0);

		// verify that new Item was created
		titleModelSearchResult = getSearchResult("select {PK} from {Title}", TitleModel.NAME, TEST_VALUE_1);
		titleModel = verifySearchResult(titleModelSearchResult, TEST_VALUE_1);

		// verify that correct ExternalImportKey was created
		verifyExternalImportKey(titleModel, remoteSyncKey);

		// insert_update Title again, now with changed name
		// no ExternalImportKey should be created, existing title model should be updated
		data = header + remoteSyncKey + ";" + TEST_CODE_0 + ";" + TEST_VALUE_2;
		importResult = getImportResult(data, multithreaded);
		assertTrue("import was not successful", importResult.isSuccessful());

		// verify that only one ExternalImportKey exists
		verifyExternalImportKey(titleModel, remoteSyncKey);

		// verify that title model was updated
		titleModelSearchResult = getSearchResult("select {PK} from {Title}", TitleModel.PK, titleModel.getPk());
		verifySearchResult(titleModelSearchResult, TEST_VALUE_2);

		// verify that the first title model was not updated
		titleModelSearchResult = getSearchResult("select {PK} from {Title}", TitleModel.PK, remoteSyncPK);
		verifySearchResult(titleModelSearchResult, TEST_VALUE_0);
	}

	private TitleModel verifySearchResult(final SearchResult<TitleModel> titleModelSearchResult, final String name)
	{
		final List<TitleModel> titleModels;
		final TitleModel titleModel;
		assertNotNull(titleModelSearchResult);
		titleModels = titleModelSearchResult.getResult();
		assertEquals("invalid models count", 1, titleModels.size());
		titleModel = titleModels.get(0);
		assertNotNull("model is null", titleModel);
		assertEquals("invalid name", name, titleModel.getName());
		return titleModel;
	}

	private <T> SearchResult<T> getSearchResult(final String query, final String paramName, final Object paramValue)
	{
		final FlexibleSearchQuery fsq;
		final SearchResult<T> searchResult;
		String realQuery = query;
		if (paramName != null)
		{
			realQuery += " where {" + paramName + "}=?" + paramName;
			fsq = new FlexibleSearchQuery(realQuery);
			fsq.addQueryParameter(paramName, paramValue);
		}
		else
		{
			fsq = new FlexibleSearchQuery(realQuery);
		}
		searchResult = flexibleSearchService.search(fsq);
		return searchResult;
	}

	private ImportResult getImportResult(final String data, final boolean multithreaded)
	{
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setMaxThreads(multithreaded ? -1 : 1);
		final ImpExResource res = new StreamBasedImpExResource(new ByteArrayInputStream(data.getBytes()),
				CSVConstants.HYBRIS_ENCODING);
		importConfig.setScript(res);
		return importService.importData(importConfig);
	}

	private void verifyExternalImportKey(final TitleModel titleModel, final String remoteSyncKey)
	{
		final SearchResult<ExternalImportKeyModel> lteModelSearchResult;
		final List<ExternalImportKeyModel> lteModels;
		final ExternalImportKeyModel lte;
		lteModelSearchResult = getSearchResult("select {PK} from {ExternalImportKey}", null, null);
		assertEquals("invalid models count", 1, lteModelSearchResult.getCount());
		lteModels = lteModelSearchResult.getResult();
		lte = lteModels.get(0);
		assertNotNull("model is null", lte);
		assertEquals("invalid source key", remoteSyncKey, lte.getSourceKey());
		assertEquals("invalid target pk", titleModel.getPk(), lte.getTargetPK());
		assertEquals("invalid source system id", SYSTEM_ID, lte.getSourceSystemID());
	}




}
