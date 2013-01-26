package com.example.ifest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity{

	
	Thread t ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawableResource(R.drawable.splash);
		t = new Thread(){
			public void run() {
				try {
					Thread.sleep(2000);
					Intent iy = new Intent("com.example.ifest.PROFILEVIEW");
					startActivity(iy);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		t.start();
		
	}
		
}
