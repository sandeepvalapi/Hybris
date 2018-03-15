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
package de.hybris.platform.validation.interceptors;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.annotations.NotEmpty;
import de.hybris.platform.validation.annotations.mapping.AnnotationsMappingRegistry;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class AttributeConstraintValidatorTest
{
	@InjectMocks
	private final AttributeConstraintValidator validator = new AttributeConstraintValidator();
	@Mock
	private TypeService typeService;
	@Mock
	private AnnotationsMappingRegistry registry;
	@Mock
	private AttributeConstraintModel constraint;
	@Mock
	private InterceptorContext ctx;
	@Mock
	private ComposedTypeModel composedType;
	@Mock
	private AttributeDescriptorModel attributeDescriptor;

    @Test
    public void shouldValidateNotEmptyAnnotationAsValidForJavaLangStringProperty() throws Exception
    {
    	// given
        given(constraint.getQualifier()).willReturn("stringProperty");
        given(constraint.getTarget()).willReturn(TestClass.class);
        given(constraint.getAnnotation()).willReturn(NotEmpty.class);
        given(Boolean.valueOf(registry.isAnnotationRegisteredForType(NotEmpty.class, "java.lang.String"))).willReturn(Boolean.TRUE);

    	// when
        validator.onValidate(constraint, ctx);

    	// then
        verify(registry).isAnnotationRegisteredForType(NotEmpty.class, "java.lang.String");
    }

    @Test
    public void shouldThrowAnExceptionWhenConstraintDescriptorAndTypeIsNullButQualifierRerersToPropertyWithoutAccessor() throws Exception
    {
    	// given
        given(constraint.getQualifier()).willReturn("stringPropertyNoAccessor");
        given(constraint.getType()).willReturn(null);
        given(constraint.getDescriptor()).willReturn(null);
        given(constraint.getTarget()).willReturn(TestClass.class);

        try
        {
            // when
            validator.onValidate(constraint, ctx);
            fail("Should throw InterceptorException");
        }
        catch (final InterceptorException e)
        {
            // then fine
        }
    }

    @Test
    public void shouldThrowAnExceptionWhenConstraintDescriptorAbdTypeIsPresentButQualifierRerersToPropertyWithoutAccessor() throws Exception
    {
    	// given
        given(constraint.getQualifier()).willReturn("stringPropertyNoAccessor");
        given(constraint.getType()).willReturn(composedType);
        given(constraint.getDescriptor()).willReturn(attributeDescriptor);
        given(constraint.getTarget()).willReturn(TestClass.class);

        try
        {
            // when
            validator.onValidate(constraint, ctx);
            fail("Should throw InterceptorException");
        }
        catch (final InterceptorException e)
        {
            // then fine
        }
    }

	@Test
	public void shouldThrowAnExceptionWhenConstraintDescriptorIsNullButTypeIsSet() throws Exception
	{
		// given
		given(constraint.getQualifier()).willReturn("stringProperty");
		given(constraint.getType()).willReturn(composedType);
		given(constraint.getDescriptor()).willReturn(null);

		try
		{
			// when
			validator.onValidate(constraint, ctx);
			fail("Should throw InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowAnExceptionWhenConstraintTypeIsNullButDescriptorIsSet() throws Exception
	{
		// given
		given(constraint.getQualifier()).willReturn("stringProperty");
		given(constraint.getType()).willReturn(null);
		given(constraint.getDescriptor()).willReturn(attributeDescriptor);

		try
		{
			// when
			validator.onValidate(constraint, ctx);
			fail("Should throw InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowAnExceptionWhenConstraintQualifierIsNull() throws Exception
	{
		// given
		given(constraint.getQualifier()).willReturn(null);

		try
		{
			// when
			validator.onValidate(constraint, ctx);
			fail("Should throw InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowAnExceptionWhenConstraintQualifierIsEmpty() throws Exception
	{
		// given
		given(constraint.getQualifier()).willReturn(StringUtils.EMPTY);

		try
		{
			// when
			validator.onValidate(constraint, ctx);
			fail("Should throw InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// then fine
		}
	}

}
