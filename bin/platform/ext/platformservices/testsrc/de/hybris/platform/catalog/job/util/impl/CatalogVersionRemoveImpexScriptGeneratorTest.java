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
package de.hybris.platform.catalog.job.util.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.util.CSVConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@UnitTest
public class CatalogVersionRemoveImpexScriptGeneratorTest
{

	@Mock
	private CatalogVersionJobDao catalogVersionJobDao;


	private CatalogVersionRemoveImpexScriptGenerator converter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		converter = new CatalogVersionRemoveImpexScriptGenerator();
		converter.setCatalogVersionJobDao(catalogVersionJobDao);
	}

	@Test
	public void testFillInWithSomesTypeInstances()
	{

		final CatalogVersionModel version = new CatalogVersionModel();

		final ComposedTypeModel ctOne = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		final ComposedTypeModel ctTwo = createComposedTypeModel("java.lang.Integer", "somePublicfieldTwo", "SomeFunkyCustomType");

		final List<ComposedTypeModel> unorderedComposedTypes = Arrays.asList(ctOne, ctTwo);

		final PK one = PK.createFixedCounterPK(1000, 0);
		final PK two = PK.createFixedCounterPK(1000, 1);
		final PK three = PK.createFixedCounterPK(1000, 2);
		final PK four = PK.createFixedCounterPK(1000, 3);

		final List<PK> firstResult = createSearchResultMock(one, two);

		//record here result for a somePublicfieldTwo
		final List<PK> secondResult = createSearchResultMock(one, two, three, four);


		final Stack<List<PK>> stackOfResults = new Stack<List<PK>>();
		stackOfResults.add(secondResult);
		stackOfResults.add(firstResult);

		Mockito.doAnswer(new Answer<List<PK>>()
		{

			@Override
			public List<PK> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				Assert.assertTrue(args[0] instanceof ComposedTypeModel);
				Assert.assertTrue(args[1] instanceof CatalogVersionModel);
				try
				{
					return stackOfResults.pop();
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(List.class);
				}

			}

		}).when(catalogVersionJobDao).getPKList(Mockito.any(ComposedTypeModel.class), Mockito.any(CatalogVersionModel.class));

		final StringBuffer expectedBuffer = new StringBuffer(1000);
		expectedBuffer
				.append(
						"#% de.hybris.platform.jalo.JaloSession.getCurrentSession().getSessionContext().setAttribute( de.hybris.platform.jalo.Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);")
				.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append("remove SomeCustomType;pk[unique=true]").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093023208").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093055976").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append("# end of pk list for SomeCustomType").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append("remove SomeFunkyCustomType;pk[unique=true]").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093023208").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093055976").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093088744").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(";8796093121512").append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		expectedBuffer.append("# end of pk list for SomeFunkyCustomType").append(CSVConstants.HYBRIS_LINE_SEPARATOR);

		Assert.assertEquals(expectedBuffer.toString(), converter.generate(version, unorderedComposedTypes).toString());

	}


	@Test
	public void testFillInWithoutTypesNoInstances()
	{

		final CatalogVersionModel version = new CatalogVersionModel();

		final List<ComposedTypeModel> unorderedComposedTypes = Collections.EMPTY_LIST;

		Mockito.doAnswer(new Answer<List<PK>>()
		{

			@Override
			public List<PK> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				Assert.assertTrue(args[0] instanceof ComposedTypeModel);
				Assert.assertTrue(args[1] instanceof CatalogVersionModel);
				try
				{
					return Collections.EMPTY_LIST;
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(List.class);
				}

			}

		}).when(catalogVersionJobDao).getPKList(Mockito.any(ComposedTypeModel.class), Mockito.any(CatalogVersionModel.class));

		Assert.assertEquals(0, converter.generate(version, unorderedComposedTypes).length());
	}

	@Test
	public void testFillInWithSomeTypesNoInstances()
	{

		final CatalogVersionModel version = new CatalogVersionModel();

		final ComposedTypeModel ctOne = createComposedTypeModel("java.lang.String", "fieldOne", "SomeCustomType");
		final ComposedTypeModel ctTwo = createComposedTypeModel("java.lang.Integer", "somePublicfieldTwo", "SomeFunkyCustomType");

		final List<ComposedTypeModel> unorderedComposedTypes = Arrays.asList(ctOne, ctTwo);

		Mockito.doAnswer(new Answer<List<PK>>()
		{

			@Override
			public List<PK> answer(final InvocationOnMock invocation) throws Throwable
			{
				final Object[] args = invocation.getArguments();

				Assert.assertTrue(args[0] instanceof ComposedTypeModel);
				Assert.assertTrue(args[1] instanceof CatalogVersionModel);
				try
				{
					return Collections.EMPTY_LIST;
				}
				catch (final EmptyStackException ese)
				{
					return Mockito.mock(List.class);
				}

			}

		}).when(catalogVersionJobDao).getPKList(Mockito.any(ComposedTypeModel.class), Mockito.any(CatalogVersionModel.class));

		final StringBuffer expectedBuffer = new StringBuffer(1000);
		expectedBuffer
				.append(
						"#% de.hybris.platform.jalo.JaloSession.getCurrentSession().getSessionContext().setAttribute( de.hybris.platform.jalo.Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);")
				.append(CSVConstants.HYBRIS_LINE_SEPARATOR);

		Assert.assertEquals(expectedBuffer.toString(), converter.generate(version, unorderedComposedTypes).toString());



	}


	private ComposedTypeModel createComposedTypeModel(final String attributeTypeName, final String attribiteQualifier,
			final String composedTypeCode)
	{
		final TypeModel type = new TypeModel();
		type.setCode(attributeTypeName);

		final AttributeDescriptorModel descriptor = new AttributeDescriptorModel();
		descriptor.setQualifier(attribiteQualifier);
		descriptor.setAttributeType(type);

		final ComposedTypeModel composedType = new ComposedTypeModel();
		composedType.setCode(composedTypeCode);
		composedType.setCatalogVersionAttribute(descriptor);

		return composedType;
	}

	/**
	 * Creates a mock of result returning one row as integer value of <code>resultCount</code>
	 */
	private List<PK> createSearchResultMock(final PK... pks)
	{
		final List<PK> underlyingResult = Arrays.asList(pks);

		return underlyingResult;
	}
}
