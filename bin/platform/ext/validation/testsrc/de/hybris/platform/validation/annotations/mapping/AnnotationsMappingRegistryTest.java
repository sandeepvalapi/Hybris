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
package de.hybris.platform.validation.annotations.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.validation.annotations.NotEmpty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@UnitTest
public class AnnotationsMappingRegistryTest
{
	private AnnotationsMappingRegistry registry;

	@Before
	public void setUp() throws Exception
	{
		registry = new AnnotationsMappingRegistry()
		{

			@Override
			Map<String, String> getPropertiesWithPrefix(final String prefix)
			{
				if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
				{
					return ImmutableMap.of("validation.constraints.attribute.group.booleans", "java.lang.Boolean,boolean",
							"validation.constraints.attribute.group.dates", "java.util.Date,java.util.Calendar",
							"validation.constraints.attribute.group.size",
							"java.lang.String,localized:java.lang.String,java.util.Collection,java.util.Map,collectionType,mapType",
							"validation.constraints.attribute.group.strings",
							"java.lang.String,localized:java.lang.String,mapType,collectionType",
							"validation.constraints.attribute.group.numbers",
							"java.math.BigDecimal,java.math.BigInteger,java.lang.String,java.lang.Byte,java.lang.Short,java.lang.Integer,java.lang.Long,java.lang.Double,byte,short,int,long,double");
				}
				else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
				{
					return ImmutableMap.of("validation.constraints.attribute.mapping.javax.validation.constraints.AssertFalse",
							"booleans", "validation.constraints.attribute.mapping.javax.validation.constraints.DecimalMax", "numbers",
							"validation.constraints.attribute.mapping.javax.validation.constraints.Future", "dates",
							"validation.constraints.attribute.mapping.javax.validation.constraints.Size", "size",
							"validation.constraints.attribute.mapping.de.hybris.platform.validation.annotations.NotEmpty", "strings");
				}

				return Collections.emptyMap();
			}
		};

		registry.init();
	}

	@Test
	public void shouldInitializeWithEmptyGroupsAndMappingsIfThereIsNoPropertyConfigurationSet() throws Exception
	{
		// given
		final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
		{
			@Override
			Map<String, String> getPropertiesWithPrefix(final String prefix)
			{
				return Collections.emptyMap();
			}
		};

		// when
		registry.init();

		// then
		assertThat(registry.getAnnotationToGroupsMapping()).isEmpty();
		assertThat(registry.getGroupsOfAttributeTypes()).isEmpty();
	}

	@Test
	public void shouldInitlaizeWithJustGroupsIfThereIsNoPropertyConfigurationForConstraints() throws Exception
	{
		// given
        final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
        {

            @Override
            Map<String, String> getPropertiesWithPrefix(final String prefix)
            {
                if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.group.booleans", "java.lang.Boolean,boolean",
                            "validation.constraints.attribute.group.dates", "java.util.Date,java.util.Calendar",
                            "validation.constraints.attribute.group.size",
                            "java.lang.String,localized:java.lang.String,java.util.Collection,java.util.Map,collectionType,mapType",
                            "validation.constraints.attribute.group.strings",
                            "java.lang.String,localized:java.lang.String,mapType,collectionType",
                            "validation.constraints.attribute.group.numbers",
                            "java.math.BigDecimal,java.math.BigInteger,java.lang.String,java.lang.Byte,java.lang.Short,java.lang.Integer,java.lang.Long,java.lang.Double,byte,short,int,long,double");
                }
                else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
                {
                    return Collections.emptyMap();
                }

                return Collections.emptyMap();
            }
        };

		// when
        registry.init();

		// then
        assertThat(registry.getGroupsOfAttributeTypes()).hasSize(5);
        assertThat(registry.getAnnotationToGroupsMapping()).isEmpty();
	}

	@Test
	public void shouldInitializeWithGroupNamesContainingDotInTheName() throws Exception
	{
        // given
        final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
        {

            @Override
            Map<String, String> getPropertiesWithPrefix(final String prefix)
            {
                if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.group.bool.eans", "java.lang.Boolean,boolean");
                }
                else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.mapping.javax.validation.constraints.AssertFalse",
                            "bool.eans");
                }

                return Collections.emptyMap();
            }
        };

        // when
        registry.init();

        // then
        assertThat(registry.getAnnotationToGroupsMapping()).hasSize(1);
        assertThat(registry.getGroupsOfAttributeTypes()).hasSize(1);
        assertThat(registry.getAnnotationToGroupsMapping().get(AssertFalse.class)).isEqualTo("bool.eans");
        assertThat(registry.getGroupsOfAttributeTypes().get("bool.eans")).containsOnly("java.lang.Boolean", "boolean");
	}

	@Test
	public void shouldInitializeWithGroupContainingEmptyTypesWhenNoValueInPropertyIsSet() throws Exception
	{
        // given
        final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
        {

            @Override
            Map<String, String> getPropertiesWithPrefix(final String prefix)
            {
                if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.group.booleans", "");
                }
                else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.mapping.javax.validation.constraints.AssertFalse",
                            "booleans");
                }

                return Collections.emptyMap();
            }
        };

        // when
        registry.init();

        // then
        assertThat(registry.getGroupsOfAttributeTypes()).hasSize(1);
        assertThat(registry.getGroupsOfAttributeTypes().get("booleans")).isEmpty();
	}

	@Test
	public void shouldInitializeWithGroupContainingEmptyTypesWhenValueContainsJustCommasAnEmptySpaces() throws Exception
	{
        // given
        final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
        {

            @Override
            Map<String, String> getPropertiesWithPrefix(final String prefix)
            {
                if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.group.booleans", ", ,,");
                }
                else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.mapping.javax.validation.constraints.AssertFalse",
                            "booleans");
                }

                return Collections.emptyMap();
            }
        };

        // when
        registry.init();

        // then
        assertThat(registry.getGroupsOfAttributeTypes()).hasSize(1);
        assertThat(registry.getGroupsOfAttributeTypes().get("booleans")).isEmpty();
	}

	@Test
	public void shouldInitializeWithGroupContainingEmptyTypesWhenValueContainsOneValidEntryAndMoreSpacesAndUselessCommas() throws Exception
	{
        // given
        final AnnotationsMappingRegistry registry = new AnnotationsMappingRegistry()
        {

            @Override
            Map<String, String> getPropertiesWithPrefix(final String prefix)
            {
                if (AnnotationsMappingRegistry.GROUP_PROPERTY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.group.booleans", ", ,,java.lang.Boolean ,");
                }
                else if (AnnotationsMappingRegistry.MAPPING_PROPERY_PREFIX.equals(prefix))
                {
                    return ImmutableMap.of("validation.constraints.attribute.mapping.javax.validation.constraints.AssertFalse",
                            "booleans");
                }

                return Collections.emptyMap();
            }
        };

        // when
        registry.init();

        // then
        assertThat(registry.getGroupsOfAttributeTypes()).hasSize(1);
        assertThat(registry.getGroupsOfAttributeTypes().get("booleans")).containsOnly("java.lang.Boolean");
	}

	@Test
	public void shouldHaveFiveConstraintAnnotationsRegistered() throws Exception
	{
		// when
		final Map<Class<?>, String> mapping = registry.getAnnotationToGroupsMapping();

		// then
		assertThat(mapping).hasSize(5);
		assertThat(mapping.get(AssertFalse.class)).isEqualTo("booleans");
		assertThat(mapping.get(DecimalMax.class)).isEqualTo("numbers");
		assertThat(mapping.get(Future.class)).isEqualTo("dates");
		assertThat(mapping.get(Size.class)).isEqualTo("size");
		assertThat(mapping.get(NotEmpty.class)).isEqualTo("strings");
	}

	@Test
	public void shouldHaveFiveGroupsOfAttributeTypesRegistered() throws Exception
	{
		// when
		final Map<String, List<String>> groups = registry.getGroupsOfAttributeTypes();

		// then
		assertThat(groups).hasSize(5);
		assertThat(groups.get("booleans")).hasSize(2).containsOnly("java.lang.Boolean", "boolean");
		assertThat(groups.get("dates")).hasSize(2).containsOnly("java.util.Date", "java.util.Calendar");
		assertThat(groups.get("size")).hasSize(6).containsOnly("java.lang.String", "localized:java.lang.String",
				"java.util.Collection", "java.util.Map", "collectionType", "mapType");
		assertThat(groups.get("strings")).hasSize(4).containsOnly("java.lang.String", "localized:java.lang.String", "mapType",
				"collectionType");
		assertThat(groups.get("numbers")).hasSize(13).containsOnly("java.math.BigDecimal", "java.math.BigInteger",
				"java.lang.String", "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Double",
				"byte", "short", "int", "long", "double");

	}

	@Test
	public void shouldValidateAssertFalseAsValidForSmallBoolean() throws Exception
	{
		// given
		final Class<AssertFalse> annotation = AssertFalse.class;
		final String propertyType = "boolean";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isTrue();
	}

	@Test
	public void shouldValidateDecimalMaxAsValidForJavaLangLong() throws Exception
	{
		// given
		final Class<DecimalMax> annotation = DecimalMax.class;
		final String propertyType = "java.lang.Long";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isTrue();
	}

	@Test
	public void shouldValidateFutureAsValidForJavaUtilDate() throws Exception
	{
		// given
		final Class<Future> annotation = Future.class;
		final String propertyType = "java.util.Date";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isTrue();
	}

	@Test
	public void shouldValidateSizeAsValidForJavaUtilCollection() throws Exception
	{
		// given
		final Class<Size> annotation = Size.class;
		final String propertyType = "java.util.Collection";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isTrue();
	}

	@Test
	public void shouldValidateHybrisNotEmptyAsValidForJavaLangString() throws Exception
	{
		// given
		final Class<NotEmpty> annotation = NotEmpty.class;
		final String propertyType = "java.lang.String";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isTrue();
	}

	@Test
	public void shouldValidateNotRegisteredAnnotationClassAsInvalid() throws Exception
	{
		// given
		final Class<String> notValidClass = String.class;
		final String propertyType = "java.lang.String";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(notValidClass, propertyType);

		// then
		assertThat(validForType).isFalse();
	}

	@Test
	public void shouldValidateNotExistentPropertyTypeAsInvalid() throws Exception
	{
		// given
		final Class<NotEmpty> annotation = NotEmpty.class;
		final String propertyType = "not.existent";

		// when
		final boolean validForType = registry.isAnnotationRegisteredForType(annotation, propertyType);

		// then
		assertThat(validForType).isFalse();
	}


}
