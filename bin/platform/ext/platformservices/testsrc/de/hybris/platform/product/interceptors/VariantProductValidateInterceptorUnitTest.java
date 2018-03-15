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
package de.hybris.platform.product.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class VariantProductValidateInterceptorUnitTest
{
	@Mock
	private TypeService typeService;

	private final VariantProductValidateInterceptor interceptor = new VariantProductValidateInterceptor();


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		interceptor.setTypeService(typeService);

	}

	@Test
	public void testValidateForNull() throws InterceptorException
	{
		interceptor.onValidate(null, Mockito.mock(InterceptorContext.class));
	}

	@Test
	public void testValidateForNotAVariantProduct() throws InterceptorException
	{
		final ProductModel model = Mockito.mock(ProductModel.class);
		interceptor.onValidate(model, Mockito.mock(InterceptorContext.class));

		Mockito.verifyZeroInteractions(model);
	}


	@Test
	public void testValidateForAVariantProduct() throws InterceptorException
	{
		final VariantProductModel model = Mockito.mock(VariantProductModel.class);
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);

		interceptor.onValidate(model, ctx);

		Mockito.verifyZeroInteractions(model);
	}

	@Test
	public void testValidateForVariantProductWhenNew() throws InterceptorException
	{
		final VariantProductModel model = Mockito.mock(VariantProductModel.class);
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isNew(model))).thenReturn(Boolean.TRUE);
		interceptor.onValidate(model, ctx);

		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	@Test
	public void testValidateForVariantProductWhenBaseProdcutChanged() throws InterceptorException
	{
		final VariantProductModel model = Mockito.mock(VariantProductModel.class);
		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		interceptor.onValidate(model, ctx);

		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}


	@Test
	public void testValidateForVariantProductWithNullBaseProductType() throws InterceptorException
	{
		final VariantProductModel model = Mockito.mock(VariantProductModel.class);

		final ProductModel baseProduct = Mockito.mock(ProductModel.class);

		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		Mockito.when(model.getBaseProduct()).thenReturn(baseProduct);
		try
		{
			interceptor.onValidate(model, ctx);
			Assert.fail("When base product's variant type is null validation should fail ");
		}
		catch (final InterceptorException e)
		{
			//ok here 
		}
		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	/**
	 * case
	 * <p>
	 * base product variantType -> Shoe
	 * <p>
	 * variant product type -> Shirt
	 * <p>
	 */
	@Test
	public void testValidateForVariantProductWithIncompatibleBaseProductType() throws InterceptorException
	{
		Mockito.when(
				Boolean.valueOf(typeService.isAssignableFrom(Mockito.any(ShirtVariantTypeModel.class),
						Mockito.any(ShoeVariantTypeModel.class)))).thenReturn(Boolean.FALSE);

		final VariantProductModel model = Mockito.mock(VariantProductModel.class);

		final ProductModel baseProduct = Mockito.mock(ProductModel.class);
		Mockito.when(baseProduct.getVariantType()).thenReturn(new ShoeVariantTypeModel());//shoe variant for base product 

		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getComposedTypeForCode(Mockito.anyString())).thenReturn(new ShirtVariantTypeModel()); //shirt for VP itself
		Mockito.when(model.getBaseProduct()).thenReturn(baseProduct);
		try
		{
			interceptor.onValidate(model, ctx);
			Assert.fail("When base product's variant type is not compatible with variant product Shoe <-> Shirt");
		}
		catch (final InterceptorException e)
		{
			//ok here 
		}
		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	/**
	 * case
	 * <p>
	 * base product variantType -> Shirt
	 * <p>
	 * variant product type -> FencyShirt (more specific)
	 * <p>
	 */
	@Test
	public void testValidateForVariantProductWithCompatibleBaseProductTypeMoreSpecific() throws InterceptorException
	{
		Mockito.when(
				Boolean.valueOf(typeService.isAssignableFrom(Mockito.any(FencyShirtVariantTypeModel.class),
						Mockito.any(ShirtVariantTypeModel.class)))).thenReturn(Boolean.FALSE);

		Mockito.when(
				Boolean.valueOf(typeService.isAssignableFrom(Mockito.any(ShirtVariantTypeModel.class),
						Mockito.any(FencyShirtVariantTypeModel.class)))).thenReturn(Boolean.TRUE);


		final VariantProductModel model = Mockito.mock(VariantProductModel.class);

		final ProductModel baseProduct = Mockito.mock(ProductModel.class);
		Mockito.when(baseProduct.getVariantType()).thenReturn(new ShirtVariantTypeModel());//shoe variant for base product 

		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getComposedTypeForCode(Mockito.anyString())).thenReturn(new FencyShirtVariantTypeModel()); //shirt for VP itself
		Mockito.when(model.getBaseProduct()).thenReturn(baseProduct);

		interceptor.onValidate(model, ctx);

		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	/**
	 * case
	 * <p>
	 * base product variantType -> Shirt
	 * <p>
	 * variant product type -> Shirt
	 * <p>
	 */
	@Test
	public void testValidateForVariantProductWithCompatibleBaseProductType() throws InterceptorException
	{

		Mockito.when(
				Boolean.valueOf(typeService.isAssignableFrom(Mockito.any(FencyShirtVariantTypeModel.class),
						Mockito.any(ShirtVariantTypeModel.class)))).thenReturn(Boolean.FALSE);

		Mockito.when(
				Boolean.valueOf(typeService.isAssignableFrom(Mockito.any(ShirtVariantTypeModel.class),
						Mockito.any(ShirtVariantTypeModel.class)))).thenReturn(Boolean.TRUE);


		final VariantProductModel model = Mockito.mock(VariantProductModel.class);

		final ProductModel baseProduct = Mockito.mock(ProductModel.class);
		Mockito.when(baseProduct.getVariantType()).thenReturn(new ShirtVariantTypeModel());//shoe variant for base product 

		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getComposedTypeForCode(Mockito.anyString())).thenReturn(new ShirtVariantTypeModel()); //shirt for VP itself
		Mockito.when(model.getBaseProduct()).thenReturn(baseProduct);

		interceptor.onValidate(model, ctx);

		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	/**
	 * case
	 * <p>
	 * base product variantType -> FencyShirt (more specific)
	 * <p>
	 * variant product type -> Shirt
	 * <p>
	 */
	@Test
	public void testValidateForVariantProductWithIncompatibleBaseProductTypeTooSpecific() throws InterceptorException
	{

		final VariantProductModel model = Mockito.mock(VariantProductModel.class);

		final ProductModel baseProduct = Mockito.mock(ProductModel.class);
		Mockito.when(baseProduct.getVariantType()).thenReturn(new ShirtVariantTypeModel());//shoe variant for base product 

		final InterceptorContext ctx = Mockito.mock(InterceptorContext.class);
		Mockito.when(Boolean.valueOf(ctx.isModified(model, VariantProductModel.BASEPRODUCT))).thenReturn(Boolean.TRUE);
		Mockito.when(typeService.getComposedTypeForCode(Mockito.anyString())).thenReturn(new FencyShirtVariantTypeModel()); //shirt for VP itself
		Mockito.when(model.getBaseProduct()).thenReturn(baseProduct);

		try
		{
			interceptor.onValidate(model, ctx);
			//Assert.fail("When base product's variant type is not compatible with variant product Shoe <-> Shirt");
		}
		catch (final InterceptorException e)
		{
			//ok here 
		}
		Mockito.verify(model, Mockito.times(1)).getBaseProduct();
	}

	class ShoeVariantTypeModel extends VariantTypeModel
	{
		@Override
		public String toString()
		{
			return "shoe";
		}
	}

	class ShirtVariantTypeModel extends VariantTypeModel
	{
		@Override
		public String toString()
		{
			return "shirt";
		}
	}

	class FencyShirtVariantTypeModel extends ShirtVariantTypeModel
	{
		@Override
		public String toString()
		{
			return "fency-shirt";
		}
	}

}
