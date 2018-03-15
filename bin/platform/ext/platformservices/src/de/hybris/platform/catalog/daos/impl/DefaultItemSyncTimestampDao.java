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
package de.hybris.platform.catalog.daos.impl;

import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ItemSyncTimestampDao}.
 */
public class DefaultItemSyncTimestampDao implements ItemSyncTimestampDao
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<ItemSyncTimestampModel> findSyncTimestampsByItem(final ItemModel item, final int limit)
	{
		final StringBuffer sql = new StringBuffer(150);

		sql.append("SELECT tbl.pk FROM (");
		sql.append("{{SELECT {").append(ItemModel.PK).append("} as pk FROM {").append(ItemSyncTimestampModel._TYPECODE)
				.append("*} ");
		sql.append("WHERE {").append(ItemSyncTimestampModel.SOURCEITEM).append("}=?item }}");
		sql.append(" UNION ALL ");
		sql.append("{{SELECT {").append(ItemModel.PK).append("} as pk FROM {").append(ItemSyncTimestampModel._TYPECODE)
				.append("*} ");
		sql.append("WHERE {").append(ItemSyncTimestampModel.TARGETITEM).append("}=?item}}");
		sql.append(") tbl");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString(), Collections.singletonMap("item", item));
		query.setCount(limit);
		final SearchResult<ItemSyncTimestampModel> res = flexibleSearchService.search(query);

		return res.getResult();
	}

	@Override
	public List<ItemSyncTimestampModel> findSyncTimestampsByCatalogVersion(final CatalogVersionModel catalogVersion,
			final int limit)
	{
		final StringBuffer sql = new StringBuffer(150);

		sql.append("SELECT tbl.pk FROM (");
		sql.append("{{SELECT {").append(ItemModel.PK).append("} as pk FROM {").append(ItemSyncTimestampModel._TYPECODE)
				.append("*} ");
		sql.append("WHERE {").append(ItemSyncTimestampModel.SOURCEVERSION).append("}=?catalogVersion }}");
		sql.append(" UNION ALL ");
		sql.append("{{SELECT {").append(ItemModel.PK).append("} as pk FROM {").append(ItemSyncTimestampModel._TYPECODE)
				.append("*} ");
		sql.append("WHERE {").append(ItemSyncTimestampModel.TARGETVERSION).append("}=?catalogVersion}}");
		sql.append(") tbl");


		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString(),
				Collections.singletonMap("catalogVersion", catalogVersion));
		query.setCount(limit);
		final SearchResult<ItemSyncTimestampModel> res = flexibleSearchService.search(query);
		return res.getResult();
	}


	public List<ItemSyncTimestampModel> findLastSourceSyncTimestamps(final SyncItemJobModel syncJob, final ItemModel source)
	{

		final boolean excl = BooleanUtils.toBoolean(syncJob.getExclusiveMode());
		final Map<String, Object> params = new HashMap();
		params.put("source", source);
		if (excl)
		{
			params.put("syncJob", syncJob);
		}
		else
		{
			params.put("targetVersion", syncJob.getTargetVersion());
		}
		final SearchResult<ItemSyncTimestampModel> rows = flexibleSearchService.search(//
				"SELECT {" + ItemSyncTimestampModel.PK + "} " //
						+ "FROM {" + ItemSyncTimestampModel._TYPECODE + "*} " //
						+ "WHERE {" + ItemSyncTimestampModel.SYNCJOB + "}" + (excl ? "=?syncJob" : "=0") + " AND "//
						+ (excl ? "" : "{" + ItemSyncTimestampModel.TARGETVERSION + "}=?targetVersion AND ") //
						+ "{" + ItemSyncTimestampModel.SOURCEITEM + "}=?source " //
						+ "ORDER BY {" + ItemSyncTimestampModel.LASTSYNCTIME + "} DESC, " //
						+ "{" + ItemModel.CREATIONTIME + "} DESC",
				params);
		return rows.getResult();
	}


	public List<ItemSyncTimestampModel> findLastTargetSyncTimestamps(final SyncItemJobModel syncJob, final ItemModel target)
	{

		final boolean excl = BooleanUtils.toBoolean(syncJob.getExclusiveMode());
		final Map<String, Object> params = new HashMap();
		params.put("target", target);
		if (excl)
		{
			params.put("syncJob", syncJob);
		}
		else
		{
			params.put("sourceVersion", syncJob.getSourceVersion());
		}
		final SearchResult<ItemSyncTimestampModel> rows = flexibleSearchService.search(//
				"SELECT {" + ItemSyncTimestampModel.PK + "} " //
						+ "FROM {" + ItemSyncTimestampModel._TYPECODE + "*} " //
						+ "WHERE {" + ItemSyncTimestampModel.SYNCJOB + "}" + (excl ? "=?syncJob" : "=0") + " AND "//
						+ (excl ? "" : "{" + ItemSyncTimestampModel.SOURCEVERSION + "}=?sourceVersion AND ") //
						+ "{" + ItemSyncTimestampModel.TARGETITEM + "}=?target " //
						+ "ORDER BY {" + ItemSyncTimestampModel.LASTSYNCTIME + "} DESC, " //
						+ "{" + ItemModel.CREATIONTIME + "} DESC",
				params);
		return rows.getResult();


	}


	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
