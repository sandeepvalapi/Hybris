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
package de.hybris.platform.scripting.engine.impl;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.repository.impl.ModelScriptsRepository;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class ModelScriptsRepositoryIntegrationTest extends ServicelayerTransactionalBaseTest
{
	public static final String PRINTLN_FOO = "println 'foo'";

	@Resource
	private ModelService modelService;
	@Resource(mappedName = "databaseScriptsRepository")
	private ModelScriptsRepository repository;

	private ScriptModel scriptModel;

	@Before
	public void setUp() throws Exception
	{
		scriptModel = prepareScriptModel("fooBar", PRINTLN_FOO);
	}

	@Test
	public void shouldHandleModelProtocol() throws Exception
	{
		// given
		final String protocol = "model";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isTrue();
	}

	@Test
	public void shouldNotHandleInvalidProtocol() throws Exception
	{
		// given
		final String protocol = "non-existent";

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldNotHandleNullProtocol() throws Exception
	{
		// given
		final String protocol = null;

		// when
		final boolean valid = repository.getSupportedProtocols().contains(protocol);

		// then
		assertThat(valid).isFalse();
	}

	@Test
	public void shouldThrowScriptNotFoundExceptionWhenScriptCannotBeFoundInRepository() throws Exception
	{
		// given
		final String protocol = "model";
		final String scriptCode = "nonExistent";

		try
		{
			// when
			repository.lookupScript(protocol, scriptCode);
			fail("should throw ScriptNotFoundException");
		}
		catch (final ScriptNotFoundException e)
		{
			// then fine
		}

	}

	@Test
	public void shouldFindScriptForGivenScriptPathInRepository() throws Exception
	{
		// given
		final String protocol = "model";
		final String scriptCode = "fooBar";

		// when
		final ScriptContent scriptContent = repository.lookupScript(protocol, scriptCode);

		// then
		assertThat(scriptContent).isNotNull();
		assertThat(scriptContent.getEngineName()).isEqualTo("groovy");
		assertThat(scriptContent.getContent()).isEqualTo(PRINTLN_FOO);
	}

	@Test
	public void shouldFindScriptForGivenScriptPathAndRevision() throws Exception
	{
		// given
		final String protocol = "model";
		final String scriptCode = "fooBar" + ModelScriptsRepository.REVISION_SEPARATOR + scriptModel.getVersion();

		// when
		final ScriptContent scriptContent = repository.lookupScript(protocol, scriptCode);

		// then
		assertThat(scriptContent).isNotNull();
		assertThat(scriptContent.getEngineName()).isEqualTo("groovy");
		assertThat(scriptContent.getContent()).isEqualTo(PRINTLN_FOO);
	}

	@Test
	public void shouldFindActiveScriptForGivenScriptPath_whenMultipleRevisionsExist() throws Exception
	{
		// given
		final String protocol = "model";
		final String scriptCode = "fooBar";

		// when
		updateScriptContent(scriptModel, "new content");

		// then
		final ScriptContent scriptContent = repository.lookupScript(protocol, scriptCode);
		assertThat(scriptContent).isNotNull();
		assertThat(scriptContent.getEngineName()).isEqualTo("groovy");
		assertThat(scriptContent.getContent()).isEqualTo("new content");
	}

	@Test
	public void shouldFindScriptForGivenScriptPathAndRevision_whenMultipleRevisionsExist() throws Exception
	{
		// given
		final String protocol = "model";
		final String scriptCode = "fooBar";
		final Long oldRevision = scriptModel.getVersion();
		final String oldRevisionUri = scriptCode + ModelScriptsRepository.REVISION_SEPARATOR + oldRevision;

		// when
		updateScriptContent(scriptModel, "new content");

		// then
		assertThat(oldRevision).isNotEqualTo(scriptModel.getVersion());

		final ScriptContent scriptContent = repository.lookupScript(protocol, oldRevisionUri);
		assertThat(scriptContent).isNotNull();
		assertThat(scriptContent.getEngineName()).isEqualTo("groovy");
		assertThat(scriptContent.getContent()).isEqualTo(PRINTLN_FOO);
	}

	@Test
	public void shouldFindAllActiveScriptsForGivenType()
	{
		// given
		final ScriptModel scriptModel1 = prepareScriptModel("model1", "content1", ScriptType.JAVASCRIPT);
		final ScriptModel scriptModel2 = prepareScriptModel("model2", "content1", ScriptType.JAVASCRIPT);
		final ScriptModel scriptModel3 = prepareScriptModel("model3", "content1", ScriptType.JAVASCRIPT);

		updateScriptContent(scriptModel1, "newContent1");
		updateScriptContent(scriptModel3, "newContent3");

		// when
		final List<ScriptModel> foundScripts = repository.findAllActiveScriptsForType(ScriptType.JAVASCRIPT);

		// then
		assertThat(foundScripts).containsOnly(scriptModel1, scriptModel2, scriptModel3);
	}

	@Test
	public void shouldFindAllRevisionsOfScriptForGivenCode()
	{
		// given
		final String scriptCode = "fooBar";

		final String hash0 = scriptModel.getChecksum();
		final String hash1 = updateScriptContent(scriptModel, "newContent1");
		final String hash2 = updateScriptContent(scriptModel, "newContent2");

		// sanity check to make sure each revision has different hash
		assertThat(Sets.newHashSet(hash0, hash1, hash2)).hasSize(3);

		// when
		final List<ScriptModel> revisions = repository.findAllRevisionsForCode(scriptCode);

		// then
		assertThat(revisions).hasSize(3);

		validateRevision(revisions.get(0), hash0, PRINTLN_FOO);
		validateRevision(revisions.get(1), hash1, "newContent1");
		validateRevision(revisions.get(2), hash2, "newContent2");
	}

	private void validateRevision(final ScriptModel rev1, final String hash1, final String expectedContent)
	{
		assertThat(rev1.getChecksum()).isEqualTo(hash1);
		assertThat(rev1.getContent()).isEqualTo(expectedContent);
	}

	private String updateScriptContent(final ScriptModel scriptModel1, final String newContent)
	{
		scriptModel1.setContent(newContent);
		modelService.save(scriptModel1);
		return scriptModel1.getChecksum();
	}


	private ScriptModel prepareScriptModel(final String code, final String content)
	{
		return prepareScriptModel(code, content, ScriptType.GROOVY);
	}

	private ScriptModel prepareScriptModel(final String code, final String content, final ScriptType scriptType)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(scriptType);
		script.setCode(code);
		script.setContent(content);

		modelService.save(script);

		return script;
	}
}
