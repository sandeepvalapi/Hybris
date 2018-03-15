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
package de.hybris.platform.test;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ItemEJB;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.persistence.type.ComposedTypeRemote;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ItemDummy implements ItemRemote
{
	private final PK pk;

	public ItemDummy(final PK pk)
	{
		this.pk = pk;
	}

	public String getItemTableName()
	{
		throw new RuntimeException();
	}

	@Override
	public boolean hasJNDIName(final String jndiName)
	{
		throw new RuntimeException();
	}

	public String getItemJNDIName()
	{
		throw new RuntimeException();
	}

	@Override
	public long getHJMPTS()
	{
		return 0;
	}

	@Override
	public PK getPkString()
	{
		return pk;
	}

	public String getPrimaryKey()
	{
		return pk == null ? null : pk.toString();
	}

	@Override
	public PK getPK()
	{
		return pk;
	}

	@SuppressWarnings("unused")
	public void setStagedCopy(final ItemRemote rem)
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public void setRemoved(final boolean bool)
	{
		throw new RuntimeException();
	}

	public void checkValid()
	{
		throw new RuntimeException();
	}

	public boolean isCreated()
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public void deepCopyFrom(final ItemRemote remote)
	{
		throw new RuntimeException();
	}

	/*
	 * public Map getContentMap() { throw new RuntimeException(); } public void fillContentMap() { throw new
	 * RuntimeException(); }
	 */

	public boolean isCopy()
	{
		throw new RuntimeException();
	}

	public boolean isRemoved()
	{
		throw new RuntimeException();
	}

	public boolean stagingActivated()
	{
		throw new RuntimeException();
	}

	public ItemRemote getStagedCopy()
	{
		throw new RuntimeException();
	}


	public boolean hasModified()
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public void setCreated(final boolean bool)
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public void setCopy(final boolean bool)
	{
		throw new RuntimeException();
	}

	public void clearEntityCaches()
	{
		throw new RuntimeException();
	}


	@Override
	public void remove()
	{
		throw new RuntimeException();
	}

	@Override
	public Date getCreationTime()
	{
		throw new RuntimeException();
	}

	@Override
	public void setCreationTime(final Date creationTime)
	{
		// DOCTODO Document reason, why this block is empty
	}

	@Override
	public Date getModifiedTime()
	{
		throw new RuntimeException();
	}

	@Override
	public void setModifiedTime(final Date timestamp)
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public long getLastModifiedTime(final int stage)
	{
		throw new RuntimeException();
	}

	@Override
	public boolean wasModifiedSince(final Date time)
	{
		throw new RuntimeException();
	}

	@Override
	public ComposedTypeRemote getComposedType()
	{
		throw new RuntimeException();
	}

	@Override
	public void setComposedType(final ComposedTypeRemote type)
	{
		throw new RuntimeException();
	}

	@Override
	public ItemRemote getOwner()
	{
		throw new RuntimeException();
	}

	@Override
	public void setOwner(final ItemRemote item)
	{
		throw new RuntimeException();
	}

	public ItemRemote copy()
	{
		throw new RuntimeException();
	}

	@Override
	public int checkItemPermission(final PK principalPK, final PK permissionPK)

	{
		throw new RuntimeException();
	}

	public Collection getACLOwners()
	{
		throw new RuntimeException();
	}

	@Override
	public Collection getPermissionPKs(final PK principalPK, final boolean negative)

	{
		throw new RuntimeException();
	}

	@Override
	public Collection getRestrictedPrincipalPKs()
	{
		throw new RuntimeException();
	}

	@Override
	public boolean removePermission(final PK principalPK, final PK permissionPK) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean removePermissions(final Collection permissions) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean setPermission(final PK principalPK, final PK permissionPK, final boolean negative) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean setPermissions(final Collection permissions) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	public String getACLOwnerPKsInternal()
	{
		throw new RuntimeException();
	}

	@SuppressWarnings("unused")
	public void setACLOwnerPKsInternal(final String pk)
	{
		throw new RuntimeException();
	}

	@Override
	public PK getTypeKey()
	{
		throw new RuntimeException();
	}

	@Override
	public Map getPrincipalToBooleanListMap(final List rightPKs)
	{
		throw new RuntimeException();
	}

	@Override
	public void setPrincipalToBooleanListMap(final List rightPKs, final Map principalPKToBooleanListMap)
			throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	public ItemEJB getUnderlayingEntity()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setGlobalPermission(final PK permissionPK, final boolean negative) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean setGlobalPermissions(final Collection permissions) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean removeGlobalPermission(final PK permissionPK) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public boolean removeGlobalPermissions(final Collection permissions) throws EJBSecurityException
	{
		throw new RuntimeException();
	}

	@Override
	public Collection getGlobalPermissionPKs(final boolean negative)
	{
		throw new RuntimeException();
	}

	@Override
	public int checkOwnGlobalPermission(final PK permissionPK)
	{
		throw new RuntimeException();
	}


}
