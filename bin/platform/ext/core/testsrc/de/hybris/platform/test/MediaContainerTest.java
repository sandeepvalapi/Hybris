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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.media.MediaContext;
import de.hybris.platform.jalo.media.MediaFormat;
import de.hybris.platform.jalo.media.MediaFormatMapping;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MediaContainerTest extends HybrisJUnit4TransactionalTest
{
	private MediaContainer container = null;
	private MediaFormat format1 = null;
	private MediaFormat format2 = null;
	private MediaFormat format3 = null;
	private MediaContext context = null;
	private Media media1WithFormat1 = null;
	private Media media2WithFormat1 = null;
	private Media media3WithFormat2 = null;
	private Media mediaWithoutFormat = null;

	@Before
	public void setUp()
	{
		container = MediaManager.getInstance().createMediaContainer("testContainer");
		format1 = MediaManager.getInstance().createMediaFormat("testFormat1");
		format2 = MediaManager.getInstance().createMediaFormat("testFormat2");
		format3 = MediaManager.getInstance().createMediaFormat("testFormat3");
		context = MediaManager.getInstance().createMediaContext("testContext");
		media1WithFormat1 = MediaManager.getInstance().createMedia("testMedia1WithFormat1", format1);
		media2WithFormat1 = MediaManager.getInstance().createMedia("testMedia2WithFormat1", format1);
		media3WithFormat2 = MediaManager.getInstance().createMedia("testMedia3WithFormat2", format2);
		mediaWithoutFormat = MediaManager.getInstance().createMedia("testMediaWithoutFormat");
	}

	@Test
	public void testMediaContainerCreation()
	{
		assertEquals("testContainer", container.getQualifier());
		assertEquals(0, container.getMedias().size());
		assertNull(media1WithFormat1.getMediaContainer());
	}

	@Test(expected = JaloSystemException.class)
	public void testMediaFormatCreation()
	{
		assertEquals("testFormat1", format1.getQualifier());
		MediaManager.getInstance().createMediaFormat("testFormat1");
	}

	@Test(expected = JaloSystemException.class)
	public void testMediaContextCreation()
	{
		assertEquals("testContext", context.getQualifier());
		MediaManager.getInstance().createMediaContext("testContext");
	}

	@Test
	public void testMediaContainer()
	{
		try
		{
			container.setMedia(null);
			fail("Have set null to container and no exception is thrown");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}

		// add media
		container.setMedia(media1WithFormat1);
		assertEquals(media1WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		assertEquals(container, media1WithFormat1.getMediaContainer());
		// add same media again
		container.setMedia(media1WithFormat1);
		assertEquals(media1WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		// add same media again
		container.addToMedias(media1WithFormat1);
		assertEquals(media1WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		// add same media as collection
		container.setMedias(Collections.singletonList(media1WithFormat1));
		assertEquals(media1WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		// add different media with same format
		container.setMedia(media2WithFormat1);
		assertEquals(media2WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		// add different media with same format as collection
		container.setMedias(Collections.singletonList(media1WithFormat1));
		assertEquals(media1WithFormat1, container.getMedia(format1));
		assertEquals(1, container.getMedias().size());
		// add medias with same format as collection
		container.setMedias(Arrays.asList(media1WithFormat1, media2WithFormat1));
		assertThat(container.getMedias()).hasSize(2).containsOnly(media1WithFormat1, media2WithFormat1);

		// remove media
		container.removeFromMedias(media1WithFormat1);
		container.removeFromMedias(media2WithFormat1);
		assertNull(container.getMedia(format1));
		assertNull(media1WithFormat1.getMediaContainer());
		assertNull(media2WithFormat1.getMediaContainer());
		assertEquals(0, container.getMedias().size());
	}

	@Test
	public void testMediaGetForContextMethod()
	{
		assertNull(mediaWithoutFormat.getForContext(context));
		assertNull(media1WithFormat1.getForContext(context));
		container.setMedia(media1WithFormat1);
		assertNull(media1WithFormat1.getForContext(context));
		MediaFormatMapping mapping = MediaManager.getInstance().createMediaFormatMapping(format3, format1);
		context.addToMappings(mapping);
		assertNull(media1WithFormat1.getForContext(context));
		mapping = MediaManager.getInstance().createMediaFormatMapping(format1, format2);
		context.addToMappings(mapping);
		assertNull(media1WithFormat1.getForContext(context));
		container.setMedia(media3WithFormat2);
		assertEquals(media3WithFormat2, media1WithFormat1.getForContext(context));
	}

	@Test
	public void testMediaGetInFormatMethod()
	{
		// getInFormat with no container set
		assertEquals(media1WithFormat1, media1WithFormat1.getInFormat(format1));
		assertNull(media1WithFormat1.getInFormat(format2));
		assertNull(mediaWithoutFormat.getInFormat(format1));
		assertNull(mediaWithoutFormat.getInFormat(format2));
		// add media1 to container
		container.setMedia(media1WithFormat1);
		// getInFormat with container set at media1
		assertEquals(media1WithFormat1, media1WithFormat1.getInFormat(format1));
		assertNull(media1WithFormat1.getInFormat(format2));
		// add media2 to container
		container.setMedia(media3WithFormat2);
		assertEquals(media1WithFormat1, media1WithFormat1.getInFormat(format1));
		assertEquals(media3WithFormat2, media1WithFormat1.getInFormat(format2));
		assertEquals(media3WithFormat2, media3WithFormat2.getInFormat(format2));
		assertEquals(media1WithFormat1, media3WithFormat2.getInFormat(format1));
	}

	@Test
	public void testMediaContext()
	{
		context.addToMappings(null);
		assertEquals(0, context.getMappings().size());
		// add a mapping
		MediaFormatMapping mapping = MediaManager.getInstance().createMediaFormatMapping(format1, format2);
		context.addToMappings(mapping);
		assertEquals(1, context.getMappings().size());
		assertEquals(format2, context.getTargetFormat(format1));
		assertNull(context.getTargetFormat(format2));
		// add a mapping with same source
		mapping = MediaManager.getInstance().createMediaFormatMapping(format1, format3);
		try
		{
			context.addToMappings(mapping);
			fail("Have added two mappings with same source format to MediaContext!!!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}

		// add a mapping with same source - second way
		try
		{
			context.setMappings(Arrays.asList(MediaManager.getInstance().createMediaFormatMapping(format1, format2), MediaManager
					.getInstance().createMediaFormatMapping(format1, format3)));
			fail("Have added two mappings with same source format to MediaContext in second way!!!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}

		// add a mapping with same source - third way
		try
		{
			MediaManager.getInstance().createMediaFormatMapping(format1, format2).setMediaContext(context);
			fail("Have added two mappings with same source format to MediaContext in third way!!!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}

		assertEquals(1, context.getMappings().size());
		// add another mapping
		mapping = MediaManager.getInstance().createMediaFormatMapping(format2, format3);
		context.addToMappings(mapping);
		assertEquals(format3, context.getTargetFormat(format2));
		assertEquals(2, context.getMappings().size());
		// remove a mapping
		context.removeFromMappings(mapping);
		assertFalse(mapping.isAlive());
		assertEquals(1, context.getMappings().size());
		// add mapping as collection
		try
		{
			context.setMappings(Arrays.asList(MediaManager.getInstance().createMediaFormatMapping(format2, format3), MediaManager
					.getInstance().createMediaFormatMapping(format2, format1)));
			fail("Have added two mappings with same source format to MediaContext!!!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}
		assertEquals(1, context.getMappings().size());
		mapping = context.getMappings().iterator().next();
		context.setMappings(Arrays.asList(MediaManager.getInstance().createMediaFormatMapping(format2, format3), MediaManager
				.getInstance().createMediaFormatMapping(format3, format1)));
		assertEquals(2, context.getMappings().size());
		assertFalse(mapping.isAlive());
	}
}
