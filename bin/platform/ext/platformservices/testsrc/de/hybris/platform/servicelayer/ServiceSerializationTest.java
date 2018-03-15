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
package de.hybris.platform.servicelayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.impl.LazyLoadMultiColumnModelList;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class ServiceSerializationTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(ServiceSerializationTest.class.getName());

	private static final int EXPECTED_PRODUCTS = 10;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Test
	public void testSearchListSerialization() throws Exception
	{
		createCoreData();
		createDefaultCatalog();

		final LazyLoadModelList singleColumnList = getList();
		verifyListSize(singleColumnList);
		final Object deserializedList = deserializeObject(serializeObject(singleColumnList));
		Assert.assertTrue("deserialized object is not a List, instead it is " + deserializedList.getClass(),
				deserializedList instanceof LazyLoadModelList);
		verifyListSize((LazyLoadModelList) deserializedList);

		final LazyLoadMultiColumnModelList multiColumnList = getMultiColumList();
		verifyListSize(multiColumnList);
		final Object deserializedMultiList = deserializeObject(serializeObject(multiColumnList));
		Assert.assertTrue("deserialized object is not a MultiColumnList, instead it is " + deserializedMultiList.getClass(),
				deserializedMultiList instanceof LazyLoadMultiColumnModelList);
		verifyListSize((LazyLoadMultiColumnModelList) deserializedMultiList);
	}

	@Test
	public void testServiceSerialization() throws IOException, ClassNotFoundException
	{
		assertNotNull(userService);
		assertNotNull(modelService);
		assertNotNull(flexibleSearchService);

		final UserService userser = (UserService) deserializeObject(serializeObject(userService));
		final ModelService modser = (ModelService) deserializeObject(serializeObject(modelService));
		final FlexibleSearchService flexser = (FlexibleSearchService) deserializeObject(serializeObject(flexibleSearchService));

		assertSame(userser, userService);
		assertSame(modser, modelService);
		assertSame(flexser, flexibleSearchService);
	}

	private void verifyListSize(final List list)
	{
		Assert.assertTrue("list is null ", list != null);
		Assert.assertTrue(list.getClass().getName() + " list is empty", !list.isEmpty());
		Assert.assertTrue(list.getClass().getName() + " list does not contain expected " + EXPECTED_PRODUCTS + " items ",
				list.size() == EXPECTED_PRODUCTS);
	}

	//create simple list
	private LazyLoadModelList getList()
	{

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(//
				"SELECT {code},{name},{unit} FROM {Product}  ");

		searchQuery.setResultClassList(Arrays.asList(String.class, String.class, Unit.class));

		final SearchResult<List<?>> searchResult = flexibleSearchService.search(searchQuery);
		final List list = (List) ModelUtils.getFieldValue(searchResult, "resultList");
		assertNotNull(list);
		assertTrue(list instanceof LazyLoadMultiColumnModelList);
		final LazyLoadMultiColumnModelList colList = (LazyLoadMultiColumnModelList) list;
		return (LazyLoadModelList) ModelUtils.getFieldValue(colList, "wrappedItemList");
	}

	//create simple multicolumn list
	private LazyLoadMultiColumnModelList getMultiColumList()
	{

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(//
				"SELECT {code},{name},{unit} FROM {Product} ");

		flexibleSearchQuery.setResultClassList(Arrays.asList(String.class, String.class, Unit.class));

		final SearchResult<List<?>> searchResult = flexibleSearchService.search(flexibleSearchQuery);
		final List list = (List) ModelUtils.getFieldValue(searchResult, "resultList");
		assertNotNull(list);
		assertTrue(list instanceof LazyLoadMultiColumnModelList);
		return (LazyLoadMultiColumnModelList) list;

	}

	private byte[] serializeObject(final Object object) throws IOException
	{
		LOG.info("serializing object " + object);

		ObjectOutputStream objectStream = null;
		ByteArrayOutputStream byteStream = null;
		try
		{
			byteStream = new ByteArrayOutputStream();
			objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(object);
			objectStream.flush();
			return byteStream.toByteArray();
		}
		finally
		{
			if (objectStream != null)
			{
				objectStream.close();
			}
		}
	}

	private Object deserializeObject(final byte[] serializedObject) throws IOException, ClassNotFoundException
	{
		Object returnObject = null;
		ObjectInputStream objectStream = null;
		ByteArrayInputStream byteStream = null;
		try
		{
			byteStream = new ByteArrayInputStream(serializedObject);
			objectStream = new ObjectInputStream(byteStream);
			returnObject = objectStream.readObject();
			LOG.info("deserialized object " + returnObject);
			return returnObject;
		}
		finally
		{
			if (objectStream != null)
			{
				objectStream.close();
			}
		}
	}
}
