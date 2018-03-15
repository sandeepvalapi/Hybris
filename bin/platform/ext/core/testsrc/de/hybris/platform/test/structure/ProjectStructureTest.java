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
package de.hybris.platform.test.structure;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import org.apache.log4j.Logger;
import org.fest.assertions.BasicDescription;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class ProjectStructureTest extends ServicelayerBaseTest
{

	List<List<String>> duplicateJarPairsToIgnore = new ArrayList<>();

	@Before
	public void setUp()
	{
		final Map<String, String> ignoredMD5 = Registry.getCurrentTenantNoFallback().getConfig()
				.getParametersMatching("duplicatejarchecker\\.ignore\\.(.*)", true);
		final Set<String> md5Pairs = ignoredMD5.keySet();
		for (final String md5Pair : md5Pairs)
		{
			final List<String> partialPaths = Arrays.asList(md5Pair.split("_____"));
			assertThat(partialPaths).hasSize(2).overridingErrorMessage("Duplicate jars checker parameter should be in expected format duplicatejarchecker.ignore.partialPathToFirstLib_____partialPathToSecondLib \n");

			duplicateJarPairsToIgnore.add(partialPaths);

		}
	}

	@Test
	public void testDuplicateJarFiles() throws IOException, NoSuchAlgorithmException
	{

		final Set<String> dirToParse = new HashSet<>();

		final List<ExtensionInfo> allExtensionsInfo = ConfigUtil.getPlatformConfig(ProjectStructureTest.class).getExtensionInfosInBuildOrder();

		dirToParse.addAll(allExtensionsInfo.stream().map(extensionInfo -> extensionInfo.getExtensionDirectory().toString()).collect(Collectors.toList()));

		final List<Path> jarList = new ArrayList<>();
		for (final String dir : dirToParse)
		{
			Files.walk(Paths.get(dir)).filter(Files::isRegularFile).filter(p -> p.toAbsolutePath().toString().contains("lib"))
					.filter(p -> !p.toAbsolutePath().toString().contains("platform" + File.separator + "resources"))
					.filter(p -> !p.toAbsolutePath().toString().contains("platform" + File.separator + "apache-ant"))
					.filter(p -> p.getFileName().toString().endsWith(".jar")).forEach(path -> jarList.add(path));
		}

		final List<List<String>> listOfConflicts = new ArrayList<>();

		for (int i = 0; i < jarList.size(); i++)
		{
			for (int j = i + 1; j < jarList.size(); j++)
			{
				final String filename1 = jarList.get(i).getFileName().toString();
				final String filename2 = jarList.get(j).getFileName().toString();

				final Pattern p = Pattern.compile("-(\\d*[^a-zA-Z])+");
				final Matcher m = p.matcher(filename1);
				Integer index1 = null;
				Integer index2 = null;

				if (m.find()) {
					index1 = m.start();
				}

				final Matcher m2 = p.matcher(filename2);
				if (m2.find()) {
					index2 = m2.start();
				}

				final String parsedFileName1 = (index1 != null) ? filename1.substring(0, index1) : filename1;
				final String parsedFileName2 = (index2 != null) ? filename2.substring(0, index2) : filename2;

				if (parsedFileName1.equals(parsedFileName2))
				{

					if (!(jarList.get(i).toString().contains("WEB-INF") && jarList.get(j).toString().contains("WEB-INF")))
					{

						final List<String> conflict = new ArrayList<>();
						conflict.add(jarList.get(i).toString());
						conflict.add(jarList.get(j).toString());
						listOfConflicts.add(conflict);
					}
				}
			}
		}

		final List<List<String>> copyOfListConflicts = new ArrayList<>(listOfConflicts);


		for (int K = 0; K < listOfConflicts.size(); K++){

			final List<String> duplicatePairFound = listOfConflicts.get(K);
			final String found1 = duplicatePairFound.get(0);
			final String found2 = duplicatePairFound.get(1);

			for (final List<String> duplicateToIgnore : duplicateJarPairsToIgnore)
			{
				final String pathToIgnore1 = duplicateToIgnore.get(0);
				final String pathToIgnore2 = duplicateToIgnore.get(1);

				if ((found1.contains(pathToIgnore1) && found2.contains(pathToIgnore2))
						||  found1.contains(pathToIgnore2) && found2.contains(pathToIgnore1))

				{
					copyOfListConflicts.remove(duplicatePairFound);
				}

			}
		}

		assertThat(copyOfListConflicts).hasSize(0).as(new BasicDescription("Can be overridden in project.properties"));

	}

}