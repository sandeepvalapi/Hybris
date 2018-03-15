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
package de.hybris.platform.catalog;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * created for HOR-262
 */
@IntegrationTest
public class ItemSyncTimeStampTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	private static Item sourceItem = null;
	private static Item targetItem = null;
	private static CatalogVersion srcVersion = null;
	private static CatalogVersion tgtVersion = null;
	private static SyncItemJob syncJob = null;

	@Before
	public void prepare() throws ConsistencyCheckException
	{
		sourceItem = UserManager.getInstance().createTitle("fooItem_a");
		targetItem = UserManager.getInstance().createTitle("barItem_a");

		final Catalog catalog = CatalogManager.getInstance().createCatalog("fooCatalog_a");
		srcVersion = CatalogManager.getInstance().createCatalogVersion(catalog, "barVersion_b", null);
		srcVersion.setLanguages(Collections.singletonList(getOrCreateLanguage("en")));

		tgtVersion = CatalogManager.getInstance().createCatalogVersion(catalog, "barTargetVersion_b", null);
		tgtVersion.setLanguages(Collections.singletonList(getOrCreateLanguage("en")));

		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "fooJob_a");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, srcVersion);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgtVersion);
		syncJob = CatalogManager.getInstance().createCatalogVersionSyncJob(args);
	}

	//test no source item
	@Test(expected = de.hybris.platform.servicelayer.exceptions.ModelSavingException.class)
	public void testSimpleCreateModelNoSourceItem() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{

			final ItemSyncTimestampModel modelItem = modelService.create(ItemSyncTimestampModel.class);
			modelService.save(modelItem);

		}
		finally
		{
			//
		}
	}

	//test no source or target version 
	@Test(expected = de.hybris.platform.servicelayer.exceptions.ModelSavingException.class)
	public void testSimpleCreateModelNoTgtVersion() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{

			final ItemSyncTimestampModel modelItem = modelService.create(ItemSyncTimestampModel.class);
			modelItem.setSourceVersion((CatalogVersionModel) modelService.get(srcVersion));
			//modelItem.setSourceVersion((CatalogVersionModel) modelService.get(tgtVersion));
			modelService.save(modelItem);

		}
		finally
		{
			//
		}
	}

	//test no source or target version 
	@Test(expected = de.hybris.platform.servicelayer.exceptions.ModelSavingException.class)
	public void testSimpleCreateModelNoSrcVersion() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{

			final ItemSyncTimestampModel modelItem = modelService.create(ItemSyncTimestampModel.class);
			//modelItem.setSourceVersion((CatalogVersionModel) modelService.get(srcVersion));
			modelItem.setSourceVersion((CatalogVersionModel) modelService.get(tgtVersion));
			modelService.save(modelItem);

		}
		finally
		{
			//
		}
	}

	//	without target item we won't save anything
	public void testSimpleCreateModelNoSyncJob1() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{


			final ItemSyncTimestampModel modelItem = modelService.create("ItemSyncTimestamp"/* ItemSyncTimestampModel.class */);

			modelItem.setSourceItem((ItemModel) modelService.get(sourceItem));
			modelItem.setSourceVersion((CatalogVersionModel) modelService.get(srcVersion));
			modelItem.setTargetVersion((CatalogVersionModel) modelService.get(tgtVersion));
			modelItem.setTargetItem((ItemModel) modelService.get(targetItem));

			modelService.save(modelItem);
			Assert.assertNull(modelItem.getSyncJob());

			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getTargetVersion().equals(modelItem.getTargetVersion()));
			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getSourceVersion().equals(modelItem.getSourceVersion()));
		}
		finally
		{
			//
		}
	}

	//target/source version get assigned from synjob
	@Test(expected = de.hybris.platform.servicelayer.exceptions.ModelSavingException.class)
	public void testSimpleCreateModelSyncJobMissingTarget() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{

			final ItemSyncTimestampModel modelItem = modelService.create(ItemSyncTimestampModel.class);

			modelItem.setSourceItem((ItemModel) modelService.get(sourceItem));
			modelItem.setSyncJob((SyncItemJobModel) modelService.get(syncJob));
			//modelItem.setTargetItem((ItemModel) modelService.get(targetItem));
			modelService.save(modelItem);

			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getTargetVersion().equals(modelItem.getTargetVersion()));
			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getSourceVersion().equals(modelItem.getSourceVersion()));

		}
		finally
		{
			//
		}
	}

	//target/source version get assigned from synjob
	@Test
	public void testSimpleCreateModelSyncJob() throws JaloGenericCreationException, JaloAbstractTypeException,
			ConsistencyCheckException
	{
		try
		{


			//final Language lan = C2LManager.getInstance().getLanguageByIsoCode("en");
			final ItemSyncTimestampModel modelItem = modelService.create(ItemSyncTimestampModel.class);

			modelItem.setSourceItem((ItemModel) modelService.get(sourceItem));
			modelItem.setSyncJob((SyncItemJobModel) modelService.get(syncJob));
			modelItem.setTargetItem((ItemModel) modelService.get(targetItem));
			modelService.save(modelItem);

			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getTargetVersion().equals(modelItem.getTargetVersion()));
			Assert.assertTrue(((SyncItemJobModel) modelService.get(syncJob)).getSourceVersion().equals(modelItem.getSourceVersion()));

		}
		finally
		{

			//
		}
	}

	@Test
	public void getSyncJobOnItemSyncTimestampShouldNotThrowExceptionWhenNotSet()
	{

		final Map<String, Object> attributeMap = new HashMap<>();
		attributeMap.put(ItemSyncTimestamp.SOURCEITEM, sourceItem);
		attributeMap.put(ItemSyncTimestamp.TARGETITEM, targetItem);
		attributeMap.put(ItemSyncTimestamp.SOURCEVERSION, srcVersion);
		attributeMap.put(ItemSyncTimestamp.TARGETVERSION, tgtVersion);

		final ItemSyncTimestamp item = CatalogManager.getInstance().createItemSyncTimestamp(attributeMap);

		Assert.assertNull(item.getSyncJob());

	}
}
