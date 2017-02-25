package com.bn.ninjatrader.dataimport.history.parser;

import com.bn.ninjatrader.model.entity.DailyQuote;

import java.util.List;

/**
 * Created by Brad on 4/28/16.
 */
public interface DataParser<T> {

  public List<DailyQuote> parse(T t) throws Exception;
}
