package com.bn.ninjatrader.simulation.scanner;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Scan stocks using an algorithm and returns result for each symbol.
 *
 * @author bradwee2000@gmail.com
 */
public interface StockScanner {

  Map<String, ScanResult> scan(final ScanRequest req);

  void scanAsync(final ScanRequest req, Consumer<Map<String, ScanResult>> callback);
}
