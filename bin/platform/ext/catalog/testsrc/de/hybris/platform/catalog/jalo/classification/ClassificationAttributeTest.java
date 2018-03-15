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
package de.hybris.platform.catalog.jalo.classification;

import static junit.framework.Assert.assertTrue;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Test;


public class ClassificationAttributeTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;

	private ClassificationSystemModel classificationSystem;
	private ClassificationSystemVersionModel classificationSystemVersion;
	private ClassificationAttributeModel classificationAttribute;
	private ClassificationAttributeValueModel classificationAttributeValue;

	private final long sleepingTime = 1000l;

	@Test
	public void testModifiedTime() throws Exception
	{
		classificationSystem = modelService.create(ClassificationSystemModel.class);
		classificationSystem.setId("testClassificationSystem");
		classificationSystemVersion = modelService.create(ClassificationSystemVersionModel.class);
		classificationSystemVersion.setCatalog(classificationSystem);
		classificationSystemVersion.setVersion("testVersion");
		classificationAttribute = modelService.create(ClassificationAttributeModel.class);
		classificationAttribute.setCode("testClassificationAttributeCode");
		classificationAttribute.setSystemVersion(classificationSystemVersion);
		classificationAttributeValue = modelService.create(ClassificationAttributeValueModel.class);
		classificationAttributeValue.setCode("classificationAttributeValueCode");
		classificationAttributeValue.setSystemVersion(classificationSystemVersion);
		modelService.saveAll();
		final long creationTimeBefore = classificationAttribute.getModifiedtime().getTime();

		Thread.sleep(sleepingTime);
		classificationAttribute.setDefaultAttributeValues(Collections.singletonList(classificationAttributeValue));
		modelService.save(classificationAttribute);
		final long modifiedTimeaAfter = classificationAttribute.getModifiedtime().getTime();

		final long difference = modifiedTimeaAfter - creationTimeBefore;
		assertTrue("modified time at least one second later", (difference - sleepingTime) >= 0);
	}

}
