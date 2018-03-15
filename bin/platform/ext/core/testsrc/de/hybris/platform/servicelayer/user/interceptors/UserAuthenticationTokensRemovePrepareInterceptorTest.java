package de.hybris.platform.servicelayer.user.interceptors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.webservicescommons.model.OAuthAccessTokenModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationTokensRemovePrepareInterceptorTest
{
	@Mock
	private TimeService timeService;
	@Mock
	private InterceptorContext context;

	@InjectMocks
	private UserAuthenticationTokensRemovePrepareInterceptor interceptor;

	private final Instant now = Instant.now();

	@Before
	public void setUp()
	{
		when(timeService.getCurrentTime()).thenReturn(Date.from(now));
	}

	@Test
	public void interceptorShouldClearTokensWhenLoginDisabledFlagIsSet() throws InterceptorException
	{
		//given
		final UserModel model = getUserModel();
		model.setLoginDisabled(true);

		//when
		interceptor.onPrepare(model, context);

		//then
		assertThat(model.getTokens()).isEmpty();
		verify(context).registerElementFor(any(OAuthAccessTokenModel.class), eq(PersistenceOperation.DELETE));
	}

	@Test
	public void interceptorShouldClearTokensWhenDeactivationDateIsTheSameAsNow() throws InterceptorException
	{
		//given
		final UserModel model = getUserModel();
		model.setDeactivationDate(Date.from(now));

		//when
		interceptor.onPrepare(model, context);

		//then
		assertThat(model.getTokens()).isEmpty();
		verify(context).registerElementFor(any(OAuthAccessTokenModel.class), eq(PersistenceOperation.DELETE));
	}

	@Test
	public void interceptorShouldClearTokensWhenDeactivationDateIsInThePast() throws InterceptorException
	{
		//given
		final UserModel model = getUserModel();
		model.setDeactivationDate(Date.from(now.minusSeconds(44444)));

		//when
		interceptor.onPrepare(model, context);

		//then
		assertThat(model.getTokens()).isEmpty();
		verify(context).registerElementFor(any(OAuthAccessTokenModel.class), eq(PersistenceOperation.DELETE));
	}

	@Test
	public void interceptorShouldNotClearTokensWhenDeactivationDateIsInTheFuture() throws InterceptorException
	{
		//given
		final UserModel model = getUserModel();
		model.setDeactivationDate(Date.from(now.plusSeconds(33333)));

		//when
		interceptor.onPrepare(model, context);

		//then
		assertThat(model.getTokens()).isNotEmpty();
	}

	@Test
	public void interceptorShouldNotClearTokensWhenDeactivationDateIsNull() throws InterceptorException
	{
		//given
		final UserModel model = getUserModel();
		model.setDeactivationDate(null);

		//when
		interceptor.onPrepare(model, context);

		//then
		assertThat(model.getTokens()).isNotEmpty();
	}

	protected UserModel getUserModel()
	{
		final UserModel model = new UserModel();
		final ArrayList<OAuthAccessTokenModel> tokens = new ArrayList<>();
		tokens.add(new OAuthAccessTokenModel());
		model.setTokens(tokens);
		return model;
	}
}