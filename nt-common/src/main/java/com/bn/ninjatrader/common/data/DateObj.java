package com.bn.ninjatrader.common.data;

import java.time.LocalDate;

/**
 * Created by Brad on 6/11/16.
 */
public interface DateObj<T> extends Comparable<T> {

  LocalDate getDate();
}
