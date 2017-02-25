//package com.bn.ninjatrader.common.function.handler;
//
//import com.bn.ninjatrader.common.data.Ichimoku;
//import com.google.inject.Singleton;
//
///**
// * Created by Brad on 6/1/16.
// */
//@Singleton
//public class IchimokuSenkouShiftForwardHandler implements ShiftHandler<Ichimoku> {
//
//  public void process(Ichimoku from, Ichimoku to) {
//    to.setSenkouA(from.getSenkouA());
//    to.setSenkouB(from.getSenkouB());
//  }
//
//  public Ichimoku newInstance() {
//    return new Ichimoku();
//  }
//
//  public void destroy(Ichimoku ichimoku) {
//    ichimoku.setSenkouA(0);
//    ichimoku.setSenkouB(0);
//  }
//}
