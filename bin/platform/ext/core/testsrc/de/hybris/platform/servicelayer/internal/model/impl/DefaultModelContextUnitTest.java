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
package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.model.ModelContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 *
 */
public class DefaultModelContextUnitTest
{
	private final ModelContext modelContext = new DefaultModelContext();

	@Mock
	private ModelConverter converter;

	@Mock
	private Item titleJalo;

	@Mock
	private PersistenceObject persistenceObject;


	private final PK titlePK = PK.createFixedCounterPK(1010, System.currentTimeMillis());

	private final TitleModel titleModel = new TitleModel();


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		BDDMockito.given(converter.getSource(titleModel)).willReturn(titlePK);
		BDDMockito.given(converter.getPersistenceSource(titleModel)).willReturn(persistenceObject);
		BDDMockito.given(titleJalo.getPK()).willReturn(titlePK);
		BDDMockito.given(persistenceObject.getPK()).willReturn(titlePK);
	}


	@Test
	public void testIsAttachedForANew()
	{
		Assert.assertFalse(modelContext.isAttached(titleModel, converter));
	}

	@Test
	public void testIsAttachedForAttached()
	{
		BDDMockito.given(Boolean.valueOf(converter.isNew(titleModel))).willReturn(Boolean.TRUE);

		modelContext.attach(titleModel, null, converter);
		Assert.assertTrue(modelContext.isAttached(titleModel, converter));
	}

	@Test
	public void testIsAttachedForDettached()
	{
		BDDMockito.given(Boolean.valueOf(converter.isNew(titleModel))).willReturn(Boolean.TRUE);

		modelContext.attach(titleModel, null, converter);
		modelContext.detach(titleModel, null, converter);
		Assert.assertFalse(modelContext.isAttached(titleModel, converter));
	}


	@Test
	public void testIsAttachedForNotModified()
	{
		BDDMockito.given(Boolean.valueOf(converter.isModified(titleModel))).willReturn(Boolean.FALSE);

		modelContext.attach(titleModel, titleJalo, converter);
		Assert.assertTrue(modelContext.isAttached(titleModel, converter));

		Assert.assertFalse(modelContext.getModified().contains(titleModel));
	}

	@Test
	public void testIsAttachedForModified()
	{
		BDDMockito.given(Boolean.valueOf(converter.isModified(titleModel))).willReturn(Boolean.TRUE);

		modelContext.attach(titleModel, titleJalo, converter);
		Assert.assertTrue(modelContext.isAttached(titleModel, converter));

		Assert.assertTrue(modelContext.getModified().contains(titleModel));
	}


	@Test
	public void testIsAttachedForNotModifiedAfterDetach()
	{
		BDDMockito.given(Boolean.valueOf(converter.isModified(titleModel))).willReturn(Boolean.FALSE);

		modelContext.attach(titleModel, titleJalo, converter);
		modelContext.detach(titleModel, titleJalo, converter);

		Assert.assertFalse(modelContext.isAttached(titleModel, converter));
		Assert.assertFalse(modelContext.getModified().contains(titleModel));

	}

	@Test
	public void testIsAttachedForModifiedAfterDetach()
	{
		BDDMockito.given(Boolean.valueOf(converter.isModified(titleModel))).willReturn(Boolean.TRUE);

		modelContext.attach(titleModel, titleJalo, converter);
		modelContext.detach(titleModel, titleJalo, converter);

		Assert.assertFalse(modelContext.isAttached(titleModel, converter));
		Assert.assertFalse(modelContext.getModified().contains(titleModel));

	}

}
