package com.bn.ninjatrader.simulation.order.cancel;

/**
 * @author bradwee2000@gmail.com
 */
public final class CancelTypes {

  public static final CancelType cancelAll() {
    return CancelAll.instance();
  }

  private CancelTypes() {}
}
