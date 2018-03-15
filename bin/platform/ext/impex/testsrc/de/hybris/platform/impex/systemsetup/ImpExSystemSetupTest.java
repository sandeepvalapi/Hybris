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
package de.hybris.platform.impex.systemsetup;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class ImpExSystemSetupTest extends HybrisJUnit4Test
{
	private static final Logger log = Logger.getLogger(ImpExSystemSetupTest.class);

	private MyImpExSystemSetup testSetup;
	private static String expectedFile = "";
	private static String[] invalidFile = new String[2];
	private final SystemSetupContext ctx = new SystemSetupContext(Collections.EMPTY_MAP, Type.ALL,
			de.hybris.platform.core.initialization.SystemSetup.Process.INIT, "impex");

	@Test
	public void testcreateAutoImpexEssentialData() throws Exception
	{
		log.info("Starting test method testcreateAutoImpexEssentialData(de.hybris.platform.impex.systemsetup.ImpExSystemSetupTest");
		final String prev = Config.getParameter("impex." + ImpExSystemSetup.PARAMETER_ESSENTIAL);
		try
		{
			Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_ESSENTIAL, null);
			if (Boolean.parseBoolean(System.getProperty("glassfish", "false")))
			{
				expectedFile = "impex/essentialdata-impexsystemsetuptestdefault.impex";
				invalidFile = new String[]
				{ "impex/impexsystemsetuptestdefault.impex", "essentialdata-impexsystemsetuptestconfig.impex" };
			}
			else
			{
				expectedFile = "resources/impex/essentialdata-impexsystemsetuptestdefault.impex";
				invalidFile = new String[]
				{ "resources/impex/impexsystemsetuptestdefault.impex", "resources/essentialdata-impexsystemsetuptestconfig.impex" };
			}
			testSetup = new MyImpExSystemSetup();
			testSetup.afterPropertiesSet();
			testSetup.createAutoImpexEssentialData(ctx);

			assertTrue("The ImpEx import of essentialdata by convention does not work.", processAndValidate());

			// this test won't work on glassfish because on glassfish only files from resources/impex directory are available 
			if (!Boolean.parseBoolean(System.getProperty("glassfish", "false")))
			{
				Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_ESSENTIAL, "essentialdata*.impex");
				expectedFile = "resources/essentialdata-impexsystemsetuptestconfig.impex";
				invalidFile = new String[]
				{ "resources/impexsystemsetuptestconfig.impex", "resources/impex/essentialdata-impexsystemsetuptestdefault.impex" };
				testSetup = new MyImpExSystemSetup();
				testSetup.afterPropertiesSet();
				testSetup.createAutoImpexEssentialData(ctx);

				assertTrue("The ImpEx import of essentialdata by configuration does not work.", processAndValidate());
			}
			log.info("Finished test method testcreateAutoImpexEssentialData(de.hybris.platform.impex.systemsetup.ImpExSystemSetupTest");
		}
		finally
		{
			Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_ESSENTIAL, prev);
		}
	}

	@Test
	public void testcreateAutoImpexProjectData() throws Exception
	{
		log.info("Starting test method testcreateAutoImpexProjectData(de.hybris.platform.impex.systemsetup.ImpExSystemSetupTest");

		final String prev = Config.getParameter("impex." + ImpExSystemSetup.PARAMETER_PROJECT);
		try
		{
			Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_PROJECT, null);
			if (Boolean.parseBoolean(System.getProperty("glassfish", "false")))
			{
				expectedFile = "impex/projectdata-impexsystemsetuptestdefault.impex";
				invalidFile = new String[]
				{ "impex/impexsystemsetuptestdefault.impex", "projectdata-impexsystemsetuptestconfig.impex" };
			}
			else
			{
				expectedFile = "resources/impex/projectdata-impexsystemsetuptestdefault.impex";
				invalidFile = new String[]
				{ "resources/impex/impexsystemsetuptestdefault.impex", "resources/projectdata-impexsystemsetuptestconfig.impex" };
			}
			testSetup = new MyImpExSystemSetup();
			testSetup.afterPropertiesSet();
			testSetup.createAutoImpexProjectData(ctx);

			assertTrue("The ImpEx import of projectdata by convention does not work.", processAndValidate());

			// this test won't work on glassfish because on glassfish only files from resources/impex directory are available
			if (!Boolean.parseBoolean(System.getProperty("glassfish", "false")))
			{
				Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_PROJECT, "projectdata*.impex");
				expectedFile = "resources/projectdata-impexsystemsetuptestconfig.impex";
				invalidFile = new String[]
				{ "resources/impexsystemsetuptestconfig.impex", "resources/impex/projectdata-impexsystemsetuptestdefault.impex" };
				testSetup = new MyImpExSystemSetup();
				testSetup.afterPropertiesSet();
				testSetup.createAutoImpexProjectData(ctx);

				assertTrue("The ImpEx import of projectdata by configuration does not work.", processAndValidate());
			}
			log.info("Finished test method testcreateAutoImpexProjectData(de.hybris.platform.impex.systemsetup.ImpExSystemSetupTest");
		}
		finally
		{
			Config.setParameter("impex." + ImpExSystemSetup.PARAMETER_PROJECT, prev);
		}
	}

	private boolean processAndValidate()
	{
		boolean expected = false;
		boolean invalid = false;

		for (String file : testSetup.matchedFiles)
		{
			file = file.toLowerCase(Locale.getDefault());
			if (expectedFile.equals(file))
			{
				expected = true;
			}
			else if (invalidFile[0].equals(file) || invalidFile[1].equals(file))
			{
				invalid = true;
			}
		}

		return expected && !invalid;
	}

	private static class MyImpExSystemSetup extends ImpExSystemSetup
	{
		final List<String> matchedFiles = new ArrayList<String>();

		@Override
		protected void importCSVFromResources(final String csv)
		{
			matchedFiles.add(csv);
		}
	}
}
