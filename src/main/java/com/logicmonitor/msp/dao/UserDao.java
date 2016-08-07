package com.logicmonitor.msp.dao;

import java.util.List;

import com.logicmonitor.msp.domain.User;

public interface UserDao {
//  public abstract String getUserPassword(String username);  
  public abstract User findUserByAccountAndPsw(String username, String password);
  public abstract void getUserWatchList(String username, List<String> watchlist);  
  public abstract void addOneToWatchList(String username, String symbol);
  public abstract void delOneFromWatchList(String username, String symbol);
//  public abstract void regist(User user);  //add user
}
