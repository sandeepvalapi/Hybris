/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.security.captcha;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;


/**
 * An aspect which uses google ReCaptcha api to validate captcha answer on the storefront Registration form.
 */

public class ReCaptchaAspect
{
	private static final Logger LOG = Logger.getLogger(ReCaptchaAspect.class);

	private static final String RECAPTCHA_SITE_KEY_PROPERTY = "recaptcha.publickey";
	private static final String RECAPTCHA_SECRET_KEY_PROPERTY = "recaptcha.privatekey";
	private static final String RECAPTCHA_RESPONSE_PARAM = "g-recaptcha-response";
	private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	private SiteConfigService siteConfigService;
	private BaseStoreService baseStoreService;

	public Object prepare(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		final List<Object> args = Arrays.asList(joinPoint.getArgs());
		final HttpServletRequest request = (HttpServletRequest) CollectionUtils.find(args,
				PredicateUtils.instanceofPredicate(HttpServletRequest.class));

		if (request != null)
		{
			final boolean captchaEnabledForCurrentStore = isCaptchaEnabledForCurrentStore();
			request.setAttribute("captchaEnabledForCurrentStore", Boolean.valueOf(captchaEnabledForCurrentStore));
			if (captchaEnabledForCurrentStore)
			{
				request.setAttribute("recaptchaPublicKey", getSiteConfigService().getProperty(RECAPTCHA_SITE_KEY_PROPERTY));
			}
		}
		return joinPoint.proceed();
	}

	public Object advise(final ProceedingJoinPoint joinPoint) throws Throwable
	{

		final boolean captchaEnabledForCurrentStore = isCaptchaEnabledForCurrentStore();
		if (captchaEnabledForCurrentStore)
		{
			final List<Object> args = Arrays.asList(joinPoint.getArgs());
			HttpServletRequest request = (HttpServletRequest) CollectionUtils.find(args,
					PredicateUtils.instanceofPredicate(HttpServletRequest.class));

			if (request == null && RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes)
			{
				final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				request = requestAttributes.getRequest();
			}

			if (request != null)
			{
				request.setAttribute("captchaEnabledForCurrentStore", Boolean.valueOf(captchaEnabledForCurrentStore));
				request.setAttribute("recaptchaPublicKey", getSiteConfigService().getProperty(RECAPTCHA_SITE_KEY_PROPERTY));
				final String recaptchaResponse = request.getParameter(RECAPTCHA_RESPONSE_PARAM);
				if (StringUtils.isBlank(recaptchaResponse) || !checkAnswer(recaptchaResponse))
				{
					// if there is an error add a message to binding result.
					final BindingResult bindingResult = (BindingResult) CollectionUtils.find(args,
							PredicateUtils.instanceofPredicate(BindingResult.class));
					if (bindingResult != null)
					{
						bindingResult.reject("recaptcha.challenge.field.invalid", "Challenge Answer is invalid.");
					}
					request.setAttribute("recaptchaChallangeAnswered", Boolean.FALSE);
				}
			}
		}
		return joinPoint.proceed();
	}

	protected boolean checkAnswer(final String recaptchaResponse)
	{
		final RequestConfig config = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
		final HttpPost method = new HttpPost(RECAPTCHA_VERIFY_URL);

		final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("secret", getSiteConfigService().getProperty(RECAPTCHA_SECRET_KEY_PROPERTY)));
		urlParameters.add(new BasicNameValuePair("response", recaptchaResponse));

		HttpClientBuilder httpClientBuilder = HttpClients.custom().setRetryHandler(
				new DefaultHttpRequestRetryHandler(3, false));

		try (final CloseableHttpClient client = httpClientBuilder.setDefaultRequestConfig(config).build())
		{
			method.setEntity(new UrlEncodedFormEntity(urlParameters));
			final HttpResponse httpResponse = client.execute(method);

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				return false;
			}

			final JSONObject response = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
			return response.getBoolean("success");
		}
		catch (IOException | JSONException e)
		{
			LOG.error("Exception occurred while checking captcha answer", e);
			return false;
		}
		finally
		{
			method.releaseConnection();
		}
	}

	protected boolean isCaptchaEnabledForCurrentStore()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return currentBaseStore != null && Boolean.TRUE.equals(currentBaseStore.getCaptchaCheckEnabled());
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
