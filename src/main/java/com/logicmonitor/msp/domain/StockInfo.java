package com.logicmonitor.msp.domain;
/*
 * class to maintain static information of one stock
 */
public class StockInfo {
	String symbol = ""; 
	String name = "";
	String currency = "USD";
	String stockExchange = "";

	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getStockExchange() {
		return stockExchange;
	}
	
	public void setStockExchange(String stockExchange) {
		this.stockExchange = stockExchange;
	}
	
	public String toString() {
		return symbol + " " + name + " " + currency + " " + stockExchange;	
	}

}
