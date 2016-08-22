package com.logicmonitor.msp.dao;

public interface JedisDao {
  public void set(String key, String value);
  public String get(String key);
  public void del(String key);

}
