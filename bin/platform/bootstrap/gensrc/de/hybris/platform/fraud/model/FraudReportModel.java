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
package de.hybris.platform.fraud.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type FraudReport first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class FraudReportModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FraudReport";
	
	/**<i>Generated relation code constant for relation <code>OrderFraudReportRelation</code> defining source attribute <code>order</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDERFRAUDREPORTRELATION = "OrderFraudReportRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.provider</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PROVIDER = "provider";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.timestamp</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TIMESTAMP = "timestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.status</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.explanation</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String EXPLANATION = "explanation";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.order</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudReport.fraudSymptomScorings</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String FRAUDSYMPTOMSCORINGS = "fraudSymptomScorings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FraudReportModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FraudReportModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>FraudReport</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>FraudReport</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public FraudReportModel(final String _code, final Date _timestamp)
	{
		super();
		setCode(_code);
		setTimestamp(_timestamp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>FraudReport</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _timestamp initial attribute declared by type <code>FraudReport</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public FraudReportModel(final String _code, final ItemModel _owner, final Date _timestamp)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setTimestamp(_timestamp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.explanation</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the explanation
	 */
	@Accessor(qualifier = "explanation", type = Accessor.Type.GETTER)
	public String getExplanation()
	{
		return getPersistenceContext().getPropertyValue(EXPLANATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.fraudSymptomScorings</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the fraudSymptomScorings
	 */
	@Accessor(qualifier = "fraudSymptomScorings", type = Accessor.Type.GETTER)
	public List<FraudSymptomScoringModel> getFraudSymptomScorings()
	{
		return getPersistenceContext().getPropertyValue(FRAUDSYMPTOMSCORINGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.order</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public OrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.provider</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the provider
	 */
	@Accessor(qualifier = "provider", type = Accessor.Type.GETTER)
	public String getProvider()
	{
		return getPersistenceContext().getPropertyValue(PROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.status</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public FraudStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudReport.timestamp</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
	public Date getTimestamp()
	{
		return getPersistenceContext().getPropertyValue(TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>FraudReport.code</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudReport.explanation</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the explanation
	 */
	@Accessor(qualifier = "explanation", type = Accessor.Type.SETTER)
	public void setExplanation(final String value)
	{
		getPersistenceContext().setPropertyValue(EXPLANATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudReport.fraudSymptomScorings</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the fraudSymptomScorings
	 */
	@Accessor(qualifier = "fraudSymptomScorings", type = Accessor.Type.SETTER)
	public void setFraudSymptomScorings(final List<FraudSymptomScoringModel> value)
	{
		getPersistenceContext().setPropertyValue(FRAUDSYMPTOMSCORINGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudReport.order</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudReport.provider</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the provider
	 */
	@Accessor(qualifier = "provider", type = Accessor.Type.SETTER)
	public void setProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(PROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudReport.status</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final FraudStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>FraudReport.timestamp</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
	public void setTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIMESTAMP, value);
	}
	
}
