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
package de.hybris.platform.util;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class ASCIITableReportTest
{
	@Test
	public void shouldAllowToPutSameValuesForManyCollumns() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder().withTopHeaders("Col1", "Col2", "Col3");
		builder.addDataRow("Foo", "Foo", "Foo").titledBy("Same values for all collumns 1");
		builder.addDataRow("Foo", "Foo", "Foo").titledBy("Same values for all collumns 2");

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo(
				"+--------------------------------+------+------+------+\n"
						+ "|                -               | Col1 | Col2 | Col3 |\n"
						+ "+--------------------------------+------+------+------+\n"
						+ "| Same values for all collumns 1 |  Foo |  Foo |  Foo |\n"
						+ "| Same values for all collumns 2 |  Foo |  Foo |  Foo |\n"
						+ "+--------------------------------+------+------+------+\n");
	}

	@Test
	public void shouldAllowToPutSameTopHeadersForManyCollumns() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder().withTopHeaders("Col", "Col", "Col");
		builder.addDataRow("Moo Moo", "Meow Meow", "Oink Oink").titledBy("Row title");

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo(
				"+-----------+---------+-----------+-----------+\n" + "|     -     |   Col   |    Col    |    Col    |\n"
						+ "+-----------+---------+-----------+-----------+\n" + "| Row title | Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+-----------+---------+-----------+-----------+\n");
	}

	@Test
	public void shouldPrintTableWithoutRowTitlesEvenIfTheyAreSetExplicite() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder().withTopHeaders("Col 1", "Col 2", "Col 3");
		builder.addDataRow("Moo Moo", "Meow Meow", "Oink Oink").titledBy("Row");
		builder.disableRowTitles();

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo(
				"+---------+-----------+-----------+\n" + "|  Col 1  |   Col 2   |   Col 3   |\n"
						+ "+---------+-----------+-----------+\n" + "| Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+---------+-----------+-----------+\n");
	}

	@Test
	public void shouldPrintTableWithoutTopColumnHeaders() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder();
		builder.addDataRow("Moo Moo", "Meow Meow", "Oink Oink");

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo(
				"+---------+-----------+-----------+\n" + "| Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+---------+-----------+-----------+\n");
	}

	@Test
	public void shouldPrintTableWithoutTopColumnHeadersButWithRowTitles() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder();
		builder.addDataRow("Moo Moo", "Meow Meow", "Oink Oink").titledBy("Row title");

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo(
				"+-----------+---------+-----------+-----------+\n" + "| Row title | Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+-----------+---------+-----------+-----------+\n");
	}

	@Test
	public void shouldAllowToDisableAndEnableRowTitleOnTheSameBuilderInstance() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder().withTopHeaders("Col1", "Col2", "Col3");
		builder.addDataRow("Moo Moo", "Meow Meow", "Oink Oink").titledBy("Row title");

		// when
		builder.disableRowTitles();
		final String tableString1 = builder.getTable();
		builder.enableRowTitles();
		final String tableString2 = builder.getTable();

		// then
		assertThat(tableString1).isEqualTo(
				"+---------+-----------+-----------+\n" + "|   Col1  |    Col2   |    Col3   |\n"
						+ "+---------+-----------+-----------+\n" + "| Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+---------+-----------+-----------+\n");
		assertThat(tableString2).isEqualTo(
				"+-----------+---------+-----------+-----------+\n" + "|     -     |   Col1  |    Col2   |    Col3   |\n"
						+ "+-----------+---------+-----------+-----------+\n" + "| Row title | Moo Moo | Meow Meow | Oink Oink |\n"
						+ "+-----------+---------+-----------+-----------+\n");
	}

	@Test
	public void shouldNotPrintTableWhenNoDataIsSpecified() throws Exception
	{
		// given
		final ASCIITableReport builder = ASCIITableReport.builder();

		// when
		final String tableString = builder.getTable();

		// then
		assertThat(tableString).isEqualTo("No data specified. Report disabled.");
	}

}
