package com.bn.ninjatrader.process.util;

import com.bn.ninjatrader.model.data.PriceData;
import com.mongodb.DBCursor;
import org.jongo.MongoCursor;
import org.jongo.ResultHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 6/11/16.
 */
public class MockCursor extends MongoCursor<PriceData> {

  public MockCursor(DBCursor cursor, ResultHandler<PriceData> resultHandler) {
    super(cursor, resultHandler);
  }

  List<PriceData> priceDataList;
  Iterator<PriceData> iterator;
  boolean isClosed = false;

  public MockCursor(List<PriceData> priceDataList) {
    super(null, null);
    this.priceDataList = priceDataList;
    this.iterator = priceDataList.iterator();
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public Iterator<PriceData> iterator() {
    return iterator;
  }

  public Iterator<PriceData> getIterator() {
    return iterator;
  }

  @Override
  public void close() throws IOException {
    isClosed = true;
  }

  public boolean isClosed() {
    return isClosed;
  }
}
