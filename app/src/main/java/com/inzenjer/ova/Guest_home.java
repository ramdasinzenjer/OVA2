package com.inzenjer.ova;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Guest_home extends Activity{
	
	
	ImageView im_rsa,im_srv,im_emy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_home);
		
		im_rsa=(ImageView)findViewById(R.id.img_rsa);
		im_srv=(ImageView)findViewById(R.id.img_srv);		
		im_emy=(ImageView)findViewById(R.id.img_emc);
		
		im_rsa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Home.class);
				startActivity(i);
				
			}
		});
		im_srv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences share=getSharedPreferences("keygst", MODE_WORLD_READABLE);
				SharedPreferences.Editor ed=share.edit();
				ed.putString("srtgst","guest");
				ed.commit();
				Intent i=new Intent(getApplicationContext(),Nearby_list.class);
				startActivity(i);
			}
		});
		im_emy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),E_contact.class);
				startActivity(i);
			}
		});
		
	}
}
