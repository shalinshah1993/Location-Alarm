package com.example.ifest;

import java.io.IOException;
import java.util.Calendar;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class ServiceAlarm extends Service implements LocationListener {
	Calendar cal;
	WifiManager wm;
	BluetoothAdapter bb;
	boolean onGPS, check = true;
	float[] result = new float[3];
	LocationManager lm;
	private static double lati = 23.18846, longi = 72.628491;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "MyAlarmService.onCreate()",
		// Toast.LENGTH_SHORT).show();
		// wm = (WifiManager) this.getSystemService(WIFI_SERVICE);
		// if(!wm.isWifiEnabled())
		// wm.setWifiEnabled(false);
		// else
		// wm.setWifiEnabled(true);
		// Log.d("Wifi", "State:"+wm.isWifiEnabled());
		// if(a.tp1.getCurrentHour() == cal.getTime().getHours() &&
		// a.tp1.getCurrentMinute() == cal.getTime().getMinutes()){
		// Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();
		// }
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		// Toast.makeText(this, "MyAlarmService.onStart()",
		// Toast.LENGTH_SHORT).show();
		lm = (LocationManager) getSystemService(Alarm.LOCATION_SERVICE);
		onGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(onGPS)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		Bundle b = intent.getExtras();
		int hour1 = b.getInt("hour1");
		int min1 = b.getInt("min1");
		int hour2 = b.getInt("hour2");
		int min2 = b.getInt("min2");
		String type = b.getString("type");
		boolean click = b.getBoolean("click");
		// Log.d("hour on start", " "+hour+" ");
		// Log.d("min on start", " "+min+ " ");
		cal = Calendar.getInstance();
		 Log.d("GPS", onGPS+"");
		// Log.d("current min", " " + cal.getTime().getMinutes());
		// Log.d("tp1 hour", " " + hour1);
		// Log.d("tp1 min", " " + min1);
		// Log.d("tp2 hour", " " + hour2);
		// Log.d("tp2 min", " " + min2);
		Log.d("on/off", "" + click);
		if (check) {
			if (type.equals("WIFI")) {
				if (hour1 == cal.getTime().getHours()
						&& min1 == cal.getTime().getMinutes()) {
					wm = (WifiManager) this.getSystemService(WIFI_SERVICE);
					wm.setWifiEnabled(true);
					Toast.makeText(this, "WIFI ON", Toast.LENGTH_LONG).show();
				} else if (hour2 == cal.getTime().getHours()
						&& min2 == cal.getTime().getMinutes()) {
					wm = (WifiManager) this.getSystemService(WIFI_SERVICE);
					wm.setWifiEnabled(false);
					Toast.makeText(this, "WIFI OFF", Toast.LENGTH_LONG).show();
				}
			} else if (type.equals("BLUETOOTH")) {
				bb = BluetoothAdapter.getDefaultAdapter();
				if (hour1 == cal.getTime().getHours()
						&& min1 == cal.getTime().getMinutes()) {
					if (bb.isEnabled() != true) {
						bb.enable();
					}
					Toast.makeText(this, "BLUETOOTH ON", Toast.LENGTH_LONG)
							.show();
				} else if (hour2 == cal.getTime().getHours()
						&& min2 == cal.getTime().getMinutes()) {
					Toast.makeText(this, "BLUETOOTH OFF", Toast.LENGTH_LONG)
							.show();
					bb.disable();
				}
			} else if (type.equals("MEDIA")) {
				if (hour1 == cal.getTime().getHours()
						&& min1 == cal.getTime().getMinutes()) {
					AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
					am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					Toast.makeText(this, "MEDIA OFF", Toast.LENGTH_LONG).show();
				} else if (hour2 == cal.getTime().getHours()
						&& min2 == cal.getTime().getMinutes()) {
					Toast.makeText(this, "MEDIA ON", Toast.LENGTH_LONG).show();
					AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
					am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
			} else if (type.equals("ALARM")) {
				if (hour1 == cal.getTime().getHours()
						&& min1 == cal.getTime().getMinutes()) {
					MediaPlayer mp = MediaPlayer.create(this, R.raw.official_tone);
					mp.start();
				}
			}
		}else{
			Toast.makeText(this, "Not in region", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double x = location.getLatitude();
		double y = location.getLongitude();
		Log.d("locations", x + " " + y);
		Toast.makeText(this, "lat" + x + " long" + y, Toast.LENGTH_LONG).show();
		Location.distanceBetween(location.getLatitude(),
				location.getLongitude(), lati, longi, result);
		float d = result[0];
		if (d <= 2000) {
			Toast.makeText(this, "dist not far", Toast.LENGTH_LONG).show();
			check = true;
		} else {
			Toast.makeText(this, "dist far", Toast.LENGTH_LONG).show();
			check = false;
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
