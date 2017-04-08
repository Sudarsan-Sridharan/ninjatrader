package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.model.entity.User;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class SaveUserRequest {

  private List<User> users = Lists.newArrayList();

  public static SaveUserRequest withUser(final User user) {
    return new SaveUserRequest().addUser(user);
  }

  public SaveUserRequest addUser(final User user) {
    this.users.add(user);
    return this;
  }

  public SaveUserRequest addUsers(final Collection<User> users) {
    this.users.addAll(users);
    return this;
  }

  public SaveUserRequest addUsers(final User user, final User ... more) {
    this.users.addAll(Lists.asList(user, more));
    return this;
  }

  public List<User> getUsers() {
    return users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SaveUserRequest that = (SaveUserRequest) o;
    return Objects.equal(users, that.users);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(users);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("users", users)
        .toString();
  }
}
