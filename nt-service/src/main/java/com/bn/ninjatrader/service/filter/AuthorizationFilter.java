package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.auth.exception.InvalidTokenException;
import com.bn.ninjatrader.auth.exception.UnauthorizedMethodAccessException;
import com.bn.ninjatrader.auth.token.DecodedToken;
import com.bn.ninjatrader.auth.token.TokenVerifier;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.service.annotation.Secured;
import com.bn.ninjatrader.service.security.AuthenticatedUser;
import com.bn.ninjatrader.service.security.NtSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Adds authorization checking for methods or classes annotated with Secured.
 *
 * @author bradwee2000@gmail.com
 */
@Singleton
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);
  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER = "Bearer ";
  private static final String EMPTY = "";

  @Context
  private ResourceInfo resourceInfo;

  private final TokenVerifier tokenVerifier;

  @Inject
  public AuthorizationFilter(final TokenVerifier tokenVerifier) {
    this.tokenVerifier = tokenVerifier;
  }

  @Override
  public void filter(final ContainerRequestContext req) throws IOException {
    final Class<?> resourceClass = resourceInfo.getResourceClass();
    final Method resourceMethod = resourceInfo.getResourceMethod();

    if (resourceClass.isAnnotationPresent(Secured.class) ||
        resourceMethod.isAnnotationPresent(Secured.class)) {

      try {
        final DecodedToken token = verifyToken(req);

        // Allow passage if request is a System call
        if (token.hasRole(Role.SYSTEM)) {
          return;
        }

        final List<Role> classRoles = extractRoles(resourceClass);
        final List<Role> methodRoles = extractRoles(resourceMethod);

        // The method annotations override the class annotations
        final List<Role> allowedRoles = methodRoles.isEmpty() ? classRoles : methodRoles;

        // Check if the user is allowed to execute the method
        if (!allowedRoles.isEmpty()) {
          verifyRole(token, allowedRoles);
        }

        // If all is well, create SecurityContext
        initSecurityContext(req, token);

      } catch (InvalidTokenException e) {
        LOG.error("{}", e.getMessage());
        throw new UnauthorizedMethodAccessException();
      }
    }
  }

  /**
   * Extract list of roles from annotation.
   */
  private List<Role> extractRoles(AnnotatedElement annotatedElement) {
    if (annotatedElement == null) {
      return Collections.emptyList();
    } else {
      final Secured secured = annotatedElement.getAnnotation(Secured.class);
      if (secured == null) {
        return Collections.emptyList();
      } else {
        final Role[] allowedRoles = secured.value();
        return Arrays.asList(allowedRoles);
      }
    }
  }

  /**
   * Verify that request header contains valid token.
   */
  private DecodedToken verifyToken(final ContainerRequestContext req) {
    String authentication = req.getHeaderString(AUTH_HEADER);

    // If no authentication header, check query parameters
    if (StringUtils.isEmpty(authentication)) {
      authentication = req.getUriInfo().getQueryParameters().getFirst("au");

      // If no token found in query parameters, check in cookies
      if (StringUtils.isEmpty(authentication)) {

        // If still no token found, forbid access
        if (req.getCookies().get("au") == null || StringUtils.isEmpty(req.getCookies().get("au").getValue())) {
          throw new UnauthorizedMethodAccessException();
        }

        authentication = req.getCookies().get("au").getValue();
      }
    } else {
      authentication = authentication.replace(BEARER, EMPTY);
    }
    return tokenVerifier.verifyToken(authentication);
  }

  /**
   * Verify that user has required role to access method.
   */
  private void verifyRole(final DecodedToken token, final List<Role> allowedRoles) {
    for (final Role role : allowedRoles) {
      if (token.hasRole(role)) {
        return;
      }
    }
    throw new UnauthorizedMethodAccessException();
  }

  private void initSecurityContext(final ContainerRequestContext req, final DecodedToken token) {
    // Create User and SecurityContext
    final AuthenticatedUser user = new AuthenticatedUser(
        token.getUserId(),
        token.getFirstName(),
        token.getLastName(),
        token.getRoles());
    final String scheme = req.getUriInfo().getRequestUri().getScheme();
    req.setSecurityContext(new NtSecurityContext(user, scheme));
  }
}
