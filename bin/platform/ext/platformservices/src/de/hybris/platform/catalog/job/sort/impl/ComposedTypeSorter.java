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
package de.hybris.platform.catalog.job.sort.impl;

import de.hybris.bootstrap.util.RequirementHolder;
import de.hybris.bootstrap.util.RequirementSolver;
import de.hybris.bootstrap.util.RequirementSolverException;
import de.hybris.platform.catalog.job.sort.Sorter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of the sorter using {@link RequirementSolver#solve(Collection)} for determining order. TODO write unit
 * test
 */
public class ComposedTypeSorter implements Sorter<ComposedTypeModel>
{

	private TypeService typeService;

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @param originalComposedTypeSet
	 *           unsorted set of composed types from the catalog manager
	 * @return a list of the same composed types, an element of this list depends on another element of the same list
	 *         with a lower index (or not)
	 */
	@Override
	public List<ComposedTypeModel> sort(final Collection<ComposedTypeModel> originalComposedTypeSet)
	{
		final List<ComposedTypeModel> returnlist = new ArrayList<ComposedTypeModel>();
		final Set<ComposedTypeModel> origSet = new HashSet<ComposedTypeModel>(originalComposedTypeSet); //clone the set, we remove elements from it
		final Map<ComposedTypeModel, Integer> countMap = new HashMap<ComposedTypeModel, Integer>();
		final Map<ComposedTypeModel, ComposedTypeNode> ct_ctn_map = new HashMap<ComposedTypeModel, ComposedTypeNode>();

		while (true)
		{
			countMap.clear();
			ct_ctn_map.clear();
			for (final ComposedTypeModel ct : origSet)
			{
				ct_ctn_map.put(ct, new ComposedTypeNode(ct));
				countMap.put(ct, Integer.valueOf(0));
			}
			createNodes(origSet, ct_ctn_map, countMap);
			try
			{
				final List<ComposedTypeNode> sortedList = RequirementSolver.solve(ct_ctn_map.values());
				for (final ComposedTypeNode ctn : sortedList)
				{
					returnlist.add(ctn.getComposedType());
				}
				return returnlist;
			}
			catch (final RequirementSolverException e)
			{
				final ComposedTypeModel firstInList = getComposedTypewithHighestCount(countMap);
				returnlist.add(firstInList);
				origSet.remove(firstInList);
			}
		}
	}


	//--------------------------------- code for solving 	dependence ---------------------------------------------

	private class ComposedTypeNode implements RequirementHolder
	{
		private final ComposedTypeModel composedtype;
		private final Set<ComposedTypeNode> requirements;

		public ComposedTypeNode(final ComposedTypeModel comptype)
		{
			this.composedtype = comptype;
			requirements = new HashSet<ComposedTypeNode>();
		}

		public void addRequirement(final ComposedTypeNode ctn)
		{
			requirements.add(ctn);
		}

		@Override
		public Set<? extends RequirementHolder> getRequirements()
		{
			return requirements;
		}

		public ComposedTypeModel getComposedType()
		{
			return composedtype;
		}
	}

	private void createNodes(final Set<ComposedTypeModel> origSet, final Map<ComposedTypeModel, ComposedTypeNode> ct_ctn_map,
			final Map<ComposedTypeModel, Integer> countMap)
	{
		for (final ComposedTypeModel ct : origSet)
		{
			for (final AttributeDescriptorModel ad : typeService.getAttributeDescriptorsForType(ct))
			{
				if (BooleanUtils.isTrue(ad.getPartOf()))
				{
					final TypeModel attrType = ad.getAttributeType(); //ad.getRealAttributeType();
					final ComposedTypeModel nested;
					if (attrType instanceof ComposedTypeModel)
					{
						nested = (ComposedTypeModel) attrType;
					}
					else if (attrType instanceof CollectionTypeModel
							&& ((CollectionTypeModel) attrType).getElementType() instanceof ComposedTypeModel)
					{
						nested = (ComposedTypeModel) ((CollectionTypeModel) attrType).getElementType();
					}
					else if (attrType instanceof MapTypeModel
							&& ((MapTypeModel) attrType).getReturntype() instanceof ComposedTypeModel)
					{
						nested = (ComposedTypeModel) ((MapTypeModel) attrType).getReturntype();
					}
					else
					{
						nested = null;
					}

					if (ct_ctn_map.containsKey(nested))
					{
						final Set<ComposedTypeModel> sub = new HashSet<ComposedTypeModel>();
						sub.add(ct);
						sub.addAll(ct.getAllSubTypes());
						for (final ComposedTypeModel t : sub)
						{
							if (!t.equals(nested) && ct_ctn_map.containsKey(t))
							{
								ct_ctn_map.get(t).addRequirement(ct_ctn_map.get(nested));
								final int count = countMap.get(nested).intValue();
								countMap.put(nested, Integer.valueOf(count + 1));
							}
						}
					}
				}
			}
		}
	}


	/**
	 * @return the composedType from the given map which has the highest Integer value
	 */
	private ComposedTypeModel getComposedTypewithHighestCount(final Map<ComposedTypeModel, Integer> countMap)
	{
		int counter = 0;
		ComposedTypeModel retct = null;
		for (final Map.Entry<ComposedTypeModel, Integer> me : countMap.entrySet())
		{
			if (retct == null)
			{
				retct = me.getKey();
			}
			if (me.getValue().intValue() > counter)
			{
				counter = me.getValue().intValue();
				retct = me.getKey();
			}
		}
		return retct;
	}

}
