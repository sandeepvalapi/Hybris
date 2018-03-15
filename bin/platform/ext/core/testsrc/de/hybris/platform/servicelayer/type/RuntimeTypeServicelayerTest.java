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
package de.hybris.platform.servicelayer.type;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


/**
 * test for purpose of HOR-176
 */
@IntegrationTest
public class RuntimeTypeServicelayerTest extends ServicelayerTransactionalBaseTest
{
	private static final Logger LOG = Logger.getLogger(RuntimeTypeServicelayerTest.class);

	private static final String QUALIFIER = "someProperty";

	private static final String SOME_TYPE_NAME = "strangeType3";

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	private ComposedType myType = null;


	@After
	public void tearDown() throws ConsistencyCheckException
	{
		if (myType != null)
		{
			myType.remove();
		}
	}

	@Test
	public void testChangeComposTypeAtRuntime() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			ConsistencyCheckException, JaloDuplicateQualifierException, JaloSecurityException
	{
		//create my type as sub type of unit composed type
		final ComposedType unitT = TypeManager.getInstance().getComposedType(Unit.class);
		myType = TypeManager.getInstance().createComposedType(unitT, SOME_TYPE_NAME);

		final UnitModel modelAsUnit = modelService.create(myType.getCode());

		Assert.assertTrue(modelAsUnit != null);

		myType.remove(); // kill type again			

		//create my type as sub type of media composed type
		final ComposedType mediaT = TypeManager.getInstance().getComposedType(Media.class);
		myType = TypeManager.getInstance().createComposedType(mediaT, SOME_TYPE_NAME);

		// this causes ClassCastException since old 'MyType' is still mapped somewhere and produces UnitModel instances !!!
		final MediaModel modelAsMedia = modelService.create(myType.getCode());

		Assert.assertTrue(modelAsMedia != null);

	}

	@Test
	public void testChangeComposWithPropsTypeAtRuntime() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			ConsistencyCheckException, JaloDuplicateQualifierException, JaloSecurityException
	{
		//create my type as sub type of unit composed type
		final ComposedType unitT = TypeManager.getInstance().getComposedType(Unit.class);
		myType = TypeManager.getInstance().createComposedType(unitT, SOME_TYPE_NAME);

		final AtomicType atomicBoolean = jaloSession.getTypeManager().getRootAtomicType(Boolean.class);

		//add at runtime  some attribute
		myType.createAttributeDescriptor(QUALIFIER, atomicBoolean, AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);
		// fine here
		final UnitModel modelAsUnit = modelService.create(myType.getCode());


		Assert.assertTrue(modelAsUnit != null);

		//verify attrs	
		boolean isQualifier = false;
		for (final AttributeDescriptorModel attrModel : typeService.getAttributeDescriptors(SOME_TYPE_NAME))
		{
			LOG.info(attrModel.getQualifier());
			if (QUALIFIER.compareToIgnoreCase(attrModel.getQualifier()) == 0)
			{
				isQualifier = true;
			}
		}
		Assert.assertTrue("should have contained qualifier " + QUALIFIER, isQualifier);



		myType.remove(); // kill type again			

		//create my type as sub type of media composed type
		final ComposedType mediaT = TypeManager.getInstance().getComposedType(Media.class);
		myType = TypeManager.getInstance().createComposedType(mediaT, SOME_TYPE_NAME);

		// this causes ClassCastException since old 'MyType' is still mapped somewhere and produces UnitModel instances !!!
		final MediaModel modelAsMedia = modelService.create(myType.getCode());

		Assert.assertTrue(modelAsMedia != null);

		myType.createAttributeDescriptor(QUALIFIER, atomicBoolean, AttributeDescriptor.SEARCH_FLAG | AttributeDescriptor.WRITE_FLAG
				| AttributeDescriptor.READ_FLAG | AttributeDescriptor.REMOVE_FLAG);

		isQualifier = false;
		//verify attrs
		for (final AttributeDescriptorModel attrModel : typeService.getAttributeDescriptors(SOME_TYPE_NAME))
		{
			LOG.info(attrModel.getQualifier());
			if (QUALIFIER.compareToIgnoreCase(attrModel.getQualifier()) == 0)
			{
				isQualifier = true;
			}
		}
		Assert.assertTrue("should have contained qualifier " + QUALIFIER, isQualifier);

	}

}
