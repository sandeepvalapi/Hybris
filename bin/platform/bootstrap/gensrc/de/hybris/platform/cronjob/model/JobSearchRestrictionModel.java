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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JobSearchRestriction first defined at extension processing.
 */
@SuppressWarnings("all")
public class JobSearchRestrictionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "JobSearchRestriction";
	
	/**<i>Generated relation code constant for relation <code>JobSearchRestrictionRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBSEARCHRESTRICTIONRELATION = "JobSearchRestrictionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.jobPOS</code> attribute defined at extension <code>processing</code>. */
	public static final String JOBPOS = "jobPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. */
	public static final String JOB = "job";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JobSearchRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JobSearchRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public JobSearchRestrictionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code - ID
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. 
	 * @return the job - assigned job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public JobModel getJob()
	{
		return getPersistenceContext().getPropertyValue(JOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. 
	 * @return the query - Search Restriction query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.GETTER)
	public String getQuery()
	{
		return getPersistenceContext().getPropertyValue(QUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. 
	 * @return the type - Code of the Type for which the restriction is defined
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public ComposedTypeModel getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code - ID
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.job</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the job - assigned job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		getPersistenceContext().setPropertyValue(JOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.query</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the query - Search Restriction query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		getPersistenceContext().setPropertyValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JobSearchRestriction.type</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the type - Code of the Type for which the restriction is defined
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
