package com.bn.ninjatrader.model.dao;

import org.jongo.MongoCollection;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Brad on 5/4/16.
 */
public class AbstractDaoTest {

  private MongoCollection collection = mock(MongoCollection.class);

  @Test
  public void testSave_shouldSaveToCollection() {
    AbstractDao dao = new DummyDao(collection);
    dao.save("Object");

    verify(collection).save("Object");
  }

  /**
   * Dummy test class
   */
  public static class DummyDao extends AbstractDao {

    public DummyDao(MongoCollection mongoCollection) {
      super(mongoCollection);
    }
  }
}
