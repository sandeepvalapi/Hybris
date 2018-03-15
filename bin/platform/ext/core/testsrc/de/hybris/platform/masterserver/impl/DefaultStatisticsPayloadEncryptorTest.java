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
package de.hybris.platform.masterserver.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import de.hybris.platform.masterserver.StatisticsPayloadEncryptor;

import com.hybris.encryption.asymmetric.AsymmetricEncryptor;
import com.hybris.encryption.asymmetric.impl.RSAEncryptor;
import com.hybris.encryption.asymmetric.impl.RSAKeyPairManager;
import com.hybris.encryption.symmetric.SymmetricEncryptor;
import com.hybris.encryption.symmetric.impl.AESEncryptor;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Before;
import org.junit.Test;

public class DefaultStatisticsPayloadEncryptorTest
{
	private StatisticsPayloadEncryptor encryptor;
	private final SymmetricEncryptor aesEncryptor = new AESEncryptor();
	private final AsymmetricEncryptor rsaEncryptor = new RSAEncryptor();
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;

	@Before
	public void setUp() throws Exception
	{
		final RSAKeyPairManager keyPairManager = new RSAKeyPairManager();
		publicKey = keyPairManager.getPublicKey("/test_public_key.der");
		privateKey = keyPairManager.getPrivateKey("/test_private_key.der");
		encryptor = new DefaultStatisticsPayloadEncryptor(aesEncryptor, rsaEncryptor, publicKey);
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenStatisticsDataIsNull() throws Exception
	{
		// given
		final String statisticsData = null;
		final String homeURL = "homeURL";

		try
		{
			// when
			encryptor.encrypt(statisticsData, homeURL);
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenHomeURLIsNull() throws Exception
	{
		// given
		final String statisticsData = "some fancy data";
		final String homeURL = null;

		try
		{
			// when
			encryptor.encrypt(statisticsData, homeURL);
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldEncryptAndDecryptDataProperly() throws Exception
	{
		// given
		final String statisticsData = "some fancy data";
		final String homeURL = "homeURL";

		// when
		final StatisticsPayload payload = encryptor.encrypt(statisticsData, homeURL);

		// then
		assertThat(payload).isNotNull();
		assertThat(payload.getHomeURL()).isNotNull().isEqualTo(homeURL);
		assertThat(payload.getPassword()).isNotNull();
		assertThat(payload.getData()).isNotNull();

		final String decryptedPassword = rsaEncryptor.decrypt(payload.getPassword(), privateKey);
		assertThat(decryptedPassword).isNotNull();
		assertThat(aesEncryptor.decrypt(payload.getData(), decryptedPassword)).isNotNull().isEqualTo(statisticsData);
	}
}
