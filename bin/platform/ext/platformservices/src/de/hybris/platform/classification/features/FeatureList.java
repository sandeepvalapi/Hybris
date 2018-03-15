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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;


/**
 * A list of features for a product. This class is the service representation of
 * {@link de.hybris.platform.catalog.jalo.classification.util.FeatureContainer}
 */
public class FeatureList implements Iterable<Feature>
{

	private final List<Feature> features;

	/**
	 * Initializes this object with a list of features.
	 * 
	 * @param features
	 *           the list of features
	 * @throws IllegalArgumentException
	 *            if the list is null
	 */
	public FeatureList(final List<Feature> features)
	{
		validateParameterNotNull(features, "features list must not be null!");
		this.features = Collections.unmodifiableList(features);
	}

	/**
	 * Initializes this object with variable array of features.
	 * 
	 * @param features
	 *           the list of features
	 * @throws IllegalArgumentException
	 *            if the list is null
	 */
	public FeatureList(final Feature... features)
	{
		this(Lists.newArrayList(features));
	}

	/**
	 * Returns the list of features. The list is unmodifiable.
	 * 
	 * @return the list of features
	 */
	public List<Feature> getFeatures()
	{
		return features;
	}

	/**
	 * 
	 * @return <code>true</code> if this feature list contains no features at all.
	 */
	public boolean isEmpty()
	{
		return this.features.isEmpty();
	}

	/**
	 * Returns the feature with the given name of null if there's no such feature.
	 * 
	 * @param name
	 *           the of the feature to look up
	 * @return the feature with the given name of null if there's no such feature
	 * @throws IllegalArgumentException
	 *            if the name is null
	 */
	public Feature getFeatureByName(final String name)
	{
		validateParameterNotNull(name, "name must not be null!");
		for (final Feature feature : features)
		{
			if (name.equals(feature.getName()))
			{
				return feature;
			}
		}
		return null;
	}

	/**
	 * Gets the feature by assignment.
	 * 
	 * @param assignment
	 *           the assignment
	 * @return the feature by assignment
	 */
	public Feature getFeatureByAssignment(final ClassAttributeAssignmentModel assignment)
	{
		validateParameterNotNull(assignment, "assignment must not be null!");
		for (final Feature feature : features)
		{
			if (assignment.equals(feature.getClassAttributeAssignment()))
			{
				return feature;
			}
		}
		return null;
	}

	/**
	 * Returns the (typed) feature assigned to the attribute with the given code, or null if there's no such feature.
	 * 
	 * @param code
	 *           the code of the attribute the feature is assigned to
	 * @return the (typed) feature assigned to the attribute with the given code, or null if there's no such feature
	 * @throws IllegalArgumentException
	 *            if the code is null
	 */

	public Feature getFeatureByCode(final String code)
	{
		validateParameterNotNull(code, "code must not be null!");
		for (final Feature feature : features)
		{
			if (code.equals(feature.getCode()))
			{
				return feature;
			}
		}
		return null;
	}

	@Override
	public Iterator<Feature> iterator()
	{
		return features.iterator();
	}

	/**
	 * @return a {@link HashSet} with {@link ClassificationClassModel}s.
	 */
	public Set<ClassificationClassModel> getClassificationClasses()
	{
		if (features.isEmpty())
		{
			return Collections.EMPTY_SET;
		}
		else
		{
			final Set<ClassificationClassModel> returnset = new LinkedHashSet<ClassificationClassModel>();
			for (final Feature feature : features)
			{
				final ClassAttributeAssignmentModel caam = feature.getClassAttributeAssignment();
				if (caam != null)
				{
					returnset.add(caam.getClassificationClass());
				}
			}
			return returnset;
		}
	}

	/**
	 * @return a {@link HashSet} with {@link ClassAttributeAssignmentModel}s.
	 */
	public Set<ClassAttributeAssignmentModel> getClassAttributeAssignments()
	{
		if (features.isEmpty())
		{
			return Collections.EMPTY_SET;
		}
		else
		{
			final Set<ClassAttributeAssignmentModel> returnset = new LinkedHashSet<ClassAttributeAssignmentModel>();
			for (final Feature feature : features)
			{
				final ClassAttributeAssignmentModel caam = feature.getClassAttributeAssignment();
				if (caam != null)
				{
					returnset.add(caam);
				}
			}
			return returnset;
		}
	}

	/**
	 * @return <code>true</code> if this feature list contains any untyped feature.
	 */
	public boolean hasUntypedFeatures()
	{
		if (features.isEmpty())
		{
			return false;
		}
		else
		{
			for (final Feature feature : features)
			{
				if (feature.getClassAttributeAssignment() == null)
				{
					return true;
				}
			}
			return false;
		}
	}
}
