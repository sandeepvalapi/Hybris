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

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.daos.ProductFeaturesDao;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.classification.strategy.LoadStoreFeaturesStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * Default implementation of <code>LoadStoreFeaturesStrategy</code> interface.
 */
public class DefaultLoadStoreFeaturesStrategy implements LoadStoreFeaturesStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultLoadStoreFeaturesStrategy.class);
	private ProductFeaturesDao productFeaturesDao;
	private ModelService modelService;
	private TransactionTemplate txTemplate;
	private I18NService i18nService;
	private CommonI18NService commonI18nService;

	@Override
	public List<Feature> loadFeatures(final List<ClassAttributeAssignmentModel> assignments, final ProductModel product)
	{
		checkProductModelIsPersisted(product);

		if (assignments == null || assignments.isEmpty())
		{
			return loadUntypedFeatures(product);
		}
		else
		{
			return loadTypedFeatures(assignments, product);
		}
	}

	protected List<Feature> loadUntypedFeatures(final ProductModel product)
	{
		final List<Feature> result = new ArrayList<Feature>();

		final Map<ClassAttributeAssignmentModel, List<ProductFeatureModel>> featuresMap = convertFeaturesResult(productFeaturesDao
				.findProductFeaturesByProductAndAssignment(product, null));

		final List<ProductFeatureModel> productFeatures = featuresMap.get(null);
		if (productFeatures != null)
		{
			final Map<String, List<ProductFeatureModel>> _productFeatues = new HashMap<String, List<ProductFeatureModel>>();

			for (final ProductFeatureModel productFeatureModel : productFeatures)
			{
				if (_productFeatues.containsKey(productFeatureModel.getQualifier()))
				{
					_productFeatues.get(productFeatureModel.getQualifier()).add(productFeatureModel);
				}
				else
				{
					_productFeatues.put(productFeatureModel.getQualifier(), Lists.newArrayList(productFeatureModel));
				}
			}

			for (final Map.Entry<String, List<ProductFeatureModel>> entry : _productFeatues.entrySet())
			{
				if (entry.getValue().get(0).getLanguage() == null)
				{
					result.add(new UnlocalizedFeature(entry.getKey(), getFeaturesValues(entry.getValue())));
				}
				else
				{
					result.add(new LocalizedFeature(entry.getKey(), getLocalizedFeaturesValues(entry.getValue()), i18nService
							.getCurrentLocale()));
				}
			}
		}
		return result;
	}

	protected List<Feature> loadTypedFeatures(final List<ClassAttributeAssignmentModel> assignments, final ProductModel product)
	{
		final List<Feature> result = new ArrayList<Feature>();

		final Map<ClassAttributeAssignmentModel, List<ProductFeatureModel>> featuresMap = convertFeaturesResult(productFeaturesDao
				.findProductFeaturesByProductAndAssignment(product, assignments));

		for (final ClassAttributeAssignmentModel assignment : assignments)
		{
			final List<ProductFeatureModel> productFeatures = featuresMap.get(assignment);
			if (assignment.getLocalized().booleanValue())
			{
				result.add(new LocalizedFeature(assignment, getLocalizedFeaturesValues(productFeatures), i18nService
						.getCurrentLocale()));

			}
			else
			{
				result.add(new UnlocalizedFeature(assignment, getFeaturesValues(productFeatures)));
			}
		}

		return result;
	}

	@Override
	public void storeFeatures(final ProductModel product, final List<Feature> features)
	{
		checkProductModelIsPersisted(product);

		txTemplate.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				storeFeaturesInTx(product, features);
			}
		});
	}

	@Override
	public void replaceFeatures(final List<ClassAttributeAssignmentModel> allAssignments, final ProductModel product,
			final List<Feature> features)
	{
		checkProductModelIsPersisted(product);

		txTemplate.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final List<ProductFeatureModel> correctlyStored = storeFeaturesInTx(product, features);
				deleteObsoleteValues(product, allAssignments, correctlyStored);
			}
		});
	}

	protected List<ProductFeatureModel> storeFeaturesInTx(final ProductModel product, final List<Feature> features)
	{
		final List<ProductFeatureModel> correctlyStored = new ArrayList<ProductFeatureModel>();
		int featurePosition = 0;
		for (final Feature feature : features)
		{
			if (feature instanceof UnlocalizedFeature)
			{
				final List<FeatureValue> featureValues = ((UnlocalizedFeature) feature).getValues();
				if (!featureValues.isEmpty())
				{
					correctlyStored.addAll(writeFeatureValues(product, feature.getClassAttributeAssignment(), featurePosition++,
							feature, featureValues, null));
				}
			}
			else if (feature instanceof LocalizedFeature)
			{
				final Map<Locale, List<FeatureValue>> values = ((LocalizedFeature) feature).getValuesForAllLocales();
				for (final Map.Entry<Locale, List<FeatureValue>> entry : values.entrySet())
				{
					correctlyStored.addAll(writeFeatureValues(product, feature.getClassAttributeAssignment(), featurePosition++,
							feature, entry.getValue(), entry.getKey()));
				}
			}
		}

		modelService.saveAll(correctlyStored);
		// TODO: Ask about setting modifiedtime for product

		return correctlyStored;
	}

	protected void deleteObsoleteValues(final ProductModel product, final List<ClassAttributeAssignmentModel> allAssignments,
			final List<ProductFeatureModel> correctlyStored)
	{
		final List<List<ItemModel>> foundRemainingFeatures = productFeaturesDao.findProductFeaturesByProductAndAssignment(product,
				allAssignments, correctlyStored);

		final Map<ClassAttributeAssignmentModel, List<ProductFeatureModel>> convertFeaturesResult = convertFeaturesResult(foundRemainingFeatures);

		if (foundRemainingFeatures != null && !foundRemainingFeatures.isEmpty())
		{
			final Collection<ProductFeatureModel> toRemove = new ArrayList<ProductFeatureModel>();
			for (final Map.Entry<ClassAttributeAssignmentModel, List<ProductFeatureModel>> entry : convertFeaturesResult.entrySet())
			{
				toRemove.addAll(entry.getValue());
			}
			modelService.removeAll(toRemove);
		}
	}

	@Required
	public void setProductFeaturesDao(final ProductFeaturesDao productFeaturesDao)
	{
		this.productFeaturesDao = productFeaturesDao;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setTxTemplate(final TransactionTemplate txTemplate)
	{
		this.txTemplate = txTemplate;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	@Required
	public void setCommonI18nService(final CommonI18NService commonI18nService)
	{
		this.commonI18nService = commonI18nService;
	}

	private List<FeatureValue> getFeaturesValues(final List<ProductFeatureModel> productFeatures)
	{
		List<FeatureValue> result = null;
		if (productFeatures != null)
		{
			Collections.sort(productFeatures, new ProductFeatureComparator());
			result = new ArrayList<FeatureValue>();
			for (final ProductFeatureModel productFeature : productFeatures)
			{
				final FeatureValue featureValue = new FeatureValue(productFeature.getValue(), productFeature.getDescription(),
						productFeature.getUnit(), productFeature.getPk());
				result.add(featureValue);
			}
		}
		return result;
	}

	private Map<Locale, List<FeatureValue>> getLocalizedFeaturesValues(final List<ProductFeatureModel> productFeatures)
	{
		Map<Locale, List<FeatureValue>> result = null;
		if (productFeatures != null)
		{
			Collections.sort(productFeatures, new ProductFeatureComparator());
			result = new HashMap<Locale, List<FeatureValue>>();

			for (final ProductFeatureModel productFeature : productFeatures)
			{
				final LanguageModel language = productFeature.getLanguage();
				if (language == null)
				{
					LOG.error("ProductFeature: " + productFeature + " is localized but has no language set!");
				}
				else
				{
					final Locale locale = commonI18nService.getLocaleForLanguage(productFeature.getLanguage());
					final FeatureValue featureValue = new FeatureValue(productFeature.getValue(), productFeature.getDescription(),
							productFeature.getUnit(), productFeature.getPk());
					if (result.containsKey(locale))
					{
						result.get(locale).add(featureValue);
					}
					else
					{
						result.put(locale, Lists.newArrayList(featureValue));
					}
				}
			}
		}
		return result;
	}

	private Map<ClassAttributeAssignmentModel, List<ProductFeatureModel>> convertFeaturesResult(
			final List<List<ItemModel>> features)
	{
		final Map<ClassAttributeAssignmentModel, List<ProductFeatureModel>> result = new LinkedHashMap<ClassAttributeAssignmentModel, List<ProductFeatureModel>>();
		for (final List<ItemModel> row : features)
		{
			final ProductFeatureModel productFeature = (ProductFeatureModel) row.get(0);
			final ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel) row.get(1);
			List<ProductFeatureModel> _features = result.get(assignment);
			if (_features == null)
			{
				result.put(assignment, _features = new ArrayList());
			}
			_features.add(productFeature);
		}
		return result;
	}

	private Set<ProductFeatureModel> writeFeatureValues(final ProductModel product,
			final ClassAttributeAssignmentModel assignment, final int featurePosition, final Feature feature,
			final List<FeatureValue> values, final Locale locale)
	{

		final Set<ProductFeatureModel> toSave = new HashSet<ProductFeatureModel>();

		int index = getMaxValuePosition(product, assignment);
		for (final FeatureValue featureValue : values)
		{
			ProductFeatureModel productFeature = null;
			final PK productFeaturePk = featureValue.getProductFeaturePk();

			if (productFeaturePk == null)
			{
				productFeature = createNewProductFeature(product, assignment, feature, locale);
			}
			else
			{
				final ProductFeatureModel _productFeature = modelService.get(featureValue.getProductFeaturePk());
				if (_productFeature != null && _productFeature.getProduct().equals(product))
				{
					// product has not been changed
					productFeature = _productFeature;
				}
				else
				{
					// request for changing product, existing product feature must stay and new one must to be created
					productFeature = createNewProductFeature(product, assignment, feature, locale);
				}
			}

			productFeature.setValue(featureValue.getValue());
			productFeature.setDescription(featureValue.getDescription());
			productFeature.setUnit(featureValue.getUnit());
			productFeature.setValuePosition(Integer.valueOf(++index));
			productFeature.setFeaturePosition(assignment == null ? Integer.valueOf(featurePosition) : assignment.getPosition());

			toSave.add(productFeature);
		}
		return toSave;
	}

	private int getMaxValuePosition(final ProductModel product, final ClassAttributeAssignmentModel assignment)
	{
		final List<Integer> maxValuePosition = productFeaturesDao.getProductFeatureMaxValuePosition(product, assignment);
		return maxValuePosition.get(0) == null ? 0 : maxValuePosition.get(0).intValue();
	}

	private ProductFeatureModel createNewProductFeature(final ProductModel product,
			final ClassAttributeAssignmentModel assignment, final Feature feature, final Locale locale)
	{
		ProductFeatureModel productFeature;
		productFeature = modelService.create(ProductFeatureModel.class);
		productFeature.setProduct(product);
		productFeature.setClassificationAttributeAssignment(assignment);
		productFeature.setQualifier(feature.getCode());
		if (locale != null)
		{
			try
			{
				final Locale dataLocale = i18nService.getBestMatchingLocale(locale);
				final LanguageModel language = commonI18nService.getLanguage(dataLocale.toString());
				productFeature.setLanguage(language);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.error("Cannot set language for iso code: " + locale.getLanguage());
			}
		}
		return productFeature;
	}

	private class ProductFeatureComparator implements Comparator<ProductFeatureModel>
	{

		@Override
		public int compare(final ProductFeatureModel first, final ProductFeatureModel other)
		{
			final Integer firstPosition = first.getValuePosition() == null ? Integer.valueOf(0) : first.getValuePosition();
			final Integer otherPosition = other.getValuePosition() == null ? Integer.valueOf(0) : other.getValuePosition();

			return firstPosition.intValue() - otherPosition.intValue();
		}
	}

	private void checkProductModelIsPersisted(final ProductModel product)
	{
		Preconditions.checkArgument(!modelService.isNew(product), "ProductModel is not persisted");
	}
}
