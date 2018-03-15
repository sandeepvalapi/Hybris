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
package de.hybris.platform.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.cronjob.model.CompositeJobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.testframework.TestModelUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;




/**
 * Test has only a chance to work if a relation.handle.legacy is equal false or empty
 */
@IntegrationTest
public class OneToManyRelationOrderTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	private CompositeCronJobModel mainCompositeCronJob;
	private CompositeJobModel jobEntryOne;

	private CompositeEntryModel entryMister;
	private CompositeEntryModel entrySir;
	private CompositeEntryModel entryDuke;

	private Boolean legacyMode = null;

	@Before
	public void prepareComposite()
	{
		jobEntryOne = modelService.create(CompositeJobModel.class);
		jobEntryOne.setCode("rootJob");
		modelService.save(jobEntryOne);

		mainCompositeCronJob = createCompositeCronJob(jobEntryOne, "ourHero");


		legacyMode = StringUtils.isEmpty(Config.getParameter("relation.handle.legacy")) ? null : Boolean.valueOf(Config
				.getParameter("relation.handle.legacy"));
		Config.setParameter("relation.handle.legacy", Boolean.FALSE.toString());
	}



	@After
	public void unprepareComposite()
	{
		if (legacyMode != null)
		{
			Config.setParameter("relation.handle.legacy", legacyMode.toString());
		}
	}

	/**
	 * In terms of 1:n relation this is scenario for setting 4 entries from 1 side of the relation
	 */
	@Test
	public void testSetRelationEntriesFromOneSide()
	{
		prepareThreeEntries();
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryMister, entrySir, entryDuke);


		final CompositeEntryModel firstEntry = modelService.create(CompositeEntryModel.class);
		firstEntry.setCode("count");

		modelService.saveAll(firstEntry);
		mainCompositeCronJob.setCompositeEntries(Arrays.asList(entryMister, entrySir, entryDuke, firstEntry));
		modelService.save(mainCompositeCronJob);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryMister, entrySir, entryDuke, firstEntry);

		editEntry(entryMister);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryMister, entrySir, entryDuke, firstEntry);

		editEntry(entryDuke);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryMister, entrySir, entryDuke, firstEntry);

	}

	@Test
	public void testSetRelationEntriesFromManySideWithoutCollectionAssignment()
	{

		final CompositeEntryModel entryFirst = createEntryFromManySide(mainCompositeCronJob, "first code");

		verifyPosValuesOrder(mainCompositeCronJob, 0);

		final CompositeEntryModel entryMiddle = createEntryFromManySide(mainCompositeCronJob, "middle code");

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMiddle);

		final CompositeEntryModel entryLast = createEntryFromManySide(mainCompositeCronJob, "last code");

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMiddle, entryLast);

		editEntry(entryMiddle);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMiddle, entryLast);

		editEntry(entryFirst);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMiddle, entryLast);
	}




	@Test
	public void testSetRelationEntriesFromManySide()
	{
		final CompositeEntryModel entryFirst = createEntryFromManySide(mainCompositeCronJob, "first code");

		verifyPosValuesOrder(mainCompositeCronJob, 0);

		prepareThreeEntries();
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMister, entrySir, entryDuke);

		final CompositeEntryModel entryLast = createEntryFromManySide(mainCompositeCronJob, "last code");

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3, 4);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMister, entrySir, entryDuke, entryLast);


		editEntry(entryFirst);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3, 4);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMister, entrySir, entryDuke, entryLast);

		editEntry(entrySir);

		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2, 3, 4);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entryMister, entrySir, entryDuke, entryLast);


	}


	// PLA-12468
	@Test
	public void testDirtyFlagInconsequence()
	{
		final CompositeEntryModel entryFirst = createEntryFromManySide(mainCompositeCronJob, "first code");
		final CompositeEntryModel entrySecond = createEntryFromManySide(mainCompositeCronJob, "second code");
		final CompositeEntryModel entryThird = createEntryFromManySide(mainCompositeCronJob, "third code");

		//		modelService.detachAll();//it does not help
		//first check of the order
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entrySecond, entryThird);

		rewriteEntry(mainCompositeCronJob, entryFirst);
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entrySecond, entryThird);

		rewriteEntry(mainCompositeCronJob, entrySecond);
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entrySecond, entryThird);


		rewriteEntry(mainCompositeCronJob, entryThird);
		verifyPosValuesOrder(mainCompositeCronJob, 0, 1, 2);
		verifyEntryOrder(mainCompositeCronJob.getCompositeEntries(), entryFirst, entrySecond, entryThird);

		//but 
		final CompositeCronJobModel oneSideComposite = createCompositeCronJob(jobEntryOne, "one side test");

		final CompositeEntryModel entryFirstOneSide = createEntryFromOneSide(oneSideComposite, "first from one side");
		final CompositeEntryModel entrySecondOneSide = createEntryFromOneSide(oneSideComposite, "second from one side");
		final CompositeEntryModel entryThirdOneSide = createEntryFromOneSide(oneSideComposite, "third from one side");

		//		modelService.detachAll();//it does not help

		//first check of the order
		verifyPosValuesOrder(oneSideComposite, 0, 1, 2);
		verifyEntryOrder(oneSideComposite.getCompositeEntries(), entryFirstOneSide, entrySecondOneSide, entryThirdOneSide);

		rewriteEntry(oneSideComposite, entryFirstOneSide);
		verifyPosValuesOrder(oneSideComposite, 0, 1, 2);
		verifyEntryOrder(oneSideComposite.getCompositeEntries(), entryFirstOneSide, entrySecondOneSide, entryThirdOneSide);

		rewriteEntry(oneSideComposite, entrySecondOneSide);
		verifyPosValuesOrder(oneSideComposite, 0, 1, 2);
		verifyEntryOrder(oneSideComposite.getCompositeEntries(), entryFirstOneSide, entrySecondOneSide, entryThirdOneSide);


		rewriteEntry(oneSideComposite, entryThirdOneSide);
		verifyPosValuesOrder(oneSideComposite, 0, 1, 2);
		verifyEntryOrder(oneSideComposite.getCompositeEntries(), entryFirstOneSide, entrySecondOneSide, entryThirdOneSide);
	}




	private void prepareThreeEntries()
	{
		modelService.refresh(mainCompositeCronJob);
		final List<CompositeEntryModel> existingEntries = new ArrayList<CompositeEntryModel>(
				mainCompositeCronJob.getCompositeEntries());

		entryMister = modelService.create(CompositeEntryModel.class);
		entryMister.setCode("mr");

		entrySir = modelService.create(CompositeEntryModel.class);
		entrySir.setCode("sir");

		entryDuke = modelService.create(CompositeEntryModel.class);
		entryDuke.setCode("duke");

		modelService.saveAll(entryMister, entrySir, entryDuke);

		existingEntries.addAll(Arrays.asList(entryMister, entrySir, entryDuke));

		mainCompositeCronJob.setCompositeEntries(existingEntries);
		modelService.save(mainCompositeCronJob);

		//verifyPosValuesOrder(0, 1, 2);
	}

	private CompositeCronJobModel createCompositeCronJob(final CompositeJobModel jobEntryOne, final String code)
	{
		final CompositeCronJobModel localCompositeCronJob = modelService.create(CompositeCronJobModel.class);
		localCompositeCronJob.setCode(code);
		localCompositeCronJob.setJob(jobEntryOne);

		modelService.save(localCompositeCronJob);
		modelService.refresh(jobEntryOne);

		return localCompositeCronJob;
	}

	private CompositeEntryModel createEntryFromManySide(final CompositeCronJobModel cronJob, final String code)
	{
		final CompositeEntryModel entry = modelService.create(CompositeEntryModel.class);
		entry.setCode(code);
		entry.setCompositeCronJob(cronJob);

		modelService.save(entry);
		modelService.refresh(cronJob);
		return entry;
	}

	private CompositeEntryModel createEntryFromOneSide(final CompositeCronJobModel cronJob, final String code)
	{
		final CompositeEntryModel entry = modelService.create(CompositeEntryModel.class);
		entry.setCode(code);
		//entryFirst.setCompositeCronJob(mainCompositeCronJob);

		final List<CompositeEntryModel> allEntries = new ArrayList<CompositeEntryModel>(cronJob.getCompositeEntries());

		allEntries.add(entry);
		cronJob.setCompositeEntries(allEntries);

		modelService.saveAll(entry, cronJob);
		modelService.refresh(cronJob);
		return entry;
	}

	private void editEntry(final CompositeEntryModel _entry)
	{
		// TODO Interesting thing is that we need to re-read model to obtain proper (real) optimistic counter value (hjmpTS), otherwise test will fail (of course!)
		final CompositeEntryModel entry = TestModelUtils.reReadModel(_entry);

		entry.setCode(entry.getCode() + "(edited)");
		//entryMiddle.setCompositeCronJob(mainCompositeCronJob);

		modelService.save(entry);
		modelService.refresh(mainCompositeCronJob);
	}


	private void rewriteEntry(final CompositeCronJobModel mainCompositeCronJob, final CompositeEntryModel entry)
	{
		entry.setCode(entry.getCode() + "(edited)");
		entry.setCompositeCronJob(mainCompositeCronJob);

		modelService.save(entry);
		modelService.refresh(mainCompositeCronJob);
	}

	/**
	 * 
	 */
	private void verifyEntryOrder(final List<CompositeEntryModel> allEntries, final CompositeEntryModel... expectedEntries)
	{
		int i = 0;
		for (final CompositeEntryModel expectedSingleEntry : expectedEntries)
		{
			Assert.assertEquals("Expected <" + expectedSingleEntry.getCode() + "> but got <" + allEntries.get(i).getCode() + ">",
					allEntries.get(i++), expectedSingleEntry);
		}
	}


	/**
	 * Return _POS column value for a
	 */
	private Iterator<Integer> queryForPositions(final CompositeCronJobModel itemModel)
	{
		final String posColumn = CompositeEntryModel.COMPOSITECRONJOB + "pos";
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT  {" + posColumn + "} FROM " + //
				" {" + CompositeEntryModel._TYPECODE + "} WHERE {" + CompositeEntryModel.COMPOSITECRONJOB + "} = ?REL" + //
				" ORDER BY {" + posColumn + "}", Collections.singletonMap("REL", itemModel));
		fsq.setResultClassList(Collections.singletonList(Integer.class));
		final SearchResult<Integer> result = flexibleSearchService.search(fsq);
		final Iterator<Integer> elems = result.getResult().iterator();
		return elems;
	}


	private void verifyPosValuesOrder(final CompositeCronJobModel mainCompositeCronJob, final int... positions)
	{
		final Iterator<Integer> results = queryForPositions(mainCompositeCronJob);
		for (final int pos : positions)
		{
			Assert.assertTrue(results.hasNext());
			Assert.assertEquals(Integer.valueOf(pos), results.next());
		}
		Assert.assertFalse("There should not more elements than expected " + positions.length, results.hasNext());
	}
}
