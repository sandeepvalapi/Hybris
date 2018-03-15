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
package com.hybris.training.cockpits.cmscockpit.session.impl;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.events.impl.CmsLiveEditEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModelListener;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Represents a browser area of <b>Live Edit Perspective</b>
 */
public class DefaultLiveEditBrowserArea extends LiveEditBrowserArea
{
	private static final Logger LOG = Logger.getLogger(DefaultLiveEditBrowserArea.class);
	
	private static final String UNABLE_TO_LOAD_LIVE_EDIT_BROWSER_MODEL_MSG = "It is not possible to load LiveEdit Browser Model";

	private CommonI18NService commonI18NService;
	private CMSSiteModel currentSite = null;
	private AdvancedBrowserModel welcomeBrowserModel = null;

	private boolean initialized = false;
	private boolean liveEditModeEnabled = false;
	private final DefaultSearchBrowserModelListener liveEditBrowserListener = newDefaultSearchBrowserModelListener();

	@Override
	public void initialize(final Map<String, Object> params)
	{
		if (!this.initialized)
		{
			this.initialized = true;
			final DefaultLiveEditBrowserModel browserModel = newDefaultLiveEditBrowserModel();
			browserModel.setCurrentSite(this.currentSite);
			browserModel.addBrowserModelListener(liveEditBrowserListener);
			addVisibleBrowser(browserModel);
			setFocusedBrowser(browserModel);

			UISessionUtils.getCurrentSession().addSessionListener(newLiveEditBrowserAreaUISessionListener());
		}
	}

	protected DefaultLiveEditBrowserModel newDefaultLiveEditBrowserModel()
	{
		return new DefaultLiveEditBrowserModel();
	}

	protected DefaultSearchBrowserModelListener newDefaultSearchBrowserModelListener()
	{
		return new DefaultSearchBrowserModelListener(this);
	}

	@Override
	public boolean addVisibleBrowser(final int index, final BrowserModel browserModel)
	{
		// this area does not support tabbing so we need to make sure now "unexpected" browsers are added.
		if (browserModel instanceof DefaultLiveEditBrowserModel)
		{
			return super.addVisibleBrowser(index, browserModel);
		}
		else
		{
			LOG.warn("Not showing browser " + browserModel + ". Reason: Only " + DefaultLiveEditBrowserModel.class.getCanonicalName() + " allowed.");
			return false;
		}
	}

	@Override
	public BrowserModelListener getBrowserListener()
	{
		return null;
	}

	@Override
	public void saveQuery(final BrowserModel browserModel)
	{
		// Isn't intended to be use here.
	}

	@Override
	public void refreshContent(final CMSSiteModel siteModel)
	{
		this.currentSite = siteModel;
		if (getFocusedBrowser() instanceof DefaultLiveEditBrowserModel)
		{
			((DefaultLiveEditBrowserModel) getFocusedBrowser()).setCurrentSite(siteModel);
			((DefaultLiveEditBrowserModel) getFocusedBrowser()).updateItems();
		}
		else
		{
			LOG.warn(UNABLE_TO_LOAD_LIVE_EDIT_BROWSER_MODEL_MSG);
		}
	}

	@Override
	public void refreshContent()
	{
		if (getFocusedBrowser() instanceof DefaultLiveEditBrowserModel)
		{
			((DefaultLiveEditBrowserModel) getFocusedBrowser()).refresh();
		}
		else
		{
			LOG.warn(UNABLE_TO_LOAD_LIVE_EDIT_BROWSER_MODEL_MSG);
		}
	}

	/**
	 * Called whenever user changes a browser area mode in <b> Live Edit Perspective </b>
	 */
	@Override
	public void fireModeChange()
	{
		if (getFocusedBrowser() instanceof DefaultLiveEditBrowserModel)
		{
			final DefaultLiveEditBrowserModel model = (DefaultLiveEditBrowserModel) getFocusedBrowser();
			if (isLiveEditModeEnabled())
			{
				setLiveEditModeEnabled(false);
			}
			else
			{
				setLiveEditModeEnabled(true);
			}
			//
			model.fireModeChange(getCorrespondingContentBrowser(getFocusedBrowser()));
		}
		else
		{
			LOG.warn(UNABLE_TO_LOAD_LIVE_EDIT_BROWSER_MODEL_MSG);
		}
	}

	@Override
	public void fireModeChange(final boolean liveEditMode)
	{
		if (getFocusedBrowser() instanceof DefaultLiveEditBrowserModel)
		{
			final DefaultLiveEditBrowserModel model = (DefaultLiveEditBrowserModel) getFocusedBrowser();
			setLiveEditModeEnabled(liveEditMode);
			model.fireModeChange(getCorrespondingContentBrowser(getFocusedBrowser()));
		}
		else
		{
			LOG.warn(UNABLE_TO_LOAD_LIVE_EDIT_BROWSER_MODEL_MSG);
		}
	}

	@Override
	public boolean isLiveEditModeEnabled()
	{
		return liveEditModeEnabled;
	}

	@Override
	public void setLiveEditModeEnabled(final boolean liveEditModeEnabled)
	{
		this.liveEditModeEnabled = liveEditModeEnabled;
	}

	public CMSSiteModel getCurrentSite()
	{
		return currentSite;
	}

	@Override
	public AdvancedBrowserModel getWelcomeBrowserModel()
	{
		return this.welcomeBrowserModel;
	}

	@Override
	public void setWelcomeBrowserModel(final AdvancedBrowserModel welcomeBrowserModel)
	{
		this.welcomeBrowserModel = welcomeBrowserModel;
	}

	@Override
	public boolean isClosable(final BrowserModel browserModel)
	{
		boolean closable = super.isClosable(browserModel);

		if (browserModel instanceof DefaultLiveEditBrowserModel)
		{
			closable = false;
		}

		return closable;
	}

	@Override
	public void onCockpitEvent(final CockpitEvent event)
	{
		super.onCockpitEvent(event);

		if (event instanceof CmsLiveEditEvent)
		{
			processCmsLiveEditEvent((CmsLiveEditEvent) event);
		}
		else if (event instanceof ItemChangedEvent)
		{
			processItemChangedEvent((ItemChangedEvent) event);
		}
		else if (event instanceof CmsUrlChangeEvent)
		{
			processCmsUrlChangeEvent(event);

		}
		else if (event instanceof CmsPerspectiveInitEvent)
		{
			processCmsPerspectiveInitEvent(event);
		}
		else
		{
			processGenericEvent();
		}

	}

	protected void processGenericEvent() {
		final BrowserModel focusedBrowserModel = getFocusedBrowser();
		if (focusedBrowserModel instanceof DefaultLiveEditBrowserModel)
        {
            final DefaultLiveEditBrowserModel liveBrowserModel = (DefaultLiveEditBrowserModel) focusedBrowserModel;
            liveBrowserModel.setRelatedPagePk(null);
        }
	}

	protected void processCmsPerspectiveInitEvent(final CockpitEvent event) {
		if (event.getSource() == null || !event.getSource().equals(getPerspective()))
        {
            return;
        }
		final BrowserModel focusedBrowserModel = getFocusedBrowser();
		if (focusedBrowserModel instanceof DefaultLiveEditBrowserModel)
        {
            final DefaultLiveEditBrowserModel liveBrowserModel = (DefaultLiveEditBrowserModel) focusedBrowserModel;
            liveBrowserModel.onCmsPerpsectiveInitEvent();
        }
	}

	protected void processCmsUrlChangeEvent(final CockpitEvent event) {
		//exit when comes from another perspective!
		if (!event.getSource().equals(getPerspective()))
        {
            return;
        }
		final AbstractContentBrowser abstractContentBrowser = getCorrespondingContentBrowser(getFocusedBrowser());
		if (abstractContentBrowser != null)
        {
            final DefaultLiveEditContentBrowser liveEditContentBrowser = (DefaultLiveEditContentBrowser) abstractContentBrowser;
            liveEditContentBrowser.updateAfterChangedUrl((CmsUrlChangeEvent) event);
        }
	}

	protected void processItemChangedEvent(final ItemChangedEvent event) {
		final AbstractContentBrowser abstractContentBrowser = getCorrespondingContentBrowser(getFocusedBrowser());
		if (abstractContentBrowser != null)
        { //update changed item
            abstractContentBrowser.updateItem(event.getItem(), Collections.emptySet());
        }
	}

	protected void processCmsLiveEditEvent(final CmsLiveEditEvent event) {
		if (!event.getUrl().isEmpty())
        {
            if (getFocusedBrowser() instanceof DefaultLiveEditBrowserModel)
            {
                ((DefaultLiveEditBrowserModel) getFocusedBrowser()).setCurrentUrl(event.getUrl());
            }
            refreshContent(this.getCurrentSite());
        }
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected class LiveEditBrowserAreaUISessionListener implements UISessionListener
	{
		@Override
		public void perspectiveChanged(final UICockpitPerspective previous, final UICockpitPerspective newOne)
		{
			// NOOP
		}

		@Override
		public void globalDataLanguageChanged()
		{

			for (final BrowserModel browserModel : DefaultLiveEditBrowserArea.this.getBrowsers())
			{
				if (browserModel instanceof DefaultLiveEditBrowserModel)
				{
					final PreviewDataModel previewData = ((DefaultLiveEditBrowserModel) browserModel).getPreviewData();
					if (previewData != null)
					{
						previewData.setLanguage(getCommonI18NService().getLanguage(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso()));
						((DefaultLiveEditBrowserModel) browserModel).setPreviewData(previewData);
						((DefaultLiveEditBrowserModel) browserModel).clearPreviewPageIfAny();
					}
				}
			}
		}

		@Override
		public void beforeLogout(final UserModel user)
		{
			// NOOP
		}

		@Override
		public void afterLogin(final UserModel user)
		{
			// NOOP
		}
	}
}
