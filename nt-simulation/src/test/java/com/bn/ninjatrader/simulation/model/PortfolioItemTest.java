package com.bn.ninjatrader.simulation.model;

import com.bn.ninjatrader.common.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author bradwee2000@gmail.com
 */
public class PortfolioItemTest {

  private static PortfolioItem orig = PortfolioItem.builder()
      .symbol("MEG").totalShares(1000).committedShares(1000).avgPrice(3.5).build();
  private static PortfolioItem equal = PortfolioItem.builder()
      .symbol("MEG").totalShares(1000).committedShares(1000).avgPrice(3.5).build();
  private static PortfolioItem diffSym = PortfolioItem.builder()
      .symbol("BDO").totalShares(1000).committedShares(1000).avgPrice(3.5).build();
  private static PortfolioItem diffShares = PortfolioItem.builder()
      .symbol("MEG").totalShares(500).committedShares(1000).avgPrice(3.5).build();
  private static PortfolioItem diffUncommittedShares = PortfolioItem.builder()
      .symbol("MEG").totalShares(1000).committedShares(500).avgPrice(3.5).build();
  private static PortfolioItem diffPrice = PortfolioItem.builder()
      .symbol("MEG").totalShares(1000).committedShares(1000).avgPrice(3.61).build();

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getSymbol()).isEqualTo("MEG");
    assertThat(orig.getTotalShares()).isEqualTo(1000);
    assertThat(orig.getCommittedShares()).isEqualTo(1000);
    assertThat(orig.getAvgPrice()).isEqualTo(3.5);
  }

  @Test
  public void testAddShares_shouldAddTotalAndAdjustAveragePrice() {
    assertThat(PortfolioItem.builder().build().addSharesWithPrice(1000, 3.51).getTotalShares()).isEqualTo(1000);
    assertThat(PortfolioItem.builder().build().addSharesWithPrice(1000, 3.51).getAvgPrice()).isEqualTo(3.51);

    assertThat(orig.addSharesWithPrice(1000, 3.0).getTotalShares()).isEqualTo(2000);
    assertThat(orig.addSharesWithPrice(1000, 3.0).getAvgPrice()).isEqualTo(3.25);
  }

  @Test
  public void testCommitShares_shouldAddToCommittedShares() {
    final PortfolioItem portItem = PortfolioItem.builder()
        .symbol("MEG").totalShares(1000).committedShares(0).avgPrice(3.5).build();

    assertThat(portItem.commitShares(300).getTotalShares()).isEqualTo(1000);
    assertThat(portItem.commitShares(300).getCommittedShares()).isEqualTo(300);
    assertThat(portItem.commitShares(300).commitShares(700).getCommittedShares()).isEqualTo(1000);

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {
      portItem.commitShares(1001);
    });
  }

  @Test
  public void testCanCommitShares_shouldReturnTrueIfNumOfSharesLessThanTotalUncommittedShares() {
    final PortfolioItem portItem = PortfolioItem.builder()
        .symbol("MEG").totalShares(1000).committedShares(0).avgPrice(3.5).build();

    assertThat(portItem.canCommitShares(1000)).isTrue();
    assertThat(portItem.canCommitShares(1)).isTrue();
    assertThat(portItem.canCommitShares(1001)).isFalse();
  }

  @Test
  public void testCancelCommittedShares_shouldDeductFromCommittedShares() {
    assertThat(orig.cancelCommittedShares(1000).getTotalShares()).isEqualTo(1000);
    assertThat(orig.cancelCommittedShares(1000).getCommittedShares()).isEqualTo(0);
    assertThat(orig.cancelCommittedShares(100).cancelCommittedShares(400).getCommittedShares()).isEqualTo(500);
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {
      orig.cancelCommittedShares(1001);
    });
  }

  @Test
  public void testFulfillCommittedShares_shouldDeductFromTotalShares() {
    assertThat(orig.fulfillCommittedShares(1000).getTotalShares()).isEqualTo(0);
    assertThat(orig.fulfillCommittedShares(1000).getCommittedShares()).isEqualTo(0);
    assertThat(orig.fulfillCommittedShares(500).getTotalShares()).isEqualTo(500);
    assertThat(orig.fulfillCommittedShares(500).getCommittedShares()).isEqualTo(500);
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {
      orig.fulfillCommittedShares(1001);
    });
  }

  @Test
  public void testEquityValue_shouldReturnTotalEquityValue() {
    assertThat(orig.getEquityValue()).isEqualTo(3500);
    assertThat(diffPrice.getEquityValue()).isEqualTo(3610);
  }

  @Test
  public void testEqual_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffSym)
        .isNotEqualTo(diffPrice)
        .isNotEqualTo(diffShares)
        .isNotEqualTo(diffUncommittedShares);
  }

  @Test
  public void testHashcode_shouldHaveEqualHashcodeIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffSym, diffPrice, diffShares, diffUncommittedShares))
        .containsExactlyInAnyOrder(orig, diffSym, diffPrice, diffShares, diffUncommittedShares);
  }

  @Test
  public void testSerializeDeserialize_shouldProduceEqualObject() throws IOException {
    final ObjectMapper om = TestUtil.objectMapper();
    final String json = om.writeValueAsString(orig);
    assertThat(om.readValue(json, PortfolioItem.class)).isEqualTo(orig);
  }
}
