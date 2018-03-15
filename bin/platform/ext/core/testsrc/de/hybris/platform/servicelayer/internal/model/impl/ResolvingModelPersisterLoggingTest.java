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
package de.hybris.platform.servicelayer.internal.model.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;


@RunWith(MockitoJUnitRunner.class)
public class ResolvingModelPersisterLoggingTest
{

	private ResolvingModelPersister modelPersister;
	@Mock
	private ModelWrapper wrapper1, wrapper2;
	@Mock
	private ItemModelConverter converter1, converter2;
	@Mock
	private ItemModel model1, model2;
	private Map<String, Set<Locale>> dirtyAttrs1, dirtyAttrs2;

	@Before
	public void setUp() throws Exception
	{
		modelPersister = new ResolvingModelPersister();

		given(wrapper1.getConverter()).willReturn(converter1);
		given(wrapper2.getConverter()).willReturn(converter2);
		given(wrapper1.getModel()).willReturn(model1);
		given(wrapper2.getModel()).willReturn(model2);

		dirtyAttrs1 = ImmutableMap.<String, Set<Locale>> builder().put("foo", new HashSet<>()).build();
		dirtyAttrs2 = ImmutableMap.<String, Set<Locale>> builder().put("bar", new HashSet<>()).build();
	}

	@Test
	public void testGetDirtyAttributesReport() throws Exception
	{
		// given
		final List<ModelWrapper> newOnes = Arrays.asList(wrapper1, wrapper2);
		given(converter1.getDirtyAttributes(model1)).willReturn(dirtyAttrs1);
		given(converter2.getDirtyAttributes(model2)).willReturn(dirtyAttrs2);

		// when
		final List<Map<String, ?>> report = modelPersister
				.getDirtyAttributesReport(newOnes, Collections.<ModelWrapper>emptyList());

		// then
        assertThat(report).containsOnly(dirtyAttrs1, dirtyAttrs2);
	}


}
