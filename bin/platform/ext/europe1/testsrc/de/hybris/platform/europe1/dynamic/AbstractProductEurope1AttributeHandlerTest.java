package de.hybris.platform.europe1.dynamic;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.model.PDTRowModel;

import java.util.function.Consumer;
import java.util.function.Function;

import org.fest.assertions.IntAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.stubbing.Answer;


@Ignore
@RunWith(Parameterized.class)
public abstract class AbstractProductEurope1AttributeHandlerTest
{
	private static final String NOT_RELEVANT_STRING = "ignored";

	private final Function<Void, IntAssert> function;

	protected AbstractProductEurope1AttributeHandlerTest(final Function<Void, IntAssert> function)
	{
		this.function = function;
	}

	@Test
	public void shouldExecuteComparisionAssertions()
	{
		function.apply(null);
	}

	protected static class Builder<MODEL extends PDTRowModel>
	{
		MODEL model;

		public static <MODEL extends PDTRowModel> Builder<MODEL> of(final Class<MODEL> clazz)
		{
			final Builder<MODEL> builder = new Builder<>();
			builder.model = mock(clazz, (Answer) invocationOnMock -> null);
			return builder;
		}

		Builder<MODEL> with(final Consumer<MODEL> consumer)
		{
			consumer.accept(model);
			return this;
		}

		Builder<MODEL> withUserUid(final String userUid)
		{
			return with(model -> {
				final UserModel userMock = mock(UserModel.class);
				given(userMock.getUid()).willReturn(userUid);
				given(model.getUser()).willReturn(userMock);
			});
		}

		Builder<MODEL> withPdtRowCode(final String code)
		{
			return with(model -> {
				final HybrisEnumValue hybrisEnumValueMock = mock(HybrisEnumValue.class);
				given(hybrisEnumValueMock.getCode()).willReturn(code);
				given(model.getUg()).willReturn(hybrisEnumValueMock);
			});
		}

		Builder<MODEL> withPresentProduct()
		{
			return with(model -> {
				final ProductModel productMock = mock(ProductModel.class);
				given(model.getProduct()).willReturn(productMock);
			});
		}

		Builder<MODEL> withAbsentProduct()
		{
			return with(model -> given(model.getProduct()).willReturn(null));
		}

		Builder<MODEL> withPresentProductId()
		{
			return with(model -> given(model.getProductId()).willReturn(NOT_RELEVANT_STRING));
		}

		Builder<MODEL> withAbsentProductId()
		{
			return with(model -> given(model.getProductId()).willReturn(null));
		}

		Builder<MODEL> withPresentPg()
		{
			return with(model -> given(model.getPg()).willReturn(ProductPriceGroup.valueOf(NOT_RELEVANT_STRING)));
		}

		Builder<MODEL> withAbsentPg()
		{
			return with(model -> given(model.getPg()).willReturn(null));
		}

		Builder<MODEL> withPK(final PK pk)
		{
			return with(model -> given(model.getPk()).willReturn(pk));
		}

		Builder<MODEL> withUgCode(final String code)
		{
			return with(model -> {
				final HybrisEnumValue hybrisEnumValueMock = mock(HybrisEnumValue.class);
				given(hybrisEnumValueMock.getCode()).willReturn(code);
				given(model.getUg()).willReturn(hybrisEnumValueMock);
			});
		}

		MODEL build()
		{
			return model;
		}
	}
}
