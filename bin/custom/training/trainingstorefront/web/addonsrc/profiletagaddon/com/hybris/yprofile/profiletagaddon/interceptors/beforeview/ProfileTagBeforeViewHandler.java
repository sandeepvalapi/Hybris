/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.yprofile.profiletagaddon.interceptors.beforeview;

import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.platform.acceleratorservices.storefront.data.JavaScriptVariableData;
import de.hybris.platform.addonsupport.config.javascript.BeforeViewJsPropsHandlerAdaptee;
import de.hybris.platform.addonsupport.config.javascript.JavaScriptVariableDataFactory;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang.StringUtils.isBlank;


public class ProfileTagBeforeViewHandler extends BeforeViewJsPropsHandlerAdaptee
{
	private static final String PROFILE_TAG_DEBUG_QUERY_PARAMETER = "profileTagDebug";
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	@Resource
	private ProfileConfigurationService profileConfigurationService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;


	@Override
	public String beforeViewJsProps(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{

		attachCustomJSVariablesToModel(model, request);
		return viewName;
	}

	/**
	 * Provides a combination of setting attributes as JS variables with base.js.properties or traditional way of setting
	 * the value in model object.
	 *
	 * @param model
	 * @param request
	 */
	protected void attachCustomJSVariablesToModel(final ModelMap model, final HttpServletRequest request)
	{
		if (model != null)
		{
			final Map<String, String> jsPropsMap = new HashMap<>();
			final List<JavaScriptVariableData> jsPropList = JavaScriptVariableDataFactory.createFromMap(jsPropsMap);
			Map<String, List<JavaScriptVariableData>> jsVariables = (Map<String, List<JavaScriptVariableData>>) model
					.get(detectJsModelName());

			if (jsVariables == null)
			{
				jsVariables = new HashMap<String, List<JavaScriptVariableData>>();
				model.addAttribute(detectJsModelName(), jsVariables);
			}

			//Loads the key-values from base.js.properties and available as JS variables in the storefront
			List<JavaScriptVariableData> jsVariablesList = jsVariables.get(getMessageSource().getAddOnName());
			if (jsVariablesList != null && !jsVariablesList.isEmpty())
			{
				jsVariablesList.addAll(jsPropList);
			}
			else
			{
				jsVariablesList = jsPropList;
			}
			jsVariables.put(getMessageSource().getAddOnName(), jsVariablesList);

			model.addAttribute("SITE_ID", getSiteId());
			model.addAttribute("PROFILETAG_URL", profileConfigurationService.getProfileTagUrl());
			model.addAttribute("PROFILETAG_CONFIG_URL", profileConfigurationService.getProfileTagConfigUrl());
			model.addAttribute("TENANT", profileConfigurationService.getTenant(getSiteId()));
			model.addAttribute("CLIENT_ID", profileConfigurationService.getClientIdForProfileTag(getSiteId()));
		}


		setProfileTagDebugFlagInSessionIfPresent(request);
		setProfileTagDebugFlagIfPresentForCurrentUser();
	}

	private void setProfileTagDebugFlagInSessionIfPresent(final HttpServletRequest request) {
		final Optional<Boolean> profileTagDebug = getProfileTagDebug(request);

		if (profileTagDebug.isPresent()){
			final Boolean debug = profileTagDebug.get();
			profileConfigurationService.setProfileTagDebugFlagInSession(debug);
		}
	}

	private void setProfileTagDebugFlagIfPresentForCurrentUser() {
		final UserModel currentUser = userService.getCurrentUser();

		final Boolean profileTagDebugSession = profileConfigurationService.isProfileTagDebugEnabledInSession();

		if (!userService.isAnonymousUser(currentUser) && (profileTagDebugSession != null)
				&& !profileTagDebugSession.equals(currentUser.getProfileTagDebug())) {
			currentUser.setProfileTagDebug(profileTagDebugSession);
			modelService.save(currentUser);
			modelService.refresh(currentUser);
		}
	}

	protected Optional<Boolean> getProfileTagDebug(final HttpServletRequest request) {

		final String parameter = request.getParameter(PROFILE_TAG_DEBUG_QUERY_PARAMETER);

		if (isBlank(parameter)){
		    return Optional.empty();
        }

		if (TRUE.equals(parameter)) {
            return Optional.of(Boolean.TRUE);
        }

        if (FALSE.equals(parameter)) {
            return Optional.of(Boolean.FALSE);
        }

		 return Optional.empty();
	}


	protected String getTenant() {
		return profileConfigurationService.getTenant(getSiteId());
	}

	protected String getSiteId(){
		return getCurrentBaseSiteModel().isPresent() ? getCurrentBaseSiteModel().get().getUid() : StringUtils.EMPTY;
	}

	protected Optional<BaseSiteModel> getCurrentBaseSiteModel() {
		return ofNullable(baseSiteService.getCurrentBaseSite());
	}


}