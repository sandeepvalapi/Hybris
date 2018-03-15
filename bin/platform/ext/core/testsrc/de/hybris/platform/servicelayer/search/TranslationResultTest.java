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
package de.hybris.platform.servicelayer.search;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


@UnitTest
public class TranslationResultTest
{
	@Test
	public void shouldSetResultListAsLocalUnmodifiableCopyInsteadOfUsingReference()
	{
		// given
		final List<Object> unsafeList = new ArrayList<Object>();
		unsafeList.add("Foo");
		unsafeList.add("Bar");

		// when
		final TranslationResult tResult = new TranslationResult("SELECT {PK} FROM {Product}", unsafeList);
		unsafeList.clear();

		// then
		assertThat(unsafeList).isEmpty();
		assertThat(tResult.getSQLQueryParameters()).isNotEmpty();
		assertThat(tResult.getSQLQueryParameters()).hasSize(2);
		try
		{
			tResult.getSQLQueryParameters().add("Baz");
			fail("Should throw UnsupportedOperationException");
		}
		catch (final UnsupportedOperationException e)
		{
			// that's OK
		}
	}
}
