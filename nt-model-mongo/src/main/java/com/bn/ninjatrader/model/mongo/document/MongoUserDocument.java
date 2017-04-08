package com.bn.ninjatrader.model.mongo.document;

import com.bn.ninjatrader.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoUserDocument {

  public static final MongoUserDocument copyFrom(final User user) {
    return new MongoUserDocument(user.getUserId(),
        user.getUsername(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getMobile(),
        user.getWatchList());
  }

  @MongoId
  @MongoObjectId
  private String mongoId;

  @JsonProperty("userId")
  private String userId;

  @JsonProperty("username")
  private String username;

  @JsonProperty("fname")
  private String firstname;

  @JsonProperty("lname")
  private String lastname;

  @JsonProperty("email")
  private String email;

  @JsonProperty("mobile")
  private String mobile;

  @JsonProperty("watchList")
  private List<String> watchList;

  public MongoUserDocument(@JsonProperty("userId") final String userId,
                           @JsonProperty("username") final String username,
                           @JsonProperty("fname") final String firstname,
                           @JsonProperty("lname")final String lastname,
                           @JsonProperty("email") final String email,
                           @JsonProperty("mobile") final String mobile,
                           @JsonProperty("watchList") final List<String> watchList) {
    this.userId = userId;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.mobile = mobile;
    this.watchList = watchList == null || watchList.size() == 0 ? null : watchList;
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

  public List<String> getWatchList() {
    return watchList;
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
        .build();
  }
}
