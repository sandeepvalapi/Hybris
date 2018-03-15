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
package de.hybris.platform.jalo.media;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class MediaContainerIntegrationTest extends HybrisJUnit4TransactionalTest
{
	private MediaContainer container;
	private MediaFormat format1, format2, format3;
	private Media media1WithFormat1, media2WithFormat1, media3WithFormat2, media4WithFormat3, media5WithNullFormat,
			media6WithNullFormat;

	@Before
	public void setUp() throws Exception
	{
		container = createMediaContainer("container1");
		format1 = createMediaFormat("format1");
		format2 = createMediaFormat("format2");
		format3 = createMediaFormat("format3");
		media1WithFormat1 = createMediaForFormat("testMedia1WithFormat1", format1);
		media2WithFormat1 = createMediaForFormat("testMedia2WithFormat1", format1);
		media3WithFormat2 = createMediaForFormat("testMedia3WithFormat2", format2);
		media4WithFormat3 = createMediaForFormat("testMedia4WithFormat2", format3);
		media5WithNullFormat = createMediaForFormat("testMedia5WithNullFormat", null);
		media6WithNullFormat = createMediaForFormat("testMedia6WithNullFormat", null);
	}

	private MediaContainer createMediaContainer(final String code)
	{
		return MediaManager.getInstance().createMediaContainer(code);
	}

	private MediaFormat createMediaFormat(final String code)
	{
		return MediaManager.getInstance().createMediaFormat(code);
	}

	private Media createMediaForFormat(final String code, final MediaFormat format)
	{
		return MediaManager.getInstance().createMedia(code, format);
	}

	/**
	 * Test method for {@link de.hybris.platform.jalo.media.MediaContainer#setMedias(java.util.Collection)} .
	 */
	@Test
	public void shouldAllowMediasWithNullFormat()
	{
		// given
		final Collection<Media> medias = Lists.newArrayList(media5WithNullFormat);

		// when
		container.setMedias(medias);
		final Collection<Media> containerMedias = container.getMedias();

		// then
		assertThat(containerMedias).isNotNull().hasSize(1).containsOnly(media5WithNullFormat);
	}

	/**
	 * Test method for {@link de.hybris.platform.jalo.media.MediaContainer#setMedias(java.util.Collection)} .
	 */
	@Test
	public void shouldSetMediasWithoutCheckingForFormatUniqueness()
	{
		// given
		final Collection<Media> medias = Lists.newArrayList(media1WithFormat1, media2WithFormat1, media3WithFormat2,
				media4WithFormat3);

		// when
		container.setMedias(medias);
		final Collection<Media> containerMedias = container.getMedias();

		// then
		assertThat(containerMedias).isNotNull().hasSize(4);
		assertThat(containerMedias).containsOnly(media1WithFormat1, media2WithFormat1, media3WithFormat2, media4WithFormat3);
	}

	/**
	 * Test method for {@link de.hybris.platform.jalo.media.MediaContainer#setMedias(java.util.Collection)} .
	 */
	@Test
	public void shouldSetMediasWithoutCheckingForFormatUniquenessAllowingNullFormats()
	{
		// given
		final Collection<Media> medias = Lists.newArrayList(media1WithFormat1, media2WithFormat1, media3WithFormat2,
				media4WithFormat3, media5WithNullFormat, media6WithNullFormat);

		// when
		container.setMedias(medias);
		final Collection<Media> containerMedias = container.getMedias();

		// then
		assertThat(containerMedias).isNotNull().hasSize(6);
		assertThat(containerMedias).containsOnly(media1WithFormat1, media2WithFormat1, media3WithFormat2, media4WithFormat3,
				media5WithNullFormat, media6WithNullFormat);
	}

	@Test
	public void shouldAddMediasWithOnlyUniqueFormatsAllowingNullFormats()
	{
		// given
		final Collection<Media> medias = Lists.newArrayList(media1WithFormat1, media3WithFormat2, media4WithFormat3);
		container.setMedias(medias);

		// when
		container.addToMedias(media1WithFormat1);
		container.addToMedias(media5WithNullFormat);
		final Collection<Media> containerMedias = container.getMedias();

		// then
		assertThat(containerMedias).isNotNull().hasSize(4);
		assertThat(containerMedias).containsOnly(media1WithFormat1, media3WithFormat2, media4WithFormat3, media5WithNullFormat);
	}

}
