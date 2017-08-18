package com.bn.ninjatrader.common.type;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author bradwee2000@gmail.com
 */
public enum Role {
  SYSTEM("system"),

  ADMIN("admin"),

  SUBSCRIBER("subscriber"),

  VIEWER("viewer");

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

  /**
   * Role ID will be used to store in database. This prevents errors when enum objects are renamed.
   */
  @JsonValue
  public String getId() {
    return id;
  }
}
