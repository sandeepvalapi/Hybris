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
package de.hybris.platform.servicelayer.interceptor.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.dynamiccontent.DynamicContentChecksumCalculator;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import org.fest.assertions.Condition;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class AbstractDynamicContentInterceptorTest extends ServicelayerBaseTest
{
	private static final String TEST_ITEM_TYPE = "Script";
	private static final String TEST_CODE = "tEsTcOdE";
	private static final String TEST_INTITIAL_CONTENT = "#@! INITIAL CONTENT !@#";
	private static final String TEST_UPDATED_CONTENT_1 = "&^% UPDATED CONTENT ONE %^&";
	private static final String TEST_UPDATED_CONTENT_2 = "&^% UPDATED CONTENT TWO %^&";

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private DynamicContentChecksumCalculator dynamicContentChecksumCalculator;

	@Test
	public void shouldCreateActiveContent()
	{
		final AbstractDynamicContentModel newContent = givenNewContent();

		modelService.save(newContent);

		assertThat(newContent.getActive()).isTrue();
	}

	@Test
	public void shouldCalculateChecksumForNewContent()
	{
		final AbstractDynamicContentModel newContent = givenNewContent();

		modelService.save(newContent);

		assertThat(newContent.getChecksum()).isNotNull().isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_INTITIAL_CONTENT));
	}

	@Test
	public void shouldRecalculateChecksumForNewContent()
	{
		final AbstractDynamicContentModel newContent = givenNewContent();

		newContent.setChecksum("invalid checksum");
		modelService.save(newContent);

		assertThat(newContent.getChecksum()).isNotNull().isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_INTITIAL_CONTENT));
	}

	@Test
	public void shouldNotCreateHistoricalVersionForNewContent()
	{
		final AbstractDynamicContentModel newContent = givenNewContent();

		modelService.save(newContent);

		assertThat(newContent.getVersion()).isNotNull().isEqualTo(0L);
	}

	@Test
	public void shouldNotChangeActiveFlagOfExistingActiveContent()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_1);
		modelService.save(existingContent);

		assertThat(existingContent.getActive()).isTrue();
	}

	@Test
	public void shouldModifyExistingContent()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_1);
		modelService.save(existingContent);

		assertThat(existingContent.getContent()).isEqualTo(TEST_UPDATED_CONTENT_1);
	}

	@Test
	public void shouldRecalculateChecksumForExistingContent()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_1);
		modelService.save(existingContent);

		assertThat(existingContent.getChecksum()).isNotNull().isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_UPDATED_CONTENT_1));
	}

	@Test
	public void shouldOverrideChecksumForExistingContent()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_1);
		existingContent.setChecksum("invalid checksum");
		modelService.save(existingContent);

		assertThat(existingContent.getChecksum()).isNotNull().isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_UPDATED_CONTENT_1));
	}

	@Test
	public void shouldCreateHistoricalVersionWhenExistingContentHasChanged()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_1);
		modelService.save(existingContent);

		final AbstractDynamicContentModel predecessor = findPredecessorFor(existingContent);
		assertThat(predecessor).isNotNull();
		assertThat(predecessor.getActive()).isFalse();
		assertThat(predecessor.getVersion()).isNotNull().isEqualTo(0L);
		assertThat(predecessor.getCode()).isEqualTo(existingContent.getCode());
		assertThat(predecessor.getContent()).isEqualTo(TEST_INTITIAL_CONTENT);
		assertThat(predecessor.getChecksum())
				.isEqualTo(dynamicContentChecksumCalculator.calculateChecksumOf(TEST_INTITIAL_CONTENT));
	}

	@Test
	public void shouldNotCreateHistoricalVersionWhenContentWithHistoricalVersionHasNotChanged()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContentWithSingleHistoricalContent();

		modelService.save(existingContent);

		assertThat(existingContent.getActive()).isTrue();
		final AbstractDynamicContentModel predecessor = findPredecessorFor(existingContent);
		assertThat(predecessor).isNotNull();
		assertThat(predecessor.getActive()).isFalse();
		assertThat(predecessor.getVersion()).isNotNull().isEqualTo(0L);
		assertThat(predecessor.getCode()).isEqualTo(existingContent.getCode());
		assertThat(predecessor.getContent()).isEqualTo(TEST_INTITIAL_CONTENT);
		assertThat(predecessor.getChecksum())
				.isEqualTo(dynamicContentChecksumCalculator.calculateChecksumOf(TEST_INTITIAL_CONTENT));
	}

	@Test
	public void shouldCreateHistoricalVersionWhenContentWithHistoricalVersionHasChanged()
	{
		final AbstractDynamicContentModel existingContent = givenExistingContentWithSingleHistoricalContent();

		existingContent.setContent(TEST_UPDATED_CONTENT_2);
		modelService.save(existingContent);

		assertThat(existingContent.getActive()).isTrue();
		assertThat(existingContent.getContent()).isEqualTo(TEST_UPDATED_CONTENT_2);
		assertThat(existingContent.getChecksum()).isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_UPDATED_CONTENT_2));
		assertThat(existingContent.getVersion()).isNotNull().isEqualTo(2);

		final AbstractDynamicContentModel predecessor = findPredecessorFor(existingContent);
		assertThat(predecessor).isNotNull();
		assertThat(predecessor.getActive()).isFalse();
		assertThat(predecessor.getVersion()).isNotNull().isEqualTo(1L);
		assertThat(predecessor.getCode()).isEqualTo(existingContent.getCode());
		assertThat(predecessor.getContent()).isEqualTo(TEST_UPDATED_CONTENT_1);
		assertThat(predecessor.getChecksum()).isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_UPDATED_CONTENT_1));

		final AbstractDynamicContentModel firstVersion = findPredecessorFor(predecessor);
		assertThat(firstVersion).isNotNull();
		assertThat(firstVersion.getActive()).isFalse();
		assertThat(firstVersion.getVersion()).isNotNull().isEqualTo(0L);
		assertThat(firstVersion.getCode()).isEqualTo(existingContent.getCode());
		assertThat(firstVersion.getContent()).isEqualTo(TEST_INTITIAL_CONTENT);
		assertThat(firstVersion.getChecksum()).isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(TEST_INTITIAL_CONTENT));
	}

	@Test
	public void shouldNotAllowToModifyHistoricalVersion()
	{
		final AbstractDynamicContentModel activeContent = givenExistingContentWithSingleHistoricalContent();
		final AbstractDynamicContentModel historicalVersion = findPredecessorFor(activeContent);

		historicalVersion.setContent("not allowed operation");

		try
		{
			modelService.save(historicalVersion);
		}
		catch (final ModelSavingException e)
		{
			assertThat(e.getCause()).isNotNull().isInstanceOf(InterceptorException.class).satisfies(new Condition<Throwable>()
			{
				@Override
				public boolean matches(final Throwable expected)
				{
					final String message = expected.getMessage();
					return message != null && message.contains(AbstractDynamicContentValidateInterceptor.class.getName());
				}
			});
			return;
		}
		org.junit.Assert.fail("Exception was expected");
	}

	@Test
	public void shouldFailWhenCreatingNewContentWithDuplicatedCode()
	{
		givenExistingContent();
		final AbstractDynamicContentModel newContent = givenNewContent();
		try
		{
			modelService.save(newContent);
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(ModelSavingException.class);
			assertThat(ex.getCause()).isNotNull().isInstanceOf(InterceptorException.class).satisfies(new Condition<Throwable>()
			{
				@Override
				public boolean matches(final Throwable expected)
				{
					final String message = expected.getMessage();
					return message != null && message.contains(AbstractDynamicContentValidateInterceptor.class.getName());
				}
			});
			return;
		}
		org.junit.Assert.fail("Exception was expected");
	}

	@Test
	public void smokeTest()
	{
		final AbstractDynamicContentModel activeContent = givenNewContent();
		final int count = 100;

		for (int i = 0; i < count; i++)
		{
			activeContent.setContent(Integer.toString(i));
			modelService.save(activeContent);
			assertThat(activeContent.getActive()).isTrue();
			assertThat(activeContent.getCode()).isEqualTo(TEST_CODE);
		}

		assertThat(activeContent.getVersion()).isNotNull().isEqualTo(count - 1);
		assertThat(activeContent.getContent()).isEqualTo(Integer.toString(count - 1));
		assertThat(activeContent.getChecksum()).isEqualTo(
				dynamicContentChecksumCalculator.calculateChecksumOf(Integer.toString(count - 1)));

		AbstractDynamicContentModel historicalContentToCheck = findPredecessorFor(activeContent);
		for (int i = count - 2; i >= 0; i--)
		{
			assertThat(historicalContentToCheck).isNotNull();
			assertThat(historicalContentToCheck.getActive()).isFalse();
			assertThat(historicalContentToCheck.getCode()).isEqualTo(TEST_CODE);
			assertThat(historicalContentToCheck.getContent()).isEqualTo(Integer.toString(i));
			assertThat(historicalContentToCheck.getChecksum()).isEqualTo(
					dynamicContentChecksumCalculator.calculateChecksumOf(Integer.toString(i)));
			assertThat(historicalContentToCheck.getVersion()).isNotNull().isEqualTo(i);

			historicalContentToCheck = findPredecessorFor(historicalContentToCheck);
		}


		final List<AbstractDynamicContentModel> allContentToVerify = flexibleSearchService.<AbstractDynamicContentModel> search(
				"select {PK} from {AbstractDynamicContent} where {code} = ?code", ImmutableMap.of("code", TEST_CODE)).getResult();
		assertThat(allContentToVerify).hasSize(count);
	}

	private AbstractDynamicContentModel findPredecessorFor(final AbstractDynamicContentModel content)
	{
		final SearchResult<AbstractDynamicContentModel> queryResult = flexibleSearchService.<AbstractDynamicContentModel> search(
				"select {PK} from {" + TEST_ITEM_TYPE + "} where {code}=?code and {version}<?version order by {version} desc",
				ImmutableMap.<String, Object> of("code", content.getCode(), "version", content.getVersion()));

		final List<AbstractDynamicContentModel> result = queryResult.getResult();

		return result.isEmpty() ? null : result.get(0);
	}

	private AbstractDynamicContentModel givenExistingContentWithSingleHistoricalContent()
	{
		final AbstractDynamicContentModel content = givenExistingContent();
		content.setContent(TEST_UPDATED_CONTENT_1);
		modelService.save(content);
		return content;
	}

	private AbstractDynamicContentModel givenExistingContent()
	{
		final AbstractDynamicContentModel content = givenNewContent();
		modelService.save(content);
		return content;
	}

	private AbstractDynamicContentModel givenNewContent()
	{
		final AbstractDynamicContentModel result = modelService.<AbstractDynamicContentModel> create(TEST_ITEM_TYPE);
		result.setCode(TEST_CODE);
		result.setContent(TEST_INTITIAL_CONTENT);
		return result;
	}
}
