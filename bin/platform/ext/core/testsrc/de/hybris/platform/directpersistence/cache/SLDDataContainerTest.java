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
package de.hybris.platform.directpersistence.cache;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.TypeInfoMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class SLDDataContainerTest
{
	private PK itemPk, typePk, langPk;
	@Mock
	private TypeInfoMap typeInfoMap;

	@Before
	public void setUp() throws Exception
	{
		itemPk = PK.createFixedUUIDPK(1, 1);
		typePk = PK.createFixedUUIDPK(2, 2);
		langPk = PK.createFixedUUIDPK(3, 3);
		given(typeInfoMap.getCode()).willReturn("fooBarBaz");
	}

	@Test
	public void shouldCreateContainerOnlyWithAttribute() throws Exception
	{
		// given
        final SLDDataContainer.AttributeValue fooAttrVal = new SLDDataContainer.AttributeValue("foo", "bar");

		// when
		final SLDDataContainer container = SLDDataContainer.builder() //
				.withPk(itemPk) //
				.withTypePk(typePk) //
				.withTypeInfoMap(typeInfoMap) //
				.withAttributes(Lists.newArrayList(fooAttrVal)) //
				.withVersion(Long.valueOf(1)).build();

		// then
		assertThat(container).isNotNull();
		assertThat(container.getPk()).isEqualTo(itemPk);
		assertThat(container.getTypePk()).isEqualTo(typePk);
		assertThat(container.getTypeCode()).isEqualTo("fooBarBaz");
		assertThat(container.getVersion()).isEqualTo(1);
		assertThat(container.getAttributeValue("foo", null)).isEqualTo(fooAttrVal);
		assertThat(container.getAttributeValue("foo", langPk)).isNull();
		assertThat(container.getAttributeValue("nonExistent", null)).isNull();
		assertThat(container.getAttributeValue("nonExistent", langPk)).isNull();
	}

	@Test
	public void shouldCreateContainerWithAttributesAndLocalizedAttributes() throws Exception
	{
		// given
        final SLDDataContainer.AttributeValue fooAttrVal = new SLDDataContainer.AttributeValue("foo", "bar");
        final SLDDataContainer.AttributeValue barAttrVal = new SLDDataContainer.AttributeValue("bar", "someVal", langPk);

		// when
		final SLDDataContainer container = SLDDataContainer.builder() //
				.withPk(itemPk) //
				.withTypePk(typePk) //
				.withTypeInfoMap(typeInfoMap) //
				.withAttributes(Lists.newArrayList(fooAttrVal)) //
				.withLocalizedAttributes(Lists.newArrayList(barAttrVal)) //
				.withVersion(Long.valueOf(1)).build();

		// then
		assertThat(container).isNotNull();
		assertThat(container.getPk()).isEqualTo(itemPk);
		assertThat(container.getTypePk()).isEqualTo(typePk);
		assertThat(container.getTypeCode()).isEqualTo("fooBarBaz");
		assertThat(container.getVersion()).isEqualTo(1);
		assertThat(container.getAttributeValue("foo", null)).isEqualTo(fooAttrVal);
		assertThat(container.getAttributeValue("nonExistent", null)).isNull();
		assertThat(container.getAttributeValue("nonExistent", langPk)).isNull();
		assertThat(container.getAttributeValue("bar", langPk)).isEqualTo(barAttrVal);
	}

	@Test
	public void shouldCreateContainerWithAttributesLocalizedAttributesAndProperties() throws Exception
	{
		// given
       final SLDDataContainer.AttributeValue fooAttrVal = new SLDDataContainer.AttributeValue("foo", "bar");
        final SLDDataContainer.AttributeValue barAttrVal = new SLDDataContainer.AttributeValue("bar", "someVal", langPk);
        final SLDDataContainer.AttributeValue bazPropVal = new SLDDataContainer.AttributeValue("baz", "barbaz");
        final SLDDataContainer.AttributeValue blurpPropVal = new SLDDataContainer.AttributeValue("blurp", "muuu", langPk);


		// when
		final SLDDataContainer container = SLDDataContainer.builder() //
				.withPk(itemPk) //
				.withTypePk(typePk) //
				.withTypeInfoMap(typeInfoMap) //
				.withAttributes(Lists.newArrayList(fooAttrVal)) //
				.withLocalizedAttributes(Lists.newArrayList(barAttrVal)) //
				.withProperties(Lists.newArrayList(bazPropVal, blurpPropVal)) //
				.withVersion(Long.valueOf(1)).build();

		// then
		assertThat(container).isNotNull();
		assertThat(container.getPk()).isEqualTo(itemPk);
		assertThat(container.getTypePk()).isEqualTo(typePk);
		assertThat(container.getTypeCode()).isEqualTo("fooBarBaz");
		assertThat(container.getVersion()).isEqualTo(1);
		assertThat(container.getAttributeValue("foo", null)).isEqualTo(fooAttrVal);
		assertThat(container.getAttributeValue("nonExistent", null)).isNull();
		assertThat(container.getAttributeValue("nonExistent", langPk)).isNull();
		assertThat(container.getAttributeValue("bar", langPk)).isEqualTo(barAttrVal);
		assertThat(container.getAttributeValue("baz", null)).isEqualTo(bazPropVal);
		assertThat(container.getAttributeValue("blurp", langPk)).isEqualTo(blurpPropVal);
	}

	@Test
	public void shouldThrowAnExceptionWhenDataContainerBuilderHasNoTypeInfoMap() throws Exception
	{
		try
		{
			// when
			SLDDataContainer.builder() //
					.withPk(itemPk) //
					.withTypePk(typePk) //
					.withVersion(Long.valueOf(1)).build();
			fail("should throw NullPointerException");

		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}
}
