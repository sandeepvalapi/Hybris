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
package de.hybris.platform.classification.strategy.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.daos.ProductFeaturesDao;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLoadStoreFeaturesStrategyTest
{
	private static final String PROD_FEAT_QUALIFIER_1 = "PROD_FEAT_QUAL_1";
	private static final String PROD_FEAT_QUALIFIER_2 = "PROD_FEAT_QUAL_2";
	private static final String PROD_FEAT_DESC_1 = "PROD_FEAT_DESC_1";
	private static final String PROD_FEAT_DESC_2 = "PROD_FEAT_DESC_2";
	private static final String PROD_FEAT_DESC_3 = "PROD_FEAT_DESC_3";
	private static final String PROD_FEAT_VALUE_1 = "PROD_FEAT_VALUE_1";
	private static final String PROD_FEAT_VALUE_2 = "PROD_FEAT_VALUE_2";
	private static final String PROD_FEAT_VALUE_3 = "PROD_FEAT_VALUE_3";
	private static final String CLS_ATTR_CODE_2 = "CLS_ATTR_CODE_2";
	private static final String CLS_ATTR_CODE_1 = "CLS_ATTR_CODE_1";
	private static final String FEATURE_CODE_1 = "myID/someVer/classificationClassCODE.cls_attr_code_1";
	private static final String FEATURE_CODE_2 = "myID/someVer/classificationClassCODE.cls_attr_code_2";

	@InjectMocks
	private final DefaultLoadStoreFeaturesStrategy strategy = new DefaultLoadStoreFeaturesStrategy();
	@Mock
	private ProductFeaturesDao productFeaturesDao;
	@Mock
	private CommonI18NService commonI18nService;
	@Mock
	private I18NService i18nService;
	@Mock
	private ModelService modelService; //NOPMD
	@Mock
	private ProductModel product;
	@Mock
	private ClassificationClassModel classification;
	@Mock
	private ClassAttributeAssignmentModel assignment1, assignment2;
	@Mock
	private ProductFeatureModel productFeature1, productFeature2, productFeature3;
	@Mock
	private ClassificationAttributeModel classAttribute1, classAttribute2;
	@Mock
	private ClassificationAttributeUnitModel unit1, unit2;
	@Mock
	private LanguageModel language;
	@Mock
	private ClassificationSystemVersionModel systemVersion;
	@Mock
	private ClassificationSystemModel system;

	private PK pkMock1, pkMock2;
	private List<ClassAttributeAssignmentModel> assigments;
	private List<List<ItemModel>> daoResult;

	@Before
	public void setUp() throws Exception
	{
		assigments = new ArrayList<ClassAttributeAssignmentModel>();
		assigments.add(assignment1);
		assigments.add(assignment2);

		pkMock1 = PK.createFixedUUIDPK(1, 10);
		pkMock2 = PK.createFixedUUIDPK(1, 11);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.strategy.impl.DefaultLoadStoreFeaturesStrategy#loadFeatures(List, ProductModel)}
	 * .
	 */
	@Test
	public void shouldLoadFeaturesForProduct()
	{
		// given
		daoResult = new ArrayList<List<ItemModel>>();
		daoResult.add(Lists.newArrayList(productFeature1, assignment1));
		daoResult.add(Lists.newArrayList(productFeature2, assignment2));

		given(productFeaturesDao.findProductFeaturesByProductAndAssignment(product, assigments)).willReturn(daoResult);
		given(assignment1.getClassificationAttribute()).willReturn(classAttribute1);
		given(assignment2.getClassificationAttribute()).willReturn(classAttribute2);

		given(assignment1.getSystemVersion()).willReturn(systemVersion);
		given(assignment1.getClassificationClass()).willReturn(classification);
		given(assignment1.getClassificationAttribute()).willReturn(classAttribute1);
		given(systemVersion.getCatalog()).willReturn(system);
		given(systemVersion.getVersion()).willReturn("someVer");
		given(system.getId()).willReturn("myID");
		given(classification.getCode()).willReturn("classificationClassCODE");

		given(assignment2.getSystemVersion()).willReturn(systemVersion);
		given(assignment2.getClassificationClass()).willReturn(classification);
		given(assignment2.getClassificationAttribute()).willReturn(classAttribute2);
		given(systemVersion.getCatalog()).willReturn(system);
		given(systemVersion.getVersion()).willReturn("someVer");
		given(system.getId()).willReturn("myID");
		given(classification.getCode()).willReturn("classificationClassCODE");

		given(classAttribute1.getCode()).willReturn(CLS_ATTR_CODE_1);
		given(classAttribute2.getCode()).willReturn(CLS_ATTR_CODE_2);
		given(productFeature1.getValue()).willReturn(PROD_FEAT_VALUE_1);
		given(productFeature1.getDescription()).willReturn(PROD_FEAT_DESC_1);
		given(productFeature1.getUnit()).willReturn(unit1);
		given(productFeature1.getPk()).willReturn(pkMock1);
		given(productFeature2.getValue()).willReturn(PROD_FEAT_VALUE_2);
		given(productFeature2.getDescription()).willReturn(PROD_FEAT_DESC_2);
		given(productFeature2.getUnit()).willReturn(unit2);
		given(productFeature2.getPk()).willReturn(pkMock2);

		// when
		final List<Feature> features = strategy.loadFeatures(assigments, product);
		final FeatureList fList = new FeatureList(features);

		// then
		assertThat(features).isNotNull();
		assertThat(features).hasSize(2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1)).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2)).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue()).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue()).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getValue()).isEqualTo(PROD_FEAT_VALUE_1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getValue()).isEqualTo(PROD_FEAT_VALUE_2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getDescription()).isEqualTo(PROD_FEAT_DESC_1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getDescription()).isEqualTo(PROD_FEAT_DESC_2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getUnit()).isEqualTo(unit1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getUnit()).isEqualTo(unit2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.strategy.impl.DefaultLoadStoreFeaturesStrategy#loadFeatures(List, ProductModel)}
	 * .
	 */
	@Test
	public void shouldLoadFeaturesForProductWithLocalizedAssignment()
	{
		// given
		daoResult = new ArrayList<List<ItemModel>>();
		daoResult.add(Lists.newArrayList(productFeature1, assignment1));
		daoResult.add(Lists.newArrayList(productFeature2, assignment2));
		daoResult.add(Lists.newArrayList(productFeature3, assignment2));
		final Locale locale = new Locale("en");

		given(productFeaturesDao.findProductFeaturesByProductAndAssignment(product, assigments)).willReturn(daoResult);
		given(assignment1.getClassificationAttribute()).willReturn(classAttribute1);
		given(assignment2.getClassificationAttribute()).willReturn(classAttribute2);
		given(assignment1.getLocalized()).willReturn(Boolean.TRUE);
		given(assignment2.getLocalized()).willReturn(Boolean.TRUE);
		given(classAttribute1.getCode()).willReturn(CLS_ATTR_CODE_1);
		given(classAttribute2.getCode()).willReturn(CLS_ATTR_CODE_2);
		given(productFeature1.getValue()).willReturn(PROD_FEAT_VALUE_1);
		given(productFeature1.getDescription()).willReturn(PROD_FEAT_DESC_1);
		given(productFeature1.getUnit()).willReturn(unit1);
		//		given(productFeature1.getPk()).willReturn(pkMock1);
		given(productFeature1.getLanguage()).willReturn(language);
		given(productFeature2.getValue()).willReturn(PROD_FEAT_VALUE_2);
		given(productFeature2.getDescription()).willReturn(PROD_FEAT_DESC_2);
		given(productFeature2.getUnit()).willReturn(unit2);
		//		given(productFeature2.getPk()).willReturn(pkMock2);
		given(productFeature2.getLanguage()).willReturn(language);
		given(productFeature3.getValue()).willReturn(PROD_FEAT_VALUE_3);
		given(productFeature3.getDescription()).willReturn(PROD_FEAT_DESC_3);
		given(productFeature3.getUnit()).willReturn(unit2);
		//		given(productFeature3.getPk()).willReturn(pkMock2);
		given(productFeature3.getLanguage()).willReturn(language);
		given(commonI18nService.getLocaleForLanguage(language)).willReturn(locale);
		given(i18nService.getCurrentLocale()).willReturn(locale);


		given(assignment1.getSystemVersion()).willReturn(systemVersion);
		given(assignment1.getClassificationClass()).willReturn(classification);
		given(assignment1.getClassificationAttribute()).willReturn(classAttribute1);
		given(systemVersion.getCatalog()).willReturn(system);
		given(systemVersion.getVersion()).willReturn("someVer");
		given(system.getId()).willReturn("myID");
		given(classification.getCode()).willReturn("classificationClassCODE");

		given(assignment2.getSystemVersion()).willReturn(systemVersion);
		given(assignment2.getClassificationClass()).willReturn(classification);
		given(assignment2.getClassificationAttribute()).willReturn(classAttribute2);
		given(systemVersion.getCatalog()).willReturn(system);
		given(systemVersion.getVersion()).willReturn("someVer");
		given(system.getId()).willReturn("myID");
		given(classification.getCode()).willReturn("classificationClassCODE");

		// when
		final List<Feature> features = strategy.loadFeatures(assigments, product);
		final FeatureList fList = new FeatureList(features);

		// then
		assertThat(features).isNotNull();
		assertThat(features).hasSize(2);
		assertThat(features.get(0)).isInstanceOf(LocalizedFeature.class);
		assertThat(features.get(1)).isInstanceOf(LocalizedFeature.class);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1)).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2)).isNotNull();
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValues()).hasSize(1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValues()).hasSize(2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getValue()).isEqualTo(PROD_FEAT_VALUE_1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getValue()).isEqualTo(PROD_FEAT_VALUE_2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getDescription()).isEqualTo(PROD_FEAT_DESC_1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getDescription()).isEqualTo(PROD_FEAT_DESC_2);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_1).getValue().getUnit()).isEqualTo(unit1);
		assertThat(fList.getFeatureByCode(FEATURE_CODE_2).getValue().getUnit()).isEqualTo(unit2);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.classification.strategy.impl.DefaultLoadStoreFeaturesStrategy#loadFeatures(List, ProductModel)}
	 * .
	 */
	@Test
	public void shouldLoadUntypedFeaturesForProduct()
	{
		// given
		daoResult = new ArrayList<List<ItemModel>>();
		final List<ItemModel> row1 = new ArrayList<ItemModel>();
		row1.add(productFeature1);
		row1.add(null);
		final List<ItemModel> row2 = new ArrayList<ItemModel>();
		row2.add(productFeature2);
		row2.add(null);
		daoResult.add(row1);
		daoResult.add(row2);

		given(productFeaturesDao.findProductFeaturesByProductAndAssignment(product, null)).willReturn(daoResult);
		given(productFeature1.getQualifier()).willReturn(PROD_FEAT_QUALIFIER_1);
		given(productFeature2.getQualifier()).willReturn(PROD_FEAT_QUALIFIER_2);
		given(productFeature1.getValue()).willReturn(PROD_FEAT_VALUE_1);
		given(productFeature2.getValue()).willReturn(PROD_FEAT_VALUE_2);

		// when
		final List<Feature> features = strategy.loadFeatures(Collections.EMPTY_LIST, product);
		final FeatureList fList = new FeatureList(features);

		// then
		assertThat(features).isNotNull();
		assertThat(features).hasSize(2);
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_1)).isNotNull();
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_2)).isNotNull();
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_1).getValues()).hasSize(1);
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_2).getValues()).hasSize(1);
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_1).getClassAttributeAssignment()).isNull();
		assertThat(fList.getFeatureByCode(PROD_FEAT_QUALIFIER_2).getClassAttributeAssignment()).isNull();
	}


}
