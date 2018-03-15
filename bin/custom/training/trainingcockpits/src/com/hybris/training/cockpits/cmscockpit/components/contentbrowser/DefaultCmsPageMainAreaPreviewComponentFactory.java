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
package com.hybris.training.cockpits.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.components.contentbrowser.AbstractCmsPageMainAreaBrowserComponent;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaPreviewComponentFactory;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import com.hybris.training.cockpits.components.liveedit.DefaultLiveEditView;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;


/**
 *
 *
 */
public class DefaultCmsPageMainAreaPreviewComponentFactory extends CmsPageMainAreaPreviewComponentFactory
{
	private static final Logger LOG = Logger.getLogger(DefaultCmsPageMainAreaPreviewComponentFactory.class);


	public DefaultCmsPageMainAreaPreviewComponentFactory(final TypedObject wrappedCurrentPageModel)
	{
		super(wrappedCurrentPageModel);
	}


	@Override
	public AbstractMainAreaBrowserComponent createInstance(final AdvancedBrowserModel model,
			final AbstractContentBrowser contentBrowser)
	{
		AbstractCmsPageMainAreaBrowserComponent comp = null;
		if (model instanceof SectionBrowserModel)
		{
			comp = new AbstractCmsPageMainAreaBrowserComponent((SectionBrowserModel) model, contentBrowser)
			{
				@Override
				protected void renderInternal()
				{
					doRenderInternal(this, this.innerParent);
				}

				@Override
				protected Div createMainArea()
				{
					return null;
				}

				@Override
				protected UIItemView getCurrentItemView()
				{
					return null;
				}

			};
		}
		return comp;
	}

	protected void doRenderInternal(final AbstractCmsPageMainAreaBrowserComponent component,
								  final HtmlBasedComponent parent) {
		final TypedObject page = component.getModel().getCurrentPageObject();
		final CMSSiteModel site = component.getModel().getActiveSite();
		if (page == null || site == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Could not get Structure view configuration. Reason: No page or site set.");
			}
		}
		else
		{
			final boolean openPreviewInNewTab = site.isOpenPreviewInNewTab();
			final String previewUrl = renderPreview(component, parent, openPreviewInNewTab, site, page);
			if (StringUtils.isNotEmpty(previewUrl) && openPreviewInNewTab)
			{
				openPreviewInNewTab(previewUrl);
			}
		}
	}

	/**
	 * Responsible for displaying a preview of particular page </p> Note: </p>
	 */
	protected String renderPreview(final AbstractCmsPageMainAreaBrowserComponent component,
								   final HtmlBasedComponent parent, final boolean openPreviewInNewTab,
								   final CMSSiteModel site, final TypedObject page)
	{
		if (page.getObject() instanceof AbstractPageModel)
		{
			final AbstractPageModel pageModel = (AbstractPageModel) page.getObject();

			final DefaultLiveEditViewModel liveEditViewModel = newDefaultLiveEditViewModel();

			liveEditViewModel.setSite(site);
			liveEditViewModel.setPage(pageModel);
			liveEditViewModel.setPagePreview(true);
			liveEditViewModel.setWelcomePanelVisible(false);

			if (!openPreviewInNewTab)
			{
				final DefaultLiveEditView liveEditView = newDefaultLiveEditView(liveEditViewModel);

				liveEditView.getViewComponent().setParent(parent);
				component.getContentBrowser().setAttribute(PREVIEW_FRAME_KEY, liveEditView.getContentFrame());
			}
			return liveEditViewModel.computeFinalUrl();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Hook for custom DefaultLiveEditViewModel 
	 */
	protected DefaultLiveEditViewModel newDefaultLiveEditViewModel()
	{
		return new DefaultLiveEditViewModel();
	}

	/**
	 * Hook for custom DefaultLiveEditView 
	 */
	protected DefaultLiveEditView newDefaultLiveEditView(final DefaultLiveEditViewModel liveEditViewModel)
	{
		return new DefaultLiveEditView(liveEditViewModel);
	}
}
