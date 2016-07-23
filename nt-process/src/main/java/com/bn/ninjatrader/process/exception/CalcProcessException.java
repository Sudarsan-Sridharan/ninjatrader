package com.bn.ninjatrader.process.exception;

/**
 * Created by Brad on 6/11/16.
 */
public class CalcProcessException extends RuntimeException {

  public CalcProcessException(Exception e) {
    super(e);
  }

  public CalcProcessException(String msg, Exception e) {
    super(msg, e);
  }
}
