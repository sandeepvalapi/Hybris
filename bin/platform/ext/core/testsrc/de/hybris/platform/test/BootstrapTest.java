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
package de.hybris.platform.test;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.util.RequirementHolder;
import de.hybris.bootstrap.util.RequirementSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


@UnitTest
public class BootstrapTest
{

	@Test
	public void testRequirements() throws Exception
	{
		final Map<String, Req> globalreqs = new HashMap();

		Req r1, r2, r3;
		List l = new ArrayList();
		r1 = new Req(globalreqs, "1");
		l.add(r1);
		r2 = new Req(globalreqs, "2", "1");
		l.add(r2);
		List<List<? extends RequirementHolder>> solved = RequirementSolver.solveParallel(l);
		assertEquals(solved.get(0).get(0), r1);
		assertEquals(solved.get(1).get(0), r2);

		l = new ArrayList();
		r1 = new Req(globalreqs, "1");
		l.add(r1);
		r2 = new Req(globalreqs, "2", "1");
		l.add(r2);
		r3 = new Req(globalreqs, "3", "1");
		l.add(r3);
		solved = RequirementSolver.solveParallel(l);
		assertEquals(solved.get(0).get(0), r1);
		assertTrue(solved.get(1).size() == 2);
		assertTrue(solved.get(1).contains(r2));
		assertTrue(solved.get(1).contains(r3));
	}



	static class Req implements RequirementHolder
	{
		private final Map<String, Req> globalreq;
		private final String name;
		private final String[] reqs;

		public Req(final Map<String, Req> globalreq, final String name, final String... reqs)
		{
			this.globalreq = globalreq;
			this.name = name;
			this.reqs = reqs;
			globalreq.put(name, this);
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == null)
			{
				return false;
			}

			return (((Req) obj).name.equals(name));
		}

		@Override
		public int hashCode()
		{
			return name.hashCode();
		}

		@Override
		public Set<? extends RequirementHolder> getRequirements()
		{
			final Set set = new HashSet();

			for (final String s : Arrays.asList(reqs))
			{
				set.add(getReq(s));
			}
			return set;
		}

		Req getReq(final String name)
		{
			final Req s = this.globalreq.get(name);
			Assert.assertNotNull(s);
			return s;
		}
	}
}
