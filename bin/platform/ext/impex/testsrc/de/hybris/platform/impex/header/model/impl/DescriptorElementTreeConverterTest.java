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
package de.hybris.platform.impex.header.model.impl;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.header.model.AlternativeListElement;
import de.hybris.platform.impex.header.model.ChildrenListElement;
import de.hybris.platform.impex.header.model.DescriptorElement;
import de.hybris.platform.impex.header.model.ValueElement;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor.ColumnParams;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Test to check if conversion from old table of lists (List<ColumnParams>[]) is converted into new object oriented data
 * structure. See DescriptorElement class description.
 */
@UnitTest
public class DescriptorElementTreeConverterTest
{

	/**
	 * Test conversion of complicated structure
	 */
	@Test
	public void headerParsingTest() throws HeaderValidationException
	{
		final String pattern = "a,b|c(d|e(f),g),h";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);

		Assert.assertTrue(root instanceof AlternativeListElement);
		Assert.assertEquals(2, ((AlternativeListElement) root).getAlternatives().length);
		final DescriptorElement element = ((AlternativeListElement) root).getAlternatives()[1];
		assertNotNull("NULL!!", element);
		Assert.assertTrue(element instanceof ChildrenListElement);

		Assert.assertEquals(3, ((ChildrenListElement) element).getChildren().length);

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));

	}

	/**
	 * Just simple conversion test for two value elements
	 */
	@Test
	public void headerParsingTest1() throws HeaderValidationException
	{
		final String pattern = "a,b";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);
		Assert.assertTrue(root instanceof ChildrenListElement);
		Assert.assertEquals(2, ((ChildrenListElement) root).getChildren().length);

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}

	/**
	 * Tests more nested example
	 */
	@Test
	public void headerParsingTest2() throws HeaderValidationException
	{
		final String pattern = "a|b(c|d(e|f))";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);

		Assert.assertTrue(root instanceof AlternativeListElement);
		Assert.assertEquals(2, ((AlternativeListElement) root).getAlternatives().length);
		DescriptorElement element = ((AlternativeListElement) root).getAlternatives()[1];
		Assert.assertTrue(element instanceof ValueElement);
		element = ((ValueElement) element).getSpecifier();
		Assert.assertNotNull(element);

		Assert.assertTrue(element instanceof AlternativeListElement);
		Assert.assertEquals(2, ((AlternativeListElement) element).getAlternatives().length);
		element = ((AlternativeListElement) root).getAlternatives()[1];
		Assert.assertTrue(element instanceof ValueElement);
		element = ((ValueElement) element).getSpecifier();
		Assert.assertNotNull(element);

		Assert.assertTrue(element instanceof AlternativeListElement);
		Assert.assertEquals(2, ((AlternativeListElement) element).getAlternatives().length);
		element = ((AlternativeListElement) root).getAlternatives()[1];
		Assert.assertTrue(element instanceof ValueElement);
		element = ((ValueElement) element).getSpecifier();
		Assert.assertNotNull(element);

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}

	/**
	 * Boundary case - only one element
	 */
	@Test
	public void headerParsingTest3() throws HeaderValidationException
	{
		final String pattern = "a";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);
		Assert.assertTrue(root instanceof ValueElement);
		Assert.assertTrue(((ValueElement) root).getSpecifier() == null);
		Assert.assertEquals(((ValueElement) root).getQualifier(), "a");

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}

	/**
	 * Boundary case - only one element
	 */
	@Test
	public void headerParsingTest3a() throws HeaderValidationException
	{
		final String pattern = "a(b)";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);
		Assert.assertTrue(root instanceof ValueElement);
		Assert.assertTrue(((ValueElement) root).getSpecifier() != null);
		Assert.assertEquals(((ValueElement) root).getQualifier(), "a");

		final DescriptorElement specifier = ((ValueElement) root).getSpecifier();
		Assert.assertTrue(((ValueElement) specifier).getSpecifier() == null);
		Assert.assertEquals(((ValueElement) specifier).getQualifier(), "b");


		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}

	/**
	 * Boundary case - no elements at all
	 */
	@Test
	public void headerParsingTest4() throws HeaderValidationException
	{
		final String pattern = "";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		Assert.assertNull("Expecting NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		Assert.assertNull("Expecting NULL!!", root);

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}

	/**
	 * Just simple conversion test for value element
	 */
	@Test
	public void headerParsingTest5() throws HeaderValidationException
	{
		final String pattern = "a(b,c)";
		final List<ColumnParams>[] lists = AbstractDescriptor.extractItemPathElements(pattern);
		assertNotNull("NULL!!", lists);

		final DescriptorElement root = DescriptorElementTreeConverter.convertToDescriptorElement(lists);
		assertNotNull("NULL!!", root);

		final List<ColumnParams>[] root2 = DescriptorElementTreeConverter.convertFromDescriptorElement(root);
		Assert.assertEquals(Arrays.deepToString(lists), Arrays.deepToString(root2));
	}
}
