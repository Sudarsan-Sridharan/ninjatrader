package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.auth.exception.InvalidTokenException;
import com.bn.ninjatrader.auth.token.DecodedToken;
import com.bn.ninjatrader.auth.token.TokenVerifier;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.exception.UnauthorizedMethodAccessExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * @author bradwee2000@gmail.com
 */
public class AuthorizationFilterTest extends JerseyTest {
  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilterTest.class);
  private static final TokenVerifier tokenVerifier = mock(TokenVerifier.class);
  private static final String AUTH_HEADER = "Authorization";
  private static final String VALID_TOKEN = "Bearer VALID_TOKEN";
  private static final String INVALID_TOKEN = "Bearer INVALID_TOKEN";

  public DecodedToken token;

  @Override
  protected Application configure() {
    final AuthorizationFilter filter = new AuthorizationFilter(tokenVerifier);
    return new ResourceConfig()
        .register(DummyResource.class)
        .register(UnauthorizedMethodAccessExceptionMapper.class)
        .register(filter);
  }

  @Before
  public void before() {
    reset(tokenVerifier);

    token = mock(DecodedToken.class);

    when(tokenVerifier.verifyToken(VALID_TOKEN)).thenReturn(token);
    when(tokenVerifier.verifyToken(INVALID_TOKEN)).thenThrow(new InvalidTokenException());
  }

  @Test
  public void testSecuredMethodWithRequiredRole_shouldAllowAccess() {
    when(token.hasRole(Role.ADMIN)).thenReturn(true);

    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  @Test
  public void testAdminSecuredMethodWithNoToken_shouldRespondWithForbidden() {
    final Response response = target("/dummy/admin").request().get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testAdminSecuredMethodWithInvalidToken_shouldRespondWithForbidden() {
    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, INVALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testSecuredMethodWithNoRole_shouldAllowAccess() {
    when(token.hasRole(Role.ADMIN)).thenReturn(false);

    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testUnsecuredMethodWithNoToken_shouldAllowAccess() {
    final Response response = target("/dummy/unsecured").request().get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  @Test
  public void testUnsecuredMethodWithValidToken_shouldAllowAccess() {
    final Response response = target("/dummy/unsecured").request().header(AUTH_HEADER, VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  /**
   * Dummy resource to test security.
   */
  @Path("/dummy")
  public static final class DummyResource {

    @GET
    @Secured(Role.ADMIN)
    @Path("/admin")
    public Response getAdminSecured() {
      return Response.ok().build();
    }

    @GET
    @Path("/unsecured")
    public Response getUnsecured() {
      return Response.ok().build();
    }
  }
}
