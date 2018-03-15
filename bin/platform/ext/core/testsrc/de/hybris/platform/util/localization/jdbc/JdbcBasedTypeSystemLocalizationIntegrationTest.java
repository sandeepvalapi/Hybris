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
package de.hybris.platform.util.localization.jdbc;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.TestEnum;
import de.hybris.platform.core.model.test.TestItemModel;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.localization.jdbc.executor.BatchedStatementsExecutor;
import de.hybris.platform.util.localization.jdbc.info.JaloBasedDbInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.sql.DataSource;

import de.hybris.platform.util.localization.jdbc.info.PropertiesBasedLocalizationInfo;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;


@IntegrationTest
public class JdbcBasedTypeSystemLocalizationIntegrationTest extends ServicelayerBaseTest
{
	private static final String TEST_TYPECODE = TestItemModel._TYPECODE.toLowerCase();
	private static final String TEST_TYPE_NAME_KEY = "type." + TEST_TYPECODE + ".name";
	private static final String TEST_TYPE_DESCRIPTION_KEY = "type." + TEST_TYPECODE + ".description";

	private static final String SUBTYPE_TYPECODE = TestItemType2Model._TYPECODE.toLowerCase();

	private static final String TEST_ATTRIBUTE_QUALIFIER = TestItemModel.DOUBLE.toLowerCase();
	private static final String TEST_ATTRIBUTE_NAME_KEY = "type." + TEST_TYPECODE + "." + TEST_ATTRIBUTE_QUALIFIER + ".name";
	private static final String TEST_ATTRIBUTE_DESCRIPTION_KEY = "type." + TEST_TYPECODE + "." + TEST_ATTRIBUTE_QUALIFIER
			+ ".description";

	private static final String SUBTYPE_ATTRIBUTE_QUALIFIER = TestItemType2Model.DOUBLE.toLowerCase();
	private static final String SUBTYPE_ATTRIBUTE_NAME_KEY = "type." + SUBTYPE_TYPECODE + "." + SUBTYPE_ATTRIBUTE_QUALIFIER + ".name";
	private static final String SUBTYPE_ATTRIBUTE_DESCRIPTION_KEY = "type." + SUBTYPE_TYPECODE + "." + SUBTYPE_ATTRIBUTE_QUALIFIER
			+ ".description";

	private static final String TEST_ENUM_TYPECODE = TestEnum._TYPECODE.toLowerCase();
	private static final String TEST_ENUM_VALUE_CODE = TestEnum.TESTVALUE2.getCode().toLowerCase();
	private static final String TEST_ENUM_VALUE_NAME_KEY = "type." + TEST_ENUM_TYPECODE + "." + TEST_ENUM_VALUE_CODE + ".name";

	@Resource
	private DataSource dataSource;

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;

	private Language currentLanguage;
	private JaloBasedDbInfo dbInfo;


	private final Map<Long, String> pkToType = new HashMap<>();

	@Before
	public void setUp()
	{
		currentLanguage = Registry.getCurrentTenantNoFallback().getActiveSession().getSessionContext().getLanguage();
		dbInfo = new JaloBasedDbInfo();

		final ComposedTypeModel superType = typeService.getComposedTypeForCode(TEST_TYPECODE);
		final ComposedTypeModel subtype= typeService.getComposedTypeForCode(SUBTYPE_TYPECODE);

		pkToType.put(superType.getPk().getLong(), TEST_TYPECODE);
		pkToType.put(subtype.getPk().getLong(), SUBTYPE_TYPECODE);
	}

	@Test
	public void shouldSeeChangesAfterTypeNameChange()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_TYPE_NAME_KEY, newName), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldSeeChangesAfterTypeDescriptionChange()
	{
		//given
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_TYPE_DESCRIPTION_KEY, newDescription), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldSeeChangesWhenBothNameAndDescriptionOfTypeHaveCahnged()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final String newName = concat(nameBefore, "CN");
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(TEST_TYPE_NAME_KEY, newName, TEST_TYPE_DESCRIPTION_KEY, newDescription), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldNotChangeTypeNameOrDescriptionWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionBefore = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getName();
		final String descriptionAfter = typeService.getComposedTypeForCode(TEST_TYPECODE).getDescription();
		assertThat(nameAfter).isEqualTo(nameBefore);
		assertThat(descriptionAfter).isEqualTo(descriptionBefore);
	}

	@Test
	public void shouldSeeChangesAfterAttributeNameChange()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_NAME_KEY, newName), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldSeeChangesAfterAttributeDescriptionChange()
	{
		//given
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_DESCRIPTION_KEY, newDescription), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldSeeChangesWhenBothNameAndDescriptionOfAttributeHaveCahnged()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final String newName = concat(nameBefore, "CN");
		final String newDescription = concat(descriptionBefore, "CD");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(TEST_ATTRIBUTE_NAME_KEY, newName, TEST_ATTRIBUTE_DESCRIPTION_KEY, newDescription), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);
	}

	@Test
	public void shouldNotChangeAttributeNameOrDescriptionWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER)
				.getDescription();
		assertThat(nameAfter).isEqualTo(nameBefore);
		assertThat(descriptionAfter).isEqualTo(descriptionBefore);
	}

	@Test
	public void shouldSeeChangesAfterEnumValueNameChange()
	{
		//given
		final String nameBefore = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		final String newName = concat(nameBefore, "CN");
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ENUM_VALUE_NAME_KEY, newName), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);
	}

	@Test
	public void shouldNotChangeEnumValueNameWhenNoLocalizationIsGiven()
	{
		//given
		final String nameBefore = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(),
				ImmutableMap.<String, String> of(), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getEnumerationValue(TEST_ENUM_TYPECODE, TEST_ENUM_VALUE_CODE).getName();
		assertThat(nameAfter).isEqualTo(nameBefore);
	}

	@Test
	public void shouldAllowToOverrideAttributeNameInSubtype()
	{
		//given
		final String nameBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		final String newName = concat(nameBefore, "CN");

		final String subtypeNameBefore = typeService.getAttributeDescriptor(SUBTYPE_TYPECODE, SUBTYPE_ATTRIBUTE_QUALIFIER).getName();
		final String newSubtypeName = concat(subtypeNameBefore, "SUB_CN");

		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_NAME_KEY, newName, SUBTYPE_ATTRIBUTE_NAME_KEY, newSubtypeName), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String nameAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getName();
		assertThat(nameAfter).isNotNull().isNotEqualTo(nameBefore).isEqualTo(newName);

		final String subtypeNameAfter = typeService.getAttributeDescriptor(SUBTYPE_TYPECODE, SUBTYPE_ATTRIBUTE_QUALIFIER).getName();
		assertThat(subtypeNameAfter).isNotNull().isNotEqualTo(subtypeNameBefore).isEqualTo(newSubtypeName);
	}

	@Test
	public void shouldAllowToOverrideAttributeDescriptionInSubtype()
	{
		//given
		final String descriptionBefore = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getDescription();
		final String newDescription = concat(descriptionBefore, "CN");

		final String subtypeDescriptionBefore = typeService.getAttributeDescriptor(SUBTYPE_TYPECODE, SUBTYPE_ATTRIBUTE_QUALIFIER).getDescription();
		final String newSubtypeDescription = concat(subtypeDescriptionBefore, "SUB_CN");

		final LocalizationInfo localizationInfo = new TestLocalizationInfo(currentLanguage.getPK(), ImmutableMap.of(
				TEST_ATTRIBUTE_DESCRIPTION_KEY, newDescription, SUBTYPE_ATTRIBUTE_DESCRIPTION_KEY, newSubtypeDescription), pkToType);

		//when
		localizeTypeSystem(localizationInfo);

		//then
		final String descriptionAfter = typeService.getAttributeDescriptor(TEST_TYPECODE, TEST_ATTRIBUTE_QUALIFIER).getDescription();
		assertThat(descriptionAfter).isNotNull().isNotEqualTo(descriptionBefore).isEqualTo(newDescription);

		final String subTypeDescriptionAfter = typeService.getAttributeDescriptor(SUBTYPE_TYPECODE, SUBTYPE_ATTRIBUTE_QUALIFIER).getDescription();
		assertThat(subTypeDescriptionAfter).isNotNull().isNotEqualTo(subtypeDescriptionBefore).isEqualTo(newSubtypeDescription);
	}


	private String concat(final String s, final String suffix)
	{
		Preconditions.checkNotNull(suffix);
		return (s == null ? "" : s) + suffix;
	}

	private void localizeTypeSystem(final LocalizationInfo localizationInfo)
	{
		modelService.detachAll();
		final ExecutorService executorService = MoreExecutors.newDirectExecutorService();
		final BatchedStatementsExecutor statementsExecutor = new BatchedStatementsExecutor(executorService, dataSource);
		new JdbcBasedTypeSystemLocalization(executorService, dataSource, dbInfo, localizationInfo, statementsExecutor)
				.localizeTypeSystem();
	}

	private static class TestLocalizationInfo extends PropertiesBasedLocalizationInfo
	{
		private final Map<String, String> localizations;
		private final PK languagePK;

		public TestLocalizationInfo(final PK languagePK, final Map<String, String> localizations, final Map<Long, String> pkToType)
		{
			super(Collections.emptyMap(), pkToType);
			Preconditions.checkNotNull(languagePK);
			Preconditions.checkNotNull(localizations);

			this.languagePK = languagePK;
			this.localizations = localizations;
		}

		@Override
		public Collection<PK> getLanguagePKs()
		{
			return ImmutableSet.of(languagePK);
		}

		@Override
		public String getLocalizedProperty(final PK languagePK, final String propertyKey)
		{
			if (!this.languagePK.equals(languagePK))
			{
				return null;
			}
			return localizations.get(propertyKey);
		}

	}
}
