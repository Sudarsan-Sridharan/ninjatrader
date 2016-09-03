package com.bn.ninjatrader.process.util;

import com.bn.ninjatrader.model.document.PriceDocument;
import com.mongodb.DBCursor;
import org.jongo.MongoCursor;
import org.jongo.ResultHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Brad on 6/11/16.
 */
public class MockCursor extends MongoCursor<PriceDocument> {

  public MockCursor(DBCursor cursor, ResultHandler<PriceDocument> resultHandler) {
    super(cursor, resultHandler);
  }

  List<PriceDocument> priceDataList;
  Iterator<PriceDocument> iterator;
  boolean isClosed = false;

  public MockCursor(List<PriceDocument> priceDataList) {
    super(null, null);
    this.priceDataList = priceDataList;
    this.iterator = priceDataList.iterator();
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public Iterator<PriceDocument> iterator() {
    return iterator;
  }

  public Iterator<PriceDocument> getIterator() {
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
