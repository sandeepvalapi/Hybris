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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static de.hybris.platform.test.ExtensionRequirementsTest.getExtensionFilter;
import static org.junit.Assert.assertTrue;

@IntegrationTest
public class ExtensionRequirementsNotAwareAboutAddonsTest extends HybrisJUnit4Test
{
    private static final String REQ_EXCLUDED_EXTENSIONS = "reqExcludedExtensions";
    private static final String REQ_INCLUDED_EXTENSIONS = "reqIncludedExtensions";

    private static final Logger LOG = Logger.getLogger(ExtensionRequirementsTest.class);

    private static PlatformConfig platformConfig;
    private static Set<ExtensionInfo> includedExtensions;
    private static Set<ExtensionInfo> excludedExtensions;
    private static Set<ExtensionInfo> platformExtensions;
    private static Set<ExtensionInfo> illegalRequirements;

    @BeforeClass
    public static void setUp()
    {
        platformConfig = ConfigUtil.getPlatformConfig(Utilities.class);

        // get platform extensions as extensionInfo
        platformExtensions = new HashSet<ExtensionInfo>();
        for (final String platformExtension : platformConfig.getAllPlatformExtensionNames())
        {
            platformExtensions.add(platformConfig.getExtensionInfo(platformExtension));
        }

        // evaluate included extensions
        includedExtensions = getExtensionFilter(REQ_INCLUDED_EXTENSIONS);
        if (includedExtensions.isEmpty())
        {
            for (final ExtensionInfo extension : platformConfig.getExtensionInfosInBuildOrder())
            {
                includedExtensions.add(extension);
            }
        }

        // evaluate excluded extensions
        excludedExtensions = getExtensionFilter(REQ_EXCLUDED_EXTENSIONS);

        // get illega requirements like all templates, sampledata etc.
        illegalRequirements = new HashSet<ExtensionInfo>();
        for (final ExtensionInfo extension : platformConfig.getExtensionInfosInBuildOrder())
        {
            if (extension.getName().equals("sampledata") || extension.getName().equals("testdata"))
            {
                illegalRequirements.add(extension);
            }
        }
    }



    /**
     * Checks if the .classpath and the extensioninfo.xml files within an extension have matching requirements
     * This test will fail on CS pipeline and in eny environment configured with add-ons, Platform is unaware of add-on logic
     * Test is in a separate class so it can be ignored in the pipeline configuration
     * @throws Exception
     */
    @Test
    public void testEclipseClasspathMatchesExtensionInfoXml() throws Exception
    {
        final StringBuilder exceptions = new StringBuilder();
        for (final ExtensionInfo extension : includedExtensions)
        {
            // skip the platform and external extensions since they don't really need proper requirements or if explicitly excluded
            if (platformExtensions.contains(extension) || excludedExtensions.contains(extension) || extension.isExternalExtension())
            {
                continue;
            }
            final EclipseClasspath eclipseClasspath = EclipseClasspathReader.readClasspath(extension);

            // compare extensions (direct) requirements with classpath source entries
            for (final ExtensionInfo requiredExtension : extension.getRequiredExtensionInfos())
            {
                // again, skip platform extensions. if they are required or not doesn't matter
                if (platformExtensions.contains(requiredExtension))
                {
                    continue;
                }
                boolean classpathEntryFound = false;
                for (final ClasspathEntry entry : eclipseClasspath.getSources())
                {
                    if (("/" + requiredExtension.getName()).equals(entry.path))
                    {
                        classpathEntryFound = true;
                    }
                }
                if (!classpathEntryFound)
                {
                    exceptions.append(extension + "/.classpath entry for required extension '" + requiredExtension
                            + "' not found, please add : <classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/"
                            + requiredExtension.getName() + "\" />\n");
                }
            }

            // and vice verca, test if any classpath entries exist that aren't defined in the extensioninfo.xml file
            for (final ClasspathEntry entry : eclipseClasspath.getSources())
            {
                if (entry.path != null && entry.path.startsWith("/") && !entry.path.equals("/platform"))
                {
                    boolean requiredExtensionFound = false;
                    for (final ExtensionInfo requiredExtension : extension.getRequiredExtensionInfos())
                    {
                        if (("/" + requiredExtension.getName()).equals(entry.path))
                        {
                            requiredExtensionFound = true;
                        }
                    }
                    if (!requiredExtensionFound)
                    {
                        exceptions.append(extension + "/.classpath has an entry for required extension '" + entry.path.substring(1)
                                + "', but that dependency is not defined in the " + extension + "/extensioninfo.xml file!\n");
                    }
                }
            }
        }

        assertTrue(exceptions.toString(), exceptions.length() == 0);
    }

}
