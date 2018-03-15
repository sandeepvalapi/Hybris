/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.oauth2.jwt.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.oauth2.jwt.exceptions.JwtException;
import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;


@UnitTest
public class RsaKeyStoreHelperTest
{
	private static final String ALIAS = "test1";
	private static final String KEYSTORE_LOCATION = "test/keystore.jks";
	private static final String PASSWORD = "nimda123";
	ClassPathResource resource;
	RsaKeyStoreHelper keyStoreHelper;

	@Before
	public void setUp()
	{
		resource = new ClassPathResource(KEYSTORE_LOCATION);
		keyStoreHelper = new RsaKeyStoreHelper();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.oauth2.jwt.util.RsaKeyStoreHelper#getKeyStore(java.io.InputStream, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGetKeyStoreInputStreamString() throws FileNotFoundException, IOException
	{
		try
		{
			final KeyStore keyStore = keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD);
			assertNotNull(keyStore);
		}
		catch (final KeyStoreProcessingException e)
		{
			fail("test failed " + e.getMessage() + " " + e.getClass());
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.oauth2.jwt.util.RsaKeyStoreHelper#getKeyStore(java.io.InputStream, java.lang.String)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test(expected = KeyStoreProcessingException.class)
	public void testGetKeyStoreInputStreamStringInvalidPassword()
			throws KeyStoreProcessingException, FileNotFoundException, IOException
	{
		keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD + "invalid");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.oauth2.jwt.util.RsaKeyStoreHelper#getPrivateKey(java.security.KeyStore, java.lang.String, java.lang.String)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGetPrivateKey() throws FileNotFoundException, IOException
	{
		try
		{
			final KeyStore keyStore = keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD);
			assertNotNull(keyStore);

			final PrivateKey key = keyStoreHelper.getPrivateKey(keyStore, ALIAS, PASSWORD);
			assertNotNull(key);
			assertTrue(key instanceof RSAPrivateKey);
		}
		catch (final KeyStoreProcessingException e)
		{
			fail("test failed " + e.getMessage() + " " + e.getClass());
		}
	}

	@Test(expected = NullPointerException.class)
	public void testGetPrivateKeyInvalidAlias() throws KeyStoreProcessingException, FileNotFoundException, IOException
	{
		final KeyStore keyStore = keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD);
		assertNotNull(keyStore);

		keyStoreHelper.getPrivateKey(keyStore, ALIAS + "invalid", PASSWORD);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.oauth2.jwt.util.RsaKeyStoreHelper#getPublicKey(java.security.KeyStore, java.lang.String)}.
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGetPublicKey() throws FileNotFoundException, IOException
	{
		try
		{
			final KeyStore keyStore = keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD);
			assertNotNull(keyStore);

			final PublicKey key = keyStoreHelper.getPublicKey(keyStore, ALIAS);
			assertNotNull(key);
			assertTrue(key instanceof RSAPublicKey);
		}
		catch (final KeyStoreProcessingException e)
		{
			fail("test failed " + e.getMessage() + " " + e.getClass());
		}
	}

	@Test
	public void testGetSignerInputStreamStringString() throws FileNotFoundException, IOException
	{
		try
		{
			final Signer signer = keyStoreHelper.getSigner(RsaKeyStoreHelper.DEFAULT_INSTANCE_TYPE,
					new FileInputStream(resource.getFile()), ALIAS, PASSWORD);
			assertNotNull(signer);
		}
		catch (final KeyStoreProcessingException e)
		{
			fail("test failed " + e.getMessage() + " " + e.getClass());
		}
	}

	@Test
	public void buildAndVerify() throws FileNotFoundException, IOException
	{
		try
		{
			final Signer signer = keyStoreHelper.getSigner(RsaKeyStoreHelper.DEFAULT_INSTANCE_TYPE,
					new FileInputStream(resource.getFile()), ALIAS, PASSWORD);
			final KeyStore keyStore = keyStoreHelper.getKeyStore(new FileInputStream(resource.getFile()), PASSWORD);
			IdTokenHelper idTokenHelper;

			idTokenHelper = new IdTokenHelper.IdTokenBuilder(new IdTokenHelper.HeaderBuilder().getHeaders(),
					new IdTokenHelper.ClaimsBuilder().getClaims()).build();
			final Jwt jwt = idTokenHelper.encodeAndSign(signer);

			assertNotNull(
					JwtHelper.decodeAndVerify(jwt.getEncoded(), new RsaVerifier(keyStoreHelper.getPublicKey(keyStore, ALIAS))));
		}
		catch (KeyStoreProcessingException | JwtException e)
		{
			fail(e.getMessage());
		}
	}
}
