package com.bn.ninjatrader.process.request;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author bradwee2000@gmail.com
 */
public class CalcServiceRequest {

  public static final Builder builder() {
    return new Builder();
  }

  private final List<String> processNames;
  private final List<String> symbols;
  private final LocalDate from;
  private final LocalDate to;
  private final boolean isAllSymbols;
  private final boolean isAllProcesses;

  private CalcServiceRequest(final List<String> processNames,
                             final List<String> symbols,
                             final LocalDate from,
                             final LocalDate to,
                             final boolean isAllProcesses,
                             final boolean isAllSymbols) {
    this.processNames = Collections.unmodifiableList(processNames);
    this.symbols = Collections.unmodifiableList(symbols);
    this.from = from;
    this.to = to;
    this.isAllProcesses = isAllProcesses;
    this.isAllSymbols = isAllSymbols;
  }

  public List<String> getProcessNames() {
    return processNames;
  }

  public List<String> getSymbols() {
    return symbols;
  }

  public Optional<LocalDate> getFrom() {
    return Optional.ofNullable(from);
  }

  public  Optional<LocalDate> getTo() {
    return Optional.ofNullable(to);
  }

  public boolean isAllProcesses() {
    return isAllProcesses;
  }

  public boolean isAllSymbols() {
    return isAllSymbols;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final CalcServiceRequest that = (CalcServiceRequest) o;
    return isAllSymbols == that.isAllSymbols &&
        isAllProcesses == that.isAllProcesses &&
        Objects.equal(processNames, that.processNames) &&
        Objects.equal(symbols, that.symbols) &&
        Objects.equal(from, that.from) &&
        Objects.equal(to, that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(processNames, symbols, from, to, isAllSymbols, isAllProcesses);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("processNames", processNames)
        .add("symbols", symbols)
        .add("from", from)
        .add("to", to)
        .add("isAllSymbols", isAllSymbols)
        .add("isAllProcesses", isAllProcesses)
        .toString();
  }

  /**
   * Builder class
   */
  public static final class Builder {
    private final List<String> processNames = Lists.newArrayList();
    private final List<String> symbols = Lists.newArrayList();
    private LocalDate from;
    private LocalDate to;
    private boolean isAllSymbols = false;
    private boolean isAllProcesses = false;

    public Builder copyOf(final CalcServiceRequest req) {
      this.processNames.clear();
      this.symbols.clear();
      this.processNames.addAll(req.getProcessNames());
      this.symbols.addAll(req.getSymbols());
      this.to = req.to;
      this.from = req.from;
      this.isAllProcesses = req.isAllProcesses;
      this.isAllSymbols = req.isAllSymbols;
      return this;
    }

    public Builder addProcessName(final String processName) {
      this.processNames.add(processName);
      return this;
    }

    public Builder addProcessNames(final Collection<String> processNames) {
      this.processNames.addAll(processNames);
      return this;
    }

    public Builder addProcessNames(final String processName, final String ... more) {
      this.processNames.addAll(Lists.asList(processName, more));
      return this;
    }

    public Builder addSymbol(final String symbol) {
      this.symbols.add(symbol);
      return this;
    }

    public Builder addSymbols(final Collection<String> symbols) {
      this.symbols.addAll(symbols);
      return this;
    }

    public Builder addSymbols(final String symbol, final String ... more) {
      symbols.addAll(Lists.asList(symbol, more));
      return this;
    }

    public Builder allSymbols() {
      this.isAllSymbols = true;
      symbols.clear();
      return this;
    }

    public Builder allProcesses() {
      this.isAllProcesses = true;
      processNames.clear();
      return this;
    }

    public Builder from(final LocalDate from) {
      this.from = from;
      return this;
    }

    public Builder to(final LocalDate to) {
      this.to = to;
      return this;
    }

    public CalcServiceRequest build() {
      return new CalcServiceRequest(processNames, symbols, from, to, isAllProcesses, isAllSymbols);
    }
  }
}
