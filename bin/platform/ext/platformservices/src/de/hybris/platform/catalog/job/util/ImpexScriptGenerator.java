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
package de.hybris.platform.catalog.job.util;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.List;



/**
 * Abstract of the converter from a {@link List} of the {@link ComposedTypeModel}s within a {@link CatalogVersionModel}
 * to {@link StringBuffer}.
 */
public interface ImpexScriptGenerator
{

	/**
	 * Generates a text representation of the {@link List} of {@link ComposedTypeModel}s related to
	 * {@link CatalogVersionModel}.
	 */
	StringBuilder generate(final CatalogVersionModel version, final List<ComposedTypeModel> orderedComposedTypes);

}
