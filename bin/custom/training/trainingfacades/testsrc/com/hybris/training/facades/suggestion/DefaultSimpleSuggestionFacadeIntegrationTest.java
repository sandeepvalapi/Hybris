/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.facades.suggestion;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import com.hybris.training.facades.suggestion.impl.DefaultSimpleSuggestionFacade;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link DefaultSimpleSuggestionFacade}.
 */
@IntegrationTest
public class DefaultSimpleSuggestionFacadeIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private SimpleSuggestionFacade simpleSuggestionFacade;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		userService.setCurrentUser(userService.getAnonymousUser());
		importCsv("/trainingfacades/test/testSimpleSuggestionFacade.csv", "utf-8");
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("testSite"), false);
	}

	@Test
	public void testReferencesForPurchasedInCategory()
	{
		final UserModel user = userService.getUserForUID("dejol");
		userService.setCurrentUser(user);
		List<ProductData> result = simpleSuggestionFacade.getReferencesForPurchasedInCategory("cameras", Collections.EMPTY_LIST,
				false, null);
		Assert.assertEquals(4, result.size());
		result = simpleSuggestionFacade.getReferencesForPurchasedInCategory("cameras", Collections.EMPTY_LIST, false,
				NumberUtils.INTEGER_ONE);
		Assert.assertEquals(1, result.size());
		result = simpleSuggestionFacade.getReferencesForPurchasedInCategory("cameras",
				Arrays.asList(ProductReferenceTypeEnum.SIMILAR), false, null);
		Assert.assertEquals(1, result.size());
		result = simpleSuggestionFacade.getReferencesForPurchasedInCategory("cameras",
				Arrays.asList(ProductReferenceTypeEnum.ACCESSORIES), false, null);
		Assert.assertEquals(2, result.size());
		result = simpleSuggestionFacade.getReferencesForPurchasedInCategory("cameras",
				Arrays.asList(ProductReferenceTypeEnum.ACCESSORIES), true, null);
		Assert.assertEquals(1, result.size());
		final ProductData product = result.get(0);
		Assert.assertEquals("adapterDC", product.getCode());
		Assert.assertEquals("adapter", product.getName());
	}
}
