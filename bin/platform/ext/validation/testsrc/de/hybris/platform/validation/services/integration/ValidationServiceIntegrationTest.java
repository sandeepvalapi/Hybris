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
package de.hybris.platform.validation.services.integration;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;
import de.hybris.platform.validation.services.ValidationService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test presenting different use cases of {@link ValidationService}.
 */
@IntegrationTest
public class ValidationServiceIntegrationTest extends AbstractConstraintTest
{
	private static final String DETAILED_VIOLATION_MSG = "detailed violation for groupXXX";
	private static final String GENERAL_VIOLATION_MSG = "general violation for group";
	private ConstraintGroupModel group;
	private ProductModel product;

	@Before
	public void prepareTest()
	{
		group = createGroup("yyy");
		modelService.save(group);

		final SizeConstraintModel sizeConstraint1 = createConstraint("localizedNameSizeTest", ProductModel.class,
				ProductModel.NAME, Collections.singleton(group), GENERAL_VIOLATION_MSG);
		modelService.save(sizeConstraint1);

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("catalogOne");
		catalog.setName("catalogOne Name");


		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("versionOne");
		catalogVersion.setCatalog(catalog);

		product = modelService.create(ProductModel.class);
		product.setCode("productCode");
		product.setName("some value which is too long and will break validation");
		product.setDescription("some value which is too long and will break validation");
		product.setCatalogVersion(catalogVersion);
		modelService.saveAll(catalog, catalogVersion, product);
	}

	/**
	 * test validating a {@link de.hybris.platform.variants.jalo.VariantProduct} with constraint for a custom group
	 * PLA-10962
	 */
	@Test
	public void testLocalizedPropertyForVariantForCustomGroup()
	{
		//test for product variant
		final VariantTypeModel testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);

		final ProductModel baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseProductCode");
		baseProduct.setName("ok base product");
		baseProduct.setCatalogVersion(product.getCatalogVersion());
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

		final VariantProductModel variantProduct = modelService.create(VariantProductModel.class);
		variantProduct.setCode("variantProductCode");
		variantProduct.setName("some value which is too long and will break validation for VP");
		variantProduct.setCatalogVersion(product.getCatalogVersion());
		variantProduct.setBaseProduct(baseProduct);
		modelService.save(variantProduct);

		//reload engine
		validationService.reloadValidationEngine();

		final Set<HybrisConstraintViolation> violations = validationService.validate(variantProduct, Collections.singleton(group));
		assertFalse("There should be a violation for group(" + group + ")", violations.isEmpty());//explicitly validate
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG);
	}

	/**
	 * test validating a {@link de.hybris.platform.variants.jalo.VariantProduct} with constraint for a default group
	 * PLA-10962
	 */
	@Test
	public void testLocalizedPropertyForVariantForDefaultGroup()
	{
		//test for product variant
		final VariantTypeModel testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);


		final ProductModel baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseProductCode");
		baseProduct.setName("ok base product");
		baseProduct.setCatalogVersion(product.getCatalogVersion());
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

		final VariantProductModel variantProduct = modelService.create(VariantProductModel.class);
		variantProduct.setCode("variantProductCode");
		variantProduct.setName("some value which is too long and will break validation for VP");
		variantProduct.setCatalogVersion(product.getCatalogVersion());
		variantProduct.setBaseProduct(baseProduct);
		modelService.save(variantProduct);

		//reload engine
		validationService.reloadValidationEngine();

		assertTrue("There should be no violations for default group", validationService.validate(variantProduct).isEmpty());//explicitly validate
	}

	/**
	 * <pre>
	 * test case two constraint
	 * * one for {@link VariantProductModel#getName()} assigned to xxx group 
	 * * second for  {@link ProductModel#getName()} assigned to yyy group
	 * 
	 * validating a variant product which violates both of them validating goes 
	 * * for default group no violation should be reported 
	 * * for xx group first violation should be reported 
	 * * for yy group second violation should  be reported 
	 * * for xx,yy group both violation should be reported
	 * </pre>
	 */
	@Ignore("PLA-11256")
	@Test
	public void testOneConstraintForProductAndOneForVariantProductValidatingVariantProduct()
	{
		final ConstraintGroupModel groupOther = createGroup("xxx");
		modelService.save(groupOther);

		final SizeConstraintModel sizeConstraint1 = createConstraint("localizedNameSizeTestForXXX", VariantProductModel.class,
				ProductModel.NAME, Collections.singleton(groupOther), DETAILED_VIOLATION_MSG);
		sizeConstraint1.setMin(Long.valueOf(6));
		sizeConstraint1.setMax(Long.valueOf(26));
		modelService.save(sizeConstraint1);

		//test for product variant
		final VariantTypeModel testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);

		final ProductModel baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseProductCode");
		baseProduct.setName("ok base product");
		baseProduct.setCatalogVersion(product.getCatalogVersion());
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

		final VariantProductModel variantProduct = modelService.create(VariantProductModel.class);
		variantProduct.setCode("variantProductCode");
		variantProduct.setName("some value which is too long and will break validation for VP");
		variantProduct.setCatalogVersion(product.getCatalogVersion());
		variantProduct.setBaseProduct(baseProduct);
		modelService.save(variantProduct);

		//reload engine
		validationService.reloadValidationEngine();

		assertTrue("There should be no violation for default group ", validationService.validate(variantProduct).isEmpty());//explicitly validate

		Set<HybrisConstraintViolation> violations = null;
		violations = validationService.validate(variantProduct, Collections.singleton(groupOther));
		assertFalse("There should be a 1 violation for group(" + groupOther + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(1, violations.size());
		validateViolationMessage(violations, DETAILED_VIOLATION_MSG);

		violations = validationService.validate(variantProduct, Collections.singleton(group));
		assertFalse("There should be a 1 violation for group(" + group + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(1, violations.size());
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG);

		violations = validationService.validate(variantProduct, Arrays.asList(group, groupOther));
		assertFalse("There should be 2 violations for group(" + groupOther + "," + group + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(2, violations.size());
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG, DETAILED_VIOLATION_MSG);

	}

	/**
	 * <pre>
	 * test case two constraint
	 * * one for {@link VariantProductModel#getName()} assigned to xxx group 
	 * * second for  {@link ProductModel#getName()} assigned to yyy group
	 * 
	 * validating a  product which violates both of them validating goes 
	 * * for default group no violation should be reported 
	 * * for xx group first violation should be reported 
	 * * for yy group second violation should  be reported 
	 * * for xx,yy group both violation should be reported
	 * </pre>
	 */
	@Test
	public void testOneConstraintForProductAndOneForVariantProductValidatingProduct()
	{
		final ConstraintGroupModel groupOther = createGroup("xxx");
		modelService.save(groupOther);

		final SizeConstraintModel sizeConstraint1 = createConstraint("localizedNameSizeTestForXXX", VariantProductModel.class,
				ProductModel.NAME, Collections.singleton(groupOther), DETAILED_VIOLATION_MSG);
		sizeConstraint1.setMin(Long.valueOf(6));
		sizeConstraint1.setMax(Long.valueOf(26));
		modelService.save(sizeConstraint1);

		//test for product variant
		final VariantTypeModel testVariantTypeModel = modelService.create(VariantTypeModel.class);
		testVariantTypeModel.setCode("vt");
		testVariantTypeModel.setSingleton(Boolean.FALSE);
		testVariantTypeModel.setGenerate(Boolean.TRUE);
		testVariantTypeModel.setCatalogItemType(Boolean.FALSE);
		modelService.save(testVariantTypeModel);

		final ProductModel baseProduct = modelService.create(ProductModel.class);
		baseProduct.setCode("baseProductCode");
		baseProduct.setName("ok base product");
		baseProduct.setCatalogVersion(product.getCatalogVersion());
		baseProduct.setVariantType(testVariantTypeModel);
		modelService.save(baseProduct);

		final VariantProductModel variantProduct = modelService.create(VariantProductModel.class);
		variantProduct.setCode("variantProductCode");
		variantProduct.setName("some value which is too long and will break validation for VP");
		variantProduct.setCatalogVersion(product.getCatalogVersion());
		variantProduct.setBaseProduct(baseProduct);
		modelService.save(variantProduct);

		//reload engine
		validationService.reloadValidationEngine();

		assertTrue("There should be no violation for default group ", validationService.validate(product).isEmpty());//explicitly validate

		Set<HybrisConstraintViolation> violations = null;
		violations = validationService.validate(product, Collections.singleton(groupOther));
		assertTrue("There should be a no violations for group(" + groupOther + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(0, violations.size());

		violations = validationService.validate(product, Collections.singleton(group));
		assertFalse("There should be a 1 violation for group(" + group + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(1, violations.size());
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG);

		violations = validationService.validate(product, Arrays.asList(group, groupOther));
		assertFalse("There should be 1 violations for group(" + groupOther + "," + group + ") ", violations.isEmpty());//explicitly validate		
		assertEquals(1, violations.size());
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG);
	}

	@Test
	public void testLocalizedPropertyForProductForDefaultGroup()
	{
		//reload engine
		validationService.reloadValidationEngine();
		assertTrue("There should be no violations for default group", validationService.validate(product).isEmpty());//explicitly validate
	}

	@Test
	public void testLocalizedPropertyForProductForCustomGroup()
	{
		//reload engine
		validationService.reloadValidationEngine();
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, Collections.singleton(group));
		assertFalse("There should be a violation for group(" + group + ")", violations.isEmpty());//explicitly validate
		validateViolationMessage(violations, GENERAL_VIOLATION_MSG);
	}

	@Test
	public void shouldValidateAttributeStartingWithUppercaseChar()
	{
		// given
		final ProductModel product = createProduct();

		// when
		validationService.validateProperty(product, "Europe1PriceFactory_PTG", Collections.emptyList());
		validationService.validateProperty(product, "europe1PriceFactory_PTG", Collections.emptyList());

		// then fine
	}

	@Test
	public void shouldValidateDynamicAttributes()
	{
		createSizeBetween5And10Constraint(MediaModel.class, MediaModel.URL);
		validationService.reloadValidationEngine();

		final Set<HybrisConstraintViolation> urlSizeViolations = validationService.validate(mediaWithUrl("nope"));
		assertThat(urlSizeViolations).hasSize(1);

		final Set<HybrisConstraintViolation> noViolations = validationService.validate(mediaWithUrl("urlIsOk"));
		assertThat(noViolations).isEmpty();
	}

	private CatalogUnawareMediaModel mediaWithUrl(final String url)
	{
		final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		media.setInternalURL(url);
		return  media;
	}

	private void createSizeBetween5And10Constraint(final Class model, final String attribute)
	{
		final ComposedTypeModel productType = typeService.getComposedTypeForClass(model);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(productType, attribute);

		final SizeConstraintModel constraint = modelService.create(SizeConstraintModel.class);
		constraint.setMin(5L);
		constraint.setMax(10L);
		constraint.setId(UUID.randomUUID().toString());
		constraint.setDescriptor(attributeDescriptor);
		constraint.setSeverity(Severity.ERROR);

		modelService.save(constraint);
	}

	private ProductModel createProduct()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);

		catalogVersion.setActive(Boolean.TRUE);
		catalogVersion.setVersion("online");
		catalogVersion.setCatalog(catalog);
		catalog.setId("default");



		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("prodWithPrice");
		product.setCatalogVersion(catalogVersion);
		product.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		modelService.saveAll();

		return product;
	}

	private ConstraintGroupModel createGroup(final String id)
	{
		final ConstraintGroupModel group = modelService.create(ConstraintGroupModel.class);
		group.setId(id);
		return group;
	}

	private void validateViolationMessage(final Set<HybrisConstraintViolation> violations, final String... strings)
	{
		final List<String> expactedMessages = new ArrayList<String>(Arrays.asList(strings));
		for (final HybrisConstraintViolation viol : violations)
		{
			if (viol.getLocalizedMessage() != null)
			{
				expactedMessages.remove(viol.getLocalizedMessage());
			}
		}

		assertEquals("Among violations " + violations + " there should be all of " + strings + " messages ", 0,
				expactedMessages.size());
	}

	private SizeConstraintModel createConstraint(final String id, final Class<? extends ItemModel> type, final String attribute,
			final Set<ConstraintGroupModel> groups, final String customMessage)
	{
		final ComposedTypeModel productType = typeService.getComposedTypeForClass(type);
		final AttributeDescriptorModel nameAttrDesc = typeService.getAttributeDescriptor(productType, attribute);
		final SizeConstraintModel constraint = modelService.create(SizeConstraintModel.class);
		constraint.setId(id);
		constraint.setMin(Long.valueOf(5));
		constraint.setMax(Long.valueOf(25));
		constraint.setDescriptor(nameAttrDesc);
		constraint.setConstraintGroups(groups);
		constraint.setMessage(customMessage);
		return constraint;
	}
}
