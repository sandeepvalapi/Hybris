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
package de.hybris.platform.catalog.model;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Dynamic attribute bean for handling <code>ClassificationClassModel#getAllClassificationAttributeAssignments()</code>
 * call.
 */
public class AllClassificationAttributesAssignments implements
		DynamicAttributeHandler<List<ClassAttributeAssignmentModel>, ClassificationClassModel>
{

	/**
	 * Returns list of all <code>ClassAttributeAssignmentModel</code> objects for <code>ClassificationClassModel</code>
	 * recursively to the top.
	 * 
	 * @param model
	 *           the <code>ClassificationClassModel</code> object for which <code>ClassAttributeAssignmentModel</code>s
	 *           have to be obtained.
	 * @return the result list of <code>ClassAttributeAssignmentModel</code> objects.
	 */
	@Override
	public List<ClassAttributeAssignmentModel> get(final ClassificationClassModel model)
	{
		final Set<ClassificationAttributeModel> attributes = new HashSet<ClassificationAttributeModel>();
		// 1. put in our own assignments
		final List<ClassAttributeAssignmentModel> declaredAssignments = model.getDeclaredClassificationAttributeAssignments();

		final List<ClassAttributeAssignmentModel> assignments = declaredAssignments != null ? new ArrayList<ClassAttributeAssignmentModel>(
				declaredAssignments) : new ArrayList<ClassAttributeAssignmentModel>();

		for (final ClassAttributeAssignmentModel asgnmt : assignments)
		{
			attributes.add(asgnmt.getClassificationAttribute());
		}

		// 2. get assignments from super classes depth-first
		Set<ClassificationClassModel> superClasses = new LinkedHashSet<ClassificationClassModel>(getSuperClasses(model));

		while (superClasses != null && !superClasses.isEmpty())
		{
			Set<ClassificationClassModel> nextLevel = null; // next level of super classes
			Set<ClassAttributeAssignmentModel> toAdd = null; // assignments to add -> need to be filtered by attribute because of overriding
			for (final ClassificationClassModel superClass : superClasses)
			{
				final Collection<ClassAttributeAssignmentModel> clAsgnmt = superClass.getDeclaredClassificationAttributeAssignments();
				if (!clAsgnmt.isEmpty())
				{
					if (toAdd == null)
					{
						toAdd = new LinkedHashSet<ClassAttributeAssignmentModel>();
					}
					toAdd.addAll(clAsgnmt);
				}
				final Collection nextSuperClasses = getSuperClasses(superClass);
				if (!nextSuperClasses.isEmpty())
				{
					if (nextLevel == null)
					{
						nextLevel = new LinkedHashSet<ClassificationClassModel>();
					}
					nextLevel.addAll(nextSuperClasses);
				}
			}
			// add all assignments of the current level AFTER current assignments
			// -> this way the own attribute are on top, followed by the super class ones etc
			if (toAdd != null)
			{
				for (final ClassAttributeAssignmentModel asgnmt : toAdd)
				{
					// avoid adding assignments for attributes which are already within the result
					if (attributes.add(asgnmt.getClassificationAttribute()))
					{
						assignments.add(asgnmt);
					}
				}
			}
			// process next super class level
			superClasses = nextLevel;
		}

		return assignments;
	}

	@Override
	public void set(final ClassificationClassModel model, final List<ClassAttributeAssignmentModel> value)
	{
		throw new UnsupportedOperationException("set attribute is not supported");
	}

	private Set<ClassificationClassModel> getSuperClasses(final ClassificationClassModel model)
	{
		final Set<ClassificationClassModel> result = new LinkedHashSet<ClassificationClassModel>();
		final List<CategoryModel> superCategories = model.getSupercategories();
		if (superCategories != null)
		{
			for (final CategoryModel category : superCategories)
			{
				if (category instanceof ClassificationClassModel)
				{
					result.add((ClassificationClassModel) category);
				}
			}
		}
		return result;
	}

}
