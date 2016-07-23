package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.common.data.Ichimoku;
import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.common.function.ShiftBackward;
import com.bn.ninjatrader.common.function.ShiftForward;
import com.bn.ninjatrader.common.function.handler.IchimokuChikouShiftBackwardHandler;
import com.bn.ninjatrader.common.function.handler.IchimokuSenkouShiftForwardHandler;
import com.bn.ninjatrader.common.util.DateObjUtil;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 7/11/16.
 */
public class IchimokuCalculator {
  private static final Logger log = LoggerFactory.getLogger(IchimokuCalculator.class);

  @Inject
  private IchimokuSenkouShiftForwardHandler senkouShiftForwardHandler;

  @Inject
  private IchimokuChikouShiftBackwardHandler chikouShiftBackwardHandler;

  public List<Ichimoku> calc(IchimokuParameters parameters) {
    if (parameters.isIncomplete()) {
      return Collections.emptyList();
    }

    List<Ichimoku> ichimokuList = Lists.newArrayList();

    SubProcess tenkanProcess = new TenkanSubProcess(parameters.getTenkanList());
    SubProcess kijunProcess = new KijunSubProcess(parameters.getKijunList());
    SubProcess senkouAProcess = new SenkouAProcess(parameters.getKijunList());
    SubProcess senkouBProcess = new SenkouBProcess(parameters.getSenkouBList());

    for (Price price : parameters.getPriceList()) {
      Ichimoku ichimoku = new Ichimoku();
      ichimoku.setDate(price.getDate());
      ichimoku.setChikou(price.getClose());

      tenkanProcess.process(price, ichimoku);
      kijunProcess.process(price, ichimoku);
      senkouAProcess.process(price, ichimoku);
      senkouBProcess.process(price, ichimoku);

      ichimokuList.add(ichimoku);
    }

    // Shift Chikou backward to past
    ShiftBackward.forValues(ichimokuList)
        .period(parameters.getChickouShiftBackPeriods())
        .handler(chikouShiftBackwardHandler)
        .execute();

    // Shift Senkou A & B forward to future
    ShiftForward.forValues(ichimokuList)
        .period(parameters.getSenkouShiftForwardPeriods())
        .handler(senkouShiftForwardHandler)
        .execute();

    return ichimokuList;
  }

  /**
   * Tenkan SubProcess to set Tenkan values in the Ichimoku
   */
  private static class TenkanSubProcess extends SubProcess {

    TenkanSubProcess(List<Value> tenkanList) {
      super(tenkanList);
    }

    @Override
    public void processValue(Value value, Ichimoku ichimoku) {
      ichimoku.setTenkan(value.getValue());
    }
  }

  /**
   * Kijun SubProcess to set Kijun values in the Ichimoku
   */
  private static class KijunSubProcess extends SubProcess {

    KijunSubProcess(List<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(Value value, Ichimoku ichimoku) {
      ichimoku.setKijun(value.getValue());
    }
  }

  /**
   * SenkouA SubProcess to set SenkouA values in the Ichimoku
   */
  private static class SenkouAProcess extends SubProcess {
    SenkouAProcess(List<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(Value kijun, Ichimoku ichimoku) {
      ichimoku.setSenkouA((ichimoku.getTenkan() + kijun.getValue()) / 2);
    }
  }

  /**
   * SenkouB SubProcess to set SenkouB values in the Ichimoku
   */
  private static class SenkouBProcess extends SubProcess {

    SenkouBProcess(List<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(Value value, Ichimoku ichimoku) {
      ichimoku.setSenkouB(value.getValue());
    }
  }

  /**
   * Abstract sub-process to set different values in the Ichimoku
   */
  private static abstract class SubProcess {
    private Value[] values;
    private int index;
    private boolean isStart;

    SubProcess(List<Value> valueList) {
      this.values = valueList.toArray(new Value[]{});
    }

    public void process(Price price, Ichimoku ichimoku) {
      startIfDateMatches(price);

      if (isStart) {
        try {
          processValue(values[index], ichimoku);
        } catch (ArrayIndexOutOfBoundsException e) {
          log.error("Error processing [price={}] [ichimoku={}] [last value={}]", price, ichimoku, values[values.length-1]);
          log.error(e.getMessage(), e);
        }
        index++;
      }
    }

    private void startIfDateMatches(Price price) {
      if (!isStart && DateObjUtil.isDateEquals(price, values[0])) {
        isStart = true;
      }
    }

    public abstract void processValue(Value value, Ichimoku ichimoku);
  }
}

