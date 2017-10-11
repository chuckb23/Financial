package com.financial.data;

public class Stock {
	public String stockSymbol;
	public int countryId;
	
	
	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "Stocks [stockSymbol=" + stockSymbol + ", countryId=" + countryId + "]";
	}
	

}
