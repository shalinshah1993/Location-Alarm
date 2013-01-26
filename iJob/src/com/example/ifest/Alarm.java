package com.example.ifest;

import java.sql.SQLData;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Alarm extends Activity {

	private TextView tv;
	private Intent ix;
	private PendingIntent pi;
	private int i;
	public TimePicker tp1, tp2;
	int hour1, min1, min2, hour2;
	Button b1, b3;
	String name, type;
	protected boolean click;
	private static boolean on = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		b1 = (Button) findViewById(R.id.button1);
		b3 = (Button) findViewById(R.id.button3);
		tp1 = (TimePicker) this.findViewById(R.id.timePicker1);
		tp2 = (TimePicker) this.findViewById(R.id.timePicker2);
		tv = (TextView) findViewById(R.id.textView2_Alarm);
		
		// if(on)
		// b3.setVisibility(View.VISIBLE);
		// else
		// b3.setVisibility(View.INVISIBLE);
		Bundle bb = getIntent().getExtras();
		i = bb.getInt("id");
		name = bb.getString("name");
		type = bb.getString("type");
		//Log.d("id", i + "");
		openDB();
		if (type.equals("ALARM")) {
			tv.setVisibility(View.INVISIBLE);
			//tp2.setVisibility(View.INVISIBLE);
		}

		b1.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(Alarm.this, "Start", Toast.LENGTH_LONG).show();
				ix = new Intent(Alarm.this, ServiceAlarm.class);
				updateDB();
				ix.putExtra("hour1", tp1.getCurrentHour());
				ix.putExtra("min1", tp1.getCurrentMinute());
				ix.putExtra("hour2", tp2.getCurrentHour());
				ix.putExtra("min2", tp2.getCurrentMinute());
				ix.putExtra("type", type);
				Log.d("on/ff", ""+on);
				on = true;
				//b3.setVisibility(View.VISIBLE);
				//Log.d("hour here", " " + tp1.getCurrentHour());
				//Log.d("min here", " " + tp1.getCurrentMinute());
				//Log.d("hour here2", " " + tp2.getCurrentHour());
				//Log.d("min here2", " " + tp2.getCurrentMinute());

				pi = PendingIntent.getService(Alarm.this, i, ix, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.SECOND, 10);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), 60 * 1000, pi);
			}
		});

		b3.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				if (on) {
					Log.d("on/ff", ""+on);
					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					on = false;
					PendingIntent p =  PendingIntent.getService(getBaseContext(), i, ix, 0);
					//stopService(ix);
					alarmManager.cancel(p);
					Toast.makeText(Alarm.this, "stop", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	public void updateDB() {
		DBHandler handler = new DBHandler(this);
		SQLiteDatabase db = handler.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("_name", name);
		cv.put("_type", type);
		cv.put("_no", i);
		cv.put("_hour1", tp1.getCurrentHour());
		cv.put("_hour2", tp2.getCurrentHour());
		cv.put("_min1", tp1.getCurrentMinute());
		cv.put("_min2", tp2.getCurrentMinute());
		db.update("_table", cv, "_no =" + i, null);
		db.close();
	}

	public void openDB() {
		DBHandler handler = new DBHandler(getBaseContext());
		SQLiteDatabase db = handler.getReadableDatabase();
		Cursor c = db.query("_table", new String[] { "_hour1", "_min1",
				"_hour2", "_min2" }, "_no = " + i, null, null, null, null);
		c.moveToFirst();
		hour1 = c.getInt(0);
		hour2 = c.getInt(2);
		min1 = c.getInt(1);
		min2 = c.getInt(3);
		tp1.setCurrentHour(hour1);
		tp1.setCurrentMinute(min1);
		tp2.setCurrentHour(hour2);
		tp2.setCurrentMinute(min2);
		c.close();
		db.close();
	}
}