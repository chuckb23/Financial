package com.financial.ingest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.financial.data.StockApplication;
import com.financial.data.StockDaily;
import com.financial.utils.FinancialHelper;
import com.financial.utils.FinancialsSQLHelper;

public class StockIngest {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	String apiQ = "_1Z4h9WjsswNztTb41zg";
	String apiKey = "ea3af990d47f88bd8ae48b5dade5f9b3";
	String getMarket = "http://marketdata.websol.barchart.com/getHistory.json?key=<APIKey>&symbol=BRS&type=daily&startDate=20150313000000";
	// String getMarket =
	//String getMarket = "http://marketdata.websol.barchart.com/getHistory.json?key=<APIKey>&symbol=<Symbol>&type=daily&startDate=<StartDate>";
	// Configure this
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
	FinancialHelper fp = new FinancialHelper();
	FinancialsSQLHelper fsh = new FinancialsSQLHelper();

	public StockIngest() {
		getQuote();
	}
	
	public StockIngest(String symbol, String startDate) {
		setUrl(symbol, startDate);
		getQuote();
	}

	//Does not work
	public StockIngest(String[] symbols, String startDate) {
		getMarket.replace("<StartDate>", startDate);
		for(int i = 0; i< symbols.length; i++){
			getMarket.replace("<Symbol>", symbols[i]);
			getQuote();
		}
		// 
	}
	
	private void setUrl(String symbol, String startDate){
		getMarket.replace("<Symbol>", symbol);
		getMarket.replace("<StartDate>", startDate);
	}

	public static void main(String[] args) {
		StockApplication sa = new StockApplication();
		StockDaily[] sd = sa.getDailyStock("IBM");
		StockIngest st = new StockIngest();
	}

	private void getQuote() {
		String stringResponse = fp.getResponseFromUrl(getMarket.replace("<APIKey>", apiKey));
		try {
			JSONObject jsonResponse = new JSONObject(stringResponse);
			JSONArray stocks = jsonResponse.getJSONArray("results");
			for (int i = 0; i < stocks.length(); i++) {
				JSONObject stock = stocks.getJSONObject(i);
				parseStock(stock);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseStock(JSONObject stock) {
		String symbol = stock.getString("symbol");
		double close = stock.getDouble("close");
		double open = stock.getDouble("open");
		String dateString = stock.getString("tradingDay");
		Date date = Date.valueOf(dateString);
		fsh.insertDailyStock(symbol, open, close, date);

	}

}
