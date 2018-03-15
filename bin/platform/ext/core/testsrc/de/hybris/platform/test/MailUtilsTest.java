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

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MailUtilsTest extends HybrisJUnit4Test
{
	/**
	 * Clears internal config cache, since some test methods influence (populate) it.
	 */
	@Before
	public void before()
	{
		Registry.getCurrentTenant().getConfig().clearCache();
	}

	@Test
	public void testValidateEmailServer()
	{
		try
		{
			MailUtils.validateEmailServer(null);
			fail("EmailException expected cause of null server");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validateEmailServer("");
			fail("EmailException expected cause of empty server");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validateEmailServer("a");
		}
		catch (final EmailException e)
		{
			fail("unexpected EMailException: " + e.getMessage());
		}
	}

	@Test
	public void testValidatePop3()
	{
		try
		{
			MailUtils.validatePop3(null, null, null);
			fail("EmailException expected cause of null test");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validatePop3("", "", null);
			fail("EmailException expected cause of empty test");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validatePop3("bla", "bla", null);
		}
		catch (final EmailException e)
		{
			fail("Unexpected EmailException: " + e.getMessage());
		}
	}

	@Test
	public void testValidateParameter()
	{
		try
		{
			MailUtils.validateParameter(null, null);
			fail("EmailException expected cause of empty test");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validateParameter("", null);
			fail("EmailException expected cause of empty test");
		}
		catch (final EmailException e)
		{
			// OK
		}
		try
		{
			MailUtils.validateParameter("a", null);
		}
		catch (final EmailException e)
		{
			fail("Unexpected EmailException: " + e.getMessage());
		}
	}

	@Test
	public void testValidateEmailAddress()
	{
		assertWrongEMailAddress(null);
		assertWrongEMailAddress("a");
		assertWrongEMailAddress("");
		assertWrongEMailAddress("a@b");
		assertWrongEMailAddress("a.b");
		assertWrongEMailAddress("a@.b");
		assertWrongEMailAddress("a@.");
		assertWrongEMailAddress("a@b.");
		try
		{
			MailUtils.validateEmailAddress("a@b.c", null);
		}
		catch (final EmailException e)
		{
			fail("Unexpected EmailException: " + e.getMessage());
		}
	}

	@Test
	public void testConvertCache()
	{
		final int defaultSMTPPort = -1;
		//this will put integer into cache 
		final int intSMTPPort = Config.getInt(Config.Params.MAIL_SMTP_PORT, defaultSMTPPort);
		//this will try to get Integer as string from cache - shouldn't result in clasCastException
		String stringSMTPPort = null;
		try
		{
			stringSMTPPort = Config.getString(Config.Params.MAIL_SMTP_PORT, String.valueOf(defaultSMTPPort));
		}
		catch (final ClassCastException cce)
		{
			fail("Cache is not resistful for subsequent calls of getters (of different returned type) for same parameter:"
					+ cce.getMessage());
		}
		assertEquals("Should be equal", String.valueOf(intSMTPPort), stringSMTPPort);
	}

	@Test
	public void testPreConfiguredEmail() throws EmailException
	{
		//read out original values
		final String orgSMTPServer = Config.getString(Config.Params.MAIL_SMTP_SERVER, null);
		final String orgFrom = Config.getString(Config.Params.MAIL_FROM, null);
		final String orgUseTLS = Config.getString(Config.Params.MAIL_USE_TLS, Boolean.FALSE.toString());
		final String orgSMTPPort = Config.getString(Config.Params.MAIL_SMTP_PORT, "-1");

		//define test values
		String smtpPort = "42";
		String smtpServer = "smtp.hybris.de";
		String from = "from@hybris.de";
		String useTLS = Boolean.FALSE.toString();
		//test test values
		Config.setParameter(Config.Params.MAIL_SMTP_SERVER, smtpServer);
		Config.setParameter(Config.Params.MAIL_SMTP_PORT, smtpPort);
		Config.setParameter(Config.Params.MAIL_FROM, from);
		Config.setParameter(Config.Params.MAIL_USE_TLS, useTLS);
		//check test values
		Email email = MailUtils.getPreConfiguredEmail();
		assertEquals(email.getSmtpPort(), smtpPort);
		assertEquals(email.getHostName(), smtpServer);
		assertEquals(email.getFromAddress().getAddress(), from);
		assertEquals(Boolean.toString(email.isTLS()), useTLS);

		email = null;

		//define new test values
		smtpPort = "43";
		smtpServer = "smtp2.hybris.de";
		from = "from2@hybris.de";
		useTLS = Boolean.TRUE.toString();
		//set new test values
		Config.setParameter(Config.Params.MAIL_SMTP_SERVER, smtpServer);
		Config.setParameter(Config.Params.MAIL_SMTP_PORT, smtpPort);
		Config.setParameter(Config.Params.MAIL_FROM, from);
		Config.setParameter(Config.Params.MAIL_USE_TLS, useTLS);
		//check changed test values
		email = MailUtils.getPreConfiguredEmail();
		assertEquals(email.getSmtpPort(), smtpPort);
		assertEquals(email.getHostName(), smtpServer);
		assertEquals(email.getFromAddress().getAddress(), from);
		assertEquals(Boolean.toString(email.isTLS()), useTLS);

		//restore old values
		Config.setParameter(Config.Params.MAIL_SMTP_SERVER, orgSMTPServer);
		Config.setParameter(Config.Params.MAIL_SMTP_PORT, orgSMTPPort);
		Config.setParameter(Config.Params.MAIL_FROM, orgFrom);
		Config.setParameter(Config.Params.MAIL_USE_TLS, orgUseTLS);
	}

	private void assertWrongEMailAddress(final String address)
	{
		try
		{
			MailUtils.validateEmailAddress(address, null);
			fail("EmailException expected");
		}
		catch (final EmailException e)
		{
			// OK
		}
	}
}
