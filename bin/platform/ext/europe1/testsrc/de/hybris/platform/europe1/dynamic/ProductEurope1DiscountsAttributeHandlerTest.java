package de.hybris.platform.europe1.dynamic;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.europe1.model.DiscountRowModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import org.fest.assertions.IntAssert;
import org.junit.runners.Parameterized;


public class ProductEurope1DiscountsAttributeHandlerTest extends AbstractProductEurope1AttributeHandlerTest
{

	public ProductEurope1DiscountsAttributeHandlerTest(final Function<Void, IntAssert> function)
	{
		super(function);
	}

	@Parameterized.Parameters
	public static Iterable<Object> data()
	{
		return Arrays.asList( //
				compare(null, null).andThen(IntAssert::isZero),

				compareBuilding(left -> left.withUserUid("ab123"), right -> right.withUserUid("AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withUserUid("ab234"), right -> right.withUserUid("AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withUserUid("theSame"), right -> right.withUserUid("theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(left -> left.withUgCode("ab123"), right -> right.withUgCode("AB234")).andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withUgCode("ab234"), right -> right.withUgCode("AB123")).andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withUgCode("theSame"), right -> right.withUgCode("theSame")).andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentProduct, Builder::withPresentProduct).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentProduct, Builder::withAbsentProduct).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withAbsentProduct, Builder::withAbsentProduct).andThen(IntAssert::isZero),
				compareBuilding(Builder::withPresentProduct, Builder::withPresentProduct).andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentProductId, Builder::withPresentProductId).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentProductId, Builder::withAbsentProductId).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withAbsentProductId, Builder::withAbsentProductId).andThen(IntAssert::isZero),
				compareBuilding(Builder::withPresentProductId, Builder::withPresentProductId).andThen(IntAssert::isZero),

				compareBuilding(Builder::withAbsentPg, Builder::withPresentPg).andThen(IntAssert::isNegative),
				compareBuilding(Builder::withPresentPg, Builder::withAbsentPg).andThen(IntAssert::isPositive),
				compareBuilding(Builder::withAbsentPg, Builder::withAbsentPg).andThen(IntAssert::isZero),
				compareBuilding(Builder::withPresentPg, Builder::withPresentPg).andThen(IntAssert::isZero),

				compareBuilding(left -> withDiscountCode(left, "ab123"), right -> withDiscountCode(right, "AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> withDiscountCode(left, "ab234"), right -> withDiscountCode(right, "AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> withDiscountCode(left, "theSame"), right -> withDiscountCode(right, "theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(left -> withCurrencyIsoCode(left, "ab123"), right -> withCurrencyIsoCode(right, "AB234"))
						.andThen(IntAssert::isNegative),
				compareBuilding(left -> withCurrencyIsoCode(left, "ab234"), right -> withCurrencyIsoCode(right, "AB123"))
						.andThen(IntAssert::isPositive),
				compareBuilding(left -> withCurrencyIsoCode(left, "theSame"), right -> withCurrencyIsoCode(right, "theSame"))
						.andThen(IntAssert::isZero),

				compareBuilding(left -> left.withPK(PK.NULL_PK), right -> right.withPK(PK.BIG_PK)).andThen(IntAssert::isNegative),
				compareBuilding(left -> left.withPK(PK.BIG_PK), right -> right.withPK(PK.NULL_PK)).andThen(IntAssert::isPositive),
				compareBuilding(left -> left.withPK(PK.NULL_PK), right -> right.withPK(PK.NULL_PK)).andThen(IntAssert::isZero));
	}

	static Function<Void, IntAssert> compareBuilding(
			final Function<Builder<DiscountRowModel>, Builder<DiscountRowModel>> functionA,
			final Function<Builder<DiscountRowModel>, Builder<DiscountRowModel>> functionB)
	{
		final DiscountRowModel builtModelA = functionA.andThen(Builder::build).apply(Builder.of(DiscountRowModel.class));
		final DiscountRowModel builtModelB = functionB.andThen(Builder::build).apply(Builder.of(DiscountRowModel.class));
		return compare(builtModelA, builtModelB);
	}

	static Function<Void, IntAssert> compare(final DiscountRowModel builtModelA, final DiscountRowModel builtModelB)
	{
		return ignored -> {
			final Comparator<DiscountRowModel> comparator = new ProductEurope1DiscountsAttributeHandler().getPdtRowComparator();
			return assertThat(comparator.compare(builtModelA, builtModelB));
		};
	}

	static Builder<DiscountRowModel> withCurrencyIsoCode(final Builder<DiscountRowModel> builder, final String code)
	{
		return builder.with(model -> {
			final CurrencyModel currencyMock = mock(CurrencyModel.class);
			given(currencyMock.getIsocode()).willReturn(code);
			given(model.getCurrency()).willReturn(currencyMock);
		});
	}

	static Builder<DiscountRowModel> withDiscountCode(final Builder<DiscountRowModel> builder, final String code)
	{
		return builder.with(model -> {
			final DiscountModel discountMock = mock(DiscountModel.class);
			given(discountMock.getCode()).willReturn(code);
			given(model.getDiscount()).willReturn(discountMock);
		});
	}
}
