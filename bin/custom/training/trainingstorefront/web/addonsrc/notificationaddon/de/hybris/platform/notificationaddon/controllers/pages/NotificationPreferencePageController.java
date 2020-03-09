/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.notificationaddon.forms.NotificationChannelForm;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationfacades.facades.NotificationPreferenceFacade;

import java.util.List;
import java.util.stream.Collectors;

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
	private static final String[] DISALLOWED_FIELDS = new String[] {};

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "notificationPreferenceFacade")
	private NotificationPreferenceFacade notificationPreferenceFacade;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
	    binder.setDisallowedFields(DISALLOWED_FIELDS);
	}


	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String getNotificationPreferences(final Model model) throws CMSItemNotFoundException
	{

		final List<NotificationPreferenceData> notificationPreferenceList = notificationPreferenceFacade
				.getValidNotificationPreferences();

		final NotificationChannelForm form = new NotificationChannelForm();
		form.setChannels(notificationPreferenceList.stream()
				.sorted((a, b) -> a.getChannel().getCode().compareTo(b.getChannel().getCode())).collect(Collectors.toList()));

		model.addAttribute(NOTIFICATION_PREFERENCE_FORM, form);
		final ContentPageModel notificationPreferencePage = getContentPageForLabelOrId(NOTIFICATION_PREFERENCE_CMS_PAGE);
		storeCmsPageInModel(model, notificationPreferencePage);
		setUpMetaDataForContentPage(model, notificationPreferencePage);

		model.addAttribute(BREADCRUMBS_ATTR,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.notificationPreferenceSetting"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		return getViewForPage(model);
	}

	@RequestMapping(method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateNotificationPreference(final NotificationChannelForm notificationPreferenceForm,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		notificationPreferenceFacade.updateNotificationPreference(notificationPreferenceForm.getChannels());

		model.addAttribute(BREADCRUMBS_ATTR,
				accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.notificationPreferenceSetting"));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
				"notification.preference.confirmation.message.title");

		return REDIRECT_TO_GET_PREFERENCE_PAGE;
	}


}
