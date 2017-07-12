package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class UnauthorizedMethodAccessException extends RuntimeException {

  private static final String MSG = "User is not authorized to access method.";

  public UnauthorizedMethodAccessException() {
    super(MSG);
  }
}
