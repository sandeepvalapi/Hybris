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


import static de.hybris.platform.testframework.Assert.assertEquals;
import static com.hybris.training.storefront.interceptors.beforeview.ConsentManagementBeforeViewHandler.CONSENT_TEMPLATES;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.consent.data.ConsentCookieData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentData;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;


@UnitTest
public class ConsentManagementBeforeViewHandlerTest
{

	private static final ObjectMapper mapper = new ObjectMapper();

	public static final String PREVIOUS_LANGUAGE_ISO = "de";
	public static final String UTF_8 = "UTF-8";
	public static final String TEMPLATE_CODE_GIVEN = "templateCodeGiven";
	public static final String TEMPLATE_CODE_WITHDRAWN = "templateCodeWithdrawn";
	public static final String CONSENT_GIVEN = "given";
	public static final String CONSENT_WITHDRAWN = "withdrawn";
	private static List<ConsentCookieData> consentData;
	private static List<ConsentCookieData> consentDataNoState;
	private static String consentCookieValue;
	private static String consentCookieValueNoState;
	private static List<ConsentTemplateData> consentTemplateDataNoState;
	private static List<ConsentTemplateData> consentTemplateData;

	@InjectMocks
	private ConsentManagementBeforeViewHandler consentManagementBeforeViewHandler;

	@Mock
	private ConsentFacade consentFacade;
	@Mock
	private SessionService sessionService;
	@Mock
	private StoreSessionFacade storeSessionFacade;
	@Mock
	private UserFacade userFacade;
	@Mock
	private HttpServletRequest request;
	@Spy
	private final HttpServletResponse response = new MockHttpServletResponse();
	@Spy
	private final ModelAndView modelAndView = new ModelAndView();

	private ConsentTemplateData givenTemplate;


	@Before
	public void setUp() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		// consent cookie data setup
		final ConsentCookieData given = new ConsentCookieData();
		given.setConsentState(WebConstants.CONSENT_GIVEN);
		given.setTemplateCode(TEMPLATE_CODE_GIVEN);
		given.setTemplateVersion(1);

		final ConsentCookieData withdrawn = new ConsentCookieData();
		withdrawn.setConsentState(WebConstants.CONSENT_WITHDRAWN);
		withdrawn.setTemplateCode(TEMPLATE_CODE_WITHDRAWN);
		withdrawn.setTemplateVersion(1);
		consentData = Arrays.asList(given, withdrawn);

		final ConsentCookieData empty = new ConsentCookieData();
		empty.setConsentState(null);
		empty.setTemplateCode("empty");
		empty.setTemplateVersion(2);
		consentDataNoState = Arrays.asList(empty);

		final LanguageData currentLang = new LanguageData();
		currentLang.setIsocode("en");

		// consent template data setup
		givenTemplate = new ConsentTemplateData();
		final ConsentData givenConsent = new ConsentData();
		givenConsent.setCode("given");
		givenConsent.setConsentGivenDate(new Date());
		givenTemplate.setConsentData(givenConsent);
		givenTemplate.setVersion(Integer.valueOf(1));
		givenTemplate.setDescription("given");
		givenTemplate.setExposed(true);
		givenTemplate.setId(TEMPLATE_CODE_GIVEN);

		final ConsentTemplateData withdrawnTemplate = new ConsentTemplateData();
		final ConsentData withdrawnConsent = new ConsentData();
		withdrawnConsent.setCode("withdrawn");
		withdrawnConsent.setConsentGivenDate(new Date());
		withdrawnConsent.setConsentWithdrawnDate(new Date());
		withdrawnTemplate.setConsentData(withdrawnConsent);
		withdrawnTemplate.setVersion(Integer.valueOf(1));
		withdrawnTemplate.setDescription("withdrawn");
		withdrawnTemplate.setExposed(true);
		withdrawnTemplate.setId(TEMPLATE_CODE_WITHDRAWN);

		consentTemplateData = Arrays.asList(givenTemplate, withdrawnTemplate);

		final ConsentTemplateData emptyTemplate = new ConsentTemplateData();
		final ConsentData emptyConsent = new ConsentData();
		emptyConsent.setCode("empty");
		emptyConsent.setConsentGivenDate(null);
		emptyTemplate.setConsentData(emptyConsent);
		emptyTemplate.setVersion(Integer.valueOf(1));
		emptyTemplate.setDescription("empty");
		emptyTemplate.setExposed(true);
		emptyTemplate.setId("empty");
		consentTemplateDataNoState = Arrays.asList(emptyTemplate);

		consentCookieValue = mapper.writeValueAsString(consentData);
		consentCookieValueNoState = mapper.writeValueAsString(consentDataNoState);

		// other methods
		when(Boolean.valueOf(userFacade.isAnonymousUser())).thenReturn(Boolean.TRUE);
		when(sessionService.getAttribute(ConsentManagementBeforeViewHandler.CONSENT_TEMPLATES)).thenReturn(consentTemplateData);
		when(sessionService.getAttribute(ConsentManagementBeforeViewHandler.PREVIOUS_LANGUAGE)).thenReturn(PREVIOUS_LANGUAGE_ISO);
		when(storeSessionFacade.getCurrentLanguage()).thenReturn(currentLang);
		when(consentManagementBeforeViewHandler.getConsentTemplates()).thenReturn(consentTemplateData);
	}

	@Test
	public void shouldCreateCookiesIfTheyAreNotExist() throws Exception
	{
		Mockito.when(request.getCookies()).thenReturn(null);

		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		// verify that method was called at least
		Mockito.verify(response).addCookie(any());

		// verify consentTemplateData was added to model
		Mockito.verify(modelAndView).addObject(CONSENT_TEMPLATES, consentTemplateData);
		// verify consentTemplateData was added to model and eq to test data
		assertTrue(modelAndView.getModelMap().get(CONSENT_TEMPLATES).equals(consentTemplateData));
	}

	@Test
	public void shouldNotPopulateTheModelIfConsentsHasState() throws Exception
	{
		final Cookie[] cookie = new Cookie[1];
		cookie[0] = new Cookie(WebConstants.ANONYMOUS_CONSENT_COOKIE, consentCookieValue);
		when(request.getCookies()).thenReturn(cookie);

		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		// verify that method was called at least
		verify(response).addCookie(any());

		// verify consentTemplateData was NOT added to model
		verify(modelAndView).addObject(CONSENT_TEMPLATES, Collections.emptyList());
		// verify consentTemplateData was NOT added to model
		assertTrue(modelAndView.getModelMap().get(CONSENT_TEMPLATES).equals(Collections.emptyList()));
	}

	@Test
	public void shouldPopulateTheModelIfConsentHasNoState() throws Exception
	{
		final Cookie[] cookie = new Cookie[1];
		cookie[0] = new Cookie(WebConstants.ANONYMOUS_CONSENT_COOKIE, consentCookieValueNoState);
		when(request.getCookies()).thenReturn(cookie);
		// overriding
		when(consentManagementBeforeViewHandler.getConsentTemplates()).thenReturn(consentTemplateDataNoState);

		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		// verify that method was called at least
		verify(response).addCookie(any());

		// verify consentTemplateData was added to model
		verify(modelAndView).addObject(CONSENT_TEMPLATES, consentTemplateDataNoState);
		// verify consentTemplateData was added to model
		assertTrue(modelAndView.getModelMap().get(CONSENT_TEMPLATES).equals(consentTemplateDataNoState));
	}

	@Test
	public void shouldUpdateTheCookieAndModelWithNewVersion() throws Exception
	{
		final Cookie[] cookie =
		{ new Cookie(WebConstants.ANONYMOUS_CONSENT_COOKIE, consentCookieValue) };
		when(request.getCookies()).thenReturn(cookie);
		// adding templates
		final ConsentTemplateData v2 = prepareUpdatedTemplates();

		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		// verify that method was called at least
		verify(response).addCookie(any());

		// verify v2 was added to model
		verify(modelAndView).addObject(CONSENT_TEMPLATES, Collections.singletonList(v2));
		// verify v2 was added to model
		assertTrue(modelAndView.getModelMap().get(CONSENT_TEMPLATES).equals(Collections.singletonList(v2)));
	}

	@Test
	public void shouldRemoveConsentTemplatesFromSessionOnLanguageChange()
	{
		// Given
		final String currentLangIso = storeSessionFacade.getCurrentLanguage().getIsocode();
		final String previousLanguageIso = sessionService.getAttribute(ConsentManagementBeforeViewHandler.PREVIOUS_LANGUAGE);
		assertNotNull(sessionService.getAttribute(CONSENT_TEMPLATES));
		assertTrue(!currentLangIso.equals(previousLanguageIso));

		// When
		consentManagementBeforeViewHandler.checkLanguageChange();

		// verify that consent templates attribute was removed
		verify(sessionService).removeAttribute(CONSENT_TEMPLATES);
		verify(sessionService).setAttribute(ConsentManagementBeforeViewHandler.PREVIOUS_LANGUAGE, currentLangIso);
	}

	@Test
	public void shouldPopulateSessionWithAnonymousConsentsCookie() throws Exception
	{
		//when
		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		//then
		verify(response).addCookie(any());
		final ArgumentCaptor<Map> consentsInSession = ArgumentCaptor.forClass(Map.class);
		verify(sessionService).setAttribute(eq(WebConstants.USER_CONSENTS), consentsInSession.capture());
		final Map<String, String> consents = consentsInSession.getValue();
		assertTrue(consents.size() == 2);
		assertTrue(consents.containsKey(TEMPLATE_CODE_GIVEN));
		assertTrue(consents.containsKey(TEMPLATE_CODE_WITHDRAWN));
	}

	@Test
	public void shouldCreateCookieWhenAnonymousUserVistisPageFirstTime() throws Exception
	{
		//when
		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);
		//then
		assertAllConsentsInCookie(TEMPLATE_CODE_GIVEN, TEMPLATE_CODE_WITHDRAWN);
		assertConsentTemplatesAddedToModel();
	}

	@Test
	public void shouldFilterOutConsentsThatWasAcceptedOrDeclined() throws Exception
	{
		//given
		given(Boolean.valueOf(userFacade.isAnonymousUser())).willReturn(Boolean.TRUE);
		given(request.getCookies()).willReturn(buildConsentsCookie(getConsentCookieData()));
		when(sessionService.getAttribute(CONSENT_TEMPLATES)).thenReturn(getConsentTemplateData());
		given(consentFacade.getConsentTemplatesWithConsents()).willReturn(consentTemplateData);

		//when
		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		//then
		assertAllConsentsInCookie("templateCode1", "templateCode2", "templateCode3");
		assertOnlyNullConsentsInModel();
	}


	@Test
	public void shouldGetTemplatesFromDBOnlyWhenNoTemplatesInSession() throws Exception
	{
		//given
		given(sessionService.getAttribute(ConsentManagementBeforeViewHandler.CONSENT_TEMPLATES)).willReturn(null);
		given(consentFacade.getConsentTemplatesWithConsents()).willReturn(consentTemplateData);

		//when
		consentManagementBeforeViewHandler.beforeView(request, response, modelAndView);

		//then
		verify(consentFacade, atLeastOnce()).getConsentTemplatesWithConsents();
		verify(sessionService, atLeastOnce()).setAttribute(CONSENT_TEMPLATES, consentTemplateData);
	}

	protected List<ConsentTemplateData> getConsentTemplateData()
	{
		final ConsentData rrr = new ConsentData();
		rrr.setConsentGivenDate(new Date());

		return Arrays.asList(ConsentTemplateDataBuilder.aConsentTemplateData().withId("templateCode1").withVersion(1).build(),
				ConsentTemplateDataBuilder.aConsentTemplateData().withId("templateCode2").withVersion(1).withConsentData(rrr).build(),
				ConsentTemplateDataBuilder.aConsentTemplateData().withId("templateCode3").withVersion(1).build());
	}

	protected List<ConsentCookieData> getConsentCookieData()
	{
		return Arrays.asList(
				ConsentCookieDataBuilder.aConsentCookieData().withConsentState(CONSENT_GIVEN).withTemplateCode("templateCode1")
						.withTemplateVersion(1).build(),
				ConsentCookieDataBuilder.aConsentCookieData().withConsentState(CONSENT_WITHDRAWN).withTemplateCode("templateCode2")
						.withTemplateVersion(1).build(),
				ConsentCookieDataBuilder.aConsentCookieData().withTemplateCode("templateCode3").withTemplateVersion(1).build());
	}

	protected void assertOnlyNullConsentsInModel()
	{
		final ArgumentCaptor<List> attributesCaptor = ArgumentCaptor.forClass(List.class);
		verify(modelAndView).addObject(eq(CONSENT_TEMPLATES), Collections.singletonList(attributesCaptor.capture()));
		final List<ConsentTemplateData> consentTemplateData = attributesCaptor.getValue();
		assertTrue(consentTemplateData.size() == 1);
		assertTrue(consentTemplateData.get(0).getConsentData() == null);
	}

	protected void assertAllConsentsInCookie(final String... consentsId) throws Exception
	{
		final ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
		verify(response).addCookie(cookieArgumentCaptor.capture());
		final Cookie cookie = cookieArgumentCaptor.getValue();
		final List<ConsentCookieData> consentCookie = new ArrayList(
				Arrays.asList(mapper.readValue(URLDecoder.decode(cookie.getValue(), UTF_8), ConsentCookieData[].class)));

		assertTrue(consentCookie.size() == consentsId.length);
		for (int i = 0; i < consentsId.length; i++)
		{
			assertEquals(consentsId[i], consentCookie.get(i).getTemplateCode());
		}

	}

	protected Cookie[] buildConsentsCookie(final List<ConsentCookieData> consentCookieData) throws IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		return new Cookie[]
		{ new Cookie(WebConstants.ANONYMOUS_CONSENT_COOKIE, mapper.writeValueAsString(consentCookieData)) };
	}


	protected void assertConsentTemplatesAddedToModel()
	{
		final ArgumentCaptor<List> attributesCaptor = ArgumentCaptor.forClass(List.class);
		verify(modelAndView).addObject(eq(CONSENT_TEMPLATES), Collections.singletonList(attributesCaptor.capture()));
		final List<ConsentTemplateData> consentTemplateData = attributesCaptor.getValue();
		assertTrue(consentTemplateData.size() == 2);
		assertEquals(consentTemplateData.get(0).getId(), TEMPLATE_CODE_GIVEN);
		assertEquals(consentTemplateData.get(1).getId(), TEMPLATE_CODE_WITHDRAWN);
	}

	protected ConsentTemplateData prepareUpdatedTemplates()
	{
		final ConsentTemplateData consentV2 = new ConsentTemplateData();
		final ConsentData withdrawnConsent = new ConsentData();
		withdrawnConsent.setCode("withdrawn");
		withdrawnConsent.setConsentGivenDate(new Date());
		withdrawnConsent.setConsentWithdrawnDate(new Date());
		consentV2.setConsentData(withdrawnConsent);
		consentV2.setVersion(Integer.valueOf(2));
		consentV2.setDescription("withdrawn");
		consentV2.setExposed(true);
		consentV2.setId("withdrawn");
		// one old and consentV2 v2
		final List<ConsentTemplateData> consentTemplateData = Arrays.asList(givenTemplate, consentV2);

		when(consentManagementBeforeViewHandler.getConsentTemplates()).thenReturn(consentTemplateData);
		return consentV2;
	}
}


class ConsentCookieDataBuilder
{

	private String templateCode;

	private int templateVersion;

	private String consentState;

	private ConsentCookieDataBuilder()
	{
	}

	public static ConsentCookieDataBuilder aConsentCookieData()
	{
		return new ConsentCookieDataBuilder();
	}

	public ConsentCookieDataBuilder withTemplateCode(final String templateCode)
	{
		this.templateCode = templateCode;
		return this;
	}

	public ConsentCookieDataBuilder withTemplateVersion(final int templateVersion)
	{
		this.templateVersion = templateVersion;
		return this;
	}

	public ConsentCookieDataBuilder withConsentState(final String consentState)
	{
		this.consentState = consentState;
		return this;
	}

	public ConsentCookieData build()
	{
		final ConsentCookieData consentCookieData = new ConsentCookieData();
		consentCookieData.setTemplateCode(templateCode);
		consentCookieData.setTemplateVersion(templateVersion);
		consentCookieData.setConsentState(consentState);
		return consentCookieData;
	}
}

class ConsentTemplateDataBuilder
{
	private String id;

	private String name;

	private String description;

	private int version;

	private boolean exposed;

	private ConsentData consentData;

	private ConsentTemplateDataBuilder()
	{
	}

	public static ConsentTemplateDataBuilder aConsentTemplateData()
	{
		return new ConsentTemplateDataBuilder();
	}

	public ConsentTemplateDataBuilder withId(final String id)
	{
		this.id = id;
		return this;
	}

	public ConsentTemplateDataBuilder withName(final String name)
	{
		this.name = name;
		return this;
	}

	public ConsentTemplateDataBuilder withDescription(final String description)
	{
		this.description = description;
		return this;
	}

	public ConsentTemplateDataBuilder withVersion(final int version)
	{
		this.version = version;
		return this;
	}

	public ConsentTemplateDataBuilder withExposed(final boolean exposed)
	{
		this.exposed = exposed;
		return this;
	}

	public ConsentTemplateDataBuilder withConsentData(final ConsentData consentData)
	{
		this.consentData = consentData;
		return this;
	}

	public ConsentTemplateData build()
	{
		final ConsentTemplateData consentTemplateData = new ConsentTemplateData();
		consentTemplateData.setId(id);
		consentTemplateData.setName(name);
		consentTemplateData.setDescription(description);
		consentTemplateData.setVersion(Integer.valueOf(version));
		consentTemplateData.setExposed(exposed);
		consentTemplateData.setConsentData(consentData);
		return consentTemplateData;
	}
}


