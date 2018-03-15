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
package de.hybris.platform.classification.interceptors;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.LinkedList;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ClassificationAttributeValueRemoveInterceptorTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	private ClassificationAttributeValueModel unusedValue;
	private ClassificationAttributeValueModel usedValue;
	private ClassAttributeAssignmentModel assignment;

	@Before
	public void setUp() throws Exception
	{
		final ClassificationSystemModel system = modelService.create(ClassificationSystemModel.class);
		system.setId("CS");

		final ClassificationSystemVersionModel version = modelService.create(ClassificationSystemVersionModel.class);
		version.setVersion("1.0");
		version.setCatalog(system);

		final ClassificationAttributeValueModel value0 = modelService.create(ClassificationAttributeValueModel.class);
		value0.setSystemVersion(version);
		value0.setCode("VALUE0");

		final ClassificationAttributeValueModel value1 = modelService.create(ClassificationAttributeValueModel.class);
		value1.setSystemVersion(version);
		value1.setCode("VALUE1");

		final ClassificationAttributeValueModel value2 = modelService.create(ClassificationAttributeValueModel.class);
		value2.setSystemVersion(version);
		value2.setCode("VALUE2");

		final ClassificationAttributeModel attribute = modelService.create(ClassificationAttributeModel.class);
		attribute.setCode("attribute");
		attribute.setSystemVersion(version);

		final ClassificationClassModel classificationClass = modelService.create(ClassificationClassModel.class);
		classificationClass.setCatalogVersion(version);
		classificationClass.setCode("ClassificationClass");

		assignment = modelService.create(ClassAttributeAssignmentModel.class);
		assignment.setClassificationAttribute(attribute);
		assignment.setClassificationClass(classificationClass);
		assignment.setAttributeValues(Arrays.asList(value0, value1));

		modelService.saveAll();

		unusedValue = value2;
		usedValue = value1;
	}

	@Test
	public void shouldRemoveUnusedValue()
	{
		modelService.remove(unusedValue);

		modelService.refresh(assignment);

		assertThat(assignment.getAttributeValueDisplayString()).contains(usedValue.getCode());
	}

	@Test
	public void shouldFailOnRemovingValueInUse()
	{
		try
		{
			modelService.remove(usedValue);
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(ModelRemovalException.class);
			assertThat(ex.getCause()).isInstanceOf(InterceptorException.class).hasNoCause();
			assertThat(ex.getCause().getMessage()).contains(usedValue.getCode());
			return;
		}
		Assert.fail("Exception should be thrown");
	}

	@Test
	public void shouldRemoveValueWhichIsNoLongerInUse()
	{
		final String valueCodeToCheck = usedValue.getCode();

		final LinkedList<ClassificationAttributeValueModel> values = new LinkedList<>(assignment.getAttributeValues());
		values.remove(usedValue);
		assignment.setAttributeValues(values);
		modelService.saveAll();

		modelService.remove(usedValue);
		modelService.refresh(assignment);

		assertThat(assignment.getAttributeValueDisplayString()).doesNotContain(valueCodeToCheck);
	}
}
