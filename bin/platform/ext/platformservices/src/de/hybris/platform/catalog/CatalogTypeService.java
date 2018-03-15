/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.catalog;

import de.hybris.platform.catalog.data.CatalogVersionOverview;
import de.hybris.platform.catalog.exceptions.CatalogAwareObjectResolvingException;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * Provides methods for using the catalog type system.
 */
public interface CatalogTypeService
{
	/**
	 * Returns <code>true</code> if the given <code>typeCode</code> string represents a catalog version capable type.
	 * 
	 * @param typeCode
	 *           the typeCode of ComposedType
	 * @return <code>false</code> if the <code>typeCode</code> is not a catalog item type.
	 * @throws IllegalArgumentException
	 *            if the <code>typeCode</code> is null.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if the <code>typeCode</code> does not correspond to a valid type.
	 * @deprecated since ages - Use{@link #isCatalogVersionAwareType(String)} instead.
	 */
	@Deprecated
	boolean isCatalogItemType(String typeCode);


	/**
	 * Returns <code>true</code> if the given <code>typeCode</code> string represents a type that is catalog version
	 * aware.
	 * 
	 * @param typeCode
	 *           the typeCode of ComposedType
	 * @return <code>false</code> if the <code>typeCode</code> is not a catalog item type.
	 * @throws IllegalArgumentException
	 *            if the <code>typeCode</code> is null.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if the <code>typeCode</code> does not correspond to a valid type.
	 */
	boolean isCatalogVersionAwareType(String typeCode);

	/**
	 * Returns for the given <code>typeCode</code> the qualifier of the CatalogVersion attribute. This method will try to
	 * examine the given <code>type<code> for attribute where the
	 * CatalogVersion is stored and return it's qualifier.
	 * 
	 * @param typeCode
	 *           the typeCode of the target {@link ComposedTypeModel}
	 * @return the catalog version attribute qualifier
	 * @throws IllegalArgumentException
	 *            if <ul> <li>the <code>typeCode</code> is null <b>OR</b></li> <li>corresponding type has no catalog
	 *            version container attribute</li> </ul>
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            - when more than one composed type for given <code>typeCode</code> is found.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            - when no composed type for given <code>typeCode</code> is found.
	 */
	String getCatalogVersionContainerAttribute(String typeCode);


	/**
	 * Returns a set of attribute qualifiers by which an instance with the given <code>typeCode</code> can be identified
	 * as <b>unique<b> within any catalog version.
	 * 
	 * @param typeCode
	 *           the ComposedType's code
	 * @return an empty set if the given <code>typeCode</code> has no such attributes
	 * @throws IllegalArgumentException
	 *            if the <code>typeCode</code> is null.
	 */
	Set<String> getCatalogVersionUniqueKeyAttribute(String typeCode);

	/**
	 * Returns for the given {@link CatalogVersionModel}, the <code>typeCode</code> and the map with the unique catalog
	 * key attributes and their values an {@link ItemModel} (or a given subtype).
	 * <p>
	 * Please refer to {@link #getCatalogVersionUniqueKeyAttribute(String)} to see how a set of the unique catalog key
	 * attributes for a given type can be recognized.
	 * <p>
	 * If the
	 * <code>version<code> is not persisted or one of the <code>uniqueKeyValues<code> members is a non-persisted model, the method will return null;
	 * 
	 * @param version
	 *           the {@link CatalogVersionModel}
	 * @param typeCode
	 *           String typeCode of the {@link ComposedTypeModel}
	 * @param uniqueKeyValues
	 *           a map with <b>ALL</b> unique catalog key attribute and their values to be searched for. Any non-unique
	 *           catalog key attribute in the given map will be ignored.
	 * @return an ItemModel or a subtype of ItemModel which matches the given parameters <b>OR <code>null</code></b> if
	 *         any item model in the <code>uniqueKeyValues</code> map was not persisted yet.
	 * @throws IllegalArgumentException
	 *            if:
	 *            <ul>
	 *            <li>the <code>version</code> is null</li> <li>the <code>typeCode</code> is null</li> <li>the <code>
	 *            uniqueKeyValues</code> is null</li> <li>the <code>typeCode</code> is not a catalog item type</li> <li>
	 *            if <code>uniqueKeyValues</code> map does not contain all unique attributes for the given catalog item
	 *            type</li>
	 *            </ul>
	 * @throws IllegalStateException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> has no unique catalog attributes defined</li> <li>if a unique
	 *            catalog key attribute is not searchable</li>
	 *            </ul>
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> does not correspond to a system type</li>
	 *            </ul>
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> corresponds to more than one system type</li>
	 *            </ul>
	 * @deprecated since ages - Use{@link #getCatalogVersionAwareModel(CatalogVersionModel, String, Map)} instead. It allows you to
	 *             handle cases when non persisted models disallow fetching of the catalog items.
	 */
	@Deprecated
	<T extends ItemModel> T getCatalogVersionItem(CatalogVersionModel version, String typeCode, Map<String, Object> uniqueKeyValues);

	/**
	 * Returns for the given {@link CatalogVersionModel}, the <code>typeCode</code> and the map with the unique catalog
	 * key attributes and their values an {@link ItemModel} (or a given subtype).
	 * <p>
	 * Please refer to {@link #getCatalogVersionUniqueKeyAttribute(String)} to see how a set of the unique catalog key
	 * attributes for a given type can be recognized.
	 * <p>
	 * If the
	 * <code>version<code> is not persisted or one of the <code>uniqueKeyValues<code> members is a non-persisted model, the method will throw {@link CatalogAwareObjectResolvingException};
	 * 
	 * @param version
	 *           the {@link CatalogVersionModel}
	 * @param typeCode
	 *           String typeCode of the {@link ComposedTypeModel}
	 * @param uniqueKeyValues
	 *           a map with <b>ALL</b> unique catalog key attribute and their values to be searched for. Any non-unique
	 *           catalog key attribute in the given map will be ignored.
	 * @return an ItemModel or a subtype of ItemModel which matches the given parameters <b>OR <code>null</code></b> if
	 *         any item model in the <code>uniqueKeyValues</code> map was not persisted yet.
	 * @throws IllegalArgumentException
	 *            if:
	 *            <ul>
	 *            <li>the <code>version</code> is null</li> <li>the <code>typeCode</code> is null</li> <li>the <code>
	 *            uniqueKeyValues</code> is null</li> <li>the <code>typeCode</code> is not a catalog item type</li> <li>
	 *            if <code>uniqueKeyValues</code> map does not contain all unique attributes for the given catalog item
	 *            type</li>
	 *            </ul>
	 * @throws IllegalStateException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> has no unique catalog attributes defined</li> <li>if a unique
	 *            catalog key attribute is not searchable</li>
	 *            </ul>
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> does not correspond to a system type</li>
	 *            </ul>
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if:
	 *            <ul>
	 *            <li>the given <code>typeCode</code> corresponds to more than one system type</li>
	 *            </ul>
	 * @throws CatalogAwareObjectResolvingException
	 *            if
	 *            <code>version<code> in not persisted <b>OR<b> one of the unique parameters is a model and is not persisted.
	 */
	ItemModel getCatalogVersionAwareModel(CatalogVersionModel version, String typeCode, Map<String, Object> uniqueKeyValues)
			throws CatalogAwareObjectResolvingException;

	/**
	 * Returns all composed types which are catalog version aware (or catalog contained).</br> The method has two modes
	 * of operation:
	 * <ul>
	 * <li>If the <b>supertypesOnly</b> argument is <b>false</b> : all the types with
	 * {@link ComposedTypeModel#CATALOGITEMTYPE} equal TRUE will be returned.</li>
	 * <li>If the <b>supertypesOnly</b> argument is <b>true</b> : the method will return all {@link ComposedTypeModel}s
	 * which fulfill the following:
	 * <ul>
	 * <li>{@link ComposedTypeModel#CATALOGITEMTYPE} flag equal TRUE</li>
	 * <li>super type <b>is not</b> catalog version aware <b>OR</b> super type <b>is</b> catalog version aware but it's
	 * unique attributes are different than unique attributes of the given type</li>
	 * </ul>
	 * </li>
	 * 
	 * @param supertypesOnly
	 *           if set to true only root types will be returned.
	 * @return collection of {@link ComposedTypeModel}s which have {@link ComposedTypeModel#CATALOGITEMTYPE} equal TRUE.
	 */
	Collection<ComposedTypeModel> getAllCatalogVersionAwareTypes(boolean supertypesOnly);

	/**
	 * Checks if the given <code>type<code> is catalog version aware (catalog contained type).
	 * 
	 * @param type
	 *           target {@link ComposedTypeModel}
	 * @return boolean value
	 * @throws IllegalArgumentException
	 *            if <code>type<code> is null.
	 */
	boolean isCatalogVersionAwareType(final ComposedTypeModel type);

	/**
	 * Checks if the given <code>model<code> is catalogVersion aware (catalog contained type).
	 * 
	 * @param model
	 *           the target item model
	 * @return boolean value
	 * @throws IllegalArgumentException
	 *            if <code>model<code> is null.
	 */
	boolean isCatalogVersionAwareModel(final ItemModel model);

	/**
	 * Retrieves the owning catalog version for a catalog version contained item <code>model<code>.
	 * 
	 * @param model
	 *           the target item model
	 * @see #isCatalogVersionAwareModel(ItemModel)
	 * @throws IllegalArgumentException
	 *            if <code>model<code> is null
	 * @throws IllegalStateException
	 *            if <ul> <li>catalog version attribute was of unexpected type, or</li> <li>the model given is not
	 *            catalog version aware</li> </ul>
	 */
	CatalogVersionModel getCatalogVersionForCatalogVersionAwareModel(ItemModel model);

	/**
	 * Collects all catalog version aware types, where at least one item exists, which belongs to given catalog version.
	 * Some statistics, like the amount of items per type are collected as well.
	 * 
	 * @param catalogVersion
	 *           the catalog version
	 * @return <code>CatalogVersionOverview</code> object
	 */
	CatalogVersionOverview getCatalogVersionOverview(CatalogVersionModel catalogVersion);

}
