package de.hybris.platform.audit;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.core.PK;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class TypeAuditReportConfigTest
{
	@Mock
	private AuditReportConfig auditReportConfig;

	@Test
	public void shouldThrowExceptionOnTryToBuildAuditReportConfigLessViewConfig() throws Exception
	{
		// given
		final PK rootTypePk = PK.createFixedUUIDPK(2, 2);

		try
		{
			// when
			TypeAuditReportConfig.builder().withRootTypePk(rootTypePk).build();
			fail("should throw Exception");
		}
		catch (final Exception e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowExceptionOnTryToBuildRootTypePKLessViewConfig() throws Exception
	{
		// given
		final String configName = "testConfig";

		try
		{
			// when
			TypeAuditReportConfig.builder().withConfigName(configName).build();
			fail("should throw Exception");
		}
		catch (final Exception e)
		{
			// then fine
		}
	}

	@Test
	public void shouldCorrectlyBuildViewConfigWithOnlyNameOfTheAuditReportConfigAndRootTypePk() throws Exception
	{
		// given
		final String configName = "testConfig";
		final PK rootTypePk = PK.createFixedUUIDPK(2, 2);
		final TypeAuditReportConfig.Builder builder = TypeAuditReportConfig.builder().withConfigName(configName)
				.withRootTypePk(rootTypePk);

		// when
		final TypeAuditReportConfig typeAuditReportConfig = new TestTypeAuditReportConfig(builder);

		// then
		assertThat(typeAuditReportConfig).isNotNull();
		assertThat(typeAuditReportConfig.getReportConfig()).isNotNull().isEqualTo(auditReportConfig);
		assertThat(typeAuditReportConfig.getRootTypePk()).isNotNull().isEqualTo(rootTypePk);
	}
	@Test
	public void shouldCorrectlyBuildViewConfigWithOnlyAuditReportConfigAndRootTypePk() throws Exception
	{
		// given
		final PK rootTypePk = PK.createFixedUUIDPK(2, 2);
        final TypeAuditReportConfig.Builder builder = TypeAuditReportConfig.builder().withConfig(auditReportConfig).withRootTypePk(rootTypePk);

        // when
		final TypeAuditReportConfig typeAuditReportConfig = new TestTypeAuditReportConfig(builder);

		// then
		assertThat(typeAuditReportConfig).isNotNull();
		assertThat(typeAuditReportConfig.getReportConfig()).isNotNull().isEqualTo(auditReportConfig);
		assertThat(typeAuditReportConfig.getRootTypePk()).isNotNull().isEqualTo(rootTypePk);
	}

	private class TestTypeAuditReportConfig extends TypeAuditReportConfig
	{

		TestTypeAuditReportConfig(final Builder builder)
		{
			super(builder);
		}

		@Override
		AuditReportConfig getReportConfigForName(final String configName)
		{
            return auditReportConfig;
		}
	}

}
