package com.logicmonitor.msp.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
  //  name 
  //  id
  //  token 
  //  password// md5
  private int id;
  private String username;  
  private String password;  
  private String email;  
  private String phone;  
  public List<String> watchlist;

  public User() {  
  }  
  public User( String username) {  
    this.username = username;  
  }
  public User( String username, String password) {  
    super();  
    this.username = username;  
    this.password = password;  
    this.watchlist = new ArrayList<>();
  }
  public User(int id, String username, String password, String email, String phone) {  
    super();  
    this.id = id;
    this.username = username;  
    this.password = password;  
    this.email = email;  
    this.phone = phone;  
    this.watchlist = new ArrayList<>();
  }
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  public String getUsername() {  
    return username;  
  }  

  public void setUsername(String username) {  
    this.username = username;  
  }  

  public String getPassword() {  
    return password;  
  }  

  public void setPassword(String password) {  
    this.password = password;  
  }  

  public String getEmail() {  
    return email;  
  }  

  public void setEmail(String email) {  
    this.email = email;  
  }  

  public String getPhone() {  
    return phone;  
  }  

  public void setPhone(String phone) {  
    this.phone = phone;  
  }  
  public void addStock (String stock) {
    watchlist.add(stock);
  }
  public void delStock (String stock) {
    watchlist.remove(stock);
  }

  @Override  
  public String toString() {  
    // TODO Auto-generated method stub  
    return "User [username=" + username + ",password=" + password  
        + ",email=" + email + ",phone" + phone + "]";  
  }  


}
