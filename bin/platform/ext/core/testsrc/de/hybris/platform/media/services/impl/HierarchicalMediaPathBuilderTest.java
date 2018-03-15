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
package de.hybris.platform.media.services.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class HierarchicalMediaPathBuilderTest
{
	private static final String FOLDER_PATH = "sys_master/root/";
	private static final String MEDIA_ID = "12345678.jpg";

	@Test
	public void shouldBuildFolderPathWithoutSubfoldersWhenDepthIsZero()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(0);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/");
	}

	@Test
	public void shouldBuildFolderPathWithoutSubfoldersWhenDepthIsGreaterThanFour()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(5);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/");
	}

	@Test
	public void shouldBuildFolderPathPlus1LevelSubPath()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(1);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/hd1/");
	}

	@Test
	public void shouldBuildFolderPathPlus2LevelSubPath()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(2);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/hd1/hc2/");
	}

	@Test
	public void shouldBuildFolderPathPlus3LevelSubPath()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(3);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/hd1/hc2/h00/");
	}

	@Test
	public void shouldBuildFolderPathPlus4LevelSubPath()
	{
		// given
		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(4);

		// when
		final String path = pathBuilder.buildPath(FOLDER_PATH, MEDIA_ID);

		// then
		assertThat(path).isNotNull().isEqualTo("sys_master/root/hd1/hc2/h00/h00/");
	}

}
