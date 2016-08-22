package com.logicmonitor.msp.dao.impl;

//import java.net.URI;
//import java.net.URISyntaxException;

import com.logicmonitor.msp.dao.JedisDao;
import com.logicmonitor.msp.dao.RedisUtil;

import redis.clients.jedis.Jedis;

public class JedisDaoImpl implements JedisDao{
  //  private static Jedis getConnection() throws URISyntaxException {
  ////    URI redisURI = new URI(System.getenv("REDIS_URL"));
  ////    Jedis jedis = new Jedis(redisURI);
  //    Jedis jedis = new Jedis("localhost");
  //    System.out.println("Connection to server sucessfully");
  //    return jedis;
  //}
  private Jedis jedis = null;
  public void set(String key, String value) {
    Jedis jedis = null;
      jedis = RedisUtil.getJedis();
      jedis.set(key, value);
      RedisUtil.returnResource(jedis);
    }

  public String get(String key) {
    Jedis jedis = null;
    String value = null;

    jedis = RedisUtil.getJedis();
    value = jedis.get(key);
    RedisUtil.returnResource(jedis);
    return value;
  }

  public void del(String key) {
    Jedis jedis = null;
    jedis = RedisUtil.getJedis();
    jedis.del(key);
    RedisUtil.returnResource(jedis);
  }
}
