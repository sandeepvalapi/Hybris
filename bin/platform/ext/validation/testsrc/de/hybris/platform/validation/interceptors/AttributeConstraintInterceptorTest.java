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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.annotations.mapping.AnnotationsMappingRegistry;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import de.hybris.platform.validation.pojos.PojoOne;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * This test tests the AttributeConstraint preparer and validator.
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class AttributeConstraintInterceptorTest
{
	private AttributeConstraintPreparer preparer;
	private AttributeConstraintValidator attrValidator;
	private TypeConstraintValidator typeValidator;
	private ComposedTypeModel prodModCT;
	private AttributeDescriptorModel prodModCodeAD;
	private ModelService modelService;
    @Mock
    private AnnotationsMappingRegistry annotationsMappingRegistry;
    @Mock
    private TypeService typeService;
    @Mock
    private AttributeDescriptorModel ad;
    @Mock
    private TypeModel tm;


	/**
	 * creating some dummy data.
	 */
	@Before
	public void init()
	{
		modelService = createMock(ModelService.class);

		preparer = new AttributeConstraintPreparer();
		preparer.setModelService(modelService);

		attrValidator = new AttributeConstraintValidator();
        attrValidator.setAnnotationsMappingRegistry(annotationsMappingRegistry);
        attrValidator.setTypeService(typeService);

		typeValidator = new TypeConstraintValidator();
		typeValidator.setModelService(modelService);

		prodModCT = new ComposedTypeModel();
		prodModCT.setJaloclass(Product.class);
		prodModCT.setCode("Product");

		prodModCodeAD = new AttributeDescriptorModel();
		prodModCodeAD.setQualifier("code");
		prodModCodeAD.setEnclosingType(prodModCT);
	}

	/**
	 * Testing Preparer with empty constraint. Expect: nothing changes.
	 */
	@Test
	public void testPreparerWithEmptyConstraint() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		preparer.onPrepare(constraint, null);
		assertNull(constraint.getTarget());
		assertNull(constraint.getType());
		assertNull(constraint.getAnnotation());
		assertNull(constraint.getQualifier());
	}

	/**
	 * Testing preparer, descriptor is set, other attributes filled with dummy data. Expect: descriptor overwrites all
	 * other fields.
	 */
	@Test
	public void testPreparerWithAttributeDescriptorOnly() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setDescriptor(prodModCodeAD);
		constraint.setAnnotation(Min.class);
		constraint.setType(new ComposedTypeModel());
		constraint.setTarget(LanguageModel.class);
		constraint.setQualifier("xxx");

		final ProductModel productModel = new ProductModel();
		expect(modelService.create(prodModCT.getCode())).andReturn(productModel);
		modelService.detach(productModel);
		expectLastCall();

		replay(modelService);
		preparer.onPrepare(constraint, null);
		verify(modelService);

		assertEquals(ProductModel.class, constraint.getTarget());
		assertEquals(prodModCT, constraint.getType());
		assertEquals(Min.class, constraint.getAnnotation());
		assertEquals("code", constraint.getQualifier());
	}

	/**
	 * Testing validator with empty constraint. Expect: exception, annotation is not set.
	 */
	@Test
	public void testValidatorWithNullAnnotation()
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		try
		{
			typeValidator.onValidate(constraint, null);
			fail("expected InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// fine here 
		}
		catch (final Exception e)
		{
			fail("unknown exception: " + e);
		}
		try
		{
			attrValidator.onValidate(constraint, null);
			fail("expected InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// fine here 
		}
		catch (final Exception e)
		{
			fail("unknown exception: " + e);
		}
	}


	/**
	 * Testing validator with an itemmodel which is ok. Expect: nothing.
	 */
	@Test
	public void testValidatorItemModelOK() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setAnnotation(NotEmpty.class);
		constraint.setDescriptor(prodModCodeAD);
		constraint.setType(prodModCT);
		constraint.setTarget(ProductModel.class);
		constraint.setQualifier("code");

		expect(modelService.getModelTypeClass(ProductModel.class)).andReturn(Product.class);

		replay(modelService);
		typeValidator.onValidate(constraint, null);
        given(Boolean.valueOf(annotationsMappingRegistry.isAnnotationRegisteredForType(NotEmpty.class, "java.lang.String"))).willReturn(Boolean.TRUE);
        given(typeService.getAttributeDescriptor("Product", "code")).willReturn(ad);
        given(ad.getAttributeType()).willReturn(tm);
        given(tm.getCode()).willReturn("java.lang.String");
		attrValidator.onValidate(constraint, null);
		verify(modelService);
	}

	/**
	 * Testing validator with a correct pojo. Expect: nothing happens.
	 */
	@Test
	public void testValidatorPojoOk() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setAnnotation(Past.class);
		constraint.setTarget(PojoOne.class);
		constraint.setQualifier("pojoOnePrivate");

		replay(modelService);
		typeValidator.onValidate(constraint, null);
        given(Boolean.valueOf(annotationsMappingRegistry.isAnnotationRegisteredForType(Past.class, "java.util.Date"))).willReturn(Boolean.TRUE);
		attrValidator.onValidate(constraint, null);
		verify(modelService);
	}

	/**
	 * Test validator. Pojo is ok, attribute qualifier exists, but it is all lower case. Expect: exception.
	 */
	@Test
	public void testValidatorPojoFail2() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setAnnotation(Past.class);
		constraint.setTarget(PojoOne.class);
		constraint.setQualifier("pojooneprivate");

		replay(modelService);
		typeValidator.onValidate(constraint, null);
		verify(modelService);

		try
		{
			attrValidator.onValidate(constraint, null);
			fail("There was no InterceptorException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("Unable to find method"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

	}


	/**
	 * Testing validator with incorrect pojo. Expect: exception
	 */
	@Test
	public void testValidatorPojoFails() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setAnnotation(Past.class);
		constraint.setTarget(PojoOne.class);
		constraint.setQualifier("one");

		replay(modelService);
		typeValidator.onValidate(constraint, null);
		verify(modelService);

		try
		{
			attrValidator.onValidate(constraint, null);
			fail("There was no InterceptorException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("Unable to find method"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
	}

	/**
	 * Testing validator where qualifier is only missing. Expect: exception
	 */
	@Test
	public void testValidatorEmptyQualifier() throws InterceptorException
	{
		final AttributeConstraintModel constraint = new AttributeConstraintModel();
		constraint.setAnnotation(NotEmpty.class);
		constraint.setDescriptor(prodModCodeAD);
		constraint.setType(prodModCT);
		constraint.setTarget(ProductModel.class);

		expect(modelService.getModelTypeClass(ProductModel.class)).andReturn(Product.class);

		replay(modelService);
		typeValidator.onValidate(constraint, null);
		verify(modelService);

		try
		{
			attrValidator.onValidate(constraint, null);
			fail("There was no InterceptorException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("is empty!"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
	}
}
