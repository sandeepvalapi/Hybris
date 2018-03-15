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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportJob;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author ag
 */
@IntegrationTest
public class ImpExMultThreadedTest extends HybrisJUnit4Test
{
	private final static Logger LOG = Logger.getLogger(ImpExMultThreadedTest.class);

	private static final int LINES = 10;
	private static final int MAX = 3000;
	private static final int MULTI_FACTOR = 4;
	private static final int MAX3 = MAX / 3;

	private StringBuilder buffer1;
	private StringBuilder buffer2;
	private StringBuilder buffer3;
	private ComposedType varType;

	private String legacyModeBackup;

	@Before
	public void setUp() throws JaloInvalidParameterException, JaloDuplicateCodeException, JaloItemNotFoundException
	{
		legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");

		getOrCreateLanguage("de");
		getOrCreateLanguage("en");

		// --------------------------------------------------------
		// --- Products for run 1
		// --------------------------------------------------------

		buffer1 = new StringBuilder();

		buffer1.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + "); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		for (int i = 0; i < MAX; i++)
		{
			buffer1.append("; product-1-").append(i).append(";name-de-").append(i).append(";name-en-").append(i).append(";foo1")
					.append('\n');
		}

		buffer1.append(
				"INSERT MyVar; " + Product.CODE + "[unique=true]; baseProduct(" + Product.CODE
						+ ");variantType(code); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		buffer1.append("; product-1-var-3").append(";product-1-var-2; ").append('\n');
		buffer1.append("; product-1-var-2").append(";product-1-base; MyVar").append('\n');

		buffer1.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + ");variantType(code) ;catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		buffer1.append("; product-1-base").append(";name-de-base").append(";name-en-base").append(";foo1; MyVar").append('\n');

		// put unit at end to cause all products to be dumped
		buffer1.append("# trallala \n");
		buffer1.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		buffer1.append("; foo1; bar \n");

		// --------------------------------------------------------
		// --- Products for run 2
		// --------------------------------------------------------

		buffer2 = new StringBuilder();

		buffer2.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + "); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		final int multi_max = MAX * MULTI_FACTOR;

		for (int i = 0; i < multi_max; i++)
		{
			buffer2.append("; product-2-").append(i).append(";name-de-").append(i).append(";name-en-").append(i).append(";foo2")
					.append('\n');
		}
		buffer2.append(
				"INSERT MyVar; " + Product.CODE + "[unique=true]; baseProduct(" + Product.CODE
						+ ");variantType(code); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		buffer2.append("; product-2-var-3").append(";product-2-var-2").append('\n');
		buffer2.append("; product-2-var-2").append(";product-2-base; MyVar").append('\n');

		buffer2.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + ");variantType(code) ;catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		buffer2.append("; product-2-base").append(";name-de-base").append(";name-en-base").append(";foo2; MyVar").append('\n');
		// put unit at end to cause all products to be dumped
		buffer2.append("# trallala \n");
		buffer2.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		buffer2.append("; foo2; bar \n");

		// --------------------------------------------------------
		// --- Products for run 3
		// --------------------------------------------------------

		buffer3 = new StringBuilder();

		buffer3.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + "); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		for (int i = 0; i < MAX3; i++)
		{
			buffer3.append("; product-2-").append(i).append(";name-de-").append(i).append(";name-en-").append(i).append(";foo2")
					.append('\n');
		}
		buffer3.append(
				"INSERT MyVar; " + Product.CODE + "[unique=true]; baseProduct(" + Product.CODE
						+ ");variantType(code); catalogVersion(catalog(id),version)[allowNull=true]").append('\n');

		buffer3.append("; product-2-var-3").append(";product-2-var-2").append('\n');
		buffer3.append("; product-2-var-2").append(";product-2-base; MyVar").append('\n');

		buffer3.append(
				"INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME + "[lang=en]; "
						+ Product.UNIT + "(" + Unit.CODE + ");variantType(code) ;catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		buffer3.append("; product-2-base").append(";name-de-base").append(";name-en-base").append(";foo2; MyVar").append('\n');
		// put unit at end to cause all products to be dumped
		buffer3.append("# trallala \n");
		buffer3.append("INSERT Unit; " + Unit.CODE + "[unique=true]; " + Unit.UNITTYPE + " \n");
		buffer3.append("; foo2; bar \n");



		final TypeManager typeManager = TypeManager.getInstance();
		varType = typeManager.createComposedType(typeManager.getComposedType("VariantProduct"), "MyVar");

		try
		{
			final AttributeDescriptor attributeDescriptor = varType.getAttributeDescriptorIncludingPrivate("approvalStatus");
			attributeDescriptor.setOptional(true);
		}
		catch (final JaloItemNotFoundException e)
		{
			// fine too
		}
	}

	@After
	public void setLegacyMode()
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
	}

	protected void checkMultiThreadedImport(final MultiThreadedImporter mImp, final int max, final Set<Product> products)
	{
		assertFalse(mImp.hadError());
		assertFalse(mImp.isAborted());
		assertFalse(mImp.isRunning());
		assertTrue(mImp.isFinished());
		assertEquals(max + 3, products.size());
		// pass 1: dump MAX + 2 variants + 1 base -> pass 2: dump 1 variant
		assertTrue(max + (2 + 1) + 1 >= mImp.getDumpedLineCountOverall());

		final ProductManager productManager = ProductManager.getInstance();
		for (int i = 0; i < max; i++)
		{
			assertNotNull(productManager.getProductsByCode("product-2-" + i));
		}
		assertNotNull(productManager.getProductsByCode("product-2-base"));
		assertNotNull(productManager.getProductsByCode("product-2-var-3"));
		assertNotNull(productManager.getProductsByCode("product-2-var-2"));
	}

	@Test
	public void testAfterEachInMultiThreadedImpEx() throws ImpExException
	{
		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(
			"INSERT_UPDATE Title; code[unique=true]; \n"+ //
			"\"#% afterEach: impex.getLastImportedItem().setProperty(\"\"turn\"\",\"\"1\"\");\"\n"+ //
			";T1;\n"+//
			";T2;\n"+//
			";T3;\n"+//
			";T4;\n"+//
			";T5;\n"+//
			"\"#% afterEach: end \"\n"+ //
			"INSERT_UPDATE Title; code[unique=true]; \n"+ //
			"\"#% afterEach: impex.getLastImportedItem().setProperty(\"\"turn\"\",\"\"2\"\");\"\n"+ //
			";T6;\n"+//
			";T7;\n"+//
			";T8;\n"+//
			"INSERT_UPDATE Title; code[unique=true]; \n"+ //
			";T9;\n"+//
			";T10;+\n"+//
			"INSERT_UPDATE Title; code[unique=true]; \n"+ //
			"\"#% afterEach: impex.getLastImportedItem().setProperty(\"\"turn\"\",\"\"4\"\");\"\n"+ //
			";T11;\n"+//
			";T12;\n"+//
			"\"#% afterEach: end \"\n"+ //
			";T13;\n"+//
			";T14;\n"//
		));
		mImp.setMaxThreads(10);
		mImp.getReader().enableCodeExecution(true);

		mImp.importAll();
		
		assertFalse(mImp.hadError());
		
		assertTitleAfterEach("T1","1");
		assertTitleAfterEach("T2","1");
		assertTitleAfterEach("T3","1");
		assertTitleAfterEach("T4","1");
		assertTitleAfterEach("T5","1");

		assertTitleAfterEach("T6","2");
		assertTitleAfterEach("T7","2");
		assertTitleAfterEach("T8","2");
		
		assertTitleAfterEach("T9",null);
		assertTitleAfterEach("T10",null);

		assertTitleAfterEach("T11","4");
		assertTitleAfterEach("T12","4");
		
		assertTitleAfterEach("T13",null);
		assertTitleAfterEach("T14",null);
	}

	void assertTitleAfterEach(final String code, final String afterEachPropertyValue)
	{
		final Title t = UserManager.getInstance().getTitleByCode(code);
		assertEquals(afterEachPropertyValue, t.getProperty("turn"));
	}
	
	@Test
	public void testMinimalThreadSetup() throws ImpExException
	{
		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(buffer3.toString()));
		mImp.setMaxThreads(1);

		final Set<Product> productSet2 = new LinkedHashSet<Product>(MAX3);

		LOG.info("Minimal multi-threaded (" + mImp.getMaxThreads() + ") import of " + MAX3 + " products...");
		final long time3 = System.currentTimeMillis();
		for (Item p = mImp.importNext(); p != null; p = mImp.importNext())
		{
			if (p instanceof Product)
			{
				productSet2.add((Product) p);
			}
		}
		final long time4 = System.currentTimeMillis();
		LOG.info("done in " + (time4 - time3) + " ms after " + mImp.getCurrentPass() + " passes");

		checkMultiThreadedImport(mImp, MAX3, productSet2);
	}

	@Test
	public void testImport() throws ImpExException
	{
		final Importer imp = new Importer(new CSVReader(buffer1.toString()));
		final Set<Product> productSet1 = new LinkedHashSet<Product>(MAX);

		LOG.info("Single-threaded import of " + MAX + " products...");
		final long time1 = System.currentTimeMillis();
		for (Item p = imp.importNext(); p != null; p = imp.importNext())
		{
			if (p instanceof Product)
			{
				productSet1.add((Product) p);
			}
		}
		final long time2 = System.currentTimeMillis();
		LOG.info("done in " + (time2 - time1) + " ms after " + imp.getCurrentPass() + " passes. Rate is "
				+ ((MAX * 1000) / (time2 - time1)) + " items/s");

		assertFalse(imp.hadError());
		assertFalse(imp.isAborted());
		assertFalse(imp.isRunning());
		assertTrue(imp.isFinished());
		assertEquals(MAX + 3, productSet1.size());
		// pass 1: dump MAX + 2 variants + 1 base -> pass 2: dump 1 variant
		assertEquals(MAX + (2 + 1) + 1, imp.getDumpedLineCountOverall());

		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(buffer2.toString()));

		final Set<Product> productSet2 = new LinkedHashSet<Product>(MAX * MULTI_FACTOR);

		LOG.info("Multi-threaded (" + mImp.getMaxThreads() + ") import of " + (MAX * MULTI_FACTOR) + " products...");
		final long time3 = System.currentTimeMillis();
		for (Item p = mImp.importNext(); p != null; p = mImp.importNext())
		{
			if (p instanceof Product)
			{
				productSet2.add((Product) p);
			}
		}
		final long time4 = System.currentTimeMillis();
		LOG.info("done in " + (time4 - time3) + " ms after " + mImp.getCurrentPass() + " passes. Rate is "
				+ ((MAX * MULTI_FACTOR * 1000) / (time4 - time3)) + " items/s");

		checkMultiThreadedImport(mImp, MAX * MULTI_FACTOR, productSet2);

		LOG.info("Difference single-multi = " + (((time4 - time3) * 100) / (MULTI_FACTOR * (time2 - time1))) + " % ");
	}

	/*
	 * Tests if INSERT_UPDATE lines with same content do *not* lead to multiple items to be created even in
	 * multi-threaded setup.
	 */
	@Test
	public void testUniqueKeySerialization() throws ImpExException
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append("INSERT Catalog; id[unique=true];").append('\n');
		buffer.append("; foo;").append('\n');
		buffer.append("INSERT CatalogVersion; version[unique=true]; catalog(id)\n");
		buffer.append("; bar; foo;").append('\n');
		buffer.append("INSERT_UPDATE Media; ");
		buffer.append("code[unique=true]; ");
		buffer.append("catalogVersion(catalog(id[default='foo']),version[default='bar'])[unique=true]\n");
		for (int i = 0; i < 1000; i++)
		{
			buffer.append("; mmm ; \n");
		}
		for (int i = 0; i < 1000; i++)
		{
			buffer.append(";mmm;foo:bar \n");
			buffer.append(";mmm; \n");
		}

		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(buffer.toString()));
		mImp.setMaxThreads(Math.max(20, ImpExImportJob.getDefaultMaxThreads(Registry.getCurrentTenant())));

		mImp.importNext(); // create catalog
		final Item catalogVersion = mImp.importNext(); // create version

		final Set<Media> medias = new HashSet<Media>();
		for (Media t = (Media) mImp.importNext(); t != null; t = (Media) mImp.importNext())
		{
			medias.add(t);
		}
		assertEquals(1, medias.size());

		final List<String> duplicateCodes = FlexibleSearch.getInstance().search( //
				"SELECT {code} FROM {Media} WHERE {catalogVersion}=?cv GROUP BY {code} HAVING COUNT({PK})>1", //
				Collections.singletonMap("cv", catalogVersion),//
				String.class).getResult();

		assertEquals(Collections.EMPTY_LIST, duplicateCodes);
	}

	@Test
	public void testPLA11906() throws ImpExException
	{
		final StringBuilder buffer = new StringBuilder();
		buffer.append("REMOVE Media;code[unique=true];\n");
		buffer.append("; foo;").append('\n');// remove not existing media in mutlithreaded mode
		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(buffer.toString()));
		mImp.setMaxThreads(Math.max(20, ImpExImportJob.getDefaultMaxThreads(Registry.getCurrentTenant())));
		mImp.doImport(); // test passes if no NPE is thrown!
	}

	@Test
	public void testIgnoreMissingItemsForRemoval() throws ImpExException
	{
		final String csv = "REMOVE Media;code[unique=true];\n" + //
				"; doesntExist1;\n" + //
				"; doesntExist2;\n" + //
				"; doesntExist3;\n" + //
				"; doesntExist4;\n" + //
				"; doesntExist5;\n" + //
				"; doesntExist6;\n" + //
				"; doesntExist7;\n" + //
				"; doesntExist8;\n" + //
				"; doesntExist9;\n" + //
				"; doesntExist10;\n"; //
		final MultiThreadedImporter mImp = new MultiThreadedImporter(new CSVReader(csv));
		mImp.setMaxThreads(10);
		try
		{
			mImp.importAll();
		}
		catch (final ImpExException e)
		{
			if (e.getErrorCode() == ImpExException.ErrorCodes.CAN_NOT_RESOLVE_ANYMORE)
			{
				fail("ImpEx wasn't able to removing non-exitent items: " + e.getMessage());
			}
			throw e; // unexpected error
		}

		assertTrue(mImp.isFinished());
		assertEquals(0, mImp.getDumpedLineCountOverall());
		// test passes of all lines are ignored and *not* dumped
	}


	@Test
	public void testWorkersReturnedToPoolAfterExceptionThrownInReadLine()
	{
		final StringBuilder buffer = new StringBuilder();

		buffer.append("INSERT Product; " + Product.CODE + "[unique=true]; " + Product.NAME + "[lang=de]; " + Product.NAME
				+ "[lang=en]; " + Product.UNIT + "(" + Unit.CODE + "); catalogVersion(catalog(id),version)[allowNull=true]")
				.append('\n');

		for (int i = 0; i < LINES; i++)
		{
			buffer.append("; product-1-").append(i).append(";name-de-").append(i).append(";name-en-").append(i).append(";foo1")
					.append('\n');
		}


		final TestMTIR importReader = new TestMTIR(new CSVReader(buffer.toString()));
		importReader.setMaxThreads(10);


		try
		{
			importReader.readAll();
		}
		catch (final ImpExException e)
		{
			// OK
		}

		waitForWorkersToFinish(importReader, 5);


		assertTrue(importReader.isReaderFinished());
		assertTrue(importReader.isResultProcessorFinished());
		assertTrue(importReader.isAllWorkerFinished());
	}


	private void waitForWorkersToFinish(final TestMTIR reader, final int seconds)
	{
		int tick = 0;
		do
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}
		}
		while (++tick < seconds && !allWorkersFinished(reader));
	}

	private boolean allWorkersFinished(final TestMTIR reader)
	{
		return reader.isReaderFinished() && reader.isResultProcessorFinished() && reader.isAllWorkerFinished();
	}

	static class TestMTIR extends MultiThreadedImpExImportReader
	{

		private int counter = 0;

		TestMTIR(final CSVReader reader)
		{
			super(reader);
		}


		@Override
		public boolean isAllWorkerFinished()
		{
			return super.isAllWorkerFinished();
		}

		@Override
		public boolean isReaderFinished()
		{
			return super.isReaderFinished();
		}

		@Override
		public boolean isResultProcessorFinished()
		{
			return super.isResultProcessorFinished();
		}

		@Override
		public AbstractCodeLine getBeforeEachCode()
		{
			return new SimpleCodeLine(null, null, null, 0, null);
		}

		@Override
		protected void execute(final AbstractCodeLine line, final Map csvLine, final boolean forEachMode) throws ImpExException
		{
			if (++counter == LINES)
			{
				throw new ImpExException("throw exception in readLine()");
			}
		}
	}
}
