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
package de.hybris.platform.impex.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.DefaultCronJobMediaDataHandler;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;



/**
 * Test class for testing implemented MediaDataHandler and whole media import process.
 */
@IntegrationTest
public class ImpExMediasImportTest extends AbstractImpExTest
{

	/**
	 * Performs a simple media import using a simple media handler, which sets the import media path as media content (no
	 * external media needed).
	 */
	@Test
	public void testSimpleMediaImport()
	{
		// create media handler
		MediaDataTranslator.setMediaDataHandler(new MediaDataHandler()
		{
			@Override
			public void importData(final Media media, final String path)
			{
				try
				{
					media.setData(path.getBytes());
				}
				catch (final JaloBusinessException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public String exportData(final Media media)
			{
				return "foobar";
			}

			@Override
			public void cleanUp()
			{
				// DOCTODO Document reason, why this block is empty
			}
		});

		// import test media
		final String lines = "INSERT_UPDATE Media; " + Media.CODE + "[unique=true];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX
				+ "media[" + ImpExConstants.Syntax.Modifier.TRANSLATOR + "=" + MediaDataTranslator.class.getName() + "] \n"
				+ "# empty line \n" + "; impex; path_to_media:media.jpg";
		final ImpExImportReader impExImportReader = new ImpExImportReader(lines);
		impExImportReader.setValidationMode(ImpExManager.getImportRelaxedMode());

		Media media = null;
		try
		{
			media = (Media) impExImportReader.readLine();
			assertNotNull(media);
			assertEquals(new String(media.getData()), new String("path_to_media:media.jpg".getBytes())); // NOPMD
		}
		catch (final ImpExException e)
		{
			fail(e.getMessage());
		}
		catch (final JaloBusinessException e)
		{
			fail(e.getMessage());
		}
		finally
		{
			MediaDataTranslator.unsetMediaDataHandler();
		}
	}

	/**
	 * Tests the <code>importData</code> method of {@link DefaultMediaDataHandler} with different file formats.
	 */
	@Test
	public void testDefaultMediaDataHandler()
	{
		final Media media = MediaManager.getInstance().createMedia("testMedia");
		final MediaDataHandler handler = new DefaultMediaDataHandler();
		mediaImportFromAbsolute(handler, media);
		mediaImportFromZip(handler, media);
		mediaImportFromJar(handler, media);
		mediaImportFromUrl(handler, media);
	}

	/**
	 * Tests the <code>importData</code> method of {@link DefaultCronJobMediaDataHandler} with different file formats.
	 */
	@Test
	public void testDefaultCronjobMediaDataHandler()
	{
		final Media media = MediaManager.getInstance().createMedia("testMediaCron");
		final ImpExImportJob job = ImpExManager.getInstance().getOrCreateImpExImportJob();
		final ImpExImportCronJob cronJob = ImpExManager.getInstance().createDefaultImpExImportCronJob(job);
		final MediaDataHandler handler = new DefaultCronJobMediaDataHandler(cronJob);

		mediaImportFromAbsolute(handler, media);

		mediaImportFromZip(handler, media);

		mediaImportFromMediasMedia(handler, media, cronJob);

		cronJob.setUnzipMediasMedia(true);
		mediaImportFromMediasMedia(handler, media, cronJob);
		cronJob.setUnzipMediasMedia(false);

		mediaImportFromJar(handler, media);

		mediaImportFromUrl(handler, media);
	}

	/**
	 * Calls the <code>importData</code> method of given handler with an absolute file path in different formats. Tests
	 * path formats in windows and unix notation as well as in relative and absolute.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 */
	private void mediaImportFromAbsolute(final MediaDataHandler handler, final Media media)
	{
		File testFile = null;
		try
		{
			testFile = File.createTempFile("mediaImportTest", ".txt");
			final PrintWriter printer = new PrintWriter(testFile);
			printer.print("testest");
			printer.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		final String unixPathRel = ImpExConstants.Syntax.ABSOLUTE_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToUnix(testFile.getPath());
		final String unixPathAbs = ImpExConstants.Syntax.ABSOLUTE_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToUnix(testFile.getAbsolutePath());
		final String winPathRel = ImpExConstants.Syntax.ABSOLUTE_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToWindows(testFile.getPath());
		final String winPathAbs = ImpExConstants.Syntax.ABSOLUTE_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToWindows(testFile.getAbsolutePath());

		mediaImport(handler, media, unixPathRel, "testest");
		mediaImport(handler, media, unixPathAbs, "testest");
		mediaImport(handler, media, winPathRel, "testest");
		mediaImport(handler, media, winPathAbs, "testest");
		if (!testFile.delete())
		{
			fail("Can not delete temp file: " + testFile.getPath());
		}
	}

	/**
	 * Calls the <code>importData</code> method of given handler with an zip-file path in different formats.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 */
	private void mediaImportFromZip(final MediaDataHandler handler, final Media media)
	{
		File testFile = null;
		try
		{
			testFile = File.createTempFile("mediaImportTest", ".zip");
			final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testFile));
			zos.putNextEntry(new ZipEntry(new File("files", "dummy.txt").getPath()));
			zos.putNextEntry(new ZipEntry(new File("files", "test.txt").getPath()));
			final PrintWriter printer = new PrintWriter(zos);
			printer.print("testest");
			printer.flush();
			zos.flush();
			printer.close();
			zos.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		final String unixPathRel = ImpExConstants.Syntax.ZIP_BASED_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToUnix(testFile.getPath()) + "&files/test.txt";
		final String unixPathAbs = ImpExConstants.Syntax.ZIP_BASED_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToUnix(testFile.getAbsolutePath()) + "&files/test.txt";
		final String winPathRel = ImpExConstants.Syntax.ZIP_BASED_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToWindows(testFile.getPath()) + "&files\\test.txt";
		final String winPathAbs = ImpExConstants.Syntax.ZIP_BASED_FILE_PATH_PREFIX
				+ FilenameUtils.separatorsToWindows(testFile.getAbsolutePath()) + "&files\\test.txt";
		try
		{
			mediaImport(handler, media, unixPathRel, "testest");
			mediaImport(handler, media, unixPathAbs, "testest");
			mediaImport(handler, media, winPathRel, "testest");
			mediaImport(handler, media, winPathAbs, "testest");
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
		if (!testFile.delete())
		{
			fail("Can not delete temp file: " + testFile.getPath());
		}
	}

	/**
	 * Calls the <code>importData</code> method of given handler with an zip-file path in different formats.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 */
	private void mediaImportFromMediasMedia(final MediaDataHandler handler, final Media media, final ImpExImportCronJob cronJob)
	{
		MediaDataHandler myHandler = handler;
		File testFile = null;
		try
		{
			testFile = File.createTempFile("mediaImportTest", ".zip");
			final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testFile));
			zos.putNextEntry(new ZipEntry(new File("notunzip\\notexist.txt").getPath()));
			zos.putNextEntry(new ZipEntry(new File("files\\dummy.txt").getPath()));
			zos.putNextEntry(new ZipEntry(new File("files\\test.txt").getPath()));
			final PrintWriter printer = new PrintWriter(zos);
			printer.print("testest");
			printer.flush();
			zos.flush();
			printer.close();
			zos.close();
		}
		catch (final IOException e)
		{
			fail(e.getMessage());
		}
		try
		{
			final Media mediasMedia = ImpExManager.getInstance().createImpExMedia("mediasMedia_" + UUID.randomUUID(), "UTF-8",
					new FileInputStream(testFile));
			cronJob.setMediasMedia(mediasMedia);

			mediaImport(myHandler, media, "files/test.txt", "testest");
			myHandler.cleanUp();
			myHandler = new DefaultCronJobMediaDataHandler(cronJob);
			mediaImport(myHandler, media, "files\\test.txt", "testest");
			myHandler.cleanUp();
			cronJob.setMediasTarget("files");
			myHandler = new DefaultCronJobMediaDataHandler(cronJob);
			mediaImport(myHandler, media, "test.txt", "testest");
			myHandler.cleanUp();
			cronJob.setMediasTarget(null);
		}
		catch (final Exception e)
		{
			fail(e.getMessage());
		}
		if (!testFile.delete())
		{
			fail("Can not delete temp file: " + testFile.getPath());
		}
	}

	/**
	 * Calls the <code>importData</code> method of given handler with an jar-file path.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 */
	private void mediaImportFromJar(final MediaDataHandler handler, final Media media)
	{
		final String path = ImpExConstants.Syntax.JAR_BASED_FILE_PATH_PREFIX + ImpExManager.class.getName()
				+ "&/impex/testfiles/testcases.csv";
		mediaImport(handler, media, path, null);
		final String path2 = ImpExConstants.Syntax.JAR_BASED_FILE_PATH_PREFIX + "/impex/testfiles/testcases.csv";
		mediaImport(handler, media, path2, null);
	}

	/**
	 * Calls the <code>importData</code> method of given handler with an url path.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 */
	private void mediaImportFromUrl(@SuppressWarnings("unused") final MediaDataHandler handler,
			@SuppressWarnings("unused") final Media media)
	{
		// XXX: Need an media import from URL test (URL has to be available always)
		// String path = ImpExConstants.Syntax.URL_BASED_FILE_PATH_PREFIX + "http://localhost:9001/impex/images/transp.gif";
		// mediaImport( handler, m, path, null );
	}

	/**
	 * Calls the <code>importData</code> method of given handler with given path.
	 * 
	 * @param handler
	 *           handler which will be used for test
	 * @param media
	 *           media where the data will be imported to
	 * @param path
	 *           path to data for import
	 * @param toCheck
	 *           media data will be checked for this text after import, if not null
	 */
	private void mediaImport(final MediaDataHandler handler, final Media media, final String path, final String toCheck)
	{
		try
		{
			handler.importData(media, path);
			assertTrue(media.hasData());
			if (toCheck != null)
			{
				assertEquals(toCheck, new String(media.getData()));
			}
			media.removeData(true);
			assertNull(media.getSize());
			assertNull(media.getURL());
		}
		catch (final JaloBusinessException e)
		{
			fail("Error while testing url: " + path + " with handler " + handler.getClass().getName() + ": " + e.getMessage());
		}
	}
}
