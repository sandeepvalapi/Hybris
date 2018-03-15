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
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Employee first defined at extension core.
 */
@SuppressWarnings("all")
public class EmployeeModel extends UserModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Employee";
	
	/** <i>Generated constant</i> - Attribute key of <code>Employee.organizationRoles</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ORGANIZATIONROLES = "organizationRoles";
	
	/** <i>Generated constant</i> - Attribute key of <code>Employee.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKETSTORES = "ticketstores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EmployeeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EmployeeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public EmployeeModel(final boolean _loginDisabled, final String _uid)
	{
		super();
		setLoginDisabled(_loginDisabled);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customerReviews initial attribute declared by type <code>User</code> at extension <code>customerreview</code>
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public EmployeeModel(final Collection<CustomerReviewModel> _customerReviews, final boolean _loginDisabled, final ItemModel _owner, final String _uid)
	{
		super();
		setCustomerReviews(_customerReviews);
		setLoginDisabled(_loginDisabled);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Employee.organizationRoles</code> dynamic attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the organizationRoles - OrganizationRoles will only have organization related roles as its members
	 */
	@Accessor(qualifier = "organizationRoles", type = Accessor.Type.GETTER)
	public Collection<PrincipalGroupModel> getOrganizationRoles()
	{
		return getPersistenceContext().getDynamicValue(this,ORGANIZATIONROLES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Employee.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ticketstores
	 */
	@Accessor(qualifier = "ticketstores", type = Accessor.Type.GETTER)
	public List<BaseStoreModel> getTicketstores()
	{
		return getPersistenceContext().getPropertyValue(TICKETSTORES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Employee.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the ticketstores
	 */
	@Accessor(qualifier = "ticketstores", type = Accessor.Type.SETTER)
	public void setTicketstores(final List<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(TICKETSTORES, value);
	}
	
}
