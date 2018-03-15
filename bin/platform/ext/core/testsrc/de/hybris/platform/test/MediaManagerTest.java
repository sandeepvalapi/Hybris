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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MediaManagerTest extends HybrisJUnit4TransactionalTest
{
	private MediaManager mm;
	static private final int size = 10;
	private final Media[] media = new Media[size];
	private final String[] codes = new String[size];
	private final String[] urls = new String[size];
	private final PK[] pk = new PK[size];
	private int initSize;
	static private final String URLStarts = "xxxURL";

	//  for testGetAllMediaByCode() all codes must start with "xxx"
	@Before
	public void setUp() throws Exception
	{
		mm = jaloSession.getMediaManager();
		final Randy r = new Randy();

		initSize = mm.getAllMedia().size();

		for (int i = 0; i < size; i++)
		{
			codes[i] = "xxx" + (i + 100) + r.distinctRandomString(20);
			urls[i] = URLStarts + (i + 100) + r.distinctRandomString(20);
			media[i] = mm.createMedia(codes[i]);
			media[i].setURL(urls[i]);
			pk[i] = media[i].getPK();
		}
	}

	@After
	public void tearDown() throws Exception
	{
		for (int i = 0; i < size; i++)
		{
			media[i].remove();
		}
	}

	@Test
	public void shouldHaveMediaStorageStrategyInjectedBySpring()
	{
		// when
		final MediaStorageRegistry mediaStorageFactory = mm.getMediaStorageFactory();

		// then
		assertThat(mediaStorageFactory).isNotNull();
	}

	@Test
	public void testGetAllMedia()
	{
		final ArrayList c = new ArrayList(mm.getAllMedia());

		for (int i = 0; i < size; i++)
		{
			assertTrue("Media missing: " + i, c.remove(media[i]));
		}

		assertEquals("Wrong Media count", initSize, c.size());
	}

	@Test
	public void testGetMediaByPK() throws Exception
	{
		for (int i = 0; i < size; i++)
		{
			assertTrue("Wrong Media returned", media[i].equals(jaloSession.<Item> getItem(pk[i])));
		}
	}

	@Test
	public void testGetMediaByCode()
	{
		final ArrayList l = new ArrayList();

		for (int i = 0; i < size; i++)
		{
			l.clear();
			l.addAll(mm.getMediaByCode(codes[i]));
			assertTrue("Media is missing: " + i + ", codes[i]=" + codes[i] + ",media[i].getCode()=" + media[i].getCode() + ".",
					l.contains(media[i]));
		}
	}

	@Test
	public void testGetAllMediaByCode()
	{
		final ArrayList c = new ArrayList(mm.getMediaByCode("xxx%"));

		for (int i = 0; i < size; i++)
		{
			assertTrue("Media is missing: " + i, c.remove(media[i]));
		}

	}

	@Test
	public void testGetMediaByMimeType() throws JaloBusinessException
	{
		final int step = 3;
		for (int i = 0; i < size; i += step)
		{
			media[i].setMime("hybris/special");
		}

		Collection search = mm.getMediaByMimeType("hybris/special");
		for (int i = 0; i < size; i++)
		{
			if (i % step == 0)
			{
				assertTrue(search.contains(media[i]));
			}
			else
			{
				assertFalse("found " + media[i].getMime(), search.contains(media[i]));
			}
		}

		search = mm.getMediaByMimeType("hybris/%");
		for (int i = 0; i < size; i++)
		{
			if (i % step == 0)
			{
				assertTrue("didn't find " + i + ". media, mime type is " + media[i].getMime(), search.contains(media[i]));
			}
			else
			{
				assertFalse("found " + media[i].getMime(), search.contains(media[i]));
			}
		}
	}

	@Test
	public void testGetMediaByURL()
	{
		final ArrayList l = new ArrayList();

		for (int i = 0; i < size; i++)
		{
			l.clear();
			l.addAll(mm.getMediaByURL("%" + URLStarts + "%"));
			assertTrue("Media is missing: " + i + ", URL LIKE '" + URLStarts + "',media[i].getCode()=" + media[i].getCode() + ".",
					l.contains(media[i]));
		}
	}

	@Test
	public void testGetMediaByURLExact()
	{
		final ArrayList l = new ArrayList();

		for (int i = 0; i < size; i++)
		{
			l.clear();
			l.addAll(mm.getMediaByURLExact(urls[i]));
			assertTrue("Media is missing: " + i + ", URL[i]=" + urls[i] + ",media[i].getCode()=" + media[i].getCode() + ".",
					l.contains(media[i]));
		}
	}


	@Test(expected = MediaInvalidLocationException.class)
	public void folderQualifierShouldMatch()
	{
		mm.isPathTargetSecuredFolder("foo", "bar");
	}

	@Test(expected = MediaInvalidLocationException.class)
	public void locationShouldHaveAtLeastOneSegment()
	{
		mm.isPathTargetSecuredFolder("foo", "");
	}

	@Test
	public void shouldNotThrowExceptionIfFolderMatchesLocation()
	{
		mm.isPathTargetSecuredFolder("foo", "foo");
	}

	@Test
	public void shouldNotCheckMatchingIfFolderIsRoot()
	{
		mm.isPathTargetSecuredFolder(MediaManager.ROOT_FOLDER_QUALIFIER, "foo");
	}

	@Test(expected = MediaInvalidLocationException.class)
	public void shouldFailIfNormalizedLocationDoesNotMatchFolder()
	{
		mm.isPathTargetSecuredFolder("foo", "foo/../bar");
	}

	@Test
	public void shouldDefineFolderAsSecuredIfPointingFromRootToSecuredSubfolder()
	{
		final Collection<String> securedFolders = Registry.getApplicationContext().getBean(MediaStorageConfigService.class)
				.getSecuredFolders();
		Assume.assumeTrue(isNotEmpty(securedFolders));

		if (isNotEmpty(securedFolders))
		{
			final String securedFolder = securedFolders.iterator().next();
			assertTrue(mm.isPathTargetSecuredFolder(MediaManager.ROOT_FOLDER_QUALIFIER, securedFolder));
		}
	}
}
