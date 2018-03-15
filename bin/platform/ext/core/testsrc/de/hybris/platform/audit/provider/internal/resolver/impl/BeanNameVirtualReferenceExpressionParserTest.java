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
package de.hybris.platform.audit.provider.internal.resolver.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceExpressionParser;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BeanNameVirtualReferenceExpressionParserTest
{
	private static final String BEAN_NAME_EXPRESSION = "value=beanName(theBean)";
	private static final String VALUE_NOT_RELATED_TO_ANY_BEAN = "valueNotRelatedToAnyBean";

	private final VirtualReferenceExpressionParser nextParser = mock(VirtualReferenceExpressionParser.class);
	private final VirtualReferenceValuesExtractor extractor = mock(VirtualReferenceValuesExtractor.class);

	@Spy
	private final BeanNameVirtualReferenceExpressionParser parser = new BeanNameVirtualReferenceExpressionParser(nextParser);

	@Before
	public void setUp()
	{
		doReturn(extractor).when(parser).getExtractorBean(any());
	}

	@Test
	public void parserShouldReturnProperBean()
	{
		//when
		final VirtualReferenceValuesExtractor result = parser.getResolver(BEAN_NAME_EXPRESSION);

		//then
		verifyZeroInteractions(nextParser);
		assertThat(result).isEqualTo(extractor);
		verify(parser).getExtractorBean("theBean");
	}

	@Test
	public void parserShouldCallGetResolverFromNextParser()
	{
		//when
		parser.getResolver(VALUE_NOT_RELATED_TO_ANY_BEAN);

		//then
		verify(nextParser).getResolver(VALUE_NOT_RELATED_TO_ANY_BEAN);
	}

	@Test
	public void parserShouldExtractQualifier()
	{
		//when
		final String result = parser.getQualifier(BEAN_NAME_EXPRESSION);

		//then
		assertThat(result).isEqualTo("value");
	}

	@Test
	public void parserShouldCallGetQualifierFromNextParser()
	{
		//when
		parser.getQualifier(VALUE_NOT_RELATED_TO_ANY_BEAN);

		//then
		verify(nextParser).getQualifier(VALUE_NOT_RELATED_TO_ANY_BEAN);
	}

}