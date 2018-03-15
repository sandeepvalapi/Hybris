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
package com.hybris.training.cockpits.cmscockpit.services.impl;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.cmscockpit.services.impl.CmsSearchProvider;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import com.hybris.training.cockpits.cmscockpit.browser.filters.AbstractUiExperienceFilter;

import java.util.ArrayList;
import java.util.List;


public class UiExperienceCmsSearchProvider extends CmsSearchProvider
{


	private static final String UI_EXPERIENCE_LEVEL = "UiExperienceLevel ";
	private static final String CMS_UI_EXPERIENCE_RESTRICTION = "CMSUiExperienceRestriction";
	private static final String RESTRICTIONS_FOR_PAGES = "RestrictionsForPages";
	private static final String UI_EXPERIENCE = "uiExperience";
	private static final String NAME = "name";
	private static final String ITEM = "item";
	private static final String PK = "pk";
	private static final String REST = "rest";
	private static final String TARGET = "target";
	private static final String SOURCE = "source";
	private static final String LEVEL = "level";
	private static final String REST2PAGE = "rest2page";

	@Override
	public List<GenericCondition> createConditions(final Query query, final GenericQuery genQuery)
	{

		final List<GenericCondition> conditions = new ArrayList<GenericCondition>();
		conditions.addAll(super.createConditions(query, genQuery));
		conditions.addAll(createUiExperienceCondition(query, genQuery));
		return conditions;
	}

	protected List<GenericCondition> createUiExperienceCondition(final Query query, final GenericQuery genQuery)
	{

		final List<GenericCondition> list = new ArrayList<GenericCondition>();
		final UiExperienceLevel ret = (UiExperienceLevel) query.getContextParameter(AbstractUiExperienceFilter.UI_EXPERIENCE_PARAM);
		if (ret != null)
		{
			final GenericCondition itemJoinCondition = GenericCondition.createJoinCondition(new GenericSearchField(ITEM, PK),
					new GenericSearchField(REST2PAGE, SOURCE));
			final GenericCondition restJoinCondition = GenericCondition.createJoinCondition(new GenericSearchField(REST, PK),
					new GenericSearchField(REST2PAGE, TARGET));
			final GenericCondition levelJoinCondition = GenericCondition.createJoinCondition(new GenericSearchField(LEVEL, PK),
					new GenericSearchField(REST, UI_EXPERIENCE));
			final GenericCondition uiExpirenceCondition = GenericCondition.createConditionForValueComparison(new GenericSearchField(
					LEVEL, NAME), de.hybris.platform.core.Operator.EQUAL, ret.getCode());
			genQuery.addInnerJoin(RESTRICTIONS_FOR_PAGES, REST2PAGE, itemJoinCondition);
			genQuery.addInnerJoin(CMS_UI_EXPERIENCE_RESTRICTION, REST, restJoinCondition);
			genQuery.addInnerJoin(UI_EXPERIENCE_LEVEL, LEVEL, levelJoinCondition);
			list.add(uiExpirenceCondition);
		}
		return list;
	}
}
