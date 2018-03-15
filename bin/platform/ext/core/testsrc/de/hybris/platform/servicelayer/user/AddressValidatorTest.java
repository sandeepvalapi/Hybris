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
package de.hybris.platform.servicelayer.user;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.AddressValidator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AddressValidatorTest
{
	private AddressModel a1;
	private CountryModel c1;
	private RegionModel r1;
	private CountryModel c2;
	private RegionModel r2;
	private UserModel u1;

	@Before
	public void setUp() throws Exception
	{
		c1 = new CountryModel();
		c1.setActive(Boolean.TRUE);
		c1.setIsocode("c1");

		r1 = new RegionModel();
		r1.setActive(Boolean.TRUE);
		r1.setCountry(c1);
		r1.setIsocode("r1");
		final List<RegionModel> l1 = new ArrayList();
		l1.add(r1);
		c1.setRegions(l1);

		c2 = new CountryModel();
		c2.setActive(Boolean.TRUE);
		c2.setIsocode("c2");
		r2 = new RegionModel();
		r2.setActive(Boolean.TRUE);
		r2.setCountry(c2);
		r2.setIsocode("r2");
		final List<RegionModel> l2 = new ArrayList();
		l2.add(r2);
		c2.setRegions(l2);

		a1 = new AddressModel();
		a1.setOwner(u1);
		a1.setStreetname("Street");
		a1.setTown("Town");
		a1.setCountry(c1);
		a1.setRegion(r1);
	}

	@Test(expected = InterceptorException.class)
	public void testAddressValidator() throws InterceptorException
	{
		final AddressValidator vali = new AddressValidator();
		a1.setCountry(c1);
		a1.setRegion(r2);
		vali.onValidate(a1, null);
	}
}
