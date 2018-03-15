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
package de.hybris.platform.catalog.dynamic;

import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


public abstract class AbstractCompanyAddressAttributeHandler
		implements DynamicAttributeHandler<Collection<AddressModel>, CompanyModel>
{
	private UserService userService;

	@Override
	public Collection<AddressModel> get(final CompanyModel model)
	{
		final Collection<AddressModel> addresses = getAllAddresses(model);
		filterOutAddresses(addresses);
		return addresses;
	}

	public abstract void filterOutAddresses(final Collection<AddressModel> addresses);

	private Collection<AddressModel> getAllAddresses(final CompanyModel model)
	{
		final Collection<AddressModel> addresses = Lists.newArrayList(model.getAddresses());
		for (final CompanyModel companyModel : userService.getAllUserGroupsForUserGroup(model, CompanyModel.class))
		{
			addresses.addAll(companyModel.getAddresses());
		}
		return addresses;
	}

	@Override
	public void set(final CompanyModel companyModel, final Collection<AddressModel> model)
	{
		throw new UnsupportedOperationException();
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
