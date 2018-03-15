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
package de.hybris.platform.classification.interceptors;

import de.hybris.platform.catalog.jalo.classification.AttributeValueAssignment;
import de.hybris.platform.catalog.model.classification.AttributeValueAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class ClassificationAttributeValueRemoveInterceptor implements RemoveInterceptor<ClassificationAttributeValueModel>
{
	private static final String CHECK_VALUE_IN_USE_QUERY = "select count({PK}) from {" + AttributeValueAssignmentModel._TYPECODE
			+ "} where {" + AttributeValueAssignment.VALUE + "} = ?" + AttributeValueAssignment.VALUE;

	private FlexibleSearchService flexibleSearchService;
	private L10NService l10nService;

	@Override
	public void onRemove(final ClassificationAttributeValueModel value, final InterceptorContext ctx) throws InterceptorException
	{
		if (isValueInUse(value))
		{
			throw new InterceptorException(l10nService.getLocalizedString("exception.classification.value_stil_in_use", new Object[]
			{ value.getCode() }));
		}
	}

	private boolean isValueInUse(final ClassificationAttributeValueModel value)
	{

		final Map<String, Object> params = ImmutableMap.<String, Object> of(AttributeValueAssignment.VALUE, value);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(CHECK_VALUE_IN_USE_QUERY, params);
		query.setResultClassList(ImmutableList.of(Long.class));

		final SearchResult<Long> searchResult = flexibleSearchService.<Long> search(query);

		return searchResult.getResult().get(0).longValue() > 0;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setL10nService(final L10NService l10nService)
	{
		this.l10nService = l10nService;
	}

}
