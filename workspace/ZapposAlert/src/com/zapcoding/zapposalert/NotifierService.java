package com.zapcoding.zapposalert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zapposalert.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class NotifierService extends IntentService {
  



public NotifierService() {
		super(TAG);
		// TODO Auto-generated constructor stub
		
	    Log.d(TAG, "UpdaterService constructed");

	}


private static final String TAG = "Notification Service";

 

  
  @Override
  protected void onHandleIntent(Intent inIntent) {
    
	  Log.d(TAG, "Trigger service");
	  Cursor cur = ((ZapposApplication)getApplication()).getdatabase().getProducts();
	  
	  cur.moveToFirst();
	  try {
	  while (cur.isAfterLast() == false) 
	  {
	      int product_id  = cur.getInt(0);
	      int style_id = cur.getInt(1);
	      int discount_cond = cur.getInt(6);
	      RestService rs = new RestService();
	      
	      JSONObject jsonObj;
		
	      
	      
	      String caller = "filters=";
	      String encodethis = "{\"productId\":[\""+product_id+"\"],"+ "\"styleId\":[\""+style_id+"\"]}";
	      
	      try {
			encodethis = URLEncoder.encode(encodethis,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      String jsonresult = rs.call(caller+encodethis);
	      jsonObj = new JSONObject(jsonresult);
	      if(jsonObj.getInt("statusCode") == 401)
	      {
	    	  CharSequence text = "Zappos API is not responding";

	    	  int duration = Toast.LENGTH_SHORT;

	    	  Toast toast = Toast.makeText(getApplicationContext(), text, duration);
	    	  toast.show();
	      }
		 
	      JSONArray results = jsonObj.getJSONArray("results");
		
		  int compareProductId = results.getJSONObject(0).getInt(SearchResultsActivity.TAG_ID);
		
	      
	      int compareStyleId = results.getJSONObject(0).getInt(SearchResultsActivity.TAG_STYLE_ID);
		
	      String discount = results.getJSONObject(0).getString(SearchResultsActivity.TAG_DISCOUNT);
	      String tokens[] = discount.split("%");
	        
	
	      
	      if(product_id == compareProductId && style_id == compareStyleId && Integer.parseInt(tokens[0]) >= 20 && discount_cond ==0)
	      {
	    	    discount_cond = 1;
	    	    
	    	    String product_name = results.getJSONObject(0).getString(SearchResultsActivity.TAG_NAME);
	    	    String original_price = results.getJSONObject(0).getString(SearchResultsActivity.TAG_ORIGINAL_PRICE);
	    	    String current_price = results.getJSONObject(0).getString(SearchResultsActivity.TAG_CURR_PRICE);
	    	    
	    	    ContentValues values = new ContentValues();
				
				values.put(ProductData.P_ID, product_id);
				values.put(ProductData.P_STYLE_ID, style_id);
				values.put(ProductData.P_NAME, product_name);
				values.put(ProductData.P_ORI_PRICE, original_price);
				values.put(ProductData.P_CURR_PRICE, current_price);
				values.put(ProductData.P_DISCOUNT, discount);
				values.put(ProductData.P_DISCOUNT_COND, discount_cond);
				
				((ZapposApplication)this.getApplication()).getdatabase().updateProducts(values, product_id, style_id);
				
	    	  
	    	  NotificationCompat.Builder mBuilder =
	  		        new NotificationCompat.Builder(this)
	  		        .setSmallIcon(R.drawable.ic_launcher)
	  		        .setContentTitle("Price notification")
	  		        .setContentText("Hello World!");
	  		// Creates an explicit intent for an Activity in your app
	  		Intent resultIntent = new Intent(this, NotifiedProductsActivity.class);

	  		// The stack builder object will contain an artificial back stack for the
	  		// started Activity.
	  		// This ensures that navigating backward from the Activity leads out of
	  		// your application to the Home screen.
	  		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	  		// Adds the back stack for the Intent (but not the Intent itself)
	  		stackBuilder.addParentStack(NotifiedProductsActivity.class);
	  		// Adds the Intent that starts the Activity to the top of the stack
	  		stackBuilder.addNextIntent(resultIntent);
	  		PendingIntent resultPendingIntent =
	  		        stackBuilder.getPendingIntent(
	  		            0,
	  		            PendingIntent.FLAG_UPDATE_CURRENT
	  		        );
	  		mBuilder.setContentIntent(resultPendingIntent);
	  		NotificationManager mNotificationManager =
	  		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	  		// mId allows you to update the notification later on.
	  		mNotificationManager.notify(0, mBuilder.build());
	      }
	    		  
	      
	      
	      cur.moveToNext();
	  }
	  
    

	  }
		catch(JSONException je)
		{
			je.printStackTrace();
		}
   
      // <5>
    }


  /**
   * Creates a notification in the notification bar telling user there are new
   * messages
   * 
   * @param timelineUpdateCount
   *          Number of new statuses
   */
  private void sendNotification(Cursor c) {
	  
	  
    
  }

  
  


@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}
}
