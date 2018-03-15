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
package de.hybris.platform.notificationaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.notificationaddon.forms.NotificationPreferenceForm;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for displaying and updating notification preference
 */
@Controller
@Scope("tenant")
@RequestMapping("/my-account/notification-preference")
public class NotificationPreferencePageController extends AbstractPageController
{
	private static final String REDIRECT_TO_GET_PREFERENCE_PAGE = REDIRECT_PREFIX + "/my-account/notification-preference";
	private static final String NOTIFICATION_PREFERENCE_CMS_PAGE = "notification-preference";
	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	private static final String NOTIFICATION_PREFERENCE_FORM = "notificationPreferenceForm";

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "notificationPreferenceFacade")
	private NotificationPreferenceFacade notificationPreferenceFacade;
	
	final String[] DISALLOWED_FIELDS = new String[]{};
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getNotificationPreference(final Model model) throws CMSItemNotFoundException
	{
		final NotificationPreferenceForm notificationPreferenceForm = new NotificationPreferenceForm();
		final NotificationPreferenceData notificationPreferenceData = notificationPreferenceFacade.getNotificationPreference();

		notificationPreferenceForm.setEmailEnabled(notificationPreferenceData.isEmailEnabled());
		notificationPreferenceForm.setEmailAddress(notificationPreferenceData.getEmailAddress());
		notificationPreferenceForm.setSmsEnabled(notificationPreferenceData.isSmsEnabled());
		notificationPreferenceForm.setMobileNumber(notificationPreferenceData.getMobileNumber());

		model.addAttribute(NOTIFICATION_PREFERENCE_FORM, notificationPreferenceForm);
		storeCmsPageInModel(model, getContentPageForLabelOrId(NOTIFICATION_PREFERENCE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NOTIFICATION_PREFERENCE_CMS_PAGE));

		model.addAttribute(BREADCRUMBS_ATTR,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.notificationPreferenceSetting"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		return getViewForPage(model);
	}

	@RequestMapping(method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateNotificationPreference(final NotificationPreferenceForm notificationPreferenceForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectModel)
					throws CMSItemNotFoundException
	{
		final NotificationPreferenceData notificationPreferenceData = new NotificationPreferenceData();
		notificationPreferenceData.setEmailEnabled(notificationPreferenceForm.isEmailEnabled());
		notificationPreferenceData.setSmsEnabled(notificationPreferenceForm.isSmsEnabled());

		notificationPreferenceFacade.updateNotificationPreference(notificationPreferenceData);

		model.addAttribute(BREADCRUMBS_ATTR,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.notificationPreferenceSetting"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"notification.preference.confirmation.message.title");

		return REDIRECT_TO_GET_PREFERENCE_PAGE;
	}

}
