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
package de.hybris.platform.validation.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.validation.extractor.ConstraintConverter;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToBeanTypeConverter;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToFieldTypeConverter;
import de.hybris.platform.validation.extractor.impl.AttributeConstraintToGetterTypeConverter;
import de.hybris.platform.validation.extractor.impl.ConstraintGroupsToGroupsTypeConverter;
import de.hybris.platform.validation.extractor.impl.ConstraintsToConstraintMappingsTypeConverter;
import de.hybris.platform.validation.extractor.impl.DefaultConstraintsExtractor;
import de.hybris.platform.validation.extractor.impl.SeverityToPayloadTypeConverter;
import de.hybris.platform.validation.extractor.impl.TypeConstraintToBeanTypeConverter;
import de.hybris.platform.validation.localized.ConstraintModelValidator;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import de.hybris.platform.validation.localized.TypeLocalizedConstraints;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import de.hybris.platform.validation.model.constraints.TypeConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertFalseConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertTrueConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.FutureConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NullConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.PastConstraintModel;
import de.hybris.platform.validation.services.impl.DefaultValidationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;

import org.hibernate.validator.internal.xml.BeanType;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ConcurentLoadValidationTest
{
	private static final int MAX_THREADS = 100;
	private List<AbstractConstraintModel> constraints;

	@Before
	public void prepare()
	{
		final NullConstraintModel ncm = new NullConstraintModel();
		ncm.setId("ncm1");
		ncm.setTarget(NullConstraintModel.class);
		ncm.setQualifier(AbstractConstraintModel.ACTIVE);
		ncm.setActive(true);
		ncm.setAnnotation(Null.class);

		final NotNullConstraintModel nncm = new NotNullConstraintModel();
		nncm.setId("nncm1");
		nncm.setTarget(NotNullConstraintModel.class);
		nncm.setQualifier(AbstractConstraintModel.ACTIVE);
		nncm.setActive(true);
		nncm.setAnnotation(NotNull.class);

		final AssertFalseConstraintModel asfcm = new AssertFalseConstraintModel();
		asfcm.setId("ascm");
		asfcm.setTarget(AssertFalseConstraintModel.class);
		asfcm.setQualifier(AbstractConstraintModel.ACTIVE);
		asfcm.setActive(true);
		asfcm.setAnnotation(AssertFalse.class);

		final AssertTrueConstraintModel astcm = new AssertTrueConstraintModel();
		astcm.setId("astcm");
		astcm.setTarget(AssertTrueConstraintModel.class);
		astcm.setQualifier(AbstractConstraintModel.ACTIVE);
		astcm.setActive(true);
		astcm.setAnnotation(AssertTrue.class);

		final FutureConstraintModel fcm = new FutureConstraintModel();
		fcm.setId("fcm");
		fcm.setTarget(FutureConstraintModel.class);
		fcm.setQualifier(AbstractConstraintModel.ACTIVE);
		fcm.setActive(true);
		fcm.setAnnotation(Future.class);

		final PastConstraintModel pcm = new PastConstraintModel();
		pcm.setId("pcm");
		pcm.setTarget(PastConstraintModel.class);
		pcm.setQualifier(AbstractConstraintModel.ACTIVE);
		pcm.setActive(true);
		pcm.setAnnotation(Past.class);

		constraints = Arrays.asList(new AbstractConstraintModel[]
		{ ncm, nncm, asfcm, astcm, fcm, pcm });
	}

	@Test
	public void concurrentLoadTest()
	{
		final ConstraintService dummyConstraintService = new ConstraintService()
		{
			@Override
			public boolean isConstraintDuplicated(final AbstractConstraintModel model)
			{
				return false;
			}

			@Override
			public List<AbstractConstraintModel> getPojoRelatedConstraints()
			{
				return constraints;
			}

			@Override
			public List<ComposedTypeModel> getConstraintedComposedTypes()
			{
				return Collections.EMPTY_LIST;
			}

			@Override
			public List<AbstractConstraintModel> getAllConstraints()
			{
				// YTODO Auto-generated method stub
				return constraints;
			}
		};
		final Set<String> ignored = new HashSet<String>(Arrays.asList(new String[]
		{ "message", "payload", "groups" }));

		final ConstraintGroupsToGroupsTypeConverter cgtctc = new ConstraintGroupsToGroupsTypeConverter();

		final ConstraintConverter<TypeConstraintModel, BeanType> tcbtc = new TypeConstraintToBeanTypeConverter();
		((TypeConstraintToBeanTypeConverter) tcbtc).setGroupsConverter(cgtctc);
		((TypeConstraintToBeanTypeConverter) tcbtc).setIgnoredAnnotationMethods(ignored);
		((TypeConstraintToBeanTypeConverter) tcbtc).setSeverityConverter(new SeverityToPayloadTypeConverter());


		final ConstraintConverter<AttributeConstraintModel, BeanType> actbtc = new AttributeConstraintToBeanTypeConverter();
		((AttributeConstraintToBeanTypeConverter) actbtc).setFieldConverter(new AttributeConstraintToFieldTypeConverter());
		((AttributeConstraintToBeanTypeConverter) actbtc).setGetterConverter(new AttributeConstraintToGetterTypeConverter());
		((AttributeConstraintToBeanTypeConverter) actbtc).setSeverityConverter(new SeverityToPayloadTypeConverter());
		((AttributeConstraintToBeanTypeConverter) actbtc).setIgnoredAnnotationMethods(ignored);
		((AttributeConstraintToBeanTypeConverter) actbtc).setGroupsConverter(cgtctc);

		final ConstraintsToConstraintMappingsTypeConverter allConverter = new de.hybris.platform.validation.extractor.impl.ConstraintsToConstraintMappingsTypeConverter();
		allConverter.setConverters(Arrays.asList(tcbtc, actbtc));
		final ConstraintsToConstraintMappingsTypeConverter allConverterSkippingErrors = new de.hybris.platform.validation.extractor.impl.ConstraintsToConstraintMappingsTypeConverter();
		allConverterSkippingErrors.setConverters(Arrays.asList(tcbtc, actbtc));
		allConverterSkippingErrors.setSkipOnError(true);


		final DefaultConstraintsExtractor extractor = new DefaultConstraintsExtractor();
		extractor.setConstraintService(dummyConstraintService);
		extractor.setConstraintMappingsTypeConverter(allConverter);
		extractor.setSkipOnErrorConstraintMappingsTypeConverter(allConverterSkippingErrors);
		extractor.setConstraintModelValidator(new ConstraintModelValidator());

		final DefaultValidationService service = new DefaultValidationService()
		{
			@Override
			protected boolean isMultiLanguageValidationDisabled()
			{
				return false;
			}
		};
		service.setConstraintsExtractor(extractor);
		service.setLocalizedConstraintsRegistry(new LocalizedConstraintsRegistry()
		{
			@Override
			protected Map<String, TypeLocalizedConstraints> loadLocalizedConstraintsGroupedByType()
			{
				return Collections.emptyMap();
			}
		});

		final Object pojo = new Object();

		final TestThreadsHolder threads = new TestThreadsHolder<>(MAX_THREADS, new Runnable()
		{
			@Override
			public void run()
			{
				service.reloadValidationEngine();
				assertEquals(Collections.emptySet(), service.validate(pojo));
			}
		});
		threads.startAll();
		assertTrue(threads.waitAndDestroy(30));
		assertEquals(Collections.emptyMap(), threads.getErrors());
	}

	// HORST-2057
	@Test
	public void testIgnoreErrorInSingleConstraint()
	{
		final ConstraintsToConstraintMappingsTypeConverter allConverterDummy = new de.hybris.platform.validation.extractor.impl.ConstraintsToConstraintMappingsTypeConverter()
		{
			@Override
			protected ConstraintConverter<AbstractConstraintModel, BeanType> getConverter(final AbstractConstraintModel source)
			{
				return new ConstraintConverter()
				{
					@Override
					public Object convert(final Object source)
					{
						throw new RuntimeException("Booom - HORST-2057!!!");
					}
				};
			}
		};
		allConverterDummy.setSkipOnError(true);
		try
		{
			TestUtils.disableFileAnalyzer("converter exception expected to be logged");
			// don't need a real model here since we 'hacked' getConverter() above
			allConverterDummy.convert(Arrays.asList(new AbstractConstraintModel[]
			{ null }));
		}
		catch (final Exception e)
		{
			fail("single converter exceptions should be ignored but got " + e);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}


	}

}
