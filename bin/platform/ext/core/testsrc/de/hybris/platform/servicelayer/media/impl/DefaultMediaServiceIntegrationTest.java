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
package de.hybris.platform.servicelayer.media.impl;

import static de.hybris.platform.testframework.assertions.InputStreamAssert.assertThat;
import static de.hybris.platform.testframework.assertions.MediaAssert.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


@IntegrationTest
public class DefaultMediaServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private MediaStorageConfigService mediaStorageConfigService;
	@Resource
	private MediaStorageRegistry mediaStorageRegistry;
	@Resource
	private ModelService modelService;
	@Resource
	private DefaultMediaService mediaService;

	private MediaStorageStrategy rootStorageStrategy;
	private MediaModel media1, media2, media3;
	private MediaFolderModel sampleFolder;
	private byte[] randomBytes;
	private CatalogVersionModel catalogVersion;
	private MediaStorageConfigService.MediaFolderConfig rootFolderConfig, sampleFolderConfig;
	private CatalogModel catalog;

	private UserModel permittedUser;
	private UserModel deniedUser;

	private final static String DERIVED_MEDIA_THUMBNAIL = "thumbnail";

	@Before
	public void setUp() throws Exception
	{
		randomBytes = getRandomBytes();

		rootFolderConfig = mediaStorageConfigService.getConfigForFolder(mediaService.getRootFolder().getQualifier());
		rootStorageStrategy = mediaStorageRegistry.getStorageStrategyForFolder(rootFolderConfig);


		catalog = this.modelService.create(CatalogModel.class);
		catalog.setId("my_favorite_catalog");
		modelService.save(catalog);

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("tolle_version");
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		sampleFolder = prepareMediaFolder();
		sampleFolderConfig = mediaStorageConfigService.getConfigForFolder(sampleFolder.getQualifier());
		media1 = prepareMedia("media1", mediaService.getRootFolder());
		media2 = prepareMedia("media2", mediaService.getRootFolder());
		media3 = prepareMedia("media3", sampleFolder);

		modelService.saveAll(media1, media2, media3);

		permittedUser = prepareUser();
		deniedUser = prepareUser();
		modelService.saveAll(permittedUser, deniedUser);

		AccessManager.getInstance().getOrCreateUserRightByCode("read");
	}

	@After
	public void tearDown() throws Exception
	{
		modelService.remove(media1);
		modelService.remove(media2);
		modelService.remove(media3);
		modelService.remove(sampleFolder);
		modelService.remove(catalogVersion);
		modelService.remove(catalog);
	}

	private byte[] getRandomBytes()
	{
		return RandomStringUtils.randomAlphabetic(1024).getBytes();
	}

	private MediaModel prepareMedia(final String code, final MediaFolderModel folder)
	{
		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode(code);
		media.setCatalogVersion(catalogVersion);
		media.setFolder(folder);

		return media;
	}

	private UserModel prepareUser()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		return user;

	}

	private MediaFolderModel prepareMediaFolder()
	{
		final MediaFolderModel folder = modelService.create(MediaFolderModel.class);
		folder.setQualifier("fooBar");
		folder.setPath("fooBar");
		modelService.save(folder);

		return folder;
	}

	@Test
	public void shouldConnectDataFromOneMediaToAnotherIfBothMediasAreInTheSameFolder() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		assertThat(mediaService.hasData(media2)).isFalse();

		// when
		mediaService.copyData(media1, media2);

		// then
		assertThat(mediaService.hasData(media1)).isTrue();
		assertThat(mediaService.hasData(media2)).isTrue();
		assertThat(mediaService.getStreamFromMedia(media2)).hasSameDataAs(randomBytes);
		assertThat(media1).hasSameMetaDataAs(media2);
		assertThat(media1).hasSameDataPkAs(media2);
	}

	@Test
	public void shouldCopyDataFromOneMediaToAnotherIfBothMediasAreInDifferentFolders() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		assertThat(mediaService.hasData(media3)).isFalse();

		// when
		mediaService.copyData(media1, media3);

		// then
		assertThat(mediaService.hasData(media1)).isTrue();
		assertThat(mediaService.hasData(media3)).isTrue();
		assertThat(mediaService.getStreamFromMedia(media3)).hasSameDataAs(randomBytes);
		assertThat(media1).hasSameSizeAs(media3);
		assertThat(media1).hasSameMimeAs(media3);
		assertThat(media1).hasSameRealFileNameAs(media3);
	}

	@Test
	public void shouldCopyUrlFromOneMediaToAnotherIfSourceMediaIsUrlBasedAndTargetMediaHasData() throws Exception
	{
		// given
		mediaService.setUrlForMedia(media1, "http://foo.bar/baz.jpg");
		mediaService.setStreamForMedia(media2, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		// when
		mediaService.copyData(media1, media2);

		// then
		assertThat(mediaService.hasData(media2)).isFalse();
		assertThat(media2).hasSameInternalUrlAs(media1);
	}

	@Test
	public void shouldThrowIllegalStateExceptionOnCopyingDataWhenSourceMediaHasNotDataAndEmptyUrl() throws Exception
	{
		try
		{
			// when
			mediaService.copyData(media1, media2);
			fail("Should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldCopyUrlFromOneMediaToAnotherIfSourceMediaIsUrlBasedAndTargetMediaHasNoData() throws Exception
	{
		// given
		mediaService.setUrlForMedia(media1, "http://foo.bar/baz.jpg");

		// when
		mediaService.copyData(media1, media2);

		// then
		assertThat(mediaService.hasData(media2)).isFalse();
		assertThat(media2).hasSameInternalUrlAs(media1);
	}

	@Test
	public void shouldMoveDataFromOneMediaToAnother() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		assertThat(mediaService.hasData(media1)).isTrue();
		assertThat(mediaService.hasData(media3)).isFalse();

		// when
		mediaService.moveData(media1, media3);

		// then
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(mediaService.hasData(media3)).isTrue();
		assertThat(mediaService.getStreamFromMedia(media3)).hasSameDataAs(randomBytes);
	}

	@Test
	public void shouldReturnMediaDataAsListOfFiles() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		// when
		final Collection<File> files = mediaService.getFiles(media1);

		// then
		assertThat(files).isNotEmpty().hasSize(1);
		final File file = Iterables.get(files, 0);
		final FileInputStream fileInputStream = new FileInputStream(file);
		assertThat(fileInputStream).hasSameDataAs(randomBytes);
	}

	@Test
	public void shouldThrowNoDataAvailableExceptionWhenAnyExceptionOccursDuringObtainingDataAsFiles()
	{
		// given
		media1.setInternalURL(MediaUtil.URL_HAS_DATA);

		try
		{
			// when
			mediaService.getFiles(media1);
			fail("should throw NoDataAvailableException");
		}
		catch (final NoDataAvailableException e)
		{
			// then OK
		}
	}

	private InputStream getSampleInputStream(final byte[] data)
	{
		return new DataInputStream(new ByteArrayInputStream(data));
	}

	@Test
	public void shouldSetStreamDataToMedia()
	{
		// given
		final InputStream inputStream = getSampleInputStream(randomBytes);

		// when
		mediaService.setStreamForMedia(media1, inputStream);

		// then
		assertThat(mediaService.getStreamFromMedia(media1)).hasSameDataAs(randomBytes);
		assertThat(media1.getFolder().getQualifier()).isEqualToIgnoringCase("root");
	}

	@Test
	public void shouldSetUrlForSavedModelWhichDoesNotContainAnyData() throws Exception
	{
		// given
		final String url = "/foo/bar";

		// when
		media1.setURL(url);
		modelService.save(media1);

		// then
		assertThat(media1.getURL()).isEqualTo("/foo/bar");
		assertThat(mediaService.hasData(media1)).isFalse();
	}

	@Test
	public void shouldSetUrlForNonSavedModel() throws Exception
	{
		// given
		final MediaModel media = prepareMedia("myCode", sampleFolder);
		final String url = "/foo/bar";

		// when
		media.setURL(url);

		// then
		assertThat(media.getURL()).isEqualTo("/foo/bar");
		assertThat(mediaService.hasData(media)).isFalse();
	}

	@Test
	public void shouldSetUrlForMediaWhichContainsDataAndShouldRemoveItsDataIfItIsNotReferenced() throws Exception
	{
		// given
		final String url = "/foo/bar";
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		final String oldLocation = media1.getLocation();


		// when
		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public <T> T execute() throws Exception
			{
				media1.setURL(url);
				modelService.save(media1);
				return null;
			}
		});


		// then
		modelService.refresh(media1);
		assertThat(media1.getURL()).isEqualTo("/foo/bar");
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(isMediaDataExistInStorage(oldLocation, rootFolderConfig)).isFalse();
	}

	@Test
	public void shouldSetUrlForMediaWhichContainsDataAndShouldNotRemoveItsDataIfItIsReferenced() throws Exception
	{
		// given
		final String url = "/foo/bar";
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.copyData(media1, media2);
		final String oldLocation = media1.getLocation();


		// when
		final Transaction tx = Transaction.current();
		tx.execute(new TransactionBody()
		{
			@Override
			public <T> T execute() throws Exception
			{
				media1.setURL(url);
				modelService.save(media1);
				return null;
			}
		});


		// then
		modelService.refresh(media1);
		assertThat(media1.getURL()).isEqualTo("/foo/bar");
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(mediaService.hasData(media2)).isTrue();
		assertThat(isMediaDataExistInStorage(oldLocation, rootFolderConfig)).isTrue();
	}

	private boolean isMediaDataExistInStorage(final String location,
			final MediaStorageConfigService.MediaFolderConfig folderConfig)
	{
		// TODO We should enrich StorageStrategy with method like exist(config, location)

		InputStream asStream = null;
		try
		{
			asStream = rootStorageStrategy.getAsStream(folderConfig, location);
			return asStream != null;
		}
		catch (final Exception e)
		{
			return false;
		}
		finally
		{
			IOUtils.closeQuietly(asStream);
		}
	}

	@Test
	public void shouldReturnMediaDataAsByteArray()
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		// when
		final byte[] data = mediaService.getDataFromMedia(media1);

		// then
		assertThat(data).isNotEmpty().isEqualTo(randomBytes);
	}

	@Test
	public void shouldReturnMediaDataAsStreamWhenMediaHasRealData()
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		// when
		final InputStream stream = mediaService.getStreamFromMedia(media1);

		// then
		assertThat(stream).hasSameDataAs(randomBytes);
	}

	@Test
	public void shouldReturnMediaDataAsStreamWhenMediaHasOnlyUrl() throws Exception
	{
		// given
		mediaService.setUrlForMedia(media1, getUrlForTestFileInClassloader());

		// when
		final InputStream stream = mediaService.getStreamFromMedia(media1);

		// then
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(stream).isNotNull();
	}

	private String getUrlForTestFileInClassloader()
	{
		final URL resource = getClass().getResource("/servicelayer/test/sampleMediaFile.txt");
		assertThat(resource).overridingErrorMessage("test file /servicelayer/test/sampleMediaFile.txt does not exist").isNotNull();
		return resource.toExternalForm();
	}

	@Test
	public void shouldReturnMediaDataAsStreamWhenMediaHasOnlyUrlWithFROMJARPrefix() throws Exception
	{
		// given
		final String url = MediaUtil.getLocalMediaWebRootUrl() + "/fromjar/servicelayer/test/sampleMediaFile.txt";
		mediaService.setUrlForMedia(media1, url);

		// when
		final InputStream stream = mediaService.getStreamFromMedia(media1);

		// then
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(stream).isNotNull();
	}

	@Test
	public void shouldThrowNoDataAvailableExceptionWhenAnyExceptionOccursDuringObtainingDataAsByteArray()
	{
		// given
		final MediaModel media = new MediaModel();
		media.setLocation("nonexistent");
		media.setInternalURL(MediaUtil.URL_HAS_DATA);

		try
		{
			// when
			mediaService.getDataFromMedia(media);
			fail("should throw NoDataAvailableException");
		}
		catch (final NoDataAvailableException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldThrowNoDataAvailableExceptionWhenAnyExceptionOccursDuringObtainingDataAsStream()
	{
		try
		{
			// when
			mediaService.getStreamFromMedia(media1);
			fail("should throw NoDataAvailableException");
		}
		catch (final NoDataAvailableException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldMoveMediaToAnotherFolder() throws Exception
	{
		// given
		final InputStream inputStream = getSampleInputStream(randomBytes);
		mediaService.setStreamForMedia(media1, inputStream, "fooBar.jpg", "image/jpeg");
		final String oldLocation = media1.getLocation();

		// when
		mediaService.moveMediaToFolder(media1, sampleFolder);

		// then
		assertThat(media1.getFolder()).isEqualTo(sampleFolder);
		assertThat(mediaService.hasData(media1)).isTrue();
		assertThat(mediaService.getStreamFromMedia(media1)).hasSameDataAs(randomBytes);
		assertThat(isMediaDataExistInStorage(oldLocation, rootFolderConfig)).isFalse();
	}

	@Test
	public void shouldAddMediaVersionToExistingMedia()
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		final String versionId = "thumbnail";

		// when
		mediaService.addVersionStreamForMedia(media1, versionId, getSampleInputStream(randomBytes));

		// then
		assertThat(media1.getDerivedMedias()).hasSize(1);
	}

	@Test
	public void shouldPreventAddingMediaVersionWithNotUniqueVersionId()
	{
		// given
		TestUtils.disableFileAnalyzer("Log ERROR in this case is OK");
		final String versionId = "thumbnail";

		try
		{
			// when
			mediaService.addVersionStreamForMedia(media1, versionId, getSampleInputStream(randomBytes));
			mediaService.addVersionStreamForMedia(media1, versionId, getSampleInputStream(randomBytes));
			fail("Should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then OK
		}
		TestUtils.enableFileAnalyzer();
	}

	@Test
	public void shouldReturnDataStreamForExistingMediaVersion()
	{
		// given
		final String versionId = "thumbnail";
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.addVersionStreamForMedia(media1, versionId, getSampleInputStream(randomBytes));

		// when
		final InputStream stream = mediaService.getStreamForMediaVersion(media1, versionId);

		// then
		assertThat(stream).hasSameDataAs(randomBytes);
	}


	@Test
	public void shouldReturnUrlForExistingMediaVersion()
	{
		// given
		final String versionId = "thumbnail";
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.addVersionStreamForMedia(media1, versionId, getSampleInputStream(randomBytes));

		// when
		final String url = mediaService.getUrlForMediaVersion(media1, versionId);

		// then
		assertThat(url).isNotNull().startsWith("/medias/?context");
	}

	@Test
	public void shouldRemoveVersionFromExistingMedia()
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.addVersionStreamForMedia(media1, "thumbnail", getSampleInputStream(randomBytes));
		mediaService.addVersionStreamForMedia(media1, "picture", getSampleInputStream(randomBytes));
		mediaService.addVersionStreamForMedia(media1, "frontend", getSampleInputStream(randomBytes));
		assertThat(media1.getDerivedMedias()).hasSize(3);

		// when
		mediaService.removeVersionForMedia(media1, "picture");

		// then
		assertThat(Iterables.transform(media1.getDerivedMedias(), new Function<DerivedMediaModel, String>()
		{

			@Override
			public String apply(final DerivedMediaModel input)
			{
				assertNotNull(input);
				return input.getVersion();
			}
		})).containsOnly("thumbnail", "frontend");
	}

	@Test
	public void shouldRemoveDataFromMediaByJustClearingMetadataIfItShareItWithAnotherObject() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.copyData(media1, media2);

		// when
		mediaService.removeDataFromMedia(media1);
		final InputStream stream = mediaService.getStreamFromMedia(media2);

		// then
		assertThat(mediaService.hasData(media2)).isTrue();
		assertThat(stream).hasSameDataAs(randomBytes);
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(media1.getLocation()).isNull();
		assertThat(media1.getLocationHash()).isNull();
		assertThat(media1.getDataPK()).isNull();
		assertThat(media1.getSize()).isNull();
		assertThat(media1.getMime()).isNull();
		assertThat(media1.getInternalURL()).isNull();
	}

	@Test
	public void shouldRemoveDataFromMediaCompletelyIfItDoesNotShareItWithAnotherObject() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		try
		{
			// when
			mediaService.removeDataFromMedia(media1);
			mediaService.getStreamFromMedia(media1);
			fail("Should throw NoDataAvailableException");
		}
		catch (final NoDataAvailableException e)
		{
			// OK
		}

		// then
		assertThat(mediaService.hasData(media1)).isFalse();
		assertThat(media1.getLocation()).isNull();
		assertThat(media1.getLocationHash()).isNull();
		assertThat(media1.getDataPK()).isNull();
		assertThat(media1.getSize()).isNull();
		assertThat(media1.getMime()).isNull();
		assertThat(media1.getInternalURL()).isNull();
	}

	@Test
	public void shouldRemoveDataFromStorageOnMediaModelRemovalIfItDoesNotShareItWithAnotherObject() throws Exception
	{
		// given
		final MediaModel media = prepareMedia("toRemove", sampleFolder);
		modelService.save(media);
		mediaService.setStreamForMedia(media, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		final String oldLocation = media.getLocation();

		// when
		modelService.remove(media);

		// then
		assertThat(modelService.isRemoved(media)).isTrue();
		assertThat(isMediaDataExistInStorage(oldLocation, rootFolderConfig)).isFalse();
	}

	@Test
	public void shouldRemoveDataFromStorageOnMediaModelRemovalIfItDoesNotShareItWithAnotherObjectEvenWithMessedLocation()
			throws Exception
	{
		// given
		final MediaModel media = prepareMedia("toRemove", sampleFolder);
		modelService.save(media);
		mediaService.setStreamForMedia(media, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		final String oldLocation = media.getLocation();
		media.setLocation("some-nasty-one");

		// when
		modelService.remove(media);

		// then
		assertThat(modelService.isRemoved(media)).isTrue();
		assertThat(isMediaDataExistInStorage(oldLocation, sampleFolderConfig)).isFalse();
	}

	@Test
	public void shouldNotRemoveDataFromStorageOnMediaModelRemovalIfItShareItWithAnotherObject() throws Exception
	{
		// given
		final MediaModel media = prepareMedia("toRemove", sampleFolder);
		modelService.save(media);
		mediaService.setStreamForMedia(media, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		mediaService.copyData(media, media3);
		final String oldLocation = media.getLocation();

		// when
		modelService.remove(media);

		// then
		assertThat(modelService.isRemoved(media)).isTrue();
		assertThat(isMediaDataExistInStorage(oldLocation, sampleFolderConfig)).isTrue();
		assertThat(mediaService.hasData(media3)).isTrue();
		assertThat(media3.getLocation()).isEqualTo(oldLocation);
	}

	@Test
	public void allowDerivedMediaWithSameVersionForDifferentMedias()
	{
		// given
		final DerivedMediaModel derivedMedia = new DerivedMediaModel();
		derivedMedia.setDataPK(Long.valueOf(1L));
		derivedMedia.setMedia(media1);
		derivedMedia.setVersion(DERIVED_MEDIA_THUMBNAIL);

		final DerivedMediaModel derivedMedia2 = new DerivedMediaModel();
		derivedMedia2.setDataPK(Long.valueOf(2L));
		derivedMedia2.setMedia(media2);
		derivedMedia2.setVersion(DERIVED_MEDIA_THUMBNAIL);

		// when
		modelService.save(derivedMedia);
		modelService.save(derivedMedia2);

		// then
		assertThat(media1.getDerivedMedias()).containsOnly(derivedMedia);
		assertThat(media2.getDerivedMedias()).containsOnly(derivedMedia2);
	}


	@Test
	public void shouldProvideDownloadUrlInModel() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

		// when
		final String modelDownloadURL = media1.getDownloadURL();

		assertThat(modelDownloadURL).isNotEmpty();
		assertThat(modelDownloadURL).isEqualTo(asSource(media1).getDownloadURL());
	}

	private Media asSource(final MediaModel media)
	{
		return modelService.getSource(media);
	}


	@Test
	public void shouldSetFolderToRootIfNotSaveDuringSave() throws Exception
	{
		// given
		media1.setFolder(null);
		assertThat(media1.getFolder()).isNull();

		// when
		modelService.save(media1);

		// then
		assertThat(media1.getFolder()).isEqualTo(mediaService.getRootFolder());
	}

	@Test
	public void shouldFindForeignDataOwner() throws Exception
	{
		// given
		mediaService.setStreamForMedia(media1, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");
		assertThat(mediaService.getMediaWithSameDataReference(media1)).isEmpty();
		mediaService.copyData(media1, media2);

		// when
		final Collection<MediaModel> foreignDataOwners = mediaService.getMediaWithSameDataReference(media1);

		// then
		assertThat(foreignDataOwners).isNotEmpty();
		assertThat(foreignDataOwners.iterator().next().getPk()).isEqualTo(media2.getPk());
	}

	@Test
	public void shouldNotRemoveNotRemovableMedia()
	{
		final MediaModel media = prepareMedia("toRemove", sampleFolder);
		media.setRemovable(Boolean.FALSE);
		modelService.save(media);

		try
		{
			modelService.remove(media);
			fail("Expected ModelRemovalException");
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(ModelRemovalException.class);
			assertThat(ex.getCause()).isInstanceOf(InterceptorException.class);
		}
		finally
		{
			media.setRemovable(Boolean.TRUE);
			modelService.save(media);
			modelService.remove(media);
		}
	}

	private Collection<Principal> asUserCollection(final UserModel user)
	{
		return ImmutableList.of(modelService.getSource(user));
	}

	@Test
	public void shouldReadDeniedAndPermittedPrincipals()
	{
		// given
		final Media mediaSource = modelService.getSource(media1);
		mediaSource.setPermittedPrincipals(asUserCollection(permittedUser));
		mediaSource.setDeniedPrincipals(asUserCollection(deniedUser));

		// when
		final Collection<Principal> permittedPrincipalsFromJalo = mediaSource.getPermittedPrincipals();
		final Collection<PrincipalModel> permittedPrincipalsFromSld = media1.getPermittedPrincipals();

		// then
		assertSamePrincipals(permittedPrincipalsFromJalo, permittedPrincipalsFromSld);

		// and when
		final Collection<Principal> deniedPrincipalsFromJalo = mediaSource.getDeniedPrincipals();
		final Collection<PrincipalModel> deniedPrincipalsFromSld = media1.getDeniedPrincipals();

		// then
		assertSamePrincipals(deniedPrincipalsFromJalo, deniedPrincipalsFromSld);
	}

	private void assertSamePrincipals(final Collection<Principal> jaloPrincipals,
			final Collection<PrincipalModel> sldPrincipals)
	{
		assertThat(jaloPrincipals).hasSize(1);
		assertThat(sldPrincipals).hasSize(1);

		final Principal allowedJaloPrincipal = jaloPrincipals.iterator().next();
		final PrincipalModel allowedSldPrincipal = sldPrincipals.iterator().next();

		assertThat(allowedJaloPrincipal.getUid()).isEqualTo(allowedSldPrincipal.getUid());
	}


	@Test
	public void shouldSetDeniedAndPermittedPrincipals()
	{
		// given
		media1.setPermittedPrincipals(ImmutableList.of(permittedUser));
		media1.setDeniedPrincipals(ImmutableList.of(deniedUser));
		modelService.save(media1);

		// when
		final Collection<PrincipalModel> permittedPrincipals = media1.getPermittedPrincipals();
		final Collection<PrincipalModel> deniedPrincipals = media1.getDeniedPrincipals();

		// then
		assertThat(permittedPrincipals).hasSize(1);
		assertThat(permittedPrincipals.iterator().next().getUid()).isEqualTo(permittedUser.getUid());
		assertThat(deniedPrincipals).hasSize(1);
		assertThat(deniedPrincipals.iterator().next().getUid()).isEqualTo(deniedUser.getUid());
	}

	@Test
	public void shouldSetDeniedAndPermittedPrincipalsNoMatterOrder()
	{
		// given
		media1.setDeniedPrincipals(ImmutableList.of(deniedUser));
		media1.setPermittedPrincipals(ImmutableList.of(permittedUser));
		modelService.save(media1);

		// when
		final Collection<PrincipalModel> permittedPrincipals = media1.getPermittedPrincipals();
		final Collection<PrincipalModel> deniedPrincipals = media1.getDeniedPrincipals();

		// then
		assertThat(permittedPrincipals).hasSize(1);
		assertThat(permittedPrincipals.iterator().next().getUid()).isEqualTo(permittedUser.getUid());
		assertThat(deniedPrincipals).hasSize(1);
		assertThat(deniedPrincipals.iterator().next().getUid()).isEqualTo(deniedUser.getUid());
	}


	@Test
	public void shouldSaveMediaViaSLD()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final MediaModel media = prepareMedia("saveViaSLD", sampleFolder);
			try
			{
				modelService.save(media);
				mediaService.setStreamForMedia(media, getSampleInputStream(randomBytes), "fooBar.jpg", "image/jpeg");

				PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, media);

				assertThat(media.getCode()).isEqualTo("saveViaSLD");
				PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(media);
			}
			finally
			{
				modelService.remove(media);
			}
			return null;
		});
	}

}
