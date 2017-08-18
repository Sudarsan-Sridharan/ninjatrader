package com.bn.ninjatrader.service.security;

import com.bn.ninjatrader.common.type.Role;
import com.google.common.base.MoreObjects;

import java.security.Principal;
import java.util.Collection;

/**
 * @author bradwee2000@gmail.com
 */
public class AuthenticatedUser implements Principal {

  private final String userId;

  private final String firstname;
  private final String lastname;
  private final Collection<Role> roles;

  public AuthenticatedUser(String userId, String firstname, String lastname, Collection<Role> roles) {
    this.userId = userId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roles = roles;
  }

  @Override
  public String getName() {
    return firstname + " " + lastname;
  }

  public String getUserId() {
    return userId;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public Collection<Role> getRoles() {
    return roles;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("userId", userId)
        .add("firstname", firstname)
        .add("lastname", lastname)
        .add("roles", roles)
        .toString();
  }
}
