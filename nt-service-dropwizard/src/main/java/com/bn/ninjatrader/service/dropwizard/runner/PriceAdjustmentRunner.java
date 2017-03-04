package com.bn.ninjatrader.service.dropwizard.runner;

import com.bn.ninjatrader.logical.expression.operation.Operations;
import com.bn.ninjatrader.model.mongo.guice.NtModelMongoModule;
import com.bn.ninjatrader.process.adjustment.PriceAdjustmentProcess;
import com.bn.ninjatrader.process.adjustment.PriceAdjustmentRequest;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.time.LocalDate;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentRunner {

  public static void main(final String args[]) {
    final Injector injector = Guice.createInjector(new NtModelMongoModule());
    final PriceAdjustmentProcess process = injector.getInstance(PriceAdjustmentProcess.class);

    process.process(PriceAdjustmentRequest.forSymbol("CNPF")
        .from(LocalDate.now().minusYears(100))
        .to(LocalDate.of(2016, 7, 11))
        .adjustment(Operations.startWith(PriceAdjustmentRequest.PRICE).div(1.5)));
  }
}
