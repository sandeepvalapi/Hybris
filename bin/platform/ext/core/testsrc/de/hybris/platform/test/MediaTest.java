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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.impl.DefaultMimeService;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import junit.framework.AssertionFailedError;


@IntegrationTest
public class MediaTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(MediaTest.class.getName());

	private final int size = 10;
	private Media media;
	private Media mediaTestMoveDataSource;
	private Media mediaTestMoveDataDest;
	private Media mediaTestRemoveData;
	private Media mediaTestSetDataByURL;
	private Media m;
	private Media m2;
	private MediaManager mediaManager;
	private Media secureMedia;

	private final static byte[] data1 = new byte[]
	{ 100, -100, 50 };
	private final byte[] data2 = new byte[]
	{ 10, 20, 30, 40, 50, 60, };
	private final byte[] data3 = new byte[]
	{ 100, 10, 20, 30, 40, 50, 60, 10, 20, 30, 40, 50, 60, 100, };

	private Media[] medias;
	private String[] codes;

	@Before
	public void setUp() throws Exception
	{
		medias = new Media[size];
		codes = new String[size];
		mediaManager = jaloSession.getMediaManager();
		final Randy r = new Randy();

		for (int i = 0; i < size; i++)
		{
			codes[i] = r.distinctRandomString(30);
			assertNotNull(medias[i] = mediaManager.createMedia(codes[i]));
		}
		assertNotNull(media = mediaManager.createMedia(r.distinctRandomString(30)));
		assertNotNull(mediaTestMoveDataSource = mediaManager.createMedia(r.distinctRandomString(30)));
		assertNotNull(mediaTestMoveDataDest = mediaManager.createMedia(r.distinctRandomString(30)));
		assertNotNull(mediaTestRemoveData = mediaManager.createMedia(r.distinctRandomString(30)));
		assertNotNull(mediaTestSetDataByURL = mediaManager.createMedia(r.distinctRandomString(30)));
		assertNotNull(m = jaloSession.getMediaManager().createMedia(r.distinctRandomString(30)));
		assertNotNull(m2 = jaloSession.getMediaManager().createMedia(r.distinctRandomString(30)));
		assertNotNull(secureMedia = jaloSession.getMediaManager().createMedia("secureMedia1234"));
	}

	@Test
	public void testMime() throws JaloBusinessException
	{
		final String[] test = new String[size];
		for (int i = 0; i < size; i++)
		{
			test[i] = Randy.randomString(30);
			medias[i].setMime(test[i]);
		}

		for (int i = 0; i < size; i++)
		{
			assertEquals(test[i], medias[i].getMime());
		}
	}

	@Test
	public void testRealFileName()
	{
		final String[] test = new String[size];
		for (int i = 0; i < size; i++)
		{
			test[i] = Randy.randomString(30);
			medias[i].setRealFileName(test[i]);
		}
		for (int i = 0; i < size; i++)
		{
			assertEquals(test[i], medias[i].getRealFileName());
		}
	}

	@Test
	public void testCode()
	{
		final String[] test = new String[size];
		for (int i = 0; i < size; i++)
		{
			test[i] = Randy.randomString(30);
			medias[i].setCode(test[i]);
		}
		for (int i = 0; i < size; i++)
		{
			assertEquals(test[i], medias[i].getCode());
		}
	}

	@Test
	public void testData()
	{
		try
		{
			final byte[] data = Randy.randomByteArray(1024 * 1024);
			final byte[] result = new byte[data.length];

			medias[0].setDataFromStream(new DataInputStream(new ByteArrayInputStream(data)));
			final InputStream in = medias[0].getDataFromStream();

			// in case the stream does not allow reading all data at once: iterate !
			for (int read = 0, offs = 0; read > 0; read = in.read(result, offs, data.length - offs))
			{
				offs += read;
			}
			assertTrue(Randy.compareByteArray(data, result));
		}
		catch (final IOException e)
		{
			fail("error while reading result media data : " + e);
		}
		catch (final JaloBusinessException e)
		{
			fail("error while reading result media data : " + e);
		}
	}

	@Test
	public void testProperties()
	{
		final String[] name = new String[size];
		final String[] prop = new String[size];
		final Randy r = new Randy();

		for (int i = 0; i < size; i++)
		{
			name[i] = "__" + i + "_" + r.distinctRandomString(20);
			prop[i] = Randy.randomString(100);
			media.setProperty(name[i], prop[i]);
		}

		for (int i = 0; i < size; i++)
		{
			assertEquals(prop[i], media.getProperty(name[i]));
			media.removeProperty(name[i]);
			assertNull(media.getProperty(name[i]));

			//assertTrue( "prop[i]="+prop[i]+", m.getProperty(name[i])="+m.getProperty(name[i])+".", !prop[i].equals(m.getProperty(name[i])) );
		}
	}

	@Test
	public void testGetData() throws Exception
	{
		Media somemedia = null;
		assertNotNull(somemedia = jaloSession.getMediaManager().createMedia("MediaData"));
		final byte[] data = new byte[]
		{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		somemedia.setData(data);

		final byte[] readData = somemedia.getData();

		final StringBuilder dataBuffer = new StringBuilder("[");
		for (int i = 0; i < data.length; i++)
		{
			dataBuffer.append(Byte.toString(data[i]) + ",");
		}
		dataBuffer.append("]");
		final StringBuilder readBuffer = new StringBuilder("[");
		for (int i = 0; i < readData.length; i++)
		{
			readBuffer.append(Byte.toString(readData[i]) + ",");
		}
		readBuffer.append("]");
		assertTrue("Expected " + dataBuffer + " but got " + readBuffer, Arrays.equals(data, readData));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testIsOnServer() throws JaloBusinessException
	{
		assertFalse(media.isOnServer());
		try
		{
			media.getData();
			fail("expected JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			// fine
		}
		media.setData(new byte[]
		{ 0, 1, 2, 3, 4, 5 });
		assertTrue(media.isOnServer());
		assertTrue(Arrays.equals(new byte[]
		{ 0, 1, 2, 3, 4, 5 }, media.getData()));
	}

	@Test
	public void testDescription() throws Exception
	{
		final String[] test = new String[size];
		for (int i = 0; i < size; i++)
		{
			test[i] = Randy.randomString(30);
			medias[i].setDescription(test[i]);
		}
		for (int i = 0; i < size; i++)
		{
			assertEquals(test[i], medias[i].getDescription());
		}
	}

	@Test
	public void testMoveData() throws Exception
	{
		final byte[] data = new byte[]
		{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		mediaTestMoveDataSource.setData(data);
		assertTrue(Arrays.equals(mediaTestMoveDataSource.getData(), data));

		mediaTestMoveDataSource.moveData(mediaTestMoveDataDest);

		try
		{
			mediaTestMoveDataSource.getData();
			fail("expected JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			// OK
		}

		assertTrue(Arrays.equals(mediaTestMoveDataDest.getData(), data));

		mediaTestMoveDataDest.moveData(mediaTestMoveDataSource);
		assertTrue(Arrays.equals(mediaTestMoveDataSource.getData(), data));

		try
		{
			mediaTestMoveDataDest.getData();
			fail("expected JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			// OK
		}

		try
		{
			mediaTestMoveDataDest.moveData(mediaTestMoveDataSource);
			fail("expected JaloSystemException");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine
		}
	}

	@Test
	public void testRemoveData() throws Exception
	{
		// given
		InputStream stream = null;
		try
		{
			mediaTestRemoveData.setData(getTestData());
			stream = mediaTestRemoveData.getDataFromStream();
			assertThat(stream).isNotNull();
		}
		finally
		{
			if (stream != null)
			{
				IOUtils.closeQuietly(stream);
			}
		}

		// when
		mediaTestRemoveData.removeData(true);

		try
		{
			// then
			mediaTestRemoveData.getDataFromStream();
		}
		catch (final MediaNotFoundException e)
		{
			// OK
		}
	}

	private byte[] getTestData()
	{
		return new byte[]
		{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	}

	@Test
	public void testSetDataByURL() throws Exception
	{
		final URL url = getClass().getResource("MediaTest.class");
		final String urlString = url.toString();
		final byte[] bytes = getBytes(url);
		mediaTestSetDataByURL.setURL(urlString);
		assertFalse(mediaTestSetDataByURL.hasData());
		assertEquals(urlString, mediaTestSetDataByURL.getURL());
		mediaTestSetDataByURL.setDataByURL();
		assertTrue(mediaTestSetDataByURL.hasData());
		assertFalse(urlString.equals(mediaTestSetDataByURL.getURL()));
		assertTrue("application/x-java-applet".equalsIgnoreCase(mediaTestSetDataByURL.getMime())
				|| "application/java".equalsIgnoreCase(mediaTestSetDataByURL.getMime()));
		assertTrue(mediaTestSetDataByURL.hasData());
		assertTrue(Randy.compareByteArray(bytes, mediaTestSetDataByURL.getData()));
		assertTrue(Randy.compareByteArray(bytes, getBytes(mediaTestSetDataByURL.getDataFromStream())));
		// not working with relative urls
		//assertEquals(bytes, getBytes(m.getURL()));
		try
		{
			mediaTestSetDataByURL.setDataByURL();
			fail("should have thrown exception");
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("media has already data", e.getMessage());
		}
	}

	@Test
	public void testMediaURL() throws Exception
	{
		if (Config.getBoolean("mode.dump", false))
		{
			if (log.isInfoEnabled())
			{
				log.info("Skipping MediaTest.testMediaURL");
			}
			return;
		}
		final char[] urlStringRaw = new char[1000];
		Arrays.fill(urlStringRaw, 'a');
		final String urlString = new String(urlStringRaw);
		m.setURL(urlString);
		assertEquals(urlString, m.getURL());
		assertEquals(DefaultMimeService.FALLBACK_MIME, m.getMime());
		try
		{
			m.getData();
			fail("should have thrown JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("Media has not data uploaded", e.getMessage());
		}

		assertThat(m.getDataFromStream()).isNull();
	}

	@Test
	public void testMediaData() throws Exception
	{
		m.setMime("major1/minor1");
		assertEquals("major1/minor1", m.getMime());
		final DataInputStream is1 = getStream(data1);
		m.setDataFromStream(is1);
		is1.close();

		assertEqualsByteArray(data1, m.getData());
		final InputStream is2 = m.getDataFromStream();
		assertEqualsByteArray(data1, getBytes(is2));
		is2.close();

		final DataInputStream is3 = getStream(data2);
		m.setDataFromStream(is3);
		is3.close();

		final InputStream is4 = m.getDataFromStream();
		assertEqualsByteArray(data2, getBytes(is4));
		is4.close();

		assertEqualsByteArray(data2, m.getData());
		final InputStream is5 = m.getDataFromStream();
		assertEqualsByteArray(data2, getBytes(is5));
		is5.close();

		m.setMime("major2/minor2");
		assertEquals("major2/minor2", m.getMime());
		assertEqualsByteArray(data2, m.getData());
		assertEqualsByteArray(data2, getBytes(m.getDataFromStream()));

		// test setting the same mime type
		m.setMime("major2/minor2");
		assertEquals("major2/minor2", m.getMime());
		assertEqualsByteArray(data2, m.getData());
		assertEqualsByteArray(data2, getBytes(m.getDataFromStream()));

		m.setDataFromStream(getStream(data3));

		assertEqualsByteArray(data3, m.getData());
		assertEqualsByteArray(data3, getBytes(m.getDataFromStream()));

		m.setData(data1);
		assertEqualsByteArray(data1, m.getData());
		assertEqualsByteArray(data1, getBytes(m.getDataFromStream()));
	}

	@Test
	public void testMoveDataTo() throws Exception
	{
		m.setDataFromStream(getStream(data1));
		assertNotNull(m2);
		m.moveDataTo(m2);
		assertEqualsByteArray(data1, m2.getData());
		assertEqualsByteArray(data1, getBytes(m2.getDataFromStream()));
		assertEquals(MediaUtil.URL_IS_EMPTY, m.getURL());
		try
		{
			m.getData();
			fail("should have thrown JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("Media has not data uploaded", e.getMessage());
		}

		// now test, if this works, if the target already has some data
		m.setDataFromStream(getStream(data2));
		m2.moveDataTo(m);
		assertEqualsByteArray(data1, m.getData());
		assertEqualsByteArray(data1, getBytes(m.getDataFromStream()));
		assertEquals(MediaUtil.URL_IS_EMPTY, m2.getURL());
		try
		{
			m2.getData();
			fail("should have thrown JaloBusinessException");
		}
		catch (final JaloBusinessException e)
		{
			assertEquals("Media has not data uploaded", e.getMessage());
		}
	}

	private DataInputStream getStream(final byte[] data) throws Exception
	{
		return new DataInputStream(new ByteArrayInputStream(data));
	}

	private static void assertEqualsByteArray(final byte[] expected, final byte[] actual)
	{
		assertEqualsByteArray("", expected, actual);
	}

	private static void assertEqualsByteArray(final String message, final byte[] expected, final byte[] actual)
	{
		if (!Arrays.equals(expected, actual))
		{
			if (expected.length != actual.length)
			{
				fail(message + " length differs: expected " + expected.length + ", but was " + actual.length);
			}
			final int length = expected.length;
			for (int i = 0; i < length; i++)
			{
				if (expected[i] != actual[i])
				{
					fail(message + " differs at position " + i + ": expected " + expected[i] + ", but was " + actual[i]);
				}
			}
			fail("should have failed until now");
		}
	}

	@Test
	public void testMediaTest() throws Exception
	{
		// tests for assertEquals(byte[], byte[])
		assertEquals(data1, data1);
		try
		{
			assertEquals(data1, data2);
			fail("should have thrown AssertionFailedError");
		}
		catch (final AssertionFailedError e)
		{

			// thats ok.
		}
		assertEqualsByteArray(data1, getBytes(new ByteArrayInputStream(data1)));
	}

	private byte[] getBytes(final URL url) throws Exception
	{
		return getBytes(url.openStream());
	}

	private byte[] getBytes(final InputStream in) throws Exception
	{
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024 * 64];
		int len;
		while ((len = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, len);
		}
		out.flush();
		in.close();

		return out.toByteArray();
	}

	@Test
	public void testSecureUrlWithRenderer() throws Exception
	{
		assertNotNull(secureMedia.getPK());
		final String pkAsStr = secureMedia.getPK().toString();

		//create custom renderer
		MediaUtil.setCurrentSecureMediaURLRenderer(media -> "/securemedias?mediaPK=" + media.getMediaPk());

		final DataInputStream is1 = getStream(data1);
		secureMedia.setDataFromStream(is1);
		is1.close();

		try
		{
			Config.setParameter("media.folder.root.secured", "true");
			final String url = secureMedia.getURL();
			assertEquals("/securemedias?mediaPK=" + pkAsStr, url);
		}
		finally
		{
			Config.setParameter("media.folder.root.secured", "false");
		}
	}

	@Test
	public void testSecureUrlNoRendererFound() throws Exception
	{
		final DataInputStream is1 = getStream(data1);
		secureMedia.setDataFromStream(is1);
		is1.close();

		assertNotNull(secureMedia.getPK());

		try
		{
			Config.setParameter("media.folder.root.secured", "true");
			MediaUtil.setCurrentSecureMediaURLRenderer(null);
			final String url = secureMedia.getURL();
			assertThat(url).isEmpty();
		}
		finally
		{
			Config.setParameter("media.folder.root.secured", "false");
		}
	}

	@Test
	public void testPermittedPrincipals() throws JaloBusinessException
	{
		final UserRight userRight = AccessManager.getInstance().getOrCreateUserRightByCode("read");
		final Media m = MediaManager.getInstance().createMedia("media123");
		final Collection<Principal> existingGrantedPrincipals = m.getPermittedPrincipals();
		assertThat(existingGrantedPrincipals).isEmpty();

		final Collection<Principal> principalsToBePermitted = new HashSet<Principal>();
		final User testUser1 = UserManager.getInstance().createUser("testUser1");
		final User testUser2 = UserManager.getInstance().createUser("testUser2");
		principalsToBePermitted.add(testUser1);
		principalsToBePermitted.add(testUser2);

		m.setPermittedPrincipals(principalsToBePermitted);
		assertCollection(principalsToBePermitted, m.getPermittedPrincipals());

		//check the positive permissions
		assertTrue(m.getPermissions(testUser1, false).contains(userRight.getPK()));
		assertTrue(m.getPermissions(testUser2, false).contains(userRight.getPK()));

		principalsToBePermitted.clear();
		final User testUser3 = UserManager.getInstance().createUser("testUser3");
		principalsToBePermitted.add(testUser3);
		m.setPermittedPrincipals(principalsToBePermitted);
		assertCollection(principalsToBePermitted, m.getPermittedPrincipals());
		assertTrue(m.getPermissions(testUser3, false).contains(userRight.getPK()));
	}

	@Test
	public void testDeniedPrincipals() throws JaloBusinessException
	{
		final UserRight userRight = AccessManager.getInstance().getOrCreateUserRightByCode("read");
		final Media m = MediaManager.getInstance().createMedia("media123");
		final Collection<Principal> existingDeniedPrincipals = m.getDeniedPrincipals();
		assertThat(existingDeniedPrincipals).isEmpty();

		final Collection<Principal> principalsToBeDenied = new HashSet<Principal>();
		final User testUser1 = UserManager.getInstance().createUser("testUser1");
		final User testUser2 = UserManager.getInstance().createUser("testUser2");
		principalsToBeDenied.add(testUser1);
		principalsToBeDenied.add(testUser2);

		m.setDeniedPrincipals(principalsToBeDenied);
		assertCollection(principalsToBeDenied, m.getDeniedPrincipals());

		//check the negative permissions
		assertTrue(m.getPermissions(testUser1, true).contains(userRight.getPK()));
		assertTrue(m.getPermissions(testUser2, true).contains(userRight.getPK()));

		principalsToBeDenied.clear();
		final User testUser3 = UserManager.getInstance().createUser("testUser3");
		principalsToBeDenied.add(testUser3);
		m.setDeniedPrincipals(principalsToBeDenied);
		assertCollection(principalsToBeDenied, m.getDeniedPrincipals());
		assertTrue(m.getPermissions(testUser3, true).contains(userRight.getPK()));
	}

	@Test
	public void testFileNameForPrint() throws JaloBusinessException
	{
		final String sep = System.getProperty("file.separator");
		final Media m = MediaManager.getInstance().createMedia("pritest");
		assertNotNull(m);
		m.setURL("/medias/fromjar/blup");
		assertEquals("blup", m.getFileNameForFileBasedSoftware());
		m.setURL("/medias/fromjar/here/we/go.gif");
		assertEquals("here" + sep + "we" + sep + "go.gif", m.getFileNameForFileBasedSoftware());
		m.setURL("/NOmedias/fromjar/here/we/go.gif");
		assertEquals("NOmedias" + sep + "fromjar" + sep + "here" + sep + "we" + sep + "go.gif",
				m.getFileNameForFileBasedSoftware());
	}

	@Test
	public void testRemovable()
	{
		Media m = null;
		assertNotNull(m = MediaManager.getInstance().createMedia("removetest"));
		try
		{
			m.remove();
			assertNotNull(m);
		}
		catch (final ConsistencyCheckException e)
		{
			fail("ConsistencyCheckException was not expected");
		}

		assertNotNull(m = MediaManager.getInstance().createMedia("removetest"));
		m.setRemovable(false);
		try
		{
			m.remove();
			assertNotNull(m);
			fail("ConsistencyCheckException was expected but not thrown");
		}
		catch (final ConsistencyCheckException e)
		{
			// OK
		}

		m.setRemovable(true);
		try
		{
			m.remove();
			assertNotNull(m);
		}
		catch (final ConsistencyCheckException e)
		{
			fail("ConsistencyCheckException was not expected");
		}
	}

	@Test
	public void testMediaFolder() throws JaloBusinessException
	{
		final Media media = MediaManager.getInstance().createMedia("test");
		final MediaFolder folder = MediaManager.getInstance().createMediaFolder("test", "test");
		media.setData(new DataInputStream(new ByteArrayInputStream("test".getBytes())), "test", "test", folder);
		assertNotNull(media.getData());
	}
}
