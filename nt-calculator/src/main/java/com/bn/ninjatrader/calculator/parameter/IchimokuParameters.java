package com.bn.ninjatrader.calculator.parameter;

import com.bn.ninjatrader.common.data.Price;
import com.bn.ninjatrader.common.data.Value;

import java.util.List;

/**
 * Created by Brad on 7/12/16.
 */
public class IchimokuParameters {

  private List<Price> priceList;
  private List<Value> tenkanList;
  private List<Value> kijunList;
  private List<Value> senkouBList;
  private int chickouShiftBackPeriods;
  private int senkouShiftForwardPeriods;

  public static IchimokuParameterBuilder builder() {
    return new IchimokuParameterBuilder();
  }

  private IchimokuParameters() {}

  public List<Price> getPriceList() {
    return priceList;
  }

  public List<Value> getTenkanList() {
    return tenkanList;
  }

  public List<Value> getKijunList() {
    return kijunList;
  }

  public List<Value> getSenkouBList() {
    return senkouBList;
  }

  public int getChickouShiftBackPeriods() {
    return chickouShiftBackPeriods;
  }

  public int getSenkouShiftForwardPeriods() {
    return senkouShiftForwardPeriods;
  }

  public boolean isIncomplete() {
    if (tenkanList == null || tenkanList.isEmpty() ||
        kijunList == null || kijunList.isEmpty() ||
        senkouBList == null || senkouBList.isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Builder for IchimokuParameters
   */
  public static class IchimokuParameterBuilder {
    IchimokuParameters parameters = new IchimokuParameters();

    public IchimokuParameterBuilder priceList(List<Price> priceList) {
      parameters.priceList = priceList;
      return this;
    }

    public IchimokuParameterBuilder tenkanList(List<Value> tenkanList) {
      parameters.tenkanList = tenkanList;
      return this;
    }

    public IchimokuParameterBuilder kijunList(List<Value> kijunList) {
      parameters.kijunList = kijunList;
      return this;
    }

    public IchimokuParameterBuilder senkouBList(List<Value> senkouBList) {
      parameters.senkouBList = senkouBList;
      return this;
    }

    public IchimokuParameterBuilder chickouShiftBackPeriods(int periods) {
      parameters.chickouShiftBackPeriods = periods;
      return this;
    }

    public IchimokuParameterBuilder senkouShiftForwardPeriods(int periods) {
      parameters.senkouShiftForwardPeriods = periods;
      return this;
    }

    public IchimokuParameters build() {
      return parameters;
    }
  }
}
