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
package de.hybris.platform.commons.renderer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static java.lang.String.format;

import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.Renderer;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.commons.renderer.daos.RendererTemplateDao;
import de.hybris.platform.commons.renderer.exceptions.RendererNotFoundException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;



/**
 * Default implementation of {@link RendererService}
 */
public class DefaultRendererService extends AbstractBusinessService implements RendererService
{
	private Map<RendererTypeEnum, Renderer> mapping;
	private RendererTemplateDao rendererTemplateDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(final Renderer renderer, final RendererTemplateModel template, final Object context, final Writer output)
	{
		renderer.render(template, context, output);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RendererTemplateModel getRendererTemplateForCode(final String code)
	{
		final List<RendererTemplateModel> templates = rendererTemplateDao.findRendererTemplatesByCode(code);

		validateIfSingleResult(templates, format("RendererTemplate with code '%s' not found!", code), format(
				"Code '%s' is not unique, %d templates found!", code, Integer.valueOf(templates.size())));

		return templates.iterator().next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(final RendererTemplateModel template, final Object context, final Writer output)
	{
		if (!mapping.containsKey(template.getRendererType()))
		{
			throw new RendererNotFoundException(String.format("Renderer for type %s has not been found", template.getRendererType()));
		}

		final Renderer renderer = mapping.get(template.getRendererType());
		render(renderer, template, context, output);
	}

	@Required
	public void setRendererTemplateDao(final RendererTemplateDao rendererTemplateDao)
	{
		this.rendererTemplateDao = rendererTemplateDao;
	}

	@Required
	public void setMapping(final Map<RendererTypeEnum, Renderer> mapping)
	{
		this.mapping = mapping;
	}
}
