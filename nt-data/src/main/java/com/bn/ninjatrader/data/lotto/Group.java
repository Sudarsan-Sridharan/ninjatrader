package com.bn.ninjatrader.data.lotto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by Brad on 6/23/16.
 */
public class Group {

  private int[] count;

  private LocalDate date;

  public Group(int max) {
    count = new int[max + 1];
  }

  public Group(LocalDate date, int[] count) {
    this.date = date;
    this.count = count;
  }

  public int[] getCount() {
    return count;
  }

  public void incrementWinningNumbers(int ... lottoNums) {
    for (int lottoNum : lottoNums) {
      count[lottoNum]++;
    }
  }

  public void setCount(int[] nums) {
    this.count = nums;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("date", date)
        .append("nums", Arrays.toString(count))
        .toString();
  }

  public String print() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append("'").append(date).append("', ");
    for(int i=1; i<count.length; i++) {
      if (i > 1) {
        sb.append(",");
      }
      sb.append(count[i]);
    }
    sb.append("]");
    return sb.toString();
  }
}
