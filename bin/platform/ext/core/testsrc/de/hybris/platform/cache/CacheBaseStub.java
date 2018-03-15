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
package de.hybris.platform.cache;

import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.collections.CacheMap;
import de.hybris.platform.util.collections.YMap;
import de.hybris.platform.util.collections.YMap.ClearHandler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;


/**
 * Internal container for cached data. Works tenant ID sensitively.
 */
class CacheBaseStub
{
	private static final Logger LOG = Logger.getLogger(CacheBaseStub.class.getName()); //NOPMD

	// guarded by treeLock
	private final Map unitMapTree;

	private final CacheMap<AbstractCacheUnit, AbstractCacheUnit> cacheMap;
	private final boolean isYMap;
	private final ReadWriteLock nonYMapRWLock;

	private final Object treeLock; // for synchronization upon tree

	private long gets = 0;
	private long misses = 0;
	private long removes = 0;
	private long adds = 0;

	CacheBaseStub(@SuppressWarnings("unused") final Tenant tenant, final int max)
	{
		// we can use HashMap here since any access is guarded via treeLock
		this.unitMapTree = new HashMap();

		this.treeLock = unitMapTree;
		this.cacheMap = internalCreateMapInstance(tenant, max);
		this.isYMap = cacheMap instanceof YMap;
		this.nonYMapRWLock = isYMap ? null : new ReentrantReadWriteLock();
		/*
		 * PLA-11286, CORE-167 if (LOG.isDebugEnabled()) { LOG.debug("[cache configuration: " + "maxAllowedSize=" +
		 * cacheMap.maxSize() + "]"); }
		 */
	}

	protected CacheMap internalCreateMapInstance(final Tenant tenant, final int max)
	{
		final CacheMap cacheMap;
		final Class cacheMapDefinition;

		final Class[] argumentsClass = new Class[]
		{ CacheBaseStub.class, int.class };
		final Object[] argumentValues = new Object[]
		{ this, Integer.valueOf(max) };
		final Constructor constructor;
		String mapName = null;
		try
		{
			mapName = tenant.getConfig().getParameter("cache.main.map");
			cacheMapDefinition = Class.forName(mapName);
			constructor = cacheMapDefinition.getConstructor(argumentsClass);
			cacheMap = (CacheMap) constructor.newInstance(argumentValues);
			return cacheMap;
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("can't initialize cache: " + mapName, e);
		}
	}

	/**
	 * @return the maximum reached number of entries since creation of the cache. This is only reseted if
	 *         {@link #clear()} called.
	 */
	int getMaxReachedSize()
	{
		return cacheMap.getMaxReachedSize();
	}

	/**
	 * The upper limit for the cache. This normally equals the setting 'cache.main' and is the number of cache entries
	 * that are allowed until the cache mechanism will remove the least recently used entries before adding new ones.
	 * 
	 * @return the max allowed size
	 */
	int getMaxAllowedSize()
	{
		return cacheMap.maxSize();
	}

	/**
	 * @return how many entities where added to the cache since creation of the cache. This is reseted by calling
	 *         {@link #clear()}.
	 */
	long getAddCount()
	{
		return adds;
	}

	/**
	 * @return how many entities where removed from the cache since creation of the cache. This is reseted by calling
	 *         {@link #clear()}.
	 */
	long getRemoveCount()
	{
		return removes;
	}

	/**
	 * @return how many entities where requested from the cache since creation of the cache. This is reseted by calling
	 *         {@link #clear()}.
	 */
	long getGetCount()
	{
		return gets;
	}

	/**
	 * @return how many entities where requested from the cache but wasn't in the cache (yet) since creation of the
	 *         cache. This is reseted by calling {@link #clear()}.
	 */
	long getMissCount()
	{
		return misses;
	}

	protected void putIntoCacheMap(final AbstractCacheUnit unit)
	{
		if (isYMap)
		{
			cacheMap.put(unit, unit);
		}
		else
		{
			// for Non-YMap cache maps we need to lock map operations against
			// our clear() logic because it requires iterating over all entries
			nonYMapRWLock.readLock().lock();
			try
			{
				cacheMap.put(unit, unit);
			}
			finally
			{
				nonYMapRWLock.readLock().unlock();
			}
		}
	}

	protected void removeFromCacheMap(final AbstractCacheUnit unit)
	{
		if (isYMap)
		{
			cacheMap.remove(unit);
		}
		else
		{
			// for Non-YMap cache maps we need to lock map operations against
			// our clear() logic because it requires iterating over all entries
			nonYMapRWLock.readLock().lock();
			try
			{
				cacheMap.remove(unit);
			}
			finally
			{
				nonYMapRWLock.readLock().unlock();
			}
		}
	}

	protected void clearCacheMap()
	{
		if (isYMap)
		{
			// YMap offers a synchronized way of clearing all entries while
			// also performing some logic upon them
			((YMap<AbstractCacheUnit, AbstractCacheUnit>) this.cacheMap).clear(//
					new ClearHandler<AbstractCacheUnit, AbstractCacheUnit>()
					{
						@Override
						public void handleClearedEntry(final Entry<AbstractCacheUnit, AbstractCacheUnit> entry)
						{
							entry.getKey().removedFromCache();
						}
					});
		}
		else
		{
			// otherwise we have to aquire a write lock in order to process
			// them in a safe way
			nonYMapRWLock.writeLock().lock();
			try
			{
				for (final Map.Entry<AbstractCacheUnit, AbstractCacheUnit> entry : cacheMap.entrySet())
				{
					entry.getKey().removedFromCache();
				}
				cacheMap.clear();
			}
			finally
			{
				nonYMapRWLock.writeLock().unlock();
			}
		}

	}

	/**
	 * Clears the cache and cache statistics (get, add, remove, miss count).
	 */
	// YTODO partial clear if slave tenant is shut down !!!
	synchronized void clear()
	{
		synchronized (treeLock)
		{
			unitMapTree.clear();
		}
		clearCacheMap();

		gets = 0;
		removes = 0;
		adds = 0;
		misses = 0;
	}

	/**
	 * @return the allowed maximum size of the cache.
	 */
	int getSize()
	{
		return cacheMap.size();
	}

	public void removeUnit(final AbstractCacheUnit unit)
	{
		removeUnitFromNestedMapOnly(unit);
		removeFromCacheMap(unit);
	}

	AbstractCacheUnit getUnit(final Cache caller, final String firstKeyElement, final String secondKeyElement,
			final String thirdKeyElement, final PK fourthKeyElement)
	{
		final Object[] key = new Object[]
		{ firstKeyElement, secondKeyElement, thirdKeyElement, fourthKeyElement };
		return getUnit(new AnonymousCacheUnit(caller, key));
	}

	AbstractCacheUnit getUnit(final AbstractCacheUnit unit)
	{
		if (unit.isCachingSupported())
		{
			gets++;
			final AbstractCacheUnit ret = cacheMap.get(unit);
			if (ret == null || !ret.isValueKnown())
			{
				misses++;
			}
			return ret;
		}
		else
		{
			return null;
		}
	}

	void addUnit(final AbstractCacheUnit unit)
	{
		if (unit.isCachingSupported())
		{
			adds++;
			final Object[] key = unit.getKeyAsArray();
			final String tenantID = unit.getTenantID();

			// add unit to cache map BEFORE adding to tree maps
			// otherwise it could already be removed from tree maps when adding it to cache map
			unit.addedToCacheBeforeComputation();
			putIntoCacheMap(unit);

			synchronized (treeLock)
			{
				if (DefaultCache.isMultiPathKey(key))
				{
					final int numberOfKeys = key.length;
					for (int i = 0; i < numberOfKeys; i++)
					{
						final Object[] singleKey = (Object[]) key[i];
						getNodeForKey(tenantID, singleKey).put(singleKey[singleKey.length - 1], unit);
					}
				}
				else
				{
					getNodeForKey(tenantID, key).put(key[key.length - 1], unit);
				}
			}
		}
	}

	void invalidate(final Cache caller, final Object[] key, @SuppressWarnings("unused") final int invalidationType)
	{
		if (DefaultCache.isMultiPathKey(key))
		{
			throw new RuntimeException("cannot invalidate multi-path keys ");
		}
		invalidateRecursively(removeKeyFromPath(getFullPathForKey(caller.getTenant().getTenantID(), key), key));
		final AbstractCacheUnit abstractCacheUnit = new AnonymousCacheUnit(caller, key);
		if (cacheMap.containsKey(abstractCacheUnit))
		{
			removeFromCacheMap(abstractCacheUnit);
		}
		removes++;
	}

	// --------------------------------------------------------------------
	// --- internal
	// --------------------------------------------------------------------

	protected void removeUnitFromNestedMapOnly(final AbstractCacheUnit unit)
	{
		removes++;
		final Object[] key = unit.getKeyAsArray();
		final String tenantID = unit.getTenantID();
		if (DefaultCache.isMultiPathKey(key))
		{
			for (final Object inner : key)
			{
				final Object[] singleKey = (Object[]) inner;
				removeKeyFromPath(getFullPathForKey(tenantID, singleKey), singleKey);
			}
		}
		else
		{
			removeKeyFromPath(getFullPathForKey(tenantID, key), key);
		}
		unit.removedFromCache();
	}

	//	 Example:
	//	 	key  = ["hjmp", "1", 123456789 ])
	//	   path = [ { "hjmp" -> { } ... }, { "1" -> { } ... } , { 123456 -> CacheUnit } ]
	// 
	//    ret = path[2].get( 123456789 ) = CacheUnit
	//    path[2].remove( 123456789 )
	//    path[2] empty -> path[1].remove( "1" )
	//    path[1] empty -> path[0].remove( 123456789 )
	private Object removeKeyFromPath(final Map[] path, final Object[] key)
	{
		if (path != null)
		{
			synchronized (treeLock)
			{
				if (path.length != key.length)
				{
					throw new IllegalStateException("path length doesnt match key length (path=" + Arrays.asList(path) + ", key="
							+ Arrays.asList(key) + ")");
				}
				final int lastPos = key.length - 1;
				// get actual value from last map
				final Object ret = path[lastPos].get(key[lastPos]);
				// remove key from maps last one fist
				for (int i = lastPos; i >= 0; i--)
				{
					path[i].remove(key[i]);
					if (!path[i].isEmpty())
					{
						break;
					}
				}
				return ret;
			}
		}
		else
		{
			return null;
		}
	}

	// requires lock of treeLock !!!
	private Map getNodeForKey(final String tenantID, final Object[] key)
	{
		Map current = getTenantRootMap(tenantID);
		for (final Object k : key)
		{
			Map newMap = (Map) current.get(k);
			if (newMap == null)
			{
				// the new map creation is guared by treeLock
				current.put(k, newMap = new HashMap());
			}
			current = newMap;
		}
		return current;
	}

	//	 Example:
	//	 	getFullPathForKey("master", ["hjmp", "1", 123456789 ])
	//	 +
	//	  	root: "master" -> { "hjmp" ->{ } ... , }
	//	    1: "hjmp" -> { "1" -> { } , "2" -> { } ... }  
	//	 	2: "1" -> { 1234567 -> CacheUnit, ... }
	//	 =
	//	 	[ { "hjmp" -> { } ... }, { "1" -> { } ... } , { 123456 -> CacheUnit } ] 
	private Map[] getFullPathForKey(final String tenantID, final Object[] key)
	{
		synchronized (treeLock)
		{
			final int depth = key.length; // 3
			final Map[] ret = new Map[key.length];

			Map current = getTenantRootMap(tenantID);
			ret[0] = current; // make root first result node

			for (int i = 1; i < depth && current != null; i++) // 1: get("hjmp")  , 2 : get( "1" )
			{
				current = (Map) current.get(key[i - 1]); // remember we start with 1 !
				ret[i] = current;
			}
			return current != null ? ret : null;
		}
	}

	// requires lock of treeLock !!!
	private Map getTenantRootMap(final String tenantID)
	{
		Map root = (Map) unitMapTree.get(tenantID);
		if (root == null)
		{
			unitMapTree.put(tenantID, root = new HashMap());
		}
		return root;
	}


	private void invalidateRecursively(final Object obj)
	{
		if (obj != null)
		{
			final Collection<AbstractCacheUnit> units;
			if (obj instanceof AbstractCacheUnit)
			{
				units = Collections.singleton((AbstractCacheUnit) obj);
			}
			else
			{
				units = new ArrayList<AbstractCacheUnit>(30);
				synchronized (treeLock) //because the 'obj' comes from there
				{
					extractUnits((Map) obj, units);
				}
			}
			for (final AbstractCacheUnit acu : units)
			{
				acu.executeInvalidation();
				removeUnit(acu);
			}
		}
	}

	private void extractUnits(final Map<?, ?> pathElement, final Collection<AbstractCacheUnit> toAddTo)
	{
		for (final Map.Entry<?, ?> e : pathElement.entrySet())
		{
			final Object value = e.getValue();
			if (value instanceof AbstractCacheUnit)
			{
				toAddTo.add((AbstractCacheUnit) value);
			}
			else
			{
				extractUnits((Map<?, ?>) value, toAddTo);
			}
		}
		pathElement.clear();
	}

	private static class AnonymousCacheUnit extends AbstractCacheUnit
	{
		private final Object[] key;

		AnonymousCacheUnit(final Cache cache, final Object[] key)
		{
			super(cache);
			this.key = key;
		}

		@Override
		public Object compute()
		{
			throw new RuntimeException("must not compute AnonymousCacheUnit");
		}

		@Override
		public final Object[] createKey()
		{
			return key;
		}
	}

}
