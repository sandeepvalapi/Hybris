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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.StopWatch;
import de.hybris.platform.util.Utilities;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ItemTest extends HybrisJUnit4Test
{
	TypeManager typeManager;
	ComposedType productType, specialProductType;
	ComposedType languageType;
	Product product;


	@Before
	public void setUp() throws Exception
	{
		typeManager = jaloSession.getTypeManager();
		productType = typeManager.getRootComposedTypeForJaloClass(Product.class);
		languageType = typeManager.getRootComposedType(Language.class);
		specialProductType = typeManager.createComposedType(productType, "specialProductType");
		product = jaloSession.getProductManager().createProduct("newtype.ItemTest");
	}

	@Test
	public void testIsRemoving() throws ConsistencyCheckException
	{
		assertEquals(0, Item.getCurrentlyRemovingCount());

		final Country c = C2LManager.getInstance().createCountry("foo");
		final Region r1 = c.addNewRegion("r1");
		final Region r2 = c.addNewRegion("r2");

		c.remove(); // during removal the stack count will increase but after that w should see 0 again

		assertEquals(0, Item.getCurrentlyRemovingCount());

		assertFalse(c.isAlive());
		assertFalse(r1.isAlive());
		assertFalse(r2.isAlive());
	}


	@Test
	public void testProduct() throws JaloInvalidParameterException, ConsistencyCheckException
	{
		assertEquals(productType, product.getComposedType());
		product.setComposedType(specialProductType);
		assertEquals(specialProductType, product.getComposedType());
		/*
		 * try { product.setNewType( languageType ); fail( "expected JaloInvalidParameterException" ); } catch
		 * (JaloInvalidParameterException e) { // fine... }
		 */
	}

	@Test
	public void testGenericAccess() throws Exception
	{
		assertEquals("newtype.ItemTest", product.getAttribute("code"));
	}

	@Test
	public void testDelete() throws Exception
	{
		final ComposedType specialProductType2 = typeManager.createComposedType(productType, "specialProductType2");
		final String code = specialProductType2.getCode();

		specialProductType2.remove();
		try
		{
			typeManager.getComposedType(code);
			fail("Should have raised a JaloItemNotFoundException!");
		}
		catch (final JaloItemNotFoundException e)
		{
			// DOCTODO Document reason, why this block is empty
		}

		try
		{
			productType.remove();
			fail("Should have raised a ConsistencyCheckException!");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
		/*
		 * try { final ComposedType wkzFactorType = typeManager.getComposedType( "WKZFactor" ); try {
		 * wkzFactorType.remove(); fail( "Should have raised a ConsistencyCheckException!" ); } catch(
		 * ConsistencyCheckException e ) { } } catch( JaloItemNotFoundException e ) { //do nothing }
		 */
	}

	// see PLA-5044
	@Test
	public void testSetCreationTime() throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
	{
		Title t;
		t = UserManager.getInstance().createTitle("xtc");

		final Date createdAt = t.getCreationTime();
		assertNotNull(createdAt);
		assertEquals(createdAt, t.getAttribute(Item.CREATION_TIME));


		final Calendar cal = Utilities.getDefaultCalendar();
		cal.setTime(createdAt);
		// jump one year behind
		cal.add(Calendar.YEAR, -1);

		final Date newDate = cal.getTime();

		// XXX we need to tweak the system here a bit since initial attribute creationTime is not allowed to be changed now
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setAttribute(Item.INITIAL_CREATION_FLAG, Boolean.TRUE);
		t.setAttribute(ctx, Item.CREATION_TIME, newDate);

		assertEquals(newDate, t.getCreationTime());
	}



	@Test
	public void isStillAliveTest() throws Exception
	{
		final Title t = UserManager.getInstance().createTitle("xtc");
		t.getCode();
		final StopWatch w = new StopWatch("is alive check");
		for (int i = 0; i < 1000000; i++)
		{
			t.isAlive();
		}
		w.stop();
	}











}
