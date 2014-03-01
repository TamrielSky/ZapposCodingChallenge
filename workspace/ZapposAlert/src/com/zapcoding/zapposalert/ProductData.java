package com.zapcoding.zapposalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductData {
  private static final String TAG = ProductData.class.getSimpleName();

  static final int VERSION = 1;
  static final String DATABASE = "products.db";
  static final String TABLE = "savedProducts";

  public static final String P_ID = "_id";
  public static final String P_STYLE_ID = "style_id";
  
  public static final String P_NAME = "product_name";
  public static final String P_ORI_PRICE =	 "product_originalPrice";
  public static final String P_CURR_PRICE = "product_currentPrice";
  public static final String P_DISCOUNT = "product_discount";
  public static final String P_DISCOUNT_COND = "product_discount_cond";


  final DbHelper dbHelper;
  // DbHelper implementations
  class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
      super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.i(TAG, "Creating database: " + DATABASE);
      String sql = "create table " + TABLE + " (" + P_ID + " int primary key, " + P_STYLE_ID + " int, "+ P_NAME + " text, " +P_ORI_PRICE  + " text, " + P_CURR_PRICE + " text,	" + P_DISCOUNT + " text ," + P_DISCOUNT_COND + " int)";
      db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("drop table " + TABLE);
      this.onCreate(db);
    }
  }

  

  public ProductData(Context context) {
    this.dbHelper = new DbHelper(context);
    Log.i(TAG, "Initialized data");
  }

  public void close() {
    this.dbHelper.close();
  }

  public void insert(ContentValues values) {
	  
    
    
    SQLiteDatabase db = this.dbHelper.getWritableDatabase();
    try {
      db.insertWithOnConflict(TABLE, null, values,
          SQLiteDatabase.CONFLICT_IGNORE);
    } finally {
      db.close();
    }
  }

  /**
   * 
   * @return Cursor where the columns are going to be id, created_at, user, txt
   */
  public Cursor getProducts() {
    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
    return db.query(TABLE, null, null, null, null, null,null);
  }

  public int updateProducts(ContentValues content, int product_id, int style_id) {
	    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
	    String whereClause = P_STYLE_ID+"="+style_id;
	    return db.update(TABLE, content, whereClause, null);
	  }
  
  public Cursor getNotifiedProducts() {
	    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
	    String whereClause = P_DISCOUNT_COND+"=1";
	    return db.query(TABLE, null, null, null, null, null,null);
	  }
  
  /**
   * Deletes ALL the data
   */
  public void delete() {
    // Open Database
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    // Delete the data
    db.delete(TABLE, null, null);

    // Close Database
    db.close();
  }



}
