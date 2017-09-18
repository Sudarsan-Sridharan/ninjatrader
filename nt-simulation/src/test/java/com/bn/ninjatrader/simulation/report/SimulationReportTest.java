package com.bn.ninjatrader.simulation.report;

import com.bn.ninjatrader.model.util.TestUtil;
import com.bn.ninjatrader.simulation.model.Mark;
import com.bn.ninjatrader.simulation.model.stat.DefaultTradeStatistic;
import com.bn.ninjatrader.simulation.model.stat.TradeStatistic;
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

  private final TradeStatistic tradeStatistic = new DefaultTradeStatistic();
  private final Transaction txn = BuyTransaction.buy().symbol("MEG").build();
  private final Mark mark = Mark.onDate(LocalDate.of(2016, 2, 1));

  private final SimulationReport report = SimulationReport.builder()
      .symbol("MEG")
      .tradeStatistics(tradeStatistic)
      .startingCash(10000)
      .endingCash(20000)
      .profit(10000)
      .profitPcnt(0.50)
      .addTransaction(txn)
      .addMark(mark)
      .build();

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(report.getStartingCash()).isEqualTo(10000d);
    assertThat(report.getEndingCash()).isEqualTo(20000d);
    assertThat(report.getProfit()).isEqualTo(10000d);
    assertThat(report.getProfitPcnt()).isEqualTo(0.5);
    assertThat(report.getSymbol()).isEqualTo("MEG");
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
