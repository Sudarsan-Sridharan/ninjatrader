package com.bn.ninjatrader.process.adjustment;

import com.beust.jcommander.internal.Lists;
import com.bn.ninjatrader.logical.expression.operation.Operation;
import com.bn.ninjatrader.logical.expression.operation.Operations;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentRequestTest {

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 10);

  @Test
  public void testCreate_shouldSetProperties() {
    final Operation adjustment = Operations.create(3);
    final PriceAdjustmentRequest request =
        PriceAdjustmentRequest.forAllSymbols().from(from).to(to).adjustment(adjustment);

    assertThat(request.isForAllSymbols()).isTrue();
    assertThat(request.getFromDate()).isEqualTo(from);
    assertThat(request.getToDate()).isEqualTo(to);
    assertThat(request.getOperation()).isEqualTo(adjustment);

    assertThat(PriceAdjustmentRequest.forSymbols("MEG", "BDO").isForAllSymbols()).isFalse();
    assertThat(PriceAdjustmentRequest.forSymbols("MEG", "BDO").getSymbols())
        .containsExactlyInAnyOrder("MEG", "BDO");
    assertThat(PriceAdjustmentRequest.forSymbol("MEG").getSymbols())
        .containsExactly("MEG");
    assertThat(PriceAdjustmentRequest.forSymbols(Lists.newArrayList("TEL", "MBT")).getSymbols())
        .containsExactlyInAnyOrder("TEL", "MBT");
  }
}
