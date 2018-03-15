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
package de.hybris.platform.persistence;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.testframework.TestUtils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


// The test shows the problems associated with marking the attributes as INITIAL from core types(that are still not fully migrated)
@IntegrationTest
public class InitialAttributePersistenceTest extends HybrisJUnit4TransactionalTest
{
	public final static String ATTRIBUTE_NAME = "myCountry";

	private ComposedType countryType;
	private Country country;

	@Before
	public void setUp() throws Exception
	{
		TestUtils.disableFileAnalyzer("log error expected for all methods");
		countryType = TypeManager.getInstance().getComposedType(Country.class);
		country = C2LManager.getInstance().createCountry("pl");
	}


	@Before
	public void tearDown() throws Exception
	{
		TestUtils.enableFileAnalyzer();
	}

	/**
	 * User type is INITIAL aware.
	 */
	@Test
	public void testUserFixed() throws JaloGenericCreationException, JaloAbstractTypeException, JaloDuplicateQualifierException,
			JaloInvalidParameterException, ConsistencyCheckException, JaloSecurityException, JaloDuplicateCodeException
	{
		final ComposedType userType = TypeManager.getInstance().getComposedType(User.class);
		final ComposedType myUserType = TypeManager.getInstance().createComposedType(userType, "MyTestUser");
		myUserType.setJaloClass(MyTestUser.class);

		myUserType.createAttributeDescriptor(ATTRIBUTE_NAME, countryType, AttributeDescriptor.INITIAL_FLAG
				| AttributeDescriptor.PROPERTY_FLAG | AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);

		final Map params = new HashMap();
		params.put(User.UID, "MyTestUser1");
		params.put(ATTRIBUTE_NAME, country);
		final MyTestUser myTestUser = (MyTestUser) myUserType.newInstance(params);

		// the 'uid' attribute which is derived directly from User type
		assertNotNull("The 'UID' attribute of the MyTestUser can not be null!", myTestUser.getUID());

		// 1) myCountry is marked as a INITIAL attribute within MyTestUser#createItem method
		// 2) the User type and all subtypes currently support INITIAL flag and the 'myCountry' attribute is included in INSERT SQL statement (CORE-18, PLA-10479)
		// 3) the User type extends GenericItem and thus the 'myCountry' attribute is excluded from the UPDATE SQL statement.
		assertNotNull("The 'myCountry' attribute of the MyTestUser can not be null!", myTestUser.getAttribute(ATTRIBUTE_NAME));
	}

	/**
	 * Unit type is NOT INITIAL aware.
	 */
	@Test
	public void testUnitStillNotFixed() throws JaloDuplicateQualifierException, JaloInvalidParameterException,
			JaloGenericCreationException, JaloAbstractTypeException, JaloSecurityException, JaloDuplicateCodeException
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final ComposedType myUnitType = TypeManager.getInstance().createComposedType(unitType, "MyTestUnit");
		myUnitType.setJaloClass(MyTestUnit.class);

		myUnitType.createAttributeDescriptor(ATTRIBUTE_NAME, countryType, AttributeDescriptor.INITIAL_FLAG
				| AttributeDescriptor.PROPERTY_FLAG | AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);

		final Map params = new HashMap();
		params.put(Unit.CODE, "MyTestUnit1");
		params.put(Unit.UNITTYPE, "Type1");
		params.put(ATTRIBUTE_NAME, country);

		final MyTestUnit myTestUnit = (MyTestUnit) myUnitType.newInstance(params);

		// the 'code' attribute which is derived directly from User type
		assertNotNull("The 'code' attribute of the MyTestUnit can not be null!", myTestUnit.getCode());

		// 1) myCountry is marked as a INITIAL attribute within MyTestUnit#createItem method
		// 2) the Unit type and all subtypes DO NOT support INITIAL flag and the 'myCountry' attribute IS NOT
		//    included in INSERT SQL statement (Still to do!)
		// 3) the Unit type extends only LocalizableItem and thus the 'myCountry' attribute is NOT excluded
		//    from the UPDATE SQL statement AND this entails that it does not return null AND it seems that everything works well...
		assertNotNull("The 'myCountry' attribute of the MyTestUnit can not be null!", myTestUnit.getAttribute(ATTRIBUTE_NAME));
	}

	/**
	 * Language type is NOT INITIAL aware.
	 * 
	 * PLA-10479 solves the null value problem. Within GenericItem#getInitialProperties and
	 * GenericItem#getNonInitialAttributes the markInitialPropertyFetch() feature is used which ensures that within all
	 * core types the null problem not exists.
	 */
	@Test
	public void testLanguageStillNotFixed() throws JaloDuplicateQualifierException, JaloInvalidParameterException,
			JaloGenericCreationException, JaloAbstractTypeException, JaloSecurityException, JaloDuplicateCodeException
	{
		final ComposedType languageType = TypeManager.getInstance().getComposedType(Language.class);
		final ComposedType myLanguageType = TypeManager.getInstance().createComposedType(languageType, "MyTestLanguage");
		myLanguageType.setJaloClass(MyTestLanguage.class);

		myLanguageType.createAttributeDescriptor(ATTRIBUTE_NAME, countryType, AttributeDescriptor.INITIAL_FLAG
				| AttributeDescriptor.PROPERTY_FLAG | AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);

		final Map params = new HashMap();
		params.put(Language.ISOCODE, "MyTestLanguage1");
		params.put(ATTRIBUTE_NAME, country);

		final MyTestLanguage myTestUnit = (MyTestLanguage) myLanguageType.newInstance(params);

		// the 'isocode' attribute which is derived directly from User type
		assertNotNull("The 'isocode' attribute of the MyTestLanguage can not be null!", myTestUnit.getIsoCode());

		// 1) myCountry is marked as a INITIAL attribute within MyTestLanguage#createItem method
		// 2) the Language type and all subtypes DO NOT support INITIAL flag and the 'myCountry' attribute IS NOT
		//    included in INSERT SQL statement (Still to do!)
		// 3) the Language type extends GenericItem:
		//    a) (before PLA-10479 fix: and the initial values will be excluded from the UPDATE process
		//        and as a result of these steps the null value is returned!);
		//	   b) (after PLA-10479 fix: the initial values will BE included in UPDATE process)
		assertNotNull("The 'myCountry' attribute of the MyTestLanguage can not be null!", myTestUnit.getAttribute(ATTRIBUTE_NAME));
	}
}
