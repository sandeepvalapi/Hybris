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
package de.hybris.platform.impex.jalo;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.imp.ValueLine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

@UnitTest
public class ValueLineDumpTest
{
	
	@Test
	public void testValueLineDumpAfterMarkedAsProcessed()
	{
		final Map<Integer,String> csvValues = new HashMap<>();
		csvValues.put(Integer.valueOf(1), "Foo");
		csvValues.put(Integer.valueOf(3), "Bar");
		csvValues.put(Integer.valueOf(4), "");
		csvValues.put(Integer.valueOf(5), ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat");
		
		final ValueLine line = new ValueLine(null, "1", csvValues, 33, "some_location.csv");

		final PK dummyPK = PK.createFixedCounterPK(1, 1000);
		line.resolve(dummyPK, Collections.EMPTY_LIST);
		
		final Map<Integer,String> dump = line.dump();
		assertEquals(5, dump.size() );
		assertEquals("1,"+dummyPK+",,,", dump.get(ImpExReader.FIRST));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"Foo", dump.get(Integer.valueOf(1)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"Bar", dump.get(Integer.valueOf(3)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"", dump.get(Integer.valueOf(4)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat", dump.get(Integer.valueOf(5)));
	}

	@Test
	public void testValueLineDumpAfterMarkedAsProcessedWithUnresolvedValue()
	{
		final Map<Integer,String> csvValues = new HashMap<>();
		csvValues.put(Integer.valueOf(1), "Foo");
		csvValues.put(Integer.valueOf(3), "Bar");
		csvValues.put(Integer.valueOf(4), "");
		csvValues.put(Integer.valueOf(5), ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat");
		
		final ValueLine line = new ValueLine(null, "1", csvValues, 33, "some_location.csv");
		
		line.getValueEntry(3).markUnresolved("because");
		line.getValueEntry(1).resolve("whatever");
		
		final PK dummyPK = PK.createFixedCounterPK(1, 1000);
		
		line.resolve(dummyPK, Collections.EMPTY_LIST);
		
		final Map<Integer,String> dump = line.dump();
		assertEquals(5, dump.size() );
		assertEquals("1," + dummyPK + ",,,column 3: because", dump.get(ImpExReader.FIRST));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"Foo", dump.get(Integer.valueOf(1)));
		assertEquals("Bar", dump.get(Integer.valueOf(3)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"", dump.get(Integer.valueOf(4)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat", dump.get(Integer.valueOf(5)));
	}

	@Test
	public void testValueLineDumpAfterMarkedAsUnresolved()
	{
		final Map<Integer,String> csvValues = new HashMap<>();
		csvValues.put(Integer.valueOf(1), "Foo");
		csvValues.put(Integer.valueOf(3), "Bar");
		csvValues.put(Integer.valueOf(4), "");
		csvValues.put(Integer.valueOf(5), ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat");
		
		final ValueLine line = new ValueLine(null, "1", csvValues, 33, "some_location.csv");
		
		line.getValueEntry(3).markUnresolved("because");
		line.getValueEntry(1).resolve("whatever");
		
		line.markUnresolved("because I say so");
		
		final Map<Integer,String> dump = line.dump();
		assertEquals(5, dump.size() );
		assertEquals("1,,,,because I say so| column 3: because", dump.get(ImpExReader.FIRST));
		assertEquals("Foo", dump.get(Integer.valueOf(1)));
		assertEquals("Bar", dump.get(Integer.valueOf(3)));
		assertEquals("", dump.get(Integer.valueOf(4)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat", dump.get(Integer.valueOf(5)));
	}

	
	@Test
	public void testValueLineDumpBeforeMarkedAsProcessed()
	{
		final Map<Integer,String> csvValues = new HashMap<>();
		csvValues.put(Integer.valueOf(1), "Foo");
		csvValues.put(Integer.valueOf(3), "Bar");
		csvValues.put(Integer.valueOf(4), "");
		csvValues.put(Integer.valueOf(5), ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat");
		
		final ValueLine line = new ValueLine(null, "1", csvValues, 33, "some_location.csv");
		
	
		final Map<Integer,String> dump = line.dump();
		assertEquals(5, dump.size() );
		assertEquals("1,,,,", dump.get(ImpExReader.FIRST));
		assertEquals("Foo", dump.get(Integer.valueOf(1)));
		assertEquals("Bar", dump.get(Integer.valueOf(3)));
		assertEquals("", dump.get(Integer.valueOf(4)));
		assertEquals(ImpExConstants.Syntax.IGNORE_PREFIX+"ShouldIgnoreThat", dump.get(Integer.valueOf(5)));
	}

}
