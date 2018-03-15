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
package de.hybris.platform.product.daos;

import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.List;


/**
 * The {@link VariantTypeModel} DAO.
 * 
 * @spring.bean variantTypeDao
 */
public interface VariantTypeDao extends Dao
{

	/**
	 * Finds all {@link VariantTypeModel}s.
	 * 
	 * @return a List of all {@link VariantTypeModel}s
	 */
	List<VariantTypeModel> findAllVariantTypes();

	/**
	 * Returns the amount of products which use the given variant type.
	 * 
	 * @param variantType
	 *           the variant type
	 * 
	 * @return the amount of products which use this variant type
	 */
	public int getBaseProductCount(VariantTypeModel variantType);

}
