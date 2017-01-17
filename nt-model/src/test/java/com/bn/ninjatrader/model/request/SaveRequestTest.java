package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.common.data.Value;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.bn.ninjatrader.common.type.TimeFrame.ONE_WEEK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class SaveRequestTest {

  private final LocalDate now = LocalDate.of(2016, 1, 1);
  private final Value value1 = Value.of(now, 1d);
  private final Value value2 = Value.of(now, 2d);

  private final SaveRequest orig = SaveRequest.save("MEG").period(1).values(value1);
  private final SaveRequest equal = SaveRequest.save("MEG").period(1).values(value1);

  private final SaveRequest diffSymbol = SaveRequest.save("BDO").period(1).values(value1);
  private final SaveRequest diffPeriod = SaveRequest.save("MEG").period(2).values(value1);
  private final SaveRequest diffValue = SaveRequest.save("MEG").period(1).values(value2);
  private final SaveRequest diffValueSize = SaveRequest.save("MEG").period(1).values(value1, value1);
  private final SaveRequest diffTimeFrame = SaveRequest.save("MEG").period(1).values(value1).timeFrame(ONE_WEEK);

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffSymbol)
        .isNotEqualTo(diffPeriod)
        .isNotEqualTo(diffValue)
        .isNotEqualTo(diffValueSize)
        .isNotEqualTo(diffTimeFrame);
  }

  @Test
  public void testHashCode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffSymbol, diffPeriod, diffValue, diffValueSize, diffTimeFrame))
        .containsExactlyInAnyOrder(orig, diffSymbol, diffPeriod, diffValue, diffValueSize, diffTimeFrame);
  }
}
