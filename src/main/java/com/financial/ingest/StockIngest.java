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
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	private String apiQ = "_1Z4h9WjsswNztTb41zg";
	private String apiKey = "ea3af990d47f88bd8ae48b5dade5f9b3";
	//private String getMarket = "http://marketdata.websol.barchart.com/getHistory.json?key=<APIKey>&symbol=BRS&type=daily&startDate=20150313000000";
	// String getMarket =
	String getMarket = "http://marketdata.websol.barchart.com/getHistory.json?key=<APIKey>&symbol=<Symbol>&type=daily&startDate=<StartDate>";
	// Configure this
	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
	private FinancialHelper fp = new FinancialHelper();
	private FinancialsSQLHelper fsh = new FinancialsSQLHelper();

	public StockIngest() {
		getQuote();
	}
	
	public StockIngest(String symbol, String startDate) {
		setUrl(symbol, startDate);
		getQuote();
	}

	//Does not work
	public StockIngest(String[] symbols, String startDate) {
		getMarket = getMarket.replace("<StartDate>", startDate);
		for(int i = 0; i< symbols.length; i++){
			getMarket = getMarket.replace("<Symbol>", symbols[i]);
			getQuote();
		}
	}
	
	private void setUrl(String symbol, String startDate){
		getMarket = getMarket.replace("<Symbol>", symbol).replace("<StartDate>", startDate);
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
			System.out.println("Failure to insert fill quote for <STOCK>");
			e.printStackTrace();
		}
	}
	
	/* Parse the provided Json Object provided and 
	 * insert the dailyStock*/
	private void parseStock(JSONObject stock) {
		String symbol = stock.getString("symbol");
		double close = stock.getDouble("close");
		double open = stock.getDouble("open");
		String dateString = stock.getString("tradingDay");
		Date date = Date.valueOf(dateString);
		fsh.insertDailyStock(symbol, open, close, date);

	}

}
