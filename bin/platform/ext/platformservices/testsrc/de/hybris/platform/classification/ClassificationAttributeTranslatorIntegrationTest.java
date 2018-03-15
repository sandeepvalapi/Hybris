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
package de.hybris.platform.classification;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class ClassificationAttributeTranslatorIntegrationTest extends ServicelayerTest
{
	private String clsAttrValueFallbackBackup;
	@Resource
	private ProductService productService;
	@Resource
	private ClassificationService classificationService;
	@Resource
	private ImportService importService;
	@Resource
	private MediaService mediaService;

	@Before
	public void setUp() throws Exception
	{
		clsAttrValueFallbackBackup = Config
				.getParameter(ClassificationAttributeTranslator.IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY);
		createCoreData();
		createDefaultCatalog();
		createHardwareCatalog();
	}

	@After
	public void setFallbackMode()
	{
		Config.setParameter(ClassificationAttributeTranslator.IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY,
				clsAttrValueFallbackBackup);
	}

	@Test
	public void shouldMarkAsUnresolvedClassificationValueOfTypeEnumIfThisValueDoesNotExistYetInFallbackModeDisabled()
	{
		// given
		Config.setParameter(ClassificationAttributeTranslator.IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY, "false");
		final StringBuilder builder = new StringBuilder("$systemName=SampleClassification");
		builder.append("\n").append("$systemVersion=1.0").append("\n");
		builder.append("$YCL=system='$systemName',version='$systemVersion',");
		builder.append("translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;");
		builder.append("\n").append("UPDATE Product;code[unique=true];@lanSpeed[$YCL];");
		builder.append("catalogVersion[unique=true](catalog(id),version)[virtual=true,default=hwcatalog:Online]").append("\n");
		builder.append(";HW2200-0878;SomeNonExistendOne").append("\n");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(builder.toString());
		importConfig.setRemoveOnSuccess(false);

		ImportResult result = null;

		try
		{
			TestUtils.disableFileAnalyzer("the non imported line here is ok, needed for the test", 100);
			// when
			result = importService.importData(importConfig);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
		// then
		assertThat(result).isNotNull();
		assertThat(result.isFinished()).isTrue();
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(result.isError()).isTrue();

		final ProductModel product = productService.getProductForCode("HW2200-0878");
		final FeatureList features = classificationService.getFeatures(product);
		final Feature feature = features.getFeatureByCode("SampleClassification/1.0/boards.lanspeed");

		assertThat(feature).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValue().getValue()).isInstanceOf(ClassificationAttributeValueModel.class);
		assertThat(((ClassificationAttributeValueModel) feature.getValue().getValue()).getCode()).isEqualTo("L1G");
	}

	@Test
	public void shouldImportdClassificationValueOfTypeEnumAsStringIfThisValueDoesNotExistYetInFallbackModeEnabled()
	{
		// given
		Config.setParameter(ClassificationAttributeTranslator.IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY, "true");
		final StringBuilder builder = new StringBuilder("$systemName=SampleClassification");
		builder.append("\n").append("$systemVersion=1.0").append("\n");
		builder.append("$YCL=system='$systemName',version='$systemVersion',");
		builder.append("translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;");
		builder.append("\n").append("UPDATE Product;code[unique=true];@lanSpeed[$YCL];");
		builder.append("catalogVersion[unique=true](catalog(id),version)[virtual=true,default=hwcatalog:Online]").append("\n");
		builder.append(";HW2200-0878;SomeNonExistendOne").append("\n");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(builder.toString());
		importConfig.setRemoveOnSuccess(false);

		// when
		final ImportResult result = importService.importData(importConfig);

		// then
		assertThat(result).isNotNull();
		assertThat(result.isFinished()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(result.isError()).isFalse();

		final ProductModel product = productService.getProductForCode("HW2200-0878");
		final FeatureList features = classificationService.getFeatures(product);
		final Feature feature = features.getFeatureByCode("SampleClassification/1.0/boards.lanspeed");

		assertThat(feature).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValue().getValue()).isInstanceOf(String.class);
		assertThat(feature.getValue().getValue()).isEqualTo("SomeNonExistendOne");
	}

	@Test
	public void shouldImportdClassificationValueOfTypeEnumIfThisValueIsFurtherInTheScriptInFallbackModeDisabled()
	{
		// given
		Config.setParameter(ClassificationAttributeTranslator.IMPEX_NONEXISTEND_CLSATTRVALUE_FALLBACK_KEY, "false");
		final StringBuilder builder = new StringBuilder("$systemName=SampleClassification");
		builder.append("\n").append("$systemVersion=1.0").append("\n");
		builder.append("$YCL=system='$systemName',version='$systemVersion',");
		builder.append("translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;");
		builder.append("\n").append("UPDATE Product;code[unique=true];@lanSpeed[$YCL];");
		builder.append("catalogVersion[unique=true](catalog(id),version)[virtual=true,default=hwcatalog:Online]").append("\n");
		builder.append(";HW2200-0878;SomeNonExistendOne").append("\n");
		builder.append("$sysVer=systemVersion(catalog(id[default=$systemName]),version[default=$systemVersion]);").append("\n");
		builder
				.append("INSERT_UPDATE ClassificationAttributeValue;code[unique=true]; name[lang=de]; name[lang=en]; $sysVer[virtual=true,unique=true];");
		builder.append("\n").append(";SomeNonExistendOne; SomeNonExistendOne; SomeNonExistendOne").append("\n");
		builder
				.append("INSERT_UPDATE ClassificationAttribute;code[unique=true];name[lang=de]; name[lang=en]; defaultAttributeValues(code,$sysVer);$sysVer[virtual=true,unique=true]");
		builder.append("\n").append("; lanSpeed ; LAN Geschwindigkeit ; LAN speed ; L10, L100, L1G, SomeNonExistendOne");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(builder.toString());
		importConfig.setRemoveOnSuccess(false);

		// when
		final ImportResult result = importService.importData(importConfig);

		// then
		assertThat(result).isNotNull();
		assertThat(result.isFinished()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(result.isError()).isFalse();

		final ProductModel product = productService.getProductForCode("HW2200-0878");
		final FeatureList features = classificationService.getFeatures(product);
		final Feature feature = features.getFeatureByCode("SampleClassification/1.0/boards.lanspeed");

		assertThat(feature).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValue().getValue()).isInstanceOf(ClassificationAttributeValueModel.class);
		assertThat(((ClassificationAttributeValueModel) feature.getValue().getValue()).getCode()).isEqualTo("SomeNonExistendOne");
	}

	@Test
	public void testShouldImportClassificationUnitWithInlineOrDefaultUnitValue()
	{
		final String lines = "$catalogName=hwcatalog\r\n"
				+ "$versionName=Online\r\n"
				+ "$systemName=SampleClassification\r\n"
				+ "$systemVersion=1.0\r\n"
				+ "$catalogversion=catalogversion(catalog(id[default='$catalogVersion']),version[default='$versionName'])[unique=true,default='$catalogName:$versionName']\r\n"
				+ "$YCL=system='$systemName',version='$systemVersion',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator\r\n"
				+ "UPDATE Product;code[unique=true];$catalogversion;@clockSpeed(unit[default='ghz'])[$YCL,lang='de']\r\n"
				+ ";HW2110-0012;hwcatalog:Online;11:ghz\r\n" + ";HW2110-0019;hwcatalog:Online;12:mhz\r\n"
				+ ";HW2110-0027;hwcatalog:Online;13\r\n" + ";HW2110-0029;hwcatalog:Online;14:wrongUnit\r\n"
				+ "UPDATE Product;code[unique=true];$catalogversion;@clockSpeed(unit[default='wrongUnit'])[$YCL,lang='de']\r\n"
				+ ";HW2120-0341;hwcatalog:Online;15\r\n";

		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(lines);
		importConfig.setRemoveOnSuccess(false);

		// when
		final ImportResult result = importService.importData(importConfig);

		// then
		assertThat(result).isNotNull();
		assertThat(result.isFinished()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(result.isError()).isFalse();

		final ProductModel product1 = productService.getProductForCode("HW2110-0012");
		final ProductModel product2 = productService.getProductForCode("HW2110-0019");
		final ProductModel product3 = productService.getProductForCode("HW2110-0027");
		final ProductModel product4 = productService.getProductForCode("HW2110-0029");
		final ProductModel product5 = productService.getProductForCode("HW2120-0341");

		final FeatureList features1 = classificationService.getFeatures(product1);
		final Feature feature1 = features1.getFeatureByCode("SampleClassification/1.0/cpu.clockspeed");
		final FeatureList features2 = classificationService.getFeatures(product2);
		final Feature feature2 = features2.getFeatureByCode("SampleClassification/1.0/cpu.clockspeed");
		final FeatureList features3 = classificationService.getFeatures(product3);
		final Feature feature3 = features3.getFeatureByCode("SampleClassification/1.0/cpu.clockspeed");
		final FeatureList features4 = classificationService.getFeatures(product4);
		final Feature feature4 = features4.getFeatureByCode("SampleClassification/1.0/cpu.clockspeed");
		final FeatureList features5 = classificationService.getFeatures(product5);
		final Feature feature5 = features5.getFeatureByCode("SampleClassification/1.0/cpu.clockspeed");

		// both default and inline unit are set to the same value
		assertThat(feature1).isNotNull();
		assertThat(feature1.getValues()).hasSize(1);
		assertThat(feature1.getValue().getValue()).isInstanceOf(Double.class);
		assertThat(feature1.getValue().getValue()).isEqualTo(Double.valueOf(11d));
		assertThat(feature1.getValue().getUnit()).isInstanceOf(ClassificationAttributeUnitModel.class);
		assertThat(feature1.getValue().getUnit().getCode()).isEqualTo("ghz");

		// both default and inline unit are set to different units (inline unit should be used) 
		assertThat(feature2).isNotNull();
		assertThat(feature2.getValues()).hasSize(1);
		assertThat(feature2.getValue().getValue()).isInstanceOf(Double.class);
		assertThat(feature2.getValue().getValue()).isEqualTo(Double.valueOf(12d));
		assertThat(feature2.getValue().getUnit()).isInstanceOf(ClassificationAttributeUnitModel.class);
		assertThat(feature2.getValue().getUnit().getCode()).isEqualTo("mhz");

		// inline unit is empty (default unit should be used)
		assertThat(feature3).isNotNull();
		assertThat(feature3.getValues()).hasSize(1);
		assertThat(feature3.getValue().getValue()).isInstanceOf(Double.class);
		assertThat(feature3.getValue().getValue()).isEqualTo(Double.valueOf(13d));
		assertThat(feature3.getValue().getUnit()).isInstanceOf(ClassificationAttributeUnitModel.class);
		assertThat(feature3.getValue().getUnit().getCode()).isEqualTo("ghz");

		// inline unit is wrong (default unit should be used)
		assertThat(feature4).isNotNull();
		assertThat(feature4.getValues()).hasSize(1);
		assertThat(feature4.getValue().getValue()).isInstanceOf(Double.class);
		assertThat(feature4.getValue().getValue()).isEqualTo(Double.valueOf(14d));
		assertThat(feature4.getValue().getUnit()).isInstanceOf(ClassificationAttributeUnitModel.class);
		assertThat(feature4.getValue().getUnit().getCode()).isEqualTo("ghz");

		// default unit is wrong and inline unit is not specified (default unit (from attribute assignment) should be used)
		assertThat(feature5).isNotNull();
		assertThat(feature5.getValues()).hasSize(1);
		assertThat(feature5.getValue().getValue()).isInstanceOf(Double.class);
		assertThat(feature5.getValue().getValue()).isEqualTo(Double.valueOf(15d));
		assertThat(feature5.getValue().getUnit()).isInstanceOf(ClassificationAttributeUnitModel.class);
		assertThat(feature5.getValue().getUnit().getCode()).isEqualTo(feature5.getClassAttributeAssignment().getUnit().getCode());
	}

	@Test
	public void testMultiThreadedImport() throws InterruptedException
	{
		final String lines = "$catalogVersion=catalogversion(catalog(id[default='hwcatalog']),version[default='Online'])[unique=true,default='hwcatalog:Online']\n"
				+ //
				"$feature1=@weight[system='SampleClassification',version='1.0',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]\n"
				+ //
				"INSERT_UPDATE Product;code[unique=true];$feature1;$catalogVersion;\n" + //
				";HW2110-0012;10g\n" + //
				";HW2110-0019;20g\n" + //
				";HW2110-0027;30g\n" + //
				";HW2110-0029;40g\n" + //
				";HW2120-0341;50g\n";//

		for (int i = 0; i < 10; i++)
		{
			final ImportConfig importConfig = new ImportConfig();
			importConfig.setScript(lines);
			importConfig.setMaxThreads(4);
			importConfig.setSynchronous(false);
			importConfig.setRemoveOnSuccess(false);

			final ImportResult result = importService.importData(importConfig);
			waitForFinish(result, 30);
			assertImportResult(result);

			assertFeatureValue("HW2110-0012", "SampleClassification/1.0/electronics.weight", new Double("10"));
			assertFeatureValue("HW2110-0019", "SampleClassification/1.0/electronics.weight", new Double("20"));
			assertFeatureValue("HW2110-0027", "SampleClassification/1.0/electronics.weight", new Double("30"));
			assertFeatureValue("HW2110-0029", "SampleClassification/1.0/electronics.weight", new Double("40"));
			assertFeatureValue("HW2120-0341", "SampleClassification/1.0/electronics.weight", new Double("50"));
		}
	}

	void assertImportResult(final ImportResult result)
	{
		assertThat(result).isNotNull();
		assertThat(result.isFinished()).isTrue();
		if (result.hasUnresolvedLines())
		{
			try
			{
				final BufferedReader reader = new BufferedReader(new InputStreamReader(mediaService.getStreamFromMedia(result
						.getUnresolvedLines()), "utf-8"));
				try
				{
					System.err.println("--------------------------------------------------------");
					System.err.println("dumping unresolved lines:");
					for (String line = reader.readLine(); line != null; line = reader.readLine())
					{
						System.err.println(line);
					}
					System.err.println("--------------------------------------------------------");
				}
				catch (final IOException e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					IOUtils.closeQuietly(reader);
				}
			}
			catch (final UnsupportedEncodingException e)
			{
				throw new RuntimeException(e);
			}
			Assert.fail("got unreolved lines - see console");
		}
		assertThat(result.isError()).isFalse();
	}

	void assertFeatureValue(final String productCode, final String featureId, final Object value)
	{
		final ProductModel product = productService.getProductForCode(productCode);
		final FeatureList features = classificationService.getFeatures(product);
		final Feature feature = features.getFeatureByCode(featureId);
		assertThat(feature).isNotNull();
		assertThat(feature.getValues()).hasSize(1);
		assertThat(feature.getValue().getValue()).isInstanceOf(value.getClass());
		assertThat(feature.getValue().getValue()).isEqualTo(value);
	}

	void waitForFinish(final ImportResult result, final int timeoutSec) throws InterruptedException
	{
		final long max = System.currentTimeMillis() + (timeoutSec * 1000);
		do
		{
			Thread.sleep(100);
		}
		while (!result.isFinished() && System.currentTimeMillis() < max);

		assertThat(result.isFinished()).isTrue();
	}
}
