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
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests demonstrating usage of the search restriction service.
 */
@DemoTest
public class DefaultSearchRestrictionServiceDemoTest extends ServicelayerTest
{
	@Resource
	private TypeService typeService;
	@Resource
	private SearchRestrictionService searchRestrictionService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
	}

	@After
	public void tearDown()
	{
		// we must ensure that no session search restrictions are left at the session
		// to avoid problems during automatic test item removal
		searchRestrictionService.removalAllSessionSearchRestrictions();
	}

	/**
	 * Demonstrates how to create <code>SessionSearchRestriction</code> object and add it to the session.
	 * 
	 * Test scenario:<br />
	 * 
	 * - prepare <code>SessionSearchRestriction</code> object<br />
	 * - add session search restriction with usage of
	 * {@code SearchRestrictionService#addSessionSearchRestrictions(java.util.Collection)} method<br />
	 */
	@Test
	public void createAndAddSessionSearchRestriction()
	{
		// (given) prepare SessionSearchRestriction object with part of query for Language type
		final ComposedTypeModel restrictedType = typeService.getComposedTypeForClass(LanguageModel.class);
		final SessionSearchRestriction restriction = new SessionSearchRestriction("some code", "{active} IS TRUE", restrictedType);
		final Collection<SessionSearchRestriction> restrictions = new ArrayList<SessionSearchRestriction>();
		restrictions.add(restriction);

		// check whether any session search restrictions are currently in the session
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isEmpty();

		// (when) add session search restriction
		searchRestrictionService.addSessionSearchRestrictions(restrictions);

		// (then) check results
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isNotEmpty();
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).hasSize(1);
		final SessionSearchRestriction restrictionFromTheSession = searchRestrictionService.getSessionSearchRestrictions()
				.iterator().next();
		assertThat(restrictionFromTheSession.getCode()).isEqualTo("some code");
		assertThat(restrictionFromTheSession.getQuery()).isEqualTo("{active} IS TRUE");
		assertThat(restrictionFromTheSession.getRestrictedType()).isEqualTo(restrictedType);

		// test what happens if we create a new item of that type now
		final LanguageModel newLang = modelService.create(LanguageModel.class);
		newLang.setIsocode("NewLang");
		modelService.save(newLang);

		commonI18NService.setCurrentLanguage(newLang);
		assertEquals(newLang, commonI18NService.getCurrentLanguage());
		assertEquals(modelService.getSource(newLang), jaloSession.getSessionContext().getLanguage());
	}

	/**
	 * Demonstrates how to clear all registered session search restrictions.
	 * 
	 * Test scenario:<br />
	 * 
	 * - prepare <code>SessionSearchRestriction</code> object<br />
	 * - add session search restriction with usage of
	 * {@code SearchRestrictionService#addSessionSearchRestrictions(java.util.Collection)} method<br />
	 * - clear session search restrictions
	 */
	@Test
	public void clearSessionSearchRestriction()
	{
		// (given) prepare SessionSearchRestriction object with part of query for Language type
		final ComposedTypeModel restrictedType = typeService.getComposedTypeForClass(LanguageModel.class);
		final SessionSearchRestriction restriction = new SessionSearchRestriction("some code", "{active} IS TRUE", restrictedType);
		final Collection<SessionSearchRestriction> restrictions = new ArrayList<SessionSearchRestriction>();
		restrictions.add(restriction);

		// check whether any session search restrictions are currently in the session
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isEmpty();

		// add session search restriction
		searchRestrictionService.addSessionSearchRestrictions(restrictions);

		// check whether any session search restrictions are currently in the session
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isNotEmpty();

		// (when) clear session search restrictions
		searchRestrictionService.clearSessionSearchRestrictions();

		// (then) check results
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isEmpty();
	}

	/**
	 * Demonstrates how to remove few session search restrictions from list of registered session search restrictions.
	 * 
	 * Test scenario:<br />
	 * 
	 * - prepare <code>SessionSearchRestriction</code> objects<br />
	 * - add session search restriction with usage of
	 * {@code SearchRestrictionService#addSessionSearchRestrictions(java.util.Collection)} method<br />
	 * - remove one session search restriction from list of registered session search restrictions.
	 */
	@Test
	public void removeSessionSearchRestriction()
	{
		// (given) prepare SessionSearchRestriction objects with part of query for Language type
		final ComposedTypeModel restrictedType = typeService.getComposedTypeForClass(LanguageModel.class);
		final SessionSearchRestriction restriction1 = new SessionSearchRestriction("some code 1", "{active} IS TRUE",
				restrictedType);
		final SessionSearchRestriction restriction2 = new SessionSearchRestriction("some code 2", "{foo} = 'bar'", restrictedType);
		final SessionSearchRestriction restriction3 = new SessionSearchRestriction("some code 3", "{baz} != 'bam'", restrictedType);
		final Collection<SessionSearchRestriction> restrictions = new ArrayList<SessionSearchRestriction>();
		restrictions.add(restriction1);
		restrictions.add(restriction2);
		restrictions.add(restriction3);

		// check whether any session search restrictions are currently in the session
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).isEmpty();

		// add session search restriction
		searchRestrictionService.addSessionSearchRestrictions(restrictions);

		// check whether any session search restrictions are currently in the session
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).hasSize(3);

		// (when) remove one session search restriction from list of registered session search restrictions
		final ArrayList<SessionSearchRestriction> restrictionsToRemove = new ArrayList<SessionSearchRestriction>();
		restrictionsToRemove.add(restriction2);
		searchRestrictionService.removeSessionSearchRestrictions(restrictionsToRemove);

		// (then) check results
		assertThat(searchRestrictionService.getSessionSearchRestrictions()).hasSize(2);
	}

	/**
	 * Demonstrates how enable and disable search restrictions (both - session and persistent model based restrictions).
	 * 
	 * Test scenario:<br />
	 * 
	 * - check whether restrictions are enabled<br />
	 * - disable restrictions<br />
	 * - check whether restrictions are disabled<br />
	 * - enable restrictions again<br />
	 * - check whether restrictions are enabled<br />
	 */
	@Test
	public void enableAndDisableSearchRestriction()
	{
		// check whether restrictions are already enabled
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).overridingErrorMessage(
				"Initially restrictions should be enabled").isTrue();

		// disable search restrictions
		searchRestrictionService.disableSearchRestrictions();
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).isFalse();

		// enable restrictions again
		searchRestrictionService.enableSearchRestrictions();
		assertThat(searchRestrictionService.isSearchRestrictionsEnabled()).isTrue();
	}

	/**
	 * Demonstrates how to check whether type has restrictions then create model based restrictions for some type and
	 * next obtain them from that type.
	 * 
	 * Test scenario:<br />
	 * - check whether model has some restrictions<br />
	 * - create restrictions for type <br />
	 * - get restrictions from model
	 */
	@Test
	public void checkSearchRestrictionCreateAndGetSearchRestrictionsForType()
	{
		final ComposedTypeModel restrictedType = typeService.getComposedTypeForClass(LanguageModel.class);
		final PrincipalModel principal = userService.getUserForUID("ahertz");

		// check whether LanguageModel have any restrictions
		assertThat(searchRestrictionService.hasRestrictions(principal, false, restrictedType)).isFalse();

		// (given) create restriction for LanguageModel type 
		final SearchRestrictionModel searchRestriction = modelService.create(SearchRestrictionModel.class);
		searchRestriction.setCode("some code");
		searchRestriction.setActive(Boolean.TRUE);
		searchRestriction.setQuery("{active} IS TRUE");
		searchRestriction.setRestrictedType(restrictedType);
		searchRestriction.setPrincipal(principal);
		searchRestriction.setGenerate(Boolean.TRUE);
		modelService.save(searchRestriction);

		// (when) search for restrictions for LanguageModel type
		final Collection<ComposedTypeModel> types = new ArrayList<ComposedTypeModel>();
		types.add(restrictedType);
		final Collection<SearchRestrictionModel> restrictions = searchRestrictionService.getSearchRestrictions(principal, false,
				types);

		// (then) check result
		assertThat(restrictions).isNotNull();
		assertThat(restrictions).isNotEmpty();
		assertThat(restrictions.iterator().next()).isEqualTo(searchRestriction);
	}
}
