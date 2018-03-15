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
package de.hybris.platform.commons.renderer.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.Renderer;
import de.hybris.platform.commons.renderer.daos.RendererTemplateDao;
import de.hybris.platform.commons.renderer.exceptions.RendererNotFoundException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



@UnitTest
public class DefaultRendererServiceTest
{
	private DefaultRendererService rendererService;

	@Before
	public void setUp()
	{
		rendererService = new DefaultRendererService();
	}

	@Test
	public void testRendering()
	{
		//given
		final Renderer renderer = Mockito.mock(Renderer.class);
		final Object context = Mockito.mock(Object.class);
		final RendererTemplateModel template = Mockito.mock(RendererTemplateModel.class);
		final Writer output = Mockito.mock(Writer.class);

		//when
		rendererService.render(renderer, template, context, output);

		//then
		Mockito.verify(renderer).render(template, context, output);
	}

	@Test
	public void testRenderingWithMapping()
	{
		//given
		final Renderer renderer = Mockito.mock(Renderer.class);
		final Object context = Mockito.mock(Object.class);
		final RendererTemplateModel template = Mockito.mock(RendererTemplateModel.class);
		when(template.getRendererType()).thenReturn(RendererTypeEnum.VELOCITY);
		final Writer output = Mockito.mock(Writer.class);

		final Map<RendererTypeEnum, Renderer> mapping = new HashMap<RendererTypeEnum, Renderer>();
		mapping.put(RendererTypeEnum.VELOCITY, renderer);
		rendererService.setMapping(mapping);

		//when
		rendererService.render(template, context, output);

		//then
		Mockito.verify(renderer).render(template, context, output);
	}

	@Test
	public void testRenderingWithMappingWhenNoMappingFound()
	{
		//given
		final Renderer renderer = Mockito.mock(Renderer.class);
		final Object context = Mockito.mock(Object.class);
		final RendererTemplateModel template = Mockito.mock(RendererTemplateModel.class);
		when(template.getRendererType()).thenReturn(RendererTypeEnum.VELOCITY);
		final Writer output = Mockito.mock(Writer.class);

		rendererService.setMapping(Collections.EMPTY_MAP);

		//when
		try
		{
			rendererService.render(template, context, output);
			Assert.fail();
		}
		catch (final RendererNotFoundException ex)
		{
			//then OK
		}
		//then
		Mockito.verifyZeroInteractions(renderer);
	}

	@Test
	public void testGetRendererTemplateForCode()
	{
		//given
		final String TEST_CODE = "TestCode";
		final RendererTemplateModel mockTemplate = Mockito.mock(RendererTemplateModel.class);
		final RendererTemplateDao mockRendererDao = Mockito.mock(RendererTemplateDao.class);
		Mockito.when(mockRendererDao.findRendererTemplatesByCode(TEST_CODE)).thenReturn(Collections.singletonList(mockTemplate));
		rendererService.setRendererTemplateDao(mockRendererDao);

		//when
		final RendererTemplateModel template = rendererService.getRendererTemplateForCode(TEST_CODE);

		//then
		Assert.assertNotNull(template);
		Assert.assertSame(template, mockTemplate);
	}

	@Test
	public void testGetRendererTemplateForCodeAndThrowUnknownIdentifierException()
	{
		//given
		final String TEST_CODE = "TestCode";
		final RendererTemplateDao mockRendererDao = Mockito.mock(RendererTemplateDao.class);
		Mockito.when(mockRendererDao.findRendererTemplatesByCode(TEST_CODE)).thenReturn(Collections.EMPTY_LIST);
		rendererService.setRendererTemplateDao(mockRendererDao);

		//when
		try
		{
			rendererService.getRendererTemplateForCode(TEST_CODE);
			Assert.fail("Should throw UnknownIdentifierException because nothing has been found");
		}
		catch (final UnknownIdentifierException ex)
		{
			//OK
		}

	}

	@Test
	public void testGetRendererTemplateForCodeAndThrowNullPointerException()
	{
		//given
		final RendererTemplateDao mockRendererDao = Mockito.mock(RendererTemplateDao.class);
		Mockito.when(mockRendererDao.findRendererTemplatesByCode(null)).thenThrow(new NullPointerException());
		rendererService.setRendererTemplateDao(mockRendererDao);

		//when
		try
		{
			rendererService.getRendererTemplateForCode(null);
			Assert.fail("Should throw NullPointerException because null code passed");
		}
		catch (final NullPointerException ex)
		{
			//OK
		}
	}

	@Test
	public void testGetRendererTemplateForCodeAndThrowAmbiguousIdentifierException()
	{
		//given
		final String TEST_CODE = "TestCode";
		final RendererTemplateModel mockTemplate = Mockito.mock(RendererTemplateModel.class);
		final RendererTemplateDao mockRendererDao = Mockito.mock(RendererTemplateDao.class);
		Mockito.when(mockRendererDao.findRendererTemplatesByCode(TEST_CODE)).thenReturn(Arrays.asList(mockTemplate, mockTemplate));
		rendererService.setRendererTemplateDao(mockRendererDao);

		//when
		try
		{
			rendererService.getRendererTemplateForCode(TEST_CODE);
			Assert.fail("Should throw AmbiguousIdentifierException because more than one result found");
		}
		catch (final AmbiguousIdentifierException ex)
		{
			//OK
		}

	}
}
