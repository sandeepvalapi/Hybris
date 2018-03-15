package de.hybris.platform.product;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultConfiguratorSettingsService;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link de.hybris.platform.product.impl.DefaultConfiguratorSettingsService}
 */
@UnitTest
public class DefaultConfiguratorSettingsServiceTest
{
	@InjectMocks
	private DefaultConfiguratorSettingsService configuratorSettingsService = new DefaultConfiguratorSettingsService();

	@Mock
	private ConfiguratorSettingsResolutionStrategy configuratorSettingsResolutionStrategy;

	@Before
	public void setUp()
	{
		initMocks(this);
	}

	@Test
	public void testGetConfigurationsSettingsNullProduct()
	{
		try
		{
			configuratorSettingsService.getConfiguratorSettingsForProduct(null);
			Assert.fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			verifyNoMoreInteractions(configuratorSettingsResolutionStrategy);
		}
	}

	@Test
	public void testGetConfigurationsSettings()
	{
		AbstractConfiguratorSettingModel settingModel = mock(AbstractConfiguratorSettingModel.class);
		ProductModel product = mock(ProductModel.class);
		BDDMockito.given(configuratorSettingsResolutionStrategy.getConfiguratorSettingsForProduct(any(ProductModel.class)))
				.willReturn(Collections.singletonList(settingModel));

		List<AbstractConfiguratorSettingModel> configuratorSettings = configuratorSettingsService.getConfiguratorSettingsForProduct(product);

		assertThat(configuratorSettings).containsExactly(settingModel);
		verify(configuratorSettingsResolutionStrategy).getConfiguratorSettingsForProduct(product);
		verifyNoMoreInteractions(configuratorSettingsResolutionStrategy);
	}
}