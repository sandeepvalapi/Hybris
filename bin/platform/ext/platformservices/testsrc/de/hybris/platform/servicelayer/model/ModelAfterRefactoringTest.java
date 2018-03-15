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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.impl.DefaultModelConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


public class ModelAfterRefactoringTest extends ServicelayerBaseTest
{
	@Resource
	I18NService i18nService;

	@Resource
	CommonI18NService commonI18NService;

	@Resource
	ModelService modelService;

	@Resource(name = "defaultConverterRegistry")
	DefaultModelConverterRegistry defaultModelConverterRegistry;

	@Before
	public void createLanguages()
	{
		getOrCreateLanguage(Locale.GERMAN.toString());
		getOrCreateLanguage(Locale.ENGLISH.toString());
	}

	@Test
	public void testWrapping()
	{
		final User u = jaloSession.getUser();
		assertNotNull(u);

		final UserModel um1 = modelService.get(u.getPK());
		assertNotNull(um1);
		assertFalse(modelService.isNew(um1));
		assertFalse(modelService.isModified(um1));
		assertFalse(modelService.isRemoved(um1));
		assertTrue(modelService.isUpToDate(um1));
		assertEquals(u.getUid(), um1.getUid());
		assertEquals(u.getPK(), um1.getPk());
		assertEquals(u.getCreationTime(), um1.getCreationtime());

		final UserModel um2 = modelService.get(u);
		assertNotNull(um2);
		assertSame(um1, um2);

		modelService.refresh(um1);
		assertFalse(modelService.isNew(um1));
		assertFalse(modelService.isModified(um1));
		assertFalse(modelService.isRemoved(um1));
		assertTrue(modelService.isUpToDate(um1));
		assertEquals(u.getUid(), um1.getUid());
		assertEquals(u.getPK(), um1.getPk());
		assertEquals(u.getCreationTime(), um1.getCreationtime());
	}

	@Test
	public void testRefresh()
	{
		final User jaloUser = jaloSession.getUser();
		final String nameBefore = jaloUser.getName();
		final long persistenceVersionBefore = jaloUser.getPersistenceVersion();
		try
		{
			final UserModel u = modelService.get(jaloUser);

			assertEquals(jaloSession.getUser().getUid(), u.getUid());
			assertEquals(jaloSession.getUser().getPK(), u.getPk());
			assertEquals(nameBefore, u.getName());
			assertEquals(persistenceVersionBefore, ModelContextUtils.getItemModelContext(u).getPersistenceVersion());
			assertEquals(jaloSession.getUser().getModificationTime(), u.getModifiedtime());

			assertTrue(modelService.isUpToDate(u));

			jaloUser.setName("some name for test");

			assertEquals(nameBefore, u.getName());
			assertEquals(persistenceVersionBefore, ModelContextUtils.getItemModelContext(u).getPersistenceVersion());
			assertEquals("some name for test", jaloUser.getName());
			assertTrue(jaloUser.getPersistenceVersion() > persistenceVersionBefore);

			assertFalse(modelService.isUpToDate(u));

			modelService.refresh(u);
			assertEquals("some name for test", u.getName());
			assertEquals(jaloUser.getPersistenceVersion(), ModelContextUtils.getItemModelContext(u).getPersistenceVersion());
		}
		finally
		{
			jaloUser.setName(nameBefore);
		}
	}

	@Test
	public void testCreation()
	{
		final TitleModel title = modelService.create(TitleModel.class);
		assertTrue(modelService.isNew(title));
		assertTrue(modelService.isModified(title));
		assertFalse(modelService.isRemoved(title));
		assertFalse(modelService.isUpToDate(title));
		assertNull(title.getPk());
		assertNull(title.getCreationtime());
		try
		{
			modelService.getSource(title);
			fail("IllegalStateException expected");
		}
		catch (final IllegalStateException e)
		{
			// fine
		}

		ItemModelContext context = ModelContextUtils.getItemModelContext(title);

		assertNotLoaded(context, Title.CODE);
		assertFalse(context.isDirty(Title.CODE));
		title.setCode("Foo");
		assertNotLoaded(context, Title.CODE);
		assertTrue(context.isDirty(Title.CODE));

		assertFalse(context.isLoaded(Title.NAME, Locale.GERMAN));
		assertFalse(context.isLoaded(Title.NAME, Locale.ENGLISH));
		title.setName("name_de", Locale.GERMAN);
		assertFalse(context.isLoaded(Title.NAME, Locale.GERMAN));
		assertFalse(context.isLoaded(Title.NAME, Locale.ENGLISH));
		assertTrue(context.isDirty(Title.NAME, Locale.GERMAN));
		assertFalse(context.isDirty(Title.NAME, Locale.ENGLISH));

		i18nService.setCurrentLocale(Locale.ENGLISH);
		title.setName("name_en");
		assertFalse(context.isLoaded(Title.NAME, Locale.GERMAN));
		assertFalse(context.isLoaded(Title.NAME, Locale.ENGLISH));
		assertTrue(context.isDirty(Title.NAME, Locale.GERMAN));
		assertTrue(context.isDirty(Title.NAME, Locale.ENGLISH));

		modelService.save(title);
		context = ModelContextUtils.getItemModelContext(title);

		assertFalse(modelService.isNew(title));
		assertFalse(modelService.isModified(title));
		assertFalse(modelService.isRemoved(title));
		assertTrue(modelService.isUpToDate(title));
		assertNotLoaded(context, Title.CODE);
		assertFalse(context.isLoaded(Title.NAME, Locale.GERMAN));
		assertFalse(context.isLoaded(Title.NAME, Locale.ENGLISH));
		assertFalse(context.isDirty(Title.NAME, Locale.GERMAN));
		assertFalse(context.isDirty(Title.NAME, Locale.ENGLISH));
		assertTrue(context.isLoaded(Title.PK));
		assertTrue(context.isLoaded(Title.TYPE));

		assertNotNull(title.getPk());
		assertNotNull(title.getCreationtime());

		assertEquals("Foo", title.getCode());
		assertEquals("name_en", title.getName());
		assertEquals("name_en", title.getName(Locale.ENGLISH));
		assertEquals("name_de", title.getName(Locale.GERMAN));
		i18nService.setCurrentLocale(Locale.GERMAN);
		assertEquals("name_de", title.getName());

		final Title jaloT = jaloSession.getItem(title.getPk());
		assertNotNull(jaloT);
		assertEquals(jaloT, modelService.getSource(title));
	}

	private void assertNotLoaded(final ItemModelContext context, final String qualifier)
	{
		if (!isEagerlyLoaded(context.getItemType(), qualifier))
		{
			assertFalse(context.isLoaded(Title.CODE));
		}
	}

	private void assertLoaded(final ItemModelContext context, final String qualifier) //NOPMD
	{
		assertTrue(context.isLoaded(Title.CODE));
	}

	private boolean isEagerlyLoaded(final String type, final String qualifier)
	{
		return ((ItemModelConverter) defaultModelConverterRegistry.getModelConverterBySourceType(type)).getInfo(qualifier)
				.getAttributeInfo().isPreFetched();
	}
}
