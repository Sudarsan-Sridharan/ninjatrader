package com.bn.ninjatrader.model.document;

import com.bn.ninjatrader.common.data.Setting;
import com.bn.ninjatrader.model.util.QueryParamName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.List;

/**
 * Created by Brad on 6/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingDocument {

  @MongoId
  @MongoObjectId
  private String id;

  @JsonProperty(QueryParamName.OWNER)
  private String owner;

  @JsonProperty(QueryParamName.DATA)
  private List<Setting> settings = Lists.newArrayList();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public List<Setting> getSettings() {
    return settings;
  }

  public void setSettings(List<Setting> settings) {
    this.settings = settings;
  }
}
