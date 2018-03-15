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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.interceptor.impl.JaloInitDefaultsInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class JaloInitDefaultInterceptorTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private TypeService typeService;
	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;

	private JaloInitDefaultsInterceptor inter;
	private InterceptorContext ctx;

	@Before
	public void setUp()
	{
		inter = new JaloInitDefaultsInterceptor();
		inter.setTypeService(typeService);
		inter.setSessionService(sessionService);
		ctx = new InterceptorContext()
		{
			final Map<String, Object> attributes = new HashMap<String, Object>();
			final Map<String, Set<Locale>> dirtyattributes = new HashMap<String, Set<Locale>>();

			@Override
			public boolean contains(final Object model)
			{
				return false;
			}

			@Override
			public boolean contains(final Object model, final PersistenceOperation operation)
			{
				return false;
			}

			@Override
			public boolean exists(final Object model)
			{
				return false;
			}

			@Override
			public Set<Object> getAllRegisteredElements()
			{
				return Collections.EMPTY_SET;
			}

			@Override
			public Set<Object> getElementsRegisteredFor(final PersistenceOperation operation)
			{
				return null;
			}

			@Override
			public ModelService getModelService()
			{
				return modelService;
			}

			@Override
			public Object getSource(final Object model)
			{
				return null;
			}

			@Override
			public boolean isModified(final Object model)
			{
				return true;
			}

			@Override
			public boolean isModified(final Object model, final String attribute)
			{
				return true;
			}

			@Override
			public boolean isNew(final Object model)
			{
				return true;
			}

			@Override
			public boolean isRemoved(final Object model)
			{
				return false;
			}

			@Override
			public Object getAttribute(final String key)
			{
				return attributes.get(key);
			}

			@Override
			public void setAttribute(final String key, final Object value)
			{
				attributes.put(key, value);
				dirtyattributes.put(key, null);
			}

			@Override
			public void registerElement(final Object model, final Object source)
			{
				// do nothing
			}

			@Override
			public void registerElement(final Object model)
			{
				// do nothing
			}

			@Override
			public void registerElementFor(final Object model, final PersistenceOperation mode)
			{
				// do nothing
			}

			@Override
			public Map<String, Set<Locale>> getDirtyAttributes(final Object model)
			{
				return dirtyattributes;
			}

		};
	}

	@Test(expected = InterceptorException.class)
	public void testWithNull() throws InterceptorException
	{
		inter.onInitDefaults(null, ctx);
	}

	@Test
	public void testInitDefaults() throws InterceptorException
	{
		final ImpExImportCronJobModel cronJob = new ImpExImportCronJobModel();
		assertNull(cronJob.getLocale());
		assertNull(cronJob.getEnableHmcSavedValues());
		assertNull(cronJob.getLogLevelDatabase());
		inter.onInitDefaults(cronJob, ctx);
		assertEquals(Locale.GERMAN.toString(), cronJob.getLocale());
		assertEquals(Boolean.FALSE, cronJob.getEnableHmcSavedValues());
		assertEquals(JobLogLevel.ERROR, cronJob.getLogLevelDatabase());
	}

	@Test
	public void testInitDefaultsNonLocalizedAttr()
	{
		final UnitModel unit = new UnitModel();
		unit.setCode("test");
		unit.setConversion(Double.valueOf(2.3));
		unit.setUnitType("xxx");
		unit.setName("name", Locale.GERMAN);

		modelService.initDefaults(unit);

		assertEquals("test", unit.getCode());
		assertEquals(Double.valueOf(2.3), unit.getConversion());
		assertEquals("xxx", unit.getUnitType());
		assertNull(unit.getName(Locale.ENGLISH));
		assertEquals("name", unit.getName(Locale.GERMAN));
	}
}
