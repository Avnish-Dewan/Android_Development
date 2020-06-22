package com.example.studentrecords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME="StudentRecords.db";
	public static final String TABLE_NAME="RECORDS";
	public static final String COL_1="ID";
	public static final String COL_2="NAME";
	public static final String COL_3="EMAIL";
	public static final String COL_4="COUNT";

	public DatabaseHelper(@Nullable Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+TABLE_NAME+
				" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,COUNT INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME );
		onCreate(db);
	}

	public boolean insertData(String name,String email,String courseCounts){

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_2,name);
		contentValues.put(COL_3,email);
		contentValues.put(COL_4,courseCounts);

		long result = db.insert(TABLE_NAME,null,contentValues);

		return (result != -1);
	}

	public boolean updateData(String id,String name,String email,String courseCounts){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_1,id);
		contentValues.put(COL_2,name);
		contentValues.put(COL_3,email);
		contentValues.put(COL_4,courseCounts);

		long result = db.update(TABLE_NAME,contentValues,"ID=?",new String[]{id});

		return (result != -1);
	}

	public Cursor getData(String id){
		SQLiteDatabase db = this.getWritableDatabase();

		return db.rawQuery("select * from "+TABLE_NAME+" where id = '"+id+"'",null);

	}

	public Integer deleteData(String id){
		SQLiteDatabase db = this.getWritableDatabase();

		return db.delete(TABLE_NAME,"ID=?",new String[]{id});
	}

	public Cursor getAllData(){
		SQLiteDatabase db = this.getWritableDatabase();

		return db.rawQuery("select * from "+TABLE_NAME,null);
	}

	public Integer deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='"+TABLE_NAME+"'");

		return db.delete(TABLE_NAME,null,null);
	}

}
