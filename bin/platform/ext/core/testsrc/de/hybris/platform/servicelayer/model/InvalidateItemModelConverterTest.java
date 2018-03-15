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
package de.hybris.platform.servicelayer.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.InvalidateModelConverterRegistryEvent;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Checks the invalidation of internal cache of {@link ItemModelConverter}.
 */
@IntegrationTest
public class InvalidateItemModelConverterTest extends ServicelayerTransactionalBaseTest
{
	private ComposedType unitType;
	private ItemModelConverter unitConverter;

	@Resource
	private ConverterRegistry defaultConverterRegistry;

	@Resource
	private EventService eventService;

	@Before
	public void setUp()
	{
		unitType = TypeManager.getInstance().getComposedType(Unit.class);

		unitConverter = (ItemModelConverter) defaultConverterRegistry.getModelConverterBySourceType(UnitModel._TYPECODE);
	}

	@After
	public void tearDown()
	{
		simulateAdUpdateEvent();
	}

	/**
	 * Modifies attribute Unit.code by setting it to non-writable. Internal converter attribute info has to show this
	 * change.
	 */
	@Test
	public void testAttributeModification()
	{
		final AttributeDescriptor codeAttr = unitType.getAttributeDescriptor(Unit.CODE);
		codeAttr.setInitial(false);
		simulateAdUpdateEvent();

		assertTrue("Unit.code is not writable?", unitConverter.getInfo(UnitModel.CODE).getAttributeInfo().isWritable());
		codeAttr.setWritable(false);
		simulateAdUpdateEvent();
		assertFalse("Converter cache was not invalidated on attribute change", unitConverter.getInfo(UnitModel.CODE)
				.getAttributeInfo().isWritable());
	}

	/**
	 * Removes Unit.conversion. Internal converter attribute info has to show this change.
	 */
	@Test
	public void testAttributeRemoval() throws JaloDuplicateQualifierException, ConsistencyCheckException
	{
		final AttributeDescriptor convAttr = unitType.getAttributeDescriptor(Unit.CONVERSION);

		assertNotNull("Unit.conversion attribute not existent?", unitConverter.getInfo(UnitModel.CONVERSION));
		convAttr.remove();
		assertNull("Converter cache was not invalidated on attribute remove", unitConverter.getInfo(UnitModel.CONVERSION));
	}

	/**
	 * First removes Unit.conversion and then adds it again. Internal converter attribute info has to show this change.
	 * Removal first is needed because the model class needs to have the field, otherwise it will not be added to cache.
	 */
	@Test
	public void testAttributeCreation() throws JaloDuplicateQualifierException, ConsistencyCheckException
	{
		AttributeDescriptor convAttr = unitType.getAttributeDescriptor(Unit.CONVERSION);
		assertNotNull("Unit.conversion attribute not existent?", unitConverter.getInfo(UnitModel.CONVERSION));
		convAttr.remove();
		simulateAdUpdateEvent();
		assertNull("Converter cache was not invalidated on attribute remove", unitConverter.getInfo(UnitModel.CONVERSION));

		convAttr = unitType.createAttributeDescriptor(Unit.CONVERSION,
				(Type) TypeManager.getInstance().getAtomicTypesForJavaClass(Double.class).iterator().next(),
				AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG);
		simulateAdUpdateEvent();
		assertNotNull("Converter cache was not invalidated on attribute creation", unitConverter.getInfo(UnitModel.CONVERSION));
	}

	// This is necessary now since this test runs inside a transaction and the (global) converter
	// cache is NOT being updated inside the transaction!
	private void simulateAdUpdateEvent()
	{
		final InvalidateModelConverterRegistryEvent evt = new InvalidateModelConverterRegistryEvent();
		evt.setRefresh(true);
		eventService.publishEvent(evt);
	}
}
