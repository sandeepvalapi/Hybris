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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultMediaDaoIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private MediaDao mediaDao;

	MediaFolderModel folder;
	List<MediaModel> medias;

	@Before
	public void setUp() throws Exception
	{
		folder = createTestMediaFolder();
		medias = createTestMedias(folder, 10);
	}

	@Test
	public void testFindMediaByCode()
	{
		for (final MediaModel m : medias)
		{
			assertEquals(Arrays.asList(m), mediaDao.findMediaByCode(m.getCode()));
			assertEquals(Collections.EMPTY_LIST, mediaDao.findMediaByCode("invalid_" + m.getCode()));
		}
	}

	@Test
	public void testFindFolderByCode()
	{
		assertEquals(Arrays.asList(folder), mediaDao.findMediaFolderByQualifier(folder.getQualifier()));
		assertEquals(Collections.EMPTY_LIST, mediaDao.findMediaFolderByQualifier("illegal_" + folder.getQualifier()));
	}

	List<MediaModel> createTestMedias(final MediaFolderModel folder, final int count)
	{
		final List<MediaModel> result = new ArrayList<MediaModel>();
		for (int i = 0; i < count; i++)
		{
			final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
			media.setCode(RandomStringUtils.randomAlphabetic(5));
			media.setFolder(folder);
			modelService.save(media);

			result.add(media);
		}
		return result;
	}

	MediaFolderModel createTestMediaFolder()
	{
		final MediaFolderModel mediaFolder = modelService.create(MediaFolderModel.class);
		mediaFolder.setPath("foo");
		mediaFolder.setQualifier("testingFolder");
		modelService.save(mediaFolder);
		return mediaFolder;
	}
}
