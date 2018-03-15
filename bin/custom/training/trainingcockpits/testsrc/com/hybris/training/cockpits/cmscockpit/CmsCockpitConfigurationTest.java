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
package com.hybris.training.cockpits.cmscockpit;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.cockpit.model.meta.DefaultEditorFactory;
import de.hybris.platform.cockpit.model.meta.DefaultPropertyEditorDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.core.Registry;
import de.hybris.platform.spring.ctx.ScopeTenantIgnoreDocReader;

@IntegrationTest
public class CmsCockpitConfigurationTest extends BaseCommerceBaseTest {
	private static ApplicationContext applicationContext;
	
	@BeforeClass
	public static void testsSetup(){
		Registry.setCurrentTenantByID("junit");
		final GenericApplicationContext context = new GenericApplicationContext();
		context.setResourceLoader(new DefaultResourceLoader(Registry.class.getClassLoader()));
		context.setClassLoader(Registry.class.getClassLoader());
		context.getBeanFactory().setBeanClassLoader(Registry.class.getClassLoader());
		context.setParent(Registry.getGlobalApplicationContext());
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		xmlReader.setBeanClassLoader(Registry.class.getClassLoader());
		xmlReader.setDocumentReaderClass(ScopeTenantIgnoreDocReader.class);
		xmlReader.loadBeanDefinitions(getSpringConfigurationLocations());
		context.refresh();
		applicationContext = context;
	}
	
	@Test
	public void verifyClassesExist(){
		final DefaultEditorFactory factory = (DefaultEditorFactory) applicationContext.getBean("acceleratorEditorFactory");
		for (PropertyEditorDescriptor descriptor : factory.getAllEditorDescriptors()){
			DefaultPropertyEditorDescriptor defaultDescriptor = (DefaultPropertyEditorDescriptor) descriptor;
			for (final String editorClazz : defaultDescriptor.getEditors().values()){
				try {
					Class.forName(editorClazz);
				} catch (ClassNotFoundException e) {
					Assert.fail(String.format("Class %s used in trainingcockpits/cmscockpit configuration does not exist", editorClazz));
				}
			}
		}
	}
	
	@After
	public void destroyApplicationContext()
	{
		if (applicationContext != null)
		{
			((GenericApplicationContext) applicationContext).destroy();
			applicationContext = null;
		}
	}
	
	protected static String[] getSpringConfigurationLocations()
	{
		return new String[]
		{  "cmscockpit/cmscockpit-spring-configs.xml", //
				"classpath:/trainingcockpits/cmscockpit/spring/import.xml"};
	}

}