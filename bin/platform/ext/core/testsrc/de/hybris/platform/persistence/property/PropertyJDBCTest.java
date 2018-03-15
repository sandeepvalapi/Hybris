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
package de.hybris.platform.persistence.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for PropertyJDBC class. Only for manual tests, especially to proove the PLA-13083 issue. First, It's
 * recommended to change the column type mapping of the DeliveryMode.name attribute in core-items.xml.
 * HYBRIS_LONG_STRING will be mapped to VARCGAR2(4000) on oracle, which is what we mostly expect here.
 */
@ManualTest
public class PropertyJDBCTest extends ServicelayerBaseTest
{

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	private LanguageModel langDE;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		langDE = commonI18NService.getLanguage("de");
		assertNotNull(langDE);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.property.PropertyJDBC#writeProperties(de.hybris.platform.persistence.property.EJBPropertyRowCache, de.hybris.platform.core.PK, de.hybris.platform.core.PK, de.hybris.platform.persistence.property.TypeInfoMap, boolean)}
	 * .
	 * 
	 * for oracle only.
	 */
	@Test
	public void testWritePropertiesWithOracleClob()
	{
		final DeliveryModeModel dm = modelService.create(DeliveryModeModel.class);
		dm.setCode("testDM");
		modelService.save(dm);
		final DeliveryMode dmJalo = modelService.getSource(dm);
		final PK langPK = langDE.getPk();

		final PK itemPK = dmJalo.getPK();
		final PK typePK = dmJalo.getComposedTypePK();
		final TypeInfoMap infoMap = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typePK);
		final boolean localized = true;

		final EJBPropertyRowCache rowCache4Test = EJBPropertyRowCache.createLocalized(langPK, 0,
				Arrays.asList(DeliveryMode.DESCRIPTION, DeliveryMode.NAME));

		final String descriptionCLOB = createBigString(1001); //change this value as you wish
		rowCache4Test.setProperty(DeliveryMode.DESCRIPTION, langPK, descriptionCLOB);

		final String nameVarchar4000 = createBigString(1001); //change this value as you wish
		rowCache4Test.setProperty(DeliveryMode.NAME, langPK, nameVarchar4000);

		PropertyJDBC.writeProperties(rowCache4Test, itemPK, typePK, infoMap, localized);

		final DeliveryModeModel dmSaved = modelService.get(dm.getPk());
		// boom !!! ???
		//in case  the CLOB field is placed before the varchar field...
		//...the tests have shown that it fails in case of Clob field size>1000 and varcharFiled size >1000.
		//But the fix should take care of the reordering of the statement

		assertEquals(descriptionCLOB, dmSaved.getDescription(Locale.GERMAN));
		assertEquals(nameVarchar4000, dmSaved.getName(Locale.GERMAN));
	}

	private String createBigString(final int size)
	{
		final StringBuilder strBuilder = new StringBuilder(size);
		for (int i = 0; i < size; i++)
		{
			strBuilder.append('a');
		}
		return strBuilder.toString();
	}

}
