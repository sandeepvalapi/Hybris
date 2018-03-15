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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.daos.TypeDao;
import de.hybris.platform.servicelayer.type.impl.DefaultTypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test class that tests {@link DefaultTypeService}
 */
@UnitTest
public class DefaultTypeServiceTest
{
	private DefaultTypeService typeService;
	private ComposedTypeModel model1;
	private ComposedTypeModel model2;
	private AtomicTypeModel atomicModel1;

	private List<ComposedTypeModel> singleComposedTypeModel;
	private List<TypeModel> singleModel;
	private List<ComposedTypeModel> multiComposedTypeModel;
	private List<TypeModel> multiModel;


	@Mock
	private ModelService modelService;

	@Mock
	private TypeManager typeManager; //NOPMD

	@Mock
	private EnumerationManager enumerationManager;

	@Mock
	private TypeDao typeDao;

	@Mock
	private ConverterRegistry converterRegistry;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		typeService = new DefaultTypeService();
		typeService.setModelService(modelService);
		typeService.setEnumerationManager(enumerationManager);
		typeService.setTypeDao(typeDao);
		typeService.setConverterRegistry(converterRegistry);

		model1 = new ComposedTypeModel();
		model1.setCode("model1");
		model2 = new ComposedTypeModel();
		model2.setCode("model2");
		singleComposedTypeModel = new ArrayList<ComposedTypeModel>();
		singleComposedTypeModel.add(model1);
		singleModel = new ArrayList<TypeModel>();
		singleModel.add(model1);
		multiModel = new ArrayList<TypeModel>();
		multiModel.add(model1);
		multiModel.add(model2);
		multiComposedTypeModel = new ArrayList<ComposedTypeModel>();
		multiComposedTypeModel.add(model1);
		multiComposedTypeModel.add(model2);
		atomicModel1 = new AtomicTypeModel();
		atomicModel1.setCode("atomicModel");
	}

	@Test
	public void testFindNonExistingComposedType()
	{
		when(typeDao.findComposedTypeByCode("noCodeFound")).thenThrow(new UnknownIdentifierException(""));
		try
		{
			typeService.getComposedTypeForCode("noCodeFound");
			Assert.fail("Non existing component model found!");
		}
		catch (final UnknownIdentifierException e)
		{
			// perfectly ok
		}
	}



	@Test
	public void testFindComposedTypeForCode()
	{
		when(typeDao.findComposedTypeByCode("singleCodeFound")).thenReturn(model1);
		Assert.assertNotNull("One composed type should be found", typeService.getComposedTypeForCode("singleCodeFound"));
	}


	@Test
	public void testFindComposedCodeForNonExistingClass()
	{
		when(modelService.getModelType(Object.class)).thenReturn(null);
		try
		{
			typeService.getComposedTypeForClass(Object.class);
			Assert.fail("Non existing component model for java.lang.Object found!");
		}
		catch (final UnknownIdentifierException e)
		{
			// perfectly ok
		}
	}


	@Test
	public void testFindComposedTypeForClass()
	{
		when(modelService.getModelType(java.lang.String.class)).thenReturn("String");
		when(typeDao.findComposedTypeByCode("String")).thenReturn(model1);


		final ComposedTypeModel actualModel = typeService.getComposedTypeForCode("String");
		Assert.assertEquals("Method returned unexpected model", model1, actualModel);
	}


	@Test
	public void testFindAttributeDescriptorsForType()
	{
		final ComposedTypeModel model = mock(ComposedTypeModel.class);
		when(model.getDeclaredattributedescriptors()).thenReturn(Collections.EMPTY_LIST);
		final AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		when(model.getInheritedattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptor));

		final Set<AttributeDescriptorModel> attributeDescriptors = typeService.getAttributeDescriptorsForType(model);
		Assert.assertEquals("One attribute descriptor should be returned, got: " + attributeDescriptors.size(), 1,
				attributeDescriptors.size());
	}


	@Test
	public void testFindAtomicTypeForCode()
	{
		when(typeDao.findAtomicTypeByCode("atomicCode")).thenReturn(atomicModel1);
		final AtomicTypeModel actualModel = typeService.getAtomicTypeForCode("atomicCode");
		Assert.assertEquals("Method returned unexpected model", atomicModel1, actualModel);
	}


	@Test
	public void testGetInitialAttributeDescriptorsForType()
	{
		final ComposedTypeModel model = mock(ComposedTypeModel.class);
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setInitial(Boolean.TRUE);
		when(model.getDeclaredattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptor1));

		final AttributeDescriptorModel attributeDescriptor2 = new AttributeDescriptorModel();
		attributeDescriptor2.setInitial(Boolean.FALSE);
		when(model.getInheritedattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptor2));

		final Set<AttributeDescriptorModel> attributeDescriptors = typeService.getInitialAttributeDescriptorsForType(model);
		Assert.assertEquals("One initial attribute descriptor should be returned, got: " + attributeDescriptors.size(), 1,
				attributeDescriptors.size());
	}


	@Test
	public void testFindTypeForNonExistingCode()
	{
		when(typeDao.findTypeByCode("noCodeFound")).thenThrow(new UnknownIdentifierException("Identifier not found"));
		try
		{
			typeService.getTypeForCode("noCodeFound");
			Assert.fail("Non existing component model found!");
		}
		catch (final UnknownIdentifierException e)
		{
			// perfectly ok
		}
	}


	@Test
	public void testFindTypeForAmbiguousCode()
	{
		final List<TypeModel> multiModel = new ArrayList<TypeModel>();
		multiModel.add(model1);
		multiModel.add(model2);
		when(typeDao.findTypeByCode("multiCodeFound")).thenThrow(
				new AmbiguousIdentifierException("Ambiguous identifier multiCodeFound"));

		try
		{
			typeService.getTypeForCode("multiCodeFound");
			Assert.fail("Not failed after more than one component model found!");
		}
		catch (final AmbiguousIdentifierException e)
		{
			//perfectly ok
		}
	}


	@Test
	public void testFindEnumerationTypeForCode()
	{
		final EnumerationType enumType1 = mock(EnumerationType.class);
		final EnumerationMetaTypeModel enumModel1 = mock(EnumerationMetaTypeModel.class);

		when(enumerationManager.getEnumerationType("one")).thenReturn(enumType1);
		when(modelService.get(enumType1)).thenReturn(enumModel1);

		final EnumerationMetaTypeModel actualModel = typeService.getEnumerationTypeForCode("one");
		Assert.assertEquals("Got unexpected enumeration type model", enumModel1, actualModel);
	}


	@Test
	public void testFindMandatoryAttributes()
	{
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setModifiers(Integer.valueOf(AttributeDescriptor.WRITE_FLAG));

		final Set<String> mandatory = new HashSet<String>();
		mandatory.add("mandatory1");
		final ItemModelConverter modelConverter = mock(ItemModelConverter.class);
		when(modelConverter.getMandatoryAttributes()).thenReturn(mandatory);
		when(modelConverter.getMandatoryAttributesForCreation()).thenReturn(Collections.EMPTY_SET);

		when(converterRegistry.getModelConverterBySourceType("model1")).thenReturn(modelConverter);

		Set<String> actualMandatory = typeService.getMandatoryAttributes("model1", false);
		Assert.assertEquals("One attribute expected", 1, actualMandatory.size());
		Assert.assertEquals("Unexpected attribute got", "mandatory1", actualMandatory.iterator().next());
		actualMandatory = typeService.getMandatoryAttributes("model1", true);
		Assert.assertEquals("Zero attributes expected with force create set", 0, actualMandatory.size());
	}


	@Test
	public void testFindMandatoryAttributesWithoutModelConverter()
	{
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setModifiers(Integer.valueOf(AttributeDescriptor.WRITE_FLAG));
        attributeDescriptor1.setQualifier("foo");

		final ModelConverter nonItemModelConverter = mock(ModelConverter.class);
		final ComposedTypeModel nonItemModel = mock(ComposedTypeModel.class);
		when(converterRegistry.getModelConverterBySourceType("nonItemModel")).thenReturn(nonItemModelConverter);
		when((nonItemModel).getDeclaredattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptor1));
		when(typeDao.findComposedTypeByCode("nonItemModel")).thenReturn(nonItemModel);

		final Set<String> actualMandatory = typeService.getMandatoryAttributes("nonItemModel", false);
		Assert.assertEquals("One attribute expected", 1, actualMandatory.size());
	}


	@Test
	public void testFindUniqueAttributes()
	{
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setUnique(Boolean.TRUE);

		final Set<String> unique = new HashSet<String>();
		unique.add("unique1");
		final ItemModelConverter modelConverter = mock(ItemModelConverter.class);
		when(modelConverter.getUniqueAttributes()).thenReturn(unique);

		when(converterRegistry.getModelConverterBySourceType("model1")).thenReturn(modelConverter);
		final Set<String> actualUnique = typeService.getUniqueAttributes("model1");
		Assert.assertEquals("One attribute expected", 1, actualUnique.size());
		Assert.assertEquals("Unexpected attribute got", "unique1", actualUnique.iterator().next());
	}


	@Test
	public void testGetUniqueAttributesWithoutModelConverter()
	{
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setUnique(Boolean.TRUE);
        attributeDescriptor1.setQualifier("bar");

		final ModelConverter nonItemModelConverter = mock(ModelConverter.class);
		final ComposedTypeModel nonItemModel = mock(ComposedTypeModel.class);
		when(converterRegistry.getModelConverterBySourceType("nonItemModel")).thenReturn(nonItemModelConverter);
		when((nonItemModel).getDeclaredattributedescriptors()).thenReturn(Collections.singletonList(attributeDescriptor1));
		when(typeDao.findComposedTypeByCode("nonItemModel")).thenReturn(nonItemModel);

		final Set<String> actualUnique = typeService.getUniqueAttributes("nonItemModel");
		Assert.assertEquals("One attribute expected", 1, actualUnique.size());
	}


	@Test
	public void testFindUniqueModelRootType()
	{
		final Set<String> unique = new HashSet<String>();
		unique.add("unique1");
		final ItemModelConverter modelConverter = mock(ItemModelConverter.class);
		when(modelConverter.getUniqueAttributes()).thenReturn(unique);

		when(converterRegistry.getModelConverterBySourceType("type1")).thenReturn(modelConverter);
		when(converterRegistry.getModelConverterBySourceType("type2")).thenReturn(modelConverter);

		final ComposedTypeModel composedType1 = mock(ComposedTypeModel.class);
		final ComposedTypeModel composedType2 = mock(ComposedTypeModel.class);
		when((composedType1).getCode()).thenReturn("type1");
		when((composedType2).getCode()).thenReturn("type2");
		when((composedType2).getSuperType()).thenReturn(composedType1);
		when(typeDao.findComposedTypeByCode("type2")).thenReturn(composedType2);

		final String actualUnique = typeService.getUniqueModelRootType("type2");
		Assert.assertEquals("Got unexpected unique root type", "type1", actualUnique);
	}



	@Test
	public void testGetAttributesForModiferCriteria()
	{
		final AttributeDescriptorModel declaredDescriptor = mock(AttributeDescriptorModel.class);
		when(declaredDescriptor.getModifiers()).thenReturn(Integer.valueOf(AttributeModifiers.READ));

		final AttributeDescriptorModel privateDescriptor = mock(AttributeDescriptorModel.class);
		when(privateDescriptor.getModifiers()).thenReturn(Integer.valueOf(AttributeModifiers.READ | AttributeModifiers.PRIVATE));
		when(privateDescriptor.getPrivate()).thenReturn(Boolean.TRUE);

		final AttributeDescriptorModel inheritedDescriptor = mock(AttributeDescriptorModel.class);
		when(inheritedDescriptor.getModifiers())
				.thenReturn(Integer.valueOf(AttributeModifiers.READ | AttributeModifiers.INHERITED));

		final AttributeDescriptorModel writableDescriptor = mock(AttributeDescriptorModel.class);
		when(writableDescriptor.getModifiers()).thenReturn(Integer.valueOf(AttributeModifiers.READ | AttributeModifiers.WRITE));
		when(writableDescriptor.getWritable()).thenReturn(Boolean.TRUE);

		final AttributeDescriptorModel partofDescriptor = mock(AttributeDescriptorModel.class);
		when(partofDescriptor.getModifiers()).thenReturn(
				Integer.valueOf(AttributeModifiers.READ | AttributeModifiers.PARTOF | AttributeModifiers.WRITE));
		when(partofDescriptor.getWritable()).thenReturn(Boolean.TRUE);
		when(partofDescriptor.getPartOf()).thenReturn(Boolean.TRUE);

		final Set<AttributeDescriptorModel> declaredDescriptorModels = new HashSet<AttributeDescriptorModel>();
		declaredDescriptorModels.add(declaredDescriptor);
		declaredDescriptorModels.add(writableDescriptor);
		declaredDescriptorModels.add(partofDescriptor);

		final Set<AttributeDescriptorModel> inheritedDescriptorModels = new HashSet<AttributeDescriptorModel>();
		inheritedDescriptorModels.add(inheritedDescriptor);

		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		when((typeModel).getDeclaredattributedescriptors()).thenReturn(declaredDescriptorModels);
		when((typeModel).getInheritedattributedescriptors()).thenReturn(inheritedDescriptorModels);

		final PK descriptorPk = PK.fromLong(1234567890);
		final AttributeDescriptor privateDescriptorItem = mock(AttributeDescriptor.class);
		when(privateDescriptorItem.getPK()).thenReturn(descriptorPk);
		when(Boolean.valueOf(privateDescriptorItem.isPrivate())).thenReturn(Boolean.TRUE);

		final Set<AttributeDescriptor> privateDescriptorItems = new HashSet<AttributeDescriptor>();
		privateDescriptorItems.add(privateDescriptorItem);

		final ComposedType composedType = mock(ComposedType.class);
		when(modelService.getSource(typeModel)).thenReturn(composedType);
		when(composedType.getAttributeDescriptorsIncludingPrivate()).thenReturn(privateDescriptorItems);
		when(modelService.get(privateDescriptorItem)).thenReturn(privateDescriptor);

		when(typeDao.findComposedTypeByCode("modifierTest")).thenReturn(typeModel);

		final AttributeModifierCriteria modifierCriteria = new AttributeModifierCriteria(0, AttributeModifiers.ALL, 0);
		Assert.assertEquals("Five declared descriptors expected", 5,
				typeService.getAttributesForModifiers("modifierTest", modifierCriteria).size());

		modifierCriteria.addDisallowed(AttributeModifiers.PRIVATE);
		Assert.assertEquals("Four declared descriptors expected", 4,
				typeService.getAttributesForModifiers("modifierTest", modifierCriteria).size());

		modifierCriteria.addDisallowed(AttributeModifiers.INHERITED);
		Assert.assertEquals("Three declared descriptors expected", 3,
				typeService.getAttributesForModifiers("modifierTest", modifierCriteria).size());

		modifierCriteria.addRequired(AttributeModifiers.WRITE);
		Assert.assertEquals("Two declared descriptors expected", 2,
				typeService.getAttributesForModifiers("modifierTest", modifierCriteria).size());

		modifierCriteria.addRequired(AttributeModifiers.PARTOF);
		Assert.assertEquals("One declared descriptor expected", 1,
				typeService.getAttributesForModifiers("modifierTest", modifierCriteria).size());
	}
}
