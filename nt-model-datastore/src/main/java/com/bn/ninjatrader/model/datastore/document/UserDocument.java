package com.bn.ninjatrader.model.datastore.document;

import com.bn.ninjatrader.common.model.User;
import com.bn.ninjatrader.common.type.Role;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@Entity
public class UserDocument {

  public static final UserDocument copyFrom(final User user) {
    return new UserDocument(user);
  }

  @Id
  private String userId;

  @Index
  private String username;

  @Unindex
  private String firstname;

  @Unindex
  private String lastname;

  @Index
  private String email;

  @Unindex
  private String mobile;

  @Unindex
  private List<String> watchList;

  @Unindex
  private List<Role> roles;

  private UserDocument(final User user) {
    this.userId = user.getUserId();
    this.username = user.getUsername();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.email = user.getEmail();
    this.mobile = user.getMobile();
    this.watchList = user.getWatchList();
    this.roles = Lists.newArrayList(user.getRoles());
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

  public List<String> getWatchList() {
    return watchList;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public User toUser() {
    return User.builder()
        .userId(userId)
        .username(username)
        .firstname(firstname)
        .lastname(lastname)
        .email(email)
        .mobile(mobile)
        .addAllToWatchList(watchList)
        .addRoles(roles)
        .build();
  }
}
