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
package de.hybris.platform.persistence.links.jdbc.dml.context;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;
import de.hybris.platform.tx.Transaction;

import java.util.Date;

import org.junit.After;
import org.junit.Before;


@IntegrationTest
public class RunningTransactionContextIntegrationTest extends AbstractRelationModifictionContextIntegrationTest
{
	@Override
	@Before
	public void setUp() throws ConsistencyCheckException
	{
		Transaction.current().begin();
		super.setUp();
	}

	@After
	public void tearDown()
	{
		Transaction.current().rollback();
	}

	@Override
	protected RelationModifictionContext instantiateContext(final Date now)
	{
		return new RunningTransactionContext(RELATION_CODE, writePersistenceGateway, true, now);
	}
}
