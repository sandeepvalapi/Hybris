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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.validation.constants.ValidationConstants;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToBeanTypeConverter;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToFieldTypeConverter;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToGetterTypeConverter;
import de.hybris.platform.validation.extractor.impl.ConstraintGroupsToGroupsTypeConverter;
import de.hybris.platform.validation.extractor.impl.SeverityToPayloadTypeConverter;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.PatternConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;
import de.hybris.platform.validation.pojos.PojoTwo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.hibernate.validator.internal.xml.BeanType;
import org.hibernate.validator.internal.xml.ConstraintType;
import org.hibernate.validator.internal.xml.ElementType;
import org.hibernate.validator.internal.xml.FieldType;
import org.hibernate.validator.internal.xml.GetterType;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AttributeConstraintConverterTest
{
	private static final String SOME_MESSAGE_HERE = "some message here";
	private AttributeConstraintToBeanTypeConverter builder;

	@Before
	public void setUp()
	{
		builder = new AttributeConstraintToBeanTypeConverter();
		builder.setIgnoredAnnotationMethods(new HashSet<String>(Arrays.asList("message", "payload", "groups")));
		builder.setGroupsConverter(new ConstraintGroupsToGroupsTypeConverter());
		builder.setSeverityConverter(new SeverityToPayloadTypeConverter());
		builder.setGetterConverter(new AttributeConstraintToGetterTypeConverter());
		builder.setFieldConverter(new AttributeConstraintToFieldTypeConverter());
	}

	@Test
	public void testSimpleAttributeHierarchyConstraintPojoParse()
	{
		final NullConstraintModel attributeOne = new NullConstraintModel();
		attributeOne.setId("one");
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(attributeOne)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		attributeOne.setActive(true);
		attributeOne.setAnnotation(javax.validation.constraints.Null.class);
		attributeOne.setMessage("some message here");
		attributeOne.setTarget(PojoTwo.class);
		attributeOne.setQualifier("pojoOnePrivate");
		attributeOne.setConstraintGroups(Collections.EMPTY_SET);

		checkSingleModelHierarchyPojoConstraint(builder.convert(attributeOne));
	}

	/**
	 * Parsing pojo configuration case.
	 */
	@Test
	public void testSimplePojoConstraintParse()
	{
		final PatternConstraintModel patternConstraint = new PatternConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(patternConstraint)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		patternConstraint.setId("threePojoMapping");
		patternConstraint.setAnnotation(javax.validation.constraints.Pattern.class);
		patternConstraint.setRegexp(".*");
		patternConstraint.setActive(true);
		patternConstraint.setFlags(Collections.singleton(de.hybris.platform.validation.enums.RegexpFlag.DOTALL));
		patternConstraint.setTarget(PojoTwo.class);
		patternConstraint.setQualifier("pojoTwoPrivate");
		patternConstraint.setConstraintGroups(Collections.EMPTY_SET);

		checkSinglePatternPojoConstraint(builder.convert(patternConstraint));
	}

	@Test
	public void testSimplePojoConstraintFromSuperParse()
	{
		final PatternConstraintModel patternConstraint = new PatternConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(patternConstraint)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		patternConstraint.setId("threePojoMapping");
		patternConstraint.setAnnotation(javax.validation.constraints.Pattern.class);
		patternConstraint.setRegexp(".*");
		patternConstraint.setActive(true);
		patternConstraint.setFlags(Collections.singleton(de.hybris.platform.validation.enums.RegexpFlag.DOTALL));
		patternConstraint.setTarget(PojoTwo.class);
		patternConstraint.setQualifier("pojoOnePrivate");
		patternConstraint.setMessage("some message here");
		patternConstraint.setConstraintGroups(Collections.EMPTY_SET);

		checkSinglePatternPojoSuperConstraint(builder.convert(patternConstraint));
	}

	/**
	 * Parsing one constraint for the model case.
	 */
	@Test
	public void testSimpleAttributeConstraintParse()
	{
		final NullConstraintModel attributeOne = new NullConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(attributeOne)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		attributeOne.setId("one");
		attributeOne.setAnnotation(javax.validation.constraints.Null.class);
		attributeOne.setMessage(SOME_MESSAGE_HERE);
		attributeOne.setSeverity(Severity.ERROR);
		attributeOne.setTarget(ProductModel.class);
		attributeOne.setQualifier("code");

		checkSingleModelConstraint(builder.convert(attributeOne));
	}

	@Test
	public void testSimpleAttributeHierarchyConstraintParse()
	{
		final NullConstraintModel attributeOne = new NullConstraintModel();
		attributeOne.setId("one");
		attributeOne.setAnnotation(javax.validation.constraints.Null.class);
		attributeOne.setSeverity(Severity.ERROR);
		attributeOne.setTarget(CustomerModel.class);
		attributeOne.setQualifier("description");

		checkSingleModelHierarchyConstraint(builder.convert(attributeOne));
	}

	/**
	 * parsing two constraints for the same model case
	 */
	@Test
	public void testProductTwoConstraintsParse() throws ClassNotFoundException
	{
		final NotNullConstraintModel attributeOne = new NotNullConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(attributeOne)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		attributeOne.setId("oneProductMapping");
		attributeOne.setAnnotation(javax.validation.constraints.NotNull.class);
		attributeOne.setTarget(ProductModel.class);
		attributeOne.setQualifier("code");

		final SizeConstraintModel attributeTwo = new SizeConstraintModel();
		((ItemModelContextImpl) ModelContextUtils.getItemModelContext(attributeTwo)).setLocaleProvider(new StubLocaleProvider(
				Locale.ENGLISH));
		attributeTwo.setId("twoProductMapping");
		attributeTwo.setAnnotation(javax.validation.constraints.Size.class);
		attributeTwo.setMin(Long.valueOf(10));
		attributeTwo.setMax(Long.valueOf(20));
		attributeTwo.setMessage("some {1} other message {2} with mapping");
		attributeTwo.setTarget(ProductModel.class);
		attributeTwo.setQualifier("code");

		checkDoubleModelConstraintNotNull(builder.convert(attributeOne));
		checkDoubleModelConstraintSize(builder.convert(attributeTwo));
	}

	private void checkDoubleModelConstraintNotNull(final BeanType bean)
	{
		assertEquals("de.hybris.platform.core.model.product.ProductModel", bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue(bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("code", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());
		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals(NotNull.class.getName(), constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
	}

	private void checkDoubleModelConstraintSize(final BeanType bean)
	{
		assertEquals("de.hybris.platform.core.model.product.ProductModel", bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not build field for the Product#code", bean.getField().size() == 0);
		assertTrue(bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("code", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());
		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals(Size.class.getName(), constraint.getAnnotation());
		//assertEquals("some {1} other message {2} with mapping", constraint.getMessage());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
		assertTrue(constraint.getElement().size() == 2);
		final ElementType elementMin = constraint.getElement().get(0);
		assertEquals("min", elementMin.getName());
		assertTrue(elementMin.getContent().size() == 1);
		assertEquals("10", elementMin.getContent().get(0));
		final ElementType elementMax = constraint.getElement().get(1);
		assertEquals("max", elementMax.getName());
		assertTrue(elementMax.getContent().size() == 1);
		assertEquals("20", elementMax.getContent().get(0));

	}

	/**
	 * Validates created bean type for a qualifier for a attribute "description" which is not accessible with in
	 * {@link CustomerModel} directly.
	 */
	private void checkSingleModelHierarchyConstraint(final BeanType bean)
	{
		assertEquals(CustomerModel.class.getName(), bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not create field since description is not accessible directly from customermodel class ", bean
				.getField().size() == 0);
		assertTrue("Should not create getter for  description is only accessible as inhereited method  ",
				bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("description", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());

		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals(Null.class.getName(), constraint.getAnnotation());
		//assertEquals(SOME_MESSAGE_HERE, constraint.getMessage());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
		assertNotNull("Should assign severity", constraint.getPayload());
		assertNotNull("Should assign severity value", constraint.getPayload().getValue());
		assertEquals("Should assign one severity", 1, constraint.getPayload().getValue().size());
		final String payload = constraint.getPayload().getValue().get(0);
		assertEquals("JAXB element should have value as Error ", de.hybris.platform.validation.payloads.Error.class.getName(),
				payload);
	}

	/**
	 * Validates created bean type for a qualifier for a attribute "code" which is accessible with in
	 * {@link ProductModel} directly.
	 */
	private void checkSingleModelConstraint(final BeanType bean)
	{
		assertEquals("de.hybris.platform.core.model.product.ProductModel", bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not create fields ", bean.getField().size() == 0);
		assertTrue("Should  create getter ", bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("code", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());

		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals(Null.class.getName(), constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());

		assertNotNull("Should assign severity", constraint.getPayload());
		assertNotNull("Should assign severity value", constraint.getPayload().getValue());
		assertEquals("Should assign one severity", 1, constraint.getPayload().getValue().size());
		final String payload = constraint.getPayload().getValue().get(0);
		assertEquals("JAXB element should have value as Error ", de.hybris.platform.validation.payloads.Error.class.getName(),
				payload);
	}

	private void checkSinglePatternPojoSuperConstraint(final BeanType bean)
	{
		assertEquals(PojoTwo.class.getName(), bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not create field since superName is not accessible directly from PojoClass class ", bean.getField()
				.size() == 0);
		assertTrue("Should  create getter for superName is only accessible as inhereited method  ", bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("pojoOnePrivate", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());

		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals("javax.validation.constraints.Pattern", constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
		assertTrue("Elemet for pattern constraint has to have 2 elements ", constraint.getElement().size() == 2);

		checkPatternConstraint(constraint);
	}

	private void checkPatternConstraint(final ConstraintType constraint)
	{
		if ("flags".equals(constraint.getElement().get(0).getName()))
		{
			final ElementType elementFlags = constraint.getElement().get(0);
			assertEquals("flags", elementFlags.getName());
			assertEquals(1, elementFlags.getContent().size());
			assertEquals(de.hybris.platform.validation.enums.RegexpFlag.DOTALL.getCode(), elementFlags.getContent().get(0));

			final ElementType elementRegexp = constraint.getElement().get(1);
			assertEquals("regexp", elementRegexp.getName());
			assertEquals(1, elementRegexp.getContent().size());
			assertEquals(".*", elementRegexp.getContent().get(0));
		}
		else
		{
			final ElementType elementFlags = constraint.getElement().get(1);
			assertEquals("flags", elementFlags.getName());
			assertEquals(1, elementFlags.getContent().size());
			assertEquals(de.hybris.platform.validation.enums.RegexpFlag.DOTALL.getCode(), elementFlags.getContent().get(0));

			final ElementType elementRegexp = constraint.getElement().get(0);
			assertEquals("regexp", elementRegexp.getName());
			assertEquals(1, elementRegexp.getContent().size());
			assertEquals(".*", elementRegexp.getContent().get(0));
		}
	}

	private void checkSinglePatternPojoConstraint(final BeanType bean)
	{
		assertEquals(PojoTwo.class.getName(), bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should  create field since name is accessible directly from PojoClass class ", bean.getField().size() == 1);
		assertTrue("Should not create getter for name is only accessible as inhereited method  ", bean.getGetter().size() == 0);
		final FieldType field = bean.getField().get(0);
		assertEquals("pojoTwoPrivate", field.getName());
		assertEquals(Boolean.FALSE, field.getIgnoreAnnotations());

		assertTrue(field.getConstraint().size() == 1);
		final ConstraintType constraint = field.getConstraint().get(0);
		assertEquals("javax.validation.constraints.Pattern", constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());

		checkPatternConstraint(constraint);
	}

	/**
	 * Validates created bean type for a qualifier for a attribute "description" which is not accessible with in
	 * {@link CustomerModel} directly.
	 */
	private void checkSingleModelHierarchyPojoConstraint(final BeanType bean)
	{
		assertEquals(PojoTwo.class.getName(), bean.getClazz());
		assertEquals(Boolean.FALSE, bean.getIgnoreAnnotations());
		assertTrue("Should not create field since description is not accessible directly from PojoClass class ", bean.getField()
				.size() == 0);
		assertTrue("Should create getter for description is only accessible as inhereited method  ", bean.getGetter().size() == 1);
		final GetterType getter = bean.getGetter().get(0);
		assertEquals("pojoOnePrivate", getter.getName());
		assertEquals(Boolean.FALSE, getter.getIgnoreAnnotations());

		assertTrue(getter.getConstraint().size() == 1);
		final ConstraintType constraint = getter.getConstraint().get(0);
		assertEquals("javax.validation.constraints.Null", constraint.getAnnotation());
		assertEquals(String.format("{%s.%s}", constraint.getAnnotation(), ValidationConstants.ANNOTATION_MESSAGE_METHOD),
				constraint.getMessage());
	}
}
