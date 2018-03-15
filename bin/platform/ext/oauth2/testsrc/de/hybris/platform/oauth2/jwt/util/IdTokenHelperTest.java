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

import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;


@UnitTest
public class IdTokenHelperTest
{
	private static final List<String> SCOPE = new LinkedList<>();
	private static final String SUB = "admin";
	private static final String STATE = "7";
	private static final String NONCE = "11";
	private static final String ISS = "http://localhost:8090";
	private static final String SCOPE_NAME = "scope";

	static final String ADMIN_CLAIM_SEGMENT = "{\"sub\":\"admin\",\"scope\":[\"openid\", \"hybris.document_view\"],\"iss\":\"http://localhost:8090\",\"state\":\"7\",\"exp\":1501754597,\"nonce\":\"11\",\"iat\":1501750997}";

	static final String ADMIN_RSA_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InRlc3QxIn0.eyJzdWIiOiJhZG1pbiIsImtpZCI6InRlc3QxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDkwIiwic3RhdGUiOiIzIiwic2NvcGVzIjoib3BlbmlkIiwiZXhwIjoiMTUwMTY3Mjk1MjAxOSIsIm5vbmNlIjoiNSIsImlhdCI6IjE1MDE2NjkzNTIwMTkifQ.AA_4T5Qcj_57bnbXVqy4CQ3yG9msrx6KPf-hf60_QEulIiJCO8zk7Xd7U0hA5Kb58riAHIT0zhqLb-S1H3M84aAfGXHTHKLxYUOwTbL9FSW7FJvEcY8yi5PYWtlre1o06JD1QbYD-kqpW4oZ1PgwemMrPbXoEBR550-I-1aNUAE";

	static final Map<String, String> headers = new HashMap<String, String>();

	public static String N_TEXT = "AJojY27cjdMBhEZ4o3e3tAmGs4tq+/CHnCMTWcx0vrhoOXC0FW8GzG479cQEL1E3BdYuISGhkHp7cCvzgiG6Xgfq6E06f9taxq3djRDiTK9/3YT9y/Y336z2SbinZOO3YfDloBP6FEqiAdK+Ess96lXrEmmbWFAcNOe/+lQFBE2/";
	public static String D_TEXT = "WIdkz8K7bmrbl0Io2VTYgfQs4TF0PIUW54pwtB6FgBLUkufLu7YS6mlj0c55gtLwdkCOZOuPuNl25rd4kXiiElNr2eaewznIP+m4J3oGkXgzSAxb1/ENPcxjcPJpU365w/67Z7JjliTsrXhg1BgnZhIv1lYuywu7ZdwxkS32xQk=";
	public static String E_TEXT = "AQAB";

	public static final String KID = "test1";

	public static BigInteger N;
	public static BigInteger D;
	public static BigInteger E;

	private static int idTokenValiditySeconds = 60 * 60;

	private static String algorithm = IdTokenHelper.HeaderBuilder.ALG_RS256;

	@Before
	public void setUp()
	{
		N = new BigInteger(1, Base64.getDecoder().decode(N_TEXT));
		D = new BigInteger(1, Base64.getDecoder().decode(D_TEXT));
		E = new BigInteger(1, Base64.getDecoder().decode(E_TEXT));

		headers.put("alg", algorithm);
		headers.put("typ", "JWT");
		headers.put("kid", KID);

		SCOPE.add("openid");
		SCOPE.add("hybris.document_view");
	}

	@Test
	public void buildAndVerify()
	{
		final RsaSigner signer = new RsaSigner(N, D);
		IdTokenHelper idTokenHelper;

		try
		{
			idTokenHelper = new IdTokenHelper.IdTokenBuilder(new IdTokenHelper.HeaderBuilder().alg(algorithm).kid(KID).getHeaders(),
					new IdTokenHelper.ClaimsBuilder().iss(ISS).nonce(NONCE).state(STATE).sub(SUB).iat().exp(idTokenValiditySeconds)
							.addScopes(SCOPE_NAME, SCOPE).getClaims()).build();
			final Jwt jwt = idTokenHelper.encodeAndSign(signer);

			//System.out.println(jwt.getEncoded());
			//System.out.println(jwt.getClaims());

			assertNotNull(JwtHelper.decodeAndVerify(jwt.getEncoded(), new RsaVerifier(N, E)));

		}
		catch (final JwtException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void rsaSignedTokenParsesAndVerifies()
	{
		final Jwt jwt = JwtHelper.decode(ADMIN_RSA_TOKEN);
		jwt.verifySignature(new RsaVerifier(N, E));

		final Jwt jwt3 = JwtHelper.encode(ADMIN_CLAIM_SEGMENT, new RsaSigner(N, D), headers);
		System.out.println(jwt3.getEncoded());
		jwt3.verifySignature(new RsaVerifier(N, E));
	}

	@Test(expected = InvalidSignatureException.class)
	public void invalidRsaSignatureRaisesException()
	{
		JwtHelper.decodeAndVerify(ADMIN_RSA_TOKEN, new RsaVerifier(N, D));
	}

	@Test
	public void addScopes()
	{
		IdTokenHelper.IdTokenBuilder idTokenHelper;


		idTokenHelper = new IdTokenHelper.IdTokenBuilder(new IdTokenHelper.HeaderBuilder().getHeaders(),
				new IdTokenHelper.ClaimsBuilder().addScopes(SCOPE_NAME, Stream.of("a").collect(Collectors.toList()))
						.addScopes(SCOPE_NAME, Stream.of("b").collect(Collectors.toList())).getClaims());

		final List<String> scopesOriginal = (List<String>) idTokenHelper.getClaims().get(SCOPE_NAME);
		assertTrue(scopesOriginal.contains("a"));
		assertTrue(scopesOriginal.contains("b"));
	}
}
