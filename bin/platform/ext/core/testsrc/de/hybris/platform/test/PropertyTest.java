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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.type.AtomicTypeRemote;
import de.hybris.platform.persistence.type.CollectionTypeRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.EJBDuplicateCodeException;
import de.hybris.platform.persistence.type.MapTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.persistence.type.TypeRemote;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PropertyTest extends HybrisJUnit4TransactionalTest
{
	ComposedTypeRemote productType, languageType;
	AtomicTypeRemote stringType, integerType, booleanType, classType;
	CollectionTypeRemote collectionType;
	Map localizedTypes;

	TypeManagerEJB typeManager;

	@Before
	public void setUp() throws Exception
	{
		typeManager = SystemEJB.getInstance().getTypeManager();
		productType = typeManager.getRootComposedType(Constants.TC.Product);
		languageType = typeManager.getRootComposedType(Constants.TC.Language);
		stringType = typeManager.getOrCreateAtomicType(null, String.class);
		integerType = typeManager.getOrCreateAtomicType(null, Integer.class);
		booleanType = typeManager.getOrCreateAtomicType(null, Boolean.class);
		classType = typeManager.getOrCreateAtomicType(null, Class.class);
		collectionType = typeManager.createCollectionType(null, "propertytest-collectiontype", stringType);
		assertNotNull(collectionType);
		localizedTypes = new HashMap();
	}

	@After
	public void tearDown() throws Exception
	{
		localizedTypes = null;
	}

	@Test
	public void testSimple()
	{
		final Product product = ProductManager.getInstance().createProduct(null, "simplepropertytest");
		assertNotNull(product);
		assertEquals(null, product.setProperty("someprop", "somevalue"));
		assertEquals("somevalue", product.setProperty("someprop", "someothervalue"));
		assertEquals(null, product.setProperty("someotherprop", "somethirdvalue"));
		assertEquals("someothervalue", product.setProperty("someprop", null));
	}

	/*
	 * public void testProductUnlocalizedOld() throws Exception { final String qualifier = "proptestprop";
	 * ExtensibleItemRemote itemA = SystemEJB.getInstance().getProductManager().createProduct( "propertytestC" );
	 * assertNotNull( itemA, SystemEJB.getInstance().getProductManager() ); ExtensibleItemRemote itemB =
	 * SystemEJB.getInstance().getProductManager().createProduct( "propertytestD" ); assertNotNull( itemB,
	 * SystemEJB.getInstance().getProductManager() ); executeTest( productType, itemA, itemB, new FieldAccess() { public
	 * boolean isLegalFieldValue( Object value ) { return value==null || ( value instanceof Serializable && ! ( value
	 * instanceof Number || value instanceof Boolean || value instanceof Date || value instanceof java.awt.Color ) ); }
	 * 
	 * public Object setField( ExtensibleItemRemote item, Object value )
	 * 
	 * { Object result = ((ProductRemote)item).setProperty( qualifier, (Serializable)value ); return result; }
	 * 
	 * public Object getField( ExtensibleItemRemote item )
	 * 
	 * { return ((ProductRemote)item).getProperty( qualifier ); }
	 * 
	 * public boolean returnsOldValue() { return true; } }, qualifier, null, true ); }
	 * 
	 * public void testProductLocalizedOld() throws Exception { final String qualifier = "proptestproploc";
	 * ExtensibleItemRemote itemA = SystemEJB.getInstance().getProductManager().createProduct( "propertytestE" );
	 * assertNotNull( itemA, SystemEJB.getInstance().getProductManager() ); ExtensibleItemRemote itemB =
	 * SystemEJB.getInstance().getProductManager().createProduct( "propertytestF" ); assertNotNull( itemB,
	 * SystemEJB.getInstance().getProductManager() ); executeTest( productType, itemA, itemB, new FieldAccess() { public
	 * boolean isLegalFieldValue( Object value ) { return value==null || ( value instanceof Serializable && ! ( value
	 * instanceof Number || value instanceof Boolean || value instanceof Date || value instanceof java.awt.Color ) ); }
	 * 
	 * public Object setField( ExtensibleItemRemote item, Object value )
	 * 
	 * { return ((ProductRemote)item).setLocalizedProperty( qualifier, defaultLanguage, (Serializable)value ); }
	 * 
	 * public Object getField( ExtensibleItemRemote item )
	 * 
	 * { return ((ProductRemote)item).getLocalizedProperty( qualifier, defaultLanguage ); }
	 * 
	 * public boolean returnsOldValue() { return true; } }, qualifier, defaultLanguage, true ); }
	 * 
	 * public void testProductName() throws Exception { ExtensibleItemRemote itemA =
	 * SystemEJB.getInstance().getProductManager().createProduct( "propertytestA" ); assertNotNull( itemA,
	 * SystemEJB.getInstance().getProductManager() ); ExtensibleItemRemote itemB =
	 * SystemEJB.getInstance().getProductManager().createProduct( "propertytestB" ); assertNotNull( itemB,
	 * SystemEJB.getInstance().getProductManager() ); executeTest( productType, itemA, itemB, new FieldAccess() { public
	 * boolean isLegalFieldValue( Object value ) { return value==null || value instanceof String; }
	 * 
	 * public Object setField( ExtensibleItemRemote item, Object value )
	 * 
	 * { ((ProductRemote)item).setName( defaultLanguage, (String)value ); return null; }
	 * 
	 * public Object getField( ExtensibleItemRemote item )
	 * 
	 * { return ((ProductRemote)item).getName( defaultLanguage ); }
	 * 
	 * public boolean returnsOldValue() { return false; } }, Features.Product.NAME , defaultLanguage, false ); }
	 */


	private static interface FieldAccess
	{
		public boolean isLegalFieldValue(Object value);

		/** does {@link #setField} return the old value? */
		public boolean returnsOldValue();

		public Object setField(ExtensibleItemRemote item, Object value);

		public Object getField(ExtensibleItemRemote item);
	}

	protected final void executeTest(final ComposedTypeRemote type, final ExtensibleItemRemote itemA,
			final ExtensibleItemRemote itemB, final FieldAccess access, @SuppressWarnings("unused") final String searchQualifier,
			@SuppressWarnings("unused") final boolean createDescriptor)
	{
		assertEquals(null, access.getField(itemA));
		assertEquals(null, access.getField(itemB));

		access.setField(itemA, "test");
		assertEquals(type, (itemA.getComposedType()));
		assertEquals("test", access.getField(itemA));
		access.setField(itemA, null);
		assertEquals(null, access.getField(itemA));

		final Object[] values = new Object[]
		{ "value", "_", /* "", */null, Integer.valueOf(6), Boolean.TRUE, String.class, "some string again",
				"a string\n\twith\nnewlines", /* Collections.EMPTY_LIST, */" ",
				/* Arrays.asList( new Object[]{"a", "b", "c"} ), */
				new ItemPropertyValue(itemA.getPK()), new ItemPropertyValue(itemB.getPK()) };
		access.setField(itemB, "completelydifferentvalue");
		Object oldValue = null;
		for (int i = 0; i < values.length; i++)
		{
			final Object next = values[i];
			if (access.isLegalFieldValue(next))
			{
				final Object returnedValue = access.setField(itemA, next);
				if (access.returnsOldValue())
				{
					assertEquals(oldValue, returnedValue);
				}
				assertEquals(next, access.getField(itemA));
				assertEquals("completelydifferentvalue", access.getField(itemB));
				//executeGenericSearch( type, searchQualifier, searchLocalization, next, oldValue, itemA, itemB, createDescriptor );
				oldValue = next;
			}
		}
	}

	//	protected void executeGenericSearch( ComposedTypeRemote type, String qualifier, LanguageRemote localization,
	//									Object value, Object notTheValue,
	//									ExtensibleItemRemote expected, ExtensibleItemRemote notExpected,
	//									boolean createDescriptor )
	//		throws EJBAbstractTypeException, EJBInvalidParameterException,
	//				EJBDuplicateQualifierException, EJBConsistencyCheckException, EJBDuplicateCodeException
	//	{
	//		AttributeDescriptorRemote fd = null;
	//		try
	//		{
	//			if (createDescriptor)
	//			{
	//				fd = typeManager.createPropertyDescriptor(
	//					productType,
	//					qualifier,
	//					qualifier,
	//					getValueType(value, localization!=null),
	//					Features.AttributeDescriptor.SEARCH | Features.AttributeDescriptor.REMOVE,
	//					false
	//				);
	//			}
	//
	//			final EJBSearchContext firstContext = new EJBSearchContext();
	//			firstContext.setProperty( GenericSearchConstants.DONT_NEED_TOTAL, Boolean.TRUE );
	//			firstContext.setProperty(
	//				GenericSearchConstants.CONDITIONS,
	//				value==null ?
	//					GenericSearchCondition.createIsNullCondition( qualifier, null ) :
	//					GenericSearchCondition.createEqualCondition( qualifier, value )
	//			);
	//			if (localization!=null) firstContext.setLanguage( localization );
	//			EJBSearchResult firstSearchResult = typeManager.search( type, firstContext );
	//			Collection foundItems = firstSearchResult.getResult();
	//			String msg = "found "+foundItems.toString()+" for localization=<"+localization+">, value=<"+value+">, notTheValue=<"+notTheValue+">";
	//			assertTrue( msg, foundItems.contains(expected) );
	//			assertTrue( msg, ! foundItems.contains(notExpected) );
	//
	//			final EJBSearchContext secondContext = new EJBSearchContext();
	//			secondContext.setProperty( GenericSearchConstants.DONT_NEED_TOTAL, Boolean.TRUE );
	//			secondContext.setProperty(
	//				GenericSearchConstants.CONDITIONS,
	//				notTheValue==null ?
	//					GenericSearchCondition.createIsNullCondition( qualifier ) :
	//					GenericSearchCondition.createEqualCondition( qualifier, notTheValue )
	//			);
	//			if (localization!=null) secondContext.setLanguage( localization );
	//			try
	//			{
	//				EJBSearchResult secondSearchResult = typeManager.search( type, secondContext );
	//				foundItems = secondSearchResult.getResult();
	//				msg = "found "+foundItems.toString()+" for value=<"+value+">, notTheValue=<"+notTheValue+">";
	//				assertTrue( msg, ! foundItems.contains(expected) );
	//			}
	//			catch (EJBInvalidParameterException e)
	//			{
	//				// happens if value and notTheValue have different types...
	//			}
	//		}
	//		catch (EJBInvalidParameterException e)
	//		{
	//			throw new EJBInvalidParameterException( e, "failure for value=<"+value+">, notTheValue=<"+notTheValue+">", 0 );
	//		}
	//		finally
	//		{
	//			if (fd!=null) typeManager.removeAttributeDescriptor( fd );
	//		}
	//	}

	protected TypeRemote getValueType(final Object value, final boolean localized) throws EJBDuplicateCodeException,
			EJBInvalidParameterException
	{
		TypeRemote unlocalized;
		if (value instanceof String || value == null)
		{
			unlocalized = stringType;
		}
		else if (value instanceof Integer)
		{
			unlocalized = integerType;
		}
		else if (value instanceof Collection)
		{
			unlocalized = collectionType;
		}
		/*
		 * else if (value instanceof java.awt.Color) { unlocalized = colorType; }
		 */
		else if (value instanceof Boolean)
		{
			unlocalized = booleanType;
		}
		else if (value instanceof Class)
		{
			unlocalized = classType;
		}
		else if (value instanceof ItemPropertyValue)
		{
			unlocalized = productType;
		}
		else
		{
			fail("unexpected value class: " + value.getClass().getName());
			throw new RuntimeException("just to make the compiler happy");
		}

		if (localized)
		{
			return getLocalized(unlocalized);
		}
		else
		{
			return unlocalized;
		}
	}

	protected MapTypeRemote getLocalized(final TypeRemote unlocalized) throws EJBDuplicateCodeException,
			EJBInvalidParameterException
	{
		MapTypeRemote result = (MapTypeRemote) localizedTypes.get(unlocalized);
		if (result == null)
		{
			result = typeManager.createMapType(null, "propertytest-loc-" + unlocalized.getCode(), languageType, unlocalized);
			assertNotNull(result);
			localizedTypes.put(unlocalized, result);
		}
		return result;
	}
}
