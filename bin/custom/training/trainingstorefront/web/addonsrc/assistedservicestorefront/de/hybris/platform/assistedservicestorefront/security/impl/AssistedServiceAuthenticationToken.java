/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.security.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * Assisted Service Auth token that deals with {@link AssistedServiceAgentPrincipal} as principal.
 */
public class AssistedServiceAuthenticationToken extends UsernamePasswordAuthenticationToken
{
	private boolean emulating = false;

	public AssistedServiceAuthenticationToken(final AssistedServiceAgentPrincipal principal)
	{
		super(principal, "");
	}

	public AssistedServiceAuthenticationToken(final AssistedServiceAgentPrincipal principal,
			final Collection<? extends GrantedAuthority> authorities)
	{
		super(principal, "", authorities);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities()
	{
		// When agent doesn't emulate customer - return only anonymous role
		if (!isEmulating())
		{
			final List<GrantedAuthority> authorirites = new ArrayList();
			authorirites.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
			return Collections.unmodifiableCollection(authorirites);
		}
		else
		{
			return super.getAuthorities();
		}
	}

	public boolean isEmulating()
	{
		return emulating;
	}

	/**
	 * Set whether or not as agent token is used for emulating customer.
	 *
	 * @param emulating
	 */
	public void setEmulating(final boolean emulating)
	{
		this.emulating = emulating;
	}

	@Override
	public String getName()
	{
		return ((AssistedServiceAgentPrincipal) getPrincipal()).getName();
	}
}