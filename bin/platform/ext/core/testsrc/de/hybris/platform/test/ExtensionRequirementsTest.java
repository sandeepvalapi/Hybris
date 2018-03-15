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
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;


@IntegrationTest
public class ExtensionRequirementsTest extends HybrisJUnit4Test
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
	 * Checks if platform classpath entry exists and is not exported
	 *
	 * @throws Exception
	 */
	@Test
	public void testCorrectPlatformClasspathEntry() throws Exception
	{
		final StringBuilder violations = new StringBuilder();

		for (final ExtensionInfo extension : includedExtensions)
		{
			// skip the platform and external extensions since they don't really need proper requirements
			if (platformExtensions.contains(extension) || excludedExtensions.contains(extension) || extension.isExternalExtension())
			{
				continue;
			}

			boolean platformEntryFound = false;
			final EclipseClasspath eclipseClasspath = EclipseClasspathReader.readClasspath(extension);
			for (final ClasspathEntry classpathEntry : eclipseClasspath.getSources())
			{
				if ("/platform".equals(classpathEntry.path))
				{
					// check for doubled platform export
					if (platformEntryFound)
					{
						violations
								.append(extension
										+ "/.classpath has more than one platform classpath entry! Please delete all but one and set exported=false!\n");
						break;
					}
					platformEntryFound = true;
					if (classpathEntry.exported)
					{
						violations
								.append(extension
										+ "/.classpath has a platform classpath entry but it is exported. Please set platform export to false!\n");
					}
				}
			}
			if (!platformEntryFound)
			{
				violations
						.append(extension
								+ "/.classpath has no platform classpath entry! Please add <classpathentry combineaccessrules=\"false\" exported=\"false\" kind=\"src\" path=\"/platform\" />\n");
			}
		}

		assertTrue(violations.toString(), violations.length() == 0);
	}

	/**
	 * Checks if the .classpath or extensioninfo.xml files require sampledata or a template
	 */
	@Test
	public void testIllegalRequirements()
	{
		final StringBuilder violations = new StringBuilder();

		for (final ExtensionInfo extension : includedExtensions)
		{
			if (!excludedExtensions.contains(extension))
			{
				for (final ExtensionInfo requiredExtension : extension.getRequiredExtensionInfos())
				{
					if (illegalRequirements.contains(requiredExtension))
					{
						violations.append(extension + " requires an extension which is not allowed to be required : "
								+ requiredExtension + "\n");
					}
					if (isTemplate(requiredExtension) && !isTemplate(extension)
							&& !Config.getBoolean(extension + ".illegalrequirementstest.excluded", false))
					{
						violations.append(extension + " is not a template extension but requires a template extension : "
								+ requiredExtension + "\n");
					}
				}
			}
		}

		assertTrue(violations.toString(), violations.length() == 0);
	}


	/**
	 * Checks if illegal anything of the web folder is exported
	 *
	 * @throws Exception
	 */
	@Test
	public void testIllegalWebExports() throws Exception
	{
		final StringBuilder illegalWebExports = new StringBuilder();
		for (final ExtensionInfo extension : includedExtensions)
		{
			if (shouldCheckForWebExports(extension))
			{
				for (final ClasspathEntry classpathEntry : EclipseClasspathReader.readClasspath(extension).getAllClasspathEntries())
				{
					if (classpathEntry.exported && classpathEntry.path.startsWith("web"))
					{
						illegalWebExports.append(extension + "/.classpath contains illegal web export : " + classpathEntry + "\n");
					}
				}
			}
		}
		assertTrue(illegalWebExports.toString(), illegalWebExports.length() == 0);
	}

	protected boolean shouldCheckForWebExports(final ExtensionInfo extension)
	{
		return !platformExtensions.contains(extension) && !excludedExtensions.contains(extension)
				&& !"hmc".equalsIgnoreCase(extension.getName()) && !"backoffice".equalsIgnoreCase(extension.getName());
	}

	private boolean isTemplate(final ExtensionInfo extension)
	{
		return new File(extension.getExtensionDirectory(), "extgen.properties").exists();
	}

	protected static Set<ExtensionInfo> getExtensionFilter(final String filterName)
	{
		final Set<ExtensionInfo> extensionFilter = new HashSet<ExtensionInfo>();

		final String extensionProperty = System.getProperty(filterName);

		if (extensionProperty != null && !extensionProperty.isEmpty())
		{
			for (final String filter : extensionProperty.split(","))
			{
				final ExtensionInfo ext = platformConfig.getExtensionInfo(filter.trim());
				if (ext != null)
				{
					extensionFilter.add(ext);
					LOG.info("adding to filter '" + filterName + "' : " + ext);
				}
				else
				{
					LOG.error("Could not find extension '" + filter + "' in included extensions list!");
				}
			}
		}
		return extensionFilter;
	}

}

class EclipseClasspathReader
{
	public static EclipseClasspath readClasspath(final ExtensionInfo extensionInfo) throws Exception
	{
		final File extensionInfoFile = new File(extensionInfo.getExtensionDirectory(), ".classpath");

		try
		{
			final EclipseClasspath eclipseClasspath = new EclipseClasspath(extensionInfo);
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(extensionInfoFile);
			final Element classpath = doc.getDocumentElement();
			final NodeList childs = classpath.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++)
			{
				final Node classpathentryNode = childs.item(i);
				if (classpathentryNode.getNodeName().equals("classpathentry"))
				{
					final ClasspathEntry entry = new ClasspathEntry();
					for (int j = 0; j < classpathentryNode.getAttributes().getLength(); j++)
					{
						final Node attribute = classpathentryNode.getAttributes().item(j);
						if (attribute.getNodeName().equals("kind"))
						{
							entry.kind = attribute.getNodeValue();
						}
						if (attribute.getNodeName().equals("path"))
						{
							entry.path = attribute.getNodeValue();
						}
						if (attribute.getNodeName().equals("exported"))
						{
							entry.exported = Boolean.parseBoolean(attribute.getNodeValue());
						}
						if (attribute.getNodeName().equals("combineaccessrules"))
						{
							entry.combineaccessrules = Boolean.parseBoolean(attribute.getNodeValue());
						}
					}
					eclipseClasspath.addClasspathEntry(entry);
				}
			}
			return eclipseClasspath;
		}
		catch (final Exception e)
		{
			throw new Exception("Error while reading classpath file : " + extensionInfoFile.getAbsolutePath(), e);
		}
	}
}


class EclipseClasspath
{
	private final ExtensionInfo extension;
	private final List<ClasspathEntry> sources = new ArrayList<ClasspathEntry>();
	private final List<ClasspathEntry> libs = new ArrayList<ClasspathEntry>();

	public EclipseClasspath(final ExtensionInfo extension)
	{
		super();
		this.extension = extension;
	}

	public void addClasspathEntry(final ClasspathEntry entry)
	{
		if ("src".equals(entry.kind))
		{
			addSource(entry.path, entry.exported, entry.combineaccessrules);
		}
		if ("lib".equals(entry.kind))
		{
			addLibrary(entry.path, entry.exported, entry.combineaccessrules);
		}
	}

	public void addSource(final String path, final boolean exported, final boolean combinedaccessrules)
	{
		sources.add(new ClasspathEntry(exported, "src", path, combinedaccessrules));
	}

	public void addLibrary(final String path, final boolean exported, final boolean combinedaccessrules)
	{
		libs.add(new ClasspathEntry(exported, "lib", path, combinedaccessrules));
	}

	public List<ClasspathEntry> getAllClasspathEntries()
	{
		final List<ClasspathEntry> entries = new ArrayList<ClasspathEntry>();
		entries.addAll(sources);
		entries.addAll(libs);
		return entries;
	}

	public List<ClasspathEntry> getLibs()
	{
		return libs;
	}

	public List<ClasspathEntry> getSources()
	{
		return sources;
	}

	@Override
	public String toString()
	{
		final StringBuffer buffer = new StringBuffer();
		buffer.append("Extension : " + extension);
		buffer.append(", Sources   : " + sources);
		buffer.append(", Libraries : " + libs);
		return buffer.toString();
	}

	public ExtensionInfo getExtension()
	{
		return extension;
	}
}


class ClasspathEntry
{
	public boolean exported;
	public String kind;
	public String path;
	public boolean combineaccessrules;

	public ClasspathEntry()
	{
		super();
	}

	public ClasspathEntry(final boolean exported, final String kind, final String path, final boolean combineaccessrules)
	{
		super();
		this.exported = exported;
		this.kind = kind;
		this.path = path;
		this.combineaccessrules = combineaccessrules;
	}

	@Override
	public String toString()
	{
		return path;
	}
}
