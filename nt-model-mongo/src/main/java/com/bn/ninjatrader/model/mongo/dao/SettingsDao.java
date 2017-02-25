package com.bn.ninjatrader.model.mongo.dao;

import com.bn.ninjatrader.model.deprecated.Setting;
import com.bn.ninjatrader.model.mongo.annotation.SettingsCollection;
import com.bn.ninjatrader.model.mongo.document.SettingDocument;
import com.bn.ninjatrader.model.mongo.util.Queries;
import com.bn.ninjatrader.model.mongo.util.QueryParam;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brad on 7/28/16.
 */
public class SettingsDao extends AbstractDao {
  private static final Logger LOG = LoggerFactory.getLogger(SettingsDao.class);

  @Inject
  public SettingsDao(@SettingsCollection MongoCollection mongoCollection) {
    super(mongoCollection);
    mongoCollection.ensureIndex(
        String.format("{%s : 1}", QueryParam.USER), "{unique: true}");
  }

  public List<Setting> find(String owner) {
    SettingDocument settingData = getMongoCollection().findOne(Queries.FIND_BY_USER, owner).as(SettingDocument.class);
    if (settingData == null) {
      return Collections.emptyList();
    }
    return settingData.getSettings();
  }

  public void save(String owner, Setting setting) {
    save(owner, Lists.newArrayList(setting));
  }

  public void save(String owner, Setting setting1, Setting setting2) {
    save(owner, Lists.newArrayList(setting1, setting2));
  }

  public void save(String owner, Setting setting1, Setting setting2, Setting ... moreSettings) {
    List<Setting> settings = Lists.newArrayList(setting1, setting2);
    settings.addAll(Arrays.asList(moreSettings));
    save(owner, settings);
  }

  public void save(String owner, List<Setting> settings) {
    LocalDateTime now = LocalDateTime.now();
    List<String> names = Lists.newArrayList();
    for (Setting setting : settings) {
      setting.setLastUpdateDate(now);
      names.add(setting.getName());
    }

    removeByNames(owner, names);

    getMongoCollection().update(Queries.FIND_BY_USER, owner)
        .upsert()
        .with("{$push: { data: { $each: #, $sort: { n: 1}}}}", settings);
  }

  public void removeByNames(String owner, List<String> names) {
    if (!names.isEmpty()) {
      getMongoCollection().update(Queries.FIND_BY_USER, owner).multi()
          .with("{$pull: {data :{n: {$in: #}}}}", names);
    }
  }
}
