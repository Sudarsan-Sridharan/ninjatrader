package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.process.adjustment.PriceAdjustmentProcess;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentRunner {

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(new NtModelMongoModule());
    final PriceAdjustmentProcess process = injector.getInstance(PriceAdjustmentProcess.class);

    //TODO USE GROOVY SCRIPT
//    process.process(PriceAdjustmentRequest.forSymbol("UBP")
//        .from(LocalDate.now().minusYears(100))
//        .to(LocalDate.of(2014, 11, 12))
//        .adjustment(Operations.startWith(PriceAdjustmentRequest.PRICE).mult(20).div(33)));
  }
}
