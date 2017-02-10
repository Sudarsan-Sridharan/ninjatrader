package com.bn.ninjatrader.process.adjustment;

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
    final Operation adjustment = Operations.startWith(3);
    final PriceAdjustmentRequest request =
        PriceAdjustmentRequest.forSymbol("MEG").from(from).to(to).adjustment(adjustment);

    assertThat(request.getSymbol()).isEqualTo("MEG");
    assertThat(request.getFromDate()).isEqualTo(from);
    assertThat(request.getToDate()).isEqualTo(to);
    assertThat(request.getOperation()).isEqualTo(adjustment);
  }
}
