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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Test;


/**
 *
 */
@IntegrationTest
public class LinkModelTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	@Resource
	UserService userService;

	@Test
	public void testPlainLinkCreationAsModel()
	{
		// we need a item to link from/to
		final ItemModel item1 = userService.getAdminUser();
		final ItemModel item2 = userService.getAnonymousUser();

		final LinkModel link = modelService.create(LinkModel.class);
		link.setSource(item1);
		link.setTarget(item2);
		link.setQualifier("FooBar");
		modelService.save(link);

		assertTrue(modelService.isUpToDate(link));
		assertEquals("FooBar", link.getQualifier());
		assertEquals(item1, link.getSource());
		assertEquals(item2, link.getTarget());

		final Collection<LinkModel> links = getLinks(item1, item2, "FooBar");
		assertEquals(1, links.size());
		assertEquals(Collections.singletonList(link), links);

		final Collection<LinkModel> noLinks = getLinks(item2, item1, "FooBar");
		assertEquals(Collections.emptyList(), noLinks);
	}

	@Test
	public void testRelationLinkCreationAsModel()
	{
		final PrincipalModel principal1 = modelService.create(CustomerModel.class);
		principal1.setUid("u1");
		final PrincipalModel principal2 = modelService.create(CustomerModel.class);
		principal2.setUid("u2");
		final PrincipalModel principal3 = modelService.create(CustomerModel.class);
		principal3.setUid("u3");
		final PrincipalGroupModel group1 = modelService.create(UserGroupModel.class);
		group1.setUid("grp1");
		final PrincipalGroupModel group2 = modelService.create(UserGroupModel.class);
		group2.setUid("grp2");
		modelService.saveAll(principal1, principal2, principal3, group1, group2);

		// PrincipalGroupRelation:
		// u1<->grp1 
		// u2<->grp1 
		// u3<->grp2 
		// u2<->grp2 
		final LinkModel u1g1 = createTypedLink("PrincipalGroupRelation", principal1, group1, null, null);
		final LinkModel u2g1 = createTypedLink("PrincipalGroupRelation", principal2, group1, null, null);
		final LinkModel u3g2 = createTypedLink("PrincipalGroupRelation", principal3, group2, null, null);
		final LinkModel u2g2 = createTypedLink("PrincipalGroupRelation", principal2, group2, null, null);
		modelService.saveAll(u1g1, u2g1, u3g2, u2g2);

		assertLink(u1g1, "PrincipalGroupRelation", "PrincipalGroupRelation", principal1, group1, null, null);
		assertLink(u2g1, "PrincipalGroupRelation", "PrincipalGroupRelation", principal2, group1, null, null);
		assertLink(u3g2, "PrincipalGroupRelation", "PrincipalGroupRelation", principal3, group2, null, null);
		assertLink(u2g2, "PrincipalGroupRelation", "PrincipalGroupRelation", principal2, group2, null, null);

		modelService.refresh(principal1);
		modelService.refresh(principal2);
		modelService.refresh(principal3);
		modelService.refresh(group1);
		modelService.refresh(group2);

		assertEquals(new HashSet<>(Arrays.asList(principal1, principal2)), group1.getMembers());
		assertEquals(new HashSet<>(Arrays.asList(principal3, principal2)), group2.getMembers());
		assertEquals(new HashSet<>(Arrays.asList(group1)), principal1.getGroups());
		assertEquals(new HashSet<>(Arrays.asList(group1, group2)), principal2.getGroups());
		assertEquals(new HashSet<>(Arrays.asList(group2)), principal3.getGroups());
	}

	protected void assertLink(final LinkModel link, final String type, final String qualifier, final ItemModel src,
			final ItemModel tgt, final Integer seq, final Integer rSeq)
	{
		assertEquals(type, link.getItemtype());
		assertEquals(qualifier, link.getQualifier());
		assertEquals(src, link.getSource());
		assertEquals(tgt, link.getTarget());
		if (seq != null)
		{
			assertEquals(seq, link.getSequenceNumber());
		}
		if (rSeq != null)
		{
			assertEquals(rSeq, link.getReverseSequenceNumber());
		}
	}

	protected LinkModel createTypedLink(final String type, final ItemModel src, final ItemModel tgt, final Integer seq,
			final Integer rSeq)
	{
		final LinkModel link = modelService.create(type);
		link.setSource(src);
		link.setTarget(tgt);
		if (seq != null)
		{
			link.setSequenceNumber(seq);
		}
		if (rSeq != null)
		{
			link.setReverseSequenceNumber(rSeq);
		}
		return link;
	}

	protected Collection<LinkModel> getLinks(final ItemModel src, final ItemModel tgt, final String qualifier)
	{
		return modelService.toModelLayer(LinkManager.getInstance().getLinks(qualifier, (Item) modelService.getSource(src),
				(Item) modelService.getSource(tgt)));
	}
}
