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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * PLA-8821
 */
@IntegrationTest
public class UniqueOptionalAttributeTest extends ServicelayerTest
{

	@Resource
	private TypeService typeService;
	@Resource
	private ModelService modelService;

	private UserModel source;
	private UserModel target;
	private LanguageModel language;

	private final LinkManager lm = LinkManager.getInstance();

	@Before
	public void setUp()
	{
		source = modelService.create(UserModel.class);
		source.setUid("testUserSource");

		target = modelService.create(UserModel.class);
		target.setUid("testUserTarget");

		language = modelService.create(LanguageModel.class);
		language.setIsocode("PL");
		language.setName("polish");

		modelService.saveAll();
	}

	@Test
	public void testUniqueNullAttributeSlayerAPI()
	{

		final ComposedTypeModel typeUnderInvestigation = typeService.getComposedTypeForClass(LinkModel.class);
		final AttributeDescriptorModel attributeDesc = typeService.getAttributeDescriptor(typeUnderInvestigation,
				LinkModel.LANGUAGE);
		Assert.assertTrue(attributeDesc.getOptional().booleanValue());
		Assert.assertTrue(attributeDesc.getUnique().booleanValue());

		final LinkModel link1 = modelService.create(LinkModel.class);
		link1.setQualifier("link1");
		link1.setSource(source);
		link1.setTarget(target);
		//the optional unique attribute
		link1.setLanguage(language);

		modelService.save(link1); //<-- OK!

		final LinkModel link2 = modelService.create(LinkModel.class);
		link2.setQualifier("link2");
		link2.setSource(source);
		link2.setTarget(target);
		//the optional unique attribute
		link2.setLanguage(null);

		modelService.save(link2); //<-- OK! //<-- still OK, only one null value!

		final LinkModel link3 = modelService.create(LinkModel.class);
		link3.setQualifier("link3");
		link3.setSource(source);
		link3.setTarget(target);
		//the optional unique attribute
		link3.setLanguage(null);

		boolean success = false;
		try
		{
			modelService.save(link3); //<-- what happens now?!
			Assert.fail("Should have failed with 'ModelSavingException' for ambiguous unique attribute, which is 'null'");
		}
		catch (final ModelSavingException e)
		{
			success = true;
		}
		Assert.assertTrue("Should have failed with 'ModelSavingException' for ambiguous unique attribute, which is 'null'", success);
	}

	@Test
	public void testJalo() throws ConsistencyCheckException
	{
		lm.createLink("testJaloLink", (Language) modelService.getSource(language), source.getPk(), target.getPk(), 1, 1);
		lm.createLink("testJaloLink", null, source.getPk(), target.getPk(), 1, 1);
		lm.createLink("testJaloLink", null, source.getPk(), target.getPk(), 1, 1);

	}

}
