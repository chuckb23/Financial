package com.financial.utils;

import java.awt.List;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.financial.data.StockDaily;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class FinancialsSQLHelper {
	
	static final String url = "jdbc:mysql://localhost:3306/financials";
    static final String username = "root";
    static final String password = "Mchuckb23";
    
	public static Connection getConnection() throws Exception {
		//Configure this elsewhere
		//System.out.println("Latest has been picked up");
		Class.forName("com.mysql.jdbc.Driver");
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	  }
	
	public static ResultSet getResultSet(String query, String symbol){
		Connection conn;
		ResultSet rs = null;
		try {
			conn = getConnection();
			CallableStatement cs = conn.prepareCall(query);
			cs.setString(1, symbol);
			rs = cs.executeQuery();
			//conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs; 
	}
	
	public void insertDailyStock (String symbol, double open, double close, Date tradingDay){
		try {
			Connection conn = getConnection();
			String query = "CALL spInsertDailyStockPrice(?,?,?,?)";
			CallableStatement cs = conn.prepareCall(query);
			cs.setString(1,symbol);
			cs.setDouble(2, open);
			cs.setDouble(3, close);
			cs.setDate(4, tradingDay);
			cs.executeQuery();
			cs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void insertCurrentStock (String symbol, double price){
		try {
			Connection conn = getConnection();
			String query = "CALL spInsertStockCurrent(?,?)";
			CallableStatement cs = conn.prepareCall(query);
			cs.setString(1,symbol);
			cs.setDouble(2, price);
			cs.executeQuery();
			cs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*public Stock getStocks(){
		try {
			Connection conn = getConnection();
			String query = "CALL spGetStocks()";
			CallableStatement cs = conn.prepareCall(query);
			ResultSet rs = cs.executeQuery();
			while(rs.next()){
				rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	public StockDaily[] getDailyStock(String symbol){
		//getDailyStockMongo(symbol);
		ResultSet rs = getResultSet("CALL spGetStockDaily(?)",symbol);
		try{
			//need to either switch to a list or get rows 
			ArrayList<StockDaily> stocks = new ArrayList<StockDaily>();
			while(rs.next()){
				StockDaily stock = new StockDaily();
				stock.setOpen(rs.getDouble(2));
				stock.setClose(rs.getDouble(3));
				stock.setTradingDay(rs.getDate(4));
				stock.setSymbol(rs.getString(6));
				stock.setPercentChange(rs.getDouble(7));
				stocks.add(stock);
			}
			StockDaily[] stockArray = new StockDaily[stocks.size()];
			stocks.toArray(stockArray);
			return stockArray;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public StockDaily[] getDailyStockMongo(String symbol){
		try {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("financial");
			DBCollection collection = database.getCollection("dailyStockPrice");
			DBObject query = new BasicDBObject("item", "BRS");
			DBCursor cursor = collection.find(query);
			//DBObject cursor = collection.findOne();
			//System.out.println(cursor);
			double price = (double)cursor.next().get("price");
			System.out.println(price);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
