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
package de.hybris.platform.search.restriction.session.converter.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSessionSearchRestrictionConverterTest
{
	private final DefaultSessionSearchRestrictionConverter converter = new DefaultSessionSearchRestrictionConverter();
	@Mock
	private ModelService modelService;
	@Mock
	private ComposedTypeModel composedTypeModelMock1;
	@Mock
	private ComposedTypeModel composedTypeModelMock2;
	@Mock
	private ComposedType composedTypeMock1;
	@Mock
	private ComposedType composedTypeMock2;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		converter.setModelService(modelService);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.session.converter.impl.DefaultSessionSearchRestrictionConverter#convertFromFilters(java.util.Collection)}
	 * .
	 */
	@Test
	public void shouldConvertFromCollectionOfContextQueryFilter()
	{
		// given
		final Collection<ContextQueryFilter> filters = new ArrayList<ContextQueryFilter>();
		final ContextQueryFilter queryFilter1 = new ContextQueryFilter("FooBar", composedTypeMock1, "WHERE {Foo} = 'bar'");
		final ContextQueryFilter queryFilter2 = new ContextQueryFilter("BazDam", composedTypeMock2, "WHERE {Baz} = 'dam'");
		filters.add(queryFilter1);
		filters.add(queryFilter2);
		given(modelService.get(any(ComposedType.class))).willReturn(composedTypeModelMock1, composedTypeModelMock2);

		// when
		final Collection<SessionSearchRestriction> restrictions = converter.convertFromFilters(filters);

		// then
		final SessionSearchRestriction restriction1 = getElementWithCode(queryFilter1.getCode(), restrictions);
		final SessionSearchRestriction restriction2 = getElementWithCode(queryFilter2.getCode(), restrictions);

		assertThat(restrictions).hasSize(2);
		assertThat(restriction1.getCode()).isEqualTo(queryFilter1.getCode());
		assertThat(restriction2.getCode()).isEqualTo(queryFilter2.getCode());
		assertThat(restriction1.getQuery()).isEqualTo(queryFilter1.getQuery());
		assertThat(restriction2.getQuery()).isEqualTo(queryFilter2.getQuery());
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.session.converter.impl.DefaultSessionSearchRestrictionConverter#convert(de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter)}
	 * .
	 */
	@Test
	public void shouldConvertFromContextQueryFilter()
	{
		// given
		final ContextQueryFilter filter = new ContextQueryFilter("FooBar", composedTypeMock1, "WHERE {Foo} = 'bar'");
		given(modelService.get(composedTypeMock1)).willReturn(composedTypeModelMock1);

		// when
		final SessionSearchRestriction restriction = converter.convert(filter);

		// then
		assertThat(restriction).isNotNull();
		assertThat(restriction.getCode()).isEqualTo(filter.getCode());
		assertThat(restriction.getQuery()).isEqualTo(filter.getQuery());
		assertThat(restriction.getRestrictedType()).isEqualTo(composedTypeModelMock1);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.session.converter.impl.DefaultSessionSearchRestrictionConverter#convert(de.hybris.platform.search.restriction.session.SessionSearchRestriction)}
	 * .
	 */
	@Test
	public void shouldConvertFromSessionSearchRestriction()
	{
		// given
		final SessionSearchRestriction restriction = new SessionSearchRestriction("FooBar", "WHERE {Foo} = 'bar'",
				composedTypeModelMock1);
		given(modelService.getSource(composedTypeModelMock1)).willReturn(composedTypeMock1);

		// when
		final ContextQueryFilter filter = converter.convert(restriction);

		// then
		assertThat(restriction).isNotNull();
		assertThat(filter.getCode()).isEqualTo(restriction.getCode());
		assertThat(filter.getQuery()).isEqualTo(restriction.getQuery());
		assertThat(filter.getRestrictionType()).isEqualTo(composedTypeMock1);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.search.restriction.session.converter.impl.DefaultSessionSearchRestrictionConverter#convertFromRestrictions(java.util.Collection)}
	 * .
	 */
	@Test
	public void testConvertFromCollectionOfSessionSearchRestriction()
	{
		// given
		final Collection<SessionSearchRestriction> restrictions = new ArrayList<SessionSearchRestriction>();
		final SessionSearchRestriction restriction1 = new SessionSearchRestriction("FooBar", "WHERE {Foo} = 'bar'",
				composedTypeModelMock1);
		final SessionSearchRestriction restriction2 = new SessionSearchRestriction("BazDam", "WHERE {Baz} = 'dam'",
				composedTypeModelMock2);
		restrictions.add(restriction1);
		restrictions.add(restriction2);
		given(modelService.getSource(any(ComposedTypeModel.class))).willReturn(composedTypeMock1, composedTypeMock2);

		// when
		final Collection<ContextQueryFilter> filters = converter.convertFromRestrictions(restrictions);

		// then
		final ContextQueryFilter filter1 = getElementWithCode(restriction1.getCode(), filters);
		final ContextQueryFilter filter2 = getElementWithCode(restriction2.getCode(), filters);

		assertThat(restrictions).hasSize(2);
		assertThat(filter1.getCode()).isEqualTo(restriction1.getCode());
		assertThat(filter2.getCode()).isEqualTo(restriction2.getCode());
		assertThat(filter1.getQuery()).isEqualTo(restriction1.getQuery());
		assertThat(filter2.getQuery()).isEqualTo(restriction2.getQuery());
	}

	private <T> T getElementWithCode(final String code, final Collection col)
	{
		for (final Object object : col)
		{
			if (object instanceof ContextQueryFilter)
			{
				if (code.equals(((ContextQueryFilter) object).getCode()))
				{
					return (T) object;
				}
			}
			if (object instanceof SessionSearchRestriction)
			{
				if (code.equals(((SessionSearchRestriction) object).getCode()))
				{
					return (T) object;
				}
			}
		}
		return null;
	}

}
