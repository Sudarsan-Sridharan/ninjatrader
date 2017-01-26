package com.bn.ninjatrader.simulation.operation;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataType;

/**
 * @author bradwee2000@gmail.com
 */
public class Variables {

  public static final Variable BAR_INDEX = Variable.of(DataType.BAR_INDEX);

  public static final Variable PRICE_OPEN = Variable.of(DataType.PRICE_OPEN);
  public static final Variable PRICE_HIGH = Variable.of(DataType.PRICE_HIGH);
  public static final Variable PRICE_LOW = Variable.of(DataType.PRICE_LOW);
  public static final Variable PRICE_CLOSE = Variable.of(DataType.PRICE_CLOSE);
  public static final Variable VOLUME = Variable.of(DataType.VOLUME);

  public static final Variable ICHIMOKU_TENKAN = Variable.of(DataType.TENKAN);
  public static final Variable ICHIMOKU_KIJUN = Variable.of(DataType.KIJUN);
  public static final Variable ICHIMOKU_SENKOU_A = Variable.of(DataType.SENKOU_A);
  public static final Variable ICHIMOKU_SENKOU_B = Variable.of(DataType.SENKOU_B);
  public static final Variable ICHIMOKU_CHIKOU = Variable.of(DataType.CHIKOU);

  public static final Variable EMA = Variable.of(DataType.EMA);
  public static final Variable SMA = Variable.of(DataType.SMA);
}
