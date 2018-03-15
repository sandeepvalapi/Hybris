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
package de.hybris.platform.validation.coverage.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.CoverageConstraintGroupModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.coverage.CoverageCalculationService;
import de.hybris.platform.validation.coverage.CoverageInfo;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Tests for {@link CoverageCalculationService}.
 */
@IntegrationTest
public class CoverageCalculationServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	CoverageCalculationService coverageCalculationService;

	@Resource
	ModelService modelService;

	@Resource
	TypeService typeService;

	@Resource
	ValidationService validationService;

	@Resource
	LanguageDao languageDao;

	private ProductModel testProduct;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("en");
		getOrCreateLanguage("de");
		getOrCreateLanguage("fr");

		final CatalogModel catalog1 = modelService.create(CatalogModel.class);
		catalog1.setId("catalog1");
		modelService.save(catalog1);

		final CatalogVersionModel testCatalogVersion = modelService.create(CatalogVersionModel.class);
		testCatalogVersion.setCatalog(catalog1);
		testCatalogVersion.setVersion("v1.0");

		testProduct = modelService.create(ProductModel.class);
		testProduct.setCode("testProduct");
		testProduct.setCatalogVersion(testCatalogVersion);

		modelService.saveAll();
	}

	@Test
	public void shouldReturn100PercentCoverageForConstraintsWithoutViolations() throws Exception
	{
		//given
		final AbstractConstraintModel notNullNameConstraint = createNotNullConstraint("notNullName", "Product", ProductModel.NAME,
				null);
		final AbstractConstraintModel notNullDeliveryTimeConstraint = createNotNullConstraint("notNullDeliveryTime", "Product",
				ProductModel.DELIVERYTIME, null);
		final AbstractConstraintModel sizeEanConstraint = createSizeConstraint("sizeEan", Long.valueOf(8), Long.valueOf(13),
				"Product", ProductModel.EAN);

		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameConstraint, notNullDeliveryTimeConstraint,
				sizeEanConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);

		modelService.save(constraintGroup);
		testProduct.setName("testName");
		testProduct.setEan("Ean_Correct");
		testProduct.setDeliveryTime(12d);
		modelService.save(testProduct);

		validationService.reloadValidationEngine();

		//when
		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		//then
		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(1.0d);
	}

	@Test
	public void shouldReturnZeroCoverageForAllConstrainsWithViolations() throws Exception
	{
		//given
		final AbstractConstraintModel notNullNameConstraint = createNotNullConstraint("notNullName", "Product", ProductModel.NAME,
				null);
		final AbstractConstraintModel notNullDeliveryTimeConstraint = createNotNullConstraint("notNullDeliveryTime", "Product",
				ProductModel.DELIVERYTIME, null);
		final AbstractConstraintModel sizeEanConstraint = createSizeConstraint("sizeEan", Long.valueOf(8), Long.valueOf(13),
				"Product", ProductModel.EAN);

		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameConstraint, notNullDeliveryTimeConstraint,
				sizeEanConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);

		modelService.save(constraintGroup);
		testProduct.setEan("Ean_Attribute_Too_Long");
		modelService.save(testProduct);
		assertThat(testProduct.getName()).isNull();
		assertThat(testProduct.getDeliveryTime()).isNull();

		validationService.reloadValidationEngine();
		//when
		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		//then
		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(0.0d);
	}

	@Test
	public void shouldReturn100PercentCoverageForAllLocalesConstraintWithoutViolations() throws Exception
	{
		final AbstractConstraintModel notNullNameLocalizedConstraint = createNotNullConstraint("notNullLocalizedName", "Product",
				ProductModel.NAME, ImmutableSet.of(getLanguage("en"), getLanguage("de"), getLanguage("fr")));
		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameLocalizedConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);
		modelService.save(constraintGroup);

		testProduct.setName("myProduct", Locale.ENGLISH);
		testProduct.setName("meinProdukt", Locale.GERMAN);
		testProduct.setName("Je ne sais pas", Locale.FRENCH);

		modelService.save(testProduct);

		validationService.reloadValidationEngine();

		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(1.0);
	}

	@Test
	public void shouldReturnZeroPercentCoverageForAllLocalesConstraintWithViolations() throws Exception
	{
		final AbstractConstraintModel notNullNameLocalizedConstraint = createNotNullConstraint("notNullLocalizedName", "Product",
				ProductModel.NAME, ImmutableSet.of(getLanguage("en"), getLanguage("de"), getLanguage("fr")));
		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameLocalizedConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);
		modelService.save(constraintGroup);

		testProduct.setName(null, Locale.ENGLISH);
		testProduct.setName(null, Locale.GERMAN);
		testProduct.setName(null, Locale.FRENCH);

		modelService.save(testProduct);

		validationService.reloadValidationEngine();

		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(0.0d);
	}

	@Test
	public void shouldReturnHalfCoverageFor1of2LocalesConstraintWithViolations() throws Exception
	{
		final AbstractConstraintModel notNullNameLocalizedConstraint = createNotNullConstraint("notNullLocalizedName", "Product",
				ProductModel.NAME, ImmutableSet.of(getLanguage("en"), getLanguage("de")));
		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameLocalizedConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);
		modelService.save(constraintGroup);

		testProduct.setName("myProduct", Locale.ENGLISH);
		testProduct.setName(null, Locale.GERMAN);

		modelService.save(testProduct);

		validationService.reloadValidationEngine();

		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(0.5d);
	}

	@Test
	public void shouldReturnPropertyInfoMessagesForAllLocalesConstraintWithViolations() throws Exception
	{
		final String itemType = "Product";
		final String attribute = ProductModel.NAME;
		final Locale locale = Locale.GERMAN;
		final String languageCode = locale.getLanguage();

		final AbstractConstraintModel notNullNameLocalizedConstraint = createNotNullConstraint("notNullLocalizedName", itemType,
				attribute, ImmutableSet.of(getLanguage(languageCode)));
		final Set<AbstractConstraintModel> constraints = Sets.newHashSet(notNullNameLocalizedConstraint);

		final CoverageConstraintGroupModel constraintGroup = modelService.create(CoverageConstraintGroupModel.class);
		constraintGroup.setId("testConstraintGroup");
		constraintGroup.setConstraints(constraints);
		modelService.save(constraintGroup);

		testProduct.setName(null, locale);

		modelService.save(testProduct);

		validationService.reloadValidationEngine();

		final CoverageInfo coverageInfo = coverageCalculationService.calculate(testProduct, null);

		assertThat(coverageInfo).isNotNull();
		assertThat(coverageInfo.getPropertyInfoMessages()).isNotEmpty();

		final String expectedPropertyQualifier = itemType + "." + attribute + "[" + languageCode + "]";
		final String expectedPropertyInfoMsg = "The attribute \"" + attribute + "\" must not be null in language: "
				+ locale.getDisplayLanguage() + ".";

		assertThat(coverageInfo.getPropertyInfoMessages().get(0).getPropertyQualifier()).isEqualTo(expectedPropertyQualifier);
		assertThat(coverageInfo.getPropertyInfoMessages().get(0).getMessage()).isEqualTo(expectedPropertyInfoMsg);
		assertThat(coverageInfo.getCoverageIndex()).isEqualTo(0.0d);
	}


	private NotNullConstraintModel createNotNullConstraint(final String id, final String itemType, final String attribute,
			final Set<LanguageModel> languages)
	{
		final NotNullConstraintModel constraint = modelService.create(NotNullConstraintModel.class);
		constraint.setActive(true);
		constraint.setId(id);
		constraint.setAnnotation(NotNull.class);
		constraint.setDescriptor(typeService.getAttributeDescriptor(itemType, attribute));
		if (CollectionUtils.isNotEmpty(languages))
		{
			constraint.setLanguages(languages);
		}
		modelService.save(constraint);

		return constraint;
	}

	private SizeConstraintModel createSizeConstraint(final String id, final Long min, final Long max, final String itemType,
			final String attribute)
	{
		final SizeConstraintModel constraint = modelService.create(SizeConstraintModel.class);
		constraint.setActive(true);
		constraint.setId(id);
		constraint.setMin(min);
		constraint.setMax(max);
		constraint.setDescriptor(typeService.getAttributeDescriptor(itemType, attribute));
		modelService.save(constraint);

		return constraint;
	}

	private LanguageModel getLanguage(final String code)
	{
		return languageDao.findLanguagesByCode(code).get(0);
	}

}
