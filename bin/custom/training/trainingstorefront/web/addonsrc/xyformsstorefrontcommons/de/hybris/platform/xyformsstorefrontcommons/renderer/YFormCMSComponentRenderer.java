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
package de.hybris.platform.xyformsstorefrontcommons.renderer;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.model.component.YFormCMSComponentModel;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;


/**
 * Dynamically renders the component's contents that were retrieved from the form integration service call instead of a
 * jsp file.
 */
public class YFormCMSComponentRenderer extends DefaultAddOnCMSComponentRenderer<YFormCMSComponentModel>
{
	private static final Logger LOG = Logger.getLogger(YFormCMSComponentRenderer.class);

	@Resource
	private YFormFacade yformFacade;

	@Override
	public void renderComponent(final PageContext pageContext, final YFormCMSComponentModel component)
			throws ServletException, IOException
	{
		try
		{
			final JspWriter out = pageContext.getOut();

			final String applicationId = component.getApplicationId();
			final String formId = component.getFormId();
			final YFormDataActionEnum action = component.getAction();
			final String formDataId = component.getFormDataId();

			// ** This is a simply CMS Component **
			// The default strategy does not apply any transformation to content nor creates NEW records with
			// a given formDataId.
			final String content = yformFacade.getInlineFormHtml(applicationId, formId, action, formDataId);
			out.write(content);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}
}
