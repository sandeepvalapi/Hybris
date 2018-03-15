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
package de.hybris.platform.commons.renderer.daos;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;

import java.util.List;


/**
 * Dao for commons renderer
 */
public interface RendererTemplateDao
{
	/**
	 * Search for RendererTemplateModels by its code.
	 * 
	 * @param code
	 *           template code
	 * 
	 * @throws IllegalArgumentException
	 *            if code is null
	 * 
	 * @return result of search
	 */
	List<RendererTemplateModel> findRendererTemplatesByCode(String code);
}
