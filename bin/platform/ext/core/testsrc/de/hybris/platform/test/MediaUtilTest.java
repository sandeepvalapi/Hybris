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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.util.MediaUtil;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Intention of this test is to cover all lightweight methods which DO NOT start platform/tenant. Other methods are
 * tested in separate tests. If you like to test any method which must startup tenant please use
 * <code>MediaUtilIntegrationTest</code>.
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class MediaUtilTest
{

	@Mock
	private MediaFolder mediaFolder;

	@Test
	public void shouldNormalizeRealFileNameAndReplaceAllNonAlphabeticCharactersToSingleDash()
	{
		// given
		final String fileName = "abc_ABC_123--!@#$%^&*()+ąśćńółż---@\"\\def.jpg";

		// when
		final String normalizedFileName = MediaUtil.normalizeRealFileName(fileName);

		// then
		assertThat(normalizedFileName).isNotNull().isEqualTo("abc-ABC-123-def.jpg");
	}


	@Test
	public void shouldRemoveFileExtsionFromFileName()
	{
		// given
		final String fileName = "fooBar.jpg";

		// when
		final String baseFileName = MediaUtil.removeFileExtension(fileName);

		// then
		assertThat(baseFileName).isNotEmpty().isEqualTo("fooBar");
	}


	@Test
	public void shouldNotTouchFileNameOnExtensionRemovalWhenFileNameDoesNotHaveExtension()
	{
		// given
		final String fileName = "fooBar";

		// when
		final String baseFileName = MediaUtil.removeFileExtension(fileName);

		// then
		assertThat(baseFileName).isNotEmpty().isEqualTo(fileName);
	}


	@Test
	public void shouldThrowIllegalArgumentExceptionWhenTryingToRemoveExtensionFromNullFileName()
	{
		// given
		final String fileName = null;

		try
		{
			// when
			MediaUtil.removeFileExtension(fileName);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("fileName is required!");
		}
	}

	@Test
	public void shouldAddLeadingFileSerparatorToPath()
	{
		// given
		final String path = "foobar";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addLeadingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isNotEmpty().isEqualTo(MediaUtil.FILE_SEP + path);
	}

	@Test
	public void shouldNotAddLeadingFileSerparatorToPathWhenPathAlreadyContainIt()
	{
		// given
		final String path = "/foobar";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addLeadingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isNotEmpty().isEqualTo(path);
	}


	@Test
	public void shouldReturnEmptyPathOnTryingToAddLeadingFileSeparatorToEmptyPath()
	{
		// given
		final String path = "";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addLeadingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isEmpty();
	}

	@Test
	public void shouldReturnEmptyPathOnTryingToAddLeadingFileSeparatorToNullPath()
	{
		// given
		final String path = null;

		// when
		final String pathWithLeadingFileSep = MediaUtil.addLeadingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isEmpty();
	}

	@Test
	public void shouldAddTrailingFileSerparatorToPath()
	{
		// given
		final String path = "foobar";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addTrailingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isNotEmpty().isEqualTo(path + MediaUtil.FILE_SEP);
	}

	@Test
	public void shouldNotAddTrailingFileSerparatorToPathWhenPathAlreadyContainIt()
	{
		// given
		final String path = "foobar/";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addTrailingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isNotEmpty().isEqualTo(path);
	}


	@Test
	public void shouldReturnEmptyPathOnTryingToAddTrailingFileSeparatorToEmptyPath()
	{
		// given
		final String path = "";

		// when
		final String pathWithLeadingFileSep = MediaUtil.addTrailingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isEmpty();
	}

	@Test
	public void shouldReturnEmptyPathOnTryingToAddTrailingFileSeparatorToNullPath()
	{
		// given
		final String path = null;

		// when
		final String pathWithLeadingFileSep = MediaUtil.addTrailingFileSepIfNeeded(path);

		// then
		assertThat(pathWithLeadingFileSep).isEmpty();
	}

	@Test
	public void shouldConcatenateMediaFolderPathToGivenDirectoryAndReturnAsFileObject()
	{
		// given
		given(mediaFolder.getPath()).willReturn("fooBar");
		final File dir = new File("baz");

		// when
		final File fullDir = MediaUtil.concatDirectoryWithFolder(dir, mediaFolder);

		// then
		assertThat(fullDir).isNotNull();
		assertThat(fullDir.getPath()).isEqualTo("baz" + File.separator + "fooBar");
	}


	@Test
	public void shouldReturnDirPathWhenConcatenatingMediaFolderToGivenDirectoryAndMediaFolderIsNull()
	{
		// given
		final MediaFolder mediaFolder = null;
		final File dir = new File("baz");

		// when
		final File fullDir = MediaUtil.concatDirectoryWithFolder(dir, mediaFolder);

		// then
		assertThat(fullDir).isNotNull();
		assertThat(fullDir.getPath()).isEqualTo("baz");
	}


	@Test
	public void shouldReturnFileExtensionFromGivenFileName()
	{
		// given
		final String fileName = "fooBar.jpg";

		// when
		final String fileExtension = MediaUtil.getFileExtension(fileName);

		// then
		assertThat(fileExtension).isNotEmpty().isEqualTo("jpg");
	}

	@Test
	public void shouldReturnLowerCaseFileExtensionFromGivenFileNameIfInitiallyItIsUppercase()
	{
		// given
		final String fileName = "fooBar.JPG";

		// when
		final String fileExtension = MediaUtil.getFileExtension(fileName);

		// then
		assertThat(fileExtension).isNotEmpty().isEqualTo("jpg");
	}

	@Test
	public void shouldReturnBaseFileNameNoCaseChangeOnGetFileExtensionWhenThereIsNoExtension()
	{
		// given
		final String fileName = "fooBar";

		// when
		final String fileExtension = MediaUtil.getFileExtension(fileName);

		// then
		assertThat(fileExtension).isNotEmpty().isEqualTo(fileName);
	}


	@Test
	public void shouldComposeFileFromParentAndChildIfChildIsNotEmpty()
	{
		// given
		final File parent = new File("foo");
		final String child = "bar";

		// when
		final File composedFile = MediaUtil.composeOrGetParent(parent, child);

		// then
		assertThat(composedFile).isNotNull();
		assertThat(composedFile.getParent()).isEqualTo("foo");
		assertThat(composedFile.getName()).isEqualTo("bar");
	}

	@Test
	public void shouldComposeFileFromParentOnlyIfChildIsEmpty()
	{
		// given
		final File parent = new File("foo");
		final String child = "";

		// when
		final File composedFile = MediaUtil.composeOrGetParent(parent, child);

		// then
		assertThat(composedFile).isNotNull();
		assertThat(composedFile.getParent()).isNull();
		assertThat(composedFile.getName()).isEqualTo("foo");
	}

	@Test
	public void shouldFailWhenEffectiveFileIsNotInTheParrentDirectory()
	{
		final File parent = new File("foo");
		final String child = Paths.get("..", "..", "etc", "passwd").toString();

		try
		{
			MediaUtil.composeOrGetParent(parent, child);
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(IllegalArgumentException.class);
			assertThat(e.getMessage()).isEqualTo("Effective path to the child and the parent directory don't match to each other.");
			return;
		}
		fail("Exception should be thrown.");
	}

	@Test
	public void shouldNotFailWhenEffectiveFileIsInTheParrentDirectory()
	{
		final File parent = new File("foo");
		final String child = Paths.get("..", "foo", "mydir", "mymedia").toString();

		final File composedFile = MediaUtil.composeOrGetParent(parent, child);

		assertThat(composedFile).isNotNull();
		assertThat(composedFile.getParent()).isEqualTo(Paths.get("foo", "..", "foo", "mydir").toString());
		assertThat(composedFile.getName()).isEqualTo("mymedia");
	}
}
