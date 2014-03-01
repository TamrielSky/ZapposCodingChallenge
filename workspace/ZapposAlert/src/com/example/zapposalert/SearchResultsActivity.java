package com.example.zapposalert;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsActivity extends Activity {

	public static final String TAG_ID 	= "productId";
	public static final String TAG_STYLE_ID = "styleId";
	public static final String TAG_RESULTS = "results";
	public static final String TAG_NAME = "productName";
	public static final String TAG_ORIGINAL_PRICE = "originalPrice";
	public static final String TAG_CURR_PRICE = "price";
	public static final String TAG_DISCOUNT = "percentOff";
	ListAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.search_main);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		
		handleIntent(getIntent());
		
		

	}
	
	
		
		
	
		public void onClickSaveButton(View v) {
			// TODO Auto-generated method stub
		
			RelativeLayout row = (RelativeLayout)v.getParent();
			

			TextView tvid = (TextView) row.findViewById(R.id.product_id);
			TextView tvstyleid = (TextView) row.findViewById(R.id.styleid);
			TextView tvname = (TextView) row.findViewById(R.id.product_name);
			TextView tvorprice = (TextView) row.findViewById(R.id.original_price);
			TextView tvcurrprice = (TextView) row.findViewById(R.id.curr_price);
			TextView tvperdiscount = (TextView) row.findViewById(R.id.per_discount);
			
			
			String product_name = tvname.getText().toString();
			int product_id = Integer.parseInt(tvid.getText().toString());
			int style_id = Integer.parseInt(tvstyleid.getText().toString());
			String original_price =tvorprice.getText().toString();
			String current_price = tvcurrprice.getText().toString();
			String per_discount = tvperdiscount.getText().toString();
            String tokens[] = per_discount.split("%");
	        int discount_cond=0;
			if(Integer.parseInt(tokens[0]) >= 20)
	        	 discount_cond = 1;
	        
			ContentValues values = new ContentValues();
			
			values.put(ProductData.P_ID, product_id);
			values.put(ProductData.P_STYLE_ID, style_id);
			values.put(ProductData.P_NAME, product_name);
			values.put(ProductData.P_ORI_PRICE, original_price);
			values.put(ProductData.P_CURR_PRICE, current_price);
			values.put(ProductData.P_DISCOUNT, per_discount);
			values.put(ProductData.P_DISCOUNT_COND, discount_cond);
			((ZapposApplication)this.getApplication()).getdatabase().insert(values);
			
			
		}

	


	@Override
	protected void onNewIntent(Intent intent) {

		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {



		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			
			String query = intent.getStringExtra(SearchManager.QUERY);

			new GetProductList().execute(query);

			//use the query to search your data somehow
		}
	}



	class GetProductList extends AsyncTask<String, Integer, String> {
		// Called to initiate the background activity
		@Override
		protected String doInBackground(String... query) {
			String Result = null;
			try {

				RestService rs = new RestService();


				Result = rs.call("term="+query[0]);

				Log.d("yo", Result);

			} catch (RuntimeException e) {
				Log.e("yo", "Failed", e);

			}
			return Result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog

			/**
			 * Updating parsed JSON data into ListView
			 * */

			ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
			if(result != null)
			{
                
				try{
					JSONObject jsonObj = new JSONObject(result);
					
					  if(jsonObj.getInt("statusCode") == 401)
				      {
				    	  CharSequence text = "Zappos API is not responding";

				    	  int duration = Toast.LENGTH_SHORT;

				    	  Toast toast = Toast.makeText(getApplicationContext(), text, duration);
				    	  toast.show();
				      }
					 

					JSONArray results = jsonObj.getJSONArray(TAG_RESULTS);
					ListView lv = (ListView) findViewById(R.id.listView);

					for (int i = 0; i < results.length(); i++) {
						JSONObject c = results.getJSONObject(i);

						String id = c.getString(TAG_ID);
						String styleid = c.getString(TAG_STYLE_ID);

						String name = c.getString(TAG_NAME);
						String currentPrice = c.getString(TAG_CURR_PRICE);
						String originalPrice = c.getString(TAG_ORIGINAL_PRICE);
						String percentDiscount = c.getString(TAG_DISCOUNT);



						// tmp hashmap for single contact
						HashMap<String, String> product = new HashMap<String, String>();

								// adding each child node to HashMap key => value
						        product.put(TAG_ID, id);
						        product.put(TAG_STYLE_ID, styleid);
								product.put(TAG_NAME, name);
								product.put(TAG_CURR_PRICE, currentPrice);
								product.put(TAG_ORIGINAL_PRICE, originalPrice);
								product.put(TAG_DISCOUNT, percentDiscount);


								// adding contact to contact list
								productList.add(product);
					}
					adapter = new SimpleAdapter(
							SearchResultsActivity.this, productList,
							R.layout.row_item, new String[] { TAG_ID, TAG_STYLE_ID, TAG_NAME, TAG_ORIGINAL_PRICE,
									TAG_CURR_PRICE, TAG_DISCOUNT }, new int[] { R.id.product_id, R.id.styleid, R.id.product_name,
									R.id.original_price, R.id.curr_price, R.id.per_discount	 });
					
					lv.setAdapter(adapter);
					
                    
					
					

				}
				catch(JSONException je)
				{
					je.printStackTrace();	
				}
			}
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		
		

		// Associate searchable configuration with the SearchView
		//	    SearchManager searchManager =
		//	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//	    SearchView searchView =
		//	            (SearchView) menu.findItem(R.id.searchView).getActionView();
		//	    searchView.setSearchableInfo(
		//	            searchManager.getSearchableInfo(getComponentName()));




		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.itemshownotify:
	      startActivity(new Intent(this, NotifiedProductsActivity.class));
	      break;
	    
	    }

	    return true;
	  }
}



