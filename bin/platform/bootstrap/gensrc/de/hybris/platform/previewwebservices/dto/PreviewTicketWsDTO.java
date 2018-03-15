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
package de.hybris.platform.previewwebservices.dto;

import java.io.Serializable;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.previewwebservices.dto.CatalogVersionWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Preview Ticket.
 */
@ApiModel(value="previewTicket", description="Preview Ticket.")
public  class PreviewTicketWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Catalog ID of the preview ticket. It is deprecated attribute (use catalogVersions instead)<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.catalog</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="catalog", value="Catalog ID of the preview ticket. It is deprecated attribute (use catalogVersions instead)") 	
	private String catalog;

	/** Catalog version of the preview ticket. It is deprecated attribute (use catalogVersions instead)<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.catalogVersion</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="catalogVersion", value="Catalog version of the preview ticket. It is deprecated attribute (use catalogVersions instead)") 	
	private String catalogVersion;

	/** Resource path.<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.resourcePath</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="resourcePath", value="Resource path.", required=true) 	
	private String resourcePath;

	/** User of the preview ticket<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.user</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="user", value="User of the preview ticket") 	
	private String user;

	/** User group of the preview ticket<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.userGroup</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="userGroup", value="User group of the preview ticket") 	
	private String userGroup;

	/** Language of the preview ticket<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.language</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="language", value="Language of the preview ticket") 	
	private String language;

	/** Time of the preview ticket<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.time</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="time", value="Time of the preview ticket") 	
	private Date time;

	/** Preview ticket ID<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.ticketId</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="ticketId", value="Preview ticket ID") 	
	private String ticketId;

	/** CMS page ID to use for the preview<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.pageId</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="pageId", value="CMS page ID to use for the preview") 	
	private String pageId;

	/** Catalog version list<br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.catalogVersions</code> property defined at extension <code>previewwebservices</code>. */
	@ApiModelProperty(name="catalogVersions", value="Catalog version list") 	
	private List<CatalogVersionWsDTO> catalogVersions;

	/** <i>Generated property</i> for <code>PreviewTicketWsDTO.variations</code> property defined at extension <code>previewpersonalizationweb</code>. */
	@ApiModelProperty(name="variations") 	
	private Collection<CxVariationKey> variations;

	/** <i>Generated property</i> for <code>PreviewTicketWsDTO.segments</code> property defined at extension <code>previewpersonalizationweb</code>. */
	@ApiModelProperty(name="segments") 	
	private Collection<String> segments;
	
	public PreviewTicketWsDTO()
	{
		// default constructor
	}
	
	@Deprecated
    /**
     * @deprecated Deprecated since 6.5, please use catalogVersions attribute instead.
     */
		
	
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}

	@Deprecated
	/**
	 * @deprecated Deprecated since 6.5, please use catalogVersions attribute instead.
	 */
		
	
	public String getCatalog() 
	{
		return catalog;
	}
	
	@Deprecated
    /**
     * @deprecated Deprecated since 6.5, please use catalogVersions attribute instead.
     */
		
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	@Deprecated
	/**
	 * @deprecated Deprecated since 6.5, please use catalogVersions attribute instead.
	 */
		
	
	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	
		
	
	public void setResourcePath(final String resourcePath)
	{
		this.resourcePath = resourcePath;
	}

		
	
	public String getResourcePath() 
	{
		return resourcePath;
	}
	
		
	
	public void setUser(final String user)
	{
		this.user = user;
	}

		
	
	public String getUser() 
	{
		return user;
	}
	
		
	
	public void setUserGroup(final String userGroup)
	{
		this.userGroup = userGroup;
	}

		
	
	public String getUserGroup() 
	{
		return userGroup;
	}
	
		
	
	public void setLanguage(final String language)
	{
		this.language = language;
	}

		
	
	public String getLanguage() 
	{
		return language;
	}
	
		
	
	public void setTime(final Date time)
	{
		this.time = time;
	}

		
	
	public Date getTime() 
	{
		return time;
	}
	
		
	
	public void setTicketId(final String ticketId)
	{
		this.ticketId = ticketId;
	}

		
	
	public String getTicketId() 
	{
		return ticketId;
	}
	
		
	
	public void setPageId(final String pageId)
	{
		this.pageId = pageId;
	}

		
	
	public String getPageId() 
	{
		return pageId;
	}
	
		
	
	public void setCatalogVersions(final List<CatalogVersionWsDTO> catalogVersions)
	{
		this.catalogVersions = catalogVersions;
	}

		
	
	public List<CatalogVersionWsDTO> getCatalogVersions() 
	{
		return catalogVersions;
	}
	
		
	
	public void setVariations(final Collection<CxVariationKey> variations)
	{
		this.variations = variations;
	}

		
	
	public Collection<CxVariationKey> getVariations() 
	{
		return variations;
	}
	
		
	
	public void setSegments(final Collection<String> segments)
	{
		this.segments = segments;
	}

		
	
	public Collection<String> getSegments() 
	{
		return segments;
	}
	


}
