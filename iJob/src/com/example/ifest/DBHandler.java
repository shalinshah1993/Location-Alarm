package com.example.ifest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

	private static final String DB_NAME = "event_db";
	private static final int DB_VERSION = 5;
	private static final String TABLE_NAME = "_table";
	private static final String EVENT_NAME = "_name";
	private static final String EVENT_ID = "_no";
	private static final String EVENT_TYPE = "_type";
	private static final String EVENT_HOUR1 = "_hour1";
	private static final String EVENT_MINUTE1 = "_min1";
	private static final String EVENT_HOUR2 = "_hour2";
	private static final String EVENT_MINUTE2 = "_min2";

	public DBHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + EVENT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME
				+ " TEXT NOT NULL, " + EVENT_TYPE + " TEXT NOT NULL, "
				+ EVENT_HOUR1 + "  INTEGER, " + EVENT_MINUTE1 + " INTEGER, "
				+ EVENT_HOUR2 + " INTEGER, " + EVENT_MINUTE2 + " INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
