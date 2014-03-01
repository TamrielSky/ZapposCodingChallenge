package com.example.zapposalert;



import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NotifiedProductsActivity extends Activity {

	ListAdapter adapter;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notified_products);
		
		
		
		Cursor c = ((ZapposApplication)this.getApplication()).getdatabase().getNotifiedProducts();
					
				
		adapter = new SimpleCursorAdapter(
						NotifiedProductsActivity.this,
						R.layout.row_item, c, new String[] { ProductData.P_ID, ProductData.P_NAME, ProductData.P_ORI_PRICE, ProductData.P_CURR_PRICE, ProductData.P_DISCOUNT
								 }, new int[] {R.id.product_id,  R.id.product_name,
								R.id.original_price, R.id.curr_price, R.id.per_discount	 } ,1);

		ListView lv = (ListView) findViewById(R.id.listView1);

		
	lv.setAdapter(adapter);
	}
	
	
	
	

}
