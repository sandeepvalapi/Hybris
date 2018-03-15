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
package de.hybris.platform.catalog.job.strategy.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.job.callback.RemoveCallback;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.job.util.ImpexScriptGenerator;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.RemoveCatalogVersionCronJobModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Strategy for removing catalog (which obviously can not be null in {@link RemoveCatalogVersionCronJobModel} instance.
 * TODO extend tests with exception handling.
 */
@UnitTest
public class RemoveCatalogStrategyTest
{
	private RemoveCatalogStrategy strategy;


	@Mock
	private ModelService modelService;

	@Mock
	private CatalogVersionJobDao catalogVersionJobDao;

	@Mock
	private ImpexScriptGenerator impexScriptGenerator;

	@Mock
	private RemoveCallback removeCallback;

	@Mock
	private ImpExResource impexResourceMock;

	@Mock
	private ImportService importService;

	private ImportConfig importConfig;

	private List<ComposedTypeModel> unorderedComposedTypes;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		strategy = Mockito.spy(new RemoveCatalogStrategy());

		strategy.setModelService(modelService);
		strategy.setCatalogVersionJobDao(catalogVersionJobDao);
		strategy.setRemoveScriptGenerator(impexScriptGenerator);
		strategy.setRemoveCallback(removeCallback);
		strategy.setImportService(importService);

		unorderedComposedTypes = Mockito.spy(new ArrayList<ComposedTypeModel>());
		importConfig = Mockito.spy(new ImportConfig());

		Mockito.doReturn(impexResourceMock).when(strategy).createImpexResource(Mockito.any(StringBuilder.class));
		Mockito.doReturn(importConfig).when(strategy).createImpexConfig();
	}


	@Test(expected = NullPointerException.class)
	public void testWithoutCatalogVersions()
	{
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("bunny");

		final RemoveCatalogVersionCronJobModel cronJob = new RemoveCatalogVersionCronJobModel();
		cronJob.setCatalog(catalog);

		strategy.remove(cronJob);
	}


	@Test
	public void testWithEmptyCatalogVersions()
	{
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("bunny");
		catalog.setCatalogVersions(Collections.EMPTY_SET);

		final RemoveCatalogVersionCronJobModel cronJob = new RemoveCatalogVersionCronJobModel();
		cronJob.setCatalog(catalog);


		final ImportResult impexResult = Mockito.mock(ImportResult.class);

		Mockito.when(impexResult.getCronJob()).thenReturn(new ImpExImportCronJobModel());

		Mockito.when(importService.importData(importConfig)).thenReturn(impexResult);

		final PerformResult result = strategy.remove(cronJob);

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());

		Mockito.verify(removeCallback).beforeRemove(cronJob, Collections.EMPTY_SET);
		Mockito.verify(removeCallback).afterRemoved(cronJob, Collections.EMPTY_SET, null);
		Mockito.verify(catalogVersionJobDao).getOrderedComposedTypes();

		Mockito.verifyZeroInteractions(importService);

		Mockito.verify(modelService).refresh(catalog);
	}


	@Test
	public void testWithCatalogVersions()
	{
		final StringBuilder sampleScript = new StringBuilder(200);
		sampleScript
				.append("Jellyfish (also known as jellies or sea jellies or Medusozoa) are free-swimming members of the phylum Cnidaria.");

		final CatalogVersionModel catalogVersion = Mockito.spy(new CatalogVersionModel());
		catalogVersion.setVersion("version 4 bunny");


		final Set<CatalogVersionModel> versions = Collections.singleton(catalogVersion);

		final CatalogModel catalog = Mockito.spy(new CatalogModel());
		catalog.setId("bunny");
		catalog.setCatalogVersions(versions);



		Mockito.when(catalog.getCatalogVersions()).thenReturn(versions, Collections.EMPTY_SET);

		final RemoveCatalogVersionCronJobModel cronJob = Mockito.spy(new RemoveCatalogVersionCronJobModel());
		cronJob.setCatalog(catalog);

		final ComposedTypeModel ctOne = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		final ComposedTypeModel ctTwo = createComposedTypeModel("java.lang.Integer", "somePublicfieldTwo", "SomeCustomType");

		unorderedComposedTypes.addAll(Arrays.asList(ctOne, ctTwo));

		Mockito.when(catalogVersionJobDao.getOrderedComposedTypes()).thenReturn(unorderedComposedTypes);

		Mockito.when(Integer.valueOf(catalogVersionJobDao.getItemInstanceCount(catalogVersion, unorderedComposedTypes)))
				.thenReturn(Integer.valueOf(2));

		Mockito.when(impexScriptGenerator.generate(catalogVersion, unorderedComposedTypes)).thenReturn(sampleScript);


		final ImportResult impexResult = Mockito.mock(ImportResult.class);
		Mockito.when(impexResult.getCronJob()).thenReturn(new ImpExImportCronJobModel());
		Mockito.when(Boolean.valueOf(impexResult.isRunning())).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
				Boolean.FALSE);

		Mockito.doReturn(impexResult).when(importService).importData(Mockito.any(ImportConfig.class));

		final PerformResult result = strategy.remove(cronJob);

		Mockito.verify(catalogVersionJobDao).getOrderedComposedTypes();

		final Set<String> setOfQueries = new java.util.HashSet<String>();
		setOfQueries.add("SELECT count({pk}) FROM {ComposedType!} WHERE {fieldOne}  = ?version");
		setOfQueries.add("SELECT count({pk}) FROM {ComposedType!} WHERE {somePublicfieldTwo}  = ?version");

		final ArgumentMatcher<StringBuilder> stringMatcher = new ArgumentMatcher<StringBuilder>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof StringBuilder))
				{
					return false;
				}
				final StringBuilder buffer = (StringBuilder) argument;
				if (!buffer.toString().equalsIgnoreCase(sampleScript.toString()))
				{
					return false;
				}
				return true;
			}
		};

		Mockito.verify(strategy).createImpexResource(Mockito.argThat(stringMatcher));

		Mockito.verify(removeCallback).beforeRemove(cronJob, versions);

		Mockito.verify(removeCallback, Mockito.times(3)).doRemove(cronJob, versions, impexResult);

		Mockito.verify(modelService, Mockito.times(3)).refresh(impexResult.getCronJob());

		Mockito.verify(modelService).remove(catalog);

		Mockito.verify(removeCallback).afterRemoved(cronJob, versions, impexResult);

		//verify impexConfig

		final ArgumentMatcher<ImportConfig> importConfigMatcher = new ArgumentMatcher<ImportConfig>()
		{

			@Override
			public boolean matches(final Object argument)
			{
				if (!(argument instanceof ImportConfig))
				{
					return false;
				}
				final ImportConfig config = (ImportConfig) argument;

				if (config.getScript() != impexResourceMock)
				{
					return false;
				}
				if (config.isSynchronous())
				{
					return true; //PLA-10759
				}

				if (config.isRemoveOnSuccess())
				{
					return false;
				}

				return true;
			}

		};
		Mockito.verify(importService).importData(Mockito.argThat(importConfigMatcher));

		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());


	}

	private ComposedTypeModel createComposedTypeModel(final String attributeTypeName, final String attribiteQualifier,
			final String composedTypeCode)
	{
		final TypeModel type = new TypeModel();
		type.setCode(attributeTypeName);

		final AttributeDescriptorModel descriptor = new AttributeDescriptorModel();
		descriptor.setQualifier(attribiteQualifier);
		descriptor.setAttributeType(type);

		final ComposedTypeModel composedType = new ComposedTypeModel();
		composedType.setCode(composedTypeCode);
		composedType.setCatalogVersionAttribute(descriptor);

		return composedType;
	}

}
