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
package de.hybris.platform.catalog.jalo.synchronization;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.TestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * PLA-12402
 */
@IntegrationTest
public class CatalogVersionDependentJobsTest extends ServicelayerBaseTest
{
	@Resource
	private EnumerationService enumerationService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private static final String CTGR0 = "Ctgr0";
	private static final String CTLG_A = "ctlgA";
	private static final String CTLG_B = "ctlgB";
	private static final String STAGED = "staged";
	private static final String PREVIEW = "preview";
	private static final String PRODUCT0_CODE = "prd0";

	private CatalogModel ctlgA;
	private CatalogModel ctlgB;
	private CatalogVersionModel ctlgAStaged;
	private CatalogVersionModel ctlgAPreview;
	private CatalogVersionModel ctlgBStaged;
	private CatalogVersionModel ctlgBPreview;
	private ProductModel prd0;
	private CategoryModel ctgr0A;
	private CategoryModel ctgr0B;
	private CatalogVersionSyncJobModel ctlgAStaged2PreviewProducts;
	private CatalogVersionSyncJobModel ctlgAStaged2PreviewCategories;
	private CatalogVersionSyncJobModel ctlgBStaged2PreviewCategories;

	@Before
	public void setUp()
	{
		ctlgA = createCatalogModel(CTLG_A, Boolean.FALSE);
		ctlgB = createCatalogModel(CTLG_B, Boolean.FALSE);
		modelService.save(ctlgA);
		modelService.save(ctlgB);
		ctlgAStaged = createCatalogVersion(ctlgA, STAGED, Boolean.TRUE);
		ctlgAPreview = createCatalogVersion(ctlgA, PREVIEW, Boolean.TRUE);
		ctlgBStaged = createCatalogVersion(ctlgB, STAGED, Boolean.TRUE);
		ctlgBPreview = createCatalogVersion(ctlgB, PREVIEW, Boolean.TRUE);
		modelService.save(ctlgAStaged);
		modelService.save(ctlgAPreview);
		modelService.save(ctlgBStaged);
		modelService.save(ctlgBPreview);
		prd0 = createProduct(PRODUCT0_CODE, ctlgAStaged, ArticleApprovalStatus.CHECK);
		modelService.save(prd0);
		ctgr0A = createCategory(CTGR0, ctlgAStaged);
		modelService.save(ctgr0A);
		ctgr0B = createCategory(CTGR0, ctlgBStaged);
		modelService.save(ctgr0B);

		final List<CategoryModel> superCategories = new ArrayList<>();
		superCategories.add(ctgr0A);
		superCategories.add(ctgr0B);
		prd0.setSupercategories(superCategories);
		modelService.save(prd0);

		ctlgAStaged2PreviewProducts = createCatalogVersionSyncJob("CtlgA Staged->Preview Products", ctlgAStaged, ctlgAPreview,
				Collections.singletonList(typeService.getComposedTypeForClass(ProductModel.class)), Boolean.TRUE, null);
		modelService.save(ctlgAStaged2PreviewProducts);

		ctlgAStaged2PreviewCategories = createCatalogVersionSyncJob("CtlgA Staged->Preview Categories", ctlgAStaged, ctlgAPreview,
				Collections.singletonList(typeService.getComposedTypeForClass(CategoryModel.class)), Boolean.TRUE, null);
		modelService.save(ctlgAStaged2PreviewCategories);

		ctlgBStaged2PreviewCategories = createCatalogVersionSyncJob("CtlgB Staged->Preview Categories", ctlgBStaged, ctlgBPreview,
				Collections.singletonList(typeService.getComposedTypeForClass(CategoryModel.class)), Boolean.TRUE,
				Collections.singleton(ctlgAStaged2PreviewProducts));
		modelService.save(ctlgBStaged2PreviewCategories);
	}

	@Test
	public void testDependentJobsWithDifferentCatalogVersionsAsTargetAndSource()
	{
		try
		{
			TestUtils.disableFileAnalyzer("Expecting 'sync ended with 1 unfinished items - see last sync media for details'");

			assertFinished(performSynchronization(ctlgBStaged2PreviewCategories));

			assertFinished(performSynchronization(ctlgAStaged2PreviewCategories));
			assertFinished(performSynchronization(ctlgAStaged2PreviewProducts));

			final SearchResult result = flexibleSearchService.search(buildQuery(CTLG_A, PREVIEW));

			assertEquals(1, result.getCount());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	private CatalogModel createCatalogModel(final String id, final Boolean isDefault)
	{
		final CatalogModel ctlg = new CatalogModel();
		ctlg.setId(id);
		ctlg.setDefaultCatalog(isDefault);
		return ctlg;
	}

	private CatalogVersionModel createCatalogVersion(final CatalogModel catalog, final String version, final Boolean isActive)
	{
		final CatalogVersionModel cv = new CatalogVersionModel();
		cv.setCatalog(catalog);
		cv.setVersion(version);
		cv.setActive(isActive);
		return cv;
	}

	private ProductModel createProduct(final String code, final CatalogVersionModel catalogVersion,
			final ArticleApprovalStatus status)
	{
		final ProductModel pm = new ProductModel();
		pm.setCode(code);
		pm.setCatalogVersion(catalogVersion);
		pm.setApprovalStatus(status);
		return pm;
	}

	private CategoryModel createCategory(final String code, final CatalogVersionModel catalogVersion)
	{
		final CategoryModel c = new CategoryModel();
		c.setCode(code);
		c.setCatalogVersion(catalogVersion);
		return c;
	}

	private CatalogVersionSyncJobModel createCatalogVersionSyncJob(final String code, final CatalogVersionModel source,
			final CatalogVersionModel target, final List<ComposedTypeModel> rootTypes, final Boolean isCreateNewItems,
			final Set<CatalogVersionSyncJobModel> dependsOn)
	{
		final CatalogVersionSyncJobModel cvsj = new CatalogVersionSyncJobModel();
		cvsj.setActive(Boolean.TRUE);
		cvsj.setCode(code);
		cvsj.setSourceVersion(source);
		cvsj.setTargetVersion(target);
		cvsj.setRootTypes(rootTypes);
		cvsj.setCreateNewItems(isCreateNewItems);
		if (dependsOn != null)
		{
			cvsj.setDependsOnSyncJobs(dependsOn);
		}
		return cvsj;
	}

	private CatalogVersionSyncCronJob createFullSyncCronJob(final CatalogVersionSyncJob job)
	{
		final CatalogVersionSyncCronJob cj = (CatalogVersionSyncCronJob) job.newExecution();
		job.configureFullVersionSync(cj);
		return cj;
	}

	private void assertFinished(final CatalogVersionSyncJob job)
	{
		assertEquals(
				enumerationService.<CronJobStatus> getEnumerationValue(CronJobStatus.class,
						de.hybris.platform.cronjob.constants.GeneratedCronJobConstants.Enumerations.CronJobStatus.FINISHED).getCode(),
				job.getCronJobs().iterator().next().getStatus().getCode());
	}

	private CatalogVersionSyncJob performSynchronization(final CatalogVersionSyncJobModel catalogVersionSyncJobModel)
	{
		final CatalogVersionSyncJob catalogVersionSyncJob = modelService
				.<CatalogVersionSyncJob> getSource(catalogVersionSyncJobModel);
		final CatalogVersionSyncCronJob catalogVersionSyncJobModelCronJob = createFullSyncCronJob(catalogVersionSyncJob);
		catalogVersionSyncJob.perform(catalogVersionSyncJobModelCronJob, true);
		return catalogVersionSyncJob;
	}

	private String buildQuery(final String catalog, final String version)
	{
		return "select {cpr.pk}, {cpr.source}, {cpr.target} from {Catalog as c}, {CatalogVersion as cv}, {Category as cg}, {CategoryProductRelation as cpr} where {c.id}='"
				+ catalog
				+ "' and {c.pk}={cv.catalog} and {cv.version}='"
				+ version
				+ "' and {cv.pk}={cg.catalogversion} and {cg.pk}={cpr.source}";
	}

}
