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
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class DefaultItemModelSearchStrategyTest
{
	@InjectMocks
	private final DefaultItemModelSearchStrategy modelSearchStrategy = new DefaultItemModelSearchStrategy();
	@Mock
	private I18NService i18nServiceMock;
	@Mock
	private TypeService typeServiceMock;
	@Mock
	private FlexibleSearchService flexibleSearchServiceMock;
	@Mock
	private ItemModel exampleMock;
	@Mock
	private ItemModel resultModelMock;
	@Mock
	private ModelConverter converterMock;
	@Mock
	private AttributeDescriptorModel attributeDescriptorMock;
	@Mock
	private MapTypeModel returnMapTypeMock;
	@Mock
	private CollectionTypeModel returnCollectionTypeMock;
	@Mock
	private AtomicTypeModel returnAtomicTypeMock;
	@Mock
	private SearchResult searchResultMock;
	@Mock
	private SessionService sessionServiceMock;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenConverterIsNull()
	{
		modelSearchStrategy.getModelByExample(null, exampleMock);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenExampleIsNull()
	{
		modelSearchStrategy.getModelByExample(converterMock, null);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModelIsNotNew()
	{
		// given
		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.FALSE);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("must be new");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModelIsNewButDoesNotContainsChangedAttributes()
	{
		// given
		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(Collections.EMPTY_MAP);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("must be new");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModifiedAttributeIsNotSearchable()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.FALSE);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("is not searchable");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModifiedAttributeReturnTypeIsMapType()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnMapTypeMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase(
					"contains as returntype a map/collection and therefore is not searchable");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModifiedAttributeReturnTypeIsCollectionType()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnCollectionTypeMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase(
					"contains as returntype a map/collection and therefore is not searchable");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModifiedAttributeIsLocalizedAndReturnTypeIsMapType()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getLocalized()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnMapTypeMock);
		given(returnMapTypeMock.getReturntype()).willReturn(returnMapTypeMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase(
					"contains as returntype a map/collection and therefore is not searchable");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelLoadingExceptionWhenModifiedAttributeIsLocalizedAndReturnTypeIsCollectionType()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getLocalized()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnMapTypeMock);
		given(returnMapTypeMock.getReturntype()).willReturn(returnCollectionTypeMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelLoadingException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase(
					"contains as returntype a map/collection and therefore is not searchable");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelNotFoundExceptionWhenResultIsEmpty()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnAtomicTypeMock);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);
		given(searchResultMock.getResult()).willReturn(Collections.EMPTY_LIST);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelNotFoundException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("No result for the given example");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowAmbiguousIdentifierExceptionWhenResultIsGreaterThanOne()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnAtomicTypeMock);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);

		final List resultingListMock = mock(List.class);
		given(Integer.valueOf(resultingListMock.size())).willReturn(Integer.valueOf(2));
		given(searchResultMock.getResult()).willReturn(resultingListMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final AmbiguousIdentifierException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("Found 2 results for the given example");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldThrowModelNotFoundExceptionWhenResultContainsOnlyNullValue()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnAtomicTypeMock);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);

		final List resultingListMock = mock(List.class);
		given(Integer.valueOf(resultingListMock.size())).willReturn(Integer.valueOf(1));
		given(resultingListMock.get(0)).willReturn(null);
		given(searchResultMock.getResult()).willReturn(resultingListMock);

		try
		{
			// when
			modelSearchStrategy.getModelByExample(converterMock, exampleMock);
			fail();
		}
		catch (final ModelNotFoundException e)
		{
			// then
			assertThat(e.getMessage()).containsIgnoringCase("No result for the given example");
		}
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldReturnFoundModelByExample()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnAtomicTypeMock);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);

		final List resultingListMock = mock(List.class);
		given(Integer.valueOf(resultingListMock.size())).willReturn(Integer.valueOf(1));
		given(resultingListMock.get(0)).willReturn(resultModelMock);
		given(searchResultMock.getResult()).willReturn(resultingListMock);

		// when
		final ItemModel foundModel = modelSearchStrategy.getModelByExample(converterMock, exampleMock);

		// then
		assertThat(foundModel).isNotNull();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.internal.model.impl.DefaultItemModelSearchStrategy#getModelsByExample(de.hybris.platform.servicelayer.internal.converter.ModelConverter, java.lang.Object)}
	 * .
	 */
	@Test
	public void shouldReturnListOfFoundModelsByExample()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		dirtyAttrs.put("fooBar", null);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnAtomicTypeMock);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);

		final List resultingListMock = mock(List.class);
		given(Integer.valueOf(resultingListMock.size())).willReturn(Integer.valueOf(1));
		given(resultingListMock.get(0)).willReturn(resultModelMock);
		given(searchResultMock.getResult()).willReturn(resultingListMock);

		// when
		final List<ItemModel> foundModels = modelSearchStrategy.getModelsByExample(converterMock, exampleMock);

		// then
		assertThat(foundModels).isNotNull();
		assertThat(foundModels).isNotEmpty();
	}

	@Test
	public void shouldReturnFoundModelByExampleForLocalizedDescriptor()
	{
		// given
		final Map<String, Set<Locale>> dirtyAttrs = new HashMap<String, Set<Locale>>();
		final Set<Locale> locales = new HashSet<Locale>();
		final Locale locale = new Locale("en");
		locales.add(locale);
		dirtyAttrs.put("fooBar", locales);

		given(Boolean.valueOf(converterMock.isNew(exampleMock))).willReturn(Boolean.TRUE);
		given(converterMock.getDirtyAttributes(exampleMock)).willReturn(dirtyAttrs);
		given(converterMock.getType(exampleMock)).willReturn("SomeType");
		given(converterMock.getAttributeValue(exampleMock, "fooBar")).willReturn("SomeValue");
		given(typeServiceMock.getAttributeDescriptor("SomeType", "fooBar")).willReturn(attributeDescriptorMock);
		given(attributeDescriptorMock.getSearch()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getLocalized()).willReturn(Boolean.TRUE);
		given(attributeDescriptorMock.getAttributeType()).willReturn(returnMapTypeMock);

		given(i18nServiceMock.getBestMatchingLocale(locale)).willReturn(locale);
		given(flexibleSearchServiceMock.search(anyString(), anyMap())).willReturn(searchResultMock);
		final List resultingListMock = mock(List.class);
		given(Integer.valueOf(resultingListMock.size())).willReturn(Integer.valueOf(1));
		given(resultingListMock.get(0)).willReturn(resultModelMock);
		given(searchResultMock.getResult()).willReturn(resultingListMock);

		// when
		when(sessionServiceMock.executeInLocalView(Mockito.any(SessionExecutionBody.class))).thenAnswer(new Answer<Object>()
		{
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable
			{
				final SessionExecutionBody args = (SessionExecutionBody) invocation.getArguments()[0];
				return args.execute();
			}
		});

		final ItemModel foundModel = modelSearchStrategy.getModelByExample(converterMock, exampleMock);

		// then
		assertThat(foundModel).isNotNull();
		verify(flexibleSearchServiceMock, times(1)).search(eq("SELECT {pk} from {SomeType} WHERE {fooBar[en]}=?fooBar_en "),
				eq(Collections.singletonMap("fooBar_en", "SomeValue")));
	}
}
