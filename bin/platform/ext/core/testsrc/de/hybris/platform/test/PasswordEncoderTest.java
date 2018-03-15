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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.security.EJBCannotDecodePasswordException;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class PasswordEncoderTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(PasswordEncoderTest.class.getName());

	@Test
	public void testEncoders()
	{
		final Collection encodings = Registry.getCurrentTenant().getJaloConnection().getPasswordEncoderFactory()
				.getSupportedEncodings();
		assertTrue("wrong default encodings : " + encodings,
				encodings != null && !encodings.isEmpty() && encodings.contains(SystemEJB.DEFAULT_ENCODING));
		try
		{
			final PasswordEncoder enc = Registry.getCurrentTenant().getJaloConnection()
					.getPasswordEncoder(SystemEJB.DEFAULT_ENCODING);
			assertNotNull("no default password encoder found : " + enc, enc);
			/* conv-log */log.debug("encoder class is " + enc.getClass().getName());
			String encoded = null;
			/* conv-log */log.debug("encoding  'test' " + (encoded = enc.encode(Constants.USER.ADMIN_EMPLOYEE, "nimda")));
			/* conv-log */log.debug("decoding  '" + encoded + "' " + enc.decode(encoded));
		}
		catch (final PasswordEncoderNotFoundException e)
		{
			fail("cannot get default password encoder (though it is mapped) : " + e);
		}
		catch (final EJBCannotDecodePasswordException e)
		{
			/* conv-log */log.debug("default encoder cannot decode password !!!");
		}
	}

}
