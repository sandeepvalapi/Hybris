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
package de.hybris.platform.classification.daos;

import static junit.framework.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for {@link ClassificationSystemDao}.
 */
@IntegrationTest
public class ClassificationSystemDaoTest extends ServicelayerTransactionalTest
{

	@Resource
	private ClassificationSystemDao classificationSystemDao;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		createHardwareCatalog();
	}

	/**
	 * Tests the findClassificationSystemsById(String systemId). There will be 1 classification system found.
	 */
	@Test
	public void testFindClassificationSystemsById()
	{
		Collection<ClassificationSystemModel> classificationSystems = classificationSystemDao
				.findSystemsById("NonExistingClassification");
		assertEquals("There should be no ClassificationSystemModel found", 0, classificationSystems.size());

		// Call a findClassificationSystemsById with existing classification system
		classificationSystems = classificationSystemDao.findSystemsById("SampleClassification");

		// ClassificiationSystemModels Collection should have 1 element 
		assertEquals("There should be 1 ClassificationSystemModel found", 1, classificationSystems.size());
	}

}
