package com.bn.ninjatrader.process.runner;

import com.bn.ninjatrader.process.annotation.CalcAllProcess;
import com.bn.ninjatrader.process.calc.CalcProcess;
import com.bn.ninjatrader.process.guice.NtProcessModule;
import com.bn.ninjatrader.process.request.CalcRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by Brad on 8/15/16.
 */
public class CalcAllProcessRunner implements Runnable {

  @Inject
  @CalcAllProcess
  private CalcProcess calcAllProcess;

  private final CalcRequest request;

  public CalcAllProcessRunner(CalcRequest request) {
    this.request = request;
  }

  @Override
  public void run() {
    calcAllProcess.process(request);
  }

  public static void main(String args[]) {
    Injector injector = Guice.createInjector(
        new NtProcessModule()
    );

    injector.getInstance(CalcAllProcessRunner.class);
  }
}
