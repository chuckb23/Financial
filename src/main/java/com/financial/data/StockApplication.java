package com.financial.data;

import com.financial.utils.FinancialsSQLHelper;

public class StockApplication {
	public StockDaily[] getDailyStock(String symbol){
		FinancialsSQLHelper fh = new FinancialsSQLHelper();
		return fh.getAllDailyStockPrices(symbol);
		
	}
}
