package com.bn.ninjatrader.service.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;

/**
 * @author bradwee2000@gmail.com
 */
public class CreateUserRequest {
  private static final Logger LOG = LoggerFactory.getLogger(CreateUserRequest.class);

  @FormParam("username")
  private String username;

  @FormParam("firstname")
  private String firstname;

  @FormParam("from")
  private String lastname;

  @FormParam("email")
  private String email;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateUserRequest that = (CreateUserRequest) o;
    return Objects.equal(username, that.username) &&
        Objects.equal(firstname, that.firstname) &&
        Objects.equal(lastname, that.lastname) &&
        Objects.equal(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username, firstname, lastname, email);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("username", username)
        .add("firstname", firstname)
        .add("lastname", lastname)
        .add("email", email)
        .toString();
  }
}
