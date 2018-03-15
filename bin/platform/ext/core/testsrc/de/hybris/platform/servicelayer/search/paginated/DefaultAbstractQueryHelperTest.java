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
package de.hybris.platform.servicelayer.search.paginated;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.servicelayer.search.AbstractQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.impl.DefaultAbstractQueryHelper;

import java.util.ArrayList;
import java.util.Locale;

import org.junit.Test;


/**
 * Unit test for {@link DefaultAbstractQueryHelper}
 */
@UnitTest
public class DefaultAbstractQueryHelperTest
{
	private static final String FS_QUERY = "SELECT {p:pk} from {Product AS p}";
	private static final String FS_QUERY_UPDATED = "SELECT {p:pk} from {Product AS p} ORDER BY {p:code} asc";

	private final DefaultAbstractQueryHelper abstractQueryHelper = new DefaultAbstractQueryHelper();
	private final GenericQuery gQuery = new GenericQuery("Product");
	private final GenericQuery gQueryUpdated = new GenericQuery("VariantProduct");

	@Test
	public void shouldUpdateFlexibleSearchQuery()
	{
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(FS_QUERY);
		setAllAttributes(fsQuery);
		fsQuery.addQueryParameter("code", "productCode");

		final FlexibleSearchQuery updatedFsQuery = abstractQueryHelper.getUpdatedFlexibleSearchQuery(fsQuery, FS_QUERY_UPDATED);

		assertClonedResults(fsQuery, updatedFsQuery);
		assertThat(updatedFsQuery.getQueryParameters()).isEqualTo(fsQuery.getQueryParameters());
		assertThat(updatedFsQuery.getQuery()).isEqualTo(FS_QUERY_UPDATED);
	}

	@Test
	public void shouldUpdateGenericSearchQuery()
	{
		final GenericSearchQuery gsQuery = new GenericSearchQuery(gQuery);
		setAllAttributes(gsQuery);
		final GenericSearchQuery updatedGsQuery = abstractQueryHelper.getUpdatedGenericSearchQuery(gsQuery, gQueryUpdated);

		assertClonedResults(gsQuery, updatedGsQuery);
		assertThat(updatedGsQuery.getQuery()).isEqualTo(gQueryUpdated);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionNullStringQuery()
	{
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(FS_QUERY);
		abstractQueryHelper.getUpdatedFlexibleSearchQuery(fsQuery, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionNullFlexibleSearchQuery()
	{
		abstractQueryHelper.getUpdatedFlexibleSearchQuery(null, FS_QUERY_UPDATED);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionNullGenericQuery()
	{
		final GenericSearchQuery gsQuery = new GenericSearchQuery(gQuery);
		abstractQueryHelper.getUpdatedGenericSearchQuery(gsQuery, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionNullGenericSearchQuery()
	{
		abstractQueryHelper.getUpdatedGenericSearchQuery(null, gQueryUpdated);
	}

	/**
	 * Sets all attributes of given search query object, so that these values can be compared with the ones returned from
	 * abstractQueryHelper class
	 */
	protected <T extends AbstractQuery> void setAllAttributes(final T original)
	{
		original.setCount(10);
		original.setStart(10);
		original.setNeedTotal(true);
		original.setLocale(new Locale("en"));
		original.setSessionSearchRestrictions(new ArrayList());
		original.setCatalogVersions(new ArrayList());
		original.setResultClassList(new ArrayList());
		original.setFailOnUnknownFields(true);
		original.setUser(new UserModel());
		original.setDisableSearchRestrictions(true);
		original.setDisableSpecificDbLimitSupport(Boolean.TRUE);
		original.setDisableCaching(true);
	}

	/**
	 * Assert the given two AbstractQuery sub-class parameters have the same attribute values.
	 *
	 * @param original
	 *           the original
	 * @param updated
	 *           the updated
	 */
	protected <T extends AbstractQuery> void assertClonedResults(final T original, final T updated)
	{
		assertThat(updated.getCount()).isEqualTo(original.getCount());
		assertThat(updated.getStart()).isEqualTo(original.getStart());
		assertThat(updated.isNeedTotal()).isEqualTo(original.isNeedTotal());
		assertThat(updated.getCatalogVersions()).isEqualTo(original.getCatalogVersions());
		assertThat(updated.isDisableCaching()).isEqualTo(original.isDisableCaching());
		assertThat(updated.isDisableSearchRestrictions()).isEqualTo(original.isDisableSearchRestrictions());
		assertThat(updated.isDisableSpecificDbLimitSupport()).isEqualTo(original.isDisableSpecificDbLimitSupport());
		assertThat(updated.isFailOnUnknownFields()).isEqualTo(original.isFailOnUnknownFields());
		assertThat(updated.getLocale()).isEqualTo(original.getLocale());
		assertThat(updated.getResultClassList()).isEqualTo(original.getResultClassList());
		assertThat(updated.getSessionSearchRestrictions()).isEqualTo(original.getSessionSearchRestrictions());
		assertThat(updated.getUser()).isEqualTo(original.getUser());
	}
}
