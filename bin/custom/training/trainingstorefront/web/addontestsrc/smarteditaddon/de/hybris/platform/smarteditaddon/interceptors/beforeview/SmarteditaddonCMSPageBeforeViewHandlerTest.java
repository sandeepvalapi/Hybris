/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.smarteditaddon.interceptors.beforeview;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SmarteditaddonCMSPageBeforeViewHandlerTest
{

	private String pageUID = "page_123*4";
	@Mock
	private ResponsiveUtils responsiveUtils;
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

	@InjectMocks
	private SmarteditaddonCmsPageBeforeViewHandler beforeViewHandler;

	@Before
	public void setUp()
	{
		when(modelAndView.getModel()).thenReturn(model);
		when(model.get("cmsPage")).thenReturn(page);
		when(page.getUid()).thenReturn(pageUID);
		when(modelAndView.getModelMap()).thenReturn(modelMap);
	}

	@Test
	public void whenPageBodyCssClassesIsNotSetItWillCreateIt() throws Exception
	{
		when(modelMap.get("pageBodyCssClasses")).thenReturn(null);

		beforeViewHandler.beforeView(request, response, modelAndView);

		verify(modelAndView).addObject("pageBodyCssClasses", "smartedit-page-uid-page_1234 ");
	}

	@Test
	public void whenPageBodyCssClassesIsSetItWillCreateIt() throws Exception
	{
		when(modelMap.get("pageBodyCssClasses")).thenReturn("preset");

		beforeViewHandler.beforeView(request, response, modelAndView);

		verify(modelAndView).addObject("pageBodyCssClasses", "preset smartedit-page-uid-page_1234 ");
	}

}
