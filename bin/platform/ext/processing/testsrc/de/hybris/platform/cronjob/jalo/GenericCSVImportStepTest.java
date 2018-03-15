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
package de.hybris.platform.cronjob.jalo;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class GenericCSVImportStepTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(GenericCSVImportStepTest.class.getName());



	private CronJobManager manager;
	private final GenericCSVImportStepClon genericCSVImportStep = new GenericCSVImportStepClon();

	private final String filename_start = "csvtest-";
	private final String filename_ending = ".csv";
	private final String path_to_testfile = "/cronjob/unittest/";
	private final String unescaped = "_unescaped";

	private BatchJob batchJob;
	private MediaProcessCronJob mediaProcessCronJob;
	private JobMedia jobMedia;

	private final List result = new ArrayList();

	private Map datenzeile1;
	private Map datenzeile2;
	private Map datenzeile3;
	private Map datenzeile4;
	private Map datenzeile5;
	private Map datenzeile6;
	private Map datenzeile7;


	@Before
	public void setUp() throws Exception
	{
		manager = (CronJobManager) jaloSession.getExtensionManager().getExtension(CronJobConstants.EXTENSIONNAME);
		//generate possible result rows
		datenzeile1 = new HashMap();
		datenzeile1.put(Integer.valueOf(0), "das");
		datenzeile1.put(Integer.valueOf(1), "ist");
		datenzeile1.put(Integer.valueOf(2), "ein");
		datenzeile1.put(Integer.valueOf(3), "header");

		datenzeile2 = new HashMap();
		datenzeile2.put(Integer.valueOf(0), "eine");
		datenzeile2.put(Integer.valueOf(1), "Daten");
		datenzeile2.put(Integer.valueOf(2), "zeile");

		datenzeile3 = new HashMap();
		datenzeile3.put(Integer.valueOf(0), "noch");
		datenzeile3.put(Integer.valueOf(1), "eine");
		datenzeile3.put(Integer.valueOf(2), "Daten");
		datenzeile3.put(Integer.valueOf(3), "zeile");

		datenzeile4 = new HashMap();
		datenzeile4.put(Integer.valueOf(0), "ein paar sonderzeichen:");
		datenzeile4.put(Integer.valueOf(1), "\u00e4\u00f6\u00fc\u00df");
		datenzeile4.put(Integer.valueOf(2), "\"");
		datenzeile4.put(Integer.valueOf(3), "\\\\");

		datenzeile5 = new HashMap();
		datenzeile5.put(Integer.valueOf(0), "eine");
		datenzeile5.put(Integer.valueOf(1), "Daten");
		datenzeile5.put(Integer.valueOf(2), "zeile");
		datenzeile5.put(Integer.valueOf(3), "nach");
		datenzeile5.put(Integer.valueOf(4), "einer");
		datenzeile5.put(Integer.valueOf(5), "Leer zeile");

		datenzeile6 = new HashMap();
		datenzeile6.put(Integer.valueOf(0), "\u20ac");

		datenzeile7 = new HashMap();
		datenzeile7.put(Integer.valueOf(0), "eine");
		datenzeile7.put(Integer.valueOf(1), "Daten");
		datenzeile7.put(Integer.valueOf(2), "zeile");
		datenzeile7.put(Integer.valueOf(3), "nach viel platz");
	}

	/**
	 * haupttestmethode
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGenericCSVImportStep() throws Exception
	{
		//default settings testen
		assertFalse(genericCSVImportStep.skipFirstLine());
		assertTrue(Arrays.equals(genericCSVImportStep.getTokenCharacters(), new char[]
		{ CronJobConstants.CHARS.VERTICAL_LINE }));
		assertTrue(Arrays.equals(genericCSVImportStep.getCommentOutCharacters(), new char[]
		{ CronJobConstants.CHARS.POUND, CronJobConstants.CHARS.PERCENT }));


		//encodings testen
		final List soll1 = new ArrayList();
		soll1.add(datenzeile1);
		soll1.add(datenzeile2);
		soll1.add(datenzeile3);
		soll1.add(datenzeile4);
		soll1.add(datenzeile5);
		soll1.add(datenzeile6);
		soll1.add(datenzeile7);
		testeMitEncoding("UTF-8", soll1, 0, false);
		testeMitEncoding("UTF-8", soll1, 0, true);

		testeMitEncoding("UTF-16", soll1, 0, false);
		testeMitEncoding("UTF-16", soll1, 0, true);

		final List soll2 = new ArrayList();
		soll2.add(datenzeile1);
		soll2.add(datenzeile2);
		soll2.add(datenzeile3);
		soll2.add(datenzeile4);
		soll2.add(datenzeile5);
		soll2.add(datenzeile7);
		testeMitEncoding("ISO-8859-1", soll2, 0, false);
		testeMitEncoding("ISO-8859-1", soll2, 0, true);

		testeMitEncoding("UTF-16BE", soll1, 0, false);
		testeMitEncoding("UTF-16BE", soll1, 0, true);

		testeMitEncoding("UTF-16LE", soll1, 0, false);
		testeMitEncoding("UTF-16LE", soll1, 0, true);

		final List soll3 = new ArrayList();
		soll3.add(datenzeile1);
		soll3.add(datenzeile2);
		soll3.add(datenzeile3);
		soll3.add(datenzeile5);
		soll3.add(datenzeile7);
		testeMitEncoding("US-ASCII", soll3, 0, false);
		testeMitEncoding("US-ASCII", soll3, 0, true);

		//header rausschneiden
		soll1.remove(0);
		testeMitEncoding("UTF-8", soll1, 1, false);
		testeMitEncoding("UTF-8", soll1, 1, true);
		testeMitEncoding("UTF-16", soll1, 1, false);
		testeMitEncoding("UTF-16", soll1, 1, true);
		soll2.remove(0);
		testeMitEncoding("ISO-8859-1", soll2, 1, false);
		testeMitEncoding("ISO-8859-1", soll2, 1, true);
		testeMitEncoding("UTF-16BE", soll1, 1, false);
		testeMitEncoding("UTF-16BE", soll1, 1, true);
		testeMitEncoding("UTF-16LE", soll1, 1, false);
		testeMitEncoding("UTF-16LE", soll1, 1, true);
		soll3.remove(0);
		testeMitEncoding("US-ASCII", soll3, 1, false);
		testeMitEncoding("US-ASCII", soll3, 1, true);

		//nur die letzte zeile soll rauskommen
		final List soll4 = new ArrayList();
		soll4.add(0, datenzeile7);
		testeMitEncoding("UTF-8", soll4, 15, false);
		testeMitEncoding("UTF-8", soll4, 15, true);
		testeMitEncoding("UTF-16", soll4, 15, false);
		testeMitEncoding("UTF-16", soll4, 15, true);
	}

	private void testeMitEncoding(final String encoding, final List soll, final int skipLine, final boolean un) throws Exception
	{
		final String filename = path_to_testfile + filename_start + encoding + (un ? unescaped : "") + filename_ending;
		final InputStream is = CronJobManager.class.getResourceAsStream(filename);

		final Map params = new HashMap();

		//		batchjob erzeugen
		params.clear();
		params.put(BatchJob.CODE, (encoding + (un ? unescaped : "") + "batchjob-skipline-" + skipLine));
		batchJob = manager.createBatchJob(params);
		assertNotNull(batchJob);

		//		ein! media erstellen
		params.clear();
		// must ensure unique media codes!!!
		params.put(JobMedia.CODE, filename+"-"+UUID.randomUUID());
		jobMedia = manager.createJobMedia(params);
		jobMedia.setData(new DataInputStream(is), filename, filename_ending);
		assertNotNull(jobMedia);

		//		mediaProcessCronJob erzeugen mit dem media und batchjob
		params.clear();
		params.put(MediaProcessCronJob.CODE, (encoding + (un ? unescaped : "") + "mediaProcessCronJob" + skipLine));
		params.put(MediaProcessCronJob.JOB, batchJob);
		params.put(MediaProcessCronJob.ACTIVE, Boolean.TRUE);
		params.put(MediaProcessCronJob.JOBMEDIA, jobMedia);
		mediaProcessCronJob = manager.createMediaProcessCronJob(params);
		mediaProcessCronJob.setJobMedia(jobMedia);
		assertThat(mediaProcessCronJob.getJobMedia()).isEqualTo(jobMedia);
		assertThat(mediaProcessCronJob.getJobMedia().hasData()).isTrue();
		mediaProcessCronJob.setLastSuccessfulLine(skipLine);
		assertNotNull(mediaProcessCronJob);

		result.clear();
		genericCSVImportStep.setCharSet(encoding);
		genericCSVImportStep.performStep(mediaProcessCronJob);
		if ("ISO-8859-1".equals(encoding))
		{
			if (skipLine == 0)
			{
				result.remove(5);
			}
			if (skipLine == 1)
			{
				result.remove(4);
			}
		}
		if ("US-ASCII".equals(encoding))
		{
			if (skipLine == 0)
			{
				result.remove(5);
				result.remove(3);
			}
			if (skipLine == 1)
			{
				result.remove(4);
				result.remove(2);
			}
		}
		assertCollection(soll, result);
	}

	public class GenericCSVImportStepClon extends GenericCSVImportStep
	{
		private String encoding = "UTF-8";

		@Override
		protected boolean processObjects(final Map map, final CronJob cronJob) throws JaloBusinessException
		{
			result.add(map);
			return true;
		}

		@Override
		protected void undoStep(final CronJob cronJob)
		{
			// DOCTODO Document reason, why this block is empty
		}

		private void setCharSet(final String encoding)
		{
			this.encoding = encoding;
		}

		@Override
		protected String getCharSet()
		{
			return this.encoding;
		}

		@Override
		public boolean isDebugEnabled()
		{
			return false;
		}

		@Override
		public boolean isInfoEnabled()
		{
			return false;
		}

	}
}
