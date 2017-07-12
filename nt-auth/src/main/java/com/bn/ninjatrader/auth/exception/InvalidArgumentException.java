package com.bn.ninjatrader.auth.exception;

/**
 * @author bradwee2000@gmail.com
 */
public class InvalidArgumentException extends RuntimeException {

  private static final String MSG = "First method argument must be of type javax.servlet.http.HttpServletRequest";

  public InvalidArgumentException() {
    super(MSG);
  }
}
