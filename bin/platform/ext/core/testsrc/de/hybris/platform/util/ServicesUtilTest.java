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
package de.hybris.platform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;


/**
 * tests the {@link ServicesUtil} class
 */
@UnitTest
public class ServicesUtilTest
{
	@Test
	public void testWithModels()
	{
		ServicesUtil.validateIfSingleResult(Collections.singleton(new ProductModel()), ItemModel.class, "x", "y");

		try
		{
			ServicesUtil.validateIfSingleResult(Collections.singleton(new ProductModel()), MediaModel.class, "x", "y");
			fail("expected IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("element in result ('class de.hybris.platform.core.model.product.ProductModel') is not "
					+ "the same class or a subclass of 'class de.hybris.platform.core.model.media.MediaModel'", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}
	}

	@Test
	public void validateParameterNotNullStandardMessage()
	{
		try
		{
			ServicesUtil.validateParameterNotNullStandardMessage(null, null);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Parameter null can not be null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//fine
		ServicesUtil.validateParameterNotNullStandardMessage(null, new Object());
	}

	@Test
	public void validateParameterNotNull()
	{
		try
		{
			ServicesUtil.validateParameterNotNull(null, null);
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//fine
		ServicesUtil.validateParameterNotNull(new Object(), null);

	}

	@Test
	public void validateIfSingleResultWithClazz()
	{
		//emptycollection
		try
		{
			ServicesUtil.validateIfSingleResult(Collections.EMPTY_SET, Object.class, "x", "y");
			fail("expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals("Object with x 'y' not found!", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//null collection
		try
		{
			ServicesUtil.validateIfSingleResult(null, Object.class, "a", "");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("the result collection can not be null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//null class
		try
		{
			ServicesUtil.validateIfSingleResult(Collections.EMPTY_SET, null, "a", "");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("the given clazz can not be null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//null qualifier
		try
		{
			ServicesUtil.validateIfSingleResult(Collections.EMPTY_SET, Object.class, null, "");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("qualifier must contain something", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//empty
		try
		{
			ServicesUtil.validateIfSingleResult(Collections.EMPTY_SET, Object.class, "", "");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("qualifier must contain something", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}


		//fine
		ServicesUtil.validateIfSingleResult(Collections.singleton(BigInteger.ONE), Number.class, "a", "a");

		//not fine
		try
		{

			ServicesUtil.validateIfSingleResult(Collections.singleton(new Object()), Number.class, "a", "a");
			fail("expected IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			assertEquals(
					"element in result ('class java.lang.Object') is not the same class or a subclass of 'class java.lang.Number'",
					e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//too much
		try
		{

			ServicesUtil.validateIfSingleResult(Arrays.asList(new Object(), new Object()), Object.class, "a", "a");
			fail("expected AmbiguousIdentifierException");
		}
		catch (final AmbiguousIdentifierException e)
		{
			assertEquals("Object a 'a' is not unique, 2 instances  of type Object found!", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}
	}

	@Test
	public void validateIfSingleResult()
	{
		try
		{
			ServicesUtil.validateIfSingleResult(null, "unknownIdException", "ambiguousIdException");
			fail("expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("the result collection can not be null", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		try
		{
			ServicesUtil.validateIfSingleResult(Collections.EMPTY_SET, "unknownIdException", "ambiguousIdException");
			fail("expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals("unknownIdException", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		ServicesUtil.validateIfSingleResult(Collections.singleton(new Object()), "unknownIdException", "ambiguousIdException");


		try
		{
			ServicesUtil.validateIfSingleResult(Arrays.asList(new Object(), new Object()), "unknownIdException",
					"ambiguousIdException");
			fail("expected AmbiguousIdentifierException");
		}
		catch (final AmbiguousIdentifierException e)
		{
			assertEquals("ambiguousIdException", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}
	}

	@Test
	public void testValidateAnyResult()
	{
		//null checks
		try
		{
			ServicesUtil.validateIfAnyResult(null, "unknownIdException");
			fail("expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals("unknownIdException", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//empty coll check
		try
		{
			ServicesUtil.validateIfAnyResult(new ArrayList<Object>(), "is empty");
			fail("expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals("is empty", e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}

		//check positive result
		ServicesUtil.validateIfAnyResult(Collections.singleton(new Object()), "one element");

		//checks double null
		try
		{
			ServicesUtil.validateIfAnyResult(null, null);
			fail("expected UnknownIdentifierException");
		}
		catch (final UnknownIdentifierException e)
		{
			assertEquals(null, e.getMessage());
		}
		catch (final Exception e)
		{
			fail("got unexpected exception: " + e);
		}
	}
}
