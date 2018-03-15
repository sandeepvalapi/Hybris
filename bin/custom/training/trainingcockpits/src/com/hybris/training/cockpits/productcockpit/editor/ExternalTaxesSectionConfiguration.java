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
package com.hybris.training.cockpits.productcockpit.editor;

import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.UpdateAwareCustomSectionConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 * Represents custom section which show external taxes for Products.
 */
public class ExternalTaxesSectionConfiguration extends DefaultEditorSectionConfiguration implements
		UpdateAwareCustomSectionConfiguration
{
	private SectionRenderer sectionRenderer;

	@Override
	public void allInitialized(final EditorConfiguration config, final ObjectType type, final TypedObject object)
	{
		// NOP
	}

	@Override
	public List<EditorSectionConfiguration> getAdditionalSections()
	{
		// NOP
		return Collections.emptyList();
	}

	@Override
	public SectionRenderer getCustomRenderer()
	{
		return this.sectionRenderer;
	}

	@Override
	public void initialize(final EditorConfiguration config, final ObjectType type, final TypedObject object)
	{

		// NOP
	}

	@Override
	public void loadValues(final EditorConfiguration config, final ObjectType type, final TypedObject object,
			final ObjectValueContainer objectValues)
	{
		// NOP

	}

	@Override
	public void saveValues(final EditorConfiguration config, final ObjectType type, final TypedObject object,
			final ObjectValueContainer objectValues)
	{
		// NOP

	}

	@Required
	public void setSectionRenderer(final SectionRenderer sectionRenderer)
	{
		this.sectionRenderer = sectionRenderer;
	}

	public SectionRenderer getSectionRenderer()
	{
		return this.sectionRenderer;
	}


	@Override
	public Set<PropertyDescriptor> getUpdateTriggerProperties()
	{
		return new HashSet<>();
	}

	@Override
	public Set<ObjectType> getUpdateTriggerTypes()
	{
		return Collections.emptySet();
	}
}
