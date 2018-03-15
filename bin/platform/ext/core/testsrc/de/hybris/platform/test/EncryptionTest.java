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

import static junit.framework.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.encryption.EncryptionUtil;
import de.hybris.platform.util.encryption.ValueEncryptor;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Before;

@IntegrationTest
public class EncryptionTest extends HybrisJUnit4TransactionalTest
{
	private static final Logger log = Logger.getLogger(EncryptionTest.class.getName());
	private final String[] algos = new String[]
	{ "PBEWITHSHA1AND192BITAES-CBC-BC", "PBEWITHSHA1AND128BITAES-CBC-BC", "PBEWITHMD5ANDRC2", "PBEWITHSHA1ANDRC2",
			"PBEWITHSHAAND256BITAES-CBC-BC", "PBEWITHSHA256AND192BITAES-CBC-BC", "PBEWITHMD5AND256BITAES-CBC-OPENSSL",
			"PBEWITHSHAAND40BITRC4", "PBEWITHMD5AND192BITAES-CBC-OPENSSL", "PBEWITHSHAAND128BITRC4",
			"PBEWITHSHAAND2-KEYTRIPLEDES-CBC", "PBEWITHSHA-256AND256BITAES-CBC-BC", "PBEWITHSHA1ANDDES", "PBEWITHSHAANDTWOFISH-CBC",
			"PBEWITHSHAAND128BITRC2-CBC", "PBEWITHSHAAND192BITAES-CBC-BC", "PBEWithSHAAnd3KeyTripleDES",
			"PBEWITHSHA-256AND128BITAES-CBC-BC", "PBEWITHSHA-1AND128BITAES-CBC-BC", "PBEWITHSHA256AND128BITAES-CBC-BC",
			"PBEWITHSHA-256AND192BITAES-CBC-BC", "PBEWITHMD5AND128BITAES-CBC-OPENSSL", "PBEWITHSHAAND128BITAES-CBC-BC",
			"PBEWITHSHA1AND256BITAES-CBC-BC", "PBEWITHSHAANDIDEA-CBC", "PBEWITHMD5ANDDES" };



	/**
	 * getValueEncryptor() initializes lazily some encryption properties in Master Tenant Config (eg. symmetric.key.file.1)
	 * Previously test passed due to test invocation sequence which changed when upgrading to Java 8.
	 */
	@Before
	public void doBefore() {
		Registry.getMasterTenant().getValueEncryptor();
	}


	@Test
	public void testDumpProviderInfos()
	{
		ValueEncryptor.dumpProviderInfo();
	}

	@Test
	public void testEncryptionEngine()
	{
		final ValueEncryptor engine = Registry.getMasterTenant().getValueEncryptor();

		final String str = "teststring";
		String ciphertext = null;
		try
		{
			ciphertext = engine.encrypt(str);
		}
		catch (final Exception e)
		{
			log.error(e.getMessage());
		}
		log.info("Encrypted 'java.lang.String' (" + str + ") is...");
		log.info(ciphertext);
		final String plaintext = engine.decrypt(ciphertext);
		assertEquals(str, plaintext);
		log.info("Decrypted 'java.lang.String': " + plaintext);
	}

	@Test
	public void testSupportedAlgorithms()
	{
		final String keyfile = EncryptionUtil.DEFAULT_ENCRYPTION_KEY_FILE_NAME;
		SecretKey key = null;
		try
		{
			key = EncryptionUtil.loadKey(keyfile);
		}
		catch (final Exception e)
		{
			log.error(e.getMessage());
		}
		final Map<String, SecretKey> keyfiles = new HashMap<String, SecretKey>();
		keyfiles.put("1", key);

		final String plaintext = "test 1 2 3";

		final ConfigIntf cfg = Registry.getMasterTenant().getConfig();

		final String provider = cfg.getParameter(Constants.Encryption.PROVIDERCLASS);
		final String sig = cfg.getParameter(Constants.Encryption.PROVIDERSIGNATURE);

		for (int i = 0; i < algos.length; i++)
		{
			final String algo = algos[i];
			String encrypted = null;
			String decrypted = null;
			try
			{
				final ValueEncryptor engine = new ValueEncryptor(cfg, provider, sig, keyfiles, algo);
				try
				{
					encrypted = engine.encrypt(plaintext);
				}
				catch (final Exception e)
				{
					log.error(e.getMessage());
				}
				if (encrypted != null)
				{
					decrypted = engine.decrypt(encrypted);
				}
				log.info(algo + ":: PLAIN: " + plaintext + ", ENCRYPTED: " + encrypted + ", DECRYPTED: " + decrypted);
				assertEquals(plaintext, decrypted);
			}
			catch (final InvalidParameterException e)
			{
				log.error(e.getMessage());
			}
		}
	}
}
