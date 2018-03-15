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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.Renderer;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJobNotificationTemplateContext;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.StringWriter;
import java.util.Locale;

import javax.annotation.Resource;

import org.joda.time.DateMidnight;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Integration test for renderer framework
 */
@IntegrationTest
public class RendererIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private RendererService rendererService;

	@Resource
	private Renderer velocityTemplateRenderer;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		importCsv("/cronjob/DefaultCronJobFinishNotificationTemplate.csv", "windows-1252");
		importCsv("/workflow/testdata/workflow_test_data.csv", "windows-1252");
	}

	@Test
	public void testRenderDefaultCronJobFinishNotificationTemplate()
	{
		final RendererTemplateModel template = rendererService
				.getRendererTemplateForCode("DefaultCronJobFinishNotificationTemplate");

		assertThat(template).isNotNull();

		final StringWriter output = new StringWriter();
		final TestContext context = new TestContext();

		rendererService.render(velocityTemplateRenderer, template, context, output);

		final String outputContent = output.toString();

		final StringWriter output2 = new StringWriter();
		rendererService.render(template, context, output2);

		final String outputContent2 = output2.toString();

		assertThat(outputContent).isEqualTo(outputContent2);

		assertThat(outputContent).contains("Job: testCronJobName<br/>");
		assertThat(outputContent).contains("Gestartet: 09-01-2011 00:00<br/>");
		assertThat(outputContent).contains("Beendet:   10-01-2011 00:00<br/>");
		assertThat(outputContent).contains("Dauer:     25.0<br/>");
		assertThat(outputContent).contains(
				"<font color=\"green\">Job wurde erfolgreich abgeschlossen, aktueller Status: FINISHED</font>");
	}

	@Test
	public void testRemoveTemplateWithLocalizedMedias()
	{
		final RendererTemplateModel template = rendererService.getRendererTemplateForCode("productValidation");

		assertThat(template).isNotNull();
		final MediaModel contentEn = template.getContent(Locale.ENGLISH);
		final MediaModel contentDe = template.getContent(Locale.GERMAN);

		assertThat(contentEn).isNotNull();
		assertThat(contentDe).isNotNull();

		modelService.remove(template);
		assertThat(modelService.isRemoved(template)).isTrue();
		assertThat(modelService.isRemoved(contentEn)).isTrue();
		assertThat(modelService.isRemoved(contentDe)).isTrue();
	}


	private static class TestContext implements CronJobNotificationTemplateContext
	{

		@Override
		public String getCronJobName()
		{
			return "testCronJobName";
		}

		@Override
		public String getDuration()
		{
			return "25.0";
		}

		@Override
		public String getEndDate()
		{
			return new DateMidnight(2011, 1, 10).toString("dd-MM-yyyy HH:mm");
		}

		@Override
		public String getResult()
		{
			return CronJobResult.SUCCESS.toString();
		}

		@Override
		public String getStartDate()
		{
			return new DateMidnight(2011, 1, 9).toString("dd-MM-yyyy HH:mm");
		}

		@Override
		public String getStatus()
		{
			return CronJobStatus.FINISHED.toString();
		}

	}
}
