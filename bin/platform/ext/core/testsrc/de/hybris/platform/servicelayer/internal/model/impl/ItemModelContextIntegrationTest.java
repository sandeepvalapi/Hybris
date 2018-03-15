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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.CollectionUtils;


/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
@IntegrationTest
public class ItemModelContextIntegrationTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(ItemModelContextIntegrationTest.class);

	private static final String ORIGINAL_CODE_VALUE = "new Code";
	private static final String ORIGINAL_EAN_VALUE = "new Ean";
	private static final String ORIGINAL_NAME_VALUE = "new Name";
	private static final String EAN_CHANGED_VALUE = "changed";
	private static final String NAME_CHANGED_VALUE = "name changed";

	private static final Locale testFirstLocale = Locale.KOREAN;
	private static final Locale testSecondLocale = Locale.JAPANESE;


	@Resource
	private ModelService modelService;

	@Resource
	private I18NService i18NService;

	@Resource
	private SessionService sessionService;


	@Test
	public void testValueHistoryForNotLoadedAttributeCratedByNew()
	{
		final ProductModel newModel = new ProductModel();
		newModel.setCode(ORIGINAL_CODE_VALUE);
		newModel.setCatalogVersion(prepareCatalogVersion());
		//
		newModel.setEan(ORIGINAL_EAN_VALUE);
		testValueHistoryForNonLocalizedAttribute(newModel, ProductModel.EAN);
	}

	@Test
	public void testValueHistoryForNotLoadedAttributeCratedByModelService()
	{
		final ProductModel newModel = modelService.create(ProductModel.class);
		newModel.setCode(ORIGINAL_CODE_VALUE);
		newModel.setCatalogVersion(prepareCatalogVersion());
		//
		newModel.setEan(ORIGINAL_EAN_VALUE);
		testValueHistoryForNonLocalizedAttribute(newModel, ProductModel.EAN);
	}


	@Test
	public void testLocalizedValueHistoryForNotLoadedAttributeCratedByNew()
	{
		getOrCreateLanguage(testFirstLocale.getLanguage());
		getOrCreateLanguage(testSecondLocale.getLanguage());

		final TitleModel newModel = new TitleModel();
		newModel.setCode(ORIGINAL_CODE_VALUE);
		newModel.setName(ORIGINAL_NAME_VALUE, testFirstLocale);


		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				i18NService.setCurrentLocale(testFirstLocale);
				testValueHistoryForLocalizedAttribute(newModel, testFirstLocale, testSecondLocale);
			}
		});
	}

	@Test
	public void testLocalizedValueHistoryForNotLoadedAttributeCratedByModelService()
	{
		getOrCreateLanguage(testFirstLocale.getLanguage());
		getOrCreateLanguage(testSecondLocale.getLanguage());

		final TitleModel newModel = modelService.create(TitleModel.class);
		newModel.setCode(ORIGINAL_CODE_VALUE);
		newModel.setName(ORIGINAL_NAME_VALUE, testFirstLocale);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				i18NService.setCurrentLocale(testFirstLocale);
				testValueHistoryForLocalizedAttribute(newModel, testFirstLocale, testSecondLocale);
			}
		});
	}

	@Test
	public void testModelAttributeLifeCycleForLocalizedProperty()
	{
		final Locale currentLocale = i18NService.getCurrentLocale();

		final CatalogVersionModel catalogVersion = prepareCatalogVersion();


        final ProductModel newModelChangedViaCtx = modelService.create(ProductModel.class);
        newModelChangedViaCtx.setCode(ORIGINAL_CODE_VALUE + "-ViaCtx");
        newModelChangedViaCtx.setCatalogVersion(catalogVersion);

		final ProductModel newModelChangedViaModel = modelService.create(ProductModel.class);
		newModelChangedViaModel.setCode(ORIGINAL_CODE_VALUE + "-ViaModel");
		newModelChangedViaModel.setCatalogVersion(catalogVersion);

		modelService.saveAll(newModelChangedViaModel, newModelChangedViaCtx);

		checkModelAttributeForLocalizedProperty(newModelChangedViaModel,newModelChangedViaCtx, currentLocale, ProductModel.NAME);

		final PK newModelChangedViaModelPK = newModelChangedViaModel.getPk();
        final PK newModelChangedViaCtxPK = newModelChangedViaCtx.getPk();

		//clean to
		modelService.detachAll();

		final ProductModel loadedModelChangedViaModel = modelService.get(newModelChangedViaModelPK);
        final ProductModel loadedModelChangedViaCtx = modelService.get(newModelChangedViaCtxPK);

		checkModelAttributeForLocalizedProperty(loadedModelChangedViaModel,loadedModelChangedViaCtx, currentLocale, ProductModel.NAME);

		modelService.removeAll(Arrays.asList(loadedModelChangedViaModel,loadedModelChangedViaCtx));

        loadedModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterRemove(true,loadedModelChangedViaModel, currentLocale, ProductModel.NAME, null);

        checkLocalizedFlagsAfterRemove(false,loadedModelChangedViaCtx, currentLocale, ProductModel.NAME, null);

		loadedModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterRemove(true,loadedModelChangedViaModel, currentLocale, ProductModel.NAME, null);

        checkLocalizedFlagsAfterRemove(false,loadedModelChangedViaCtx, currentLocale, ProductModel.NAME, null);
	}


	@Test
	public void testModelAttributeLifeCycleForNonLocalizedProperty()
	{
		final CatalogVersionModel catalogVersion = prepareCatalogVersion();

        final ProductModel newModelChangedViaCtx = modelService.create(ProductModel.class);
        newModelChangedViaCtx.setCode(ORIGINAL_CODE_VALUE + "-ViaCtx");
        newModelChangedViaCtx.setCatalogVersion(catalogVersion);

        final ProductModel newModelChangedViaModel = modelService.create(ProductModel.class);
		newModelChangedViaModel.setCode(ORIGINAL_CODE_VALUE + "-ViaModel");
		newModelChangedViaModel.setCatalogVersion(catalogVersion);

		modelService.saveAll(newModelChangedViaModel,newModelChangedViaCtx);


		checkModelAttributeForNonLocalizedProperty(newModelChangedViaModel,newModelChangedViaCtx, ProductModel.EAN);

		final PK newModelChangedViaModelPK = newModelChangedViaModel.getPk();
        final PK newModelChangedViaCtxPK = newModelChangedViaCtx.getPk();

		//clean to
		modelService.detachAll();

		final ProductModel loadedModelChangedViaModel = modelService.get(newModelChangedViaModelPK);
        final ProductModel loadedModelChangedViaCtx = modelService.get(newModelChangedViaCtxPK);

		checkModelAttributeForNonLocalizedProperty(loadedModelChangedViaModel,loadedModelChangedViaCtx, ProductModel.EAN);

		modelService.removeAll(Arrays.asList(loadedModelChangedViaModel,loadedModelChangedViaCtx));

		loadedModelChangedViaModel.getEan();//load original value
		checkFlagsAfterRemove(true,loadedModelChangedViaModel, ProductModel.EAN, null);

        checkFlagsAfterRemove(false,loadedModelChangedViaCtx, ProductModel.EAN, null);
	}

	private void checkModelAttributeForNonLocalizedProperty(final ProductModel newModelChangedViaModel, final ProductModel newModelChangedViaCtx, final String attribute)
	{
		newModelChangedViaModel.getEan();//load original value
		checkFlagsAfterSave(true,newModelChangedViaModel, attribute, null);

        checkFlagsAfterSave(false,newModelChangedViaCtx, attribute, null);

		newModelChangedViaModel.setEan(EAN_CHANGED_VALUE);
        newModelChangedViaCtx.setEan(EAN_CHANGED_VALUE);

		newModelChangedViaModel.getEan();//load original value
		checkFlagsAfterChange(true,newModelChangedViaModel, attribute, null);

        checkFlagsAfterChange(false,newModelChangedViaCtx, attribute, null);

		modelService.saveAll(newModelChangedViaModel,newModelChangedViaCtx);

		newModelChangedViaModel.getEan();//load original value
		checkFlagsAfterSave(true,newModelChangedViaModel, attribute, EAN_CHANGED_VALUE);

        checkFlagsAfterSave(false,newModelChangedViaCtx, attribute, EAN_CHANGED_VALUE);

		newModelChangedViaModel.setEan(null);
        newModelChangedViaCtx.setEan(null);

		newModelChangedViaModel.getEan();//load original value
		checkFlagsAfterChange(true,newModelChangedViaModel, attribute, EAN_CHANGED_VALUE);

        checkFlagsAfterChange(false,newModelChangedViaCtx, attribute, EAN_CHANGED_VALUE);

		modelService.saveAll(newModelChangedViaModel,newModelChangedViaCtx);

		newModelChangedViaModel.getEan();//load original value
		checkFlagsAfterSave(true,newModelChangedViaModel, attribute, null);

        checkFlagsAfterSave(false,newModelChangedViaCtx, attribute, null);
	}

	private void checkModelAttributeForLocalizedProperty(final ProductModel newModelChangedViaModel,final ProductModel newModelChangedViaCtx, final Locale locale,
			final String attribute)
	{

		newModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterSave(true,newModelChangedViaModel, locale, attribute, null);

        checkLocalizedFlagsAfterSave(false,newModelChangedViaCtx, locale, attribute, null);


		newModelChangedViaModel.setName(NAME_CHANGED_VALUE);
        newModelChangedViaCtx.setName(NAME_CHANGED_VALUE);


		newModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterChange(true,newModelChangedViaModel, locale, attribute, null);

        checkLocalizedFlagsAfterChange(false,newModelChangedViaCtx, locale, attribute, null);

		modelService.saveAll(newModelChangedViaModel,newModelChangedViaCtx);


		newModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterSave(true,newModelChangedViaModel, locale, attribute, NAME_CHANGED_VALUE);

        checkLocalizedFlagsAfterSave(false,newModelChangedViaCtx, locale, attribute, NAME_CHANGED_VALUE);

		newModelChangedViaModel.setName(null);
        newModelChangedViaCtx.setName(null);

		newModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterChange(true,newModelChangedViaModel, locale, attribute, NAME_CHANGED_VALUE);

        checkLocalizedFlagsAfterChange(false,newModelChangedViaCtx, locale, attribute, NAME_CHANGED_VALUE);

		modelService.saveAll(newModelChangedViaModel,newModelChangedViaCtx);

		newModelChangedViaModel.getName();//load original value
		checkLocalizedFlagsAfterSave(true,newModelChangedViaModel, locale, attribute, null);

        checkLocalizedFlagsAfterSave(false,newModelChangedViaCtx, locale, attribute, null);
	}


	private CatalogVersionModel prepareCatalogVersion()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("Catalog-ModelValueHistoryIntegrationTest");

		//Create test online catalog version
		final CatalogVersionModel onlineVersion = modelService.create(CatalogVersionModel.class);
		onlineVersion.setCatalog(catalog);
		onlineVersion.setVersion("Online-ModelValueHistoryIntegrationTest");

		modelService.saveAll(onlineVersion, catalog);

		return onlineVersion;
	}

	private void checkFlagsAfterRemove(final boolean loadedOnDemand, final ProductModel model, final String attribute, final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute));
		Assert.assertFalse(model.getItemModelContext().getDirtyAttributes().contains(attribute));
		Assert.assertFalse(model.getItemModelContext().isDirty(attribute));
        Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand,
                model.getItemModelContext().isLoaded(attribute));
		Assert.assertFalse(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertTrue(model.getItemModelContext().isRemoved());
		Assert.assertFalse(model.getItemModelContext().isUpToDate());
	}

	private void checkLocalizedFlagsAfterRemove(final boolean loadedOnDemand,final ProductModel model, final Locale locale, final String attribute,
			final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute, locale));

		final Set<?> attrs = model.getItemModelContext().getDirtyLocalizedAttributes().get(locale);
		Assert.assertTrue(CollectionUtils.isEmpty(attrs));

		Assert.assertFalse(model.getItemModelContext().isDirty(attribute, locale));
        Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand,
                model.getItemModelContext().isLoaded(attribute, locale));
		Assert.assertFalse(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertTrue(model.getItemModelContext().isRemoved());
		Assert.assertFalse(model.getItemModelContext().isUpToDate());
	}


	private void checkFlagsAfterSave(final boolean loadedOnDemand ,final ProductModel model, final String attribute, final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute));
		Assert.assertFalse(model.getItemModelContext().getDirtyAttributes().contains(attribute));
		Assert.assertFalse(model.getItemModelContext().isDirty(attribute));
        Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand,
                model.getItemModelContext().isLoaded(attribute));
		Assert.assertFalse(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertFalse(model.getItemModelContext().isRemoved());
		Assert.assertTrue(model.getItemModelContext().isUpToDate());
	}

	private void checkLocalizedFlagsAfterSave(final boolean loadedOnDemand ,final ProductModel model, final Locale locale, final String attribute,
			final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute, locale));

		final Set<?> attrs = model.getItemModelContext().getDirtyLocalizedAttributes().get(locale);
		Assert.assertTrue(CollectionUtils.isEmpty(attrs));

		Assert.assertFalse(model.getItemModelContext().isDirty(attribute, locale));
		Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand,
                model.getItemModelContext().isLoaded(attribute, locale));
		Assert.assertFalse(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertFalse(model.getItemModelContext().isRemoved());
		Assert.assertTrue(model.getItemModelContext().isUpToDate());
	}

	private void checkFlagsAfterChange(final boolean loadedOnDemand ,final ProductModel model, final String attribute, final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute));
		Assert.assertTrue(model.getItemModelContext().getDirtyAttributes().contains(attribute));
		Assert.assertTrue(model.getItemModelContext().isDirty(attribute));
		//Assert.assertTrue(model.getItemModelContext().isLoaded(attribute));
        Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand, model.getItemModelContext().isLoaded(attribute));
		Assert.assertTrue(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertFalse(model.getItemModelContext().isRemoved());
		Assert.assertFalse(model.getItemModelContext().isUpToDate());
	}

	private void checkLocalizedFlagsAfterChange(final boolean loadedOnDemand, final ProductModel model, final Locale locale, final String attribute,
			final Object expectedValue)
	{
		Assert.assertEquals(expectedValue, getOriginalValue(model, attribute, locale));


            final Set<?> attrs = model.getItemModelContext().getDirtyLocalizedAttributes().get(locale);
            Assert.assertTrue(attrs != null);
            Assert.assertTrue(attrs.contains(attribute));


		Assert.assertTrue(model.getItemModelContext().isDirty(attribute, locale));
        Assert.assertEquals("Loaded flag should be as the load on demand", loadedOnDemand,model.getItemModelContext().isLoaded(attribute, locale));
		Assert.assertTrue(model.getItemModelContext().isDirty());
		Assert.assertFalse(model.getItemModelContext().isNew());
		Assert.assertFalse(model.getItemModelContext().isRemoved());
		Assert.assertFalse(model.getItemModelContext().isUpToDate());
	}

	private void testValueHistoryForNonLocalizedAttribute(final ProductModel newModel, final String attribute)
	{
		ModelContextUtils.getItemModelContext(newModel);
		try
		{
			getOriginalValue(newModel, "foo");
			Assert.fail("This attribute 'foo' can not be loaded");
		}
		catch (final Exception e)
		{
			//ok
		}
		try
		{
			getOriginalValue(newModel, attribute);
			Assert.fail("This attribute 'code' can not be loaded");
		}
		catch (final Exception e)
		{
			//ok
		}
		try
		{
			getOriginalValue(newModel, attribute, Locale.ENGLISH);
			Assert.fail("This attribute 'code' can not be loaded  - there is no such localized attribute");
		}
		catch (final Exception e)
		{
			//ok
		}

		modelService.attach(newModel);
		modelService.save(newModel);

		try
		{
			getOriginalValue(newModel, "foo");
			Assert.fail("This attribute 'foo' can not be loaded");

		}
		catch (final Exception e)
		{
			//ok
		}
		try
		{
			Assert.assertEquals(ORIGINAL_EAN_VALUE, getOriginalValue(newModel, attribute));
		}
		catch (final Exception e)
		{
			LOG.error(e);
			Assert.fail(e.getMessage());
		}
		try
		{
			getOriginalValue(newModel, attribute, testFirstLocale);
			Assert.fail("This attribute 'code' can not be loaded  - there is no such localized attribute");
		}
		catch (final Exception e)
		{
			//ok
		}
	}


	private void testValueHistoryForLocalizedAttribute(final TitleModel newModel, final Locale existingEntryLocale,
			final Locale nonExistingEntryLocale)
	{
		ModelContextUtils.getItemModelContext(newModel);
		try
		{
			getOriginalValue(newModel, "foo", existingEntryLocale);
			Assert.fail("This attribute 'foo' can not be loaded");
		}
		catch (final Exception e)
		{
			//ok
		}
		try
		{
			getOriginalValue(newModel, TitleModel.NAME, existingEntryLocale);
			Assert.fail("This attribute 'name' can not be loaded");
		}
		catch (final Exception e)
		{
			//ok
		}

		try
		{
			getOriginalValue(newModel, TitleModel.NAME, nonExistingEntryLocale);
			Assert.fail("This attribute 'name' can not be loaded");
		}
		catch (final Exception e)
		{
			//ok
		}
		try
		{
			getOriginalValue(newModel, TitleModel.NAME);
			Assert.fail("This attribute 'name' can not be loaded  - there is no such non-localized attribute");
		}
		catch (final Exception e)
		{
			//ok
		}

		modelService.attach(newModel);
		modelService.save(newModel);

		try
		{
			getOriginalValue(newModel, "foo");
			Assert.fail("This attribute 'foo' can not be loaded");

		}
		catch (final Exception e)
		{
			//ok
		}


		Assert.assertEquals(ORIGINAL_NAME_VALUE, getOriginalValue(newModel, TitleModel.NAME, existingEntryLocale));
		Assert.assertNull(getOriginalValue(newModel, TitleModel.NAME, nonExistingEntryLocale));
		Assert.assertEquals(ORIGINAL_NAME_VALUE, getOriginalValue(newModel, TitleModel.NAME));
	}

	private Object getOriginalValue(final AbstractItemModel model, final String attribute)
	{
		return model.getItemModelContext().getOriginalValue(attribute);
	}

	private Object getOriginalValue(final AbstractItemModel model, final String attribute, final Locale locale)
	{
		return model.getItemModelContext().getOriginalValue(attribute, locale);
	}

}
