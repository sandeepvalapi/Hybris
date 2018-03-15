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
package de.hybris.platform.servicelayer.user.interceptors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.PhoneContactInfoModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContactInfoPrepareInterceptorTest
{
	@InjectMocks
	private final ContactInfoPrepareInterceptor interceptor = new ContactInfoPrepareInterceptor();
	@Mock
	private InterceptorContext context;
	@Mock
	private KeyGenerator keyGenerator;

	@Test
	public void shouldGenerateContactInfoCodeIfItIsEmpty() throws Exception
	{
		// given
		final PhoneContactInfoModel contactInfo = new PhoneContactInfoModel();
		given(keyGenerator.generate()).willReturn("newCode123");
		given(context.isNew(contactInfo)).willReturn(Boolean.TRUE);

		// when
		interceptor.onPrepare(contactInfo, context);

		// then
		assertThat(contactInfo.getCode()).isEqualTo("newCode123");
	}

	@Test
	public void shouldNotTouchContactInfoCodeIfItWasProvidedByUser() throws Exception
	{
		// given
		final PhoneContactInfoModel contactInfo = new PhoneContactInfoModel();
		contactInfo.setCode("myCustomCode");
		given(context.isNew(contactInfo)).willReturn(Boolean.TRUE);

		// when
		interceptor.onPrepare(contactInfo, context);

		// then
		assertThat(contactInfo.getCode()).isEqualTo("myCustomCode");
	}

	@Test
	public void shouldNotTouchContactInfoCodeIfNotNew() throws Exception
	{
		// given
		final PhoneContactInfoModel contactInfo = new PhoneContactInfoModel();
		given(context.isNew(contactInfo)).willReturn(Boolean.FALSE);

		// when
		interceptor.onPrepare(contactInfo, context);

		// then
		assertThat(contactInfo.getCode()).isNull();
	}
}
