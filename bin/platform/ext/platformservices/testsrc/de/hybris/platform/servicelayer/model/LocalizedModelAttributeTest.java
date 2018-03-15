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
package de.hybris.platform.servicelayer.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.util.Config;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

/**
 * Dedicated test for proving, that jalo by pass isn not worse than service layer regarding merging, ordering, dropping duplicate localized attributes.
 */
@IntegrationTest
public class LocalizedModelAttributeTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(LocalizedModelAttributeTest.class);
	private final Locale ROOT_LOCALE = Locale.GERMAN;
	private final Locale MID_LOCALE = new Locale(ROOT_LOCALE.getLanguage(), "au");
	private final Locale OTHER_LEAF_LOCALE = new Locale(ROOT_LOCALE.getLanguage(), "nl", "ax");//other leaf locale with the exact length
	private final Locale LEAF_LOCALE = new Locale(ROOT_LOCALE.getLanguage(), "de", "by"); //bayern
	private Locale currentLocale;
	private boolean cfgSaved = false;
	private String cfgBefore = null;
	@Resource
	private ModelService modelService;
	@Resource
	private I18NService i18NService;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage(ROOT_LOCALE.getLanguage());
		currentLocale = i18NService.getCurrentLocale();
		i18NService.setCurrentLocale(ROOT_LOCALE);
	}

	@After
	public void tearDown() throws Exception
	{
		if (cfgSaved)
		{
			Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, cfgBefore);
			cfgSaved = false;
			cfgBefore = null;
		}
		i18NService.setCurrentLocale(currentLocale);

	}

	private void testAttachedModelAttributesChange()
	{
		final ProductModel productModel = preapreAttachedProduct();


		productModel.setDescription("other desc leaf", OTHER_LEAF_LOCALE);
		productModel.setDescription("foo desc", ROOT_LOCALE);

		productModel.setName("leaf locale", LEAF_LOCALE);
		productModel.setName("foo default locale");
		productModel.setDescription("foo desc mid", MID_LOCALE);
		productModel.setName("mid locale", MID_LOCALE); //last one wins
		productModel.setDescription("foo desc leaf", LEAF_LOCALE); //last one wins

		modelService.save(productModel);


		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, ROOT_LOCALE));

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, OTHER_LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, ROOT_LOCALE));

		Assert.assertEquals("mid locale", productModel.getName());
		Assert.assertEquals("mid locale", productModel.getName(MID_LOCALE));
		Assert.assertEquals("mid locale", productModel.getName(LEAF_LOCALE));
		Assert.assertEquals("mid locale", productModel.getName(ROOT_LOCALE));

		Assert.assertEquals("foo desc leaf", productModel.getDescription());
		Assert.assertEquals("foo desc leaf", productModel.getDescription(MID_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(LEAF_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(OTHER_LEAF_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(ROOT_LOCALE));


		modelService.refresh(productModel);

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, ROOT_LOCALE));

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, OTHER_LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, ROOT_LOCALE));

		Assert.assertEquals("mid locale", productModel.getName());
		Assert.assertEquals("mid locale", productModel.getName(MID_LOCALE));
		Assert.assertEquals("mid locale", productModel.getName(LEAF_LOCALE));
		Assert.assertEquals("mid locale", productModel.getName(ROOT_LOCALE));

		Assert.assertEquals("foo desc leaf", productModel.getDescription());
		Assert.assertEquals("foo desc leaf", productModel.getDescription(MID_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(LEAF_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(OTHER_LEAF_LOCALE));
		Assert.assertEquals("foo desc leaf", productModel.getDescription(ROOT_LOCALE));
	}

	private void testUnAttachedModelAttributesChange(final boolean attachOnDemand)
	{
		final ProductModel productModel = preapreUnAttachedProduct();


		productModel.setDescription("foo desc", ROOT_LOCALE);

		productModel.setName("leaf locale", LEAF_LOCALE); //the most specific  wins
		//productModel.setName("foo default locale");
		productModel.setDescription("foo desc mid", MID_LOCALE);
		productModel.setName("mid locale", MID_LOCALE);

		productModel.setDescription("other foo desc leaf", OTHER_LEAF_LOCALE); //the most specific wins
		productModel.setDescription("foo desc leaf", LEAF_LOCALE); //the most specific wins

		if (attachOnDemand)
		{
			modelService.attach(productModel);
		}
		modelService.save(productModel);

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, MID_LOCALE));
		//TODO
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, ROOT_LOCALE));

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, OTHER_LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, MID_LOCALE));
		//TODO
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, ROOT_LOCALE));

		Assert.assertEquals("leaf locale", productModel.getName());
		Assert.assertEquals("leaf locale", productModel.getName(MID_LOCALE));
		Assert.assertEquals("leaf locale", productModel.getName(LEAF_LOCALE));
		Assert.assertEquals("leaf locale", productModel.getName(ROOT_LOCALE));

		Assert.assertEquals("other foo desc leaf", productModel.getDescription());
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(MID_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(LEAF_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(OTHER_LEAF_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(ROOT_LOCALE));


		modelService.refresh(productModel);

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.NAME, ROOT_LOCALE));

		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, OTHER_LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, LEAF_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, MID_LOCALE));
		Assert.assertFalse(productModel.getItemModelContext().isDirty(ProductModel.DESCRIPTION, ROOT_LOCALE));

		Assert.assertEquals("leaf locale", productModel.getName());
		Assert.assertEquals("leaf locale", productModel.getName(MID_LOCALE));
		Assert.assertEquals("leaf locale", productModel.getName(LEAF_LOCALE));
		Assert.assertEquals("leaf locale", productModel.getName(ROOT_LOCALE));

		Assert.assertEquals("other foo desc leaf", productModel.getDescription());
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(MID_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(OTHER_LEAF_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(LEAF_LOCALE));
		Assert.assertEquals("other foo desc leaf", productModel.getDescription(ROOT_LOCALE));
	}

	@Test
	public void testAttachedModelAttributesChangeInDirectModel()
	{
		forceLegacyMode(false);
		testAttachedModelAttributesChange();

	}

	@Test
	public void testAttachedModelAttributesChangeInLegacyMode()
	{
		forceLegacyMode(true);
		testAttachedModelAttributesChange();
	}

	@Test
	public void testUnAttachedModelAttributesChangeInDirectModel()
	{
		forceLegacyMode(false);
		testUnAttachedModelAttributesChange(false);

	}

	@Test
	public void testUnAttachedModelAttributesChangeInLegacyMode()
	{
		forceLegacyMode(true);
		testUnAttachedModelAttributesChange(false);
	}

	@Test
	public void testAttachedOnDemandModelAttributesChangeInDirectModel()
	{
		forceLegacyMode(false);
		testUnAttachedModelAttributesChange(true);

	}

	@Test
	public void testAttachedOnDemandModelAttributesChangeInLegacyMode()
	{
		forceLegacyMode(true);
		testUnAttachedModelAttributesChange(true);
	}

	///infra

	private void forceLegacyMode(final boolean legacy)
	{
		if (!cfgSaved)
		{
			cfgBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			cfgSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, String.valueOf(legacy));
	}

	private CatalogVersionModel createCatalogVersion()
	{
		final CatalogModel catModel = modelService.create(CatalogModel.class);
		catModel.setId(" test catalog - " + this.getClass().getSimpleName());

		final CatalogVersionModel cvModel = modelService.create(CatalogVersionModel.class);
		cvModel.setVersion(" test cv  -  " + this.getClass().getSimpleName());
		cvModel.setCatalog(catModel);
		modelService.saveAll(catModel, cvModel);

		return cvModel;
	}

	private ProductModel preapreAttachedProduct()
	{

		final ProductModel product = modelService.create(ProductModel.class);
		product.setCatalogVersion(createCatalogVersion());
		product.setCode("foo");
		return product;
	}

	private ProductModel preapreUnAttachedProduct()
	{

		final ProductModel product = new ProductModel();
		product.setCatalogVersion(createCatalogVersion());
		product.setCode("foo");
		return product;
	}

}
