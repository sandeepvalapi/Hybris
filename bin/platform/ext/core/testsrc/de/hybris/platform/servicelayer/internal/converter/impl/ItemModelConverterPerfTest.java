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
package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;


/**
 * Low level test checks if many thread accessing ItemModelConverter may cause inconsistent state between model's
 * localized attribute field and model's value history.
 */
@PerformanceTest
public class ItemModelConverterPerfTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(ItemModelConverterPerfTest.class);

	private static final int DURATION_SEC = 10; //NOPMD

	@Resource
	private ConverterRegistry converterRegistry;

	@Resource
	private I18NService i18nService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private ModelService modelService;

	@Resource
	private SelfHealingService selfHealingService;

	private ItemModelConverter originalConverter;
	private ItemModelConverter freshConverter;

	private ItemModelConverter getCurrent(final Class modelClass)
	{
		return (ItemModelConverter) ((DefaultModelConverterRegistry) converterRegistry).getModelConverterByModelType(modelClass);
	}


	@Before
	public void prepare()
	{
		originalConverter = getCurrent(TitleModel.class);

		freshConverter = new ItemModelConverter(modelService, i18nService, commonI18NService, "Title", TitleModel.class,
				originalConverter.getSerializationStrategy(), originalConverter.getSourceTransformer(), selfHealingService)
		{
			@Override
			protected AttributePrefetchMode readPrefetchSettings()
			{

				return new AttributePrefetchMode()
				{
					@Override
					public boolean isPrefetched(final de.hybris.platform.jalo.type.AttributeDescriptor desc)
					{
						return "name".equalsIgnoreCase(desc.getQualifier());
					}

					@Override
					public String toString()
					{
						return "local in  ItemModelConverterPerfTest";
					}
				};
			}
		};
		((DefaultModelConverterRegistry) converterRegistry).removeModelConverterBySourceType("Title");
		((DefaultModelConverterRegistry) converterRegistry).registerModelConverter("Title", TitleModel.class, freshConverter);
		LOG.info("loading new converter for " + TitleModel.class + " type Title");
	}

	@After
	public void unPrepare()
	{
		((DefaultModelConverterRegistry) converterRegistry).removeModelConverterBySourceType("Title");
		((DefaultModelConverterRegistry) converterRegistry).registerModelConverter("Title", TitleModel.class, originalConverter);
		LOG.info("reloading old converter for " + TitleModel.class + " type Title");
	}

	//	@Test
	//	public void testConcurrentAddRemoveForLocalizedAttributes()
	//	{
	//
	//		final TitleModel model = new TitleModel();
	//
	//
	//		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(4, true)
	//		{
	//			@Override
	//			public Runnable newRunner(final int threadNumber)
	//			{
	//				if (threadNumber % 2 == 0)
	//				{
	//					return new ClearModelConverter(model, freshConverter);
	//				}
	//				else
	//				{
	//					return new LoadModelConverter(model, freshConverter, threadNumber);
	//				}
	//
	//			}
	//		};
	//		threads.startAll();
	//		try
	//		{
	//			Thread.sleep(DURATION_SEC * 1000);
	//		}
	//		catch (final InterruptedException e)
	//		{
	//			Thread.currentThread().interrupt();
	//		}
	//		assertTrue(threads.stopAndDestroy(30));
	//
	//		for (final Throwable runner : threads.getErrors().values())
	//		{
	//			runner.printStackTrace();
	//		}
	//
	//		final Field allLocField = ReflectionUtils.findField(model.getClass(), ServicelayerConstants.ATTRIBUTE_SUFFIX
	//				+ ServicelayerConstants.ALL_LOC_ATTRIBUTE_PROPERTY);
	//		allLocField.setAccessible(true);
	//
	//		final Map<String, Map<Locale, Object>> localizedAttributtesFieldValue = (Map<String, Map<Locale, Object>>) ReflectionUtils
	//				.getField(allLocField, model);
	//
	//		//compare with model history
	//		final ItemModelContext ctx = ModelContextUtils.getItemModelContext(model);
	//
	//		final Map<Locale, Object> locals = localizedAttributtesFieldValue.get("name");
	//		LOG.info("retrieved locals from model instance :" + locals);
	//		if (MapUtils.isNotEmpty(locals))
	//		{
	//			final Object fromHistory = ctx.getOriginalValue("name", Locale.ENGLISH);
	//			Assert.assertSame(locals.get(Locale.ENGLISH), fromHistory);
	//		}
	//		else
	//		//empty model map so getOriginalValue should throw an IllegalStateException
	//		{
	//			try
	//			{
	//				final Object faultyEnties = ctx.getOriginalValue("name", Locale.ENGLISH);
	//				Assert.fail("Inconsistence between model content and history , history has additional entries " + faultyEnties);
	//			}
	//			catch (final IllegalStateException ise)
	//			{
	//				Assert.assertEquals("no original value loaded for name", ise.getMessage());
	//			}
	//		}
	//	}

	static abstract class ModelConverterAccessor implements Runnable
	{

		private final ItemModel model;
		private final ItemModelConverter converter;

		public ModelConverterAccessor(final ItemModel model, final ItemModelConverter converter)
		{
			this.model = model;
			this.converter = converter;
		}

		@Override
		public void run()
		{
			modify(model);
		}

		protected ItemModelConverter getConverter()
		{
			return converter;
		}

		abstract void modify(ItemModel tm);
	}

	class ClearModelConverter extends ModelConverterAccessor
	{
		public ClearModelConverter(final ItemModel model, final ItemModelConverter converter)
		{
			super(model, converter);
		}

		@Override
		void modify(final ItemModel tm)
		{
			getConverter().clearLocAttribute(tm, TitleModel.NAME, Locale.ENGLISH);
		}

	}


	//	class LoadModelConverter extends ModelConverterAccessor
	//	{
	//		private final Field allLocField;
	//		private final String value;
	//
	//		public LoadModelConverter(final ItemModel model, final ItemModelConverter converter, final int index)
	//		{
	//			super(model, converter);
	//			allLocField = ReflectionUtils.findField(model.getClass(), ServicelayerConstants.ATTRIBUTE_SUFFIX
	//					+ ServicelayerConstants.ALL_LOC_ATTRIBUTE_PROPERTY);
	//			allLocField.setAccessible(true);
	//			this.value = "value" + index;
	//		}
	//
	//		@Override
	//		void modify(final ItemModel tm)
	//		{
	//			final Map<String, LocMap<Locale, Object>> localizedAttributtesFieldValue = (Map<String, LocMap<Locale, Object>>) ReflectionUtils
	//					.getField(allLocField, tm);
	//			getConverter().preLoadLocAttribute(localizedAttributtesFieldValue, tm, "name", Locale.ENGLISH, value);
	//		}
	//
	//	}


}
