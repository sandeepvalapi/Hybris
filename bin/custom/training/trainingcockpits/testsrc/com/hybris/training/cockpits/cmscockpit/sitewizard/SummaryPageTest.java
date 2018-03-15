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
package com.hybris.training.cockpits.cmscockpit.sitewizard;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

/**
 * Test class for {@link SummaryPage}
 */
@UnitTest
public class SummaryPageTest
{
	private static final String TEST_LABEL = "testLabelValue";
	private static final String TEST_SITE_NAME = "test site name";
	private static final String TEST_CONTENT_CATALOG_NAME = "test content catalog name";
	private SummaryPage summaryPage;
	private Map<String, Object> information;

	@Before
	public void setUp()
	{
		summaryPage = Mockito.mock(SummaryPage.class, Mockito.CALLS_REAL_METHODS);
		Mockito.doReturn(false).when(summaryPage).isCurrentSessionUsingTestIDs();
		information = new HashMap<>();
		information.put("siteName", TEST_SITE_NAME);
		information.put("contentcatalogname", TEST_CONTENT_CATALOG_NAME);

	}

	@Test
	public void createSummaryEntryRowTest()
	{
		final Component button = new Button();
		final Hbox testRowWithNonNullElements = (Hbox) summaryPage.createSummaryEntryRow(TEST_LABEL, button);
		Assert.assertNotNull(testRowWithNonNullElements);
		Assert.assertEquals("45%,55%", testRowWithNonNullElements.getWidths());
		Assert.assertEquals("summaryRow", testRowWithNonNullElements.getSclass());
		Assert.assertEquals(TEST_LABEL, ((Label) testRowWithNonNullElements.getFirstChild()).getValue());
		Assert.assertEquals(button, testRowWithNonNullElements.getLastChild());
	}

	@Test
	public void createNameRowTest()
	{
		final Vbox contextInformation = new Vbox();
		summaryPage.createNameRow(information, contextInformation);
		Assert.assertEquals(TEST_SITE_NAME, ((Textbox) contextInformation.getFirstChild().getLastChild()).getValue());
		Assert.assertEquals(true, ((Textbox) contextInformation.getFirstChild().getLastChild()).isReadonly());
	}

	@Test
	public void createSiteActiveRowTest()
	{
		final Vbox contextInformation = new Vbox();
		information.put("active", true);
		summaryPage.createSiteActiveRow(information, contextInformation);
		Assert.assertEquals("/cockpit/images/bool_true.gif", ((Image) contextInformation.getLastChild()
				.getLastChild()).getSrc());

		information.put("active", false);
		summaryPage.createSiteActiveRow(information, contextInformation);
		Assert.assertEquals("/cockpit/images/bool_false.gif", ((Image) contextInformation.getLastChild()
				.getLastChild()).getSrc());

		information.put("active", null);
		summaryPage.createSiteActiveRow(information, contextInformation);
		Assert.assertEquals("/cockpit/images/bool_null.gif", ((Image) contextInformation.getLastChild()
				.getLastChild()).getSrc());
	}

	@Test
	public void createContentCatalogRowTest()
	{
		final Vbox contextInformation = new Vbox();
		summaryPage.createContentCatalogRow(information, contextInformation, new SummaryPage.ListboxRenderer());
		Assert.assertEquals(TEST_CONTENT_CATALOG_NAME, ((Textbox) contextInformation.getFirstChild()
				.getLastChild()).getValue());
		Assert.assertEquals(true, ((Textbox) contextInformation.getFirstChild().getLastChild()).isReadonly());
	}
}
