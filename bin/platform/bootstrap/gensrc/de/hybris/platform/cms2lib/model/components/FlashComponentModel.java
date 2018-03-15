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
package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2lib.enums.FlashQuality;
import de.hybris.platform.cms2lib.enums.FlashSalign;
import de.hybris.platform.cms2lib.enums.FlashScale;
import de.hybris.platform.cms2lib.enums.FlashWmode;
import de.hybris.platform.cms2lib.model.components.AbstractBannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type FlashComponent first defined at extension cms2lib.
 */
@SuppressWarnings("all")
public class FlashComponentModel extends AbstractBannerComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FlashComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.play</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PLAY = "play";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.loop</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String LOOP = "loop";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.menu</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String MENU = "menu";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.quality</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String QUALITY = "quality";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.scale</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String SCALE = "scale";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.wmode</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String WMODE = "wmode";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.sAlign</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String SALIGN = "sAlign";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.bgcolor</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String BGCOLOR = "bgcolor";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.width</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String WIDTH = "width";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.height</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String HEIGHT = "height";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.pageLabelOrId</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PAGELABELORID = "pageLabelOrId";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashComponent.page</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String PAGE = "page";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlashComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlashComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _external initial attribute declared by type <code>AbstractBannerComponent</code> at extension <code>cms2lib</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public FlashComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _external initial attribute declared by type <code>AbstractBannerComponent</code> at extension <code>cms2lib</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public FlashComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.bgcolor</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the bgcolor
	 */
	@Accessor(qualifier = "bgcolor", type = Accessor.Type.GETTER)
	public String getBgcolor()
	{
		return getPersistenceContext().getPropertyValue(BGCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.height</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the height
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.GETTER)
	public Integer getHeight()
	{
		return getPersistenceContext().getPropertyValue(HEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.loop</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the loop
	 */
	@Accessor(qualifier = "loop", type = Accessor.Type.GETTER)
	public Boolean getLoop()
	{
		return getPersistenceContext().getPropertyValue(LOOP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.menu</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the menu
	 */
	@Accessor(qualifier = "menu", type = Accessor.Type.GETTER)
	public Boolean getMenu()
	{
		return getPersistenceContext().getPropertyValue(MENU);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.page</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the page
	 */
	@Accessor(qualifier = "page", type = Accessor.Type.GETTER)
	public ContentPageModel getPage()
	{
		return getPersistenceContext().getPropertyValue(PAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.pageLabelOrId</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the pageLabelOrId
	 */
	@Accessor(qualifier = "pageLabelOrId", type = Accessor.Type.GETTER)
	public String getPageLabelOrId()
	{
		return getPersistenceContext().getPropertyValue(PAGELABELORID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.play</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the play
	 */
	@Accessor(qualifier = "play", type = Accessor.Type.GETTER)
	public Boolean getPlay()
	{
		return getPersistenceContext().getPropertyValue(PLAY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.quality</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the quality
	 */
	@Accessor(qualifier = "quality", type = Accessor.Type.GETTER)
	public FlashQuality getQuality()
	{
		return getPersistenceContext().getPropertyValue(QUALITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.sAlign</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the sAlign
	 */
	@Accessor(qualifier = "sAlign", type = Accessor.Type.GETTER)
	public FlashSalign getSAlign()
	{
		return getPersistenceContext().getPropertyValue(SALIGN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.scale</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the scale
	 */
	@Accessor(qualifier = "scale", type = Accessor.Type.GETTER)
	public FlashScale getScale()
	{
		return getPersistenceContext().getPropertyValue(SCALE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.width</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the width
	 */
	@Accessor(qualifier = "width", type = Accessor.Type.GETTER)
	public Integer getWidth()
	{
		return getPersistenceContext().getPropertyValue(WIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashComponent.wmode</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the wmode
	 */
	@Accessor(qualifier = "wmode", type = Accessor.Type.GETTER)
	public FlashWmode getWmode()
	{
		return getPersistenceContext().getPropertyValue(WMODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.bgcolor</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the bgcolor
	 */
	@Accessor(qualifier = "bgcolor", type = Accessor.Type.SETTER)
	public void setBgcolor(final String value)
	{
		getPersistenceContext().setPropertyValue(BGCOLOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.height</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the height
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.SETTER)
	public void setHeight(final Integer value)
	{
		getPersistenceContext().setPropertyValue(HEIGHT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.loop</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the loop
	 */
	@Accessor(qualifier = "loop", type = Accessor.Type.SETTER)
	public void setLoop(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOOP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.menu</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the menu
	 */
	@Accessor(qualifier = "menu", type = Accessor.Type.SETTER)
	public void setMenu(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MENU, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.page</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the page
	 */
	@Accessor(qualifier = "page", type = Accessor.Type.SETTER)
	public void setPage(final ContentPageModel value)
	{
		getPersistenceContext().setPropertyValue(PAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.play</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the play
	 */
	@Accessor(qualifier = "play", type = Accessor.Type.SETTER)
	public void setPlay(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PLAY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.quality</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the quality
	 */
	@Accessor(qualifier = "quality", type = Accessor.Type.SETTER)
	public void setQuality(final FlashQuality value)
	{
		getPersistenceContext().setPropertyValue(QUALITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.sAlign</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the sAlign
	 */
	@Accessor(qualifier = "sAlign", type = Accessor.Type.SETTER)
	public void setSAlign(final FlashSalign value)
	{
		getPersistenceContext().setPropertyValue(SALIGN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.scale</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the scale
	 */
	@Accessor(qualifier = "scale", type = Accessor.Type.SETTER)
	public void setScale(final FlashScale value)
	{
		getPersistenceContext().setPropertyValue(SCALE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.width</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the width
	 */
	@Accessor(qualifier = "width", type = Accessor.Type.SETTER)
	public void setWidth(final Integer value)
	{
		getPersistenceContext().setPropertyValue(WIDTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashComponent.wmode</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the wmode
	 */
	@Accessor(qualifier = "wmode", type = Accessor.Type.SETTER)
	public void setWmode(final FlashWmode value)
	{
		getPersistenceContext().setPropertyValue(WMODE, value);
	}
	
}
