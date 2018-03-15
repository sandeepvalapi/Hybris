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
package de.hybris.platform.persistence.flexiblesearch.typecache.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.property.TypeInfoMap.PropertyColumnInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultCachedTypeDataTest
{
	private DefaultCachedTypeData cachedTypeData;
	@Mock
	private TypeInfoMap typeInfoMap;
	@Mock
	private PropertyColumnInfo propertyColumnInfo;

	private final PK pk = PK.createFixedUUIDPK(1, 2);

	@Before
	public void setUp() throws Exception
	{
		cachedTypeData = new DefaultCachedTypeData(typeInfoMap);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getTypePk()}.
	 */
	@Test
	public void shouldReturnTypePk()
	{
		// given
		given(typeInfoMap.getTypePK()).willReturn(pk);

		// when
		final PK typePk = cachedTypeData.getTypePk();

		// then
		assertThat(typePk).isNotNull().isEqualTo(pk);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#isAbstract()}.
	 */
	@Test
	public void shouldReturnBooleanTrueIfTypeIsAbstract()
	{
		// given
		given(Boolean.valueOf(typeInfoMap.isAbstract())).willReturn(Boolean.TRUE);

		// when
		final boolean isAbstract = cachedTypeData.isAbstract();

		// then
		assertThat(isAbstract).isTrue();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#isAbstract()}.
	 */
	@Test
	public void shouldReturnBooleanFalseIfTypeIsNotAbstract()
	{
		// given
		given(Boolean.valueOf(typeInfoMap.isAbstract())).willReturn(Boolean.FALSE);

		// when
		final boolean isAbstract = cachedTypeData.isAbstract();

		// then
		assertThat(isAbstract).isFalse();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getLocalizedTableName()}
	 * .
	 */
	@Test
	public void shouldReturnLocalizedTableNameForType()
	{
		// given
		given(typeInfoMap.getTableName(true)).willReturn("localizedTableName");

		// when
		final String localizedTableName = cachedTypeData.getLocalizedTableName();

		// then
		assertThat(localizedTableName).isNotNull().isEqualTo("localizedTableName");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getUnlocalizedTableName()}
	 * .
	 */
	@Test
	public void shouldReturnUnlocalizedTableNameForType()
	{
		// given
		given(typeInfoMap.getTableName(false)).willReturn("unlocalizedTableName");

		// when
		final String localizedTableName = cachedTypeData.getUnlocalizedTableName();

		// then
		assertThat(localizedTableName).isNotNull().isEqualTo("unlocalizedTableName");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getStandardTableName()}
	 * .
	 */
	@Test
	public void shouldReturnStandardTableNameForType()
	{
		// given
		given(typeInfoMap.getItemTableName()).willReturn("standardTableName");

		// when
		final String standardTableName = cachedTypeData.getStandardTableName();

		// then
		assertThat(standardTableName).isNotNull().isEqualTo("standardTableName");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getPropertyTableName()}
	 * .
	 */
	@Test
	public void shouldReturnPropertyTableName()
	{
		// given
		given(typeInfoMap.getOldPropTableName()).willReturn("propertyTableName");

		// when
		final String propertyTableName = cachedTypeData.getPropertyTableName();

		// then
		assertThat(propertyTableName).isNotNull().isEqualTo("propertyTableName");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getPropertyTypeForName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldReturnPropertyTypeForGivenName()
	{
		// given
		given(Integer.valueOf(typeInfoMap.getPropertyType("foobar"))).willReturn(Integer.valueOf(1));

		// when
		final int propertyType = cachedTypeData.getPropertyTypeForName("foobar");

		// then
		assertThat(propertyType).isEqualTo(1);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getLocalizedPropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldReturnLocalizedPropertyColumnNameForGivenFieldName()
	{
		// given
		given(typeInfoMap.getInfoForProperty("foobar", true)).willReturn(propertyColumnInfo);
		given(propertyColumnInfo.getColumnName()).willReturn("bazBar");

		// when
		final String localizedPropertyColumnName = cachedTypeData.getLocalizedPropertyColumnName("foobar");

		// then
		assertThat(localizedPropertyColumnName).isNotNull().isEqualTo("bazBar");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getLocalizedPropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalStateExceptionWhenPropertyColumnInfoForLocalizedPropertyIsNull()
	{
		// given
		given(typeInfoMap.getInfoForProperty("foobar", true)).willReturn(null);

		try
		{
			// when
			cachedTypeData.getLocalizedPropertyColumnName("foobar");
		}
		catch (final IllegalStateException e)
		{
			// then
			assertThat(e).hasMessage("No localized PropertyColumnInfo for field: foobar");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getUnlocalizedPropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldReturnUnlocalizedPropertyColumnNameForGivenFieldName()
	{
		// given
		given(typeInfoMap.getInfoForProperty("foobar", false)).willReturn(propertyColumnInfo);
		given(propertyColumnInfo.getColumnName()).willReturn("bazBar");

		// when
		final String localizedPropertyColumnName = cachedTypeData.getUnlocalizedPropertyColumnName("foobar");

		// then
		assertThat(localizedPropertyColumnName).isNotNull().isEqualTo("bazBar");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getUnlocalizedPropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldThrowIllegalStateExceptionWhenPropertyColumnInfoForUnlocalizedPropertyIsNull()
	{
		// given
		given(typeInfoMap.getInfoForProperty("foobar", false)).willReturn(null);

		try
		{
			// when
			cachedTypeData.getUnlocalizedPropertyColumnName("foobar");
		}
		catch (final IllegalStateException e)
		{
			// then
			assertThat(e).hasMessage("No unlocalized PropertyColumnInfo for field: foobar");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getCorePropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldReturnCorePropertyColumnNameForFieldNameWhenPropertyColumnInfoIsNotNull()
	{
		// given
		given(typeInfoMap.getInfoForCoreProperty("foobar")).willReturn(propertyColumnInfo);
		given(propertyColumnInfo.getColumnName()).willReturn("bazBar");

		// when
		final String corePropertyColumnName = cachedTypeData.getCorePropertyColumnName("foobar");

		// then
		assertThat(corePropertyColumnName).isNotNull().isEqualTo("bazBar");
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.persistence.flexiblesearch.typecache.impl.DefaultCachedTypeData#getCorePropertyColumnName(java.lang.String)}
	 * .
	 */
	@Test
	public void shouldReturnFieldNameInsteadOfCorePropertyColumnNameForFieldNameWhenPropertyColumnInfoNull()
	{
		// given
		given(typeInfoMap.getInfoForCoreProperty("foobar")).willReturn(null);

		// when
		final String corePropertyColumnName = cachedTypeData.getCorePropertyColumnName("foobar");

		// then
		assertThat(corePropertyColumnName).isNotNull().isEqualTo("foobar");
	}
}
