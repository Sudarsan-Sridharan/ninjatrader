package com.bn.ninjatrader.common.util;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Brad on 8/18/16.
 */
public class ListUtilTest {

  @Test
  public void testFillToSize() {
    List<String> list = Lists.newArrayList();

    ListUtil.fillToSize(list, "filler", 1);
    assertThat(list).containsExactly("filler");

    ListUtil.fillToSize(list, "", 10);
    assertThat(list).hasSize(10);
    assertThat(list).startsWith("");
    assertThat(list).endsWith("filler");

    ListUtil.fillToSize(list, "", 1);
    assertThat(list).hasSize(10);
    assertThat(list).startsWith("");
    assertThat(list).endsWith("filler");
  }
}
