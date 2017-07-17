package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.auth.exception.InvalidTokenException;
import com.bn.ninjatrader.auth.exception.UnauthorizedMethodAccessException;
import com.bn.ninjatrader.auth.token.DecodedToken;
import com.bn.ninjatrader.auth.token.TokenVerifier;
import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.service.annotation.Secured;
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
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);
  private static final String AUTH_HEADER = "Authorization";

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
        final List<Role> classRoles = extractRoles(resourceClass);
        final List<Role> methodRoles = extractRoles(resourceMethod);

        // Check if the user is allowed to execute the method
        // The method annotations override the class annotations
        verifyRole(token, methodRoles.isEmpty() ? classRoles : methodRoles);

      } catch (InvalidTokenException e) {
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
    final String authToken = req.getHeaderString(AUTH_HEADER);
    if (StringUtils.isEmpty(authToken)) {
      throw new UnauthorizedMethodAccessException();
    }
    return tokenVerifier.verifyToken(authToken);
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
}
