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
package de.hybris.platform.directpersistence;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.TypeManagerManaged;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@IntegrationTest
@Ignore
public abstract class AbstractSLDUnsafeJALOAttributesTest extends ServicelayerBaseTest
{
	@Resource
	private SLDUnsafeTypesProviderBuilder SLDUnsafeTypesProviderBuilder;

	protected abstract List<String> getExtensions();

	protected SLDUnsafeTypesProvider unsafeTypesProvider = null;

	@Before
	public void setUp()
	{
		this.unsafeTypesProvider = this.SLDUnsafeTypesProviderBuilder.build(getExtensions());
	}

	@Test
	public void shouldFindNoUnknownUnsafeForSLDAttributes() throws IOException
	{
		final Collection<UnsafeTypeInfo> allTypes = unsafeTypesProvider.getAllUnsafeTypes();

		final Set<String> unsafeTypes = new LinkedHashSet<>();
		final Set<String> unsafeUnknownAttributes = new LinkedHashSet<>();
		final Set<String> unsafeMarkedAttributes = new LinkedHashSet<>();
		
		StringWriter sw = new StringWriter();
		CSVWriter csv = new CSVWriter(sw);

		System.err.println("###########################################################################");
		System.err.println("### SLD Unsafe Types Report                                             ###");
		System.err.println("###########################################################################");
		for (final UnsafeTypeInfo ut : allTypes)
		{
			unsafeTypes.add(ut.getTypeCode());
			
			System.err.println("Type " + ut.getTypeCode()+" extension:"+ut.getExtensionName());
			if (ut.isUnsafeToRead())
			{
				final Collection<String> read = ut.getUnsafeAttributesToRead();
				System.err.println("\tREAD: " + read.size());
				read.stream().sorted().map(attr -> ut.getUnsafeToRead(attr)).flatMap(methods -> methods.stream()).filter(m -> ut.isNotCoveredByParent(m)).sorted()
						.forEachOrdered(method -> {
							registerAttribute(unsafeUnknownAttributes, unsafeMarkedAttributes, ut, method);
							writeToCSV(ut, method, true, csv);
							writeToConsole(method);
						});
			}
			if (ut.isUnsafeToWrite())
			{
				final Collection<String> write = ut.getUnsafeAttributesToWrite();
				System.err.println("\tWRITE: " + write.size());
				write.stream().sorted().map(attr -> ut.getUnsafeToWrite(attr)).flatMap(methods -> methods.stream()).filter(m -> ut.isNotCoveredByParent(m)).sorted()
						.forEachOrdered(method -> {
							registerAttribute(unsafeUnknownAttributes, unsafeMarkedAttributes, ut, method);
							writeToCSV(ut, method, false, csv);
							writeToConsole(method);
						});
			}
			System.err.println();
		}
		Set<String> extensiions = new HashSet<>(getExtensions());
		List<String> safeTypes = TypeManager.getInstance().getAllComposedTypes().stream()
				.filter(type -> !TypeManagerManaged.class.isAssignableFrom(type.getJaloClass()) && !(type instanceof RelationType || type instanceof ViewType || type instanceof EnumerationType)
						&& extensiions.contains(type.getExtensionName()) && !unsafeTypes.contains(type.getCode()))
				.map(type -> type.getCode()).sorted().collect(Collectors.toList());
		
		System.err.println("###########################################################################");
		System.err.println("Total unknown attributes: " + unsafeUnknownAttributes.size());
		System.err.println("Total marked attributes : " + unsafeMarkedAttributes.size());
		System.err.println("Unsafe types : " + unsafeTypes);
		System.err.println("Safe types : " + safeTypes);
		System.err.println("###########################################################################");

		System.err.println("###########################################################################");
		System.err.println("### SLD Unsafe Types Report as CSV                                      ###");
		System.err.println("###########################################################################");

		csv.closeQuietly();
		
		IOUtils.copy(new StringReader(sw.getBuffer().toString()), System.err);
		
		System.err.println("###########################################################################");

		
		//disabling assert but leaving the test to collect commerce-suite wide feedback
		assertEquals("Unsafe attributes/methods found which weren't marked", Collections.emptySet(), unsafeUnknownAttributes);
	}

	protected void writeToConsole(UnsafeMethodInfo method)
	{
		System.err.println("\t\t: " + (method.isMarkedAsKnowProblem() ? "(ok)" : "(!!)") + " attribute:"
				+ method.getAttribute() + " method:" + method.getMethod());
	}
	
	protected void writeToCSV( UnsafeTypeInfo ut, UnsafeMethodInfo method, boolean read, CSVWriter csv)
	{
		// 0: code
		// 1: extension
		// 2: attribute
		// 3: read|write
		// 4: ok|unknown
		// 5: method name
		try
		{
			int i = 0;
			Map<Integer,String> line = new HashMap<>();
			line.put(Integer.valueOf(i++),ut.getTypeCode());
			line.put(Integer.valueOf(i++),ut.getExtensionName());
			line.put(Integer.valueOf(i++),method.getAttribute());
			line.put(Integer.valueOf(i++),read?"read":"write");
			line.put(Integer.valueOf(i++),method.isMarkedAsKnowProblem() ? "ok":"unknown");
			line.put(Integer.valueOf(i++),method.getMethod().toString());
			csv.write(line);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected void registerAttribute(final Set<String> unsafeUnknownAttributes, final Set<String> unsafeMarkedAttributes,
			final UnsafeTypeInfo ut, final UnsafeMethodInfo method)
	{
		final String attribute = ut.getTypeCode() + "." + method.getAttribute();
		// unknown --> register as unknown
		if (!method.isMarkedAsKnowProblem())
		{
			unsafeUnknownAttributes.add(attribute);
			unsafeMarkedAttributes.remove(attribute);
		}
		// marked --> register as marked, unless already registered as unknown
		else if (!unsafeUnknownAttributes.contains(attribute))
		{
			unsafeMarkedAttributes.add(attribute);
		}
	}
}
