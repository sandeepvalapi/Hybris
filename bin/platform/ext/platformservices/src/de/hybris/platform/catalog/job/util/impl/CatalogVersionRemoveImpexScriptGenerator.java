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
package de.hybris.platform.catalog.job.util.impl;

import de.hybris.platform.catalog.job.util.CatalogVersionJobDao;
import de.hybris.platform.catalog.job.util.ImpexScriptGenerator;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.enums.ImpExProcessModeEnum;
import de.hybris.platform.util.CSVConstants;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Generates an impex script for removing any instance of {@link ItemModel}s for the {@link ComposedTypeModel} related
 * to given {@link CatalogVersionModel}.
 */
public class CatalogVersionRemoveImpexScriptGenerator implements ImpexScriptGenerator
{

	private CatalogVersionJobDao catalogVersionJobDao;

	@Required
	public void setCatalogVersionJobDao(final CatalogVersionJobDao catalogVersionJobDao)
	{
		this.catalogVersionJobDao = catalogVersionJobDao;
	}

	@Override
	public StringBuilder generate(final CatalogVersionModel version, final List<ComposedTypeModel> orderedComposedTypes)
	{

		StringBuilder buffer = null;
		for (final ComposedTypeModel currentct : orderedComposedTypes)
		{
			if (buffer == null)//lazy init
			{
				buffer = new StringBuilder(1000);
				buffer.append("#% de.hybris.platform.jalo.JaloSession.getCurrentSession().getSessionContext()."
						+ "setAttribute( de.hybris.platform.jalo.Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE);");
				buffer.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
			}
			createImpexMediaForComposedType(buffer, currentct, version);
		}
		return buffer == null ? new StringBuilder() : buffer;
	}

	/**
	 * Appends a given <code>bufferIn</code> with content header , script body.
	 * <p>
	 * Header is specific for a {@link ComposedTypeModel}.
	 * <p>
	 * Body contains {@link PK}sof the {@link ItemModel} instances of the given {@link ComposedTypeModel}.
	 * 
	 * @throws java.io.IOException
	 */
	protected void createImpexMediaForComposedType(final StringBuilder bufferIn, final ComposedTypeModel comptype,
			final CatalogVersionModel version)
	{
		boolean anyInstanceExists = false;

		final List<PK> result = catalogVersionJobDao.getPKList(comptype, version);
		for (final PK pk : result)
		{
			if (!anyInstanceExists)
			{
				bufferIn.append(ImpExProcessModeEnum.REMOVE.getCode() + " " + comptype.getCode()
						+ CSVConstants.HYBRIS_FIELD_SEPARATOR + "pk[unique=true]");
				bufferIn.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
			}
			bufferIn.append(CSVConstants.HYBRIS_FIELD_SEPARATOR + pk.toString());
			bufferIn.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
			anyInstanceExists = true;
		}
		if (anyInstanceExists)
		{
			bufferIn.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
			bufferIn.append(CSVConstants.DEFAULT_COMMENT_CHAR + " end of pk list for " + comptype.getCode());
			bufferIn.append(CSVConstants.HYBRIS_LINE_SEPARATOR);
		}
	}


}
