/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at Mar 15, 2018 5:02:42 PM
* ----------------------------------------------------------------
*
* [y] hybris Platform
*
* Copyright (c) 2000-2017 SAP SE
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* Hybris ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the
* terms of the license agreement you entered into with SAP Hybris.
*/
package de.hybris.platform.rulebuilderbackoffice.editors;
 
/**
 * Represents severity level of Rule Validation results
 */
public enum ValidationInfoSeverity   
{
	/** <i>Generated enum value</i> for <code>ValidationInfoSeverity.ERROR("z-icon-times-circle", "ye-validation-error")</code> value defined at extension <code>rulebuilderbackoffice</code>. */
	ERROR("z-icon-times-circle", "ye-validation-error")  , 
	/** <i>Generated enum value</i> for <code>ValidationInfoSeverity.WARN("z-icon-exclamation-triangle", "ye-validation-warn")</code> value defined at extension <code>rulebuilderbackoffice</code>. */
	WARN("z-icon-exclamation-triangle", "ye-validation-warn")  , 
	/** <i>Generated enum value</i> for <code>ValidationInfoSeverity.INFO("z-icon-info-circle", "ye-validation-info")</code> value defined at extension <code>rulebuilderbackoffice</code>. */
	INFO("z-icon-info-circle", "ye-validation-info")  , 
	/** <i>Generated enum value</i> for <code>ValidationInfoSeverity.NONE("", "ye-validation-none")</code> value defined at extension <code>rulebuilderbackoffice</code>. */
	NONE("", "ye-validation-none") ; 
 
	private final String iconStyleClass;
	private final String styleClass;

	private ValidationInfoSeverity(final String iconStyleClass, final String styleClass)
	{
		this.iconStyleClass = iconStyleClass;
		this.styleClass = styleClass;
	}

	public String getIconStyleClass()
	{
		return iconStyleClass;
	}

	public String getStyleClass()
	{
		return styleClass;
	}
}