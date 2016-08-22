package com.logicmonitor.msp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logicmonitor.msp.dao.UserDao;
import com.logicmonitor.msp.dao.impl.UserDaoImpl;
import com.logicmonitor.msp.domain.User;

public class UserService {
  private static UserService instance = null;
  UserDao udi;
  public Map<String, User> userList;
  private UserService() {
    udi = new UserDaoImpl();
    userList = new HashMap<String, User>();
  }
  public static UserService getInstance() {
    if (instance == null) {
        synchronized (UserService.class) {
            if (instance == null) {
                instance = new UserService();
            }
        }
    }
    return instance;
}
  public User checkLogin(String username, String password) {
    return udi.findUserByAccountAndPsw(username, password);
  }
  public void logout(String username) {
    userList.remove(username);
  }
  

  public void addOneToWatchList(String username, String symbol) {
    User curUser = userList.get(username);
    curUser.watchlist.add(symbol);
    udi.addOneToWatchList(curUser.getUsername(), symbol);
  }
  public void delOneFromWatchList(String username, String symbol) {
    User curUser = userList.get(username);
    curUser.watchlist.remove(symbol);
    udi.delOneFromWatchList(curUser.getUsername(), symbol);
  }
  public void getUserWatchList(String username, List<String> watchlist) {
    User curUser = userList.get(username);
    udi.getUserWatchList(username, watchlist);
    curUser.watchlist.addAll(watchlist);
  }
  
}
