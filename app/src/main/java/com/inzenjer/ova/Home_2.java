package com.inzenjer.ova;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home_2 extends Activity{
	
	Button bt_grg,bt_mv,bt_gest,bt_rsa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_2);
		
		bt_grg=(Button)findViewById(R.id.bt_garage);
	//	bt_mv=(Button)findViewById(R.id.bt_status);
		bt_gest=(Button)findViewById(R.id.bt_getestimate);
		bt_rsa=(Button)findViewById(R.id.bt_roadsideasst);
		
		/*bt_mv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),Veh_status.class);
				startActivity(i);
			}
		});*/
		bt_rsa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(getApplicationContext(),Home.class);
				startActivity(i);
				
			}
		});
		
		bt_grg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent i=new Intent(getApplicationContext(),Garage.class);
					startActivity(i);
					
				}
			});
		bt_gest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i=new Intent(getApplicationContext(),Get_estimate.class);
				startActivity(i);
				
			}
		});

	}

}
