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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.CoreImpExConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;



/**
 * tests behaviour of links
 *
 *
 *
 */
@IntegrationTest
public class LinkTest extends HybrisJUnit4TransactionalTest
{
	ComposedType mediaType, productType, linkType;
	Media media;
	Product source, target;
	Link link;

	@Before
	public void setUp() throws Exception
	{
		final TypeManager typeManager = jaloSession.getTypeManager();

		mediaType = typeManager.getRootComposedType(Media.class);
		productType = typeManager.getRootComposedType(Product.class);
		linkType = typeManager.getRootComposedType(Link.class);

		assertNotNull(media = jaloSession.getMediaManager().createMedia("media"));
		assertNotNull(source = jaloSession.getProductManager().createProduct("jalo.link.Source"));
		assertNotNull(target = jaloSession.getProductManager().createProduct("jalo.link.Target"));
		assertNotNull(link = jaloSession.getLinkManager().createLink("jalo.link.Link", source, target, 0, 0));

		// set 'groups' default value back to null (often changed by CoreBasicDataCreator.createBasicUserGroups())
		final ComposedType employeeT = TypeManager.getInstance().getComposedType(Employee.class);
		employeeT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);

		final ComposedType customerT = TypeManager.getInstance().getComposedType(Customer.class);
		customerT.getAttributeDescriptor(Customer.GROUPS).setDefaultValue(null);
	}

	@Test
	public void testConnection()
	{
		assertEquals(source, link.getSource());
		assertEquals(target, link.getTarget());
	}

	@Test
	public void testItemLinks()
	{
		final String quali = "testQuali";
		final Item[] items = new Item[4];
		final ProductManager productManager = jaloSession.getProductManager();
		final LinkManager linkManager = jaloSession.getLinkManager();
		assertNotNull(items[0] = productManager.createUnit("xyz", "blah1"));
		assertNotNull(items[1] = productManager.createUnit("xyz", "blah2"));
		assertNotNull(items[2] = productManager.createUnit("xyz", "blah3"));
		assertNotNull(items[3] = productManager.createUnit("xyz", "blah4"));

		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(true, quali, null));
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(false, quali, null));

		media.setLinkedItems(true, quali, null, Arrays.asList(items));
		assertEquals(Arrays.asList(items), media.getLinkedItems(true, quali, null));
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(false, quali, null));
		media.setLinkedItems(true, quali, null, Collections.EMPTY_LIST);
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(true, quali, null));
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(false, quali, null));

		media.setLinkedItems(false, quali, null, Arrays.asList(items));
		assertEquals(new HashSet(Arrays.asList(items)), new HashSet(media.getLinkedItems(false, quali, null)));
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(true, quali, null));
		media.setLinkedItems(false, quali, null, Collections.EMPTY_LIST);
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(true, quali, null));
		assertEquals(Collections.EMPTY_LIST, media.getLinkedItems(false, quali, null));

		media.addLinkedItems(true, quali, null, Collections.singletonList(items[0]));
		assertEquals(Collections.singletonList(items[0]), media.getLinkedItems(true, quali, null));
		media.addLinkedItems(true, quali, null, Collections.singletonList(items[1]), 0);
		assertEquals(ImmutableList.of(items[1], items[0]), media.getLinkedItems(true, quali, null));
		media.addLinkedItems(true, quali, null, Collections.singletonList(items[2]), -1);
		assertEquals(ImmutableList.of(items[1], items[0], items[2]), media.getLinkedItems(true, quali, null));

		Collection links = linkManager.getLinks(quali, media, Link.ANYITEM);
		assertEquals(3, links.size());
		assertEquals(ImmutableList.of(items[1], items[0], items[2]), getOrderedTargets(links));

		media.removeLinkedItems(true, quali, null, Collections.singletonList(items[0]));
		assertEquals(ImmutableList.of(items[1], items[2]), media.getLinkedItems(true, quali, null));
		links = linkManager.getLinks(quali, media, Link.ANYITEM);
		assertEquals(2, links.size());
		assertEquals(ImmutableList.of(items[1], items[2]), getOrderedTargets(links));

		media.addLinkedItems(true, quali, null, Collections.singletonList(items[0]), 1);
		links = linkManager.getLinks(quali, media, Link.ANYITEM);
		assertEquals(3, links.size());
		assertEquals(ImmutableList.of(items[1], items[0], items[2]), getOrderedTargets(links));

		media.addLinkedItems(true, quali, null, Collections.singletonList(items[3]), 0, false);
		links = linkManager.getLinks(quali, media, Link.ANYITEM);
		assertEquals(4, links.size());
		assertEquals(ImmutableList.of(items[3], items[1], items[0], items[2]), getOrderedTargets(links));
	}

	@Test
	public void testOrderings() throws ConsistencyCheckException
	{
		final UserManager userManager = UserManager.getInstance();
		final LinkManager linkManager = LinkManager.getInstance();

		final UserGroup ug1 = userManager.createUserGroup("ug1");
		final UserGroup ug2 = userManager.createUserGroup("ug2");
		final UserGroup ug3 = userManager.createUserGroup("ug3");


		assertNotNull(ug1);
		assertNotNull(ug2);
		assertNotNull(ug3);

		final Set<PrincipalGroup> defaultValue = (Set<PrincipalGroup>) TypeManager.getInstance().getComposedType(Employee.class)
				.getAttributeDescriptorIncludingPrivate(Employee.GROUPS).getDefaultValue();
		if (defaultValue != null)
		{
			final Iterable<String> dvStr = Iterables.transform(defaultValue, new Function<PrincipalGroup, String>()
			{
				@Override
				@Nullable
				public String apply(@Nullable final PrincipalGroup grp)
				{
					return grp == null ? "<null>" : grp.getUid();
				}
			});
			fail("unexpected default value Employee.groups = " + dvStr);
		}

		final Employee employee1 = userManager.createEmployee("e1");
		final Employee employee2 = userManager.createEmployee("e2");
		final Employee employee3 = userManager.createEmployee("e3");

		assertNotNull(employee1);
		assertNotNull(employee2);
		assertNotNull(employee3);

		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(ug2, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.EMPTY_LIST,
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// ug1 = [ e1, e2, e3 ]
		linkManager.setLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null,
				Arrays.asList(employee1, employee2, employee3));
		assertEquals(Arrays.asList(employee1, employee2, employee3),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// reorder ug1 = [e3, e2, e1 ]
		linkManager.setLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null,
				Arrays.asList(employee3, employee2, employee1));
		assertEquals(Arrays.asList(employee3, employee2, employee1),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// e1 -> [ ug1, ug2, ug3 ]
		linkManager.addLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null, Arrays.asList(ug2, ug3));
		assertEquals(Arrays.asList(ug1, ug2, ug3),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Arrays.asList(employee3, employee2, employee1),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug2, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// reoder e1 -> [ug2, ug3, ug1 ]
		linkManager.setLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null,
				Arrays.asList(ug2, ug3, ug1));
		assertEquals(Arrays.asList(ug2, ug3, ug1),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Arrays.asList(employee3, employee2, employee1),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug2, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// add ug3 -> [ e1, e3 ]
		linkManager.addLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null, Arrays.asList(employee3));

		assertEquals(Arrays.asList(ug2, ug3, ug1),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Arrays.asList(ug1, ug3),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		assertEquals(Arrays.asList(employee3, employee2, employee1),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug2, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Arrays.asList(employee1, employee3),
				linkManager.getLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		// remove ug1 -> [ -employee3, employee2, employee1 ]
		linkManager.removeLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null, Arrays.asList(employee3));

		assertEquals(Arrays.asList(ug2, ug3, ug1),
				linkManager.getLinkedItems(employee1, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug1),
				linkManager.getLinkedItems(employee2, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(ug3),
				linkManager.getLinkedItems(employee3, true, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));

		assertEquals(Arrays.asList(employee2, employee1),
				linkManager.getLinkedItems(ug1, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Collections.singletonList(employee1),
				linkManager.getLinkedItems(ug2, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
		assertEquals(Arrays.asList(employee1, employee3),
				linkManager.getLinkedItems(ug3, false, CoreConstants.Relations.PRINCIPALGROUPRELATION, null));
	}

	@Test
	public void testLanguage() throws ConsistencyCheckException
	{
		Link localizedLink = null;
		try
		{
			assertNull(link.getLanguage());
			assertNotNull(jaloSession.getSessionContext().getLanguage());
			localizedLink = jaloSession.getLinkManager().createLink("jalo.link.LocalizedLink",
					jaloSession.getSessionContext().getLanguage(), source, target, 0, 0);
			assertEquals(jaloSession.getSessionContext().getLanguage(), localizedLink.getLanguage());
		}
		finally
		{
			if (localizedLink != null)
			{
				localizedLink.remove();
			}
		}
	}

	//	@Ignore("pla-12421")
	@Test
	public void testSetAllLinkedItems() throws ConsistencyCheckException
	{
		final Language language1 = C2LManager.getInstance().createLanguage("l1");
		final Language language2 = C2LManager.getInstance().createLanguage("l2");
		assertNotNull(language1);
		assertNotNull(language2);

		assertEquals(Collections.EMPTY_MAP, media.getAllLinkedItems(true, "testLocLink"));

		final Map<Language, List<Media>> values = new HashMap<Language, List<Media>>();
		values.put(language1, Collections.singletonList(media));
		values.put(language2, Arrays.asList(media, media));

		media.setAllLinkedItems(true, "testLocLink", values);

		assertEquals(values, media.getAllLinkedItems(true, "testLocLink"));
		// remove with empty map -> nothing should happen
		media.setAllLinkedItems(true, "testLocLink", Collections.EMPTY_MAP);
		assertEquals(values, media.getAllLinkedItems(true, "testLocLink"));

		// now remove using language keys
		final Map<Language, List<Media>> nullValues = new HashMap<Language, List<Media>>();
		nullValues.put(language1, null);
		nullValues.put(language2, Collections.EMPTY_LIST);
		media.setAllLinkedItems(true, "testLocLink", nullValues);
		assertEquals(Collections.EMPTY_MAP, media.getAllLinkedItems(true, "testLocLink"));
	}

	@Test
	public void testFinder()
	{
		Collection found = jaloSession.getLinkManager().getLinks("jalo.link.Link", source, target);
		assertEquals(1, found.size());
		assertEquals(link, found.iterator().next());

		found = jaloSession.getLinkManager().getLinks("%", source, target);
		assertEquals(1, found.size());
		assertEquals(link, found.iterator().next());

		found = jaloSession.getLinkManager().getLinks("bla%", source, target);
		assertEquals(0, found.size());

		found = jaloSession.getLinkManager().getLinks("jalo.link.Link", Link.ANYITEM, Link.ANYITEM);
		assertEquals(1, found.size());
		assertEquals(link, found.iterator().next());

		found = jaloSession.getLinkManager().getLinks("jalo.link.Link", target, Link.ANYITEM);
		assertEquals(0, found.size());

		found = jaloSession.getLinkManager().getLinks("jalo.link.Link", source, target);
		assertEquals(1, found.size());
		assertEquals(link, found.iterator().next());

		// try link type with own deployment
		found = jaloSession.getLinkManager().getLinks("PrincipalGroupRelation", Link.ANYITEM, Link.ANYITEM);
		assertFalse(found.isEmpty());

		found = jaloSession.getLinkManager().getLinks("PrincipalGroupRelatio%", Link.ANYITEM, Link.ANYITEM);
		assertFalse(found.isEmpty());

		found = jaloSession.getLinkManager().getLinks("PrincipalGroupRelatio", Link.ANYITEM, Link.ANYITEM);
		assertTrue(found.isEmpty());

	}

	@Test
	public void testLocalizedLinks() throws ConsistencyCheckException
	{
		final Product product = ProductManager.getInstance().createProduct("plnk");
		final Media media1 = MediaManager.getInstance().createMedia("lnkM1");
		final Media media2 = MediaManager.getInstance().createMedia("lnkM2");
		final Media media3 = MediaManager.getInstance().createMedia("lnkM3");

		assertNotNull(product);
		assertNotNull(media1);
		assertNotNull(media2);
		assertNotNull(media3);

		final String QUALI = "blahFasel";

		final Language language1 = C2LManager.getInstance().createLanguage("la1");
		final Language language2 = C2LManager.getInstance().createLanguage("la2");
		final Language language3 = C2LManager.getInstance().createLanguage("la3");
		assertNotNull(language1);
		assertNotNull(language2);
		assertNotNull(language3);

		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language1));
		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language2));
		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language3));
		assertEquals(Collections.EMPTY_MAP, product.getAllLinkedItems(true, QUALI));

		// set single setters

		product.setLinkedItems(true, QUALI, language1, Collections.singletonList(media1));
		final Map<Language, List<?>> expected = new HashMap<Language, List<?>>();
		expected.put(language1, Collections.singletonList(media1));

		assertEquals(Collections.singletonList(media1), product.getLinkedItems(true, QUALI, language1));
		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language2));
		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language3));
		assertEquals(expected, product.getAllLinkedItems(true, QUALI));

		product.setLinkedItems(true, QUALI, language3, Arrays.asList(media2, media3));
		expected.put(language3, Arrays.asList(media2, media3));

		assertEquals(Collections.singletonList(media1), product.getLinkedItems(true, QUALI, language1));
		assertEquals(Collections.EMPTY_LIST, product.getLinkedItems(true, QUALI, language2));
		assertEquals(Arrays.asList(media2, media3), product.getLinkedItems(true, QUALI, language3));
		assertEquals(expected, product.getAllLinkedItems(true, QUALI));

		expected.clear();
		expected.put(language2, Arrays.asList(media3, media2, media1));
		expected.put(language3, Arrays.asList(media3));

		product.setAllLinkedItems(true, QUALI, expected);
		//since preserveHiddenLanguages is activated (since pla-12421)- l1 will not be removed in such case
		expected.put(language1, Arrays.asList(media1));
		assertEquals(expected, product.getAllLinkedItems(true, QUALI));
		assertEquals(Arrays.asList(media1), product.getLinkedItems(true, QUALI, language1));
		assertEquals(Arrays.asList(media3, media2, media1), product.getLinkedItems(true, QUALI, language2));
		assertEquals(Arrays.asList(media3), product.getLinkedItems(true, QUALI, language3));

		product.addLinkedItems(true, QUALI, language3, Collections.singletonList(media1));
		expected.put(language3, Arrays.asList(media3, media1));

		assertEquals(expected, product.getAllLinkedItems(true, QUALI));
		assertEquals(Collections.singletonList(media1), product.getLinkedItems(true, QUALI, language1));
		assertEquals(Arrays.asList(media3, media2, media1), product.getLinkedItems(true, QUALI, language2));
		assertEquals(Arrays.asList(media3, media1), product.getLinkedItems(true, QUALI, language3));

		product.removeLinkedItems(true, QUALI, language2, Collections.singletonList(media2));
		expected.put(language2, Arrays.asList(media3, media1));

		assertEquals(expected, product.getAllLinkedItems(true, QUALI));
		assertEquals(Collections.singletonList(media1), product.getLinkedItems(true, QUALI, language1));
		assertEquals(Arrays.asList(media3, media1), product.getLinkedItems(true, QUALI, language2));
		assertEquals(Arrays.asList(media3, media1), product.getLinkedItems(true, QUALI, language3));
	}

	@Test
	public void testOracleLimitWhileLinking() throws ConsistencyCheckException
	{
		if (Config.isOracleUsed())
		{
			final User user = jaloSession.getUser();
			final List<Title> testSetCollection = new ArrayList<Title>();
			final UserManager userManager = UserManager.getInstance();

			final String quali = "oracleLinkTest";

			for (int i = 0; i < 2500; i++)
			{
				testSetCollection.add(userManager.createTitle("ttt_" + i));
			}

			assertEquals(Collections.EMPTY_LIST, user.getLinkedItems(true, quali, null));

			user.setLinkedItems(true, quali, null, testSetCollection);

			assertEquals(testSetCollection, user.getLinkedItems(true, quali, null));

			user.setLinkedItems(true, quali, null, null);

			assertEquals(Collections.EMPTY_LIST, user.getLinkedItems(true, quali, null));
		}
	}

	@Test
	public void testSkipQueryExistingLinks() throws ConsistencyCheckException
	{
		final UserManager userManager = UserManager.getInstance();
		final LinkManager linkManager = LinkManager.getInstance();

		final List<Title> newItems = new ArrayList<Title>(5);
		for (int i = 0; i < 5; i++)
		{
			newItems.add(userManager.createTitle("T_" + i + "_" + System.nanoTime()));
		}

		final Title src = newItems.get(0);
		final List<Title> targets = Arrays.asList(newItems.get(1), newItems.get(2), newItems.get(3), newItems.get(4));

		jaloSession.getSessionContext().setAttribute(CoreImpExConstants.CTX_DONT_CHANGE_EXISTING_LINKS, Boolean.TRUE);

		linkManager.setLinkedItems(src, true, "dummy", null, targets);

		final List<Title> check = linkManager.getLinkedItems(src, true, "dummy", null);

		assertEquals(targets, check);
	}

	private List<Item> getOrderedTargets(final Iterable<Link> links)
	{
		final List<Link> ordered = bySequenceNumber().sortedCopy(links);
		return ImmutableList.copyOf(Iterables.transform(ordered, targetExtractor()));
	}

	private Function<Link, Item> targetExtractor()
	{
		return new Function<Link, Item>()
		{
			@Override
			public Item apply(final Link input)
			{
				return input.getTarget();
			}
		};
	}

	private Ordering<Link> bySequenceNumber()
	{
		return Ordering.from(new Comparator<Link>()
		{
			@Override
			public int compare(final Link o1, final Link o2)
			{
				return o1.getSequenceNumber() - o2.getSequenceNumber();
			}
		});
	}

}
