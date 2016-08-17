package com.bn.ninjatrader.model.dao.period;

import com.bn.ninjatrader.common.data.Value;
import com.bn.ninjatrader.model.dao.AbstractDao;
import com.bn.ninjatrader.model.dao.ValueDao;
import com.bn.ninjatrader.model.util.QueryParamName;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Brad on 4/30/16.
 */
public abstract class AbstractPeriodDao extends AbstractDao<Value>  implements ValueDao {

  private static final Logger log = LoggerFactory.getLogger(AbstractPeriodDao.class);
  private static final String ENSURE_INDEX_QUERY = String.format(" { %s : 1, %s : 1, %s : 1} ",
      QueryParamName.SYMBOL, QueryParamName.YEAR, QueryParamName.PERIOD);

  private PeriodDataFinder periodDataFinder;
  private PeriodDataSaver periodDataSaver;

  public AbstractPeriodDao(MongoCollection mongoCollection) {
    super(mongoCollection);
    periodDataFinder = new PeriodDataFinder(mongoCollection);
    periodDataSaver = new PeriodDataSaver(mongoCollection);
    ensureIndex();
  }

  private void ensureIndex() {
    getMongoCollection().ensureIndex(ENSURE_INDEX_QUERY, "{ unique: true }");
  }

  public void save(SaveRequest saveRequest) {
    periodDataSaver.save(saveRequest);
  }

  public List<Value> find(FindRequest findRequest) {
    return periodDataFinder.find(findRequest);
  }
}
