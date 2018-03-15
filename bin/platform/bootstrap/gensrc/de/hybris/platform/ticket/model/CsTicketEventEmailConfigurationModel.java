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
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import java.util.Set;

/**
 * Generated model class for type CsTicketEventEmailConfiguration first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketEventEmailConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicketEventEmailConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.code</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.plainTextTemplate</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String PLAINTEXTTEMPLATE = "plainTextTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.htmlTemplate</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String HTMLTEMPLATE = "htmlTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.subject</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.eventType</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EVENTTYPE = "eventType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.alteredAttributes</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ALTEREDATTRIBUTES = "alteredAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketEventEmailConfiguration.recipientType</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String RECIPIENTTYPE = "recipientType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketEventEmailConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketEventEmailConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CsTicketEventEmailConfiguration</code> at extension <code>ticketsystem</code>
	 */
	@Deprecated
	public CsTicketEventEmailConfigurationModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CsTicketEventEmailConfiguration</code> at extension <code>ticketsystem</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CsTicketEventEmailConfigurationModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.alteredAttributes</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the alteredAttributes
	 */
	@Accessor(qualifier = "alteredAttributes", type = Accessor.Type.GETTER)
	public Set<AttributeDescriptorModel> getAlteredAttributes()
	{
		return getPersistenceContext().getPropertyValue(ALTEREDATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.code</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.eventType</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the eventType
	 */
	@Accessor(qualifier = "eventType", type = Accessor.Type.GETTER)
	public CommentTypeModel getEventType()
	{
		return getPersistenceContext().getPropertyValue(EVENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.htmlTemplate</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the htmlTemplate
	 */
	@Accessor(qualifier = "htmlTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getHtmlTemplate()
	{
		return getPersistenceContext().getPropertyValue(HTMLTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.plainTextTemplate</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the plainTextTemplate
	 */
	@Accessor(qualifier = "plainTextTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getPlainTextTemplate()
	{
		return getPersistenceContext().getPropertyValue(PLAINTEXTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.recipientType</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the recipientType
	 */
	@Accessor(qualifier = "recipientType", type = Accessor.Type.GETTER)
	public CsEmailRecipients getRecipientType()
	{
		return getPersistenceContext().getPropertyValue(RECIPIENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketEventEmailConfiguration.subject</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.alteredAttributes</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the alteredAttributes
	 */
	@Accessor(qualifier = "alteredAttributes", type = Accessor.Type.SETTER)
	public void setAlteredAttributes(final Set<AttributeDescriptorModel> value)
	{
		getPersistenceContext().setPropertyValue(ALTEREDATTRIBUTES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CsTicketEventEmailConfiguration.code</code> attribute defined at extension <code>ticketsystem</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.eventType</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the eventType
	 */
	@Accessor(qualifier = "eventType", type = Accessor.Type.SETTER)
	public void setEventType(final CommentTypeModel value)
	{
		getPersistenceContext().setPropertyValue(EVENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.htmlTemplate</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the htmlTemplate
	 */
	@Accessor(qualifier = "htmlTemplate", type = Accessor.Type.SETTER)
	public void setHtmlTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(HTMLTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.plainTextTemplate</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the plainTextTemplate
	 */
	@Accessor(qualifier = "plainTextTemplate", type = Accessor.Type.SETTER)
	public void setPlainTextTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(PLAINTEXTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.recipientType</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the recipientType
	 */
	@Accessor(qualifier = "recipientType", type = Accessor.Type.SETTER)
	public void setRecipientType(final CsEmailRecipients value)
	{
		getPersistenceContext().setPropertyValue(RECIPIENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketEventEmailConfiguration.subject</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
}
