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
package de.hybris.platform.hac.custom;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.InputStreamResource;


@UnitTest
public class DefaultHacMenuProviderTest
{

	private static final Logger LOG = Logger.getLogger(DefaultHacMenuProviderTest.class);


	private String config = "[ {\n" +
			"  \"basePath\" : \"/platform\",\n" +
			"  \"mainTabLabel\" : \"platform\",\n" +
			"  \"subTabs\" : [ {\n" +
			"    \"path\" : \"/tenants/\",\n" +
			"    \"label\" : \"tenants\",\n" +
			"    \"skipPrefix\" : true\n" +
			"  }]\n" +
			"}, {\n" +
			"  \"basePath\" : \"/maintain\",\n" +
			"  \"mainTabLabel\" : \"maintenance\",\n" +
			"  \"subTabs\" : [ {\n" +
			"    \"path\" : \"/cleanup\",\n" +
			"    \"label\" : \"cleanup\",\n" +
			"    \"skipPrefix\" : false\n" +
			"  }, {\n" +
			"    \"path\" : \"/keys\",\n" +
			"    \"label\" : \"encryption keys\",\n" +
			"    \"skipPrefix\" : false\n" +
			"  } ]\n" +
			"} ]\n";


	private String invalidConfig = "\n" +
			"  \"basePath\" : \"/platform\",\n" +
			"  \"mainTabLabel\" : \"platform\",\n" +
			"  \"subTabs\" : [ {\n" +
			"    \"path\" : \"/tenants/\",\n" +
			"    \"label\" : \"tenants\",\n" +
			"    \"skipPrefix\" : true\n" +
			"  }]\n" +
			"}, {\n" +
			"  \"basePath\" : \"/maintain\",\n" +
			"  \"mainTabLabel\" : \"maintenance\",\n" +
			"  \"subTabs\" : [ {\n" +
			"    \"path\" : \"/cleanup\",\n" +
			"    \"label\" : \"cleanup\",\n" +
			"    \"skipPrefix\" : false\n" +
			"  }, {\n" +
			"    \"path\" : \"/keys\",\n" +
			"    \"label\" : \"encryption keys\",\n" +
			"    \"skipPrefix\" : false\n" +
			"  } ]\n" +
			"} ]\n";


	private JsonHacConfiguration createCustomTabConfiguration(final String json)
	{
		final JsonHacConfiguration jsonHacConfiguration = new JsonHacConfiguration();
		InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
		jsonHacConfiguration.setResource(new InputStreamResource(stream));
		jsonHacConfiguration.initialize();

		return  jsonHacConfiguration;
	}

	@Test
	public void shouldMapJsonToObject() throws IOException
	{
		final JsonHacConfiguration jsonHacConfiguration = createCustomTabConfiguration(config);

		final List<CustomTabInfo> info = jsonHacConfiguration.getInfo();

		assertThat(info).hasSize(2);

		final CustomTabInfo platformTab = info.get(0);
		final CustomTabInfo maintenanceTab = info.get(1);

		assertThat(platformTab.getBasePath()).isEqualTo("/platform");
		assertThat(platformTab.getMainTabLabel()).isEqualTo("platform");
		assertThat(platformTab.getSubTabs()).hasSize(1);
		assertThat(platformTab.getSubTabs().get(0).getLabel()).isEqualTo("tenants");

		assertThat(maintenanceTab.getBasePath()).isEqualTo("/maintain");
		assertThat(maintenanceTab.getMainTabLabel()).isEqualTo("maintenance");
		assertThat(maintenanceTab.getSubTabs()).hasSize(2);
		assertThat(maintenanceTab.getSubTabs().get(0).getPath()).isEqualTo("/cleanup");
		assertThat(maintenanceTab.getSubTabs().get(1).getPath()).isEqualTo("/keys");
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowException() throws IOException
	{
		final JsonHacConfiguration jsonHacConfiguration = createCustomTabConfiguration(invalidConfig);

		jsonHacConfiguration.getInfo();
	}
}
