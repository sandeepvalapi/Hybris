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
package de.hybris.platform.servicelayer.impex.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.impex.ImpExValueLineError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class ValueLineErrorTest
{
	@Mock
	private ValueLine valueLine;
	@Mock
	private HeaderDescriptor headerDescriptor;
	@Mock
	private HeaderValidationException headerValidationException;
    @Mock
    private EnumerationValue headerMode;

    @Before
    public void setUp() throws Exception
    {
        given(valueLine.getHeader()).willReturn(headerDescriptor);
    	given(headerDescriptor.getMode()).willReturn(headerMode);
    }

	@Test
	public void shouldHaveWrongOrMissingHeaderErrorTypeIfValueLineHasNoHeader() throws Exception
	{
		// given
		given(valueLine.getHeader()).willReturn(null);
		final ValueLineError vlError = getValueLineError();

		// when
		final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

		// then
		assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
	}

	@Test
	public void shouldHaveWrongOrMissingHeaderErrorTypeIfValueLineHasInvalidHeader() throws Exception
	{
		// given
		given(headerDescriptor.getInvalidHeaderException()).willReturn(headerValidationException);
		final ValueLineError vlError = getValueLineError();

		// when
		final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

		// then
		assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
	}
    
    @Test
    public void shouldHaveNotExistingItemErrorTypeWhenErrorMessageContains_NotExistingItemFound() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("no existing item found for update");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.NOT_EXISTING_ITEM);
    }

    @Test
    public void shouldHaveReferenceViolationErrorTypeWhenErrorMessageContains_CouldNotResolveItemFor() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("could not resolve item for foobar");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
    }

    @Test
    public void shouldHaveReferenceViolationErrorTypeWhenErrorMessageContains_CannotResolveValue() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("column 3: cannot resolve value 'iso2,iso3' for attribute 'fallbackLanguages'");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
    }

    @Test
    public void shouldHaveReferenceViolationErrorTypeWhenErrorMessageContains_CouldNotRemoveItem() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("column 3: cannot resolve value 'nonExistent' for attribute 'unit'");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION);
    }

    @Test
    public void shouldHaveCannotRemoveItemErrorTypeWhenErrorMessageContains_CouldNotRemoveItem() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("could not remove item 123456 due to foobar");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.CANNOT_REMOVE_ITEM);
    }

    @Test
    public void shouldHaveNotProcessedConflictingErrorTypeWhenErrorMessageContains_AmbigousUniqueKeys() throws Exception
    {
        // given
        given(valueLine.getPlainUnresolvedReason()).willReturn("ambiguous unique keys");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
    }

    @Test
    public void shouldHavePartiallyProcessedErrorTypeWhenModeIsUPDATEAndProcessedItemPKIsPresent() throws Exception
    {
        // given
        given(valueLine.getProcessedItemPK()).willReturn(PK.createFixedUUIDPK(1,2));
        given(headerMode.getCode()).willReturn("update");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.PARTIALLY_PROCESSED);
    }

    @Test
    public void shouldHaveNotProcessedErrorTypeWhenModeIsUPDATEAndProcessedItemPKIsNull() throws Exception
    {
        // given
        given(valueLine.getProcessedItemPK()).willReturn(null);
        given(headerMode.getCode()).willReturn("update");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED);
    }

    @Test
    public void shouldHavePartiallyProcessedErrorTypeWhenModeIsINSERT_UPDATEAndProcessedItemPKIsPresent() throws Exception
    {
        // given
        given(valueLine.getProcessedItemPK()).willReturn(PK.createFixedUUIDPK(1,2));
        given(headerMode.getCode()).willReturn("insert_update");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.PARTIALLY_PROCESSED);
    }

    @Test
    public void shouldHaveNotProcessedErrorTypeWhenModeIsINSERT_UPDATEAndProcessedItemPKIsNull() throws Exception
    {
        // given
        given(valueLine.getProcessedItemPK()).willReturn(null);
        given(headerMode.getCode()).willReturn("insert_update");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED);
    }

    @Test
    public void shouldHaveNotProcessedWithErrorErrorTypeWhenModeIsINSERTAndConflictingItemPkIsNull() throws Exception
    {
        // given
        given(valueLine.getConflictingItemPk()).willReturn(null);
        given(headerMode.getCode()).willReturn("insert");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_WITH_ERROR);
    }

    @Test
    public void shouldHaveUnknownErrorTypeWhenModeIsREMOVEAndErrorMessageIsEmpty() throws Exception
    {
        // given
        given(headerMode.getCode()).willReturn("remove");
        final ValueLineError vlError = getValueLineError();

        // when
        final ImpExValueLineError.ImpExLineErrorType errorType = vlError.getErrorType();

        // then
        assertThat(errorType).isEqualTo(ImpExValueLineError.ImpExLineErrorType.UNKNOWN);
    }


	private ValueLineError getValueLineError()
	{
		return new ValueLineError(valueLine);
	}
}
