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
package de.hybris.platform.cms2.model.preview;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.enums.LiveEditVariant;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type PreviewData first defined at extension cms2.
 */
@SuppressWarnings("all")
public class PreviewDataModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PreviewData";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.user</code> attribute defined at extension <code>cms2</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.userGroup</code> attribute defined at extension <code>cms2</code>. */
	public static final String USERGROUP = "userGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.country</code> attribute defined at extension <code>cms2</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.language</code> attribute defined at extension <code>cms2</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.time</code> attribute defined at extension <code>cms2</code>. */
	public static final String TIME = "time";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.liveEdit</code> attribute defined at extension <code>cms2</code>. */
	public static final String LIVEEDIT = "liveEdit";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.editMode</code> attribute defined at extension <code>cms2</code>. */
	public static final String EDITMODE = "editMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.resourcePath</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESOURCEPATH = "resourcePath";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.page</code> attribute defined at extension <code>cms2</code>. */
	public static final String PAGE = "page";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.previewCategory</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWCATEGORY = "previewCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.previewProduct</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWPRODUCT = "previewProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.previewCatalog</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWCATALOG = "previewCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.activeSite</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVESITE = "activeSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.activeCatalogVersion</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.catalogVersions</code> attribute defined at extension <code>cms2</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.uiExperience</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String UIEXPERIENCE = "uiExperience";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.code</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.variations</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String VARIATIONS = "variations";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.segments</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String SEGMENTS = "segments";
	
	/** <i>Generated constant</i> - Attribute key of <code>PreviewData.liveEditVariant</code> attribute defined at extension <code>cmscockpit</code>. */
	public static final String LIVEEDITVARIANT = "liveEditVariant";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PreviewDataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PreviewDataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _liveEdit initial attribute declared by type <code>PreviewData</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public PreviewDataModel(final Boolean _liveEdit)
	{
		super();
		setLiveEdit(_liveEdit);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _liveEdit initial attribute declared by type <code>PreviewData</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PreviewDataModel(final Boolean _liveEdit, final ItemModel _owner)
	{
		super();
		setLiveEdit(_liveEdit);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.activeCatalogVersion</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeCatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getActiveCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(ACTIVECATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.activeSite</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeSite
	 */
	@Accessor(qualifier = "activeSite", type = Accessor.Type.GETTER)
	public CMSSiteModel getActiveSite()
	{
		return getPersistenceContext().getPropertyValue(ACTIVESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.catalogVersions</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public Collection<CatalogVersionModel> getCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.code</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.country</code> attribute defined at extension <code>cms2</code>. 
	 * @return the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		return getPersistenceContext().getPropertyValue(COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.editMode</code> attribute defined at extension <code>cms2</code>. 
	 * @return the editMode
	 */
	@Accessor(qualifier = "editMode", type = Accessor.Type.GETTER)
	public Boolean getEditMode()
	{
		return getPersistenceContext().getPropertyValue(EDITMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.language</code> attribute defined at extension <code>cms2</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.liveEdit</code> attribute defined at extension <code>cms2</code>. 
	 * @return the liveEdit
	 */
	@Accessor(qualifier = "liveEdit", type = Accessor.Type.GETTER)
	public Boolean getLiveEdit()
	{
		return getPersistenceContext().getPropertyValue(LIVEEDIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.liveEditVariant</code> attribute defined at extension <code>cmscockpit</code>. 
	 * @return the liveEditVariant
	 */
	@Accessor(qualifier = "liveEditVariant", type = Accessor.Type.GETTER)
	public LiveEditVariant getLiveEditVariant()
	{
		return getPersistenceContext().getPropertyValue(LIVEEDITVARIANT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.page</code> attribute defined at extension <code>cms2</code>. 
	 * @return the page
	 */
	@Accessor(qualifier = "page", type = Accessor.Type.GETTER)
	public AbstractPageModel getPage()
	{
		return getPersistenceContext().getPropertyValue(PAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.previewCatalog</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewCatalog
	 */
	@Accessor(qualifier = "previewCatalog", type = Accessor.Type.GETTER)
	public CatalogModel getPreviewCatalog()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.previewCategory</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewCategory
	 */
	@Accessor(qualifier = "previewCategory", type = Accessor.Type.GETTER)
	public CategoryModel getPreviewCategory()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.previewProduct</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewProduct
	 */
	@Accessor(qualifier = "previewProduct", type = Accessor.Type.GETTER)
	public ProductModel getPreviewProduct()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.resourcePath</code> attribute defined at extension <code>cms2</code>. 
	 * @return the resourcePath
	 */
	@Accessor(qualifier = "resourcePath", type = Accessor.Type.GETTER)
	public String getResourcePath()
	{
		return getPersistenceContext().getPropertyValue(RESOURCEPATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.segments</code> attribute defined at extension <code>personalizationcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the segments
	 */
	@Accessor(qualifier = "segments", type = Accessor.Type.GETTER)
	public Collection<CxSegmentModel> getSegments()
	{
		return getPersistenceContext().getPropertyValue(SEGMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.time</code> attribute defined at extension <code>cms2</code>. 
	 * @return the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.GETTER)
	public Date getTime()
	{
		return getPersistenceContext().getPropertyValue(TIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.uiExperience</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the uiExperience
	 */
	@Accessor(qualifier = "uiExperience", type = Accessor.Type.GETTER)
	public UiExperienceLevel getUiExperience()
	{
		return getPersistenceContext().getPropertyValue(UIEXPERIENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.user</code> attribute defined at extension <code>cms2</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.userGroup</code> attribute defined at extension <code>cms2</code>. 
	 * @return the userGroup
	 */
	@Accessor(qualifier = "userGroup", type = Accessor.Type.GETTER)
	public UserGroupModel getUserGroup()
	{
		return getPersistenceContext().getPropertyValue(USERGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PreviewData.variations</code> attribute defined at extension <code>personalizationcms</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the variations
	 */
	@Accessor(qualifier = "variations", type = Accessor.Type.GETTER)
	public Collection<CxVariationModel> getVariations()
	{
		return getPersistenceContext().getPropertyValue(VARIATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.activeCatalogVersion</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeCatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.SETTER)
	public void setActiveCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIVECATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.activeSite</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeSite
	 */
	@Accessor(qualifier = "activeSite", type = Accessor.Type.SETTER)
	public void setActiveSite(final CMSSiteModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIVESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.catalogVersions</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
	public void setCatalogVersions(final Collection<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.code</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.country</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		getPersistenceContext().setPropertyValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.editMode</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the editMode
	 */
	@Accessor(qualifier = "editMode", type = Accessor.Type.SETTER)
	public void setEditMode(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(EDITMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.language</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.liveEdit</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the liveEdit
	 */
	@Accessor(qualifier = "liveEdit", type = Accessor.Type.SETTER)
	public void setLiveEdit(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LIVEEDIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.liveEditVariant</code> attribute defined at extension <code>cmscockpit</code>. 
	 *  
	 * @param value the liveEditVariant
	 */
	@Accessor(qualifier = "liveEditVariant", type = Accessor.Type.SETTER)
	public void setLiveEditVariant(final LiveEditVariant value)
	{
		getPersistenceContext().setPropertyValue(LIVEEDITVARIANT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.page</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the page
	 */
	@Accessor(qualifier = "page", type = Accessor.Type.SETTER)
	public void setPage(final AbstractPageModel value)
	{
		getPersistenceContext().setPropertyValue(PAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.previewCatalog</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewCatalog
	 */
	@Accessor(qualifier = "previewCatalog", type = Accessor.Type.SETTER)
	public void setPreviewCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.previewCategory</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewCategory
	 */
	@Accessor(qualifier = "previewCategory", type = Accessor.Type.SETTER)
	public void setPreviewCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWCATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.previewProduct</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewProduct
	 */
	@Accessor(qualifier = "previewProduct", type = Accessor.Type.SETTER)
	public void setPreviewProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.resourcePath</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the resourcePath
	 */
	@Accessor(qualifier = "resourcePath", type = Accessor.Type.SETTER)
	public void setResourcePath(final String value)
	{
		getPersistenceContext().setPropertyValue(RESOURCEPATH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.segments</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the segments
	 */
	@Accessor(qualifier = "segments", type = Accessor.Type.SETTER)
	public void setSegments(final Collection<CxSegmentModel> value)
	{
		getPersistenceContext().setPropertyValue(SEGMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.time</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.SETTER)
	public void setTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.uiExperience</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the uiExperience
	 */
	@Accessor(qualifier = "uiExperience", type = Accessor.Type.SETTER)
	public void setUiExperience(final UiExperienceLevel value)
	{
		getPersistenceContext().setPropertyValue(UIEXPERIENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.user</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.userGroup</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the userGroup
	 */
	@Accessor(qualifier = "userGroup", type = Accessor.Type.SETTER)
	public void setUserGroup(final UserGroupModel value)
	{
		getPersistenceContext().setPropertyValue(USERGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PreviewData.variations</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the variations
	 */
	@Accessor(qualifier = "variations", type = Accessor.Type.SETTER)
	public void setVariations(final Collection<CxVariationModel> value)
	{
		getPersistenceContext().setPropertyValue(VARIATIONS, value);
	}
	
}
