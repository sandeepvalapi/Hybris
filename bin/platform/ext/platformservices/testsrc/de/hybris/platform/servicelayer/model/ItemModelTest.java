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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.enums.IDType;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import de.hybris.platform.servicelayer.constants.ServicelayerConstants;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultI18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.impl.DefaultModelConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemAttributeProvider;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter.ModelAttributeInfo;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategyLeanImpl;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StopWatch;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.FallbackConfig;
import de.hybris.platform.util.config.FastHashMapConfig;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
abstract public class ItemModelTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(ItemModelTest.class);

	@Resource
	protected ModelService modelService;
	@Resource
	protected I18NService i18nService;
	@Resource
	protected CommonI18NService commonI18NService;
	@Resource
	protected FlexibleSearchService flexibleSearchService;
	@Resource(name = "testImportCsvUtil")
	protected TestImportCsvUtil importCsvUtil;
	@Resource
	protected ConverterRegistry converterRegistry;
	@Resource
	protected SourceTransformer sourceTransformer;
	@Resource
	protected SelfHealingService selfHealingService;
	protected Product defaultProduct;
	protected String beforeCfg;
	protected String persistenceModeBefore;
	protected boolean persistenceModeSaved;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");
		createCoreData();
		createDefaultCatalog();
		createDefaultUsers();
		defaultProduct = (Product) ProductManager.getInstance().getProductsByCode("testProduct0").iterator().next();

		reloadConvertersBefore();
	}

	private void reloadConvertersBefore()
	{
		beforeCfg = Config.getParameter(ServicelayerConstants.PARAM_PREFETCH);

		Config.setParameter(ServicelayerConstants.PARAM_PREFETCH, getPrefetchMode());
		for (final Map.Entry<String, Class<? extends ItemModel>> entry : getModelConvertersToReload().entrySet())
		{
			reloadConverter(entry.getKey(), entry.getValue());
		}
	}

	@After
	public void tearDown()
	{
		Config.setParameter(ServicelayerConstants.PARAM_PREFETCH, beforeCfg);
		for (final Map.Entry<String, Class<? extends ItemModel>> entry : getModelConvertersToReload().entrySet())
		{
			reloadConverter(entry.getKey(), entry.getValue());
		}

		if (persistenceModeSaved)
		{
			Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, persistenceModeBefore);
			persistenceModeSaved = false;
			persistenceModeBefore = null;
		}
	}

	protected void enableDirectMode()
	{
		if (!persistenceModeSaved)
		{
			persistenceModeBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			persistenceModeSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "false");
	}

	protected void forceLegacyMode()
	{
		if (!persistenceModeSaved)
		{
			persistenceModeBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			persistenceModeSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "true");
	}

	abstract protected String getPrefetchMode();

	abstract protected Map<String, Class<? extends ItemModel>> getModelConvertersToReload();

	@Test
	public void testSerializableStrategies() throws IOException, ClassNotFoundException, JaloSecurityException
	{
		// check default settings
		final SerializationStrategy defaultStrategy = ((DefaultModelConverterRegistry) ((DefaultModelService) modelService)
				.getConverterRegistry()).getDefaulItemModelSerializationStrategy();
		assertNotNull(defaultStrategy);
		assertTrue(defaultStrategy instanceof SerializationStrategyLeanImpl);

		// Set up something to be serialized
		final ProductModel originalModel = modelService.get(defaultProduct);

		// Same tests as testSerializable but repeated once for lean strategy and once for default strategy
		// and we then compare the buffer sizes needed for serializing the object

		// Serialize it with lean strategy and then default strategy and remember the buffer sizes
		final ItemModelConverter conv = (ItemModelConverter) ((DefaultModelService) modelService).getConverterRegistry()
				.getModelConverterByModelType(originalModel.getClass());
		final SerializationStrategyLeanImpl leanStrategy = new SerializationStrategyLeanImpl();
		setSerializationStrategy(originalModel, leanStrategy);
		
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		try( ObjectOutputStream os = new ObjectOutputStream(buffer))
		{
			os.writeObject(originalModel);
		}

		// Read it back in and assert it is equal to the original
		final ProductModel modelReadInSameContext;
		try(ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())))
		{
			modelReadInSameContext = (ProductModel) is.readObject();
		}

		// try to read model even without tenant being active
		final Tenant t = Registry.getCurrentTenantNoFallback();
		final ProductModel modelReadWithoutTenant;
		try
		{
			Registry.unsetCurrentTenant();
			try(ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())))
			{
				modelReadWithoutTenant = (ProductModel) is.readObject();
			}
		}
		finally
		{
			Registry.setCurrentTenant(t);
		}

		// now read inside different tenant
		final ProductModel modelReadWhileDifferentTenantIsActive;
		try
		{
			Registry.activateMasterTenant();
			try(ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())))
			{
				modelReadWhileDifferentTenantIsActive = (ProductModel) is.readObject();
			}
		}
		finally
		{
			Registry.setCurrentTenant(t);
		}

		
		// test serializable after changes
		originalModel.setEan("foo bar");
		assertTrue(conv.isModified(originalModel));
		assertSame(leanStrategy, getSerializationStrategy(originalModel));

		try( ObjectOutputStream os = new ObjectOutputStream(new ByteArrayOutputStream()))
		{
			os.writeObject(originalModel);
			fail("dirty models must not be serializable");
		}
		catch (final IllegalStateException e)
		{
			// expected
		}

		assertSame(originalModel, modelReadInSameContext);
		assertEquals(originalModel, modelReadInSameContext);
		
		assertSame(originalModel, modelReadWithoutTenant);
		assertEquals(originalModel, modelReadWithoutTenant);
		
		assertSame(originalModel, modelReadWhileDifferentTenantIsActive);
		assertEquals(originalModel, modelReadWhileDifferentTenantIsActive);
	}
	
	@Test
	public void testNotSerializableWhenNewChangedOrRemoved() throws IOException, ClassNotFoundException, JaloSecurityException 
	{
		final TitleModel model = modelService.create(TitleModel.class);
		model.setCode("SomeTitle");

		//serialize new one -> exception
		try(ObjectOutputStream outputStream = new ObjectOutputStream(new ByteArrayOutputStream()))
		{
			outputStream.writeObject(model);
			fail("new model should cause exception on serialization but didnt");
		}
		catch (final IllegalStateException e)
		{
			// fine
		}
		
		// serialize existing one -> fine
		modelService.save(model);

		try(ObjectOutputStream outputStream = new ObjectOutputStream(new ByteArrayOutputStream()))
		{
			outputStream.writeObject(model); //OK
		}

		// serialize modified one -> exception
		model.setName("SomeName");
		
		assertTrue(model.getItemModelContext().isDirty());
		try(ObjectOutputStream outputStream = new ObjectOutputStream(new ByteArrayOutputStream()))
		{
			outputStream.writeObject(model); //OK
			fail("dirty model should cause exception on serialization but didnt");
		}
		catch (final IllegalStateException e)
		{
			// fine
		}
		
		
		// serialize existing one -> fine
		modelService.save(model);
		modelService.remove(model);

		
		assertTrue(model.getItemModelContext().isRemoved());
		try(ObjectOutputStream outputStream = new ObjectOutputStream(new ByteArrayOutputStream()))
		{
			outputStream.writeObject(model); //OK
			fail("removed model should cause exception on serialization but didnt");
		}
		catch (final IllegalStateException e)
		{
			// fine
		}
	}

	@Test
	abstract public void testLoadingNormalAttributeUsingDirectPersistence();

	@Test
	abstract public void testLoadingNormalAttributeUsingOldPersistence();

	@Test
	public void testLoadingReferenceAttribute()
	{
		final Unit expectedUnit = defaultProduct.getUnit();
		final ProductModel model = modelService.get(defaultProduct);

		final String field_unit = "unit";
		assertTrue(ModelUtils.existsField(model.getClass(), field_unit));
		assertTrue(ModelUtils.existsMethod(model.getClass(), "getUnit"));
		assertTrue(ModelUtils.existsMethod(model.getClass(), "setUnit"));

		assertNotNull(getLoadedValue(model, field_unit)); // load original
		assertNull(ModelUtils.getFieldValue(model, field_unit));

		final UnitModel unit = model.getUnit();
		assertEquals(unit, getLoadedValue(model, field_unit));
		assertEquals(expectedUnit.getCode(), unit.getCode());

		assertEquals(unit, getLoadedValue(model, field_unit));
		assertEquals(unit, ModelUtils.getFieldValue(model, field_unit));

		modelService.save(model);

		assertNotNull(getLoadedValue(model, field_unit));
		assertNull(ModelUtils.getFieldValue(model, field_unit));
		assertEquals(unit, model.getUnit());
		assertEquals(unit, getLoadedValue(model, field_unit));
		assertEquals(unit, ModelUtils.getFieldValue(model, field_unit));

		model.setUnit(null);

		assertEquals(unit, getLoadedValue(model, field_unit));
		assertNull(ModelUtils.getFieldValue(model, field_unit));
		assertNull(model.getUnit());
		assertNull(ModelUtils.getFieldValue(model, field_unit));

		assertNotNull(defaultProduct.getUnit());

		modelService.refresh(model);

		assertNotNull(getLoadedValue(model, field_unit));
		assertNull(ModelUtils.getFieldValue(model, field_unit));
		assertEquals(unit, model.getUnit());
		assertEquals(unit, getLoadedValue(model, field_unit));
		assertEquals(unit, ModelUtils.getFieldValue(model, field_unit));

		model.setUnit(null);

		assertEquals(unit, getLoadedValue(model, field_unit));
		assertNull(ModelUtils.getFieldValue(model, field_unit));
		assertNull(model.getUnit());
		assertNull(ModelUtils.getFieldValue(model, field_unit));

		assertNotNull(defaultProduct.getUnit());

		modelService.save(model);

		assertNull(getLoadedValue(model, field_unit));
		assertNull(ModelUtils.getFieldValue(model, field_unit));
		assertNull(model.getUnit());

		assertNull(defaultProduct.getUnit());
	}

	@Test
	public void testChangingUnloadedReference()
	{
		final Unit currentUnit = defaultProduct.getUnit();
		final ProductModel model = modelService.get(defaultProduct);
		assertEquals(currentUnit, defaultProduct.getUnit());

		// make sure it's not loaded yet
		assertNotNull(getLoadedValue(model, "unit"));
		assertNull(ModelUtils.getFieldValue(model, "unit"));

		// try setting it to zero
		model.setUnit(null);
		modelService.save(model);
		assertNull(defaultProduct.getUnit());
		assertNull(model.getUnit());

		// try setting to another unite
		final UnitModel newUnit = new UnitModel();
		newUnit.setCode("foo");
		newUnit.setUnitType("bar");

		modelService.save(newUnit);
		modelService.detach(model);

		final ProductModel model2 = modelService.get(defaultProduct);
		assertNotSame(model, model2);
		model2.setUnit(newUnit);
		modelService.save(model2);

		assertEquals(newUnit, model2.getUnit());
		assertEquals(modelService.getSource(newUnit), defaultProduct.getUnit());
	}

	@Test
	public void testReloadOfJaloChangedAttribute() throws ConsistencyCheckException
	{
		final ItemModelConverter converter = createPrefetchNoneModelConverter();

		// prepare new item
		final TitleModel t = (TitleModel) converter.create("Title");
		t.setCode("T" + System.nanoTime());
		converter.save(t, null);
		final Title jaloT = modelService.getSource(t);

		// load hidden jalo change from unloaded model attribute
		final TitleModel t2 = (TitleModel) converter.load(jaloT);
		assertNotNull(getLoadedValue(t2, TitleModel.CODE));
		t2.setName("blah");
		jaloT.setCode("TTT"); // this is a hidden Jalo change
		converter.save(t2, null);
		assertEquals("TTT", t2.getCode()); // check if CODE was (re)loaded correctly

		// load hidden jalo change from unloaded model attribute
		final TitleModel t3 = (TitleModel) converter.load(jaloT);
		assertEquals("TTT", t3.getCode());
		assertEquals("TTT", getLoadedValue(t3, TitleModel.CODE));
		t3.setName("blubb");
		jaloT.setCode("XXX"); // this is a hidden Jalo change too
		converter.save(t3, null);
		assertEquals("XXX", t3.getCode()); // check if CODE was reloaded correctly
	}

	@Test
	public void testSetLocalizedAttributeToNull() throws ConsistencyCheckException
	{
		final Locale de = new Locale("de", "foo");

		final LanguageModel langDe = commonI18NService.getLanguage("de");

		i18nService.setCurrentLocale(de);

		final TitleModel tm = modelService.create(TitleModel.class);

		modelService.setAttributeValue(tm, TitleModel.CODE, "code");
		modelService.setAttributeValue(tm, TitleModel.NAME, Collections.singletonMap(langDe, null));

		modelService.save(tm);

		modelService.refresh(tm);

		assertEquals("should here be just a plain null ", null, tm.getName(de));
	}

	@Test
	abstract public void testLoadingPrimitiveAttributeUsingDirectPersistence();

	abstract public void testLoadingPrimitiveAttributeUsingOldPersistence();

	@Test
	public void testLoadingOnlyInitialAttribute() throws Exception
	{
		final ProductModel model = modelService.get(defaultProduct);

		assertTrue(ModelUtils.existsMethod(model.getClass(), "setOwner"));
		assertTrue(ModelUtils.existsMethod(model.getClass(), "getOwner"));
		final String field_owner = "owner";
		assertTrue(ModelUtils.existsField(model.getClass(), field_owner));

		assertNotNull(getLoadedValue(model, field_owner));
		assertNull(ModelUtils.getFieldValue(model, field_owner));

		final ItemModel owner = model.getOwner();
		assertNotNull(owner);

		assertNotNull(getLoadedValue(model, field_owner));
		assertNotNull(ModelUtils.getFieldValue(model, field_owner));

		modelService.save(model);
	}

	@Test
	public void testLocalizedLoading()
	{
		final Locale de = new Locale("de", "foo");
		final Locale en = new Locale("en");

		i18nService.setCurrentLocale(de);

		final ProductModel model = modelService.get(defaultProduct);
		assertEquals("testProduct0de", model.getName()); // loaded de value by default
		assertEquals("testProduct0de", model.getName(de));
		assertEquals("testProduct0en", model.getName(en));

		i18nService.setCurrentLocale(en);

		assertSame(model, modelService.get(defaultProduct));
		assertEquals("testProduct0en", model.getName()); // test correct switch to current language en
		assertEquals("testProduct0en", model.getName(en));
		assertEquals("testProduct0de", model.getName(de));

		i18nService.setCurrentLocale(de);

		assertEquals("testProduct0de", model.getName()); // test switch back again

		// try to change name[de]
		model.setName("test", en);
		model.setName("foo"); // second modification
		modelService.save(model); // store
		assertEquals("test", model.getName(en));

		// here check

		modelService.refresh(model);
		assertEquals("foo", model.getName());

		i18nService.setCurrentLocale(en);
		assertEquals("test", model.getName(en)); // loaded en value by default
		assertEquals("test", model.getName()); // test correct switch to current language en
		assertEquals("foo", model.getName(de));

		// test chaning both values at once

		model.setName("foo");// en
		i18nService.setCurrentLocale(de);
		model.setName("bar"); // de

		modelService.save(model);
		modelService.refresh(model);
		assertEquals("bar", model.getName());
		assertEquals("bar", model.getName(de));
		assertEquals("foo", model.getName(en));

		i18nService.setCurrentLocale(en);
		assertEquals("foo", model.getName());
		assertEquals("foo", model.getName(en));
		assertEquals("bar", model.getName(de));

		// test direct setters
		model.setName("name_de", de);
		model.setName("name_en", en);

		assertEquals("name_en", model.getName());// since current locale is en
		assertEquals("name_en", model.getName(en));
		assertEquals("name_de", model.getName(de));

		try
		{
			model.setName("wrong", new Locale("xxx", "yyy", "zzz"));
			fail("expected IllegalArgumentException using wrong locale");
		}
		catch (final IllegalArgumentException e)
		{
			// fine
		}

		modelService.save(model);

		modelService.refresh(model);

		assertEquals("name_en", model.getName());// since current locale is en
		assertEquals("name_en", model.getName(en));
		assertEquals("name_de", model.getName(de));
	}

	@Test
	public void testLoadingSettingComposedAttribute()
	{
		Object defBefore = null;
		AttributeDescriptor ad = null;
		try
		{
			final LanguageModel defaultLang = new LanguageModel();
			defaultLang.setIsocode("def");
			defaultLang.setActive(Boolean.TRUE);
			modelService.save(defaultLang);

			final ComposedType t = TypeManager.getInstance().getComposedType(Language.class);
			ad = t.getAttributeDescriptorIncludingPrivate(Language.FALLBACKLANGUAGES);
			defBefore = ad.getDefaultValue();
			ad.setDefaultValue(Collections.singletonList(modelService.getSource(defaultLang)));

			// test if we're able to overwrite default value using NULL
			final LanguageModel testModel = new LanguageModel();
			testModel.setIsocode("test");
			testModel.setActive(Boolean.TRUE);
			testModel.setFallbackLanguages(null);
			modelService.save(testModel);
			assertEquals(Collections.EMPTY_LIST, testModel.getFallbackLanguages());

			final LanguageModel testModel2 = new LanguageModel();
			testModel2.setIsocode("test2");
			testModel2.setActive(Boolean.TRUE);
			modelService.save(testModel2);
			assertEquals(Collections.singletonList(defaultLang), testModel2.getFallbackLanguages());
		}
		finally
		{
			if (ad != null)
			{
				ad.setDefaultValue(defBefore);
			}
		}
	}

	@Test
	public void testCreateByModelClass()
	{
		{
			final ProductModel model = modelService.create(ProductModel.class);
			assertNotNull(model);
			assertTrue(modelService.isNew(model));
			assertNull(model.getPk());
			assertEquals("Product", model.getItemtype());
		}
		{
			final VariantProductModel model = modelService.create(VariantProductModel.class);
			assertNotNull(model);
			assertTrue(modelService.isNew(model));
			assertNull(model.getPk());
			assertEquals("VariantProduct", model.getItemtype());
		}
	}

	@Test
	public void testCreateByTypeCode()
	{
		final ProductModel model = modelService.create("Product");
		assertNotNull(model);
		assertTrue(modelService.isNew(model));
		assertNull(model.getPk());
		assertEquals("Product", model.getItemtype());
	}

	protected <T> T getLoadedValue(final AbstractItemModel model, final String attribute)
	{
		try
		{
			return ModelContextUtils.getItemModelContext(model).getOriginalValue(attribute);
		}
		catch (final IllegalStateException e)
		{
			return null;
		}
	}

	@Test
	public void testLoadByPK()
	{
		final ProductModel model = modelService.get(defaultProduct.getPK());
		assertNotNull(model);
		assertEquals(defaultProduct.getPK(), model.getPk());
	}

	@Test
	public void testTypeWithoutModel() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		ComposedType myType = null;
		try
		{
			final ComposedType unitT = TypeManager.getInstance().getComposedType(Unit.class);
			myType = TypeManager.getInstance().createComposedType(unitT, "MyUnit");

			final UnitModel model = modelService.create("MyUnit");
			assertNotNull(model);
			assertTrue(modelService.isNew(model));
			assertNull(model.getPk());
			assertEquals("MyUnit", model.getItemtype());

			model.setCode("foo");
			model.setUnitType("bar");
			model.setConversion(Double.valueOf(1));

			modelService.save(model);

			final Unit u = modelService.getSource(model);
			assertNotNull(u);
			assertEquals(myType, u.getComposedType());
			assertEquals("foo", u.getCode());
			assertEquals("bar", u.getUnitType());
			assertEquals(1d, u.getConversionFactor(), 0.00000001);

			final UnitModel model2 = modelService.get(u);

			assertEquals(myType.getCode(), model2.getItemtype());
		}
		finally
		{
			if (myType != null)
			{
				try
				{
					myType.remove();
				}
				catch (final Exception e)
				{
					// can't help it
				}
			}
		}
	}

	@Test
	public void testCreateAttachLocalization()
	{
		// create empty model
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("testProduct");

		final CatalogVersion cvItem = CatalogManager.getInstance().getCatalog("testCatalog").getCatalogVersion("Online");
		final CatalogVersionModel cv = (CatalogVersionModel) modelService.toModelLayer(cvItem);
		product.setCatalogVersion(cv);

		// set name in uk locale - no need to attach here
		product.setName("foo", Locale.UK);

		// for using localized attributes on a model created with "new" we have to attach mode first
		modelService.attach(product);

		// set name in uk locale again - make
		product.setName("ukName", Locale.UK); // must be exactly 'ukName' because YSimpleValidationInterceptor checks this


		// set name in german locale (using current locale mechanism)
		i18nService.setCurrentLocale(Locale.GERMAN);
		product.setName("germanName");

		// persist product
		modelService.save(product);

		// test jalo working correctly
		final Product jaloProduct = modelService.getSource(product);
		assertEquals("testProduct", jaloProduct.getCode());
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(getOrCreateLanguage("en"));
		assertEquals("ukName", jaloProduct.getName(ctx));

		final ProductModel saved = modelService.get(jaloProduct);

		assertNotNull(saved);
		assertEquals("testProduct", saved.getCode());
		assertEquals("ukName", saved.getName(Locale.ENGLISH));
		assertEquals("ukName", saved.getName(Locale.UK));
		assertEquals("germanName", saved.getName());
		assertEquals("germanName", saved.getName(Locale.GERMAN));

		assertEquals(saved, product);
	}

	// see PLA-7394
	@Test
	public void testDetachedLocalization()
	{
		final Language de = getOrCreateLanguage("de");
		final Language en = getOrCreateLanguage("en");

		final TitleModel titleModel = new TitleModel();
		titleModel.setCode("tLocTest");

		assertNull(titleModel.getName(de.getLocale()));
		assertNull(titleModel.getName(en.getLocale()));

		titleModel.setName("de name", de.getLocale());
		assertEquals("de name", titleModel.getName(de.getLocale()));

		titleModel.setName("en name", en.getLocale());
		assertEquals("en name", titleModel.getName(en.getLocale()));

		titleModel.setName("de_DE name", Locale.GERMANY);
		assertEquals("de name", titleModel.getName(de.getLocale()));
		assertEquals("de_DE name", titleModel.getName(Locale.GERMANY));

		titleModel.setName("de_DE_foo name", new Locale("de", "DE", "foo"));
		assertEquals("de name", titleModel.getName(de.getLocale()));
		assertEquals("de_DE name", titleModel.getName(Locale.GERMANY));
		assertEquals("de_DE_foo name", titleModel.getName(new Locale("de", "DE", "foo")));

		modelService.save(titleModel);

		// assuring the most recent locale value has been persisted
		assureModelValueForTheMostRecentLocalePersisted(de, en, titleModel, "de_DE_foo name", "en name");
		assureItemValueForTheMostRecentLocalePersisted(de, en, titleModel, "de_DE_foo name", "en name");


		// assuring the most recent locale value has been persisted
		titleModel.setName("the most specific", new Locale("de", "DE", "foo"));
		titleModel.setName("a bit specific", Locale.GERMANY);
		titleModel.setName("zee general", de.getLocale());

		titleModel.setName("en name foo", Locale.US);

		modelService.save(titleModel);

		assureModelValueForTheMostRecentLocalePersisted(de, en, titleModel, "zee general", "en name foo");
		assureItemValueForTheMostRecentLocalePersisted(de, en, titleModel, "zee general", "en name foo");
	}

	private void assureItemValueForTheMostRecentLocalePersisted(final Language de, final Language en, final TitleModel titleModel,
			final String expectedDE, final String expectedEN)
	{
		final Title titleItem = modelService.getSource(titleModel);
		// check item too
		assertEquals("tLocTest", titleItem.getCode());
		final SessionContext ctx = jaloSession.createLocalSessionContext();

		try
		{
			ctx.setLanguage(de);
			assertEquals(expectedDE, titleItem.getName(ctx));
			ctx.setLanguage(en);
			assertEquals(expectedEN, titleItem.getName(ctx));
		}
		finally
		{
			jaloSession.removeLocalSessionContext();
		}
	}

	private void assureModelValueForTheMostRecentLocalePersisted(final Language de, final Language en, final TitleModel titleModel,
			final String expectedDE, final String expectedEN)
	{
		// check reloaded name attribute
		assertEquals(expectedDE, titleModel.getName(de.getLocale()));
		assertEquals(expectedDE, titleModel.getName(Locale.GERMANY));
		assertEquals(expectedDE, titleModel.getName(new Locale("de", "DE", "foo")));
		assertEquals(expectedEN, titleModel.getName(en.getLocale()));
	}

	@Test
	public void testUpToDate()
	{
		final UnitModel u1 = new UnitModel();
		u1.setCode("foo1");
		u1.setUnitType("bar");

		final UnitModel u2 = new UnitModel();
		u2.setCode("foo2");
		u2.setUnitType("bar");

		assertFalse(modelService.isUpToDate(u1));
		assertFalse(modelService.isUpToDate(u2));

		modelService.saveAll(Arrays.asList(u1, u2));

		assertNull(u1.getName());
		assertNull(u2.getName());

		assertTrue(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u2));

		final Unit u1Item = modelService.getSource(u1);
		final Unit u2Item = modelService.getSource(u2);

		assertTrue(u1Item.isAlive());
		assertTrue(u2Item.isAlive());

		final long u1Version = u1Item.getPersistenceVersion();
		final long u2Version = u2Item.getPersistenceVersion();

		modelService.detach(u1);

		assertTrue(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u2));

		final UnitModel u1a = modelService.get(u1.getPk());

		assertNotSame(u1, u1a);

		assertTrue(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u1a));
		assertTrue(modelService.isUpToDate(u2));

		// PLA-9469 saving localized properties does not always trigger persistens version update!!!!!!!!
		// u1a.setName("foo");
		u1a.setUnitType("barbara");

		// test change from service layer
		modelService.save(u1a);

		assertEquals(u1Version + 1, u1Item.getPersistenceVersion()); // maybe just check u1Version < current ???

		assertFalse(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u1a));
		assertTrue(modelService.isUpToDate(u2));

		assertEquals("barbara", u1a.getUnitType());
		assertNull(u1.getName());

		// test change outside

		modelService.detach(u2);

		final UnitModel u2a = modelService.get(u2.getPk());

		assertNotSame(u2, u2a);

		assertFalse(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u1a));
		assertTrue(modelService.isUpToDate(u2));
		assertTrue(modelService.isUpToDate(u2a));

		// PLA-9469 saving localized properties does not always trigger persistens version update!!!!!!!!
		// u2Item.setName("bar");
		u2Item.setUnitType("bar2");

		assertEquals(u2Version + 1, u2Item.getPersistenceVersion()); // maybe just check u2Version < current ???

		assertEquals("bar2", u2Item.getUnitType());
		assertNull(u2.getName());
		assertNull(u2a.getName());

		assertFalse(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u1a));
		assertFalse(modelService.isUpToDate(u2));
		assertFalse(modelService.isUpToDate(u2a));

		modelService.refresh(u2a);

		assertFalse(modelService.isUpToDate(u1));
		assertTrue(modelService.isUpToDate(u1a));
		assertFalse(modelService.isUpToDate(u2));
		assertTrue(modelService.isUpToDate(u2a));

		modelService.refresh(u2);
		assertTrue(modelService.isUpToDate(u2));
		assertTrue(modelService.isUpToDate(u2a));
	}

	@Test
	public void testDetachViaSource()
	{
		final UnitModel uModel = new UnitModel();
		uModel.setCode("xxx");
		uModel.setUnitType("yyy");

		modelService.save(uModel);

		assertSame(uModel, modelService.get(uModel.getPk()));

		final Unit uItem = jaloSession.getItem(uModel.getPk());

		assertSame(uModel, modelService.get(uItem));

		// detach model
		modelService.detach(uModel);

		final UnitModel uModel2 = modelService.get(uItem.getPK());

		assertNotSame(uModel, uModel2);

		// detach via item
		modelService.detach(uItem);

		final UnitModel uModel3 = modelService.get(uItem.getPK());

		assertNotSame(uModel, uModel3);
		assertNotSame(uModel2, uModel3);

		// detach via PK
		modelService.detach(uItem.getPK());

		final UnitModel uModel4 = modelService.get(uItem.getPK());

		assertNotSame(uModel, uModel4);
		assertNotSame(uModel2, uModel4);
		assertNotSame(uModel3, uModel4);

		// detach via PK (wrong method)
		modelService.detach((Object) uItem.getPK());

		final UnitModel uModel5 = modelService.get(uItem.getPK());

		assertNotSame(uModel, uModel5);
		assertNotSame(uModel2, uModel5);
		assertNotSame(uModel3, uModel5);
		assertNotSame(uModel4, uModel5);
	}

	// we need that to simulate core returning modifiable collections
	private static class MyCategory extends Category
	{
		@Override
		@SLDSafe
		public List<Product> getProducts(final SessionContext ctx)
		{
			return new ArrayList<Product>(super.getProducts(ctx));
		}
	}

	@Test
	public void testModifieableCollections()
	{
		final Category _c = CategoryManager.getInstance().createCategory("cat");
		final Category c = new MyCategory();
		c.setImplementation(_c.getImplementation());

		final Product p1 = ProductManager.getInstance().createProduct("p1");
		final Product p2 = ProductManager.getInstance().createProduct("p2");
		final Product p3 = ProductManager.getInstance().createProduct("p3");

		c.setProducts(Arrays.asList(p1, p2));

		final List<Product> productsJalo = c.getProducts();
		assertEquals(Arrays.asList(p1, p2), productsJalo);
		assertEquals(ArrayList.class, productsJalo.getClass());

		assertEquals(Collections.singletonList(c), CategoryManager.getInstance().getSupercategories(p1));
		assertEquals(Collections.singletonList(c), CategoryManager.getInstance().getSupercategories(p2));
		assertEquals(Collections.EMPTY_LIST, CategoryManager.getInstance().getSupercategories(p3));

		final ProductModel p1Model = modelService.get(p1);
		final Collection<CategoryModel> p1SuperCategories = p1Model.getSupercategories();

		final CategoryModel cModel = modelService.get(c);
		assertEquals(Collections.singletonList(cModel), p1SuperCategories);

		final ProductModel p2Model = modelService.get(p2);

		final List<ProductModel> products = cModel.getProducts();
		assertEquals(Arrays.asList(p1Model, p2Model), products);

		final ProductModel p3Model = modelService.get(p3);

		try
		{
			products.add(p3Model);
			// may be there is some hidden magic here - let's test

			assertEquals(Arrays.asList(p1Model, p2Model, p3Model), products);

			modelService.save(cModel);

			final List<ProductModel> productsNew = cModel.getProducts();

			// check saved model
			assertEquals(Arrays.asList(p1Model, p2Model, p3Model), productsNew);
			// check jalo as well -> can't trust model ;)
			assertEquals(Arrays.asList(p1, p2, p3), c.getProducts());
		}
		catch (final Exception e)
		{
			// a) fine -> not allowed to modify
		}
	}

	@Test
	public void testLanguageFallback2() throws ConsistencyCheckException
	{
		// See HOR-123, HOR-150: Implementing language fallback support for service layer

		// Get a default product model
		final ProductModel product = modelService.get(defaultProduct);

		// With default locale, we should get a name with and without a locale parameter
		assertNotNull(product.getName(i18nService.getCurrentLocale()));
		assertEquals(product.getName(), product.getName(i18nService.getCurrentLocale()));

		// Make a new language and set its fallback to the default language
		final Language defLang = jaloSession.getSessionContext().getLanguage();
		final Language newLang1 = C2LManager.getInstance().createLanguage("newLang1");
		newLang1.setFallbackLanguages(Collections.singletonList(defLang));

		// With fallback disabled, we should get nothing for the new language's locale
		assertNull(product.getName(newLang1.getLocale()));

		// With fall back enabled, we should get values for the new language (as it falls back to default)
		i18nService.setLocalizationFallbackEnabled(true);
		assertNotNull(product.getName(newLang1.getLocale()));
		assertEquals(product.getName(), product.getName(newLang1.getLocale()));

		// To be sure current locale is not intefering, repeat the last tests with another current locale
		i18nService.setCurrentLocale(newLang1.getLocale());
		assertNotNull(product.getName(newLang1.getLocale()));
		assertEquals(product.getName(), product.getName(newLang1.getLocale()));

		// Set the current locale back
		i18nService.setCurrentLocale(defLang.getLocale());
		// Turn fallback back off
		i18nService.setLocalizationFallbackEnabled(false);

		// With fallback disabled, we should again get nothing for the new language's locale ..
		assertNull(product.getName(newLang1.getLocale()));
		// .. but should do for the default locale
		assertNotNull(product.getName());

		// Try with two levels of fallback
		final Language newLang2 = C2LManager.getInstance().createLanguage("newLang2");
		newLang2.setFallbackLanguages(newLang1, defLang);

		// Without fallback enabled, we should get nothing for the new language's locale
		assertNull(product.getName(newLang2.getLocale()));

		// Enable fallback
		i18nService.setLocalizationFallbackEnabled(true);
		// With fall back we should get values for the new language (as it falls back to newLang1 then to default)
		assertNotNull(product.getName(newLang2.getLocale()));
		assertEquals(product.getName(), product.getName(newLang2.getLocale()));

		// With a detached model, the getters and setters relying on the (non existent) localeProvider
		// should throw an IllegalStateException
		final ProductModel detachedModel = new ProductModel();
		try
		{
			detachedModel.setName("myName");
			fail("Should have thrown IllegalArgumentException");
		}
		catch (final IllegalStateException e)
		{
			// This exception should be thrown for detached models
		}
		try
		{
			detachedModel.getName();
			fail("Should have thrown IllegalArgumentException");
		}
		catch (final IllegalStateException e)
		{
			// This exception should be thrown for detached models
		}
		// But the getters and setters with locale specified should not throw exceptions
		detachedModel.setName("newName", defLang.getLocale());
		assertEquals("newName", detachedModel.getName(defLang.getLocale()));

		// Localization examples with currency
		i18nService.setCurrentLocale(Locale.ENGLISH);
		assertEquals(i18nService.getCurrency("CHF").getName(), "Swiss franc");
		i18nService.setCurrentLocale(Locale.GERMAN);
		assertEquals(i18nService.getCurrency("CHF").getName(), "Schweizer Franken");
		assertEquals(i18nService.getCurrency("CHF").getName(Locale.ENGLISH), "Swiss franc");

	}

	@Test
	public void testLanguageFallback()
	{
		final SessionContext localCtx = jaloSession.createLocalSessionContext();
		try
		{
			localCtx.removeAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED);
			// currently we may just test that language fallback is ignored !!!

			final Language de = getOrCreateLanguage("de");
			final Language en = getOrCreateLanguage("en");
			final Language de_DE = getOrCreateLanguage("de_DE");
			de_DE.setFallbackLanguages(de);

			final Unit u = ProductManager.getInstance().createUnit("foo", "bar");

			final SessionContext ctx = jaloSession.createSessionContext();

			ctx.setLanguage(en);
			u.setName(ctx, "en name");
			ctx.setLanguage(de);
			u.setName(ctx, "de name");

			ctx.setLanguage(de_DE);
			assertNull(u.getName(ctx));
			ctx.setLanguage(de);
			assertEquals("de name", u.getName(ctx));
			ctx.setLanguage(en);
			assertEquals("en name", u.getName(ctx));

			// load without fallback

			assertNull(localCtx.getAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED));

			final UnitModel uModel1 = modelService.get(u);

			assertNull(uModel1.getName(de_DE.getLocale()));
			assertEquals("de name", uModel1.getName(de.getLocale()));
			assertEquals("en name", uModel1.getName(en.getLocale()));

			modelService.detach(uModel1);

			// load with language fallback enabled
			localCtx.setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
			final UnitModel uModel2 = modelService.get(u);

			assertNotSame(uModel1, uModel2);

			assertNull(uModel2.getName(de_DE.getLocale()));
			assertEquals("de name", uModel2.getName(de.getLocale()));
			assertEquals("en name", uModel2.getName(en.getLocale()));

			ctx.setLanguage(de_DE);
			assertNull(u.getName(ctx));
			ctx.setLanguage(de);
			assertEquals("de name", u.getName(ctx));
			ctx.setLanguage(en);
			assertEquals("en name", u.getName(ctx));

		}
		finally
		{
			jaloSession.removeLocalSessionContext();
		}
	}

	@Test
	public void testLoadingError()
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");

		final UnitModel u = new UnitModel();
		u.setCode("foo");
		u.setUnitType("bar");
		u.setName("de name", Locale.GERMAN);
		u.setName("en name", Locale.ENGLISH);

		modelService.save(u);

		modelService.detachAll();

		final Unit uItem = jaloSession.getItem(u.getPk());

		final Unit mockUnit = new Unit()
		{
			@Override
			@SLDSafe
			public String getUnitType(final SessionContext ctx)
			{
				throw new RuntimeException("error1");
			}

			@Override
			@SLDSafe
			public String getName(final SessionContext ctx)
			{
				if ("de".equals(ctx.getLanguage().getIsoCode()))
				{
					throw new RuntimeException("error2");
				}
				return super.getName(ctx);
			}
		};

		// replace implementation to force error
		mockUnit.setImplementation(uItem.getImplementation());

		// warp into model
		final UnitModel errorUnit = modelService.get(mockUnit);

		// unfortunately we also have to replace the AttributeProvide
		// because normally it tries to re-fetch the item upon attribute
		// lazy loading
		final ItemModelContextImpl ctx = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(errorUnit);
		final ItemAttributeProvider attrProv = (ItemAttributeProvider) ctx.getAttributeProvider();
		final PersistenceObject mockPersistenceObject = sourceTransformer.transformSource(mockUnit);
		ctx.setAttributeProvider(new ItemAttributeProvider(mockPersistenceObject, attrProv.getConverter())
		{
			@Override
			public PersistenceObject getPersistenceObject()
			{
				return mockPersistenceObject;
			}
		});

		assertEquals("foo", errorUnit.getCode());
		assertEquals("en name", errorUnit.getName(Locale.ENGLISH));
		try
		{
			errorUnit.getUnitType();
			fail("ModelLoadingException exepcted");
		}
		catch (final Exception e)
		{
			// depending upon preRetch mode it's either ModelLoadingException
			// or the plain runtime exception
			Throwable t = e;
			if (e instanceof ModelLoadingException)
			{
				t = e.getCause();
			}
			assertTrue(t instanceof RuntimeException);
			assertEquals("error1", t.getMessage());
		}
		try
		{
			errorUnit.getName(Locale.GERMAN);
			fail("ModelLoadingException exepcted");
		}
		catch (final Exception e)
		{
			// depending upon preRetch mode it's either ModelLoadingException
			// or the plain runtime exception
			Throwable t = e;
			if (e instanceof ModelLoadingException)
			{
				t = e.getCause();
			}
			assertTrue(t instanceof RuntimeException);
			assertEquals("error2", t.getMessage());
		}

		// test overwriting

		errorUnit.setUnitType("fixed");
		assertEquals("fixed", errorUnit.getUnitType());

		errorUnit.setName("name fixed", Locale.GERMAN);
		assertEquals("name fixed", errorUnit.getName(Locale.GERMAN));
	}

	@Test
	public void testLoadingWithoutLanguage() throws ConsistencyCheckException
	{
		final Language l = jaloSession.getSessionContext().getLanguage();
		final Title t = UserManager.getInstance().createTitle("ttt");
		t.setName("foo");

		try
		{
			jaloSession.getSessionContext().setLanguage(null);
			jaloSession.getSessionContext().setLocale(null);
			try
			{
				((DefaultI18NService) i18nService).getLocaleService().getCurrentDataLocale();
				fail("IllegalStateException expected");
			}
			catch (final IllegalStateException e)
			{
				// expected that
			}

			final TitleModel tm = modelService.get(t);
			assertNotNull(tm);
			assertEquals("ttt", tm.getCode());
			assertEquals("foo", tm.getName(l.getLocale()));

		}
		finally
		{
			jaloSession.getSessionContext().setLanguage(l);
		}
	}

	@Test
	public void testUnsupportedMandatoryAttributesCheck() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			JaloItemNotFoundException, JaloGenericCreationException, JaloAbstractTypeException, JaloDuplicateQualifierException
	{
		final TypeManager tm = TypeManager.getInstance();
		final ComposedType fooType = tm.createComposedType(tm.getComposedType(Unit.class), "Foo");
		// add mandatory attribute 'bar' : String -> generated unit model does not support this because of missing
		// getter and setter methods
		fooType.createAttributeDescriptor("bar", tm.getType(String.class.getName()),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.PROPERTY_FLAG);

		final Unit u1 = ComposedType.newInstance(null, fooType.getCode(), Unit.CODE, "fooUnit", Unit.UNITTYPE, "fooUnitType");

		assertEquals(fooType, u1.getComposedType());

		final UnitModel u1Model = modelService.get(u1);

		assertEquals(u1.getPK(), u1Model.getPk());
		assertEquals(fooType.getCode(), u1Model.getItemtype());
		assertEquals(fooType.getCode(), modelService.getModelType(u1Model));

		u1Model.setName("test test");

		// finally try to save this model: this causes
		// a error if the MandatoryAttributeValueInterceptor does not
		// skip unsupported mandatory attributes !!!
		modelService.save(u1Model);
	}

	// see PLA-7780
	@Test
	public void testSaveMapAttribute()
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId("test_Catalog");

		modelService.save(catalogModel);

		final CatalogVersionModel catVersionModel = modelService.create(CatalogVersionModel.class);
		catVersionModel.setCatalog(catalogModel);
		catVersionModel.setVersion("test_Version");

		modelService.save(catVersionModel);

		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCatalogVersion(catVersionModel);
		productModel.setCode("testUpdateProductModel");

		modelService.save(productModel);

		final Map<IDType, String> buyerIds = new HashMap<IDType, String>();
		buyerIds.put(IDType.UNSPECIFIED, "unspecified");
		productModel.setBuyerIDS(buyerIds);

		modelService.save(productModel);// line 102
	}

	@Test
	public void testEnumLocalization()
	{
		final EnumerationValue cancelledItem = EnumerationManager.getInstance().getEnumerationValue("OrderStatus", "cancelled");
		final OrderStatus cancelledModel = OrderStatus.CANCELLED;

		assertEquals(cancelledItem.getName(), i18nService.getEnumLocalizedName(cancelledModel));

		cancelledItem.setName("foo");
		assertEquals("foo", cancelledItem.getName());
		assertEquals("foo", i18nService.getEnumLocalizedName(cancelledModel));

		i18nService.setEnumLocalizedName(cancelledModel, "bar");
		assertEquals("bar", cancelledItem.getName());
		assertEquals("bar", i18nService.getEnumLocalizedName(cancelledModel));
	}

	@Test
	public void testHashMapVersusArrays()
	{
		// With regards to HOR-110 (analysis servicelayer performance) it is helpful to understand the performance
		// characteristics of hashmaps compared to arrays, as hashmaps are used extensively in the servicelayer framework.

		// Set up a hashmap and array with similar data..
		final int totalSize = 100000;
		final HashMap hashMap = new HashMap<String, String>();
		final String array[] = new String[totalSize];
		for (int i = 0; i < totalSize; i++)
		{
			hashMap.put(Integer.valueOf(i), " " + i);
			array[i] = " " + i;
		}

		// Query and update entries in the hashmaps and arrays, and compare the times taken..
		// This can be improved to make the test cases more similar
		for (int t = 10; t < totalSize * 10; t *= 10)
		{
			final StopWatch hashmapSW = new StopWatch("Hashmap");

			for (int i = 0; i < t; i++)
			{
				hashMap.put(Integer.valueOf(i), i + " ");
			}
			final long t1 = hashmapSW.stop();
			final StopWatch hashmapArray = new StopWatch("Array");
			for (int i = 0; i < t; i++)
			{
				array[i] = array[i] + " ";
			}
			final long t2 = hashmapArray.stop();
			LOG.info("Ratio of array/hashmap for length " + t + " is " + (double) t2 / t1);
		}
	}

	/**
	 * See PLA-8878.
	 */
	@Test
	public void testLazyLoadingConfiguration() throws JaloInvalidParameterException, JaloDuplicateCodeException
	{
		final TypeManager tm = TypeManager.getInstance();
		final ComposedType mediaType = tm.getComposedType(Media.class);
		// create own type to avoid disturbing others
		final ComposedType testType = tm.createComposedType(mediaType, "MyMedia");

		try
		{
			// atomic attribute
			final AttributeDescriptor codeAd = testType.getAttributeDescriptor(Media.CODE);
			// reference attribute
			final AttributeDescriptor cvAd = testType.getAttributeDescriptor(CatalogConstants.Attributes.Media.CATALOGVERSION);

			// assemble own config to play around with global parameters
			final ConfigIntf realCfg = Registry.getCurrentTenant().getConfig();
			final ConfigIntf testCfg = new FastHashMapConfig(Collections.EMPTY_MAP);
			final ConfigIntf fallbackCfg = new FallbackConfig(testCfg, realCfg);

			// 0. check setup
			assertNull(codeAd.getProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG));
			assertNull(cvAd.getProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG));

			// 1. servicelayer.prefetch=literal + no attribute props
			// code -> preFetch == true
			// cv -> preFetch == false
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_LITERAL);

			ItemModelConverter testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(),
					MediaModel.class, null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertTrue(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertFalse(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());

			// 2. servicelayer.prefetch=literal + cv: preFetch = true
			// code -> preFetch == true
			// cv -> preFetch == true
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_LITERAL);
			cvAd.setProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG, Boolean.TRUE);

			testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(), MediaModel.class,
					null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertTrue(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertTrue(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());

			// 3. servicelayer.prefetch = none + no attribute props
			// code -> preFetch == false
			// cv -> preFetch == false
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_NONE);
			cvAd.removeProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG);

			testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(), MediaModel.class,
					null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertFalse(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertFalse(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());

			// 3.a servicelayer.prefetch = none + code:preFetch = true + cv:preFetch = true
			// code -> preFetch == true
			// cv -> preFetch == true
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_NONE);
			cvAd.setProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG, Boolean.TRUE);
			codeAd.setProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG, Boolean.TRUE);

			testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(), MediaModel.class,
					null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertTrue(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertTrue(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());

			// 4. servicelayer.prefetch = all + no attribute props
			// code -> preFetch == true
			// cv -> preFetch == true
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_ALL);
			cvAd.removeProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG);
			codeAd.removeProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG);

			testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(), MediaModel.class,
					null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertTrue(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertTrue(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());

			// 4.a servicelayer.prefetch = all + cv:preFetch = false + code:preFetch = false
			// code -> preFetch == false
			// cv -> preFetch == false
			testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_ALL);
			cvAd.setProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG, Boolean.FALSE);
			codeAd.setProperty(ServicelayerConstants.ATTRIBUTE_PREFETCH_MODE_FLAG, Boolean.FALSE);

			testConv = new ItemModelConverter(modelService, i18nService, commonI18NService, testType.getCode(), MediaModel.class,
					null, sourceTransformer, selfHealingService)
			{
				@Override
				protected ConfigIntf getConfig()
				{
					return fallbackCfg;
				}
			};
			assertFalse(testConv.getInfo(codeAd.getQualifier()).getAttributeInfo().isPreFetched());
			assertFalse(testConv.getInfo(cvAd.getQualifier()).getAttributeInfo().isPreFetched());
		}
		finally
		{
			try
			{
				testType.remove();
			}
			catch (final Exception e)
			{
				LOG.error("cannot remove type:" + e.getMessage());
			}
		}
	}

	private ItemModelConverter createPrefetchNoneModelConverter()
	{
		final ConfigIntf realCfg = Registry.getCurrentTenant().getConfig();
		final ConfigIntf testCfg = new FastHashMapConfig(Collections.EMPTY_MAP);
		final ConfigIntf fallbackCfg = new FallbackConfig(testCfg, realCfg);

		// switch off preFetching
		testCfg.setParameter(ServicelayerConstants.PARAM_PREFETCH, ServicelayerConstants.VALUE_PREFETCH_NONE);

		final ItemModelConverter conv = new ItemModelConverter(modelService, i18nService, commonI18NService, "Title",
				TitleModel.class, null, sourceTransformer, selfHealingService)
		{
			@Override
			protected ConfigIntf getConfig()
			{
				return fallbackCfg;
			}
		};
		conv.init(((DefaultModelService) modelService).getConverterRegistry());

		return conv;
	}

	@Test
	public void testReloadNoPrefetch() throws ConsistencyCheckException
	{
		final ItemModelConverter conv = createPrefetchNoneModelConverter();

		// test preFetch settings
		for (final ModelAttributeInfo attrInfo : conv.getAllModelAttributes())
		{
			if (!"PK".equalsIgnoreCase(attrInfo.getQualifier()))
			{
				assertFalse(attrInfo.getAttributeInfo().isPreFetched());
			}
		}

		final Title t1 = UserManager.getInstance().createTitle("ttt");
		t1.setName("t1 name");

		assertEquals("ttt", t1.getCode());
		assertEquals("t1 name", t1.getName());

		final TitleModel t1Model = (TitleModel) conv.load(t1);

		assertEquals("ttt", t1Model.getCode());
		assertEquals("t1 name", t1Model.getName());

		// now simulate update from somewhere else

		t1.setName("t1 name new");
		t1.setCode("ttt new");

		assertEquals("ttt new", t1.getCode());
		assertEquals("t1 name new", t1.getName());

		// model should still show old values before reload
		assertEquals("ttt", t1Model.getCode());
		assertEquals("t1 name", t1Model.getName());

		conv.reload(t1Model);

		// now model should show new values
		assertEquals("ttt new", t1Model.getCode());
		assertEquals("t1 name new", t1Model.getName());
	}

	/**
	 * PLA-9413
	 */
	@Test
	public void testContextStateChange()
	{
		final ModelContext ctx = ((DefaultModelService) modelService).getModelContext();

		final int newBefore = ctx.getNew().size();
		final int modifiedBefore = ctx.getModified().size();

		final TitleModel newone = modelService.create(TitleModel.class);

		assertTrue(modelService.isNew(newone));
		assertTrue(ctx.getNew().contains(newone));
		assertFalse(ctx.getModified().contains(newone));

		newone.setCode("foo");

		modelService.save(newone);

		assertFalse(modelService.isNew(newone));

		final NewModelIdentityTestProxy oldHashCodeProxy = new NewModelIdentityTestProxy(newone);

		assertFalse(ctx.getNew().contains(newone));
		assertFalse(ctx.getModified().contains(newone));

		assertFalse(ctx.getNew().contains(oldHashCodeProxy));
		assertFalse(ctx.getModified().contains(oldHashCodeProxy));

		assertEquals(newBefore, ctx.getNew().size());
		assertEquals(modifiedBefore, ctx.getModified().size());
	}

	/**
	 * PLA-10327
	 */
	@Test
	public void testRelationSetterWithNull()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("thalersch");
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("thalersch");
		catalogVersion.setActive(Boolean.FALSE);
		catalogVersion.setCatalog(catalog);
		catalog.setCatalogVersions(Collections.singleton(catalogVersion));
		modelService.save(catalog);
		assertEquals(1, catalog.getCatalogVersions().size());
		catalog.setCatalogVersions(null);
		modelService.save(catalog);
		assertEquals(0, catalog.getCatalogVersions().size());
	}

	/**
	 * PLA-10327
	 */
	@Test
	public void testRelationSetterWithEmptyColl()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("thalersch");
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("thalersch");
		catalogVersion.setActive(Boolean.FALSE);
		catalogVersion.setCatalog(catalog);
		catalog.setCatalogVersions(Collections.singleton(catalogVersion));
		modelService.save(catalog);
		assertEquals(1, catalog.getCatalogVersions().size());
		catalog.setCatalogVersions(Collections.EMPTY_SET);
		modelService.save(catalog);
		assertEquals(0, catalog.getCatalogVersions().size());
	}

	@Test
	public void testLocalizedSetterWithoutSessionLang()
	{
		// switch off session language
		jaloSession.getSessionContext().setLanguage(null);
		assertNull(jaloSession.getSessionContext().getLanguage());

		// create model
		final TitleModel model = modelService.create(TitleModel.class);
		model.setCode("T" + System.nanoTime());

		// a) set directly
		model.setName("de", Locale.GERMANY);
		model.setName("uk", Locale.UK);

		assertEquals("de", model.getName(Locale.GERMANY));
		assertEquals("uk", model.getName(Locale.UK));
		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME, Locale.GERMANY));
		assertEquals("uk", modelService.getAttributeValue(model, TitleModel.NAME, Locale.UK));

		modelService.save(model);

		assertEquals("de", model.getName(Locale.GERMANY));
		assertEquals("uk", model.getName(Locale.UK));

		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME, Locale.GERMANY));
		assertEquals("uk", modelService.getAttributeValue(model, TitleModel.NAME, Locale.UK));
	}

	@Test
	public void testLocalizedSetterWithSessionLang()
	{
		// set session lang
		jaloSession.getSessionContext().setLanguage(getOrCreateLanguage("de"));
		assertEquals(getOrCreateLanguage("de"), jaloSession.getSessionContext().getLanguage());

		// create model
		final TitleModel model = modelService.create(TitleModel.class);
		model.setCode("T" + System.nanoTime());

		// a) set directly
		model.setName("de");
		model.setName("uk", Locale.UK);

		assertEquals("de", model.getName());
		assertEquals("de", model.getName(Locale.GERMANY));
		assertEquals("uk", model.getName(Locale.UK));
		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME));
		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME, Locale.GERMANY));
		assertEquals("uk", modelService.getAttributeValue(model, TitleModel.NAME, Locale.UK));

		modelService.save(model);

		assertEquals("de", model.getName());
		assertEquals("de", model.getName(Locale.GERMANY));
		assertEquals("uk", model.getName(Locale.UK));
		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME));
		assertEquals("de", modelService.getAttributeValue(model, TitleModel.NAME, Locale.GERMANY));
		assertEquals("uk", modelService.getAttributeValue(model, TitleModel.NAME, Locale.UK));
	}

	/**
	 * Example of test fetching localized attribute from super parent type "TypeManagerManaged" , where in type hierarchy
	 * comes another type "Type" which adds additional localized values. This is special case when the protected
	 * _localizedValues could be overriden and localized attribute might get lost.
	 */
	@Test
	public void testSetLocalizedAttributesForSubTypeInModelHierarchy()
	{

		importCsvUtil.importCsv("/platformservices/test/testSetLocalizedAttributes.csv", "UTF-8");

		final ComposedTypeModel example = new ComposedTypeModel();
		example.setCode("TestComposedTypeChild");

		final ComposedTypeModel actualModel = // typeDao.findComposedTypeByCode("TestComposedTypeChild");
		flexibleSearchService.getModelByExample(example);

		assertNotNull("Composed type can't be null", actualModel);
		assertEquals("Different object found", "TestComposedTypeChild", actualModel.getName());
		assertEquals("Improper supertype", "TestComposedTypeParent", actualModel.getSuperType().getName());

	}

	// PLA-12923
	@Test
	public void testSaveAfterAttachedTwice()
	{
		final TitleModel t = modelService.create(TitleModel.class);
		t.setCode("foo");

		// attach again
		modelService.attach(t);

		modelService.saveAll();

		assertFalse(modelService.isNew(t));
		assertTrue(modelService.isUpToDate(t));
	}

	@Test
	public void testGeneratePKForNewModel()
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode("foo");
		assertNull(title.getPk());

		final ItemModelInternalContext ictx = (ItemModelInternalContext) ModelContextUtils.getItemModelContext(title);
		assertNull(ictx.getPK());
		assertNull(ictx.getNewPK());
		final PK newPK = ictx.generateNewPK(); // this should generate a new PK
		assertEquals(newPK, ictx.getNewPK());
		assertNull(title.getPk());
		modelService.save(title);
		assertEquals(newPK, title.getPk());

		final TitleModel savedTitle = modelService.get(title.getPk());
		assertNotNull(savedTitle);
		assertEquals(title.getPk(), savedTitle.getPk());
		assertEquals("foo", savedTitle.getCode());
	}

	private static class NewModelIdentityTestProxy
	{
		private final Object model;

		NewModelIdentityTestProxy(final Object model)
		{
			this.model = model;
		}

		@Override
		public int hashCode()
		{
			return System.identityHashCode(model);
		}

		@Override
		public boolean equals(final Object obj)
		{
			return model == obj || (model != null && model.equals(obj));
		}
	}

	// technical internals how to reload ItemModelConverter
	private ModelConverter adjustConverter(final String type, final Class modelClass, final ModelConverter newConverter,
			final ModelConverter oldConverter)
	{
		((DefaultModelConverterRegistry) converterRegistry).removeModelConverterBySourceType(type);
		((DefaultModelConverterRegistry) converterRegistry).registerModelConverter(type, modelClass, newConverter);
		LOG.info("reloading converter for " + modelClass + " type " + type);
		return oldConverter;
	}

	private ItemModelConverter deepCloneConverter(final ItemModelConverter given)
	{
		return new ItemModelConverter(modelService, i18nService, commonI18NService, given.getDefaultType(), given.getModelClass(),
				given.getSerializationStrategy(), sourceTransformer, selfHealingService);
	}

	private ItemModelConverter getCurrent(final Class modelClass)
	{
		return (ItemModelConverter) ((DefaultModelConverterRegistry) converterRegistry).getModelConverterByModelType(modelClass);
	}

	private void reloadConverter(final String type, final Class modelClass)
	{
		final ItemModelConverter existing = getCurrent(modelClass);
		final ItemModelConverter fresh = deepCloneConverter(existing);
		adjustConverter(type, modelClass, fresh, existing);
	}

	/**
	 * @deprecated since ages
	 */
	@Deprecated
	void setSerializationStrategy(final AbstractItemModel model, final SerializationStrategy strategy)
	{
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(model)).setSerializationStrategy(strategy);
	}

	/**
	 * @deprecated since ages
	 */
	@Deprecated
	SerializationStrategy getSerializationStrategy(final AbstractItemModel model)
	{
		return ((ItemModelContextImpl) ModelContextUtils.getItemModelContext(model)).getSerializationStrategy();
	}

}
