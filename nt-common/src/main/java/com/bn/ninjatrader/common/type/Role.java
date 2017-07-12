package com.bn.ninjatrader.common.type;

/**
 * @author bradwee2000@gmail.com
 */
public enum Role {

  ADMIN("admin"),

  SUBSCRIBER("subscriber");

  private static final String ROLE_ID_NOT_FOUND = "Role with id %s is not found";

  public static final Role findById(final String id) {
    for (final Role role : Role.values()) {
      if (role.id.equals(id)) {
        return role;
      }
    }
    throw new IllegalArgumentException(String.format(ROLE_ID_NOT_FOUND, id));
  }

  final String id;

  Role(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
