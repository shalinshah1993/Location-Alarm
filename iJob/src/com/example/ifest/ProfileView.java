package com.example.ifest;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class ProfileView extends ListActivity{

	int hour,min;
	TimePicker tp ;
	String e,e1;
	static int p = 0;
	Spinner spn ,sp1;
	Button b1,b2,b3,b4,b5,add,del;
	EditText et,et1;
	String str1;
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter,adapter1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        //if(p == 0)
        //list.add("Create");
        openDB();
        p++;
        add = (Button) findViewById(R.id.but1_Main);
        del = (Button) findViewById(R.id.but2_Main);
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        setListAdapter(adapter);      
        add.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
					final Dialog build = new Dialog(ProfileView.this);
		    		build.setTitle("String Name and Details");
		    		build.setContentView(R.layout.activity_dialog);
		    		build.show();
		    		spn = (Spinner)build.findViewById(R.id.spinner1_Dialog);
		    		et = (EditText) build.findViewById(R.id.editText1_Dialog);
		    		b2 = (Button) build.findViewById(R.id.button1_Dialog);
		    		//tp = (TimePicker)build.findViewById(R.id.timePicker1_Dialog);
		    		b3 = (Button)build.findViewById(R.id.button2_Dialog);
		    		//tp.setIs24HourView(true);
		            //tp.setCurrentMinute(00);
		            
		    		b2.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							build.dismiss();
							//hour = tp.getCurrentHour();
							//min = tp.getCurrentMinute();
							list.add(et.getText().toString());
							addDB(et.getText().toString(),spn.getLastVisiblePosition());
							adapter.notifyDataSetChanged();
							setListAdapter(adapter);
						}
					});
		    		
		    		b3.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							build.dismiss();
						}
					});
				}
			});
        
        del.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				final Dialog d = new Dialog(ProfileView.this);
				d.setTitle("Delete Event");
				d.setContentView(R.layout.activity_dialog2);
				b4 = (Button) d.findViewById(R.id.button1_Dialog2);
				sp1 = (Spinner) d.findViewById(R.id.spinner1_Dialog2);
				b5 = (Button) d.findViewById(R.id.button2_Dialog2);
				adapter1 = new ArrayAdapter<String>(ProfileView.this,android.R.layout.simple_spinner_dropdown_item,list);
				sp1.setAdapter(adapter1);
				d.show();
				b4.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						d.dismiss();
						////Logd.d("Index", " " + sp1.getLastVisiblePosition());
						////Logd.d("Index", " " + list.get(sp1.getLastVisiblePosition()));
						DBHandler handler = new DBHandler(ProfileView.this);
						SQLiteDatabase db = handler.getWritableDatabase();
						Cursor c = db.rawQuery("SELECT * FROM "+ "_table",null);
			    		c.moveToFirst();
			    		
						////Logd.d("cur", c.getString(1));
						while(!c.getString(1).equals(list.get(sp1.getLastVisiblePosition()))){
							c.moveToNext();
							////Logd.d("cur", c.getString(1));
							////Logd.d("yes/no",""+c.getString(1).equals(list.get(sp1.getLastVisiblePosition())));
						}
						db.delete("_table" , "_no =" + c.getInt(0), null);
						c.close();
						db.close();
						list.remove(list.get(sp1.getLastVisiblePosition()));
						adapter.notifyDataSetChanged();
						setListAdapter(adapter);
						//openDB();
					}
				});
				b5.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						d.dismiss();
					}
				});
			}
		});
        		
    }

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    		DBHandler handler = new DBHandler(this);
			SQLiteDatabase db = handler.getReadableDatabase();
			Cursor c = db.query("_table", new String[]{"_no","_name","_type"}, null, null, null, null, null);
			c.moveToFirst();
			while(position != 0){
				c.moveToNext();
				position--;
			}
			//Logd.d("ID", c.getString(0));
			Bundle bb = new Bundle();
			bb.putInt("id",c.getInt(0));
			bb.putString("name", c.getString(1));
			bb.putString("type", c.getString(2));
			//bb.putInt("hour", c.getInt(3));
			//bb.putInt("minute", c.getInt(4));
			////Logd.d("column no", c.getString(1));
			c.close();
			db.close();
			Intent ix = new Intent("com.example.ifest.ALARM");
			ix.putExtras(bb);
			startActivity(ix);			
		
    }
    
    protected void addDB(String name,int id) {
    	DBHandler handle = new DBHandler(this);
		SQLiteDatabase db = handle.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("_name",name);
		if(id == 0)
			cv.put("_type","WIFI");
		else if(id == 1)
			cv.put("_type","BLUETOOTH");
		else if(id == 2)
			cv.put("_type","MEDIA");
		else if(id == 3)
			cv.put("_type", "ALARM");
		//cv.put("_hour", h);
		//cv.put("_min", m);
		db.insert("_table", null , cv);
		db.close();
    }

    private void openDB() {
    	DBHandler handle = new DBHandler(this);
		SQLiteDatabase db = handle.getReadableDatabase();
    	if(p != 0){	
    		Cursor c = db.rawQuery("SELECT * FROM "+ "_table",null);
    		c.moveToFirst();
    		list.add(c.getString(1));
    		//Logd.d("cursor", c.getString(1));
    		while(c.moveToNext()){
    			list.add(c.getString(1));
    			//Logd.d("cursor", c.getString(1));
    		}
    		c.close();
    		//Logd.d("open/close",""+db.isOpen());
    		db.close();
    		//Logd.d("open/close",""+db.isOpen());
    	}/*else{
    		Dialog d = new Dialog(this);
    		d.setTitle("Intro to the app!");
    		d.setContentView(R.layout.activity_dialog3);
    		d.show();
    	}*/
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.item1:
			final Dialog build = new Dialog(ProfileView.this);
    		build.setTitle("String Name and Details");
    		build.setContentView(R.layout.activity_dialog);
    		build.show();
    		spn = (Spinner)build.findViewById(R.id.spinner1_Dialog);
    		et = (EditText) build.findViewById(R.id.editText1_Dialog);
    		b2 = (Button) build.findViewById(R.id.button1_Dialog);
    		//tp = (TimePicker)build.findViewById(R.id.timePicker1_Dialog);
    		b3 = (Button)build.findViewById(R.id.button2_Dialog);
    		//tp.setIs24HourView(true);
            //tp.setCurrentMinute(00);
            
    		b2.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					build.dismiss();
					//hour = tp.getCurrentHour();
					//min = tp.getCurrentMinute();
					list.add(et.getText().toString());
					addDB(et.getText().toString(),spn.getLastVisiblePosition());
					adapter.notifyDataSetChanged();
					setListAdapter(adapter);
				}
			});
    		
    		b3.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					build.dismiss();
				}
			});
    		break;
			
		case R.id.item2:
			final Dialog d = new Dialog(this);
			d.setTitle("Delete Event");
			d.setContentView(R.layout.activity_dialog2);
			b4 = (Button) d.findViewById(R.id.button1_Dialog2);
			sp1 = (Spinner) d.findViewById(R.id.spinner1_Dialog2);
			b5 = (Button) d.findViewById(R.id.button2_Dialog2);
			adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
			sp1.setAdapter(adapter1);
			d.show();
			b4.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					d.dismiss();
					//list.remove(list.indexOf(et1.getText().toString()));
					//Logd.d("Index" , list.get(sp1.getLastVisiblePosition()));
					DBHandler handler = new DBHandler(ProfileView.this);
					SQLiteDatabase db = handler.getWritableDatabase();
					Cursor c = db.rawQuery("SELECT * FROM "+ "_table",null);
		    		c.moveToFirst();
		    		
					////Logd.d("cur", c.getString(1));
					while(!c.getString(1).equals(list.get(sp1.getLastVisiblePosition()))){
						c.moveToNext();
						//Logd.d("cur", c.getString(1));
						//Logd.d("yes/no",""+c.getString(1).equals(list.get(sp1.getLastVisiblePosition())));
					}
					db.delete("_table" , "_no =" + c.getInt(0), null);
					c.close();
					list.remove(list.get(sp1.getLastVisiblePosition()));
					adapter.notifyDataSetChanged();
					setListAdapter(adapter);
				}
			});
			b5.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					d.dismiss();
				}
			});
			break;
			
		case R.id.item3:
			Intent i = new Intent("com.example.ifest.ABOUTUS");
			startActivity(i);
			break;
			
		case R.id.item4:
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Exit");
			b.setMessage("Are you sure you want to exit?");
			b.setCancelable(true);
			b.setPositiveButton("Yes", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();					
				}
			});
			b.setNegativeButton("No", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog ad = b.create();
			ad.show();
			break;		
		}
		return true;
	}

}
