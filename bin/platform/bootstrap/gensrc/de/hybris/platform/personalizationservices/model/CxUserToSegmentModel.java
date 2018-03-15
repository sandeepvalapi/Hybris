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
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

/**
 * Generated model class for type CxUserToSegment first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxUserToSegmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxUserToSegment";
	
	/**<i>Generated relation code constant for relation <code>CxSegmentToUserRelation</code> defining source attribute <code>segment</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXSEGMENTTOUSERRELATION = "CxSegmentToUserRelation";
	
	/**<i>Generated relation code constant for relation <code>CxUserToSegmentRelation</code> defining source attribute <code>user</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXUSERTOSEGMENTRELATION = "CxUserToSegmentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUserToSegment.affinity</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String AFFINITY = "affinity";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUserToSegment.segment</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String SEGMENT = "segment";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxUserToSegment.user</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxUserToSegmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxUserToSegmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _affinity initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 * @param _segment initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 * @param _user initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxUserToSegmentModel(final BigDecimal _affinity, final CxSegmentModel _segment, final UserModel _user)
	{
		super();
		setAffinity(_affinity);
		setSegment(_segment);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _affinity initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _segment initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 * @param _user initial attribute declared by type <code>CxUserToSegment</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxUserToSegmentModel(final BigDecimal _affinity, final ItemModel _owner, final CxSegmentModel _segment, final UserModel _user)
	{
		super();
		setAffinity(_affinity);
		setOwner(_owner);
		setSegment(_segment);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUserToSegment.affinity</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the affinity - The affinity of relation between user and segment. Range [0-1]
	 */
	@Accessor(qualifier = "affinity", type = Accessor.Type.GETTER)
	public BigDecimal getAffinity()
	{
		return getPersistenceContext().getPropertyValue(AFFINITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUserToSegment.segment</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the segment
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.GETTER)
	public CxSegmentModel getSegment()
	{
		return getPersistenceContext().getPropertyValue(SEGMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxUserToSegment.user</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUserToSegment.affinity</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the affinity - The affinity of relation between user and segment. Range [0-1]
	 */
	@Accessor(qualifier = "affinity", type = Accessor.Type.SETTER)
	public void setAffinity(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(AFFINITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUserToSegment.segment</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the segment
	 */
	@Accessor(qualifier = "segment", type = Accessor.Type.SETTER)
	public void setSegment(final CxSegmentModel value)
	{
		getPersistenceContext().setPropertyValue(SEGMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxUserToSegment.user</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
