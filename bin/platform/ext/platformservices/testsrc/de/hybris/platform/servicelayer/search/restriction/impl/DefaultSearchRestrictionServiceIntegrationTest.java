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
package de.hybris.platform.servicelayer.search.restriction.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.fest.assertions.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultSearchRestrictionServiceIntegrationTest extends ServicelayerTest
{
	@Resource
	private SearchRestrictionService searchRestrictionService;
	@Resource
	private TypeService typeService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		// Create data for tests
		createCoreData();
		createDefaultCatalog();
		createDefaultUsers();
	}

	@After
	public void tearDown()
	{
		// we must ensure that no session search restrictions are left at the session
		// to avoid problems during automatic test item removal
		searchRestrictionService.removalAllSessionSearchRestrictions();
	}


	@Test
	public void shouldAddSessionSearchRestrictions()
	{
		// given
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isEmpty();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		final SessionSearchRestriction restriction1 = new SessionSearchRestriction("restr1", "foo != bar", composedTypeModel);
		final SessionSearchRestriction restriction2 = new SessionSearchRestriction("restr2", "bar IS TRUE", composedTypeModel);
		final Collection<SessionSearchRestriction> restrictions = new ArrayList<SessionSearchRestriction>();
		restrictions.add(restriction1);
		restrictions.add(restriction2);

		// when
		searchRestrictionService.addSessionSearchRestrictions(restrictions);

		// then
		final Collection<SessionSearchRestriction> foundRestrictions = searchRestrictionService.getSessionSearchRestrictions();
		assertThat(foundRestrictions).isNotEmpty();
		assertThat(foundRestrictions).hasSize(2);
		assertThat(foundRestrictions).satisfies(new Condition<Collection<?>>()
		{

			@Override
			public boolean matches(final Collection<?> restrictions)
			{
				for (final Object restriction : restrictions)
				{
					if (!((SessionSearchRestriction) restriction).getCode().startsWith("restr"))
					{
						return false;
					}
				}
				return true;
			}

		});
	}

	@Test
	public void shouldAddSessionSearchRestrictionsToExistingOnesWithNewInstanceOfCollection()
	{
		// given
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		final SessionSearchRestriction restriction1 = new SessionSearchRestriction("restr1", "foo != bar", composedTypeModel);
		final SessionSearchRestriction restriction2 = new SessionSearchRestriction("restr2", "bar IS TRUE", composedTypeModel);
		final Collection<SessionSearchRestriction> existingRestrictions = new ArrayList<SessionSearchRestriction>();
		existingRestrictions.add(restriction1);
		existingRestrictions.add(restriction2);
		searchRestrictionService.addSessionSearchRestrictions(existingRestrictions);

		final SessionSearchRestriction restriction3 = new SessionSearchRestriction("restr3", "xxx = 'yyy'", composedTypeModel);
		final SessionSearchRestriction restriction4 = new SessionSearchRestriction("restr4", "b > a", composedTypeModel);
		final Collection<SessionSearchRestriction> restrictionsToAdd = new ArrayList<SessionSearchRestriction>();
		restrictionsToAdd.add(restriction3);
		restrictionsToAdd.add(restriction4);

		// when
		searchRestrictionService.addSessionSearchRestrictions(existingRestrictions);

		// then
		final Collection<SessionSearchRestriction> foundRestrictions = searchRestrictionService.getSessionSearchRestrictions();
		assertThat(foundRestrictions).isNotEmpty();
		assertThat(foundRestrictions).hasSize(4);
		assertThat(foundRestrictions).satisfies(new Condition<Collection<?>>()
		{

			@Override
			public boolean matches(final Collection<?> restrictions)
			{
				for (final Object restriction : restrictions)
				{
					if (!((SessionSearchRestriction) restriction).getCode().startsWith("restr"))
					{
						return false;
					}
				}
				return true;
			}

		});
	}

	@Test
	public void shouldAddSessionSearchRestrictionsUsingVarargs()
	{
		// given
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		final SessionSearchRestriction restriction1 = new SessionSearchRestriction("restr1", "foo != bar", composedTypeModel);
		final SessionSearchRestriction restriction2 = new SessionSearchRestriction("restr2", "bar IS TRUE", composedTypeModel);

		// when
		searchRestrictionService.addSessionSearchRestrictions(restriction1, restriction2);

		// then
		final Collection<SessionSearchRestriction> foundRestrictions = searchRestrictionService.getSessionSearchRestrictions();
		assertThat(foundRestrictions).isNotEmpty();
		assertThat(foundRestrictions).hasSize(2);
		assertThat(foundRestrictions).satisfies(new Condition<Collection<?>>()
		{

			@Override
			public boolean matches(final Collection<?> restrictions)
			{
				for (final Object restriction : restrictions)
				{
					if (!((SessionSearchRestriction) restriction).getCode().startsWith("restr"))
					{
						return false;
					}
				}
				return true;
			}

		});
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetSearchRestrictionsWithoutGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some restriction for TriggerModel
		final SearchRestrictionModel createdRestriction = createRestriction(principal, composedTypeModel, "FooBar",
				"WHERE foo != 'bar'", Boolean.TRUE);

		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getSearchRestrictions(principal,
				false, types);

		// Check assertions
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(1);
		assertThat(foundRestictions).containsOnly(createdRestriction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetSearchRestrictionsWithGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some restriction for TriggerModel for principal 
		final SearchRestrictionModel createdRestriction = createRestriction(principal, composedTypeModel, "FooBar",
				"WHERE foo != 'bar'", Boolean.TRUE);

		// Create some restriction for TriggerModel for principal 
		final SearchRestrictionModel createGroupRestriction = createRestriction(principal.getAllGroups().iterator().next(),
				composedTypeModel, "BazBar", "WHERE foo != 'bar'", Boolean.TRUE);


		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getSearchRestrictions(principal, true,
				types);

		// Check assertions
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(2);
		assertThat(foundRestictions).contains(createdRestriction, createGroupRestriction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getSearchRestrictionsForType(ComposedTypeModel)}
	 * .
	 */
	@Test
	public void testGetSearchRestrictionsForType()
	{
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);

		// Get user
		final PrincipalModel principal1 = userService.getUserForUID("ahertz");
		final PrincipalModel principal2 = userService.getUserForUID("abrode");

		// Create some restriction for TriggerModel for principal1 and principal2
		final SearchRestrictionModel createdRestriction1 = createRestriction(principal1, composedTypeModel, "FooBar",
				"WHERE foo != 'bar'", Boolean.TRUE);
		final SearchRestrictionModel createdRestriction2 = createRestriction(principal2, composedTypeModel, "BazBar",
				"WHERE baz > bar", Boolean.TRUE);


		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService
				.getSearchRestrictionsForType(composedTypeModel);

		// Check assertions
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(2);
		assertThat(foundRestictions).contains(createdRestriction1, createdRestriction2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getInactiveSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetInactiveSearchRestrictionsWithoutGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some active restriction for TriggerModel for principal "ahertz's"
		createRestriction(principal, composedTypeModel, "FooBarActive", "WHERE foo != 'bar'", Boolean.TRUE);
		// Create some inactive restriction for TriggerModel for principal "ahertz's"
		final SearchRestrictionModel createdInactiveRestriction = createRestriction(principal, composedTypeModel, "FooBarInactive",
				"WHERE foo != 'bar'", Boolean.FALSE);


		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getInactiveSearchRestrictions(
				principal, false, types);

		// Check assertions		
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(1);
		assertThat(foundRestictions).containsOnly(createdInactiveRestriction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getInactiveSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetInactiveSearchRestrictionsWithGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some active restriction for TriggerModel for principal "ahertz's"
		createRestriction(principal, composedTypeModel, "FooBarActive", "WHERE foo != 'bar'", Boolean.TRUE);
		// Create some inactive restriction for TriggerModel for principal "ahertz's"
		final SearchRestrictionModel createdInactiveRestriction = createRestriction(principal, composedTypeModel, "FooBarInactive",
				"WHERE foo != 'bar'", Boolean.FALSE);
		// Create some inactive restriction for TriggerModel for principal "ahertz's" group UserGroup
		final SearchRestrictionModel createGroupRestriction = createRestriction(principal.getAllGroups().iterator().next(),
				composedTypeModel, "BazBarGroup", "WHERE foo != 'bar'", Boolean.FALSE);


		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getInactiveSearchRestrictions(
				principal, true, types);

		// Check assertions		
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(2);
		assertThat(foundRestictions).contains(createdInactiveRestriction, createGroupRestriction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getActiveSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetActiveSearchRestrictionsWithoutGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some active restriction for TriggerModel for principal "ahertz"
		final SearchRestrictionModel createdActiveRestriction = createRestriction(principal, composedTypeModel, "FooBarActive",
				"WHERE foo != 'bar'", Boolean.TRUE);
		// Create some inactive restriction for TriggerModel for principal "ahertz"
		createRestriction(principal, composedTypeModel, "FooBarInactive", "WHERE foo != 'bar'", Boolean.FALSE);


		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getActiveSearchRestrictions(principal,
				false, types);

		// Check assertions		
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(1);
		assertThat(foundRestictions).containsOnly(createdActiveRestriction);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.impl.DefaultSearchRestrictionService#getActiveSearchRestrictions(PrincipalModel, boolean, Collection)}
	 * .
	 */
	@Test
	public void testGetActiveSearchRestrictionsWithGroups()
	{
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TriggerModel.class);
		types.add(composedTypeModel);

		// Get user
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// Create some inactive restriction for TriggerModel for principal "ahertz"
		createRestriction(principal, composedTypeModel, "FooBarInActive", "WHERE foo != 'bar'", Boolean.FALSE);
		// Create some active restriction for TriggerModel for principal "ahertz"
		final SearchRestrictionModel createdActiveRestriction = createRestriction(principal, composedTypeModel, "FooBarActive",
				"WHERE foo != 'bar'", Boolean.TRUE);
		// Create some active restriction for TriggerModel for principal "ahertz's" group UserGroup
		final SearchRestrictionModel createGroupRestriction = createRestriction(principal.getAllGroups().iterator().next(),
				composedTypeModel, "BazBarGroup", "WHERE foo != 'bar'", Boolean.TRUE);

		// Trigger tested method and obtain search restriction
		final Collection<SearchRestrictionModel> foundRestictions = searchRestrictionService.getActiveSearchRestrictions(principal,
				true, types);

		// Check assertions		
		assertThat(foundRestictions).isNotEmpty();
		assertThat(foundRestictions.size()).isEqualTo(2);
		assertThat(foundRestictions).contains(createdActiveRestriction, createGroupRestriction);
	}

	private SearchRestrictionModel createRestriction(final PrincipalModel principal, final ComposedTypeModel type,
			final String code, final String query, final Boolean active)
	{
		final SearchRestrictionModel model = (SearchRestrictionModel) modelService.create(SearchRestrictionModel.class);
		model.setCode(code);
		model.setActive(active);
		model.setQuery(query);
		model.setRestrictedType(type);
		model.setPrincipal(principal);
		model.setGenerate(Boolean.TRUE);
		modelService.save(model);
		return model;
	}

}
