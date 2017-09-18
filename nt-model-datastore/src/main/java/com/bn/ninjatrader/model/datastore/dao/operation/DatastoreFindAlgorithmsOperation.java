package com.bn.ninjatrader.model.datastore.dao.operation;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.datastore.document.AlgorithmDocument;
import com.googlecode.objectify.cmd.Query;

import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * @author bradwee2000@gmail.com
 */
public class DatastoreFindAlgorithmsOperation implements AlgorithmDao.FindAlgorithmsOperation {

  private String userId;
  private Boolean isAutoScan;

  @Override
  public AlgorithmDao.FindAlgorithmsOperation withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  @Override
  public AlgorithmDao.FindAlgorithmsOperation isAutoScan(boolean isAutoScan) {
    this.isAutoScan = isAutoScan;
    return this;
  }

  @Override
  public List<Algorithm> now() {
    Query<AlgorithmDocument> query = ofy().load().type(AlgorithmDocument.class);

    if (userId != null) {
      query = query.filter("userId = ", userId);
    }

    if (isAutoScan != null) {
      query = query.filter("isAutoScan = ", isAutoScan);
    }

    final List<AlgorithmDocument> docs = query.list();

    return docs.stream()
        .map(doc -> doc.toAlgorithm())
        .collect(Collectors.toList());
  }
}
