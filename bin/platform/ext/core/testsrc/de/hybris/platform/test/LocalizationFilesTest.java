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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests different attributes of the localization-files. Following types of localization files are tested:
 * "*.properties", "*.xml" and "*.zul"
 */
@IntegrationTest
public class LocalizationFilesTest extends HybrisJUnit4Test
{
	private static final Logger LOG = Logger.getLogger(LocalizationFilesTest.class);
	private static List<File> allLocalizationFiles = new LinkedList<File>();
	private static int prePathLength = 0;

	// for performance reasons: just scan once !!!
	@BeforeClass
	public static void setUp()
	{
		final LocalizationFileGrabber locGrabber = new LocalizationFileGrabber();
		allLocalizationFiles = locGrabber.grabFiles();
		prePathLength = locGrabber.prePathLength;
	}

	@AfterClass
	public static void tearDown()
	{
		allLocalizationFiles = null;
		prePathLength = 0;
	}

	/**
	 * Checks if the file was formatted correctly in UTF-8.
	 */
	@Test
	public void testEncodedInUTF8()
	{
		boolean successfulTest = true;
		final List<String> failMessages = new LinkedList<String>();

		for (final File file : allLocalizationFiles)
		{
			try
			{
				final FileInputStream fileStream = new FileInputStream(file);
				final InputStreamReader inputStream = new InputStreamReader(fileStream);
				final BufferedReader reader = new BufferedReader(inputStream);
				final byte[] bytes = new byte[1000000];

				fileStream.read(bytes);
				if (!isValidUTF8(bytes))
				{
					final String message = "The file does not have the correct encoding: "
							+ file.getAbsolutePath().substring(prePathLength);
					LOG.error(message);
					failMessages.add(message);
					successfulTest = false;
				}

				reader.close();
				inputStream.close();
				fileStream.close();
			}
			catch (final FileNotFoundException fnFE)
			{
				final String message = "The file was removed while testing: " + file.getAbsolutePath().substring(prePathLength);
				LOG.error(message);
				failMessages.add(message);
				successfulTest = false;
			}
			catch (final IOException e)
			{
				final String message = "There was an error while reading the file: "
						+ file.getAbsolutePath().substring(prePathLength);
				LOG.error(message);
				failMessages.add(message);
				successfulTest = false;
			}
		}
		final StringBuilder failedMessage = new StringBuilder();
		for (final String message : failMessages)
		{
			failedMessage.append(message + "\n");
		}
		failedMessage.append(failMessages.size() + " Files in total.");
		assertTrue(failedMessage.toString(), successfulTest);
	}

	/**
	 * Tests if the XML-specific UTF-8 Tag is present.
	 */
	@Test
	public void testCorrectXmlUTF8Tag()
	{
		boolean successfulTest = true;
		final List<String> failMessages = new LinkedList<String>();

		for (final File file : allLocalizationFiles)
		{
			if (file == null)
			{
				continue;
			}
			final String fileName = file.getName().toLowerCase(Locale.getDefault());
			if (!(fileName.endsWith(".xml") || fileName.endsWith(".zul")))
			{
				continue;
			}

			BufferedReader reader;
			try
			{
				reader = new BufferedReader(new FileReader(file));
				String line = "";
				do
				{
					if (line.contains("<"))
					{
						final String splitLine = line.split("<")[1];
						if (splitLine.startsWith("?xml version=") && splitLine.contains("encoding=\"UTF-8\"?>"))
						{
							break;
						}
						else
						{
							final String message = "This file is missing the correct UTF-8 XML-Tag: "
									+ file.getAbsolutePath().substring(prePathLength);
							failMessages.add(message);
							LOG.error(message);
							successfulTest = false;
							break;
						}
					}

					line = reader.readLine();
				}
				while (line != null);
			}
			catch (final FileNotFoundException fnFE)
			{
				final String message = "The file was removed while testing: " + file.getAbsolutePath().substring(prePathLength);
				failMessages.add(message);
				LOG.error(message);
				successfulTest = false;
			}
			catch (final IOException e)
			{
				final String message = "There was an error while reading the file: "
						+ file.getAbsolutePath().substring(prePathLength);
				failMessages.add(message);
				LOG.error(message);
				successfulTest = false;
			}
		}
		final StringBuilder failedMessage = new StringBuilder();
		for (final String message : failMessages)
		{
			failedMessage.append(message + "\n");
		}
		failedMessage.append(failMessages.size() + " Files in total.");
		assertTrue(failedMessage.toString(), successfulTest);
	}

	/**
	 * Tests if there are XML-files present. At this time if there are XML-files within alllocalizationFiles, Jira Issue
	 * PLA-11692 counts as not resolved.
	 */
	@Test
	public void testXmlNotUIConfigFile()
	{
		boolean successfulTest = true;
		final List<String> failMessages = new LinkedList<String>();

		for (final File file : allLocalizationFiles)
		{
			if (file.getName().toLowerCase(Locale.getDefault()).endsWith(".xml"))
			{
				failMessages.add("This cockpit-ui config file has a forbidden \"<label lang=\"-tag: "
						+ file.getAbsolutePath().substring(prePathLength));
				successfulTest = false;
			}
		}
		final StringBuilder failedMessage = new StringBuilder();
		for (final String message : failMessages)
		{
			failedMessage.append(message + "\n");
		}
		failedMessage.append(failMessages.size() + " Files in total.");
		assertTrue(failedMessage.toString(), successfulTest);
	}

	/**
	 * Tests if the property-files are Byte-Order-Mark-less because this could cause problems. It fails if a file is
	 * detected with a BOM.
	 */
	@Test
	public void testIfWithoutByteOrderMark()
	{
		boolean successfulTest = true;
		final List<String> failMessages = new LinkedList<String>();
		final String failMessage = "This file has the UTF-8 Byte-Order-Mark: ";

		for (final File file : allLocalizationFiles)
		{
			if (!file.getName().endsWith(".properties"))
			{
				continue;
			}
			try
			{
				final InputStream fileIS = new FileInputStream(file);

				final int[] utf8BOM = new int[]
				{ 0xEF, 0xBB, 0xBF };
				final int[] fileBOM = new int[]
				{ fileIS.read(), fileIS.read(), fileIS.read() };
				fileIS.close();

				if (Arrays.equals(fileBOM, utf8BOM))
				{
					failMessages.add(failMessage + file.getAbsolutePath().substring(prePathLength));
					successfulTest = false;
				}
			}
			catch (final FileNotFoundException e)
			{
				failMessages.add("The read file was removed during the test:" + file.getAbsolutePath().substring(prePathLength));
				successfulTest = false;
			}
			catch (final IOException ioE)
			{
				failMessages
						.add("There was an IO error while reading in the file:" + file.getAbsolutePath().substring(prePathLength));
				successfulTest = false;
			}
		}
		final StringBuilder failedMessage = new StringBuilder();
		for (final String message : failMessages)
		{
			failedMessage.append(message + "\n");
		}
		failedMessage.append(failMessages.size() + " Files in total.");
		assertTrue(failedMessage.toString(), successfulTest);
	}

	/**
	 * Test to determine if files have a Byte-Order-Mark and fails if they don't. Adding a Byte-Order-Mark to all
	 * property-files has been discussed and dismissed, because it causes more problems than it solves. Therefore this
	 * test is deactivated.
	 */
	@Ignore("PLA-11695 Activate in case Byte-Order-Mark has to be part of localization files.")
	public void testLocalizationFilesHaveCharsetByteOrderMark()
	{
		boolean successfulTest = true;
		final List<String> failMessages = new LinkedList<String>();
		final String failMessage = "This file is missing the UTF-8 Byte-Order-Mark: ";

		for (final File file : allLocalizationFiles)
		{
			if (!file.getName().endsWith(".properties"))
			{
				continue;
			}
			try
			{
				final InputStream fileIS = new FileInputStream(file);

				final int[] utf8BOM = new int[]
				{ 0xEF, 0xBB, 0xBF };
				final int[] fileBOM = new int[]
				{ fileIS.read(), fileIS.read(), fileIS.read() };
				fileIS.close();

				if (!Arrays.equals(fileBOM, utf8BOM))
				{
					failMessages.add(failMessage + file.getAbsolutePath().substring(prePathLength));
					successfulTest = false;
				}
			}
			catch (final FileNotFoundException e)
			{
				failMessages.add("The read file was removed during the test: " + file.getAbsolutePath().substring(prePathLength));
				successfulTest = false;
			}
			catch (final IOException ioE)
			{
				failMessages.add("There was an IO error while reading in the file: "
						+ file.getAbsolutePath().substring(prePathLength));
				successfulTest = false;
			}
		}
		final StringBuilder failedMessage = new StringBuilder();
		for (final String message : failMessages)
		{
			failedMessage.append(message + "\n");
		}
		failedMessage.append(failMessages.size() + " Files in total.");
		assertTrue(failedMessage.toString(), successfulTest);
	}

	/**
	 * Helper-class to grab all localization-files independent of their location.
	 */
	private static class LocalizationFileGrabber
	{
		private String workingDir;
		private List<ExtensionInfo> extensions;
		private int prePathLength = 0;

		private final BlockingQueue<File> fileList = new LinkedBlockingQueue<File>();

		private static final String[] keywords = new String[]
		{ "locales", "label", "welcome", "messages" };

		/**
		 * protected final ThreadPoolExecutor tPoolExecutor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new
		 * LinkedBlockingQueue<Runnable>());
		 */
		public LocalizationFileGrabber()
		{
			try
			{
				final PlatformConfig config = ConfigUtil.getPlatformConfig(Utilities.class);
				extensions = config.getExtensionInfosInBuildOrder();
				workingDir = config.getSystemConfig().getPlatformHome().getParentFile().getCanonicalPath();
				prePathLength = workingDir.length();
			}
			catch (final IOException ioe)
			{
				final File file = new File("");
				workingDir = file.getAbsolutePath().split("platform")[0];
			}
		}

		/**
		 * Grabs all property files with the keywords "locales", "label", "welcome" and "messages" and xml-files with the
		 * String "label name=", adds their references to a List and returns that list.
		 * 
		 * @return File-reference list with all localization-files within the extension folders.
		 */
		public List<File> grabFiles()
		{
			final File workingDirectory = new File(workingDir);
			if (!extensions.isEmpty())
			{
				for (final ExtensionInfo extension : extensions)
				{
					if(extension.isExternalExtension())
					{
						continue;
					}
					locateFiles(extension.getExtensionDirectory());
				}
				locateFiles(new File(new File(workingDirectory, "platform"), "extgen"));
			}
			else
			{
				throw new IllegalArgumentException("The path for the extensions could not be determined");
			}

			final List<File> fileLinkedList = new LinkedList<File>();

			fileList.drainTo(fileLinkedList);

			return fileLinkedList;
		}

		static final FileFilter[] fileFilters = new FileFilter[]
		{ new XMLFileFilter(), new PropertyAndZulFileFilter(), new CSVAndImpExFileFilter() };

		static final FileFilter directoryFilter = new DirectoryFilter();

		void locateFiles(final File path)
		{
			if (path.isDirectory())
			{
				for (final FileFilter filter : fileFilters)
				{
					for (final File f : path.listFiles(filter))
					{
						if (!fileList.contains(f))
						{
							fileList.add(f);
						}
					}
				}
				for (final File folder : path.listFiles(directoryFilter))
				{
					locateFiles(folder);
				}
			}
		}

		private static class DirectoryFilter implements FileFilter
		{
			@Override
			public boolean accept(final File file)
			{
				return file.isDirectory();
			}
		}

		private static class XMLFileFilter implements FileFilter
		{
			@SuppressWarnings("resource")
			@Override
			public boolean accept(final File file)
			{
				final String fileName = file.getName().toLowerCase(Locale.getDefault());
				if (!fileName.endsWith(".xml") || "localization.xml".equals(fileName))
				{
					return false;
				}

				BufferedReader reader = null;
				try
				{
					reader = new BufferedReader(new FileReader(file));
					for (String line = reader.readLine(); line != null; line = reader.readLine())
					{
						if (line.toLowerCase(Locale.getDefault()).contains("label lang="))
						{
							return true;
						}
					}
				}
				catch (final FileNotFoundException fnFE)
				{
					return false;
				}
				catch (final IOException e)
				{
					// 
				}
				finally
				{
					IOUtils.closeQuietly(reader);
				}
				return false;
			}
		}

		private static class PropertyAndZulFileFilter implements FileFilter
		{
			@Override
			public boolean accept(final File file)
			{
				final String fileName = file.getName().toLowerCase(Locale.getDefault());
				if (fileName.endsWith(".properties") || fileName.endsWith(".zul"))
				{
					for (final String keyword : keywords)
					{
						if (fileName.contains(keyword))
						{
							return true;
						}
					}
				}
				return false;
			}
		}

		private static class CSVAndImpExFileFilter implements FileFilter
		{
			@Override
			public boolean accept(final File file)
			{
				final String fileName = file.getName().toLowerCase(Locale.getDefault());
				if ((fileName.endsWith(".impex") || fileName.endsWith(".csv"))
						&& fileName.lastIndexOf('.') - 3 == fileName.lastIndexOf('_'))
				{
					return true;
				}
				return false;
			}
		}
	}

	/**
	 * Checks if given byte array is correctly UTF-8 encoded.
	 * 
	 * @param bytes
	 *           That have been read from somewhere.
	 * @return true when valid UTF8 encoded.
	 */
	public static boolean isValidUTF8(final byte[] bytes)
	{
		try
		{
			Charset.availableCharsets().get("UTF-8").newDecoder().decode(ByteBuffer.wrap(bytes));
		}
		catch (final CharacterCodingException e)
		{
			return false;
		}
		return true;
	}
}
