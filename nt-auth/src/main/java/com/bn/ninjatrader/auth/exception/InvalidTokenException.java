package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class InvalidTokenException extends RuntimeException {
  private static final String MSG = "Token is invalid.";

  public InvalidTokenException(final Throwable throwable) {
    super(MSG, throwable);
  }
}
