package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.document.ValueDocument;
import com.bn.ninjatrader.model.request.FindRequest;
import com.bn.ninjatrader.model.request.SaveRequest;
import com.bn.ninjatrader.model.util.QueryParam;
import com.bn.ninjatrader.model.util.ValueDocumentSaver;
import com.bn.ninjatrader.model.util.ValueDocumentFinder;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
public abstract class AbstractValueDao<T extends Value>
    extends AbstractDateObjDao<ValueDocument, T>
    implements ValueDao<T> {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractValueDao.class);
  private static final String ENSURE_INDEX_QUERY = String.format(" { %s : 1, %s : 1, %s : 1, %s : 1} ",
      QueryParam.SYMBOL, QueryParam.TIMEFRAME, QueryParam.YEAR, QueryParam.PERIOD);

  private final ValueDocumentFinder valueDocumentFinder;
  private final ValueDocumentSaver valueDocumentSaver;

  public AbstractValueDao(final MongoCollection mongoCollection) {
    super(mongoCollection);
    valueDocumentFinder = new ValueDocumentFinder<T>(getMongoCollection(), getDocumentClass());
    valueDocumentSaver = new ValueDocumentSaver(getMongoCollection());
    ensureIndex();
  }

  private void ensureIndex() {
    getMongoCollection().ensureIndex(ENSURE_INDEX_QUERY, "{ unique: true }");
  }

  @Override
  public void save(final SaveRequest saveRequest) {
    valueDocumentSaver.save(saveRequest);
  }

  @Override
  public List<T> find(final FindRequest findRequest) {
    return valueDocumentFinder.find(findRequest);
  }

  public Class<? extends ValueDocument> getDocumentClass() {
    return ValueDocument.class;
  }
}
