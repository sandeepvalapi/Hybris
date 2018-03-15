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
package de.hybris.platform.catalog.references;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@DemoTest
public class ProductReferencesServiceTest extends ServicelayerTransactionalTest
{

	//constants
	private static final String CAMERA1 = "camera1";
	private static final String CAMERA2 = "camera2";

	private static final String BATTERY_AAA = "batteryAAA";

	private static final String TRIPOD = "tripod";
	private static final String ADAPTER = "adapterDC";
	private static final String LENSE = "lense";

	//resources
	@Resource
	private ProductReferenceService productReferenceService;
	@Resource
	private ProductService productService;

	//shared products
	private ProductModel camera2;
	private ProductModel camera1;

	private ProductModel batteryAAA;
	private ProductModel tripod;
	private ProductModel lense;
	private ProductModel adapter;


	@Before
	public void setUp() throws Exception
	{

		// FOR THE PURPOSE OF THIS TEST, THE FOLLOWING PRODUCT REFERENCES ARE CREATED:
		// camera1: SIMILAR -> camera2; ACCESSORIES -> batteryAA (qty: 2); ACCESSORIES -> adapterDC (inactive!)
		// camera2: OTHERS -> camera1; ACCESSORIES -> batteryAAA (qty: 2); ACCESSORIES -> adapterDC (inactive!); SPAREPART -> lense
		// tripod: UPSELLING -> lense; FOLLOWUP -> lense

		createCoreData();
		importCsv("/platformservices/test/productReferences/testProductReferences.csv", "UTF-8");


		camera1 = getProduct(CAMERA1);
		camera2 = getProduct(CAMERA2);

		batteryAAA = getProduct(BATTERY_AAA);

		lense = getProduct(LENSE);
		tripod = getProduct(TRIPOD);
		adapter = getProduct(ADAPTER);
	}



	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForNullSourceProduct()
	{
		productReferenceService.getProductReferencesForSourceProduct(null, ProductReferenceTypeEnum.CONSISTS_OF, false);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.references.ProductReferenceService#getProductReferencesForSourceProduct(de.hybris.platform.core.model.product.ProductModel, ProductReferenceTypeEnum, boolean)}
	 * .
	 */
	@Test
	public void testGetProductReferencesForSourceProduct()
	{
		//non persisted product case:
		assertThat(productReferenceService.getProductReferencesForSourceProduct(new ProductModel(), null, true)).isEmpty();

		//get all references for camera2
		final Collection<ProductReferenceModel> camera2references = productReferenceService.getProductReferencesForSourceProduct(
				camera2, null, false);

		//expected 4 references:
		//OTHERS -> camera1; ACCESSORIES -> batteryAAA (qty: 2); ACCESSORIES -> adapterDC (inactive!); SPAREPART -> lense
		assertThat(camera2references).hasSize(4);
		assertCollectionHasReferenceOfTypeForProduct(camera2references, camera1, ProductReferenceTypeEnum.OTHERS, false);
		assertCollectionHasReferenceOfTypeForProduct(camera2references, batteryAAA, ProductReferenceTypeEnum.ACCESSORIES, false);
		assertCollectionHasReferenceOfTypeForProduct(camera2references, adapter, ProductReferenceTypeEnum.ACCESSORIES, false);
		assertCollectionHasReferenceOfTypeForProduct(camera2references, lense, ProductReferenceTypeEnum.SPAREPART, false);

		//get all active references for camera2
		final Collection<ProductReferenceModel> camera2Activereferences = productReferenceService
				.getProductReferencesForSourceProduct(camera2, null, true);

		//expect only 3 references as one of the original ones is inactive:
		//OTHERS -> camera1; ACCESSORIES -> batteryAAA (qty: 2); SPAREPART -> lense
		assertThat(camera2Activereferences).hasSize(3);
		assertCollectionHasReferenceOfTypeForProduct(camera2Activereferences, camera1, ProductReferenceTypeEnum.OTHERS, false);
		assertCollectionHasReferenceOfTypeForProduct(camera2Activereferences, batteryAAA, ProductReferenceTypeEnum.ACCESSORIES,
				false);
		assertCollectionHasReferenceOfTypeForProduct(camera2Activereferences, lense, ProductReferenceTypeEnum.SPAREPART, false);

		//get all accessories for camera2
		final Collection<ProductReferenceModel> camera2accessories = productReferenceService.getProductReferencesForSourceProduct(
				camera2, ProductReferenceTypeEnum.ACCESSORIES, false);

		//expected 2 accessories:
		//ACCESSORIES -> batteryAAA (qty: 2); ACCESSORIES -> adapterDC (inactive!);
		assertThat(camera2accessories).hasSize(2);
		assertCollectionHasReferenceOfTypeForProduct(camera2accessories, adapter, ProductReferenceTypeEnum.ACCESSORIES, false);
		assertCollectionHasReferenceOfTypeForProduct(camera2accessories, batteryAAA, ProductReferenceTypeEnum.ACCESSORIES, false);

		//get all active accessories for camera2
		final Collection<ProductReferenceModel> camera2ActiveAccessories = productReferenceService
				.getProductReferencesForSourceProduct(camera2, ProductReferenceTypeEnum.ACCESSORIES, true);

		//expected only 1 accessory:
		//ACCESSORIES -> batteryAAA (qty: 2)
		assertThat(camera2ActiveAccessories).hasSize(1);
		assertCollectionHasReferenceOfTypeForProduct(camera2ActiveAccessories, batteryAAA, ProductReferenceTypeEnum.ACCESSORIES,
				false);

	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForNullTargetProduct()
	{
		productReferenceService.getProductReferencesForTargetProduct(null, ProductReferenceTypeEnum.CONSISTS_OF, true);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.references.ProductReferenceService#getProductReferencesForTargetProduct(de.hybris.platform.core.model.product.ProductModel, ProductReferenceTypeEnum, boolean)}
	 * .
	 */
	@Test
	public void testGetProductReferencesForTargetProduct()
	{
		//non persisted model case
		assertThat(productReferenceService.getProductReferencesForTargetProduct(new ProductModel(), null, true)).isEmpty();

		//get all product references in which adapter is an accessory
		final Collection<ProductReferenceModel> adapterIsAccessoryReferences = productReferenceService
				.getProductReferencesForTargetProduct(adapter, ProductReferenceTypeEnum.ACCESSORIES, false);

		//expect 2: it is accessory for both cameras.
		assertThat(adapterIsAccessoryReferences).hasSize(2);
		assertCollectionHasReferenceOfTypeForProduct(adapterIsAccessoryReferences, camera1, ProductReferenceTypeEnum.ACCESSORIES,
				true);
		assertCollectionHasReferenceOfTypeForProduct(adapterIsAccessoryReferences, camera2, ProductReferenceTypeEnum.ACCESSORIES,
				true);

		//get all active product references in which adapter is an accessory
		final Collection<ProductReferenceModel> adapterIsActiveAccessoryReferences = productReferenceService
				.getProductReferencesForTargetProduct(adapter, ProductReferenceTypeEnum.ACCESSORIES, true);

		//expect empty
		assertThat(adapterIsActiveAccessoryReferences).isEmpty();

		//get active product references in which lenses is target of any type of reference
		final Collection<ProductReferenceModel> lensesIsActiveTargetReferences = productReferenceService
				.getProductReferencesForTargetProduct(lense, null, true);

		//expect 3 references
		assertThat(lensesIsActiveTargetReferences).hasSize(3);
		assertCollectionHasReferenceOfTypeForProduct(lensesIsActiveTargetReferences, camera2, ProductReferenceTypeEnum.SPAREPART,
				true);
		assertCollectionHasReferenceOfTypeForProduct(lensesIsActiveTargetReferences, tripod, ProductReferenceTypeEnum.FOLLOWUP,
				true);
		assertCollectionHasReferenceOfTypeForProduct(lensesIsActiveTargetReferences, tripod, ProductReferenceTypeEnum.UPSELLING,
				true);

	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForNullSourceAndTarget()
	{
		productReferenceService.getProductReferencesForSourceAndTarget(null, new ProductModel(), true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForSourceAndNullTarget()
	{
		productReferenceService.getProductReferencesForSourceAndTarget(new ProductModel(), null, false);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.references.ProductReferenceService#getProductReferencesForSourceAndTarget(de.hybris.platform.core.model.product.ProductModel, de.hybris.platform.core.model.product.ProductModel, boolean)}
	 * .
	 */
	@Test
	public void testGetProductReferencesForSourceAndTarget()
	{
		//unsaved models case
		assertThat(productReferenceService.getProductReferencesForSourceAndTarget(new ProductModel(), new ProductModel(), true))
				.isEmpty();

		//no such reference case:
		assertThat(productReferenceService.getProductReferencesForSourceAndTarget(camera1, lense, false)).isEmpty();

		//no such active reference case:
		assertThat(productReferenceService.getProductReferencesForSourceAndTarget(camera1, adapter, true)).isEmpty();

		//but when checking also inactive ones, there should be something found.
		final Collection<ProductReferenceModel> camera1ToAdapterReferences = productReferenceService
				.getProductReferencesForSourceAndTarget(camera1, adapter, false);
		assertSingleReference(camera1ToAdapterReferences, camera1, adapter, ProductReferenceTypeEnum.ACCESSORIES);

		//if we have more than one references between source and target? (tripod -> lense)
		final Collection<ProductReferenceModel> tripodToLense = productReferenceService.getProductReferencesForSourceAndTarget(
				tripod, lense, false);

		assertThat(tripodToLense).hasSize(2);
		assertCollectionHasReference(tripodToLense, tripod, lense, ProductReferenceTypeEnum.UPSELLING);
		assertCollectionHasReference(tripodToLense, tripod, lense, ProductReferenceTypeEnum.FOLLOWUP);

		//the reverse configuration: lense->tripod should return empty.
		assertThat(productReferenceService.getProductReferencesForSourceAndTarget(lense, tripod, false)).isEmpty();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForNullSource()
	{
		productReferenceService.getProductReferences("test", null, camera1, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetProductReferencesForNullTarget()
	{
		productReferenceService.getProductReferences("test", camera1, null, true);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.catalog.references.ProductReferenceService#getProductReferences(java.lang.String, de.hybris.platform.core.model.product.ProductModel, de.hybris.platform.core.model.product.ProductModel, boolean)}
	 * .
	 */
	@Test
	public void testGetProductReferences()
	{
		//qualifier 'cam1tripod' : camera1 -> tripod
		final Collection<ProductReferenceModel> cam1tripod = productReferenceService.getProductReferences("cam1tripod", camera1,
				tripod, true);
		assertThat(cam1tripod).hasSize(1);
		assertCollectionHasReference(cam1tripod, camera1, tripod, ProductReferenceTypeEnum.CROSSELLING);

		//by null name - should be found
		final Collection<ProductReferenceModel> nullQualifier = productReferenceService.getProductReferences(null, camera1, tripod,
				true);
		assertThat(nullQualifier).hasSize(1);
		assertCollectionHasReference(nullQualifier, camera1, tripod, ProductReferenceTypeEnum.CROSSELLING);

		//by empty name - should not be found
		final Collection<ProductReferenceModel> emptyQualifier = productReferenceService.getProductReferences("", camera1, tripod,
				true);
		assertThat(emptyQualifier).hasSize(1);
		assertCollectionHasReference(emptyQualifier, camera1, tripod, ProductReferenceTypeEnum.CROSSELLING);

		//by wrong name - should not be found
		assertThat(productReferenceService.getProductReferences("wrongName", camera1, tripod, true)).isEmpty();


		//qualifier 'tripodlense1': tripod -> lense (upselling)
		//qualifier 'tripodlense2': tripod -> lense (followup)

		//by empty name - both should be found
		final Collection<ProductReferenceModel> emptyQualifier2 = productReferenceService.getProductReferences("", tripod, lense,
				true);
		assertThat(emptyQualifier2).hasSize(2);
		assertCollectionHasReference(emptyQualifier2, tripod, lense, ProductReferenceTypeEnum.UPSELLING);
		assertCollectionHasReference(emptyQualifier2, tripod, lense, ProductReferenceTypeEnum.FOLLOWUP);

		//by null name, both should be found
		final Collection<ProductReferenceModel> nullQualifier2 = productReferenceService.getProductReferences(null, tripod, lense,
				true);
		assertThat(nullQualifier2).hasSize(2);
		assertCollectionHasReference(nullQualifier2, tripod, lense, ProductReferenceTypeEnum.UPSELLING);
		assertCollectionHasReference(nullQualifier2, tripod, lense, ProductReferenceTypeEnum.FOLLOWUP);



		//by correct names only particular ones should be found,
		final Collection<ProductReferenceModel> tripodlense1 = productReferenceService.getProductReferences("tripodlense1", tripod,
				lense, true);
		assertThat(tripodlense1).hasSize(1);
		assertCollectionHasReference(tripodlense1, tripod, lense, ProductReferenceTypeEnum.UPSELLING);

		final Collection<ProductReferenceModel> tripodlense2 = productReferenceService.getProductReferences("tripodlense2", tripod,
				lense, true);
		assertThat(tripodlense2).hasSize(1);
		assertCollectionHasReference(tripodlense2, tripod, lense, ProductReferenceTypeEnum.FOLLOWUP);

	}

	private ProductModel getProduct(final String code)
	{
		final ProductModel product = productService.getProductForCode(code);
		Assert.assertNotNull(product);
		return product;
	}

	private void assertCollectionHasReferenceOfTypeForProduct(final Collection<ProductReferenceModel> referencesToTest,
			final ProductModel product, final ProductReferenceTypeEnum type, final boolean source)
	{
		final String msg = "Returned references do not contain " + (source ? "source" : "target") + " reference for product "
				+ product + " of type " + type;
		if (referencesToTest == null)
		{
			Assert.fail(msg);
		}
		for (final ProductReferenceModel reference : referencesToTest)
		{
			if (source)
			{
				if (reference.getSource().equals(product) && reference.getReferenceType().equals(type))
				{
					return;
				}
			}
			else
			{
				if (reference.getTarget().equals(product) && reference.getReferenceType().equals(type))
				{
					return;
				}
			}
		}
		Assert.fail(msg);
	}

	private void assertCollectionHasReference(final Collection<ProductReferenceModel> referencesToTest,
			final ProductModel sourceProduct, final ProductModel targetProduct, final ProductReferenceTypeEnum type)
	{
		final String msg = "Returned references do not contain reference from product " + sourceProduct + " to product "
				+ targetProduct + " of type " + type;
		if (referencesToTest == null)
		{
			Assert.fail(msg);
		}
		for (final ProductReferenceModel reference : referencesToTest)
		{
			if (reference.getSource().equals(sourceProduct) && reference.getTarget().equals(targetProduct)
					&& reference.getReferenceType().equals(type))
			{
				return;
			}
		}
		Assert.fail(msg);
	}

	private void assertSingleReference(final Collection<ProductReferenceModel> referencesToCheck,
			final ProductModel expectedSource, final ProductModel expectedTarget, final ProductReferenceTypeEnum expectedType)
	{
		final String msg = expectedType + " reference from " + expectedSource + " to " + expectedTarget + " was not found";
		assertThat(referencesToCheck).hasSize(1);
		final ProductReferenceModel reference = referencesToCheck.iterator().next();
		Assert.assertEquals(msg, expectedSource, reference.getSource());
		Assert.assertEquals(msg, expectedTarget, reference.getTarget());
		Assert.assertEquals(msg, expectedType, reference.getReferenceType());
	}


}
