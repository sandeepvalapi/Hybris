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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.exceptions.RendererException;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import junit.framework.Assert;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests VelocityTemplateRenderer functionality
 */
@UnitTest
public class VelocityTemplateRendererTest
{
	private VelocityTemplateRenderer velocityTemplateRenderer;
	private MediaService mockMediaService;

	@Before
	public void setUp()
	{
		velocityTemplateRenderer = new VelocityTemplateRenderer();
		mockMediaService = mock(MediaService.class);
		velocityTemplateRenderer.setMediaService(mockMediaService);
		velocityTemplateRenderer.setContextName("ctx");
	}

	@Test
	public void testRenderingTemplateCorrect() throws IOException
	{
		//given
		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		final MediaModel mockMedia = mock(MediaModel.class);
		when(template.getContent()).thenReturn(mockMedia);

		final DataInputStream mockDataStream = new DataInputStream(new StringBufferInputStream("This is $ctx.classToTest test"));
		when(mockMediaService.getStreamFromMedia(mockMedia)).thenReturn(mockDataStream);

		final MockContextClass context = new MockContextClass(Collections.singletonMap("classToTest",
				"VelocityTemplateRendererTest"));
		final StringWriter output = new StringWriter();

		//when
		velocityTemplateRenderer.render(template, context, output);

		//then
		Assert.assertEquals(output.toString(), "This is VelocityTemplateRendererTest test");
	}

	@Test
	public void testRenderingWhenContextClassCannotBeFound() throws IOException
	{
		//given
		final String UNKNOWN_CLASS = "unknown";

		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(UNKNOWN_CLASS);

		final MockContextClass context = new MockContextClass(Collections.singletonMap("classToTest",
				"VelocityTemplateRendererTest"));
		final StringWriter output = new StringWriter();

		//when
		try
		{
			velocityTemplateRenderer.render(template, context, output);
			Assert.fail();
		}
		catch (final RendererException ex)
		{
			//then OK
			assertThat(ex.getMessage()).isEqualTo("Cannot find class: " + UNKNOWN_CLASS);
		}
	}

	@Test
	public void testRenderingWhenContextClassDoesNotMatch() throws IOException
	{
		//given

		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(String.class.getName());

		final MockContextClass context = new MockContextClass(Collections.singletonMap("classToTest",
				"VelocityTemplateRendererTest"));
		final StringWriter output = new StringWriter();

		//when
		try
		{
			velocityTemplateRenderer.render(template, context, output);
			Assert.fail();
		}
		catch (final RendererException ex)
		{
			//then OK
			assertThat(ex.getMessage()).isEqualTo(
					"The context class [" + MockContextClass.class.getName() + "] is not correctly defined.");
		}
	}

	@Test
	public void testRenderingWhenContextNotPassed() throws IOException
	{
		//given
		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		final MediaModel mockMedia = mock(MediaModel.class);
		when(template.getContent()).thenReturn(mockMedia);

		final DataInputStream mockDataStream = new DataInputStream(new StringBufferInputStream("This is $ctx.classToTest test"));
		when(mockMediaService.getStreamFromMedia(mockMedia)).thenReturn(mockDataStream);

		final StringWriter output = new StringWriter();

		//when
		velocityTemplateRenderer.render(template, null, output);

		//then
		Assert.assertEquals(output.toString(), "This is $ctx.classToTest test");

	}

	@Test
	public void testRenderingWhenProblem() throws IOException
	{
		//given
		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		final MediaModel mockMedia = mock(MediaModel.class);
		when(template.getContent()).thenReturn(mockMedia);

		final DataInputStream mockDataStream = new DataInputStream(new StringBufferInputStream("This is $ctx.classToTest test"));
		when(mockMediaService.getStreamFromMedia(mockMedia)).thenReturn(mockDataStream);

		final Writer output = mock(Writer.class);
		doThrow(new RuntimeException()).when(output).write(anyString());

		//when
		try
		{
			velocityTemplateRenderer.render(template, null, output);
			Assert.fail();
		}
		catch (final RendererException ex)
		{
			//then OK
			assertThat(ex.getMessage()).isEqualTo("Problem with get velocity stream");
		}


	}

	@Test
	public void testRenderingWhenContentNotSpecified() throws IOException
	{
		//given
		final String TEST_TEMPLATE_CODE = "Test template code";

		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		when(template.getContent()).thenReturn(null);
		when(template.getCode()).thenReturn(TEST_TEMPLATE_CODE);

		final MockContextClass context = new MockContextClass(null);
		final StringWriter output = new StringWriter();

		//when
		try
		{
			velocityTemplateRenderer.render(template, context, output);
			Assert.fail();
		}
		catch (final RendererException ex)
		{
			//then OK
			assertThat(ex.getMessage()).isEqualTo("No content found for template " + TEST_TEMPLATE_CODE);
		}
	}

	@Test
	public void testWhenVelocityEvaluationThrowsException() throws Exception
	{
		//given
		velocityTemplateRenderer = new VelocityTemplateRenderer();
		mockMediaService = mock(MediaService.class);
		velocityTemplateRenderer.setMediaService(mockMediaService);
		velocityTemplateRenderer.setContextName("ctx");

		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		final MediaModel mockMedia = mock(MediaModel.class);
		when(template.getContent()).thenReturn(mockMedia);

		final DataInputStream mockDataStream = new DataInputStream(new StringBufferInputStream("This is $ctx.classToTest test"));
		when(mockMediaService.getStreamFromMedia(mockMedia)).thenReturn(mockDataStream);

		final MockContextClass context = new MockContextClass(Collections.singletonMap("classToTest",
				"VelocityTemplateRendererTest"));
		final StringWriter output = new StringWriter();

		final VelocityTemplateRenderer rendererWithMockedEvaluateMethod = spy(velocityTemplateRenderer);

		doThrow(new ParseErrorException("")).doThrow(new ResourceNotFoundException("")).doThrow(new IOException())
				.doThrow(new MethodInvocationException(null, null, null, null, 0, 0)).when(rendererWithMockedEvaluateMethod)
				.evaluate((Writer) any(), (VelocityContext) any(), (Reader) any());

		//when
		for (int i = 0; i < 4; i++)
		{
			try
			{
				rendererWithMockedEvaluateMethod.render(template, context, output);
				Assert.fail();
			}
			catch (final RendererException ex)
			{
				//then OK
				assertThat(ex.getMessage()).isEqualTo("Problem with get velocity stream");
			}
		}

	}

	@Test
	public void shouldNotAllowJavaReflectionAPICalls() throws IOException
	{
		//given
		final RendererTemplateModel template = mock(RendererTemplateModel.class);
		when(template.getContextClass()).thenReturn(MockContextClass.class.getName());
		final MediaModel mockMedia = mock(MediaModel.class);
		when(template.getContent()).thenReturn(mockMedia);

		final DataInputStream mockDataStream = new DataInputStream(new StringBufferInputStream(
				"#set( $str = \"\" )\n#set( $systemClass = ${str.getClass().forName( \"java.lang.System\" )} )\n${systemClass.exit(1)}\n"
						+ "This is $ctx.classToTest test"));
		when(mockMediaService.getStreamFromMedia(mockMedia)).thenReturn(mockDataStream);

		final MockContextClass context = new MockContextClass(
				Collections.singletonMap("classToTest", "VelocityTemplateRendererTest"));
		final StringWriter output = new StringWriter();

		//when
		velocityTemplateRenderer.render(template, context, output);

		//then
		Assert.assertEquals("${systemClass.exit(1)}\nThis is VelocityTemplateRendererTest test", output.toString());
	}

	private static class MockContextClass extends VelocityContext
	{
		public MockContextClass(final Map<String, String> values)
		{
			super(values);
		}
	}
}
