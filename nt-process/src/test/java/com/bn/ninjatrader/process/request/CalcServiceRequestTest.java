package com.bn.ninjatrader.process.request;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bradwee2000@gmail.com
 */
public class CalcServiceRequestTest {

  private final LocalDate from = LocalDate.of(2016, 2, 1);
  private final LocalDate to = LocalDate.of(2016, 2, 5);

  private final CalcServiceRequest orig = CalcServiceRequest.builder()
      .addProcessName("p1").addProcessNames("p2").addSymbol("MEG").addSymbol("BDO").from(from).to(to).build();
  private final CalcServiceRequest equal = CalcServiceRequest.builder()
      .addProcessNames("p1", "p2").addSymbols("MEG", "BDO").from(from).to(to).build();
  private final CalcServiceRequest diffFrom = CalcServiceRequest.builder().copyOf(orig).from(from.plusDays(1)).build();
  private final CalcServiceRequest diffTo = CalcServiceRequest.builder().copyOf(orig).to(to.plusDays(1)).build();
  private final CalcServiceRequest diffProcess = CalcServiceRequest.builder().copyOf(orig).addProcessName("p3").build();
  private final CalcServiceRequest diffSymbols = CalcServiceRequest.builder().copyOf(orig).addSymbol("TEL").build();
  private final CalcServiceRequest allSymbols = CalcServiceRequest.builder().copyOf(orig).allSymbols().build();
  private final CalcServiceRequest allProcess = CalcServiceRequest.builder().copyOf(orig).allProcesses().build();

  @Test
  public void testCreate_shouldSetProperties() {
    assertThat(orig.getProcessNames()).containsExactly("p1", "p2");
    assertThat(orig.getSymbols()).containsExactly("MEG", "BDO");
    assertThat(orig.getFrom()).hasValue(from);
    assertThat(orig.getTo()).hasValue(to);
    assertThat(orig.isAllProcesses()).isFalse();
    assertThat(orig.isAllSymbols()).isFalse();

    assertThat(allSymbols.isAllSymbols()).isTrue();
    assertThat(allSymbols.getSymbols()).isEmpty();

    assertThat(allProcess.isAllProcesses()).isTrue();
    assertThat(allProcess.getProcessNames()).isEmpty();
  }

  @Test
  public void testCreateEmpty_shouldSetDefaults() {
    final CalcServiceRequest req = CalcServiceRequest.builder().build();
    assertThat(req.getProcessNames()).isEmpty();
    assertThat(req.getSymbols()).isEmpty();
    assertThat(req.getFrom()).isEmpty();
    assertThat(req.getTo()).isEmpty();
    assertThat(req.isAllSymbols()).isFalse();
    assertThat(req.isAllProcesses()).isFalse();
  }

  @Test
  public void testEquals_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(equal).isEqualTo(orig);
    assertThat(orig).isEqualTo(orig).isEqualTo(equal)
        .isNotEqualTo(null)
        .isNotEqualTo("")
        .isNotEqualTo(diffProcess)
        .isNotEqualTo(diffSymbols)
        .isNotEqualTo(diffFrom)
        .isNotEqualTo(diffTo)
        .isNotEqualTo(allProcess)
        .isNotEqualTo(allSymbols);
  }

  @Test
  public void testHashcode_shouldBeEqualIfAllPropertiesAreEqual() {
    assertThat(Sets.newHashSet(orig, equal, diffProcess, diffSymbols, diffFrom, diffTo, allProcess, allSymbols))
        .containsExactlyInAnyOrder(orig, diffProcess, diffSymbols, diffFrom, diffTo, allProcess, allSymbols);
  }
}
