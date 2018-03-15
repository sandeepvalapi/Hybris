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
package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.impl.AbstractModelService;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MockModelService extends AbstractModelService
{
	private final Map<PK, ItemModel> persistenceStore = new ConcurrentHashMap<PK, ItemModel>();

	@Override
	public void refresh(final Object model)
	{
		persistenceStore.get(((ItemModel) model).getPk());
	}

	@Override
	public void remove(final Object model)
	{
		persistenceStore.remove(((ItemModel) model).getPk());
	}

	@Override
	public void remove(final PK pk) throws ModelRemovalException
	{
		persistenceStore.remove(pk);
	}

	@Override
	public boolean isUpToDate(final Object model)
	{
		return true;
	}

	@Override
	public void save(final Object model)
	{
		persistenceStore.put(((ItemModel) model).getPk(), (ItemModel) model);
	}

	@Override
	public void saveAll(final Collection<? extends Object> models) throws ModelSavingException
	{
		for (final Object o : models)
		{
			save(o);
		}
	}

	/**
	 * @deprecated since 6.1.0
	 */
	@Override
	@Deprecated
	public boolean isUniqueConstraintErrorAsRooCause(Exception e)
	{
		return isUniqueConstraintErrorAsRootCause(e);
	}

	@Override
	public boolean isUniqueConstraintErrorAsRootCause(Exception e)
	{
		return false;
	}

	@Override
	public void attach(final Object model)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach(final Object model)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach(final PK sourcePK)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveAll() throws ModelSavingException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Object> T clone(final T original)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T clone(final Object original, final Class<T> targetType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Object> T clone(final T original, final ModelCloningContext ctx)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T clone(final Object original, final Class<T> targetType, final ModelCloningContext ctx)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T create(final Class typeAsModel)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T create(final String typeCode)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getModelType(final Class modelClass)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getSource(final Object model)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(final Object source)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(final Object source, final String conversionType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(final PK sourcePK)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void initDefaults(final Object model)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getModelType(final Object model)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getAttributeValue(final Object model, final String attributeQualifier)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getAttributeValue(final Object model, final String attributeQualifier, final Locale locale)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Map<Locale, T> getAttributeValues(final Object model, final String attributeQualifier, final Locale... locales)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributeValue(final Object model, final String attributeQualifier, final Object value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> void setAttributeValue(final Object model, final String attributeQualifier, final Map<Locale, T> values)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detachAll()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object getModelForPersistentValue(final Object persistentValue)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object getPersistentValueForModel(final Object model)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean isModified(final Object model)
	{
		return false;
	}

	@Override
	public boolean isNew(final Object model)
	{
		return false;
	}

	@Override
	public boolean isRemoved(final Object model)
	{
		return false;
	}

	@Override
	public <T extends Object> T getByExample(final T example)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Class getModelTypeClass(final Class modelClass)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Collection> T getAll(final Collection<? extends Object> sources, final T result)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Collection> T getAll(final Collection<? extends Object> sources, final T result,
			final String conversionType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Collection> T getAllSources(final Collection<? extends Object> models, final T result)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAll(final Collection<? extends Object> models) throws ModelRemovalException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAll(final Object... models) throws ModelRemovalException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void enableTransactions()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void disableTransactions()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearTransactionsSettings()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void lock(final PK itemPK)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void lock(final Object source)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getWithLock(final Object source)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAttached(final Object model)
	{
		return false;
	}

	@Override
	public boolean isSourceAttached(final Object source)
	{
		return false;
	}
}
