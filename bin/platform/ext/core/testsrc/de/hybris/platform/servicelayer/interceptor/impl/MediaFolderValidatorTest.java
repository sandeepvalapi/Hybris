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
package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.media.MediaService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for a {@link MediaFolderValidator}'s logic.
 */
@UnitTest
public class MediaFolderValidatorTest
{
	private MediaFolderValidator validator;

	@Mock
	private InterceptorContext ctx;

	@Mock
	private MediaService mediaService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		validator = new MediaFolderValidator();
		validator.setMediaService(mediaService);
	}

	@Test
	public void testNullModel() throws InterceptorException
	{
		validator.onValidate(null, ctx);
	}

	@Test
	public void testOtherFolderModel() throws InterceptorException
	{
		final MediaFolderModel otherModel = new MediaFolderModel();
		otherModel.setQualifier("otherfooFolder");

		final MediaFolderModel model = new MediaFolderModel();
		model.setQualifier("fooFolder");

		Mockito.when(mediaService.getFolder(Mockito.eq("fooFolder"))).thenReturn(otherModel);

		validator.onValidate(model, ctx);
	}


	@Test
	public void testTheSameFolderModel() throws InterceptorException
	{
		//final MediaFolderModel otherModel = new MediaFolderModel();
		//otherModel.setQualifier("otherfooFolder");

		final MediaFolderModel model = new MediaFolderModel();
		model.setQualifier("fooFolder");

		Mockito.when(mediaService.getFolder(Mockito.eq("fooFolder"))).thenReturn(model);

		validator.onValidate(model, ctx);
	}

	@Test
	public void testOtherWithDuplicatedQualifierFolderModel() throws InterceptorException
	{
		final MediaFolderModel otherModel = new MediaFolderModel();
		otherModel.setQualifier("fooFolder");

		final MediaFolderModel model = new MediaFolderModel();
		model.setQualifier("fooFolder");

		Mockito.when(mediaService.getFolder(Mockito.eq("fooFolder"))).thenReturn(otherModel);
		try
		{
			validator.onValidate(model, ctx);
		}
		catch (final InterceptorException ie)
		{
			Assert.assertTrue(ie.getInterceptor() instanceof MediaFolderValidator);
			Assert.assertTrue(ie.getMessage().contains("MediaFolder with qualifier: fooFolder already exists in the system."));
		}
	}

	@Test
	public void testNotExistingFolderModel() throws InterceptorException
	{
		final MediaFolderModel model = new MediaFolderModel();
		model.setQualifier("fooFolder");

		Mockito.when(mediaService.getFolder(Mockito.eq("fooFolder"))).thenThrow(new UnknownIdentifierException("foo not found"));
		try
		{
			validator.onValidate(model, ctx);
		}
		catch (final InterceptorException ie)
		{
			Assert.fail("Not existing folder should not break validation ");
		}
	}

	@Test
	public void testAmbiguousFolderModel() throws InterceptorException
	{
		final MediaFolderModel model = new MediaFolderModel();
		model.setQualifier("fooFolder");

		Mockito.when(mediaService.getFolder(Mockito.eq("fooFolder"))).thenThrow(new AmbiguousIdentifierException("foo not found"));
		try
		{
			validator.onValidate(model, ctx);
			Assert.fail("Ambiguous folder qualifier should break validation ");
		}
		catch (final InterceptorException ie)
		{
			//
		}
	}
}
