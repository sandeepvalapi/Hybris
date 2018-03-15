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
package de.hybris.platform.orderscheduling.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrderTemplateToOrderCronJob first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderTemplateToOrderCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderTemplateToOrderCronJob";
	
	/**<i>Generated relation code constant for relation <code>Order2OrderTemplateToOrderCronJob</code> defining source attribute <code>orderTemplate</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDER2ORDERTEMPLATETOORDERCRONJOB = "Order2OrderTemplateToOrderCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderTemplateToOrderCronJob.orderTemplate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERTEMPLATE = "orderTemplate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderTemplateToOrderCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderTemplateToOrderCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public OrderTemplateToOrderCronJobModel(final JobModel _job)
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
	public OrderTemplateToOrderCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderTemplateToOrderCronJob.orderTemplate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the orderTemplate
	 */
	@Accessor(qualifier = "orderTemplate", type = Accessor.Type.GETTER)
	public OrderModel getOrderTemplate()
	{
		return getPersistenceContext().getPropertyValue(ORDERTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderTemplateToOrderCronJob.orderTemplate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the orderTemplate
	 */
	@Accessor(qualifier = "orderTemplate", type = Accessor.Type.SETTER)
	public void setOrderTemplate(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERTEMPLATE, value);
	}
	
}
