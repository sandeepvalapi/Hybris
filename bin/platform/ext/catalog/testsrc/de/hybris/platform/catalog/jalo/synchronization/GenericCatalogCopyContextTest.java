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
package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.SynchronizationPersistenceAdapter;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Level;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;


@UnitTest
public class GenericCatalogCopyContextTest
{
	private GenericCatalogCopyContext context;

	@Mock
	private SessionContext ctxMock;

	@Mock
	private CatalogVersionSyncJob syncJob;

	@Mock
	private CatalogVersionSyncCronJob syncCronJob;

	@Mock
	private FlexibleSearch flexibleSerachMock;

	@Mock
	private JaloSession jaloSessionMock;

	@Mock
	private Language langMock;

	@Mock
	private SearchResult searchResultMock;


	public class TestGenericCatalogCopyContext extends GenericCatalogCopyContext
	{
		public TestGenericCatalogCopyContext(final SessionContext ctx, final CatalogVersionSyncJob job,
				  final CatalogVersionSyncCronJob cronJob, final Level logLevel, final boolean forceUpdate)
		{
			super(ctx, job, cronJob, logLevel, forceUpdate);
		}

		@Override
		protected int getCurrentTurn()
		{
			return 0;
		}

		@Override
		protected JaloSession createSession()
		{
			return jaloSessionMock;
		}

		@Override
		protected Set<AttributeDescriptor> getCreationAttributes()
		{
			return Collections.EMPTY_SET;
		}

		@Override
		protected void setTargetLanguages(final java.util.Collection<Language> languages)
		{
			//noop
		}

		@Override
		protected String getRootCatalogType(final ComposedType composedType)
		{
			return "mou";
		}

		@Override
		protected SynchronizationPersistenceAdapter initPersistenceAdapter()
		{
			return null;
		}

		//
	}

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.when(syncJob.getSyncLanguages(ctxMock)).thenReturn(Collections.singleton(langMock));

		context = Mockito.spy(new TestGenericCatalogCopyContext(ctxMock, syncJob, syncCronJob, Level.OFF, true));
		Mockito.doReturn(flexibleSerachMock).when(context).getFlexibleSearch();

	}

	@Test
	public void testQueryCatalogItemCopy()
	{
		final Item itemMock = Mockito.mock(Item.class);

		Mockito.doReturn("ennie-minnie").when(context).getCatalogVersionAttribute(Mockito.any(ComposedType.class));
		//,mine,mou
		Mockito.doReturn(Collections.singleton("minnie")).when(context).getUniqueKeyAttributes(Mockito.any(ComposedType.class));

		Mockito.when(
				  flexibleSerachMock.search(Mockito.any(SessionContext.class) /* ctxMock */, Mockito.anyString(), Mockito.anyMap(),
							 Mockito.any(Class.class))).thenReturn(searchResultMock);

		context.queryCatalogItemCopy(itemMock);


		Mockito.verify(flexibleSerachMock, Mockito.times(1)).search(Mockito.any(SessionContext.class),
				  (Mockito.argThat(new ArgumentMatcher<String>()
				  {
					  @Override
					  public boolean matches(final Object argument)
					  {
						  Assert.assertTrue(argument instanceof String);
						  final String query = (String) argument;
						  Assert.assertEquals(
									 "SELECT x.PK FROM ({{ SELECT {pk} AS PK FROM {mou} WHERE {ennie-minnie}=?tgtVer  AND {minnie} IS " +
												"NULL }} UNION ALL {{ SELECT {pk} AS PK FROM {ItemSyncTimestamp*} WHERE " +
												"{sourceItem}=?srcItem AND {targetVersion}=?tgtVer AND {syncJob}=0  }} ) x",
									 query);
						  return true;
					  }
				  })), Mockito.anyMap(), Mockito.any(Class.class));
	}


	@Test
	public void testQueryNonCatalogItemCopy()
	{
		final Item itemMock = Mockito.mock(Item.class);

		Mockito.doReturn("ennie-minnie").when(context).getCatalogVersionAttribute(Mockito.any(ComposedType.class));
		//,mine,mou
		Mockito.doReturn(Collections.singleton("minnie")).when(context)
				  .getNonCatalogItemUniqueAttributes(Mockito.any(ComposedType.class));

		Mockito.doReturn("whou").when(context).getRootUniqueItemType(Mockito.any(ComposedType.class), Mockito.anySet());


		Mockito.when(
				  flexibleSerachMock.search(Mockito.any(SessionContext.class) /* ctxMock */, Mockito.anyString(), Mockito.anyMap(),
							 Mockito.any(Class.class))).thenReturn(searchResultMock);

		context.queryNonCatalogItemCopy(itemMock);

		Mockito.verify(flexibleSerachMock).search(Mockito.any(SessionContext.class),
				Mockito
						.eq("SELECT {pk} FROM {ItemSyncTimestamp*}WHERE {syncJob}=0 AND {targetVersion}=?tgtVer AND {sourceItem}=?srcItem"),
				Mockito.anyMap(), Mockito.any(Class.class));
		Mockito.verify(flexibleSerachMock).search(Mockito.any(SessionContext.class),
				Mockito.eq("SELECT {pk} AS PK FROM {whou} WHERE 1=1 AND  {minnie} IS NULL "), Mockito.anyMap(),
				Mockito.any(Class.class));
	}
}
