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
package de.hybris.platform.servicelayer.security.dynamic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class PrincipalAllSearchRestrictionsAttributeHandlerTest
{

	private final PrincipalAllSearchRestrictionsAttributeHandler handler = new PrincipalAllSearchRestrictionsAttributeHandler();
	@Mock
	private PrincipalModel principalModel;
	@Mock
	private PrincipalGroupModel group1;
	@Mock
	private PrincipalGroupModel group2;
	@Mock
	private PrincipalGroupModel group3;
	@Mock
	private SearchRestrictionModel restiction1;
	@Mock
	private SearchRestrictionModel restiction2;
	@Mock
	private SearchRestrictionModel restiction3;
	@Mock
	private SearchRestrictionModel restiction4;
	@Mock
	private SearchRestrictionModel restiction5;
	@Mock
	private SearchRestrictionModel restiction6;
	@Mock
	private SearchRestrictionModel restiction7;
	@Mock
	private SearchRestrictionModel restiction8;

	@Test
	public void shouldReturnEmptyCollectionIfPrincipalDoesNotHaveRestrictionsAndZeroGroups() throws Exception
	{
		// given
		given(principalModel.getSearchRestrictions()).willReturn(Collections.emptyList());
		given(principalModel.getGroups()).willReturn(Collections.emptySet());

		// when
		final Collection<SearchRestrictionModel> result = handler.get(principalModel);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldReturnEmptyCollectionIfPrincipalHasNullRestrictionsAndZeroGroups() throws Exception
	{
		// given
		given(principalModel.getSearchRestrictions()).willReturn(null);
		given(principalModel.getAllGroups()).willReturn(Collections.emptySet());

		// when
		final Collection<SearchRestrictionModel> result = handler.get(principalModel);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldReturnEmptyCollectionIfPrincipalHasEmptyRestrictionsAndNullGroups() throws Exception
	{
		// given
		given(principalModel.getSearchRestrictions()).willReturn(Collections.emptyList());
		given(principalModel.getAllGroups()).willReturn(null);

		// when
		final Collection<SearchRestrictionModel> result = handler.get(principalModel);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	public void shouldReturnOnlyPrincipalRestrictionsIfPrincipalHasRestrictionsButEmptyGroups() throws Exception
	{
		// given
		given(principalModel.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction1, restiction2));
		given(principalModel.getAllGroups()).willReturn(Collections.emptySet());

		// when
		final Collection<SearchRestrictionModel> result = handler.get(principalModel);

		// then
		assertThat(result).hasSize(2).containsOnly(restiction1, restiction2);
	}

	@Test
	public void shouldCombineRestrictionsFromPrincipalAndItsGroups() throws Exception
	{
		// given
		given(principalModel.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction1, restiction2));
		given(principalModel.getAllGroups()).willReturn(Sets.newHashSet(group1, group2, group3));
		given(group1.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction3, restiction4));
		given(group2.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction5, restiction6));
		given(group3.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction7, restiction8));

		// when
		final Collection<SearchRestrictionModel> result = handler.get(principalModel);

		// then
		assertThat(result).hasSize(8).containsOnly(restiction1, restiction2, restiction3, restiction4, restiction5, restiction6,
				restiction7, restiction8);
	}

	@Test
	public void shouldCombineRestrictionsFromPrincipalAndItsGroupsEvenIfOneOfGroupsReturnsNullRestrictions() throws Exception
	{
        // given
        given(principalModel.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction1, restiction2));
        given(principalModel.getAllGroups()).willReturn(Sets.newHashSet(group1, group2, group3));
        given(group1.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction3, restiction4));
        given(group2.getSearchRestrictions()).willReturn(Sets.newHashSet(restiction5, restiction6));
        given(group3.getSearchRestrictions()).willReturn(null);

        // when
        final Collection<SearchRestrictionModel> result = handler.get(principalModel);

        // then
        assertThat(result).hasSize(6).containsOnly(restiction1, restiction2, restiction3, restiction4, restiction5, restiction6);
	}
}
