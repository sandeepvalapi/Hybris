/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.security.captcha.controllers.pages;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * This controller responsible to render captcha widget
 */
@Controller
@RequestMapping(value = "/register/captcha")
public class CaptchaPageController
{

	@RequestMapping(value = "/widget/{widgetName:.*}", method = RequestMethod.GET)
	public String getWidget(@PathVariable("widgetName") final String widgetName, final Model model,
			final HttpServletRequest request)
	{
		return "addon:/captchaaddon/pages/widget/" + widgetName;
	}
}
