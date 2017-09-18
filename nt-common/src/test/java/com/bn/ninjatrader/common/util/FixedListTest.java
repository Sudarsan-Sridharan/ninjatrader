package com.bn.ninjatrader.common.util;


import org.junit.Test;

import java.util.Arrays;

import static com.bn.ninjatrader.common.util.FixedList.TrimDirection.RIGHT_TO_LEFT;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Brad on 5/28/16.
 */
public class FixedListTest {

  @Test
  public void testFixedListAdd() {
    FixedList<Integer> fixedList = new FixedList<Integer>(2);

    fixedList.add(1);
    assertThat(fixedList.size()).isEqualTo(1);
    assertThat(fixedList.get(0).intValue()).isEqualTo(1);

    fixedList.add(2);
    assertThat(fixedList.size()).isEqualTo(2);
    assertThat(fixedList.get(0).intValue()).isEqualTo(1);

    fixedList.add(3);
    assertThat(fixedList.size()).isEqualTo(2);
    assertThat(fixedList.get(0).intValue()).isEqualTo(2);

    fixedList.add(4);
    assertThat(fixedList.size()).isEqualTo(2);
    assertThat(fixedList.get(0).intValue()).isEqualTo(3);
  }

  @Test
  public void testFixedListAddAtIndex() {
    final FixedList<Integer> fixedList = new FixedList<>(2);

    fixedList.add(0, 2);
    assertThat(fixedList.asList()).containsExactly(2);

    fixedList.add(0, 1);
    assertThat(fixedList.asList()).containsExactly(1, 2);

    fixedList.add(0, 0);
    assertThat(fixedList.asList()).containsExactly(1, 2);

    fixedList.add(2, 10);
    assertThat(fixedList.asList()).containsExactly(2, 10);
  }

  @Test
  public void testFixedListAddAll() {
    final FixedList<Integer> fixedList = FixedList.withMaxSize(2);

    fixedList.addAll(Arrays.asList(1, 2, 3, 4));
    assertThat(fixedList.asList()).containsExactly(3, 4);

    fixedList.addAll(Arrays.asList(5, 6 ,7));
    assertThat(fixedList.asList()).containsExactly(6, 7);
  }

  @Test
  public void testTrimRightToLeft() {
    final FixedList<Integer> fixedList = FixedList.withMaxSizeAndTrimDirection(2, RIGHT_TO_LEFT);
    fixedList.addAll(Arrays.asList(5, 6, 7, 8));

    assertThat(fixedList.asList()).containsExactly(5, 6);

    fixedList.add(0, 4);
    assertThat(fixedList.asList()).containsExactly(4, 5);
  }
}
