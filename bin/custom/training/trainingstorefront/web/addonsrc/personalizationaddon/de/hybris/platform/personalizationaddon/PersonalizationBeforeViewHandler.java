/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationaddon;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.personalizationaddon.properties.CxScriptPropertiesSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;


public class PersonalizationBeforeViewHandler implements BeforeViewHandler
{
	private static final Logger LOG = LoggerFactory.getLogger(PersonalizationBeforeViewHandler.class);

	public static final String PERSONALIZATION_ACTIONS = "personalizationActionList";
	public static final String PERSONALIZATION_SEGMENTS = "personalizationSegmentList";

	private CxScriptPropertiesSupplier propertiesSupplier;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		try
		{
			modelAndView.addObject(PERSONALIZATION_ACTIONS, getPropertiesSupplier().getEncodedActionResults());
			modelAndView.addObject(PERSONALIZATION_SEGMENTS, getPropertiesSupplier().getEncodedSegmentData());
		}
		catch (final RuntimeException e)
		{
			LOG.debug("Adding personalization data to the page has failed", e);
		}
	}

	public void setPropertiesSupplier(final CxScriptPropertiesSupplier propertiesSupplier)
	{
		this.propertiesSupplier = propertiesSupplier;
	}

	protected CxScriptPropertiesSupplier getPropertiesSupplier()
	{
		return propertiesSupplier;
	}
}
