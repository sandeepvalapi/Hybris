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
package de.hybris.platform.commons.renderer;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;

import java.io.Writer;


/**
 * Service to gather renderer templates and render them.
 * 
 * @spring.bean rendererService
 */
public interface RendererService
{
	/**
	 * Renders template using renderer passed as a parameter. Rendered content is stored into output.
	 * 
	 * @param renderer
	 *           renderer used to render template
	 * 
	 * @param template
	 *           to render
	 * 
	 * @param context
	 *           context (usually map of properties)
	 * 
	 * @param output
	 *           rendered content
	 */
	void render(Renderer renderer, RendererTemplateModel template, Object context, Writer output);

	/**
	 * Renders template using default renderer. Rendered content is stored into output.
	 * 
	 * @param template
	 *           to render
	 * 
	 * @param context
	 *           context (usually map of properties)
	 * 
	 * @param output
	 *           rendered content
	 */
	void render(RendererTemplateModel template, Object context, Writer output);

	/**
	 * Gets RendererTemplate that match a code
	 * 
	 * @param code
	 *           template identifier
	 * 
	 * @return found template
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no RendererTemplate with the specified code is found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one RendererTemplate with the specified code is found
	 */
	RendererTemplateModel getRendererTemplateForCode(String code);
}
