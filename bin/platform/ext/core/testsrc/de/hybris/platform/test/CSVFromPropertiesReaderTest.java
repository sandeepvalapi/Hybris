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
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.CSVFromPropertiesReader;

import java.io.StringReader;
import java.util.Map;

import org.junit.Test;


/*
 * Had to make this integration test since CSVReader internally starts the platform, which makes this test fail otherwise.
 */
@IntegrationTest
public class CSVFromPropertiesReaderTest extends HybrisJUnit4Test
{

	@Test
	public void testMappingIgnoringUnknownIgnoringCase()
	{
		final String de_props = "product01.name=product01.name.de\n" + //
				"product01.description=product01.description.de\n" + //
				"product02.name=product02.name.de\n" + //
				"product02.description=product02.description.de\n" + //
				"product03.name=product03.name.de\n" + //
				"product03.bar=product03.bar.de\n" + //
				"product03.description=product03.description.de\n" + //
				"product04.NAME=product04.name.de\n" + //
				"product04.description=product04.description.de\n" + //
				"product05.DescRiption=product05.description.de\n" + //
				"someCompleteMessWhichDoesntMatch=whatever\n" + //
				"product05.foo=product05.foo.de\n" + //
				"pattern.with.way.to.many.dots=juhu\n" + //
				"product06.name=product06.name.de\n";

		final CSVFromPropertiesReader fakeCSVReader = CSVFromPropertiesReader.builder(new StringReader(de_props))
				.withIdAndAttributePattern("^\\s*([^\\.]+)\\.([^\\.]+)\\s*$").withColumn("name", 1).withColumn("description", 2)
				.withLineBufferOf(2).withMissingValueToken("<ignore>").build();

		assertTrue(fakeCSVReader.readNextLine());
		Map<Integer, String> line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product01", line.get(Integer.valueOf(0)));
		assertEquals("product01.name.de", line.get(Integer.valueOf(1)));
		assertEquals("product01.description.de", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product02", line.get(Integer.valueOf(0)));
		assertEquals("product02.name.de", line.get(Integer.valueOf(1)));
		assertEquals("product02.description.de", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product03", line.get(Integer.valueOf(0)));
		assertEquals("product03.name.de", line.get(Integer.valueOf(1)));
		assertEquals("product03.description.de", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product04", line.get(Integer.valueOf(0)));
		assertEquals("product04.name.de", line.get(Integer.valueOf(1)));
		assertEquals("product04.description.de", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product05", line.get(Integer.valueOf(0)));
		assertEquals("<ignore>", line.get(Integer.valueOf(1)));
		assertEquals("product05.description.de", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product06", line.get(Integer.valueOf(0)));
		assertEquals("product06.name.de", line.get(Integer.valueOf(1)));
		assertEquals("<ignore>", line.get(Integer.valueOf(2)));

		assertFalse(fakeCSVReader.readNextLine());
	}

	@Test
	public void testBufferUntilCompleteAndDrainAfterEOF()
	{
		// 1) We want line XXX to be buffered until the last property is read!
		// Note that all lines are incomplete so that they'll be buffered.
		final String de_props = "product01.name=product01.name.de\n" + //
				"product02.name=product02.name.de\n" + //
				"product03.name=product03.name.de\n" + //
				"product03.bar=product03.bar.de\n" + //
				"product01.description=product01.description.de\n"; // missing 'description' comes here -> line complete

		final CSVFromPropertiesReader fakeCSVReader = CSVFromPropertiesReader.builder(new StringReader(de_props))
				.withIdAndAttributePattern("([^\\.]+)\\.(.*)").withColumn("name", 1).withColumn("description", 2).withLineBufferOf(3)
				.withMissingValueToken("<ignore>").build();

		// 'XXX' is the only one which became complete -> need to be returned first

		assertTrue(fakeCSVReader.readNextLine());
		Map<Integer, String> line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product01", line.get(Integer.valueOf(0)));
		assertEquals("product01.name.de", line.get(Integer.valueOf(1)));
		assertEquals("product01.description.de", line.get(Integer.valueOf(2)));

		// since all other lines are still incomplete (description missing) they must be returned in the order of first occurrence

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product02", line.get(Integer.valueOf(0)));
		assertEquals("product02.name.de", line.get(Integer.valueOf(1)));
		assertEquals("<ignore>", line.get(Integer.valueOf(2)));

		assertTrue(fakeCSVReader.readNextLine());
		line = fakeCSVReader.getLine();
		assertNotNull(line);
		assertEquals(3, line.size());
		assertEquals("product03", line.get(Integer.valueOf(0)));
		assertEquals("product03.name.de", line.get(Integer.valueOf(1)));
		assertEquals("<ignore>", line.get(Integer.valueOf(2)));

		assertFalse(fakeCSVReader.readNextLine());
	}


	@Test
	public void testEmpty()
	{
		final CSVFromPropertiesReader fakeCSVReader = CSVFromPropertiesReader.builder(new StringReader(""))
				.withIdAndAttributePattern("([^\\.]+)\\.(.*)").withColumn("name", 1).withColumn("description", 2).withLineBufferOf(3)
				.withMissingValueToken("<ignore>").build();

		assertFalse(fakeCSVReader.readNextLine());
	}

	@Test
	public void testNoMatchesAtAll()
	{
		final String de_props = "product03.bar=product03.bar.de\n" + //
				"someCompleteMessWhichDoesntMatch=whatever\n" + //
				"product05.foo=product05.foo.de\n" + //
				"\n\n\n" + //
				"# some comment \n" + //
				"##### another comment \n" + //
				"pattern.with.way.to.many.dots=juhu\n";

		final CSVFromPropertiesReader fakeCSVReader = CSVFromPropertiesReader.builder(new StringReader(de_props))
				.withIdAndAttributePattern("([^\\.]+)\\.(.*)").withColumn("name", 1).withColumn("description", 2).withLineBufferOf(3)
				.withMissingValueToken("<ignore>").build();

		assertFalse(fakeCSVReader.readNextLine());
	}


}
