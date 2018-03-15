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
package de.hybris.platform.directpersistence.read;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class SLDDataContainerAssert extends GenericAssert<SLDDataContainerAssert, SLDDataContainer>
{
	private LanguageModel currentLanguage;

	private SLDDataContainerAssert(final SLDDataContainer actual)
	{
		super(SLDDataContainerAssert.class, actual);
	}

	public static SLDDataContainerAssert assertThat(final SLDDataContainer actual)
	{
		return new SLDDataContainerAssert(actual);
	}

	public <T extends ItemModel> SLDDataContainerAssert hasEqualMetaDataAs(final T model)
	{
		Assertions.assertThat(actual.getPk()).isEqualTo(model.getPk());
		final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(model.getClass());
		Assertions.assertThat(actual.getTypePk()).isEqualTo(composedType.getPk());
		Assertions.assertThat(actual.getTypeCode()).isEqualTo(composedType.getCode());
		return this;
	}

	public AttributeValueAssert containsAttribute(final String attributeName)
	{
		Assertions.assertThat(actual).isNotNull();
		final SLDDataContainer.AttributeValue attributeValue = actual.getAttributeValue(attributeName, null);
		Assertions.assertThat(attributeValue)
				.overridingErrorMessage("Value of '" + attributeName + "' for model '" + actual.getTypeCode() + "' is null!")
				.isNotNull();
		return new AttributeValueAssert(attributeValue);
	}

	public AttributeValueAssert containsLocalizedAttribute(final String attributeName)
	{
		Assertions.assertThat(actual).isNotNull();
		if (currentLanguage == null)
		{
			final CommonI18NService commonI18NService = Registry.getApplicationContext().getBean("commonI18NService",
					CommonI18NService.class);
			currentLanguage = commonI18NService.getCurrentLanguage();
		}
		final SLDDataContainer.AttributeValue attributeValue = actual.getAttributeValue(attributeName, currentLanguage.getPk());
		Assertions.assertThat(attributeValue).isNotNull();
		return new AttributeValueAssert(attributeValue);
	}

	private TypeService getTypeService()
	{
		return Registry.getApplicationContext().getBean("typeService", TypeService.class);
	}
}
