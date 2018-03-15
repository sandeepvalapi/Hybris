/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.retention.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;


@UnitTest
public class FlexibleSearchRetentionRuleTest
{

	@Test
	public void shoulReturnCalculatedParam()
	{
		//given
		final FlexibleSearchRetentionRuleModel ruleModel = new FlexibleSearchRetentionRuleModel();
		ruleModel.setRetentionTimeSeconds(Long.valueOf(600));
		final FlexibleSearchRetentionItemsProvider retentionProvider = new FlexibleSearchRetentionItemsProvider(ruleModel);

		//when
		final Map<String, Object> finalValueMap = retentionProvider.getFinalQueryParams();

		//then
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_CALC_RETIREMENT_DATE)).isInstanceOf(Date.class);
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_JAVA_CURRENT_TIME)).isInstanceOf(Date.class);
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_RETENTION_TIME_SECONDS)).isInstanceOf(Long.class);
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_RETENTION_TIME_SECONDS))
				.isEqualTo(Long.valueOf(600));
	}

	@Test
	public void shoulReturnCalculatedParamAndAdditionalParam()
	{
		//given
		final FlexibleSearchRetentionRuleModel ruleModel = new FlexibleSearchRetentionRuleModel();
		final String ADDITIONAL_PARAM = "ADDITIONAL_PARAM";
		final Map<String, String> params = new HashedMap<>();
		params.put(ADDITIONAL_PARAM, "VALUE");
		ruleModel.setRetentionTimeSeconds(Long.valueOf(6000));
		ruleModel.setQueryParameters(params);
		final FlexibleSearchRetentionItemsProvider retentionProvider = new FlexibleSearchRetentionItemsProvider(ruleModel);

		//when
		final Map<String, Object> finalValueMap = retentionProvider.getFinalQueryParams();

		//then
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_CALC_RETIREMENT_DATE)).isInstanceOf(Date.class);
		assertThat(finalValueMap.get(ADDITIONAL_PARAM)).isInstanceOf(String.class);
		assertThat(finalValueMap.get(ADDITIONAL_PARAM)).isEqualTo("VALUE");

	}

	@Test
	public void shouldOverrideCaluclatedParam()
	{
		//given
		final FlexibleSearchRetentionRuleModel ruleModel = new FlexibleSearchRetentionRuleModel();
		final Map<String, String> params = new HashedMap<>();
		params.put(FlexibleSearchRetentionItemsProvider.PARAM_CALC_RETIREMENT_DATE, "OVERRIDE");
		ruleModel.setQueryParameters(params);
		final FlexibleSearchRetentionItemsProvider retentionProvider = new FlexibleSearchRetentionItemsProvider(ruleModel);

		//when
		final Map<String, Object> finalValueMap = retentionProvider.getFinalQueryParams();

		//then
		assertThat(finalValueMap.get(FlexibleSearchRetentionItemsProvider.PARAM_CALC_RETIREMENT_DATE)).isEqualTo("OVERRIDE");
	}



}
