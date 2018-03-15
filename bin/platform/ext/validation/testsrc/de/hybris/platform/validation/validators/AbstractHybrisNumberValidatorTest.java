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
package de.hybris.platform.validation.validators;


import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Ignore;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;


/**
 * Abstract for common test for checking a validator for a big numbers and those near to 0
 */
@Ignore
abstract public class AbstractHybrisNumberValidatorTest<T extends Number, A extends java.lang.annotation.Annotation>
{
	protected ConstraintValidator<A, Number> validator = null;
	protected final ConstraintValidatorContext ctx = Mockito.mock(ConstraintValidatorContext.class);
	private String fullClassName;
	private static AtomicInteger index = new AtomicInteger(0);

	/**
	 * Value has to exact the used bean definition annotation value
	 */
	abstract protected T getBorderCaseMin();

	/**
	 * Value has to exact the used bean definition annotation value
	 */
	abstract protected T getBorderCaseMax();

	abstract protected T getValueAboveGivenMin(final T borderValue);

	abstract protected T getValueBelowGivenMin(final T borderValue);

	abstract protected T getValueAboveGivenMax(final T borderValue);

	abstract protected T getValueBelowGivenMax(final T borderValue);

	protected void assertIsNotValidValue(final T value)
	{
		Assert.assertFalse(validator.isValid(value, ctx));
	}

	protected void assertIsValidValue(final T value)
	{
		Assert.assertTrue(validator.isValid(value, ctx));
	}

	private Class<A> getAnnotationClass()
	{
		final ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<A>) (superclass).getActualTypeArguments()[1];
	}

	protected A prepareMinAnnotationInstance()
	{
		return prepareAnnotation(false);
	}

	protected A prepareMaxAnnotationInstance()
	{
		return prepareAnnotation(true);
	}

	/**
	 * Creates a annotation instance for dynamic bean with configured min or max from {@link #getBorderCaseMax()}/
	 * {@link #getBorderCaseMin()} value
	 */
	private A prepareAnnotation(final boolean max)
	{
		final Class<A> annotationClassTemplate = getAnnotationClass();
		Assert.assertNotNull(annotationClassTemplate);
		try
		{
			final ClassPool pool = ClassPool.getDefault();
			fullClassName = "de.hybris.platform.validation.validators." + this.getClass().getSimpleName() + "_"
					+ (index.incrementAndGet());
			final CtClass clazz = pool.makeClass(fullClassName);

			final AnnotationsAttribute attribute = new AnnotationsAttribute(clazz.getClassFile().getConstPool(),
					AnnotationsAttribute.visibleTag);

			final Annotation idAnnotation = new Annotation(annotationClassTemplate.getName(), clazz.getClassFile().getConstPool());

			final StringMemberValue memberValue = (StringMemberValue) idAnnotation.createMemberValue(clazz.getClassFile().getConstPool(),
					ClassPool.getDefault().get(String.class.getName()));
			memberValue.setValue(max ? getBorderCaseMax().toString() : getBorderCaseMin().toString());
			idAnnotation.addMemberValue("value", memberValue);

			attribute.addAnnotation(idAnnotation);

			final CtField field = new CtField(CtClass.longType, "field", clazz);
			field.getFieldInfo().addAttribute(attribute);
			clazz.addField(field);

			final Class dynamicBeanWithValidator = clazz.toClass();
			return ReflectionUtils.findField(dynamicBeanWithValidator, "field").getAnnotation(annotationClassTemplate);
		}
		catch (final Exception e)
		{
			Assert.fail("Unable to create a dynamic bean " + fullClassName + " for annotation " + annotationClassTemplate
					+ " and value " + (max ? getBorderCaseMax() : getBorderCaseMin()));
		}
		return null;
	}

	@After
	public void clearClassAfterTest() throws ClassNotFoundException
	{
		if (fullClassName != null)
		{
			final ClassPool pool = ClassPool.getDefault();
			final CtClass clazz = pool.getOrNull(fullClassName);
			if (clazz != null)
			{
				clazz.detach();
			}
		}
	}
}
