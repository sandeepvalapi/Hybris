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
package de.hybris.platform.tx;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TransactionalCacheIntegrationTest extends ServicelayerBaseTest
{
	private static final Object TOPIC_KEY = "TEST_" + UUID.randomUUID();

	private static final InvalidationListener INVALIDATION_LISTENER = new InvalidationListener()
	{
		@Override
		public void keyInvalidated(final Object[] key, final int invalidationType, final InvalidationTarget target,
				final RemoteInvalidationSource remoteSrc)
		{
			target.invalidate(key, invalidationType);
		}
	};

	private Cache cache;

	private final PropertyConfigSwitcher enableTxCacheProperty = new PropertyConfigSwitcher(Transaction.CFG_ENABLE_TX_CACHE);

	@Before
	public void setUp()
	{
		cache = Registry.getCurrentTenant().getCache();
		final InvalidationTopic invalidationTopic = InvalidationManager.getInstance().getOrCreateInvalidationTopic(new Object[]
		{ TOPIC_KEY });
		invalidationTopic.addInvalidationListener(INVALIDATION_LISTENER);
		cache.clear();
	}

	@After
	public void tearDown()
	{
		enableTxCacheProperty.switchBackToDefault();
		final InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic(new Object[]
		{ TOPIC_KEY });
		topic.removeInvalidationListener(INVALIDATION_LISTENER);
		//there is no way to remove topic :-(
	}

	@Test
	public void testBehaviorWithoutTransaction() throws Exception
	{
		final MyCacheUnit firstCacheUnit = new MyCacheUnit(cache, "FIRST", TOPIC_KEY, "UNIT");
		final MyCacheUnit secondCacheUnit = new MyCacheUnit(cache, "SECOND", TOPIC_KEY, "UNIT");
		final MyCacheUnit thirdCacheUnit = new MyCacheUnit(cache, "THIRD", TOPIC_KEY, "UNIT");

		assertThat(cache.getUnit(firstCacheUnit)).isNull();
		assertThat(firstCacheUnit.get()).isEqualTo("FIRST1");
		assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("FIRST1");
		assertThat(cache.getOrAddUnit(thirdCacheUnit)).isSameAs(firstCacheUnit);
		assertThat(secondCacheUnit.get()).isEqualTo("FIRST1");

		cache.invalidate(firstCacheUnit.getKeyAsArray(), AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

		assertThat(cache.getUnit(secondCacheUnit)).isNull();
		assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("SECOND1");
		assertThat(cache.getOrAddUnit(firstCacheUnit)).isSameAs(secondCacheUnit);
		assertThat(firstCacheUnit.get()).isEqualTo("SECOND1");
		assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("SECOND1");

		cache.invalidate(firstCacheUnit.getKeyAsArray(), AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

		assertThat(cache.getUnit(thirdCacheUnit)).isNull();
		assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("THIRD1");
		assertThat(cache.getOrAddUnit(secondCacheUnit)).isSameAs(thirdCacheUnit);
		assertThat(secondCacheUnit.get()).isEqualTo("THIRD1");
		assertThat(cache.getOrAddUnit(firstCacheUnit).get()).isEqualTo("THIRD1");
	}

	@Test
	public void testBehaviorWithTransactionButWithoutTransactionalCache() throws Exception
	{
		testBehaviorWithTransactionButWithoutTransactionalCache(true);
	}

	@Test
	public void testBehaviorWithTransactionButTransactionalCacheDisabledInTx() throws Exception
	{
		testBehaviorWithTransactionButWithoutTransactionalCache(false);
	}

	
	void testBehaviorWithTransactionButWithoutTransactionalCache( boolean disableViaConfig ) throws Exception
	{
		final MyCacheUnit firstCacheUnit = new MyCacheUnit(cache, "FIRST", TOPIC_KEY, "UNIT");
		final MyCacheUnit secondCacheUnit = new MyCacheUnit(cache, "SECOND", TOPIC_KEY, "UNIT");
		final MyCacheUnit thirdCacheUnit = new MyCacheUnit(cache, "THIRD", TOPIC_KEY, "UNIT");

		if( disableViaConfig )
		{
			enableTxCacheProperty.switchToValue(Boolean.FALSE.toString());
		}
		else
		{
			// prepare for inner-tx logic
			enableTxCacheProperty.switchToValue(Boolean.TRUE.toString());
		}
		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				if( !disableViaConfig)
				{
					tx.enableTxCache(false);
				}
				
				assertThat(cache.getUnit(firstCacheUnit)).isNull();
				assertThat(firstCacheUnit.get()).isEqualTo("FIRST1");
				assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("FIRST1");
				assertThat(cache.getOrAddUnit(thirdCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("FIRST1");

				Transaction.current().invalidate(firstCacheUnit, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

				assertThat(cache.getUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("FIRST2");
				assertThat(cache.getOrAddUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("SECOND1");
				assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("FIRST3");

				Transaction.current().invalidate(firstCacheUnit, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

				assertThat(cache.getUnit(thirdCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("FIRST4");
				assertThat(cache.getOrAddUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("SECOND2");
				assertThat(cache.getOrAddUnit(firstCacheUnit).get()).isEqualTo("FIRST5");

				return null;
			}
		});
	}

	@Test
	public void testBehaviorWithTransactionAndWithTransactionalCache() throws Exception
	{
		testBehaviorWithTransactionAndWithTransactionalCache(true);
	}

	@Test
	public void testBehaviorWithTransactionAndWithTransactionalCacheEnabledInTx() throws Exception
	{
		testBehaviorWithTransactionAndWithTransactionalCache(false);
	}

	void testBehaviorWithTransactionAndWithTransactionalCache( boolean enableViaConfig ) throws Exception
	{
		final MyCacheUnit firstCacheUnit = new MyCacheUnit(cache, "FIRST", TOPIC_KEY, "UNIT");
		final MyCacheUnit secondCacheUnit = new MyCacheUnit(cache, "SECOND", TOPIC_KEY, "UNIT");
		final MyCacheUnit thirdCacheUnit = new MyCacheUnit(cache, "THIRD", TOPIC_KEY, "UNIT");

		if( enableViaConfig )
		{
			enableTxCacheProperty.switchToValue(Boolean.TRUE.toString());
		}
		else
		{
			// prepare for inner-tx logic
			enableTxCacheProperty.switchToValue(Boolean.FALSE.toString());
		}
		Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public Object execute() throws Exception
			{
				if( !enableViaConfig)
				{
					tx.enableTxCache(true);
				}

				assertThat(cache.getUnit(firstCacheUnit)).isNull();
				assertThat(firstCacheUnit.get()).isEqualTo("FIRST1");
				assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("FIRST1");
				assertThat(cache.getOrAddUnit(thirdCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("FIRST1");

				Transaction.current().invalidate(firstCacheUnit, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

				assertThat(cache.getUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(cache.getOrAddUnit(secondCacheUnit).get()).isEqualTo("FIRST2");
				assertThat(cache.getOrAddUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("FIRST2");
				assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("FIRST2");

				Transaction.current().invalidate(firstCacheUnit, AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);

				assertThat(cache.getUnit(thirdCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(cache.getOrAddUnit(thirdCacheUnit).get()).isEqualTo("FIRST3");
				assertThat(cache.getOrAddUnit(secondCacheUnit)).isSameAs(firstCacheUnit);
				assertThat(secondCacheUnit.get()).isEqualTo("FIRST3");
				assertThat(cache.getOrAddUnit(firstCacheUnit).get()).isEqualTo("FIRST3");

				return null;
			}
		});
	}

}

class MyCacheUnit extends AbstractCacheUnit
{

	final Object[] key;
	private final String initialValue;
	private int computeCount = 0;

	public MyCacheUnit(final Cache cache, final String initialValue, final Object... key)
	{
		super(cache);
		this.initialValue = initialValue;
		this.key = key;
	}

	@Override
	public int getInvalidationTopicDepth()
	{
		return key.length - 1;
	}

	@Override
	public Object[] createKey()
	{
		return key;
	}

	@Override
	public String compute() throws Exception
	{
		return initialValue + ++computeCount;
	}

}
