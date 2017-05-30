package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class ExpiredTokenException extends RuntimeException {
  private static final String MSG = "Token is expired.";

  public ExpiredTokenException(final Throwable throwable) {
    super(MSG, throwable);
  }
}
