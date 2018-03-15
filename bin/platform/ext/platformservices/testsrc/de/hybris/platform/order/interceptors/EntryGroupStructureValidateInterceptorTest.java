package de.hybris.platform.order.interceptors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link EntryGroupStructureValidateInterceptor}
 */
public class EntryGroupStructureValidateInterceptorTest
{
	private EntryGroupStructureValidateInterceptor validateInterceptor;

	@Mock
	private AbstractOrderModel abstractOrderModel;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		validateInterceptor = new EntryGroupStructureValidateInterceptor();
	}

	@Test
	public void shouldReturnWhenNoEntryGroups() throws InterceptorException
	{
		given(abstractOrderModel.getEntryGroups()).willReturn(Collections.emptyList());

		validateInterceptor.onValidate(abstractOrderModel, interceptorContext);
	}

	@Test
	public void shouldPassOnCorrectEntryGroupStructure() throws InterceptorException
	{
		final EntryGroup firstRootGroup = entryGroup(1,
				entryGroup(2,
						entryGroup(3),
						entryGroup(4)),
				entryGroup(5,
						entryGroup(6)));

		final EntryGroup secondRootGroup = entryGroup(7,
				entryGroup(8),
				entryGroup(9));

		given(abstractOrderModel.getEntryGroups()).willReturn(Arrays.asList(firstRootGroup, secondRootGroup));

		validateInterceptor.onValidate(abstractOrderModel, interceptorContext);
	}

	@Test
	public void shouldFailOnDuplicateEntryGroups() throws InterceptorException
	{
		final EntryGroup duplicateEntryGroup = entryGroup(5);
		final EntryGroup firstRootGroup = entryGroup(1,
				entryGroup(2,
						entryGroup(3),
						entryGroup(4)),
				duplicateEntryGroup);

		final EntryGroup secondRootGroup = entryGroup(7,
				entryGroup(8),
				duplicateEntryGroup);

		given(abstractOrderModel.getEntryGroups()).willReturn(Arrays.asList(firstRootGroup, secondRootGroup));

		assertThatThrownBy(() -> validateInterceptor.onValidate(abstractOrderModel, interceptorContext)).isInstanceOf(
				InterceptorException.class).hasCause(
				new IllegalArgumentException("Duplicate entry group(s): "
						+ Collections.singletonList(duplicateEntryGroup.getGroupNumber()) + " found in order entry group trees"));
	}

	@Test
	public void shouldFailOnCyclicEntryGroupDependencies() throws InterceptorException
	{
		final EntryGroup cyclicGroup = entryGroup(5);
		final EntryGroup referencedGroup = entryGroup(3,
				entryGroup(4,
						cyclicGroup));
		cyclicGroup.setChildren(Collections.singletonList(referencedGroup));
		final EntryGroup rootGroup = entryGroup(1,
				referencedGroup,
				entryGroup(2));

		given(abstractOrderModel.getEntryGroups()).willReturn(Collections.singletonList(rootGroup));

		assertThatThrownBy(() -> validateInterceptor.onValidate(abstractOrderModel, interceptorContext)).isInstanceOf(
				InterceptorException.class).hasCause(
				new IllegalArgumentException("Duplicate entry group(s): [3] found in order entry group trees"));

	}

	protected EntryGroup entryGroup(final Integer number, final EntryGroup... children)
	{
		final EntryGroup result = new EntryGroup();
		result.setGroupNumber(number);
		result.setChildren(Stream.of(children).collect(Collectors.toList()));
		return result;
	}
}
