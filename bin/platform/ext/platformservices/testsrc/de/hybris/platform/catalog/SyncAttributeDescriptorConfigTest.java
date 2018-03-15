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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.impl.SyncAttributeDescriptorConfigValidator;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * Integration test for verifying a {@link SyncAttributeDescriptorConfigValidator}
 */
@IntegrationTest
public class SyncAttributeDescriptorConfigTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Test
	public void testConfigModel() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final SyncItemJobModel syncItemModel = modelService.create(SyncItemJobModel.class);
		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");
		modelService.save(cm1);

		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);

		syncItemModel.setSourceVersion(cmv1);

		final CatalogModel cm2 = modelService.create(CatalogModel.class);
		cm2.setId("sl_b");

		modelService.save(cm2);
		final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
		cmv2.setCatalog(cm2);
		cmv2.setVersion("v1.0");

		modelService.save(cmv2);
		syncItemModel.setTargetVersion(cmv2);

		modelService.save(syncItemModel);

		final AttributeDescriptorModel nameAttribute = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("Product"), ProductModel.NAME);

		final SyncAttributeDescriptorConfigModel sadcModel = modelService.create(SyncAttributeDescriptorConfigModel.class);

		sadcModel.setSyncJob(syncItemModel);
		sadcModel.setAttributeDescriptor(nameAttribute);
		sadcModel.setIncludedInSync(Boolean.FALSE);

		modelService.save(sadcModel);
	}

	/**
	 * Tests {@link SyncAttributeDescriptorConfigModel} for inherited attribute in this case {@link ItemModel#PK}
	 */
	@Test
	public void testConfigModelInherited() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final SyncItemJobModel syncItemModel = modelService.create(SyncItemJobModel.class);

		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");

		modelService.save(cm1);
		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);

		syncItemModel.setSourceVersion(cmv1);

		final CatalogModel cm2 = modelService.create(CatalogModel.class);
		cm2.setId("sl_b");

		modelService.save(cm2);
		final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
		cmv2.setCatalog(cm2);
		cmv2.setVersion("v2.0");

		modelService.save(cmv2);

		syncItemModel.setTargetVersion(cmv2);

		modelService.save(syncItemModel);

		final AttributeDescriptorModel pkAttribute = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("Product"), ItemModel.PK);

		final SyncAttributeDescriptorConfigModel sadcModel = new SyncAttributeDescriptorConfigModel();

		sadcModel.setSyncJob(syncItemModel);
		sadcModel.setAttributeDescriptor(pkAttribute);
		sadcModel.setIncludedInSync(Boolean.FALSE);
		try
		{
			modelService.save(sadcModel);
			Assert.fail("It should not be possible to save a SyncAttributeDescriptorConfigModel for an inhertied attribute");
		}
		catch (final ModelSavingException e)
		{
			assertTrue("", e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof SyncAttributeDescriptorConfigValidator);
		}
		catch (final Exception e)
		{
			Assert.fail("unexpected exception: " + e);
		}
	}

	/**
	 * Tests case where there are two {@link SyncAttributeDescriptorConfigModel} for the same attribute assigned to one
	 * {@link SyncItemJobModel}
	 */
	@Test
	public void testModelTwoConfigs() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final SyncItemJobModel syncItemJobModel = modelService.create(SyncItemJobModel.class);

		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");
		modelService.save(cm1);

		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);

		syncItemJobModel.setSourceVersion(cmv1);

		final CatalogModel cm2 = modelService.create(CatalogModel.class);
		cm2.setId("sl_b");
		modelService.save(cm2);
		final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
		cmv2.setCatalog(cm2);
		cmv2.setVersion("v2.0");
		modelService.save(cmv2);

		syncItemJobModel.setTargetVersion(cmv2);

		modelService.save(syncItemJobModel);

		final AttributeDescriptorModel nameAttribute1 = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("Product"), ProductModel.NAME);

		final SyncAttributeDescriptorConfigModel syncAttributeConfigModel1 = modelService
				.create(SyncAttributeDescriptorConfigModel.class);

		syncAttributeConfigModel1.setSyncJob(syncItemJobModel);
		syncAttributeConfigModel1.setAttributeDescriptor(nameAttribute1);
		syncAttributeConfigModel1.setIncludedInSync(Boolean.FALSE);

		modelService.save(syncAttributeConfigModel1);

		final AttributeDescriptorModel nameAttribute2 = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("Product"), ProductModel.NAME);

		final SyncAttributeDescriptorConfigModel syncAttributeConfigModel2 = new SyncAttributeDescriptorConfigModel();

		syncAttributeConfigModel2.setSyncJob(syncItemJobModel);
		syncAttributeConfigModel2.setAttributeDescriptor(nameAttribute2);
		syncAttributeConfigModel2.setIncludedInSync(Boolean.FALSE);
		try
		{
			modelService.save(syncAttributeConfigModel2);
			Assert.fail("should not be possible to assign two configs to one attribute");
		}
		catch (final ModelSavingException e)
		{
			assertTrue("", e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof SyncAttributeDescriptorConfigValidator);
		}
		catch (final Exception e)
		{
			Assert.fail("unexpected exception: " + e);
		}
	}

	@Test
	public void testConfigModelWithInheritedAD()
	{
		final SyncItemJobModel syncItemJobModel = modelService.create(SyncItemJobModel.class);

		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");
		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		syncItemJobModel.setSourceVersion(cmv1);

		final CatalogModel cm2 = modelService.create(CatalogModel.class);
		cm2.setId("sl_b");
		final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
		cmv2.setCatalog(cm2);
		cmv2.setVersion("v2.0");
		syncItemJobModel.setTargetVersion(cmv2);

		final AttributeDescriptorModel approvalStatusAttribute = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("Product"), ProductModel.APPROVALSTATUS);

		final SyncAttributeDescriptorConfigModel sadcm = modelService.create(SyncAttributeDescriptorConfigModel.class);
		sadcm.setSyncJob(syncItemJobModel);
		sadcm.setAttributeDescriptor(approvalStatusAttribute);
		modelService.save(sadcm);

		final AttributeDescriptorModel approvalStatusAttribute2 = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForCode("VariantProduct"), VariantProductModel.APPROVALSTATUS);

		final SyncAttributeDescriptorConfigModel sadcm2 = modelService.create(SyncAttributeDescriptorConfigModel.class);
		sadcm2.setSyncJob(syncItemJobModel);
		sadcm2.setAttributeDescriptor(approvalStatusAttribute2);
		try
		{
			modelService.save(sadcm2);
			Assert.fail("It should not be possible to save a SyncAttributeDescriptorConfigModel for an inhertied attribute");
		}
		catch (final ModelSavingException e)
		{
			assertTrue("", e.getCause() instanceof InterceptorException);
			final InterceptorException interceptorException = (InterceptorException) e.getCause();
			assertTrue(interceptorException.getInterceptor() instanceof SyncAttributeDescriptorConfigValidator);
		}
		catch (final Exception e)
		{
			Assert.fail("unexpected exception: " + e);
		}

	}

	@Test
	public void testInterceptorIsInstalled()
	{
		final InterceptorRegistry reg = ((DefaultModelService) modelService).getInterceptorRegistry();
		final Collection<ValidateInterceptor> validaters = reg.getValidateInterceptors("SyncAttributeDescriptorConfig");
		assertFalse(validaters.isEmpty());
		boolean found = false;
		for (final ValidateInterceptor inter : validaters)
		{
			if (inter instanceof SyncAttributeDescriptorConfigValidator)
			{
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
}
