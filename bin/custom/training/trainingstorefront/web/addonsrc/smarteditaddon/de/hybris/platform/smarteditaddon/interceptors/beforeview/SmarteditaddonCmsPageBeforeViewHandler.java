/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.interceptors.beforeview;

import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;


/**
 * This handler adds an almost unaltered page uid to a class of the jsp body with the format smartedit-page-uid-
 * <pageUID>
 */
public class SmarteditaddonCmsPageBeforeViewHandler implements BeforeViewHandler
{

	private static final String PAGE_BODY_CSS_CLASSES = "pageBodyCssClasses";
	private static final String PAGEUID_CHARACTER_EXCLUSION_REGEXP = "[^a-zA-Z0-9-_]";

	private UniqueItemIdentifierService uniqueItemIdentifierService;


	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{

		final UniqueItemIdentifierService uiis = getUniqueItemIdentifierService();

		final AbstractPageModel page = (AbstractPageModel) modelAndView.getModel().get(AbstractPageController.CMS_PAGE_MODEL);

		if (page != null && page.getUid() != null)
		{
			final String presetCssClasses = (String) modelAndView.getModelMap().get(PAGE_BODY_CSS_CLASSES);

			final ItemData pageData = uiis.getItemData(page).orElseThrow(
					() -> new UnknownIdentifierException("Cannot generate uuid for page in SmarteditaddonCmsPageBeforeViewHandler"));

			final ItemData catalogVersionData = uiis.getItemData(page.getCatalogVersion()).orElseThrow(
					() -> new UnknownIdentifierException("Cannot generate uuid for component in CMSSmartEditDynamicAttributeService"));

			final StringBuilder cssClasses = new StringBuilder();

			if (isNotBlank(presetCssClasses))
			{
				cssClasses.append(presetCssClasses);
				cssClasses.append(' ');
			}

			// PAGE UID
			cssClasses.append(SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS)
					.append(page.getUid().replaceAll(PAGEUID_CHARACTER_EXCLUSION_REGEXP, "-")).append(' ');

			// PAGE UUID
			cssClasses.append(SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS).append(pageData.getItemId()).append(' ');

			// PAGE CATALOG VERSION UUID
			cssClasses.append(SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS).append(catalogVersionData.getItemId())
					.append(' ');

			modelAndView.addObject(PAGE_BODY_CSS_CLASSES, cssClasses.toString());
		}
	}


	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}


}
