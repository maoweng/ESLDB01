package com.javahelps.esldb01;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static String DB_PATH = "/data/data/com.javahelps.esldb01/databases/";
	public static String DB_NAME = "multilanguage04.db";
	public static final int DB_VERSION = 1;
	
	public static final String TB_USER = "wordsList";
	
	private SQLiteDatabase myDB;
	private Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);	
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void close(){
		if(myDB!=null){
			myDB.close();
		}
		super.close();
	}
		
	public List<String> getAllUsers(){
		List<String> listUsers = new ArrayList<String>();
	//	SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c;
		
		try {
			c = db.rawQuery("SELECT * FROM " + TB_USER , null);
			if(c == null) return null;
			
			String name;
			c.moveToFirst();
			do {			
				name = c.getString(1);
				listUsers.add(name);
			} while (c.moveToNext()); 
			c.close();
		} catch (Exception e) {
			Log.e("tle99", e.getMessage());
		}
		
		
		db.close();		
		
		return listUsers;
	}
	
		
	/***
	 * Open database
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	/***
	 * Copy database from source code assets to device
	 * @throws IOException
	 */
	public void copyDataBase() throws IOException {
		try {
			InputStream myInput = context.getAssets().open(DB_NAME);
			String outputFileName = DB_PATH + DB_NAME;
			OutputStream myOutput = new FileOutputStream(outputFileName);

			byte[] buffer = new byte[1024];
			int length;

			while((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			Log.e("tle99 - copyDatabase", e.getMessage());
		}
		
	}
	
	/***
	 * Check if the database doesn't exist on device, create new one
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();		
		
		if (dbExist) {

		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e("tle99 - create", e.getMessage());
			}
		}
	}
	
	// ---------------------------------------------
	// PRIVATE METHODS
	// ---------------------------------------------
	
	/***
	 * Check if the database is exist on device or not
	 * @return
	 */
	private boolean checkDataBase() {
		SQLiteDatabase tempDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			tempDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			Log.e("tle99 - check", e.getMessage());
		}
		if (tempDB != null)
			tempDB.close();
		return tempDB != null ? true : false;
	}

// added methods start



	public String databaseInfo(String type) {
		String info = "";

		switch(type) {
			case "all":
	//			readDBinfo();
				break;
			case "1":
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				break;
			default:
				break;

		}
		return info;
	}

	public String DBcolumnData(int colNum) {
		String columnData = "";

		return columnData;
	}

	public int DBcolumnValue(int colNum) {
		int columnVal = 0;
		return columnVal;
	}

// added methods end

}
