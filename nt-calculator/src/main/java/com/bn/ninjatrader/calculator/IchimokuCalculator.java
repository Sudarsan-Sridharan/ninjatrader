package com.bn.ninjatrader.calculator;

import com.bn.ninjatrader.calculator.parameter.IchimokuParameters;
import com.bn.ninjatrader.model.deprecated.Ichimoku;
import com.bn.ninjatrader.model.deprecated.Value;
import com.bn.ninjatrader.model.entity.Price;
import com.bn.ninjatrader.model.util.DateObjUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 7/11/16.
 */
public class IchimokuCalculator {
  private static final Logger LOG = LoggerFactory.getLogger(IchimokuCalculator.class);

//  @Inject
//  private IchimokuSenkouShiftForwardHandler senkouShiftForwardHandler;
//
//  @Inject
//  private IchimokuChikouShiftBackwardHandler chikouShiftBackwardHandler;

  public List<Ichimoku> calc(final IchimokuParameters parameters) {
    if (parameters.isIncomplete()) {
      return Collections.emptyList();
    }

    final List<Ichimoku> ichimokuList = Lists.newArrayList();

    final SubProcess tenkanProcess = new TenkanSubProcess(parameters.getTenkanList());
    final SubProcess kijunProcess = new KijunSubProcess(parameters.getKijunList());
    final SubProcess senkouAProcess = new SenkouAProcess(parameters.getKijunList());
    final SubProcess senkouBProcess = new SenkouBProcess(parameters.getSenkouBList());

    for (final Price price : parameters.getPriceList()) {
      final Ichimoku.Builder ichimokuBuilder = Ichimoku.builder()
          .date(price.getDate())
          .chikou(price.getClose());

      tenkanProcess.process(price, ichimokuBuilder);
      kijunProcess.process(price, ichimokuBuilder);
      senkouAProcess.process(price, ichimokuBuilder);
      senkouBProcess.process(price, ichimokuBuilder);

      ichimokuList.add(ichimokuBuilder.build());
    }

    // Shift Chikou backward to past
//    ShiftBackward.forValues(ichimokuList)
//        .period(parameters.getChickouShiftBackPeriods())
//        .handler(chikouShiftBackwardHandler)
//        .execute();
//
//    // Shift Senkou A & B forward to future
//    ShiftForward.forValues(ichimokuList)
//        .period(parameters.getSenkouShiftForwardPeriods())
//        .handler(senkouShiftForwardHandler)
//        .execute();

    return ichimokuList;
  }

  /**
   * Tenkan SubProcess to set Tenkan values in the Ichimoku
   */
  private static class TenkanSubProcess extends SubProcess {

    TenkanSubProcess(final Collection<Value> tenkanList) {
      super(tenkanList);
    }

    @Override
    public void processValue(final Value value, final Ichimoku.Builder ichimokuBuilder) {
      ichimokuBuilder.tenkan(value.getValue());
    }
  }

  /**
   * Kijun SubProcess to set Kijun values in the Ichimoku
   */
  private static class KijunSubProcess extends SubProcess {

    KijunSubProcess(final Collection<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(final Value value, final Ichimoku.Builder ichimokuBuilder) {
      ichimokuBuilder.kijun(value.getValue());
    }
  }

  /**
   * SenkouA SubProcess to set SenkouA values in the Ichimoku
   */
  private static class SenkouAProcess extends SubProcess {
    SenkouAProcess(final Collection<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(final Value kijun, final Ichimoku.Builder ichimokuBuilder) {
      ichimokuBuilder.senkouA((ichimokuBuilder.getTenkan() + kijun.getValue()) / 2);
    }
  }

  /**
   * SenkouB SubProcess to set SenkouB values in the Ichimoku
   */
  private static class SenkouBProcess extends SubProcess {

    SenkouBProcess(final Collection<Value> kijunList) {
      super(kijunList);
    }

    @Override
    public void processValue(final Value value, final Ichimoku.Builder ichimokuBuilder) {
      ichimokuBuilder.senkouB(value.getValue());
    }
  }

  /**
   * Abstract sub-process to set different values in the Ichimoku
   */
  private static abstract class SubProcess {
    private Value[] values;
    private int index;
    private boolean isStart;

    SubProcess(final Collection<Value> valueList) {
      this.values = valueList.toArray(new Value[]{});
    }

    public void process(final Price price, final Ichimoku.Builder ichimokuBuilder) {
      startIfDateMatches(price);

      if (isStart) {
        try {
          processValue(values[index], ichimokuBuilder);
        } catch (ArrayIndexOutOfBoundsException e) {
          LOG.error("Error processing {} [index={}] [value size={}]", price, index, values.length);
          LOG.error(e.getMessage(), e);
        } catch (Exception e) {
          LOG.error("Error processing. [values size={}] [index={}]", values.length, index);
          throw e;
        }
        index++;
      }
    }

    private void startIfDateMatches(final Price price) {
      if (!isStart && DateObjUtil.isDateEquals(price, values[0])) {
        isStart = true;
      }
    }

    public abstract void processValue(final Value value, final Ichimoku.Builder ichimokuBuilder);
  }
}

