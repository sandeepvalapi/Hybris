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
package de.hybris.platform.misc;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deliveryzone.constants.ZoneDeliveryModeConstants;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * HOR-262
 */
@IntegrationTest
public class ZoneDeliveryModeTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void testZoneDeliveryModeCreateParams()
	{
		try
		{
			final Map attrs = new HashMap();
			Item zdm = null;
			attrs.put(ZoneDeliveryMode.CODE, "fooZone");
			final ComposedType type = JaloSession.getCurrentSession().getTenant().getJaloConnection().getTypeManager()
					.getComposedType(ZoneDeliveryModeConstants.ComposedTypes.ZoneDeliveryMode);

			zdm = type.newInstance(jaloSession.getSessionContext(), attrs);
			final ItemModel modelItem = modelService.create(ZoneDeliveryModeConstants.ComposedTypes.ZoneDeliveryMode);
			Assert.assertTrue(modelItem instanceof ZoneDeliveryModeModel);
			((ZoneDeliveryModeModel) modelItem).setCode("dummyZone");
			((ZoneDeliveryModeModel) modelItem).setNet(Boolean.TRUE);
			modelService.save(modelItem);
			Assert.assertEquals(zdm.getComposedType(), ((Item) modelService.getSource(modelItem)).getComposedType());

		}
		catch (final JaloGenericCreationException e)
		{
			// YTODO Auto-generated catch block
			fail(e.getMessage());
		}
		catch (final JaloAbstractTypeException e)
		{
			// YTODO Auto-generated catch block
			fail(e.getMessage());
		}
	}
}
