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
package de.hybris.platform.core.model.initialization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SystemSetupAudit first defined at extension core.
 */
@SuppressWarnings("all")
public class SystemSetupAuditModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SystemSetupAudit";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.hash</code> attribute defined at extension <code>core</code>. */
	public static final String HASH = "hash";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.extensionName</code> attribute defined at extension <code>core</code>. */
	public static final String EXTENSIONNAME = "extensionName";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.required</code> attribute defined at extension <code>core</code>. */
	public static final String REQUIRED = "required";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.patch</code> attribute defined at extension <code>core</code>. */
	public static final String PATCH = "patch";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.user</code> attribute defined at extension <code>core</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.className</code> attribute defined at extension <code>core</code>. */
	public static final String CLASSNAME = "className";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.methodName</code> attribute defined at extension <code>core</code>. */
	public static final String METHODNAME = "methodName";
	
	/** <i>Generated constant</i> - Attribute key of <code>SystemSetupAudit.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SystemSetupAuditModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SystemSetupAuditModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _className initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _extensionName initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _hash initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _methodName initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _name initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _patch initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _required initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public SystemSetupAuditModel(final String _className, final String _extensionName, final String _hash, final String _methodName, final String _name, final boolean _patch, final boolean _required, final UserModel _user)
	{
		super();
		setClassName(_className);
		setExtensionName(_extensionName);
		setHash(_hash);
		setMethodName(_methodName);
		setName(_name);
		setPatch(_patch);
		setRequired(_required);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _className initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _extensionName initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _hash initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _methodName initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _name initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _patch initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _required initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>SystemSetupAudit</code> at extension <code>core</code>
	 */
	@Deprecated
	public SystemSetupAuditModel(final String _className, final String _extensionName, final String _hash, final String _methodName, final String _name, final ItemModel _owner, final boolean _patch, final boolean _required, final UserModel _user)
	{
		super();
		setClassName(_className);
		setExtensionName(_extensionName);
		setHash(_hash);
		setMethodName(_methodName);
		setName(_name);
		setOwner(_owner);
		setPatch(_patch);
		setRequired(_required);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.className</code> attribute defined at extension <code>core</code>. 
	 * @return the className
	 */
	@Accessor(qualifier = "className", type = Accessor.Type.GETTER)
	public String getClassName()
	{
		return getPersistenceContext().getPropertyValue(CLASSNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.extensionName</code> attribute defined at extension <code>core</code>. 
	 * @return the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
	public String getExtensionName()
	{
		return getPersistenceContext().getPropertyValue(EXTENSIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.hash</code> attribute defined at extension <code>core</code>. 
	 * @return the hash
	 */
	@Accessor(qualifier = "hash", type = Accessor.Type.GETTER)
	public String getHash()
	{
		return getPersistenceContext().getPropertyValue(HASH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.methodName</code> attribute defined at extension <code>core</code>. 
	 * @return the methodName
	 */
	@Accessor(qualifier = "methodName", type = Accessor.Type.GETTER)
	public String getMethodName()
	{
		return getPersistenceContext().getPropertyValue(METHODNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.user</code> attribute defined at extension <code>core</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.patch</code> attribute defined at extension <code>core</code>. 
	 * @return the patch
	 */
	@Accessor(qualifier = "patch", type = Accessor.Type.GETTER)
	public boolean isPatch()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(PATCH));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SystemSetupAudit.required</code> attribute defined at extension <code>core</code>. 
	 * @return the required
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.GETTER)
	public boolean isRequired()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(REQUIRED));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.className</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the className
	 */
	@Accessor(qualifier = "className", type = Accessor.Type.SETTER)
	public void setClassName(final String value)
	{
		getPersistenceContext().setPropertyValue(CLASSNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SystemSetupAudit.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.extensionName</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
	public void setExtensionName(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTENSIONNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.hash</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the hash
	 */
	@Accessor(qualifier = "hash", type = Accessor.Type.SETTER)
	public void setHash(final String value)
	{
		getPersistenceContext().setPropertyValue(HASH, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.methodName</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the methodName
	 */
	@Accessor(qualifier = "methodName", type = Accessor.Type.SETTER)
	public void setMethodName(final String value)
	{
		getPersistenceContext().setPropertyValue(METHODNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.name</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.patch</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the patch
	 */
	@Accessor(qualifier = "patch", type = Accessor.Type.SETTER)
	public void setPatch(final boolean value)
	{
		getPersistenceContext().setPropertyValue(PATCH, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.required</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the required
	 */
	@Accessor(qualifier = "required", type = Accessor.Type.SETTER)
	public void setRequired(final boolean value)
	{
		getPersistenceContext().setPropertyValue(REQUIRED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SystemSetupAudit.user</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
