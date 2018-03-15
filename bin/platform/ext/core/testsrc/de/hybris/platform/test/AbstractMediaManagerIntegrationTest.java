/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.test;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.impl.ModelMediaSource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.web.urlrenderer.DefaultPublicMediaURLRenderer;
import de.hybris.platform.servicelayer.web.urlrenderer.DefaultSecureMediaURLRenderer;
import de.hybris.platform.testframework.seed.MediaTestDataCreator;
import de.hybris.platform.util.MediaUtil;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;


@Ignore
public class AbstractMediaManagerIntegrationTest extends ServicelayerBaseTest
{
	@Resource(name = "core.mediaManager")
	protected MediaManager mediaManager;
	@Resource
	protected ModelService modelService;
	@Resource
	protected MediaService mediaService;
	protected MediaTestDataCreator testDataCreator;
	protected MediaModel testMedia;
	protected MediaModel testMediaWithExternalUrl;
	protected CatalogVersionModel catalogVersion;

	@Before
	public void setUp() throws Exception
	{
		testDataCreator = new MediaTestDataCreator(modelService, mediaService);

		final CatalogModel ctg = testDataCreator.createCatalog("testCtg");
		catalogVersion = testDataCreator.createCatalogVersion("testVersion", ctg);
	}

	protected String urlForMedia(final MediaModel media)
	{
		return urlForMedia(media, false);
	}

	protected String downloadUrlForMedia(final MediaModel media)
	{
		return urlForMedia(media, true);
	}

	protected String urlForMedia(final MediaModel media, final boolean forDownload)
	{
		final String folderQualifier = media.getFolder().getQualifier();
		final ModelMediaSource mediaSource = new ModelMediaSource(media);

		return forDownload ? mediaManager.getDownloadURLForMedia(folderQualifier, mediaSource)
				: mediaManager.getURLForMedia(folderQualifier, mediaSource);
	}

	protected String urlForMediaWithRenderer(final MediaModel media, final boolean addCtxPath, final boolean forDownload)
	{
		try
		{
			if (addCtxPath) // this simulates how the WebAppMediFilter works
			{
				MediaUtil.setCurrentPublicMediaURLRenderer(new DefaultPublicMediaURLRenderer("/testwebapp"));
			}
			return urlForMedia(media, forDownload);
		}
		finally
		{
			MediaUtil.unsetCurrentPublicMediaURLRenderer();
		}
	}

	protected String urlForMediaWithSecureRenderer(final MediaModel media, final boolean addCtxPath, final boolean forDownload)
	{
		try
		{
			MediaUtil.setCurrentSecureMediaURLRenderer(
					new DefaultSecureMediaURLRenderer("medias/__secure__", addCtxPath, "/testwebapp"));
			return urlForMedia(media, forDownload);
		}
		finally
		{
			MediaUtil.unsetCurrentSecureMediaURLRenderer();
		}
	}

	protected boolean forDownload()
	{
		return true;
	}

	protected boolean forDisplay()
	{
		return false;
	}

	protected boolean addContextPath()
	{
		return true;
	}

	protected boolean dontAddContextPath()
	{
		return false;
	}
}
