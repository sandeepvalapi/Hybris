/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
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
 *  
 */
package de.hybris.platform.ycommercewebservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type OldCartRemovalCronJob first defined at extension ycommercewebservices.
 */
@SuppressWarnings("all")
public class OldCartRemovalCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OldCartRemovalCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>OldCartRemovalCronJob.sites</code> attribute defined at extension <code>ycommercewebservices</code>. */
	public static final String SITES = "sites";
	
	/** <i>Generated constant</i> - Attribute key of <code>OldCartRemovalCronJob.cartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. */
	public static final String CARTREMOVALAGE = "cartRemovalAge";
	
	/** <i>Generated constant</i> - Attribute key of <code>OldCartRemovalCronJob.anonymousCartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. */
	public static final String ANONYMOUSCARTREMOVALAGE = "anonymousCartRemovalAge";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OldCartRemovalCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OldCartRemovalCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public OldCartRemovalCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OldCartRemovalCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldCartRemovalCronJob.anonymousCartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 * @return the anonymousCartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 14 days.
	 */
	@Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.GETTER)
	public Integer getAnonymousCartRemovalAge()
	{
		return getPersistenceContext().getPropertyValue(ANONYMOUSCARTREMOVALAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldCartRemovalCronJob.cartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 * @return the cartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 28 days.
	 */
	@Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.GETTER)
	public Integer getCartRemovalAge()
	{
		return getPersistenceContext().getPropertyValue(CARTREMOVALAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OldCartRemovalCronJob.sites</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the sites - BaseSites for which old carts should be removed
	 */
	@Accessor(qualifier = "sites", type = Accessor.Type.GETTER)
	public Collection<BaseSiteModel> getSites()
	{
		return getPersistenceContext().getPropertyValue(SITES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OldCartRemovalCronJob.anonymousCartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 *  
	 * @param value the anonymousCartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 14 days.
	 */
	@Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.SETTER)
	public void setAnonymousCartRemovalAge(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ANONYMOUSCARTREMOVALAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OldCartRemovalCronJob.cartRemovalAge</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 *  
	 * @param value the cartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 28 days.
	 */
	@Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.SETTER)
	public void setCartRemovalAge(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CARTREMOVALAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OldCartRemovalCronJob.sites</code> attribute defined at extension <code>ycommercewebservices</code>. 
	 *  
	 * @param value the sites - BaseSites for which old carts should be removed
	 */
	@Accessor(qualifier = "sites", type = Accessor.Type.SETTER)
	public void setSites(final Collection<BaseSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(SITES, value);
	}
	
}
