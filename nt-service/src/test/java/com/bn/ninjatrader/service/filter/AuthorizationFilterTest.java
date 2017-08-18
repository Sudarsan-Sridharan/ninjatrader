package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.auth.exception.InvalidTokenException;
import com.bn.ninjatrader.auth.token.DecodedToken;
import com.bn.ninjatrader.auth.token.TokenVerifier;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.exception.UnauthorizedMethodAccessExceptionMapper;
import com.bn.ninjatrader.service.security.AuthenticatedUser;
import org.assertj.core.util.Lists;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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
  private static final String VALID_TOKEN = "VALID_TOKEN";
  private static final String INVALID_TOKEN = "INVALID_TOKEN";

  private DummyResource resource;
  private DecodedToken token;

  @Override
  protected Application configure() {
    resource = new DummyResource();
    final AuthorizationFilter filter = new AuthorizationFilter(tokenVerifier);
    return new ResourceConfig()
        .register(resource)
        .register(UnauthorizedMethodAccessExceptionMapper.class)
        .register(filter);
  }

  @Before
  public void before() {
    reset(tokenVerifier);

    token = mock(DecodedToken.class);

    when(token.getUserId()).thenReturn("USER_ID");
    when(token.getFirstName()).thenReturn("John");
    when(token.getLastName()).thenReturn("Doe");
    when(token.getRoles()).thenReturn(Lists.newArrayList(Role.ADMIN));

    when(tokenVerifier.verifyToken(VALID_TOKEN)).thenReturn(token);
    when(tokenVerifier.verifyToken(INVALID_TOKEN)).thenThrow(new InvalidTokenException());
  }

  @Test
  public void testSecuredMethodWithRequiredRole_shouldAllowAccess() {
    when(token.hasRole(Role.ADMIN)).thenReturn(true);

    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, "Bearer " + VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify SecurityContext
    final SecurityContext securityContext = resource.getSecurityContext();
    assertThat(securityContext).isNotNull();
    assertThat(securityContext.getUserPrincipal()).isNotNull();

    // Verify Principal
    final AuthenticatedUser user = (AuthenticatedUser) securityContext.getUserPrincipal();
    assertThat(user.getUserId()).isEqualTo(token.getUserId());
    assertThat(user.getFirstname()).isEqualTo(token.getFirstName());
    assertThat(user.getLastname()).isEqualTo(token.getLastName());
    assertThat(user.getRoles()).isEqualTo(token.getRoles());
  }

  @Test
  public void testSecuredMethodWithTokenInQueryParam_shouldRespondWithForbidden() {
    when(token.hasRole(Role.ADMIN)).thenReturn(true);

    final Response response = target("/dummy/admin").queryParam("au", VALID_TOKEN).request().get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  @Test
  public void testSecuredMethodWithNoToken_shouldRespondWithForbidden() {
    final Response response = target("/dummy/admin").request().get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testSecuredMethodWithInvalidToken_shouldRespondWithForbidden() {
    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, "Bearer " + INVALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testSecuredMethodWithNoRole_shouldAllowAccess() {
    when(token.hasRole(Role.ADMIN)).thenReturn(false);

    final Response response = target("/dummy/admin").request().header(AUTH_HEADER, "Bearer " + VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(UNAUTHORIZED.getStatusCode());
  }

  @Test
  public void testUnsecuredMethodWithNoToken_shouldAllowAccess() {
    final Response response = target("/dummy/unsecured").request().get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  @Test
  public void testUnsecuredMethodWithValidToken_shouldAllowAccess() {
    final Response response = target("/dummy/unsecured").request().header(AUTH_HEADER, "Bearer " + VALID_TOKEN).get();
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
  }

  /**
   * Dummy resource to test security.
   */
  @Path("/dummy")
  public static final class DummyResource {

    private SecurityContext securityContext;

    @GET
    @Secured(Role.ADMIN)
    @Path("/admin")
    public Response getAdminSecured(@Context final SecurityContext securityContext,
                                    @QueryParam("au") final String token) {
      this.securityContext = securityContext;
      return Response.ok().build();
    }

    @GET
    @Path("/unsecured")
    public Response getUnsecured() {
      return Response.ok().build();
    }

    public SecurityContext getSecurityContext() {
      return securityContext;
    }
  }
}
