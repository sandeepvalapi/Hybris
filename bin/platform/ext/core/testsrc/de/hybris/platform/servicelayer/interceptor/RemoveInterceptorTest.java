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
package de.hybris.platform.servicelayer.interceptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import de.hybris.platform.servicelayer.internal.model.extractor.CascadingDependenciesResolver;
import de.hybris.platform.servicelayer.internal.model.extractor.PersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultModelExtractor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceType;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * This junit test tests the Partof remove interceptor and the partof save interceptor
 */
@IntegrationTest
public class RemoveInterceptorTest extends ServicelayerTransactionalBaseTest
{

	public static final String SOME_TEST_TYPE = "SomeTestType";

	@Resource
	private ModelService modelService;
	@Resource
	private I18NService i18nService;
	@Resource
	private SessionService sessionService;
	@Resource
	private Cascader cascader;
	@Resource
	private CascadingDependenciesResolver cascadingDependenciesResolver;
	@Mock
	private PersistenceTypeService persistenceTypeService;

	private DefaultInterceptorRegistry reg = null;

	private final List<ItemModel> gotRegionInterceptors = new ArrayList<ItemModel>();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode("DE");
		country.setActive(Boolean.TRUE);
		modelService.save(country);

		final RegionModel region = modelService.create(RegionModel.class);
		region.setIsocode("DE-BW");
		region.setCountry(country);
		modelService.save(region);

		country = modelService.create(CountryModel.class);
		country.setIsocode("US");
		country.setActive(Boolean.TRUE);
		modelService.save(country);

		reg = (DefaultInterceptorRegistry) ((DefaultModelService) modelService).getInterceptorRegistry();

		gotRegionInterceptors.clear();
		reg.registerInterceptor("Region", new RegionRemoveInterceptor(), Collections.EMPTY_SET);

		given(persistenceTypeService.getPersistenceType(anyCollection())).willReturn(PersistenceType.JALO);
	}

	@Test
	public void testRemovePartOfAttributes()
	{
		final CountryModel germany = i18nService.getCountry("DE");
		Assert.assertNotNull(germany);

		final CountryModel usa = i18nService.getCountry("US");
		Assert.assertNotNull(usa);

		final Collection<RegionModel> beforeCollection = new HashSet<RegionModel>(germany.getRegions());
		beforeCollection.addAll(usa.getRegions());
		Assert.assertFalse(beforeCollection.isEmpty());

		for (final RegionModel reg : beforeCollection)
		{
			Assert.assertFalse(modelService.isRemoved(reg));
		}

		modelService.removeAll(Arrays.asList(germany, usa));

		for (final RegionModel reg : beforeCollection)
		{
			Assert.assertTrue(modelService.isRemoved(reg));
		}
		Assert.assertTrue(modelService.isRemoved(germany));
		Assert.assertTrue(modelService.isRemoved(usa));
		Assert.assertEquals(gotRegionInterceptors.size(), beforeCollection.size());
	}


	//	/*
	//	 * Test scenario:
	//	 * m1 -> m11
	//	 *    -> m12 -> m121
	//	 *           -> m122
	//	 *    -> m13 -> m131
	//	 * m2 ->
	//	 * m3 -> m31 -> m311
	//	 *           -> m312
	//	 *
	//	 * expected: [m11, m121, m122, m12, m131, m13, m1, m2, m311, m312, m31, m3]
	//	 */
	@Test
	public void testRemovalScheduling()
	{
		final Object m1 = new TestItem("m1");
		final Object m11 = new TestItem("m11");
		final Object m12 = new TestItem("m12");
		final Object m121 = new TestItem("m121");
		final Object m122 = new TestItem("m122");
		final Object m13 = new TestItem("m13");
		final Object m131 = new TestItem("m131");
		final Object m2 = new TestItem("m2");
		final Object m3 = new TestItem("m3");
		final Object m31 = new TestItem("m31");
		final Object m311 = new TestItem("m311");
		final Object m312 = new TestItem("m312");

		final Map<Object, Object[]> containments = new HashMap<Object, Object[]>();
		containments.put(m1, new Object[]
		{ m11, m12, m13 });
		containments.put(m12, new Object[]
		{ m121, m122 });
		containments.put(m13, new Object[]
		{ m131 });
		containments.put(m3, new Object[]
		{ m31 });
		containments.put(m31, new Object[]
		{ m311, m312 });

		final List<Object> expectedRemovalSchedule = Arrays.asList(//
				m11, m121, m122, m12, m131, m13, m1, m2, m311, m312, m31, m3//
		);

		final List<Object> removed = new ArrayList<Object>();

		final List<Object> expectedNotifySchedule = Arrays.asList(//
				m1, m11, m12, m121, m122, m13, m131, m2, m3, m31, m311, m312//
		);

		final List<Object> notified = new ArrayList<Object>();

		final ModelConverter mockConverter = new MockModelConverter(removed);
		final RemoveInterceptor mockRemoveInterceptor = new RemoveInterceptor()
		{
			@Override
			public void onRemove(final Object o, final InterceptorContext ctx) throws InterceptorException
			{
				notified.add(o);

				if (containments.containsKey(o))
				{
					for (final Object subItem : containments.get(o))
					{
						ctx.registerElement(subItem, null);
					}
				}
			}
		};

		final ModelService mockService = new MockModelService(mockRemoveInterceptor, mockConverter);

		((DefaultModelService) mockService).setPersistenceTypeService(persistenceTypeService);
		mockService.removeAll(Arrays.asList(m1, m2, m3));
		assertEquals(expectedRemovalSchedule, removed);
		assertEquals(expectedNotifySchedule, notified);

		removed.clear();
		notified.clear();
		mockService.remove(m2);
		assertEquals(Arrays.asList(m2), removed);
		assertEquals(Arrays.asList(m2), notified);

		removed.clear();
		notified.clear();
		mockService.remove(m1);
		assertEquals(Arrays.asList(m11, m121, m122, m12, m131, m13, m1), removed);
		assertEquals(Arrays.asList(m1, m11, m12, m121, m122, m13, m131), notified);
	}

	//	/*
	//	 * Test scenario 2:
	//	 * m1 -> m11 -> m111
	//	 *    -> m12 -> m121
	//	 *           -> m3
	//	 * m2 -> m4
	//	 * m3 -> m111
	//	 * m4 -> m2
	//	 * m5 -> m51 -> m5 //actually illegal
	//	 * expected: [m111, m11, m121, m3, m12, m1, m2, m4, m5, m51]
	//	 *
	//	 */
	@Test
	public void testRemovalScheduling2()
	{
		final Object m1 = new TestItem("m1");
		final Object m11 = new TestItem("m11");
		final Object m111 = new TestItem("m111");
		final Object m121 = new TestItem("m121");
		final Object m3 = new TestItem("m3");
		final Object m12 = new TestItem("m12");
		final Object m2 = new TestItem("m2");
		final Object m4 = new TestItem("m4");
		final Object m5 = new TestItem("m5");
		final Object m51 = new TestItem("m51");

		final Map<Object, Object[]> containments = new HashMap<Object, Object[]>();
		containments.put(m1, new Object[]
		{ m11, m12 });
		containments.put(m11, new Object[]
		{ m111 });
		containments.put(m12, new Object[]
		{ m121, m3 });
		containments.put(m2, new Object[]
		{ m4 });
		containments.put(m3, new Object[]
		{ m111 });
		containments.put(m4, new Object[]
		{ m2 });
		containments.put(m5, new Object[]
		{ m51 });
		containments.put(m51, new Object[]
		{ m5 });

		final List<Object> expectedRemovalSchedule = Arrays.asList(//
				m111, m11, m121, m3, m12, m1, m2, m4, m5, m51//
		);

		final List<Object> removed = new ArrayList<Object>();

		final List<Object> expectedNotifySchedule = Arrays.asList(//
				m1, m11, m111, m12, m121, m3, m2, m4, m5, m51//
		);

		final List<Object> notified = new ArrayList<Object>();

		final ModelConverter mockConverter = new MockModelConverter(removed);

		final RemoveInterceptor mockRemoveInterceptor = new RemoveInterceptor()
		{
			@Override
			public void onRemove(final Object o, final InterceptorContext ctx) throws InterceptorException
			{
				notified.add(o);

				if (containments.containsKey(o))
				{
					for (final Object subItem : containments.get(o))
					{
						ctx.registerElement(subItem, null);
					}
				}
			}
		};

		final ModelService mockService = new MockModelService(mockRemoveInterceptor, mockConverter);
		((DefaultModelService) mockService).setPersistenceTypeService(persistenceTypeService);
		mockService.removeAll(Arrays.asList(m1, m2, m3, m4, m5));
		assertEquals(expectedRemovalSchedule, removed);
		assertEquals(expectedNotifySchedule, notified);

		removed.clear();
		notified.clear();
		mockService.remove(m2);
		assertEquals(Arrays.asList(m2, m4), removed);
		assertEquals(Arrays.asList(m2, m4), notified);

		removed.clear();
		notified.clear();
		mockService.remove(m1);
		assertEquals(Arrays.asList(m111, m11, m121, m3, m12, m1), removed);
		assertEquals(Arrays.asList(m1, m11, m111, m12, m121, m3), notified);
	}

	private static class MockConverterRegistry implements ConverterRegistry
	{
		private final ModelConverter mockConverter;

		public MockConverterRegistry(final ModelConverter mockConverter)
		{
			this.mockConverter = mockConverter;
		}

		@Override
		public ModelService getModelService()
		{
			return null;
		}

		@Override
		public ModelConverter getModelConverterByModelType(final Class<?> modelClass)
		{
			return null;
		}

		@Override
		public boolean hasModelConverterForModelType(final Class<?> modelClass)
		{
			return false;
		}

		@Override
		public ModelConverter getModelConverterBySourceType(final String key)
		{
			return null;
		}

		@Override
		public boolean hasModelConverterForSourceType(final String key)
		{
			return false;
		}

		@Override
		public String getMappedType(final Class<?> modelClass)
		{
			return null;
		}

		@Override
		public ModelConverter removeModelConverterBySourceType(final String type)
		{
			return null;
		}

		@Override
		public Collection<ModelConverter> getModelConverters()
		{
			return null;
		}

		@Override
		public void clearModelConverters()
		{

		}

		@Override
		public ModelConverter getModelConverterByModel(final Object model)
		{
			return mockConverter;
		}
	}

	public class RegionRemoveInterceptor implements RemoveInterceptor
	{
		@Override
		public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
		{
			if (model instanceof RegionModel)
			{
				gotRegionInterceptors.add((RegionModel) model);
			}
		}

	}

	private static class MockInterceptorRegistry implements InterceptorRegistry
	{
		private final RemoveInterceptor mockRemoveInterceptor;

		public MockInterceptorRegistry(final RemoveInterceptor mockRemoveInterceptor)
		{
			this.mockRemoveInterceptor = mockRemoveInterceptor;
		}

		@Override
		public Collection<PrepareInterceptor> getPrepareInterceptors(final String type)
		{
			return null;
		}

		@Override
		public Collection<LoadInterceptor> getLoadInterceptors(final String type)
		{
			return null;
		}

		@Override
		public Collection<RemoveInterceptor> getRemoveInterceptors(final String type)
		{
			if (type.equals(SOME_TEST_TYPE))
			{
				return Collections.singletonList(mockRemoveInterceptor);
			}

			throw new RuntimeException("This mock was not prepared for type " + type);
		}

		@Override
		public Collection<ValidateInterceptor> getValidateInterceptors(final String type)
		{
			return null;
		}

		@Override
		public Collection<InitDefaultsInterceptor> getInitDefaultsInterceptors(final String type)
		{
			return null;
		}
	}

	class TestItem extends AbstractItemModel
	{
		private final Object val;

		TestItem(final Object val)
		{
			this.val = val;
		}

		@Override
		public String toString()
		{
			return val.toString();
		}

		@Override
		public boolean equals(final Object obj)
		{
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		@Override
		public int hashCode()
		{
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}

	private class DummyModelConverter implements ModelConverter
	{
		@Override
		public void setAttributeValue(final Object model, final String attributeQualifier, final Object value)
		{
			throw new RuntimeException();
		}

		@Override
		public void save(final Object model, final Collection<String> exclude)
		{
			throw new RuntimeException();
		}

		@Override
		public void remove(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public void reload(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public Object load(final Object source)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isUpToDate(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isRemoved(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isNew(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isModified(final Object model, final String attribute, final Locale loc)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isModified(final Object model, final String attribute)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean isModified(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public void init(final ConverterRegistry registry)
		{
			throw new RuntimeException();
		}

		@Override
		public String getType(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public Object getSource(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public Map<String, Set<Locale>> getDirtyAttributes(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public Object getAttributeValue(final Object model, final String attributeQualifier)
		{
			throw new RuntimeException();
		}

		@Override
		public boolean exists(final Object model)
		{
			throw new RuntimeException();
		}

		@Override
		public Object create(final String type)
		{
			throw new RuntimeException();
		}

		@Override
		public void beforeAttach(final Object model, final ModelContext ctx)
		{
			throw new RuntimeException();
		}

		@Override
		public void afterDetach(final Object model, final ModelContext ctx)
		{
			throw new RuntimeException();
		}

		@Override
		public Set<String> getWritablePartOfAttributes(final TypeService typeService)
		{
			throw new RuntimeException();
		}

		@Override
		public Object getLocalizedAttributeValue(final Object model, final String attributeQualifier, final Locale locale)
		{
			throw new RuntimeException();
		}

		@Override
		public PersistenceObject getPersistenceSource(final Object model)
		{
			return null;
		}
	}

	class MockModelConverter extends DummyModelConverter
	{

		final List<Object> removed;

		MockModelConverter(final List<Object> removed)
		{
			this.removed = removed;
		}

		@Override
		public void remove(final Object model)
		{
			removed.add(model);
		}

		@Override
		public boolean isRemoved(final Object model)
		{
			// all are not removed
			return false;
		}

		@Override
		public Object getSource(final Object model)
		{
			// use same instance
			return model;
		}

		@Override
		public String getType(final Object model)
		{
			return SOME_TEST_TYPE;
		}

		@Override
		public boolean isNew(final Object model)
		{
			return false;
		}
	}

	private class MockModelService extends DefaultModelService
	{

		private final RemoveInterceptor mockRemoveInterceptor;
		private final ModelConverter mockConverter;

		public MockModelService(final RemoveInterceptor mockRemoveInterceptor, final ModelConverter mockConverter)
		{
			final DefaultModelExtractor defaultModelExtractor = new DefaultModelExtractor();
			defaultModelExtractor.setDependenciesResolver(cascadingDependenciesResolver);

			this.mockRemoveInterceptor = mockRemoveInterceptor;
			this.mockConverter = mockConverter;
			this.setModelExtractor(defaultModelExtractor);
			this.setCascader(cascader);
			this.setModelContext(Mockito.mock(ModelContext.class));
		}

		@Override
		public InterceptorRegistry lookupInterceptorRegistry()
		{
			return new MockInterceptorRegistry(mockRemoveInterceptor);
		}

		@Override
		public ConverterRegistry lookupConverterRegistry()
		{
			return new MockConverterRegistry(mockConverter);
		}

		@Override
		protected void doDetach(final Object model, final Object source, final ModelConverter conv)
		{
			// do nothing
		}

		@Override
		protected ModelConverter getModelConverterByModel(final Object model)
		{
			return mockConverter;
		}

		@Override
		public SessionService lookupSessionService()
		{
			return sessionService;
		}
	}

}
