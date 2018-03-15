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
package de.hybris.platform.processengine.definition;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DynamicProcessDefinitionInterceptorTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Test
	public void shouldSaveDynamicProcessDefinitionOnlyWithContent()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setContent(getTestProcessDefinitionContent("TEST_DEFINITION_CODE"));

		modelService.save(model);

		assertThat(model.getCode()).isNotNull().isEqualTo("TEST_DEFINITION_CODE");
	}

	@Test
	public void shouldOverrideCodeOfNewDynamicProcessDefinition()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setCode("WRONG_CODE");
		model.setContent(getTestProcessDefinitionContent("TEST_DEFINITION_CODE"));

		modelService.save(model);

		assertThat(model.getCode()).isNotNull().isEqualTo("TEST_DEFINITION_CODE");
	}

	@Test
	public void shouldFailWhenChangingContentOfHistoricalDefinition()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setCode("TEST_DEFINITION_CODE");
		model.setActive(Boolean.FALSE);
		model.setContent(getTestProcessDefinitionContent("TEST_DEFINITION_CODE"));
		modelService.save(model);

		model.setContent(getTestProcessDefinitionContent("TEST_DEFINI"));

		try
		{
			modelService.save(model);
		}
		catch (final Exception ex)
		{
			assertThat(ex).isNotNull().isInstanceOf(ModelSavingException.class);
			assertThat(ex.getCause()).isNotNull().isInstanceOf(InterceptorException.class);
			return;
		}
		Assert.fail("Exception was expected");
	}

	@Test
	public void shouldFailWhenNoContentIsGiven()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);

		try
		{
			modelService.save(model);
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(ModelSavingException.class);
			assertThat(exception.getCause()).isNotNull().isInstanceOf(InterceptorException.class);
			return;
		}

		Assert.fail("Exception was expected");
	}

	@Test
	public void shouldFailWhenContentIsInvalidAndCodeIsNotGiven()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setContent(getInvalidTestProcessDefinitionContent());

		try
		{
			modelService.save(model);
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(ModelSavingException.class);
			assertThat(exception.getCause()).isNotNull().isInstanceOf(InterceptorException.class);
			return;
		}

		Assert.fail("Exception was expected");
	}

	@Test
	public void shouldFailWhenContentIsChangedToInvalidValue()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setContent(getTestProcessDefinitionContent("TEST_DEFINITION_CODE"));
		modelService.save(model);

		model.setContent(getInvalidTestProcessDefinitionContent());
		try
		{
			modelService.save(model);
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(ModelSavingException.class);
			assertThat(exception.getCause()).isNotNull().isInstanceOf(InterceptorException.class);
			return;
		}
		Assert.fail("Exception was expected");
	}

	@Test
	public void shouldFailWhenNameOfExistingDefinitionIsChanged()
	{
		final DynamicProcessDefinitionModel model = modelService.create(DynamicProcessDefinitionModel.class);
		model.setContent(getTestProcessDefinitionContent("TEST_DEFINITION_CODE"));
		modelService.save(model);

		model.setContent(getTestProcessDefinitionContent("CHANGED_CODE"));
		try
		{
			modelService.save(model);
		}
		catch (final Exception exception)
		{
			assertThat(exception).isInstanceOf(ModelSavingException.class);
			assertThat(exception.getCause()).isNotNull().isInstanceOf(InterceptorException.class);
			return;
		}
		Assert.fail("Exception was expected");
	}

	private static String getTestProcessDefinitionContent(final String definitionCode)
	{
		final StringBuilder result = new StringBuilder();

		result.append("<?xml version='1.0' encoding='utf-8'?>");
		result.append("<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='").append(definitionCode)
				.append("'>");
		result.append("	<scriptAction id='action0'>");
		result.append("		<script type='javascript'>(function() { return 'itworks' })()</script>");
		result.append("		<transition name='itworks' to='success'/>");
		result.append("	</scriptAction>");
		result.append("	<end id='success' state='SUCCEEDED'>Everything was fine</end>");
		result.append("</process>");

		return result.toString();
	}

	private static String getInvalidTestProcessDefinitionContent()
	{
		final StringBuilder result = new StringBuilder();

		result.append("<?xml version='1.0' encoding='utf-8'?>");
		result.append("<wrong/>");

		return result.toString();
	}
}
