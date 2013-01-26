package com.example.ifest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutUs extends Activity{

	Button b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		b = (Button) findViewById(R.id.button1_AboutUs);
		b.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				finish();
			}
			
		});
	}
}
