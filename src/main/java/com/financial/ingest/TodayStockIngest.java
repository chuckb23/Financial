package com.financial.ingest;

import java.text.SimpleDateFormat;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.financial.utils.FinancialHelper;
import com.financial.utils.FinancialsSQLHelper;

public class TodayStockIngest {
	 	CloseableHttpClient httpclient = HttpClients.createDefault();
		String apiKey = "ea3af990d47f88bd8ae48b5dade5f9b3";
		String getMarket = "http://marketdata.websol.barchart.com/getQuote.json?key=<APIKey>&symbols=ZC*1,IBM,GOOGL";
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssX");
		FinancialHelper fp = new FinancialHelper();
		FinancialsSQLHelper fsh = new FinancialsSQLHelper();
		private void setUrl(){
			//getMarket = getMarke
		}
		private void handle(){
			String stringResponse = fp.getResponseFromUrl(getMarket.replace("<APIKey>", apiKey));
			try {
				JSONObject jsonResponse = new JSONObject(stringResponse);
				JSONArray stocks = jsonResponse.getJSONArray("results");
				for(int i =0;i < stocks.length();i++){
					JSONObject stock = stocks.getJSONObject(i);
					parseStock(stock);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		private void parseStock(JSONObject stock){
			try {
				String symbol = stock.getString("symbol");
				String exchange = stock.getString("exchange");
				String name = stock.getString("name");
				String tradeString = stock.getString("tradeTimeStamp");
				String serverString = stock.getString("serverTimeStamp");
				double close = stock.getDouble("close");
				double open = stock.getDouble("open");
				double high = stock.getDouble("high");
				double low = stock.getDouble("low");
				double lastPrice = stock.getDouble("lastPrice");
				java.util.Date tradeDate = sdf.parse(tradeString);
				java.util.Date serverDate = sdf.parse(serverString);
				fsh.insertCurrentStock(symbol, lastPrice);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
}
