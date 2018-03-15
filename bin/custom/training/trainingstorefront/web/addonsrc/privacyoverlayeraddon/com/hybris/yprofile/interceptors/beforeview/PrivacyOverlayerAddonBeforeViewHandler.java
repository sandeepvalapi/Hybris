/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.hybris.yprofile.interceptors.beforeview;

import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.addonsupport.config.javascript.BeforeViewJsPropsHandlerAdaptee;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class PrivacyOverlayerAddonBeforeViewHandler extends BeforeViewJsPropsHandlerAdaptee {

    private static final String CONSENT_REFERENCE_SESSION_ATTR_KEY = "consent-reference";
    private static final String CONSENT_REFERENCE_TOKEN_SESSION_ATTR_KEY = "consent-reference-token";

    private SiteConfigService siteConfigService;
    private SessionService sessionService;
    private BaseSiteService baseSiteService;

    private ProfileConfigurationService profileConfigurationService;
    /**
     * Sets values needed for the cookie bar in model object.
     *
     * @param request
     * @param response
     * @param model
     * @param viewName
     * @return
     */
    @Override
    public String beforeViewJsProps(HttpServletRequest request, HttpServletResponse response, ModelMap model, String viewName) {

        if (model != null)
        {
            model.addAttribute("TENANT", getTenant());
            model.addAttribute("CONSENT_REFERENCE_ID", getSessionService().getAttribute(CONSENT_REFERENCE_SESSION_ATTR_KEY));
            model.addAttribute("CONSENT_REFERENCE_TOKEN", getSessionService().getAttribute(CONSENT_REFERENCE_TOKEN_SESSION_ATTR_KEY));
        }

        return viewName;
    }

    protected String getTenant() {
        return getProfileConfigurationService().getYaaSTenant(getSiteId());
    }

    protected String getSiteId(){
        return getCurrentBaseSiteModel().isPresent() ? getCurrentBaseSiteModel().get().getUid() : StringUtils.EMPTY;
    }

    protected Optional<BaseSiteModel> getCurrentBaseSiteModel() {
        return ofNullable(getBaseSiteService().getCurrentBaseSite());
    }

    public SiteConfigService getSiteConfigService() {
        return siteConfigService;
    }

    @Required
    public void setSiteConfigService(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    @Required
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }
}
