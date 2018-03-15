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
package de.hybris.platform.servicelayer.internal.model.impl;

import static de.hybris.platform.servicelayer.internal.model.impl.RecordAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.directpersistence.record.DefaultRelationChanges;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.InsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.LocalizedRelationChanges;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.directpersistence.record.RemoveManyToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;
import de.hybris.platform.directpersistence.record.impl.DefaultInsertManyToManyRelationRecord;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.servicelayer.ExtendedServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import de.hybris.platform.servicelayer.internal.model.extractor.ChangeSetBuilder;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapperContext;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * Test class for {@link de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultChangeSetBuilder}
 */
@IntegrationTest
public class DefaultChangeSetBuilderTest extends ExtendedServicelayerTransactionalBaseTest
{
	private static final String CATEGORY_TO_KEYWORD_RELATION = "Category2KeywordRelation";
	private static final String USER_TO_ADDRESS = "User2Addresses";
	private static final String CUSTOMER_DESCRIPTION = "description";
	private static final String CUSTOMER_UID = "uid";
	private static final String CUSTOMER_TYPE = "Customer";
	private static final String ADDRESS_TYPE = "Address";
	private static final String ADDRESS_OWNER = "owner";
	private static final String PRINCIPAL_GROUP_RELATION = "PrincipalGroupRelation";
	private static final String ADDRESS3_STREETNAME = "3333";
	private static final String ADDRESS2_STREETNAME = "2222";
	private static final String ADDRESS1_STREETNAME = "1111";

	private static final String CUSTOMER_DESCRIPTION_TEXT = "Customer for cascader test";
	private static final String CUSTOMER_NAME = "Jan";
	private static final String UNIT_PERSISTENCE_TYPE = "Unit";
	private static final String UNIT1_TYPE = "Unit1Type";
	private static final Double UNIT1_CONVERSION = new Double(1);
	private static final String UNIT1_CODE = "Unit1";

	private static final String UNIT2_TYPE = "Unit2Type";
	private static final Double UNIT2_CONVERSION = new Double(2);
	private static final String UNIT2_CODE = "Unit2";

	private static final String UNIT3_TYPE = "Unit3Type";
	private static final Double UNIT3_CONVERSION = new Double(3);
	private static final String UNIT3_CODE = "Unit3";
	private static final String UNIT3_NEW_CODE = "New Unit3";

	private static final String NEW = "New ";
	private static final String UNIT3_ENGLISH_NAME = "The Unit 3";
	private static final String UNIT3_GERMAN_NAME = "Der Unit 3";

	@Resource
	private ChangeSetBuilder defaultChangeSetBuilder;

	@Resource
	private ModelService modelService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private Cascader cascader;

	private CustomerModel customer;
	private AddressModel ad1;
	private AddressModel ad2;
	private AddressModel ad3;

	private CategoryModel cat;
	private CategoryModel cat0;
	private CategoryModel cat2;
	private KeywordModel k1;
	private KeywordModel k2;
	private KeywordModel k3;

	private UnitModel unit1;
	private UnitModel unit2;
	private UnitModel unit3;
	private LanguageModel englishLang;

	@Before
	public void setUp() throws Exception
	{
		assertThat(modelService).isNotNull();
		assertThat(defaultChangeSetBuilder).isNotNull();

		createCoreData();
		createDefaultCatalog();

		englishLang = commonI18NService.getLanguage(Locale.ENGLISH.getLanguage());

		customer = modelService.create(CustomerModel.class);
		customer.setUid(CUSTOMER_NAME);
		customer.setDescription(CUSTOMER_DESCRIPTION_TEXT);

		ad1 = modelService.create(AddressModel.class);
		ad1.setOwner(customer);
		ad1.setStreetname(ADDRESS1_STREETNAME);

		ad2 = modelService.create(AddressModel.class);
		ad2.setOwner(customer);
		ad2.setStreetname(ADDRESS2_STREETNAME);

		ad3 = modelService.create(AddressModel.class);
		ad3.setOwner(customer);
		ad3.setStreetname(ADDRESS3_STREETNAME);

		unit1 = modelService.create(UnitModel.class);
		unit1.setCode(UNIT1_CODE);
		unit1.setConversion(UNIT1_CONVERSION);
		unit1.setUnitType(UNIT1_TYPE);

		unit2 = modelService.create(UnitModel.class);
		unit2.setCode(UNIT2_CODE);
		unit2.setConversion(UNIT2_CONVERSION);
		unit2.setUnitType(UNIT2_TYPE);

		unit3 = modelService.create(UnitModel.class);
		unit3.setCode(UNIT3_CODE);
		unit3.setConversion(UNIT3_CONVERSION);
		unit3.setUnitType(UNIT3_TYPE);
		unit3.setName(UNIT3_ENGLISH_NAME, Locale.ENGLISH);
		unit3.setName(UNIT3_GERMAN_NAME, Locale.GERMAN);
	}

	@Test
	public void testBuildForSingleUnit()
	{
		// given
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(InsertRecord.class);
		assertThat((InsertRecord) records.get(0)).isTypeOf(UNIT_PERSISTENCE_TYPE).hasChanges();
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("code", UNIT1_CODE);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("conversion", UNIT1_CONVERSION);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("unitType", UNIT1_TYPE);
	}

	@Test
	public void testBuildFor2Unit()
	{
		// given
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1, unit2),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(2);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(InsertRecord.class);
		assertThat(records.get(1)).isInstanceOf(InsertRecord.class);
		assertThat((InsertRecord) records.get(0)).isTypeOf(UNIT_PERSISTENCE_TYPE).hasChanges();
		assertThat((InsertRecord) records.get(1)).isTypeOf(UNIT_PERSISTENCE_TYPE).hasChanges();
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("code", UNIT1_CODE);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("conversion", UNIT1_CONVERSION);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("unitType", UNIT1_TYPE);
		assertThat((InsertRecord) records.get(1)).hasPropertyHolderWithNameAndValue("code", UNIT2_CODE);
		assertThat((InsertRecord) records.get(1)).hasPropertyHolderWithNameAndValue("conversion", UNIT2_CONVERSION);
		assertThat((InsertRecord) records.get(1)).hasPropertyHolderWithNameAndValue("unitType", UNIT2_TYPE);
	}

	@Test
	public void testBuildForSingleUnitWithLocale()
	{
		// given
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(InsertRecord.class);
		assertThat((InsertRecord) records.get(0)).isTypeOf(UNIT_PERSISTENCE_TYPE).hasChanges().hasLocalizedChanges();
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("code", UNIT3_CODE);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("conversion", UNIT3_CONVERSION);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("unitType", UNIT3_TYPE);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", UNIT3_ENGLISH_NAME, Locale.ENGLISH);
		assertThat((InsertRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", UNIT3_GERMAN_NAME, Locale.GERMAN);
	}

	@Test
	public void testUpdateSingleUnit()
	{
		// given
		modelService.save(unit3);
		unit3.setCode(UNIT3_NEW_CODE);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(UpdateRecord.class);
		assertThat((UpdateRecord) records.get(0)).hasChangesOfSize(1);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("code", UNIT3_NEW_CODE);
	}


	@Test
	public void testInsertUpdateUnitSort()
	{
		// given
		modelService.saveAll(unit1, unit2);
		unit1.setCode(UNIT3_NEW_CODE);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit3, unit1, unit2),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(3);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		final long pk1 = unit1.getPk().getLongValue();
		final long pk2 = unit2.getPk().getLongValue();
		assertThat(pk1).isLessThan(pk2);
		assertThat(records.get(0)).isInstanceOf(UpdateRecord.class);
		assertThat(records.get(1)).isInstanceOf(UpdateRecord.class);
		assertThat(records.get(0).getPK()).isEqualTo(unit1.getPk());
		assertThat(records.get(1).getPK()).isEqualTo(unit2.getPk());
		assertThat(records.get(2)).isInstanceOf(InsertRecord.class);
	}

	@Test
	public void testUpdateSingleUnitWithLocale()
	{
		// given
		modelService.save(unit3);
		unit3.setName(NEW + UNIT3_ENGLISH_NAME, Locale.ENGLISH);
		unit3.setName(NEW + UNIT3_GERMAN_NAME, Locale.GERMAN);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(UpdateRecord.class);
		assertThat((UpdateRecord) records.get(0)).hasLocalizedChangesOfSize(2);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", NEW + UNIT3_ENGLISH_NAME,
				Locale.ENGLISH);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", NEW + UNIT3_GERMAN_NAME, Locale.GERMAN);
	}

	@Test
	public void testUpdateSingleUnitNonLocaleAndLocale()
	{
		// given
		modelService.save(unit3);
		unit3.setCode(UNIT3_NEW_CODE);
		unit3.setName(NEW + UNIT3_ENGLISH_NAME, Locale.ENGLISH);
		unit3.setName(NEW + UNIT3_GERMAN_NAME, Locale.GERMAN);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(UpdateRecord.class);
		assertThat((UpdateRecord) records.get(0)).hasChangesOfSize(1);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("code", UNIT3_NEW_CODE);
		assertThat((UpdateRecord) records.get(0)).hasLocalizedChangesOfSize(2);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", NEW + UNIT3_ENGLISH_NAME,
				Locale.ENGLISH);
		assertThat((UpdateRecord) records.get(0)).hasPropertyHolderWithNameAndValue("name", NEW + UNIT3_GERMAN_NAME, Locale.GERMAN);
	}

	@Test
	public void testDeleteSingleUnit()
	{
		// given
		modelService.save(unit1);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1),
				PersistenceOperation.DELETE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(DeleteRecord.class);
		assertThat(records.get(0).getType()).isEqualTo(UNIT_PERSISTENCE_TYPE);
		assertThat(records.get(0).getPK()).isEqualTo(unit1.getPk());
	}

	@Test
	public void testDelete3Unit()
	{
		// given
		modelService.saveAll(unit1, unit2, unit3);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1, unit2, unit3),
				PersistenceOperation.DELETE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(3);
		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(DeleteRecord.class);
		assertThat(records.get(0).getType()).isEqualTo(UNIT_PERSISTENCE_TYPE);
		assertThat(records.get(0).getPK()).isEqualTo(unit1.getPk());
		assertThat(records.get(1)).isInstanceOf(DeleteRecord.class);
		assertThat(records.get(1).getType()).isEqualTo(UNIT_PERSISTENCE_TYPE);
		assertThat(records.get(1).getPK()).isEqualTo(unit2.getPk());
		assertThat(records.get(2)).isInstanceOf(DeleteRecord.class);
		assertThat(records.get(2).getType()).isEqualTo(UNIT_PERSISTENCE_TYPE);
		assertThat(records.get(2).getPK()).isEqualTo(unit3.getPk());
	}

	@Test
	public void testBuildForCustomerWithAddress()
	{
		// given
		customer.setAddresses(Arrays.asList(ad1));
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(customer, ad1),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(2);
		final Map<String, RelationChanges> relations = changeSet.getRelationChanges();
		assertThat(relations).hasSize(2);

		final List<EntityRecord> records = Lists.newArrayList(changeSet.getEntityRecords());
		assertThat(records.get(0)).isInstanceOf(InsertRecord.class);
		assertThat(records.get(1)).isInstanceOf(InsertRecord.class);

		final InsertRecord customerRecord;
		final InsertRecord addressRecord;

		if (records.get(0).getType().equals(ADDRESS_TYPE))
		{
			addressRecord = (InsertRecord) records.get(0);
			customerRecord = (InsertRecord) records.get(1);
		}
		else
		{
			addressRecord = (InsertRecord) records.get(1);
			customerRecord = (InsertRecord) records.get(0);
		}

		assertThat(customerRecord).hasPropertyHolderWithNameAndValue(CUSTOMER_UID, CUSTOMER_NAME);
		assertThat(customerRecord).hasPropertyHolderWithNameAndValue(CUSTOMER_DESCRIPTION, CUSTOMER_DESCRIPTION_TEXT);
		final PK customerPk = customerRecord.getPK();
		assertThat(addressRecord).isNotNull().isTypeOf(ADDRESS_TYPE);


		checkAddress(addressRecord, ADDRESS1_STREETNAME, customerPk);
		assertThat(addressRecord).hasPropertyHolderWithNameAndValue(ADDRESS_OWNER, new ItemPropertyValue(customerPk));
		assertThat(relations.keySet()).contains(PRINCIPAL_GROUP_RELATION);
		final DefaultRelationChanges relationChangeSet = (DefaultRelationChanges) relations.get(PRINCIPAL_GROUP_RELATION);
		assertThat(relationChangeSet.getInsertManyToManyRelationRecords()).hasSize(1);
		assertThat(relationChangeSet.getRemoveManyToManyRelationsRecords()).hasSize(0);
		assertThat(relationChangeSet.getInsertManyToManyRelationRecords().iterator().next())
				.isInstanceOf(InsertManyToManyRelationRecord.class);
		final InsertManyToManyRelationRecord record = relationChangeSet.getInsertManyToManyRelationRecords().iterator().next();
		assertThat(record.getSourcePk()).isEqualTo(customerPk);
	}

	private void checkAddress(final InsertRecord insertRecord, final String streetname1, final PK owner)
	{
		assertThat(insertRecord).isNotNull();
		assertThat(insertRecord.getType().equals(ADDRESS_TYPE));
		//		assertThat(insertRecord.getChanges().get(ADDRESS_STREETNAME)).isEqualTo(streetname1);
		//		assertThat(insertRecord.getChanges().get(ADDRESS_OWNER)).isInstanceOf(ItemPropertyValue.class);
		//		assertThat(((ItemPropertyValue) insertRecord.getChanges().get(ADDRESS_OWNER)).getPK()).isEqualTo(owner);
	}

	@Test
	public void testBuildForCustomerWithThreeAddresses()
	{
		// given
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(customer, ad1, ad2, ad3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(4);
		final Optional<EntityRecord> customerRecord = getFirstRecorOfType(changeSet.getEntityRecords(), CUSTOMER_TYPE);
		assertThat(customerRecord.isPresent()).isTrue();
		assertThat((InsertRecord) customerRecord.get()).hasPropertyHolderWithNameAndValue(CUSTOMER_UID, CUSTOMER_NAME);
		assertThat((InsertRecord) customerRecord.get()).hasPropertyHolderWithNameAndValue(CUSTOMER_DESCRIPTION,
				CUSTOMER_DESCRIPTION_TEXT);
		final Iterable<EntityRecord> addressRecords = getRecordsOfType(changeSet.getEntityRecords(), ADDRESS_TYPE);

		for (final EntityRecord record : addressRecords)
		{
			assertThat((InsertRecord) record).hasPropertyHolderWithNameAndValue(ADDRESS_OWNER,
					new ItemPropertyValue(customerRecord.get().getPK()));
		}
	}

	private Optional<EntityRecord> getFirstRecordForPK(final Iterable<EntityRecord> records, final PK pk)
	{
		return Iterables.tryFind(records, new Predicate<EntityRecord>()
		{

			@Override
			public boolean apply(final EntityRecord input)
			{
				return pk.equals(input.getPK());
			}
		});
	}

	private Optional<EntityRecord> getFirstRecorOfType(final Iterable<EntityRecord> records, final String type)
	{
		return Iterables.tryFind(records, new Predicate<EntityRecord>()
		{

			@Override
			public boolean apply(final EntityRecord input)
			{
				return input.getType().equals(type);
			}
		});
	}

	private Iterable<EntityRecord> getRecordsOfType(final Iterable<EntityRecord> records, final String type)
	{
		return Iterables.filter(records, new Predicate<EntityRecord>()
		{

			@Override
			public boolean apply(final EntityRecord input)
			{
				return input.getType().equals(type);
			}
		});
	}

	@Test
	public void testBuildForCustomerRemoveAddresses()
	{
		// given
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		modelService.saveAll(customer, ad1, ad2, ad3);
		customer.setAddresses(Collections.EMPTY_LIST);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(customer),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);

		final Map<String, RelationChanges> relations = changeSet.getRelationChanges();
		assertThat(relations).hasSize(1);
		final DefaultRelationChanges relationChangeSet = (DefaultRelationChanges) relations.get(USER_TO_ADDRESS);
		assertThat(relationChangeSet.getInsertManyToManyRelationRecords()).hasSize(0);
		assertThat(relationChangeSet.getRemoveOneToManyRelationsRecords()).hasSize(1);
		assertThat(relationChangeSet.getRemoveOneToManyRelationsRecords().iterator().next()).isInstanceOf(
				RemoveOneToManyRelationsRecord.class);
		final RemoveOneToManyRelationsRecord record = relationChangeSet.getRemoveOneToManyRelationsRecords().iterator().next();
		assertThat(record.getSourcePk()).isEqualTo(customer.getPk());

		final Collection<EntityRecord> records = changeSet.getEntityRecords();
		final EntityRecord customerRecord = getFirstRecordForPK(records, customer.getPk()).get();

		assertThat(customerRecord).isInstanceOf(UpdateRecord.class);

		final Set<PropertyHolder> changes = ((UpdateRecord) customerRecord).getChanges();
		assertThat(changes).hasSize(0);
	}

	@Test
	public void createCategoryWithKeywords()
	{
		// given
		cat = prepareCategoryModel("cat");
		cat0 = prepareCategoryModel("cat0");
		cat2 = prepareCategoryModel("cat2");

		k1 = prepareKeywordModel("k1");
		k2 = prepareKeywordModel("k2");
		k3 = prepareKeywordModel("k3");

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.ENGLISH);

		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(cat, cat0, cat2, k1, k2, k3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(6);
		final Map<String, RelationChanges> relations = changeSet.getRelationChanges();
		assertThat(relations).hasSize(1);
		assertThat(relations.containsKey(CATEGORY_TO_KEYWORD_RELATION)).isTrue();
		assertThat(relations.get(CATEGORY_TO_KEYWORD_RELATION)).isInstanceOf(LocalizedRelationChanges.class);
		final LocalizedRelationChanges locRelationChanges = (LocalizedRelationChanges) relations.get(CATEGORY_TO_KEYWORD_RELATION);
		assertThat(locRelationChanges.getRelationChangeForLanguage(Locale.ENGLISH)).isNotNull();
		final Collection<InsertManyToManyRelationRecord> insertRecords = locRelationChanges
				.getRelationChangeForLanguage(Locale.ENGLISH).getInsertManyToManyRelationRecords();
		assertThat(insertRecords).hasSize(5);

		checkInsertRecords(wrappers, insertRecords);
	}

	@Test
	public void createCategoryWithKeywords2Langs()
	{
		// given
		cat = prepareCategoryModel("cat");
		cat0 = prepareCategoryModel("cat0");
		cat2 = prepareCategoryModel("cat2");

		k1 = prepareKeywordModel("k1");
		k2 = prepareKeywordModel("k2");
		k3 = prepareKeywordModel("k3");

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.ENGLISH);

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.GERMAN);
		k2.setCategories(Arrays.asList(cat0, cat, cat2), Locale.GERMAN);

		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(cat, cat0, cat2, k1, k2, k3),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(6);

		final Map<String, RelationChanges> relations = changeSet.getRelationChanges();
		assertThat(relations).hasSize(1);
		assertThat(relations.get(CATEGORY_TO_KEYWORD_RELATION)).isInstanceOf(LocalizedRelationChanges.class);

		final LocalizedRelationChanges locChagnes = (LocalizedRelationChanges) relations.get(CATEGORY_TO_KEYWORD_RELATION);
		assertThat(locChagnes.getRelationChanges()).hasSize(2);
		assertThat(locChagnes.getRelationChanges().keySet()).contains(Locale.ENGLISH);
		assertThat(locChagnes.getRelationChanges().keySet()).contains(Locale.GERMAN);

		final Collection<InsertManyToManyRelationRecord> insertRecordsEn = locChagnes.getRelationChangeForLanguage(Locale.ENGLISH)
				.getInsertManyToManyRelationRecords();
		final Collection<InsertManyToManyRelationRecord> insertRecordsDe = locChagnes.getRelationChangeForLanguage(Locale.GERMAN)
				.getInsertManyToManyRelationRecords();
		checkInsertRecords(wrappers, insertRecordsEn);
		checkInsertRecords(wrappers, insertRecordsDe);

		// TODO Finish implementation of that test
	}

	private void checkInsertRecords(final Collection<ModelWrapper> wrappers,
			final Collection<InsertManyToManyRelationRecord> insertRecords)
	{
		final Map<Object, PK> object2Pk = new HashMap<Object, PK>();
		for (final ModelWrapper wrapper : wrappers)
		{
			object2Pk.put(wrapper.getModel(), wrapper.getResolvedPk());
		}

		final InsertManyToManyRelationRecord catK1 = new DefaultInsertManyToManyRelationRecord(object2Pk.get(cat),
				object2Pk.get(k1), true);
		catK1.setSourceToTargetPosition(Integer.valueOf(0));
		final InsertManyToManyRelationRecord catK2 = new DefaultInsertManyToManyRelationRecord(object2Pk.get(cat),
				object2Pk.get(k2), true);
		catK2.setSourceToTargetPosition(Integer.valueOf(1));
		catK2.setTargetToSourcePosition(Integer.valueOf(1));
		final InsertManyToManyRelationRecord catK3 = new DefaultInsertManyToManyRelationRecord(object2Pk.get(cat),
				object2Pk.get(k3), true);
		catK3.setSourceToTargetPosition(Integer.valueOf(2));
		final InsertManyToManyRelationRecord k2Cat0 = new DefaultInsertManyToManyRelationRecord(object2Pk.get(cat0),
				object2Pk.get(k2), false);
		k2Cat0.setTargetToSourcePosition(Integer.valueOf(0));
		final InsertManyToManyRelationRecord k2Cat2 = new DefaultInsertManyToManyRelationRecord(object2Pk.get(cat2),
				object2Pk.get(k2), false);
		k2Cat2.setTargetToSourcePosition(Integer.valueOf(2));

		assertThat(insertRecords).containsOnly(catK1, catK2, catK3, k2Cat0, k2Cat2);
	}

	@Test
	public void removeKeywordsFromCategory()
	{
		// given
		cat = prepareCategoryModel("cat");

		k1 = prepareKeywordModel("k1");
		k2 = prepareKeywordModel("k2");
		k3 = prepareKeywordModel("k3");

		cat.setKeywords(Arrays.asList(k1, k2, k3), Locale.ENGLISH);
		modelService.saveAll(cat, k1, k2, k3);

		cat.setKeywords(Collections.<KeywordModel> emptyList(), Locale.ENGLISH);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(cat),
				PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);

		final Map<String, RelationChanges> relations = changeSet.getRelationChanges();
		assertThat(relations).hasSize(1);

		assertThat(relations.get(CATEGORY_TO_KEYWORD_RELATION)).isNotNull();
		assertThat(relations.get(CATEGORY_TO_KEYWORD_RELATION)).isInstanceOf(LocalizedRelationChanges.class);

		final LocalizedRelationChanges locRelChangeSet = (LocalizedRelationChanges) relations.get(CATEGORY_TO_KEYWORD_RELATION);
		assertThat(locRelChangeSet.getRelationChanges()).hasSize(1);

		final DefaultRelationChanges relationChanges = locRelChangeSet.getRelationChangeForLanguage(Locale.ENGLISH);
		assertThat(relationChanges.getInsertManyToManyRelationRecords()).hasSize(0);
		assertThat(relationChanges.getRemoveManyToManyRelationsRecords()).hasSize(1);
		assertThat(relationChanges.getRemoveManyToManyRelationsRecords().iterator().next())
				.isInstanceOf(RemoveManyToManyRelationsRecord.class);

		final RemoveManyToManyRelationsRecord removeRelationsRecord = relationChanges.getRemoveManyToManyRelationsRecords()
				.iterator().next();
		assertThat(removeRelationsRecord.getPk()).isEqualTo(cat.getPk());
	}

	@Test
	public void removeCategoryFromKeyword()
	{
		// given
		cat = prepareCategoryModel("cat");
		k1 = prepareKeywordModel("k1");

		cat.setKeywords(Arrays.asList(k1), Locale.ENGLISH);
		modelService.saveAll();

		k1.setCategories(Collections.<CategoryModel> emptyList(), Locale.ENGLISH);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(k1), PersistenceOperation.SAVE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForModification(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		assertThat(changeSet.getRelationChanges()).hasSize(1);
		assertThat(changeSet.getRelationChanges().get(CATEGORY_TO_KEYWORD_RELATION)).isNotNull();
		assertThat(changeSet.getRelationChanges().get(CATEGORY_TO_KEYWORD_RELATION)).isInstanceOf(LocalizedRelationChanges.class);

		final LocalizedRelationChanges locRelationChanges = (LocalizedRelationChanges) changeSet.getRelationChanges()
				.get(CATEGORY_TO_KEYWORD_RELATION);
		assertThat(locRelationChanges.getRelationChangeForLanguage(Locale.ENGLISH)).isNotNull();
		final DefaultRelationChanges relChanges = locRelationChanges.getRelationChangeForLanguage(Locale.ENGLISH);

		assertThat(relChanges.getInsertManyToManyRelationRecords()).hasSize(0);
		assertThat(relChanges.getRemoveManyToManyRelationsRecords()).hasSize(1);

		final RemoveManyToManyRelationsRecord record = relChanges.getRemoveManyToManyRelationsRecords().iterator().next();
		assertThat(record.getPk()).isEqualTo(k1.getPk());
	}

	@Test
	public void deleteSingleUnit()
	{
		// given
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1),
				PersistenceOperation.DELETE);
		modelService.save(unit1);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(1);
		assertThat(changeSet.getEntityRecords().iterator().next()).isInstanceOf(DeleteRecord.class);
		final DeleteRecord deleteRecord = (DeleteRecord) changeSet.getEntityRecords().iterator().next();
		assertThat(deleteRecord.getPK()).isEqualTo(unit1.getPk());
		assertThat(deleteRecord.getRelationNames()).hasSize(1);
	}

	@Test
	public void deleteThreeUnit()
	{
		// given
		final Collection<AbstractItemModel> models = Arrays.<AbstractItemModel> asList(unit1, unit2, unit3);
		modelService.saveAll(models);
		final Collection<ModelWrapper> wrappers = prepareWrappers(models, PersistenceOperation.DELETE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet).isNotNull();
		assertThat(changeSet.getEntityRecords()).hasSize(3);

		final Set<PK> pksFromChangeset = new HashSet<PK>();
		final Set<PK> pksFromSave = new HashSet<PK>();
		for (final EntityRecord record : changeSet.getEntityRecords())
		{
			assertThat(record).isInstanceOf(DeleteRecord.class);
			pksFromChangeset.add(record.getPK());
		}
		pksFromSave.add(unit1.getPk());
		pksFromSave.add(unit2.getPk());
		pksFromSave.add(unit3.getPk());

		assertThat(pksFromChangeset).isEqualTo(pksFromSave);
	}

	@Test
	public void testDeleteCustomerAndAddresses()
	{
		// given
		final Collection<AbstractItemModel> models = Arrays.<AbstractItemModel> asList(customer, ad1, ad2, ad3);
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		modelService.saveAll(models);

		final Collection<ModelWrapper> wrappers = prepareWrappers(models, PersistenceOperation.DELETE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet.getEntityRecords()).hasSize(4);
	}

	@Test
	public void testDeleteCustomerWithoutAssignedAddresses()
	{
		// given
		final Collection<AbstractItemModel> models = Arrays.<AbstractItemModel> asList(customer, ad1, ad2, ad3);
		customer.setAddresses(Arrays.asList(ad1, ad2, ad3));
		modelService.saveAll(models);
		final Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(customer),
				PersistenceOperation.DELETE);

		// when
		final ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		// then
		assertThat(changeSet.getEntityRecords()).hasSize(1);
	}


	@Test
	public void testPersistenceVersion()
	{
		modelService.save(unit1);
		Collection<ModelWrapper> wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1), PersistenceOperation.DELETE);

		ChangeSet changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);

		assertThat(Long.valueOf(changeSet.getEntityRecords().iterator().next().getVersion())).isEqualTo(Long.valueOf(0));

		unit1.setCode("CodeVersion1");
		modelService.save(unit1);
		wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1), PersistenceOperation.DELETE);
		changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);
		assertThat(Long.valueOf(changeSet.getEntityRecords().iterator().next().getVersion())).isEqualTo(Long.valueOf(1));

		unit1.setCode("CodeVersion2");
		modelService.save(unit1);
		wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1), PersistenceOperation.DELETE);
		changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);
		assertThat(Long.valueOf(changeSet.getEntityRecords().iterator().next().getVersion())).isEqualTo(Long.valueOf(2));

		unit1.setCode("CodeVersion3");
		modelService.save(unit1);
		wrappers = prepareWrappers(Arrays.<AbstractItemModel> asList(unit1), PersistenceOperation.DELETE);
		changeSet = defaultChangeSetBuilder.buildForDelete(wrappers);
		assertThat(Long.valueOf(changeSet.getEntityRecords().iterator().next().getVersion())).isEqualTo(Long.valueOf(3));
	}

	private CategoryModel prepareCategoryModel(final String code)
	{
		final CategoryModel cat = modelService.create(CategoryModel.class);
		cat.setCode(code);

		return cat;
	}

	private KeywordModel prepareKeywordModel(final String keyword)
	{
		final KeywordModel keywordModel = modelService.create(KeywordModel.class);
		keywordModel.setKeyword(keyword);
		keywordModel.setLanguage(englishLang);

		return keywordModel;
	}

	private Collection<ModelWrapper> prepareWrappers(final Collection<? extends AbstractItemModel> models,
			final PersistenceOperation operation)
	{
		final Collection<ModelWrapper> wrappers = new ArrayList<>(models.size());
		for (final Object model : models)
		{
			final DefaultModelService defModelService = (DefaultModelService) modelService;
			wrappers.add(new ModelWrapper(model, //
					operation, //
					new ModelWrapperContext(defModelService.getConverterRegistry(), //
							defModelService.getInterceptorRegistry(), //
							cascader)));
		}
		return wrappers;
	}
}
