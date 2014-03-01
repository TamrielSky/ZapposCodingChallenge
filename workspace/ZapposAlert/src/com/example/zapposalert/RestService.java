package com.example.zapposalert;


import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;

import java.io.IOException;
import java.net.*;

import org.apache.commons.*;
import org.apache.http.impl.client.*;	
import org.apache.http.util.EntityUtils;


public class RestService {
	
	private static final String API_KEY = "b05dcd698e5ca2eab4a0cd1eee4117e7db2a10c4";
	private static final String URL = "http://api.zappos.com/";

	public String call(String query)
	{
		HttpResponse httpResponse = null;
        String response = null;  
		try {
			// http client
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			
			
			String requestUrl = URL+"Search?"+query+"&key="+API_KEY;
       
			
				HttpGet httpGet = new HttpGet(requestUrl);

			httpResponse = httpClient.execute(httpGet);

			
			httpEntity = httpResponse.getEntity();
	        response = EntityUtils.toString(httpEntity);



		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;


	}
}


