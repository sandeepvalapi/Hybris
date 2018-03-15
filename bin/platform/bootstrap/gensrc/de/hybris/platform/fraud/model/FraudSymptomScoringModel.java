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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type FraudSymptomScoring first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class FraudSymptomScoringModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FraudSymptomScoring";
	
	/**<i>Generated relation code constant for relation <code>FraudReportFraudSymptomScoringRelation</code> defining source attribute <code>fraudReport</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _FRAUDREPORTFRAUDSYMPTOMSCORINGRELATION = "FraudReportFraudSymptomScoringRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudSymptomScoring.name</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudSymptomScoring.score</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String SCORE = "score";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudSymptomScoring.explanation</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String EXPLANATION = "explanation";
	
	/** <i>Generated constant</i> - Attribute key of <code>FraudSymptomScoring.fraudReport</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String FRAUDREPORT = "fraudReport";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FraudSymptomScoringModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FraudSymptomScoringModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _fraudReport initial attribute declared by type <code>FraudSymptomScoring</code> at extension <code>basecommerce</code>
	 * @param _name initial attribute declared by type <code>FraudSymptomScoring</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public FraudSymptomScoringModel(final FraudReportModel _fraudReport, final String _name)
	{
		super();
		setFraudReport(_fraudReport);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _fraudReport initial attribute declared by type <code>FraudSymptomScoring</code> at extension <code>basecommerce</code>
	 * @param _name initial attribute declared by type <code>FraudSymptomScoring</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public FraudSymptomScoringModel(final FraudReportModel _fraudReport, final String _name, final ItemModel _owner)
	{
		super();
		setFraudReport(_fraudReport);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudSymptomScoring.explanation</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the explanation
	 */
	@Accessor(qualifier = "explanation", type = Accessor.Type.GETTER)
	public String getExplanation()
	{
		return getPersistenceContext().getPropertyValue(EXPLANATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudSymptomScoring.fraudReport</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the fraudReport
	 */
	@Accessor(qualifier = "fraudReport", type = Accessor.Type.GETTER)
	public FraudReportModel getFraudReport()
	{
		return getPersistenceContext().getPropertyValue(FRAUDREPORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudSymptomScoring.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FraudSymptomScoring.score</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the score
	 */
	@Accessor(qualifier = "score", type = Accessor.Type.GETTER)
	public double getScore()
	{
		return toPrimitive((Double)getPersistenceContext().getPropertyValue(SCORE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FraudSymptomScoring.explanation</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the explanation
	 */
	@Accessor(qualifier = "explanation", type = Accessor.Type.SETTER)
	public void setExplanation(final String value)
	{
		getPersistenceContext().setPropertyValue(EXPLANATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>FraudSymptomScoring.fraudReport</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the fraudReport
	 */
	@Accessor(qualifier = "fraudReport", type = Accessor.Type.SETTER)
	public void setFraudReport(final FraudReportModel value)
	{
		getPersistenceContext().setPropertyValue(FRAUDREPORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>FraudSymptomScoring.name</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>FraudSymptomScoring.score</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the score
	 */
	@Accessor(qualifier = "score", type = Accessor.Type.SETTER)
	public void setScore(final double value)
	{
		getPersistenceContext().setPropertyValue(SCORE, toObject(value));
	}
	
}
