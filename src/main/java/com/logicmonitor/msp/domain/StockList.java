package com.logicmonitor.msp.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StockList {
  Map<String, Integer> stockList;
  public StockList() {
    stockList = Collections.synchronizedMap(new HashMap<String, Integer>());
  }
  public void remove(String symbol) {
    stockList.remove(symbol);
  }
  public int getOrDefault(String symbol, int i) {
    return stockList.getOrDefault(symbol, i);
  }
  public void put(String symbol, int i) {
    stockList.put(symbol, i);
  }
  public int size() {
    return stockList.size();
  }
  public String[] toArray(String[] strings) {
    int i = 0;
    for (Entry<String, Integer> entry : stockList.entrySet()) {
      String key = entry.getKey();
      strings[i] = key;
//      stockList.remove(); // avoids a ConcurrentModificationException
  }
    return strings;
  }
  public boolean contains(String string) {
    return stockList.containsKey(string);
  }
  public void add(String string) {
    stockList.put(string, 1);
  }
  public int get(String string) {
    return stockList.get(string);
  }

}
