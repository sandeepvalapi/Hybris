/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.filter;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;
import de.hybris.platform.assistedservicestorefront.restrictions.AssistedServicePathRestrictionEvaluator;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAgentLoginStrategy;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Assisted Service filter. Used for applying restrictions to storefront paths for AS agent and login agent based on
 * request.
 */
public class AssistedServiceFilter extends OncePerRequestFilter
{
	private static final Logger LOG = Logger.getLogger(AssistedServiceFilter.class);

	private AssistedServicePathRestrictionEvaluator assistedServicePathRestrictionEvaluator;
	private AssistedServiceFacade assistedServiceFacade;
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;

	@Override
	protected void doFilterInternal(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse,
			final FilterChain filterchain) throws ServletException, IOException
	{
		if (AssistedServiceUtils.getSamlCookie(httpservletrequest) != null)
		{
			try
			{
				performLogin(httpservletrequest, httpservletresponse);
			}
			catch (final AssistedServiceException e)
			{
				getAssistedServiceFacade().getAsmSession().setFlashErrorMessage(e.getMessageCode());
				LOG.debug(e.getMessage(), e);
			}
			catch (final RuntimeException e)
			{
				// in case of wrong token value - just show error message and remove this token
				getAssistedServiceFacade().getAsmSession().setFlashErrorMessage("asm.action.restricted");
				AssistedServiceUtils.eraseSamlCookie(httpservletresponse);
				LOG.debug(e.getMessage(), e);
			}
		}
		
		final String asmRequestParam = httpservletrequest.getParameter(AssistedservicestorefrontConstants.ASM_REQUEST_PARAM);
        
		if (Boolean.TRUE.toString().equals(asmRequestParam))
		{
			httpservletresponse.addHeader("Set-Cookie" ,createProfileTrackingPauseCookie());
		}

		if (getAssistedServicePathRestrictionEvaluator().evaluate(httpservletrequest, httpservletresponse))
		{
			filterchain.doFilter(httpservletrequest, httpservletresponse);
		}

	}
	
	private void performLogin(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse)
			throws AssistedServiceException
	{
		//in case it is a logout request then remove the saml cookie if exist and proceed with the normal logout flow
		if ("/assisted-service/logoutasm".equalsIgnoreCase(httpservletrequest.getServletPath()))
		{
			AssistedServiceUtils.eraseSamlCookie(httpservletresponse);
		}

		final LoginToken token = new CookieBasedLoginToken(AssistedServiceUtils.getSamlCookie(httpservletrequest));

		// terminate in case token doesn't belong to currently logged in agent
		if (getAssistedServiceFacade().isAssistedServiceAgentLoggedIn()
				&& getAssistedServiceFacade().getAsmSession().getAgent().getUid().equals(token.getUser().getUid()))
		{
			return;
		}
		if (getAssistedServiceFacade().isAssistedServiceAgentLoggedIn())
		{
			getAssistedServiceFacade().logoutAssistedServiceAgent();
		}
		getAssistedServiceFacade().loginAssistedServiceAgentSAML(token.getUser().getUid(), token.getPassword());
		getAssistedServiceAgentLoginStrategy().login(token.getUser().getUid(), httpservletrequest, httpservletresponse);
		getAssistedServiceFacade().emulateAfterLogin();
	}

	protected String createProfileTrackingPauseCookie()
	{
		final String cookieName = Config.getString(AssistedservicestorefrontConstants.PROFILE_COOKIE_NAME, AssistedservicestorefrontConstants.ASM_PROFILE_TRACKING_PAUSE_COOKIE);
		final StringBuilder buf = new StringBuilder(cookieName);
		buf.append("=");
		buf.append(StringUtils.EMPTY);

		final long age = AssistedservicestorefrontConstants.IMPERSISTENCE_COOKIE_INDEX;
		buf.append("; Max-Age=\"");
		buf.append(age);
		buf.append("\"");

		buf.append("; Path=\"");
		buf.append("/");
		buf.append("\"");

		buf.append("; Secure");
		buf.append("; HttpOnly");
		return buf.toString();
	}

	/**
	 * @return the assistedServicePathRestrictionEvaluator
	 */
	protected AssistedServicePathRestrictionEvaluator getAssistedServicePathRestrictionEvaluator()
	{
		return assistedServicePathRestrictionEvaluator;
	}

	/**
	 * @param assistedServicePathRestrictionEvaluator
	 *           the assistedServicePathRestrictionEvaluator to set
	 */
	@Required
	public void setAssistedServicePathRestrictionEvaluator(
			final AssistedServicePathRestrictionEvaluator assistedServicePathRestrictionEvaluator)
	{
		this.assistedServicePathRestrictionEvaluator = assistedServicePathRestrictionEvaluator;
	}

	/**
	 * @return the assistedServiceFacade
	 */
	protected AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	/**
	 * @param assistedServiceFacade
	 *           the assistedServiceFacade to set
	 */
	@Required
	public void setAssistedServiceFacade(final AssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}

	/**
	 * @return the assistedServiceAgentLoginStrategy
	 */
	protected AssistedServiceAgentLoginStrategy getAssistedServiceAgentLoginStrategy()
	{
		return assistedServiceAgentLoginStrategy;
	}

	/**
	 * @param assistedServiceAgentLoginStrategy
	 *           the assistedServiceAgentLoginStrategy to set
	 */
	@Required
	public void setAssistedServiceAgentLoginStrategy(final AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy)
	{
		this.assistedServiceAgentLoginStrategy = assistedServiceAgentLoginStrategy;
	}
}
