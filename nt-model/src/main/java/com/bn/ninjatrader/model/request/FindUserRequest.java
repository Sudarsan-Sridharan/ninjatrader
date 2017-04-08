package com.bn.ninjatrader.model.request;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author bradwee2000@gmail.com
 */
public class FindUserRequest {

  private String username;
  private String userId;

  public static FindUserRequest withUserId(final String userId) {
    return new FindUserRequest().userId(userId);
  }
  public static FindUserRequest withUsername(final String username) {
    return new FindUserRequest().username(username);
  }

  public FindUserRequest() {}

  public FindUserRequest username(final String username) {
    this.username = username;
    return this;
  }

  public FindUserRequest userId(final String userId) {
    this.userId = userId;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public String getUserId() {
    return userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FindUserRequest that = (FindUserRequest) o;
    return Objects.equal(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("username", username)
        .toString();
  }
}
