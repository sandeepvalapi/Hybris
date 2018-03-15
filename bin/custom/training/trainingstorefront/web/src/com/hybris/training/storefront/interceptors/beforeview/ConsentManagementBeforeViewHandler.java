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
package com.hybris.training.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorstorefrontcommons.consent.data.ConsentCookieData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;


/**
 * This class is responsible to handle cookie consents for anonymous users. <br/>
 * <br/>
 * 1. Creates and synchronizes the 'anonymous-consent' cookie with all latest consent-templates <br/>
 * 2. Populates the model with the {consentTemplatesToDisplay}:List<ConsentTemplateData>, which are used to render in
 * JSP.
 *
 */
public class ConsentManagementBeforeViewHandler implements BeforeViewHandler
{
	private static final Logger LOG = Logger.getLogger(ConsentManagementBeforeViewHandler.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static final String CONSENT_TEMPLATES = "consentTemplatesToDisplay";
	public static final String PREVIOUS_LANGUAGE = "previousConsentLanguage";

	private static final int NEVER_EXPIRES = (int) TimeUnit.DAYS.toSeconds(365); // 365 days for expiring
	public static final String UTF_8 = "UTF-8";

	@Resource(name = "consentFacade")
	private ConsentFacade consentFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		if (!userFacade.isAnonymousUser())
		{
			return;
		}

		checkLanguageChange();

		final Cookie cookie = WebUtils.getCookie(request, WebConstants.ANONYMOUS_CONSENT_COOKIE);

		// Synchronize cookies by filtering old templates and adding new ones
		final List<ConsentCookieData> upToDateCookies = (cookie != null) ? syncCookiesWithSession(cookie)
				: getConsentTemplates().stream().map(this::populateConsentCookieFromTemplate).collect(Collectors.toList());

		// Filter templates to display
		final List<ConsentTemplateData> consentTemplatesToDisplay = (cookie == null) ? getConsentTemplates()
				: filterDisplayTemplates(upToDateCookies);

		// Update client cookie
        updateCookieAndSession(response, upToDateCookies);
		modelAndView.addObject(CONSENT_TEMPLATES, consentTemplatesToDisplay);
	}

	protected List<ConsentTemplateData> filterDisplayTemplates(final List<ConsentCookieData> upToDateCookies)
	{
		return upToDateCookies.stream()
				.filter(data -> StringUtils.isEmpty(data.getConsentState())).map(data -> getConsentTemplates().stream()
						.filter(template -> template.getId().equals(data.getTemplateCode())).findFirst())
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	/**
	 * Erase CONSENT_TEMPLATES from session on language change
	 */
	protected void checkLanguageChange()
	{
		final String currentLang = storeSessionFacade.getCurrentLanguage().getIsocode();
		final String previousLanguage = sessionService.getAttribute(PREVIOUS_LANGUAGE);
		if (StringUtils.isEmpty(previousLanguage) || !currentLang.equals(previousLanguage))
		{
			sessionService.removeAttribute(CONSENT_TEMPLATES);
			sessionService.setAttribute(PREVIOUS_LANGUAGE, currentLang);
		}
	}

	protected List<ConsentCookieData> syncCookiesWithSession(final Cookie anonymousConsentCookie)
	{
		final List<ConsentCookieData> consentCookieDataList;
		try
		{
			consentCookieDataList = new ArrayList(Arrays
					.asList(mapper.readValue(URLDecoder.decode(anonymousConsentCookie.getValue(), UTF_8), ConsentCookieData[].class)));
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("UnsupportedEncodingException occured while decoding the cookie", e);
			return Collections.emptyList();
		}
		catch (final IOException e)
		{
			LOG.error("IOException occured while reading the cookie", e);
			return Collections.emptyList();
		}
		final List<ConsentTemplateData> consentTemplates = getConsentTemplates();

		return updateCookieConsents(consentTemplates, consentCookieDataList);
	}

	protected List<ConsentCookieData> updateCookieConsents(final List<ConsentTemplateData> consentTemplates,
			final List<ConsentCookieData> cookieConsents)
	{
		// Remove stale
		final List<ConsentCookieData> cookieConsentsToRemove = new ArrayList<>();
		for (final ConsentCookieData cookieConsent : cookieConsents)
		{
			final Optional<ConsentTemplateData> templateData = getConsentTemplateById(consentTemplates,
					cookieConsent.getTemplateCode());
			if (!templateData.isPresent() || (templateData.isPresent()
					&& templateData.get().getVersion().equals(Integer.valueOf(cookieConsent.getTemplateVersion())) == false))
			{
				cookieConsentsToRemove.add(cookieConsent);
			}
		}
		cookieConsents.removeAll(cookieConsentsToRemove);

		// Add new
		final List<String> cookieConsentCodes = cookieConsents.stream().map(ConsentCookieData::getTemplateCode)
				.collect(Collectors.toList());
		for (final ConsentTemplateData consentTemplate : consentTemplates)
		{
			if (!cookieConsentCodes.contains(consentTemplate.getId()))
			{
				final ConsentCookieData cookieConsent = populateConsentCookieFromTemplate(consentTemplate);
				cookieConsents.add(cookieConsent);
			}
		}
		return cookieConsents;
	}

	protected Optional<ConsentTemplateData> getConsentTemplateById(final List<ConsentTemplateData> consentTemplates,
			final String id)
	{
		return consentTemplates.stream().filter(template -> id.equals(template.getId())).findFirst();
	}

	protected ConsentCookieData populateConsentCookieFromTemplate(final ConsentTemplateData template)
	{
		final ConsentCookieData consentCookie = new ConsentCookieData();
		consentCookie.setTemplateCode(template.getId());
		consentCookie.setTemplateVersion(template.getVersion().intValue());
		return consentCookie;
	}

	protected List<ConsentTemplateData> getConsentTemplates()
	{
		List<ConsentTemplateData> consentTemplates = sessionService.getAttribute(CONSENT_TEMPLATES);
		if (consentTemplates == null)
		{
			consentTemplates = consentFacade.getConsentTemplatesWithConsents().stream().filter(ConsentTemplateData::isExposed)
					.collect(Collectors.toList());
			sessionService.setAttribute(CONSENT_TEMPLATES, consentTemplates);
		}
		return consentTemplates;
	}

	protected void updateCookieAndSession(final HttpServletResponse response, final List<ConsentCookieData> consentCookies)
	{
		updateCookie(response,consentCookies);
		populateConsentCookiesIntoSession(consentCookies);
	}

	protected void updateCookie(final HttpServletResponse response, final List<ConsentCookieData> consentCookies)
	{
		try
		{
			final String cookieValue = mapper.writeValueAsString(consentCookies);
			final Cookie cookie = new Cookie(WebConstants.ANONYMOUS_CONSENT_COOKIE, URLEncoder.encode(cookieValue, UTF_8));
			cookie.setMaxAge(NEVER_EXPIRES);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("UnsupportedEncodingException occured while writing the cookie to the Servlet Response", e);
		}
		catch (final IOException e)
		{
			LOG.error("IOException occured while writing the cookie to the Servlet Response", e);
		}
	}

	protected void populateConsentCookiesIntoSession(final List<ConsentCookieData> consentCookies)
	{
		final Map<String, String> consentsMap = new HashMap<>();
		for(ConsentCookieData cookieData:consentCookies){
			consentsMap.put(cookieData.getTemplateCode(),cookieData.getConsentState());
		}
		sessionService.setAttribute(WebConstants.USER_CONSENTS, consentsMap);
	}
}
