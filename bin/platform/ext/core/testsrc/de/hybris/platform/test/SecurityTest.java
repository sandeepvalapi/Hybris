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

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SecurityTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(SecurityTest.class.getName());
	private UserRight perm;
	private Principal pcpl1, pcpl2, pcpl3;
	private UserGroup g1, g2;
	private AccessManager am;
	private UserManager um;
	TypeManager tm;
	private Principal admin;

	private static final int count = 100;

	@Before
	public void setUp() throws Exception
	{
		// set 'groups' default value back to null (changed by CoreBasicDataCreator.createBasicUserGroups())
		final ComposedType employeeT = TypeManager.getInstance().getComposedType(Employee.class);
		employeeT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);

		final ComposedType customerT = TypeManager.getInstance().getComposedType(Customer.class);
		customerT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);


		am = jaloSession.getAccessManager();
		tm = jaloSession.getTypeManager();
		// get amin to set permission
		um = jaloSession.getUserManager();
		admin = um.getAdminEmployee();

		// create some principals
		assertNotNull(pcpl1 = um.createEmployee("test.principal.1"));
		assertNotNull(pcpl2 = um.createEmployee("test.principal.2"));
		assertNotNull(pcpl3 = um.createEmployee("test.principal.3"));
		assertNotNull(g1 = um.createUserGroup("test.group1"));
		assertNotNull(g2 = um.createUserGroup("test.group2"));
		pcpl2.addToGroup(g1);
		pcpl2.addToGroup(g2);
	}

	@Test
	public void testItemsMap() throws ConsistencyCheckException
	{
		final UserRight r1, r2;
		assertNotNull(r1 = am.createUserRight("testItemsMap.r1"));
		assertNotNull(r2 = am.createUserRight("testItemsMap.r2"));
		final List userRightsList = Arrays.asList(new Object[]
		{ r1, r2 });

		final Principal p;
		assertNotNull(p = um.createCustomer("testItemsMap_Customer"));

		final Item i1, i2;
		assertNotNull(i1 = um.createTitle("testItemsMap.title1"));
		assertNotNull(i2 = um.createTitle("testItemsMap.title2"));

		assertEquals(Collections.EMPTY_MAP, p.getItemPermissionsMap(Arrays.asList(new Object[]
		{ r1, r2 })));
		/*
		 * r1 r2 i1 t - i2 - -
		 */
		i1.addPermission(p, r1, true);
		assertEquals(Collections.singletonMap(i1, Arrays.asList(new Object[]
		{ Boolean.TRUE, null })), p.getItemPermissionsMap(userRightsList));
		/*
		 * r1 r2 i1 t f i2 - -
		 */
		i1.addPermission(p, r2, false);
		assertEquals(Collections.singletonMap(i1, Arrays.asList(new Object[]
		{ Boolean.TRUE, Boolean.FALSE })), p.getItemPermissionsMap(userRightsList));
		/*
		 * r1 r2 i1 t f i2 - t
		 */
		i2.addPermission(p, r2, true);
		final Map expected = new HashMap();
		expected.put(i1, Arrays.asList(new Object[]
		{ Boolean.TRUE, Boolean.FALSE }));
		expected.put(i2, Arrays.asList(new Object[]
		{ null, Boolean.TRUE }));
		assertEquals(expected, p.getItemPermissionsMap(userRightsList));
		/*
		 * r1 r2 i1 - - i2 - t
		 */
		i1.clearPermission(p, r1);
		i1.clearPermission(p, r2);
		assertEquals(Collections.singletonMap(i2, Arrays.asList(new Object[]
		{ null, Boolean.TRUE })), p.getItemPermissionsMap(userRightsList));
		/*
		 * r1 r2 i1 - - i2 - -
		 */
		i2.clearPermission(p, r2);
		assertEquals(Collections.EMPTY_MAP, p.getItemPermissionsMap(userRightsList));

		/*
		 * using Map-Setter r1 r2 i1 - f i2 t -
		 */
		Map permissions = new HashMap();
		permissions.put(i1, Arrays.asList(new Object[]
		{ null, Boolean.FALSE }));
		permissions.put(i2, Arrays.asList(new Object[]
		{ Boolean.TRUE, null }));
		p.setItemPermissionsByMap(userRightsList, permissions);
		assertEquals(permissions, p.getItemPermissionsMap(userRightsList));

		/*
		 * using Map-Setter r1 r2 i1 t - i2 - -
		 */
		permissions = new HashMap();
		permissions.put(i1, Arrays.asList(new Object[]
		{ Boolean.TRUE, null }));
		p.setItemPermissionsByMap(userRightsList, permissions);
		assertEquals(permissions, p.getItemPermissionsMap(userRightsList));

		/*
		 * using Map-Setter r1 r2 i1 - - i2 - -
		 */
		p.setItemPermissionsByMap(userRightsList, Collections.EMPTY_MAP);
		assertEquals(Collections.EMPTY_MAP, p.getItemPermissionsMap(userRightsList));

	}

	@Test
	public void testTypedRights() throws ConsistencyCheckException, JaloSecurityException
	{
		/*
		 * setup
		 */
		final UserRight read, write;
		assertNotNull(read = am.createUserRight("typedRead"));
		assertNotNull(write = am.createUserRight("typedWrite"));
		final Principal p;
		assertNotNull(p = um.createCustomer("TypedCustomer"));
		/*
		 * test
		 */
		final ComposedType itemType = tm.getComposedType(Item.class);
		final ComposedType unitType = tm.getComposedType(Unit.class);

		final AttributeDescriptor pkAtItem = itemType.getAttributeDescriptor(Item.PK);
		final AttributeDescriptor pkAtUnit = unitType.getAttributeDescriptor(Item.PK);
		final AttributeDescriptor codeAtUnit = unitType.getAttributeDescriptor(Unit.CODE);

		assertFalse(pkAtItem.equals(pkAtUnit));
		/*
		 * check nothing set
		 */
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertFalse(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertFalse(pkAtItem.checkTypePermission(p, read));
		assertFalse(pkAtItem.checkTypePermission(p, write));
		assertFalse(pkAtUnit.checkTypePermission(p, read));
		assertFalse(pkAtUnit.checkTypePermission(p, write));
		assertFalse(codeAtUnit.checkTypePermission(p, read));
		assertFalse(codeAtUnit.checkTypePermission(p, write));
		assertEquals(Collections.EMPTY_MAP, itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, codeAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		/*
		 * set at type
		 */
		unitType.addPermission(p, read, false);
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertTrue(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertFalse(pkAtItem.checkTypePermission(p, read));
		assertFalse(pkAtItem.checkTypePermission(p, write));
		assertTrue(pkAtUnit.checkTypePermission(p, read));
		assertFalse(pkAtUnit.checkTypePermission(p, write));
		assertTrue(codeAtUnit.checkTypePermission(p, read));
		assertFalse(codeAtUnit.checkTypePermission(p, write));
		assertEquals(Collections.EMPTY_MAP, itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));

		Map settings = unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ Boolean.FALSE, null }), (List) settings.get(p));
		/*
		 * check type inheritance
		 */
		itemType.addPermission(p, write, true);
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertTrue(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertFalse(pkAtItem.checkTypePermission(p, read));
		assertFalse(pkAtItem.checkTypePermission(p, write));
		assertTrue(pkAtUnit.checkTypePermission(p, read));
		assertFalse(pkAtUnit.checkTypePermission(p, write));
		assertTrue(codeAtUnit.checkTypePermission(p, read));
		assertFalse(codeAtUnit.checkTypePermission(p, write));
		assertEquals(Collections.EMPTY_MAP, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));

		settings = unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ Boolean.FALSE, null }), (List) settings.get(p));

		settings = itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ null, Boolean.TRUE }), (List) settings.get(p));
		/*
		 * check attribute permission
		 */
		pkAtUnit.addPermission(p, write, false);
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertTrue(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertFalse(pkAtItem.checkTypePermission(p, read));
		assertFalse(pkAtItem.checkTypePermission(p, write));
		assertTrue(pkAtUnit.checkTypePermission(p, read));
		assertTrue(pkAtUnit.checkTypePermission(p, write));
		assertEquals(Collections.EMPTY_MAP, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));

		settings = unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ Boolean.FALSE, null }), (List) settings.get(p));

		settings = itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ null, Boolean.TRUE }), (List) settings.get(p));

		settings = pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ null, Boolean.FALSE }), (List) settings.get(p));
		/*
		 * check attribute inheritance
		 */
		unitType.clearPermission(p, read);
		pkAtItem.addPermission(p, read, false);
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertFalse(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertTrue(pkAtItem.checkTypePermission(p, read));
		assertFalse(pkAtItem.checkTypePermission(p, write));
		assertTrue(pkAtUnit.checkTypePermission(p, read));
		assertTrue(pkAtUnit.checkTypePermission(p, write));
		assertEquals(Collections.EMPTY_MAP, unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));

		settings = itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ null, Boolean.TRUE }), (List) settings.get(p));

		settings = pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ null, Boolean.FALSE }), (List) settings.get(p));

		settings = pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write }));
		assertCollection(Collections.singleton(p), settings.keySet());
		assertCollection(Arrays.asList(new Object[]
		{ Boolean.FALSE, null }), (List) settings.get(p));
		/*
		 * check stting via map
		 */
		itemType.clearPermission(p, read);
		itemType.clearPermission(p, write);
		pkAtItem.clearPermission(p, read);
		pkAtItem.clearPermission(p, write);
		pkAtUnit.clearPermission(p, read);
		pkAtUnit.clearPermission(p, write);
		assertEquals(Collections.EMPTY_MAP, itemType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, unitType.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertEquals(Collections.EMPTY_MAP, pkAtUnit.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));

		settings = new HashMap();
		settings.put(p, Arrays.asList(new Object[]
		{ Boolean.TRUE, Boolean.FALSE }));
		pkAtItem.setPermissionsByMap(Arrays.asList(new Object[]
		{ read, write }), settings);

		assertEquals(settings, pkAtItem.getPermissionMap(Arrays.asList(new Object[]
		{ read, write })));
		assertFalse(itemType.checkTypePermission(p, read));
		assertFalse(itemType.checkTypePermission(p, write));
		assertFalse(unitType.checkTypePermission(p, read));
		assertFalse(unitType.checkTypePermission(p, write));
		assertFalse(pkAtItem.checkTypePermission(p, read));
		assertTrue(pkAtItem.checkTypePermission(p, write));
		assertFalse(pkAtUnit.checkTypePermission(p, read));
		assertTrue(pkAtUnit.checkTypePermission(p, write));
	}

	@Test
	public void testAdminRights() throws ConsistencyCheckException
	{
		UserRight newOne;
		// create new user right -> no permission is set now
		assertNotNull(newOne = am.createUserRight("newOne"));

		final User admin = um.getAdminEmployee();
		final UserGroup adminGroup = um.getAdminUserGroup();
		User another;
		assertNotNull(another = um.createUser("another"));

		assertNotNull(admin);
		assertNotNull(adminGroup);
		// check global permissions
		assertTrue(admin.checkGlobalPermission(newOne));
		assertTrue(adminGroup.checkGlobalPermission(newOne));
		assertFalse(another.checkGlobalPermission(newOne));
		another.addToGroup(adminGroup);
		assertTrue(another.checkGlobalPermission(newOne));
		another.removeFromGroup(adminGroup);
		assertFalse(another.checkGlobalPermission(newOne));
		another.addGlobalPositivePermission(newOne);
		assertTrue(another.checkGlobalPermission(newOne));
		another.addGlobalNegativePermission(newOne);
		assertFalse(another.checkGlobalPermission(newOne));
		another.clearGlobalPermission(newOne);
		assertFalse(another.checkGlobalPermission(newOne));

		// check item permissions
		final Item it = jaloSession.getSessionContext().getLanguage();
		assertNotNull(it);
		assertTrue(it.checkPermission(admin, newOne));
		assertTrue(it.checkPermission(adminGroup, newOne));
		assertFalse(it.checkPermission(another, newOne));
		another.addToGroup(adminGroup);
		assertTrue(it.checkPermission(another, newOne));
		another.removeFromGroup(adminGroup);
		assertFalse(it.checkPermission(another, newOne));
		it.addPositivePermission(another, newOne);
		assertTrue(it.checkPermission(another, newOne));
		it.addNegativePermission(another, newOne);
		assertFalse(it.checkPermission(another, newOne));
		it.clearPermission(another, newOne);
		assertFalse(it.checkPermission(another, newOne));
	}

	@Test
	public void testCheckPermission()
	{
		try
		{
			// create test userright
			assertNotNull(perm = am.createUserRight("test.permission"));
			// set some permission (using userright-object as item :)
			//
			// item->{pcpl1+perm=-}
			am.addNegativePermissionOn(perm, admin, pcpl1, perm);
			// global->{pcpl3+perm=+}
			pcpl3.addGlobalPositivePermission(perm);
			// item->{g1+perm=-}
			am.addNegativePermissionOn(perm, admin, g1, perm);

			assertCollection(Arrays.asList(new Object[]
			{ g1, g2 }), pcpl2.getGroups());
			assertCollection(Collections.singleton(pcpl2), g1.getMembers());
			assertCollection(Collections.singleton(pcpl2), g2.getMembers());

			assertFalse("item->{pcpl1+perm=-}", am.checkPermissionOn(perm, pcpl1, perm));

			assertFalse("item->{g1+perm=-}", am.checkPermissionOn(perm, g1, perm));
			assertFalse("pcpl2->g1: item->{g1+perm=-}", am.checkPermissionOn(perm, pcpl2, perm));

			assertTrue("global->{pcpl3+perm=+}", am.checkPermissionOn(perm, pcpl3, perm));

			assertFalse("item->{g2+perm=o}", am.checkPermissionOn(perm, g2, perm));

			// test changes:

			// test group permission -> false
			pcpl3.addToGroup(g1);
			assertFalse("pcpl3->g1 : item->{g1+perm=-}", am.checkPermissionOn(perm, pcpl3, perm)); // NEGATIVE from group
			// test custom permission -> true
			am.addPositivePermissionOn(perm, admin, pcpl3, perm);
			assertTrue("item->{pcpl3+perm=+}", am.checkPermissionOn(perm, pcpl3, perm)); // POSITIVE own permission

			// item->{g1+perm=+}
			am.addPositivePermissionOn(perm, admin, g2, perm);
			assertTrue("item->(g2+perm=+)", am.checkPermissionOn(perm, g2, perm)); // POSITIVE for group
			assertFalse("pcpl2 -> {g1,g2} : item->(g1+perm=-) & item->(g2+perm=+)", am.checkPermissionOn(perm, pcpl2, perm)); // EVEN
			final Set grps = new HashSet(pcpl2.getGroups());
			grps.remove(g1);
			pcpl2.setGroups(grps);
			assertTrue("pcpl2 -> {g2} : item->(g2+perm=+)", am.checkPermissionOn(perm, pcpl2, perm)); // POSITIVE

			// performance test without group evaluation
			boolean ret = true;
			StopWatch w = new StopWatch("checking permission " + count + " times without group evaluation ...");
			for (int i = 0; i < count; i++)
			{
				ret &= am.checkPermissionOn(perm, pcpl1, perm);
			}
			w.stop();
			/* conv-log */log.debug("ret:" + ret);
			// performance test *with* group evaluation
			ret = true;
			w = new StopWatch("checking permission " + count + " times *with* group evaluation ...");
			for (int i = 0; i < count; i++)
			{
				ret &= am.checkPermissionOn(perm, pcpl2, perm);
			}
			w.stop();
			/* conv-log */log.debug("ret:" + ret);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);
			fail("error " + e);
		}
	}
}
