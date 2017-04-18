package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.type.TimeFrame;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;

import static com.bn.ninjatrader.model.request.FindBeforeDateRequest.builder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class FindBeforeDateRequestTest {

  private final LocalDate now = LocalDate.of(2016, 2, 1);

  @Test
  public void testCreateWithBuilder_shouldReturnFindBeforeDateRequest() {
    FindBeforeDateRequest request = builder()
        .symbol("MEG")
        .timeFrame(TimeFrame.ONE_WEEK)
        .numOfValues(1000)
        .beforeDate(now)
        .build();
    assertThat(request.getSymbol()).isEqualTo("MEG");
    assertThat(request.getTimeFrame()).isEqualTo(TimeFrame.ONE_WEEK);
    assertThat(request.getNumOfValues()).isEqualTo(1000);
    assertThat(request.getBeforeDate()).isEqualTo(now);
  }

  @Test
  public void testEqualsWithSameValues_shouldReturnEqual() {
    assertThat(builder().symbol("BDO").build())
        .isEqualTo(builder().symbol("BDO").build());
    assertThat(builder().timeFrame(TimeFrame.ONE_DAY).build())
        .isEqualTo(builder().timeFrame(TimeFrame.ONE_DAY).build());
    assertThat(builder().numOfValues(16).build())
        .isEqualTo(builder().numOfValues(16).build());
    assertThat(builder().beforeDate(now).build())
        .isEqualTo(builder().beforeDate(now).build());
  }

  @Test
  public void testEqualsWithDifferences_shouldReturnNotEqual() {
    assertThat(builder().symbol("MEG").build())
        .isNotEqualTo(builder().symbol("BDO").build());
    assertThat(builder().timeFrame(TimeFrame.ONE_DAY).build())
        .isNotEqualTo(builder().timeFrame(TimeFrame.ONE_WEEK).build());
    assertThat(builder().numOfValues(1).build())
        .isNotEqualTo(builder().numOfValues(100).build());
    assertThat(builder().beforeDate(now).build())
        .isNotEqualTo(builder().beforeDate(now.plusDays(1)).build());
  }

  @Test
  public void testHashCode_shouldReturnDiffHashCodeIfNotEqual() {
    final Set<FindBeforeDateRequest> set = Sets.newHashSet();
    set.add(builder().symbol("MEG").build());
    set.add(builder().symbol("BDO").build());
    set.add(builder().timeFrame(TimeFrame.ONE_DAY).build());
    set.add(builder().timeFrame(TimeFrame.ONE_WEEK).build());
    set.add(builder().numOfValues(1).build());
    set.add(builder().numOfValues(2).build());
    set.add(builder().beforeDate(now).build());
    set.add(builder().beforeDate(now.plusDays(1)).build());
    assertThat(set).hasSize(8);
  }
}
