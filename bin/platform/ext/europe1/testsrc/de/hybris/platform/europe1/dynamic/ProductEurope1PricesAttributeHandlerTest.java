package de.hybris.platform.europe1.dynamic;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import org.fest.assertions.IntAssert;
import org.junit.runners.Parameterized;


public class ProductEurope1PricesAttributeHandlerTest extends AbstractProductEurope1AttributeHandlerTest
{

	public ProductEurope1PricesAttributeHandlerTest(final Function<Void, IntAssert> function)
	{
		super(function);
	}

	@Parameterized.Parameters
	public static Iterable<Object> data()
	{
		return Arrays.asList(//
				compare(null, null).andThen(IntAssert::isZero),

				compareBuilding(left -> left.withUserUid("ab123"), right -> right.withUserUid("AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withUserUid("ab234"), right -> right.withUserUid("AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withUserUid("theSame"), right -> right.withUserUid("theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(left -> left.withPdtRowCode("ab123"), right -> right.withPdtRowCode("AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withPdtRowCode("ab234"), right -> right.withPdtRowCode("AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withPdtRowCode("theSame"), right -> right.withPdtRowCode("theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentProduct, Builder::withPresentProduct).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentProduct, Builder::withAbsentProduct).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withPresentProduct, Builder::withPresentProduct).andThen(IntAssert::isZero),
				compareBuilding(Builder::withAbsentProduct, Builder::withAbsentProduct).andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentProductId, Builder::withPresentProductId).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentProductId, Builder::withAbsentProductId).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withAbsentProductId, Builder::withAbsentProduct).andThen(IntAssert::isZero),
				compareBuilding(Builder::withPresentProductId, Builder::withPresentProductId).andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentPg, Builder::withPresentPg).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentPg, Builder::withAbsentPg).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withAbsentPg, Builder::withAbsentPg).andThen(IntAssert::isZero),
				compareBuilding(Builder::withPresentPg, Builder::withPresentPg).andThen(IntAssert::isZero),

				compareBuilding(left -> withCurrencyIsoCode(left, "ab123"), right -> withCurrencyIsoCode(right, "AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> withCurrencyIsoCode(left, "ab234"), right -> withCurrencyIsoCode(right, "AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> withCurrencyIsoCode(left, "theSame"), right -> withCurrencyIsoCode(right, "theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(ProductEurope1PricesAttributeHandlerTest::withAbsentNet,
						ProductEurope1PricesAttributeHandlerTest::withPresentNet).andThen(IntAssert::isNegative),
				compareBuilding(ProductEurope1PricesAttributeHandlerTest::withPresentNet,
						ProductEurope1PricesAttributeHandlerTest::withAbsentNet).andThen(IntAssert::isPositive),
				compareBuilding(ProductEurope1PricesAttributeHandlerTest::withAbsentNet,
						ProductEurope1PricesAttributeHandlerTest::withAbsentNet).andThen(IntAssert::isZero),
				compareBuilding(ProductEurope1PricesAttributeHandlerTest::withPresentNet,
						ProductEurope1PricesAttributeHandlerTest::withPresentNet).andThen(IntAssert::isZero),

				compareBuilding(left -> withUnitCode(left, "ab123"), right -> withUnitCode(right, "AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> withUnitCode(left, "ab234"), right -> withUnitCode(right, "AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> withUnitCode(left, "theSame"), right -> withUnitCode(right, "theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(left -> withMinqtd(left, 1L), right -> withMinqtd(right, 2L)).andThen(IntAssert::isNegative),
				compareBuilding(left -> withMinqtd(left, 2L), right -> withMinqtd(right, 1L)).andThen(IntAssert::isPositive),
				compareBuilding(left -> withMinqtd(left, 3L), right -> withMinqtd(right, 3L)).andThen(IntAssert::isZero),

				compareBuilding(left -> left.withPK(PK.NULL_PK), right -> right.withPK(PK.BIG_PK)).andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withPK(PK.BIG_PK), right -> right.withPK(PK.NULL_PK)).andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withPK(PK.NULL_PK), right -> right.withPK(PK.NULL_PK)).andThen(IntAssert::isZero));
	}

	static Function<Void, IntAssert> compareBuilding(final Function<Builder<PriceRowModel>, Builder<PriceRowModel>> functionA,
			final Function<Builder<PriceRowModel>, Builder<PriceRowModel>> functionB)
	{
		final PriceRowModel builtModelA = functionA.andThen(Builder::build).apply(Builder.of(PriceRowModel.class));
		final PriceRowModel builtModelB = functionB.andThen(Builder::build).apply(Builder.of(PriceRowModel.class));
		return compare(builtModelA, builtModelB);
	}

	static Function<Void, IntAssert> compare(final PriceRowModel builtModelA, final PriceRowModel builtModelB)
	{
		return ignored -> {
			final Comparator<PriceRowModel> comparator = new ProductEurope1PricesAttributeHandler().getPdtRowComparator();
			return assertThat(comparator.compare(builtModelA, builtModelB));
		};
	}

	static Builder<PriceRowModel> withCurrencyIsoCode(final Builder<PriceRowModel> builder, final String isoCode)
	{
		return builder.with(model -> {
			final CurrencyModel currencyMock = mock(CurrencyModel.class);
			given(currencyMock.getIsocode()).willReturn(isoCode);
			given(model.getCurrency()).willReturn(currencyMock);
		});
	}

	static Builder<PriceRowModel> withPresentNet(final Builder<PriceRowModel> builder)
	{
		return builder.with(model -> given(model.getNet()).willReturn(true));
	}

	static Builder<PriceRowModel> withAbsentNet(final Builder<PriceRowModel> builder)
	{
		return builder.with(model -> given(model.getNet()).willReturn(null));
	}

	static Builder<PriceRowModel> withUnitCode(final Builder<PriceRowModel> builder, final String code)
	{
		return builder.with(model -> {
			final UnitModel unitMock = mock(UnitModel.class);
			given(unitMock.getCode()).willReturn(code);
			given(model.getUnit()).willReturn(unitMock);
		});
	}

	static Builder<PriceRowModel> withMinqtd(final Builder<PriceRowModel> builder, final Long minqtd)
	{
		return builder.with(model -> given(model.getMinqtd()).willReturn(minqtd));
	}
}
