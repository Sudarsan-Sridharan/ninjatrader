package com.bn.ninjatrader.model.request;

import com.bn.ninjatrader.model.entity.TradeAlgorithm;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author bradwee2000@gmail.com
 */
public class SaveTradeAlgorithmRequest {
  public static final SaveTradeAlgorithmRequest addEntity(final TradeAlgorithm algo) {
    return new SaveTradeAlgorithmRequest(algo);
  }
  public static final SaveTradeAlgorithmRequest addEntities(final Collection<TradeAlgorithm> algos) {
    return new SaveTradeAlgorithmRequest(algos);
  }
  public static final SaveTradeAlgorithmRequest addEntities(final TradeAlgorithm algo, final TradeAlgorithm ... more) {
    return new SaveTradeAlgorithmRequest(Lists.asList(algo, more));
  }

  private final List<TradeAlgorithm> tradeAlgorithms = Lists.newArrayList();

  private SaveTradeAlgorithmRequest(final TradeAlgorithm algo) {
    this.tradeAlgorithms.add(algo);
  }

  private SaveTradeAlgorithmRequest(final Collection<TradeAlgorithm> algos) {
    this.tradeAlgorithms.addAll(algos);
  }

  public List<TradeAlgorithm> getTradeAlgorithms() {
    return Lists.newArrayList(tradeAlgorithms);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final SaveTradeAlgorithmRequest that = (SaveTradeAlgorithmRequest) o;
    return Objects.equal(tradeAlgorithms, that.tradeAlgorithms);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tradeAlgorithms);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("tradeAlgorithms", tradeAlgorithms)
        .toString();
  }
}
