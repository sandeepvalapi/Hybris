/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
package de.hybris.platform.commerceservices.organization.services;

import java.io.Serializable;
import de.hybris.platform.catalog.enums.LineOfBusiness;
import de.hybris.platform.commerceservices.model.OrgUnitModel;

public  class OrgUnitParameter  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrgUnitParameter.uid</code> property defined at extension <code>commerceservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>OrgUnitParameter.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>OrgUnitParameter.parentUnit</code> property defined at extension <code>commerceservices</code>. */
		
	private OrgUnitModel parentUnit;

	/** <i>Generated property</i> for <code>OrgUnitParameter.active</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean active;

	/** <i>Generated property</i> for <code>OrgUnitParameter.description</code> property defined at extension <code>commerceservices</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>OrgUnitParameter.lineOfBusiness</code> property defined at extension <code>commerceservices</code>. */
		
	private LineOfBusiness lineOfBusiness;

	/** <i>Generated property</i> for <code>OrgUnitParameter.orgUnit</code> property defined at extension <code>commerceservices</code>. */
		
	private OrgUnitModel orgUnit;

	/** <i>Generated property</i> for <code>OrgUnitParameter.buyer</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean buyer;

	/** <i>Generated property</i> for <code>OrgUnitParameter.supplier</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean supplier;

	/** <i>Generated property</i> for <code>OrgUnitParameter.manufacturer</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean manufacturer;

	/** <i>Generated property</i> for <code>OrgUnitParameter.carrier</code> property defined at extension <code>commerceservices</code>. */
		
	private Boolean carrier;
	
	public OrgUnitParameter()
	{
		// default constructor
	}
	
		
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

		
	
	public String getUid() 
	{
		return uid;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setParentUnit(final OrgUnitModel parentUnit)
	{
		this.parentUnit = parentUnit;
	}

		
	
	public OrgUnitModel getParentUnit() 
	{
		return parentUnit;
	}
	
		
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

		
	
	public Boolean getActive() 
	{
		return active;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setLineOfBusiness(final LineOfBusiness lineOfBusiness)
	{
		this.lineOfBusiness = lineOfBusiness;
	}

		
	
	public LineOfBusiness getLineOfBusiness() 
	{
		return lineOfBusiness;
	}
	
		
	
	public void setOrgUnit(final OrgUnitModel orgUnit)
	{
		this.orgUnit = orgUnit;
	}

		
	
	public OrgUnitModel getOrgUnit() 
	{
		return orgUnit;
	}
	
		
	
	public void setBuyer(final Boolean buyer)
	{
		this.buyer = buyer;
	}

		
	
	public Boolean getBuyer() 
	{
		return buyer;
	}
	
		
	
	public void setSupplier(final Boolean supplier)
	{
		this.supplier = supplier;
	}

		
	
	public Boolean getSupplier() 
	{
		return supplier;
	}
	
		
	
	public void setManufacturer(final Boolean manufacturer)
	{
		this.manufacturer = manufacturer;
	}

		
	
	public Boolean getManufacturer() 
	{
		return manufacturer;
	}
	
		
	
	public void setCarrier(final Boolean carrier)
	{
		this.carrier = carrier;
	}

		
	
	public Boolean getCarrier() 
	{
		return carrier;
	}
	


}
