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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;
import de.hybris.platform.validation.pojos.PojoOne;

import javax.validation.constraints.DecimalMax;

import org.junit.Before;
import org.junit.Test;


/**
 * This class tests the {@link TypeConstraintValidator} and {@link TypeConstraintPreparer} only. The easymock is used to
 * simulate the modelservice calls.
 */
@UnitTest
public class TypeConstraintInterceptorTest
{
	private TypeConstraintPreparer preparer;
	private TypeConstraintValidator validator;
	private ComposedTypeModel prodModCT;
	private ComposedTypeModel langModCT;
	private ModelService modelService;

	/**
	 * create needed dummy data.
	 */
	@Before
	public void init()
	{
		modelService = createMock(ModelService.class);

		preparer = new TypeConstraintPreparer();
		preparer.setModelService(modelService);
		validator = new TypeConstraintValidator();
		validator.setModelService(modelService);

		prodModCT = new ComposedTypeModel();
		prodModCT.setJaloclass(Product.class);
		prodModCT.setCode("Product");

		langModCT = new ComposedTypeModel();
		langModCT.setJaloclass(Language.class);
		langModCT.setCode("Language");
	}

	/**
	 * Tests preparer with empty constraint. Expect: nothing changes, no exception
	 * 
	 */
	@Test
	public void testPrepareWithEmptyModel() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		preparer.onPrepare(constraint, null);
		assertNull(constraint.getTarget());
		assertNull(constraint.getType());
	}

	/**
	 * Test validator with empty constraint Expect: IllegalArgumentException because no annotation class is set
	 * 
	 */
	@Test
	public void testValidateWithEmptyModel() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		try
		{
			validator.onValidate(constraint, null);
			fail("There was no IllegalArgumentException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("does not contain an annotation"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
	}

	/**
	 * Test validator with constrant where only an annotation is set expect: InterceptorException because target is not
	 * set
	 */
	@Test
	public void testValidateWithAnnotationAttributeOnly() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setAnnotation(DecimalMax.class);
		try
		{
			validator.onValidate(constraint, null);
			fail("There was no IllegalArgumentException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("The constraint target is empty!"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
	}

	/**
	 * Test validator with false annotation. Expect: IllegalArgumentException because annotation class is not an
	 * annotation
	 */
	@Test
	public void testValidateWithFalseAnnotationAttribute() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setAnnotation(Product.class);
		try
		{
			validator.onValidate(constraint, null);
			fail("There should be an IllegalArgumentException");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("is not an annotation."));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}

	}

	/**
	 * Test preparer with false java class as target. Expect: nothing happens
	 */
	@Test
	public void testPreparerWithJavaClass() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setTarget(String.class);
		preparer.onPrepare(constraint, null);
		assertNull(constraint.getType());
		assertEquals(String.class, constraint.getTarget());
	}

	/**
	 * Test preparer with not compatible target and type. Expect: type overwrites the target
	 * 
	 */
	@Test
	public void testPreparer() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setTarget(String.class);
		constraint.setType(prodModCT);

		final ProductModel productModel = new ProductModel();
		expect(modelService.create(prodModCT.getCode())).andReturn(productModel);
		modelService.detach(productModel);
		expectLastCall();
		replay(modelService);

		preparer.onPrepare(constraint, null);
		assertEquals(ProductModel.class, constraint.getTarget());
		assertEquals(prodModCT, constraint.getType());
		verify(modelService);
	}

	/**
	 * Test validator with a constraint where only a pojo as target is set (and the annotation). Expect: nothing happens
	 */
	@Test
	public void testValidator1() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setTarget(PojoOne.class);
		constraint.setAnnotation(DecimalMax.class);

		replay(modelService);
		validator.onValidate(constraint, null);
		verify(modelService);
	}

	/**
	 * Test validator, target and type are incompatible to each other. Expect: IllegalArgumentException
	 */
	@Test
	public void testValidator2() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setTarget(PojoOne.class);
		constraint.setAnnotation(DecimalMax.class);
		constraint.setType(prodModCT);

		expect(modelService.getModelTypeClass(PojoOne.class)).andReturn(PojoOne.class);

		replay(modelService);
		try
		{
			validator.onValidate(constraint, null);
			fail("InterceptorException expected!");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("not compatible with constraint reference type"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
		verify(modelService);
	}

	/**
	 * Test validator. target contains a different composedtype as given in the type. Expect: IllegalArgumentException
	 * 
	 * @throws InterceptorException
	 */
	@Test
	public void testValidator3() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setType(langModCT);
		constraint.setAnnotation(DecimalMax.class);
		constraint.setTarget(ProductModel.class);

		expect(modelService.getModelTypeClass(ProductModel.class)).andReturn(Product.class);

		replay(modelService);
		try
		{
			validator.onValidate(constraint, null);
			fail("InterceptorException expected!");
		}
		catch (final InterceptorException e)
		{
			assertTrue(e.getMessage().contains("not compatible with constraint reference type"));
		}
		catch (final Exception e)
		{
			fail("unexpected exception: " + e);
		}
		verify(modelService);
	}

	/**
	 * Test validator. Constraint has only the target set but which is a model. Expect: InterceptorException
	 */
	@Test
	public void testValidator4() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setAnnotation(DecimalMax.class);
		constraint.setTarget(ProductModel.class);

		try
		{
			validator.onValidate(constraint, null);
			fail("expected InterceptorException");
		}
		catch (final InterceptorException e)
		{
			// fine
		}
		catch (final Exception e)
		{
			fail("unknown exception");
		}
	}

	/**
	 * Test validator where everything is fine. expect: nothing happens
	 */
	@Test
	public void testValidator5() throws InterceptorException
	{
		final TypeConstraintModel constraint = new TypeConstraintModel();
		constraint.setAnnotation(DecimalMax.class);
		constraint.setType(prodModCT);
		constraint.setTarget(ProductModel.class);

		expect(modelService.getModelTypeClass(ProductModel.class)).andReturn(Product.class);

		replay(modelService);
		validator.onValidate(constraint, null);
		verify(modelService);
	}

}
