//package com.bn.ninjatrader.common.function.handler;
//
//import com.bn.ninjatrader.common.data.Ichimoku;
//import com.google.inject.Singleton;
//
///**
// * Created by Brad on 6/1/16.
// */
//@Singleton
//public class IchimokuChikouShiftBackwardHandler implements ShiftHandler<Ichimoku> {
//
//  public void process(Ichimoku from, Ichimoku to) {
//    to.setChikou(from.getChikou());
//  }
//
//  public Ichimoku newInstance() {
//    return new Ichimoku();
//  }
//
//  public void destroy(Ichimoku ichimoku) {
//    ichimoku.setChikou(0);
//  }
//}
