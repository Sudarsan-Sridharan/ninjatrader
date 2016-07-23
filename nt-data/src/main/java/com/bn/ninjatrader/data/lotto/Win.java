package com.bn.ninjatrader.data.lotto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by a- on 6/23/16.
 */
public class Win {

  private int nums[];

  private LocalDate date;

  public Win() {

  }

  public Win(LocalDate date, int[] nums) {
    this.date = date;
    this.nums = nums;
  }

  public int[] getNums() {
    return nums;
  }

  public void setNums(int[] nums) {
    this.nums = nums;
  }

  public LocalDate getDate() {
    return date;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("date", date)
        .append("nums", Arrays.toString(nums))
        .toString();
  }
}
