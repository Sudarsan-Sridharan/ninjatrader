//package com.bn.ninjatrader.process.util;
//
//import com.bn.ninjatrader.model.mongo.document.PriceMongoDocument;
//import com.mongodb.DBCursor;
//import org.jongo.MongoCursor;
//import org.jongo.ResultHandler;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * Created by Brad on 6/11/16.
// */
//public class MockCursor extends MongoCursor<PriceMongoDocument> {
//
//  public MockCursor(DBCursor cursor, ResultHandler<PriceMongoDocument> resultHandler) {
//    super(cursor, resultHandler);
//  }
//
//  List<PriceMongoDocument> priceDataList;
//  Iterator<PriceMongoDocument> iterator;
//  boolean isClosed = false;
//
//  public MockCursor(List<PriceMongoDocument> priceDataList) {
//    super(null, null);
//    this.priceDataList = priceDataList;
//    this.iterator = priceDataList.iterator();
//  }
//
//  @Override
//  public boolean hasNext() {
//    return iterator.hasNext();
//  }
//
//  @Override
//  public Iterator<PriceMongoDocument> iterator() {
//    return iterator;
//  }
//
//  public Iterator<PriceMongoDocument> getIterator() {
//    return iterator;
//  }
//
//  @Override
//  public void close() throws IOException {
//    isClosed = true;
//  }
//
//  public boolean isClosed() {
//    return isClosed;
//  }
//}
