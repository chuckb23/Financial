package com.financial.ingest;


import java.sql.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Class will get realtime and historical bitcoin price data and insert 
 * as a stock would be with symbol BTC
 */
public class BitcoinIngest extends BaseIngest {

	public BitcoinIngest() {
		super();
	}

	public BitcoinIngest(String startDate) {
		super(startDate);
	}


	@Override
	protected void parseThenInsertJsonPrice(JSONObject jsonPrice) {
		String symbol = "USD/BTC";
		try{
			long epochTime = jsonPrice.getLong("x")*1000;
			double price = jsonPrice.getDouble("y");
			Date date = new Date(epochTime);
			System.out.println(date.toString());
			fsh.insertDailyStock(symbol, price, price, date);
		} catch(Exception e){
			e.printStackTrace();
		}
	}


	@Override
	protected void ingestPrice(String url) {
		JSONObject jsonBTC = getJsonPriceObjectFromApi(url);
		JSONArray btc = jsonBTC.getJSONArray("values");
		for (int i = 0; i < btc.length(); i++) {
			JSONObject dailyBTC = btc.getJSONObject(i);
			parseThenInsertJsonPrice(dailyBTC);
		}
	}
	
	@Override
	protected void getConfig() {
		try {
			XMLConfiguration config = new XMLConfiguration("configs.xml");
			getMarket = config.getString("Bitcoin.url");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void setUrl(String startDate) {
		getMarket = getMarket.replace("<StartDate>", startDate);
	}
	
	
	/*
	 * Method stub for an ingest override that can run in the case 
	 * the application fails for multiple days and that information 
	 * needs to be backfilled
	 */
	private void historicalIngestOverride(){
		
	}

}
