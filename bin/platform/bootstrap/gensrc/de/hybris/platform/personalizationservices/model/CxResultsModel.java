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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type CxResults first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxResultsModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxResults";
	
	/**<i>Generated relation code constant for relation <code>CxUserToCxResults</code> defining source attribute <code>user</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXUSERTOCXRESULTS = "CxUserToCxResults";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.key</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String KEY = "key";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.sessionKey</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String SESSIONKEY = "sessionKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.results</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String RESULTS = "results";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.calculationTime</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CALCULATIONTIME = "calculationTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.anonymous</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ANONYMOUS = "anonymous";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResults.user</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxResultsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxResultsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _calculationTime initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _key initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _results initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _sessionKey initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxResultsModel(final Date _calculationTime, final CatalogVersionModel _catalogVersion, final String _key, final Object _results, final String _sessionKey)
	{
		super();
		setCalculationTime(_calculationTime);
		setCatalogVersion(_catalogVersion);
		setKey(_key);
		setResults(_results);
		setSessionKey(_sessionKey);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _calculationTime initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _key initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _results initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 * @param _sessionKey initial attribute declared by type <code>CxResults</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxResultsModel(final Date _calculationTime, final CatalogVersionModel _catalogVersion, final String _key, final ItemModel _owner, final Object _results, final String _sessionKey)
	{
		super();
		setCalculationTime(_calculationTime);
		setCatalogVersion(_catalogVersion);
		setKey(_key);
		setOwner(_owner);
		setResults(_results);
		setSessionKey(_sessionKey);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.calculationTime</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the calculationTime
	 */
	@Accessor(qualifier = "calculationTime", type = Accessor.Type.GETTER)
	public Date getCalculationTime()
	{
		return getPersistenceContext().getPropertyValue(CALCULATIONTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.key</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.GETTER)
	public String getKey()
	{
		return getPersistenceContext().getPropertyValue(KEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.results</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the results
	 */
	@Accessor(qualifier = "results", type = Accessor.Type.GETTER)
	public Object getResults()
	{
		return getPersistenceContext().getPropertyValue(RESULTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.sessionKey</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the sessionKey
	 */
	@Accessor(qualifier = "sessionKey", type = Accessor.Type.GETTER)
	public String getSessionKey()
	{
		return getPersistenceContext().getPropertyValue(SESSIONKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.user</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResults.anonymous</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the anonymous
	 */
	@Accessor(qualifier = "anonymous", type = Accessor.Type.GETTER)
	public boolean isAnonymous()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ANONYMOUS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.anonymous</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the anonymous
	 */
	@Accessor(qualifier = "anonymous", type = Accessor.Type.SETTER)
	public void setAnonymous(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ANONYMOUS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.calculationTime</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the calculationTime
	 */
	@Accessor(qualifier = "calculationTime", type = Accessor.Type.SETTER)
	public void setCalculationTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(CALCULATIONTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.key</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.SETTER)
	public void setKey(final String value)
	{
		getPersistenceContext().setPropertyValue(KEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.results</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the results
	 */
	@Accessor(qualifier = "results", type = Accessor.Type.SETTER)
	public void setResults(final Object value)
	{
		getPersistenceContext().setPropertyValue(RESULTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.sessionKey</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the sessionKey
	 */
	@Accessor(qualifier = "sessionKey", type = Accessor.Type.SETTER)
	public void setSessionKey(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONKEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResults.user</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
