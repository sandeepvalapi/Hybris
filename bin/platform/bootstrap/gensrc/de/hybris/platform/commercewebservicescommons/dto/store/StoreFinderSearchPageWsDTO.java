/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import java.util.List;

public  class StoreFinderSearchPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.stores</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PointOfServiceWsDTO> stores;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<SortWsDTO> sorts;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.locationText</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String locationText;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sourceLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double sourceLatitude;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sourceLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double sourceLongitude;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundNorthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double boundNorthLatitude;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundEastLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double boundEastLongitude;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundSouthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double boundSouthLatitude;

	/** <i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundWestLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double boundWestLongitude;
	
	public StoreFinderSearchPageWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setStores(final List<PointOfServiceWsDTO> stores)
	{
		this.stores = stores;
	}

		
	
	public List<PointOfServiceWsDTO> getStores() 
	{
		return stores;
	}
	
		
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

		
	
	public List<SortWsDTO> getSorts() 
	{
		return sorts;
	}
	
		
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

		
	
	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
		
	
	public void setLocationText(final String locationText)
	{
		this.locationText = locationText;
	}

		
	
	public String getLocationText() 
	{
		return locationText;
	}
	
		
	
	public void setSourceLatitude(final Double sourceLatitude)
	{
		this.sourceLatitude = sourceLatitude;
	}

		
	
	public Double getSourceLatitude() 
	{
		return sourceLatitude;
	}
	
		
	
	public void setSourceLongitude(final Double sourceLongitude)
	{
		this.sourceLongitude = sourceLongitude;
	}

		
	
	public Double getSourceLongitude() 
	{
		return sourceLongitude;
	}
	
		
	
	public void setBoundNorthLatitude(final Double boundNorthLatitude)
	{
		this.boundNorthLatitude = boundNorthLatitude;
	}

		
	
	public Double getBoundNorthLatitude() 
	{
		return boundNorthLatitude;
	}
	
		
	
	public void setBoundEastLongitude(final Double boundEastLongitude)
	{
		this.boundEastLongitude = boundEastLongitude;
	}

		
	
	public Double getBoundEastLongitude() 
	{
		return boundEastLongitude;
	}
	
		
	
	public void setBoundSouthLatitude(final Double boundSouthLatitude)
	{
		this.boundSouthLatitude = boundSouthLatitude;
	}

		
	
	public Double getBoundSouthLatitude() 
	{
		return boundSouthLatitude;
	}
	
		
	
	public void setBoundWestLongitude(final Double boundWestLongitude)
	{
		this.boundWestLongitude = boundWestLongitude;
	}

		
	
	public Double getBoundWestLongitude() 
	{
		return boundWestLongitude;
	}
	


}
