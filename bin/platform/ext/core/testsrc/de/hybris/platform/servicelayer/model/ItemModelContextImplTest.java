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
package de.hybris.platform.servicelayer.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.testframework.Assert;

import java.util.Collections;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for {@link ItemModelContextImpl}.
 */
@IntegrationTest
public class ItemModelContextImplTest
{

	/**
	 * Context builder for product.
	 */
	private ItemContextBuilder productContextBuilder;
	/**
	 * Context builder for other product.
	 */
	private ItemContextBuilder otherProductContextBuilder;

	@Before
	public void setUp()
	{
		productContextBuilder = ItemContextBuilder.createMockContextBuilder(ProductModel.class, null, Locale.ENGLISH,
				Collections.emptyMap());
		otherProductContextBuilder = ItemContextBuilder.createMockContextBuilder(ProductModel.class, PK.NULL_PK, Locale.GERMAN,
				Collections.emptyMap());
	}

	/**
	 * Test implementation of {@link ItemModelContextImpl#equals(Object)} and {@link ItemModelContextImpl#hashCode()}.
	 */
	@Test
	public void sameContextImplShouldBeEqual()
	{
		// Given contexts with same PK and tenant ...
		final ItemModelInternalContext productContext1 = productContextBuilder.build();
		final ItemModelInternalContext productContext2 = productContextBuilder.build();

		// .. then equals() should return true and has code should be identical.
		Assert.assertEquals(productContext1, productContext2);
	}

	@Test
	public void differentPKsShouldNotBeEqual()
	{
		// Given contexts with different PK and same tenant ...
		final ItemModelInternalContext productContext1 = productContextBuilder.build();
		final ItemModelInternalContext productContext2 = otherProductContextBuilder.build();

		// .. then equals() should return true and has code should be identical.
		Assert.assertNotEquals(productContext1, productContext2);
	}

}
