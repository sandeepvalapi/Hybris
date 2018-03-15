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
package de.hybris.platform.catalog.jalo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests preservation of localized feature values for languages <b>excluded from sync</b>.
 * 
 * See PLA-12514 for details.
 */
public class PLA_12514_Test extends HybrisJUnit4Test
{

	@Test
	public void testHiddenFeatureLanguagePreserving() throws Exception
	{
		// 1. setup
		final List<Language> allLanguages = createLanguages(4);
		final Language language0 = allLanguages.get(0);
		final Language language1 = allLanguages.get(1);
		final Language language2 = allLanguages.get(2);
		final Language language3 = allLanguages.get(3);
		final List<Language> syncLanguages = Arrays.asList(language1, language2);
		final ClassAttributeAssignment clAttr = createClassificationAttribute();
		final CatalogVersion src = createCatalogVersion("cat", "src", syncLanguages);
		final CatalogVersion tgt = createCatalogVersion("cat", "tgt", syncLanguages);

		final Product product = createProductWithLocalizedFeatures("prod", src, clAttr, syncLanguages);
		assertEquals(Collections.EMPTY_MAP, product.getAllName());

		final Map<Language, String> syncLocFValues = new HashMap<Language, String>();
		syncLocFValues.put(language1, assembleValue(language1));
		syncLocFValues.put(language2, assembleValue(language2));

		assertLocalizedFeatureValues(product, clAttr, syncLocFValues, allLanguages);

		final CatalogVersionSyncJob job = createSyncJob(src, tgt, null, 1);

		// 2. run sync first time

		final SyncItemCronJob cronJob = job.newExecution();
		job.perform(cronJob, true);


		// 3. validate

		assertLocalizedFeatureValues(product, clAttr, syncLocFValues, allLanguages);

		final Product pCopy = (Product) CatalogManager.getInstance().getCounterpartItem(product, tgt);
		assertNotNull(pCopy);

		assertLocalizedFeatureValues(pCopy, clAttr, syncLocFValues, allLanguages);

		// 4. add values for non-sync languages to both src and tgt ( shall NOT overwrite each other )
		final Map<Language, String> newTgtFValues = new HashMap<Language, String>(syncLocFValues);
		newTgtFValues.put(language0, "l0 tgt");
		newTgtFValues.put(language3, "l3 tgt");
		setFeatureValues(pCopy, clAttr, newTgtFValues);
		assertLocalizedFeatureValues(pCopy, clAttr, newTgtFValues, allLanguages);
		final Map<Language, String> newSrcFValues = new HashMap<Language, String>(syncLocFValues);
		newSrcFValues.put(language0, "l0 src");
		newSrcFValues.put(language3, "l3 src");
		setFeatureValues(product, clAttr, newSrcFValues);
		assertLocalizedFeatureValues(product, clAttr, newSrcFValues, allLanguages);

		// 5. make src product modified to trigger re-sync

		final Map<Language, String> names = new HashMap<Language, String>();
		names.put(language1, "l1 name");
		names.put(language2, "l2 name");
		Thread.sleep(1000); // make sure mod timestamp is really changed even on mysql
		product.setAllName(names);
		assertEquals(names, product.getAllName());
		assertEquals(Collections.EMPTY_MAP, pCopy.getAllName());

		// 6. run sync again

		final SyncItemCronJob cronJob2 = job.newExecution();
		job.perform(cronJob2, true);

		// 7. validate names getting copied but non-sync features getting preserved

		assertEquals(names, product.getAllName());
		assertEquals(names, pCopy.getAllName());

		assertLocalizedFeatureValues(product, clAttr, newSrcFValues, allLanguages);
		assertLocalizedFeatureValues(pCopy, clAttr, newTgtFValues, allLanguages);
	}

	@Ignore("only for manual testing")
	@Test
	public void testPerformance() throws Exception
	{
		final int PRODUCTS = 10000;
		final int THREADS = 4;

		final CatalogVersionSyncJob job = createPerfCatalogAndJob(PRODUCTS, THREADS);

		{
			final SyncItemCronJob cronJob = job.newExecution();
			final long time1 = System.currentTimeMillis();
			job.perform(cronJob, true);
			final long time2 = System.currentTimeMillis();

			System.out.println("PLA-12514 performance test (products:" + PRODUCTS + " threads:" + THREADS + " initial sync) took "
					+ ((time2 - time1) / 1000) + " seconds");
		}

		{
			final SyncItemCronJob cronJob = job.newExecution();
			cronJob.setForceUpdate(true);

			final long time1 = System.currentTimeMillis();
			job.perform(cronJob, true);
			final long time2 = System.currentTimeMillis();

			System.out.println("PLA-12514 performance test (products:" + PRODUCTS + " threads:" + THREADS + " update sync) took "
					+ ((time2 - time1) / 1000) + " seconds");
		}
	}

	private CatalogVersionSyncJob createPerfCatalogAndJob(final int products, final int maxThreads) throws Exception
	{
		final List<Language> allLanguages = createLanguages(2);
		final ClassAttributeAssignment clAttr = createClassificationAttribute();
		final CatalogVersion src = createCatalogVersion("perfCat", "src", allLanguages);
		final CatalogVersion tgt = createCatalogVersion("perfCat", "tgt", allLanguages);

		for (int i = 0; i < products; i++)
		{
			createProductWithLocalizedFeatures("perfProd" + i, src, clAttr, allLanguages);
		}

		return createSyncJob(src, tgt, allLanguages, maxThreads);
	}

	private CatalogVersionSyncJob createSyncJob(final CatalogVersion src, final CatalogVersion tgt,
			final List<Language> syncLanguages, final int maxThreads)
	{
		final Map args = new HashMap();
		args.put(CatalogVersionSyncJob.CODE, "PLA-12514-Job");
		args.put(CatalogVersionSyncJob.SOURCEVERSION, src);
		args.put(CatalogVersionSyncJob.TARGETVERSION, tgt);
		args.put(CatalogVersionSyncJob.MAXTHREADS, Integer.valueOf(maxThreads));
		if (syncLanguages != null)
		{
			args.put(CatalogVersionSyncJob.SYNCLANGUAGES, syncLanguages);
		}
		return CatalogManager.getInstance().createCatalogVersionSyncJob(args);
	}

	void assertLocalizedFeatureValues(final Product product, final ClassAttributeAssignment clAttr,
			final Map<Language, String> expected, final Collection<Language> allLanguages)
	{
		final FeatureContainer featureContainer = FeatureContainer.loadTyped(product, Collections.singletonList(clAttr));

		final TypedFeature<Object> feature = featureContainer.getFeature(clAttr);

		final SessionContext lCtx = jaloSession.createSessionContext();
		for (final Map.Entry<Language, String> e : expected.entrySet())
		{
			lCtx.setLanguage(e.getKey());
			assertEquals(Collections.singletonList(e.getValue()), feature.getValuesDirect(lCtx));
		}
		for (final Language otherLang : allLanguages)
		{
			if (!expected.containsKey(otherLang))
			{
				lCtx.setLanguage(otherLang);
				assertEquals(Collections.EMPTY_LIST, feature.getValuesDirect(lCtx));
			}
		}
	}

	String assembleValue(final Language language)
	{
		return "localized-" + language.getIsoCode();
	}

	Product createProductWithLocalizedFeatures(final String code, final CatalogVersion catalogVersion,
			final ClassAttributeAssignment clAttr, final List<Language> languages) throws JaloGenericCreationException,
			JaloAbstractTypeException, JaloItemNotFoundException, ConsistencyCheckException
	{
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(Product.CODE, code);
		attributes.put(CatalogConstants.Attributes.Product.CATALOGVERSION, catalogVersion);

		final Product product = ComposedType.newInstance(null, Product.class, attributes);

		final Map<Language, String> fValues = new HashMap<Language, String>();
		for (final Language l : languages)
		{
			fValues.put(l, assembleValue(l));
		}
		setFeatureValues(product, clAttr, fValues);

		return product;
	}

	void setFeatureValues(final Product product, final ClassAttributeAssignment clAttr, final Map<Language, String> values)
			throws ConsistencyCheckException
	{
		final FeatureContainer featureContainer = FeatureContainer.createTyped(product, Collections.singletonList(clAttr));

		final TypedFeature<Object> feature = featureContainer.getFeature(clAttr);
		final SessionContext lCtx = jaloSession.createSessionContext();
		for (final Map.Entry<Language, String> e : values.entrySet())
		{
			lCtx.setLanguage(e.getKey());
			feature.setValue(lCtx, e.getValue());
		}
		featureContainer.store();
	}

	ClassAttributeAssignment createClassificationAttribute() throws ConsistencyCheckException
	{
		final CatalogManager catalogManager = CatalogManager.getInstance();

		final ClassificationSystem clSys = catalogManager.createClassificationSystem("clsys");
		final ClassificationSystemVersion clSysVer = catalogManager
				.createClassificationSystemVersion(clSys, "ver", (Language) null);

		final ClassificationClass clClass = clSysVer.createClass("class");
		final ClassificationAttribute attr = clSysVer.createClassificationAttribute("terms");
		final ClassAttributeAssignment clAttr = clClass
				.assignAttribute(attr, ClassificationAttributeTypeEnum.STRING, null, null, 0);
		clAttr.setLocalized(true);

		return clAttr;
	}

	CatalogVersion createCatalogVersion(final String catId, final String versionId, final List<Language> languages)
	{
		final CatalogManager catalogManager = CatalogManager.getInstance();
		// get or create catalog
		Catalog cat = catalogManager.getCatalog(catId);
		if (cat == null)
		{
			cat = catalogManager.createCatalog(catId);
		}
		// create version
		final CatalogVersion catalogVersion = catalogManager.createCatalogVersion(cat, versionId, null);
		catalogVersion.setLanguages(languages);

		return catalogVersion;
	}

	List<Language> createLanguages(final int amount) throws ConsistencyCheckException
	{
		final C2LManager c2LManager = C2LManager.getInstance();
		final Language[] ret = new Language[amount];
		for (int i = 0; i < amount; i++)
		{
			ret[i] = c2LManager.createLanguage("LLL" + i);
		}
		return Arrays.asList(ret);
	}

}
