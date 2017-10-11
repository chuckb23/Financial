package com.financial.ingest;

import java.sql.Date;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.financial.data.Stock;

public class StockIngest extends BaseIngest {

	public StockIngest() {
		super();
		//This will be replaced to just grab the current date and use that as the startDate but for now just 
		//since I dont really care
		throw new UnsupportedOperationException("Error must pass in a startDate to initialize");
	}
	
	public StockIngest(String startDate) {
		super(startDate);
	}
	
	public StockIngest(String symbol, String startDate) {
		super(symbol,startDate);
		setUrl(symbol, startDate);
	}
	
	protected void setUrl(String symbol, String startDate){
		getMarket = getMarket.replace("<Symbol>", symbol).replace("<StartDate>", startDate).replace("<APIKey>", apiKey);
	}
	
	//Only will have one apiKey and one startDate for a run
	//No matter how many symbols you process
	protected void setUrl(String startDate){
		getMarket = getMarket.replace("<StartDate>", startDate).replace("<APIKey>", apiKey);
	}
	
	/* Parse the provided Json Object provided and 
	 * insert the dailyStock*/
	protected void parseThenInsertJsonPrice(JSONObject stock) {
		try{
			String symbol = stock.getString("symbol");
			double close = stock.getDouble("close");
			double open = stock.getDouble("open");
			String dateString = stock.getString("tradingDay");
			Date date = Date.valueOf(dateString);
			fsh.insertDailyStock(symbol, open, close, date);
		} catch(JSONException e) {
			System.out.println("Error::StockIngest::parseStock::Failed to parse JSON response");
			e.printStackTrace();
		} catch(IllegalArgumentException e1){
			System.out.println("Error::StockIngest::parseStock::Incorrect Date Format");
			e1.printStackTrace();
		}

	}
	protected void ingestPrice(String startDate){
		JSONArray stocks = getJsonPriceObjectFromApi(getMarket).getJSONArray("results");
		for (int i = 0; i < stocks.length(); i++) {
			JSONObject stock = stocks.getJSONObject(i);
			parseThenInsertJsonPrice(stock);
		}
	}
	
	protected void ingestAll(){
		long startTime = System.currentTimeMillis();
		ArrayList<Stock> symbols = fsh.getStocks();
		for(Stock stock : symbols){
			JSONArray stocks = getJsonPriceObjectFromApi(getMarket.replace("<Symbol>", stock.getStockSymbol())).getJSONArray("results");
			for (int i = 0; i < stocks.length(); i++) {
				JSONObject jsonStock = stocks.getJSONObject(i);
				parseThenInsertJsonPrice(jsonStock);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}
	
	protected void getConfig() {
		try {
			XMLConfiguration config = new XMLConfiguration("configs.xml");
			getMarket = config.getString("Stock.url");
			apiKey = config.getString("Stock.apiKey");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
