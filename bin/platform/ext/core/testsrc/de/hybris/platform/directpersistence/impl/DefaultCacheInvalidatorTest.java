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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.CrudEnum;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.persistence.AbstractEntityState;
import de.hybris.platform.persistence.GenericBMPBean.GenericItemEntityStateCacheUnit;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;

import java.util.Collection;

import javax.annotation.Resource;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Sets;


/**
 * Test class for {@link de.hybris.platform.directpersistence.impl.DefaultCacheInvalidator}
 */

// TP - ignored test because it is no longer relevant
public class DefaultCacheInvalidatorTest extends ServicelayerBaseTest
{
    private static final Logger LOG = Logger.getLogger(DefaultCacheInvalidatorTest.class);

	@Resource
	private CacheInvalidator cacheInvalidator;
	@Resource
	private DefaultWritePersistenceGateway writePersistenceGateway;
	@Resource
	private ModelService modelService;
    @Resource
	private TransactionTemplate transactionTemplate;

	private InsertRecord insertRecord1;
	private UpdateRecord updateRecord1;
	private DeleteRecord deleteRecord1;


	private MutableChangeSet changeSet;

	/**
	 * default setup, prepares few records for delete/insert/update operation and models to be updated/deleted
	 */
	@Before
	public void setUp() throws Exception
	{
		final PK pk = generatePkForCode("Unit");

		insertRecord1 = new InsertRecord(pk, "Unit", Sets.newHashSet(new PropertyHolder("Code", "insert1")));

		final UnitModel unit1 = modelService.create(UnitModel.class);
		unit1.setCode("test123");
		unit1.setUnitType("test");
		final UnitModel unit2 = modelService.create(UnitModel.class);
		unit2.setCode("test456");
		unit2.setUnitType("test");
		modelService.saveAll(unit1, unit2);

		updateRecord1 = new UpdateRecord(unit1.getPk(), "unit", unit1.getItemModelContext().getPersistenceVersion(),
				Sets.newHashSet(new PropertyHolder("Code", "update1")));

		deleteRecord1 = new DeleteRecord(unit2.getPk(), "unit", unit2.getItemModelContext().getPersistenceVersion());

		changeSet = new DefaultChangeSet();

        Registry.getCurrentTenantNoFallback().getCache().clear();
        LOG.info("Cleared cache for >> "+Registry.getCurrentTenantNoFallback());
	}

	@After
	public void clear() throws Exception
	{
		UnitModel unit = (UnitModel) getSavedModel(insertRecord1.getPK());
		if (unit != null)
		{
			modelService.remove(unit);
		}
		unit = (UnitModel) getSavedModel(updateRecord1.getPK());
		if (unit != null)
		{
			modelService.remove(unit);
		}
		unit = (UnitModel) getSavedModel(deleteRecord1.getPK());
		if (unit != null)
		{
			modelService.remove(unit);
		}
	}



	/**
	 * Test method for
	 * {@link de.hybris.platform.directpersistence.impl.DefaultCacheInvalidator#invalidate(java.util.Collection)}.
	 */
//	@Test
	public void testInvalidateForSimpleCreateOperation()
	{
		//given		
		changeSet.add(insertRecord1);

		final Collection<PersistResult> givenResults = writePersistenceGateway.persist(changeSet);
		assertThat(givenResults).hasSize(1);
		final PersistResult resultToInvalidate = givenResults.iterator().next();
		assertEquals(CrudEnum.CREATE, resultToInvalidate.getOperation());
		final PK pk = resultToInvalidate.getPk();
		assertEquals(insertRecord1.getPK(), pk);

		//unit has been persisted but is not in the cache yet
		GenericItemEntityStateCacheUnit cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
		assertFalse(objectExistsInCache(cacheUnit));

		//get model, after read - the unit should be in cache		
		final UnitModel savedModel = (UnitModel) getSavedModel(pk);
		assertNotNull(savedModel);
		cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
		assertTrue(objectExistsInCache(cacheUnit));
		checkCachedContent(cacheUnit, pk);

		//invalidate
		cacheInvalidator.invalidate(givenResults); //invalidation - object is removed from cache
		cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
		assertFalse(objectExistsInCache(cacheUnit));
	}

//	@Test
	public void testInvalidateForSimpleCreateOperationInTx()
	{
		final PK returnedPk = transactionTemplate.execute(new TransactionCallback<PK>()
		{
			@Override
			public PK doInTransaction(final TransactionStatus status)
			{
				changeSet.add(insertRecord1);

				final Collection<PersistResult> givenResults = writePersistenceGateway.persist(changeSet);
				assertThat(givenResults).hasSize(1);
				final PersistResult resultToInvalidate = givenResults.iterator().next();
				assertEquals(CrudEnum.CREATE, resultToInvalidate.getOperation());
				final PK pk = resultToInvalidate.getPk();
				assertEquals(insertRecord1.getPK(), pk);

				//unit has been persisted but is not in the cache yet
				GenericItemEntityStateCacheUnit cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
				assertThat(objectExistsInCache(cacheUnit)).isFalse();

				//get model, after read - the unit should be in cache		
				final UnitModel savedModel = (UnitModel) getSavedModel(pk);
				assertNotNull(savedModel);
				cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
				assertThat(objectExistsInCache(cacheUnit)).isTrue();
				checkCachedContent(cacheUnit, pk);

				//invalidate
				cacheInvalidator.invalidate(givenResults); //invalidation - object is removed from cache
				final GenericItemEntityStateCacheUnit unit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(pk);
				checkCachedContent(unit, pk);
				return pk;
			}
		});
		final GenericItemEntityStateCacheUnit unitToCheck = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(returnedPk);
		assertThat(objectExistsInCache(unitToCheck)).isFalse();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.directpersistence.impl.DefaultCacheInvalidator#invalidate(java.util.Collection)}.
	 */
//	@Test
	public void testInvalidateForSimpleUpdateOperationTransactional()
	{
		//given		
		changeSet.add(updateRecord1);
		UnitModel savedModel = (UnitModel) getSavedModel(updateRecord1.getPK());
		assertNotNull(savedModel);
		assertEquals("test123", savedModel.getCode());
		GenericItemEntityStateCacheUnit cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(updateRecord1.getPK());
		assertTrue(objectExistsInCache(cacheUnit)); //item exists in cache 

		boolean done = false;
		try
		{
			Transaction.current().begin();
			final Collection<PersistResult> givenResults = writePersistenceGateway.persist(changeSet);
			//invalidate
			cacheInvalidator.invalidate(givenResults); //invalidation inside transaction
			cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(updateRecord1.getPK());
			assertTrue(objectExistsInCache(cacheUnit)); //item still exists in the cache - transaction has not been commited yet
			Transaction.current().commit();
			done = true;
			assertThat(givenResults).hasSize(1);

			cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(updateRecord1.getPK());
			assertFalse(objectExistsInCache(cacheUnit)); //item is not in the cache - it has been invalidated

			savedModel = (UnitModel) getSavedModel(updateRecord1.getPK());
			assertNotNull(savedModel);
			cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(updateRecord1.getPK());
			assertNotNull(cacheUnit); //model was read - now it's in cache again
			checkCachedContent(cacheUnit, updateRecord1.getPK());

			assertEquals("update1", savedModel.getCode());
		}
		finally
		{
			if (!done)
			{
				Transaction.current().rollback();
			}
		}

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.directpersistence.impl.DefaultCacheInvalidator#invalidate(java.util.Collection)}.
	 */
//	@Test
	public void testInvalidateForMixedOperations()
	{
		//given		
		changeSet.add(insertRecord1, updateRecord1, deleteRecord1);

		final Collection<PersistResult> givenResults = writePersistenceGateway.persist(changeSet);
		assertThat(givenResults).hasSize(3);

		//unit has been persisted but is not in the cache yet
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecord1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(updateRecord1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(deleteRecord1.getPK())));

		//get the models - after read, the unit should be in cache
		UnitModel savedModel = (UnitModel) getSavedModel(insertRecord1.getPK());
		assertNotNull(savedModel);
		GenericItemEntityStateCacheUnit cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(insertRecord1.getPK());
		assertTrue(objectExistsInCache(cacheUnit));
		checkCachedContent(cacheUnit, insertRecord1.getPK());

		savedModel = (UnitModel) getSavedModel(updateRecord1.getPK());
		assertNotNull(savedModel);
		cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(updateRecord1.getPK());
		assertTrue(objectExistsInCache(cacheUnit));
		checkCachedContent(cacheUnit, updateRecord1.getPK());
		assertEquals("update1", savedModel.getCode());

		//the deleted unit cannot be fetched - so it will not be placed in the cache
		savedModel = (UnitModel) getSavedModel(deleteRecord1.getPK());
		assertNull(savedModel);
		cacheUnit = (GenericItemEntityStateCacheUnit) getCacheUnitForPK(deleteRecord1.getPK());
		assertFalse(objectExistsInCache(cacheUnit));

		//invalidate
		cacheInvalidator.invalidate(givenResults); //invalidation - objects are removed from cache (if present)
		assertFalse(objectExistsInCache(getCacheUnitForPK(insertRecord1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(updateRecord1.getPK())));
		assertFalse(objectExistsInCache(getCacheUnitForPK(deleteRecord1.getPK())));

	}


	private void checkCachedContent(final GenericItemEntityStateCacheUnit cacheUnit, final PK pk)
	{
		final Object[] expectedCachedKey = new Object[]
		{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, pk.getTypeCodeAsString(), pk };
		final Object[] cachedKeys = cacheUnit.getKeyAsArray();
		assertThat(cachedKeys).containsOnly(expectedCachedKey);
		try
		{
			assertThat(((AbstractEntityState) cacheUnit.get()).getPK()).isEqualTo(pk);
		}
		catch (final Exception e)
		{
			fail("Cache content doesn't match for pk=" + pk + " and cacheUnit=" + cacheUnit);
		}

	}

	private AbstractCacheUnit getCacheUnitForPK(final PK pk)
	{
		return Registry.getCurrentTenant().getCache()
				.getAbstractCacheUnit(Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, pk.getTypeCodeAsString(), pk);
	}

    private boolean objectExistsInCache(final AbstractCacheUnit cacheUnit)
    {
        try
        {
            if (cacheUnit != null)
            {
                LOG.info("Checking cache unit is in cache  :" + cacheUnit);
                return cacheUnit.get() != null;
            }
            else
            {
                return false;
            }
        }
        catch (final Exception e)
        {
            LOG.info("Cache unit can not be calculated :" + e.getMessage());
            return false;
        }
    }

	private ItemModel getSavedModel(final PK pk)
	{
		ItemModel savedModel = null;
		try
		{
			savedModel = modelService.get(pk);
			modelService.refresh(savedModel);
		}
		catch (final ModelLoadingException e)
		{
			//item not found - null be returned, instead of throwing exception
		}
		return savedModel;
	}

	private PK generatePkForCode(final String typeCode)
	{
		final TypeInfoMap persistenceInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
		return PK.createCounterPK(persistenceInfo.getItemTypeCode());
	}

}
