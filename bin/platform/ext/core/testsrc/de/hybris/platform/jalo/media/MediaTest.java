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
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


@UnitTest
public class MediaTest
{
	@Spy
	private final TestMedia media = new TestMedia();
	@Mock
	private JaloSession jaloSession;
	@Mock
	private SessionContext sessionContext;
	@Mock
	private MediaFolder mediaFolder;
	private final PK pk = PK.createFixedUUIDPK(10, 1);
	private final Long dataPK = Long.valueOf(123456789);

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		doReturn(jaloSession).when(media).getSession();
		doReturn(sessionContext).when(jaloSession).getSessionContext();
		doReturn(mediaFolder).when(media).getFolder();
		doReturn("somePath").when(mediaFolder).getPath();
		doReturn("image/jpeg").when(media).getMime();
		doReturn(Boolean.TRUE).when(media).hasData();
	}

	@Test
	public void shouldReturnLocationAseembledFromPKAndExtensionForRealRealOldMedia()
	{
		// given
		doReturn(null).when(media).getProperty(sessionContext, Media.DATAPK);
		doReturn(null).when(media).getProperty(sessionContext, Media.SUBFOLDERPATH);
		doReturn(null).when(media).getProperty(sessionContext, Media.LOCATION);
		doReturn(pk).when(media).getPK();

		// when
		final String location = media.getLocation();

		// then
		assertThat(location).isNotNull();
		assertThat(location).isEqualTo("somePath/2814749767106576.jpg");
	}

	@Test
	public void shouldReturnLocationAssembledFromDataPKAndExtensionForRealOldMedia()
	{
		// given
		doReturn(dataPK).when(media).getProperty(sessionContext, Media.DATAPK);
		doReturn(null).when(media).getProperty(sessionContext, Media.SUBFOLDERPATH);
		doReturn(null).when(media).getProperty(sessionContext, Media.LOCATION);

		// when
		final String location = media.getLocation();

		// then
		assertThat(location).isNotNull();
		assertThat(location).isEqualTo("somePath/123456789.jpg");
	}

	@Test
	public void shouldReturnLocationAssembledFromSubFolderDataPKAndExtensionForOldMedia()
	{
		// given
		doReturn(dataPK).when(media).getProperty(sessionContext, Media.DATAPK);
		doReturn("h08/h0f/").when(media).getProperty(sessionContext, Media.SUBFOLDERPATH);
		doReturn(null).when(media).getProperty(sessionContext, Media.LOCATION);

		// when
		final String location = media.getLocation();

		// then
		assertThat(location).isNotNull();
		assertThat(location).isEqualTo("somePath/h08/h0f/123456789.jpg");
	}

	@Test
	public void shouldReturnNormalLocationForNewMedia()
	{
		// given
		doReturn(dataPK).when(media).getProperty(sessionContext, Media.DATAPK);
		doReturn(null).when(media).getProperty(sessionContext, Media.SUBFOLDERPATH);
		doReturn("somePath/h08/h0f/123456789.jpg").when(media).getProperty(sessionContext, Media.LOCATION);

		// when
		final String location = media.getLocation();

		// then
		assertThat(location).isNotNull();
		assertThat(location).isEqualTo("somePath/h08/h0f/123456789.jpg");
	}

	public class TestMedia extends Media
	{
		@Override
		protected String getFileExtensionFromMime()
		{
			return "jpg";
		}
	}
}
