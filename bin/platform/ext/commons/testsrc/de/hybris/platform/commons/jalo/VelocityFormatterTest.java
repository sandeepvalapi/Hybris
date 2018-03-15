/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.commons.jalo;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;


@IntegrationTest
public class VelocityFormatterTest extends ServicelayerBaseTest
{
	private static final String TEMPLATE_VM = "template.vm";
	private static final String TEST_VELOCITY_FORMATTER = "testVelocityFormatter";

	@Test
	public void shouldNotAllowJavaReflectionAPICalls() throws IOException, JaloBusinessException
	{
		// given
		final VelocityFormatter formatter = createVelocityFormatter();

		// when
		// formatter is used also as content provider for velocity template
		final Media ret = formatter.format(formatter);
		final String line = FileUtils.readFileToString(ret.getFile());

		// then
		assertThat(line).isEqualTo("${systemClass.exit(1)}\nThis is " + TEST_VELOCITY_FORMATTER);
	}

	private VelocityFormatter createVelocityFormatter() throws IOException
	{
		final Map<String, String> attributes = new HashMap<>();
		attributes.put(VelocityFormatter.CODE, TEST_VELOCITY_FORMATTER);
		attributes.put(VelocityFormatter.LOCATION, createVelocityTemplateFile().getName());

		return CommonsManager.getInstance().createVelocityFormatter(attributes);
	}

	private File createVelocityTemplateFile() throws IOException
	{
		final File file = new File(ConfigUtil.getPlatformConfig(this.getClass()).getSystemConfig().getDataDir().getAbsolutePath()
				+ File.separator + "media" + File.separator + "sys_junit", TEMPLATE_VM);
		file.deleteOnExit();
		FileUtils.writeStringToFile(file,
				"#set( $str = \"\" )\n#set( $systemClass = ${str.getClass().forName( \"java.lang.System\" )} )\n${systemClass.exit(1)}\n"
						+ "This is $this.code");
		return file;
	}
}
