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
/**

 Scenario 1: get from cache [no element in cache, one cache region]
 - default cache region is chosen
 - unit may be stored in the registry
 - value is loaded
 - value is added to cache
 - eviction may happen
 - eviction may remove element from registry
 - cache statistics are updated [miss, ?eviction]

 Scenario 2: get from cache [element in cache, one cache region]
 - default cache region is chosen
 - value is got from cache
 - cache statistics are updated [hit]

 Scenario 3: single invalidation [one cache region]
 - invalidation invoked on default cache region
 - invalidation filter is checked
 - invalidation listener is notified (more invalidations can came)
 - cache registry is checked for more invalidations (if necessary)
 - key removed from cache registry (if necessary)
 - value removed from cache
 - cache statistics are updated [invalidate]

 Scenario 4: get from cache [no region specified, no element in cache, multiple cache regions{by type} ]
 - calculated{type} cache region is choosen
 - unit may be stored in the registry
 - value is loaded
 - value is added to cache
 - eviction may happen
 - eviction may remove element from registry
 - cache statistics are updated [miss, ?eviction]

 Scenario 5: get from cache [no region specified, multiple cache regions{by catalogVersion} ]
 - exception is thrown "No cache region specified"

 Scenario 6: get from cache [cache region specified, no element in cache, multiple cache regions{by catalogVersion} ]
 - specified cache region is chosen
 - unit may be stored in the registry
 - value is loaded
 - value is added to cache
 - eviction may happen
 - eviction may remove element from registry
 - cache statistics are updated [miss, ?eviction]

 Scenario 7: get from cache - non catalogVersion aware type [cache region specified, no element in cache, multiple cache regions{by catalogVersion} ]
 - specified cache region is chosen
 - unit may be stored in the registry
 - value is loaded
 - value is added to cache
 - eviction may happen
 - eviction may remove element from registry
 - cache statistics are updated [miss, ?eviction]

 Scenario 8: single invalidation [no region specified, multiple cache regions{by catalogVersion}]
 - All regions are invalidated with supplied key

 Scenario 9: single invalidation [cache region specified, multiple cache regions{by catalogVersion}]
 ** Possibility to specify two cache regions for "all" types which differs only by name
 - invalidation invoked on specified cache region
 - invalidation filter is checked
 - invalidation listener is notified (more invalidations can came even on different regions)
 - key removed from cache registry (if necessary)
 - value removed from cache
 - cache statistics are updated [invalidate]

 Scenario 10: get from cache [no region specified, multiple cache regions{by catalogVersion, by type} ]
 - if cache unit type can be stored in more than one cache exception is thrown "No cache region specified"
 - cache region is calculated
 - unit may be stored in the registry
 - value is loaded
 - value is added to cache
 - eviction may happen
 - eviction may remove element from registry
 - cache statistics are updated [miss, ?eviction]

 Scenario 11: single invalidation [no region specified, multiple cache regions{by catalogVersion, by type} ]
 - if cache unit type can be stored in more than one cache exception is thrown "No cache region specified"
 - cache region is calculated
 - invalidation invoked on specified cache region
 - invalidation filter is checked
 - invalidation listener is notified (more invalidations can came even on different regions)
 - key removed from cache registry (if necessary)
 - value removed from cache
 - cache statistics are updated [invalidate]

 Scenario 12:
 - Flexible search queries for online catalog are kept in region[1]
 - Flexible search queries for staging catalog are kept in region[2]
 - Invalidation coming for region[3]
 - Expected: Invalidation is performed for relevant keys in region[1] AND region[2] 

 ** not covered:
 - impex
 - ...

 */
package de.hybris.platform.regioncache.test;

