package com.bn.ninjatrader.service.filter;

import com.bn.ninjatrader.common.type.Role;
import com.bn.ninjatrader.service.annotation.Secured;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@Secured
@Singleton
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(final ContainerRequestContext req) throws IOException {
    final Class<?> resourceClass = resourceInfo.getResourceClass();
    final List<Role> classRoles = extractRoles(resourceClass);

    final Method resourceMethod = resourceInfo.getResourceMethod();
    final List<Role> methodRoles = extractRoles(resourceMethod);

    try {

      // Check if the user is allowed to execute the method
      // The method annotations override the class annotations
      if (methodRoles.isEmpty()) {
        checkPermissions(classRoles);
      } else {
        checkPermissions(methodRoles);
      }

    } catch (final Exception e) {
      req.abortWith(Response.status(Response.Status.FORBIDDEN).build());
    }
  }

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

  private void checkPermissions(List<Role> allowedRoles) throws Exception {

  }
}
