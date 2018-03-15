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
package de.hybris.platform.workflow.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.workflow.mail.WorkflowMailContext;
import de.hybris.platform.workflow.mail.impl.WorkflowMailContextImpl;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultEmailServiceTest
{
	private DefaultEmailService emailService;

	@Mock
	private RendererService rendererService;
	@Mock
	private SessionService sessionService;
	@Mock
	private L10NService l10nService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		emailService = new DefaultEmailService()
		{
			@Override
			void validateEmailAddress(final String toAddress) throws EmailException
			{
				// 
			}

			@Override
			Email getPreconfiguredEmail() throws EmailException
			{
				return mock(HtmlEmail.class);
			}

			@Override
			public WorkflowMailContext createWorkflowMailContext()
			{
				return new WorkflowMailContextImpl();
			}
		};
		emailService.setRendererService(rendererService);
		emailService.setSessionService(sessionService);
		emailService.setL10nService(l10nService);
	}

	@Test
	public void testCreateActivationEmail() throws Exception
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getSendEmail()).thenReturn(Boolean.TRUE);
		when(mockAction.getEmailAddress()).thenReturn("test@hybris.de");
		final WorkflowModel mockWorkflow = mock(WorkflowModel.class);
		when(mockAction.getWorkflow()).thenReturn(mockWorkflow);
		when(l10nService.getLocalizedString(Mockito.eq("message.workflowaction.activated.subject"), (Object[]) Mockito.anyObject()))
				.thenReturn("message.workflowaction.activated.subject");
		when(l10nService.getLocalizedString(Mockito.eq("message.workflowaction.activated.mail"), (Object[]) Mockito.anyObject()))
				.thenReturn("message.workflowaction.activated.mail");

		//when
		final HtmlEmail activationEmail = emailService.createActivationEmail(mockAction);

		//then
		assertThat(activationEmail).isNotNull();
		verify(activationEmail).setCharset("utf-8");
		verify(activationEmail).addTo("test@hybris.de");
		verify(activationEmail).setSubject("message.workflowaction.activated.subject");
		verify(activationEmail).setMsg("message.workflowaction.activated.mail");
	}

	@Test
	public void testCreateActivationEmailWithTemplateAndEmptyMessage() throws Exception
	{
		//given
		final WorkflowActionModel mockAction = mock(WorkflowActionModel.class);
		when(mockAction.getSendEmail()).thenReturn(Boolean.TRUE);
		when(mockAction.getEmailAddress()).thenReturn("test@hybris.de");
		final WorkflowModel mockWorkflow = mock(WorkflowModel.class);
		when(mockAction.getWorkflow()).thenReturn(mockWorkflow);
		final RendererTemplateModel mockTemplate = mock(RendererTemplateModel.class);

		when(mockAction.getRendererTemplate()).thenReturn(mockTemplate);
		final UserModel mockUser = mock(UserModel.class);
		when(mockAction.getPrincipalAssigned()).thenReturn(mockUser);
		when(l10nService.getLocalizedString(Mockito.eq("message.workflowaction.activated.subject"), (Object[]) Mockito.anyObject()))
				.thenReturn("message.workflowaction.activated.subject");

		//when
		final HtmlEmail activationEmail = emailService.createActivationEmail(mockAction);

		//then
		assertThat(activationEmail).isNotNull();
		verify(activationEmail).setCharset("utf-8");
		verify(activationEmail).addTo("test@hybris.de");
		verify(activationEmail).setSubject("message.workflowaction.activated.subject");
		verify(activationEmail).setHtmlMsg("");
		verifyNoMoreInteractions(activationEmail);
	}

}
