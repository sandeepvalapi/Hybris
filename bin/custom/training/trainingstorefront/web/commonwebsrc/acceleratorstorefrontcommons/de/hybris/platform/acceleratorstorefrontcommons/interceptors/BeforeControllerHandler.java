/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;


/**
 */
public interface BeforeControllerHandler
{
	/**
	 * Called before the DispatcherServlet calls the controller.
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return <code>true</code> if the execution chain should proceed with the
	 * next interceptor or the handler itself. Else, DispatcherServlet assumes
	 * that this interceptor has already dealt with the response itself.
	 * @throws Exception in case of errors
	 */
	boolean beforeController(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception;// NOSONAR
}
