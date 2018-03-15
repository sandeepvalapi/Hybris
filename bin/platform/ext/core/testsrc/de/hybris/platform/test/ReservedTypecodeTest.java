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
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;


@IntegrationTest
public class ReservedTypecodeTest extends HybrisJUnit4Test
{

	private static Properties reservedCodes;
	private final DBPersistenceManager manager = (DBPersistenceManager) Registry.getPersistenceManager();
	private static String[] includedExtensions;

	//Add an exclusion only in case the code is already released!!!!
	private final static List<Integer> exclusions = Arrays.asList(
			//
			Integer.valueOf(10001), Integer.valueOf(10002), Integer.valueOf(10004), Integer.valueOf(10005), Integer.valueOf(10007),
			Integer.valueOf(10008), Integer.valueOf(10009), Integer.valueOf(10010), Integer.valueOf(10012), Integer.valueOf(10013),
			Integer.valueOf(10017), Integer.valueOf(10018), Integer.valueOf(10021), Integer.valueOf(10022), Integer.valueOf(10023),
			Integer.valueOf(10025), Integer.valueOf(10027), Integer.valueOf(10028), Integer.valueOf(10030), Integer.valueOf(10031),
			Integer.valueOf(10032), Integer.valueOf(10033), Integer.valueOf(10034), Integer.valueOf(10035), Integer.valueOf(10036),
			Integer.valueOf(10037), Integer.valueOf(10038), Integer.valueOf(10039), Integer.valueOf(10040), Integer.valueOf(10041),
			Integer.valueOf(10042), Integer.valueOf(10123),

			Integer.valueOf(13101), Integer.valueOf(13102), Integer.valueOf(13113), Integer.valueOf(13211), Integer.valueOf(13212),
			Integer.valueOf(13213), Integer.valueOf(13214), Integer.valueOf(13215),

			Integer.valueOf(20340),

			Integer.valueOf(23401), Integer.valueOf(23402), Integer.valueOf(23403), Integer.valueOf(23404), Integer.valueOf(23405),
			Integer.valueOf(23406), Integer.valueOf(23407), Integer.valueOf(23409), Integer.valueOf(23410), Integer.valueOf(23411),
			Integer.valueOf(23412), Integer.valueOf(23413), Integer.valueOf(23414), Integer.valueOf(23420), Integer.valueOf(23421),
			Integer.valueOf(23423), Integer.valueOf(23425), Integer.valueOf(23426), Integer.valueOf(23427), Integer.valueOf(23428),
			Integer.valueOf(23429), Integer.valueOf(23430), Integer.valueOf(23431), Integer.valueOf(23432), Integer.valueOf(23451),
			Integer.valueOf(23452), Integer.valueOf(23460), Integer.valueOf(23461), Integer.valueOf(23462), Integer.valueOf(23463),
			Integer.valueOf(23464), Integer.valueOf(23465), Integer.valueOf(23466), Integer.valueOf(23467), Integer.valueOf(23904),


			Integer.valueOf(24242), Integer.valueOf(24401), Integer.valueOf(24402), Integer.valueOf(24403), Integer.valueOf(24404),
			Integer.valueOf(24441), Integer.valueOf(24500), Integer.valueOf(24600), Integer.valueOf(24601), Integer.valueOf(24602),
			Integer.valueOf(24603), Integer.valueOf(24604), Integer.valueOf(24605), Integer.valueOf(24606), Integer.valueOf(24607),
			Integer.valueOf(24608), Integer.valueOf(24609), Integer.valueOf(24610), Integer.valueOf(24611), Integer.valueOf(24612),
			Integer.valueOf(24613),

			Integer.valueOf(26000), Integer.valueOf(26001), Integer.valueOf(26002),
			Integer.valueOf(26003),
			Integer.valueOf(26004),
			Integer.valueOf(26005),
			Integer.valueOf(26006),

			//hyend2
			Integer.valueOf(32000), Integer.valueOf(32001), Integer.valueOf(32003), Integer.valueOf(32004), Integer.valueOf(32005),
			Integer.valueOf(32007), Integer.valueOf(32008), Integer.valueOf(32009), Integer.valueOf(32010), Integer.valueOf(32011),
			Integer.valueOf(32012), Integer.valueOf(32013), Integer.valueOf(32015), Integer.valueOf(32016), Integer.valueOf(32017),
			Integer.valueOf(32019), Integer.valueOf(32020), Integer.valueOf(32021), Integer.valueOf(32022), Integer.valueOf(32023),
			Integer.valueOf(32024), Integer.valueOf(32025), Integer.valueOf(32026), Integer.valueOf(32027), Integer.valueOf(32028),

			Integer.valueOf(32764), Integer.valueOf(32766), Integer.valueOf(32767));

	@BeforeClass
	public static void setUp() throws IOException
	{
		final InputStream inputStream = ReservedTypecodeTest.class.getResourceAsStream("/core/unittest/reservedTypecodes.txt");
		reservedCodes = new Properties();
		reservedCodes.load(inputStream);
		inputStream.close();

		// which extensions should be included (should be full)
		includedExtensions = new String[] {};

		final String typecodeExtensions = System.getProperty("typecodeExtensions");
		final List<String> extensions = new ArrayList<String>();

		if (typecodeExtensions != null && !"".equals(typecodeExtensions))
		{
			includedExtensions = typecodeExtensions.split(",");
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
	}

	@Test
	public void testReservedTypecodes()
	{
		final List<String> errors = new ArrayList<String>();
		for (final ComposedType cType : getTypes())
		{
			final ItemDeployment depl = manager.getItemDeployment(manager.getJNDIName(cType.getCode()));
			final int code = cType.getItemTypeCode();
			if (depl == null || !TypeManager.getInstance().getRootComposedType(code).equals(cType))
			{
				continue;
			}

			//if (code > 10000 && !exclusions.contains(Integer.valueOf(code)))
			//{
			//	errors.add("Typecode "
			//			+ code
			//			+ " of type "
			//			+ cType.getCode()
			//			+ " is bigger than 10000 which breaks the promise to customer that hybris will not use codes bigger than 10000, please choose acode lower this threshold.");
			//}

			final String reservedType = reservedCodes.getProperty(Integer.toString(code));
			if (reservedType == null)
			{
				errors.add("Typecode '"
						+ code
						+ "' of type '"
						+ cType.getCode()
						+ "' is not listed as reserved typecode. If you have added the type new please add it to file platform/ext/core/resources/core/unittest/reservedTypecodes.txt. This will ensure that this typecode will not be used in future for other types even if your extension is not installed. This will avoid compatibility problems.");
			}
			else
			{
				if (!reservedType.equalsIgnoreCase(cType.getCode()))
				{
					errors.add("Reserved typecode '"
							+ code
							+ "' does not match expected type '"
							+ reservedType
							+ "' instead type '"
							+ cType.getCode()
							+ "' was found. This will happen if you have added a new typecode which was used in former days. Please change your typecode to a one not listed at platform/ext/core/resources/core/unittest/reservedTypecodes.txt. Otherwise you get in danger to be not compatible to former releases!!!");
				}
			}
		}

		final StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("The following type code conflicts have been found:\n");
		for (final String error : errors)
		{
			errorBuilder.append(error).append("\n");
		}
		assertTrue(errorBuilder.toString(), errors.isEmpty());
	}

	private static List<ComposedType> getTypes()
	{
		final List<ComposedType> returnList = new ArrayList<ComposedType>();

		for (final ComposedType cType : TypeManager.getInstance().getAllComposedTypes())
		{
			for (final String extension : includedExtensions)
			{
				if (cType.getExtensionName().toLowerCase(Locale.getDefault()).equals(extension.toLowerCase(Locale.getDefault())))
				{
					returnList.add(cType);
					break;
				}
			}
		}

		return returnList;
	}
}
