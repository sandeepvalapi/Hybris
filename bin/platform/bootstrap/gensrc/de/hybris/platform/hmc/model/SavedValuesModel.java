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
package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.SavedValueEntryType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type SavedValues first defined at extension core.
 */
@SuppressWarnings("all")
public class SavedValuesModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SavedValues";
	
	/**<i>Generated relation code constant for relation <code>ItemSavedValuesRelation</code> defining source attribute <code>modifiedItem</code> in extension <code>core</code>.</i>*/
	public static final String _ITEMSAVEDVALUESRELATION = "ItemSavedValuesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.modifiedItemType</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDITEMTYPE = "modifiedItemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.modifiedItemDisplayString</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDITEMDISPLAYSTRING = "modifiedItemDisplayString";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.timestamp</code> attribute defined at extension <code>core</code>. */
	public static final String TIMESTAMP = "timestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.user</code> attribute defined at extension <code>core</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.changedAttributes</code> attribute defined at extension <code>core</code>. */
	public static final String CHANGEDATTRIBUTES = "changedAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.numberOfChangedAttributes</code> attribute defined at extension <code>core</code>. */
	public static final String NUMBEROFCHANGEDATTRIBUTES = "numberOfChangedAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.modificationType</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFICATIONTYPE = "modificationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.savedValuesEntries</code> attribute defined at extension <code>core</code>. */
	public static final String SAVEDVALUESENTRIES = "savedValuesEntries";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.modifiedItemPOS</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDITEMPOS = "modifiedItemPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedValues.modifiedItem</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDITEM = "modifiedItem";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SavedValuesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SavedValuesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _modificationType initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _modifiedItemDisplayString initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _modifiedItemType initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _timestamp initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedValuesModel(final SavedValueEntryType _modificationType, final String _modifiedItemDisplayString, final ComposedTypeModel _modifiedItemType, final Date _timestamp, final UserModel _user)
	{
		super();
		setModificationType(_modificationType);
		setModifiedItemDisplayString(_modifiedItemDisplayString);
		setModifiedItemType(_modifiedItemType);
		setTimestamp(_timestamp);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _modificationType initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _modifiedItem initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _modifiedItemDisplayString initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _modifiedItemType initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _timestamp initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>SavedValues</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedValuesModel(final SavedValueEntryType _modificationType, final ItemModel _modifiedItem, final String _modifiedItemDisplayString, final ComposedTypeModel _modifiedItemType, final ItemModel _owner, final Date _timestamp, final UserModel _user)
	{
		super();
		setModificationType(_modificationType);
		setModifiedItem(_modifiedItem);
		setModifiedItemDisplayString(_modifiedItemDisplayString);
		setModifiedItemType(_modifiedItemType);
		setOwner(_owner);
		setTimestamp(_timestamp);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.changedAttributes</code> attribute defined at extension <code>core</code>. 
	 * @return the changedAttributes - jalo generated string of changes attributes
	 */
	@Accessor(qualifier = "changedAttributes", type = Accessor.Type.GETTER)
	public String getChangedAttributes()
	{
		return getPersistenceContext().getPropertyValue(CHANGEDATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.modificationType</code> attribute defined at extension <code>core</code>. 
	 * @return the modificationType - the type of the 'modification' action (save,create,remove). @since 2.10
	 */
	@Accessor(qualifier = "modificationType", type = Accessor.Type.GETTER)
	public SavedValueEntryType getModificationType()
	{
		return getPersistenceContext().getPropertyValue(MODIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.modifiedItem</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedItem
	 */
	@Accessor(qualifier = "modifiedItem", type = Accessor.Type.GETTER)
	public ItemModel getModifiedItem()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.modifiedItemDisplayString</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedItemDisplayString - Display String. @since 2.10
	 */
	@Accessor(qualifier = "modifiedItemDisplayString", type = Accessor.Type.GETTER)
	public String getModifiedItemDisplayString()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDITEMDISPLAYSTRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.modifiedItemType</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedItemType - Type
	 */
	@Accessor(qualifier = "modifiedItemType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getModifiedItemType()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDITEMTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.numberOfChangedAttributes</code> attribute defined at extension <code>core</code>. 
	 * @return the numberOfChangedAttributes - jalo generated string of changes attributes
	 */
	@Accessor(qualifier = "numberOfChangedAttributes", type = Accessor.Type.GETTER)
	public Integer getNumberOfChangedAttributes()
	{
		return getPersistenceContext().getPropertyValue(NUMBEROFCHANGEDATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.savedValuesEntries</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the savedValuesEntries
	 */
	@Accessor(qualifier = "savedValuesEntries", type = Accessor.Type.GETTER)
	public Set<SavedValueEntryModel> getSavedValuesEntries()
	{
		return getPersistenceContext().getPropertyValue(SAVEDVALUESENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.timestamp</code> attribute defined at extension <code>core</code>. 
	 * @return the timestamp - the timestamp of the last modification
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
	public Date getTimestamp()
	{
		return getPersistenceContext().getPropertyValue(TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedValues.user</code> attribute defined at extension <code>core</code>. 
	 * @return the user - the user, who has modified/saved/create this item
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValues.modificationType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modificationType - the type of the 'modification' action (save,create,remove). @since 2.10
	 */
	@Accessor(qualifier = "modificationType", type = Accessor.Type.SETTER)
	public void setModificationType(final SavedValueEntryType value)
	{
		getPersistenceContext().setPropertyValue(MODIFICATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SavedValues.modifiedItem</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the modifiedItem
	 */
	@Accessor(qualifier = "modifiedItem", type = Accessor.Type.SETTER)
	public void setModifiedItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValues.modifiedItemDisplayString</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiedItemDisplayString - Display String. @since 2.10
	 */
	@Accessor(qualifier = "modifiedItemDisplayString", type = Accessor.Type.SETTER)
	public void setModifiedItemDisplayString(final String value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDITEMDISPLAYSTRING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValues.modifiedItemType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiedItemType - Type
	 */
	@Accessor(qualifier = "modifiedItemType", type = Accessor.Type.SETTER)
	public void setModifiedItemType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDITEMTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValues.timestamp</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the timestamp - the timestamp of the last modification
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
	public void setTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedValues.user</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the user - the user, who has modified/saved/create this item
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
