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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Performs an import test which checks the performance of the import process compared to programmatically import.
 */
@PerformanceTest
public class ImpExImportPerformanceTest extends AbstractImpExTest
{
	/** Used logger instance. */
	private static final Logger LOG = Logger.getLogger(ImpExImportPerformanceTest.class);

	/** How many products must be created. */
	private static final int COUNT = 1000;

	/** How many products must be created at warmup. */
	private static final int WARMUP_COUNT = 10;

	/** Allowed percent difference between impex import without key cache and jalo creation. */
	private static final double MAX_DIFFERENCE_PERCENT = 400.0;

	/**
	 * Performance test which creates {@link #COUNT} items programmatically, per import and per import.
	 */
	@Test
	public void testPerf()
	{
		// warm up
		LOG.info("-------------------------------------------------");
		LOG.info("--- ImpEx INSERT Performance Test");
		LOG.info("-------------------------------------------------");
		LOG.info("---  warmup...");
		createItemsJalo(WARMUP_COUNT, 0);
		importItems(WARMUP_COUNT, WARMUP_COUNT);
		LOG.info("-------------------------------------------------");

		// first: import, then create 
		LOG.info("---  importing " + COUNT + " items ...");
		final long timeImport1 = importItems(COUNT, 0 * COUNT + 2 * WARMUP_COUNT);
		LOG.info("---   imported " + COUNT + " items in " + timeImport1 + " ms");

		LOG.info("---  creating " + COUNT + " items...");
		final long timeCreate1 = createItemsJalo(COUNT, 1 * COUNT + 2 * WARMUP_COUNT);
		LOG.info("---   created " + COUNT + " items in " + timeCreate1 + " ms");

		LOG.info("-------------------------------------------------");
		LOG.info("---  warmup again...");
		createItemsJalo(WARMUP_COUNT, 2 * COUNT + 2 * WARMUP_COUNT);
		importItems(WARMUP_COUNT, 2 * COUNT + 3 * WARMUP_COUNT);
		LOG.info("-------------------------------------------------");

		LOG.info("---  importing " + COUNT + " items again...");
		final long timeImport2 = importItems(COUNT, 2 * COUNT + 4 * WARMUP_COUNT);
		LOG.info("---   imported " + COUNT + " items in " + timeImport2 + " ms");

		LOG.info("---  creating " + COUNT + " items again...");
		final long timeCreate2 = createItemsJalo(COUNT, 3 * COUNT + 4 * WARMUP_COUNT);
		LOG.info("---   created " + COUNT + " items in " + timeCreate2 + " ms");


		final long timeImport = timeImport1 + timeImport2;
		final long timeCreate = timeCreate1 + timeCreate2;

		final double diffPercentage = 100d * (timeImport - timeCreate) / timeCreate;

		LOG.info("-------------------------------------------------");
		LOG.info("--- jalo layer time :   " + timeCreate + " ms for " + (2 * COUNT) + " creations");
		LOG.info("--- import time:        " + timeImport + " ms for " + (2 * COUNT) + " creations");
		LOG.info("--- difference:         " + ((int) diffPercentage) + "% ( limit is " + MAX_DIFFERENCE_PERCENT + "% )");
		LOG.info("-------------------------------------------------");

		assertTrue("difference between jalo creation and import of " + ((int) diffPercentage) + "% " + "exceeds limit of "
				+ MAX_DIFFERENCE_PERCENT + "% ", (diffPercentage < 0 || diffPercentage < MAX_DIFFERENCE_PERCENT));
	}

	/**
	 * Creates unit items programmatically.
	 * 
	 * @param count
	 *           amount of items to create
	 * @param offset
	 *           number offset for naming the items
	 * @return creation time for all items
	 */
	protected long createItemsJalo(final int count, final int offset)
	{
		final ComposedType composedType = TypeManager.getInstance().getComposedType(Unit.class);
		final SessionContext ctx = jaloSession.createSessionContext();
		ctx.setLanguage(null);
		final long time1 = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
		{
			final Map attributes = new HashMap();
			attributes.put(Unit.CODE, "ImpexPerfUnit" + (i + offset));
			attributes.put(Unit.UNITTYPE, "ImpexPerfGrp");
			attributes.put(Unit.CONVERSION, new Double(i + offset));
			final Map names = new HashMap();
			names.put(german, "name_de_" + (i + offset));
			names.put(english, "name_en_" + (i + offset));

			try
			{
				// now create
				composedType.newInstance(attributes);
			}
			catch (final JaloGenericCreationException e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
			catch (final JaloAbstractTypeException e)
			{
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
		final long time2 = System.currentTimeMillis();
		return time2 - time1;
	}

	/**
	 * Creates unit items using impex import.
	 * 
	 * @param count
	 *           amount of items to create
	 * @param offset
	 *           number offset for naming the items
	 * @return time for all items
	 */
	protected long importItems(final int count, final int offset)
	{
		final String header = "INSERT " + TypeManager.getInstance().getComposedType(Unit.class).getCode() + ";" + Unit.CODE + ";"
				+ Unit.UNITTYPE + ";" + Unit.CONVERSION + ";" + Unit.NAME + "[lang=" + german.getIsoCode() + "];" + Unit.NAME
				+ "[lang=" + english.getIsoCode() + "]";

		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(header).append("\n");
		for (int i = 0; i < count; i++)
		{
			stringBuilder.append(";").append("ImpexPerfUnitX").append(i + offset).append(";");
			stringBuilder.append("ImpexPerfGrp").append(";");
			stringBuilder.append(i).append(";");
			stringBuilder.append("name_de_").append(i + offset).append(";");
			stringBuilder.append("name_en_").append(i + offset).append("\n");
		}

		try
		{
			final ImpExImportReader impExImportReader = new ImpExImportReader(stringBuilder.toString());
			// read header without time stamp
			impExImportReader.readLine();
			final long time1 = System.currentTimeMillis();
			for (Object line = impExImportReader.readLine(); line != null; line = impExImportReader.readLine())
			{
				// logic in loop header
			}

			final long time2 = System.currentTimeMillis();
			assertEquals(count, impExImportReader.getValueLineCount());
			assertEquals(0, impExImportReader.getDumpedLineCount());

			return time2 - time1;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			fail(e.getMessage());
			return 0;
		}
	}
}
