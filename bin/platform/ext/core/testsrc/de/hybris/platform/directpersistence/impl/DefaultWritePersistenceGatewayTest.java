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
package de.hybris.platform.directpersistence.impl;

import static org.fest.assertions.Assertions.assertThat;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.test.TestItemType3Model;
import de.hybris.platform.core.systemsetup.datacreator.impl.C2LDataCreator;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

@IntegrationTest
public class DefaultWritePersistenceGatewayTest extends ServicelayerTransactionalBaseTest
{
	private static final String PK_COLNAME = "PK";
	private static final String LANG_PK_COLNAME = "LANGPK";
	private static final String ITEMPK_COLNAME = "ITEMPK";
	private static final String TESTITEM_TABLE = getTablePrefix() + "testitem";
	private static final String TESTITEM_LP_TABLE = getTablePrefix() + "testitemlp";
	private static final String PROPS_TABLE = getTablePrefix() + "props";
	private static final String ITEM_TYPE = "TestItemType3";
	private static final String TEST_PROPERTY2_PROPERTY = "testProperty2";
	private static final String PROP_PROPERTY = "prop";
	private static final String PROP2_PROPERTY = "prop2";
	private static final String XXX_PROPERTY = "xxx";

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private DefaultWritePersistenceGateway writePersistenceGateway;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	private MutableChangeSet changeSet;
	@Resource
	private C2LDataCreator c2lDataCreator;

	private PK langEnPk;
	private PK langDePk;

	private static String getTablePrefix()
	{
		return Strings.nullToEmpty(Config.getParameter("db.tableprefix"));
	}

	@Before
	public void setUp() throws Exception
	{
		langEnPk = c2lDataCreator.createOrGetLanguage("en", true).getPK();
		langDePk = c2lDataCreator.createOrGetLanguage("de", true).getPK();
		changeSet = new DefaultChangeSet();
	}

	@Test
	public void shouldStoreTestItemWithUnlocalizedOnlyProperties()
	{
		// given
		final PK pk = generatePkForCode(ITEM_TYPE);
		final InsertRecord insertRecord = new InsertRecord(pk, ITEM_TYPE, Sets.newHashSet(new PropertyHolder(XXX_PROPERTY,
				  "some value")));
		changeSet.add(insertRecord);

		// when
		writePersistenceGateway.persist(changeSet);

		// then
		// (check low level)
		final Map<String, Object> foundRecords = findRecords(pk, TESTITEM_TABLE, PK_COLNAME);
		assertThat(foundRecords).isNotNull();
		assertThat(foundRecords.get("p_xxx")).isNotNull().isEqualTo("some value");
		// (check flexible search)
		final TestItemType3Model foundModel = findTestModel(pk);
		assertThat(foundModel).isNotNull();
		assertThat(foundModel.getXxx()).isNotNull().isEqualTo("some value");
	}

	@Test
	public void shouldStoreTestItemWithUnlocalizedAndLocalizedProperties()
	{
		// given
		final PK pk = generatePkForCode(ITEM_TYPE);
		final InsertRecord insertRecord = new InsertRecord(pk, ITEM_TYPE, Sets.newHashSet(new PropertyHolder(XXX_PROPERTY,
				  "some value")), mapOf(new Locale("en"), Sets.newHashSet(new PropertyHolder(TEST_PROPERTY2_PROPERTY,
				  "english val")),
				  new Locale("de"), Sets.newHashSet(new PropertyHolder(TEST_PROPERTY2_PROPERTY, "german val"))));
		changeSet.add(insertRecord);

		// when
		writePersistenceGateway.persist(changeSet);

		// then
		// (check low level)
		final Map<String, Object> foundRecords = findRecords(pk, TESTITEM_TABLE, PK_COLNAME);
		assertThat(foundRecords).isNotNull();
		assertThat(foundRecords.get("p_xxx")).isNotNull().isEqualTo("some value");
		final Map<String, Object> foundEnRecords = findRecords(pk, langEnPk, TESTITEM_LP_TABLE, ITEMPK_COLNAME, LANG_PK_COLNAME);
		assertThat(foundEnRecords).isNotNull();
		assertThat(foundEnRecords.get("p_testproperty2")).isNotNull().isEqualTo("english val");
		final Map<String, Object> foundDeRecords = findRecords(pk, langDePk, TESTITEM_LP_TABLE, ITEMPK_COLNAME, LANG_PK_COLNAME);
		assertThat(foundDeRecords).isNotNull();
		assertThat(foundDeRecords.get("p_testproperty2")).isNotNull().isEqualTo("german val");
		// (check flexible search)
		final TestItemType3Model foundModel = findTestModel(pk);
		assertThat(foundModel).isNotNull();
		assertThat(foundModel.getXxx()).isNotNull().isEqualTo("some value");
		assertThat(foundModel.getTestProperty2(new Locale("en"))).isNotNull().isEqualTo("english val");
		assertThat(foundModel.getTestProperty2(new Locale("de"))).isNotNull().isEqualTo("german val");
	}

	@Test
	public void shouldStoreTestItemWithUnlocalizedPropertiesAndUnlocalizedPropsValues()
	{
		// given
		final PK pk = generatePkForCode(ITEM_TYPE);
		final InsertRecord insertRecord = new InsertRecord(pk, ITEM_TYPE, Sets.newHashSet(new PropertyHolder(XXX_PROPERTY,
				  "some value"), new PropertyHolder(PROP_PROPERTY, "prop value")));
		changeSet.add(insertRecord);

		// when
		writePersistenceGateway.persist(changeSet);

		// then
		// (check low level)
		final Map<String, Object> foundRecords = findRecords(pk, TESTITEM_TABLE, PK_COLNAME);
		assertThat(foundRecords).isNotNull();
		assertThat(foundRecords.get("p_xxx")).isNotNull().isEqualTo("some value");
		final Map<String, Object> foundPropsRecords = findRecords(pk, PROPS_TABLE, ITEMPK_COLNAME);
		assertThat(foundPropsRecords).isNotNull();
		assertThat(foundPropsRecords.get("NAME")).isEqualTo(PROP_PROPERTY);
		assertThat(foundPropsRecords.get("REALNAME")).isEqualTo(PROP_PROPERTY);
		assertThat(foundPropsRecords.get("VALUESTRING1")).isEqualTo("prop value");
		// (check flexible search)
		final TestItemType3Model foundModel = findTestModel(pk);
		assertThat(foundModel).isNotNull();
		assertThat(foundModel.getXxx()).isNotNull().isEqualTo("some value");
		assertThat(foundModel.getProp()).isNotNull().isEqualTo("prop value");
	}

	@Test
	public void shouldStoreTestItemWithUnlocalizedPropertiesAndLocalizedPropsValues()
	{
		// given
		final PK pk = generatePkForCode(ITEM_TYPE);
		final InsertRecord insertRecord = new InsertRecord(pk, ITEM_TYPE, Sets.newHashSet(new PropertyHolder(XXX_PROPERTY,
				  "some evil value")), mapOf(new Locale("en"), Sets.newHashSet(new PropertyHolder(PROP2_PROPERTY, "english val")),
				  new Locale("de"), Sets.newHashSet(new PropertyHolder(PROP2_PROPERTY, "german val"))));
		changeSet.add(insertRecord);

		// when
		writePersistenceGateway.persist(changeSet);

		// then
		// (check low level)
		final Map<String, Object> foundRecords = findRecords(pk, TESTITEM_TABLE, PK_COLNAME);
		assertThat(foundRecords).isNotNull();
		assertThat(foundRecords.get("p_xxx")).isNotNull().isEqualTo("some evil value");
		final Map<String, Object> foundEnPropsRecords = findRecords(pk, langEnPk, PROPS_TABLE, ITEMPK_COLNAME, LANG_PK_COLNAME);
		assertThat(foundEnPropsRecords).isNotNull();
		assertThat(foundEnPropsRecords.get("NAME")).isEqualTo(PROP2_PROPERTY);
		assertThat(foundEnPropsRecords.get("REALNAME")).isEqualTo(PROP2_PROPERTY);
		assertThat(foundEnPropsRecords.get("VALUESTRING1")).isEqualTo("english val");
		final Map<String, Object> foundDePropsRecords = findRecords(pk, langDePk, PROPS_TABLE, ITEMPK_COLNAME, LANG_PK_COLNAME);
		assertThat(foundDePropsRecords).isNotNull();
		assertThat(foundDePropsRecords.get("NAME")).isEqualTo(PROP2_PROPERTY);
		assertThat(foundDePropsRecords.get("REALNAME")).isEqualTo(PROP2_PROPERTY);
		assertThat(foundDePropsRecords.get("VALUESTRING1")).isEqualTo("german val");
		// (check flexible search)
		final TestItemType3Model foundModel = findTestModel(pk);
		assertThat(foundModel).isNotNull();
		assertThat(foundModel.getXxx()).isNotNull().isEqualTo("some evil value");
		assertThat(foundModel.getProp2(new Locale("en"))).isNotNull().isEqualTo("english val");
		assertThat(foundModel.getProp2(new Locale("de"))).isNotNull().isEqualTo("german val");

	}

	private PK generatePkForCode(final String typeCode)
	{
		final TypeInfoMap persistenceInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
		return PK.createCounterPK(persistenceInfo.getItemTypeCode());
	}

	private TestItemType3Model findTestModel(final PK pk)
	{
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {PK} FROM {TestItemType3} WHERE PK = ?pk");
		fsQuery.addQueryParameter("pk", pk);
		final SearchResult<TestItemType3Model> searchResult = flexibleSearchService.search(fsQuery);
		final List<TestItemType3Model> result = searchResult.getResult();
		return result.isEmpty() ? null : result.get(0);

	}

	private Map<String, Object> findRecords(final PK pk, final String tableName, final String pkColName)
	{
		final String query = String.format("SELECT * FROM %s WHERE %s = ?", tableName, pkColName);
		try
		{
			return jdbcTemplate.queryForMap(query, Long.valueOf(pk.getLongValue()));
		}
		catch (final EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	private Map<String, Object> findRecords(final PK pk, final PK langPk, final String tableName, final String pkColName,
			  final String langPkColName)
	{
		final String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?", tableName, pkColName, langPkColName);
		try
		{
			return jdbcTemplate.queryForMap(query, Long.valueOf(pk.getLongValue()), Long.valueOf(langPk.getLongValue()));
		}
		catch (final EmptyResultDataAccessException e)
		{
			return null;
		}
	}

	private Map<Locale, Set<PropertyHolder>> mapOf(final Locale locale, final Set<PropertyHolder> value, final Locale locale2,
			  final Set<PropertyHolder> value2)
	{
		return ImmutableMap.<Locale, Set<PropertyHolder>>of(locale, value, locale2, value2);
	}
}
