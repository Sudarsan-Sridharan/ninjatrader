package com.bn.ninjatrader.simulation.scanner;

import java.util.Map;

/**
 * Scan stocks using an algorithm and returns result for each symbol.
 *
 * @author bradwee2000@gmail.com
 */
public interface StockScanner {

  Map<String, ScanResult> scan(final ScanRequest req);
}
