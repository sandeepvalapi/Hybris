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
package de.hybris.platform.spring;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/lazyInitPostProcessor-test-spring.xml")
@UnitTest
public class LazyInitOverrideBeanFactoryPostProcessorTest
{
	@Autowired
	Inner inner;

	@Test
	public void test()
	{
		//Inner is autowired so it needs to be created in context.
		//Depending on LazyInitOverriteBeanFactoryPostProcessor other beans will be also created but we can't easily check that.
		//So during their creation InnerA, InnerB and InnerC stores this information in Inner

		Assert.assertNotNull("Context wasnt initailized properly", inner);
		Assert.assertTrue("Bean InnerA wasn't created", inner.log.contains("A"));
		Assert.assertFalse("Bean InnerB was created", inner.log.contains("B"));
		Assert.assertTrue("Bean InnerC wasn't created", inner.log.contains("C"));
	}



	public static class Inner
	{
		private List<String> log;

		@PostConstruct
		public void init()
		{
			log = new ArrayList<>();
		}

		public void info(final String message)
		{
			log.add(message);
		}
	}

	public static class InnerA
	{
		@Autowired
		private Inner log;

		@PostConstruct
		public void init()
		{
			log.info("A");
		}
	}

	public static class InnerB
	{
		@Autowired
		private Inner log;

		@PostConstruct
		public void init()
		{
			log.info("B");
		}
	}

	public static class InnerC
	{
		@Autowired
		private Inner log;

		@PostConstruct
		public void init()
		{
			log.info("C");
		}
	}
}
