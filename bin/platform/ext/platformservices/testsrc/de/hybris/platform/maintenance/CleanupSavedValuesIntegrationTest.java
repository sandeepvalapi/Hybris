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
package de.hybris.platform.maintenance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.SavedValueEntryType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.jobs.GenericMaintenanceJobPerformable;
import de.hybris.platform.jobs.maintenance.impl.CleanupSavedValuesStrategy;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CleanupSavedValuesIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private SessionService sessionService;

	@Resource
	private TypeService typeService;

	@Resource
	private UserService userService;

	private GenericMaintenanceJobPerformable gmjp;
	private CleanupSavedValuesStrategy cusvmi;

	private CatalogModel catalog;
	private CatalogVersionModel catver;
	private UserModel user1;
	private UserModel user2;

	@Before
	public void setUp()
	{
		final ItemObjectResolver modelResolver = Registry.getApplicationContext()
				.getBean("modelResolver", ItemObjectResolver.class);

		//the performable with all the needed services
		gmjp = new GenericMaintenanceJobPerformable();
		gmjp.setModelService(modelService);
		gmjp.setFlexibleSearchService(flexibleSearchService);
		gmjp.setSessionService(sessionService);
		gmjp.setModelResolver(modelResolver);

		cusvmi = new CleanupSavedValuesStrategy();
		cusvmi.setFlexibleSearchService(flexibleSearchService);
		cusvmi.setModelService(modelService);

		gmjp.setMaintenanceCleanupStrategy(cusvmi);

		//sampledata objects
		catalog = modelService.create(CatalogModel.class);
		catalog.setId("test");
		catver = modelService.create(CatalogVersionModel.class);
		catver.setVersion("xxx");
		catver.setCatalog(catalog);
		user1 = modelService.create(UserModel.class);
		user1.setUid("yyy");
		user2 = modelService.create(UserModel.class);
		user2.setUid("aaa");
		modelService.saveAll(catalog, catver, user1, user2);

	}

	@Test
	public void testRemoveTooManySavedValues()
	{
		createData();

		PerformResult result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(20, countSavedValues(catalog));
		assertEquals(20, countSavedValues(catver));
		assertEquals(2, countSavedValues(user1));
		assertEquals(20, countSavedValues(user2));

		result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(20, countSavedValues(catalog));
		assertEquals(20, countSavedValues(catver));
		assertEquals(2, countSavedValues(user1));
		assertEquals(20, countSavedValues(user2));

	}

	@Test
	public void testOldestValuesAreRemoved()
	{
		//create very old value
		final SavedValuesModel willBeDeleted = modelService.create(SavedValuesModel.class);
		willBeDeleted.setModificationType(SavedValueEntryType.CHANGED);
		willBeDeleted.setModifiedItemDisplayString("WILL_BE_DELETED");
		willBeDeleted.setModifiedItemType(typeService.getComposedTypeForClass(CatalogModel.class));
		willBeDeleted.setModifiedItem(catalog);
		willBeDeleted.setTimestamp(new Date(System.currentTimeMillis() - 1000L * 3600L * 24)); //one day old
		willBeDeleted.setCreationtime(willBeDeleted.getTimestamp());
		willBeDeleted.setUser(userService.getCurrentUser());
		modelService.save(willBeDeleted);

		//create 20 not so old, but old values
		createSavedValuesForModelInstance(catalog, 20);

		//create new value
		final SavedValuesModel willStay = modelService.create(SavedValuesModel.class);
		willStay.setModificationType(SavedValueEntryType.CHANGED);
		willStay.setModifiedItemDisplayString("WILL_STAY");
		willStay.setModifiedItemType(typeService.getComposedTypeForClass(CatalogModel.class));
		willStay.setModifiedItem(catalog);
		willStay.setTimestamp(new Date());
		willStay.setCreationtime(willStay.getTimestamp());
		willStay.setUser(userService.getCurrentUser());
		modelService.save(willStay);

		assertEquals(22, countSavedValues(catalog));

		final PerformResult result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());


		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(
				"select {pk} from {savedvalues} where {modifieditem} = ?item order by {creationtime} asc",// 
				Collections.singletonMap("item", catalog));
		final SearchResult<SavedValuesModel> finalSearchResult = flexibleSearchService.search(fsq);
		final List<SavedValuesModel> res = finalSearchResult.getResult();

		assertFalse(res.contains(willBeDeleted));
		assertTrue(res.contains(willStay));
		assertEquals(20, countSavedValues(catalog));
	}

	@Test
	public void testJobWithoutAnyExistingSavedValues()
	{
		assertEquals(0, countSavedValues());
		PerformResult result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		assertEquals(0, countSavedValues());
		result = gmjp.perform(new CronJobModel());
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertEquals(0, countSavedValues());
	}

	@Test
	public void testIllegalValues()
	{
		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel mcjm = new MaintenanceCleanupJobModel();
		cjm.setJob(mcjm);
		mcjm.setThreshold(Integer.valueOf(-1));

		try
		{
			gmjp.perform(cjm);
			fail("Expected IllegalArgumentException here");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("maxAllowedValues cannot be negative.", e.getMessage());
		}
	}

	@Test
	public void testSetThreesholdPerJob()
	{
		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel mcjm = new MaintenanceCleanupJobModel();
		cjm.setJob(mcjm);

		mcjm.setThreshold(Integer.valueOf(7));

		createSavedValuesForModelInstance(catalog, 30);
		assertEquals(30, countSavedValues());

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertEquals(7, countSavedValues());

	}

	@Test
	public void testSetNullThreesholdPerJob()
	{
		final CronJobModel cjm = new CronJobModel();
		final MaintenanceCleanupJobModel mcjm = new MaintenanceCleanupJobModel();
		cjm.setJob(mcjm);

		mcjm.setThreshold(null); //default value == 20 kicks in

		createSavedValuesForModelInstance(catalog, 30);
		assertEquals(30, countSavedValues());

		final PerformResult result = gmjp.perform(cjm);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());
		assertEquals(20, countSavedValues());
	}

	private void createData()
	{
		assertEquals(0, countSavedValues());

		//catalog with 30 savedValues
		createSavedValuesForModelInstance(catalog, 30);

		//catalogversion with 21 savedvalues
		createSavedValuesForModelInstance(catver, 21);

		//user1 with 2 saved values
		createSavedValuesForModelInstance(user1, 2);

		//user2 with 25 savedvalues
		createSavedValuesForModelInstance(user2, 25);

		//overall created savedvaluies == 63
		assertEquals(78, countSavedValues());
		assertEquals(30, countSavedValues(catalog));
		assertEquals(21, countSavedValues(catver));
		assertEquals(2, countSavedValues(user1));
		assertEquals(25, countSavedValues(user2));
	}


	/*
	 * counts overall count of savedvalues in db
	 */
	private int countSavedValues()
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {pk} from {savedvalues}");
		return flexibleSearchService.search(fsq).getTotalCount();
	}

	private int countSavedValues(final ItemModel model)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {pk} from {savedvalues} where {modifieditem} = ?item",// 
				Collections.singletonMap("item", model));
		return flexibleSearchService.search(fsq).getTotalCount();
	}

	/*
	 * creates for the given model instance SavedValues in the given count
	 */
	private void createSavedValuesForModelInstance(final ItemModel model, final int count)
	{
		for (int no = 0; no < count; no++)
		{
			final SavedValuesModel svm = modelService.create(SavedValuesModel.class);
			svm.setModificationType(SavedValueEntryType.CHANGED);
			svm.setModifiedItemDisplayString(model.getPk().toString());
			svm.setModifiedItemType(typeService.getComposedTypeForClass(model.getClass()));
			svm.setModifiedItem(model);
			svm.setCreationtime(new Date(System.currentTimeMillis() - 2000L * (count - no)));
			svm.setTimestamp(svm.getCreationtime());
			svm.setUser(userService.getCurrentUser());
			modelService.save(svm);
		}
	}
}
