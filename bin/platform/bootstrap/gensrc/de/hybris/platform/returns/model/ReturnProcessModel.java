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
package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ReturnProcess first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ReturnProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ReturnProcess";
	
	/**<i>Generated relation code constant for relation <code>Return2ReturnProcess</code> defining source attribute <code>returnRequest</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _RETURN2RETURNPROCESS = "Return2ReturnProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnProcess.returnRequest</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNREQUEST = "returnRequest";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReturnProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReturnProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ReturnProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ReturnProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnProcess.returnRequest</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnRequest
	 */
	@Accessor(qualifier = "returnRequest", type = Accessor.Type.GETTER)
	public ReturnRequestModel getReturnRequest()
	{
		return getPersistenceContext().getPropertyValue(RETURNREQUEST);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnProcess.returnRequest</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnRequest
	 */
	@Accessor(qualifier = "returnRequest", type = Accessor.Type.SETTER)
	public void setReturnRequest(final ReturnRequestModel value)
	{
		getPersistenceContext().setPropertyValue(RETURNREQUEST, value);
	}
	
}
