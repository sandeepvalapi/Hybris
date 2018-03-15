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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.internal.model.impl.AttributeProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.strategies.DefaultFetchStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;


@UnitTest
public class ModelEntityContextTest
{
	private final PK itempk = PK.createFixedPK(1, 123);

	private final Map<String, Object> attributes = new HashMap<String, Object>();
	private final Map<String, Map<Locale, Object>> locAttributes = new HashMap<String, Map<Locale, Object>>();

	private final Map<String, Object> dynamicAttributes = new HashMap<String, Object>();
	private final Map<String, Map<Locale, Object>> dynamicLocAttributes = new HashMap<String, Map<Locale, Object>>();

	private final Locale LOCALE_EN = Locale.ENGLISH;

	private final String TENANT_ID = "Master";
	private final String _CODE = "code";

	private final String NAME_EN = "english_name";
	private final String NAME_DEFAULT = "default_locale_name";

	private final String SUB_CATEGORIES = "all_sub_categories";

	private final String ASSIGNMENT = "assignment";
	private final String ASSIGNMENT_DEFAULT = "assignment";

	@Test
	public void testLoadedLocalizedAttributes()
	{
		final ItemModelInternalContext ctx = createItemContext(itempk, DummyModel._TYPECODE);
		final DummyModel dummyModel = new DummyModel(ctx);
		configureAttributes();

		assertFalse(ctx.isLoaded(DummyModel.NAME, LOCALE_EN));
		assertEquals(NAME_EN, dummyModel.getName(LOCALE_EN));
		assertTrue(ctx.isLoaded(DummyModel.NAME, LOCALE_EN));
		assertFalse(ctx.isDirty(DummyModel.NAME, LOCALE_EN));
		final String newEnName = "new_en_name";
		dummyModel.setName(newEnName, LOCALE_EN);
		assertEquals(newEnName, dummyModel.getName(LOCALE_EN));
		assertTrue(ctx.isLoaded(DummyModel.NAME, LOCALE_EN));
		assertTrue(ctx.isDirty(DummyModel.NAME, LOCALE_EN));

		assertFalse(ctx.isLoaded(DummyModel.NAME, null));
		dummyModel.setName(NAME_DEFAULT);
		assertEquals(NAME_DEFAULT, dummyModel.getName());
		assertTrue(ctx.isDirty(DummyModel.NAME, null));
	}

	@Test
	public void testLoadedUnlocalizedAttributes()
	{
		final ItemModelInternalContext ctx = createItemContext(itempk, DummyModel._TYPECODE);
		final DummyModel dummyModel = new DummyModel(ctx);
		configureAttributes();

		assertEquals(itempk, dummyModel.getPk());
		assertEquals(itempk, ctx.getPK());
		assertFalse(ctx.isNew());
		assertFalse(ctx.isDirty());
		assertFalse(ctx.isLoaded(DummyModel.CODE));
		assertFalse(ctx.isLoaded(DummyModel.DISCOUNTSINCLUDEDELIVERYCOST));
		assertFalse(ctx.isLoaded(DummyModel.NAME, LOCALE_EN));

		dummyModel.getCode();
		assertTrue(ctx.isLoaded(DummyModel.CODE));
		assertEquals(_CODE, dummyModel.getCode());

		assertTrue(dummyModel.isDiscountsIncludeDeliveryCost());
		assertTrue(ctx.isLoaded(DummyModel.DISCOUNTSINCLUDEDELIVERYCOST));

		dummyModel.getName(LOCALE_EN);
		assertTrue(ctx.isLoaded(DummyModel.NAME, LOCALE_EN));
		dummyModel.getName();
		assertTrue(ctx.isLoaded(DummyModel.NAME, null));
	}

	@Test
	public void testGeneralAttributes()
	{
		final ItemModelInternalContext ctx = createItemContext(itempk, DummyModel._TYPECODE);
		final DummyModel dummyModel = new DummyModel(ctx);
		assertEquals(itempk, dummyModel.getPk());
		assertEquals(DummyModel._TYPECODE, dummyModel.getItemtype());
	}

	@Test
	public void testDirtyAttributes()
	{
		final ItemModelInternalContext ctx = createItemContext(itempk, DummyModel._TYPECODE);
		final DummyModel dummyModel = new DummyModel(ctx);
		configureAttributes();

		dummyModel.setDiscountsIncludeDeliveryCost(false);
		assertTrue(ctx.isDirty(DummyModel.DISCOUNTSINCLUDEDELIVERYCOST));

		final String newCode = "new_code";
		dummyModel.setCode(newCode);
		assertFalse(ctx.isLoaded(DummyModel.CODE));
		assertEquals(newCode, dummyModel.getCode());
		assertTrue(ctx.isDirty(DummyModel.CODE));
		assertTrue(ctx.isDirty());

		//YXX true or false after the setCode(String) is called, the interface shouldn't define that, but our impl won't load the value.
		assertFalse(ctx.isLoaded(DummyModel.CODE));
	}

	@Test
	public void testDynamicAttributes()
	{
		final ItemModelInternalContext ctx = createItemContext(itempk, DummyModel._TYPECODE);
		final DummyModel dummyModel = new DummyModel(ctx);
		configureDynamicAttributes();

		final Collection<String> allSubcategories = dummyModel.getAllSubcategories();
		assertEquals(1, allSubcategories.size());
		assertEquals(SUB_CATEGORIES, allSubcategories.iterator().next());

		assertEquals(ASSIGNMENT, dummyModel.getAssignment(LOCALE_EN));

		dummyModel.setAssignment(ASSIGNMENT_DEFAULT);
		assertEquals(ASSIGNMENT_DEFAULT, dummyModel.getAssignment());
	}

	private void configureAttributes()
	{
		//unlocalized attributes
		attributes.put(DummyModel.CODE, _CODE);
		attributes.put(DummyModel.DISCOUNTSINCLUDEDELIVERYCOST, Boolean.TRUE);

		//localized attributes
		final Map<Locale, Object> locObjects = new HashMap<Locale, Object>();
		locObjects.put(LOCALE_EN, NAME_EN);
		locAttributes.put(DummyModel.NAME, locObjects);
	}

	private void configureDynamicAttributes()
	{
		//unlocalized attributes
		dynamicAttributes.put(DummyModel.ALLSUBCATEGORIES, Collections.singletonList(SUB_CATEGORIES));

		//localized attributes
		final Map<Locale, Object> dynamLocObjects = new HashMap<Locale, Object>();
		dynamLocObjects.put(LOCALE_EN, ASSIGNMENT);
		dynamicLocAttributes.put(DummyModel.ASSIGNMENT, dynamLocObjects);
	}

	private ItemModelInternalContext createItemContext(final PK pk, final String itemType)
	{
		final ItemContextBuilder builder = new ItemContextBuilder();
		builder.setPk(pk);
		builder.setItemType(itemType);
		builder.setTenantID(TENANT_ID);
		builder.setValueHistory(new ModelValueHistory());
		builder.setFetchStrategy(new DefaultFetchStrategy());
		builder.setLocaleProvider(new MockLocaleProvider(Locale.GERMAN, null, null));
		builder.setDynamicAttributesProvider(new MockDynamicAttributesProvider(dynamicAttributes, dynamicLocAttributes));
		builder.setAttributeProvider(new MockAttributeProvider(attributes, locAttributes));
		return builder.build();
	}

	static class MockLocaleProvider implements LocaleProvider
	{
		private final Locale locale;

		private final Map<Locale, Locale> dataLocaleMap;

		private final Map<Locale, List<Locale>> fallbacksMap;

		public MockLocaleProvider(final Locale locale, final Map<Locale, Locale> dataLocaleMap,
				final Map<Locale, List<Locale>> fallbacksMap)
		{
			this.locale = locale;
			this.dataLocaleMap = dataLocaleMap;
			this.fallbacksMap = fallbacksMap;
		}

		@Override
		public Locale getCurrentDataLocale()
		{
			return this.locale;
		}

		@Override
		public List<Locale> getFallbacks(final Locale requestedLocale)
		{
			final List<Locale> ret = fallbacksMap != null ? fallbacksMap.get(requestedLocale) : null;
			return CollectionUtils.isNotEmpty(ret) ? ret : Collections.EMPTY_LIST;
		}

		@Override
		public boolean isFallbackEnabled()
		{
			return fallbacksMap != null;
		}

		@Override
		public Locale toDataLocale(final Locale external)
		{
			Locale ret = external;
			if (dataLocaleMap != null)
			{
				ret = dataLocaleMap.get(external);
				if (ret == null)
				{
					throw new IllegalArgumentException("locale " + external + " has got no data locale");
				}
			}
			return ret;
		}
	}

	static class MockAttributeProvider implements AttributeProvider
	{
		final Map<String, Object> attributes;
		final Map<String, Map<Locale, Object>> locAttributes;

		MockAttributeProvider(final Map<String, Object> attributes, final Map<String, Map<Locale, Object>> locAttributes)
		{
			this.attributes = attributes;
			this.locAttributes = locAttributes;
		}

		@Override
		public Object getAttribute(final String qualifier)
		{
			if (!attributes.containsKey(qualifier))
			{
				throw new IllegalArgumentException("no attribute " + qualifier);
			}
			return attributes.get(qualifier);
		}

		@Override
		public Object getLocalizedAttribute(final String qualifier, final Locale loc)
		{
			if (!locAttributes.containsKey(qualifier))
			{
				throw new IllegalArgumentException("no localized attribute " + qualifier);
			}
			return locAttributes.get(qualifier).get(loc);
		}
	}


	static class MockDynamicAttributesProvider implements DynamicAttributesProvider
	{
		final Map<String, Object> dynamicAttributes;
		final Map<String, Map<Locale, Object>> dynamicLocAttributes;

		MockDynamicAttributesProvider(final Map<String, Object> dynamicAttributes,
				final Map<String, Map<Locale, Object>> dynamicLocAttributes)
		{
			this.dynamicAttributes = dynamicAttributes;
			this.dynamicLocAttributes = dynamicLocAttributes;
		}

		@Override
		public void set(final AbstractItemModel model, final String attribute, final Object value)
		{
			dynamicAttributes.put(attribute, value);
		}

		@Override
		public Object get(final AbstractItemModel model, final String attribute)
		{
			if (!dynamicAttributes.containsKey(attribute))
			{
				throw new IllegalArgumentException("no attribute " + attribute);
			}
			return dynamicAttributes.get(attribute);
		}

		@Override
		public void setLocalized(final AbstractItemModel model, final String attribute, final Object value, final Locale loc)
		{
			final Map<Locale, Object> locAttributes = new HashMap<Locale, Object>();
			locAttributes.put(loc, value);
			dynamicLocAttributes.put(attribute, locAttributes);
		}

		@Override
		public Object getLocalized(final AbstractItemModel model, final String attribute, final Locale loc)
		{
			if (!dynamicLocAttributes.containsKey(attribute))
			{
				throw new IllegalArgumentException("no localized attribute " + attribute);
			}
			return dynamicLocAttributes.get(attribute).get(loc);
		}

		@Override
		public boolean isDynamic(final String attributeName)
		{
			return dynamicAttributes.containsKey(attributeName);
		}

	}
}
