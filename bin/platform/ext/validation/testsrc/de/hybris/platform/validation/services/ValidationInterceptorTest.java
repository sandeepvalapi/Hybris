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
package de.hybris.platform.validation.services;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ValidationInterceptorTest extends AbstractConstraintTest
{
	private ProductModel productModel;
	private NotNullConstraintModel notNullProduct;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		productModel = modelService.get(ProductManager.getInstance().getProductsByCode("testProduct0").iterator().next());
		notNullProduct = modelService.create(NotNullConstraintModel.class);
		notNullProduct.setId("three");
		notNullProduct.setAnnotation(javax.validation.constraints.NotNull.class);
		final AttributeDescriptorModel descrModelProduct = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.CODE);
		notNullProduct.setDescriptor(descrModelProduct);

		final SizeConstraintModel sizeProduct = modelService.create(SizeConstraintModel.class);
		sizeProduct.setId("four");
		sizeProduct.setAnnotation(javax.validation.constraints.Size.class);
		sizeProduct.setMin(Long.valueOf(1));
		sizeProduct.setMax(Long.valueOf(Integer.MAX_VALUE));
		final AttributeDescriptorModel sizedescrModelProduct = typeService.getAttributeDescriptor(
				typeService.getComposedType(ProductModel.class), ProductModel.NAME);
		sizeProduct.setDescriptor(sizedescrModelProduct);

		modelService.saveAll(notNullProduct, sizeProduct);
		validationService.reloadValidationEngine();
	}

	@Test
	public void validateProductModelFailed()
	{
		productModel.setCode(null);
		productModel.setName(null);
		try
		{
			modelService.save(productModel);
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, MandatoryAttributesValidator.class);
		}
	}

	@Test
	public void validateProductModel() throws Exception
	{
		modelService.save(productModel);
	}

	@Test
	public void localizedMessagesTest()
	{
		i18nService.setCurrentLocale(Locale.ENGLISH);
		productModel.setCode(null);
		productModel.setName(null);
		try
		{
			modelService.save(productModel);
		}
		catch (final ModelSavingException e)
		{
			//removes from '{type}.{field}' may not be null the '{type}.{field}'			
			checkException(e, ModelSavingException.class, InterceptorException.class, MandatoryAttributesValidator.class);
		}

		i18nService.setCurrentLocale(Locale.GERMAN);
		try
		{
			modelService.save(productModel);
		}
		catch (final Exception e)
		{
			//removes from 'ProductModel.code' darf nicht 'null' sein the 'ProductModel.code'			
			checkException(e, ModelSavingException.class, InterceptorException.class, MandatoryAttributesValidator.class);
		}

	}
}
