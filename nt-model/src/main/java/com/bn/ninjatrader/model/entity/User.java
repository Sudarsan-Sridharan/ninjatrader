package com.bn.ninjatrader.model.entity;

import com.bn.ninjatrader.common.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  public static final Builder builder() {
    return new Builder();
  }

  @JsonProperty("userId")
  private final String userId;

  @JsonProperty("username")
  private final String username;

  @JsonProperty("firstname")
  private final String firstname;

  @JsonProperty("lastname")
  private final String lastname;

  @JsonProperty("email")
  private final String email;

  @JsonProperty("mobile")
  private final String mobile;

  @JsonProperty("roles")
  private final Collection<Role> roles;

  @JsonProperty("watchList")
  private final List<String> watchList; // Stock symbols

  public User(@JsonProperty("userId") final String userId,
              @JsonProperty("username") final String username,
              @JsonProperty("firstname") final String firstname,
              @JsonProperty("lastname") final String lastname,
              @JsonProperty("email") final String email,
              @JsonProperty("mobile") final String mobile,
              @JsonProperty("roles") final Collection<Role> roles,
              @JsonProperty("watchList") final Collection<String> watchList) {
    this.userId = userId;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.mobile = mobile;
    this.roles = Collections.unmodifiableCollection(roles);
    this.watchList = watchList.stream().sorted().collect(Collectors.toList());
  }

  public String getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public String getMobile() {
    return mobile;
  }

  public Collection<Role> getRoles() {
    return roles;
  }

  public boolean hasRole(final String role) {
    return roles.contains(role);
  }

  public List<String> getWatchList() {
    return Lists.newArrayList(watchList);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final User user = (User) o;
    return Objects.equal(userId, user.userId) &&
        Objects.equal(username, user.username) &&
        Objects.equal(firstname, user.firstname) &&
        Objects.equal(lastname, user.lastname) &&
        Objects.equal(email, user.email) &&
        Objects.equal(mobile, user.mobile) &&
        Objects.equal(watchList, user.watchList);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userId, username, firstname, lastname, email, mobile, watchList);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("userId", userId)
        .add("username", username)
        .add("firstname", firstname)
        .add("lastname", lastname)
        .add("email", email)
        .add("mobile", mobile)
        .add("watchList", watchList)
        .toString();
  }

  /**
   * Builder
   */
  public static final class Builder {
    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private final Set<String> watchList = Sets.newHashSet();
    private final Set<Role> roles = Sets.newHashSet();

    public Builder userId(final String userId) {
      this.userId = userId;
      return this;
    }

    public Builder username(final String username) {
      this.username = username;
      return this;
    }

    public Builder firstname(final String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder lastname(final String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Builder email(final String email) {
      this.email = email;
      return this;
    }

    public Builder mobile(final String mobile) {
      this.mobile = mobile;
      return this;
    }

    public Builder addRole(final Role role) {
      this.roles.add(role);
      return this;
    }

    public Builder addRoles(final Collection<Role> roles) {
      if (roles != null) {
        this.roles.addAll(roles);
      }
      return this;
    }

    public Builder addRoles(final Role role, final Role ... more) {
      this.roles.addAll(Lists.asList(role, more));
      return this;
    }

    public Builder addToWatchList(final String symbol) {
      watchList.add(symbol);
      return this;
    }

    public Builder addAllToWatchList(final String symbol, final String ... more) {
      watchList.addAll(Lists.asList(symbol, more));
      return this;
    }

    public Builder addAllToWatchList(final Collection<String> symbols) {
      if (symbols != null) {
        watchList.addAll(symbols);
      }
      return this;
    }

    public User build() {
      return new User(userId, username, firstname, lastname, email, mobile, roles, watchList);
    }
  }
}
