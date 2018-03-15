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
package de.hybris.platform.validation.engine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.validation.annotations.Dynamic;
import de.hybris.platform.validation.annotations.XorNotNull;
import de.hybris.platform.validation.constants.ValidationConstants;
import de.hybris.platform.validation.enums.ValidatorLanguage;
import de.hybris.platform.validation.extractor.impl.ConstraintGroupsToGroupsTypeConverter;
import de.hybris.platform.validation.extractor.impl.SeverityToPayloadTypeConverter;
import de.hybris.platform.validation.extractor.impl.TypeConstraintToBeanTypeConverter;
import de.hybris.platform.validation.model.constraints.DynamicConstraintModel;
import de.hybris.platform.validation.model.constraints.XorNullReferenceConstraintModel;
import de.hybris.platform.validation.pojos.PojoTwo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import org.hibernate.validator.internal.xml.BeanType;
import org.hibernate.validator.internal.xml.ClassType;
import org.hibernate.validator.internal.xml.ConstraintType;
import org.hibernate.validator.internal.xml.ElementType;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class TypeConstraintConverterTest
{
	private static final String SOME_MESSAGE_HERE = "some message here";
	private TypeConstraintToBeanTypeConverter builder;

	@Before
	public void setUp()
	{
		builder = new TypeConstraintToBeanTypeConverter();
		builder.setIgnoredAnnotationMethods(new HashSet<String>(Arrays.asList("message", "payload", "groups")));
		builder.setGroupsConverter(new ConstraintGroupsToGroupsTypeConverter());
		builder.setSeverityConverter(new SeverityToPayloadTypeConverter());
	}

	/**
	 * Builds a {@link BeanType} object for a {@link DynamicConstraintModel} object.
	 */
	@Test
	public void testDynamicAttributeHierarchyConstraintPojoParse()
	{
		final DynamicConstraintModel dynaOne = new DynamicConstraintModel();
		dynaOne.setId("one");
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(dynaOne)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		dynaOne.setActive(true);
		dynaOne.setAnnotation(Dynamic.class);
		dynaOne.setMessage("some message here");

		dynaOne.setTarget(PojoTwo.class);
		dynaOne.setLanguage(ValidatorLanguage.BEANSHELL);
		dynaOne.setConstraintGroups(Collections.EMPTY_SET);
		dynaOne.setExpression("blah some script;");

		checkDynamicModelHierarchyPojoConstraint(builder.convert(dynaOne));
	}

	@Test
	public void testCustomXorConstraintsParse() throws ClassNotFoundException
	{
		final XorNullReferenceConstraintModel xorConstraint = new XorNullReferenceConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(xorConstraint)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		xorConstraint.setId("oneProductMapping");
		xorConstraint.setAnnotation(XorNotNull.class);
		xorConstraint.setMessage(SOME_MESSAGE_HERE);
		final ComposedTypeModel unitCTypeModel = new ComposedTypeModel();
		xorConstraint.setType(unitCTypeModel);
		xorConstraint.setTarget(UnitModel.class);
		xorConstraint.setFirstFieldName("code");
		xorConstraint.setSecondFieldName("unitType");

		//check builder separately
		checkXorConstraintNotNull(builder.convert(xorConstraint));
	}

	private void checkXorConstraintNotNull(final BeanType bean)
	{
		assertEquals(UnitModel.class.getName(), bean.getClazz());
		assertTrue(bean.getGetter().isEmpty());
		assertTrue(bean.getField().isEmpty());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertNotNull(bean.getClassType());
		final ClassType classType = bean.getClassType();
		assertEquals(Boolean.FALSE, classType.getIgnoreAnnotations());
		assertTrue(classType.getConstraint().size() == 1);
		final ConstraintType constraint = classType.getConstraint().get(0);
		assertEquals(XorNotNull.class.getName(), constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
		assertTrue(constraint.getElement().size() == 2);
		if ("firstFieldName".equals(constraint.getElement().get(0).getName()))
		{
			final ElementType elementFirst = constraint.getElement().get(0);
			assertEquals("firstFieldName", elementFirst.getName());
			assertTrue(elementFirst.getContent().size() == 1);
			assertEquals("code", elementFirst.getContent().get(0));
			final ElementType elementSecond = constraint.getElement().get(1);
			assertEquals("secondFieldName", elementSecond.getName());
			assertTrue(elementSecond.getContent().size() == 1);
			assertEquals("unitType", elementSecond.getContent().get(0));
		}
		else
		{
			final ElementType elementFirst = constraint.getElement().get(1);
			assertEquals("firstFieldName", elementFirst.getName());
			assertTrue(elementFirst.getContent().size() == 1);
			assertEquals("code", elementFirst.getContent().get(0));
			final ElementType elementSecond = constraint.getElement().get(0);
			assertEquals("secondFieldName", elementSecond.getName());
			assertTrue(elementSecond.getContent().size() == 1);
			assertEquals("unitType", elementSecond.getContent().get(0));
		}

	}

	private void checkDynamicModelHierarchyPojoConstraint(final BeanType bean)
	{
		assertEquals(PojoTwo.class.getName(), bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not create field since description is not accessible directly from PojoClass class ", bean.getField()
				.size() == 0);
		assertTrue("Should not create getter for description is only accessible as inhereited method  ",
				bean.getGetter().size() == 0);

		assertNotNull(bean.getClassType());
		assertTrue("Should be only one constraint attached ", bean.getClassType().getConstraint().size() == 1);
		final ConstraintType constraint = bean.getClassType().getConstraint().get(0);
		assertEquals(Dynamic.class.getName(), constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());

		assertEquals("Two elements should be created for a constraint", 2, constraint.getElement().size());

		checkDynamicConstraint(constraint);
	}

	private void checkDynamicConstraint(final ConstraintType constraint)
	{
		if ("language".equals(constraint.getElement().get(0).getName()))
		{
			final ElementType elementFlags = constraint.getElement().get(0);
			assertEquals("language", elementFlags.getName());
			assertEquals(1, elementFlags.getContent().size());
			assertEquals(ValidatorLanguage.BEANSHELL.getCode(), elementFlags.getContent().get(0));

			final ElementType elementRegexp = constraint.getElement().get(1);
			assertEquals("expression", elementRegexp.getName());
			assertEquals(1, elementRegexp.getContent().size());
			assertEquals("blah some script;", elementRegexp.getContent().get(0));
		}
		else
		{
			final ElementType elementFlags = constraint.getElement().get(1);
			assertEquals("language", elementFlags.getName());
			assertEquals(1, elementFlags.getContent().size());
			assertEquals(ValidatorLanguage.BEANSHELL.getCode(), elementFlags.getContent().get(0));

			final ElementType elementRegexp = constraint.getElement().get(0);
			assertEquals("expression", elementRegexp.getName());
			assertEquals(1, elementRegexp.getContent().size());
			assertEquals("blah some script;", elementRegexp.getContent().get(0));
		}
	}
}
