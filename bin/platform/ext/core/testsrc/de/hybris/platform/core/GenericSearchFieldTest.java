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
package de.hybris.platform.core;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.Assert;

import org.junit.Test;


/**
 * Unit tests for {@link GenericSearchField}.
 */
@UnitTest
public class GenericSearchFieldTest
{
	private static final String FIELD_QUALIFIER = "TEST";
	private static final String OTHER_FIELD_QUALIFIER = "TEST_OTHER";
	private static final String TYPE_IDENTIFIER = "TESTTYPE";
	private static final String OTHER_TYPE_IDENTIFIER = "OTHERTYPE";

	/**
	 * Same field qualifier without type or language information.
	 */
	@Test
	public void sameFieldNoTypeShouldBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(FIELD_QUALIFIER);

		Assert.assertEquals(field1, field2);
	}

	/**
	 * Different field qualifiers without type or langauge information.
	 */
	@Test
	public void differentFieldsShouldNotBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(OTHER_FIELD_QUALIFIER);

		Assert.assertNotEquals(field1, field2);
	}

	/**
	 * Same field qualifier and same type identifier.
	 */
	@Test
	public void sameFieldWithSameTypeShouldBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(TYPE_IDENTIFIER, FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(TYPE_IDENTIFIER, FIELD_QUALIFIER);

		Assert.assertEquals(field1, field2);
	}

	/**
	 * Same field qualifier but different type identifiers.
	 */
	@Test
	public void sameFieldWithDifferentTypesShouldNotBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(TYPE_IDENTIFIER, FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(OTHER_TYPE_IDENTIFIER, FIELD_QUALIFIER);

		Assert.assertNotEquals(field1, field2);
	}

	/**
	 * Same field qualifier and same language, no type information.
	 */
	@Test
	public void sameFieldWithSameLanguageShouldBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(FIELD_QUALIFIER);

		field1.setLanguagePK(PK.NULL_PK);
		field2.setLanguagePK(PK.NULL_PK);

		Assert.assertEquals(field1, field2);
	}

	/**
	 * Same field qualifier but different languages, no type information.
	 */
	@Test
	public void sameFieldWithDifferentLanguageShouldNotBeEqual()
	{
		final GenericSearchField field1 = new GenericSearchField(FIELD_QUALIFIER);
		final GenericSearchField field2 = new GenericSearchField(FIELD_QUALIFIER);

		field1.setLanguagePK(PK.NULL_PK);
		field2.setLanguagePK(PK.BIG_PK);

		Assert.assertNotEquals(field1, field2);
	}
}
