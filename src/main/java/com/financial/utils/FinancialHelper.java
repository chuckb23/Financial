package com.financial.utils;

import java.io.BufferedReader;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class FinancialHelper {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpGet get = null;
	InputStream is = null; 
	BufferedReader rd = null;
	
	public String getResponseFromUrl(String url){
		String stringResponse = null;
		get = new HttpGet(url);
		//Check our response code and add retry handling here
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if( statusCode == 200){
				HttpEntity ent = response.getEntity();
				stringResponse = EntityUtils.toString(ent);
			}else{
				throw new Exception("Error response code " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringResponse;
	}
	
	
}
