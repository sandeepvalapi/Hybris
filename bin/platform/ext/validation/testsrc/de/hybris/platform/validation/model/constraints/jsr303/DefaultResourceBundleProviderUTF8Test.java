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
package de.hybris.platform.validation.model.constraints.jsr303;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.validation.messages.impl.DefaultResourceBundleProvider;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;


/**
 * Tests utf-8 file encoding support of {@link DefaultResourceBundleProvider}. See PLA-12816.
 */
public class DefaultResourceBundleProviderUTF8Test extends HybrisJUnit4Test
{
	static final String TEST_UTF8_RESOURCE_NAME = "test/utf8resource";
	static final String TESTKEY = "testkey";
	static final Locale EMPTY_LOC = new Locale("");

	//	@Ignore("activate to verify PLA-12816")
	@Test
	public void testUtf8EncodingSupport()
	{
		final DefaultResourceBundleProvider provider = new DefaultResourceBundleProvider()
		{
			// overriding resource base name for test
			@Override
			protected ResourceBundle loadBundle(final Locale locale, final String resourceKey)
			{
				return super.loadBundle(locale, TEST_UTF8_RESOURCE_NAME);
			}
		};

		final ResourceBundle bundle = provider.getResourceBundle(EMPTY_LOC);
		assertNotNull(bundle);
		assertEquals("\u00E4\u00F6\u00FC\u00DF", bundle.getString(TESTKEY));
	}
}
