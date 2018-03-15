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
package de.hybris.platform.servicelayer.session;

import static de.hybris.platform.test.SessionCloneTestUtils.assertClonedContextAttributesEqual;
import static de.hybris.platform.test.SessionCloneTestUtils.cloneViaSerialization;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService.SessionAttributeLoader;
import de.hybris.platform.servicelayer.user.UserService;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class SessionServiceTest extends ServicelayerTransactionalBaseTest
{
	private static final String TEST_USER_KEY = "testUser";
	public static final String SESSION_ATTR = "foo";

	@Resource
	private SessionService sessionService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	@Test
	public void testSetAndGetAttribute() throws Exception
	{
		final UserModel userSet = userService.getUserForUID("admin");

		sessionService.setAttribute(TEST_USER_KEY, userSet);
		final String newDescription = "New description";
		userSet.setDescription(newDescription);
		final UserModel userGotten = sessionService.getAttribute(TEST_USER_KEY);
		assertNotNull("User", userGotten);
		assertSame("User", userSet, userGotten);
		assertEquals("User description", newDescription, userGotten.getDescription());
	}

	@Test
	public void testGetOrLoadAttribute() throws Exception
	{
		final UserModel userSet = userService.getUserForUID("admin");
		assertNull("User is null", sessionService.getAttribute(TEST_USER_KEY));
		final UserModel userGotten = sessionService.getOrLoadAttribute(TEST_USER_KEY, new SessionAttributeLoader<UserModel>()
		{
			@Override
			public UserModel load()
			{
				return userSet;
			}
		});
		assertNotNull("User is null", userGotten);
		assertSame("User", userSet, userGotten);
	}

	@Test
	public void testNewModel() throws Exception
	{
		final UserModel userSet = new UserModel();
		sessionService.setAttribute(TEST_USER_KEY, userSet);
		final UserModel userGot = sessionService.getAttribute(TEST_USER_KEY);
		assertEquals(userSet, userGot);
	}

	@Test
	public void testGetAllAttributes() throws Exception
	{
		final Map slAttributes = sessionService.getAllAttributes();
		final int slAttributesSize = slAttributes.size();
		final Map jaloAttributes = jaloSession.getAttributes();
		final int jaloAttributesSize = jaloAttributes.size();

		assertEquals("All attributes size", jaloAttributesSize, slAttributesSize);

		for (final Object key : jaloAttributes.keySet())
		{
			assertTrue("Session contains: " + key, slAttributes.containsKey(key));
		}
	}

	@Test
	public void testRemoveAttribute()
	{
		final UserModel userSet = new UserModel();
		sessionService.setAttribute(TEST_USER_KEY, userSet);
		assertNotNull(sessionService.getAttribute(TEST_USER_KEY));
		sessionService.removeAttribute(TEST_USER_KEY);
		assertNull(sessionService.getAttribute(TEST_USER_KEY));
	}

	@Test
	public void testSerializability() throws Exception
	{
		final Session original = sessionService.createNewSession();
		final Session serialized = cloneViaSerialization(original);

		assertClonedContextAttributesEqual(original.getAllAttributes(), serialized.getAllAttributes());
	}

	@Test
	public void shouldSilentlyRemoveDeletedItemFromSession()
	{
		// given
		final TitleModel title = createSavedTitle();

		// when
		sessionService.setAttribute(SESSION_ATTR, title);

		// then
		final Object foo = sessionService.getAttribute(SESSION_ATTR);
		assertThat(foo).isNotNull();

		// and when
		modelService.remove(title);

		try
		{
			// then
			final Object fooAfterItemDeletion = sessionService.getAttribute(SESSION_ATTR);
			assertThat(fooAfterItemDeletion).isNull();

			final Object o = sessionService.getAllAttributes().get(SESSION_ATTR);
			assertThat(o).isNull();
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			fail("Deleted catalog version should be silently removed from session");
		}
	}

	@Test
	public void shouldSilentlyRemoveDeletedMapEntryFromSession()
	{
		// given
		final TitleModel title = createSavedTitle();
		final TitleModel titleThatWillBeRemoved = createSavedTitle();

		final Map<Object, Object> map = ImmutableMap.of("title", title, "titleThatWillBeRemoved", titleThatWillBeRemoved);

		// when
		sessionService.setAttribute(SESSION_ATTR, map);

		// then
		final Map mapBeforeRemoval = sessionService.getAttribute(SESSION_ATTR);
		assertThat(mapBeforeRemoval).hasSize(2);

		// and when
		modelService.remove(titleThatWillBeRemoved);

		try
		{
			// then
			final Map mapAfterRemoval = sessionService.getAttribute(SESSION_ATTR);
			assertThat(mapAfterRemoval).hasSize(1);

			final Map mapFromContext = (Map) sessionService.getAllAttributes().get(SESSION_ATTR);
			assertThat(mapFromContext).hasSize(1);
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			fail("Deleted catalog version should be silently removed from session");
		}
	}

	@Test
	public void shouldSilentlyRemoveDeletedListEntryFromSession()
	{
		// given
		final TitleModel title = createSavedTitle();
		final TitleModel titleThatWillBeRemoved = createSavedTitle();

		final List<ItemModel> list = ImmutableList.of(title, titleThatWillBeRemoved);

		// when
		sessionService.setAttribute(SESSION_ATTR, list);

		// then
		final List<ItemModel> listBeforeRemoval = sessionService.getAttribute(SESSION_ATTR);
		assertThat(listBeforeRemoval).hasSize(2);

		// and when
		modelService.remove(titleThatWillBeRemoved);

		try
		{
			// then
			final List listAfterRemoval = sessionService.getAttribute(SESSION_ATTR);
			assertThat(listAfterRemoval).hasSize(1);

			final List listFromContext = (List) sessionService.getAllAttributes().get(SESSION_ATTR);
			assertThat(listFromContext).hasSize(1);
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			fail("Deleted catalog version should be silently removed from session");
		}
	}


	private TitleModel createSavedTitle()
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode(UUID.randomUUID().toString());

		modelService.save(title);
		return title;
	}
}
