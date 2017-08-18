package com.bn.ninjatrader.service.security;

import com.bn.ninjatrader.common.type.Role;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * @author bradwee2000@gmail.com
 */
public class NtSecurityContext implements SecurityContext {
  private static final String HTTPS_SCHEME = "https";

  private AuthenticatedUser user;
  private String scheme;

  public NtSecurityContext(AuthenticatedUser user, String scheme) {
    this.user = user;
    this.scheme = scheme;
  }

  @Override
  public Principal getUserPrincipal() {
    return user;
  }

  @Override
  public boolean isUserInRole(String roleId) {
    final Role role = Role.findById(roleId);
    if (user.getRoles() != null) {
      return user.getRoles().contains(role);
    }
    return false;
  }

  @Override
  public boolean isSecure() {
    return HTTPS_SCHEME.equals(scheme);
  }

  @Override
  public String getAuthenticationScheme() {
    return SecurityContext.BASIC_AUTH;
  }
}
