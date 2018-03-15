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
package de.hybris.platform.catalog.jalo.classification.impex;

import static de.hybris.platform.testframework.Assert.assertCollection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.exp.ImpExCSVExportWriter;
import de.hybris.platform.impex.jalo.exp.ImpExExportWriter;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ClassificationAttributeTranslatorTest extends HybrisJUnit4TransactionalTest
{
	private CatalogManager catalogManager;
	private ProductManager productManager;

	private Language german, english;
	private ClassificationSystem sys;
	private ClassificationSystemVersion sysVer;
	private ClassificationAttribute attr1, attr2, attr3, attr4, attr5, attr6;
	private ClassificationAttributeValue val1, val2;
	private ClassificationClass ccat1, ccat11, ccat12, ccat2;
	private Category outside;
	private Product product1, product2, product3;
	private Date date1;
	private String legacyModeBackup;

	@Before
	public void setUp() throws Exception
	{
		// Force impex.legacy.mode
		legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");

		english = getOrCreateLanguage("en");
		// set de as language to get german locale !!!
		jaloSession.getSessionContext().setLanguage(german = getOrCreateLanguage("de"));

		//		 attr1 -> Number 
		//		 attr2 -> String, Multiple 
		//		 attr3 -> Boolean 
		//		 attr4 -> Enumeration, Multiple 
		//		 attr5 -> Date 
		//		 attr6 -> String, Localized 
		//		 
		//		 TestSys 
		//		 +- 2.0 
		//		 		+- ccat1(attr1) 
		//		 			+- ccat11 
		//		 			+- ccat12(attr2, attr3, attr4)
		//				+- ccat2(attr2:boolean!)
		//		 
		//		 	p1<-( ccat11, outside ) 
		//		 		attr1=42 
		//		 	p2<-( ccat12 ) 
		//		 		attr1=13, 
		//		 		attr2=(axel,jens), 
		//		 		attr3=False, 
		//		 		attr4=val1,
		//		 		attr5=01.08.1980 
		//		 		p3<-()

		productManager = ProductManager.getInstance();
		catalogManager = CatalogManager.getInstance();

		assertNotNull(sys = catalogManager.createClassificationSystem("TestSys"));

		assertNotNull(sysVer = catalogManager.createClassificationSystemVersion(sys, "2.0", german));

		assertNotNull(attr1 = sysVer.createClassificationAttribute("attr1"));
		assertNotNull(attr2 = sysVer.createClassificationAttribute("attr2"));
		assertNotNull(attr3 = sysVer.createClassificationAttribute("attr3"));
		assertNotNull(attr4 = sysVer.createClassificationAttribute("attr4"));
		assertNotNull(attr5 = sysVer.createClassificationAttribute("attr5"));
		assertNotNull(attr6 = sysVer.createClassificationAttribute("attr6"));

		assertNotNull(ccat1 = sysVer.createClass("ccat1"));
		assertNotNull(ccat11 = sysVer.createClass("ccat11"));
		assertNotNull(ccat12 = sysVer.createClass("ccat12"));
		assertNotNull(ccat2 = sysVer.createClass("ccat2"));


		date1 = Utilities.getSimpleDateFormat("dd.MM.yy").parse("01.08.80");

		ccat1.setCategories(ccat11, ccat12);

		ccat1.assignAttribute(attr1);
		ccat1.setAttributeType(
				attr1,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER));

		ccat12.assignAttribute(attr2);
		ccat12.setAttributeType(
				attr2,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING));
		ccat12.setMultiValued(attr2, true);

		ccat12.assignAttribute(attr3);
		ccat12.setAttributeType(
				attr3,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN));

		ccat12.assignAttribute(attr4);
		ccat12.setAttributeType(
				attr4,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.ENUM));
		ccat12.setMultiValued(attr4, true);

		ccat12.assignAttribute(attr5);
		ccat12.setAttributeType(
				attr5,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.DATE));

		ccat12.assignAttribute(attr6);
		ccat12.setAttributeType(
				attr6,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING));
		ccat12.setLocalized(attr6, true);

		ccat2.assignAttribute(attr2);
		ccat2.setAttributeType(
				attr2,
				EnumerationManager.getInstance().getEnumerationValue(CatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM,
						CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN));


		assertNotNull(val1 = sysVer.createClassificationAttributeValue("val1"));
		assertNotNull(val2 = sysVer.createClassificationAttributeValue("val2"));
		ccat12.setAttributeValues(attr4, Arrays.asList(val1, val2));

		assertNotNull(outside = CategoryManager.getInstance().createCategory("outside"));

		assertNotNull(product1 = productManager.createProduct("p1"));
		assertNotNull(product2 = productManager.createProduct("p2"));
		assertNotNull(product3 = productManager.createProduct("p3"));

		CategoryManager.getInstance().setSupercategories(product1, Arrays.asList(new Category[]
		{ ccat11, outside }));
		CategoryManager.getInstance().setSupercategories(product2, Collections.singletonList((Category) ccat12));

		final FeatureContainer p1Cont = FeatureContainer.create(product1);
		final FeatureContainer p2Cont = FeatureContainer.create(product2);

		// p1(attr1)=42
		p1Cont.getFeature(attr1).createValue(new BigDecimal(42));
		// p2(attr1)=13
		p2Cont.getFeature(attr1).createValue(new BigDecimal(13));
		// p2(attr2)=(axel,jens)
		p2Cont.getFeature(attr2).createValue("axel");
		p2Cont.getFeature(attr2).createValue("jens");
		// p2(attr3)=False
		p2Cont.getFeature(attr3).createValue(Boolean.FALSE);
		// p2(attr4)=val1
		p2Cont.getFeature(attr4).createValue(val1);
		// p2(attr5)=01.08.1980
		p2Cont.getFeature(attr5).createValue(date1);

		p2Cont.getFeature(attr6).createValue("test DE");

		p1Cont.store();
		p2Cont.store();
	}

	@After
	public void setLegacyMode()
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
	}

	private String getCLColumn(final ClassificationAttribute attr)
	{
		return getCLColumn(attr, null);
	}

	private String getCLColumn(final ClassificationAttribute attr, final String delimiter)
	{
		return getCLColumn(attr, delimiter, null);
	}

	private String getCLColumn(final ClassificationAttribute attr, final String delimiter, final String dateformat)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX).append(attr.getCode());
		stringBuilder.append("[");
		stringBuilder.append("translator='").append(ClassificationAttributeTranslator.class.getName()).append("'");
		stringBuilder.append(",").append(ClassificationAttributeTranslator.Modifiers.SYSTEM).append("='")
				.append(attr.getSystemVersion().getClassificationSystem().getId()).append("'");
		stringBuilder.append(",").append(ClassificationAttributeTranslator.Modifiers.VERSION).append("='")
				.append(attr.getSystemVersion().getVersion()).append("'");
		if (delimiter != null)
		{
			stringBuilder.append(",").append(ImpExConstants.Syntax.Modifier.COLLECTION_VALUE_DELIMITER).append("='")
					.append(delimiter).append("'");
		}
		if (dateformat != null)
		{
			stringBuilder.append(",").append(ImpExConstants.Syntax.Modifier.DATEFORMAT).append("='").append(dateformat).append("'");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	private String getCLColumn(final ClassAttributeAssignment assignment)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX).append(assignment.getClassificationAttribute().getCode());
		stringBuilder.append("[");
		stringBuilder.append("translator='").append(ClassificationAttributeTranslator.class.getName()).append("'");
		stringBuilder.append(",").append(ClassificationAttributeTranslator.Modifiers.SYSTEM).append("='")
				.append(assignment.getClassificationClass().getSystemVersion().getClassificationSystem().getId()).append("'");
		stringBuilder.append(",").append(ClassificationAttributeTranslator.Modifiers.VERSION).append("='")
				.append(assignment.getClassificationClass().getSystemVersion().getVersion()).append("'");
		stringBuilder.append(",").append(ClassificationAttributeTranslator.Modifiers.ClASS).append("='")
				.append(assignment.getClassificationClass().getCode()).append("'");
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	@Test
	public void testSameAttributeDifferentClasses() throws ImpExException
	{
		final ClassAttributeAssignment ccat12_attr2 = ccat12.getAttributeAssignment(attr2);
		final ClassAttributeAssignment ccat2_attr2 = ccat2.getAttributeAssignment(attr2);
		FeatureContainer container = FeatureContainer.loadTyped(product2, ccat12_attr2, ccat2_attr2);

		assertEquals(Arrays.asList("axel", "jens"), container.getFeature(ccat12_attr2).getValuesDirect());
		assertEquals(Collections.EMPTY_LIST, container.getFeature(ccat2_attr2).getValuesDirect());

		final String header = "INSERT_UPDATE Product;" + //
				Product.CODE + "[unique=true];" + //
				getCLColumn(attr2) + ";" + // let attr2 match dynamically -> should only find ccat12.attr2 since p3 is NOT assigned to ccat2
				getCLColumn(ccat2_attr2) + ";\n"; // set ccat2.attr2 direct 

		final String lines = //
		"; " + product2.getCode() + "; foo; true ; \n" + //
				"; " + product3.getCode() + "; bar; false; \n";

		final StringWriter stringWriter = new StringWriter();
		final ImpExImportReader impexReader = new ImpExImportReader(new CSVReader(header + lines), new CSVWriter(stringWriter));

		assertEquals(product2, impexReader.readLine());
		assertEquals(product3, impexReader.readLine());
		assertNull(impexReader.readLine());

		container = FeatureContainer.loadTyped(product2, ccat12_attr2, ccat2_attr2);
		assertEquals(Arrays.asList("foo"), container.getFeature(ccat12_attr2).getValuesDirect());
		assertEquals(Collections.singletonList(Boolean.TRUE), container.getFeature(ccat2_attr2).getValuesDirect());

		container = FeatureContainer.loadTyped(product3, ccat12_attr2, ccat2_attr2);
		assertEquals(Collections.EMPTY_LIST, container.getFeature(ccat12_attr2).getValuesDirect()); // since p3 is NOT assigned to ccat12 first attr2 column should have no effect ! 
		assertEquals(Collections.singletonList(Boolean.FALSE), container.getFeature(ccat2_attr2).getValuesDirect());
	}

	@Test
	public void testIllegalClAttrTypeImport() throws ImpExException, IOException
	{
		final String header = "INSERT_UPDATE Product;" + //
				Product.CODE + "[unique=true];" + //
				CategoryConstants.Attributes.Product.SUPERCATEGORIES + "(" + Category.CODE + ");" + //
				getCLColumn(attr2) + ";" + //
				getCLColumn(attr4) + ";\n";

		final String lines = "; validProduct; " + ccat12.getCode() + "; foo; " + val1.getCode() + " ; \n" + //
				"; invalidProduct; " + ccat12.getCode() + "; bar; nonExistingEnumCode ; \n";

		final StringWriter stringWriter = new StringWriter();
		final ImpExImportReader imPexReader = new ImpExImportReader(new CSVReader(header + lines), new CSVWriter(stringWriter));
		// disable annoying mandatory attributes from other extensions
		imPexReader.setAttributeConstraintFilter(new HeaderDescriptor.AttributeConstraintFilter()
		{
			@Override
			public boolean isInitialOnly(final AttributeDescriptor attributeDescriptor)
			{
				return false;
			}

			@Override
			public boolean isMandatory(final AttributeDescriptor attributeDescriptor)
			{
				return Product.CODE.equalsIgnoreCase(attributeDescriptor.getQualifier());
			}
		});

		final Product product1 = (Product) imPexReader.readLine();
		assertNotNull(product1);
		assertEquals("validProduct", product1.getCode());
		final FeatureContainer fc1 = FeatureContainer.load(product1);
		assertEquals(Arrays.asList("foo"), fc1.getFeature(attr2).getValuesDirect());
		assertEquals(Arrays.asList(val1), fc1.getFeature(attr4).getValuesDirect());
		assertEquals(0, imPexReader.getDumpedLineCount());

		final Product product2 = (Product) imPexReader.readLine();
		assertNotNull(product2);
		assertEquals("invalidProduct", product2.getCode());
		final FeatureContainer fc2 = FeatureContainer.load(product2);
		assertEquals(Arrays.asList("bar"), fc2.getFeature(attr2).getValuesDirect());
		assertTrue(fc2.getFeature(attr4).isEmpty());
		assertEquals(1, imPexReader.getDumpedLineCount());

		assertNull(imPexReader.readLine());
		imPexReader.close();

		final CSVReader csvReader = new CSVReader(stringWriter.getBuffer().toString());

		// read header
		assertTrue(csvReader.readNextLine());
		final Map<Integer, String> headerLine = csvReader.getLine();
		assertNotNull(headerLine);
		// read one dumped line
		assertTrue(csvReader.readNextLine());
		final Map<Integer, String> dumpedLine = csvReader.getLine();
		assertNotNull(dumpedLine);
		// code column is marked as IGNORE
		assertTrue(dumpedLine.get(Integer.valueOf(1)).startsWith(ImpExConstants.Syntax.IGNORE_PREFIX));
		// supercategories column is marked as IGNORE
		assertTrue(dumpedLine.get(Integer.valueOf(2)).startsWith(ImpExConstants.Syntax.IGNORE_PREFIX));
		// attr2 column is marked as IGNORE
		assertTrue(dumpedLine.get(Integer.valueOf(3)).startsWith(ImpExConstants.Syntax.IGNORE_PREFIX));
		// attr4 column is NOT marked as IGNORE
		assertEquals("nonExistingEnumCode", dumpedLine.get(Integer.valueOf(4)));
		// no more lines
		assertFalse(csvReader.readNextLine());
	}

	@Test
	public void testImport()
	{
		String lines = "INSERT_UPDATE Product;" + //
				Product.CODE + "[unique=true];" + //
				CategoryConstants.Attributes.Product.SUPERCATEGORIES + "(" + Category.CODE + ");" + //
				getCLColumn(attr1, "|") + ";" + //
				getCLColumn(attr2) + ";" + //
				getCLColumn(attr3) + ";" + //
				getCLColumn(attr4) + ";" + //
				getCLColumn(attr5, null, "dd.MM.yy") + ";" + //
				"\n" + //
				"# ----------------------------------------------- \n";
		lines += "; ppp1 ; " + ccat12.getCode() + "; 123,456 	; str1, str2, str3 ; FALSE ; " + val2.getCode() + " ; "
				+ Utilities.getSimpleDateFormat("01.08.80").format(date1) + " \n" + "; ppp2 ; " + outside.getCode() + ","
				+ ccat11.getCode() + "; 789,1011 ; dummy dummy      ; dummy ; dummy                  ; dummy \n";

		final ImpExImportReader imPexReader = createImpExImportReader(lines);

		Product ppp1, ppp2;

		try
		{
			ppp1 = (Product) imPexReader.readLine();
			assertNotNull(ppp1);
			assertNotNull(ppp1);
			assertEquals("ppp1", ppp1.getCode());
			assertCollection(Collections.singleton(ccat12), CategoryManager.getInstance().getSupercategories(ppp1));

			FeatureContainer cont = FeatureContainer.load(ppp1);
			assertEquals(new HashSet(Arrays.asList(attr1, attr2, attr3, attr4, attr5, attr6)), cont.getSupportedAttributes());
			List<FeatureValue> featureValueList = cont.getFeature(attr1).getValues();
			assertNotNull(featureValueList);
			assertEquals(1, featureValueList.size());
			assertEquals(123.456, ((Number) featureValueList.get(0).getValue()).doubleValue(), 0.0001);
			featureValueList = cont.getFeature(attr2).getValues();
			assertNotNull(featureValueList);
			assertEquals(3, featureValueList.size());
			assertEquals("str1", featureValueList.get(0).getValue());
			assertEquals("str2", featureValueList.get(1).getValue());
			assertEquals("str3", featureValueList.get(2).getValue());
			featureValueList = cont.getFeature(attr3).getValues();
			assertNotNull(featureValueList);
			assertEquals(1, featureValueList.size());
			assertEquals(Boolean.FALSE, featureValueList.get(0).getValue());
			featureValueList = cont.getFeature(attr4).getValues();
			assertNotNull(featureValueList);
			assertEquals(1, featureValueList.size());
			assertEquals(val2, featureValueList.get(0).getValue());
			featureValueList = cont.getFeature(attr5).getValues();
			assertNotNull(featureValueList);
			assertEquals(1, featureValueList.size());
			assertEquals(date1.toString(), featureValueList.get(0).getValue().toString());

			assertNotNull(ppp2 = (Product) imPexReader.readLine());
			assertEquals("ppp2", ppp2.getCode());
			assertCollection(Arrays.asList(new Object[]
			{ outside, ccat11 }), CategoryManager.getInstance().getSupercategories(ppp2));

			cont = FeatureContainer.load(ppp2);
			assertEquals(new HashSet(Arrays.asList(attr1)), cont.getSupportedAttributes());
			featureValueList = cont.getFeature(attr1).getValues();
			assertEquals(1, featureValueList.size());
			assertEquals(789.1011, ((Number) featureValueList.get(0).getValue()).doubleValue(), 0.0001);
			try
			{
				cont.getFeature(attr2);
				fail("JaloInvalidParameterException expected");
			}
			catch (final JaloInvalidParameterException e)
			{
				// fine
			}
			try
			{
				cont.getFeature(attr3);
				fail("JaloInvalidParameterException expected");
			}
			catch (final JaloInvalidParameterException e)
			{
				// fine
			}
			try
			{
				cont.getFeature(attr4);
				fail("JaloInvalidParameterException expected");
			}
			catch (final JaloInvalidParameterException e)
			{
				// fine
			}

			assertNull(imPexReader.readLine());

			assertEquals(0, imPexReader.getDumpedLineCount());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			try
			{
				imPexReader.close();
			}
			catch (final IOException e)
			{
				// DOCTODO Document reason, why this block is empty
			}
		}
	}

	protected Collection assureCollection(final Object featureValue)
	{
		return featureValue != null ? (featureValue instanceof Collection ? (Collection) featureValue : Collections
				.singleton(featureValue)) : null;
	}

	@Test
	public void testExport() throws ConsistencyCheckException, Exception
	{
		final String header = "INSERT Product;" + Product.CODE + "[unique=true];"
				+ CategoryConstants.Attributes.Product.SUPERCATEGORIES + "(" + Category.CODE + ");"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr1.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "',"
				+ ImpExConstants.Syntax.Modifier.COLLECTION_VALUE_DELIMITER + "='|'" + "];"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr2.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr3.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr4.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr5.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "',"
				+ ImpExConstants.Syntax.Modifier.DATEFORMAT + "='dd.MM.yy'," + "]";

		final StringWriter stringWriter = new StringWriter();
		final ImpExExportWriter exportWriter = new ImpExCSVExportWriter(new CSVWriter(stringWriter));

		Product ppp1 = null, ppp2 = null, ppp3 = null;

		assertNotNull(ppp1 = productManager.createProduct("exppp1"));
		CategoryManager.getInstance().setSupercategories(ppp1, Arrays.asList(new Category[]
		{ ccat11 }));
		FeatureContainer cont = FeatureContainer.create(ppp1);
		cont.getFeature(attr1).createValue(new Double(123.456));
		cont.store();

		assertNotNull(ppp2 = productManager.createProduct("exppp2"));
		CategoryManager.getInstance().setSupercategories(ppp2, Arrays.asList(new Category[]
		{ ccat12 }));
		cont = FeatureContainer.create(ppp2);
		cont.getFeature(attr1).createValue(new Double(555.999));
		cont.getFeature(attr2).createValue("so ein bloeder test");
		cont.getFeature(attr3).createValue(Boolean.TRUE);
		cont.getFeature(attr4).createValue(val2);
		cont.getFeature(attr5).createValue(date1);
		cont.store();

		assertNotNull(ppp3 = productManager.createProduct("exppp3"));

		boolean closed = false;
		try
		{
			final ImpExReader impExReader = new ImpExReader(new CSVReader(new StringReader(header)), true);
			// disable annoying mandatory attributes from other extensions
			impExReader.setAttributeConstraintFilter(new HeaderDescriptor.AttributeConstraintFilter()
			{
				@Override
				public boolean isInitialOnly(final AttributeDescriptor ad)
				{
					return false;
				}

				@Override
				public boolean isMandatory(final AttributeDescriptor ad)
				{
					return Product.CODE.equalsIgnoreCase(ad.getQualifier());
				}
			});
			final HeaderDescriptor headerDescriptor = (HeaderDescriptor) impExReader.readLine();
			assertNotNull(headerDescriptor);

			exportWriter.setCurrentHeader(headerDescriptor);
			exportWriter.writeCurrentHeader(false);
			exportWriter.writeLine(ppp1);
			exportWriter.writeLine(ppp2);
			exportWriter.writeLine(ppp3);

			exportWriter.close();
			closed = true;

			final String expected = header + CSVConstants.DEFAULT_LINE_SEPARATOR + ";" + ppp1.getCode() + ";" + ccat11.getCode()
					+ ";" + "123,456" + ";" + ";" + ";" + ";" + CSVConstants.DEFAULT_LINE_SEPARATOR + ";" + ppp2.getCode() + ";"
					+ ccat12.getCode() + ";" + "555,999" + ";" + "so ein bloeder test" + ";" + Boolean.TRUE.toString() + ";"
					+ val2.getCode() + ";" + Utilities.getSimpleDateFormat("dd.MM.yy").format(date1)
					+ CSVConstants.DEFAULT_LINE_SEPARATOR + ";" + ppp3.getCode() + ";" + ";" + ";" + ";" + ";" + ";"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR;

			final String got = stringWriter.getBuffer().toString();
			assertEquals(expected, got);
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			if (!closed)
			{
				exportWriter.close();
			}
		}

	}

	@Test
	public void testProductFeatureValueImport()
	{
		final String lines = "INSERT ProductFeature;" + ProductFeature.QUALIFIER + "[unique=true];" + ProductFeature.PRODUCT + ";"
				+ ProductFeature.CLASSIFICATIONATTRIBUTEASSIGNMENT + ";" + ProductFeature.LANGUAGE + ";" + ProductFeature.VALUE
				+ "[translator=de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator];\n"
				+ "# ----------------------------------------------- \n" + "; test1 ; " + product1.getPK() + "; "
				+ ccat12.getAttributeAssignment(attr4).getPK() + " ; " + german.getPK() + " ; " + "enum," + sys.getId() + ","
				+ sysVer.getVersion() + "," + val1.getCode() + " \n";

		final ImpExImportReader imPexReader = createImpExImportReader(lines);

		ProductFeature pf1;

		try
		{
			pf1 = (ProductFeature) imPexReader.readLine();
			assertNotNull(pf1);
			assertNotNull(pf1);
			assertEquals("test1", pf1.getQualifier());
			assertEquals(val1, pf1.getValue());
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			try
			{
				imPexReader.close();
			}
			catch (final IOException e)
			{
				// DOCTODO Document reason, why this block is empty
			}
		}
	}

	@Test
	public void testProductFeatureValueExport() throws ConsistencyCheckException, Exception
	{
		final String lines = "INSERT ProductFeature;" + ProductFeature.QUALIFIER + "[unique=true];" + ProductFeature.PRODUCT + ";"
				+ ProductFeature.CLASSIFICATIONATTRIBUTEASSIGNMENT + ";" + ProductFeature.LANGUAGE + ";" + ProductFeature.VALUE
				+ "[translator=de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator];\n";


		final StringWriter stringWriter = new StringWriter();
		final ImpExExportWriter exportWriter = new ImpExCSVExportWriter(new CSVWriter(stringWriter));

		boolean closed = false;
		try
		{
			final ImpExReader impExReader = new ImpExReader(new CSVReader(new StringReader(lines)), true);
			// disable annoying mandatory attributes from other extensions
			impExReader.setAttributeConstraintFilter(new HeaderDescriptor.AttributeConstraintFilter()
			{
				@Override
				public boolean isInitialOnly(final AttributeDescriptor ad)
				{
					return false;
				}

				@Override
				public boolean isMandatory(final AttributeDescriptor ad)
				{
					return Product.CODE.equalsIgnoreCase(ad.getQualifier());
				}
			});
			final HeaderDescriptor headerDescriptor = (HeaderDescriptor) impExReader.readLine();
			assertNotNull(headerDescriptor);

			exportWriter.setCurrentHeader(headerDescriptor);
			exportWriter.writeCurrentHeader(false);
			for (final ProductFeature feature : CatalogManager.getInstance().getFeatures(product2))
			{
				if (feature.getValue().equals(val1))
				{
					exportWriter.writeLine(feature);
				}
			}

			exportWriter.close();
			closed = true;

			final String expected = "INSERT ProductFeature;qualifier[unique=true];product;classificationAttributeAssignment;language;value[translator=de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]"
					+ CSVConstants.DEFAULT_LINE_SEPARATOR
					+ ";TestSys/2.0/ccat12.attr4;"
					+ product2.getPK()
					+ ";"
					+ ccat12.getAttributeAssignment(attr4).getPK() + ";;enum,TestSys,2.0,val1" + CSVConstants.DEFAULT_LINE_SEPARATOR;

			final String got = stringWriter.getBuffer().toString();
			assertEquals(expected, got);
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally
		{
			if (!closed)
			{
				exportWriter.close();
			}
		}

	}

	@Test
	public void testPLA10283() throws ImpExException
	{
		final String lines = "INSERT_UPDATE Product;" + Product.CODE + "[unique=true];"
				+ CategoryConstants.Attributes.Product.SUPERCATEGORIES + "(" + Category.CODE + ");"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr1.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "',"
				+ ImpExConstants.Syntax.Modifier.COLLECTION_VALUE_DELIMITER + "='|'" + "];"
				+ ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr2.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr3.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr4.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'"
				+ "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX + attr5.getCode() + "[" + "translator='"
				+ ClassificationAttributeTranslator.class.getName() + "'," + ClassificationAttributeTranslator.Modifiers.SYSTEM
				+ "='" + sys.getId() + "'," + ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "',"
				+ ImpExConstants.Syntax.Modifier.DATEFORMAT + "='" + "dd.MM.yy'" + "];" + ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX
				+ attr6.getCode() + "[" + "translator='" + ClassificationAttributeTranslator.class.getName() + "',"
				+ ClassificationAttributeTranslator.Modifiers.SYSTEM + "='" + sys.getId() + "',"
				+ ClassificationAttributeTranslator.Modifiers.VERSION + "='" + sysVer.getVersion() + "'" + "]" + "["
				+ ImpExConstants.Syntax.Modifier.LANGUAGE + "='de'" + "]\n" + "# ----------------------------------------------- \n"
				+ "; ppp1 ; " + ccat12.getCode() + "; 123,456 	; str1, str2, str3 ; FALSE ; " + val2.getCode() + " ; "
				+ Utilities.getSimpleDateFormat("01.08.80").format(date1) + " ; test DE\n";

		ImpExImportReader impExReader = createImpExImportReader(lines);

		Product product1;

		product1 = (Product) impExReader.readLine();
		assertNotNull(product1);
		FeatureContainer cont = FeatureContainer.load(product1);
		List<FeatureValue> featureValueList = cont.getFeature(attr6).getValues();
		assertNotNull(featureValueList);
		assertEquals(1, featureValueList.size());
		assertEquals("test DE", featureValueList.get(0).getValue());


		final String update = "$trans=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator\r\n"
				+ "$attr6=@attr6[translator=$trans,system=TestSys,version=2.0]\r\n" + "\r\n"
				+ "\"INSERT_UPDATE Product\";\"code[unique=true]\";\"$attr6[lang=en]\";\r\n" + ";\"ppp1\";\"test EN\"";

		impExReader = new ImpExImportReader(update);
		// disable annoying mandatory attributes from other extensions
		impExReader.setAttributeConstraintFilter(new HeaderDescriptor.AttributeConstraintFilter()
		{
			@Override
			public boolean isInitialOnly(final AttributeDescriptor attributeDescriptor)
			{
				return false;
			}

			@Override
			public boolean isMandatory(final AttributeDescriptor attributeDescriptor)
			{
				return Product.CODE.equalsIgnoreCase(attributeDescriptor.getQualifier());
			}
		});
		product1 = (Product) impExReader.readLine();
		assertNotNull(product1);
		cont = FeatureContainer.load(product1);
		final SessionContext ctx = jaloSession.getSessionContext();

		try
		{
			final SessionContext ctxDe = jaloSession.createLocalSessionContext(ctx);
			jaloSession.getSessionContext().setLanguage(german);
			try
			{
				final SessionContext ctxEn = jaloSession.createLocalSessionContext(ctx);
				jaloSession.getSessionContext().setLanguage(english);
				featureValueList = cont.getFeature(attr6).getValues(ctxEn);
				assertNotNull(featureValueList);
				assertEquals(1, featureValueList.size());
				assertEquals("test EN", featureValueList.get(0).getValue());
				featureValueList = cont.getFeature(attr6).getValues(ctxDe);
				assertNotNull(featureValueList);
				assertEquals(1, featureValueList.size());
				assertEquals("test DE", featureValueList.get(0).getValue());
			}
			finally
			{
				JaloSession.getCurrentSession().removeLocalSessionContext();
			}
		}
		finally
		{
			JaloSession.getCurrentSession().removeLocalSessionContext();
		}
	}

	//PLA-12906
	@Test
	public void testEmptyValue() throws ImpExException
	{
		String lines = "INSERT_UPDATE Product;" + //
				Product.CODE + "[unique=true];" + //
				CategoryConstants.Attributes.Product.SUPERCATEGORIES + "(" + Category.CODE + ");" + //
				getCLColumn(attr2) + ";" + //
				"\n" + //
				"# ----------------------------------------------- \n";
		lines += "; product_01 ; " + ccat12.getCode() + "; value01 ; \n";

		Product product01 = (Product) createImpExImportReader(lines).readLine();
		FeatureContainer containter = FeatureContainer.load(product01);
		List<FeatureValue> featureValueList = containter.getFeature(attr2).getValues();
		assertNotNull(featureValueList);
		assertEquals(1, featureValueList.size());
		assertEquals("value01", featureValueList.get(0).getValue());

		//without value for attr2
		String newLines = "INSERT_UPDATE Product;" + //
				Product.CODE + "[unique=true];" + //
				getCLColumn(attr2) + ";" + //
				"\n" + //
				"# ----------------------------------------------- \n";
		newLines += "; product_01 ; ; \n";

		product01 = (Product) createImpExImportReader(newLines).readLine();
		containter = FeatureContainer.load(product01);
		featureValueList = containter.getFeature(attr2).getValues();
		assertNotNull(featureValueList);
		assertEquals(0, featureValueList.size());
	}

	private ImpExImportReader createImpExImportReader(final String lines)
	{
		final ImpExImportReader imPexReader = new ImpExImportReader(lines);
		// disable annoying mandatory attributes from other extensions
		imPexReader.setAttributeConstraintFilter(new HeaderDescriptor.AttributeConstraintFilter()
		{
			@Override
			public boolean isInitialOnly(final AttributeDescriptor attributeDescriptor)
			{
				return false;
			}

			@Override
			public boolean isMandatory(final AttributeDescriptor attributeDescriptor)
			{
				return Product.CODE.equalsIgnoreCase(attributeDescriptor.getQualifier());
			}
		});
		return imPexReader;
	}

}
