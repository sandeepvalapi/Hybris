package de.hybris.platform.order.interceptors;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Validates the entryGroup structure of the order to avoid cyclic dependencies between groups.
 */
public class EntryGroupStructureValidateInterceptor implements ValidateInterceptor<AbstractOrderModel>
{
	@Override
	public void onValidate(final AbstractOrderModel model, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (CollectionUtils.isEmpty(model.getEntryGroups()))
		{
			return;
		}

		try
		{
			validateRootGroupTrees(model.getEntryGroups());
		}
		catch (final IllegalArgumentException e)
		{
			throw new InterceptorException("Exception when validating entryGroup structure of the order", e, this);
		}
	}

	protected void validateRootGroupTrees(final List<EntryGroup> rootGroups)
	{
		final List<EntryGroup> existingEntryGroups = new ArrayList<>();
		rootGroups.forEach(rootGroup -> validateRootGroupTree(rootGroup, existingEntryGroups));
	}

	protected void validateRootGroupTree(final EntryGroup rootGroup, final List<EntryGroup> existingEntryGroups)
	{
		final int nextRootGroupIndex = existingEntryGroups.size();
		existingEntryGroups.add(rootGroup);

		for (int i = nextRootGroupIndex; i < existingEntryGroups.size(); i++)
		{
			final List<EntryGroup> children = existingEntryGroups.get(i).getChildren();
			if (CollectionUtils.isNotEmpty(children))
			{
				final List<Integer> childrenGroupNumbers = children.stream().map(c -> c.getGroupNumber())
						.collect(Collectors.toList());
				final List<Integer> existingEntryGroupsGroupNumbers = existingEntryGroups.stream().map(eg -> eg.getGroupNumber())
						.collect(Collectors.toList());

				final Collection<Integer> duplicateEntryGroupNumbers = CollectionUtils.intersection(childrenGroupNumbers,
						existingEntryGroupsGroupNumbers);

				if (CollectionUtils.isNotEmpty(duplicateEntryGroupNumbers))
				{
					throw new IllegalArgumentException("Duplicate entry group(s): " + duplicateEntryGroupNumbers.toString()
							+ " found in order entry group trees");
				}
				existingEntryGroups.addAll(children);
			}
		}
	}

	protected Collection<Integer> getEntryGroupNumbers(final Collection<EntryGroup> entryGroups)
	{
		return entryGroups.stream()
				.map(EntryGroup::getGroupNumber)
				.collect(Collectors.toList());
	}
}
