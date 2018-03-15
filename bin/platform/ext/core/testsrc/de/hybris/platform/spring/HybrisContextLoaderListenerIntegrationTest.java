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
package de.hybris.platform.spring;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;


@IntegrationTest
public class HybrisContextLoaderListenerIntegrationTest extends ServicelayerBaseTest
{
	JaloSession jaloToBeAsserted = null;

	public void shouldProperlyActivateJaloSession()
	{
		//given
		final HttpSession session = new MockHttpSession();

		final JaloSession jalo = new JaloSession();
		session.setAttribute(Constants.WEB.JALOSESSION, jalo);

		final HybrisContextLoaderListener listener = new HybrisContextLoaderListener();

		final Runnable runnable = () -> {
			jaloToBeAsserted = Registry.getCurrentTenant().getActiveSession();
		};

		//when
		JaloSession.deactivate();

		//then
		assertThat(Registry.getCurrentTenant().getActiveSession()).isNull();

		//when
		listener.runWithActiveJaloSession(Registry.getCurrentTenant().getTenantID(), session, runnable);

		//then
		assertThat(Registry.getCurrentTenant().getActiveSession()).isNull();
		assertThat(jaloToBeAsserted).isNotNull();
		assertThat(jaloToBeAsserted.getSessionID()).isEqualTo(jalo.getSessionID());
	}

	@Test
	public void shouldNotTouchSessionWhenAlredySetOnCurrentThread()
	{
		//given
		final HttpSession session = new MockHttpSession();

		final JaloSession jalo = new JaloSession();
		jalo.createLocalSessionContext();
		session.setAttribute(Constants.WEB.JALOSESSION, jalo);

		final HybrisContextLoaderListener listener = new HybrisContextLoaderListener();

		final Runnable runnable = () -> {
			jaloToBeAsserted = Registry.getCurrentTenant().getActiveSession();
		};

		//given
		Registry.setCurrentTenantByID("junit");
		final AbstractTenant tenant = Registry.getCurrentTenantNoFallback();
		tenant.setActiveSessionForCurrentThread(jalo);

		//when
		listener.runWithActiveJaloSession(Registry.getCurrentTenant().getTenantID(), session, runnable);

		//then
		assertThat(Registry.getCurrentTenant().getActiveSession()).isEqualTo(jalo);
		assertThat(jaloToBeAsserted).isNotNull();
		assertThat(jaloToBeAsserted.getSessionID()).isEqualTo(jalo.getSessionID());
		assertThat(jalo.isClosed()).isFalse();
	}
}
