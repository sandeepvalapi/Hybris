/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.yprofile.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type PrivacyOverlayComponent first defined at extension privacyoverlayeraddon.
 */
@SuppressWarnings("all")
public class PrivacyOverlayComponentModel extends CMSParagraphComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PrivacyOverlayComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>PrivacyOverlayComponent.acceptText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. */
	public static final String ACCEPTTEXT = "acceptText";
	
	/** <i>Generated constant</i> - Attribute key of <code>PrivacyOverlayComponent.declineButton</code> attribute defined at extension <code>privacyoverlayeraddon</code>. */
	public static final String DECLINEBUTTON = "declineButton";
	
	/** <i>Generated constant</i> - Attribute key of <code>PrivacyOverlayComponent.declineText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. */
	public static final String DECLINETEXT = "declineText";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PrivacyOverlayComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PrivacyOverlayComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public PrivacyOverlayComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public PrivacyOverlayComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrivacyOverlayComponent.acceptText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 * @return the acceptText
	 */
	@Accessor(qualifier = "acceptText", type = Accessor.Type.GETTER)
	public String getAcceptText()
	{
		return getAcceptText(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PrivacyOverlayComponent.acceptText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 * @param loc the value localization key 
	 * @return the acceptText
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "acceptText", type = Accessor.Type.GETTER)
	public String getAcceptText(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(ACCEPTTEXT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrivacyOverlayComponent.declineButton</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 * @return the declineButton
	 */
	@Accessor(qualifier = "declineButton", type = Accessor.Type.GETTER)
	public Boolean getDeclineButton()
	{
		return getPersistenceContext().getPropertyValue(DECLINEBUTTON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrivacyOverlayComponent.declineText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 * @return the declineText
	 */
	@Accessor(qualifier = "declineText", type = Accessor.Type.GETTER)
	public String getDeclineText()
	{
		return getDeclineText(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PrivacyOverlayComponent.declineText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 * @param loc the value localization key 
	 * @return the declineText
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "declineText", type = Accessor.Type.GETTER)
	public String getDeclineText(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DECLINETEXT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrivacyOverlayComponent.acceptText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 *  
	 * @param value the acceptText
	 */
	@Accessor(qualifier = "acceptText", type = Accessor.Type.SETTER)
	public void setAcceptText(final String value)
	{
		setAcceptText(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PrivacyOverlayComponent.acceptText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 *  
	 * @param value the acceptText
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "acceptText", type = Accessor.Type.SETTER)
	public void setAcceptText(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(ACCEPTTEXT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrivacyOverlayComponent.declineButton</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 *  
	 * @param value the declineButton
	 */
	@Accessor(qualifier = "declineButton", type = Accessor.Type.SETTER)
	public void setDeclineButton(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DECLINEBUTTON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrivacyOverlayComponent.declineText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 *  
	 * @param value the declineText
	 */
	@Accessor(qualifier = "declineText", type = Accessor.Type.SETTER)
	public void setDeclineText(final String value)
	{
		setDeclineText(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PrivacyOverlayComponent.declineText</code> attribute defined at extension <code>privacyoverlayeraddon</code>. 
	 *  
	 * @param value the declineText
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "declineText", type = Accessor.Type.SETTER)
	public void setDeclineText(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DECLINETEXT, loc, value);
	}
	
}
