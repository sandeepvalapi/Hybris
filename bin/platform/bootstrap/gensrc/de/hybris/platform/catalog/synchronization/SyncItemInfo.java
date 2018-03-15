/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.catalog.synchronization;

import java.io.Serializable;
import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.core.PK;

public  class SyncItemInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SyncItemInfo.itemPk</code> property defined at extension <code>catalog</code>. */
		
	private PK itemPk;

	/** <i>Generated property</i> for <code>SyncItemInfo.syncJobPk</code> property defined at extension <code>catalog</code>. */
		
	private PK syncJobPk;

	/** <i>Generated property</i> for <code>SyncItemInfo.syncStatus</code> property defined at extension <code>catalog</code>. */
		
	private SyncItemStatus syncStatus;

	/** <i>Generated property</i> for <code>SyncItemInfo.fromSource</code> property defined at extension <code>catalog</code>. */
		
	private Boolean fromSource;

	/** <i>Generated property</i> for <code>SyncItemInfo.syncTimestampPk</code> property defined at extension <code>catalog</code>. */
		
	private PK syncTimestampPk;
	
	public SyncItemInfo()
	{
		// default constructor
	}
	
		
	
	public void setItemPk(final PK itemPk)
	{
		this.itemPk = itemPk;
	}

		
	
	public PK getItemPk() 
	{
		return itemPk;
	}
	
		
	
	public void setSyncJobPk(final PK syncJobPk)
	{
		this.syncJobPk = syncJobPk;
	}

		
	
	public PK getSyncJobPk() 
	{
		return syncJobPk;
	}
	
		
	
	public void setSyncStatus(final SyncItemStatus syncStatus)
	{
		this.syncStatus = syncStatus;
	}

		
	
	public SyncItemStatus getSyncStatus() 
	{
		return syncStatus;
	}
	
		
	
	public void setFromSource(final Boolean fromSource)
	{
		this.fromSource = fromSource;
	}

		
	
	public Boolean getFromSource() 
	{
		return fromSource;
	}
	
		
	
	public void setSyncTimestampPk(final PK syncTimestampPk)
	{
		this.syncTimestampPk = syncTimestampPk;
	}

		
	
	public PK getSyncTimestampPk() 
	{
		return syncTimestampPk;
	}
	


}
