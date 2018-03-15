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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class TypeServiceTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;


	//getAtomicTypeForCode(String)
	//getAtomicType(String) - deprecated, but is the same as getAtomicTypeForCode, therfore not testing
	@Test
	public void testGetAtomicTypeForCode()
	{
		//not existing atomic type
		try
		{
			typeService.getAtomicTypeForCode("notExisting");
			fail("expected UnknownIdentifierException here");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//existing atomic type
		final AtomicTypeModel type = typeService.getAtomicTypeForCode("java.lang.String");
		assertNotNull(type);
		assertEquals("Typecode should be equal to: java.lang.String", "java.lang.String", type.getCode());


		//existing type, but not an atomic type
		try
		{
			typeService.getAtomicTypeForCode("Product");
			fail("expected UnknownIdentifierException here");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//existing atomic type, all in lower case
		final AtomicTypeModel type2 = typeService.getAtomicTypeForCode("java.math.biginteger");
		assertNotNull(type2);
		assertEquals("Typecode should be equal to: java.math.BigInteger", "java.math.BigInteger", type2.getCode());

		//null value
		try
		{
			typeService.getAtomicTypeForCode(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}


	//getAtomicTypeForJavaClass(Class)
	@Test
	public void testGetAtomicTypeForJavaClass()
	{
		//null value
		try
		{
			typeService.getAtomicTypeForJavaClass(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//existing atomic type
		final AtomicTypeModel type = typeService.getAtomicTypeForJavaClass(String.class);
		assertNotNull(type);
		assertEquals("Typecode should be equal to: java.lang.String", "java.lang.String", type.getCode());

		//existing type, but not an atomic type
		try
		{
			typeService.getAtomicTypeForJavaClass(Unit.class);
			fail("expected UnknownIdentifierException here");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}


	//getComposedType(String) - deprecated, not tested, same as below
	//getComposedTypeForCode(String)
	@Test
	public void testGetComposedTypeForCode()
	{
		//null
		try
		{
			typeService.getComposedTypeForCode(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//composedtypes are no models, same as non-existing
		try
		{
			typeService.getComposedTypeForCode("ProductModel");
			fail("should not found anything here!");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//existing composedtype

		final ComposedTypeModel type = typeService.getComposedTypeForCode("Product");
		assertNotNull(type);
		assertEquals("Product", type.getCode());

		//existing type but no composed type
		try
		{
			typeService.getComposedTypeForCode("java.lang.Integer");
			fail("should not found anything here!");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//lower/upper case
		final ComposedTypeModel type2 = typeService.getComposedTypeForCode("UNIT");
		assertNotNull(type2);
		assertEquals("Unit", type2.getCode());
	}


	//getComposedType(Class) - deprecated, not tested, same as below
	//getComposedTypeForClass(Class)
	@Test
	public void getComposedTypeForClass()
	{

		//null
		try
		{
			typeService.getComposedTypeForClass(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//java class
		try
		{
			typeService.getComposedTypeForClass(String.class);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//composedtype class
		try
		{
			typeService.getComposedTypeForClass(Product.class);
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//a modelclass
		final ComposedTypeModel comptype2 = typeService.getComposedTypeForClass(ProductModel.class);
		assertNotNull(comptype2);
		assertEquals("Product", comptype2.getCode());
	}


	//getType(String) - deprecated, not tested, same as below
	//getTypeForCode(String)
	@Test
	public void testGetTypeForCode()
	{
		//null
		try
		{
			typeService.getTypeForCode(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//existing type
		final TypeModel type = typeService.getTypeForCode("vaRiAnTtype");
		assertNotNull(type);
		assertEquals("VariantType", type.getCode());


		//non-existing type
		try
		{
			typeService.getTypeForCode("java.lang.IllegalArgumentException");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}


	//getAttributeDescriptor(ComposedTypeModel, String)
	//getAttributeDescriptor(String, String)
	@Test
	public void testGetAttributeDescriptor()
	{
		//null
		try
		{
			typeService.getAttributeDescriptor(typeService.getComposedTypeForClass(ProductModel.class), null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		// unknown composedtype
		try
		{
			typeService.getAttributeDescriptor("String", "String");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//known composedtype + unknown attribute
		try
		{
			typeService.getAttributeDescriptor("Product", "");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
		try
		{
			typeService.getAttributeDescriptor("Product", "xxx");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//known composedtype + known attribute
		final AttributeDescriptorModel attrDesc = typeService.getAttributeDescriptor("Product", "code");
		assertNotNull(attrDesc);
		assertEquals("code", attrDesc.getQualifier());
		assertEquals("Product", attrDesc.getEnclosingType().getCode());

		//not persisted models
		//XXX TODO - refactor business code here, a JaloItemNotFoundException shouldn't be thrown here... 
		//		final ComposedTypeModel ctm = new ComposedTypeModel();
		//		ctm.setCode("xxx");
		//		typeService.getAttributeDescriptor(ctm, "yyy");

		final AttributeDescriptorModel attrDesc1 = typeService.getAttributeDescriptor(
				typeService.getComposedTypeForClass(ProductModel.class), "name");
		assertNotNull(attrDesc1);
		assertEquals("name", attrDesc1.getQualifier());
		assertEquals("Product", attrDesc1.getEnclosingType().getCode());
	}

	@Test
	public void testIsAssignableFrom()
	{
		//null TODO XXX is this correct?!?
		assertFalse(typeService.isAssignableFrom((TypeModel) null, null));
		assertFalse(typeService.isAssignableFrom((String) null, null));


		final ComposedType productType = TypeManager.getInstance().getComposedType(Product.class);
		final ComposedType localizableItemType = TypeManager.getInstance().getComposedType(LocalizableItem.class);

		final ComposedTypeModel productTypeModel = modelService.get(productType);
		final ComposedTypeModel localizableItemTypeModel = modelService.get(localizableItemType);

		assertFalse(productType.isAssignableFrom(localizableItemType));
		assertTrue(localizableItemType.isAssignableFrom(productType));
		assertFalse(localizableItemType.isAssignableFrom(null));

		assertEquals(productType.isAssignableFrom(localizableItemType),
				typeService.isAssignableFrom(productTypeModel, localizableItemTypeModel));
		assertEquals(localizableItemType.isAssignableFrom(productType),
				typeService.isAssignableFrom(localizableItemTypeModel, productTypeModel));
		assertEquals(productType.isAssignableFrom(null), typeService.isAssignableFrom(productTypeModel, null));

		assertEquals(productType.isAssignableFrom(localizableItemType), typeService.isAssignableFrom("Product", "LocalizableItem"));
		assertEquals(localizableItemType.isAssignableFrom(productType), typeService.isAssignableFrom("LocalizableItem", "Product"));
		assertEquals(productType.isAssignableFrom(null), typeService.isAssignableFrom("Product", null));

		assertFalse(typeService.isAssignableFrom(null, "Product"));
		assertTrue(typeService.isAssignableFrom("Item", "VariantProduct"));
		assertTrue(typeService.isAssignableFrom("Item", "Product"));
		assertTrue(typeService.isAssignableFrom("Item", "Item"));

	}

	//isInstance(TypeModel, Object)
	@Test
	public void testIsInstance() throws Exception
	{
		//null
		//TODO XXX this is false here!!!!! typeService.isInstance(null, null) should be true?!?
		//assertFalse(typeService.isInstance(null, null));
		assertFalse(typeService.isInstance(null, new Object()));



		final ComposedType userType = TypeManager.getInstance().getRootComposedType(Constants.TC.User);
		final ComposedType employeeType = TypeManager.getInstance().getComposedType(Employee.class);

		final ComposedTypeModel userModel = modelService.get(userType);
		final ComposedTypeModel employeeModel = modelService.get(employeeType);

		Employee employee;
		assertNotNull(employee = jaloSession.getUserManager().createEmployee("usertypetest"));

		assertTrue(userType.isInstance(employee));
		assertTrue(employeeType.isInstance(employee));
		assertFalse(employeeType.isInstance(null));
		assertEquals(userType.isInstance(employee), typeService.isInstance(userModel, employee));
		assertEquals(employeeType.isInstance(employee), typeService.isInstance(employeeModel, employee));
		assertEquals(employeeType.isInstance(null), typeService.isInstance(employeeModel, null));

		assertFalse(typeService.isInstance(userModel, typeService.getEnumerationTypeForCode("ArticleApprovalStatus")));

		//TODO XXX refactor business code here! IllegalStateException should not be thrown!
		//typeService.isInstance(new TypeModel(), new Object());
	}

	//getEnumerationType(String)
	//getEnumerationTypeForCode(String)
	@Test
	public void testGetEnumerationTypeForCode()
	{
		assertNotNull(typeService.getEnumerationTypeForCode("ArticleApprovalStatus"));
		assertNotNull(typeService.getEnumerationTypeForCode("articleapprovalstatus"));
		assertNotNull(typeService.getEnumerationTypeForCode("ARTICLEAPPROVALSTATUS"));

		//null
		try
		{
			typeService.getEnumerationTypeForCode(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//empty string
		try
		{
			typeService.getEnumerationTypeForCode("");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

	}


	//getModelClass(ComposedTypeModel)
	//getModelClass(String)
	@Test
	public void testGetModelClass()
	{
		//XXX TODO JaloItemNotFound exception here ?!?!?!?!?
		//		final ComposedTypeModel ctm = new ComposedTypeModel();
		//		ctm.setCode("xxx");
		//		typeService.getModelClass(ctm);

		assertEquals(ProductModel.class, typeService.getModelClass(typeService.getComposedTypeForClass(ProductModel.class)));
		assertEquals(ArticleApprovalStatus.class,
				typeService.getModelClass(typeService.getComposedTypeForClass(ArticleApprovalStatus.class)));
		assertEquals(ArticleApprovalStatus.class, typeService.getModelClass("ArticleApprovalStatus"));
		assertEquals(ProductModel.class, typeService.getModelClass("Product"));

		//null
		try
		{
			typeService.getModelClass((ComposedTypeModel) null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		try
		{
			typeService.getModelClass((String) null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//XXX TODO JaloItemNotFound exception here ?!?!?!?!?
		//typeService.getModelClass("");

	}


	//hasAttribute(ComposedTypeModel, String)
	@Test
	public void testHasAttribute()
	{
		//null
		try
		{
			typeService.hasAttribute(null, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		assertFalse(typeService.hasAttribute(typeService.getComposedTypeForClass(ProductModel.class), null));
		assertFalse(typeService.hasAttribute(typeService.getComposedTypeForClass(ProductModel.class), ""));
		assertFalse(typeService.hasAttribute(typeService.getComposedTypeForClass(ProductModel.class), "xxx"));
		assertTrue(typeService.hasAttribute(typeService.getComposedTypeForClass(ProductModel.class), "code"));

		//TODO XXX throws a NPE?
		//typeService.hasAttribute(new ComposedTypeModel(), "xxx");

		//TODO XXX throws a JaloItemNotFoundException
		//		final ComposedTypeModel ctm = new ComposedTypeModel();
		//		ctm.setCode("xxx");
		//		typeService.hasAttribute(ctm, "yyy");
	}


	//getEnumerationValue(String, String)
	//getEnumerationValue(HybrisEnumValue)
	@Test
	public void testGetEnumerationValue()
	{
		//null
		try
		{
			typeService.getEnumerationValue(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
		try
		{
			typeService.getEnumerationValue(null, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//hybris enum value
		final EnumerationValueModel enumvalue = typeService.getEnumerationValue(ArticleApprovalStatus.APPROVED);
		assertNotNull(enumvalue);
		assertEquals(ArticleApprovalStatus.APPROVED.getCode(), enumvalue.getCode());

		//string value
		try
		{
			typeService.getEnumerationValue("", "");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		final EnumerationValueModel enumvalue1 = typeService.getEnumerationValue("ArticleApprovalStatus", "approved");
		final EnumerationValueModel enumvalue2 = typeService.getEnumerationValue("ARTICLEApprovalStatus", "APProVED");
		assertNotNull(enumvalue1);
		assertNotNull(enumvalue2);
		assertEquals(ArticleApprovalStatus.APPROVED.getCode(), enumvalue1.getCode());
		assertEquals(ArticleApprovalStatus.APPROVED.getCode(), enumvalue2.getCode());


		try
		{
			typeService.getEnumerationValue("ArticleApprovalStatus", "xxx");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		try
		{
			typeService.getEnumerationValue("yyy", "xxx");
			fail("UnknownIdentifierException expected");
		}
		catch (final UnknownIdentifierException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}

	//getUniqueAttributes(String)
	@Test
	public void testGetUniqueAttributes()
	{
		//null
		try
		{
			typeService.getUniqueAttributes(null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			//fine
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		//TODO XXX JaloItemNotFoundException here!!!!
		//typeService.getUniqueAttributes("xxx");

		//using itemmodelconverter
		final Set<String> uniqueColl = typeService.getUniqueAttributes("CatalogVersion");
		assertEquals(2, uniqueColl.size());
		assertTrue(uniqueColl.contains("version"));
		assertTrue(uniqueColl.contains("catalog"));


		//using enumerationvaluemodelconverter
		final EnumerationValueModel enumvalue = typeService.getEnumerationValue("ArticleApprovalStatus", "approved");
		assertNotNull(enumvalue);
		final Set<String> uniqueColl2 = typeService.getUniqueAttributes(enumvalue.getItemtype());
		assertEquals(2, uniqueColl2.size());
		assertTrue(uniqueColl2.contains("code"));
		assertTrue(uniqueColl2.contains("itemtype"));

		assertTrue(typeService.getUniqueAttributes("PriceRow").isEmpty());

	}

	//getMandatoryAttributes(String, boolean)
	//	@Test
	//	public void testGetMandatoryAttributes()
	//	{
	//		//TODO test this
	//		typeService.getMandatoryAttributes(type, true);
	//		typeService.getMandatoryAttributes(type, false);
	//	}


	//getAttributeDescriptors(ComposedTypeModel) -deprecated, not testes 
	//getAttributeDescriptorsForType(ComposedTypeModel)
	//getAttributeDescriptors(String) -deprecated, not testes 
	//	@Test
	//	public void testGetAttributeDescriptorsForType()
	//	{
	//		// TODO test this 
	//	}

	//getInitialAttributeDescriptorsForType(ComposedTypeModel)
	//	public void testGetInitialAttributeDescriptorsForType()
	//	{
	//		//TODO test this
	//		typeService.getInitialAttributeDescriptorsForType(composedTypeModel)
	//	}


	//getUniqueModelRootType(String)
	//	@Test
	//	public void testGetUniqueModelRootType()
	//	{
	//		//TODO test this
	//		typeService.getUniqueModelRootType(composedtypecode)
	//	}


	//getDefaultValues(String)
	//	@Test
	//	public void testGetDefaultValues()
	//	{
	//		//TODO test this
	//		typeService.getDefaultValues(type)
	//	}

	//getDefaultValues(String, Collection<String>)
	//	@Test
	//	public void testGetDefaultValues()
	//	{
	//		//TODO test this
	//		typeService.getDefaultValues(type, attributes)
	//	}

	//getAttributesForModifiers(String, AttributeModifierCriteria)
	//	@Test
	//	public void testGetAttributesForModifiers()
	//	{
	//		//TODO test this
	//		typeService.getAttributesForModifiers(composedTypeCode, criteria)
	//	}
}
