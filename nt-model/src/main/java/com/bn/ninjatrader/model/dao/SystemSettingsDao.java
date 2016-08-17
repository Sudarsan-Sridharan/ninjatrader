package com.bn.ninjatrader.model.dao;

import com.bn.ninjatrader.common.data.SystemSetting;
import com.bn.ninjatrader.model.annotation.SettingsCollection;
import com.bn.ninjatrader.model.util.Queries;
import com.bn.ninjatrader.model.util.QueryParamName;
import com.google.inject.Inject;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Brad on 7/28/16.
 */
public class SystemSettingsDao extends AbstractDao<SystemSetting> {
  private static final Logger log = LoggerFactory.getLogger(SystemSettingsDao.class);

  @Inject
  public SystemSettingsDao(@SettingsCollection MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(
        String.format("{%s : 1}", QueryParamName.SETTING_NAME), "{unique: true}");
  }

  public Optional<SystemSetting> find(String name) {
    SystemSetting setting = getMongoCollection().findOne(Queries.FIND_SETTING_BY_NAME, name).as(SystemSetting.class);
    return Optional.ofNullable(setting);
  }

  public void save(SystemSetting setting) {
    setting.setLastUpdateDate(LocalDateTime.now().withNano(0));
    getMongoCollection().update(Queries.FIND_SETTING_BY_NAME, setting.getName()).upsert().with(setting);
  }
}
