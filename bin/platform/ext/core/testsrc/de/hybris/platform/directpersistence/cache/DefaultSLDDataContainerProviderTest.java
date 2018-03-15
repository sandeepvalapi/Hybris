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
package de.hybris.platform.directpersistence.cache;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.C2LDataCreator;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.directpersistence.MutableChangeSet;
import de.hybris.platform.directpersistence.impl.DefaultChangeSet;
import de.hybris.platform.directpersistence.impl.DefaultWritePersistenceGateway;
import de.hybris.platform.directpersistence.read.SLDDataContainerAssert;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.testframework.seed.TestDataCreator;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultSLDDataContainerProviderTest extends ServicelayerBaseTest
{
	private static final String TEST_USER = "testUser";
	private static final String FOO = "Foo";
	private static final String BAR = "Bar";
	private static final String BAZ = "Baz";
	private static final String QUX = "Qux";
	private static final String QUUX = "Quux";
	private static final String CODE = "code";

	private Language en;
	private Language de;
	private UserModel userModel;

	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private DefaultSLDDataContainerProvider defaultSLDDataContainerProvider;
	@Resource
	private C2LDataCreator c2lDataCreator;
	@Resource
	private DefaultWritePersistenceGateway defaultWritePersistenceGateway;
	private TestDataCreator testDataCreator;


	@Before
	public void setUp()
	{
		userModel = modelService.create(UserModel.class);
		userModel.setUid(TEST_USER);
		modelService.save(userModel);
		en = c2lDataCreator.createOrGetLanguage("en", true);
		de = c2lDataCreator.createOrGetLanguage("de", true);

		testDataCreator = new TestDataCreator(modelService);
	}

	@Test
	public void testLoadWithNotExistingPK()
	{
		//given
		final PK pk = userModel.getPk();
		modelService.remove(userModel);

		//when
		final SLDDataContainer sldDataContainer = defaultSLDDataContainerProvider.get(pk);

		//then
		assertThat(sldDataContainer).isNull();
	}

	@Test
	public void testLoadBaseProperties()
	{
		//when
		final SLDDataContainer sldDataContainer = defaultSLDDataContainerProvider.get(userModel.getPk());

		//then
		assertThat(sldDataContainer.getPk()).isEqualTo(userModel.getPk());
		assertThat(sldDataContainer.getTypeCode()).isEqualTo(UserModel._TYPECODE);
		assertThat(sldDataContainer.getAttributeValue(UserModel.UID, null).getValue()).isEqualTo(TEST_USER);
	}

	@Test
	public void testLoadProperties() throws JaloGenericCreationException, JaloAbstractTypeException, ConsistencyCheckException
	{
		//given
		final PK pk;
		final Title title = createTitle();
		pk = title.getPK();
		final String[] strings =
		{ FOO, BAR, BAZ };
		title.setProperty(FOO, strings);
		final TestClass testClass = new TestClass(FOO);
		title.setProperty(BAR, testClass);
		title.setProperty(BAZ, userModel);
		changeSessionLanguage(en);
		title.setLocalizedProperty(QUX, CODE);
		changeSessionLanguage(de);
		title.setLocalizedProperty(QUX, FOO);
		title.setProperty(QUUX, false);

		//when
		final Title titleLoadedBySL = modelService.getSource(modelService.get(pk));
		final SLDDataContainer sldDataContainer = defaultSLDDataContainerProvider.get(pk);

		//then
		//verify that SL values are equal to jalo values
		assertThat(titleLoadedBySL.getProperty(FOO)).isEqualTo(title.getProperty(FOO)).isEqualTo(strings);
		assertThat(titleLoadedBySL.getProperty(BAR)).isEqualTo(title.getProperty(BAR)).isEqualTo(testClass);
		assertThat(titleLoadedBySL.getProperty(BAZ)).isEqualTo(title.getProperty(BAZ)).isEqualTo(userModel);
		changeSessionLanguage(en);
		assertThat(titleLoadedBySL.getLocalizedProperty(QUX)).isEqualTo(title.getLocalizedProperty(QUX)).isEqualTo(CODE);
		changeSessionLanguage(de);
		assertThat(titleLoadedBySL.getLocalizedProperty(QUX)).isEqualTo(title.getLocalizedProperty(QUX)).isEqualTo(FOO);
		assertThat(titleLoadedBySL.getProperty(QUUX)).isEqualTo(title.getProperty(QUUX)).isEqualTo(false);
		//verify that SLD values are equal to jalo values
		assertThat(sldDataContainer.getPropertyValue(FOO, null).getValue()).isEqualTo(title.getProperty(FOO)).isEqualTo(strings);
		assertThat(sldDataContainer.getPropertyValue(BAR, null).getValue()).isEqualTo(title.getProperty(BAR)).isEqualTo(testClass);
		assertThat(sldDataContainer.getPropertyValue(BAZ, null).getValue()).isEqualTo(title.getProperty(BAZ)).isEqualTo(userModel);
		changeSessionLanguage(en);
		assertThat(sldDataContainer.getPropertyValue(QUX, en.getPK()).getValue()).isEqualTo(title.getLocalizedProperty(QUX))
				.isEqualTo(CODE);
		changeSessionLanguage(de);
		assertThat(sldDataContainer.getPropertyValue(QUX, de.getPK()).getValue()).isEqualTo(title.getLocalizedProperty(QUX))
				.isEqualTo(FOO);
		assertThat(sldDataContainer.getPropertyValue(QUUX, null).getValue()).isEqualTo(title.getProperty(QUUX)).isEqualTo(false);
	}

	@Test
	@Transactional
	public void testSaveAndReadThroughSLD()
	{
		//given
		final MutableChangeSet changeSet = new DefaultChangeSet();
		final PK titlePK = generatePkForCode(TitleModel._TYPECODE);
		final TestClass testClass = new TestClass(QUX);
		final InsertRecord insertRecord = new InsertRecord(titlePK, TitleModel._TYPECODE, Sets.newHashSet(new PropertyHolder(BAR,
				userModel), new PropertyHolder(FOO, testClass)), mapOf(new Locale("en"),
				Sets.newHashSet(new PropertyHolder(BAZ, CODE)), new Locale("de"), Sets.newHashSet(new PropertyHolder(BAZ, FOO))));
		changeSet.add(insertRecord);

		defaultWritePersistenceGateway.persist(changeSet);

		//when
		final SLDDataContainer sldDataContainer = defaultSLDDataContainerProvider.get(titlePK);

		//then
		assertThat(sldDataContainer.getPk()).isEqualTo(titlePK);
		assertThat(sldDataContainer.getTypePk()).isEqualTo(
				DirectPersistenceUtils.getInfoMapForType(TitleModel._TYPECODE).getTypePK());
		assertThat(sldDataContainer.getPropertyValue(FOO, null).getValue()).isEqualTo(testClass);
		assertThat(sldDataContainer.getPropertyValue(BAR, null).getValue()).isEqualTo(userModel);
		assertThat(sldDataContainer.getPropertyValue(BAZ, en.getPK()).getValue()).isEqualTo(CODE);
		assertThat(sldDataContainer.getPropertyValue(BAZ, de.getPK()).getValue()).isEqualTo(FOO);
	}

	@Test
	public void shouldBulkLoadItems() throws Exception
	{
		// given
		final CatalogModel catalog = testDataCreator.createCatalog();
		final CatalogVersionModel catalogVersion = testDataCreator.createCatalogVersion("testVersion", catalog);
		final ProductModel p1 = testDataCreator.createProduct(catalogVersion);
		final ProductModel p2 = testDataCreator.createProduct(catalogVersion);
		final ProductModel p3 = testDataCreator.createProduct(catalogVersion);

		// when
		final List<SLDDataContainer> containers = defaultSLDDataContainerProvider.getAll(Lists.newArrayList(p1.getPk(), p2.getPk(),
				p3.getPk()));

		// then
		checkContainersAreValid(p1, p2, p3, containers);
	}

	@Test
	public void shouldBulkLoadItemsConcurrently() throws Exception
	{
		final CatalogModel catalog = testDataCreator.createCatalog();
		final CatalogVersionModel catalogVersion = testDataCreator.createCatalogVersion("testVersion", catalog);
		final ProductModel p1 = testDataCreator.createProduct(catalogVersion);
		final ProductModel p2 = testDataCreator.createProduct(catalogVersion);
		final ProductModel p3 = testDataCreator.createProduct(catalogVersion);
		final TestThreadsHolder<BulkLoadRunner> holder = new TestThreadsHolder<>(20, new BulkLoadRunner(Lists.newArrayList(p1.getPk(), p2.getPk(), p3.getPk())), true);

		holder.startAll();
		holder.waitForAll(30, TimeUnit.SECONDS);

		final Map<Integer, Throwable> errors = holder.getErrors();

		if (!errors.isEmpty())
		{
			fail("Errors occured during threads execution: " + errors);
		}
	}

	private class BulkLoadRunner implements Runnable
	{
        private final List<PK> pks;

		private BulkLoadRunner(final List<PK> pks)
		{
            this.pks = pks;
		}

		@Override
		public void run()
		{
			final List<SLDDataContainer> containers = defaultSLDDataContainerProvider.getAll(pks);
            final ProductModel p1 = getModelService().get(pks.get(0));
            final ProductModel p2 = getModelService().get(pks.get(1));
            final ProductModel p3 = getModelService().get(pks.get(2));
            checkContainersAreValid(p1, p2, p3, containers);
		}

        private ModelService getModelService()
        {
            return Registry.getApplicationContext().getBean("modelService", ModelService.class);
        }
	}

    public void checkContainersAreValid(final ProductModel p1, final ProductModel p2, final ProductModel p3,
                                        final List<SLDDataContainer> containers)
    {
        assertThat(containers).hasSize(3);
        final SLDDataContainer p1Container = getContainerWithPk(containers, p1.getPk());
        SLDDataContainerAssert.assertThat(p1Container).hasEqualMetaDataAs(p1);
        SLDDataContainerAssert.assertThat(p1Container).containsAttribute(ProductModel.CODE).withValueEqualTo(p1.getCode());
        SLDDataContainerAssert.assertThat(p1Container).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p1.getName());
        SLDDataContainerAssert.assertThat(p1Container).containsAttribute(ProductModel.CATALOGVERSION)
                .withReferenceValueEqualTo(p1.getCatalogVersion());
        SLDDataContainerAssert.assertThat(p1Container).containsLocalizedAttribute(ProductModel.DESCRIPTION)
                .withValueEqualTo(p1.getDescription());

        final SLDDataContainer p2Container = getContainerWithPk(containers, p2.getPk());
        SLDDataContainerAssert.assertThat(p2Container).hasEqualMetaDataAs(p2);
        SLDDataContainerAssert.assertThat(p2Container).containsAttribute(ProductModel.CODE).withValueEqualTo(p2.getCode());
        SLDDataContainerAssert.assertThat(p2Container).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p2.getName());
        SLDDataContainerAssert.assertThat(p2Container).containsAttribute(ProductModel.CATALOGVERSION)
                .withReferenceValueEqualTo(p2.getCatalogVersion());
        SLDDataContainerAssert.assertThat(p2Container).containsLocalizedAttribute(ProductModel.DESCRIPTION)
                .withValueEqualTo(p2.getDescription());

        final SLDDataContainer p3Container = getContainerWithPk(containers, p3.getPk());
        SLDDataContainerAssert.assertThat(p3Container).hasEqualMetaDataAs(p3);
        SLDDataContainerAssert.assertThat(p3Container).containsAttribute(ProductModel.CODE).withValueEqualTo(p3.getCode());
        SLDDataContainerAssert.assertThat(p3Container).containsLocalizedAttribute(ProductModel.NAME).withValueEqualTo(p3.getName());
        SLDDataContainerAssert.assertThat(p3Container).containsAttribute(ProductModel.CATALOGVERSION)
                .withReferenceValueEqualTo(p3.getCatalogVersion());
        SLDDataContainerAssert.assertThat(p3Container).containsLocalizedAttribute(ProductModel.DESCRIPTION)
                .withValueEqualTo(p3.getDescription());
    }

	@Test
	public void testCMPAttributes()
	{
		final ItemModel item1 = userService.getAdminUser();
		final ItemModel item2 = userService.getAnonymousUser();

		final LinkModel link = modelService.create(LinkModel.class);
		link.setSource(item1);
		link.setTarget(item2);
		link.setQualifier("FooBar");
		modelService.save(link);

		final SLDDataContainer sldDataContainer = defaultSLDDataContainerProvider.get(link.getPk());

		assertTrue(modelService.isUpToDate(link));
		assertEquals("FooBar", sldDataContainer.getAttributeValue(LinkModel.QUALIFIER, null).getValue());
		assertThat(sldDataContainer.getAttributeValue(LinkModel.SOURCE, null)).isNotNull();
		assertThat(sldDataContainer.getAttributeValue(LinkModel.SOURCE, null).getValue()).isInstanceOf(ItemPropertyValue.class);
		assertEquals(item1.getPk(),
				((ItemPropertyValue) sldDataContainer.getAttributeValue(LinkModel.SOURCE, null).getValue()).getPK());
		assertThat(sldDataContainer.getAttributeValue(LinkModel.TARGET, null)).isNotNull();
		assertThat(sldDataContainer.getAttributeValue(LinkModel.TARGET, null).getValue()).isInstanceOf(ItemPropertyValue.class);
		assertEquals(item2.getPk(),
				((ItemPropertyValue) sldDataContainer.getAttributeValue(LinkModel.TARGET, null).getValue()).getPK());

		final Collection<LinkModel> links = getLinks(item1, item2, "FooBar");
		assertEquals(1, links.size());
		assertEquals(Collections.singletonList(link), links);

		final Collection<LinkModel> noLinks = getLinks(item2, item1, "FooBar");
		assertEquals(Collections.emptyList(), noLinks);
	}

	protected Collection<LinkModel> getLinks(final ItemModel src, final ItemModel tgt, final String qualifier)
	{
		return modelService.toModelLayer(LinkManager.getInstance().getLinks(qualifier, (Item) modelService.getSource(src),
				(Item) modelService.getSource(tgt)));
	}

	private void changeSessionLanguage(final Language lang)
	{
		JaloSession.getCurrentSession().getSessionContext().setLanguage(lang);
	}

	private Title createTitle() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Item.ItemAttributeMap params = new Item.ItemAttributeMap();
		params.put(Title.CODE, CODE);
		return (Title) TypeManager.getInstance().getComposedType(Title.class).newInstance(params);
	}

	private Map<Locale, Set<PropertyHolder>> mapOf(final Locale locale, final Set<PropertyHolder> value, final Locale locale2,
			final Set<PropertyHolder> value2)
	{
		return ImmutableMap.<Locale, Set<PropertyHolder>> of(locale, value, locale2, value2);
	}

	private PK generatePkForCode(final String typeCode)
	{
		final TypeInfoMap persistenceInfo = Registry.getCurrentTenant().getPersistenceManager().getPersistenceInfo(typeCode);
		return PK.createCounterPK(persistenceInfo.getItemTypeCode());
	}

	private SLDDataContainer getContainerWithPk(final Collection<SLDDataContainer> containers, final PK pk)
	{
		final Optional<SLDDataContainer> possibleContainer = containers.stream().filter(c -> c.getPk().equals(pk)).findFirst();
		assertThat(possibleContainer.isPresent()).isTrue();
		return possibleContainer.get();
	}

}
