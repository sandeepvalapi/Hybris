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
package de.hybris.platform.servicelayer.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;


/*
 * This test must not be transactional - see PLA-8143 for details !
 */
@IntegrationTest
public class ItemModelTenantTest extends ServicelayerBaseTest
{

	//here injected is the bean for the current application context which could be the web application context specific for the tenant or the core application context for current tenant  
	@Resource
	private ModelService modelService;


	private Tenant current;

	@Before
	public void prepare()
	{
		current = Registry.getCurrentTenant();
		Assume.assumeTrue(current instanceof SlaveTenant);
	}

	@After
	public void after()
	{
		if (current != null)
		{
			Registry.setCurrentTenant(current);
		}
	}

	@Test
	public void testTenantItemBehaviour() throws ConsistencyCheckException
	{
		final PK fixedPK = PK.createFixedUUIDPK(Constants.TC.Title, 1234567);
		Title t2Title = null;
		try
		{
			final Title t1Title = UserManager.getInstance().createTitle(fixedPK, "title");
			final TitleModel t1Model = modelService.get(t1Title);

			assertEquals(current, t1Title.getTenant());
			assertEquals(current.getTenantID(), ModelContextUtils.getItemModelContext(t1Model).getTenantId());

			Registry.activateMasterTenant();

			final Tenant master = Registry.getCurrentTenant();
			assertTrue(master instanceof MasterTenant);

			if (master.getJaloConnection().isSystemInitialized())
			{

				t2Title = UserManager.getInstance().createTitle(fixedPK, "title");
				//here we have to get the bean from context for master tenant - we can not call Registry.getGlobalApplicationContext() which might return a context bounded to junit tenant since test are run in its web contexts
				final TitleModel t2Model = ((ModelService) Registry.getGlobalApplicationContext().getBean("modelService"))
						.get(t2Title);

				assertEquals(master, t2Title.getTenant());
				assertEquals(master.getTenantID(), ModelContextUtils.getItemModelContext(t2Model).getTenantId());
				assertEquals(t1Title.getPK(), t2Title.getPK());
				assertEquals(t1Model.getPk(), t2Model.getPk());
				assertFalse(t1Title.equals(t2Title));
				assertFalse(t1Model.equals(t2Model));
			}
			else
			{
				System.err.println("Cannot test with master tenant since it's not initialized!");
			}
		}
		finally
		{
			if (t2Title != null)
			{
				try
				{
					t2Title.remove();
				}
				catch (final Exception e)
				{
					// can't help it
				}
			}

		}
	}

}
