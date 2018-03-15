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
package de.hybris.platform.validation.services.impl;


import de.hybris.platform.variants.model.VariantProductModel;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.model.constraints.NotBlankConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;


@IntegrationTest
public class LocalizationAwareValidationServiceTest extends AbstractConstraintTest
{
	@Resource
	private LanguageDao languageDao;

	@Resource
	private ValidationService validationService;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private CommonI18NService commonI18NService;

	private Collection<ConstraintGroupModel> groups;
	private CatalogModel catalog;
	private CatalogVersionModel catalogVersion;

	@Before
	public void doBefore()
	{
		getOrCreateLanguage("en");
		getOrCreateLanguage("de");
		getOrCreateLanguage("fr");
		getOrCreateLanguage("es_CO");

		catalog = modelService.create(CatalogModel.class);
		catalog.setId("defaultCatalog");
		catalog.setName("defaultCatalog");

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("versionOne");
		catalogVersion.setCatalog(catalog);

		groups = validationService.getActiveConstraintGroups();
	}

	private LanguageModel getEnglish()
	{
		return languageDao.findLanguagesByCode("en").get(0);
	}

	private LanguageModel getFrench()
	{
		return languageDao.findLanguagesByCode("fr").get(0);
	}

	private LanguageModel getGerman()
	{
		return languageDao.findLanguagesByCode("de").get(0);
	}

	private LanguageModel getColumbiaSpanish()
	{
		return languageDao.findLanguagesByCode("es_CO").get(0);
	}

	private void createNotNullConstraint(final String attribute, final Severity severity, final Set<LanguageModel> languages)
	{
		final ComposedTypeModel productType = typeService.getComposedTypeForClass(ProductModel.class);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(productType, attribute);

		final NotNullConstraintModel constraint = modelService.create(NotNullConstraintModel.class);
		constraint.setId(UUID.randomUUID().toString());
		constraint.setDescriptor(attributeDescriptor);
		constraint.setLanguages(languages);
		constraint.setSeverity(severity);

		modelService.save(constraint);
	}

	private void createNotBlank(final String attribute, final Severity severity, final Set<LanguageModel> languages)
	{
		createNotBlank(attribute, severity, languages, null);
	}

	private void createNotBlank(final String attribute, final Severity severity, final Set<LanguageModel> languages,
			final Set<ConstraintGroupModel> groups)
	{
		final ComposedTypeModel productType = typeService.getComposedTypeForClass(ProductModel.class);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(productType, attribute);

		final NotBlankConstraintModel constraint = modelService.create(NotBlankConstraintModel.class);
		constraint.setId(UUID.randomUUID().toString());
		constraint.setDescriptor(attributeDescriptor);
		constraint.setLanguages(languages);
		constraint.setSeverity(severity);

		if (groups != null)
		{
			constraint.setConstraintGroups(groups);
		}

		modelService.save(constraint);
	}

	private void createConstraintSizeBetween5And10(final Class clazz, final String attribute, final Severity severity,
			final Set<LanguageModel> languages)
	{
		createConstraintSizeBetween5And10(clazz, attribute, severity, languages, null);
	}

	private void createConstraintSizeBetween5And10(final Class clazz, final String attribute, final Severity severity,
			final Set<LanguageModel> languages, final Set<ConstraintGroupModel> groups)
	{
		final ComposedTypeModel productType = typeService.getComposedTypeForClass(clazz);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(productType, attribute);
		final SizeConstraintModel constraint = modelService.create(SizeConstraintModel.class);
		constraint.setId(UUID.randomUUID().toString());
		constraint.setMin(Long.valueOf(5));
		constraint.setMax(Long.valueOf(10));
		constraint.setDescriptor(attributeDescriptor);
		constraint.setLanguages(languages);
		constraint.setSeverity(severity);
		if (groups != null)
		{
			constraint.setConstraintGroups(groups);
		}

		modelService.save(constraint);
	}

	private ProductModel createBlankProduct()
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("a");
		product.setCatalogVersion(catalogVersion);

		return product;
	}

	private VariantProductModel createBlankVariantProduct()
	{
		final VariantProductModel product = modelService.create(VariantProductModel.class);
		product.setCode("varA");
		product.setCatalogVersion(catalogVersion);

		return product;
	}



	@Test
	public void emptyValueShouldNotTriggerViolation()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(0);
	}

	@Test
	public void shouldConvertLanguageToLocaleWithCapitalizedLetters()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getColumbiaSpanish()));
		validationService.reloadValidationEngine();
		final Locale columbianLocale = commonI18NService.getLocaleForLanguage(getColumbiaSpanish());

		final ProductModel product = createBlankProduct();
		product.setName("plata o plomo", columbianLocale);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(1);
		assertThat(violations.iterator().next()).isInstanceOf(LocalizedHybrisConstraintViolation.class);
		final LocalizedHybrisConstraintViolation violation = (LocalizedHybrisConstraintViolation) violations.iterator().next();

		assertThat(violation.getViolationLanguage()).isEqualTo(columbianLocale);
	}

	@Test
	public void tooShortValueOnlyInEnglishShouldViolate()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("ap", Locale.ENGLISH);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(1);
	}

	@Test
	public void okValueOnlyInEnglishShouldNotViolate()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("apple", Locale.ENGLISH);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(0);
	}

	@Test
	public void okValueInEnglishAndGermanShouldNotViolate()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("apple", Locale.ENGLISH);
		product.setName("apfel", Locale.GERMAN);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(0);
	}

	@Test
	public void invalidGermanValueShouldViolate()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("apple", Locale.ENGLISH);
		product.setName("ap", Locale.GERMAN);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(1);

		final HybrisConstraintViolation violation = violations.iterator().next();
		assertThat(violation).isInstanceOf(LocalizedHybrisConstraintViolation.class);
		assertThat(((LocalizedHybrisConstraintViolation) violation).getViolationLanguage()).isEqualTo(Locale.GERMAN);
	}

	@Test
	public void invalidValuesInEnglishAndGermansShouldViolateTwice()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("ap", Locale.ENGLISH);
		product.setName("ap", Locale.GERMAN);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(2);
	}

	@Test
	public void invalidValuesInEnglishAndGermansForSubtypeShouldViolateTwice()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		// VariantProductModel is a subtype of ProductModel
		final VariantProductModel product = createBlankVariantProduct();
		product.setName("ap", Locale.ENGLISH);
		product.setName("ap", Locale.GERMAN);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(2);
	}


	@Test
	public void validValuesForDifferentConstraintsShouldPass()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.DESCRIPTION, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getFrench()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("apple", Locale.ENGLISH);
		product.setName("apfel", Locale.GERMAN);

		product.setDescription("apple", Locale.GERMAN);
		product.setDescription("pomme", Locale.FRANCE);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);

		// then
		assertThat(violations).hasSize(0);
	}

	@Test
	public void invalidValuesForDifferentConstraintsShouldViolate()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.DESCRIPTION, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getFrench()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("ap", Locale.ENGLISH);
		product.setName("ap", Locale.GERMAN);

		// german is not validated by constraint
		product.setDescription("ap", Locale.GERMAN);
		product.setDescription("po", Locale.FRANCE);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validate(product, groups);
		final Set<HybrisConstraintViolation> nameViolations = validationService.validateProperty(product, "name", groups);
		final Set<HybrisConstraintViolation> descriptionViolations = validationService.validateProperty(product, "description",
				groups);

		// then
		assertThat(violations).hasSize(3);

		for (final HybrisConstraintViolation v : violations)
		{
			assertThat(v).isInstanceOf(LocalizedHybrisConstraintViolation.class);
		}

		assertThat(nameViolations).hasSize(2);
		assertThat(descriptionViolations).hasSize(1);
	}


	@Test
	public void shouldViolateTwoConstraintsOnSameAttribute()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
		createNotNullConstraint(ProductModel.NAME, Severity.ERROR, ImmutableSet.of(getEnglish(), getGerman()));
		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("apple", Locale.ENGLISH);

		// when
		final Set<HybrisConstraintViolation> notNullViolations = validationService.validate(product);

		// then
		assertThat(notNullViolations).hasSize(1);
		assertViolationLocale(notNullViolations.iterator().next(), Locale.GERMAN);

		// and when
		product.setName("apf", Locale.GERMAN);
		final Set<HybrisConstraintViolation> sizeViolations = validationService.validate(product);

		// then
		assertThat(sizeViolations).hasSize(1);
		assertViolationLocale(sizeViolations.iterator().next(), Locale.GERMAN);

		product.setName("apfel", Locale.GERMAN);

		final Set<HybrisConstraintViolation> ok = validationService.validate(product);

		assertThat(ok).hasSize(0);
	}

	@Test(expected = ModelSavingException.class)
	public void disallowCreatingLocalizedConstraintOnNonLocalizableAttribute()
	{
		// CompanyModel.NAME is non-localizable
		createConstraintSizeBetween5And10(CompanyModel.class, CompanyModel.NAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
	}

	@Test
	public void allowCreatingLocalizedConstraintOnLocalizableAttribute()
	{
		// CompanyModel.LOCNAME is localizable
		createConstraintSizeBetween5And10(CompanyModel.class, CompanyModel.LOCNAME, Severity.ERROR,
				ImmutableSet.of(getEnglish(), getGerman()));
	}

	@Test
	public void violationsShouldMatchConstraintsLocales()
	{
		// given
		createConstraintSizeBetween5And10(ProductModel.class, ProductModel.NAME, Severity.ERROR, ImmutableSet.of(getEnglish()));
		createNotBlank(ProductModel.NAME, Severity.WARN, ImmutableSet.of(getGerman()));

		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("", Locale.ENGLISH);
		product.setName("", Locale.GERMAN);

		// when
		final Set<HybrisConstraintViolation> violations = validationService.validateProperty(product, "name");

		final LocalizedHybrisConstraintViolation enViolation = (LocalizedHybrisConstraintViolation) violations.stream()
				.filter(i -> isSizeConstraint(i)).findFirst().get();
		final LocalizedHybrisConstraintViolation geViolation = (LocalizedHybrisConstraintViolation) violations.stream()
				.filter(i -> !isSizeConstraint(i)).findFirst().get();

		assertThat(enViolation.getViolationLanguage()).isEqualTo(Locale.ENGLISH);
		assertThat(geViolation.getViolationLanguage()).isEqualTo(Locale.GERMAN);
	}


	private ConstraintGroupModel createConstraintGroup(final String id, final String interfaceName)
	{
		final ConstraintGroupModel newGroup = modelService.create(ConstraintGroupModel.class);
		newGroup.setId(id);
		newGroup.setInterfaceName(interfaceName);
		modelService.save(newGroup);

		return newGroup;
	}

	@Test
	public void shouldValidateGroupConstraints()
	{
		// given
		final ConstraintGroupModel notBlankGroup = createConstraintGroup("blank", "com.blank");
		final ConstraintGroupModel sizeGroup = createConstraintGroup("size", "com.size");

		createNotBlank("name", Severity.WARN, ImmutableSet.of(getGerman()), ImmutableSet.of(notBlankGroup));
		createConstraintSizeBetween5And10(ProductModel.class, "name", Severity.WARN, ImmutableSet.of(getFrench()),
				ImmutableSet.of(sizeGroup));

		validationService.reloadValidationEngine();

		final ProductModel product = createBlankProduct();
		product.setName("", Locale.GERMAN);
		product.setName("a", Locale.FRENCH);

		// when
		final Set<HybrisConstraintViolation> attrDefaultViolations = validationService.validateProperty(product, "name");
		final Set<HybrisConstraintViolation> attrNotBlankGroupViolations = validationService.validateProperty(product, "name",
				ImmutableSet.of(notBlankGroup));
		final Set<HybrisConstraintViolation> attrSizeGroupViolations = validationService.validateProperty(product, "name",
				ImmutableSet.of(sizeGroup));
		final Set<HybrisConstraintViolation> attrAllGroupsViolations = validationService.validateProperty(product, "name",
				ImmutableSet.of(sizeGroup, notBlankGroup));

		final Set<HybrisConstraintViolation> objDefaultViolations = validationService.validate(product);
		final Set<HybrisConstraintViolation> objNotBlankGroupViolations = validationService.validate(product,
				ImmutableSet.of(notBlankGroup));
		final Set<HybrisConstraintViolation> objSizeGroupViolations = validationService.validate(product,
				ImmutableSet.of(sizeGroup));
		final Set<HybrisConstraintViolation> objAllGroupsViolations = validationService.validate(product,
				ImmutableSet.of(sizeGroup, notBlankGroup));

		// then
		assertThat(attrDefaultViolations).isEmpty();
		assertThat(objDefaultViolations).isEmpty();

		assertNotBlankGroupViolations(attrNotBlankGroupViolations);
		assertNotBlankGroupViolations(objNotBlankGroupViolations);

		assertSizeGroupViolations(attrSizeGroupViolations);
		assertSizeGroupViolations(objSizeGroupViolations);

		assertThat(attrAllGroupsViolations).hasSize(2);
		assertThat(objAllGroupsViolations).hasSize(2);
	}

	@Test
	public void shouldTriggerDefaultGroupInLocalizedValidations()
	{
		// given
		final ConstraintGroupModel defaultGroup = validationService.getDefaultConstraintGroup();
		final ConstraintGroupModel fooGroup = createConstraintGroup("fooGroup", "com.foo");

		createNotBlank("name", Severity.WARN, ImmutableSet.of(getGerman()));
		final ProductModel product = createBlankProduct();
		product.setName("", Locale.GERMAN);
		product.setName("a", Locale.FRENCH);

		validationService.reloadValidationEngine();

		// when
		final Set<HybrisConstraintViolation> implicitDefaultViolations = validationService.validate(product);
		final Set<HybrisConstraintViolation> singleDefaultViolations = validationService.validate(product,
				ImmutableSet.of(defaultGroup));
		final Set<HybrisConstraintViolation> defaultAmongstManyViolations = validationService.validate(product,
				ImmutableSet.of(defaultGroup, fooGroup));
		final Set<HybrisConstraintViolation> noDefaultViolations = validationService.validate(product, ImmutableSet.of(fooGroup));

		// then
		isNotBlankConstraint(implicitDefaultViolations.iterator().next());
		isNotBlankConstraint(singleDefaultViolations.iterator().next());
		isNotBlankConstraint(defaultAmongstManyViolations.iterator().next());
		assertThat(noDefaultViolations).isEmpty();
	}


	private void assertNotBlankGroupViolations(final Set<HybrisConstraintViolation> violations)
	{
		assertThat(violations).hasSize(1);
		isNotBlankConstraint(violations.iterator().next());
	}

	private void assertSizeGroupViolations(final Set<HybrisConstraintViolation> violations)
	{
		assertThat(violations).hasSize(1);
		isSizeConstraint(violations.iterator().next());
	}

	private boolean isSizeConstraint(final HybrisConstraintViolation firstViolation)
	{
		return firstViolation.getConstraintViolation().getConstraintDescriptor().getAnnotation().annotationType().getName()
				.contains("Size");
	}

	private boolean isNotBlankConstraint(final HybrisConstraintViolation firstViolation)
	{
		return firstViolation.getConstraintViolation().getConstraintDescriptor().getAnnotation().annotationType().getName()
				.contains("NotBlank");
	}


	private void assertViolationLocale(final HybrisConstraintViolation violation, final Locale locale)
	{
		assertThat(violation).isInstanceOf(LocalizedHybrisConstraintViolation.class);
		assertThat(((LocalizedHybrisConstraintViolation) violation).getViolationLanguage()).isEqualTo(Locale.GERMAN);
	}
}
