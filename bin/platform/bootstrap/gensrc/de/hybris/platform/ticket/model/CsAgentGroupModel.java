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
package de.hybris.platform.ticket.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

/**
 * Generated model class for type CsAgentGroup first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsAgentGroupModel extends UserGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsAgentGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsAgentGroup.emailDistributionList</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EMAILDISTRIBUTIONLIST = "emailDistributionList";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsAgentGroup.defaultAssignee</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String DEFAULTASSIGNEE = "defaultAssignee";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsAgentGroup.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKETSTORES = "ticketstores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsAgentGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsAgentGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CsAgentGroupModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CsAgentGroupModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsAgentGroup.defaultAssignee</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the defaultAssignee
	 */
	@Accessor(qualifier = "defaultAssignee", type = Accessor.Type.GETTER)
	public EmployeeModel getDefaultAssignee()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTASSIGNEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsAgentGroup.emailDistributionList</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the emailDistributionList
	 */
	@Accessor(qualifier = "emailDistributionList", type = Accessor.Type.GETTER)
	public String getEmailDistributionList()
	{
		return getPersistenceContext().getPropertyValue(EMAILDISTRIBUTIONLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsAgentGroup.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the ticketstores
	 */
	@Accessor(qualifier = "ticketstores", type = Accessor.Type.GETTER)
	public List<BaseStoreModel> getTicketstores()
	{
		return getPersistenceContext().getPropertyValue(TICKETSTORES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsAgentGroup.defaultAssignee</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the defaultAssignee
	 */
	@Accessor(qualifier = "defaultAssignee", type = Accessor.Type.SETTER)
	public void setDefaultAssignee(final EmployeeModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTASSIGNEE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsAgentGroup.emailDistributionList</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the emailDistributionList
	 */
	@Accessor(qualifier = "emailDistributionList", type = Accessor.Type.SETTER)
	public void setEmailDistributionList(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAILDISTRIBUTIONLIST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsAgentGroup.ticketstores</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the ticketstores
	 */
	@Accessor(qualifier = "ticketstores", type = Accessor.Type.SETTER)
	public void setTicketstores(final List<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(TICKETSTORES, value);
	}
	
}
