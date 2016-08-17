package com.bn.ninjatrader.testplay.operation;

import com.bn.ninjatrader.testplay.type.DataType;

/**
 * Created by Brad on 8/2/16.
 */
public class Operations {

  private DataType lhsDataType;

  public static Operations prepare(DataType lhsDataType) {
    return new Operations(lhsDataType);
  }

  public Operations(DataType lhsDataType) {
    this.lhsDataType = lhsDataType;
  }
}
