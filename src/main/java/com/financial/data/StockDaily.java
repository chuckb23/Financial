package com.financial.data;

import java.util.Date;

public class StockDaily extends Object {
	public String symbol;
	public double price = 0.0; 
	public double open = 0.0;
	public double close = 0.0;
	public double percentChange = 0.0;
	public Date tradingDay; 		
	
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	
	public void setClose(double close) {
		this.close = close;
	}
	public Date getTradingDay() {
		return tradingDay;
	}
	public void setTradingDay(Date tradingDay) {
		this.tradingDay = tradingDay;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPercentChange(){
		return percentChange;
	}
	public void setPercentChange(double percentChange){
		this.percentChange = percentChange; 
	}
	
	@Override
	public String toString() {
		return "StockDaily [symbol=" + symbol + ", price=" + price + ", open=" + open + ", close=" + close
				+ ", percentChange=" + percentChange + ", tradingDay=" + tradingDay + "]";
	}
}
