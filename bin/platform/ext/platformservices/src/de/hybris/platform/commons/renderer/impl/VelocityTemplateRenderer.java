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

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.Renderer;
import de.hybris.platform.commons.renderer.exceptions.RendererException;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Required;


/**
 * Renders velocity template. Rendered content is output to Writer specified as 3rd parameter passed to render method.
 * 
 * Context object is registered as 'contextName' property in Spring XML file. Context object stores properties as
 * key-value map. So if you set 'contextName' as 'ctx' and in context you place key1=value1 you refer to it as
 * $ctx.key1.
 */
public class VelocityTemplateRenderer implements Renderer
{
	private MediaService mediaService;
	private String contextName;

	@Override
	public void render(final RendererTemplateModel template, final Object context, final Writer output)
	{
		Class clazz = null;

		try
		{
			clazz = Class.forName(template.getContextClass());
		}
		catch (final ClassNotFoundException e)
		{
			throw new RendererException("Cannot find class: " + template.getContextClass(), e);
		}

		InputStream inputStream = null;
		try
		{
			if ((context != null) && (!clazz.isAssignableFrom(context.getClass())))
			{
				throw new RendererException("The context class [" + context.getClass().getName() + "] is not correctly defined.");
			}
			final MediaModel content = template.getContent();
			if (content == null)
			{
				throw new RendererException("No content found for template " + template.getCode());
			}

			inputStream = mediaService.getStreamFromMedia(content);

			writeToOutput(output, inputStream, context);
		}
		catch (final IOException e)
		{
			throw new RendererException("Problem during rendering", e);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void writeToOutput(final Writer result, final InputStream inputStream, final Object context) throws IOException
	{
		final VelocityContext ctx = new VelocityContext();
		ctx.put(contextName, context);

		final Reader reader = new InputStreamReader(inputStream, "UTF-8");

		try
		{
			evaluate(result, ctx, reader);
			result.flush();
		}
		catch (final Exception e)
		{
			throw new RendererException("Problem with get velocity stream", e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	protected void evaluate(final Writer result, final VelocityContext ctx, final Reader reader) throws IOException
	{
		Velocity.evaluate(ctx, result, getClass().getName(), reader);
	}

	/**
	 * @param mediaService
	 *           the mediaService to set
	 */
	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	/**
	 * @param contextName
	 *           the contextName to set
	 */
	@Required
	public void setContextName(final String contextName)
	{
		this.contextName = contextName;
	}

}
