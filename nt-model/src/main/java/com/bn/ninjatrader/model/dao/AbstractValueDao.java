package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.util.VaueDocumentFinder;
import com.bn.ninjatrader.model.util.ValueDocumentSaver;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.model.document.ValueDocument;
import com.bn.ninjatrader.model.util.QueryParamName;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
public abstract class AbstractValueDao<T extends Value> extends AbstractDao<T> implements ValueDao<T> {

  private static final Logger log = LoggerFactory.getLogger(AbstractValueDao.class);
  private static final String ENSURE_INDEX_QUERY = String.format(" { %s : 1, %s : 1, %s : 1} ",
      QueryParamName.SYMBOL, QueryParamName.YEAR, QueryParamName.PERIOD);

  private VaueDocumentFinder valueDocumentFinder;
  private ValueDocumentSaver valueDocumentSaver;

  public AbstractValueDao(MongoCollection mongoCollection) {
    super(mongoCollection);
    valueDocumentFinder = new VaueDocumentFinder<T>(mongoCollection, getDocumentClass());
    valueDocumentSaver = new ValueDocumentSaver(mongoCollection);
    ensureIndex();
  }

  private void ensureIndex() {
    getMongoCollection().ensureIndex(ENSURE_INDEX_QUERY, "{ unique: true }");
  }

  @Override
  public void save(SaveRequest saveRequest) {
    valueDocumentSaver.save(saveRequest);
  }

  @Override
  public List<T> find(FindRequest findRequest) {
    return valueDocumentFinder.find(findRequest);
  }

  public Class<? extends ValueDocument> getDocumentClass() {
    return ValueDocument.class;
  }
}
