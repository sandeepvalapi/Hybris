/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.breadcrumb;

import java.util.List;


/**
 * ResourceBreadcrumbBuilder builds a list of breadcrumbs based on a resource key
 */
public interface ResourceBreadcrumbBuilder
{
	List<Breadcrumb> getBreadcrumbs(String resourceKey);
}
