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
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;

import java.util.Date;


@IntegrationTest
public class NewTransactionContextIntegrationTest extends AbstractRelationModifictionContextIntegrationTest
{
	@Override
	protected RelationModifictionContext instantiateContext(final Date now)
	{
		return new NewTransactionContext(RELATION_CODE, writePersistenceGateway, true, now);
	}
}
