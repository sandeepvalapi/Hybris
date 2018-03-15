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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.TypeLocalization;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class LocalizationTest extends HybrisJUnit4Test
{
	protected static final Logger log = Logger.getLogger(LocalizationTest.class);
	protected static Language[] languages;
	protected static String[] excludedExtensions;
	protected static String[] includedExtensions;
	protected static String[] excludedTypes;
	protected static String[] excludedAttributes;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		if (Config.getBoolean("mode.dump", false))
		{
			if (log.isInfoEnabled())
			{
				log.info("Skipping LocalizationTest.testTypeLocalizations");
			}
			return;
		}
		// install languages
		languages = new Language[]
		{ C2LManager.getInstance().getLanguageByIsoCode("en") };

		// localize system
		log.info("Starting localization ..");
		TypeLocalization.getInstance().localizeTypes();

		// say what to exclude (should be empty)
		excludedExtensions = new String[] {};

		// say what to include (should be full) 
		includedExtensions = new String[] {};

		final String localizationExtensions = System.getProperty("localizationExtensions");
		final List<String> extensions = new ArrayList<String>();

		if (localizationExtensions != null && !"".equals(localizationExtensions))
		{
			includedExtensions = localizationExtensions.split(",");
		}
		else
		{
			final PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(Utilities.class);

			for (final ExtensionInfo extension : platformConfig.getExtensionInfosInBuildOrder())
			{
				extensions.add(extension.getName());
			}
			extensions.toArray(includedExtensions = new String[extensions.size()]);
		}

		for (int i = 0; i < includedExtensions.length; i++)
		{
			includedExtensions[i] = includedExtensions[i].trim();
		}

		excludedTypes = new String[]
		{ "TestItem", "TestItemType2" };

		excludedAttributes = new String[]
		{ "datePattern" };
	}

	@After
	public void tearDown() throws ConsistencyCheckException
	{
		if (Config.getBoolean("mode.dump", false))
		{
			if (log.isInfoEnabled())
			{
				log.info("Skipping LocalizationTest.testTypeLocalizations");
			}
			return;
		}
		log.info("Reverting localization ..");
		JaloSession.getCurrentSession().getSessionContext().setLanguage(null);
		for (final Language lang : languages)
		{
			if (!lang.getIsoCode().equals("en"))
			{
				lang.remove();
			}
		}

		Connection conn = null;
		Statement stmt = null;
		final ResultSet resultSet = null;
		String query1 = null;
		String query2 = null;
		String query3 = null;
		String tablePrefix = Registry.getCurrentTenant().getDataSource().getTablePrefix();
		tablePrefix = tablePrefix == null ? "" : tablePrefix; // NOPMD
		try
		{
			conn = Registry.getCurrentTenant().getDataSource().getConnection();

			query1 = "DELETE FROM " + tablePrefix + "composedtypeslp";
			query2 = "DELETE FROM " + tablePrefix + "attributedescriptorslp";
			query3 = "DELETE FROM " + tablePrefix + "enumerationvalueslp";

			stmt = conn.createStatement();
			stmt.execute(query1);
			stmt.execute(query2);
			stmt.execute(query3);
		}
		catch (final SQLException e)
		{
			log.error("Error while deleting localization, query was " + query1 + ", " + query2 + ", " + query3
					+ ", SQLException was: " + e);
		}
		finally
		{
			Utilities.tryToCloseJDBC(conn, stmt, resultSet);
		}
		Registry.getCurrentTenant().getCache().clear();
	}

	@Test
	public void testTypeLocalizations()
	{
		if (Config.getBoolean("mode.dump", false))
		{
			if (log.isInfoEnabled())
			{
				log.info("Skipping LocalizationTest.testTypeLocalizations");
			}
			return;
		}

		final StringBuilder result = new StringBuilder();
		int count = 0;
		for (final ComposedType type : getTypes())
		{
			for (int i = 0; i < languages.length; i++)
			{
				JaloSession.getCurrentSession().getSessionContext().setLanguage(languages[i]);
				if (type.getName() == null || type.getName().length() == 0)
				{
					result.append("Type: ").append(type.getCode()).append(", Language: ").append(languages[i].getIsoCode())
							.append(", Extension: ").append(type.getExtensionName()).append("\n");
					count++;
				}
			}
		}
		assertEquals(
				"For improving the quality of the hybris platform this test checks if all type are localized. The following types are not localized:\n"
						+ result.toString(), 0, count);
	}

	@Test
	public void testAttributeLocalizations()
	{
		if (Config.getBoolean("mode.dump", false))
		{
			if (log.isInfoEnabled())
			{
				log.info("Skipping LocalizationTest.testAttributeLocalizations");
			}
			return;
		}

		final StringBuilder result = new StringBuilder();
		int count = 0;
		for (final ComposedType type : getTypes())
		{
			for (int i = 0; i < languages.length; i++)
			{
				JaloSession.getCurrentSession().getSessionContext().setLanguage(languages[i]);

				for (final AttributeDescriptor desc : getAttributes(type))
				{
					if (desc.getName() == null || desc.getName().length() == 0)
					{
						result.append("Type: ").append(type.getCode()).append(", Attribute: ").append(desc.getQualifier())
								.append(", Language: ").append(languages[i].getIsoCode()).append(", Extension: ")
								.append(desc.getExtensionName()).append("\n");
						count++;
					}
				}
			}
		}
		assertEquals(
				"For improving the quality of the hybris platform this test checks if all type attributes are localized. The following attributes are not localized:\n"
						+ result.toString(), 0, count);
	}


	private Collection<ComposedType> getTypes()
	{
		final Set<ComposedType> result = new LinkedHashSet<ComposedType>();
		outer: for (final ComposedType type : TypeManager.getInstance().getAllComposedTypes())
		{
			if (type instanceof RelationType)
			{
				continue outer;
			}

			for (final String excludeType : excludedTypes)
			{
				if (TypeManager.getInstance().getComposedType(excludeType).isAssignableFrom(type))
				{
					continue outer;
				}
			}

			for (final String excludeExtension : excludedExtensions)
			{
				if (type.getExtensionName() != null && type.getExtensionName().equals(excludeExtension))
				{
					continue outer;
				}
			}

			for (final String includedExtension : includedExtensions)
			{
				if (type.getExtensionName() != null
						&& type.getExtensionName().toLowerCase(Locale.getDefault())
								.equals(includedExtension.toLowerCase(Locale.getDefault())))
				{
					result.add(type);
					continue outer;
				}
			}

		}
		return result;
	}

	private Collection<AttributeDescriptor> getAttributes(final ComposedType type)
	{
		final Set<AttributeDescriptor> result = new LinkedHashSet<AttributeDescriptor>();
		outer: for (final AttributeDescriptor desc : type.getAttributeDescriptors())
		{
			for (final String excludeAttribute : excludedAttributes)
			{
				if (desc.getQualifier().equals(excludeAttribute))
				{
					continue outer;
				}
			}
			for (final String excludeExtension : excludedExtensions)
			{
				if (desc.getExtensionName() != null && desc.getExtensionName().equals(excludeExtension))
				{
					continue outer;
				}
			}
			result.add(desc);
		}
		return result;
	}
}
