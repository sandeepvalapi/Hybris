/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.interceptors.beforeview;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Optional;

import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SmarteditaddonCMSPageBeforeViewHandlerTest
{

	private final String PAGE_UID_INPUT = "page_123*4";
	private final String PAGE_UID_EXPECTED = "page_123-4";
	private final String PAGE_UUID = "PUUID";
	private final String CATALOG_VERSION_UUID = "CUUI";

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ModelAndView modelAndView;
	@Mock
	private ModelMap modelMap;
	@Mock
	private AbstractPageModel page;
	@Mock
	private Map<String, Object> model;
	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Mock
	private ItemData catalogData;
	@Mock
	private ItemData pageData;
	@Mock
	private CatalogVersionModel catalogVersionData;

	@InjectMocks
	private SmarteditaddonCmsPageBeforeViewHandler beforeViewHandler;


	@Before
	public void setUp()
	{
		when(modelAndView.getModel()).thenReturn(model);
		when(modelAndView.getModelMap()).thenReturn(modelMap);

		when(model.get("cmsPage")).thenReturn(page);

		when(page.getUid()).thenReturn(PAGE_UID_INPUT);
		when(page.getCatalogVersion()).thenReturn(catalogVersionData);

		when(uniqueItemIdentifierService.getItemData(page)).thenReturn(Optional.of(pageData));
		when(uniqueItemIdentifierService.getItemData(catalogVersionData)).thenReturn(Optional.of(catalogData));

		when(pageData.getItemId()).thenReturn(PAGE_UUID);
		when(catalogData.getItemId()).thenReturn(CATALOG_VERSION_UUID);
	}

	@Test
	public void whenPageBodyCssClassesIsNotSetItWillCreateIt() throws Exception
	{
		when(modelMap.get("pageBodyCssClasses")).thenReturn(null);

		beforeViewHandler.beforeView(request, response, modelAndView);

		final String expectedMV =
			SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS + PAGE_UID_EXPECTED + " " +
			SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS + PAGE_UUID + " " +
			SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS + CATALOG_VERSION_UUID + " ";

		verify(modelAndView).addObject("pageBodyCssClasses", expectedMV);
	}

	@Test
	public void whenPageBodyCssClassesIsSetItWillCreateIt() throws Exception
	{
		final String preset = "preset";

		when(modelMap.get("pageBodyCssClasses")).thenReturn(preset);

		beforeViewHandler.beforeView(request, response, modelAndView);

		final String expectedMV = preset + " " +
			SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS + PAGE_UID_EXPECTED + " " +
			SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS + PAGE_UUID + " " +
			SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS + CATALOG_VERSION_UUID + " ";

		verify(modelAndView).addObject("pageBodyCssClasses", expectedMV);
	}

}
