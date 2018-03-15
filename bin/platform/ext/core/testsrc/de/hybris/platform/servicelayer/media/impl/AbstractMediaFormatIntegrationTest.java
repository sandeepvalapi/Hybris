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
package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public abstract class AbstractMediaFormatIntegrationTest extends ServicelayerTransactionalBaseTest
{
	static final int FORMAT_COUNT = 5;

	@Resource
	protected ModelService modelService;
	protected CatalogVersionModel catalogVersion;
	private MediaFormatModel[] formats;

	@Before
	public void setup()
	{
		this.formats = new MediaFormatModel[FORMAT_COUNT];
		for (int i = 0; i < FORMAT_COUNT; i++)
		{
			final MediaFormatModel format = this.modelService.create(MediaFormatModel.class);
			format.setName("Format #" + i);
			format.setQualifier("format" + i);
			this.modelService.save(format);
			this.formats[i] = format;
		}

		final CatalogModel catalog = this.modelService.create(CatalogModel.class);
		catalog.setId("my_favorite_catalog");
		this.modelService.save(catalog);

		this.catalogVersion = this.modelService.create(CatalogVersionModel.class);
		this.catalogVersion.setVersion("tolle_version");
		this.catalogVersion.setCatalog(catalog);
		this.modelService.save(this.catalogVersion);
	}

	@Test
	public void testGetMediaByFormatWithContainerInput()
	{
		final MediaContainerModel container = this.modelService.create(MediaContainerModel.class);
		container.setQualifier("test1234");
		container.setCatalogVersion(this.catalogVersion);
		this.modelService.save(container);

		try
		{
			// nothing there yet
			this.getMediaByFormat(container, this.formats[2]);
			Assert.fail("ModelNotFoundException was not thrown as expected.");
		}
		catch (final ModelNotFoundException e)
		{
			// that's what we wanted
		}

		MediaModel last = null;
		for (int i = 0; i < FORMAT_COUNT; i++)
		{
			final String quali = "testMedia_" + this.formats[i].getQualifier();
			final MediaModel media = this.modelService.create(CatalogUnawareMediaModel.class);
			media.setCode(quali);
			media.setMediaFormat(this.formats[i]);
			media.setMediaContainer(container);
			this.modelService.save(media);

			{
				final MediaModel retrieved = this.getMediaByFormat(container, this.formats[i]);
				Assert.assertNotNull(retrieved);
				Assert.assertEquals(quali, retrieved.getCode());
				Assert.assertEquals(this.formats[i], retrieved.getMediaFormat());
				Assert.assertEquals(media, retrieved);
			}

			if (last != null)
			{
				final MediaModel retrieved = this.getMediaByFormat(last, this.formats[i]);
				Assert.assertNotNull(retrieved);
				Assert.assertEquals(quali, retrieved.getCode());
				Assert.assertEquals(this.formats[i], retrieved.getMediaFormat());
				Assert.assertEquals(media, retrieved);
			}
			last = media;
		}
	}

	abstract MediaModel getMediaByFormat(MediaModel last, MediaFormatModel mediaFormatModel);

	abstract MediaModel getMediaByFormat(MediaContainerModel container, MediaFormatModel mediaFormatModel);
}
