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
package de.hybris.platform.servicelayer.internal.i18n.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultLocalizationServiceTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(DefaultLocalizationServiceTest.class);

	@Resource
	private final ModelService modelService = null;

	@Resource
	private final I18NService i18nService = null;

	@Resource
	private final UserService userService = null;

	@Resource
	private final TypeService typeService = null;

	private UserModel restrictedUserOne = null;

	private PrincipalGroupModel restrictedGroupOne = null;

	private SearchRestrictionModel searchRestrictionOne = null;

	private Set<Locale> startupLocales = null;

	private LanguageModel koreanRestricted = null;

	private LanguageModel chineseRestricted = null;

	/**
	 * create test users
	 */
	@Before
	public void setUp() throws Exception
	{

		startupLocales = i18nService.getSupportedLocales();

		restrictedGroupOne = modelService.create(UserGroupModel.class);
		restrictedGroupOne.setUid("restricted group");

		modelService.save(restrictedGroupOne);

		restrictedUserOne = modelService.create(UserModel.class);
		restrictedUserOne.setUid("userOne");
		restrictedUserOne.setGroups(Collections.singleton(restrictedGroupOne));

		modelService.save(restrictedUserOne);

		searchRestrictionOne = modelService.create(SearchRestrictionModel.class);
		searchRestrictionOne.setActive(Boolean.TRUE);
		searchRestrictionOne.setGenerate(Boolean.TRUE); //??
		searchRestrictionOne.setCode("test_restriction");
		searchRestrictionOne.setPrincipal(restrictedGroupOne);
		searchRestrictionOne.setQuery("{" + C2LItemModel.ISOCODE + "} not like '%restricted%'");
		searchRestrictionOne.setRestrictedType(typeService.getComposedTypeForClass(LanguageModel.class));
		modelService.save(searchRestrictionOne);

		//create korean like  language
		koreanRestricted = modelService.create(LanguageModel.class);
		koreanRestricted.setIsocode("restricted_" + Locale.KOREA.getCountry());
		koreanRestricted.setName("restricted_" + Locale.KOREA.getDisplayName());

		modelService.save(koreanRestricted);

		//create chinese like  language
		chineseRestricted = modelService.create(LanguageModel.class);
		chineseRestricted.setIsocode("restricted_" + Locale.PRC.getCountry());
		chineseRestricted.setName("restricted_" + Locale.PRC.getDisplayName());

		modelService.save(chineseRestricted);



	}

	/**
	 * check available locales for user
	 */
	@Test
	public void testSupportedLocales()
	{

		final Set anonymouseLocale = new HashSet<Locale>(startupLocales);
		anonymouseLocale.add(new Locale("restricted", Locale.KOREA.getCountry()));
		anonymouseLocale.add(new Locale("restricted", Locale.PRC.getCountry()));

		checkLocales(anonymouseLocale);

		userService.setCurrentUser(restrictedUserOne);

		checkLocales(startupLocales);

		restrictedUserOne.setGroups(Collections.EMPTY_SET);

		modelService.save(restrictedUserOne);
		//if we would have a full unique caching for supported locales , full admin locale should fit the result
		//checkLocales(anonymouseLocale);

	}


	/**
	 *
	 */
	private void checkLocales(final Set<Locale> expected)
	{
		final Set<Locale> locales = i18nService.getSupportedLocales();
		LOG.info(" available loclaes for a " + userService.getCurrentUser().getUid() + " ,  " + locales);
		Assert.assertEquals(expected, locales);
	}

}
