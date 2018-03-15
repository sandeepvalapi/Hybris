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
package de.hybris.platform.enumeration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.impl.DefaultEnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * test class for DefaultEnumerationService.
 * 
 */
@UnitTest
public class DefaultEnumerationServiceTest
{

	private DefaultEnumerationService enumerationService;

	@Mock
	private TypeService typeService;
	@Mock
	private ModelService modelService;

	private static final String TYPE_CODE_ARTICLE_APPROVAL_STATUS = "ArticleApprovalStatus";
	private static final String VALUE_CODE_CHECKED = "check";
	private static final String TYPE_CODE_VAT_TYPE = "VATType";

	@Mock
	private EnumerationValueModel testModel;
	@Mock
	private EnumerationMetaTypeModel testMetaTypeModel;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		enumerationService = new DefaultEnumerationService();
		enumerationService.setTypeService(typeService);
		enumerationService.setModelService(modelService);

		testModel.setCode(TYPE_CODE_ARTICLE_APPROVAL_STATUS);
		when(testModel.getPk()).thenReturn(PK.BIG_PK);

		testMetaTypeModel.setCode(TYPE_CODE_VAT_TYPE);
		testMetaTypeModel.setValues(Collections.EMPTY_LIST);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationValueWithNulls()
	{
		when(typeService.getEnumerationValue(null, null)).thenThrow(new IllegalArgumentException());

		enumerationService.getEnumerationValue((String) null, (String) null);

		verify(typeService).getEnumerationValue(null, null);
		fail("IllegalArgumentException was expected but not thrown");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationValueWithFirstNull()
	{
		when(typeService.getEnumerationValue(null, "bod")).thenThrow(new IllegalArgumentException());

		enumerationService.getEnumerationValue((String) null, "bod");
		// Finally confirm that the call to some mocked methods was made
		verify(typeService).getEnumerationValue(null, "bod");
		fail("IllegalArgumentException was expected but not thrown");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationValueWithSecondNull()
	{
		when(typeService.getEnumerationValue("bod", null)).thenThrow(new IllegalArgumentException());

		enumerationService.getEnumerationValue("bod", null);

		verify(typeService).getEnumerationValue("bod", null);
		fail("IllegalArgumentException was expected but not thrown");
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetEnumerationValueWithEmptyStrings()
	{
		when(typeService.getEnumerationValue("", "")).thenThrow(new UnknownIdentifierException("Unknown identifier"));

		enumerationService.getEnumerationValue("", "");
		verify(typeService).getEnumerationValue("", "");
		fail("UnknownIdentifierException was expected but not thrown");
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetEnumerationValueWithCodeAndEmptyString()
	{
		when(typeService.getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, "")).thenThrow(
				new UnknownIdentifierException("Unknown identifier"));

		enumerationService.getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, "");
		verify(typeService).getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, "");

		fail("UnknownIdentifierException was expected but not thrown");
	}

	@Test
	public void testGetEnumerationValueWithValidCodeAndValue()
	{
		when(typeService.getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, VALUE_CODE_CHECKED)).thenReturn(testModel);
		when(modelService.get(testModel.getPk())).thenReturn(ArticleApprovalStatusEnumStub.CHECK);

		final HybrisEnumValue result = enumerationService
				.getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, VALUE_CODE_CHECKED);
		assertNotNull(result);
		assertEquals(TYPE_CODE_ARTICLE_APPROVAL_STATUS, result.getType());
		assertEquals(VALUE_CODE_CHECKED, result.getCode());
		verify(typeService).getEnumerationValue(TYPE_CODE_ARTICLE_APPROVAL_STATUS, VALUE_CODE_CHECKED);
		verify(modelService, atLeastOnce()).get(PK.BIG_PK);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationValuesCodeNull()
	{
		when(typeService.getEnumerationTypeForCode(null)).thenThrow(new IllegalArgumentException());

		enumerationService.getEnumerationValues((String) null);
		verify(typeService).getEnumerationTypeForCode(null);

		fail("IllegalArgumentException was expected but not thrown");

	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetEnumerationValuesCodeEmpty()
	{
		when(typeService.getEnumerationTypeForCode("")).thenThrow(new UnknownIdentifierException("Unknown identifier"));

		enumerationService.getEnumerationValues("");
		verify(typeService).getEnumerationTypeForCode("");

		fail("UnknownIdentifierException was expected but not thrown");
	}

	@Test
	public void testGetEnumerationValuesCodeValid()
	{
		final Collection<ItemModel> valueList = testMetaTypeModel.getValues();

		final EnumerationValueModel evm1 = new EnumerationValueModel();
		final EnumerationValueModel spy1 = spy(evm1);
		when(spy1.getPk()).thenReturn(PK.fromLong(1L));
		valueList.add(spy1);
		when(modelService.get(spy1.getPk())).thenReturn(VATTypeEnumStub.FULL);

		final EnumerationValueModel evm2 = new EnumerationValueModel();
		final EnumerationValueModel spy2 = spy(evm2);
		when(spy2.getPk()).thenReturn(PK.fromLong(2L));
		valueList.add(spy2);
		when(modelService.get(spy2.getPk())).thenReturn(VATTypeEnumStub.HALF);

		final EnumerationValueModel evm3 = new EnumerationValueModel();
		final EnumerationValueModel spy3 = spy(evm3);
		when(spy3.getPk()).thenReturn(PK.fromLong(3L));
		valueList.add(spy3);
		when(modelService.get(spy3.getPk())).thenReturn(VATTypeEnumStub.NONE);

		when(testMetaTypeModel.getValues()).thenReturn(valueList);
		when(typeService.getEnumerationTypeForCode(TYPE_CODE_VAT_TYPE)).thenReturn(testMetaTypeModel);

		final List<HybrisEnumValue> resultList = enumerationService.getEnumerationValues(TYPE_CODE_VAT_TYPE);
		assertTrue(resultList.size() == valueList.size());

		//	check the results - the same type code for every item
		for (final HybrisEnumValue resultItem : resultList)
		{
			assertEquals(TYPE_CODE_VAT_TYPE, resultItem.getType());
		}

		// check the results - appropriate value code for each item
		assertEquals(VATTypeEnumStub.FULL.name(), resultList.get(0).getCode());
		assertEquals(VATTypeEnumStub.HALF.name(), resultList.get(1).getCode());
		assertEquals(VATTypeEnumStub.NONE.name(), resultList.get(2).getCode());

		verify(typeService).getEnumerationTypeForCode(TYPE_CODE_VAT_TYPE);
		verify(modelService, atLeastOnce()).get(spy1.getPk());
		verify(modelService, atLeastOnce()).get(spy2.getPk());
		verify(modelService, atLeastOnce()).get(spy3.getPk());

		System.clearProperty(AbstractItemModel.MODEL_CONTEXT_FACTORY);
	}

	@Test
	public void testGetEnumerationValuesForClassFixed()
	{
		final List<ArticleApprovalStatus> resultList = enumerationService.getEnumerationValues(ArticleApprovalStatus.class);
		final ArticleApprovalStatus[] valueList = ArticleApprovalStatus.values();

		assertEquals(valueList.length, resultList.size());
		assertEquals(valueList[0], resultList.get(0));
		assertEquals(valueList[1], resultList.get(1));
		assertEquals(valueList[2], resultList.get(2));
	}

	@Test
	public void testGetEnumerationValueForClassAndCode()
	{
		final ArticleApprovalStatus status = enumerationService
				.getEnumerationValue(ArticleApprovalStatus.class, VALUE_CODE_CHECKED);
		assertEquals(status, ArticleApprovalStatus.CHECK);
	}

	@Test
	public void testGetEnumerationNameNotNull()
	{
		when(typeService.getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED)).thenReturn(testModel);
		when(testModel.getName()).thenReturn("TestName");

		final String result = enumerationService.getEnumerationName(ArticleApprovalStatusEnumStub.APPROVED);
		assertNotNull(result);
		assertEquals("TestName", result);
		verify(typeService).getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationNameNull()
	{
		when(typeService.getEnumerationValue(null)).thenThrow(new IllegalArgumentException());

		final String result = enumerationService.getEnumerationName(null);
		assertNull(result);
		verify(typeService).getEnumerationValue(null);
	}

	@Test
	public void testGetEnumerationNameLocalized()
	{
		when(typeService.getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED)).thenReturn(testModel);
		when(testModel.getName(Locale.GERMAN)).thenReturn("Neuer_Name");

		final String result = enumerationService.getEnumerationName(ArticleApprovalStatusEnumStub.APPROVED, Locale.GERMAN);

		assertNotNull(result);
		assertEquals("Neuer_Name", result);
		verify(typeService).getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetEnumerationNameLocalizedWrong()
	{
		when(typeService.getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED)).thenReturn(testModel);
		when(testModel.getName(Locale.KOREAN)).thenThrow(new IllegalArgumentException());

		enumerationService.getEnumerationName(ArticleApprovalStatusEnumStub.APPROVED, Locale.KOREAN);

		verify(typeService).getEnumerationValue(ArticleApprovalStatusEnumStub.APPROVED);
		fail("IllegalArgumentException was expected but not thrown");
	}

}
