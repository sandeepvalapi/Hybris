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
package de.hybris.platform.acceleratorservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SavedCartFileUploadProcess first defined at extension acceleratorservices.
 * <p>
 * Represents process for CSV file upload for accelerator storefront.
 */
@SuppressWarnings("all")
public class SavedCartFileUploadProcessModel extends StoreFrontCustomerProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SavedCartFileUploadProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedCartFileUploadProcess.uploadedFile</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String UPLOADEDFILE = "uploadedFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedCartFileUploadProcess.savedCart</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SAVEDCART = "savedCart";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SavedCartFileUploadProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SavedCartFileUploadProcessModel(final ItemModelContext ctx)
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
	public SavedCartFileUploadProcessModel(final String _code, final String _processDefinitionName)
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
	public SavedCartFileUploadProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.savedCart</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the savedCart - The saved cart which business process creates.
	 */
	@Accessor(qualifier = "savedCart", type = Accessor.Type.GETTER)
	public CartModel getSavedCart()
	{
		return getPersistenceContext().getPropertyValue(SAVEDCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedCartFileUploadProcess.uploadedFile</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the uploadedFile - The CSV file for upload
	 */
	@Accessor(qualifier = "uploadedFile", type = Accessor.Type.GETTER)
	public MediaModel getUploadedFile()
	{
		return getPersistenceContext().getPropertyValue(UPLOADEDFILE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedCartFileUploadProcess.savedCart</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the savedCart - The saved cart which business process creates.
	 */
	@Accessor(qualifier = "savedCart", type = Accessor.Type.SETTER)
	public void setSavedCart(final CartModel value)
	{
		getPersistenceContext().setPropertyValue(SAVEDCART, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedCartFileUploadProcess.uploadedFile</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the uploadedFile - The CSV file for upload
	 */
	@Accessor(qualifier = "uploadedFile", type = Accessor.Type.SETTER)
	public void setUploadedFile(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(UPLOADEDFILE, value);
	}
	
}
