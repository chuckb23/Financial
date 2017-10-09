package com.financial.ingest;

public class Messenger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockIngest stockMessage = new StockIngest("20150313000000");
		BitcoinIngest bitcoinMessage = new BitcoinIngest();
		//bitcoinMessage.ingestPrice("20150313000000");
		//stockMessage.ingestAll();
		//stockMessage.getQuote();

	}

}
