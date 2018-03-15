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

import de.hybris.platform.util.Config;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import junit.framework.Assert;

/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
public class TransactionConfigTest
{
    private static final Logger LOG = Logger.getLogger(TransactionConfigTest.class);

    @Test
    	public void testLogTransactionBeginTrails()
    	{
    		final boolean flagBefore = Config.getBoolean(Transaction.LOG_BEGIN_CONFIG_KEY, false);

    		try
    		{
    			Config.setParameter(Transaction.LOG_BEGIN_CONFIG_KEY, "true");

    			final Transaction current = Transaction.current();
                current.clearAddTxStackTrace();


    			Assert.assertEquals("initial stack trail size should be 0", 0, current.getBeginTransactionStack().size());

    			current.begin();
    			Assert.assertEquals("stack trail size after begin should be 1", 1, current.getBeginTransactionStack().size());
    			current.rollback();
    			Assert.assertEquals("stack trail size after rollback should be 0", 0, current.getBeginTransactionStack().size());

    			current.begin();
    			Assert.assertEquals("stack trail size after begin should be 1", 1, current.getBeginTransactionStack().size());
    			current.commit();
    			Assert.assertEquals("stack trail size after commit should be 0", 0, current.getBeginTransactionStack().size());


    			current.begin();
    			Assert.assertEquals("stack trail size after begin should be 1", 1, current.getBeginTransactionStack().size());
    			current.begin();
    			Assert.assertEquals("stack trail size after nested begin should be 2", 2, current.getBeginTransactionStack().size());
    			current.commit();
    			Assert.assertEquals("stack trail size after nested commit should be 1", 1, current.getBeginTransactionStack().size());
    			current.begin();
    			Assert.assertEquals("stack trail size after nested begin should be 2", 2, current.getBeginTransactionStack().size());
    			current.rollback();
    			Assert.assertEquals("stack trail size after nested rollback should be 1", 1, current.getBeginTransactionStack().size());
    			current.rollback();
    			Assert.assertEquals("stack trail size after commit should be 0", 0, current.getBeginTransactionStack().size());


    		}
    		finally
    		{
    			Config.setParameter(Transaction.LOG_BEGIN_CONFIG_KEY, BooleanUtils.toStringTrueFalse(flagBefore));
    		}
    	}


}
