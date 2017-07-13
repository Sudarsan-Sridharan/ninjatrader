package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class InvalidTokenException extends RuntimeException {
  private static final String MSG = "Token is invalid.";

  public InvalidTokenException() {
    super(MSG);
  }

  public InvalidTokenException(final Throwable throwable) {
    super(MSG, throwable);
  }

  public InvalidTokenException(final String msg, final Throwable throwable) {
    super(msg, throwable);
  }
}
