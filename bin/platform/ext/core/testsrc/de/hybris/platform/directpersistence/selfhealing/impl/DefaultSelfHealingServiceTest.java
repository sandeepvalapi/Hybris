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
package de.hybris.platform.directpersistence.selfhealing.impl;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.selfhealing.ItemToHeal;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultSelfHealingServiceTest extends ServicelayerBaseTest
{

	private static final String TABLE_A = "tableA";
	private static final String COLUMN_A1 = "columnA1";
	private static final String COLUMN_A2 = "columnA2";
	private static final String TABLE_B = "tableB";
	private static final String COLUMN_B1 = "columnB1";
	private static final String COLUMN_B2 = "columnB2";

	@Resource
	private WritePersistenceGateway writePersistenceGateway;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private JdbcTemplate jdbcTemplate;

	private DefaultSelfHealingService selfHealingService;

	private List<Long> range;

	@Before
	public void setup()
	{
		range = createRange();


		selfHealingService = new DefaultSelfHealingService();
		selfHealingService.setWritePersistenceGateway(writePersistenceGateway);
		selfHealingService.setBatchLimit(10000);

		// we *don't* want the background process to start -> therefore we *don't* call afterPropertiesSet() which would start it!
		selfHealingService.setEnabled(true);
	}


	@Test
	public void shouldReferencedValueBeNullWhenReferencedModelIsRemoved()
	{
		//given
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("id");

		final CompanyModel companyModel = modelService.create(CompanyModel.class);
		companyModel.setId("id_comp");
		companyModel.setUid("id_comp");

		catalogModel.setBuyer(companyModel);

		modelService.saveAll();

		//when
		modelService.remove(companyModel);

		final ItemToHeal catalogToHeal = new ItemToHeal(catalogModel.getPk(), CatalogModel._TYPECODE, CatalogModel.BUYER,
				((Item) modelService.getSource(catalogModel)).getPersistenceVersion(), null);

		selfHealingService.addItemToHeal(catalogToHeal);

		selfHealingService.batchItems();

		//then
		final AttributeDescriptorModel attrDescBuyer = typeService.getAttributeDescriptor(CatalogModel._TYPECODE,
				CatalogModel.BUYER);


		final String sql = FluentSqlBuilder.genericBuilder().select(attrDescBuyer.getDatabaseColumn())
				.from(attrDescBuyer.getEnclosingType().getTable()).where().field(ServiceCol.PK_STRING.colName()).isEqual().toSql();

		final String persistedBuyer = jdbcTemplate.queryForObject(sql, new Object[]
		{ catalogToHeal.getPk().getLong() }, String.class);


		assertThat(persistedBuyer).isNullOrEmpty();
	}

	@Test
	public void shouldNotIncrementVersionWhenPersisted()
	{
		//given
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("id");

		final CompanyModel companyModel = modelService.create(CompanyModel.class);
		companyModel.setId("id_comp");
		companyModel.setUid("id_comp");

		catalogModel.setBuyer(companyModel);

		modelService.saveAll();


		//when
		final Long version = Long.valueOf(3);
		final ItemToHeal catalogToHeal = new ItemToHeal(catalogModel.getPk(), CatalogModel._TYPECODE, CatalogModel.BUYER,
				version.longValue(), null);

		selfHealingService.addItemToHeal(catalogToHeal);

		selfHealingService.batchItems();

		//then
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(CatalogModel.class);

		final String sql = FluentSqlBuilder.genericBuilder().select(ServiceCol.HJMP_TS.colName()).from(composedTypeModel.getTable())
				.where().field(ServiceCol.PK_STRING.colName()).isEqual().toSql();

		final Long persistedBuyer = jdbcTemplate.queryForObject(sql, new Object[]
		{ catalogToHeal.getPk().getLong() }, Long.class);


		assertThat(persistedBuyer).isEqualTo(version);
	}

	@Test
	public void shouldRefencedCollecionValueChangeWhenReferencedModelCollectionChanged()
	{
		//given
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("id_catalog");

		final CatalogVersionModel catalogVersionModel1 = modelService.create(CatalogVersionModel.class);
		catalogVersionModel1.setCatalog(catalogModel);
		catalogVersionModel1.setVersion("version1");

		final CatalogVersionModel catalogVersionModel2 = modelService.create(CatalogVersionModel.class);
		catalogVersionModel2.setCatalog(catalogModel);
		catalogVersionModel2.setVersion("version2");

		final CustomerModel customerModel = modelService.create(CustomerModel.class);
		customerModel.setUid("uid_customer");

		customerModel.setPreviewCatalogVersions(Sets.newHashSet(catalogVersionModel1, catalogVersionModel2));

		modelService.saveAll();

		assertThat(customerModel.getPreviewCatalogVersions()).hasSize(2);

		//when
		modelService.remove(catalogVersionModel2);

		final ItemPropertyValue catalogVersionModel1ItemPropertyValue = new ItemPropertyValue(catalogVersionModel1.getPk());
		/*
		 * Uses Jalo which has it own health healing service. Use JDBCTemplate to verify data instead.
		 *
		 * modelService.refresh(customerModel);
		 */

		final ItemPropertyValueCollection itemPropertyValueCollection = new ItemPropertyValueCollection(
				Sets.newHashSet(catalogVersionModel1ItemPropertyValue));

		final ItemToHeal custumerToHeal = new ItemToHeal(customerModel.getPk(), CustomerModel._TYPECODE,
				CustomerModel.PREVIEWCATALOGVERSIONS, ((Item) modelService.getSource(customerModel)).getPersistenceVersion(),
				itemPropertyValueCollection);

		selfHealingService.addItemToHeal(custumerToHeal);

		selfHealingService.batchItems();


		//then
		final AttributeDescriptorModel attrDescPreviewCatalogVersions = typeService.getAttributeDescriptor(CustomerModel._TYPECODE,
				CustomerModel.PREVIEWCATALOGVERSIONS);


		final String sql = FluentSqlBuilder.genericBuilder().select(attrDescPreviewCatalogVersions.getDatabaseColumn())
				.from(attrDescPreviewCatalogVersions.getEnclosingType().getTable()).where().field(ServiceCol.PK_STRING.colName())
				.isEqual().toSql();

		final String persistedPreviewCatalogVersionsDbValue = jdbcTemplate.queryForObject(sql, new Object[]
		{ custumerToHeal.getPk().getLong() }, String.class);


		final ItemPropertyValueCollection itemPropValueCollection = (ItemPropertyValueCollection) JDBCValueMappings.getInstance()
				.getValueReader(ItemPropertyValueCollection.class).convertValueToJava(persistedPreviewCatalogVersionsDbValue);

		assertThat(itemPropValueCollection).containsExactly(catalogVersionModel1ItemPropertyValue);
	}


	@Test
	public void shouldKeepWorkingWhenConcurrentItemAdditionsOccure() throws InterruptedException, ExecutionException
	{
		//given
		final WritePersistenceGateway mock = Mockito.mock(WritePersistenceGateway.class);
		selfHealingService.setWritePersistenceGateway(mock);


		//when
		final TestThreadsHolder<Runnable> holderA1 = new TestThreadsHolder<>(10,
				() -> range.forEach(l -> selfHealingService.addItemToHeal(createItemToHeal(l, TABLE_A, COLUMN_A1))));
		final TestThreadsHolder<Runnable> holderA2 = new TestThreadsHolder<>(10,
				() -> range.forEach(l -> selfHealingService.addItemToHeal(createItemToHeal(l, TABLE_A, COLUMN_A2))));
		final TestThreadsHolder<Runnable> holderB1 = new TestThreadsHolder<>(10,
				() -> range.forEach(l -> selfHealingService.addItemToHeal(createItemToHeal(l, TABLE_B, COLUMN_B1))));
		final TestThreadsHolder<Runnable> holderB2 = new TestThreadsHolder<>(10,
				() -> range.forEach(l -> selfHealingService.addItemToHeal(createItemToHeal(l, TABLE_B, COLUMN_B2))));

		holderA1.startAll();
		holderA2.startAll();
		holderB1.startAll();
		holderB2.startAll();
		holderA1.waitForAll(20, TimeUnit.SECONDS);
		holderA2.waitForAll(20, TimeUnit.SECONDS);
		holderB1.waitForAll(20, TimeUnit.SECONDS);
		holderB2.waitForAll(20, TimeUnit.SECONDS);

		selfHealingService.batchItems();

		//then
		final ArgumentCaptor<ChangeSet> changeSetCaptor = ArgumentCaptor.forClass(ChangeSet.class);
		verify(mock).persist(changeSetCaptor.capture());

		assertThat(changeSetCaptor.getValue().getEntityRecords()).hasSize(10000);
	}


	private List<Long> createRange()
	{
		final List<Long> range = LongStream.range(1, 1000).boxed().collect(toList());
		Collections.shuffle(range);
		return range;
	}


	private static ItemToHeal createItemToHeal(final Long l, final String type, final String attribute)
	{
		return new ItemToHeal(PK.fromLong(l.longValue()), type, attribute, l.longValue(), l);
	}

}
