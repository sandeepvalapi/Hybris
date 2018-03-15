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

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaEditComponentFactory;
import de.hybris.platform.cmscockpit.components.contentbrowser.CmsPageMainAreaPersonalizeComponentFactory;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.listview.impl.SectionTableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import com.hybris.training.cockpits.cmscockpit.components.contentbrowser.DefaultCmsPageContentBrowser;
import com.hybris.training.cockpits.cmscockpit.components.contentbrowser.DefaultCmsPageMainAreaPreviewComponentFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * Default {@link CmsPageBrowserModel} for accelerator cockpits.
 */
public class DefaultCmsPageBrowserModel extends CmsPageBrowserModel
{
	private static final Logger LOG = Logger.getLogger(DefaultCmsPageBrowserModel.class);
	private List<MainAreaComponentFactory> viewModes = null;
	private TypedObject page;

    public DefaultCmsPageBrowserModel(CMSAdminSiteService cmsAdminSiteService, CmsCockpitService cmsCockpitService, ModelService modelService, CMSAdminComponentService cmsAdminComponentService, CMSAdminContentSlotService cmsAdminContentSlotService)
    {
        super(cmsAdminSiteService, cmsCockpitService, modelService, cmsAdminComponentService, cmsAdminContentSlotService);

    }


	@Override
	public List<MainAreaComponentFactory> getAvailableViewModes()
	{
		if (viewModes == null)
		{
			viewModes = new ArrayList<MainAreaComponentFactory>();
			viewModes.add(newCmsPageMainAreaEditComponentFactory());
			viewModes.add(newDefaultCmsPageMainAreaPreviewComponentFactory());
			viewModes.add(newCmsPageMainAreaPersonalizeComponentFactory());
		}
		return viewModes;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException // NOSONAR
	{
		final DefaultCmsPageBrowserModel browserModel = newDefaultCmsPageBrowserModel();
		browserModel.setCurrentPageObject(getCurrentPageObject());
		browserModel.createProperViewModel();
		browserModel.setViewMode(getViewMode());
		
		return browserModel;
	}

	protected DefaultCmsPageBrowserModel newDefaultCmsPageBrowserModel()
	{
		return new DefaultCmsPageBrowserModel(cmsAdminSiteService, cmsCockpitService, modelService, cmsAdminComponentService, cmsAdminContentSlotService);
	}

	protected CmsPageMainAreaEditComponentFactory newCmsPageMainAreaEditComponentFactory()
	{
		return new CmsPageMainAreaEditComponentFactory();
	}

	protected DefaultCmsPageMainAreaPreviewComponentFactory newDefaultCmsPageMainAreaPreviewComponentFactory()
	{
		return new DefaultCmsPageMainAreaPreviewComponentFactory(getCurrentPageObject());
	}

	protected CmsPageMainAreaPersonalizeComponentFactory newCmsPageMainAreaPersonalizeComponentFactory()
	{
		return new CmsPageMainAreaPersonalizeComponentFactory();
	}

	@Override
	public AbstractContentBrowser createViewComponent()
	{
		return new DefaultCmsPageContentBrowser();
	}

	@Override
	public boolean isBackButtonVisible()
	{
		return DefaultCmsPageMainAreaPreviewComponentFactory.VIEWMODE_ID.equals(getViewMode());
	}

	@Override
	public void onCockpitEvent(final CockpitEvent event)
	{
		// make sure newly created item is selected
		if (event instanceof ItemChangedEvent)
		{
			final ItemChangedEvent changedEvent = (ItemChangedEvent) event;
			switch (changedEvent.getChangeType())
			{
				case CREATED:
					processEventTypeCreated(changedEvent);
					break;

				case REMOVED:
					processEventTypeRemoved(changedEvent);
					break;

				case CHANGED:
					processEventTypeChanged(event, changedEvent);
					break;
				default:
					if (LOG.isDebugEnabled())
					{
						LOG.debug("No default behaviour defined for cockpit event.");
					}
			}
		}
	}

	protected void processEventTypeChanged(final CockpitEvent event, final ItemChangedEvent changedEvent) {
		for (final BrowserSectionModel sectionModel : getBrowserSectionModels())
        {
            if (sectionModel.equals(event.getSource()))
            {
                continue;
            }
            final List<TypedObject> sectionItems = sectionModel.getItems();
            final TypedObject changedItem = changedEvent.getItem();
            if (sectionItems.contains(changedItem))
            {
                final TypedObject typedObject = sectionItems.get(sectionItems.indexOf(changedItem));
                modelService.refresh(typedObject.getObject());
                sectionModel.update();
            }
            if (sectionModel.getRootItem() != null && sectionModel.getRootItem().equals(changedItem))
            {
                final TypedObject rootItem = (TypedObject) sectionModel.getRootItem();
                final ItemModel itemModel = (ItemModel) rootItem.getObject();
				modelService.refresh(itemModel);
                if (sectionModel instanceof Lockable)
                {
                    getContentEditorSection().setReadOnly(((Lockable) sectionModel).isLocked());
                }
                sectionModel.update();
            }
        }
	}

	protected void processEventTypeRemoved(final ItemChangedEvent changedEvent) {
		// if a page is deleted make sure any related struct tabs are closed
		if (changedEvent.getItem().equals(getCurrentPageObject()))
        {
            getArea().close(this);
        }

		if (changedEvent.getSource() instanceof SectionTableModel)
        {
            final BrowserSectionModel sectionModel = ((SectionTableModel) changedEvent.getSource()).getModel();

            final List<TypedObject> sectionItems = sectionModel.getItems();
			processSectionItems(changedEvent, sectionModel, sectionItems);

			if (getContentEditorSection().getRootItem() != null
                    && getContentEditorSection().getRootItem().equals(changedEvent.getItem()))
            {
                getContentEditorSection().setRootItem(null);
                getContentEditorSection().setVisible(false);
            }

            final AbstractBrowserArea area = (AbstractBrowserArea) UISessionUtils.getCurrentSession()
                    .getCurrentPerspective().getBrowserArea();
            final AbstractContentBrowser content = area.getCorrespondingContentBrowser(this);
            if (content != null && content.getToolbarComponent() != null)
            {
                content.getToolbarComponent().update();
            }

        }
	}

	protected void processSectionItems(final ItemChangedEvent changedEvent, final BrowserSectionModel sectionModel,
									 final List<TypedObject> sectionItems) {
		if (!CollectionUtils.isEmpty(sectionItems) && sectionItems.contains(changedEvent.getItem()))
        {
            final int removedIndex = sectionItems.indexOf(changedEvent.getItem());
            if (sectionModel.getSelectedIndex() != null)
            {
                if (removedIndex < sectionModel.getSelectedIndex().intValue())
                {
                    sectionModel.setSelectedIndex(sectionModel.getSelectedIndex().intValue() - 1);
                }
                else if (removedIndex == sectionModel.getSelectedIndex().intValue())
                {
                    sectionModel.setSelectedIndexes(Collections.emptyList());
                }
            }
            removeComponentFromSlot((TypedObject) sectionModel.getRootItem(), changedEvent.getItem());
            sectionModel.update();
        }
	}

	protected void processEventTypeCreated(final ItemChangedEvent changedEvent) {
		final TypedObject createdItem = changedEvent.getItem();
		if (changedEvent.getSource() instanceof BrowserSectionModel)
        {
			processBrowserSectionModel(changedEvent, createdItem);
        }
        else if (changedEvent.getSource() == null)
        {
            for (final BrowserSectionModel sectionModel : getBrowserSectionModels())
            {
                if (sectionModel.getItems().contains(createdItem))
                {
                    final int selectedIndex = sectionModel.getItems().indexOf(createdItem);
                    sectionModel.setSelectedIndex(selectedIndex);
                }
            }
            getContentEditorSection().setRootItem(createdItem);
            getContentEditorSection().setVisible(true);
            getContentEditorSection().update();
            updateItems();

        }
	}

	protected void processBrowserSectionModel(final ItemChangedEvent changedEvent, final TypedObject createdItem) {
		final BrowserSectionModel sectionModel = (BrowserSectionModel) changedEvent.getSource();
		final List<TypedObject> sectionItems = sectionModel.getItems();

		if (!CollectionUtils.isEmpty(sectionItems))
        {
            final int itemIndex = sectionItems.indexOf(createdItem);
            if (itemIndex != -1)
            {
                sectionModel.update();
                sectionModel.setSelectedIndex(itemIndex);

                getContentEditorSection().setRootItem(createdItem);
                getContentEditorSection().setVisible(true);
                getContentEditorSection().update();
            }
        }
	}


	protected void createProperViewModel()
	{
		if (getViewMode() != null && getViewMode().equals(CmsPageMainAreaEditComponentFactory.VIEW_MODE_ID))
		{
			createAndInitializeFlatOrStructureView();
		}
	}

	/**
	 * Sets given page as am active </p> Note: </p>
	 * 
	 * @param page
	 *           given page
	 */
	@Override
	public void setCurrentPageObject(final TypedObject page)
	{
		this.page = page;
	}

	/**
	 * Returns current page </p> Note: </p>
	 * 
	 * @return current page
	 */
	@Override
	public TypedObject getCurrentPageObject()
	{
		return this.page;
	}


}
