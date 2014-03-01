package com.example.zapposalert;

import android.app.Application;
import android.content.Intent;

public class ZapposApplication extends Application {

	private ProductData productData;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		this.productData = new ProductData(this);
		
		Intent intent = new Intent("BROADCAST_ALARM");
	    sendBroadcast(intent) ;
	      
		
		
	}

	
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		
	}


	public ProductData getdatabase() {
		// TODO Auto-generated method stub
		return productData;
	}
	
	

}
