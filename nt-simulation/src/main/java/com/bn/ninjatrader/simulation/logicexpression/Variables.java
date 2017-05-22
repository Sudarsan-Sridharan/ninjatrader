package com.bn.ninjatrader.simulation.logicexpression;

import com.bn.ninjatrader.logical.expression.operation.Variable;
import com.bn.ninjatrader.simulation.data.DataType;

/**
 * @author bradwee2000@gmail.com
 */
public class Variables {

  public static final Variable BAR_INDEX = Variable.of(DataType.BAR_INDEX);

  public static final Variable LOG = Variable.of(DataType.LOG);
  public static final Variable BROKER = Variable.of(DataType.BROKER);
  public static final Variable HISTORY = Variable.of(DataType.HISTORY);
  public static final Variable MARKERS = Variable.of(DataType.MARKERS);
  public static final Variable BOARDLOT = Variable.of(DataType.BOARDLOT);
  public static final Variable ACCOUNT = Variable.of(DataType.ACCOUNT);
  public static final Variable PORTFOLIO = Variable.of(DataType.PORTFOLIO);

  public static final Variable SYMBOL = Variable.of(DataType.SYMBOL);
  public static final Variable DATE = Variable.of(DataType.DATE);
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
