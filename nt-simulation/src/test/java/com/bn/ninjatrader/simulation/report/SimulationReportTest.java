package com.bn.ninjatrader.simulation.report;

import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.core.SimulationParams;
import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.TradeStatistic;
import com.bn.ninjatrader.simulation.transaction.BuyTransaction;
import com.bn.ninjatrader.simulation.transaction.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class SimulationReportTest {

  private final SimulationParams params = SimulationParams.builder().symbol("MEG").build();
  private final TradeStatistic tradeStatistic = new TradeStatistic();
  private final Transaction txn = BuyTransaction.buy().symbol("MEG").build();
  private final Mark mark = Mark.onDate(LocalDate.of(2016, 2, 1));

  private final SimulationReport report = SimulationReport.builder()
      .params(params)
      .tradeStatistics(tradeStatistic)
      .startingCash(10000)
      .endingCash(20000)
      .addTransaction(txn)
      .addMark(mark)
      .build();

  @Test
  public void testCreate_shouldSetProperties() {


    assertThat(report.getStartingCash()).isEqualTo(10000d);
    assertThat(report.getEndingCash()).isEqualTo(20000d);
    assertThat(report.getSimulationParams()).isEqualTo(params);
    assertThat(report.getTradeStatistic()).isEqualTo(tradeStatistic);
    assertThat(report.getTransactions()).containsExactly(txn);
    assertThat(report.getMarks()).containsExactly(mark);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(report);
    assertThat(om.readValue(json, SimulationReport.class)).isEqualTo(report);
  }
}
