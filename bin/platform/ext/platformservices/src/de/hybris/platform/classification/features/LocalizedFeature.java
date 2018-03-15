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
package de.hybris.platform.classification.features;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * Localized feature contains collection of values for each language. Note that all methods for setting, getting and
 * clearing values which do not have <code>Locale</code> parameter will operate on current data locale.
 */
public class LocalizedFeature extends Feature
{
	private static final Logger LOG = Logger.getLogger(LocalizedFeature.class);
	private final Map<Locale, List<FeatureValue>> values;
	private final Locale currentLocale;

	/**
	 * Instantiates a new localized typed feature.
	 * 
	 * @param assignment
	 *           the assignment
	 * @param values
	 *           the values
	 * @param currentLocale
	 *           the current locale
	 */
	public LocalizedFeature(final ClassAttributeAssignmentModel assignment, final Map<Locale, List<FeatureValue>> values,
			final Locale currentLocale)
	{
		super(assignment);
		if (values == null)
		{
			this.values = new HashMap<Locale, List<FeatureValue>>();
		}
		else
		{
			this.values = Maps.newHashMap(values);
		}
		this.currentLocale = currentLocale;
	}

	/**
	 * Instantiates a new localized untyped feature.
	 * 
	 * @param code
	 *           the code
	 * @param values
	 *           the values
	 * @param currentLocale
	 *           the current locale
	 */
	public LocalizedFeature(final String code, final Map<Locale, List<FeatureValue>> values, final Locale currentLocale)
	{
		super(code);
		if (values == null)
		{
			this.values = new HashMap<Locale, List<FeatureValue>>();
		}
		else
		{
			this.values = Maps.newHashMap(values);
		}
		this.currentLocale = currentLocale;
	}

	/**
	 * Gets the whole map of values where key is <code>Locale</code> and value list of feature values.
	 * 
	 * @return the values
	 */
	public Map<Locale, List<FeatureValue>> getValuesForAllLocales()
	{
		return values;
	}

	/**
	 * Gets the list of feature values for particular locale.
	 * 
	 * @param locale
	 *           the locale
	 * @return the values
	 */
	public List<FeatureValue> getValues(final Locale locale)
	{
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues == null)
		{
			return Collections.EMPTY_LIST;
		}
		else
		{
			return Collections.unmodifiableList(values.get(locale));
		}
	}

	@Override
	public List<FeatureValue> getValues()
	{
		return getValues(currentLocale);
	}

	@Override
	public FeatureValue getValue()
	{
		return getValue(currentLocale);
	}

	public FeatureValue getValue(final Locale locale)
	{
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues == null || featureValues.isEmpty())
		{
			return null;
		}
		else
		{
			return featureValues.get(0);
		}
	}

	@Override
	public void addValue(final FeatureValue fvalue)
	{
		addValue(fvalue, currentLocale);
	}

	public void addValue(final FeatureValue fvalue, final Locale locale)
	{
		Preconditions.checkArgument(fvalue != null, "feature value is null");
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No feature values has been found for locale: " + locale + " new entry created");
			}
			values.put(locale, Lists.newArrayList(fvalue));
		}
		else
		{
			featureValues.add(fvalue);
		}
	}

	@Override
	public void addValue(final int index, final FeatureValue fvalue)
	{
		addValue(index, fvalue, currentLocale);
	}

	public void addValue(final int index, final FeatureValue fvalue, final Locale locale)
	{
		Preconditions.checkArgument(fvalue != null, "fvalue value is null");
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No feature values has been found for locale: " + locale + " new entry created");
			}
			final List<FeatureValue> _featureValues = new ArrayList<FeatureValue>();

			Preconditions.checkArgument(index <= _featureValues.size() && index >= 0, "index is not in range of: 0 and "
					+ _featureValues.size());
			_featureValues.add(index, fvalue);
			values.put(locale, _featureValues);
		}
		else
		{
			Preconditions.checkArgument(index <= featureValues.size() && index >= 0, "index is not in range of: 0 and "
					+ featureValues.size());
			featureValues.add(index, fvalue);
		}
	}

	@Override
	public boolean removeValue(final FeatureValue fvalue)
	{
		return removeValue(fvalue, currentLocale);
	}

	public boolean removeValue(final FeatureValue fvalue, final Locale locale)
	{
		Preconditions.checkArgument(fvalue != null, "feature value is null");
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues != null)
		{
			return featureValues.remove(fvalue);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cannot remove feature value: " + fvalue + " for data locale: " + locale);
			}
			return false;
		}
	}

	@Override
	public void removeAllValues()
	{
		removeAllValues(currentLocale);
	}

	public void removeAllValues(final Locale locale)
	{
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues != null)
		{
			featureValues.clear();
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cannot remove all values for data locale: " + locale);
			}
		}
	}

	@Override
	public void setValues(final List<FeatureValue> fvalues)
	{
		setValues(fvalues, currentLocale);
	}

	public void setValues(final List<FeatureValue> fvalues, final Locale locale)
	{
		Preconditions.checkArgument(fvalues != null, "feature values list is null");
		Preconditions.checkArgument(locale != null, "locale is null");

		final List<FeatureValue> featureValues = values.get(locale);
		if (featureValues != null)
		{
			featureValues.clear();
			featureValues.addAll(fvalues);
		}
		else
		{
			values.put(locale, new ArrayList(fvalues));
		}
	}
}
