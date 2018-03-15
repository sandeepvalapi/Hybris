/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.validation.coverage.daos.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cockpit.model.CoverageConstraintGroupModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCoverageConstraintGroupDaoIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	@Resource
	TypeService typeService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	private volatile CoverageConstraintGroupModel constraintGroup = null;

	@Before
	public void setUp() throws Exception
	{
		constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setCoverageDomainID("domain");
		final ComposedTypeModel composedTypeGenericItem = typeService.getComposedTypeForCode("GenericItem");
		constraintGroup.setDedicatedTypes(Collections.singleton(composedTypeGenericItem));
		modelService.save(constraintGroup);
	}

	@Test
	public void shouldFindCoverageConstraintGroupForTypeFromTypeHierarchy()
	{
		//given
		final DefaultCoverageConstraintGroupDao dao = new DefaultCoverageConstraintGroupDao();
		dao.setFlexibleSearchService(flexibleSearchService);
		final ComposedTypeModel composedTypeProduct = typeService.getComposedTypeForCode("Product");

		//when
		final Collection<CoverageConstraintGroupModel> groups = dao.findGroupsForDomainAndType("domain", composedTypeProduct);

		//	test
		assertThat(groups.size()).isEqualTo(1);
		assertThat(groups).contains(constraintGroup);
	}
}
