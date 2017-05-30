package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class UserIdNotFoundException extends RuntimeException {
  private static final String MSG = "User with ID [%s] is not found.";

  public UserIdNotFoundException(final String userId) {
    super(String.format(MSG, userId));
  }
}
