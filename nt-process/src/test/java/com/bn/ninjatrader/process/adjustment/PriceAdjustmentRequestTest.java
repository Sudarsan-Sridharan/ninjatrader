package com.bn.ninjatrader.process.adjustment;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bradwee2000@gmail.com
 */
public class PriceAdjustmentRequestTest {

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 10);
  private PriceAdjustmentService service;

  @Before
  public void before() {
    service = mock(PriceAdjustmentService.class);
  }

  @Test
  public void testBuild_shouldSetProperties() {
    final PriceAdjustmentRequest request = PriceAdjustmentRequest.builder()
        .symbol("MEG").from(from).to(to).script("script").build();

    assertThat(request.getSymbol()).isEqualTo("MEG");
    assertThat(request.getFrom()).isEqualTo(from);
    assertThat(request.getTo()).isEqualTo(to);
    assertThat(request.getScript()).isEqualTo("script");
  }

  @Test
  public void testExecutorBuilder_shouldExecuteOnService() {
    final PriceAdjustmentRequest.ExecutorBuilder builder = new PriceAdjustmentRequest.ExecutorBuilder(service)
        .symbol("MEG").from(from).to(to).script("script");

    builder.execute();

    verify(service).adjustPrices(builder.build());
  }
}
